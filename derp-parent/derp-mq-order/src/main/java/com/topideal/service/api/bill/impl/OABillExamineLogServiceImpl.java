package com.topideal.service.api.bill.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AdvancePaymentBillDao;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.json.api.v7_1.OAExamineLogJson;
import com.topideal.mongo.dao.ContractNoMongoDao;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.service.api.bill.OABillExamineLogService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OABillExamineLogServiceImpl implements OABillExamineLogService {

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

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_12208000000, model = DERP_LOG_POINT.POINT_12208000000_Label,keyword="requestId")
    public boolean insertExamineLog(String json, String keys, String topics, String tags) throws Exception {
        //实例化json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        //JSON对象转实体
        OAExamineLogJson examineLog=(OAExamineLogJson) JSONObject.toBean(jsonData, OAExamineLogJson.class);
        if(examineLog==null){
            throw new RuntimeException("参数为空");
        }
        //获取审批处理时间
        String operateStr=examineLog.getOperateDate()+" "+examineLog.getOperateTime();

        //根据流程id查询账单是否存在
        Map<String, Object> contractNoParams = new HashMap<>();
        contractNoParams.put("contractNo", examineLog.getRequestId());
        List<ContractNoMongo> contractNoMongoList = contractNoMongoDao.findAll(contractNoParams);
        if(contractNoMongoList== null||contractNoMongoList.size() <=0){
            throw new RuntimeException("流程id为："+examineLog.getRequestId()+"对应的账单数据不存在");
        }
        ContractNoMongo contractNo=contractNoMongoList.get(0);

        //判断账单来源是预付还是应付
        if("YFKD".equals(contractNo.getType())){//预付款单
            //查询对应的预付款单是否存在
            AdvancePaymentBillModel model=new AdvancePaymentBillModel();
            model.setCode(contractNo.getOrderCode());
            AdvancePaymentBillModel paymentModel=advancePaymentBillDao.searchByModel(model);
            if(paymentModel==null){
                throw new RuntimeException("预付款单号为："+contractNo.getOrderCode()+"对应的预预付款单数据不存在");
            }
            /*//查询账单是否为待提交或审核中
            if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00.equals(paymentModel.getBillStatus())
                    ||DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())
            )){
                LOGGER.error("预付款单只对审核中的状态进行处理,单号:" + paymentModel.getCode());
                throw new RuntimeException("预付账单号为："+paymentModel.getCode()+"，只对审核中的状态进行处理！");
            }*/
            /**记录OA操作日志*/
            OperateLogModel saveLogModel = new OperateLogModel() ;
            saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_2);
            saveLogModel.setRelCode(model.getCode());
            saveLogModel.setOperater(examineLog.getPsnName());
            saveLogModel.setOperaterDepot(examineLog.getJobTitleName());
            saveLogModel.setOperateDate(TimeUtils.parse(operateStr,"yyyy-MM-dd HH:mm:ss"));
            saveLogModel.setOperateRemark(checkStr(examineLog.getRemark()));
            if(examineLog.getLogType().equals("0")){
                saveLogModel.setOperateResult("批准");
            }else if(examineLog.getLogType().equals("3")){
                saveLogModel.setOperateResult("退回");
            }
            saveLogModel.setOperateAction("OA审核");
            saveLogModel.setCreateDate(TimeUtils.getNow());
            operateLogDao.save(saveLogModel) ;
            LOGGER.error("-----预付款单更新审批日志结束-----");
            return true;
        }
        //应付
        if("YFZD".equals(contractNo.getType())){
            PaymentBillModel model = new PaymentBillModel();
            model.setCode(contractNo.getOrderCode());
            PaymentBillModel paymentBillModel=paymentBillDao.searchByModel(model);
            if(paymentBillModel==null){
                throw new RuntimeException("应付账单数据不存在,应付账单号为："+contractNo.getOrderCode());
            }
            //查询账单是否为待提交或审核中
            /*if(!(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_00.equals(paymentBillModel.getBillStatus())
                    ||DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentBillModel.getBillStatus())
            )){
                LOGGER.error("应付账单只对待提交或审核中的状态进行处理,单号:" + paymentBillModel.getCode());
                throw new RuntimeException("应付账单号为："+paymentBillModel.getCode()+"，只对审核中的状态进行处理！");
            }*/
            /**记录OA操作日志*/
            OperateLogModel saveLogModel = new OperateLogModel() ;
            saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_3);
            saveLogModel.setRelCode(model.getCode());
            saveLogModel.setOperater(examineLog.getPsnName());
            saveLogModel.setOperaterDepot(examineLog.getJobTitleName());
            saveLogModel.setOperateDate(TimeUtils.parse(operateStr,"yyyy-MM-dd HH:mm:ss"));
            saveLogModel.setOperateRemark(checkStr(examineLog.getRemark()));
            saveLogModel.setOperateResult(examineLog.getLogType().equals("0")?"批准":"退回");
            saveLogModel.setOperateAction("OA审核");
            saveLogModel.setCreateDate(TimeUtils.getNow());
            operateLogDao.save(saveLogModel) ;
            LOGGER.error("-----应付账单更新审批日志结束-----");
            return true;
        }
        return false;
    }

    /**
     * 过滤html
     * @param str
     * @return
     */
    private String checkStr(String str){
        String strContent ="";
           if(StringUtils.isNotBlank(str)){
               String t=str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
               String replace = t.replace("\n", "");
               strContent = replace.replace(" ", "");
           }
        return strContent;
    }
}
