package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;

import java.sql.SQLException;
import java.util.List;


public interface ApiGoodsConfigDao extends BaseDao<ApiGoodsConfigModel> {

    /**
     * 查询对外商品配置表该公司下启用的商品
     * @param apiGoodsConfigModel
     * @return
     * @throws SQLException
     */
    List<MerchandiseInfoModel> getConfigMerchandiseList(ApiGoodsConfigModel apiGoodsConfigModel) throws SQLException;








}
