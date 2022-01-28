package com.topideal.service.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.reporting.InventoryAnalysisDao;
import com.topideal.dao.reporting.SaleDataDao;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.inventory.BuInventoryModel;
import com.topideal.entity.vo.reporting.InventoryAnalysisModel;
import com.topideal.entity.vo.reporting.SaleDataModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.DepartmentInfoMongo;
import com.topideal.service.InventoryAnalysisGenerateService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryAnalysisGenerateServiceImpl implements InventoryAnalysisGenerateService {

    @Autowired
    private BuInventoryDao buInventoryDao;
    @Autowired
    private WeightedPriceDao weightedPriceDao;
    @Autowired
    private ExchangeRateDao exchangeRateDao;
    @Autowired
    private InventoryAnalysisDao inventoryAnalysisDao;
    @Autowired
    private SaleDataDao saleDataDao;
    @Autowired
    private BusinessUnitDao businessUnitDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private DepartmentInfoMongoDao departmentInfoMongoDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502060, model = DERP_LOG_POINT.POINT_13201502060_Label)
    public void saveInventoryAnalysis(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //指定统计月份(若不指定，默认统计本月的事业部库存)
        if (jsonData.containsKey("selectDate")) {
            month = jsonData.getString("selectDate");
        } else {
            month = sdf.format(new Date());
        }

        inventoryAnalysisDao.batchDelByDate(month);
        List<String> months = new ArrayList<>();
        //获取上个月份
        String lastMonth = TimeUtils.getUpMonth(month);
        months.add(lastMonth);
        //获取上上个月份
        String upMonth = TimeUtils.getUpMonth(lastMonth);
        months.add(upMonth);
        months.add(month);

        Map<Long, BusinessUnitModel> businessUnitModels = getBus();
        Map<Long, DepartmentInfoMongo> departmentInfoMongoMap = new HashMap<>();
        if(businessUnitModels != null) {
            Set<Long> departmentIdSet = businessUnitModels.entrySet().stream().map(entity -> {
                return entity.getValue().getDepartmentId();
            }).collect(Collectors.toSet());
            if(departmentIdSet != null && departmentIdSet.size() > 0) {
                List<DepartmentInfoMongo> departmentInfoMongoList = departmentInfoMongoDao.findAllByIn("departmentInfoId", new ArrayList(departmentIdSet));
                departmentInfoMongoList.forEach(entity -> {
                    departmentInfoMongoMap.put(entity.getDepartmentInfoId(), entity);
                });
            }
        }

        List<Long> buIds = new ArrayList<>(businessUnitModels.keySet());

        Map<String, BrandParentMongo> brandParentMongoMap = new HashMap<>();

        //根据公司、仓库、事业部和标准条码的维度统计指定月份的事业部库存
        List<BuInventoryModel> buInventoryModels = buInventoryDao.getBuInventoryByCommbarcode(month, buIds);

        //公司、仓库、事业部和标准条码的维度合并数据，根据单位进行箱托转换为件合并
        Map<String, BuInventoryModel> buInventoryModelMap = new HashMap<>();

        for (BuInventoryModel buInventory : buInventoryModels) {
            String key = buInventory.getCommbarcode() + "_" + buInventory.getMerchantId() + "_" + buInventory.getDepotId() + "_" + buInventory.getBuId();
            //箱托转换
            Integer stockNum = buInventory.getSurplusNum();
            Integer boxToUnit = 0;
            Integer torrToUnit = 0;
            String unit = buInventory.getUnit();
            //箱托转换
            if (!(org.apache.commons.lang3.StringUtils.isEmpty(unit) || unit.equals(DERP.INVENTORY_UNIT_2))) {
                MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
                merchandiseInfoModel.setCommbarcode(buInventory.getCommbarcode());
                merchandiseInfoModel.setMerchantId(buInventory.getMerchantId());
                List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseInfoDao.list(merchandiseInfoModel);
                if (merchandiseInfoModels == null || merchandiseInfoModels.size() == 0) {
                    throw new RuntimeException(buInventory.getMerchantName() + ",标准条码：" + buInventory.getCommbarcode() + "商品不存在,结束执行");
                }
                for (MerchandiseInfoModel merchandise : merchandiseInfoModels) {
                    if (unit.equals(DERP.INVENTORY_UNIT_0)) {
                        if (merchandise.getTorrToUnit() != null) {
                            torrToUnit = merchandise.getTorrToUnit();
                            break;
                        }
                    }
                    if (unit.equals(DERP.INVENTORY_UNIT_1)) {
                        if (merchandise.getBoxToUnit() != null) {
                            boxToUnit = merchandise.getBoxToUnit();
                            break;
                        }
                    }
                }
                stockNum = changeUnit(buInventory.getUnit(), new BigDecimal(buInventory.getSurplusNum()), boxToUnit, torrToUnit, buInventory.getMerchantName(), buInventory.getCommbarcode());
            }

            if (buInventoryModelMap.containsKey(key)) {
                Integer surplusNum = buInventoryModelMap.get(key).getSurplusNum();
                buInventoryModelMap.get(key).setSurplusNum(surplusNum+stockNum);
            } else {
                buInventory.setSurplusNum(stockNum);
                buInventoryModelMap.put(key, buInventory);
            }
        }

        //根据公司、仓库、事业部和标准条码的维度统计指定月份的销售数据
        List<SaleDataModel> saleDataModels = saleDataDao.getSaleDataByCommbarcode(months);
        Map<String, SaleDataModel> saleDataModelMap = new HashMap<>();
        for (SaleDataModel saleData : saleDataModels) {
            String key = saleData.getCommbarcode() + "_" + saleData.getMerchantId() + "_" + saleData.getOutDepotId() + "_" + saleData.getBuId();
            saleDataModelMap.put(key, saleData);
        }

        List<InventoryAnalysisModel> inventoryAnalysisModels = new ArrayList<>();
        for (String buInventoryKey : buInventoryModelMap.keySet()) {
            BuInventoryModel buInventory = buInventoryModelMap.get(buInventoryKey);
            InventoryAnalysisModel inventoryAnalysisModel = new InventoryAnalysisModel();
            //根据标准条码+事业部+生效月份取加权单价列表中的加权单价，库存金额=数量*加权单价
            BigDecimal price = new BigDecimal("0");
            Integer surplusNum = buInventory.getSurplusNum();
            if (StringUtils.isNotBlank(buInventory.getCommbarcode())) {
                WeightedPriceModel weightedPriceModel = new WeightedPriceModel();
                weightedPriceModel.setCommbarcode(buInventory.getCommbarcode());
                weightedPriceModel.setBuId(buInventory.getBuId());
                weightedPriceModel.setEffectiveMonth(month);
                weightedPriceModel.setMerchantId(buInventory.getMerchantId());
                WeightedPriceModel weightedPrice = weightedPriceDao.searchByModel(weightedPriceModel);
                if (weightedPrice != null) {
                    price = weightedPrice.getPrice();
                    //转化为人民币汇率
                    String currency = weightedPrice.getCurrency();
                    inventoryAnalysisModel.setCurrency(currency);
                    inventoryAnalysisModel.setOriWeightedPrice(price);
                    BigDecimal oriAmount = price.multiply(new BigDecimal(surplusNum));
                    inventoryAnalysisModel.setOriAmount(oriAmount);
                    if (!DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY.equals(currency)) {
                        ExchangeRateModel rate = exchangeRateDao.getLatestRate(month, currency, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
                        if (rate != null) {
                            price = price.multiply(new BigDecimal(rate.getAvgRate().toString()));
                        }
                    }
                }

                BrandParentMongo brandParentMongo = brandParentMongoMap.get(buInventory.getCommbarcode());

                if (brandParentMongo == null) {
                    brandParentMongo = brandParentMongoDao.getBrandParentMongoByCommbarcode(buInventory.getCommbarcode());
                }

                if (brandParentMongo != null) {
                    inventoryAnalysisModel.setBrandId(brandParentMongo.getBrandParentId());
                    inventoryAnalysisModel.setBrandName(brandParentMongo.getName());
                    inventoryAnalysisModel.setParentBrandId(brandParentMongo.getSuperiorParentBrandId());
                    inventoryAnalysisModel.setParentBrandName(brandParentMongo.getSuperiorParentBrand());
                    inventoryAnalysisModel.setParentBrandCode(brandParentMongo.getSuperiorParentBrandCode());
                    brandParentMongoMap.put(buInventory.getCommbarcode(), brandParentMongo);
                }
            }

            BigDecimal amount = price.multiply(new BigDecimal(surplusNum));
            inventoryAnalysisModel.setMerchantId(buInventory.getMerchantId());
            inventoryAnalysisModel.setMerchantName(buInventory.getMerchantName());
            inventoryAnalysisModel.setDepotId(buInventory.getDepotId());
            inventoryAnalysisModel.setDepotName(buInventory.getDepotName());
            inventoryAnalysisModel.setBuId(buInventory.getBuId());
            inventoryAnalysisModel.setBuName(buInventory.getBuName());
            inventoryAnalysisModel.setMonth(month);
            inventoryAnalysisModel.setAmount(amount);
            inventoryAnalysisModel.setSurplusNum(surplusNum);
            inventoryAnalysisModel.setCreateDate(TimeUtils.getNow());
            inventoryAnalysisModel.setCommbarcode(buInventory.getCommbarcode());
            inventoryAnalysisModel.setSaleNum(0);
            inventoryAnalysisModel.setSaleDays(getDaysByYearMonth(month));
            String key = buInventory.getCommbarcode() + "_" + buInventory.getMerchantId() + "_" + buInventory.getDepotId() + "_" + buInventory.getBuId();
            SaleDataModel saleData = saleDataModelMap.get(key);
            if (saleData != null) {
                inventoryAnalysisModel.setSaleNum(saleData.getNum());
            }

            BusinessUnitModel bu = businessUnitModels.get(buInventory.getBuId());
            inventoryAnalysisModel.setDepartmentId(bu.getDepartmentId());
            if(departmentInfoMongoMap != null) {
                DepartmentInfoMongo departmentInfoMongo = departmentInfoMongoMap.get(bu.getDepartmentId());
                if(departmentInfoMongo != null) {
                    inventoryAnalysisModel.setDepartmentCode(departmentInfoMongoMap.get(bu.getDepartmentId()).getCode());
                }
            }
            inventoryAnalysisModel.setDepartmentName(bu.getDepartmentName());
            inventoryAnalysisModels.add(inventoryAnalysisModel);
        }
        int pageSize = 1000;
        for (int i = 0; i < inventoryAnalysisModels.size(); ) {
            int pageSub = (i+pageSize) < inventoryAnalysisModels.size() ? (i+pageSize) : inventoryAnalysisModels.size();
            inventoryAnalysisDao.batchSave(inventoryAnalysisModels.subList(i, pageSub));
            i = pageSub;
        }
    }

    /**
     * 上上月1号到统计月份的日期天数
     */
    public int getDaysByYearMonth(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date today = new Date();
        String nowMonth = sdf.format(today);
        String[] strSplit = monthStr.split("-");
        int year = Integer.valueOf(strSplit[0]);
        int month = Integer.valueOf(strSplit[1]);
        int totalDays = 0;
        if (monthStr.equals(nowMonth)) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(today);
            totalDays += ca.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar thisMonth = Calendar.getInstance();
            thisMonth.set(Calendar.YEAR, year);
            thisMonth.set(Calendar.MONTH, month - 1);
            thisMonth.set(Calendar.DATE, 1);
            thisMonth.roll(Calendar.DATE, -1);
            totalDays += thisMonth.get(Calendar.DATE);
        }
        //上个月
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.set(Calendar.YEAR, year);
        lastMonth.set(Calendar.MONTH, month - 2);
        lastMonth.set(Calendar.DATE, 1);
        lastMonth.roll(Calendar.DATE, -1);
        totalDays += lastMonth.get(Calendar.DATE);
        //上上个月
        Calendar upMonth = Calendar.getInstance();
        upMonth.set(Calendar.YEAR, year);
        upMonth.set(Calendar.MONTH, month - 3);
        upMonth.set(Calendar.DATE, 1);
        upMonth.roll(Calendar.DATE, -1);
        totalDays += upMonth.get(Calendar.DATE);
        return totalDays;
    }

    /**
     * 获取事业部id
     * @return
     */
    private Map<Long, BusinessUnitModel> getBus() throws SQLException {
        Map<Long, BusinessUnitModel> businessUnitModels = new HashMap<>();
        BusinessUnitModel businessUnitModel = new BusinessUnitModel();
        businessUnitModel.setCode("E071100");//宝信
        BusinessUnitModel bxBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(bxBu.getId(), bxBu);
        businessUnitModel.setCode("E070200");//欧莱雅品牌部
        BusinessUnitModel oBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(oBu.getId(), oBu);
        businessUnitModel.setCode("E080600");//母婴美赞臣组
        BusinessUnitModel ppBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(ppBu.getId(), ppBu);
        businessUnitModel.setCode("E081300");//母婴雀巢客户部
        BusinessUnitModel qcBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(qcBu.getId(), qcBu);
        businessUnitModel.setCode("E080602");//母婴美赞臣分销组
        BusinessUnitModel mzcBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(mzcBu.getId(), mzcBu);
        businessUnitModel.setCode("E080700");//母婴新发展业务组
        BusinessUnitModel xfzBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(xfzBu.getId(), xfzBu);
        businessUnitModel.setCode("E060000");//保健品事业部E
        BusinessUnitModel bjpBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(bjpBu.getId(), bjpBu);
        businessUnitModel.setCode("F020814");//贸易金融财务组F
        BusinessUnitModel myBu = businessUnitDao.searchByModel(businessUnitModel);
        businessUnitModels.put(myBu.getId(), myBu);
        return businessUnitModels;
    }

    /**
     * 箱托转换
     * @param unit
     * @param num
     * @param boxToUnit
     * @param torrToUnit
     * @param merchantName
     * @param commbarcode
     * @return
     * @throws RuntimeException
     */
    private Integer changeUnit(String unit, BigDecimal num, Integer boxToUnit, Integer torrToUnit, String merchantName, String commbarcode) throws RuntimeException {
        Integer numLong = 0;
        if (num == null) return numLong;

        //转换单位为件后返回
        if (unit.equals(DERP.INVENTORY_UNIT_1)) {
            if (boxToUnit == null || boxToUnit.intValue() <= 0) {
                throw new RuntimeException(merchantName + ",标准条码：" + commbarcode + "商品箱转件数据为空,结束执行");
            }
            numLong = num.intValue() * boxToUnit.intValue();
        } else if (unit.equals(DERP.INVENTORY_UNIT_0)) {
            if (torrToUnit == null || torrToUnit.intValue() <= 0) {
                throw new RuntimeException(merchantName + ",标准条码：" + commbarcode + "商品托转件数据为空,结束执行");
            }
            numLong = num.intValue() * torrToUnit.intValue();
        }
        return numLong;
    }

}
