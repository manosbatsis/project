package com.topideal.service.main.impl;

import com.alibaba.fastjson.JSONArray;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.*;
import com.topideal.dao.user.RoleInfoDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.ReminderEmailConfigModel;
import com.topideal.entity.vo.main.ReminderEmailRelModel;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.service.main.ReminderEmailConfigService;
import com.topideal.webapi.form.ReminderEmailConfigForm;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReminderEmailConfigServiceImpl implements ReminderEmailConfigService {

    @Autowired
    ReminderEmailConfigDao reminderEmailConfigDao;
    @Autowired
    ReminderEmailRelDao reminderEmailRelDao;
    @Autowired
    MerchantInfoDao merchantInfoDao;
    @Autowired
    BusinessUnitDao businessUnitDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    RoleInfoDao roleInfoDao;

    @Override
    public ReminderEmailConfigDTO getListByPage(ReminderEmailConfigDTO model) throws SQLException {
        return reminderEmailConfigDao.listByPage(model);
    }

    @Override
    public Map deleteReminderEmailConfig(long id) throws SQLException {
        Map resultMap=new HashMap();
        List<Long> relIds = new ArrayList<>() ;
        ReminderEmailRelModel relModel = new ReminderEmailRelModel() ;
        relModel.setConfigId(id);
        List<ReminderEmailRelModel> relList = reminderEmailRelDao.list(relModel);
        for (ReminderEmailRelModel relTempModel : relList) {
            relIds.add(relTempModel.getId()) ;
        }
        reminderEmailRelDao.delete(relIds) ;
        reminderEmailConfigDao.deleteById(id);

        resultMap.put("code","00");
        resultMap.put("message", "删除成功！");
        return resultMap;
    }

    @Override
    public Map<String, Object> saveReminderEmailConfig(User user, ReminderEmailConfigForm form) throws SQLException {
        Map<String, Object> resultMap=new HashMap<>();
        //校验非空字段
        resultMap=checkAll(resultMap,form);
        if(resultMap.size() >0){
            return resultMap;
        }
        //校验公司+事业部+业务模块是否存在记录
        ReminderEmailConfigModel model=new ReminderEmailConfigModel();
        model.setBuId(form.getBuId());
        model.setBusinessModuleType(form.getBusinessModuleType());
        model.setMerchantId(form.getMerchantId());
        List<ReminderEmailConfigModel> emailList=reminderEmailConfigDao.list(model);
        if(emailList.size()>0){
            resultMap.put("code","01");
            resultMap.put("message","已存在相同记录，请勿重复新增");
            return resultMap;
        }
        //根据商家id查询商家名称
        MerchantInfoModel merchant=merchantInfoDao.searchById(form.getMerchantId());
        model.setMerchantName(merchant.getName());
        //根据事业部id查询事业部名字
        BusinessUnitModel business=businessUnitDao.searchById(form.getBuId());
        model.setBuName(business.getName());
        model.setCreateDate(TimeUtils.getNow());
        model.setCreateName(user.getName());
        model.setCreator(user.getId());
        //新增邮件提醒信息
        Long id = reminderEmailConfigDao.save(model);

        //新增表体
        List<ReminderEmailRelModel> addList=new ArrayList<ReminderEmailRelModel>();
        List<ReminderEmailRelDTO> reminderItems = form.getItems();
        for(ReminderEmailRelDTO item:reminderItems){
            item.setCreateDate(TimeUtils.getNow());
            item.setConfigId(id);
            ReminderEmailRelModel modelRel=new ReminderEmailRelModel();
            BeanUtils.copyProperties(item,modelRel);
            //提醒类型为用户
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_2.equals(item.getReminderType())){
                List<Long> list = StrUtils.parseIds(item.getUserIdStr());
                if(list==null) {
                    addList.add(modelRel);
                }else {
                    for (long ids : list) {
                        UserInfoModel userInfo = userInfoDao.searchById(ids);
                        modelRel = new ReminderEmailRelModel();
                        BeanUtils.copyProperties(item, modelRel);
                        modelRel.setUserId(ids);
                        modelRel.setUserName(userInfo.getName());
                        addList.add(modelRel);
                    }
                }
            }
            //提醒类型为角色
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_1.equals(item.getReminderType())){
                RoleInfoModel roleModel=roleInfoDao.searchById(item.getRoleId());
                item.setRoleName(roleModel.getName());
                BeanUtils.copyProperties(item,modelRel);
                addList.add(modelRel);
            }
        }
        if(addList.size() > 0){
            for (ReminderEmailRelModel item:addList){
                reminderEmailRelDao.save(item);
            }
        }
        resultMap.put("code","00");
        resultMap.put("message", "保存成功！");
        return resultMap;
    }

    @Override
    public Map<String, Object> updateReminderEmailConfig(User user, ReminderEmailConfigForm form) throws SQLException {
        Map<String, Object> resultMap=new HashMap<>();
        //校验非空字段
        resultMap=checkAll(resultMap,form);
        if(resultMap.size() >0){
            return resultMap;
        }
        ReminderEmailConfigModel reminderModel=reminderEmailConfigDao.searchById(form.getId());
        if(reminderModel==null){
            resultMap.put("code","01");
            resultMap.put("message", "不存在该条记录！");
            return resultMap;
        }
        ReminderEmailConfigModel model=new ReminderEmailConfigModel();
        BeanUtils.copyProperties(form,model);
        //根据商家id查询商家名称
        MerchantInfoModel merchant=merchantInfoDao.searchById(form.getMerchantId());
        model.setMerchantName(merchant.getName());
        //根据事业部id查询事业部名字
        BusinessUnitModel business=businessUnitDao.searchById(form.getBuId());
        model.setBuName(business.getName());

        model.setModifier(user.getId());
        model.setModifyName(user.getName());
        model.setModifyDate(TimeUtils.getNow());
        reminderEmailConfigDao.modify(model);

        List<Long> ids=new ArrayList<>();// 编辑删除表体ids
        //根据邮件提现表头查询表体
        ReminderEmailRelModel emailRelMode=new ReminderEmailRelModel();
        emailRelMode.setConfigId(reminderModel.getId());
        List<ReminderEmailRelModel> listEmail=reminderEmailRelDao.list(emailRelMode);
        for(ReminderEmailRelModel item:listEmail){
            ids.add(item.getId());
        }
        // 根据表头id删除表体，重新添加
        reminderEmailRelDao.delete(ids);

        //新增表体
        List<ReminderEmailRelModel> addList=new ArrayList<ReminderEmailRelModel>();
        List<ReminderEmailRelDTO> reminderItems = form.getItems();
        for(ReminderEmailRelDTO item:reminderItems){
            item.setCreateDate(TimeUtils.getNow());
            item.setConfigId(reminderModel.getId());
            ReminderEmailRelModel modelRel=new ReminderEmailRelModel();
            BeanUtils.copyProperties(item,modelRel);
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_2.equals(item.getReminderType())){
                List<Long> list = StrUtils.parseIds(item.getUserIdStr());
                if(list==null) {
                    addList.add(modelRel);
                }else{
                    for(long id:list){
                        UserInfoModel userInfo=userInfoDao.searchById(id);
                        if(userInfo!=null){
                            modelRel=new ReminderEmailRelModel();
                            BeanUtils.copyProperties(item,modelRel);
                            modelRel.setUserId(id);
                            modelRel.setUserName(userInfo.getName());
                            addList.add(modelRel);
                        }
                    }
                }
            }
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_1.equals(item.getReminderType())){
                RoleInfoModel roleModel=roleInfoDao.searchById(item.getRoleId());
                item.setRoleName(roleModel.getName());
                BeanUtils.copyProperties(item,modelRel);
                addList.add(modelRel);
            }
        }
        if(addList.size() > 0){
            for (ReminderEmailRelModel item:addList){
                reminderEmailRelDao.save(item);
            }
        }
        resultMap.put("code","00");
        resultMap.put("message", "修改成功！");
        return resultMap;
    }

    @Override
    public ReminderEmailConfigDTO getById(long id) {
        return reminderEmailConfigDao.getById(id);
    }

    private Map<String, Object> checkAll(Map<String, Object> resultMap,ReminderEmailConfigForm form){
        Map containsMap=new HashMap();
        if(form.getMerchantId()==null){
            resultMap.put("code","01");
            resultMap.put("message","商家是必填项不能为空");
            return resultMap;
        }
        if(form.getBuId()==null){
            resultMap.put("code","01");
            resultMap.put("message","事业部不能为空,必填");
            return resultMap;
        }
        if(form.getBusinessModuleType()==null){
            resultMap.put("code","01");
            resultMap.put("message","业务模块类型不能为空,必填");
            return resultMap;
        }
        //邮件提醒的内容表体校验
        List<ReminderEmailRelDTO> reminderItems = form.getItems();
        if (reminderItems.size() ==0) {
            resultMap.put("code","01");
            resultMap.put("message","邮件提醒的内容表体不能为空,必填");
            return resultMap;
        }
        for(ReminderEmailRelDTO item : reminderItems){
            if(StringUtils.isBlank(item.getType())){
                resultMap.put("code","01");
                resultMap.put("message","操作节点不能为空,必填");
                return resultMap;
            }
            if(containsMap.containsKey(item.getType())){
                resultMap.put("code","01");
                resultMap.put("message","同一个邮件提醒操作节点不能重复");
                return resultMap;
            }
            containsMap.put(item.getType(), item);

            if(StringUtils.isBlank(item.getReminderType())){
                resultMap.put("code","01");
                resultMap.put("message","提醒对象类型不能为空,必填");
                return resultMap;
            }
            //按用户
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_2.equals(item.getReminderType())){
                if(item.getUserIdStr()==null&&StringUtils.isBlank(item.getBillType())){
                    resultMap.put("code","01");
                    resultMap.put("message","单据对象和固定对象至少有一个值");
                    return resultMap;
                }
            }
            //按角色
            if(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_1.equals(item.getReminderType())){
                if(item.getRoleId()==null){
                    resultMap.put("code","01");
                    resultMap.put("message","提醒对象不能为空,必填");
                    return resultMap;
                }
            }
            if(item.getReminderChannel()==null){
                resultMap.put("code","01");
                resultMap.put("message","提醒渠道不能为空,必填");
                return resultMap;
            }
        }
        return resultMap;
    }

    public List<RoleInfoModel> getRoleList() throws SQLException {

        RoleInfoModel role = new RoleInfoModel() ;

        role.setDeleted(DERP_SYS.ROLEINFO_DELETED_0);
        List<RoleInfoModel> list = roleInfoDao.list(role);

        return list;
    }

    @Override
    public List<UserInfoModel> getUserList() throws SQLException {

        UserInfoModel user = new UserInfoModel() ;

        user.setDisable(DERP_SYS.USERINFO_DISABLE_0);

        List<UserInfoModel> userList = userInfoDao.list(user);

        return userList;
    }
}
