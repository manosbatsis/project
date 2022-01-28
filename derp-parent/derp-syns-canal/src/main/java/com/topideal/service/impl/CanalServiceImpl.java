package com.topideal.service.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.topideal.api.wx.WXUtils;
import com.topideal.canalclient.ReportCanalClient;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.config.ApolloConfig;
import com.topideal.dao.JdbcDao;
import com.topideal.service.CanalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CanalServiceImpl implements CanalService {

    /* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(ReportCanalClient.class);

    @Autowired
    JdbcDao jdbcDao;

    /**
     * 处理canal server解析binlog获得的实体类信息
     * syns* 事务控制
     */
    public void synsparseBinlogEntry(List<CanalEntry.Entry> entrys) throws Exception{
         //循环处理binlog实体
        for(CanalEntry.Entry entry : entrys) {

            //开启/关闭事务的实体类型，跳过
            if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) continue;

            //RowChange对象，包含了一行数据变化的所有特征,具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            CanalEntry.RowChange rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

            //获取事务类型
            CanalEntry.EventType eventType = rowChage.getEventType();
            String tableName = entry.getHeader().getTableName();
            String DBName = entry.getHeader().getSchemaName();
            logger.info("DBName="+DBName+"，tableName="+tableName);
            //检查是否配置同步表，否则跳过
            boolean flag = checkTabelName(DBName,tableName);
            if(flag ==false) continue;

            /**表结构变更
             * 建表--CREATE 修改表名--RENAME 删表(不处理)-ERASE
             * 添加字段、修改字段、删除字段-ALTER
             * 创建索引--CINDEX 修改索引--ALTER  删除索引--DINDEX
             * 数据变更：
             * trucate删除数据-TRUNCATE
             * 添加数据-INSERT、修改数据-UPDATE、删除数据-DELETE
             * */
            if(eventType == CanalEntry.EventType.CREATE||eventType == CanalEntry.EventType.RENAME
                    ||eventType == CanalEntry.EventType.ALTER||eventType == CanalEntry.EventType.CINDEX
                    ||eventType == CanalEntry.EventType.DINDEX||eventType == CanalEntry.EventType.TRUNCATE
                    ||eventType == CanalEntry.EventType.ERASE) {
                //执行变更SQL
                String sql = rowChage.getSql();
                //部分工具操作sql前会带库名，去除库名
                sql = sql.replace("`"+DBName+"`.","");
                sql = sql.replace(DBName+".","");
                //如果是删表则改成修改表名防止误操作
                if(eventType == CanalEntry.EventType.ERASE){
                    sql = "alter table "+tableName+" rename to "+tableName+"_"+TimeUtils.formatFullTimeNum()+";";
                }
                execute(0,sql);
            }else{
                //数据处理：获取RowChange对象里的每一行数据
                for(CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                       //删除
                    if (eventType == CanalEntry.EventType.DELETE) {
                        delete(tableName,rowData.getBeforeColumnsList());
                        //新增
                    } else if (eventType == CanalEntry.EventType.INSERT) {
                        insert(tableName,rowData.getAfterColumnsList());
                        //更新
                    } else if(eventType == CanalEntry.EventType.UPDATE) {
                        update(tableName,rowData.getBeforeColumnsList(),rowData.getAfterColumnsList());
                    }else{
                        //事件类型未匹配到
                        printColumn(eventType,rowData.getAfterColumnsList());
                    }
                }
            }

        }

    }

    private boolean checkTabelName(String dbName,String tableName){
        List<String> tableList = new ArrayList<>();
        if(dbName.equals(ApolloConfig.getPropertyByName(ApolloConfig.synInventoryDatabase))){
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synInventoryTable).split(",")));
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synInventoryBigTable).split(",")));
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synInventoryIndependentTable).split(",")));
        }else if(dbName.equals(ApolloConfig.getPropertyByName(ApolloConfig.synOrderDatabase))){
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synOrderTable).split(",")));
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synOrderBigTable).split(",")));
        }else if(dbName.equals(ApolloConfig.getPropertyByName(ApolloConfig.synStorageDatabase))){
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synStorageTable).split(",")));
        }else if(dbName.equals(ApolloConfig.getPropertyByName(ApolloConfig.synSystemDatabase))){
            tableList.addAll(Arrays.asList(ApolloConfig.getPropertyByName(ApolloConfig.synSystemTable).split(",")));
        }

        boolean flag = false;
        if(tableList.contains(tableName)){
            flag = true;
        }
        return flag;
    }
    /**表结构变更
     * @param retryNum 重试次数
     * @param sql 要执行的sql
     * */
    private void execute(int retryNum,String sql) throws Exception{
        try {
            jdbcDao.execute(sql);
            logger.info("execute完成"+sql);
        }catch (Exception e) {
            //2.执行未知异常-------->发送预警，挂停程序
            throw new RuntimeException("execute执行失败："+sql,e);
        }
    }
    /**
     * 插入数据
     * @param tableName 表名
     * @param columns 数据list
     * */
    private  void insert(String tableName, List<CanalEntry.Column> columns) throws Exception{

        String columnNames = "";//存储字段名如: name,age,amount...
        String values = "";//存储展位符如：?,?,?...
        List<Object> valueList = new ArrayList<Object>();//存储占位符的值
        Object id = null;
        for(CanalEntry.Column column : columns) {
            if(column.getName().equals("id")) id = column.getValue();
            columnNames +="`"+column.getName()+"`,";
            values += "?,";
            if(column.getIsNull()){
                valueList.add(null);
            }else {
                valueList.add(column.getValue());
            }
        }
        //去掉最后一个，
        columnNames = columnNames.substring(0,columnNames.length()-1);
        values = values.substring(0,values.length()-1);

        String sql = "insert into "+tableName+"("+columnNames+") value("+values+");";
        try {
            int n = jdbcDao.save(sql,valueList);
            logger.info("新增成功:"+tableName+",id="+id);
            logger.info(sql);
            logger.info(valueList.toString());
        }catch (Exception e){
            /**插入失败: 1.判断id是否已存在，存在则提醒检查。*/
             List<Map<String,Object>> list = jdbcDao.queryById(tableName,id);
             if(list!=null&&list.size()>0){
                 String content = "【canal管道】"+ApolloUtil.jxRemark+":提醒！新增id已存在，请排查是否需要修复数据"+tableName+",id="+id+","+TimeUtils.formatFullTime();
                 //WXUtils.sendPush(content);//蓝精灵推送
                 logger.info(content);
             }else {
                 /**2.不存在，抛异常回滚事务，发送预警*/
                 //sql = sql.replace("?", id.toString());
                 throw new RuntimeException("新增失败："+tableName+",id="+id+e.getMessage());
             }
        }

    }
    /**更新数据
     * @param tableName 表名
     * @param beforeColumns 修改前
     * @param afterColumns 修改后
     * */
    private void update(String tableName,List<CanalEntry.Column> beforeColumns,List<CanalEntry.Column> afterColumns) throws Exception{

        //获取修改前id
        Object beforeId = null;
        for(CanalEntry.Column column : beforeColumns) {
            if(column.getName().equals("id")) {
                beforeId = column.getValue();
                break;
            }
        }

        String columnNames = "";//存储字段名如: name=?,age=?,amount=?...
        List<Object> valueList = new ArrayList<Object>();//存储占位符的值
        for(CanalEntry.Column column : afterColumns) {
            //字段有修改则拼装
            if(column.getUpdated()==false) continue;
            columnNames +="`"+column.getName()+"`=?,";
            if(column.getIsNull()){
                valueList.add(null);
            }else{
                valueList.add(column.getValue());
            }
        }

        //去掉最后一个，根据修改前id更新
        columnNames = columnNames.substring(0,columnNames.length()-1);
        String sql = "update "+tableName+" set "+columnNames+" where id=?;";
        valueList.add(beforeId);
        try {
            int n = jdbcDao.update(sql,valueList);
            logger.info("更新成功:"+tableName+",beforeId="+beforeId);
        }catch (Exception e){
            //2.执行未知异常-------->发送预警，挂停程序
            throw new RuntimeException("更新失败"+tableName+",beforeId="+beforeId+","+e.getMessage());
            //logger.info("更新失败"+tableName+",beforeId="+beforeId+","+e.getMessage());
        }

    }

    /**更新数据
     * @param tableName 表名
     * @param columns 数据list
     * */
    private void delete(String tableName,List<CanalEntry.Column> columns) throws Exception{
        Object id = null;
        for(CanalEntry.Column column : columns) {
            //字段有修改则拼装
            if(column.getName().equals("id")) {
                id = column.getValue();
                break;
            }
        }
        String sql = "delete from "+tableName+" where id=?;";
        try {
            int n = jdbcDao.delete(sql,id);
            logger.info("删除成功:"+tableName+",id="+id);
        }catch (Exception e){
            //2.执行未知异常-------->发送预警，挂停程序
            throw new RuntimeException("删除失败："+tableName+",id="+id+","+e.getMessage());
        }
    }

    private void printColumn(CanalEntry.EventType eventType,List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            logger.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
        String content = "【canal管道】"+ApolloUtil.jxRemark+":提醒！"+eventType+"事件类型未匹配到"+TimeUtils.formatFullTime();
        WXUtils.sendPush(content);//蓝精灵推送
    }
}
