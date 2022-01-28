package com.topideal.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.ShopTypeEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.IndexOrderStatisticsDao;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.vo.IndexOrderStatisticsModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.service.GenerateOrderStatisticsService;

import net.sf.json.JSONObject;

/**
 * @Description: 首页—统计电商订单销售总量
 * @Author: Chen Yiluan
 * @Date: 2019/12/24 11:51
 **/
@Service
public class GenerateOrderStatisticsServiceImpl implements GenerateOrderStatisticsService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private IndexOrderStatisticsDao indexOrderStatisticsDao;

    @Override
    public void generateOrderStatisticsByShopType(String json) throws SQLException {

         JSONObject jsonData = JSONObject.fromObject(json);
        String month = null;
        //指定统计月份(若不指定，默认统计上个月的销售总量)
        if (jsonData.containsKey("selectTime")) {
            month = jsonData.getString("selectTime");
        } else {
            month = TimeUtils.getLastMonth(new Date());
        }

        MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
        merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
        List<MerchantInfoModel> merchantInfoModels = merchantInfoDao.list(merchantInfoModel);

        //先删除统计月份数据
        indexOrderStatisticsDao.batchDelByDate(month);

        //遍历商家
        for (MerchantInfoModel model : merchantInfoModels) {
            //遍历店铺类型
            for (ShopTypeEnum shopType : ShopTypeEnum.values()) {
                String shopTypeCode = shopType.getKey();
                String shopTypeName = shopType.getValue();
                // 统计对应商家主体下电商订单中不同店铺类型的电商销售总量
                List<BrandSaleDTO> brandSaleDtos = orderDao.countOrderByMerchantIdAndShopType(model.getId(), shopTypeCode, month);
                for (BrandSaleDTO brandSaleDto : brandSaleDtos) {
                    IndexOrderStatisticsModel indexOrderStatisticsModel = new IndexOrderStatisticsModel();
                    indexOrderStatisticsModel.setMerchantId(model.getId());
                    indexOrderStatisticsModel.setMerchantName(model.getName());
                    indexOrderStatisticsModel.setName(brandSaleDto.getName());
                    indexOrderStatisticsModel.setCode(brandSaleDto.getCode());
                    indexOrderStatisticsModel.setSaleNum(brandSaleDto.getSaleNum());
                    indexOrderStatisticsModel.setShopTypeCode(shopTypeCode);
                    indexOrderStatisticsModel.setShopTypeName(shopTypeName);
                    indexOrderStatisticsModel.setMonth(month);
                    indexOrderStatisticsModel.setStatisticalDimension(DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_1);
                    indexOrderStatisticsModel.setCreateDate(TimeUtils.getNow());
                    indexOrderStatisticsDao.save(indexOrderStatisticsModel);
                }

                //汇总统计不同店铺类型的订单销量top 10的品牌
                List<BrandSaleDTO> brandDtos = orderDao.getOrderTop10ByBrand(model.getId(), shopTypeCode, month);
                for (BrandSaleDTO brandSaleDto : brandDtos) {
                    IndexOrderStatisticsModel indexOrderStatisticsModel = new IndexOrderStatisticsModel();
                    indexOrderStatisticsModel.setMerchantId(model.getId());
                    indexOrderStatisticsModel.setMerchantName(model.getName());
                    indexOrderStatisticsModel.setName(brandSaleDto.getName());
                    indexOrderStatisticsModel.setCode(brandSaleDto.getCode());
                    indexOrderStatisticsModel.setSaleNum(brandSaleDto.getSaleNum());
                    indexOrderStatisticsModel.setShopTypeCode(shopTypeCode);
                    indexOrderStatisticsModel.setShopTypeName(shopTypeName);
                    indexOrderStatisticsModel.setMonth(month);
                    indexOrderStatisticsModel.setStatisticalDimension(DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_2);
                    indexOrderStatisticsModel.setCreateDate(TimeUtils.getNow());
                    indexOrderStatisticsDao.save(indexOrderStatisticsModel);
                }

            }

        }

    }

}
