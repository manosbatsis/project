package com.topideal.service.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationDao;
import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationItemDao;
import com.topideal.dao.reporting.*;
import com.topideal.dao.system.*;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantBuRelModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.InventoryFinanceAutomaticVerificationService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 业财自核表service实现
 * @Author: Chen Yiluan
 * @Date: 2020/05/18 15:49
 **/
@Service
public class InventoryFinanceAutomaticVerificationServiceImpl implements InventoryFinanceAutomaticVerificationService {

    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private BuInventorySummaryDao buInventorySummaryDao;
    @Autowired
    private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;
    @Autowired
    private BusinessFinanceAutomaticVerificationDao businessFinanceAutomaticVerificationDao;
    @Autowired
    private BusinessFinanceAutomaticVerificationItemDao businessFinanceAutomaticVerificationItemDao;
    @Autowired
    private FileTaskMongoDao fileTaskDao;
    @Autowired
    private CommbarcodeDao commbarcodeDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    private GroupCommbarcodeDao groupCommbarcodeDao;
    @Autowired
    private MerchantBuRelDao merchantBuRelDao;
    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(InventoryFinanceAutomaticVerificationServiceImpl.class);

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502011, model = DERP_LOG_POINT.POINT_13201502011_Label)
    public void saveAutoVeriReport(String json, String keys, String topics, String tags) throws Exception {
        try {
            JSONObject jsonData = JSONObject.fromObject(json);
            Map<String, Object> jsonMap = jsonData;
            Integer merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
            String month = (String) jsonMap.get("month"); //报表月份
            Object buIdObj = jsonMap.get("buId");
            Integer buId=null;
            if (buIdObj!=null) buId = Integer.valueOf(buIdObj.toString());

            //计算要刷新的月份
            if (StringUtils.isEmpty(month)) {
                //若没有指定月份则刷新上个月份
                month = TimeUtils.getLastMonth(new Date());
            }

            //查询所有商家,若指定了商家则只查本商家
            MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
            if (merchantId != null && merchantId.longValue() > 0) {
                merchantInfoModel.setId(Long.valueOf(merchantId));
            }
            List<MerchantInfoModel> merchantList = merchantInfoDao.list(merchantInfoModel);

            for (MerchantInfoModel merchant : merchantList) {

                Map<String, Object> params = new HashMap<>();
                params.put("merchantId", merchant.getId());
                params.put("month", month);

                //查询所有商家,若指定了商家则只查本商家
                MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();
                merchantBuRelModel.setMerchantId(merchant.getId());
                if (buId != null && buId.longValue() > 0) {
                    merchantBuRelModel.setBuId(Long.valueOf(buId));
                }
                List<MerchantBuRelModel> buRelModels = merchantBuRelDao.list(merchantBuRelModel);

                for (MerchantBuRelModel buRelModel : buRelModels) {
                    params.put("buId", buRelModel.getBuId());
                    Map<String, Map<String, Object>> autoModelMap = new HashMap<>();

                    //2 以公司+仓库+事业部+报表月份+标准条码的维度统计事业部业务进销存报表
                    List<Map<String, Object>> buInventorySummaryList = buInventorySummaryDao.listCommbarcodeMonth(params);
                    for (Map<String, Object> map : buInventorySummaryList) {
                        String commbarcode = (String) map.get("commbarcode");
                        BigDecimal buInventorySummaryEndNumBD = (BigDecimal) map.get("month_end_num");
                        Integer preBuInventorySummaryEndNum = buInventorySummaryEndNumBD.intValue();
                        Integer buInventorySummaryEndNum = buInventorySummaryEndNumBD.intValue();
                        String unit = (String) map.get("unit");
                        String key = commbarcode;
                        Integer boxToUnit = 0;
                        Integer torrToUnit = 0;
                        //箱托转换
                        if (!(StringUtils.isEmpty(unit) || unit.equals(DERP.INVENTORY_UNIT_2))) {
                            MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
                            merchandiseInfoModel.setCommbarcode(commbarcode);
                            merchandiseInfoModel.setMerchantId(merchant.getId());
                            List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseInfoDao.list(merchandiseInfoModel);
                            if (merchandiseInfoModels == null || merchandiseInfoModels.size() == 0) {
                                throw new RuntimeException(merchant.getName() + ",标准条码：" + commbarcode + "商品不存在,结束执行");
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
                            buInventorySummaryEndNum = changeUnit(unit, buInventorySummaryEndNumBD, boxToUnit,
                                    torrToUnit, merchant.getName(), commbarcode);
                        }
                        if (autoModelMap.containsKey(key)) {
                            if (autoModelMap.get(key).containsKey("buInventorySummaryEndNum")) {
                                Integer num = (Integer) autoModelMap.get(key).get("buInventorySummaryEndNum");
                                autoModelMap.get(key).put("buInventorySummaryEndNum", buInventorySummaryEndNum + num);
                            } else {
                                autoModelMap.get(key).put("buInventorySummaryEndNum", buInventorySummaryEndNum);
                            }
                            if (autoModelMap.get(key).containsKey("preBuInventorySummaryEndNum")) {
                                Integer preNum = (Integer) autoModelMap.get(key).get("preBuInventorySummaryEndNum");
                                autoModelMap.get(key).put("preBuInventorySummaryEndNum", preBuInventorySummaryEndNum + preNum);
                            } else {
                                autoModelMap.get(key).put("preBuInventorySummaryEndNum", preBuInventorySummaryEndNum);
                            }
                        } else {
                            Map<String, Object> inventorySummaryMap = new HashMap<>();
                            inventorySummaryMap.put("merchantId", merchant.getId());
                            inventorySummaryMap.put("commbarcode", commbarcode);
                            inventorySummaryMap.put("buInventorySummaryEndNum", buInventorySummaryEndNum);
                            inventorySummaryMap.put("preBuInventorySummaryEndNum", preBuInventorySummaryEndNum);
                            autoModelMap.put(key, inventorySummaryMap);
                        }
                    }

                    //4 以公司+仓库+报表月份+标准条码的维度统计事业部财务进销存报表
                    List<Map<String, Object>> buFinanceSummaryList = buFinanceInventorySummaryDao.listCommbarcodeMonth(params);
                    for (Map<String, Object> map : buFinanceSummaryList) {
                        String commbarcode = (String) map.get("commbarcode");
                        String key = commbarcode;
                        BigDecimal buEndNum = map.get("end_num") == null ? new BigDecimal("0") : (BigDecimal) map.get("end_num");
                        BigDecimal addSaleNoshelfNum = map.get("add_sale_noshelf_num") == null ? new BigDecimal("0") : (BigDecimal) map.get("add_sale_noshelf_num");
                        BigDecimal addTransferNoshelfNumNum =  map.get("add_transfer_noshelf_num") == null ? new BigDecimal("0") : (BigDecimal) map.get("add_transfer_noshelf_num");
                        //事业部财务进销存期末量 = 期末结存数量 - 累计销售在途量 - 累计调拨在途量
                        BigDecimal buFinanceSummaryEndNum = buEndNum.subtract(addSaleNoshelfNum).subtract(addTransferNoshelfNumNum);
                        Integer endNumInt = 0;
                        Integer financeSummaryEndNumInt = 0;
                        Integer addSaleNoshelfNumInt = 0;
                        Integer addTransferNoshelfNumNumInt = 0;
                        if (autoModelMap.containsKey(key)) {
                            if (autoModelMap.get(key).containsKey("buEndNum")) {
                                endNumInt = (Integer) autoModelMap.get(key).get("buEndNum");
                            }
                            if (autoModelMap.get(key).containsKey("addSaleNoshelfNum")) {
                                addSaleNoshelfNumInt = (Integer) autoModelMap.get(key).get("addSaleNoshelfNum");
                            }
                            if (autoModelMap.get(key).containsKey("addTransferNoshelfNumNum")) {
                                addTransferNoshelfNumNumInt = (Integer) autoModelMap.get(key).get("addTransferNoshelfNumNum");
                            }
                            if (autoModelMap.get(key).containsKey("buFinanceSummaryEndNum")) {
                                financeSummaryEndNumInt = (Integer) autoModelMap.get(key).get("buFinanceSummaryEndNum");
                            }
                            autoModelMap.get(key).put("buEndNum", buEndNum.intValue()+endNumInt);
                            autoModelMap.get(key).put("buFinanceSummaryEndNum", buFinanceSummaryEndNum.intValue()+financeSummaryEndNumInt);
                            autoModelMap.get(key).put("addSaleNoshelfNum", addSaleNoshelfNum.intValue()+addSaleNoshelfNumInt);
                            autoModelMap.get(key).put("addTransferNoshelfNumNum", addTransferNoshelfNumNum.intValue()+addTransferNoshelfNumNumInt);
                        } else {
                            Map<String, Object> inventorySummaryMap = new HashMap<>();
                            inventorySummaryMap.put("merchantId", merchant.getId());
                            inventorySummaryMap.put("commbarcode", commbarcode);
                            inventorySummaryMap.put("buEndNum", buEndNum.intValue());
                            inventorySummaryMap.put("buFinanceSummaryEndNum", buFinanceSummaryEndNum.intValue());
                            inventorySummaryMap.put("addSaleNoshelfNum", addSaleNoshelfNum.intValue());
                            inventorySummaryMap.put("addTransferNoshelfNumNum", addTransferNoshelfNumNum.intValue());
                            autoModelMap.put(key, inventorySummaryMap);
                        }
                    }

                    //4 删除数据
                    Map<String, Object> delMap = new HashMap<>();
                    delMap.put("merchantId", merchant.getId());
                    delMap.put("month", month);
                    delMap.put("buId", buRelModel.getBuId());
                    businessFinanceAutomaticVerificationDao.deleteByMap(delMap);
                    businessFinanceAutomaticVerificationItemDao.deleteByMap(delMap);

                    //5. 保存数据
                    Set<String> commbarcodeSet = new HashSet<>(); //已对平或已统计的标准条码
                    for (String key : autoModelMap.keySet()) {
                        if (!commbarcodeSet.contains(key)) {
                            boolean isMatch = false; //是否对平
                            BusinessFinanceAutomaticVerificationModel model = new BusinessFinanceAutomaticVerificationModel();
                            BusinessFinanceAutomaticVerificationItemModel itemModel = new BusinessFinanceAutomaticVerificationItemModel();
                            Map<String, Object> map = autoModelMap.get(key);
                            String commbarcode = (String) map.get("commbarcode");
                            Integer buInventorySummaryEndNum = getNumFromMap(map, "buInventorySummaryEndNum");
                            Integer preBuInventorySummaryEndNum = getNumFromMap(map, "preBuInventorySummaryEndNum");
                            Integer buFinanceSummaryEndNum = getNumFromMap(map, "buFinanceSummaryEndNum");
                            Integer addSaleNoshelfNum = getNumFromMap(map, "addSaleNoshelfNum");
                            Integer addTransferNoshelfNumNum = getNumFromMap(map, "addTransferNoshelfNumNum");
                            Integer buEndNum = getNumFromMap(map, "buEndNum");
                            model.setMerchantId(merchant.getId());
                            model.setMerchantName(merchant.getName());
                            model.setBuId(buRelModel.getBuId());
                            model.setBuName(buRelModel.getBuName());
                            model.setCommbarcode(commbarcode);
                            model.setCreateDate(TimeUtils.getNow());
                            model.setMonth(month);
                            model.setBuInventorySummaryEndNum(buInventorySummaryEndNum);
                            model.setBuFinanceSummaryEndNum(buFinanceSummaryEndNum);
                            model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_1);
                            List<Integer> nums = new ArrayList<>();
                            nums.add(buInventorySummaryEndNum);
                            nums.add(buFinanceSummaryEndNum);

                            //当出现业务进销存期末量、事业部业务进销存、财务进销存期末量、事业部财务进销存这四个表中存在一个商品的期末库存量不等（即存在其中一个表有差异时）则比标识为“未对平”；
                            boolean isSame = isSameNum(nums);
                            if (!isSame) {
                                //若对不平，判断是否存在组码关系，若是组码，则把对应的标准条码的数量相加再比对
                                List<String> commbarcodes = groupCommbarcodeDao.getCommbarcodeByCommbarcode(commbarcode);
                                if (commbarcodes != null && commbarcodes.size() > 0) {
                                    //遍历组码中的所有标准条码，对应数值相加（如果对平则不再统计，所以要判断该标准条码是否已经统计过了，避免重复计算）
                                    Set<String> countCommbarcodes = new HashSet<>();
                                    Integer buInventorySummaryEndNum1 = 0;
                                    Integer buFinanceSummaryEndNum1 = 0;
                                    for (String groupCommbarcode : commbarcodes) {
                                        if (!commbarcodeSet.contains(groupCommbarcode)) {
                                            if (!autoModelMap.containsKey(groupCommbarcode)) {
                                                continue;
                                            }
                                            Map<String, Object> otherMap = autoModelMap.get(groupCommbarcode);
                                            buInventorySummaryEndNum1 += getNumFromMap(otherMap, "buInventorySummaryEndNum");
                                            buFinanceSummaryEndNum1 += getNumFromMap(otherMap, "buFinanceSummaryEndNum");
                                            countCommbarcodes.add(groupCommbarcode);
                                        }
                                    }
                                    List<Integer> groupNums = new ArrayList<>();
                                    groupNums.add(buInventorySummaryEndNum1);
                                    groupNums.add(buFinanceSummaryEndNum1);
                                    boolean isGroupSame = isSameNum(groupNums);
                                    if (isGroupSame) {
                                        isMatch = true;
                                        commbarcodeSet.addAll(countCommbarcodes);
                                        model.setBuInventorySummaryEndNum(buInventorySummaryEndNum1);
                                        model.setBuFinanceSummaryEndNum(buFinanceSummaryEndNum1);
                                        businessFinanceAutomaticVerificationDao.save(model);
                                        continue;
                                    }
                                }

                                if (!isMatch) {
                                    commbarcodeSet.add(commbarcode);
                                    model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);
                                    Long id = businessFinanceAutomaticVerificationDao.save(model);
                                    itemModel.setMerchantId(merchant.getId());
                                    itemModel.setMerchantName(merchant.getName());
                                    itemModel.setBuId(buRelModel.getBuId());
                                    itemModel.setBuName(buRelModel.getBuName());
                                    itemModel.setCommbarcode(commbarcode);
                                    itemModel.setBuInventorySummaryEndNum(preBuInventorySummaryEndNum);
                                    itemModel.setBuFinanceSummaryEndNum(buFinanceSummaryEndNum);
                                    itemModel.setBuFinanceSummaryNum(buEndNum);
                                    itemModel.setAddSaleNoshelfNum(addSaleNoshelfNum);
                                    itemModel.setAddTransferNoshelfNum(addTransferNoshelfNumNum);
                                    itemModel.setCreateDate(TimeUtils.getNow());
                                    itemModel.setBusinessFinanceId(id);
                                    itemModel.setMonth(month);
                                    if (!StringUtils.isEmpty(commbarcode)) {
                                        CommbarcodeModel commbarcodeModel = new CommbarcodeModel();
                                        commbarcodeModel.setCommbarcode(commbarcode);
                                        CommbarcodeModel cModel = commbarcodeDao.searchByModel(commbarcodeModel);
                                        if(cModel != null) {
                                            itemModel.setGoodsName(cModel.getCommGoodsName());
                                        }
                                    }
                                    businessFinanceAutomaticVerificationItemDao.save(itemModel);
                                    continue;
                                }
                            }

                            businessFinanceAutomaticVerificationDao.save(model);
                            commbarcodeSet.add(commbarcode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delByTaskType(Map<String, Object> params) {
        fileTaskDao.remove(params);
    }


    /**
     * 比较数字是否相等
     */
    private boolean isSameNum(List<Integer> nums) {
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i).intValue() != nums.get(0).intValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 箱托转换
     *
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

    private Integer getNumFromMap(Map<String, Object> map, String key) {
        if (StringUtils.isNotBlank(key)) {
            Integer num = (Integer) map.get(key);
            if (num == null) {
                num = 0;
            }
            return num;
        }
        return 0;
    }

}
