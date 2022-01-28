package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.transfer.TransferInDepotDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.timer.InvailOrderDataSendAlertEmailService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.smurfs.tools.gateway.client.GatewayHttpClient;
import priv.smurfs.tools.gateway.common.ApiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/8 17:59
 * @Description:
 */
@Service
public class InvailOrderDataSendAlertEmailServiceImpl implements InvailOrderDataSendAlertEmailService {
    private static final Logger LOGGER = Logger.getLogger(InvailOrderDataSendAlertEmailServiceImpl.class);

    @Autowired
    private TransferOrderDao transferOrderDao;
    @Autowired
    private TransferInDepotDao transferInDepotDao;
    @Autowired
    private TransferOutDepotDao transferOutDepotDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public void checkTransferDataAndSendAlertEmail(JSONObject jsonData, String keys, String topics, String tags) throws Exception {
        // 起始时间
        String startDate = (String) jsonData.get("startDate");
        // 结束时间
        String endDate = (String) jsonData.get("endDate");

        if(StringUtils.isBlank(startDate)) {
            startDate = TimeUtils.getYesterday();
        }

        TransferInDepotDTO transferInDepotDTO = new TransferInDepotDTO();
        transferInDepotDTO.setTransferStartDate(startDate);
        transferInDepotDTO.setTransferEndDate(endDate);
//        List<TransferInDepotDTO> inList  = transferInDepotDao.listDTOByDTO(transferInDepotDTO);
        List<TransferInDepotDTO> inList = transferInDepotDao.listInvailDTOByDTO(transferInDepotDTO);

        TransferOutDepotDTO transferOutDepotDTO = new TransferOutDepotDTO();
        transferOutDepotDTO.setTransferStartDate(startDate);
        transferOutDepotDTO.setTransferEndDate(endDate);
//        List<TransferOutDepotDTO> outList = transferOutDepotDao.listDTObyDTO(transferOutDepotDTO);
        List<TransferOutDepotDTO> outList = transferOutDepotDao.listInvailDTOByDTO(transferOutDepotDTO);

        List<Map<String, Object>> listMap = new ArrayList<>();
        outList.stream().forEach(entity -> {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("transferId", entity.getTransferOrderId() == null ? "" : entity.getTransferOrderId() + "");
            resultMap.put("transferOrderCode", entity.getTransferOrderCode());
            resultMap.put("inDepotId", entity.getInDepotId() == null ? "" : entity.getInDepotId() + "");
            resultMap.put("inDepotName", entity.getInDepotName());
            resultMap.put("outDepotId", entity.getOutDepotId() == null ? "" : entity.getOutDepotId() + "");
            resultMap.put("outDepotName", entity.getOutDepotName());
            resultMap.put("merchantName", entity.getMerchantName());
            resultMap.put("remark", "调拨出库单与调拨单差异");
            listMap.add(resultMap);
        });

        inList.stream().forEach(entity -> {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("transferId", entity.getTransferOrderId() == null ? "" : entity.getTransferOrderId() + "");
            resultMap.put("transferOrderCode", entity.getTransferOrderCode());
            resultMap.put("inDepotId", entity.getInDepotId() == null ? "" : entity.getInDepotId() + "");
            resultMap.put("inDepotName", entity.getInDepotName());
            resultMap.put("outDepotId", entity.getOutDepotId() == null ? "" : entity.getOutDepotId() + "");
            resultMap.put("outDepotName", entity.getOutDepotName());
            resultMap.put("merchantName", entity.getMerchantName());
            resultMap.put("remark", "调拨入库单与调拨单差异");
            listMap.add(resultMap);
        });

        if(listMap != null && listMap.size() > 0) {
            sendEmail(listMap, ApolloUtil.internalAlertEmail, SmurfsAPICodeEnum.EMAIL_W036.getCode());
        }
    }

    @Override
    public void checkOrderDataAndSendAlertEmail(JSONObject jsonData, String keys, String topics, String tags) throws Exception {
        // 起始时间
        String startDate = (String) jsonData.get("startDate");
        // 结束时间
        String endDate = (String) jsonData.get("endDate");

        if(StringUtils.isBlank(startDate)) {
            startDate = TimeUtils.getYesterday();
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTradingStartDate(startDate);
        orderDTO.setTradingEndDate(endDate);
        int countNum = orderDao.queryInvailDtoListCount(orderDTO);
        List<Map<String, Object>> listMap = new ArrayList<>();
        Integer pageSize = 2000;
        for (int i = 0; i < countNum; ) {
            listMap.clear();
            int pageSub = (i + pageSize) < countNum ? (i + pageSize) : countNum;
            orderDTO.setBegin(i);
            orderDTO.setPageSize(pageSize);

            OrderDTO orderDTOResult = orderDao.listInvailDTOByPage(orderDTO);
            List<OrderDTO> list = orderDTOResult.getList();
            i = pageSub;

            list.stream().forEach(entity -> {
                Map<String, Object> resultMap = new HashMap<>(6);
                resultMap.put("orderId", entity.getId() == null ? "": entity.getId() + "");
                resultMap.put("merchantName", entity.getMerchantName());
                resultMap.put("externalCode", entity.getExternalCode());
                resultMap.put("tradingDate", entity.getTradingDate() == null ? "" : TimeUtils.format(entity.getTradingDate(), "yyyy-MM-dd HH:mm:ss"));
                resultMap.put("deliverDate", entity.getDeliverDate() == null ? "" : TimeUtils.format(entity.getDeliverDate(), "yyyy-MM-dd HH:mm:ss"));
                resultMap.put("remark", "电商订单的发货时间比交易时间早");
                listMap.add(resultMap);
            });

            if(listMap != null && listMap.size() > 0) {
                sendEmail(listMap, ApolloUtil.internalAlertEmail, SmurfsAPICodeEnum.EMAIL_W037.getCode());
            }

            list.clear();
        }
    }

    @Override
    @SystemServiceLog(point=DERP_LOG_POINT.POINT_20100000004,model=DERP_LOG_POINT.POINT_20100000004_Label)
    public void checkDataAndSendAlertEmail(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String type = (String) jsonData.get("type");
        if(StringUtils.equals(type, "a")) {
            checkTransferDataAndSendAlertEmail(jsonData, keys, topics, tags);
        }else if(StringUtils.equals(type, "b")) {
            checkOrderDataAndSendAlertEmail(jsonData, keys, topics, tags);
        }else {
            checkTransferDataAndSendAlertEmail(jsonData, keys, topics, tags);
            checkOrderDataAndSendAlertEmail(jsonData, keys, topics, tags);
        }
    }

    private void sendEmail(Object obj, String email, String mailCode){
        JSONObject paramJson=new JSONObject();
        paramJson.put("attArray", obj);

        LOGGER.info("参数："+paramJson.toString());

        // 推送内容
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paramJson", paramJson);
        jsonObject.put("mailCode", mailCode);
        jsonObject.put("recipients", email);

        // 调用外部接口发送邮件
        String resultMsg = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
//        String resultMsg = sendSc1(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL);
        LOGGER.info("蓝精灵返回结果："+resultMsg);
        LOGGER.info("-----------------发送邮件提醒结束----------------------");
    }


    @SuppressWarnings("static-access")
    public String sendSc1(JSONObject json, SmurfsAPIEnum apiEnum){
        try {
            GatewayHttpClient gatewayHttpClient =
                    GatewayHttpClient.newInstance("gateway.smurfs.topideal.mobi", "10002", "D8478AA375024F218902E72B04D33F58");
            //发送请求
            ApiResponse response =gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
            String resultJson = response.getResultJson();
            return resultJson;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
