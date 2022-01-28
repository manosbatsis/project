package com.topideal.order.service.platformdata.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.platformdata.PlatformCategoryConfigDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.order.service.platformdata.PlatformCategoryConfigService;

@Service
public class PlatformCategoryConfigServiceImpl implements PlatformCategoryConfigService {

    @Autowired
    PlatformCategoryConfigDao platformCategoryConfigDao ;
    @Autowired
    CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
    @Autowired
    CustomerInfoMongoDao customerInfoMongoDao ;


    @Override
    public PlatformCategoryConfigDTO getListByPage(PlatformCategoryConfigDTO dto) {

        dto = platformCategoryConfigDao.getListByPage(dto) ;

        return dto;
    }

    @Override
    public void savePlatformCategoryConfig(PlatformCategoryConfigDTO dto) throws SQLException {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;

        queryMap.put("customerId", dto.getCustomerId()) ;
        queryMap.put("merchantId", dto.getMerchantId()) ;

        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(queryMap);

        if(customerRel == null){
            throw  new DerpException("公司客户关联不存在") ;
        }


        PlatformCategoryConfigModel queryModel = new PlatformCategoryConfigModel() ;
        queryModel.setCustomerId(dto.getCustomerId());
        queryModel.setMerchantId(dto.getMerchantId());
        queryModel.setName(dto.getName());

        queryModel = platformCategoryConfigDao.searchByModel(queryModel) ;

        if(queryModel != null){
            throw  new DerpException("该平台品类配置已存在") ;
        }

        PlatformCategoryConfigModel saveModel = new PlatformCategoryConfigModel() ;
        BeanUtils.copyProperties(dto, saveModel);

        saveModel.setCustomerName(customerRel.getName());
        saveModel.setCreateDate(TimeUtils.getNow());

        platformCategoryConfigDao.save(saveModel) ;
    }

    @Override
    public Map<String, Object> importPlatformCategoryConfig(List<Map<String, String>> data, User user) throws SQLException {

        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        int success = 0;
        int pass = 0;
        int failure = 0;

        List<PlatformCategoryConfigModel> saveList = new ArrayList<>() ;

        for (int j = 1; j <= data.size(); j++) {

            Map<String, String> map = data.get(j - 1);

            String mainId = map.get("客户主数据ID");
            if(checkIsNullOrNot(j, mainId, "客户主数据ID不能为空", resultList)){
                failure += 1;
                continue;
            }

            String name = map.get("品类名称");
            if(checkIsNullOrNot(j, name, "品类名称不能为空", resultList)){
                failure += 1;
                continue;
            }

            String remarks = map.get("备注");

            Map<String, Object> queryMap = new HashMap<>() ;
            queryMap.put("mainId", mainId) ;
            queryMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
            queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

            CustomerInfoMogo cutomer = customerInfoMongoDao.findOne(queryMap);

            if(cutomer == null){
                setErrorMsg(j, "根据主数据查询客户不存在或已禁用", resultList);
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

            PlatformCategoryConfigModel queryModel = new PlatformCategoryConfigModel() ;
            queryModel.setCustomerId(rel.getCustomerId());
            queryModel.setMerchantId(user.getMerchantId());
            queryModel.setName(name);

            queryModel = platformCategoryConfigDao.searchByModel(queryModel) ;

            if(queryModel != null){
                setErrorMsg(j, "该平台品类配置已存在", resultList);
                failure += 1;
                continue;
            }

            PlatformCategoryConfigModel saveModel = new PlatformCategoryConfigModel() ;
            saveModel.setMerchantId(user.getMerchantId());
            saveModel.setMerchantName(user.getMerchantName());
            saveModel.setCustomerId(cutomer.getCustomerid());
            saveModel.setCustomerName(cutomer.getName());
            saveModel.setName(name);
            saveModel.setRemarks(remarks);
            saveModel.setCreater(user.getId());
            saveModel.setCreateName(user.getName());
            saveModel.setCreateDate(TimeUtils.getNow());

            saveList.add(saveModel);
        }

        if(failure == 0){
            for (PlatformCategoryConfigModel saveModel: saveList ) {
                platformCategoryConfigDao.save(saveModel) ;
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

    @Override
    public List<PlatformCategoryConfigModel> getListByMerchantId(PlatformCategoryConfigDTO dto) throws SQLException {

        PlatformCategoryConfigModel queryModel = new PlatformCategoryConfigModel() ;
        queryModel.setMerchantId(dto.getMerchantId());

        List<PlatformCategoryConfigModel> list = platformCategoryConfigDao.list(queryModel);

        return list;
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

	@Override
	public List<SelectBean> getSelectBean(PlatformCategoryConfigDTO dto) throws SQLException {
		return platformCategoryConfigDao.getSelectBean(dto);
	}
}
