package com.topideal.service.api.bill.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AdvancePaymentBillDao;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.mongo.dao.ContractNoMongoDao;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.service.api.bill.OAExamineResultService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OAExamineResultServiceImpl implements OAExamineResultService {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(OABillExamineLogServiceImpl.class);

    @Autowired
    private ContractNoMongoDao contractNoMongoDao;
    @Autowired
    private AdvancePaymentBillDao advancePaymentBillDao;
    @Autowired
    private OperateLogDao operateLogDao;
    @Autowired
    private PaymentBillDao paymentBillDao;
	//外部单号
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private RMQProducer rocketMQProducer;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_12208000001, model = DERP_LOG_POINT.POINT_12208000001_Label,keyword="requestId")
    public boolean updateExamineResult(String json, String keys, String topics, String tags) throws Exception {
        //实例化json对象
        JSONObject jsonData = JSONObject.fromObject(json);

        String requestId=(String)jsonData.get("requestId");//流程id
        String appResult=(String)jsonData.get("appResult");//审批结果  0：审批驳回；1：审批通过
        String oaBillCode=(String)jsonData.get("oaBillCode");//OA单据号

        /*0：审批驳回 更新单据状态为“已驳回” 1：审批通过 更新单据状态为“待付款”*/
        String status="";
        if("0".equals(appResult)){
            status=DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02;
        }else if("1".equals(appResult)){
            status=DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03;
        }

        //根据流程id查询账单是否存在
        Map<String, Object> contractNoParams = new HashMap<>();
        contractNoParams.put("contractNo", requestId);
        List<ContractNoMongo> contractNoMongoList = contractNoMongoDao.findAll(contractNoParams);
        if(contractNoMongoList== null||contractNoMongoList.size() <=0){
            throw new RuntimeException("流程id为："+requestId+"对应的账单数据不存在");
        }
        ContractNoMongo contractNo=contractNoMongoList.get(0);

        //预付款单
        if("YFKD".equals(contractNo.getType())) {
            //查询对应的预付款单是否存在
            AdvancePaymentBillModel model=new AdvancePaymentBillModel();
            model.setCode(contractNo.getOrderCode());
            AdvancePaymentBillModel paymentModel=advancePaymentBillDao.searchByModel(model);
            if(paymentModel==null){
                throw new RuntimeException("预付款单数据不存在,预付款单号为："+contractNo.getOrderCode());
            }
           /* //查询账单是否为待提交或审核中
            if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00.equals(paymentModel.getBillStatus())
                    ||DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())
                  )){
                LOGGER.error("预付款单只对待提交或审核中的状态进行处理,单号:" + paymentModel.getCode());
                throw new RuntimeException("预付账单号为："+paymentModel.getCode()+"，只对审核中的状态进行处理！");
            }*/
            //更新预付款单的账单状态
            paymentModel.setBillStatus(status);
            paymentModel.setModifyDate(TimeUtils.getNow());
            advancePaymentBillDao.modify(paymentModel);
            // 如果是驳回删除唯一表
            if (DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02.equals(status)) {
            	OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
    			orderExternalCodeModel.setExternalCode(paymentModel.getCode());
    			orderExternalCodeModel.setOrderSource(10);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 
    			orderExternalCodeModel = orderExternalCodeDao.searchByModel(orderExternalCodeModel);
    			List<Long>delIdList=new ArrayList<Long>();    			
    			if (orderExternalCodeModel!=null)delIdList.add(orderExternalCodeModel.getId());
    			orderExternalCodeDao.delete(delIdList);
			}

            //记录操作日志
            OperateLogModel saveLogModel = new OperateLogModel() ;
            saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_2);
            saveLogModel.setRelCode(model.getCode());
            saveLogModel.setOperater("OA审批流程");
            saveLogModel.setOperateDate(TimeUtils.getNow());
            saveLogModel.setOperateRemark(appResult.equals("0")?"OA审批：驳回":"OA审批：通过");
            saveLogModel.setOperateResult(appResult.equals("0")?"驳回":"通过");
            saveLogModel.setCreateDate(TimeUtils.getNow());
            saveLogModel.setOperateAction("OA审核");
            operateLogDao.save(saveLogModel) ;
            LOGGER.error("-----预付款单更新审批结果结束-----");
            return true;
        }

        //应付账单
        if("YFZD".equals(contractNo.getType())) {
            PaymentBillModel model = new PaymentBillModel();
            model.setCode(contractNo.getOrderCode());
            PaymentBillModel paymentBillModel=paymentBillDao.searchByModel(model);
            if(paymentBillModel==null){
                throw new RuntimeException("应付账单数据不存在,应付账单号为："+contractNo.getOrderCode());
            }
            /*//查询账单是否为待提交或审核中
            if(!(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_00.equals(paymentBillModel.getBillStatus())
                    ||DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentBillModel.getBillStatus())
            )){
                LOGGER.error("应付账单只对待提交或审核中的状态进行处理,单号:" + paymentBillModel.getCode());
                throw new RuntimeException("应付账单号为："+paymentBillModel.getCode()+"，只对审核中的状态进行处理！");
            }*/
            //更新预付款单的账单状态
            paymentBillModel.setBillStatus(status);
            paymentBillModel.setModifyDate(TimeUtils.getNow());
            paymentBillDao.modify(paymentBillModel);

            // 如果是驳回删除唯一表
            if (DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02.equals(status)) {
            	OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
    			orderExternalCodeModel.setExternalCode(paymentBillModel.getCode());
    			orderExternalCodeModel.setOrderSource(9);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 
    			orderExternalCodeModel = orderExternalCodeDao.searchByModel(orderExternalCodeModel);
    			List<Long>delIdList=new ArrayList<Long>();    			
    			if (orderExternalCodeModel!=null)delIdList.add(orderExternalCodeModel.getId());
    			orderExternalCodeDao.delete(delIdList);
			}
            //记录操作日志
            OperateLogModel saveLogModel = new OperateLogModel() ;
            saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_3);
            saveLogModel.setRelCode(model.getCode());
            saveLogModel.setOperater("OA审批流程");
            saveLogModel.setOperateDate(TimeUtils.getNow());
            saveLogModel.setOperateRemark(appResult.equals("0")?"OA审批：驳回":"OA审批：通过");
            saveLogModel.setOperateResult(appResult.equals("0")?"驳回":"通过");
            saveLogModel.setCreateDate(TimeUtils.getNow());
            saveLogModel.setOperateAction("OA审核");
            operateLogDao.save(saveLogModel) ;
            LOGGER.error("-----应付账单更新审批结果结束-----");

            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            emailUserDTO.setBuId(paymentBillModel.getBuId());
            emailUserDTO.setBuName(paymentBillModel.getBuName());
            emailUserDTO.setMerchantId(paymentBillModel.getMerchantId());
            emailUserDTO.setMerchantName(paymentBillModel.getMerchantName());
            emailUserDTO.setOrderCode(paymentBillModel.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
            emailUserDTO.setSupplier(paymentBillModel.getSupplierName());
            emailUserDTO.setCurrency(paymentBillModel.getCurrency());
            emailUserDTO.setAmount(paymentBillModel.getPayableAmount().toString());
            emailUserDTO.setStatus(appResult.equals("0")?"已驳回":"已通过");
            emailUserDTO.setCreateId(getOperateId(paymentBillModel,"新增"));
            emailUserDTO.setCancelId(getOperateId(paymentBillModel,"提交作废"));
            emailUserDTO.setAuditorId(getOperateId(paymentBillModel,"提交审核"));

            JSONObject jsonDataObject = JSONObject.fromObject(emailUserDTO) ;
            try {
                //发送邮件
                rocketMQProducer.send(jsonDataObject.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            } catch (Exception e) {
                LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
            }

            return true;
        }

        // 采购订单
        if(DERP.UNIQUE_ID_CGDH.equals(contractNo.getType())) {
            String orderCodes = contractNo.getOrderCode();
            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
            purchaseOrderDTO.setCodes(orderCodes);
            List<PurchaseOrderDTO> purchaseOrderDTOList = purchaseOrderDao.listByDTO(purchaseOrderDTO);
            if(purchaseOrderDTOList == null || purchaseOrderDTOList.isEmpty()){
                throw new RuntimeException("采购单数据不存在,采购单号为："+contractNo.getOrderCode());
            }

            if("0".equals(appResult)){
                status=DERP_ORDER.PURCHASEORDER_STATUS_001;
            }else if("1".equals(appResult)){
                status=DERP_ORDER.PURCHASEORDER_STATUS_003;
            }

            //批量更新采购单单的账单状态
            String finalStatus = status;
            purchaseOrderDTOList.forEach(entity -> {
                if("1".equals(appResult)) {
                    entity.setAuditDate(TimeUtils.getNow());
                    entity.setAuditName("OA审批");
                }
                entity.setStatus(finalStatus);
                entity.setModifyDate(TimeUtils.getNow());
                entity.setApprovalNo(oaBillCode);
            });
            if(purchaseOrderDTOList != null && purchaseOrderDTOList.size() > 0) {
                purchaseOrderDao.batchUpdate(purchaseOrderDTOList);
            }

            if(StringUtils.isNotBlank(orderCodes)) {
                String[] split = orderCodes.split(",");
                // 如果是驳回删除唯一表
                for (String code : split) {
                    if (DERP_ORDER.PURCHASEORDER_STATUS_001.equals(status)) {
                        OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
                        orderExternalCodeModel.setExternalCode(code);
                        orderExternalCodeModel.setOrderSource(11);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 11 采购单
                        orderExternalCodeModel = orderExternalCodeDao.searchByModel(orderExternalCodeModel);
                        List<Long>delIdList=new ArrayList<Long>();
                        if (orderExternalCodeModel!=null){delIdList.add(orderExternalCodeModel.getId());}
                        orderExternalCodeDao.delete(delIdList);
                    }
                }
            }

            StringBuilder codesBuilder = new StringBuilder();
            StringBuilder poNoBuilder = new StringBuilder();
            //记录操作日志
            for (PurchaseOrderDTO entity : purchaseOrderDTOList) {
                if(codesBuilder != null && codesBuilder.length() > 0) {
                    codesBuilder.append(",").append(entity.getCode());
                }else {
                    codesBuilder.append(entity.getCode());
                }
                if(poNoBuilder != null && poNoBuilder.length() > 0) {
                    poNoBuilder.append(",").append(entity.getPoNo());
                }else {
                    poNoBuilder.append(entity.getPoNo());
                }

                OperateLogModel saveLogModel = new OperateLogModel() ;
                saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_1);
                saveLogModel.setRelCode(entity.getCode());
                saveLogModel.setOperater("OA审批流程");
                saveLogModel.setOperateDate(TimeUtils.getNow());
                saveLogModel.setOperateRemark(appResult.equals("0")?"OA审批：驳回":"OA审批：通过");
                saveLogModel.setOperateResult(appResult.equals("0")?"驳回":"通过");
                saveLogModel.setCreateDate(TimeUtils.getNow());
                saveLogModel.setOperateAction("OA审核");
                operateLogDao.save(saveLogModel) ;
                LOGGER.error("-----采购单更新审批结果结束-----");
            };

            PurchaseOrderDTO purchaseOrderDTO1 = purchaseOrderDTOList.get(0);
            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            emailUserDTO.setBuId(purchaseOrderDTO1.getBuId());
            emailUserDTO.setBuName(purchaseOrderDTO1.getBuName());
            emailUserDTO.setMerchantId(purchaseOrderDTO1.getMerchantId());
            emailUserDTO.setMerchantName(purchaseOrderDTO1.getMerchantName());
            emailUserDTO.setOrderCode(codesBuilder.toString());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
            emailUserDTO.setSupplier(purchaseOrderDTO1.getSupplierName());
            emailUserDTO.setCurrency(purchaseOrderDTO1.getCurrency());
            emailUserDTO.setPoNum(poNoBuilder.toString());
            emailUserDTO.setStatus(appResult.equals("0")?"已驳回":"已通过");
            emailUserDTO.setUserId(Arrays.asList(String.valueOf(purchaseOrderDTO1.getSubmiter())));
            emailUserDTO.setUserName("OA审批流程");
            emailUserDTO.setSubmitId(purchaseOrderDTO1.getSubmiter()!=null?Arrays.asList(String.valueOf(purchaseOrderDTO1.getSubmiter())):null);
            String auditMethod = DERP_ORDER.PURCHASEORDER_AUDITMETHOD_1;
            String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
            emailUserDTO.setAuditMethod(auditMethodLabel);

            emailUserDTO.setAuditorId(getOperateId(codesBuilder.toString(),"提交OA审核"));

            JSONObject jsonDataObject = JSONObject.fromObject(emailUserDTO) ;
            try {
                //发送邮件
                rocketMQProducer.send(jsonDataObject.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            } catch (Exception e) {
                LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
            }

            return true;
        }

        return false;
    }

    private Long getOperateId(PaymentBillModel paymentModel,String msg) throws SQLException {
        OperateLogModel operateLogModel=new OperateLogModel();
        operateLogModel.setModule( DERP_ORDER.OPERATE_LOG_MODULE_3);
        operateLogModel.setRelCode(paymentModel.getCode());
        operateLogModel.setOperateAction(msg);

        OperateLogDTO dto=new OperateLogDTO();

        List<OperateLogModel> logList = operateLogDao.list(operateLogModel);
        List<OperateLogDTO> logModels = new ArrayList<OperateLogDTO>() ;

        for (OperateLogModel logModel : logList) {

            OperateLogDTO logDto = new OperateLogDTO() ;

            BeanUtils.copyProperties(logModel, logDto);

            logModels.add(logDto) ;
        }

        logModels = logModels.stream().sorted(Comparator.comparing(OperateLogDTO::getOperateDate).reversed()).collect(Collectors.toList()) ;
        if(logModels.size() > 0){
            dto = logModels.get(0);
        }
        return dto.getOperateId();
    }

    private Long getOperateId(String code,String msg) throws SQLException {
        OperateLogModel operateLogModel=new OperateLogModel();
        operateLogModel.setModule( DERP_ORDER.OPERATE_LOG_MODULE_3);
        operateLogModel.setRelCode(code);
        operateLogModel.setOperateAction(msg);

        OperateLogDTO dto=new OperateLogDTO();

        List<OperateLogModel> logList = operateLogDao.list(operateLogModel);
        List<OperateLogDTO> logModels = new ArrayList<OperateLogDTO>() ;

        for (OperateLogModel logModel : logList) {

            OperateLogDTO logDto = new OperateLogDTO() ;

            BeanUtils.copyProperties(logModel, logDto);

            logModels.add(logDto) ;
        }

        logModels = logModels.stream().sorted(Comparator.comparing(OperateLogDTO::getOperateDate).reversed()).collect(Collectors.toList()) ;
        if(logModels.size() > 0){
            dto = logModels.get(0);
        }
        return dto.getOperateId();
    }
}
