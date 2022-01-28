package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.service.timer.SaveToCReceiveBillByStatementOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 根据平台结算单生成ToC应收账单
 * @Author: Chen Yiluan
 * @Date: 2021/03/09 14:17
 **/
@Service
public class SaveToCReceiveBillByStatementOrderServiceImpl implements SaveToCReceiveBillByStatementOrderService {

    @Autowired
    private PlatformStatementOrderDao platformStatementOrderDao;
    @Autowired
    private PlatformStatementItemDao platformStatementItemDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;


    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160004,model=DERP_LOG_POINT.POINT_13201160004_Label)
    public boolean saveToCReceiveBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String billIds = (String) jsonData.get("billIds");
        Integer userId = (Integer) jsonData.get("userId");
        String userName = (String) jsonData.get("userName");
        String month = (String) jsonData.get("month");
        List<PlatformStatementOrderModel> platformStatementOrderModels = new ArrayList<>();
        if (StringUtils.isNotBlank(billIds)) {
            String[] billIdArr = billIds.split(",");
            for (int i = 0; i < billIdArr.length; i++) {
                PlatformStatementOrderModel platformStatementOrderModel = platformStatementOrderDao.searchById(Long.valueOf(billIdArr[i]));

                if (platformStatementOrderModel == null) {
                   throw new RuntimeException("平台结算单不存在！") ;
                }

                if (DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1.equals(platformStatementOrderModel.getIsCreateReceive())) {
                    throw new RuntimeException("平台结算单单号:" + platformStatementOrderModel.getBillCode() + "已生成应收账单！") ;
                }

                if (!DERP_ORDER.PLATFORM_STATEMENT_TYPE_2.equals(platformStatementOrderModel.getType())) {
                    throw new RuntimeException("平台结算单单号:" + platformStatementOrderModel.getBillCode() + "不是ToC类型结算单！") ;
                }
                platformStatementOrderModels.add(platformStatementOrderModel);
            }
        } else {
            //定时器触发：查询未生成Toc应收且账单状态为“ToC”的平台结算单
            if (!StringUtils.isNotBlank(month)) {
                java.util.Date today = new java.util.Date();
                month = TimeUtils.getLastMonth(today);
            }
            PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel();
            platformStatementOrderModel.setIsCreateReceive(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
            platformStatementOrderModel.setType(DERP_ORDER.PLATFORM_STATEMENT_TYPE_2);
            platformStatementOrderModel.setMonth(month);
            platformStatementOrderModels = platformStatementOrderDao.list(platformStatementOrderModel);
        }

        if (platformStatementOrderModels == null || platformStatementOrderModels.isEmpty())  {
            throw new RuntimeException("暂无平台结算生成ToC应收账单") ;
        }

        List<TocSettlementReceiveBillModel> newToCReceiveBillModels = new ArrayList<>();

        //根据“公司+平台天猫+相同店铺+相同事业部+相同结算币种+相同月份”维度遍历生成ToC应收账单
        for (PlatformStatementOrderModel platformStatementOrderModel : platformStatementOrderModels) {
            //根据店铺编码取平台编码
            Map<String, Object> shopParams = new HashMap<>();
            shopParams.put("shopCode", platformStatementOrderModel.getShopCode());
            MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
            if (shop == null) {
                throw new RuntimeException("店铺为空");
            }

            if (StringUtils.isBlank(shop.getSuperiorParentBrandNcCode())) {
                throw new RuntimeException("店铺的专营母品牌未维护");
            }


            //获取所有母品牌
            Map<String, BrandParentMongo> brandParentMap = new HashMap<>();

            //查询平台结算单对应表体数量
            Map<String, Object> platformOrderNumMap = platformStatementItemDao.statisticsByPlatformIdList(platformStatementOrderModel.getId());
            Long num = (Long) platformOrderNumMap.get("num");
            BigDecimal settlementAmount = (BigDecimal) platformOrderNumMap.get("settlementAmount");
            BigDecimal settlementAmountRmb = (BigDecimal) platformOrderNumMap.get("settlementAmountRmb");
            //分页查询表体明细
            Integer pageSize = 1000;

            //生成toc应收账单
            TocSettlementReceiveBillModel settlementReceiveBillModel = new TocSettlementReceiveBillModel();
            settlementReceiveBillModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS));
            settlementReceiveBillModel.setBuId(platformStatementOrderModel.getBuId());
            settlementReceiveBillModel.setBuName(platformStatementOrderModel.getBuName());
            settlementReceiveBillModel.setCustomerId(platformStatementOrderModel.getCustomerId());
            settlementReceiveBillModel.setCustomerName(platformStatementOrderModel.getCustomerName());
            settlementReceiveBillModel.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            settlementReceiveBillModel.setShopCode(shop.getShopCode());
            settlementReceiveBillModel.setShopName(shop.getShopName());
            settlementReceiveBillModel.setSettlementCurrency(DERP.CURRENCYCODE_CNY);
            settlementReceiveBillModel.setMerchantId(platformStatementOrderModel.getMerchantId());
            settlementReceiveBillModel.setMerchantName(platformStatementOrderModel.getMerchantName());
            settlementReceiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
            settlementReceiveBillModel.setNcStatus(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10);
            settlementReceiveBillModel.setReceivableAmount(settlementAmountRmb);
            settlementReceiveBillModel.setSettlementDate(new Date(TimeUtils.parseDay(platformStatementOrderModel.getMonth() + "-01").getTime()));
            settlementReceiveBillModel.setCreateDate(TimeUtils.getNow());
            settlementReceiveBillModel.setCreater("系统生成");
            settlementReceiveBillModel.setOriCurrency(platformStatementOrderModel.getCurrency());
            settlementReceiveBillModel.setOriReceivableAmount(settlementAmount);

            if (userId != null) {
                settlementReceiveBillModel.setCreaterId(Long.valueOf(userId));
                settlementReceiveBillModel.setCreater(userName);
            }

            settlementReceiveBillModel.setInvoiceStatus(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_00);
            settlementReceiveBillModel.setExternalCode(platformStatementOrderModel.getBillCode());
            if (shop != null) {
                settlementReceiveBillModel.setStorePlatformCode(shop.getStorePlatformCode());
            }

            Long id = tocSettlementReceiveBillDao.save(settlementReceiveBillModel);

            newToCReceiveBillModels.add(settlementReceiveBillModel);

            //查询成功订单号
            Map<String, OrderModel> successOrderList = new HashMap<>();
            Map<String, List<OrderItemModel>> successOrderItemMap = new HashMap<>();

            for (int i = 0; i < num; ) {
                //新增应收明细表体
                List<TocSettlementReceiveBillItemModel> itemModels = new ArrayList<>();
                //新增费用明细表体
                List<TocSettlementReceiveBillCostItemModel> costItemModels = new ArrayList<>();

                Map<Long, List<TocSettlementReceiveBillCostItemDTO>> costItemDTOMap = new HashMap<>();

                List<Long> goodsIdList = new ArrayList<>();
                Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

                PlatformStatementItemDTO itemDTO = new PlatformStatementItemDTO();
                itemDTO.setPlatformStatementOrderId(platformStatementOrderModel.getId());
                itemDTO.setBegin(i);
                itemDTO.setPageSize(pageSize);

                PlatformStatementItemDTO platformStatementItem = platformStatementItemDao.getListByPage(itemDTO);
                List<PlatformStatementItemDTO> platformStatementItemDTOS = platformStatementItem.getList();
                List<String> exorderCodeList = new ArrayList<>();
                for (PlatformStatementItemDTO platformStatementItemDTO : platformStatementItemDTOS) {
                    if (!exorderCodeList.contains(platformStatementItemDTO.getPoNo())) {
                        exorderCodeList.add(platformStatementItemDTO.getPoNo());
                    }
                }

                //查询订单信息
                List<OrderModel> existOrderList = orderDao.selectByExternalCode(exorderCodeList, platformStatementOrderModel.getMerchantId());
                for (OrderModel orderModel : existOrderList) {
                    successOrderList.put(orderModel.getExternalCode(), orderModel);
                    exorderCodeList.remove(orderModel.getExternalCode());
                }
                if (!exorderCodeList.isEmpty()) {
                    List<OrderModel> existOrderIdList = orderDao.selectByExOrderId(exorderCodeList, platformStatementOrderModel.getMerchantId());
                    for (OrderModel orderModel : existOrderIdList) {
                        successOrderList.put(orderModel.getExOrderId(), orderModel);
                        exorderCodeList.remove(orderModel.getExOrderId());
                    }
                }

                for (PlatformStatementItemDTO platformStatementItemDTO : platformStatementItemDTOS) {
                    String externalCode = platformStatementItemDTO.getPoNo();

                    //生成应收明细
                    if ("经销业务TO C收入".equals(platformStatementItemDTO.getSettlementConfigName())) {
                        if (successOrderList.containsKey(externalCode)) {
                            OrderModel order = successOrderList.get(externalCode);
                            List<OrderItemModel> orderItemModels = successOrderItemMap.get(externalCode);
                            if (orderItemModels == null) {
                                OrderItemModel item = new OrderItemModel();
                                item.setOrderId(order.getId());
                                orderItemModels  = orderItemDao.list(item);
                                successOrderItemMap.put(externalCode, orderItemModels);
                            }
                            Map<String, BigDecimal> tempPriceMap = calculateTempPrice(orderItemModels, platformStatementItemDTO.getSettlementAmount());
                            Map<String, BigDecimal> tempRMBPriceMap = calculateTempPrice(orderItemModels, platformStatementItemDTO.getSettlementAmountRmb());
                            for (OrderItemModel orderItemModel : orderItemModels) {
                                TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                                billItemModel.setBillId(id);
                                billItemModel.setProjectId(platformStatementItemDTO.getSettlementConfigId());
                                billItemModel.setProjectName(platformStatementItemDTO.getSettlementConfigName());
                                billItemModel.setPlatformProjectId(platformStatementItemDTO.getPlatformSettlementConfigId());
                                billItemModel.setPlatformProjectName(platformStatementItemDTO.getPlatformSettlementConfigName());
                                billItemModel.setSettlementRate(platformStatementItemDTO.getRate());
                                billItemModel.setExternalCode(externalCode);
                                billItemModel.setOrderCode(order.getCode());
                                billItemModel.setGoodsId(orderItemModel.getGoodsId());
                                billItemModel.setGoodsNo(orderItemModel.getGoodsNo());
                                billItemModel.setGoodsName(orderItemModel.getGoodsName());
                                billItemModel.setNum(orderItemModel.getNum());

                                billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                                if (platformStatementItemDTO.getSettlementAmountRmb().compareTo(BigDecimal.ZERO) > -1) {
                                    billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                                }

                                String itemKey = orderItemModel.getOrderId() + "_" + orderItemModel.getId();
                                billItemModel.setOriginalAmount(tempPriceMap.get(itemKey).abs());
                                billItemModel.setRmbAmount(tempRMBPriceMap.get(itemKey).abs());
                                itemModels.add(billItemModel);

                                if (!goodsIdList.contains(orderItemModel.getGoodsId())) {
                                    goodsIdList.add(orderItemModel.getGoodsId());
                                }
                            }
                        } else {
                            TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                            billItemModel.setBillId(id);
                            billItemModel.setProjectId(platformStatementItemDTO.getSettlementConfigId());
                            billItemModel.setProjectName(platformStatementItemDTO.getSettlementConfigName());
                            billItemModel.setPlatformProjectId(platformStatementItemDTO.getPlatformSettlementConfigId());
                            billItemModel.setPlatformProjectName(platformStatementItemDTO.getPlatformSettlementConfigName());
                            billItemModel.setSettlementRate(platformStatementItemDTO.getRate());
                            billItemModel.setExternalCode(externalCode);

                            billItemModel.setParentBrandName(shop.getSuperiorParentBrandName());
                            billItemModel.setParentBrandCode(shop.getSuperiorParentBrandNcCode());
                            billItemModel.setParentBrandId(shop.getSuperiorParentBrandId());

                            billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                            if (platformStatementItemDTO.getSettlementAmountRmb().compareTo(BigDecimal.ZERO) > -1) {
                                billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                            }

                            billItemModel.setOriginalAmount(platformStatementItemDTO.getSettlementAmount().abs());
                            billItemModel.setRmbAmount(platformStatementItemDTO.getSettlementAmountRmb().abs());

                            itemModels.add(billItemModel);
                        }
                    } else { //生成费用明细
                        if (successOrderList.containsKey(externalCode)) {
                            OrderModel order = successOrderList.get(externalCode);

                            TocSettlementReceiveBillCostItemDTO costItemDTO = new TocSettlementReceiveBillCostItemDTO();
                            costItemDTO.setBillId(id);
                            costItemDTO.setProjectId(platformStatementItemDTO.getSettlementConfigId());
                            costItemDTO.setProjectName(platformStatementItemDTO.getSettlementConfigName());
                            costItemDTO.setPlatformProjectId(platformStatementItemDTO.getPlatformSettlementConfigId());
                            costItemDTO.setPlatformProjectName(platformStatementItemDTO.getPlatformSettlementConfigName());
                            costItemDTO.setExternalCode(externalCode);
                            costItemDTO.setOrderCode(order.getCode());
                            costItemDTO.setSettlementRate(platformStatementItemDTO.getRate());
                            costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);

                            if (platformStatementItemDTO.getSettlementAmountRmb().compareTo(BigDecimal.ZERO) > -1) {
                                costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                            }

                            costItemDTO.setOriginalAmount(platformStatementItemDTO.getSettlementAmount().abs());
                            costItemDTO.setRmbAmount(platformStatementItemDTO.getSettlementAmountRmb().abs());

                            List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = new ArrayList<>();
                            if (costItemDTOMap.containsKey(order.getId())) {
                                costItemDTOS.addAll(costItemDTOMap.get(order.getId()));
                            }
                            costItemDTOS.add(costItemDTO);
                            costItemDTOMap.put(order.getId(), costItemDTOS);
                        } else {
                            TocSettlementReceiveBillCostItemModel billItemModel = new TocSettlementReceiveBillCostItemModel();
                            billItemModel.setBillId(id);
                            billItemModel.setProjectId(platformStatementItemDTO.getSettlementConfigId());
                            billItemModel.setProjectName(platformStatementItemDTO.getSettlementConfigName());
                            billItemModel.setPlatformProjectId(platformStatementItemDTO.getPlatformSettlementConfigId());
                            billItemModel.setPlatformProjectName(platformStatementItemDTO.getPlatformSettlementConfigName());
                            billItemModel.setSettlementRate(platformStatementItemDTO.getRate());
                            billItemModel.setExternalCode(externalCode);
                            billItemModel.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_0);
                            billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                            if (platformStatementItemDTO.getSettlementAmountRmb().compareTo(BigDecimal.ZERO) > -1) {
                                billItemModel.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                            }
                            billItemModel.setOriginalAmount(platformStatementItemDTO.getSettlementAmount().abs());
                            billItemModel.setRmbAmount(platformStatementItemDTO.getSettlementAmountRmb().abs());
                            billItemModel.setParentBrandName(shop.getSuperiorParentBrandName());
                            billItemModel.setParentBrandCode(shop.getSuperiorParentBrandNcCode());
                            billItemModel.setParentBrandId(shop.getSuperiorParentBrandId());
                            costItemModels.add(billItemModel);
                        }
                    }

                }

                List<Long> orderIds = new ArrayList<>(costItemDTOMap.keySet());

                for (int j = 0; j < orderIds.size(); ) {
                    int pageSub = (j+pageSize) < orderIds.size() ? (j+pageSize) : orderIds.size();
                    //根据外部订单号查询对应的结算金额最高的商品明细
                    List<OrderItemModel> orderItemModels = orderItemDao.getMaxPriceByOrderId(orderIds.subList(j, pageSub), false);

                    for (OrderItemModel itemModel : orderItemModels) {
                        List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = costItemDTOMap.get(itemModel.getOrderId());

                        for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                            costItemDTO.setGoodsId(itemModel.getGoodsId());
                            costItemDTO.setNum(itemModel.getNum());
                        }
                        if (!goodsIdList.contains(itemModel.getGoodsId())) {
                            goodsIdList.add(itemModel.getGoodsId());
                        }
                    }

                    j = pageSub;
                }

                //批量查询商品信息及母品牌
                if (!goodsIdList.isEmpty()) {
                    List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                    for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                        if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                            merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                        }
                    }

                    for (TocSettlementReceiveBillItemModel itemModel : itemModels) {
                        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(itemModel.getGoodsId());
                        if (merchandiseInfo != null) {
                            BrandParentMongo brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                            if (brandParent == null) {
                                brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                            }

                            if (brandParent != null) {
                                itemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                itemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                itemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                            }
                        }
                    }

                    for (Long key : costItemDTOMap.keySet()) {
                        List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = costItemDTOMap.get(key);

                        BrandParentMongo brandParent = null;
                        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(costItemDTOS.get(0).getGoodsId());
                        if (merchandiseInfo != null) {
                            brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                            if (brandParent == null) {
                                brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                            }

                        }

                        for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                            TocSettlementReceiveBillCostItemModel costItemModel = new TocSettlementReceiveBillCostItemModel();
                            BeanUtils.copyProperties(costItemDTO, costItemModel);

                            if (brandParent != null) {
                                costItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                costItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                costItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                            }

                            costItemModels.add(costItemModel);
                        }
                    }

                }

                //批量插入应收明细和费用明细
                if (!itemModels.isEmpty()) {
                    tocSettlementReceiveBillItemDao.batchSave(itemModels);
                }
                if (!costItemModels.isEmpty()) {
                    tocSettlementReceiveBillCostItemDao.batchSave(costItemModels);
                }
                int pageSub = (i+pageSize) < num.intValue() ? (i+pageSize) : num.intValue();
                i = pageSub;
            }

            //平台结算单的附件
            Map<String, Object> attachmentParam = new HashMap<>();
            attachmentParam.put("relationCode", platformStatementOrderModel.getBillCode()+"_"+platformStatementOrderModel.getCurrency());
            List<AttachmentInfoMongo> oriAttachmentInfos = attachmentInfoMongoDao.findAll(attachmentParam);

            //保存附件
            for (AttachmentInfoMongo attachmentInfoMongo : oriAttachmentInfos) {
                attachmentInfoMongo.setRelationCode(settlementReceiveBillModel.getCode());
                attachmentInfoMongo.setCreatorName("toc账单生成");
                attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
                attachmentInfoMongoDao.insert(attachmentInfoMongo);
            }

            List<Long> platformIds = new ArrayList<>();
            platformIds.add(platformStatementOrderModel.getId());
            //更新平台结算单状态为已生成应收单，并回填应收账单号
            platformStatementOrderDao.batchUpdate(platformIds, settlementReceiveBillModel.getCode(),DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1);

        }

        for (TocSettlementReceiveBillModel receiveBillModel : newToCReceiveBillModels) {
            /**
             * 发送审核提醒邮件
             */
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO();

            emailUserDTO.setBuId(receiveBillModel.getBuId());
            emailUserDTO.setBuName(receiveBillModel.getBuName());
            emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
            emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
            emailUserDTO.setOrderCode(receiveBillModel.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_17);
            emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
            emailUserDTO.setAmount(receiveBillModel.getSettlementCurrency() + "&nbsp;" + receiveBillModel.getReceivableAmount().toPlainString());
            if (userId != null) {
                emailUserDTO.setUserName(userName);
                emailUserDTO.setCreateId(Long.valueOf(userId));
            }
            emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, receiveBillModel.getStorePlatformCode()));

            JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

            rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        }


        return true;
    }


    /**
     * 摊分金额
     */
    private Map<String, BigDecimal> calculateTempPrice(List<OrderItemModel> orderItemModels, BigDecimal amount) {

        orderItemModels = orderItemModels.stream().sorted(Comparator.comparing(OrderItemModel::getDecTotal))
                .collect(Collectors.toList()) ;

        Map<String, BigDecimal> calculateTempPriceMap = new HashMap<>();
        //订单所有商品结算总额合计
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItemModel itemModel : orderItemModels) {
            totalPrice = totalPrice.add(itemModel.getDecTotal().abs());
        }
        //已经摊分的金额
        BigDecimal alreadyPrice = new BigDecimal("0");
        /**分摊公式为：前N-1个商品分摊的暂估应收金额= （系统订单商品结算总额/订单所有商品结算总额合计）*导入的订单结算金额，金额四舍五入，保留2位小数；
         * 最后一个商品采用倒减=订单实付总额-前面已分摊的商品结算金额（已做四舍五入的金额）汇总；*/
        for (int i = 0; i < orderItemModels.size(); i++) {
            String key = orderItemModels.get(i).getOrderId() + "_" + orderItemModels.get(i).getId();
            if (totalPrice.compareTo(new BigDecimal("0")) != 0) {
                if (i == orderItemModels.size()-1) {
                    BigDecimal tempPrice = amount.subtract(alreadyPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                    calculateTempPriceMap.put(key, tempPrice);
                } else {
                    BigDecimal tempPrice = amount.multiply(orderItemModels.get(i).getDecTotal().abs()).divide(totalPrice, 2, BigDecimal.ROUND_HALF_UP);
                    alreadyPrice = alreadyPrice.add(tempPrice);
                    calculateTempPriceMap.put(key, tempPrice);
                }
            } else {
                calculateTempPriceMap.put(key,new BigDecimal("0"));
            }
        }
        return calculateTempPriceMap;
    }
}
