package com.topideal.order.service.platformdata.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.platformdata.PlatformCategoryConfigDao;
import com.topideal.dao.platformdata.PlatfromGoodsCategoryDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.entity.vo.platformdata.PlatfromGoodsCategoryModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.platformdata.PlatformGoodsCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlatformGoodsCategoryServiceImpl implements PlatformGoodsCategoryService {

    @Autowired
    private PlatfromGoodsCategoryDao platfromGoodsCategoryDao ;
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao ;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;
    @Autowired
    private PlatformCategoryConfigDao platformCategoryConfigDao ;


    @Override
    public PlatfromGoodsCategoryDTO getListByPage(PlatfromGoodsCategoryDTO dto, User user) {

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }

        dto = platfromGoodsCategoryDao.getListByPage(dto) ;

        return dto;
    }

    @Override
    public Map<String, Object> importPlatformGoodsCategory(List<Map<String, String>> data, User user) throws SQLException {

        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        int success = 0;
        int pass = 0;
        int failure = 0;

        List<PlatfromGoodsCategoryModel> saveList = new ArrayList<PlatfromGoodsCategoryModel>() ;

        for (int j = 1; j <= data.size(); j++) {

            Map<String, String> map = data.get(j - 1);

            String buCode = map.get("事业部编码");
            if(checkIsNullOrNot(j, buCode, "事业部编码不能为空", resultList)){
                failure += 1;
                continue;
            }

            String mainId = map.get("客户主数据ID");
            if(checkIsNullOrNot(j, mainId, "客户主数据ID不能为空", resultList)){
                failure += 1;
                continue;
            }

            String barcode = map.get("条形码");
            if(checkIsNullOrNot(j, barcode, "条形码不能为空", resultList)){
                failure += 1;
                continue;
            }

            String categoryName = map.get("品类名称");
            if(checkIsNullOrNot(j, categoryName, "品类名称不能为空", resultList)){
                failure += 1;
                continue;
            }

            Map<String, Object> queryMap = new HashMap<>() ;
            queryMap.put("mainId", mainId) ;
            queryMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
            queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

            CustomerInfoMogo cutomer = customerInfoMongoDao.findOne(queryMap);

            if(cutomer == null){
                setErrorMsg(j, "根据主数据查询客户不存在", resultList);
                failure += 1;
                continue;
            }

            queryMap = new HashMap<>() ;
            queryMap.put("merchantId", user.getMerchantId()) ;
            queryMap.put("customerId", cutomer.getCustomerid()) ;

            CustomerMerchantRelMongo rel = customerMerchantRelMongoDao.findOne(queryMap);

            if(rel == null){
                setErrorMsg(j, "公司客户关联不存在", resultList);
                failure += 1;
                continue;
            }

            queryMap = new HashMap<>() ;
            queryMap.put("merchantId", user.getMerchantId()) ;
            queryMap.put("buCode", buCode) ;
            queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;

            MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);

            if(merchantBuRelMongo == null){
                setErrorMsg(j, "公司事业部关联不存在", resultList);
                failure += 1;
                continue;
            }

            PlatformCategoryConfigModel queryModel = new PlatformCategoryConfigModel() ;
            queryModel.setCustomerId(rel.getCustomerId());
            queryModel.setMerchantId(user.getMerchantId());
            queryModel.setName(categoryName);

            queryModel = platformCategoryConfigDao.searchByModel(queryModel) ;

            if(queryModel == null){
                setErrorMsg(j, "该平台品类配置不存在", resultList);
                failure += 1;
                continue;
            }

            queryMap = new HashMap<>() ;
            queryMap.put("merchantId", user.getMerchantId()) ;
            queryMap.put("barcode", barcode) ;
            queryMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

            List<MerchandiseInfoMogo> merchantList = merchandiseInfoMogoDao.findAll(queryMap);

            if(merchantList.isEmpty()){
                setErrorMsg(j, "根据条码查询商品不存在", resultList);
                failure += 1;
                continue;
            }

            MerchandiseInfoMogo merchandiseInfoMogo = merchantList.get(0);

            PlatfromGoodsCategoryModel searchModel = new PlatfromGoodsCategoryModel() ;

            searchModel.setCustomerId(cutomer.getCustomerid());
            searchModel.setMerchantId(user.getMerchantId());
            searchModel.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
            searchModel.setBuId(merchantBuRelMongo.getBuId());
            searchModel.setCategoryId(queryModel.getId());

            searchModel = platfromGoodsCategoryDao.searchByModel(searchModel) ;

            if(searchModel != null){
                setErrorMsg(j, "平台商品品类配置已存在", resultList);
                failure += 1;
                continue;
            }

            PlatfromGoodsCategoryModel saveModel = new PlatfromGoodsCategoryModel() ;
            saveModel.setCategoryId(queryModel.getId());
            saveModel.setCategoryName(queryModel.getName());
            saveModel.setCustomerId(cutomer.getCustomerid());
            saveModel.setCustomerName(cutomer.getName());
            saveModel.setBarcode(barcode);
            saveModel.setBuId(merchantBuRelMongo.getBuId());
            saveModel.setBuName(merchantBuRelMongo.getBuName());
            saveModel.setGoodsName(merchandiseInfoMogo.getName());
            saveModel.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
            saveModel.setMerchantId(user.getMerchantId());
            saveModel.setMerchantName(user.getMerchantName());
            saveModel.setCreater(user.getId());
            saveModel.setCreateName(user.getName());
            saveModel.setCreateDate(TimeUtils.getNow());

            saveList.add(saveModel);
        }

        if(failure == 0){
            for (PlatfromGoodsCategoryModel saveModel: saveList ) {
                platfromGoodsCategoryDao.save(saveModel) ;
            }

            success = data.size() ;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        map.put("message", resultList);

        return map;
    }

    /**
     * 判断输入字段是否为空
     * @param index
     * @param content
     * @param msg
     * @param resultList
     * @return
     */
    private boolean checkIsNullOrNot(int index , String content ,
                                     String msg ,List<ImportErrorMessage> resultList ) {

        if(StringUtils.isBlank(content)) {
            ImportErrorMessage message = new ImportErrorMessage();
            message.setRows(index + 1);
            message.setMessage(msg);
            resultList.add(message);

            return true ;

        }else {
            return false ;
        }

    }

    /**
     * 错误时，设置错误内容
     * @param index
     * @param msg
     * @param resultList
     */
    private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(index + 1);
        message.setMessage(msg);
        resultList.add(message);
    }
}
