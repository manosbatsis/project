package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface ApiGoodsConfigMapper extends BaseMapper<ApiGoodsConfigModel> {

    /**
     * 查询对外商品配置表该公司下启用的商品
     * @param apiGoodsConfigModel
     * @return
     * @throws SQLException
     */
    List<MerchandiseInfoModel> getConfigMerchandiseList(ApiGoodsConfigModel apiGoodsConfigModel) throws SQLException;

}
