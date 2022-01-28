package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
/**
 * Created by weixiaolei on 2018/4/10.
 */
public interface BaseDao<T> {
    /**
     * 新增
     * @param model  新增的参数
     * @return   自增长id
     * @throws SQLException
     */
     Long save(T model)throws SQLException;

    /**
     * 删除
     * @param ids  待删除id
     * @return   删除记录数
     * @throws SQLException
     */
     int delete(List ids)throws SQLException;

    /**
     *  修改
     * @param model  修改的实体类参数
     * @return  修改的记录数
     * @throws SQLException
     */
     int modify(T model)throws SQLException;

    /**
     * 分页查询
     * @param model
     * @return
     */
     T  searchByPage(T model)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
     T searchById(Long id)throws SQLException;

     /**
      * 通过实体查询实体类信息
      * @param model
      * @return
      */
      T searchByModel(T model)throws SQLException;
      
      /**
       * 列表查询
       * @param model
       * @return
       * @throws SQLException
       */
      List<T> list(T model)throws SQLException;

}
