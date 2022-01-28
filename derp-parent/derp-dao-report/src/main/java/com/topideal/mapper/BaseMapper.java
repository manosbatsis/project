package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import com.topideal.common.system.ibatis.PageDataModel;

/**
 * Created by weixiaolei on 2017/10/26.
 */
public interface BaseMapper<T> {

    /**
     * 查询所有数据
     * @return
     */
    List<T> list(T t)throws SQLException;
    /**
     * 查询所有数据
     * @return
     */
    PageDataModel<T> listByPage(T t)throws SQLException;

    /**
     * 条件过滤查询
     * @return
     */
    T get(T t)throws SQLException;

    /**
     * 新增数据
     * @return
     */
    int insert(T t)throws SQLException;

    /**
     * 修改数据
     * @return
     */
    int update(T t)throws SQLException;

    /**
     * 删除数据
     * @param id
     * @return
     */
    int del(Long id)throws SQLException;

    /**
     * 批量删除
     * @param ids
     */
    int batchDel(List ids)throws SQLException;





}
