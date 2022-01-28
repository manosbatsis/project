package com.topideal.service.timer.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc14.BillVoucherStatusQueryRoot;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AdvanceBillDao;
import com.topideal.entity.vo.bill.AdvanceBillModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.service.timer.AdvanceBillNcVoucherBackfillService;
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
public class AdvanceBillNcVoucherBackfillServiceImpl implements AdvanceBillNcVoucherBackfillService {

    private static final Logger LOG = Logger.getLogger(AdvanceBillNcVoucherBackfillServiceImpl.class) ;

    @Autowired
    private AdvanceBillDao advanceBillDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160008,model=DERP_LOG_POINT.POINT_13201160008_Label)
    public void saveReceiveBillNcVoucherBackfill(String json, String keys, String topics, String tags) throws SQLException {
        JSONObject jsonData = JSONObject.fromObject(json);
        String dataSource = (String)jsonData.get("datasource");

        BillVoucherStatusQueryRoot root = new BillVoucherStatusQueryRoot() ;

        //手动触发
        if (org.apache.commons.lang.StringUtils.isNotBlank(dataSource) && "1".equals(dataSource)) {
            String billCodes = (String) jsonData.get("billCodes");
            root.setBillId(billCodes);
        } else { //定时器触发
            //1.应收单当前账单状态不为”已作废“，且NC应收状态为“已入账”、“已关账”的应收单需查询接口状态；当该应收单已查询到凭证信息则不再继续获取
            //2.应收单当前账单状态为”已作废“，且NC应收状态为“已入账”、“已关账”、“已作废”、“已红冲”的应收单需查询接口状态
            List<AdvanceBillModel> ncVoucherBackFillList = advanceBillDao.getNcVoucherFillBackList();
            if(ncVoucherBackFillList.isEmpty()) {
                return ;
            }
            List<String> receiveCodeList = new ArrayList<String>() ;
            for (AdvanceBillModel tempModel : ncVoucherBackFillList) {
                receiveCodeList.add(tempModel.getCode()) ;
            }
            String billCodes = StringUtils.join(receiveCodeList.toArray(), ",");
            root.setBillId(billCodes);
        }
        //账单类型： 1=应收， 2=应付，3=预收，4=预付，5=收款，6=付款【不填则查询所有类型账单】
        root.setType("3");
        root.setSourceCode(ApolloUtil.ncSourceType);

        JSONObject requestJson = JSONObject.fromObject(root);
        LOG.info("-------------预收账单更新凭证的报文-------------"+requestJson);
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

            AdvanceBillModel queryModel = new AdvanceBillModel() ;
            queryModel.setCode(billId);

            queryModel = advanceBillDao.searchByModel(queryModel) ;

            if(queryModel == null) {
                LOG.info("经分销系统查询无此结算单据");
                continue ;
            }

            AdvanceBillModel updateModel = new AdvanceBillModel() ;
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
            advanceBillDao.modify(updateModel) ;
        }

    }
}
