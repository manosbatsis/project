package com.topideal.dao.impl;

import com.topideal.dao.JdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 execute方法：可以用于执行任何SQL语句，一般用于执行DDL语句；
 update方法及batchUpdate方法：update方法用于执行新增、修改、删除等语句；batchUpdate方法用于执行批处理相关语句；
 query方法及queryForXXX方法：用于执行查询相关语句；
 call方法：用于执行存储过程、函数相关语句。
 */
@Repository
public class JdbcDaoImpl implements JdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**表结构变更
     * */
    public void execute(String sql){
        jdbcTemplate.execute(sql);
    }
    /**根据sql查询
     * */
    public List<Map<String,Object>> query(String sql){
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> queryById(String tableName,Object id){
        String sql = "select * from "+tableName+" where id=?";
        return jdbcTemplate.queryForList(sql,id);
    }
    /**
     * 保存
     * @param sql 自定义保存sql
     * @param paramsList 查询条件对应的参数(List<Object>)
     * @return int 保存的数量
     */
    @Override
    public int save(String sql, List<Object> paramsList) {
        //String sql="INSERT INTO tb_name (`name`,age,create_time) VALUES(?,?,?);"
        return jdbcTemplate.update(sql, paramsList.toArray());
    }
    //
    public int update(String sql, List<Object> paramsList){
        //String sql="update tb_name set name=? where id=?";
        return jdbcTemplate.update(sql,paramsList.toArray());
    }
    public int delete(String sql, Object id){
        //String sql="delete from tb_name where id=?";
        return jdbcTemplate.update(sql,id);
    }
}
