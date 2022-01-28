package com.topideal.service.timer.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc11.BillStatusQueryRoot;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.TocSettlementReceiveBillDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.service.timer.ToCReceiveBillNcBackfillService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ToCReceiveBillNcBackfillServiceImpl implements ToCReceiveBillNcBackfillService {

	private static final Logger LOG = Logger.getLogger(ToCReceiveBillNcBackfillServiceImpl.class) ;
	
	@Autowired
	private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160002,model=DERP_LOG_POINT.POINT_13201160002_Label)
	public void saveReceiveBillNcBackfill(String json, String keys, String topics, String tags) throws SQLException {
		
		/**查询状态为：已同步/待审核/待入erp/待入账/已入账的应收账单*/
		List<TocSettlementReceiveBillModel> backFillList = tocSettlementReceiveBillDao.getNcBackfillList() ;
		
		if(backFillList.isEmpty()) {
			return ;
		}
		
		List<String> receiveCodeList = new ArrayList<String>() ;
		
		for (TocSettlementReceiveBillModel tempModel : backFillList) {
			receiveCodeList.add(tempModel.getCode()) ;
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

			TocSettlementReceiveBillModel queryModel = new TocSettlementReceiveBillModel() ;
			queryModel.setCode(confirmBillId); 
			queryModel = tocSettlementReceiveBillDao.searchByModel(queryModel) ;
			
			if(queryModel == null) {
				LOG.info("经分销系统查询无此结算单据");
				
				continue ;
			}
			
			if("-1".equals(comfirmBillJson.getString("state"))) {
				LOG.info("NC系统查询无此结算单据");
				
				continue ;
			}
			
			LOG.info("NC获取状态获取成功，状态值为：" + DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_nvSynList, comfirmBillJson.getString("state")));

			TocSettlementReceiveBillModel updateModel = new TocSettlementReceiveBillModel() ;
			updateModel.setId(queryModel.getId());
			updateModel.setNcStatus(comfirmBillJson.getString("state"));
			
			if(comfirmBillJson.get("ncCode") != null) {
				updateModel.setNcCode(comfirmBillJson.getString("ncCode"));
			}
			updateModel.setModifyDate(TimeUtils.getNow());

			tocSettlementReceiveBillDao.modify(updateModel) ;
		}
	}

}
