package com.topideal.mapper;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.IndexOrderStatisticsModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface IndexOrderStatisticsMapper extends BaseMapper<IndexOrderStatisticsModel> {

    //根据归属月份删除数据
    void batchDelByDate(@Param("month") String month) throws SQLException;

    //统计获取数据
    List<IndexOrderStatisticsModel> getTop10(IndexOrderStatisticsModel indexOrderStatisticsModel) throws SQLException;
}
