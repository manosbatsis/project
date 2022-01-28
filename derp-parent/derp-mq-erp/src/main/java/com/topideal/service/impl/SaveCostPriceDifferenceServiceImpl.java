package com.topideal.service.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.ExchangeRateDao;
import com.topideal.dao.main.BuStockLocationTypeConfigDao;
import com.topideal.dao.main.CostPriceDifferenceDao;
import com.topideal.dao.main.FixedCostPriceDao;
import com.topideal.dao.main.SupplierMerchandisePriceDao;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;
import com.topideal.entity.vo.main.CostPriceDifferenceModel;
import com.topideal.entity.vo.main.FixedCostPriceModel;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;
import com.topideal.service.SaveCostPriceDifferenceService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SaveCostPriceDifferenceServiceImpl implements SaveCostPriceDifferenceService {


    @Autowired
    private CostPriceDifferenceDao costPriceDifferenceDao;
    @Autowired
    private FixedCostPriceDao fixedCostPriceDao;
    @Autowired
    private SupplierMerchandisePriceDao supplierMerchandisePriceDao;
    @Autowired
    private ExchangeRateDao exchangeRateDao;
    @Autowired
    private BuStockLocationTypeConfigDao buStockLocationTypeConfigDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_20700000001,model=DERP_LOG_POINT.POINT_20700000001_Label)
    public void saveCostPriceDifference(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);

        Integer merchantId = (Integer) jsonData.get("merchantId");

        CostPriceDifferenceModel costPriceDifferenceModel = new CostPriceDifferenceModel();
        if (merchantId != null) {
            costPriceDifferenceModel.setMerchantId(Long.valueOf(merchantId));
        }
        List<CostPriceDifferenceModel> costPriceDifferenceModels = costPriceDifferenceDao.list(costPriceDifferenceModel);

        Set<String> keySet = new HashSet<>();

        Map<Long, BuStockLocationTypeConfigModel> stockLocationTypeConfigModelMap = new HashMap<>();

        //相同“公司+事业部+条形码+类型”的成本单价差异表数据
        Map<String, CostPriceDifferenceModel> keyDifferenceMap = new HashMap<>();
        for (CostPriceDifferenceModel differenceModel : costPriceDifferenceModels) {
            String key = differenceModel.getMerchantId() + "_" + differenceModel.getBuId() + "_" +
                    differenceModel.getBarcode() + "_" + differenceModel.getStockLocationTypeId();
            keyDifferenceMap.put(key, differenceModel);
        }

        FixedCostPriceDTO fixedCostPriceDTO = new FixedCostPriceDTO();
        fixedCostPriceDTO.setStatus(DERP_SYS.FIXED_COST_PRICE_STATUS_1);
        if (merchantId != null) {
            fixedCostPriceDTO.setMerchantId(Long.valueOf(merchantId));
        }
        List<FixedCostPriceModel> fixedCostPriceModels = fixedCostPriceDao.getLatestModel(fixedCostPriceDTO);

        List<CostPriceDifferenceModel> addCostPriceDifferenceModels = new ArrayList<>();

        for (FixedCostPriceModel fixedCostPriceModel : fixedCostPriceModels) {

            SupplierMerchandisePriceModel supplierMerchandisePriceModel = new SupplierMerchandisePriceModel();
            supplierMerchandisePriceModel.setMerchantId(fixedCostPriceModel.getMerchantId());
            supplierMerchandisePriceModel.setBuId(fixedCostPriceModel.getBuId());
            supplierMerchandisePriceModel.setBarcode(fixedCostPriceModel.getBarcode());
            supplierMerchandisePriceModel.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);

            List<SupplierMerchandisePriceModel> supplierMerchandisePriceModels = supplierMerchandisePriceDao.listGroupByStockType(supplierMerchandisePriceModel);

            //若相同“公司+事业部+条形码+类型”已有存在成本单价差异表数据记录时，则无需更新该行数据；
            // 若相同“公司+事业部+条形码+类型”无存在成本单价差异表数据记录，则以“公司+事业部+条形码+类型”为维度生成一行数据记录；
            for (SupplierMerchandisePriceModel supplierMerchandisePrice : supplierMerchandisePriceModels) {
                String key = supplierMerchandisePrice.getMerchantId() + "_" + supplierMerchandisePrice.getBuId() + "_" +
                        supplierMerchandisePrice.getBarcode() + "_" + supplierMerchandisePrice.getStockLocationTypeId();

                if (keyDifferenceMap.containsKey(key) || keySet.contains(keys) || supplierMerchandisePrice.getStockLocationTypeId() == null) {
                    continue;
                }

                keySet.add(key);

                CostPriceDifferenceModel differenceModel = new CostPriceDifferenceModel();
                differenceModel.setMerchantId(fixedCostPriceModel.getMerchantId());
                differenceModel.setMerchantName(fixedCostPriceModel.getMerchantName());
                differenceModel.setBuName(fixedCostPriceModel.getBuName());
                differenceModel.setBuId(fixedCostPriceModel.getBuId());
                differenceModel.setBarcode(fixedCostPriceModel.getBarcode());
                differenceModel.setGoodsName(fixedCostPriceModel.getGoodsName());
                differenceModel.setStockLocationTypeId(supplierMerchandisePrice.getStockLocationTypeId());
                differenceModel.setFixedCost(fixedCostPriceModel.getFixedCost());

                BuStockLocationTypeConfigModel typeConfigModel = stockLocationTypeConfigModelMap.get(supplierMerchandisePrice.getStockLocationTypeId());
                if (typeConfigModel == null) {
                    typeConfigModel = buStockLocationTypeConfigDao.searchById(supplierMerchandisePrice.getStockLocationTypeId());
                }
                differenceModel.setStockLocationTypeName(typeConfigModel.getName());

                double rate = 1.0;
                if (!fixedCostPriceModel.getCurrency().equals(supplierMerchandisePrice.getCurrency())) {
                    ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
                    exchangeRateModel.setTgtCurrencyCode(fixedCostPriceModel.getCurrency());
                    exchangeRateModel.setOrigCurrencyCode(supplierMerchandisePrice.getCurrency());
                    ExchangeRateModel rateModel = exchangeRateDao.getLatestRate(exchangeRateModel);
                    if (rateModel != null) {
                        rate = rateModel.getAvgRate();
                    }
                }

                BigDecimal purchasePrice = supplierMerchandisePrice.getSupplyPrice().multiply(new BigDecimal(String.valueOf(rate))).setScale(8, BigDecimal.ROUND_HALF_UP);
                differenceModel.setPurchasePrice(purchasePrice);
                differenceModel.setCurrency(fixedCostPriceModel.getCurrency());
                differenceModel.setCreateDate(TimeUtils.getNow());
                addCostPriceDifferenceModels.add(differenceModel);

            }
        }

        //批量新增
        if (addCostPriceDifferenceModels.size() > 0) {
            costPriceDifferenceDao.batchSave(addCostPriceDifferenceModels);
        }

    }
}
