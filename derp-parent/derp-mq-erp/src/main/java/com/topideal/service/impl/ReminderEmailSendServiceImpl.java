package com.topideal.service.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.main.ReminderEmailConfigDao;
import com.topideal.dao.main.ReminderEmailRelDao;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.dao.user.UserRoleRelDao;
import com.topideal.entity.dto.main.ReminderEmailUserDTO;
import com.topideal.entity.vo.main.ReminderEmailConfigModel;
import com.topideal.entity.vo.main.ReminderEmailRelModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserRoleRelModel;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.ReminderEmailSendService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.smurfs.tools.gateway.client.GatewayHttpClient;
import priv.smurfs.tools.gateway.common.ApiResponse;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReminderEmailSendServiceImpl implements ReminderEmailSendService {
    private static final Logger LOGGER = Logger.getLogger(ReminderEmailSendServiceImpl.class);
    @Autowired
    private ReminderEmailConfigDao reminderEmailConfigDao;
    @Autowired
    private ReminderEmailRelDao reminderEmailRelDao;
    @Autowired
    private UserRoleRelDao userRoleRelDao;
    @Autowired
    private UserBuRelDao userBuRelDao;
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700005,model=DERP_LOG_POINT.POINT_13201700005_label,keyword="orderCode")
    public void saveSendMail(String json, String keys, String topics, String tags) throws SQLException {

        JSONObject jsonData = JSONObject.fromObject(json);
        ReminderEmailUserDTO dto=(ReminderEmailUserDTO)JSONObject.toBean(jsonData, ReminderEmailUserDTO.class);
        if(dto==null){
            throw new RuntimeException("参数为空");
        }
        if (dto.getMerchantId()==null) {
            throw new RuntimeException("发送邮件提醒，商家ID为空");
        }
        if (dto.getBuId() == null) {
            throw new RuntimeException("发送邮件提醒，事业部ID为空");
        }
        if (StringUtils.isBlank(dto.getBusinessModuleType())) {
            throw new RuntimeException("发送邮件提醒，业务模块类型为空");
        }
        if (StringUtils.isBlank(dto.getType())) {
            throw new RuntimeException("发送邮件提醒，操作节点值为空");
        }
        Long merchantId = dto.getMerchantId();
        Long buId =dto.getBuId();
        String businessModuleType = dto.getBusinessModuleType();
        String type= dto.getType();

        //查询邮件提醒表头信息
        ReminderEmailConfigModel model=new ReminderEmailConfigModel();
        model.setMerchantId(merchantId);
        model.setBuId(buId);
        model.setBusinessModuleType(businessModuleType);
        List<ReminderEmailConfigModel> list=reminderEmailConfigDao.list(model);
        if(list.size()==0){
            throw new RuntimeException("邮件提醒，数据不存在");
        }
        Set<String> mailSet=new HashSet<>();//保存电话号码或邮件
        String templateCode=null;//存储消息模板
        String typeReminder=null;//存储提醒渠道

        ReminderEmailConfigModel emailModel=list.get(0);
        //查询邮件提醒表体记录
        List<ReminderEmailRelModel> listEmailRel=reminderEmailRelDao.getConfigIdAndType(emailModel.getId(),type);
        if(listEmailRel.size()==0){
            throw new RuntimeException("邮件提醒表体信息不存在");
        }
        for(ReminderEmailRelModel item:listEmailRel) {
            templateCode = item.getTemplateCode();
            if(StringUtils.isBlank(templateCode)){
                throw new RuntimeException("邮件提醒模板编码为空");
            }
            typeReminder=item.getReminderChannel();
            //类型为角色
            if (DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_1.equals(item.getReminderType())) {
                UserRoleRelModel userRoleRelModel = new UserRoleRelModel();
                userRoleRelModel.setRoleId(item.getRoleId());
                List<UserRoleRelModel> userRoleRelList = userRoleRelDao.list(userRoleRelModel);
                for (UserRoleRelModel tempUserRoleRelModel : userRoleRelList) {
                    //判断用户是否关联该事业部
                    UserBuRelModel queryUserBuModel = new UserBuRelModel();
                    if (buId != null) {
                        queryUserBuModel.setBuId(buId);
                    }
                    queryUserBuModel.setUserId(tempUserRoleRelModel.getUserId());
                    List<UserBuRelModel> UserBuRelModels = userBuRelDao.list(queryUserBuModel);
                    if (UserBuRelModels == null || UserBuRelModels.size() == 0) {
                        continue;
                    }
                    UserInfoModel queryUserModel = new UserInfoModel();
                    queryUserModel.setId(tempUserRoleRelModel.getUserId());
                    queryUserModel.setDisable(DERP_SYS.USERINFO_DISABLE_0);
                    queryUserModel = userInfoDao.searchByModel(queryUserModel);
                    if (queryUserModel != null) {
                        if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_1.equals(item.getReminderChannel())) {
                            mailSet.add(queryUserModel.getEmail());
                        }
                        if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_2.equals(item.getReminderChannel())) {
                            mailSet.add(queryUserModel.getTel());
                        }
                    }
                }


            }
            //类型为用户
            if (DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_2.equals(item.getReminderType())) {
                //判断用户是否关联该事业部
                UserBuRelModel queryUserBuModel = new UserBuRelModel();
                if (buId != null) {
                    queryUserBuModel.setBuId(buId);
                }
                queryUserBuModel.setUserId(item.getUserId());
                List<UserBuRelModel> UserBuRelModels = userBuRelDao.list(queryUserBuModel);
                if (UserBuRelModels == null || UserBuRelModels.size() == 0) {
                    continue;
                }
                if(item.getUserId()!=null){
                    UserInfoModel queryUserModel = new UserInfoModel();
                    queryUserModel.setId(item.getUserId());
                    queryUserModel.setDisable(DERP_SYS.USERINFO_DISABLE_0);
                    queryUserModel = userInfoDao.searchByModel(queryUserModel);
                    if (queryUserModel != null) {
                        if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_1.equals(item.getReminderChannel())) {
                            mailSet.add(queryUserModel.getEmail());
                        }
                        if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_2.equals(item.getReminderChannel())) {
                            mailSet.add(queryUserModel.getTel());
                        }
                    }
                }
                //1：创建人 2：提交人 3：审核人 4：核销人  5：作废核销人 6：盖章发票人 7：审核完毕人 8：作废完成人 9：开票人 10：金额审核人 11：金额修改人 12：上架人',
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1.equals(item.getBillType())&&dto.getCreateId()!=null &&dto.getCreateId()>0){
                    getUserId(mailSet,dto.getCreateId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2.equals(item.getBillType())&&dto.getSubmitId()!=null &&dto.getSubmitId().size() > 0){
                    for(String ids:dto.getSubmitId()){
                        getUserId(mailSet,Long.valueOf(ids),typeReminder);
                    }
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3.equals(item.getBillType())&&dto.getAuditorId()!=null &&dto.getAuditorId()>0){
                    getUserId(mailSet,dto.getAuditorId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_4.equals(item.getBillType())&&dto.getVerificationId()!=null &&dto.getVerificationId()>0){
                    getUserId(mailSet,dto.getVerificationId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5.equals(item.getBillType())&&dto.getCancelId()!=null &&dto.getCancelId()>0){
                    getUserId(mailSet,dto.getCancelId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_6.equals(item.getBillType())&&dto.getReminderOrgId()!=null &&dto.getReminderOrgId()>0){
                    getUserId(mailSet,dto.getReminderOrgId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_7.equals(item.getBillType())&&dto.getCompleteId()!=null &&dto.getCompleteId()>0){
                    getUserId(mailSet,dto.getCompleteId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_8.equals(item.getBillType())&&dto.getCancelCompleteId()!=null &&dto.getCancelCompleteId()>0){
                    getUserId(mailSet,dto.getCancelCompleteId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_9.equals(item.getBillType())&&dto.getDrawerId()!=null &&dto.getDrawerId()>0){
                    getUserId(mailSet,dto.getDrawerId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_10.equals(item.getBillType())&&dto.getReviewerId()!=null &&dto.getReviewerId()>0){
                    getUserId(mailSet,dto.getReviewerId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_11.equals(item.getBillType())&&dto.getModifyId()!=null &&dto.getModifyId()>0){
                    getUserId(mailSet,dto.getModifyId(),typeReminder);
                }
                if(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_12.equals(item.getBillType())&&dto.getShelvesId()!=null &&dto.getShelvesId()>0){
                    getUserId(mailSet,dto.getShelvesId(),typeReminder);
                }
            }
        }

        //类型为采购且操作节点为上架则根据上架人查询操作人
        if("采购".equals(dto.getTypeName())&&"3".equals(dto.getType())){
            UserInfoModel user=userInfoDao.searchById(dto.getShelvesId());
            
            if(user != null) {
            	jsonData.put("userName", user.getName());
            }else {
            	jsonData.put("userName", "");
            }
            
        }

        if(dto.getUserId()!=null &&dto.getUserId().size() > 0){
            for(String ids:dto.getUserId()){
                getUserId(mailSet,Long.valueOf(ids),typeReminder);
            }
        }

        JSONObject jsonObject = new JSONObject();// 推送内容
        JSONObject paramJson = new JSONObject();// 存储所有数据的结果
        //发送邮件
        if(DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_1.equals(typeReminder)){
            StringBuffer mailSB = new StringBuffer();
            for (String mail : mailSet) {
                mailSB.append(mail).append(";");
            }
            paramJson.putAll(jsonData);
            jsonObject.put("paramJson", paramJson);
            jsonObject.put("mailCode", templateCode);
            jsonObject.put("recipients", mailSB.toString());
            LOGGER.info("-----------------邮件提醒发送蓝精灵报文-----------------："+jsonObject);

            // 调用外部接口发送邮件
            String resultMsg = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
//            String resultMsg = sendSc1(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
            LOGGER.info("蓝精灵返回结果："+resultMsg);
            LOGGER.info("-----------------发送邮件提醒结束----------------------");
        }
        //发送企业微信
        if(DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_2.equals(typeReminder)){
            StringBuffer mailSB = new StringBuffer();
            for (String mail : mailSet) {
                mailSB.append(mail).append(",");
            }
            paramJson.putAll(jsonData);
            jsonObject.put("code",templateCode);//模板编码
            jsonObject.put("paramJson",paramJson);
            jsonObject.put("mentionedMobileList",mailSB.toString());

            // 调用外部接口发送企业微信
            String resultMsg= SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_WEIXN);
//            String resultMsg = sendSc1(jsonObject, SmurfsAPIEnum.SMURFS_WEIXN);
            LOGGER.info("蓝精灵返回结果："+resultMsg);
            LOGGER.info("-----------------发送企业微信提醒结束----------------------");
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param mailSet
     * @param userId
     * @param reminderChannerl
     */
    private void getUserId(Set<String> mailSet,Long userId,String reminderChannerl) {
        try{
            UserInfoModel user = new UserInfoModel();
            user.setId(userId);
            user.setDisable(DERP_SYS.USERINFO_DISABLE_0);
            user = userInfoDao.searchByModel(user);
            if (user == null) {
                throw  new RuntimeException("用户不存在");
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_1.equals(reminderChannerl)) {
                if(StringUtils.isNotBlank(user.getEmail())){
                    mailSet.add(user.getEmail());
                }
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_2.equals(reminderChannerl)) {
                if(StringUtils.isNotBlank(user.getTel())){
                    mailSet.add(user.getTel());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @SuppressWarnings("static-access")
    public String sendSc1(JSONObject json, SmurfsAPIEnum apiEnum){
//GatewayHttpClient.newInstance(host, appKey, secret)
        try {
            GatewayHttpClient gatewayHttpClient =
                    GatewayHttpClient.newInstance("gateway.smurfs.topideal.mobi", "10002", "D8478AA375024F218902E72B04D33F58");
            //发送请求
            ApiResponse response =gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
            String resultJson = response.getResultJson();
            LOGGER.info("蓝精灵返回结果："+resultJson);
            return resultJson;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("蓝精灵返回异常:"+e.getMessage());
            LOGGER.error("蓝精灵返回异常:"+e.getMessage());
            return null;
        }

    }
}