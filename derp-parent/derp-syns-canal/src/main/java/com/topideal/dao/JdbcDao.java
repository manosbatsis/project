package com.topideal.dao;

import java.util.List;
import java.util.Map;

public interface JdbcDao {
    /**表结构变更
     * */
    public void execute(String sql);
    /**根据sql查询
     * */
    public List<Map<String,Object>> query(String sql);
    /**根据id查询
     * */
    public List<Map<String,Object>> queryById(String tableName, Object id);
    /**
     * 保存
     * @param sql 自定义保存sql
     * @param paramsList 查询条件对应的参数(List<Object>)
     * @return int 保存的数量
     */
    public int save(String sql, List<Object> paramsList) ;
    //
    public int update(String sql, List<Object> paramsList);

    public int delete(String sql, Object id);

}
