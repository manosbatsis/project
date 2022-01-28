package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseFrameContractDao;
import com.topideal.dao.purchase.PurchaseTryApplyOrderDao;
import com.topideal.entity.vo.purchase.PurchaseFrameContractModel;
import com.topideal.entity.vo.purchase.PurchaseTryApplyOrderModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.DepartmentInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.UserInfoMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.UserInfoMongo;
import com.topideal.service.timer.GetPurchaseDateService;
import com.topideal.webService.OAUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/23 18:08
 * @Description:
 */
@Service
public class GetPurchaseDateServiceImpl implements GetPurchaseDateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPurchaseDateServiceImpl.class);

    @Autowired
    private PurchaseFrameContractDao purchaseFrameContractDao;
    @Autowired
    private PurchaseTryApplyOrderDao purchaseTryApplyOrderDao;
    @Autowired
    private DepartmentInfoMongoDao departmentInfoMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private UserInfoMongoDao userInfoMongoDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_20100000009, model = DERP_LOG_POINT.POINT_20100000009_Label)
    public void getPurchaseFrameContractFromOA(String json, String keys, String topics, String tags) throws Exception{
        JSONObject jsonData = JSONObject.fromObject(json);
//        String formId = jsonData.containsKey("formId") ? jsonData.getString("formId") : null;
        String beginDate = jsonData.containsKey("beginDate") ? jsonData.getString("beginDate") : null;
        String endDate = jsonData.containsKey("endDate") ? jsonData.getString("endDate") : null;

        String formId = ApolloUtil.oaPurchaseFrameContractFormId;
        if(StringUtils.isBlank(formId)) {
            throw new RuntimeException("formId 不能为空");
        }

        if(StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            endDate = TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), +1), TimeUtils.YYYY_MM_DD);
            beginDate = TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), -7), TimeUtils.YYYY_MM_DD);
        }
        JSONArray purchaseFrameContractFormOA = OAUtils.getPurchaseFrameContractFormOA(formId, beginDate, endDate);
        List<PurchaseFrameContractModel> contractModelList = (List<PurchaseFrameContractModel>) JSONArray.toCollection(purchaseFrameContractFormOA, PurchaseFrameContractModel.class);
        if(purchaseFrameContractFormOA == null || purchaseFrameContractFormOA.isEmpty()) {
            return;
        }

        // 根据合同编号去重
        contractModelList = contractModelList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(entity -> entity.getContractNo()))), ArrayList::new));

        List<String> topidealCodeList = contractModelList.stream().map(PurchaseFrameContractModel::getOurContSignComy).collect(Collectors.toList());
        List<MerchantInfoMongo> merchantInfoMongoList = merchantInfoMongoDao.findAllByIn("topidealCode", topidealCodeList);
        Map<String, MerchantInfoMongo> merchantInfoMongoMap = merchantInfoMongoList.stream().filter(entity -> {
            String status = entity.getStatus();
            return StringUtils.equals(status, DERP_SYS.MERCHANTINFO_STATUS_1);
        }).collect(Collectors.toMap(MerchantInfoMongo::getTopidealCode, MerchantInfoMongo -> MerchantInfoMongo));


        PurchaseFrameContractModel searchModel = new PurchaseFrameContractModel();
        List<PurchaseFrameContractModel> addList = new ArrayList<>();
        Map<String, DepartmentInfoMongoDao> departmentMap = new HashMap<>();
        for (PurchaseFrameContractModel model : contractModelList) {
            // 判断主体是否在经分销有记录,没有则跳过
            String ourContSignComy = model.getOurContSignComy();
            if(StringUtils.isBlank(ourContSignComy) || !merchantInfoMongoMap.containsKey(ourContSignComy)) {
                continue;
            }
            MerchantInfoMongo merchantInfoMongo = merchantInfoMongoMap.get(ourContSignComy);
            if(merchantInfoMongo != null) {
                model.setMerchantId(merchantInfoMongo.getMerchantId());
                model.setMerchantName(merchantInfoMongo.getName());
            }

            // 供应商编码
            String counterpartContSignComy = model.getCounterpartContSignComy();
            Map<String, Object> param = new HashMap<>();
            if(StringUtils.isNotBlank(counterpartContSignComy)) {
                param.put("mainId", counterpartContSignComy);
                param.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
                param.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);
                CustomerInfoMogo customerInfoMogo = customerInfoMongoDao.findOne(param);
                if(customerInfoMogo != null) {
                    model.setSupplierId(customerInfoMogo.getCustomerid());
                    model.setSupplierName(customerInfoMogo.getName());
                }
            }

            // 申请人
            String createrCode = model.getCreater();
            param.clear();
            if(StringUtils.isNotBlank(createrCode)) {
                param.put("code", createrCode);
                param.put("disable", DERP_SYS.USERINFO_DISABLE_0);
                UserInfoMongo one = userInfoMongoDao.findOne(param);
                if (one != null) {
                    model.setCreaterName(one.getName());
                }
            }

            // 业务员
            String businessManager = model.getBusinessManager();
            param.clear();
            if(StringUtils.isNotBlank(businessManager)) {
                param.put("code", businessManager);
                param.put("disable", DERP_SYS.USERINFO_DISABLE_0);
                UserInfoMongo two = userInfoMongoDao.findOne(param);
                if (two != null) {
                    model.setBusinessManagerName(two.getName());
                }
            }
            searchModel.setContractNo(model.getContractNo());
            PurchaseFrameContractModel contractModel = purchaseFrameContractDao.searchByModel(searchModel);

            if(contractModel != null) {
                // 2、如果合同协议号，存在，则判断协议开始日期、协议结束日期、合同状态是否一致，不一致则更新，一致则不更新
                String key1 = contractModel.getContractStartTime() + "_" + contractModel.getContractEndTime()
                        + "_" + contractModel.getContractState() + "_";
                String key2 = model.getContractStartTime() + "_" + model.getContractEndTime()
                        + "_" + model.getContractState() + "_";
                if(!StringUtils.equals(key1, key2)) {
                    model.setId(contractModel.getId());
                    model.setModifyDate(TimeUtils.getNow());
                    purchaseFrameContractDao.modify(model);
                }
            }else {
                // 1、如果合同协议号，不存在则新增
                addList.add(model);
            }
        }

        // 批量更新
        if(addList != null && addList.size() > 0) {
            purchaseFrameContractDao.batchSave(addList);
        }
    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_20100000010, model = DERP_LOG_POINT.POINT_20100000010_Label)
    public void getPurchaseTryApplyOrderFromOA(String json, String keys, String topics, String tags) throws Exception{
        JSONObject jsonData = JSONObject.fromObject(json);
//        String formId = jsonData.containsKey("formId") ? jsonData.getString("formId") : null;
        String beginDate = jsonData.containsKey("beginDate") ? jsonData.getString("beginDate") : null;
        String endDate = jsonData.containsKey("endDate") ? jsonData.getString("endDate") : null;

        String formId = ApolloUtil.oaPurchaseTryApplyContractFormId;
        if(StringUtils.isBlank(formId)) {
            throw new RuntimeException("formId 不能为空");
        }

        if(StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            endDate = TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), +1), TimeUtils.YYYY_MM_DD);
            beginDate = TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), -7), TimeUtils.YYYY_MM_DD);
        }
        JSONArray purchaseFrameContractFormOA = OAUtils.getPurchaseTryApplyOrderFormOA(formId, beginDate, endDate);
        if(purchaseFrameContractFormOA == null) {
            return;
        }
        List<PurchaseTryApplyOrderModel> contractModelList = (List<PurchaseTryApplyOrderModel>) JSONArray.toCollection(purchaseFrameContractFormOA, PurchaseTryApplyOrderModel.class);
        if(purchaseFrameContractFormOA == null || purchaseFrameContractFormOA.isEmpty()) {
            return;
        }

        // 根据OA流水编号去重
        contractModelList = contractModelList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(entity -> entity.getOaBillCode()))), ArrayList::new));

        List<String> topidealCodeList = contractModelList.stream().map(PurchaseTryApplyOrderModel::getOurContSignComy).collect(Collectors.toList());
        List<MerchantInfoMongo> merchantInfoMongoList = merchantInfoMongoDao.findAllByIn("topidealCode", topidealCodeList);
        Map<String, MerchantInfoMongo> merchantInfoMongoMap = merchantInfoMongoList.stream().filter(entity -> {
            String status = entity.getStatus();
            return StringUtils.equals(status, DERP_SYS.MERCHANTINFO_STATUS_1);
        }).collect(Collectors.toMap(MerchantInfoMongo::getTopidealCode, MerchantInfoMongo -> MerchantInfoMongo));


        PurchaseTryApplyOrderModel searchModel = new PurchaseTryApplyOrderModel();
        List<PurchaseTryApplyOrderModel> addList = new ArrayList<>();
        for (PurchaseTryApplyOrderModel model : contractModelList) {
            // 判断主体是否在经分销有记录
            String ourContSignComy = model.getOurContSignComy();
            if(StringUtils.isBlank(ourContSignComy) || !merchantInfoMongoMap.containsKey(ourContSignComy)) {
                continue;
            }

            MerchantInfoMongo merchantInfoMongo = merchantInfoMongoMap.get(ourContSignComy);
            if(merchantInfoMongo != null) {
                model.setMerchantId(merchantInfoMongo.getMerchantId());
                model.setMerchantName(merchantInfoMongo.getName());
            }

            // 供应商编码
            String counterpartContSignComy = model.getCounterpartContSignComy();
            Map<String, Object> param = new HashMap<>();
            if(StringUtils.isNotBlank(counterpartContSignComy)) {
                param.put("mainId", counterpartContSignComy);
                param.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
                param.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);
                CustomerInfoMogo customerInfoMogo = customerInfoMongoDao.findOne(param);
                if(customerInfoMogo != null) {
                    model.setSupplierId(customerInfoMogo.getCustomerid());
                    model.setSupplierName(customerInfoMogo.getName());
                }
            }

            // 申请人
            String createrCode = model.getCreater();
            param.clear();
            if(StringUtils.isNotBlank(createrCode)) {
                param.put("code", createrCode);
                param.put("disable", DERP_SYS.USERINFO_DISABLE_0);
                UserInfoMongo one = userInfoMongoDao.findOne(param);
                if (one != null) {
                    model.setCreaterName(one.getName());
                }
            }

            // 业务员
            String businessManager = model.getBusinessManager();
            param.clear();
            if(StringUtils.isNotBlank(businessManager)) {
                param.put("code", businessManager);
                param.put("disable", DERP_SYS.USERINFO_DISABLE_0);
                UserInfoMongo two = userInfoMongoDao.findOne(param);
                if (two != null) {
                    model.setBusinessManagerName(two.getName());
                }
            }

            searchModel.setOaBillCode(model.getOaBillCode());
            PurchaseTryApplyOrderModel contractModel = purchaseTryApplyOrderDao.searchByModel(searchModel);

            if(contractModel != null) {
                // 2、如果流水编号，存在则不更新
               continue;
            }else {
                // 1、如果流水编号，不存在则新增
                addList.add(model);
            }
        }

        // 批量更新
        if(addList != null && addList.size() > 0) {
            purchaseTryApplyOrderDao.batchSave(addList);
        }
    }
}
