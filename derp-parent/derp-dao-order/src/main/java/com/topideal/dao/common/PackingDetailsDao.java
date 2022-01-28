package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.common.PackingDetailsModel;

import java.sql.SQLException;
import java.util.List;


public interface PackingDetailsDao extends BaseDao<PackingDetailsModel> {

    //根据条件删除
    int deleteByModel(PackingDetailsModel packingDetailsModel) throws SQLException;


    //把相同柜号相同商品货号进行合并箱数、件数（托盘号若有多个，用&字符合并）
    List<PackingDetailsModel> listGroupCabinetNo(PackingDetailsModel packingDetailsModel) throws SQLException;






}
