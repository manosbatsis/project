package com.topideal.dao;

import com.topideal.entity.vo.IndexOrderStatisticsModel;

import java.sql.SQLException;
import java.util.List;


public interface IndexOrderStatisticsDao extends BaseDao<IndexOrderStatisticsModel>{

    //根据归属月份删除数据
    void batchDelByDate(String month) throws SQLException;

    //统计获取数据
    List<IndexOrderStatisticsModel> getListByPage(IndexOrderStatisticsModel indexOrderStatisticsModel) throws SQLException;
}
