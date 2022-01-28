package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.dao.bill.TocSettlementReceiveBillCostItemDao;
import com.topideal.dao.bill.TocSettlementReceiveBillDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.order.service.bill.ToCReceiveBillCostItemService;
import com.topideal.order.webapi.bill.form.ToCReceiveBillCostItemForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: toc应收费项明细service
 * @Author: Chen Yiluan
 * @Date: 2021/01/06 16:14
 **/
@Service
public class ToCReceiveBillCostItemServiceImpl implements ToCReceiveBillCostItemService {

    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;

    @Override
    public List<TocSettlementReceiveBillCostItemModel> listCostByHandAdd(Long billId) throws SQLException {
        TocSettlementReceiveBillCostItemModel costItemModel = new TocSettlementReceiveBillCostItemModel();
        costItemModel.setBillId(billId);
        costItemModel.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_1);
        return tocSettlementReceiveBillCostItemDao.list(costItemModel);
    }

    @Override
    public Map<String, Object> saveReceiveBillCost(TocSettlementReceiveBillDTO dto) throws Exception {
        Long billId = dto.getId();
        TocSettlementReceiveBillModel billModel = tocSettlementReceiveBillDao.searchById(billId);
        BigDecimal addAmount = new BigDecimal("0");
        //删除对应的费用信息
        tocSettlementReceiveBillCostItemDao.delCostItems(billId, DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_1);
        Map<String, Object> retMap = new HashMap<>();
        List<Map<String, Object>> costItemList = dto.getCostItemList();
        for (Map<String, Object> itemMap : costItemList) {
            String projectId = (String) itemMap.get("projectId");
            String projectName = (String) itemMap.get("projectName");
            String brandParent = (String) itemMap.get("brandParent");
            String num = (String) itemMap.get("num");
            String price = (String) itemMap.get("price");
            String rmbPrice = (String) itemMap.get("rmbPrice");
            String billType = (String) itemMap.get("billType");
            String remark = (String) itemMap.get("remark");

            TocSettlementReceiveBillCostItemModel receiveBillCostItemModel = new TocSettlementReceiveBillCostItemModel();
            receiveBillCostItemModel.setBillId(dto.getId());
            receiveBillCostItemModel.setProjectId(Long.valueOf(projectId));
            receiveBillCostItemModel.setProjectName(projectName);
            receiveBillCostItemModel.setRemark(remark);
            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(projectId));
            if (settlementConfigModel == null) {
                throw new RuntimeException("费用项目不存在");
            }

            if (StringUtils.isNotBlank(brandParent)) {
                Map<String, Object> params = new HashMap<>();
                params.put("brandSuperiorId", Long.valueOf(brandParent));
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                if (brandSuperior != null) {
                    receiveBillCostItemModel.setParentBrandName(brandSuperior.getName());
                    receiveBillCostItemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                    receiveBillCostItemModel.setParentBrandCode(brandSuperior.getNcCode());
                }
            }

            if (StringUtils.isNotBlank(num)) {
                receiveBillCostItemModel.setNum(Integer.valueOf(num));
            }
            receiveBillCostItemModel.setOriginalAmount(new BigDecimal(price));
            receiveBillCostItemModel.setRmbAmount(new BigDecimal(rmbPrice));
            receiveBillCostItemModel.setBillType(billType);
            receiveBillCostItemModel.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_1);
            if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0.equals(billType)) {
                addAmount = addAmount.add(new BigDecimal(rmbPrice));
            } else {
                addAmount = addAmount.subtract(new BigDecimal(rmbPrice));
            }
            tocSettlementReceiveBillCostItemDao.save(receiveBillCostItemModel);
        }
        BigDecimal totalAmount = billModel.getReceivableAmount().add(addAmount);
        TocSettlementReceiveBillModel updateModel = new TocSettlementReceiveBillModel();
        updateModel.setId(billId);
        updateModel.setReceivableAmount(totalAmount);
        tocSettlementReceiveBillDao.modify(updateModel);
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        return retMap;
    }

    @Override
    public Map<String, Object> saveAPIReceiveBillCost(User user, Long id, List<ToCReceiveBillCostItemForm> costItem) throws Exception {
        Long billId = id;
        TocSettlementReceiveBillModel billModel = tocSettlementReceiveBillDao.searchById(billId);
        BigDecimal addAmount = new BigDecimal("0");

        //删除对应的费用信息
        tocSettlementReceiveBillCostItemDao.delCostItems(billId, DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_1);
        Map<String, Object> retMap = new HashMap<>();
        for(ToCReceiveBillCostItemForm itemMap:costItem){
            TocSettlementReceiveBillCostItemModel receiveBillCostItemModel = new TocSettlementReceiveBillCostItemModel();
            receiveBillCostItemModel.setBillId(billId);
            receiveBillCostItemModel.setProjectId(itemMap.getProjectId());
            receiveBillCostItemModel.setProjectName(itemMap.getProjectName());
            receiveBillCostItemModel.setRemark(itemMap.getRemark());
            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemMap.getProjectId());
            if (settlementConfigModel == null) {
                throw new RuntimeException("费用项目不存在");
            }
            if (itemMap.getBrandParent()!=null) {
                Map<String, Object> params = new HashMap<>();
                params.put("brandSuperiorId",itemMap.getBrandParent());
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                if (brandSuperior != null) {
                    receiveBillCostItemModel.setParentBrandName(brandSuperior.getName());
                    receiveBillCostItemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                    receiveBillCostItemModel.setParentBrandCode(brandSuperior.getNcCode());
                }
            }
            if (itemMap.getNum()!=null) {
                receiveBillCostItemModel.setNum(itemMap.getNum());
            }
            receiveBillCostItemModel.setOriginalAmount(itemMap.getPrice());
            receiveBillCostItemModel.setRmbAmount(itemMap.getRmbPrice());
            receiveBillCostItemModel.setBillType(itemMap.getBillType());
            receiveBillCostItemModel.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_1);
            if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0.equals(itemMap.getBillType())) {
                addAmount = addAmount.add(itemMap.getRmbPrice());
            } else {
                addAmount = addAmount.subtract(itemMap.getRmbPrice());
            }
            tocSettlementReceiveBillCostItemDao.save(receiveBillCostItemModel);
        }
        BigDecimal totalAmount = billModel.getReceivableAmount().add(addAmount);
        TocSettlementReceiveBillModel updateModel = new TocSettlementReceiveBillModel();
        updateModel.setId(billId);
        updateModel.setReceivableAmount(totalAmount);
        tocSettlementReceiveBillDao.modify(updateModel);
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        return retMap;
    }
}
