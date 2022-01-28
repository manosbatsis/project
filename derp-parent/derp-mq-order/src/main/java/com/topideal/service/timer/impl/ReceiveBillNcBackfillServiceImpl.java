package com.topideal.service.timer.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc11.BillStatusQueryRoot;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.service.timer.ReceiveBillNcBackfillService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ReceiveBillNcBackfillServiceImpl implements ReceiveBillNcBackfillService {

	private static final Logger LOG = Logger.getLogger(ReceiveBillNcBackfillServiceImpl.class) ;
	
	@Autowired
	private ReceiveBillDao receiveBillDao ;
	@Autowired
	private PaymentBillDao paymentBillDao ;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201169400,model=DERP_LOG_POINT.POINT_13201169400_Label)
	public void saveReceiveBillNcBackfill(String json, String keys, String topics, String tags) throws SQLException {
		
		/**查询状态为：已同步/待审核/待入erp/待入账/已入账的应收账单*/
		List<ReceiveBillModel> backFillList = receiveBillDao.getNcBackfillList() ;
		/**查询应付账单*/
		List<PaymentBillModel> backFillPaymentList = paymentBillDao.getNcBackfillList() ;
		
		if(backFillList.isEmpty()
				&& backFillPaymentList.isEmpty()) {
			return ;
		}
		
		List<String> receiveCodeList = new ArrayList<String>() ;
		
		for (ReceiveBillModel tempModel : backFillList) {
			receiveCodeList.add(tempModel.getCode()) ;
		}
		
		for (PaymentBillModel paymentBillModel : backFillPaymentList) {
			receiveCodeList.add(paymentBillModel.getCode()) ;
		}
		
		String[] codeArr = receiveCodeList.toArray(new String[] {});
		
		BillStatusQueryRoot root = new BillStatusQueryRoot() ;
		root.setBillType("2");
		root.setSourceCode(ApolloUtil.ncSourceType);
		root.setConfirmBillId(Arrays.toString(codeArr));
		
		JSONObject requestJson = JSONObject.fromObject(root);
		
		/**请求NC 4.11账单状态查询接口*/
		String result = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_11.getUri(), requestJson.toString());
		
		JSONObject resultJson = JSONObject.fromObject(result);
		JSONArray dataArray = resultJson.getJSONArray("rsadata");
		
		for (Object object : dataArray) {
			
			JSONObject comfirmBillJson = JSONObject.fromObject(object) ;
			
			String confirmBillId = comfirmBillJson.getString("confirmBillId");
			
			if(confirmBillId.contains(DERP.UNIQUEID_PREFIX_YSZD)) {
				
				ReceiveBillModel queryModel = new ReceiveBillModel() ;
				queryModel.setCode(confirmBillId); 
				
				queryModel = receiveBillDao.searchByModel(queryModel) ;
				
				if(queryModel == null) {
					LOG.info("经分销系统查询无此结算单据");
					
					continue ;
				}
				
				if(!comfirmBillJson.containsKey("state") || "-1".equals(comfirmBillJson.getString("state"))) {
					LOG.info("NC系统查询无此结算单据");
					
					continue ;
				}
				
				LOG.info("NC获取状态获取成功，状态值为：" + DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_nvSynList, comfirmBillJson.getString("state")));
				
				ReceiveBillModel updateModel = new ReceiveBillModel() ;
				updateModel.setId(queryModel.getId());
				updateModel.setNcStatus(comfirmBillJson.getString("state"));
				
				if(comfirmBillJson.get("ncCode") != null) {
					updateModel.setNcCode(comfirmBillJson.getString("ncCode"));
				}
				
				updateModel.setModifyDate(TimeUtils.getNow());
				
				receiveBillDao.modify(updateModel) ;
				
			}else if(confirmBillId.contains(DERP.UNIQUE_ID_YFZD)) {
				
				PaymentBillModel queryModel = new PaymentBillModel() ;
				queryModel.setCode(confirmBillId); 
				
				queryModel = paymentBillDao.searchByModel(queryModel) ;
				
				if(queryModel == null) {
					LOG.info("经分销系统查询无此结算单据");
					
					continue ;
				}
				
				if("-1".equals(comfirmBillJson.getString("state"))) {
					LOG.info("NC系统查询无此结算单据");
					
					continue ;
				}
				
				LOG.info("NC获取状态获取成功，状态值为：" + DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_ncStatusList, comfirmBillJson.getString("state")));
				
				PaymentBillModel updateModel = new PaymentBillModel() ;
				updateModel.setId(queryModel.getId());
				updateModel.setNcStatus(comfirmBillJson.getString("state"));
				
				if(comfirmBillJson.get("ncCode") != null) {
					updateModel.setNcCode(comfirmBillJson.getString("ncCode"));
				}
				
				updateModel.setModifyDate(TimeUtils.getNow());
				
				paymentBillDao.modify(updateModel) ;
			}
			
			
		}
	}

}
