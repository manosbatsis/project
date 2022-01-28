package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AccountingReminderConfigDao;
import com.topideal.dao.bill.AccountingReminderItemDao;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;
import com.topideal.entity.dto.bill.AccountingReminderItemDTO;
import com.topideal.entity.vo.bill.AccountingReminderConfigModel;
import com.topideal.entity.vo.bill.AccountingReminderItemModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.bill.AccountingReminderConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountingReminderConfigServiceImpl implements AccountingReminderConfigService {

    @Autowired
    private AccountingReminderConfigDao accountingReminderConfigDao ;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao ;
    @Autowired
    private AccountingReminderItemDao accountingReminderItemDao ;


    @Override
    public AccountingReminderConfigDTO getListByPage(AccountingReminderConfigDTO dto) {

        dto = accountingReminderConfigDao.getListByPage(dto) ;

        return dto;
    }

    @Override
    public Long saveOrUpdateAccountingReminderConfig(AccountingReminderConfigDTO dto, User user) throws SQLException {

        AccountingReminderConfigModel model = new AccountingReminderConfigModel() ;

        BeanUtils.copyProperties(dto, model);

        Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("buId", model.getBuId()) ;
        queryMap.put("merchantId", model.getMerchantId()) ;

        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);

        if(merchantBuRelMongo == null){
            new DerpException("公司事业部关联不存在") ;
        }


        queryMap = new HashMap<>() ;
        queryMap.put("customerid", model.getCustomerId()) ;

        CustomerInfoMogo customer = customerInfoMongoDao.findOne(queryMap);

        if(customer == null){
            new DerpException("客商不存在") ;
        }

        model.setBuName(merchantBuRelMongo.getBuName());
        model.setCustomerName(customer.getName());
        model.setMerchantName(merchantBuRelMongo.getMerchantName());

        if(model.getId() == null){
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            model.setStatus(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_0);

            accountingReminderConfigDao.save(model) ;
        }else{
            model.setModifyDate(TimeUtils.getNow());

            accountingReminderConfigDao.modify(model) ;
        }

        AccountingReminderItemModel queryItemModel = new AccountingReminderItemModel() ;
        queryItemModel.setConfigId(model.getId());

        List<AccountingReminderItemModel> tempList = accountingReminderItemDao.list(queryItemModel);

        //删除表体
        if(!tempList.isEmpty()){
            List<Long> idArr = tempList.stream().map(AccountingReminderItemModel::getId).collect(Collectors.toList()) ;

            accountingReminderItemDao.delete(idArr) ;
        }

        List<AccountingReminderItemDTO> itemList = dto.getItemList();

        for (AccountingReminderItemDTO itemDto: itemList) {

            AccountingReminderItemModel itemModel = new AccountingReminderItemModel() ;

            BeanUtils.copyProperties(itemDto, itemModel);

            itemModel.setConfigId(model.getId());
            itemModel.setCreateDate(TimeUtils.getNow());

            accountingReminderItemDao.save(itemModel) ;

        }

        return model.getId() ;
    }

    @Override
    public void auditAccountingReminderConfig(Long id, User user) throws SQLException {

        AccountingReminderConfigModel accountingReminderConfigModel = accountingReminderConfigDao.searchById(id);

        if(accountingReminderConfigModel == null){
            new DerpException("根据ID查询配置不存在") ;
        }

        AccountingReminderConfigModel updateModel = new AccountingReminderConfigModel() ;
        updateModel.setId(id);
        updateModel.setStatus(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_1);
        updateModel.setAuditDate(TimeUtils.getNow());
        updateModel.setAuditer(user.getId());
        updateModel.setAuditName(user.getName());

        accountingReminderConfigDao.modify(updateModel) ;
    }

    @Override
    public void delAccountingReminderConfig(String ids) throws SQLException {

        String[] idArr = ids.split(",");

        List<Long> configIdList = new ArrayList<>() ;

        for (String id: idArr) {

            AccountingReminderConfigModel accountingReminderConfigModel = accountingReminderConfigDao.searchById(Long.valueOf(id));

            if(accountingReminderConfigModel == null){
                new DerpException("根据ID查询配置不存在") ;
            }

            if(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_1.equals(accountingReminderConfigModel.getStatus())){
                new DerpException("存在已审核配置") ;
            }

            AccountingReminderItemModel queryItemModel = new AccountingReminderItemModel() ;
            queryItemModel.setConfigId(Long.valueOf(id));

            List<AccountingReminderItemModel> tempList = accountingReminderItemDao.list(queryItemModel);

            //删除表体
            if(!tempList.isEmpty()){
                List<Long> tempIdArr = tempList.stream().map(AccountingReminderItemModel::getId).collect(Collectors.toList());

                accountingReminderItemDao.delete(tempIdArr) ;
            }

            configIdList.add(Long.valueOf(id)) ;
        }

        accountingReminderConfigDao.delete(configIdList) ;

    }

    @Override
    public AccountingReminderConfigDTO getAccountingReminderConfig(Long id) throws SQLException {

        AccountingReminderConfigModel model = accountingReminderConfigDao.searchById(id);

        AccountingReminderItemModel queryModel = new AccountingReminderItemModel() ;
        queryModel.setConfigId(id);

        List<AccountingReminderItemModel> list = accountingReminderItemDao.list(queryModel);
        List<AccountingReminderItemDTO> dtoList = new ArrayList<AccountingReminderItemDTO>() ;

        for (AccountingReminderItemModel item: list) {

            AccountingReminderItemDTO dto = new AccountingReminderItemDTO() ;

            BeanUtils.copyProperties(item, dto);

            dtoList.add(dto) ;
        }

        AccountingReminderConfigDTO dto = new AccountingReminderConfigDTO() ;
        BeanUtils.copyProperties(model, dto);

        dto.setItemList(dtoList);

        return dto;
    }
}
