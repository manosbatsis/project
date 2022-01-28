package com.topideal.service.timer.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc14.BillVoucherStatusQueryRoot;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.service.timer.ReceiveBillNcVoucherBackfillService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiveBillNcVoucherBackfillServiceImpl implements ReceiveBillNcVoucherBackfillService {

    private static final Logger LOG = Logger.getLogger(ReceiveBillNcVoucherBackfillServiceImpl.class) ;

    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private PaymentBillDao paymentBillDao ;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201169900,model=DERP_LOG_POINT.POINT_13201169900_Label)
    public void saveReceiveBillNcVoucherBackfill(String json, String keys, String topics, String tags) throws SQLException {
        JSONObject jsonData = JSONObject.fromObject(json);
        //判断触发来源：1-手动触发
        String dataSource = (String)jsonData.get("datasource");
        BillVoucherStatusQueryRoot root = new BillVoucherStatusQueryRoot() ;

        //手动触发
        if (org.apache.commons.lang.StringUtils.isNotBlank(dataSource) && "1".equals(dataSource)) {
            String billCodes = (String) jsonData.get("billCodes");
            root.setBillId(billCodes);
        } else { //定时器触发
            //1.应收单当前账单状态不为”已作废“，且NC应收状态为“已入账”、“已关账”的应收单需查询接口状态；当该应收单已查询到凭证信息则不再继续获取
            //2.应收单当前账单状态为”已作废“，且NC应收状态为“已入账”、“已关账”、“已作废”、“已红冲”的应收单需查询接口状态
            List<ReceiveBillModel> ncVoucherBackFillList = receiveBillDao.getNcVoucherFillBackList();
            
            List<PaymentBillModel> ncVoucherBackPaymentFillList = paymentBillDao.getNcVoucherFillBackList() ;
            
            if(ncVoucherBackFillList.isEmpty()
            		&& ncVoucherBackPaymentFillList.isEmpty()) {
                return ;
            }
            
            List<String> receiveCodeList = new ArrayList<String>() ;
            
            for (ReceiveBillModel tempModel : ncVoucherBackFillList) {
                receiveCodeList.add(tempModel.getCode()) ;
            }
            
            for (PaymentBillModel tempModel : ncVoucherBackPaymentFillList) {
                receiveCodeList.add(tempModel.getCode()) ;
            }
            
            String billCodes = StringUtils.join(receiveCodeList.toArray(), ",");
            root.setBillId(billCodes);
        }

        root.setSourceCode(ApolloUtil.ncSourceType);

        JSONObject requestJson = JSONObject.fromObject(root);

        /**请求NC4.14 凭证查询接口*/
        String result = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_14.getUri(), requestJson.toString());

        JSONObject resultJson = JSONObject.fromObject(result);
        if (resultJson.getString("code").equals("1002")) {
            throw new RuntimeException(resultJson.getString("msg"));
        }
        JSONArray dataArray = resultJson.getJSONArray("rsadata");

        for (Object object : dataArray) {
        	
            JSONObject voucherBillJson = JSONObject.fromObject(object) ;

            String billId = voucherBillJson.getString("billId");
            
            if(billId.contains(DERP.UNIQUEID_PREFIX_YSZD)) {
            	ReceiveBillModel queryModel = new ReceiveBillModel() ;
                queryModel.setCode(billId);

                queryModel = receiveBillDao.searchByModel(queryModel) ;

                if(queryModel == null) {
                    LOG.info("经分销系统查询无此结算单据");
                    continue ;
                }

                ReceiveBillModel updateModel = new ReceiveBillModel() ;
                updateModel.setId(queryModel.getId());
                updateModel.setVoucherStatus(voucherBillJson.getString("state"));

                if(voucherBillJson.get("voucherCode") != null) {
                    updateModel.setVoucherCode(voucherBillJson.getString("voucherCode"));
                }

                if(voucherBillJson.get("voucherName") != null) {
                    updateModel.setVoucherName(voucherBillJson.getString("voucherName"));
                }

                if(voucherBillJson.get("voucherIndex") != null) {
                    updateModel.setVoucherIndex(voucherBillJson.getString("voucherIndex"));
                }

                if(voucherBillJson.get("period") != null) {
                    updateModel.setPeriod(voucherBillJson.getString("period"));
                }

                updateModel.setModifyDate(TimeUtils.getNow());

                receiveBillDao.modify(updateModel) ;
                
            }else if(billId.contains(DERP.UNIQUE_ID_YFZD)) {
            	
            	PaymentBillModel queryModel = new PaymentBillModel() ;
                queryModel.setCode(billId);

                queryModel = paymentBillDao.searchByModel(queryModel) ;

                if(queryModel == null) {
                    LOG.info("经分销系统查询无此结算单据");
                    continue ;
                }

                PaymentBillModel updateModel = new PaymentBillModel() ;
                updateModel.setId(queryModel.getId());
                updateModel.setVoucherStatus(voucherBillJson.getString("state"));

                if(voucherBillJson.get("voucherCode") != null) {
                    updateModel.setVoucherCode(voucherBillJson.getString("voucherCode"));
                }

                if(voucherBillJson.get("voucherName") != null) {
                    updateModel.setVoucherName(voucherBillJson.getString("voucherName"));
                }

                if(voucherBillJson.get("voucherIndex") != null) {
                    updateModel.setVoucherIndex(voucherBillJson.getString("voucherIndex"));
                }
                
                if(voucherBillJson.get("period") != null) {
                    updateModel.setPeriod(voucherBillJson.getString("period"));
                }

                updateModel.setModifyDate(TimeUtils.getNow());

                paymentBillDao.modify(updateModel) ;
            	
            }

        }

    }
}
