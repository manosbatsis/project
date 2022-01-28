package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.ReceiveCloseAccountsDao;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.entity.vo.bill.ReceiveCloseAccountsModel;
import com.topideal.order.service.bill.ReceiveCloseAccountsService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.webapi.bill.form.ReceiveCloseAccountVerifyMonthForm;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:43
 * @Description: 应收关账
 */
@Service
public class ReceiveCloseAccountsServiceImpl implements ReceiveCloseAccountsService {

    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;

    @Autowired
    private CommonBusinessService commonBusinessService;
    @Autowired
    private RMQProducer rocketMQProducer;

    //关账
    private static final String OPERATION_TYPE_0 = "0";
    //反关账
    private static final String OPERATION_TYPE_1 = "1";

    @Override
    public ResponseBean getVerifyMonth(User user, ReceiveCloseAccountVerifyMonthForm form) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //yyyy-MM-dd
        String receiveDateStr = form.getReceiveDateStr();

        ReceiveCloseAccountsModel model = new ReceiveCloseAccountsModel();
        model.setMonth(TimeUtils.formatStrToStr(receiveDateStr, "yyyy-MM-dd", "yyyy-MM"));
        model.setBuId(form.getBuId());
        model.setMerchantId(user.getMerchantId());
        model.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
        ReceiveCloseAccountsModel lastestModel = receiveCloseAccountsDao.getLatestByModel(model);

        if(lastestModel == null) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "该主体和事业部下未发现相关关账记录");
        }

        resultMap.put("verifyMonth", lastestModel.getMonth());

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
    }

    @Override
    public ReceiveCloseAccountsDTO listDTOByPage(User user, ReceiveCloseAccountsDTO dto) {
        return receiveCloseAccountsDao.listDTOByPage(dto);
    }

    @Override
    public ResponseBean updateAccountStatus(User user, ReceiveCloseAccountsDTO dto, String operationType) throws Exception {

        ReceiveCloseAccountsModel receiveCloseAccountsModel = receiveCloseAccountsDao.searchById(dto.getId());
        if(receiveCloseAccountsModel == null) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "未发现相关关账记录");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", receiveCloseAccountsModel.getMerchantId());
        param.put("buId", receiveCloseAccountsModel.getBuId());
        param.put("excludeId", receiveCloseAccountsModel.getId());
        if(operationType.equals(OPERATION_TYPE_0)) {
            // 关账逻辑
            //1.当关帐状态不为“未关帐”时不能操作,返回提示
            if(!StringUtils.equals(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029, receiveCloseAccountsModel.getStatus())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "当前账单不为未关帐状态,不允许关账");
            }

            //2.点击关帐时校验往前最近一个月的应收月份是否为已关帐，若往前最近一个月份的状态为未关帐则报错提示；
            param.put("beforeMonth", receiveCloseAccountsModel.getMonth());
            ReceiveCloseAccountsDTO latestByDTO = receiveCloseAccountsDao.getLatestExcludeIdByMap(param);

            //若往前最近一个月份的状态为未关帐则报错提示
            if(latestByDTO != null && StringUtils.equals(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029, latestByDTO.getStatus())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "上个月未关账,不允许关账");
            }

            param.clear();
            param.put("targetStatus", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
            param.put("sourceStatus", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
            param.put("modifyDate", TimeUtils.getNow());
            param.put("id", receiveCloseAccountsModel.getId());
            int updateRow = receiveCloseAccountsDao.updateStatusByMap(param);
            if(updateRow <= 0) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "更新关账状态失败");
            }

        }else if(operationType.equals(OPERATION_TYPE_1)){
            // 反关账逻辑
            //1.当关帐状态不为“已关帐”时不能操作,返回提示
            if(!StringUtils.equals(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030, receiveCloseAccountsModel.getStatus())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "当前账单不为已关帐状态,不允许反关账");
            }

            //2.点击反关帐时校验往后最近一个月的应收月份是否为已关帐，若往后最近一个月份的状态为已关帐则报错提示；
            param.put("afterMonth", receiveCloseAccountsModel.getMonth());
            param.put("orderbyMonth", "ASC");
            ReceiveCloseAccountsDTO latestByDTO = receiveCloseAccountsDao.getLatestExcludeIdByMap(param);

            //若往后最近一个月份的状态为已关帐则报错提示
            if(latestByDTO != null && StringUtils.equals(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030, latestByDTO.getStatus())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "下个月已关账,不允许反关账");
            }

            param.clear();
            param.put("id", receiveCloseAccountsModel.getId());
            param.put("targetStatus", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
            param.put("sourceStatus", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
            param.put("modifyDate", TimeUtils.getNow());
            int updateRow = receiveCloseAccountsDao.updateStatusByMap(param);
            if(updateRow <= 0) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), "更新关账状态失败");
            }

        }else {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018);
        }

        String opertionAction = operationType.equals(OPERATION_TYPE_0) ? "关账" : "反关账";
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_20, String.valueOf(dto.getId()), opertionAction, null, null);


        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @Override
    public boolean validateIsClose(User user, String month) throws Exception {

        ReceiveCloseAccountsModel receiveCloseAccountsModel = new ReceiveCloseAccountsModel();
        receiveCloseAccountsModel.setMonth(month);
        receiveCloseAccountsModel.setMerchantId(user.getMerchantId());
        receiveCloseAccountsModel.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);

        List<ReceiveCloseAccountsModel> receiveCloseAccountsModels = receiveCloseAccountsDao.list(receiveCloseAccountsModel);

        if (receiveCloseAccountsModels.size() > 0) {
            return false;
        }

        return true;
    }

    @Override
    public Map<String, Object> refresh(User user, String month) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("month", month);
        param.put("merchantId", user.getMerchantId());
        JSONObject json = JSONObject.fromObject(param);

        rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_GENERATE_RECEIVE_CLOSE_ACCOUNT.getTopic(), MQOrderEnum.TIMER_GENERATE_RECEIVE_CLOSE_ACCOUNT.getTags());
        resultMap.put("code", "00");
        resultMap.put("message", "正在刷新");
        return resultMap;
    }

}
