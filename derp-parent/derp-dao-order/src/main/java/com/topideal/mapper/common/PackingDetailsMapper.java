package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PackingDetailsMapper extends BaseMapper<PackingDetailsModel> {

    //根据条件删除
    int deleteByModel(PackingDetailsModel packingDetailsModel) throws SQLException;

    //把相同柜号相同商品货号进行合并箱数、件数（托盘号若有多个，用&字符合并）
    List<PackingDetailsModel> listGroupCabinetNo(PackingDetailsModel packingDetailsModel) throws SQLException;
}
