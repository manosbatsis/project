package com.topideal.report.service.reporting.impl;

import com.topideal.dao.IndexOrderStatisticsDao;
import com.topideal.dao.order.BillOutinDepotDao;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.dao.reporting.SkuPriceWarnDao;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.dto.SkuPriceWarnDTO;
import com.topideal.entity.vo.IndexOrderStatisticsModel;
import com.topideal.report.service.reporting.ExternalAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 首页 提供给外部访问 实现类
 * @Author: Chen Yiluan
 * @Date: 2019/12/10 16:42
 **/
@Service
public class ExternalAccessServiceImpl implements ExternalAccessService {

    @Autowired
    private SaleOutDepotDao saleOutDepotDao;

    @Autowired
    private IndexOrderStatisticsDao indexOrderStatisticsDao;

    @Autowired
    private BillOutinDepotDao billOutinDepotDao;

    @Autowired
    private SkuPriceWarnDao skuPriceWarnDao;

    @Override
    public List<BrandSaleDTO> countSaleOutOrderByMerchantIdAndSaleType(Map<String, Object> paramMap) throws SQLException {

        return saleOutDepotDao.countSaleOutOrderByMerchantIdAndSaleType(paramMap);
    }

    @Override
    public List<BrandSaleDTO> getSaleOutOrderTop10ByBrand(Map<String, Object> paramMap) throws SQLException {
        return saleOutDepotDao.getSaleOutOrderTop10ByBrand(paramMap);
    }

    @Override
    public List<BrandSaleDTO> countOrderByMerchantIdAndShopType(Map<String, Object> paramMap) throws SQLException {
        Long merchantId = (Long) paramMap.get("merchantId");
        String shopType = (String) paramMap.get("shopType");
        String deliverDate = (String) paramMap.get("deliverDate");
        String statisticalDimension = (String) paramMap.get("statisticalDimension");

        IndexOrderStatisticsModel indexOrderStatisticsModel = new IndexOrderStatisticsModel();
        indexOrderStatisticsModel.setMerchantId(merchantId);
        indexOrderStatisticsModel.setShopTypeCode(shopType);
        indexOrderStatisticsModel.setStatisticalDimension(statisticalDimension);
        indexOrderStatisticsModel.setMonth(deliverDate);
        List<IndexOrderStatisticsModel> list = indexOrderStatisticsDao.getListByPage(indexOrderStatisticsModel);
        List<BrandSaleDTO> brandSaleDtos = new ArrayList<>();
        for (IndexOrderStatisticsModel model : list) {
            BrandSaleDTO brandSaleDto = new BrandSaleDTO();
            brandSaleDto.setName(model.getName());
            brandSaleDto.setSaleNum(model.getSaleNum());
            brandSaleDtos.add(brandSaleDto);
        }
        return brandSaleDtos;
    }

    @Override
    public List<Map<String, Object>> countByCustomer(Map<String, Object> params) throws SQLException {
        return billOutinDepotDao.countByCustomer(params);
    }

    @Override
    public List<Map<String, Object>> getBillOutDepotTop10ByBrand(Map<String, Object> paramMap) throws SQLException {
        return billOutinDepotDao.getBillOutDepotTop10ByBrand(paramMap);
    }

    @Override
    public List<SkuPriceWarnDTO> getSkuPriceWarns(Map<String, Object> paramMap) throws SQLException {
        SkuPriceWarnDTO dto = new SkuPriceWarnDTO();
        Long merchantId = (Long) paramMap.get("merchantId");
        dto.setMerchantId(merchantId);

        if(paramMap.containsKey("buList") && paramMap.get("buList") != null){
            List<Long> buList = (List<Long>) paramMap.get("buList");
            dto.setBuList(buList);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String month = sdf.format(new Date());
        dto.setMonth(month);
        return skuPriceWarnDao.listToDeal(dto);
    }

    @Override
    public Long countSkuPriceWarns(Map<String, Object> paramMap) throws SQLException {
        SkuPriceWarnDTO dto = new SkuPriceWarnDTO();
        Long merchantId = (Long) paramMap.get("merchantId");
        dto.setMerchantId(merchantId);

        if(paramMap.containsKey("buList") && paramMap.get("buList") != null){
            List<Long> buList = (List<Long>) paramMap.get("buList");
            dto.setBuList(buList);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String month = sdf.format(new Date());

        dto.setMonth(month);
        return skuPriceWarnDao.countForDeal(dto);
    }

}
