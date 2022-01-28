package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.DownloadUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.purchase.PurchaseSdOrderDao;
import com.topideal.dao.purchase.PurchaseSdOrderSditemDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.timer.SaveReceiveBillService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Description: 生成应收账单
 * @Author: Chen Yiluan
 * @Date: 2020/09/21 15:22
 **/
@Service
public class SaveReceiveBillServiceImpl implements SaveReceiveBillService {

    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    @Autowired
    private SaleShelfDao saleShelfDao;
    @Autowired
    private BillOutinDepotDao billOutinDepotDao;
    @Autowired
    private BillOutinDepotItemDao billOutinDepotItemDao;
    @Autowired
    private PreSaleOrderDao preSaleOrderDao;
    @Autowired
    private PreSaleOrderItemDao preSaleOrderItemDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private BrandMongoDao brandMongoDao;
    @Autowired
    private PlatformCostOrderDao platformCostOrderDao;
    @Autowired
    private PlatformCostOrderItemDao platformCostOrderItemDao;
    @Autowired
    private CrawlerVipFileDataDao crawlerVipFileDataDao;
    @Autowired
    private YunjiAccountDataDao yunjiAccountDataDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private ReceiveExternalCodeDao receiveExternalCodeDao;
    @Autowired
    private ShelfDao shelfDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private SaleOrderDao saleOrderDao;
    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;
    @Autowired
    private PurchaseSdOrderDao purchaseSdOrderDao;
    @Autowired
    private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao;
    @Autowired
    private SaleFinancingOrderDao saleFinancingOrderDao;
    @Autowired
    private SaleFinancingOrderItemDao saleFinancingOrderItemDao;
    @Autowired
    private AdvanceBillItemDao advanceBillItemDao;
    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;
    @Autowired
    private AdvanceBillVerifyItemDao advanceBillVerifyItemDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private SaleReturnOrderDao saleReturnOrderDao;
    @Autowired
    private SaleReturnIdepotDao saleReturnIdepotDao;
    @Autowired
    private SaleReturnIdepotItemDao saleReturnIdepotItemDao;


    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveReceiveBillServiceImpl.class);

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201169300,model=DERP_LOG_POINT.POINT_13201169300_Label,keyword="billCode")
    public boolean saveReceiveBill(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("生成应收账单json:"+json);
        JSONObject jsonData = JSONObject.fromObject(json);
        Integer billId = (Integer) jsonData.get("billId");
        Integer merchantId = (Integer) jsonData.get("merchantId");
        String merchantName = (String) jsonData.get("merchantName");
        Integer userId = (Integer) jsonData.get("userId");
        String userName = (String) jsonData.get("userName");
        String orderType = (String) jsonData.get("orderType");
        String relCodes = (String) jsonData.get("relCodes");
        Integer buId = (Integer) jsonData.get("buId");
        String billCode = (String) jsonData.get("billCode");
        String currency = (String) jsonData.get("currency");
        String isAddWorn = (String) jsonData.get("isAddWorn");
        List<String> relCodeList = Arrays.asList(relCodes.split("&"));


        if (billId != null) { //刷新
            //删除账单明细
            receiveBillItemDao.delItems(Long.valueOf(billId), null);
            receiveBillCostItemDao.delCostItem(Long.valueOf(billId));
        } else { //新增

            ReceiveBillModel receiveBillModel = new ReceiveBillModel();
            receiveBillModel.setCode(billCode);
            ReceiveBillModel exist = receiveBillDao.searchByModel(receiveBillModel);

            if (exist != null) {
                throw new RuntimeException("该应收账单已生成!");
            }

            //如果类型是账单出库单则关联业务单只能有一单
            if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(orderType) && relCodeList.size() > 1) {
                LOGGER.error("当关联业务单类型为账单出库单时，仅能选择一单生成应收账单!");
                throw new RuntimeException("当关联业务单类型为账单出库单时，仅能选择一单生成应收账单!");
            }

            for (String relCode : relCodeList) {
                if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(orderType)
                        || DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(orderType)
                        || DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(orderType)) {
                    //判断业务单号关联的应收账单是否存在
                    ReceiveExternalCodeModel externalCodeModel = new ReceiveExternalCodeModel();
                    externalCodeModel.setOrderType(orderType);
                    externalCodeModel.setExternalCode(relCode);
                    externalCodeModel.setBuId(Long.valueOf(buId));
                    externalCodeModel.setCurrency(currency);
                    try {
                        Long id = receiveExternalCodeDao.save(externalCodeModel);
                    } catch (Exception e) {
                        LOGGER.error("应收账单外部单号来源表已经存在 单号：" + relCode + "  保存失败");
                        throw new RuntimeException("应收账单外部单号来源表已经存在 单号：" + relCode + "  保存失败");
                    }
                }
            }
        }

        SettlementConfigModel configModel = new SettlementConfigModel();
        configModel.setProjectName("经销业务TO B收入");
        configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
        configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
        SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchByModel(configModel);
        if (toBSettlementConfigModel == null) {
            throw new RuntimeException("没有找到“经销业务TO B收入”的费项配置");
        }

        //生成的应收账单
        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        //应收明细集合
        List<ReceiveBillItemModel> receiveBillItemModels = new ArrayList<>();
        //费用明细集合
        List<ReceiveBillCostItemModel> receiveBillCostItemModels  = new ArrayList<>();
        //平台费用单（账单出库单）
        Map<String, PlatformCostOrderItemDTO> platformCostOrderItemModelMap = new HashMap<>();
        List<Long> platformOrderIdList = new ArrayList<>();
        //附件
        Map<String, String> attachmentFileMap = new HashMap<>();
        //po单号集合
        List<String> poNoList = new ArrayList<>();
        //客户
        Long customerId = null;
        //销售单号集合
        List<String> orderCodes = new ArrayList<>();

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(orderType)) {
            customerId = shelfOrderGenerate(relCodeList, merchantId, isAddWorn, orderCodes, receiveBillModel, poNoList, relCodes, receiveBillItemModels,receiveBillCostItemModels,toBSettlementConfigModel);
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(orderType)) {
            customerId =  billOutinDepotGenerate(relCodeList, merchantId, receiveBillModel, relCodes, currency, receiveBillItemModels, buId,
                    platformCostOrderItemModelMap, platformOrderIdList, attachmentFileMap,toBSettlementConfigModel);
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(orderType)) {
            customerId = preOrderGenerate(relCodeList, merchantId, receiveBillModel, poNoList, relCodes, receiveBillItemModels,toBSettlementConfigModel);
        } /*else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(orderType)) {
            customerId = saleOrderGenerate(relCodeList, merchantId, orderCodes, receiveBillModel, poNoList, relCodes, receiveBillItemModels,receiveBillCostItemModels, toBSettlementConfigModel);
        } */else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(orderType)) {
            customerId = purchaseSDOrderGenerate(relCodeList, merchantId, receiveBillModel, poNoList, relCodes, receiveBillCostItemModels);
        } /*else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_6.equals(orderType)) {
            customerId = saleFinancingOrderGenerate(relCodeList, receiveBillModel, orderCodes, poNoList, relCodes, receiveBillItemModels);
        }*/ else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(orderType)) {
            customerId = saleReturnOrderGenerate(relCodeList, merchantId, receiveBillModel, poNoList, relCodes, receiveBillItemModels);
        } else {
            throw new RuntimeException("单据类型不存在");
        }
        CustomerMerchantRelMongo customerRel = getCustomerConfig(customerId, Long.valueOf(merchantId), receiveBillModel);

        if (billId == null) {
            receiveBillModel.setCode(billCode);
            receiveBillModel.setMerchantId(Long.valueOf(merchantId));
            receiveBillModel.setMerchantName(merchantName);
            receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);
            receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
            receiveBillModel.setCreaterId(Long.valueOf(userId));
            receiveBillModel.setCreater(userName);
            receiveBillModel.setOrderType(orderType);
            receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
            receiveBillModel.setSaleModel(DERP_ORDER.RECEIVEBILL_SALEMODEL_009);
            receiveBillModel.setSettlementType("1");
            receiveBillDao.save(receiveBillModel);
        } else { //刷新
            receiveBillModel.setId(Long.valueOf(billId));
        }

        for (ReceiveBillItemModel itemModel : receiveBillItemModels) {
            itemModel.setBillId(receiveBillModel.getId());
            itemModel.setCreateDate(TimeUtils.getNow());

            if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(orderType) ||
                    DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(orderType)) {
                itemModel.setTaxAmount(itemModel.getPrice());
                if (customerRel.getRate() != null) {
                    BigDecimal tax = itemModel.getPrice().multiply(customerRel.getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal taxAmount = itemModel.getPrice().add(tax);
                    itemModel.setTaxRate(customerRel.getRate().doubleValue());
                    itemModel.setTax(tax);
                    itemModel.setTaxAmount(taxAmount);
                }
            }
        }
        int pageSize = 1000;
        for (int i = 0; i < receiveBillItemModels.size(); ) {
            int pageSub = (i+pageSize) < receiveBillItemModels.size() ? (i+pageSize) : receiveBillItemModels.size();
            receiveBillItemDao.batchSave(receiveBillItemModels.subList(i, pageSub));
            i = pageSub;
        }

        //生成费用明细
        for (ReceiveBillCostItemModel costItemModel : receiveBillCostItemModels) {
            costItemModel.setBillId(receiveBillModel.getId());
            costItemModel.setCreateDate(TimeUtils.getNow());

            if (customerRel.getRate() != null) {
                BigDecimal tax = costItemModel.getPrice().multiply(customerRel.getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = costItemModel.getPrice().add(tax);
                costItemModel.setTaxRate(customerRel.getRate().doubleValue());
                costItemModel.setTax(tax);
                costItemModel.setTaxAmount(taxAmount);
            }
        }
        for (String key : platformCostOrderItemModelMap.keySet()) {
            PlatformCostOrderItemDTO costOrderItemDTO = platformCostOrderItemModelMap.get(key);
            ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
            receiveBillCostItemModel.setBillId(receiveBillModel.getId());
            receiveBillCostItemModel.setProjectId(costOrderItemDTO.getItemProjectId());
            receiveBillCostItemModel.setProjectName(costOrderItemDTO.getItemProjectName());
            receiveBillCostItemModel.setGoodsId(costOrderItemDTO.getGoodsId());
            receiveBillCostItemModel.setGoodsNo(costOrderItemDTO.getGoodsNo());
            receiveBillCostItemModel.setGoodsName(costOrderItemDTO.getGoodsName());
            receiveBillCostItemModel.setPoNo(relCodes);
            if (costOrderItemDTO.getAmount() != null && costOrderItemDTO.getAmount().compareTo(BigDecimal.ZERO) == 1) {
                receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);
            } else {
                receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
            }
            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(costOrderItemDTO.getItemProjectId());
            if (settlementConfigModel == null) {
                throw new RuntimeException(costOrderItemDTO.getItemProjectName()+"费用项目不存在");
            }
            receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
            receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel != null) {
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
            }

            Integer costNum = costOrderItemDTO.getNum() >= 0 ? costOrderItemDTO.getNum() : -costOrderItemDTO.getNum();
            BigDecimal amount = costOrderItemDTO.getAmount().compareTo(BigDecimal.ZERO) > -1 ? costOrderItemDTO.getAmount() : costOrderItemDTO.getAmount().multiply(new BigDecimal("-1"));
            receiveBillCostItemModel.setNum(costNum);
            receiveBillCostItemModel.setPrice(amount);
            receiveBillCostItemModel.setTaxAmount(amount);

            if (customerRel.getRate() != null) {
                BigDecimal tax = amount.multiply(customerRel.getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = amount.add(tax);
                receiveBillCostItemModel.setTaxRate(customerRel.getRate().doubleValue());
                receiveBillCostItemModel.setTax(tax);
                receiveBillCostItemModel.setTaxAmount(taxAmount);
            }

            if (costOrderItemDTO.getGoodsId() != null) {
                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(costOrderItemDTO.getGoodsId());
                if (brandParent != null) {
                    receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
            }
            receiveBillCostItemModels.add(receiveBillCostItemModel);
        }
        int costSize = 1000;
        for (int i = 0; i < receiveBillCostItemModels.size(); ) {
            int pageSub = (i+costSize) < receiveBillCostItemModels.size() ? (i+costSize) : receiveBillCostItemModels.size();
            receiveBillCostItemDao.batchSave(receiveBillCostItemModels.subList(i, pageSub));
            i = pageSub;
        }

        //更新费用单状态
        if (platformOrderIdList.size() > 0) {
            PlatformCostOrderModel model = new PlatformCostOrderModel();
            model.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_3);
            model.setTransferSlipDate(TimeUtils.getNow());
            model.setTransferSliper(Long.valueOf(userId));
            model.setTransferSlipName(userName);
            platformCostOrderDao.batchUpdateOrderStatus(model, platformOrderIdList);
        }

      /*  //保存核销记录
        if (!orderCodes.isEmpty()) {
            List<ReceiveBillVerifyItemModel> verifyItemModels = getVerifyItems(orderCodes);
            for (ReceiveBillVerifyItemModel verifyItemModel : verifyItemModels) {
                verifyItemModel.setBillId(receiveBillModel.getId());
                receiveBillVerifyItemDao.save(verifyItemModel);
            }
        }*/

        //保存附件
        for (String fileName : attachmentFileMap.keySet()) {
            saveAttachmentFile(attachmentFileMap.get(fileName), fileName, receiveBillModel.getCode(), Long.valueOf(userId), userName);
        }

        LOGGER.info("====================生成应收账单结束====================");
        return true;
    }


    //上架单生成应收账单
    private Long shelfOrderGenerate(List<String> relCodeList, Integer merchantId, String isAddWorn,List<String> orderCodes,
                                    ReceiveBillModel receiveBillModel, List<String> poNoList, String relCodes,
                                    List<ReceiveBillItemModel> receiveBillItemModels,
                                    List<ReceiveBillCostItemModel> receiveBillCostItemModels,
                                    SettlementConfigModel settlementConfigModel) throws Exception {
        SettlementConfigModel discountConfig = null;
        ReceivePaymentSubjectModel paymentSubjectModel = null;
        if (DERP_ORDER.RECEIVEBILL_ISADDWORN_0.equals(isAddWorn)) {
            SettlementConfigModel configModel = new SettlementConfigModel();
            configModel.setProjectName("折扣折让TO B");
            configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
            configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
            discountConfig = settlementConfigDao.searchByModel(configModel);
            if (discountConfig == null) {
                throw new RuntimeException("没有找到“折扣折让TO B”的费项配置");
            }

            paymentSubjectModel = receivePaymentSubjectDao.searchById(discountConfig.getPaymentSubjectId());
            if (paymentSubjectModel == null) {
                //科目
                throw new RuntimeException("没有找到“折扣折让TO B”费项配置的科目配置");
            }
        }


        Long customerId = null;
        List<Timestamp> shelfDateList = new ArrayList<>();
        for (String relCode : relCodeList) {
            //上架单
            ShelfModel shelfModel = new ShelfModel();
            shelfModel.setCode(relCode);
            shelfModel.setMerchantId(Long.valueOf(merchantId));
            ShelfModel existModel = shelfDao.searchByModel(shelfModel);
            if (existModel == null) {
                throw new RuntimeException("业务单号:" + relCode + "关联的上架单不存在");
            } /*else if (!DERP_ORDER.SHELF_STATUS_1.equals(existModel.getState())) {
                throw new RuntimeException("业务单号:" + relCode + "关联的上架单状态不为“已上架”");
            }*/
            customerId = existModel.getCustomerId();
            receiveBillModel.setCustomerId(existModel.getCustomerId());
            receiveBillModel.setCustomerName(existModel.getCustomerName());
            receiveBillModel.setBuId(existModel.getBuId());
            receiveBillModel.setBuName(existModel.getBuName());
            receiveBillModel.setCurrency(existModel.getCurrency());
            receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_1);
            poNoList.add(existModel.getPoNo());
            shelfDateList.add(existModel.getShelfDate());

            SaleShelfModel saleShelfModel = new SaleShelfModel();
            saleShelfModel.setShelfId(existModel.getId());
            List<SaleShelfModel> saleShelfModels = saleShelfDao.list(saleShelfModel);

            SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
            saleOrderItemModel.setOrderId(existModel.getSaleOrderId());
            //查询销售订单表体（取上架商品对应销售金额）
            List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.list(saleOrderItemModel);
            Map<Long, BigDecimal> goodsMap = new HashMap<>();
            Map<Long, Double> taxMap = new HashMap<>();
            for (SaleOrderItemModel itemModel : saleOrderItemModels) {
                goodsMap.put(itemModel.getId(), itemModel.getPrice());
                taxMap.put(itemModel.getId(), itemModel.getTaxRate());
            }

            List<Map<String, Object>> alreadyVerifyItems = receiveBillItemDao.verifyItemList(existModel.getCode());

            Map<Long, Map<String, Object>> goodsIdsAlreadyVerifyMap = new HashMap<>();
            for (Map<String, Object> map : alreadyVerifyItems) {
                Long goodsId = (Long) map.get("goodsId");
                goodsIdsAlreadyVerifyMap.put(goodsId, map);
            }

            for (SaleShelfModel saleShelf : saleShelfModels) {
                BigDecimal verifyAmount = new BigDecimal("0");
                Integer verifyNum = 0;
                if (goodsIdsAlreadyVerifyMap.containsKey(saleShelf.getGoodsId())) {
                    Map<String, Object> map = goodsIdsAlreadyVerifyMap.get(saleShelf.getGoodsId());
                    verifyAmount = map.get("verifyAmount") != null ? (BigDecimal) map.get("verifyAmount") : new BigDecimal("0");
                    BigDecimal verifyNumBD = map.get("verifyNum") != null ? (BigDecimal) map.get("verifyNum") : new BigDecimal("0");
                    verifyNum = verifyNumBD.intValue();
                }

                Integer shelfNum = 0;
                if (saleShelf.getShelfNum() != null && saleShelf.getShelfNum() != 0) {
                    shelfNum += saleShelf.getShelfNum();
                }

                shelfNum -= verifyNum;
                if (shelfNum != 0) {
                    BigDecimal price = goodsMap.get(saleShelf.getSaleItemId()) == null ? BigDecimal.ZERO : goodsMap.get(saleShelf.getSaleItemId());
                    BigDecimal amount = price.multiply(new BigDecimal(shelfNum));

//                    amount = amount.subtract(verifyAmount);

                  /*  if (amount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }*/

                    ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                    receiveBillItemModel.setCode(existModel.getCode());
                    receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                    receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                    //写入nc配置
                    setNcConfig(receiveBillItemModel, settlementConfigModel.getId());
                    receiveBillItemModel.setPoNo(saleShelf.getPoNo());
                    receiveBillItemModel.setGoodsId(saleShelf.getGoodsId());
                    receiveBillItemModel.setGoodsNo(saleShelf.getGoodsNo());
                    receiveBillItemModel.setGoodsName(saleShelf.getGoodsName());
                    receiveBillItemModel.setPrice(amount);
                    receiveBillItemModel.setNum(shelfNum);
                    BrandMongo brand = brandMongoDao.getBrandMongo(saleShelf.getGoodsId());
                    if (brand != null) {
                        receiveBillItemModel.setBrandId(brand.getBrandId());
                        receiveBillItemModel.setBrandName(brand.getName());
                    }

                    if (taxMap.get(saleShelf.getSaleItemId()) != null) {
                        Double taxRate = taxMap.get(saleShelf.getSaleItemId());
                        receiveBillItemModel.setTaxRate(taxRate);

                        BigDecimal tax = amount.multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal taxAmount = amount.add(tax);
                        receiveBillItemModel.setTax(tax);
                        receiveBillItemModel.setTaxAmount(taxAmount);
                    }


                    BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleShelf.getGoodsId());
                    if (brandParent != null) {
                        receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                    }
                    receiveBillItemModels.add(receiveBillItemModel);
                }

                //是否增加残损金额
                if (DERP_ORDER.RECEIVEBILL_ISADDWORN_0.equals(isAddWorn)) {
                    //对于残损结算的商品数量、金额明细对应的结算费项为 “折扣折让ToB”，进入应收费用明细表
                    if (saleShelf.getDamagedNum() != null && saleShelf.getDamagedNum() != 0) {
                        BigDecimal price = goodsMap.get(saleShelf.getSaleItemId()) == null ? BigDecimal.ZERO : goodsMap.get(saleShelf.getSaleItemId());
                        BigDecimal amount = price.multiply(new BigDecimal(saleShelf.getDamagedNum()));

                        /*if (amount.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }*/

                        ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                        receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
                        receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
                        //写入nc配置
                        receiveBillCostItemModel.setProjectId(discountConfig.getId());
                        receiveBillCostItemModel.setProjectName(discountConfig.getProjectName());
                        //科目
                        receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                        receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());

                        receiveBillCostItemModel.setPoNo(saleShelf.getPoNo());
                        receiveBillCostItemModel.setGoodsId(saleShelf.getGoodsId());
                        receiveBillCostItemModel.setGoodsNo(saleShelf.getGoodsNo());
                        receiveBillCostItemModel.setGoodsName(saleShelf.getGoodsName());
                        receiveBillCostItemModel.setPrice(amount);
                        receiveBillCostItemModel.setNum(saleShelf.getDamagedNum());
                        receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);

                        if (taxMap.get(saleShelf.getSaleItemId()) != null) {
                            Double taxRate = taxMap.get(saleShelf.getSaleItemId());
                            receiveBillCostItemModel.setTaxRate(taxRate);

                            BigDecimal tax = amount.multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal taxAmount = amount.add(tax);
                            receiveBillCostItemModel.setTax(tax);
                            receiveBillCostItemModel.setTaxAmount(taxAmount);
                        }


                        BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleShelf.getGoodsId());
                        if (brandParent != null) {
                            receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        }
                        receiveBillCostItemModels.add(receiveBillCostItemModel);
                    }
                }
            }
            orderCodes.add(existModel.getSaleOrderCode());
        }

        //已存在的销售退明细
       /* List<Long> itemIds = new ArrayList<>();
        //根据PO号关联查询到的则取销售退货单表体PO对应的退货商品信息；
        List<SaleReturnOrderItemModel> saleReturnOrderItemModels = saleReturnOrderItemDao.statisticsByPoNos(poNoList);
        for (SaleReturnOrderItemModel saleReturnOrderItemModel : saleReturnOrderItemModels) {
            String poNos = saleReturnOrderItemModel.getPoNo();
            List<String> poList = Arrays.asList(poNos.split(","));
            poList.retainAll(poNoList);
            //如果该明细的po与上架单的po不一致，或者该明细已生成过费用明细了则不继续生成
            if (poList.isEmpty() || itemIds.contains(saleReturnOrderItemModel.getId())) {
                continue;
            }
            itemIds.add(saleReturnOrderItemModel.getId());

            ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
            receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
            receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
            receiveBillCostItemModel.setGoodsId(saleReturnOrderItemModel.getInGoodsId());
            receiveBillCostItemModel.setGoodsNo(saleReturnOrderItemModel.getInGoodsNo());
            receiveBillCostItemModel.setGoodsName(saleReturnOrderItemModel.getInGoodsName());
            receiveBillCostItemModel.setPoNo(relCodes);
            receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
            receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
            receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
            ReceivePaymentSubjectModel tobPaymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (tobPaymentSubjectModel != null) {
                //科目
                receiveBillCostItemModel.setSubjectCode(tobPaymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(tobPaymentSubjectModel.getSubName());
            }

            Integer costNum = saleReturnOrderItemModel.getReturnNum() + saleReturnOrderItemModel.getBadGoodsNum();
            BigDecimal price = saleReturnOrderItemModel.getPrice() != null ? saleReturnOrderItemModel.getPrice() : BigDecimal.ZERO;
            BigDecimal amount = price.multiply(new BigDecimal(costNum));
            receiveBillCostItemModel.setNum(costNum);
            receiveBillCostItemModel.setPrice(amount);
            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnOrderItemModel.getInGoodsId());
            if (brandParent != null) {
                receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
            }
            receiveBillCostItemModels.add(receiveBillCostItemModel);
        }*/
        receiveBillModel.setIsAddWorn(isAddWorn);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        Collections.sort(shelfDateList);
        receiveBillModel.setBillDate(shelfDateList.get(shelfDateList.size()-1));
        return customerId;
    }

    //账单出入库单生成应收账单
    private Long billOutinDepotGenerate(List<String> relCodeList, Integer merchantId,
                                    ReceiveBillModel receiveBillModel, String relCodes,String currency,
                                    List<ReceiveBillItemModel> receiveBillItemModels, Integer buId,
                                    Map<String, PlatformCostOrderItemDTO> platformCostOrderItemModelMap,
                                        List<Long> platformOrderIdList,Map<String, String> attachmentFileMap,
                                        SettlementConfigModel settlementConfigModel) throws Exception {
        Long customerId = null;
        for (String relCode : relCodeList) {
            //账单出库单
            BillOutinDepotModel billOutinDepotModel = new BillOutinDepotModel();
            billOutinDepotModel.setMerchantId(Long.valueOf(merchantId));
            billOutinDepotModel.setBillCode(relCode);
            billOutinDepotModel.setBuId(Long.valueOf(buId));
            billOutinDepotModel.setCurrency(currency);
            List<BillOutinDepotModel> billOutinDepotModels = billOutinDepotDao.list(billOutinDepotModel);
            if (billOutinDepotModels == null || billOutinDepotModels.size() == 0) {
                throw new RuntimeException("业务单号" + relCode + "关联的账单出入库单不存在");
            }

            customerId = billOutinDepotModels.get(0).getCustomerId();
            receiveBillModel.setCustomerId(billOutinDepotModels.get(0).getCustomerId());
            receiveBillModel.setCustomerName(billOutinDepotModels.get(0).getCustomerName());
            receiveBillModel.setBuId(billOutinDepotModels.get(0).getBuId());
            receiveBillModel.setBuName(billOutinDepotModels.get(0).getBuName());
            receiveBillModel.setCurrency(billOutinDepotModels.get(0).getCurrency());
            receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_1);
            for (BillOutinDepotModel billOutinDepot : billOutinDepotModels) {
                //不取国检出库和报废
                if (DERP_ORDER.PROCESSINGTYPE_GJC.equals(billOutinDepot.getProcessingType()) ||
                        DERP_ORDER.PROCESSINGTYPE_BFC.equals(billOutinDepot.getProcessingType())) {
                    continue;
                }
                BillOutinDepotItemModel billOutinDepotItemModel = new BillOutinDepotItemModel();
                billOutinDepotItemModel.setOutinDepotId(billOutinDepot.getId());

                List<BillOutinDepotItemModel> itemModelList = billOutinDepotItemDao.list(billOutinDepotItemModel);
                for (BillOutinDepotItemModel itemModel : itemModelList) {
                    BigDecimal price = itemModel.getAmount();
                    ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                    receiveBillItemModel.setCode(billOutinDepot.getCode());
                    receiveBillItemModel.setPoNo(itemModel.getPoNo());
                    receiveBillItemModel.setGoodsId(itemModel.getGoodsId());
                    receiveBillItemModel.setGoodsNo(itemModel.getGoodsNo());
                    receiveBillItemModel.setGoodsName(itemModel.getGoodsName());
                    receiveBillItemModel.setPlatformSku(itemModel.getPlatformSku());
                    receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                    receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                    if (billOutinDepot.getProcessingType().matches(DERP_ORDER.PROCESSINGTYPE_PYR+"|"+DERP_ORDER.PROCESSINGTYPE_KTR)) {
                        receiveBillItemModel.setPrice(price.multiply(new BigDecimal("-1")));
                        receiveBillItemModel.setNum(itemModel.getNum()*(-1));
                    } else {
                        receiveBillItemModel.setPrice(price);
                        receiveBillItemModel.setNum(itemModel.getNum());
                    }

                    receiveBillItemModel.setTaxAmount(receiveBillItemModel.getPrice());
                    Double taxRate = itemModel.getTaxRate();
                    receiveBillItemModel.setTaxRate(taxRate);

                    BigDecimal tax = receiveBillItemModel.getPrice().multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal taxAmount = receiveBillItemModel.getPrice().add(tax);
                    receiveBillItemModel.setTax(tax);
                    receiveBillItemModel.setTaxAmount(taxAmount);

                    BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                    if (brand != null) {
                        receiveBillItemModel.setBrandId(brand.getBrandId());
                        receiveBillItemModel.setBrandName(brand.getName());
                    }
                    BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                    if (brandParent != null) {
                        receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                    }
                    //写入nc配置
                    setNcConfig(receiveBillItemModel, settlementConfigModel.getId());
                    receiveBillItemModels.add(receiveBillItemModel);
                }
            }

            //业务单号+事业部查询平台费用单。若存在待确认的数据则提示错误“存在待确认的平台费用单，请先确认”
            PlatformCostOrderModel platformCostOrderModel = new PlatformCostOrderModel();
            platformCostOrderModel.setBillCode(relCode);
            platformCostOrderModel.setBuId(Long.valueOf(buId));
            platformCostOrderModel.setCurrency(currency);
            List<PlatformCostOrderModel> platformCostOrderModels = platformCostOrderDao.list(platformCostOrderModel);

            for (PlatformCostOrderModel pModel : platformCostOrderModels) {
                if (DERP_ORDER.PLATFORM_COST_ORDER_STATUS_1.equals(pModel.getStatus())) {
                    throw new RuntimeException("存在待确认的平台费用单，请先确认");
                }
                if (DERP_ORDER.PLATFORM_COST_ORDER_STATUS_2.equals(pModel.getStatus())) {
                    PlatformCostOrderItemModel itemModel = new PlatformCostOrderItemModel();
                    itemModel.setPlatformCostOrderId(pModel.getId());
                    List<PlatformCostOrderItemModel> PlatformCostOrderItemModels = platformCostOrderItemDao.list(itemModel);
                    for (PlatformCostOrderItemModel platformCostOrderItemModel : PlatformCostOrderItemModels) {
                        PlatformCostOrderItemDTO tempDto = new PlatformCostOrderItemDTO();
                        BeanUtils.copyProperties(platformCostOrderItemModel, tempDto);
                        String key = pModel.getBillCode()+ pModel.getBuId() + pModel.getItemProjectId()
                                + platformCostOrderItemModel.getGoodsNo() + platformCostOrderItemModel.getPoNo();
                        if (platformCostOrderItemModelMap.containsKey(key)) {
                            PlatformCostOrderItemDTO costOrderItemDTO = platformCostOrderItemModelMap.get(key);
                            costOrderItemDTO.setNum(platformCostOrderItemModel.getNum()+costOrderItemDTO.getNum());
                            BigDecimal temp = platformCostOrderItemModel.getAmount().add(costOrderItemDTO.getAmount());
                            costOrderItemDTO.setAmount(temp);
                            platformCostOrderItemModelMap.put(key, costOrderItemDTO);
                        } else {
                            tempDto.setItemProjectId(pModel.getItemProjectId());
                            tempDto.setItemProjectName(pModel.getItemProjectName());
                            platformCostOrderItemModelMap.put(key, tempDto);
                        }
                    }
                    platformOrderIdList.add(pModel.getId());
                }
            }
            if ("唯品会".equals(billOutinDepotModels.get(0).getCustomerName())) {
                CrawlerVipFileDataModel crawlerVipFileDataModel = new CrawlerVipFileDataModel();
                crawlerVipFileDataModel.setCustomerId(billOutinDepotModels.get(0).getCustomerId());
                crawlerVipFileDataModel.setBillCode(relCode);
                CrawlerVipFileDataModel fileDataModel = crawlerVipFileDataDao.searchByModel(crawlerVipFileDataModel);
                if (fileDataModel != null && StringUtils.isNotBlank(fileDataModel.getFileKey())) {
                    attachmentFileMap.put(relCode+"唯品账单", fileDataModel.getFileKey());
                }
            } else if ("云集".equals(billOutinDepotModels.get(0).getCustomerName())) {
                YunjiAccountDataModel yunjiAccountDataModel = new YunjiAccountDataModel();
                yunjiAccountDataModel.setSettleId(relCode);
                YunjiAccountDataModel accountDataModel = yunjiAccountDataDao.searchByModel(yunjiAccountDataModel);
                if (accountDataModel != null && StringUtils.isNotBlank(accountDataModel.getFileKey())) {
                    attachmentFileMap.put(relCode+"云集账单", accountDataModel.getFileKey());
                }
            }
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setBillDate(billOutinDepotDao.getMaxOutDepotDate(relCodeList, Long.valueOf(buId)));
        return customerId;
    }

    //预售单生成应收账单
    private Long preOrderGenerate(List<String> relCodeList, Integer merchantId,
                                    ReceiveBillModel receiveBillModel, List<String> poNoList, String relCodes,
                                    List<ReceiveBillItemModel> receiveBillItemModels,SettlementConfigModel settlementConfigModel) throws Exception {
        List<Timestamp> auditDateList = new ArrayList<>();
        Long customerId = null;
        for (String relCode : relCodeList) {
            //预售单
            PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
            preSaleOrderModel.setCode(relCode);
            preSaleOrderModel.setMerchantId(Long.valueOf(merchantId));
            PreSaleOrderModel preSaleOrder = preSaleOrderDao.searchByModel(preSaleOrderModel);
            if (preSaleOrder == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的预售单不存在");
            } else if (DERP_ORDER.PRESALEORDER_STATE_001.equals(preSaleOrder.getState())) {
                throw new RuntimeException("业务单号:" + relCode + "关联的预售单状态不能为“待审核”");
            }
            customerId = preSaleOrder.getCustomerId();
            receiveBillModel.setCustomerId(preSaleOrder.getCustomerId());
            receiveBillModel.setCustomerName(preSaleOrder.getCustomerName());
            receiveBillModel.setBuId(preSaleOrder.getBuId());
            receiveBillModel.setBuName(preSaleOrder.getBuName());
            receiveBillModel.setCurrency(preSaleOrder.getCurrency());
            receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_1);
            poNoList.add(preSaleOrder.getPoNo());
            auditDateList.add(preSaleOrder.getAuditDate());
            PreSaleOrderItemModel preSaleOrderItemModel = new PreSaleOrderItemModel();
            preSaleOrderItemModel.setOrderId(preSaleOrder.getId());
            List<PreSaleOrderItemModel> preSaleOrderItemModels = preSaleOrderItemDao.list(preSaleOrderItemModel);
            for (PreSaleOrderItemModel itemModel : preSaleOrderItemModels) {
                BigDecimal price = itemModel.getAmount();
                ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                receiveBillItemModel.setCode(preSaleOrder.getCode());
                receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                //写入nc配置
                setNcConfig(receiveBillItemModel, settlementConfigModel.getId());
                receiveBillItemModel.setPoNo(preSaleOrder.getPoNo());
                receiveBillItemModel.setGoodsId(itemModel.getGoodsId());
                receiveBillItemModel.setGoodsNo(itemModel.getGoodsNo());
                receiveBillItemModel.setGoodsName(itemModel.getGoodsName());
                receiveBillItemModel.setPrice(price);
                receiveBillItemModel.setNum(itemModel.getNum());
                receiveBillItemModel.setTaxRate(itemModel.getTaxRate());
                receiveBillItemModel.setTax(itemModel.getTax());
                receiveBillItemModel.setTaxAmount(itemModel.getTaxAmount());
                BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                if (brand != null) {
                    receiveBillItemModel.setBrandId(brand.getBrandId());
                    receiveBillItemModel.setBrandName(brand.getName());
                }
                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                if (brandParent != null) {
                    receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
                receiveBillItemModels.add(receiveBillItemModel);
            }
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        Collections.sort(auditDateList);
        receiveBillModel.setBillDate(auditDateList.get(auditDateList.size()-1));
        return customerId;
    }

    //销售单生成应收账单
    private Long saleOrderGenerate(List<String> relCodeList, Integer merchantId,List<String> orderCodes,
                                    ReceiveBillModel receiveBillModel, List<String> poNoList, String relCodes,
                                    List<ReceiveBillItemModel> receiveBillItemModels,
                                    List<ReceiveBillCostItemModel> receiveBillCostItemModels,
                                    SettlementConfigModel settlementConfigModel) throws Exception {
        Long customerId = null;
        List<Timestamp> auditDateList = new ArrayList<>();
        for (String relCode : relCodeList) {
            //销售订单
            SaleOrderModel saleOrderModel = new SaleOrderModel();
            saleOrderModel.setCode(relCode);
            saleOrderModel.setMerchantId(Long.valueOf(merchantId));
            SaleOrderModel existModel = saleOrderDao.searchByModel(saleOrderModel);
            if (existModel == null) {
                throw new RuntimeException("业务单号:" + relCode + "关联的销售订单不存在");
            } else if (!(DERP_ORDER.SALEORDER_STATE_001.equals(existModel.getState()) || DERP_ORDER.SALEORDER_STATE_003.equals(existModel.getState())
                ||DERP_ORDER.SALEORDER_STATE_018.equals(existModel.getState()))) {
                throw new RuntimeException("业务单号:" + relCode + "关联的销售订单状态不是“待审核”或“已审核”或“已出库”");
            }
            customerId = existModel.getCustomerId();
            receiveBillModel.setCustomerId(existModel.getCustomerId());
            receiveBillModel.setCustomerName(existModel.getCustomerName());
            receiveBillModel.setBuId(existModel.getBuId());
            receiveBillModel.setBuName(existModel.getBuName());
            receiveBillModel.setCurrency(existModel.getCurrency());
            receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_1);
            poNoList.add(existModel.getPoNo());
            if (existModel.getAuditDate() != null) {
                auditDateList.add(existModel.getAuditDate());
            } else {
                auditDateList.add(existModel.getCreateDate());
            }

            SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
            saleOrderItemModel.setOrderId(existModel.getId());
            //查询销售订单表体
            List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.list(saleOrderItemModel);

            for (SaleOrderItemModel itemModel : saleOrderItemModels) {
                ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                receiveBillItemModel.setCode(existModel.getCode());

                receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                //写入nc配置
                setNcConfig(receiveBillItemModel, settlementConfigModel.getId());
                receiveBillItemModel.setPoNo(existModel.getPoNo());
                receiveBillItemModel.setGoodsId(itemModel.getGoodsId());
                receiveBillItemModel.setGoodsNo(itemModel.getGoodsNo());
                receiveBillItemModel.setGoodsName(itemModel.getGoodsName());
                receiveBillItemModel.setPrice(itemModel.getAmount());
                receiveBillItemModel.setNum(itemModel.getNum());

                receiveBillItemModel.setTaxAmount(receiveBillItemModel.getPrice());
                Double taxRate = itemModel.getTaxRate();
                receiveBillItemModel.setTaxRate(taxRate);

                BigDecimal tax = receiveBillItemModel.getPrice().multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = receiveBillItemModel.getPrice().add(tax);
                receiveBillItemModel.setTax(tax);
                receiveBillItemModel.setTaxAmount(taxAmount);

                BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                if (brand != null) {
                    receiveBillItemModel.setBrandId(brand.getBrandId());
                    receiveBillItemModel.setBrandName(brand.getName());
                }
                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                if (brandParent != null) {
                    receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
                receiveBillItemModels.add(receiveBillItemModel);
            }
            orderCodes.add(relCode);
        }

        //已存在的销售退明细
        List<Long> itemIds = new ArrayList<>();
        //根据PO号关联查询到的则取销售退货单表体PO对应的退货商品信息；
        List<SaleReturnOrderItemModel> saleReturnOrderItemModels = saleReturnOrderItemDao.statisticsBySaleCode(relCodeList);
        for (SaleReturnOrderItemModel saleReturnOrderItemModel : saleReturnOrderItemModels) {
            String saleOrderCode = saleReturnOrderItemModel.getSaleOrderCode();
            List<String> saleOrderCodes = Arrays.asList(saleOrderCode.split(","));
            saleOrderCodes.retainAll(poNoList);
            //如果该明细的po与上架单的po不一致，或者该明细已生成过费用明细了则不继续生成
            if (saleOrderCodes.isEmpty() || itemIds.contains(saleReturnOrderItemModel.getId())) {
                continue;
            }
            itemIds.add(saleReturnOrderItemModel.getId());

            ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
            receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
            receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
            receiveBillCostItemModel.setGoodsId(saleReturnOrderItemModel.getInGoodsId());
            receiveBillCostItemModel.setGoodsNo(saleReturnOrderItemModel.getInGoodsNo());
            receiveBillCostItemModel.setGoodsName(saleReturnOrderItemModel.getInGoodsName());
            receiveBillCostItemModel.setPoNo(relCodes);
            receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
            receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
            receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel != null) {
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
            }

            Integer costNum = saleReturnOrderItemModel.getReturnNum() + saleReturnOrderItemModel.getBadGoodsNum();
            BigDecimal price = saleReturnOrderItemModel.getPrice() != null ? saleReturnOrderItemModel.getPrice() : BigDecimal.ZERO;
            BigDecimal amount = price.multiply(new BigDecimal(costNum));
            receiveBillCostItemModel.setNum(costNum);
            receiveBillCostItemModel.setPrice(amount);
            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnOrderItemModel.getInGoodsId());
            if (brandParent != null) {
                receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
            }
            receiveBillCostItemModels.add(receiveBillCostItemModel);
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        if (auditDateList.size() > 0) {
            Collections.sort(auditDateList);
            receiveBillModel.setBillDate(auditDateList.get(auditDateList.size()-1));
        }
        return customerId;
    }

    //采购sd单生成应收账单
    private Long purchaseSDOrderGenerate(List<String> relCodeList, Integer merchantId,
                                         ReceiveBillModel receiveBillModel, List<String> poNoList, String relCodes,
                                         List<ReceiveBillCostItemModel> receiveBillCostItemModels) throws Exception {
        SettlementConfigModel configModel = new SettlementConfigModel();
        configModel.setProjectName("采购返利");
        configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
        configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchByModel(configModel);
        if (settlementConfigModel == null) {
            throw new RuntimeException("没有找到“采购返利”的费项配置");
        }

        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());

        List<Timestamp> inboundDateList = new ArrayList<>();
        Long customerId = null;
        for (String relCode : relCodeList) {
            //采购sd单
            PurchaseSdOrderModel sdOrderModel = new PurchaseSdOrderModel();
            sdOrderModel.setCode(relCode);
            sdOrderModel.setMerchantId(Long.valueOf(merchantId));
            PurchaseSdOrderModel purchaseSdOrderModel = purchaseSdOrderDao.searchByModel(sdOrderModel);

            if (purchaseSdOrderModel == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的采购SD单不存在");
            }

            //判断供应商对应是客户是否存在
            Long supplierId = purchaseSdOrderModel.getSupplierId();
            CustomerInfoMogo customerInfoMogo = getRelCustomerId(supplierId, Long.valueOf(merchantId));
            customerId = customerInfoMogo.getCustomerid();
            receiveBillModel.setCustomerId(customerInfoMogo.getCustomerid());
            receiveBillModel.setCustomerName(customerInfoMogo.getName());
            receiveBillModel.setBuId(purchaseSdOrderModel.getBuId());
            receiveBillModel.setBuName(purchaseSdOrderModel.getBuName());
            receiveBillModel.setCurrency(purchaseSdOrderModel.getCurrency());
            receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_3);
            if (!poNoList.contains(purchaseSdOrderModel.getPoNo())) {
                poNoList.add(purchaseSdOrderModel.getPoNo());
            }
            if (purchaseSdOrderModel.getInboundDate() != null) {
                inboundDateList.add(purchaseSdOrderModel.getInboundDate());
            }

            PurchaseSdOrderSditemModel sdOrderSditemModel = new PurchaseSdOrderSditemModel();
            sdOrderSditemModel.setOrderId(purchaseSdOrderModel.getId());
            List<PurchaseSdOrderSditemModel> purchaseSdOrderSditemModels = purchaseSdOrderSditemDao.list(sdOrderSditemModel);
            for (PurchaseSdOrderSditemModel itemModel : purchaseSdOrderSditemModels) {
                BigDecimal price = itemModel.getSdAmount();
                ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
                receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                if (paymentSubjectModel != null) {
                    //科目
                    receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                    receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                }

                receiveBillCostItemModel.setPoNo(purchaseSdOrderModel.getPoNo());
                receiveBillCostItemModel.setGoodsId(itemModel.getGoodsId());
                receiveBillCostItemModel.setGoodsNo(itemModel.getGoodsNo());
                receiveBillCostItemModel.setGoodsName(itemModel.getGoodsName());

                receiveBillCostItemModel.setNum(itemModel.getNum());

                if (price != null && price.compareTo(BigDecimal.ZERO) > -1) {
                    receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);
                    receiveBillCostItemModel.setPrice(price);
                } else {
                    receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
                    receiveBillCostItemModel.setPrice(price.abs());
                }
                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                if (brandParent != null) {
                    receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
                receiveBillCostItemModels.add(receiveBillCostItemModel);
            }
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        if (!inboundDateList.isEmpty()) {
            Collections.sort(inboundDateList);
            receiveBillModel.setBillDate(inboundDateList.get(inboundDateList.size()-1));
        }

        return customerId;
    }


    //todo:融资申请单生成应收账单
    private Long saleFinancingOrderGenerate(List<String> relCodeList, ReceiveBillModel receiveBillModel,List<String> orderCodes,
                                            List<String> poNoList, String relCodes, List<ReceiveBillItemModel> receiveBillItemModels) throws Exception {
        SettlementConfigModel configModel = new SettlementConfigModel();
        configModel.setProjectName("采销执行收入");
        configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
        configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchByModel(configModel);
        if (settlementConfigModel == null) {
            throw new RuntimeException("没有找到“采销执行收入”的费项配置");
        }
        List<Timestamp> deadlineDateList = new ArrayList<>(); //实际还款日
        List<String> financeCodes = new ArrayList<>(); //赊销单号
        Long customerId = null;
        for (String relCode : relCodeList) {
            //销售订单
            SaleOrderModel saleOrderModel = new SaleOrderModel();
            saleOrderModel.setCode(relCode);
            SaleOrderModel saleOrder = saleOrderDao.searchByModel(saleOrderModel);
            if (saleOrder == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的销售订单不存在");
            }

            //融资申请单
            SaleFinancingOrderModel saleFinancingOrderModel = new SaleFinancingOrderModel();
            saleFinancingOrderModel.setOrderId(saleOrder.getId());
            SaleFinancingOrderModel financingOrderModel = saleFinancingOrderDao.searchByModel(saleFinancingOrderModel);
            if (financingOrderModel == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的融资申请单不存在");
            }

            customerId = saleOrder.getCustomerId();
            receiveBillModel.setCustomerId(saleOrder.getCustomerId());
            receiveBillModel.setCustomerName(saleOrder.getCustomerName());
            receiveBillModel.setBuId(saleOrder.getBuId());
            receiveBillModel.setBuName(saleOrder.getBuName());
            receiveBillModel.setCurrency(financingOrderModel.getCurrency());
            if (!poNoList.contains(saleOrder.getPoNo())) {
                poNoList.add(saleOrder.getPoNo());
            }
            if (financingOrderModel.getDeadlineDate() != null) {
                deadlineDateList.add(financingOrderModel.getDeadlineDate());
            }

            //表体
            SaleFinancingOrderItemModel saleFinancingOrderItemModel = new SaleFinancingOrderItemModel();
            saleFinancingOrderItemModel.setOrderId(financingOrderModel.getId());
            List<SaleFinancingOrderItemModel> financingOrderItemModels = saleFinancingOrderItemDao.list(saleFinancingOrderItemModel);

            for (SaleFinancingOrderItemModel itemModel : financingOrderItemModels) {
                if (itemModel.getRansomAmount() == null) {
                    throw new RuntimeException("业务单号" + relCode + "关联的融资申请单表体赎回金额不能为空！");
                }
                ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                receiveBillItemModel.setCode(saleOrder.getCode());
                receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                //写入nc配置
                setNcConfig(receiveBillItemModel, settlementConfigModel.getId());
                receiveBillItemModel.setPoNo(saleOrder.getPoNo());
                receiveBillItemModel.setGoodsId(itemModel.getGoodsId());
                receiveBillItemModel.setGoodsNo(itemModel.getGoodsNo());
                receiveBillItemModel.setGoodsName(itemModel.getGoodsName());
                receiveBillItemModel.setPrice(itemModel.getRansomAmount());
                receiveBillItemModel.setNum(itemModel.getNum());
                BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                if (brand != null) {
                    receiveBillItemModel.setBrandId(brand.getBrandId());
                    receiveBillItemModel.setBrandName(brand.getName());
                }
                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                if (brandParent != null) {
                    receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
                receiveBillItemModels.add(receiveBillItemModel);
            }
            orderCodes.add(relCode);
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        if (!deadlineDateList.isEmpty()) {
            Collections.sort(deadlineDateList);
            receiveBillModel.setBillDate(deadlineDateList.get(deadlineDateList.size()-1));
        }
        return customerId;
    }


    //销售退订单生成应收账单
    private Long saleReturnOrderGenerate(List<String> relCodeList, Integer merchantId,
                                         ReceiveBillModel receiveBillModel, List<String> poNoList, String relCodes,
                                         List<ReceiveBillItemModel> receiveBillItemModels) throws Exception {
        SettlementConfigModel configModel = new SettlementConfigModel();
        configModel.setProjectName("采购退货");
        configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
        configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchByModel(configModel);
        if (settlementConfigModel == null) {
            throw new RuntimeException("没有找到“采购退货”的费项配置");
        }

        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());

        List<Timestamp> returnInDateList = new ArrayList<>();
        Long customerId = null;
        for (String relCode : relCodeList) {
            //销售退入库单
            SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
            saleReturnIdepotModel.setCode(relCode);
            SaleReturnIdepotModel returnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
            if (returnIdepotModel == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的销售退货入库单不存在");
            }

            SaleReturnOrderModel saleReturnOrderModel = saleReturnOrderDao.searchById(returnIdepotModel.getOrderId());

            if (saleReturnOrderModel == null) {
                throw new RuntimeException("业务单号" + relCode + "关联的销售退订单不存在");
            }

            if (!(DERP_ORDER.SALERETURNORDER_STATUS_012.equals(saleReturnOrderModel.getStatus()) ||
                    DERP_ORDER.SALERETURNORDER_STATUS_007.equals(saleReturnOrderModel.getStatus()))) {
                throw new RuntimeException("业务单号" + relCode + "关联的销售退订单状态不为“已入库”/“已完结”");
            }

            customerId = saleReturnOrderModel.getCustomerId();
            receiveBillModel.setCustomerId(saleReturnOrderModel.getCustomerId());
            receiveBillModel.setCustomerName(saleReturnOrderModel.getCustomerName());
            receiveBillModel.setBuId(saleReturnOrderModel.getBuId());
            receiveBillModel.setBuName(saleReturnOrderModel.getBuName());
            receiveBillModel.setCurrency(saleReturnOrderModel.getCurrency());
            if (!poNoList.contains(saleReturnOrderModel.getPoNo())) {
                poNoList.add(saleReturnOrderModel.getPoNo());
            }
            if (returnIdepotModel.getReturnInDate() != null) {
                returnInDateList.add(returnIdepotModel.getReturnInDate());
            }

            //销售退货单表体
            List<SaleReturnOrderItemModel> returnOrderItemModels = saleReturnOrderItemDao.searchByOrderId(saleReturnOrderModel.getId());
            //po+商品id对应的销售退明细
            Map<String , SaleReturnOrderItemModel> poGoodsPriceMap = new HashMap<>();
            for (SaleReturnOrderItemModel saleReturnOrderItemModel : returnOrderItemModels) {
                String key = saleReturnOrderItemModel.getPoNo() + "_" + saleReturnOrderItemModel.getInGoodsId();
                poGoodsPriceMap.put(key, saleReturnOrderItemModel);
            }

            //对应销售退货入库单表体
            SaleReturnIdepotItemModel idepotItemModel = new SaleReturnIdepotItemModel();
            idepotItemModel.setSreturnIdepotId(returnIdepotModel.getId());
            List<SaleReturnIdepotItemModel> idepotItemModels = saleReturnIdepotItemDao.list(idepotItemModel);

            for (SaleReturnIdepotItemModel saleReturnIdepotItemModel : idepotItemModels) {
                ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                receiveBillItemModel.setCode(returnIdepotModel.getCode());
                receiveBillItemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_0);
                receiveBillItemModel.setPoNo(saleReturnIdepotItemModel.getPoNo());
                receiveBillItemModel.setProjectId(settlementConfigModel.getId());
                receiveBillItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                if (paymentSubjectModel != null) {
                    //科目
                    receiveBillItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                    receiveBillItemModel.setSubjectName(paymentSubjectModel.getSubName());
                }
                receiveBillItemModel.setGoodsId(saleReturnIdepotItemModel.getInGoodsId());
                receiveBillItemModel.setGoodsNo(saleReturnIdepotItemModel.getInGoodsNo());
                receiveBillItemModel.setGoodsName(saleReturnIdepotItemModel.getInGoodsName());

                Integer num = saleReturnIdepotItemModel.getReturnNum() == null ? 0 : saleReturnIdepotItemModel.getReturnNum();
                Integer wornNum = saleReturnIdepotItemModel.getWornNum() == null ? 0 : saleReturnIdepotItemModel.getWornNum();
                num += wornNum;

                SaleReturnOrderItemModel returnOrderItemModel = poGoodsPriceMap.get(saleReturnIdepotItemModel.getPoNo() + "_" + saleReturnIdepotItemModel.getInGoodsId());

                BigDecimal amount = returnOrderItemModel.getPrice().multiply(new BigDecimal(num)).negate();

                BigDecimal tax = amount.multiply(new BigDecimal(returnOrderItemModel.getTaxRate().toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = amount.add(tax);

                receiveBillItemModel.setNum(num);
                receiveBillItemModel.setPrice(amount);
                receiveBillItemModel.setTaxRate(returnOrderItemModel.getTaxRate());
                receiveBillItemModel.setTax(tax);
                receiveBillItemModel.setTaxAmount(taxAmount);

                BrandMongo brand = brandMongoDao.getBrandMongo(saleReturnIdepotItemModel.getInGoodsId());
                if (brand != null) {
                    receiveBillItemModel.setBrandId(brand.getBrandId());
                    receiveBillItemModel.setBrandName(brand.getName());
                }

                BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnIdepotItemModel.getInGoodsId());
                if (brandParent != null) {
                    receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }

                receiveBillItemModels.add(receiveBillItemModel);
            }
        }
        receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        receiveBillModel.setRelCode(relCodes);
        receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
        if (!returnInDateList.isEmpty()) {
            Collections.sort(returnInDateList);
            receiveBillModel.setBillDate(returnInDateList.get(returnInDateList.size()-1));
        }

        return customerId;
    }


    /**
     * 保存附件信息
     * @param code 应收账单单号
     * @param sourceFileUrl 源文件地址
     * @param targetName 目标文件名称
     */
    private void saveAttachmentFile(String sourceFileUrl, String targetName, String code, Long userId, String userName) throws Exception {
        byte[] fileBytes = DownloadUtil.loadFileByteFromURL(sourceFileUrl);
        String ext = sourceFileUrl.substring(sourceFileUrl.lastIndexOf(".")+1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName",targetName+"."+ext);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt",ext);
        jsonObject.put("fileSize",String.valueOf(fileBytes.length));
        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        if(jsonObj.getInt("code")!= 200){
            throw new RuntimeException("保存附件失败！");
        }
        //返回文件服务器存储URL
        String saveUrl = jsonObj.getString("url") ;
        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
        attachmentInfoMongo.setAttachmentName(targetName+"."+ext); 		//附件名
        attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileBytes.length)); 		//附件大小
        attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
        attachmentInfoMongo.setCreatorName("账单同步");
        attachmentInfoMongo.setCreateDate(TimeUtils.getNow());
        attachmentInfoMongo.setRelationCode(code);              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule("应收账单");
        attachmentInfoMongoDao.insert(attachmentInfoMongo);
    }

    /**
     * 获取客户的配置
     */
    private CustomerMerchantRelMongo getCustomerConfig(Long customerId, Long merchantId, ReceiveBillModel receiveBillModel) {
        Map<String, Object> customeParams = new HashMap<>();
        customeParams.put("customerid", customerId);
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customeParams);
        if (customer == null) {
            throw new RuntimeException("客户不存在！");
        }
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("customerId", customerId);
        customerParams.put("merchantId", merchantId);
        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(customerParams);
        if (customerRel == null) {
            throw new RuntimeException("客户关联不存在！");
        }
        receiveBillModel.setNcChannelCode(customer.getNcChannelCode());
        receiveBillModel.setNcPlatformCode(customer.getNcPlatformCode());
