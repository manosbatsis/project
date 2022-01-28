package com.topideal.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.APILog;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.tools.CollectionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.mq.tools.ConncetionUtil;
import com.topideal.service.SysDataService;

import net.sf.json.JSONObject;

/**
 * 同步数据
 * 
 * @author zhanghx
 */
@Service
public class SysDataServiceImpl implements SysDataService {

	private static final Logger logger = LoggerFactory.getLogger(SysDataServiceImpl.class);
	@Autowired
	private JSONMongoDao jsonMongoDao;
	@Autowired
	private RMQLogProducer rocketMQProducer;

	/**
	 * 修改后同步方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201501050, model = DERP_LOG_POINT.POINT_13201501050_Label)
	public synchronized void synsData(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		//指定同步时间
		String selectDate = (String) jsonMap.get("selectTime");
		String inventoryDate = (String) jsonMap.get("selectTime");
		/**库、表两个字段要么都指定，要么都不指定需同时使用*/
		String database = (String)jsonMap.get("database");
		String dbTables = (String)jsonMap.get("table");

		// 如果指定同步时间为空则取前一天的日期，批次库存快照、实时库存时间同步时间取当前日期
		if (StringUtils.isEmpty(selectDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date time = cal.getTime();
			selectDate = sdf.format(time);
			
			cal = Calendar.getInstance();
			inventoryDate = sdf.format(cal.getTime()) ;
		}

		/**多线程计数器*/
		int latchNum = 12;//默认：5库+7个大表=12
		if(!StringUtils.isEmpty(database)){
			//指定了某个库则1个线程
			latchNum = 1;
		}
		CountDownLatch latch = new CountDownLatch(latchNum);//创建一个计数器
		/**
		 * 多线程同步每个库一个线程
		 **/
		ExecutorService pool = Executors.newCachedThreadPool();
		/**主服务同步*/
		if(StringUtils.isEmpty(database)||database.equals("derp-system")){
			/**若指定了表则同步指定表，否则同步配置的所有表*/
			String systemTables = StringUtils.isEmpty(dbTables)?ApolloUtilDB.synSystemTable:dbTables;
			createThread(latch,pool,json,keys,ApolloUtilDB.systemUrl,ApolloUtilDB.systemUserName,
					ApolloUtilDB.systemPassword, ApolloUtilDB.systemDatabase, systemTables, selectDate);
		}
		/**业务库同步*/
		if(StringUtils.isEmpty(database)||database.equals("derp-order")){
			/**若指定了表则同步指定表，否则同步配置的所有表*/
			String orderTables = StringUtils.isEmpty(dbTables)?ApolloUtilDB.synOrderTable:dbTables;
			createThread(latch,pool,json,keys,ApolloUtilDB.orderUrl, ApolloUtilDB.orderUserName,
					ApolloUtilDB.orderPassword, ApolloUtilDB.orderDatabase, orderTables, selectDate);
		}
		/**业务库大表一个表一个线程去同步，6个大表*/
		if((StringUtils.isEmpty(database)||database.equals("derp-order"))&&StringUtils.isEmpty(dbTables)){
			String orderBigTables = ApolloUtilDB.synOrderBigTable;
			String[] orderBigTablesArr = orderBigTables.split(",");
			for(String bigTable:orderBigTablesArr){
				createThread(latch,pool,json,keys,ApolloUtilDB.orderUrl, ApolloUtilDB.orderUserName,
						ApolloUtilDB.orderPassword, ApolloUtilDB.orderDatabase, bigTable, selectDate);
			}
		}

		/**仓储库同步*/
		if(StringUtils.isEmpty(database)||database.equals("derp-storage")){
			/**若指定了表则同步指定表，否则同步配置的所有表*/
			String storageTables = StringUtils.isEmpty(dbTables)?ApolloUtilDB.synStorageTable:dbTables;
			createThread(latch,pool,json,keys,ApolloUtilDB.storageUrl, ApolloUtilDB.storageUserName,
					ApolloUtilDB.storagePassword, ApolloUtilDB.storageDatabase, storageTables, selectDate);
		}
		/**库存库同步*/
		if(StringUtils.isEmpty(database)||database.equals("derp-inventory")){
			/**若指定了表则同步指定表，否则同步配置的所有表*/
			String inventoryTables = StringUtils.isEmpty(dbTables)?ApolloUtilDB.synInventoryTable:dbTables;
			createThread(latch,pool,json,keys,ApolloUtilDB.inventoryUrl, ApolloUtilDB.inventoryUserName,
					ApolloUtilDB.inventoryPassword, ApolloUtilDB.inventoryDatabase, inventoryTables, selectDate);
		}
		/**库存库大表一个表一个线程去同步，1个大表*/
		if((StringUtils.isEmpty(database)||database.equals("derp-inventory"))&&StringUtils.isEmpty(dbTables)){
			String inventoryBigTables = ApolloUtilDB.synInventoryBigTable;
			String[] inventoryBigTablesArr = inventoryBigTables.split(",");
			for(String bigTable:inventoryBigTablesArr){
				createThread(latch,pool,json,keys,ApolloUtilDB.inventoryUrl, ApolloUtilDB.inventoryUserName,
						ApolloUtilDB.inventoryPassword, ApolloUtilDB.inventoryDatabase, bigTable, selectDate);
			}
		}
		/**批次库存、批次库存快照、实时库存单独同步*/
		if(StringUtils.isEmpty(database)){
			createThread(latch,pool,json,keys,ApolloUtilDB.inventoryUrl, ApolloUtilDB.inventoryUserName,
					ApolloUtilDB.inventoryPassword, ApolloUtilDB.inventoryDatabase, ApolloUtilDB.synInventoryIndependentTable, inventoryDate);
		}

		/**
		 * 阻塞线程在这里，直到所有线程执行完计数器的值为0，再继续往下走,(因删除数据mq需等所有数据同步执行完才能执行）
		 * */
		latch.await();
	}
    /**创建线程
	 * */
	private void createThread(CountDownLatch latch,ExecutorService pool,String json,String keys,
							  String dbUrl, String dbUserName, String dbPassword,String dbName, String dbTables, String selectDate) throws Exception{

		pool.execute(new Runnable() {
			public void run() {
				try {
					syns(dbUrl, dbUserName, dbPassword, dbName, dbTables, selectDate);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("dbName:"+e.getMessage(), e);
					//发送失败日志
					sendLog(keys,0,Long.valueOf(DERP_LOG_POINT.POINT_13201501050),"同步数据-"+dbName,json,e.getMessage());
				}finally {
					latch.countDown();//执行完计数器减1
				}
			}
		});
	}

