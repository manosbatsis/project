package com.topideal.service.timer.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc14.BillVoucherStatusQueryRoot;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.TocSettlementReceiveBillDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.service.timer.ToCReceiveBillNcVoucherBackfillService;
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
public class ToCReceiveBillNcVoucherBackfillServiceImpl implements ToCReceiveBillNcVoucherBackfillService {

    private static final Logger LOG = Logger.getLogger(ToCReceiveBillNcVoucherBackfillServiceImpl.class) ;

    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160001,model=DERP_LOG_POINT.POINT_13201160001_Label)
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
            List<TocSettlementReceiveBillModel> ncVoucherBackFillList = tocSettlementReceiveBillDao.getNcVoucherFillBackList();
            if(ncVoucherBackFillList.isEmpty()) {
                return ;
            }
            List<String> receiveCodeList = new ArrayList<String>() ;
            for (TocSettlementReceiveBillModel tempModel : ncVoucherBackFillList) {
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

            TocSettlementReceiveBillModel queryModel = new TocSettlementReceiveBillModel() ;
            queryModel.setCode(billId);

            queryModel = tocSettlementReceiveBillDao.searchByModel(queryModel) ;

            if(queryModel == null) {
                LOG.info("经分销系统查询无此结算单据");
                continue ;
            }

            TocSettlementReceiveBillModel updateModel = new TocSettlementReceiveBillModel() ;
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
            tocSettlementReceiveBillDao.modify(updateModel) ;
        }

    }
}
