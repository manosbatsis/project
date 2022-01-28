package com.topideal.service.impl;

import com.topideal.dao.reporting.ApiGoodsConfigDao;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.service.ApiGoodsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiGoodsConfigServiceImpl implements ApiGoodsConfigService {

    @Autowired
    private ApiGoodsConfigDao apiGoodsConfigDao;

    @Override
    public List<String> getConfigMerchandiseList(Long merchantId) throws SQLException {
        ApiGoodsConfigModel apiGoodsConfigModel = new ApiGoodsConfigModel();
        apiGoodsConfigModel.setMerchantId(merchantId);
        apiGoodsConfigModel.setStatus("1");
        List<MerchandiseInfoModel> merchandiseInfoModels = apiGoodsConfigDao.getConfigMerchandiseList(apiGoodsConfigModel);

        if (merchandiseInfoModels == null || merchandiseInfoModels.size() == 0) {
            return new ArrayList<>();
        }

        List<String> goodsNoList = merchandiseInfoModels.stream().map(MerchandiseInfoModel::getGoodsNo).collect(Collectors.toList());

        return goodsNoList;
    }
}
