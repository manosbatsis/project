package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.bill.ReceiveBillCostItemDao;
import com.topideal.dao.bill.ReceiveBillItemDao;
import com.topideal.dao.bill.ReceivePaymentSubjectDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.ReceiveBillItemModel;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.bill.ReceiveBillCostItemService;
import com.topideal.order.webapi.bill.form.ReceiveBillCostItemForm;
import com.topideal.order.webapi.bill.form.ReceiveBillForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillCostItemServiceImpl implements ReceiveBillCostItemService {

    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;


    @Override
    public List<ReceiveBillCostItemDTO> itemListGroupByBillId(Long billId, boolean isAddItems) throws SQLException {
        List<Long> billIds = new ArrayList<>();
        billIds.add(billId);
        List<ReceiveBillCostItemDTO> returnList = new ArrayList<>();
        List<ReceiveBillCostItemDTO> itemModels =  receiveBillCostItemDao.itemListByBillIds(billIds);
        for (ReceiveBillCostItemDTO itemDTO : itemModels) {
            if (itemDTO.getGoodsId() != null && itemDTO.getGoodsId()>0) {
                Map<String, Object> merchandiseParam = new HashMap<>();
                merchandiseParam.put("merchandiseId", itemDTO.getGoodsId());
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
                itemDTO.setGoodsName(merchandiseInfoMogo.getName());
                itemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                itemDTO.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
            }
            returnList.add(itemDTO);
        }
        if (isAddItems) {
            //补扣款添加的明细
            ReceiveBillItemModel ReceiveBillItemModel = new ReceiveBillItemModel();
            ReceiveBillItemModel.setBillId(billId);
            ReceiveBillItemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
            List<ReceiveBillItemModel> itemModelList = receiveBillItemDao.list(ReceiveBillItemModel);
            for (ReceiveBillItemModel itemModel : itemModelList) {
                ReceiveBillCostItemDTO itemDTO = new ReceiveBillCostItemDTO();
                itemDTO.setId(itemModel.getId());
                itemDTO.setBillId(itemModel.getBillId());
                itemDTO.setProjectId(itemModel.getProjectId());
                itemDTO.setProjectName(itemModel.getProjectName());
                itemDTO.setPoNo(itemModel.getPoNo());
                itemDTO.setInvoiceDescription(itemModel.getInvoiceDescription());
                itemDTO.setNum(itemModel.getNum());
                itemDTO.setCreateDate(itemModel.getCreateDate());
                itemDTO.setModifyDate(itemModel.getModifyDate());
                itemDTO.setParentProjectName(itemModel.getParentBrandName());
                itemDTO.setParentBrandCode(itemModel.getParentBrandCode());
                itemDTO.setParentBrandId(itemModel.getParentBrandId());
                itemDTO.setSubjectCode(itemModel.getSubjectCode());
                itemDTO.setSubjectName(itemModel.getSubjectName());
                itemDTO.setPaymentSubjectName(itemModel.getPaymentSubjectName());
                itemDTO.setPaymentSubjectId(itemModel.getPaymentSubjectId());
                itemDTO.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);
                if (itemModel.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                    itemDTO.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
                }
                itemDTO.setPrice(itemModel.getPrice().abs());
                if (itemModel.getGoodsId() != null && itemModel.getGoodsId()>0) {
                    Map<String, Object> merchandiseParam = new HashMap<>();
                    merchandiseParam.put("merchandiseId", itemModel.getGoodsId());
                    MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
                    itemDTO.setGoodsName(merchandiseInfoMogo.getName());
                    itemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                    itemDTO.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
                }
                returnList.add(itemDTO);
            }
        }
        return returnList;
    }

    @Override
    public Map<String, Object> saveReceiveBillCost(ReceiveBillDTO dto) throws Exception {
        Long billId = dto.getId();
        //删除对应的费用信息
        receiveBillCostItemDao.delCostItem(billId);
        //删除补扣款对应的明细信息
        receiveBillItemDao.delItems(billId, DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
        Map<String, Object> retMap = new HashMap<>();
        List<Map<String, Object>> costItemList = dto.getCostItemList();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<ReceiveBillCostItemModel> costItemModels = new ArrayList<>();
        for (Map<String, Object> itemMap : costItemList) {
            String projectId = (String) itemMap.get("projectId");
            String projectName = (String) itemMap.get("projectName");
            String goodsId = (String) itemMap.get("goodsId");
            String goodsNo = (String) itemMap.get("goodsNo");
            String goodsName = (String) itemMap.get("goodsName");
            String brandParent = (String) itemMap.get("brandParent");
            String brandParentName = (String) itemMap.get("brandParentName");
            String poNo = (String) itemMap.get("poNo");
            String num = (String) itemMap.get("num");
            String price = (String) itemMap.get("price");
            String billType = (String) itemMap.get("billType");
            String storePlatformCode = (String) itemMap.get("storePlatformCode");
            String remark = (String) itemMap.get("remark");
            String invoiceDescription = (String) itemMap.get("invoiceDescription");
            String taxRate = (String) itemMap.get("taxRate");

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(projectId));
            if (settlementConfigModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目不存在");
                return retMap;
            }

            if (projectName.equals("经销业务TO B收入") && StringUtils.isBlank(brandParent)) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，母品牌不能为空！");
                return retMap;
            }

            if (projectName.equals("经销业务TO B收入") && StringUtils.isBlank(goodsNo)) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，商品不能为空！");
                return retMap;
            }

            if (projectName.equals("经销业务TO B收入") && StringUtils.isBlank(num)) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，数量不能为空！");
                return retMap;
            }

            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "NC收支费项不存在");
                return retMap;
            }

            if (StringUtils.isBlank(taxRate)) {
                taxRate = "0";
            }
            String brandName = null;
            Long brandId = null;
            String brandCode = null;
            if (StringUtils.isNotBlank(brandParent)) {
                Map<String, Object> params = new HashMap<>();
                params.put("brandSuperiorId", Long.valueOf(brandParent));
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                if (brandSuperior != null) {
                    brandName = brandSuperior.getName();
                    brandId = brandSuperior.getBrandSuperiorId();
                    brandCode = brandSuperior.getNcCode();
                }
            }

            BigDecimal tax = new BigDecimal(price).multiply(new BigDecimal(taxRate)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal taxAmount = new BigDecimal(price).add(tax);

            if (projectName.equals("经销业务TO B收入")){
                ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
                itemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
                itemModel.setBillId(dto.getId());
                itemModel.setProjectId(Long.valueOf(projectId));
                itemModel.setProjectName(projectName);
                itemModel.setInvoiceDescription(invoiceDescription);
                //nc
                itemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                itemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                itemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                itemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(goodsId)) {
                    itemModel.setGoodsId(Long.valueOf(goodsId));
                    itemModel.setGoodsNo(goodsNo);
                    itemModel.setGoodsName(goodsName);
                }
                itemModel.setParentBrandCode(brandCode);
                itemModel.setParentBrandId(brandId);
                itemModel.setParentBrandName(brandName);
                itemModel.setPoNo(poNo);
                if (StringUtils.isNotBlank(num)&&Integer.parseInt(num)>0) {
                    itemModel.setNum(Integer.valueOf(num));
                }
                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1.equals(billType)) {
                    itemModel.setPrice(new BigDecimal(price).negate());
                } else {
                    itemModel.setPrice(new BigDecimal(price));
                }
                itemModel.setVerifyPlatformCode(storePlatformCode);
                itemModel.setTaxRate(Double.valueOf(taxRate));
                itemModel.setTax(tax);
                itemModel.setTaxAmount(taxAmount);
                itemModels.add(itemModel);
            } else {
                ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                receiveBillCostItemModel.setBillId(dto.getId());
                receiveBillCostItemModel.setProjectId(Long.valueOf(projectId));
                receiveBillCostItemModel.setProjectName(projectName);
                receiveBillCostItemModel.setRemark(remark);
                receiveBillCostItemModel.setInvoiceDescription(invoiceDescription);
                //nc
                receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(goodsId)) {
                    receiveBillCostItemModel.setGoodsId(Long.valueOf(goodsId));
                    receiveBillCostItemModel.setGoodsNo(goodsNo);
                    receiveBillCostItemModel.setGoodsName(goodsName);
                }
                receiveBillCostItemModel.setParentBrandCode(brandCode);
                receiveBillCostItemModel.setParentBrandId(brandId);
                receiveBillCostItemModel.setParentBrandName(brandName);
                receiveBillCostItemModel.setPoNo(poNo);
                if (StringUtils.isNotBlank(num)&&Integer.parseInt(num)>0) {
                    receiveBillCostItemModel.setNum(Integer.valueOf(num));
                }
                receiveBillCostItemModel.setPrice(new BigDecimal(price));
                receiveBillCostItemModel.setBillType(billType);
                receiveBillCostItemModel.setVerifyPlatformCode(storePlatformCode);
                receiveBillCostItemModel.setTaxRate(Double.valueOf(taxRate));
                receiveBillCostItemModel.setTax(tax);
                receiveBillCostItemModel.setTaxAmount(taxAmount);
                costItemModels.add(receiveBillCostItemModel);
            }
        }
        int pageSize = 1000;
        for (int i = 0; i < itemModels.size(); ) {
            int pageSub = (i+pageSize) < itemModels.size() ? (i+pageSize) : itemModels.size();
            receiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
            i = pageSub;
        }
        for (int i = 0; i < costItemModels.size(); ) {
            int pageSub = (i+pageSize) < costItemModels.size() ? (i+pageSize) : costItemModels.size();
            receiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
            i = pageSub;
        }
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        return retMap;
    }

    @Override
    public Map<String, String> saveApiReceiveBillCost(ReceiveBillForm form) throws Exception {
        Long billId = form.getBillId();
        //删除对应的费用信息
        receiveBillCostItemDao.delCostItem(billId);
        //删除补扣款对应的明细信息
        receiveBillItemDao.delItems(billId, DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
        Map<String, String> retMap = new HashMap<>();
        List<ReceiveBillCostItemForm> costItemList = form.getCostItemList();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<ReceiveBillCostItemModel> costItemModels = new ArrayList<>();
        for (ReceiveBillCostItemForm costItemForm : costItemList) {
            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(costItemForm.getProjectId()));
            if (settlementConfigModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目不存在");
                return retMap;
            }

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入") && StringUtils.isBlank(costItemForm.getBrandParent())) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，母品牌不能为空！");
                return retMap;
            }

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入") && StringUtils.isBlank(costItemForm.getGoodsNo())) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，商品不能为空！");
                return retMap;
            }

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入") && costItemForm.getNum() == null) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，数量不能为空！");
                return retMap;
            }

            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "NC收支费项不存在");
                return retMap;
            }

            if (StringUtils.isBlank(costItemForm.getTaxRate())) {
                costItemForm.setTaxRate("0");
            }
            String brandName = null;
            Long brandId = null;
            String brandCode = null;
            if (StringUtils.isNotBlank(costItemForm.getBrandParent())) {
                Map<String, Object> params = new HashMap<>();
                params.put("brandSuperiorId", Long.valueOf(costItemForm.getBrandParent()));
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                if (brandSuperior != null) {
                    brandName = brandSuperior.getName();
                    brandId = brandSuperior.getBrandSuperiorId();
                    brandCode = brandSuperior.getNcCode();
                }
            }

            BigDecimal price = new BigDecimal(costItemForm.getTaxAmount()).divide(new BigDecimal("1").add(new BigDecimal(costItemForm.getTaxRate())), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal tax = new BigDecimal(costItemForm.getTaxAmount()).subtract(price);

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入")){
                ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
                itemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
                itemModel.setBillId(billId);
                itemModel.setProjectId(settlementConfigModel.getId());
                itemModel.setProjectName(settlementConfigModel.getProjectName());
                itemModel.setInvoiceDescription(costItemForm.getInvoiceDescription());
                //nc
                itemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                itemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                itemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                itemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(costItemForm.getGoodsId())) {
                    itemModel.setGoodsId(Long.valueOf(costItemForm.getGoodsId()));
                    itemModel.setGoodsNo(costItemForm.getGoodsNo());
                    itemModel.setGoodsName(costItemForm.getGoodsName());
                }
                itemModel.setParentBrandCode(brandCode);
                itemModel.setParentBrandId(brandId);
                itemModel.setParentBrandName(brandName);
                itemModel.setPoNo(costItemForm.getPoNo());
                if (costItemForm.getNum() != null) {
                    itemModel.setNum(costItemForm.getNum() );
                }
                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1.equals(costItemForm.getBillType())) {
                    itemModel.setPrice(price.negate());
                    itemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()).negate());
                } else {
                    itemModel.setPrice(price);
                    itemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()));
                }
                itemModel.setVerifyPlatformCode(costItemForm.getStorePlatformCode());
                itemModel.setTaxRate(Double.valueOf(costItemForm.getTaxRate()));
                itemModel.setTax(tax);

                itemModels.add(itemModel);
            } else {
                ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                receiveBillCostItemModel.setBillId(billId);
                receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
                receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillCostItemModel.setRemark(costItemForm.getRemark());
                receiveBillCostItemModel.setInvoiceDescription(costItemForm.getInvoiceDescription());
                //nc
                receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(costItemForm.getGoodsId())) {
                    receiveBillCostItemModel.setGoodsId(Long.valueOf(costItemForm.getGoodsId()));
                    receiveBillCostItemModel.setGoodsNo(costItemForm.getGoodsNo());
                    receiveBillCostItemModel.setGoodsName(costItemForm.getGoodsName());
                }
                receiveBillCostItemModel.setParentBrandCode(brandCode);
                receiveBillCostItemModel.setParentBrandId(brandId);
                receiveBillCostItemModel.setParentBrandName(brandName);
                receiveBillCostItemModel.setPoNo(costItemForm.getPoNo());
                if (costItemForm.getNum() != null) {
                    receiveBillCostItemModel.setNum(costItemForm.getNum());
                }
                receiveBillCostItemModel.setPrice(price);
                receiveBillCostItemModel.setBillType(costItemForm.getBillType());
                receiveBillCostItemModel.setVerifyPlatformCode(costItemForm.getStorePlatformCode());
                receiveBillCostItemModel.setPlatformGoodsCode(costItemForm.getPlatformGoodsCode());
                receiveBillCostItemModel.setTaxRate(Double.valueOf(costItemForm.getTaxRate()));
                receiveBillCostItemModel.setTax(tax);
                receiveBillCostItemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()));
                costItemModels.add(receiveBillCostItemModel);
            }
        }
        int pageSize = 1000;
        for (int i = 0; i < itemModels.size(); ) {
            int pageSub = (i+pageSize) < itemModels.size() ? (i+pageSize) : itemModels.size();
            receiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
            i = pageSub;
        }
        for (int i = 0; i < costItemModels.size(); ) {
            int pageSub = (i+pageSize) < costItemModels.size() ? (i+pageSize) : costItemModels.size();
            receiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
            i = pageSub;
        }
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        return retMap;
    }
}