//        receiveBillModel.setSettlementType(customerRel.getSettlementType());
        return customerRel;
    }

    /**
     * 根据供应商id查找对应客户
     */
    private CustomerInfoMogo getRelCustomerId(Long supplierId, Long merchantId) {
        Map<String, Object> supplierParams = new HashMap<>();
        supplierParams.put("customerid", supplierId);
        supplierParams.put("cusType", "2");
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(supplierParams);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在！");
        }
        String mainId = supplier.getMainId();
        if (StringUtils.isBlank(mainId)) {
            throw new RuntimeException("供应商主数据ID为空！");
        }
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("mainId", mainId);
        allParams.put("status", "1");
        allParams.put("cusType", "1");
        List<CustomerInfoMogo> allCustomer = customerInfoMongoDao.findAll(allParams);

        List<CustomerInfoMogo> customers = new ArrayList<>();
        for (CustomerInfoMogo customerInfoMogo : allCustomer) {
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("customerId", customerInfoMogo.getCustomerid());
            customerParams.put("merchantId", merchantId);
            CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(customerParams);
            if (customerRel != null) {
                customers.add(customerInfoMogo);
            }
        }
        if (customers.isEmpty()) {
            throw new RuntimeException("客户关联不存在！");
        }
        return customers.get(0);
    }

    private void setNcConfig(ReceiveBillItemModel receiveBillItemModel, Long projectId) throws SQLException {
        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);
        if (settlementConfigModel == null) {
            throw new RuntimeException("费用项目不存在");
        }
        receiveBillItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
        receiveBillItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
        if (paymentSubjectModel != null) {
            //科目
            receiveBillItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
            receiveBillItemModel.setSubjectName(paymentSubjectModel.getSubName());
        }

    }

    /**
     * @Description 根据销售订单号查询对应的预收单
     * @param orderCodes 订单号集合
     * @return
     */
    private List<ReceiveBillVerifyItemModel> getVerifyItems(List<String> orderCodes) throws SQLException {
        List<ReceiveBillVerifyItemModel> verifyItemModels = new ArrayList<>();
        List<Long> advanceIds = new ArrayList<>();

        List<AdvanceBillItemDTO> advanceBillItemDTOS = advanceBillItemDao.listWithoutVerify(orderCodes);

        for (AdvanceBillItemDTO itemDTO : advanceBillItemDTOS) {
            advanceIds.add(itemDTO.getAdvanceId());
        }

        Map<Long, AdvanceBillOperateItemModel> auditDateMap = new HashMap<>();
        Map<Long, AdvanceBillVerifyItemModel> verifyDateMap = new HashMap<>();
        if (!advanceIds.isEmpty()) {

            //预收款单的审核通过的最新时间
            List<AdvanceBillOperateItemModel> operateItemModels = advanceBillOperateItemDao.getLatestAuditModelByAdvanceIds(advanceIds);

            for (AdvanceBillOperateItemModel operateItemModel : operateItemModels) {
                auditDateMap.put(operateItemModel.getAdvanceId(), operateItemModel);
            }

            //预收款单的核销收款的最新时间
            List<AdvanceBillVerifyItemModel> verifyModelByAdvanceIds = advanceBillVerifyItemDao.getLatestVerifyModelByAdvanceIds(advanceIds);

            for (AdvanceBillVerifyItemModel verifyItemModel : verifyModelByAdvanceIds) {
                verifyDateMap.put(verifyItemModel.getAdvanceId(), verifyItemModel);
            }

        }

        for (AdvanceBillItemDTO itemDTO : advanceBillItemDTOS) {
            ReceiveBillVerifyItemModel receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
            receiveBillVerifyItemModel.setAdvanceId(itemDTO.getId());
            receiveBillVerifyItemModel.setAdvanceCode(itemDTO.getAdvanceCode());
            receiveBillVerifyItemModel.setPrice(itemDTO.getAmount());
            receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
            receiveBillVerifyItemModel.setReceiceNo(itemDTO.getRelCode());

            AdvanceBillOperateItemModel operateItemModel = auditDateMap.get(itemDTO.getAdvanceId());
            receiveBillVerifyItemModel.setVerifyDate(new Timestamp(operateItemModel.getOperateDate().getTime()));
            receiveBillVerifyItemModel.setVerifyId(operateItemModel.getOperateId());
            receiveBillVerifyItemModel.setVerifier(operateItemModel.getOperater());

            AdvanceBillVerifyItemModel verifyItemModel = verifyDateMap.get(itemDTO.getAdvanceId());
            if (verifyItemModel != null) {
                receiveBillVerifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
            }
            verifyItemModels.add(receiveBillVerifyItemModel);
        }

        return verifyItemModels;
    }
}
