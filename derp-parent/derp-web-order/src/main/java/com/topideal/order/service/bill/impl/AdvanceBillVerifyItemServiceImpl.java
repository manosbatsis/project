package com.topideal.order.service.bill.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AdvanceBillDao;
import com.topideal.dao.bill.AdvanceBillItemDao;
import com.topideal.dao.bill.AdvanceBillOperateItemDao;
import com.topideal.dao.bill.AdvanceBillVerifyItemDao;
import com.topideal.dao.bill.ReceiveInvoicenoDao;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.bill.AdvanceBillModel;
import com.topideal.entity.vo.bill.AdvanceBillOperateItemModel;
import com.topideal.entity.vo.bill.AdvanceBillVerifyItemModel;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.order.service.bill.AdvanceBillVerifyItemService;
import com.topideal.order.shiro.ShiroUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class AdvanceBillVerifyItemServiceImpl implements AdvanceBillVerifyItemService {
    @Autowired
    private AdvanceBillVerifyItemDao advanceBillVerifyItemDao;
    @Autowired
    private AdvanceBillDao advanceBillDao;
    @Autowired
    private AdvanceBillItemDao advanceBillItemDao;
    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;
    @Autowired
    private ReceiveInvoicenoDao receiveInvoicenoDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;

    @Override
    public List<AdvanceBillVerifyItemDTO> getAdvanceById(Long advanceid) {
        return advanceBillVerifyItemDao.getAdvanceById(advanceid);
    }

    @Override
    public Map<String, Object> saveVerifyItem(String token,AdvanceBillVerifyItemDTO dto){
        Map<String, Object> returnMap = new HashedMap();

        try{
            User user= ShiroUtils.getUserByToken(token);
            //获取预收账单信息
            AdvanceBillModel model=advanceBillDao.searchById(dto.getAdvanceId());
            if(model==null){
                returnMap.put("code","01");
                returnMap.put("message","预收账单信息不存在");
                return returnMap;
            }
            if(!(DERP_ORDER.ADVANCEBILL_BILLSTATUS_03.equals(model.getBillStatus()))){
                returnMap.put("code","01");
                returnMap.put("message","预收账单：" + model.getCode() + "不是待收款状态");
                return returnMap;
            }
            String verifyDate = dto.getVerifyDateStr();
            if(StringUtils.isBlank(verifyDate)){
                returnMap.put("code","01");
                returnMap.put("message","收款日期不能为空");
                return returnMap;
            }
            if(dto.getPrice()==null){
                returnMap.put("code","01");
                returnMap.put("message","本次收款金额不能为空");
                return returnMap;
            }
            String today = TimeUtils.formatDay(new Date());
            if (today.compareTo(verifyDate) < 0) {
                returnMap.put("code", "01");
                returnMap.put("message", "核销日期不得晚于当前日期");
                return returnMap;
            }
            //查看核销的总金额
            BigDecimal verifyPrice=advanceBillVerifyItemDao.getTotalVerifyPrice(model.getId());
            verifyPrice = verifyPrice == null ? new BigDecimal("0") : verifyPrice;

            //判断核销金额是否大于预收金额
            List<Map<String, Object>> itemList=advanceBillItemDao.listItemPrice(Arrays.asList(model.getId()));
            BigDecimal amount=new BigDecimal(0);
            for(Map<String, Object> map:itemList){
                amount=(BigDecimal)map.get("totalPrice");
                if ((verifyPrice.add(dto.getPrice()).compareTo(amount)) ==1) {
                    returnMap.put("code", "01");
                    returnMap.put("message", "核销总金额不得大于预收金额");
                    return returnMap;
                }
            }


            //添加核销记录
            AdvanceBillVerifyItemModel verifymodel=new AdvanceBillVerifyItemModel();
            verifymodel.setAdvanceId(model.getId());
            verifymodel.setPrice(dto.getPrice());
            verifymodel.setVerifyDate(TimeUtils.parseDay(verifyDate));
            verifymodel.setVerifier(user.getName());
            verifymodel.setVerifierId(user.getId());
            verifymodel.setVerifyNo(dto.getVerifyNo());
            advanceBillVerifyItemDao.save(verifymodel);

            //待核销
            if (amount.compareTo(verifyPrice.add(dto.getPrice())) == 0) {
                //核销金额等于预收金额则为待核销
                model.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02);
            } 
            advanceBillDao.modify(model);
            
            //5.保存发票文件到附件
            net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
            
            Map<String, Object> attQueryMap = new HashMap<String, Object>();
            attQueryMap.put("relationCode", model.getCode());
            List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
            for (AttachmentInfoMongo att : attList) {
                if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("attachmentName", att.getAttachmentName());
                    String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                            + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
                    tempMap.put("attachmentUrl", attachmentUrl);
                    JSONObject attJson = new JSONObject();
                    attJson.putAll(tempMap);
                    attArray.add(attJson);
                }
            }
            
            
            // 获取预收金额
            AdvanceBillDTO advanceBillDTO=advanceBillDao.getAdvanceById(model.getId());
            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            JsonConfig jsonConfig = new JsonConfig();
            //getReceiveOperators(billDTOS.get(0).getId(), emailUserDTO, jsonConfig);暂时不要

            emailUserDTO.setBuId(model.getBuId());
            emailUserDTO.setBuName(model.getBuName());
            emailUserDTO.setMerchantId(user.getMerchantId());
            emailUserDTO.setMerchantName(user.getMerchantName());
            emailUserDTO.setOrderCode(model.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4);
            emailUserDTO.setSupplier(model.getCustomerName());
            emailUserDTO.setCurrency(model.getCurrency());
            emailUserDTO.setAmount(advanceBillDTO.getSumAmount().toString());
            emailUserDTO.setVeriAmount(dto.getPrice().toString());//本次收款金额
            BigDecimal sumAmount = advanceBillDTO.getSumAmount();// 预收金额
            if (sumAmount==null)sumAmount=new BigDecimal("0");
            BigDecimal advanceVerifyPrice = advanceBillDTO.getAdvanceVerifyPrice();// 已经收款金额
            if (advanceVerifyPrice==null)advanceVerifyPrice=new BigDecimal("0");
            BigDecimal subtract = sumAmount.subtract(advanceVerifyPrice);
            emailUserDTO.setUnVeriAmount(subtract.toString());//待收款金额

            emailUserDTO.setDrawerId(user.getId());
            
            // 提交人审核人
            AdvanceBillOperateItemModel opraeteI =new AdvanceBillOperateItemModel();
            opraeteI.setAdvanceId(model.getId());        
            List<AdvanceBillOperateItemModel> opraeteIList = advanceBillOperateItemDao.list(opraeteI);
            List<String> submitId=new ArrayList<String>();  
            Long auditorId=null;       
            for (AdvanceBillOperateItemModel operateItemModel : opraeteIList) {      	   
                if (DERP_ORDER.ADVANCEBILL_TYPE_0.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
                	submitId.add(operateItemModel.getOperateId().toString());
                }            
                if (DERP_ORDER.ADVANCEBILL_TYPE_1.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
                	auditorId=operateItemModel.getOperateId();
                } 
            }
            emailUserDTO.setSubmitId(submitId);//提交人
            emailUserDTO.setAuditorId(auditorId);//审核人
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setAttArray(attArray);

            JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

            rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());    
            
            returnMap.put("code", "00");
            returnMap.put("message", "保存成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnMap;
    }



}