	private void syns(String dbUrl, String dbUserName, String dbPassword, String dbName, String dbTables,
			String selectDate) throws Exception {
		logger.info("----------------同步【"+dbName+"】开始---------------");
		String report_url = ApolloUtilDB.reportUrl;
		String report_username = ApolloUtilDB.reportUserName;
		String report_password = ApolloUtilDB.reportPassword;
		String report_database = ApolloUtilDB.reportDatabase;

		String[] tableNames = dbTables.split(",");

		for (int i = 0; i < tableNames.length; i++) {
			String tableName = tableNames[i];

			// 获取报表数据库表字段名及数据类型
			Connection tbFieldConn = ConncetionUtil.getJDBCConnection(dbUrl, dbUserName, dbPassword, dbName);

			if (tbFieldConn == null) {
				throw new Exception("jdbc连接失败");
			}

			String tbFieldSql = "select * from " + tableName + " limit 1 ";
			Statement tbFieldsm = tbFieldConn.createStatement();
			ResultSet tbFieldRs = tbFieldsm.executeQuery(tbFieldSql);
			// 获取对应同步表字段以及类型集合
			Map<String, Object> tbFieldMap = getTbFieldAndType(tbFieldRs);

			tbFieldRs.close();
			tbFieldsm.close();
			
			// 查询结果集总数 该sql在大数据表命中了索引index_create_date_modify_date
			StringBuffer rsCountSqlSb = new StringBuffer();
			rsCountSqlSb.append(" select count(1) as rec from ");
			rsCountSqlSb.append(tableName);
			rsCountSqlSb.append(" where ( create_date >= '");
			rsCountSqlSb.append(selectDate);
			rsCountSqlSb.append(" 00:00:00' and create_date <= '");
			rsCountSqlSb.append(selectDate);
			rsCountSqlSb.append(" 23:59:59' )");
			rsCountSqlSb.append(" OR ( modify_date >= '");
			rsCountSqlSb.append(selectDate);
			rsCountSqlSb.append(" 00:00:00' and modify_date <= '");
			rsCountSqlSb.append(selectDate);
			rsCountSqlSb.append(" 23:59:59' )");

			Statement rsCountSqlsm = tbFieldConn.createStatement();
			logger.info("---数据库：" + dbName + "查询结果集总数sql：" + rsCountSqlSb.toString());
			ResultSet rsCountSqlRs = rsCountSqlsm.executeQuery(rsCountSqlSb.toString());
			
			int rsCount = 0;
			while(rsCountSqlRs.next()) {
				rsCount = rsCountSqlRs.getInt("rec") ;
			}
			
			ConncetionUtil.closed(tbFieldConn, rsCountSqlsm, rsCountSqlRs, null);
			//无结果集则跳过
			if(rsCount == 0) {
				continue ;
			}
			
			// 分页查询
			int interval = 10000;
			int tality = rsCount / interval + 1;
			StringBuffer querySqlSb = new StringBuffer();
			// 获取所有字段拼接字符串
			String selectSql = getSelectFieldStr(tbFieldMap);
			querySqlSb.append(" select "+selectSql);
			querySqlSb.append(" from ( ");
			querySqlSb.append(" select ");
			querySqlSb.append(selectSql);
			querySqlSb.append(" from ");
			querySqlSb.append(tableName);
			querySqlSb.append(" where ( create_date >= '");
			querySqlSb.append(selectDate);
			querySqlSb.append(" 00:00:00' and create_date <= '");
			querySqlSb.append(selectDate);
			querySqlSb.append(" 23:59:59' )");
			querySqlSb.append(" union ") ;
			querySqlSb.append(" select ");
			querySqlSb.append(selectSql);
			querySqlSb.append(" from ");
			querySqlSb.append(tableName);
			querySqlSb.append(" where ( modify_date >= '");
			querySqlSb.append(selectDate);
			querySqlSb.append(" 00:00:00' and modify_date <= '");
			querySqlSb.append(selectDate);
			querySqlSb.append(" 23:59:59' )");
			querySqlSb.append(" ) t ");
			querySqlSb.append(" limit ");
			querySqlSb.append("?");
			querySqlSb.append(",");
			querySqlSb.append(interval);

			Connection pageQueryConn = null;
			PreparedStatement pagePreparedStatement = null;

			for (int j = 0; j < tality; j++) {
				int startNum = j * interval ;

				pageQueryConn = ConncetionUtil.getJDBCConnection(dbUrl, dbUserName, dbPassword, dbName);

				if (pageQueryConn == null) {
					throw new Exception("jdbc连接失败");
				}

				pagePreparedStatement = pageQueryConn.prepareStatement(querySqlSb.toString());
				logger.info("---根据分页查询sql：" + querySqlSb.toString());
				pagePreparedStatement.setLong(1, startNum);
				ResultSet rs = pagePreparedStatement.executeQuery();
				
				PreparedStatement preparedStatement = null;
				
				// 获取数据库连接
				Connection connection_report = ConncetionUtil.getJDBCConnection(report_url,report_username, report_password,report_database);
				if (connection_report == null) {
					throw new Exception("jdbc连接失败");
				}
				
				while (rs.next()) {
					
					// 根据id查询是否存在(derp-report数据库)
					Long id = rs.getLong("id");
					String sql_report = "select id from " + tableName + " where id=" + id;
					logger.info("---根据id查询是否存在的sql：" + sql_report);
					Statement stmt_report = connection_report.createStatement();
					ResultSet rs_report = stmt_report.executeQuery(sql_report);
					int flag = 0;
					while (rs_report.next()) {
						flag = 1;
						break;
					}
					// 找得到则修改，否则新增
					StringBuffer saveOrUpdateSqlSb = new StringBuffer();
					String filedTypes="BIGINT|INTEGER|DOUBLE|FLOAT|DECIMAL|TINYINT|SMALLINT|MEDIUMINT|INT";
					if (flag == 1) {
						// 拼接编辑的sql
						saveOrUpdateSqlSb.append("update ").append(tableName).append(" set ");
						String value = "";
						for (String field_name : tbFieldMap.keySet()) {
							String filedTypeName = (String)tbFieldMap.get(field_name);
							// 根据字段类型拼接SQL
							if(filedTypeName.matches(filedTypes)){
								value += "`" + field_name + "`=" + rs.getObject(field_name) + ",";
								
							}else {
								if (rs.getString(field_name) == null || rs.getString(field_name).equals("null")) {
									value += "`" + field_name + "`=" + rs.getObject(field_name) + ",";
								} else {
									
									String temp = rs.getString(field_name) ;
									//处理存在反斜杠导致修改错误问题
									if(temp.contains("\\")) {
										temp = temp.replace("\\", "\\\\") ;
									}
									
									value += "`" + field_name + "`=" + "\'"
											+ temp.replace("'", "") + "\',";
								}
							}
						}
						value = value.substring(0, value.lastIndexOf(",")) ;
						saveOrUpdateSqlSb.append(value).append(" where id=").append(rs.getLong("id"));
						logger.info("---拼接的修改sql：" + saveOrUpdateSqlSb.toString());
						
						preparedStatement = connection_report.prepareStatement(saveOrUpdateSqlSb.toString());
						int num = preparedStatement.executeUpdate();
						logger.info("---同步修改成功数：" + num);
					} else {
						// 拼接新增的sql
						saveOrUpdateSqlSb.append("insert into ").append(tableName).append("(").append(selectSql)
								.append(") values(");
						String value = "";
						// 根据字段类型拼接SQL
						for(String field_name : tbFieldMap.keySet()) {
							String filedTypeName = (String)tbFieldMap.get(field_name);
							if(filedTypeName.matches(filedTypes)) {
								value +=rs.getObject(field_name) + ",";
								
							}else {
								if (rs.getString(field_name) == null || rs.getString(field_name).equals("null")) {
									value += rs.getObject(field_name) + ",";
								} else {
									
									String temp = rs.getString(field_name) ;
									//处理存在反斜杠导致修改错误问题
									if(temp.contains("\\")) {
										temp = temp.replace("\\", "\\\\") ;
									}
									
									value += "\'" + temp.replace("'", "") + "\',";
								}
							}
						}
						value = value.substring(0, value.lastIndexOf(",")) ;
						saveOrUpdateSqlSb.append(value).append(")");
						logger.info("---拼接的新增sql：" + saveOrUpdateSqlSb.toString());
						
						preparedStatement = connection_report.prepareStatement(saveOrUpdateSqlSb.toString());
						int num = preparedStatement.executeUpdate();
						logger.info("---同步新增成功数：" + num);
						
					}
					// 关闭连接
					preparedStatement.close();
				}
				
				ConncetionUtil.closed(connection_report, null, null , null);
				ConncetionUtil.closed(pageQueryConn, pagePreparedStatement, rs, null);
			}
		}
		logger.info("----------------同步【"+dbName+"】结束---------------");
	}

