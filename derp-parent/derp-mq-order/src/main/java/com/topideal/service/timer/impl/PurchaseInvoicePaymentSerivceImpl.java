package com.topideal.service.timer.impl;

import com.google.gson.JsonObject;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseInvoiceDao;
import com.topideal.dao.purchase.PurchaseInvoiceItemDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDataDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceItemModel;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.service.timer.PurchaseInvoicePaymentSerivce;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author huangrenya
 **/
@Service
public class PurchaseInvoicePaymentSerivceImpl implements PurchaseInvoicePaymentSerivce {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseInvoicePaymentSerivceImpl.class);

    @Autowired
    private PurchaseOrderDao purchaseOrderDao ;
    @Autowired
    private PurchaseOrderItemDao purchaseOrderItemDao;
    @Autowired
    private PurchaseInvoiceDao purchaseInvoiceDao;
    @Autowired
    private PurchaseInvoiceItemDao purchaseInvoiceItemDao;
    @Autowired
    private RMQProducer rocketMQProducer;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201110003,model=DERP_LOG_POINT.POINT_13201110003_Label)
    public void savePurchaseInvoicePayment(String json, String keys, String topics, String tags) throws Exception {


        /*查询采购单：存在预计付款日期在当前日期前后7天内（比如：若当前日期为：8月13日，按当期日期加减7天内，采购订单号不存在应付单明细中，应付单排除已删除状态*/
        List<PurchaseOrderModel> listOrder=purchaseOrderDao.getPurchaseOrderByPayDate();

        /*查询采购发票：存在预计付款日期在当前日期前后7天内（比如：若当前日期为：8月13日，按当期日期加减7天内）,发票列表的采购订单号不存在应付单明细中，应付单排除已删除状态*/
        List<PurchaseInvoiceModel> invoiceList=purchaseInvoiceDao.getPurchaseInvoiceByPayDate();

        //存储采购信息
        Map<String, JSONArray> emailDataOrderMap=new HashMap<>();
        //存储商家+事业部名
        Map<String,String> mapOrder=new HashMap<>();


        for(PurchaseOrderModel purchaseOrderModel:listOrder){
            JSONArray array=new JSONArray();

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("currency", purchaseOrderModel.getCurrency()==null?" ":purchaseOrderModel.getCurrency());
            jsonObject.put("buName", purchaseOrderModel.getBuName()==null?" ":purchaseOrderModel.getBuName());
            jsonObject.put("supplier", purchaseOrderModel.getSupplierName()==null?" ":purchaseOrderModel.getSupplierName());
            jsonObject.put("date", TimeUtils.formatDay(purchaseOrderModel.getPaymentDate()));
            jsonObject.put("orderCode",purchaseOrderModel.getCode()==null?" ":purchaseOrderModel.getCode());
            jsonObject.put("poNum",purchaseOrderModel.getPoNo()==null?" ":purchaseOrderModel.getPoNo());
            jsonObject.put("merchantName",purchaseOrderModel.getMerchantName());
            jsonObject.put("merchantId",purchaseOrderModel.getMerchantId());
            jsonObject.put("buId",purchaseOrderModel.getBuId());
            jsonObject.put("invoiceNo"," ");

            //根据商家+事业部的维度进行汇总
            String str=purchaseOrderModel.getMerchantId()+"_"+purchaseOrderModel.getBuId();

            //查询采购订单含税总金额
            BigDecimal amount=new BigDecimal(0);
            List<PurchaseOrderItemModel> itemModel=purchaseOrderItemDao.getByOrderIds(Arrays.asList(purchaseOrderModel.getId()));
            for(PurchaseOrderItemModel model:itemModel){
                amount=amount.add(model.getTaxAmount()==null?new BigDecimal(0):model.getTaxAmount());
            }
            jsonObject.put("amount",String.valueOf(amount));
            array.add(jsonObject);

            if(emailDataOrderMap.containsKey(str)){
                JSONArray jsonA=emailDataOrderMap.get(str);
                jsonA.add(jsonObject);
                emailDataOrderMap.put(str,jsonA);
            }else{
                emailDataOrderMap.put(str,array);
            }
            mapOrder.put(str,purchaseOrderModel.getMerchantId()+"_"+purchaseOrderModel.getBuId()+"_"+purchaseOrderModel.getMerchantName());
        }


        for(PurchaseInvoiceModel invoice:invoiceList){
            JSONArray array=new JSONArray();

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("currency", invoice.getCurrency()==null?" ":invoice.getCurrency());
            jsonObject.put("buName", invoice.getBuName()==null?" ":invoice.getBuName());
            jsonObject.put("supplier", invoice.getSupplierName()==null?" ":invoice.getSupplierName());
            jsonObject.put("date", TimeUtils.formatDay(invoice.getPaymentDate()));
            jsonObject.put("orderCode",invoice.getPurchaseOrderCode()==null?" ":invoice.getPurchaseOrderCode());
            jsonObject.put("poNum",invoice.getPoNo()==null?" ":invoice.getPoNo());
            jsonObject.put("merchantName",invoice.getMerchantName());
            jsonObject.put("merchantId",invoice.getMerchantId());
            jsonObject.put("buId",invoice.getBuId());
            jsonObject.put("invoiceNo",invoice.getInvoiceNo()==null?" ":invoice.getInvoiceNo());

            //根据发票id查询表体的发票含税金额
            Map<String,Object> queryMap=new HashMap();
            queryMap.put("id",invoice.getId());
            PurchaseInvoiceItemModel itemModel=purchaseInvoiceItemDao.getInvoiceNum(queryMap);
            BigDecimal amount=itemModel.getTaxAmount()==null?new BigDecimal(0):itemModel.getTaxAmount();
            jsonObject.put("amount",String.valueOf(amount));
            array.add(jsonObject);

            //根据商家+事业部的维度进行汇总
            String str=invoice.getMerchantId()+"_"+invoice.getBuId();

            if(emailDataOrderMap.containsKey(str)){
                JSONArray jsonA=emailDataOrderMap.get(str);
                jsonA.add(jsonObject);
                emailDataOrderMap.put(str,jsonA);
            }else{
                emailDataOrderMap.put(str,array);
            }
            mapOrder.put(str,invoice.getMerchantId()+"_"+invoice.getBuId()+"_"+invoice.getMerchantName());
        }

        if(emailDataOrderMap.size()>0) {
            for (String key : emailDataOrderMap.keySet()) {
                JSONArray jsonArray = emailDataOrderMap.get(key);

                String merchantName = mapOrder.get(key).split("_")[2].toString();
                Long merchantId = Long.valueOf(mapOrder.get(key).split("_")[0].toString());
                Long buId = Long.valueOf(mapOrder.get(key).split("_")[1].toString());

                ReminderEmailUserDTO emailDto = new ReminderEmailUserDTO();
                emailDto.setMerchantName(merchantName);
                emailDto.setMerchantId(merchantId);
                emailDto.setBuId(buId);
                emailDto.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
                emailDto.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_18);

                emailDto.setAttArray(jsonArray);
                if (jsonArray == null || jsonArray.size() == 0) {
                    continue;
                }
                JSONObject jSONObject = JSONObject.fromObject(emailDto);
                LOGGER.info("---------------------采购单预计付款提醒发送消息报文----------------" + jSONObject);
                try {
                    rocketMQProducer.send(jSONObject.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("采购单预计付款提醒发送消息异常");
                }
            }
        }

    }
}