	/**
	 * 返回数据库表字段名及类型集合
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> getTbFieldAndType(ResultSet rs) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();

		if (!rs.wasNull()) {

			ResultSetMetaData metaData = rs.getMetaData();
			
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				
				String typeName = metaData.getColumnTypeName(i + 1) ;
				if(Types.VARCHAR == metaData.getColumnType(i + 1)) {
					typeName = typeName + "("  + metaData.getColumnDisplaySize(i + 1) + ")" ; 
				}
				
				map.put(metaData.getColumnName(i + 1), metaData.getColumnTypeName(i + 1));
			}

		}

		return map;
	}

	/**
	 * 根据字段名属性集合拼成结果集字段
	 * 
	 * @param map
	 * @return
	 */
	private String getSelectFieldStr(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();

		for (String fieldName : map.keySet()) {
			sb.append(" `").append(fieldName).append("`").append(", ");
		}

		String sql = sb.toString();
		sql = sql.substring(0, sql.lastIndexOf(","));

		return sql;
	}
	public void sendLog(String keys,int state,Long point,String modelName,String json,String expMsg) {
		try {
			MQLog mqLog=new MQLog();
			mqLog.setKeyword("R"+ TimeUtils.getNow().getTime()); //设置主键字值
			mqLog.setDesc(modelName);
			mqLog.setModel(modelName);
			mqLog.setPoint(point);//接口编码
			mqLog.setState(state);//1 成功  0 失败
			mqLog.setStartDate(Long.valueOf(keys));//设置接收时间
			mqLog.setEndDate(System.currentTimeMillis());//结束时间
			mqLog.setExpMsg(expMsg);
			JSONObject jsonObject=JSONObject.fromObject(mqLog);
			jsonObject.put("requestMessage",json);  //设置请求报文
			jsonObject.put("id", UUID.randomUUID().toString());
			jsonObject.put("moduleCode", LogModuleType.MODULE_REPORT.getType());
			jsonObject.put("modulCode", "1002");
			SendResult resultMsg = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());

			if (resultMsg==null||!resultMsg.getSendStatus().name().equals("SEND_OK")) {
				jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
				logger.info("==报文丢失："+jsonObject.toString()+"==");
			}

			logger.info("==报文："+jsonObject.toString()+"==");
			logger.info("==响应报文："+resultMsg+"==");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
