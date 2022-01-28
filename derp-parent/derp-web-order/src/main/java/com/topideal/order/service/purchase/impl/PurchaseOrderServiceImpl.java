package com.topideal.order.service.purchase.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.topideal.api.sapience.SapienceUtils;
import com.topideal.api.sapience.sapience009.FileUploadEntity;
import com.topideal.api.sapience.sapience009.FinancingApplyRequest;
import com.topideal.api.sapience.sapience009.Goods;
import com.topideal.api.sapience.sapience010.FinancingAttachmentDownloadRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.*;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.log.CommonMQLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SdPurchaseConfigDao;
import com.topideal.dao.common.SdPurchaseConfigItemDao;
import com.topideal.dao.common.TradeLinkConfigDao;
import com.topideal.dao.purchase.*;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.purchase.*;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j07.InventoryWriteOffRootJson;
import com.topideal.json.pushapi.ywms.purchase.push.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseOrderService;
import com.topideal.order.service.sale.SaleOrderService;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.pdf.FreemakerUtils;
import com.topideal.order.tools.pdf.PdfPageHelper;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.purchase.form.DeclareOrderDeliveryItemForm;
import com.topideal.order.webapi.purchase.form.PurchaseWarehouseForm;
import com.topideal.webService.OAUtils;
import com.topideal.webService.oa.dto.WorkflowBaseInfoWrap;
import com.topideal.webService.oa.dto.WorkflowMainTableInfoWrap;
import com.topideal.webService.oa.dto.WorkflowRequestInfoWrap;
import com.topideal.webService.oa.dto.WorkflowRequestTableFieldWrap;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 采购订单service实现类
 *
 * @author zhanghx
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    /* 打印日志 */
    protected Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    // 采购订单表头
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    // 采购订单表体
    @Autowired
    private PurchaseOrderItemDao purchaseOrderItemDao;
    // 预申报单表体
    @Autowired
    private DeclareOrderItemDao declareOrderItemDao;
    // 采购入库单
    @Autowired
    private PurchaseWarehouseDao purchaseWarehouseDao;
    // 采购入库单表体
    @Autowired
    private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
    // 勾稽表
    @Autowired
    private WarehouseOrderRelDao warehouseOrderRelDao;
    // 仓库
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    // 仓库商家关联
    @Autowired
    private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
    // 商品
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    // 客户
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    // mq
    @Autowired
    private RMQProducer rocketMQProducer;
    // 批次入库单
    @Autowired
    private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
    // lbx
    @Autowired
    private LbxNoMongoDao lbxNoMongoDao;
    // 商家
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    // 销售订单表头
    @Autowired
    private SaleOrderDao saleOrderDao;
    // 销售订单表体
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    // 合同
    @Autowired
    private ContractNoMongoDao contractNoMongoDao;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao ;
    @Autowired
    private PurchaseContractDao purchaseContractDao ;
    @Autowired
    private BrandMongoDao brandMongoDao ;
    @Autowired
    private PreSaleOrderDao preSaleOrderDao ;
    @Autowired
    private PreSaleOrderItemDao preSaleOrderItemDao ;
    // 商家
    @Autowired
    private MerchantInfoMongoDao merchantMongoDao;

    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;

    @Autowired
    private FileTempDataMongoDao fileTempDataMongoDao;

    @Autowired
    private FileTempDao fileTempDao ;

    @Autowired
    private TradeLinkConfigDao tradeLinkConfigDao ;

    @Autowired
    private PurchaseLinkInfoDao purchaseLinkInfoDao ;

    @Autowired
    private SupplierMerchandisePriceMongoDao supplierMerchandisePriceMongoDao ;

    @Autowired
    private SdPurchaseConfigDao sdPurchaseConfigDao ;

    @Autowired
    private PurchaseSdOrderDao purchaseSdOrderDao ;

    @Autowired
    private CommonBusinessService commonBusinessService ;

    @Autowired
    private SaleOutDepotDao saleOutDepotDao;

    @Autowired
    private SaleOutDepotItemDao saleOutDepotItemDao;

    @Autowired
    private SaleAnalysisDao saleAnalysisDao;

    @Autowired
    private CountryOriginMongoDao countryOriginMongoDao ;

    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao ;

    @Autowired
    private DepotCustomsRelMongoDao depotCustomsRelMongoDao ;

    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao ;

    @Autowired
    private UnitMongoDao unitMongoDao ;

    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;

    @Autowired
    private SdPurchaseConfigItemDao sdPurchaseConfigItemDao ;

    @Autowired
    private PurchaseInvoiceItemDao purchaseInvoiceItemDao ;

    @Autowired
    private CustomerSalePriceMongoDao customerSalePriceMongoDao;

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private PurchaseReturnRelDao purchaseReturnRelDao;
    @Autowired
    private OperateLogDao operateLogDao;
    @Autowired
    private PurchaseFrameContractDao frameContractDao;
    @Autowired
    private PurchaseTryApplyOrderDao tryApplyOrderDao;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao;
    @Autowired
    private OrderExternalCodeDao orderExternalCodeDao;
    @Autowired
    private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
    @Autowired
    private BusinessUnitMongoDao businessUnitMongoDao;

    /**
     * 列表（分页）
     *
     * @param dto
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user)
            throws Exception {

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }

        dto = purchaseOrderDao.getlistByPage(dto);
        List<PurchaseOrderDTO> list = dto.getList();

        Map<Long, DepotInfoMongo> depotCache = new HashMap<Long, DepotInfoMongo>() ;

        for (PurchaseOrderDTO d : list) {
            // 根据预申报单id获取采购订单编码集合
//            List<String> resultList = purchaseOrderDao.getWarehouseCodeById(d.getId());
//            if (!resultList.isEmpty()) {
//                String warehouseCode = "";
//                for (String temp : resultList) {
//                    warehouseCode += temp + ",";
//                }
//                d.setWarehouseCode(warehouseCode.substring(0, warehouseCode.length() - 1));
//            }
            // 设值采购总金额
//            PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
//            itemModel.setPurchaseOrderId(d.getId());
//            List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(itemModel);
//            BigDecimal amount = new BigDecimal(0);
//            Integer num = 0 ;
//            for (int i = 0; i < itemList.size(); i++) {
//                PurchaseOrderItemModel item = itemList.get(i);
//                if(item.getAmount()!=null){
//                    amount = amount.add(item.getAmount());
//                }
//                num = num + item.getNum() ;
//            }
//            d.setGoodsAmount(amount);
//            d.setTotalNum(num);

            /**是否可开票标示*/
            d.setIsInvoiceAble("1");

            Map<String, Object> queryInvoiceItemMap = new HashMap<String, Object>() ;
            queryInvoiceItemMap.put("purchaseOrderId", d.getId()) ;

            PurchaseInvoiceItemModel countModel = purchaseInvoiceItemDao.getInvoiceNum(queryInvoiceItemMap) ;

            if(countModel != null && countModel.getNum() != null && d.getTotalNum().intValue() == countModel.getNum().intValue()) {
                d.setIsInvoiceAble("0");
            }

            if(DERP_ORDER.PURCHASEORDER_STATUS_001.equals(d.getStatus())) {
                d.setIsInvoiceAble("0");
            }

            d.setIsInDepotAble("0");
            /**是否可以打托入库*/

//            if(d.getDepotId() != null){
//                DepotInfoMongo depotInfoMongo = depotCache.get(d.getDepotId());
//
//                if(depotInfoMongo == null) {
//                    Map<String,Object> queryMap = new HashMap<String, Object>() ;
//
//                    queryMap.put("depotId", d.getDepotId()) ;
//                    depotInfoMongo = depotInfoMongoDao.findOne(queryMap) ;
//
//                    if(depotInfoMongo != null) {
//                        depotCache.put(d.getDepotId(), depotInfoMongo) ;
//                    }
//
//                }
//                if(d.getDepotId() != null){
//                    Map<String, Object> depotMerchantMap = new HashMap<String, Object>() ;
//                    depotMerchantMap.put("depotId", d.getDepotId()) ;
//                    depotMerchantMap.put("merchantId", d.getMerchantId()) ;
//
//                    depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantMap);
//                }
//            }

            if(d.getDepotId() != null){
                DepotMerchantRelMongo depotMerchantRel = null;
                Map<String, Object> depotMerchantMap = new HashMap<String, Object>() ;
                depotMerchantMap.put("depotId", d.getDepotId()) ;
                depotMerchantMap.put("merchantId", d.getMerchantId()) ;
                depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantMap);
                /**若生成预申报单标识为“否”且入库仓库进出口指令为“否”对“已审核、部分出库”状态的订单可在采购订单操作上架入库*/
                if(d.getIsGenerate().equals(DERP_ORDER.PURCHASEORDER_ISGENERATE_0)
                        && depotMerchantRel != null
                        && depotMerchantRel.getIsInOutInstruction().equals(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_0)
                        && (DERP_ORDER.PURCHASEORDER_STATUS_003.equals(d.getStatus())
                        || DERP_ORDER.PURCHASEORDER_STATUS_005.equals(d.getStatus()))) {
                    d.setIsInDepotAble("1");
                }
            }else{
                /**若生成预申报单标识为“否” 对“已审核、部分出库”状态的订单可在采购订单操作上架入库*/
                if(d.getIsGenerate().equals(DERP_ORDER.PURCHASEORDER_ISGENERATE_0)
                        && (DERP_ORDER.PURCHASEORDER_STATUS_003.equals(d.getStatus())
                        || DERP_ORDER.PURCHASEORDER_STATUS_005.equals(d.getStatus()))) {
                    d.setIsInDepotAble("1");
                }
            }

            /**是否可以打托入库*/

            /**查询是否已关账*/
            boolean isCloseAccount = false ;
            List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(d.getId());

            for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {

                // 单号发货时间不为空则检查月结、关账时间。
                FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
                closeAccountsMongo.setMerchantId(d.getMerchantId());
                closeAccountsMongo.setBuId(d.getBuId());
                closeAccountsMongo.setDepotId(purchaseWarehouseModel.getDepotId());
                String maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);

                if(purchaseWarehouseModel.getInboundDate() != null &&
                        StringUtils.isNotBlank(maxMonth)) {
                    //获取最大关账月份下一个月1日时间
                    String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
                    String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
                    // 关账下个月日期大于 入库日期
                    if (purchaseWarehouseModel.getInboundDate().getTime()
                            < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
                        isCloseAccount = true ;
                    }
                }

            }
            /**查询是否已关账*/

            /**是否可以金额确认*/
            if(isCloseAccount) {
                d.setIsAmountConfirmAble("0");
            }else {
                d.setIsAmountConfirmAble("1");
            }
            /**是否可以金额确认*/

            /**是否可以金额调整*/
            if(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_2.equals(d.getAmountConfirmStatus())) {
                d.setIsAmountAdjustmentAble("0");
            }else {

                d.setIsAmountAdjustmentAble("1");

                if(isCloseAccount) {
                    d.setIsAmountAdjustmentAble("0");
                }
                //存在相同sku的单据，只有【已完结】才可以调整金额
                if(!checkRepeatGoods(d.getId()) && !DERP_ORDER.PURCHASEORDER_STATUS_007.equals(d.getStatus())){
                    d.setIsAmountAdjustmentAble("0");
                }
            }
            /**是否可以金额调整*/
        }
        dto.setList(list);
        return dto;
    }

    /**
     * 新增
     *
     * @param model
     * @return
     */
    @Override
    public String savePurchaseOrder(PurchaseOrderModel model, User user) throws Exception {

        //以【公司+供应商+PO号】的维度检查采购订单状态为：【待提交、已提交、已审核、部分入库】或已入库且订单红冲状态为待红冲的单据是否已存在该PO号，若是则报错提示：PO:XXX已存在XXX状态单据。
        PurchaseOrderModel paramModel = new PurchaseOrderModel();
        paramModel.setMerchantId(model.getMerchantId());
        paramModel.setSupplierId(model.getSupplierId());
        paramModel.setPoNo(model.getPoNo());
        List<PurchaseOrderModel> purchaseOrderModels = purchaseOrderDao.list(paramModel);

        for (PurchaseOrderModel orderModel : purchaseOrderModels) {
            if (model.getId() != null && orderModel.getId().equals(model.getId())) {
                continue;
            }

            if (StringUtils.isBlank(orderModel.getWriteOffStatus()) && !orderModel.getStatus().equals(DERP_ORDER.PURCHASEORDER_STATUS_006)) {
                throw new RuntimeException("PO:" + model.getPoNo() + "已存在" + DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, orderModel.getStatus())  + "状态单据");
            }

            if (orderModel.getStatus().equals(DERP_ORDER.PURCHASEORDER_STATUS_007) && (DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001.equals(orderModel.getWriteOffStatus()) ||
                            DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_002.equals(orderModel.getWriteOffStatus()))) {
                throw new RuntimeException("PO:" + model.getPoNo() + "已存在" + DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, orderModel.getStatus())  + "状态单据");
            }
        }

    	PurchaseOrderModel purchaseOrderModel = saveOrModifyPurchaseOrder(model, user, "2");
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "新增", null, null);

        return purchaseOrderModel.getId().toString();
    }

    /**
     * 根据id删除(支持批量)
     *
     * @param ids
     * @return
     */
    @Override
    public boolean delPurchaseOrder(List<Long> ids, User user) throws Exception {
        int flag = 0;
        for (Long id : ids) {
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            if (!DERP_ORDER.PURCHASEORDER_STATUS_001.equals(purchaseOrder.getStatus())) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            throw new RuntimeException("操作失败，只能删除待提交订单");
        }
        for (Long id : ids) {
            // 根据id获取信息
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            // 判断状态是否为 待审核
            if (DERP_ORDER.PURCHASEORDER_STATUS_001.equals(purchaseOrder.getStatus())) {
                purchaseOrder.setStatus(DERP.DEL_CODE_006);// 已删除
                purchaseOrderDao.modify(purchaseOrder);

                commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "删除", null, null);

            }
        }



        return true;
    }

    /**
     * 新增or修改
     *
     * @param model
     * @return
     */
    @Override
    public String modifyPurchaseOrder(PurchaseOrderModel model, User user) throws Exception {
    	PurchaseOrderModel purchaseOrderModel = saveOrModifyPurchaseOrder(model, user, "2");
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "编辑", null, null);

        return purchaseOrderModel.getId().toString();
    }

    /**
     * 根据id获取详情
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrderModel searchDetail(Long id) throws SQLException {
        PurchaseOrderModel model = new PurchaseOrderModel();
        model.setId(id);
        return purchaseOrderDao.getDetails(model);
    }


    private void auditPurchaseOrder(PurchaseOrderModel purchaseOrder, DepotInfoMongo depot, User user, String status) throws Exception {

        if(DERP_ORDER.PURCHASEORDER_STATUS_001.equals(status)) {
            PurchaseOrderModel model = new PurchaseOrderModel();
            model.setId(purchaseOrder.getId());
            model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);// 待提交驳回
            model.setAuditDate(TimeUtils.getNow());// 审核时间
            model.setAuditer(user.getId());// 审核人id
            model.setAuditName(user.getName());// 审核人用户名
            model.setModifyDate(TimeUtils.getNow());
            purchaseOrderDao.modify(model);

            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            emailUserDTO.setBuId(purchaseOrder.getBuId());
            emailUserDTO.setBuName(purchaseOrder.getBuName());
            emailUserDTO.setMerchantId(purchaseOrder.getMerchantId());
            emailUserDTO.setMerchantName(purchaseOrder.getMerchantName());
            emailUserDTO.setOrderCode(purchaseOrder.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
            emailUserDTO.setSupplier(purchaseOrder.getSupplierName());
            emailUserDTO.setPoNum(purchaseOrder.getPoNo());
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setStatus("已驳回");
            emailUserDTO.setUserId(Arrays.asList(String.valueOf(purchaseOrder.getSubmiter())));
            emailUserDTO.setSubmitId(purchaseOrder.getSubmiter()!=null?Arrays.asList(String.valueOf(purchaseOrder.getSubmiter())):null);
            String auditMethod = purchaseOrder.getAuditMethod();
            String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
            emailUserDTO.setAuditMethod(auditMethodLabel);
            JSONObject json = JSONObject.fromObject(emailUserDTO) ;

            try {
                //发送邮件
                rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            } catch (Exception e) {
                LOGGER.error("--------采购发送邮件发送失败-------", json.toString());
            }

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "审核", "驳回", null);

            return ;
        }
        StringBuffer failureMsg = new StringBuffer();
        for (PurchaseOrderItemModel item : purchaseOrder.getItemList()) {
            //审核采购订单时，增加校验该货号在商品表的“品牌”、“一级类目”、二级类目是否为空，若是则提示：XX货号XX为空请及时维护XX
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchandiseId", item.getGoodsId());
            params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);

            if (merchandise != null) {
            	if(merchandise.getBrandId() == null || merchandise.getBrandId() == 0) {
            		failureMsg.append("商品货号：" + item.getGoodsNo() + "品牌为空请及时维护\n");
            	}
            	if(merchandise.getProductTypeId0() == null || merchandise.getProductTypeId0() == 0) {
            		failureMsg.append("商品货号：" + item.getGoodsNo() + "一级类目为空请及时维护\n");
            	}
				if(merchandise.getProductTypeId() == null || merchandise.getProductTypeId() == 0 ) {
					failureMsg.append("商品货号：" + item.getGoodsNo() + "二级类目为空请及时维护\n");
				}
            }

        }
        if(StringUtils.isNotBlank(failureMsg) && failureMsg.length() > 0) {
        	throw new DerpException(failureMsg.toString()) ;
        }

        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("merchantId", user.getMerchantId()) ;

        MerchantInfoMongo merchantInfoMongo = merchantMongoDao.findOne(queryMap) ;

        String accountCurrency = null ;

        if(merchantInfoMongo != null) {
            accountCurrency = merchantInfoMongo.getAccountCurrency();
        }

        PurchaseOrderModel model = new PurchaseOrderModel();
        model.setId(purchaseOrder.getId());
        model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_003);// 已审核
        model.setAuditDate(TimeUtils.getNow());// 审核时间
        model.setAuditer(user.getId());// 审核人id
        model.setAuditName(user.getName());// 审核人用户名
        model.setModifyDate(TimeUtils.getNow());
        model.setTgtCurrency(accountCurrency);
        purchaseOrderDao.modify(model);

        /**
         * 根据仓库是否下推指令和内贸仓推送众邦云仓
         */
        if(depot != null && DERP_SYS.DEPOTINFO_TYPE_G.equals(depot.getType())) {

            queryMap = new HashMap<String, Object>() ;

            queryMap.put("merchantId", purchaseOrder.getMerchantId()) ;
            queryMap.put("depotId", depot.getDepotId()) ;

            DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(queryMap);

            if(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1
                    .equals(depotMerchantRel.getIsInOutInstruction())) {
                /*
                 *  封装众邦云仓推送外部API JSON
                 */
                // 订单信息
                EntryOrder entryOrder = new EntryOrder() ;

                entryOrder.setEntryOrderCode(purchaseOrder.getCode());
                entryOrder.setOwnerCode(ApolloUtil.ywmsOwnerCode);
                entryOrder.setPurchaseOrderCode(purchaseOrder.getCode());
                entryOrder.setWarehouseCode(depot.getCode());
                entryOrder.setOrderCreateTime(TimeUtils.format(purchaseOrder.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
                entryOrder.setOrderType(PushYwmsTypeEnum.CGRK.getType());

                //商品信息
                OrderLines orderLines = new OrderLines() ;
                List<OrderLine> orderLinesList = new ArrayList<>() ;

                for (PurchaseOrderItemModel item : purchaseOrder.getItemList()) {

                    OrderLine orderLine = new OrderLine() ;

                    orderLine.setOwnerCode(ApolloUtil.ywmsOwnerCode);
                    orderLine.setItemCode(item.getGoodsNo());
                    orderLine.setPlanQty(item.getNum().toString());

                    orderLinesList.add(orderLine) ;
                }

                orderLines.setOrderLine(orderLinesList);

                Request request = new Request() ;
                request.setEntryOrder(entryOrder);
                request.setOrderLines(orderLines);

                PurchasePushRoot root = new PurchasePushRoot();
                root.setRequest(request);

                //将null 值转空字符串
                String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(root, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullStringAsEmpty) ;

                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject() ;
                json.put("type", PushYwmsTypeEnum.CGRK.getType()) ;
                json.put("jsonStr", jsonStr) ;
                json.put("method", EpassAPIMethodEnum.EPASS_E09_METHOD.getMethod()) ;
                json.put("order_id", purchaseOrder.getCode()) ;

                try {
                    //下推众邦云仓
                    rocketMQProducer.send(json.toJSONString(), MQPushApiEnum.PUSH_YWMS.getTopic(),
                            MQPushApiEnum.PUSH_YWMS.getTags());
                } catch (Exception e) {
                    LOGGER.error("--------采购审核下推众邦云仓消息发送失败-------", json.toString());
                }
            }

        }

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "审核", "通过", null);
    }

    /**
     * 生成预申报单
     *
     * @param list
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public DeclareOrderDTO generateDeclareOrder(List<Long> list, User user) throws Exception {

        // 根据id集合查询采购订单集合
        List<PurchaseOrderModel> result = purchaseOrderDao.getSupplierIdAndDepotId(list);

        // 获取第一个id的采购订单消息
        Long id = (Long) list.get(0);
        PurchaseOrderModel model = purchaseOrderDao.searchById(id);

        // 新增预申报单
        DeclareOrderModel declareOrder = new DeclareOrderModel();
        List<DeclareOrderItemModel> declareItemList = new ArrayList<DeclareOrderItemModel>() ;

        // 客户编码
        declareOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGOD));// 预申报单号
        declareOrder.setContractNo(model.getContractNo());// 合同号
        declareOrder.setPoNo(model.getPoNo());// po号
        declareOrder.setSupplierId(model.getSupplierId());// 供应商id
        declareOrder.setSupplierName(model.getSupplierName());// 供应商名称
        declareOrder.setDepotId(model.getDepotId());// 仓库id
        declareOrder.setDepotName(model.getDepotName());// 仓库名称
        declareOrder.setDeliveryAddr(model.getDeliveryAddr());// 收货地点
        declareOrder.setBusinessModel(model.getBusinessModel());// 业务模式
        declareOrder.setMerchantId(model.getMerchantId());// 商家id
        declareOrder.setMerchantName(model.getMerchantName());// 商家名称
        declareOrder.setTopidealCode(model.getTopidealCode());// 卓志编码
        declareOrder.setLadingBill(model.getLadingBill());//头程提单号
        declareOrder.setPortLoading(model.getLoadPort());// 装货港
        declareOrder.setPortDestNo(model.getUnloadPort());//卸货港
        declareOrder.setChannel(model.getChannel());//销售渠道
       // declareOrder.setTransport(model.getTransport());//运输方式  1 空运 2 海运
        declareOrder.setBoxNum(model.getBoxNum());//箱数
        declareOrder.setTorrNum(model.getTorrNum());//托数
        declareOrder.setDestinationPortName(model.getDestinationPortName());//目的港名称
        declareOrder.setPackType(model.getPackType());//包装方式
        declareOrder.setPayRules(model.getPayRules());//付款条约
        declareOrder.setBlNo(model.getBlNo());//二程提单号
        declareOrder.setImExpPort(model.getImExpPort());//进出口口岸
        declareOrder.setShipper(model.getShipper());//境外发货人
        declareOrder.setMark(model.getMark());//唛头
        declareOrder.setArriveDate(model.getArriveDate());//预计到港时间
        declareOrder.setDestinationName(model.getDestinationName());//目的地名称
        declareOrder.setInvoiceNo(model.getInvoiceNo());//发票号
       // declareOrder.setLbxNo(model.getLbxNo());// lbx单号
        declareOrder.setState(DERP_ORDER.DECLAREORDER_STATE_0);//状态
        declareOrder.setRemark(model.getRemark());//备注
        declareOrder.setTallyingUnit(model.getTallyingUnit());//理货单位
        declareOrder.setCreater(user.getId());// 创建人id
        declareOrder.setCreateName(user.getName());// 创建人用户名
        declareOrder.setCreateDate(TimeUtils.getNow());
        declareOrder.setBuId(model.getBuId());
        declareOrder.setBuName(model.getBuName());
        declareOrder.setShipDate(model.getShipDate());
        declareOrder.setInCustomsCode(model.getInCustomsCode());
        declareOrder.setInCustomsId(model.getInCustomsId());
        declareOrder.setInPlatformCustoms(model.getInPlatformCustoms());
        declareOrder.setPaymentProvision(model.getPaymentProvision());
        declareOrder.setTradeTerms(model.getTradeTerms());

        Set<String> originSet = new TreeSet<>() ; //原产国

        // 添加预申报单表体
        List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.getByOrderIds(list);

        //序号
        int index = 1 ;
        Map<String, MerchandiseInfoMogo> merchandiseInfoMap = new HashMap<String, MerchandiseInfoMogo>();
        for (PurchaseOrderItemModel pItemModel : itemList) {

            Map<String, Object> params = new HashMap<String, Object>();
            MerchandiseInfoMogo merchandise = merchandiseInfoMap.get(pItemModel.getGoodsId());
            if(merchandise == null){
                params.put("merchandiseId", pItemModel.getGoodsId());
                merchandise = merchandiseInfoMogoDao.findOne(params);

                merchandiseInfoMap.put("goodsId",merchandise);
            }
            if (merchandise != null) {
                DeclareOrderItemModel itemModel = new DeclareOrderItemModel();
                itemModel = new DeclareOrderItemModel();
                itemModel.setGoodsSpec(merchandise.getSpec());// 规格

                params = new HashMap<String, Object>();
                params.put("countryOriginId", merchandise.getCountyId());

                CountryOriginMongo country = countryOriginMongoDao.findOne(params);

                if(country != null){
                    originSet.add(country.getName()) ;
                }

                itemModel.setSeq(index);    		//序号
                itemModel.setPurchase(pItemModel.getOrderCode());// 采购订单编码
                itemModel.setPurchaseId(pItemModel.getPurchaseOrderId());//采购订单
                itemModel.setPoNo(pItemModel.getPoNo());//po号
                itemModel.setContractNo(pItemModel.getContractNo());// 子合同号
                itemModel.setGoodsId(pItemModel.getGoodsId());// 商品id
                itemModel.setGoodsCode(pItemModel.getGoodsCode());// 商品编码
                itemModel.setGoodsName(pItemModel.getGoodsName());// 商品名称
                itemModel.setGoodsNo(pItemModel.getGoodsNo());// 商品货号
                itemModel.setCreater(user.getId());// 创建人id
                itemModel.setCreateDate(TimeUtils.getNow());
                itemModel.setBoxNo(pItemModel.getBoxNo());// 箱号
                itemModel.setBatchNo(pItemModel.getBatchNo());// 批次号
                itemModel.setConstituentRatio(merchandise.getComponent());// 成分占比
                itemModel.setBrandName("境外品牌");// 品牌名称
                itemModel.setPurchaseItemId(pItemModel.getId());
                itemModel.setBarcode(pItemModel.getBarcode());

                //根据采购明细id获取已入库数量
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("purchaseItemId", pItemModel.getId());
                paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
                List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
                Integer warehouseNum = 0;
                if(numList != null && numList.size() > 0){
                    BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
                    warehouseNum = queryNum.intValue();
                }
                paramMap = new HashMap<String, Object>();
                Integer declareNum = 0;
                List<String> statusList = new ArrayList<>();
                statusList.add(DERP_ORDER.DECLAREORDER_STATUS_001);
                statusList.add(DERP_ORDER.DECLAREORDER_STATUS_002);
                statusList.add(DERP_ORDER.DECLAREORDER_STATUS_004);
                paramMap.put("purchaseItemId", pItemModel.getId());
                paramMap.put("statusList", statusList);
                DeclareOrderItemModel declareModel = declareOrderItemDao.getDeclareOrderItems(paramMap);
                if(declareModel != null){
                    declareNum = declareModel.getNum();
                }
                Integer unDeclareNum = pItemModel.getNum() - warehouseNum - declareNum;//待预申报量 = 采购量 - 已入库量 - 预申报但未入库的量
                if(unDeclareNum.intValue() < 0){
                    unDeclareNum = 0;
                }
                itemModel.setNum(unDeclareNum);// 数量

                itemModel.setAmount(pItemModel.getPrice().multiply(new BigDecimal(itemModel.getNum())));// 总金额
                itemModel.setPurchasePrice(pItemModel.getPrice());//采购单价

                BigDecimal filing_price = new BigDecimal(0);
                if(merchandise.getFilingPrice()!=null){
                    filing_price = merchandise.getFilingPrice();
                }

                Double grossWeight = merchandise.getGrossWeight();
                Double netWeight = merchandise.getNetWeight();
//                Integer num = pItemModel.getNum() ;

                itemModel.setGrossWeight(grossWeight);// 毛重
                itemModel.setNetWeight(netWeight);// 净重
                itemModel.setGrossWeightSum(grossWeight * unDeclareNum);
                itemModel.setNetWeightSum(netWeight * unDeclareNum);

                //申报单价=商品备案价*系数
                double ratio = 1.00 ;

                BrandSuperiorMongo brandSuperiorModel = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(merchandise.getMerchandiseId());

                if(brandSuperiorModel != null && brandSuperiorModel.getPriceDeclareRatio() != null) {
                    ratio = brandSuperiorModel.getPriceDeclareRatio() ;
                }
                itemModel.setPrice(filing_price.multiply(new BigDecimal(ratio)));// 单价

                declareItemList.add(itemModel) ;
                index ++ ;
            }
        }
        //提单毛重
        BigDecimal totalGrossWeight = BigDecimal.ZERO ;
        if(declareItemList.size() > 0){
            totalGrossWeight = declareItemList.stream().map(DeclareOrderItemModel :: getGrossWeightSum).map(BigDecimal::new).reduce(BigDecimal.ZERO,BigDecimal::add);
        }

        declareOrder.setBillWeight(totalGrossWeight.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        DeclareOrderDTO dto = new DeclareOrderDTO() ;
        BeanUtils.copyProperties(declareOrder, dto);

        dto.setItemList(declareItemList);

        String purchaseCodes = "" ;
        Set<String> poList=new HashSet<>();

        for (PurchaseOrderModel purchaseOrder : result) {

            if(StringUtils.isNotBlank(purchaseCodes)) {
                purchaseCodes += "," ;
            }
            purchaseCodes += purchaseOrder.getCode() ;

            poList.add(purchaseOrder.getPoNo());
        }

        dto.setPurchaseCode(purchaseCodes);
        dto.setPoNo(StringUtils.join(poList,","));

        return dto;
    }

    /**
     * 2个页签导入采购订单
     *
     * @param data
     * @param user
     * @return map
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    @Override
    public Map importPurchaseOrder(List<List<Map<String, String>>> data, User user) throws Exception {

        List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
        int success = 0;
        int pass = 0;
        int failure = 0;

        // ps:序号是表头与表体关联的标识 序号与表头是1对1，表头与表体是1对多
        Map<String, PurchaseOrderModel> modelMap = new HashMap<String, PurchaseOrderModel>();
        Map<String, List<PurchaseOrderItemModel>> itemMap = new HashMap<String, List<PurchaseOrderItemModel>>();
        //用于统计订单的总商品数量
        Map<String,Integer> totalMap= new HashMap<String,Integer>();
        // 表头
        List<Map<String, String>> objects = data.get(0);
        for (int j = 1; j <= objects.size(); j++) {

            Map<String, String> map = objects.get(j - 1);
            String index = map.get("自编外部单号");
            if (checkIsNullOrNot(j, index, "基本信息的自编外部单号不能为空", resultList)) {
                failure += 1;
                continue;
            }
            index = index.trim() ;

            // 必填字段的校验
            String supplierCode = map.get("供应商编码");
            if (checkIsNullOrNot(j, supplierCode, "供应商编码不能为空", resultList)) {
                failure += 1;
                continue;
            }
            supplierCode = supplierCode.trim() ;

            /**
            String paySubject = map.get("付款主体");
            if (checkIsNullOrNot(j, paySubject, "付款主体不能为空", resultList)) {
                failure += 1;
                continue;
            }
            paySubject = paySubject.trim() ;

            if(!DERP_ORDER.PURCHASEORDER_PAYSUBJECT_1.equals(paySubject)
                    && !DERP_ORDER.PURCHASEORDER_PAYSUBJECT_2.equals(paySubject)
                    && !DERP_ORDER.PURCHASEORDER_PAYSUBJECT_3.equals(paySubject)) {
                setErrorMsg(j, "付款主体只能是1、2、3", resultList);
                failure += 1;
                continue;
            }*/

            String buCode = map.get("事业部编码");
            if (checkIsNullOrNot(j, buCode, "事业部不能为空", resultList)) {
                failure += 1;
                continue;
            }
            buCode = buCode.trim() ;

            Map<String, Object> relParams = new HashMap<String, Object>() ;
            relParams.put("buCode", buCode);
            relParams.put("merchantId", user.getMerchantId());
            relParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
            MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);

            if(merchantBuRelMongo == null) {
                setErrorMsg(j, "公司事业部关联不存在或已禁用", resultList);
                failure += 1;
                continue;
            }
            boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
            if(!isRelate) {
                setErrorMsg(j, "用户无权限操作该事业部", resultList);
                failure += 1;
                continue;
            }

            String businessModel = map.get("业务模式");
            if (checkIsNullOrNot(j, businessModel, "业务模式不能为空", resultList)) {
                failure += 1;
                continue;
            }
            businessModel = businessModel.trim() ;

            String currency = map.get("采购币种");
            if (checkIsNullOrNot(j, currency, "采购币种不能为空", resultList)) {
                failure += 1;
                continue;
            }
            currency = currency.trim() ;

            if (StringUtils.isBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))) {
                setErrorMsg(j, "采购币种输入值无效", resultList);
                failure += 1;
                continue;
            }

            String poNo = map.get("PO单号") ;
            if (checkIsNullOrNot(j, poNo, "PO单号不能为空", resultList)) {
                failure += 1;
                continue;
            }
            poNo = poNo.trim() ;
            if(!checkPoNo(poNo)){
                setErrorMsg(j,"PO号仅限中文、大小写字母、数字和“-”这4种字符",resultList);
                failure += 1;
                continue;
            }

            String attributionDate = map.get("归属时间") ;
            if (checkIsNullOrNot(j, attributionDate, "归属时间不能为空", resultList)) {
                failure += 1;
                continue;
            }
            attributionDate = attributionDate.trim() ;

            if(!isValidDate(attributionDate)) {
                setErrorMsg(j, "该归属时间格式有误，格式为：yyyy-MM-dd", resultList);
                failure += 1;
                continue;
            }

            if(TimeUtils.daysBetween(TimeUtils.parse(attributionDate, "yyyy-MM-dd"), new Date()) < 0) {
                setErrorMsg(j, "归属时间不可超过当前时间", resultList);
                failure += 1;
                continue;
            }

            String paymentDate = map.get("预计付款日期") ;
            if(StringUtils.isNotBlank(paymentDate)){
                if(!isValidDate(paymentDate)) {
                    setErrorMsg(j, "该预计付款日期格式有误，格式为：yyyy-MM-dd", resultList);
                    failure += 1;
                    continue;
                }

                if(TimeUtils.daysBetween(TimeUtils.parse(paymentDate, "yyyy-MM-dd"), new Date()) > 0) {
                    setErrorMsg(j, "预计付款日期必须大于或等于当前时间", resultList);
                    failure += 1;
                    continue;
                }
            }

            if(StringUtils.isBlank(merchantBuRelMongo.getPurchaseAuditMethod())) {
            	setErrorMsg(j, "公司事业没有维护采购审批方式", resultList);
                failure += 1;
                continue;
			}


            //有框架合同的填写OA的框架合同编号没有的话，填写无
            String frameContractNo = map.get("框架合同号") ;
            if (checkIsNullOrNot(j, frameContractNo, "框架合同号不能为空", resultList)) {
                failure += 1;
                continue;
            }
            //有试单申请的填写OA的试单申请流水编号 没有的话，填写无
            String tryApplyCode = map.get("采购试单申请编号") ;
            if (checkIsNullOrNot(j, tryApplyCode, "采购试单申请编号不能为空", resultList)) {
                failure += 1;
                continue;
            }
            //非必填填写日期须大于等于当前日期 预计发货日期
            String estimateDeliverDate = map.get("预计发货日期") ;
            if (StringUtils.isNotBlank(estimateDeliverDate)) {
            	if(TimeUtils.daysBetween(TimeUtils.parse(estimateDeliverDate, "yyyy-MM-dd"), new Date()) > 0) {
                    setErrorMsg(j, "预计发货日期必须大于或等于当前时间", resultList);
                    failure += 1;
                    continue;
                }
			}

            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(user.getMerchantId());
            closeAccountsMongo.setBuId(merchantBuRelMongo.getBuId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
            String maxCloseAccountsMonth = "";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
                maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 入库日期
                if (Timestamp.valueOf(attributionDate + " 00:00:00").getTime() < Timestamp
                        .valueOf(maxCloseAccountsMonth).getTime()) {
                    setErrorMsg(j, "归属时间必须大于关账日期", resultList);
                    failure += 1;
                    continue;
                }
            }

            // 判断供应商是否存在
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("code", supplierCode) ;
            params.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2) ;
            params.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

            CustomerInfoMogo customer = customerInfoMongoDao.findOne(params);

            if(customer == null){
                setErrorMsg(j, "该供应商不存在", resultList);
                failure += 1;
                continue;
            }

            params = new HashMap<String, Object>();
            params.put("customerId", customer.getCustomerid()) ;
            params.put("merchantId", user.getMerchantId()) ;
            CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(params);
            if (customerRel == null) {
                setErrorMsg(j, "该公司供应商关联不存在", resultList);
                failure += 1;
                continue;
            }

            //审批方式 1-OA审批 2-经分销审批
            String auditMethod = merchantBuRelMongo.getPurchaseAuditMethod();
            Long innerMerchantId = customer.getInnerMerchantId();
            if (innerMerchantId!=null&&innerMerchantId!=0) {
				auditMethod=DERP_ORDER.PURCHASEORDER_AUDITMETHOD_2;
			}

            //需要OA审批的必填，不需要的不必填 采购方式  0：以销定采  1：备货（已立项） 2：备货（集团自主）3：试单
            String purchaseMethod = map.get("采购方式") ;
            if ("1".equals(auditMethod)) {
            	if (checkIsNullOrNot(j, purchaseMethod, "采购方式不能为空", resultList)) {
                    failure += 1;
                    continue;
                }
            	if (!("0".equals(purchaseMethod)||"1".equals(purchaseMethod)||"2".equals(purchaseMethod)||"3".equals(purchaseMethod))){
                    setErrorMsg(j, "OA审批采购方式只能是 0、1、2、3", resultList);
                    failure += 1;
                    continue;
    			}

			}
            /**库位类型*/
            String stockLocationName = map.get("库位类型");
            //通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
            if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(stockLocationName)){
                setErrorMsg(j,  "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空", resultList);
                failure += 1;
                continue;
            }else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(stockLocationName)){
                setErrorMsg(j,  "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写", resultList);
                failure += 1;
                continue;
            }
            BuStockLocationTypeConfigMongo stockLocationMongo = null;
            if(org.apache.commons.lang3.StringUtils.isNotBlank(stockLocationName)){
                Map<String,Object> stockLocationMap = new HashMap<>();
                stockLocationMap.put("merchantId", user.getMerchantId());
                stockLocationMap.put("buId", merchantBuRelMongo.getBuId());
                stockLocationMap.put("name", stockLocationName);
                stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
                stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

                if(stockLocationMongo == null){
                   setErrorMsg(j,  "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"库位类型："+stockLocationName+"，不存在", resultList);
                    failure += 1;
                    continue;
                }
            }

            PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
            purchaseOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));// 采购订单编码
            purchaseOrder.setMerchantId(user.getMerchantId());// 商家id
            purchaseOrder.setMerchantName(user.getMerchantName());// 商家名称
            purchaseOrder.setTopidealCode(user.getTopidealCode());// 卓志编码
            purchaseOrder.setSupplierId(customerRel.getCustomerId());// 供应商id
            purchaseOrder.setSupplierName(customerRel.getName());// 供应商名称
            purchaseOrder.setCreater(user.getId());// 创建人
            purchaseOrder.setCreateName(user.getName());// 创建人名称
            purchaseOrder.setBusinessModel(businessModel);// 业务模式
            purchaseOrder.setCurrency(currency);// 采购币种
            purchaseOrder.setPoNo(poNo);// PO单号
            purchaseOrder.setBuId(merchantBuRelMongo.getBuId());
            purchaseOrder.setBuName(merchantBuRelMongo.getBuName());
            purchaseOrder.setAttributionDate(TimeUtils.parse(attributionDate, "yyyy-MM-dd"));
            purchaseOrder.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
            purchaseOrder.setAmountConfirmStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0);
            if(StringUtils.isNotBlank(paymentDate)){
                purchaseOrder.setPaymentDate(TimeUtils.parse(paymentDate, "yyyy-MM-dd")) ;
            }
            purchaseOrder.setAuditMethod(auditMethod);//审批方式 1-OA审批 2-经分销审批
            purchaseOrder.setPurchaseMethod(purchaseMethod);//采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单
            purchaseOrder.setFrameContractNo(frameContractNo);//框架合同号
            purchaseOrder.setTryApplyCode(tryApplyCode);//采购试单申请编号
            purchaseOrder.setEstimateDeliverDate(TimeUtils.parse(estimateDeliverDate, "yyyy-MM-dd"));
            if(stockLocationMongo != null){
                purchaseOrder.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
                purchaseOrder.setStockLocationTypeName(stockLocationMongo.getName());
            }

			/*
			 * if (DERP_ORDER.PURCHASEORDER_PAYSUBJECT_1.equals(paySubject) ||
			 * DERP_ORDER.PURCHASEORDER_PAYSUBJECT_2.equals(paySubject) ||
			 * DERP_ORDER.PURCHASEORDER_PAYSUBJECT_3.equals(paySubject)) {
			 * purchaseOrder.setPaySubject(paySubject);// 付款主体 }
			 */

            modelMap.put(index, purchaseOrder);

        }

        if(failure == 0){
            // 表体
        	Map<String,String> checkGoodsNoAndEffectiveIntervalMap = new HashMap<String, String>();
            objects = data.get(1);
            for (int j = 1; j <= objects.size(); j++) {

                Map<String, String> map = objects.get(j - 1);
                String index = map.get("自编外部单号");
                if (checkIsNullOrNot(j, index, "商品明细的自编外部单号不能为空", resultList)) {
                    failure += 1;
                    continue;
                }
                index = index.trim() ;

                if(!modelMap.containsKey(index)) {
                    setErrorMsg(j, "基本信息自编外部单号与商品信息自编外部单号不符", resultList);
                    failure += 1;
                    continue;
                }

                String barcode = map.get("商品条形码");
                if (checkIsNullOrNot(j, barcode, "商品条形码不能为空", resultList)) {
                    failure += 1;
                    continue;
                }
                barcode = barcode.trim();

                String num = map.get("采购数量");
                if (checkIsNullOrNot(j, num, "采购数量不能为空", resultList)) {
                    failure += 1;
                    continue;
                }else if (!StringUtils.isNumeric(num)) {
                    setErrorMsg(j, "采购数量不为数值", resultList);
                    failure += 1;
                    continue;
                }
                num = num.trim() ;

                if(Integer.valueOf(num) == 0) {
                    setErrorMsg(j, "采购数量不能为0", resultList);
                    failure += 1;
                    continue;
                }
                String effectiveInterval = map.get("剩余校期");
                String effectiveIntervalKey = "";
                if(StringUtils.isNotBlank(effectiveInterval)){
                	effectiveInterval =effectiveInterval.trim();
                	effectiveIntervalKey = String.valueOf(DERP_ORDER.getKeyByLabel(DERP_ORDER.purchaseOrder_effectiveIntervalList, effectiveInterval));
                	if(StringUtils.isBlank(effectiveIntervalKey)){
                		setErrorMsg(j, "剩余校期填写有误", resultList);
                		failure += 1;
                		continue;
                	}
                }

                // 根据商品货号获取商品id
                Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
                merchandiseInfoParams.put("barcode", barcode);
                merchandiseInfoParams.put("merchantId", user.getMerchantId());
                merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
                List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findAll(merchandiseInfoParams);
                if (merchandiseList == null || merchandiseList.size() < 1) {
                    setErrorMsg(j, "条形码"+barcode+"对应商品货号禁用或不存在", resultList);
                    failure += 1;
                    continue;
                }
                MerchandiseInfoMogo merchandiseInfo = merchandiseList.get(0);

                PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
                // 新增采购订单表体
                if(merchandiseInfo != null ){
                    itemModel.setGoodsId(merchandiseInfo.getMerchandiseId());// 商品id
                    itemModel.setGoodsCode(merchandiseInfo.getGoodsCode());// 商品编码
                    itemModel.setGoodsName(merchandiseInfo.getName());// 商品名称
                    itemModel.setGoodsNo(merchandiseInfo.getGoodsNo());// 商品货号
                    itemModel.setFactoryNo(merchandiseInfo.getFactoryNo());//工厂编码

                }
                itemModel.setBarcode(barcode);// 条形码
                itemModel.setNum(Integer.parseInt(num));// 采购数量
                itemModel.setCreater(user.getId());// 创建人
                itemModel.setEffectiveInterval(effectiveIntervalKey);//剩余效期
                /**查询是否开启采购价格管理*/
                PurchaseOrderModel purchaseOrder = modelMap.get(index);
                boolean purchasePriceManage = getPurchasePriceManage(purchaseOrder.getBuId(), purchaseOrder.getSupplierId(), user);

                String amount = map.get("采购金额");
                if(!purchasePriceManage){
                    if (checkIsNullOrNot(j, amount, "采购金额不能为空", resultList)) {
                        failure += 1;
                        continue;
                    }
                    amount = amount.trim() ;
                }

                if(StringUtils.isNotBlank(amount)) {
                    if (!isNumeric(amount)) {
                        setErrorMsg(j, "采购金额不为数值", resultList);
                        failure += 1;
                        continue;
                    }

                    int indexOf = amount.indexOf(".");
                    // 如果是小数
                    if (indexOf != -1) {
                        int count = amount.length() - 1 - indexOf;
                        if (count > 2) {
                            setErrorMsg(j, "条形码：" + barcode + "，采购金额小数点后只能为两位数", resultList);
                            failure += 1;
                            continue;
                        }
                    }
                }else{
                    amount="0";
                }

                if(purchasePriceManage) {
                    Map<String, Object> smPriceMap = new HashMap<String, Object>() ;
                    smPriceMap.put("merchantId", purchaseOrder.getMerchantId()) ;
                    smPriceMap.put("customerId", purchaseOrder.getSupplierId()) ;
                    smPriceMap.put("buId", purchaseOrder.getBuId()) ;
                    smPriceMap.put("commbarcode", merchandiseInfo.getCommbarcode()) ;
                    smPriceMap.put("currency", purchaseOrder.getCurrency()) ;
                    smPriceMap.put("state", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003) ;
                    if(purchaseOrder.getStockLocationTypeId() != null){
                        smPriceMap.put("stockLocationTypeId", purchaseOrder.getStockLocationTypeId()) ;
                    }
                    List<SupplierMerchandisePriceMongo> smList = supplierMerchandisePriceMongoDao.findAll(smPriceMap);

                    if(smList.isEmpty()) {
                        setErrorMsg(j, "该商家事业部已启用采购价格管理，该商品标准条码查询供应商价格管理无数据", resultList);
                        failure += 1;
                        continue;
                    }

                    smList = smList.stream().sorted(Comparator.comparing(SupplierMerchandisePriceMongo :: getAuditDate).reversed()).collect(Collectors.toList()) ;

                    //存储有效的采购价格*数量保留两位销售和页面传来总金额进行比较
                    List<BigDecimal> supplierPriceAmountList=new ArrayList<>();
                    Map<BigDecimal, BigDecimal> supplyPriceAmountPriceMap = new HashMap<>();
                    for (SupplierMerchandisePriceMongo tempMongo : smList) {

                        String effectiveDate = tempMongo.getEffectiveDate();
                        String expiryDate = tempMongo.getExpiryDate();

                        if (TimeUtils.parse(effectiveDate, "yyyy-MM-dd").getTime() <= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()
                                && TimeUtils.parse(expiryDate, "yyyy-MM-dd").getTime() >= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()) {
                        	BigDecimal supplierPriceAmount= tempMongo.getSupplyPrice().multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP);
                        	supplierPriceAmountList.add(supplierPriceAmount);
                            supplyPriceAmountPriceMap.put(supplierPriceAmount, tempMongo.getSupplyPrice());
                        }
                    }

                    if(supplierPriceAmountList.size()==0) {
                        setErrorMsg(j, "该商家-事业部-供应商已启用采购价格管理，该归属日期查询供应商价格管理无数据", resultList);
                        failure += 1;
                        continue;
                    }

                    BigDecimal amountBig=new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);

                    //查询导入的采购价格是否存在有效的采购价格管理里面
                    Boolean flagPrice=supplierPriceAmountList.stream().anyMatch(price->amountBig.compareTo(price)==0);
                    if(!flagPrice){
                        setErrorMsg(j, "该商家-事业部-供应商已启用采购价格管理，该条形码"+barcode+"的采购价格没有维护", resultList);
                        failure += 1;
                        continue;
                    }
//                    BigDecimal supplyPrice = amountBig.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);;
                    itemModel.setPrice(supplyPriceAmountPriceMap.get(amountBig).setScale(8, BigDecimal.ROUND_HALF_UP));// 采购单价
                    itemModel.setAmount(amountBig);// 总金额
                }else{
                    BigDecimal amountTemp = new BigDecimal(amount);
                    itemModel.setAmount(amountTemp);// 总金额
                    BigDecimal priceTemp = amountTemp.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
                    itemModel.setPrice(priceTemp);// 采购单价
                }
                String  goodsNoAndEffectiveInterval = index+"_"+barcode+"_"+itemModel.getPrice();
                if(checkGoodsNoAndEffectiveIntervalMap.containsKey(goodsNoAndEffectiveInterval)) {
                    String tempEffectiveInterval = checkGoodsNoAndEffectiveIntervalMap.get(goodsNoAndEffectiveInterval);
                    if(!effectiveIntervalKey.equals(tempEffectiveInterval)) {
                        setErrorMsg(j, "相同商品信息自编外部单号:"+index+"，相同条形码："+barcode+"，相同采购单价，剩余校期不一致", resultList);
                        failure += 1;
                        continue;
                    }
                }else {
                    checkGoodsNoAndEffectiveIntervalMap.put(goodsNoAndEffectiveInterval, effectiveIntervalKey);
                }

                // 记录表体信息
                List<PurchaseOrderItemModel> itemModelList = new ArrayList<PurchaseOrderItemModel>();
                if (itemMap.containsKey(index)) {
                    itemModelList = itemMap.get(index);
                }

                if(itemModelList.size()>0) {
                    boolean flag = true;
                    for (PurchaseOrderItemModel pItemModel : itemModelList) {
                        //如果商品货号、单价相同，则数量增加
                        if(pItemModel.getBarcode().equals(itemModel.getBarcode()) && pItemModel.getPrice().compareTo(itemModel.getPrice()) == 0){
                            int sum = pItemModel.getNum() + itemModel.getNum();
                            pItemModel.setNum(sum);
                            pItemModel.setAmount(new BigDecimal(sum).multiply(pItemModel.getPrice()));
                            flag = false;
                            break;
                        }
                    }

                    if(flag){
                        itemModelList.add(itemModel);
                    }

                }else{
                    itemModelList.add(itemModel);
                }
                itemMap.put(index, itemModelList);

                //记录总商品数量
                Integer totalNum=0;
                if(totalMap.containsKey(index)){
                    totalNum = totalMap.get(index);
                }
                totalNum += Integer.valueOf(num);
                totalMap.put(index, totalNum);

            }
        }

        if(failure == 0){
            // 保存数据
            for (Entry<String, PurchaseOrderModel> entry : modelMap.entrySet()) {

                String index = entry.getKey();
                PurchaseOrderModel purchaseOrder = entry.getValue();
                // 保存表头
                Integer totalNum = totalMap.get(index);
                if (totalNum == null || totalNum == 0) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setMessage("自编外部单号：" + index + "的商品信息为空");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                // 保存表体
                List<PurchaseOrderItemModel> itemList = itemMap.get(index);
                if (itemList == null || itemList.size() == 0) {
                    ImportErrorMessage message = new ImportErrorMessage();
                    message.setMessage("自编外部单号：" + index + "的商品信息为空");
                    resultList.add(message);
                    failure += 1;
                    continue;
                }

                purchaseOrder.setCreateDate(TimeUtils.getNow());
                purchaseOrderDao.save(purchaseOrder);

                commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "导入新增", null, null);

                Map<String, Object> params = new HashMap<>() ;
                params.put("customerId", purchaseOrder.getSupplierId()) ;
                params.put("merchantId", purchaseOrder.getMerchantId()) ;
                CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(params);

                for (PurchaseOrderItemModel item : itemList) {
                    item.setPurchaseOrderId(purchaseOrder.getId());

                    if(customerRel.getRate() != null){
                        BigDecimal rate = customerRel.getRate();

                        item.setTaxRate(rate.doubleValue());

                        rate = rate.add(new BigDecimal(1)) ;

                        item.setTaxPrice(item.getPrice().multiply(rate));
                        item.setTaxAmount(item.getAmount().multiply(rate));
                        item.setTax(item.getTaxAmount().subtract(item.getAmount()));
                    }

                    purchaseOrderItemDao.save(item);
                }

                success++;
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        map.put("message", resultList);

        return map;
    }

    /**
     * 完结入库校验
     *
     * @param list
     * @return
     * @throws SQLException
     */
    @Override
    public String endPurchaseOrderCheck(List<Long> list) throws SQLException {
        String result = "";
        int flag = 0;
        for (Long id : list) {
            // 根据id获取信息
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            if (!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
                    || DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrder.getStatus()))) {
                result = "状态必须为已审核或部分入库";
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            return result;
        }
        for (Long id : list) {
            // 根据采购订单id获取商品详情
            PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
            purchaseOrder.setId(id);
            purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);
            // 根据采购订单id获取采购入库商品入库数量
            Map<String,Object> paramMap = new HashMap<String, Object>() ;
            paramMap.put("purchaseOrderId", id);
            paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
            List<Map<String, Object>> warehouseNumList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
//            Map<String, List<Map<String, Object>>> warehouseNumMap = warehouseNumList.stream().collect(Collectors.groupingBy( w->w.get("purchase_item_id").toString()));
//            double total = 0.0;
//            int index = 0;
//            for (PurchaseOrderItemModel pModel : purchaseOrder.getItemList()) {
//                Integer warehouseNum = 0;
//                List<Map<String, Object>> queryWarehouseNumList = warehouseNumMap.get(pModel.getId());
//                if(queryWarehouseNumList != null && queryWarehouseNumList.size() > 0){
//                    BigDecimal warehouseNumB = (BigDecimal) queryWarehouseNumList.get(0).get("num");
//                    warehouseNum = warehouseNumB.intValue();
//                }
//                // 总勾稽入库数量/采购数量
//                double temp = (double) warehouseNum / pModel.getNum();
//                total += temp * 100;
//                index++;
//            }
            Integer warehoustNum = 0;
            for(Map<String, Object> warehouseMap : warehouseNumList){
                BigDecimal warehouseNumB = (BigDecimal) warehouseMap.get("num");
                warehoustNum += warehouseNumB.intValue();
            }
            Integer purchaseNum = purchaseOrder.getItemList().stream().filter(i->i.getNum() !=null).mapToInt(PurchaseOrderItemModel :: getNum).sum();

            Double ratio = new BigDecimal(warehoustNum).divide(new BigDecimal(purchaseNum),4,BigDecimal.ROUND_HALF_UP).doubleValue();
            if (ratio.doubleValue() > 0) {
                result += "采购订单【" + purchaseOrder.getCode() + "】入库数量占" + (ratio * 100) + "%\n";
            } else {
                result += "采购订单【" + purchaseOrder.getCode() + "】入库数量占0%\n";
            }
        }
        return result;
    }

    /**
     * 完结入库
     *
     * @param list
     * @return
     * @throws SQLException
     */
    @Override
    public boolean endPurchaseOrder(List<Long> list,String endDateStr, User user) throws SQLException {
        Timestamp endDate=TimeUtils.parse(endDateStr, "yyyy-MM-dd HH:mm:ss");

        for (Long id : list) {
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            purchaseOrder.setId(id);
            purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
            purchaseOrder.setEndDate(endDate);//完结入库时间
            purchaseOrder.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);//是否完结

            purchaseOrderDao.modify(purchaseOrder);

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "完结入库", null, null);

        }
        return true;
    }

    /**
     * 中转仓入库
     *
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public List<InvetAddOrSubtractRootJson> saveInWarehouse(List<Long> list, User user, String inWarehouseDateStr) throws Exception {

        // 判断仓库类型是否为中途仓
        for (Long id : list) {
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            // 判断状态为 已审核或部分入库
            if (!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
                    || DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrder.getStatus())
            )) {
                throw new DerpException("采购订单状态必须为已审核或部分入库") ;
            }

            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", purchaseOrder.getDepotId());// 根据仓库id
            DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
            if (mongo == null) {
                throw new RuntimeException("仓库不存在") ;
            } else if (mongo != null && !mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_D)) {
                throw new DerpException("仓库类别必须为中转仓") ;
            }
            // 获取最大的关账日/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(purchaseOrder.getMerchantId());
            closeAccountsMongo.setDepotId(purchaseOrder.getDepotId());
            closeAccountsMongo.setBuId(purchaseOrder.getBuId());
            String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
            String maxCloseAccountsMonth="";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
                maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 中转仓入库时间(中转仓入库时间不可小于关账日期/结转日期)
                if (Timestamp.valueOf(inWarehouseDateStr).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                    throw new DerpException("中转仓入库时间必须大于关账日期/月结日期") ;
                }
            }
        }

        List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<InvetAddOrSubtractRootJson>() ;
        for (Long id : list) {
            // 获取采购订单信息
            PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
            purchaseOrder.setId(id);
            purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);

            // 添加采购入库单
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String now = sdf.format(new Date());
            PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
            purchaseWarehouse.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 编码
            purchaseWarehouse.setDepotId(purchaseOrder.getDepotId());// 仓库id
            purchaseWarehouse.setDepotName(purchaseOrder.getDepotName());// 仓库名称
            purchaseWarehouse.setCreater(user.getId());// 创建人
            purchaseWarehouse.setMerchantId(purchaseOrder.getMerchantId());// 商家id
            purchaseWarehouse.setMerchantName(purchaseOrder.getMerchantName());// 商家名称
           // purchaseWarehouse.setLbxNo(purchaseOrder.getLbxNo());// lbx单号
            purchaseWarehouse.setCrossStatus(DERP_ORDER.PURCHASEWAREHOUSE_CROSSSTATUS_1);// 采购勾稽状态 是
            purchaseWarehouse.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
            purchaseWarehouse.setInboundDate(TimeUtils.parseFullTime(inWarehouseDateStr));	// 进仓生效日期
            purchaseWarehouse.setContractNo(purchaseOrder.getContractNo());// 合同号
            purchaseWarehouse.setBusinessModel(purchaseOrder.getBusinessModel()); //业务主体
            purchaseWarehouse.setBuId(purchaseOrder.getBuId());
            purchaseWarehouse.setBuName(purchaseOrder.getBuName());
            purchaseWarehouse.setCurrency(purchaseOrder.getCurrency());
            Long result_id = purchaseWarehouseDao.save(purchaseWarehouse);

//            PurchaseAnalysisModel queryPurchaseAnalysis = new PurchaseAnalysisModel() ;
//            queryPurchaseAnalysis.setOrderId(id);
//            List<PurchaseAnalysisModel> tempAnalysisList = purchaseAnalysisDao.list(queryPurchaseAnalysis);

            //过滤已存在采购入库勾稽记录
//            tempAnalysisList = tempAnalysisList.stream().filter(tempAnalysis -> tempAnalysis.getWarehouseId() == null)
//                    .collect(Collectors.toList()) ;
            PurchaseOrderItemModel tempItemModel = new PurchaseOrderItemModel();
            tempItemModel.setPurchaseOrderId(id);
            List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(tempItemModel);
            for (PurchaseOrderItemModel purchaseItemModel : itemList) {
//                PurchaseOrderItemModel purchaseItemModel = new PurchaseOrderItemModel() ;
//                purchaseItemModel.setPurchaseOrderId(id);
//                purchaseItemModel.setGoodsId(purchaseAnalysisModel.getGoodsId());
//
//                purchaseItemModel = purchaseOrderItemDao.searchByModel(purchaseItemModel) ;
                //根据采购明细id获取已入库数量
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("purchaseItemId", purchaseItemModel.getId());
                paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
                List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
                Integer warehouseNum = 0;
                if(numList != null && numList.size() > 0){
                    BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
                    warehouseNum = queryNum.intValue();
                }
                // 添加表体
                PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
                itemModel.setWarehouseId(result_id);// 采购入库单id
                itemModel.setGoodsId(purchaseItemModel.getGoodsId());// 商品id
                itemModel.setGoodsNo(purchaseItemModel.getGoodsNo());// 商品货号
                itemModel.setGoodsName(purchaseItemModel.getGoodsName());// 商品名称
                itemModel.setBarcode(purchaseItemModel.getBarcode());// 条形码
                itemModel.setGrossWeight(purchaseItemModel.getGrossWeight());// 毛重
                itemModel.setNetWeight(purchaseItemModel.getNetWeight());// 净重
                itemModel.setPurchaseNum(purchaseItemModel.getNum() - warehouseNum);// 应收数量
                itemModel.setTallyingNum(purchaseItemModel.getNum() - warehouseNum);// 实收数量
                itemModel.setCreater(user.getId());// 创建人
                itemModel.setCreateName(user.getName());// 创建人用户名
                itemModel.setPurchaseItemId(purchaseItemModel.getId());//采购订单表体id

                // 根据商品货号获取商品id
                Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
                merchandiseInfo_params.put("merchandiseId", purchaseItemModel.getGoodsId());
                MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
                if (merchandiseInfo != null) {
                    itemModel.setVolume(merchandiseInfo.getVolume());// 体积
                    itemModel.setLength(merchandiseInfo.getLength());// 长
                    itemModel.setWidth(merchandiseInfo.getWidth());// 宽
                    itemModel.setHeight(merchandiseInfo.getHeight());// 高
                }

                Long itemId = purchaseWarehouseItemDao.save(itemModel);

                // 如果批次不为空 批次为空 保存一份采购入库单
                PurchaseWarehouseBatchModel batch = new PurchaseWarehouseBatchModel();
                batch.setItemId(itemId);
                batch.setGoodsId(purchaseItemModel.getGoodsId());
                batch.setBatchNo(purchaseItemModel.getBatchNo());
                batch.setWornNum(0);
                batch.setExpireNum(0);
                batch.setNormalNum(purchaseItemModel.getNum());
                batch.setCreater(user.getId());
                purchaseWarehouseBatchDao.save(batch);
            }
            // 添加勾稽关系
            WarehouseOrderRelModel warehouseOrderRel = new WarehouseOrderRelModel();
            warehouseOrderRel = new WarehouseOrderRelModel();
            warehouseOrderRel.setWarehouseId(result_id);// 采购入库单id
            warehouseOrderRel.setPurchaseOrderId(id);// 采购订单id
            warehouseOrderRel.setCreater(user.getId());// 创建人
            warehouseOrderRelDao.save(warehouseOrderRel);

            /**
             * 增加库存
             *
             * @author zhanghx
             */
            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", purchaseWarehouse.getDepotId());// 根据仓库id
            DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);

            InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
            inventoryRoot.setBackTopic(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTopic());
            inventoryRoot.setBackTags(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTags());
            Map<String, Object> customParam = new HashMap<String, Object>();
            customParam.put("userId", user.getId());
            inventoryRoot.setCustomParam(customParam);
            // 增加库存
            inventoryRoot.setMerchantId(purchaseWarehouse.getMerchantId().toString());
            inventoryRoot.setMerchantName(purchaseWarehouse.getMerchantName());
            inventoryRoot.setTopidealCode(user.getTopidealCode());
            inventoryRoot.setBusinessNo(purchaseOrder.getCode());
            inventoryRoot.setDepotId(purchaseWarehouse.getDepotId().toString());
            inventoryRoot.setDepotName(purchaseWarehouse.getDepotName());
            inventoryRoot.setDepotCode(mongo.getCode());
            inventoryRoot.setDepotType(mongo.getType());
            inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
            inventoryRoot.setOrderNo(purchaseWarehouse.getCode());
            inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
            inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
            inventoryRoot.setSourceDate(now);
            inventoryRoot.setBuId(purchaseWarehouse.getBuId().toString());
            inventoryRoot.setBuName(purchaseWarehouse.getBuName());

            // 获取当前年月
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
            Date inWarehouseDate = TimeUtils.StringToDate(inWarehouseDateStr);
            String inWarehouseDateStr2 = sdf2.format(inWarehouseDate);

            inventoryRoot.setOwnMonth(inWarehouseDateStr2);// 归属月份 yyyy-MM
            inventoryRoot.setDivergenceDate(inWarehouseDateStr);// 出入时间  yyyy-MM-dd HH:mm:ss

            List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
            List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch2(result_id);
            for (PurchaseWarehouseBatchModel bModel : batchList) {
                // 正常数量
                InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
                listJson.setGoodsId(bModel.getGoodsId().toString());
                listJson.setGoodsNo(bModel.getGoodsNo());
                listJson.setGoodsName(bModel.getGoodsName());

                listJson.setNum(bModel.getPurchaseNum());
                listJson.setBarcode(bModel.getBarcode());
                listJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
                invetAddOrSubtractGoodsList.add(listJson);
            }

            inventoryRoot.setGoodsList(invetAddOrSubtractGoodsList);

            jsonList.add(inventoryRoot) ;

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "中转仓入库", null, null);
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, purchaseWarehouse.getCode(), "中转仓入库", null, null);
        }


        return jsonList ;

    }

    /**
     * 采购链路入库
     *
     * @param list
     * @return
     * @throws Exception
     */
    private List<InvetAddOrSubtractRootJson> saveTradelinkInWarehouse(List<Long> list, User user,String inWarehouseDateStr,
                                                                      String batchNo, Date produceDate, Date overdueDate) throws Exception {

        for (Long id : list) {
            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);
            // 判断状态为 已审核
            if (!DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())) {
                throw new DerpException("采购订单状态必须为已审核") ;
            }

            WarehouseOrderRelModel queryModel = new WarehouseOrderRelModel() ;
            queryModel.setPurchaseOrderId(id);

            List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(queryModel);

            if(!relList.isEmpty()){
                throw new DerpException("采购订单：" + purchaseOrder.getCode() + "已入库");
            }

            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", purchaseOrder.getDepotId());// 根据仓库id
            DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
            if (mongo == null) {
                throw new DerpException("仓库不存在") ;
            }

            // 获取最大的关账日/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(purchaseOrder.getMerchantId());
            closeAccountsMongo.setDepotId(purchaseOrder.getDepotId());
            closeAccountsMongo.setBuId(purchaseOrder.getBuId());
            String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
            String maxCloseAccountsMonth="";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
                maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 入库时间(中转仓入库时间不可小于关账日期/结转日期)
                if (Timestamp.valueOf(inWarehouseDateStr).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                    throw new DerpException("入库时间必须大于关账日期/月结日期") ;
                }
            }
        }

        List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<InvetAddOrSubtractRootJson>() ;
        for (Long id : list) {
            // 获取采购订单信息
            PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
            purchaseOrder.setId(id);
            purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);

            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", purchaseOrder.getDepotId());// 根据仓库id
            DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);

            // 添加采购入库单
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String now = sdf.format(new Date());
            PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
            purchaseWarehouse.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 编码
            purchaseWarehouse.setDepotId(purchaseOrder.getDepotId());// 仓库id
            purchaseWarehouse.setDepotName(purchaseOrder.getDepotName());// 仓库名称
            purchaseWarehouse.setCreater(user.getId());// 创建人
            purchaseWarehouse.setMerchantId(purchaseOrder.getMerchantId());// 商家id
            purchaseWarehouse.setMerchantName(purchaseOrder.getMerchantName());// 商家名称
            //purchaseWarehouse.setLbxNo(purchaseOrder.getLbxNo());// lbx单号
            purchaseWarehouse.setCrossStatus(DERP_ORDER.PURCHASEWAREHOUSE_CROSSSTATUS_1);// 采购勾稽状态 是
            purchaseWarehouse.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
            purchaseWarehouse.setInboundDate(TimeUtils.parseFullTime(inWarehouseDateStr));	// 进仓生效日期
            purchaseWarehouse.setContractNo(purchaseOrder.getContractNo());// 合同号
            purchaseWarehouse.setBusinessModel(purchaseOrder.getBusinessModel()); //业务主体
            purchaseWarehouse.setBuId(purchaseOrder.getBuId());
            purchaseWarehouse.setBuName(purchaseOrder.getBuName());
            purchaseWarehouse.setCurrency(purchaseOrder.getCurrency());
            if(mongo!=null){
                if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())){
                    purchaseWarehouse.setTallyingUnit(purchaseOrder.getTallyingUnit());;// 海外仓理货单位
                }
            }

            Long result_id = purchaseWarehouseDao.save(purchaseWarehouse);
            if (result_id != null) {
                for (PurchaseOrderItemModel item : purchaseOrder.getItemList()) {
                    // 添加表体
                    PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
                    itemModel.setWarehouseId(result_id);// 采购入库单id
                    itemModel.setGoodsId(item.getGoodsId());// 商品id
                    itemModel.setGoodsNo(item.getGoodsNo());// 商品货号
                    itemModel.setGoodsName(item.getGoodsName());// 商品名称
                    itemModel.setBarcode(item.getBarcode());// 条形码
                    itemModel.setGrossWeight(item.getGrossWeight());// 毛重
                    itemModel.setNetWeight(item.getNetWeight());// 净重
                    itemModel.setPurchaseNum(item.getNum());// 应收数量
                    itemModel.setTallyingNum(item.getNum());// 实收数量
                    itemModel.setCreater(user.getId());// 创建人
                    itemModel.setCreateName(user.getName());// 创建人用户名
                    itemModel.setPurchaseItemId(item.getId());//采购订单表体id

                    // 根据商品货号获取商品id
                    Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
                    merchandiseInfo_params.put("merchandiseId", item.getGoodsId());
                    MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
                    if (merchandiseInfo != null) {
                        itemModel.setVolume(merchandiseInfo.getVolume());// 体积
                        itemModel.setLength(merchandiseInfo.getLength());// 长
                        itemModel.setWidth(merchandiseInfo.getWidth());// 宽
                        itemModel.setHeight(merchandiseInfo.getHeight());// 高
                    }
                    if(mongo!=null){
                        if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())){
                            itemModel.setTallyingUnit(purchaseOrder.getTallyingUnit());;// 海外仓理货单位
                        }
                    }

                    Long itemId = purchaseWarehouseItemDao.save(itemModel);

                    // 如果批次不为空 批次为空 保存一份采购入库单
                    PurchaseWarehouseBatchModel batch = new PurchaseWarehouseBatchModel();
                    batch.setItemId(itemId);
                    batch.setGoodsId(item.getGoodsId());
                    batch.setWornNum(0);
                    batch.setExpireNum(0);
                    batch.setNormalNum(item.getNum());
                    batch.setCreater(user.getId());

                    if(!DERP_SYS.DEPOTINFO_BATCHVALIDATION_0.equals(mongo.getBatchValidation())) {
                        batch.setBatchNo(batchNo);
                        batch.setProductionDate(produceDate);
                        batch.setOverdueDate(overdueDate);
                    }

                    purchaseWarehouseBatchDao.save(batch);
                }
                // 添加勾稽关系
                WarehouseOrderRelModel warehouseOrderRel = new WarehouseOrderRelModel();
                warehouseOrderRel = new WarehouseOrderRelModel();
                warehouseOrderRel.setWarehouseId(result_id);// 采购入库单id
                warehouseOrderRel.setPurchaseOrderId(id);// 采购订单id
                warehouseOrderRel.setCreater(user.getId());// 创建人
                warehouseOrderRelDao.save(warehouseOrderRel);

                /**
                 * 增加库存
                 *
                 * @author zhanghx
                 */

                InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
                inventoryRoot.setBackTopic(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTopic());
                inventoryRoot.setBackTags(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTags());
                Map<String, Object> customParam = new HashMap<String, Object>();
                customParam.put("userId", user.getId());
                inventoryRoot.setCustomParam(customParam);
                // 增加库存
                inventoryRoot.setMerchantId(purchaseWarehouse.getMerchantId().toString());
                inventoryRoot.setMerchantName(purchaseWarehouse.getMerchantName());
                inventoryRoot.setTopidealCode(user.getTopidealCode());
                inventoryRoot.setBusinessNo(purchaseOrder.getCode());
                inventoryRoot.setDepotId(purchaseWarehouse.getDepotId().toString());
                inventoryRoot.setDepotName(purchaseWarehouse.getDepotName());
                inventoryRoot.setDepotCode(mongo.getCode());
                inventoryRoot.setDepotType(mongo.getType());
                inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
                inventoryRoot.setOrderNo(purchaseWarehouse.getCode());
                inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
                inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
                inventoryRoot.setSourceDate(now);
                inventoryRoot.setBuId(purchaseWarehouse.getBuId().toString());
                inventoryRoot.setBuName(purchaseWarehouse.getBuName());

                // 获取当前年月
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
                Date inWarehouseDate = TimeUtils.StringToDate(inWarehouseDateStr);
                String inWarehouseDateStr2 = sdf2.format(inWarehouseDate);

                inventoryRoot.setOwnMonth(inWarehouseDateStr2);// 归属月份 yyyy-MM
                inventoryRoot.setDivergenceDate(inWarehouseDateStr);// 出入时间  yyyy-MM-dd HH:mm:ss
                int depot_flag = 0;
                if (mongo != null && mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                    depot_flag = 1;
                }
                List<InvetAddOrSubtractGoodsListJson> itemList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
                List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch2(result_id);
                for (PurchaseWarehouseBatchModel bModel : batchList) {
                    // 正常数量
                    InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
                    listJson.setGoodsId(bModel.getGoodsId().toString());
                    listJson.setGoodsNo(bModel.getGoodsNo());
                    listJson.setGoodsName(bModel.getGoodsName());
                    listJson.setIsExpire("1");

                    //如果仓库类型为批次强校验 或 入库强校验\出库弱校验
                    if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(mongo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(mongo.getBatchValidation())) {
                        listJson.setBatchNo(batchNo);
                        listJson.setProductionDate(TimeUtils.format(produceDate, "yyyy-MM-dd"));
                        listJson.setOverdueDate(TimeUtils.format(overdueDate, "yyyy-MM-dd"));
                    }

                    listJson.setNum(bModel.getPurchaseNum());
                    listJson.setBarcode(bModel.getBarcode());

                    // 海外仓必填
                    if (depot_flag == 1) {
                        listJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
                    }
                    listJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
                    itemList.add(listJson);
                }
                inventoryRoot.setGoodsList(itemList);

                jsonList.add(inventoryRoot) ;

            }
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, purchaseWarehouse.getCode(), "采购链路入库", null, null);
        }

        return jsonList ;

    }

    /**
     * 导出查询表头表体
     *
     * @param dto
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getDetailsByExport(PurchaseOrderExportDTO dto, User user)
            throws SQLException {

        Map<String, Object> resultMap = new HashMap<String, Object> () ;

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {

                resultMap.put("orderList", new ArrayList<PurchaseOrderExportDTO>()) ;
                resultMap.put("itemList", new ArrayList<PurchaseOrderItemModel>()) ;

                return resultMap ;
            }

            dto.setBuIds(buIds);
        }

        List<PurchaseOrderExportDTO> list = purchaseOrderDao.getDetailsByExport(dto);
        List<PurchaseOrderItemModel> itemList = new ArrayList<PurchaseOrderItemModel>() ;

        for (PurchaseOrderExportDTO purchaseOrderDTO : list) {

            Long supplierId = purchaseOrderDTO.getSupplierId();

            Map<String , Object> params = new HashMap<String ,Object>() ;
            params.put("customerid", supplierId) ;
            CustomerInfoMogo supplier = customerInfoMongoDao.findOne(params); ;

            if(supplier!= null) {
                if(StringUtils.isBlank(purchaseOrderDTO.getCurrency())
                        && StringUtils.isNotBlank(supplier.getCurrency())) {
                    purchaseOrderDTO.setCurrency(supplier.getCurrency());
                }

                if(DERP_SYS.CUSTOMERINFO_TYPE_1.equals(supplier.getType())){
                    purchaseOrderDTO.setIsInnerMerchant("是");
                }else {
                    purchaseOrderDTO.setIsInnerMerchant("否");
                }
            }

            if(DERP_ORDER.PURCHASEORDER_PAYSUBJECT_2.equals(purchaseOrderDTO.getPaySubject())) {
                purchaseOrderDTO.setPaySubjectLabel(purchaseOrderDTO.getMerchantName());
            }

            PurchaseOrderItemModel queryModel = new PurchaseOrderItemModel() ;
            queryModel.setPurchaseOrderId(purchaseOrderDTO.getId());
            List<PurchaseOrderItemModel> queryItemList = purchaseOrderItemDao.list(queryModel);

            BigDecimal goodsAmount = new BigDecimal(0) ;

            for (PurchaseOrderItemModel itemModel : queryItemList) {
                itemModel.setOrderCode(purchaseOrderDTO.getCode());

                if(itemModel.getAmount() != null) {
                    goodsAmount = goodsAmount.add(itemModel.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP) ;
                }

                itemList.add(itemModel) ;
            }

            purchaseOrderDTO.setAmount(goodsAmount);
        }

        resultMap.put("orderList", list) ;
        resultMap.put("itemList", itemList) ;

        return resultMap;
    }

    /**
     * 根据ids获取销售订单、预售单信息（多个订单时，表头只取其中一个，表体的商品去重叠加）转为采购订单
     *
     * @param ids
     * @throws SQLException
     * @throws NumberFormatException
     */
    @Override
    public Map<String,Object> listCopyOrSaleToOrder(String ids, String type,Long modalSupplierId,
                                                    Long isUse,String purchaseCommission) throws SQLException {

        if(!"purchaseCopy".equals(type)) {
            return null ;
        }

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();

        String[] idArr = ids.split(",");
        for (String id : idArr) {

            purchaseOrder = purchaseOrderDao.searchById(Long.parseLong(id)) ;
            purchaseOrder.setId(null);
            purchaseOrder.setCode(null);
            purchaseOrder.setCreateDate(null);
            purchaseOrder.setCreateName(null);
            purchaseOrder.setCreater(null);
            purchaseOrder.setModifyDate(null);
            purchaseOrder.setModifierUsername(null);
            purchaseOrder.setModifier(null);
            purchaseOrder.setAuditer(null);
            purchaseOrder.setAuditDate(null);
            purchaseOrder.setAuditName(null);
            purchaseOrder.setRate(null);

        }

        // 注入表体数据
        String unNeedIds = "";//用于记录已选择的商品
        List<PurchaseOrderItemModel> itemList = new ArrayList<PurchaseOrderItemModel>();

        PurchaseOrderItemModel queryModel = new PurchaseOrderItemModel() ;
        queryModel.setPurchaseOrderId(Long.valueOf(ids));
        itemList = purchaseOrderItemDao.list(queryModel) ;

        for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {
            unNeedIds += purchaseOrderItemModel.getGoodsId()+",";
        }

        purchaseOrder.setItemList(itemList);

        if(unNeedIds != null && StringUtils.isNotBlank(unNeedIds)){
            unNeedIds = unNeedIds.substring(0,unNeedIds.length()-1);
        }

        Map<String,Object> reMap = new HashMap<String,Object>();
        reMap.put("purchaseOrder", purchaseOrder);
        reMap.put("unNeedIds", unNeedIds);
        return reMap;
    }

    /**
     * 根据id获取详情
     * @param model
     * @return
     */
    @Override
    public String checkContractNoAndPoNo(PurchaseOrderModel model) throws SQLException {
        String result = "";

        if(model.getContractNo()!=null && !"".equals(model.getContractNo())){
            PurchaseOrderModel contractNoModel = new PurchaseOrderModel();
            contractNoModel.setContractNo(model.getContractNo());
            List<PurchaseOrderModel> list = purchaseOrderDao.list(contractNoModel);

            list = list.stream().filter(tempModel -> !DERP.DEL_CODE_006.equals(tempModel.getStatus()))
                    .collect(Collectors.toList()) ;

            if(list!=null && list.size()>0){
                result += "该报关合同号已经被";


                for (PurchaseOrderModel orderModel : list) {

                    result+="【"+orderModel.getCode()+"】";

                }

                result += "使用";
            }
        }

        if(model.getPoNo()!=null && !"".equals(model.getPoNo())){
            PurchaseOrderModel poNoModel = new PurchaseOrderModel();
            poNoModel.setPoNo(model.getPoNo());
            List<PurchaseOrderModel> list = purchaseOrderDao.list(poNoModel);

            list = list.stream().filter(tempModel -> !DERP.DEL_CODE_006.equals(tempModel.getStatus()))
                    .collect(Collectors.toList()) ;

            if(list!=null && list.size()>0){

                if("".equals(result)){
                    result += "该PO号已经被";
                }else{
                    result += "，该PO号已经被";
                }
                for (PurchaseOrderModel orderModel : list) {
                    result+="【"+orderModel.getCode()+"】";
                }
                result += "使用";
            }
        }
        if(!"".equals(result)){
            result+="，是否继续使用？";
        }
        return result;
    }

    /**
     * 修改录入发票 和付款日期
     * tag =1 修改录入日期
     * tag=2 修改 付款日期
     * invoiceDate 录入时间
     * invoiceName 开票人
     * payName 付款人
     * payDate 付款时间
     * id 采购订单id
     * invoiceNo 发票号
     * @return
     */
    @Override
    public void updatePurchaseOrderInvoice(PurchaseInvoiceDTO dto,String tag, User user) throws SQLException {

        PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(dto.getPurchaseOrderId());

        if (!(purchaseOrderModel.getStatus().equals(DERP_ORDER.PURCHASEORDER_STATUS_003)
                || purchaseOrderModel.getStatus().equals(DERP_ORDER.PURCHASEORDER_STATUS_007)
                || purchaseOrderModel.getStatus().equals(DERP_ORDER.PURCHASEORDER_STATUS_005))) {
            throw new DerpException("采购订单状态只能为【已审核】【部分入库】【已入库】") ;
        }

        // 修改录入时间
        if ("1".equals(tag)) {

            if (dto.getDrawInvoiceDate() == null) {
                throw new DerpException("开发票时间不能为空") ;
            }
            PurchaseOrderModel invoicePurchaseModel = new PurchaseOrderModel();
            invoicePurchaseModel.setId(dto.getPurchaseOrderId());
            invoicePurchaseModel.setInvoiceDate(dto.getInvoiceDate());// 录入时间
            invoicePurchaseModel.setInvoiceName(dto.getPayName());// 录入人名称
            invoicePurchaseModel.setDrawInvoiceDate(dto.getDrawInvoiceDate());// 发票时间
            invoicePurchaseModel.setInvoiceNo(dto.getInvoiceNo());  		// 发票号
            if (StringUtils.isBlank(purchaseOrderModel.getMailStatus())) {
                invoicePurchaseModel.setMailStatus(DERP_ORDER.PURCHASEORDER_MAILSTATUS_0);//0 未发送邮件 1,发送邮件1次  2 发送邮件2次
            }

            /**
             * 判断是否录入发票或付款时间
             */
            if(StringUtils.isBlank(purchaseOrderModel.getBillStatus())) {
                // 校验月结关账时间
                FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
                closeAccountsMongo.setMerchantId(purchaseOrderModel.getMerchantId());
                closeAccountsMongo.setDepotId(purchaseOrderModel.getDepotId());
                closeAccountsMongo.setBuId(purchaseOrderModel.getBuId());
                String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
                String maxCloseAccountsMonth="";
                if (StringUtils.isNotBlank(maxdate)) {
                    // 获取该月份下月时间
                    String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
                    maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
                }
                if (StringUtils.isNotBlank(maxCloseAccountsMonth)&&dto.getDrawInvoiceDate()!=null) {
                    // 发票日期必须大于关账日期
                    if (dto.getDrawInvoiceDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                        throw new DerpException("发票日期必须大于关账日期/月结日期") ;
                    }
                }

                invoicePurchaseModel.setBillStatus(DERP_ORDER.PURCHASEORDER_BILLSTATUS_1);//单据状态(1.收到发票,2. 已付款, 3.收到发票已付款)
                invoicePurchaseModel.setCargoCuttingDate(dto.getDrawInvoiceDate());

            }else {
                invoicePurchaseModel.setBillStatus(DERP_ORDER.PURCHASEORDER_BILLSTATUS_3);
            }

            int rows = purchaseOrderDao.modify(invoicePurchaseModel);

            if(rows > 0) {
                for(PurchaseInvoiceItemDTO invoiceItemDTO : dto.getItemList()) {
                    PurchaseOrderItemModel purchaseOrderItemModel = purchaseOrderItemDao.searchById(invoiceItemDTO.getPurchaseItemId()) ;

                    PurchaseOrderItemModel updateModel = new PurchaseOrderItemModel();
                    updateModel.setId(purchaseOrderItemModel.getId());
                    updateModel.setInvoiceAmount(invoiceItemDTO.getAmount());

                    purchaseOrderItemDao.modify(updateModel) ;
                }
            }
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "录入发票时间", null, null);
        }
        // 修改付款时间
        if ("2".equals(tag)) {
            // 支付时间非空判断
            if (dto.getPaymentDate() == null) {
                throw new DerpException("支付时间不能为空") ;
            }

            PurchaseOrderModel payPurchaseModel = new PurchaseOrderModel();
            payPurchaseModel.setId(dto.getPurchaseOrderId());
            payPurchaseModel.setPayName(dto.getPayName());// 支付人
            payPurchaseModel.setPayDate(dto.getPaymentDate());// 支付时间

            if(StringUtils.isBlank(purchaseOrderModel.getBillStatus())) {

                // 校验月结关账时间
                FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
                closeAccountsMongo.setMerchantId(purchaseOrderModel.getMerchantId());
                closeAccountsMongo.setDepotId(purchaseOrderModel.getDepotId());
                closeAccountsMongo.setBuId(purchaseOrderModel.getBuId());
                String maxdate= financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
                String maxCloseAccountsMonth="";
                if (StringUtils.isNotBlank(maxdate)) {
                    // 获取该月份下月时间
                    String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
                    maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
                }
                if (StringUtils.isNotBlank(maxCloseAccountsMonth) && dto.getPaymentDate() != null) {
                    // 付款日期必须大于关账日期
                    if (dto.getPaymentDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                        throw new DerpException("付款日期必须大于关账日期/月结日期") ;
                    }
                }

                payPurchaseModel.setBillStatus(DERP_ORDER.PURCHASEORDER_BILLSTATUS_2);//单据状态(1.收到发票,2. 已付款, 3.收到发票已付款)
                payPurchaseModel.setCargoCuttingDate(dto.getPaymentDate());

            }else {
                payPurchaseModel.setBillStatus(DERP_ORDER.PURCHASEORDER_BILLSTATUS_3);
            }

            purchaseOrderDao.modify(payPurchaseModel);

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "录入付款时间", null, null);
        }
        // 如果tag 不是1 和2  数据异常
    }

    @Override
    public ViewResponseBean getDepotInfo(String id) {
        // 通过仓库id获取仓库信息
        Map<String, Object> depotInfo_params = new HashMap<String, Object>();
        depotInfo_params.put("depotId", Long.parseLong(id));
        DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);
        if (depotInfo == null) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304);
        }
        return ResponseFactory.success(depotInfo.getIsTopBooks());
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[0-9]+(\\.)?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private boolean isValidDate(String str) {
        boolean convertSuccess=true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            convertSuccess=false;
        }
        return convertSuccess;
    }

    @Override
    public List<PurchaseOrderItemModel> getPurchaseItem(Long purchaseId) throws SQLException {

        PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel() ;
        itemModel.setPurchaseOrderId(purchaseId);

        List<PurchaseOrderItemModel> list = purchaseOrderItemDao.list(itemModel);

        return list;
    }

    @Override
    public void saveOrModifyTempOrder(PurchaseOrderModel model, User user) throws Exception {
    	PurchaseOrderModel purchaseOrderModel = saveOrModifyPurchaseOrder(model, user, "1");
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "非必填保存", null, null);
    }

    @Override
    public List<PurchaseOrderDTO> getPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException {
        return purchaseOrderDao.getPendingRecordOrders(dto);
    }

    @Override
    public Integer countPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException {
        return purchaseOrderDao.countPendingRecordOrders(dto);
    }

    /**
     * 判断输入字段是否为空
     * @param index
     * @param content
     * @param msg
     * @param resultList
     * @return
     */
    private boolean checkIsNullOrNot(int index , String content ,
                                     String msg ,List<ImportErrorMessage> resultList ) {

        if(StringUtils.isBlank(content)) {
            ImportErrorMessage message = new ImportErrorMessage();
            message.setRows(index + 1);
            message.setMessage(msg);
            resultList.add(message);

            return true ;

        }else {
            return false ;
        }

    }

    /**
     * 错误时，设置错误内容
     * @param index
     * @param msg
     * @param resultList
     */
    private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(index + 1);
        message.setMessage(msg);
        resultList.add(message);
    }
    @Override
    public PurchaseOrderDTO searchDTODetail(Long id) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(id);
        return purchaseOrderDao.getDTODetails(dto);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PurchaseOrderDTO getListPurchaseOrderByPage(PurchaseOrderDTO dto, String codes,User user) throws SQLException{

        if(dto.getBuId() == null) {
           List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }

        List<String> codeList=null;
        if (!StringUtils.isEmpty(codes)) {
            codeList = Arrays.asList(codes.split(","));
        }
        dto.setCodeList(codeList);

        return purchaseOrderDao.getListPurchaseOrderByPage(dto);
    }

    @Override
    public PurchaseContractModel getContractModelByPurchaseId(Long id) throws SQLException {

        PurchaseContractModel purchaseContractModel = new PurchaseContractModel() ;
        purchaseContractModel.setOrderId(id);
        purchaseContractModel = purchaseContractDao.searchByModel(purchaseContractModel) ;

        PurchaseOrderModel purchaseModel = purchaseOrderDao.searchById(id);

        if(purchaseContractModel != null) {

            purchaseContractModel.setPurchaseOrderNo(purchaseModel.getPoNo());
            purchaseContractModel.setCurrency(purchaseModel.getCurrency());

            return purchaseContractModel ;
        }

        purchaseContractModel = new PurchaseContractModel() ;

        if(purchaseModel != null) {

            Map<String, Object> merchanto_params = new HashMap<String, Object>();
            merchanto_params.put("merchantId", purchaseModel.getMerchantId());
            MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);
            if(merchantMogo != null) {
                purchaseContractModel.setBuyerCnName(merchantMogo.getFullName());
                purchaseContractModel.setBuyerEnName(merchantMogo.getEnglishName());
                purchaseContractModel.setBuyerAuthorizedSignature(merchantMogo.getFullName());
                purchaseContractModel.setBuyerAuthorizedSignatureEn(merchantMogo.getEnglishName());
            }

            Map<String, Object> customer_params = new HashMap<String, Object>();
            customer_params.put("customerid", purchaseModel.getSupplierId()) ;
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params); ;

            if (customer != null) {
                purchaseContractModel.setSellerCnName(customer.getName());
                purchaseContractModel.setSellerEnName(customer.getEnName());
                purchaseContractModel.setSellerAuthorizedSignature(customer.getName());
                purchaseContractModel.setSellerAuthorizedSignatureEn(customer.getEnName());
                purchaseContractModel.setBeneficiaryBankName(customer.getDepositBank());
                purchaseContractModel.setBankAddress(customer.getBankAddress());
                purchaseContractModel.setBeneficiaryName(customer.getBeneficiaryName());
                purchaseContractModel.setAccountNo(customer.getBankAccount());
                purchaseContractModel.setSwiftCode(customer.getSwiftCode());
            }

            purchaseContractModel.setOrderId(purchaseModel.getId());
            purchaseContractModel.setCurrency(purchaseModel.getCurrency());
            purchaseContractModel.setPurchaseOrderNo(purchaseModel.getPoNo());
        }

        return purchaseContractModel;
    }

    @Override
    public void modifyPurchaseContract(PurchaseContractModel model, User user) throws Exception {
        if(model.getId() == null) {
            model.setCreateDate(TimeUtils.getNow());
            purchaseContractDao.save(model) ;
        }else {
            model.setModifyDate(TimeUtils.getNow());
            purchaseContractDao.modify(model) ;
        }

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel () ;
        purchaseOrder.setId(model.getOrderId());
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        /*生成文件*/

        ByteArrayOutputStream bos = exportPurchaseContractPdf(model.getOrderId().toString());
        String pdfFileName = getPDFFileName(purchaseOrder.getCode());

        String basePath = ApolloUtilDB.orderBasepath+"/puchaseContract/"+purchaseOrder.getMerchantId();

        File file = new File(basePath) ;
        if(!file.exists()) {
            file.mkdirs() ;
        }

        pdfFileName = basePath+"/"+ pdfFileName ;

        FileOutputStream out = new FileOutputStream(pdfFileName) ;

        bos.writeTo(out);
        out.flush();

        bos.close();
        out.close();

        /*生成文件*/
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "合同编辑", null, null) ;
    }

    @Override
    public void auditPurchaseContract(PurchaseContractModel model, User user, String status) throws Exception {

        modifyPurchaseContract(model, user) ;

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel () ;
        purchaseOrder.setId(model.getOrderId());
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        Map<String, Object> depotInfo_params = new HashMap<String, Object>();
        depotInfo_params.put("depotId", purchaseOrder.getDepotId());
        DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);

        auditPurchaseOrder(purchaseOrder, depotInfo, user, status);
    }

    @Override
    public void submitPurchaseContract(PurchaseContractModel model, User user) throws Exception {

        modifyPurchaseContract(model, user) ;

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel () ;
        purchaseOrder.setId(model.getOrderId());
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        submitPurchaseOrder(purchaseOrder, user);
    }

    @Override
    public void submitJSONPurchaseContract(Map<String, Object> map, User user) throws Exception {

        modifyJSONPurchaseContract(map, user);

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel () ;
        purchaseOrder.setCode(String.valueOf(map.get("code")));
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        submitPurchaseOrder(purchaseOrder, user);

    }

    /**
     * 提交采购合同
     * @param purchaseOrder
     * @param user
     * @throws SQLException
     */
    private void submitPurchaseOrder(PurchaseOrderModel purchaseOrder, User user) throws SQLException {

        PurchaseOrderModel model = new PurchaseOrderModel();
        model.setId(purchaseOrder.getId());
        model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_002);// 已提交待审核
        model.setSubmitDate(TimeUtils.getNow());// 提交时间
        model.setSubmiter(user.getId());// 提交人id
        model.setSubmiterName(user.getName());// 提交人用户名
        model.setModifyDate(TimeUtils.getNow());

        purchaseOrderDao.modify(model);

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(purchaseOrder.getBuId());
        emailUserDTO.setBuName(purchaseOrder.getBuName());
        emailUserDTO.setMerchantId(purchaseOrder.getMerchantId());
        emailUserDTO.setMerchantName(purchaseOrder.getMerchantName());
        emailUserDTO.setOrderCode(purchaseOrder.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
        emailUserDTO.setSupplier(purchaseOrder.getSupplierName());
        emailUserDTO.setPoNum(purchaseOrder.getPoNo());
        emailUserDTO.setStatus("已提交");
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setAuditorId(purchaseOrder.getAuditer());
        emailUserDTO.setSubmitId(purchaseOrder.getSubmiter()==null?null:Arrays.asList(purchaseOrder.getSubmiter().toString()));
        emailUserDTO.setModifyId(purchaseOrder.getAmountAdjustmenter());
        String auditMethod = purchaseOrder.getAuditMethod();
        String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
        emailUserDTO.setAuditMethod(auditMethodLabel);
        JSONObject json = JSONObject.fromObject(emailUserDTO) ;

        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------采购发送邮件发送失败-------", json.toString());
        }

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "提交", null, null) ;

    }

    @Override
    public Long auditJSONPurchaseContract(Map<String, Object> map, User user, String status) throws Exception {

        modifyJSONPurchaseContract(map, user);

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel () ;
        purchaseOrder.setCode(String.valueOf(map.get("code")));
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        Map<String, Object> depotInfo_params = new HashMap<String, Object>();
        depotInfo_params.put("depotId", purchaseOrder.getDepotId());
        DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);

        auditPurchaseOrder(purchaseOrder, depotInfo, user, status);

        return purchaseOrder.getId() ;
    }

    @Override
    public ByteArrayOutputStream exportPurchaseContractPdf(String orderId) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        PurchaseContractModel contract = this.getContractModelByPurchaseId(Long.valueOf(orderId));
        List<PurchaseOrderItemModel> itemList = this.getPurchaseContractItem(Long.valueOf(orderId));

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, bos);

        String buyerEnName = contract.getBuyerEnName();
        if(StringUtils.isBlank(buyerEnName)) {
            buyerEnName = "" ;
        }
        //设置页码,添加水印
        PdfPageHelper pdfPageHelper = new PdfPageHelper();
        pdfPageHelper.setWaterMarContent(buyerEnName);
        writer.setPageEvent(pdfPageHelper);

        document.open();

        /**
         * 标题
         */
        Paragraph titlePara = PdfUtils.createParagraph();
        PdfUtils.paragraphAddChunk(titlePara, "采购订单（C）" + PdfUtils.LINEFEED, PdfUtils.titleSongfont);
        PdfUtils.paragraphAddChunk(titlePara, "Purchase Contract", PdfUtils.textEnfont);

        /**
         * 头部
         */
        StringBuffer sb = new StringBuffer() ;
        StringBuffer sbSub = new StringBuffer() ;

        PdfPTable headTabel1 = PdfUtils.createTable(new float[] {60f, 75f, 3f, 60f, 80f}) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell("采购合同编号：" , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell(contract.getPurchaseContractNo() == null ? "" : contract.getPurchaseContractNo() , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textKaifont)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell("采购订单号：" , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell(contract.getPurchaseOrderNo() == null ? "" : contract.getPurchaseOrderNo() , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;

        headTabel1.addCell(PdfUtils.createNoBorderCell("Purchase contract No.: " , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell(contract.getPurchaseContractNo() == null ? "" : contract.getPurchaseContractNo() , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textEnfont)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell("Purchase order No.: " , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel1.addCell(PdfUtils.createNoBorderCell(contract.getPurchaseOrderNo() == null ? "" : contract.getPurchaseOrderNo() , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;

        PdfPTable headTabel2 = PdfUtils.createTable(new float[] {20f, 92f, 3f, 20f, 98f}) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell("甲方：" , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell(contract.getBuyerCnName() == null ? "" : contract.getBuyerCnName() , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textKaifont)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell("乙方：" , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell(contract.getSellerCnName() == null ? "" : contract.getSellerCnName() , PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;

        headTabel2.addCell(PdfUtils.createNoBorderCell("Buyer: " , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell(buyerEnName == null ? "" : buyerEnName , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textEnfont)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell("Seller: " , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;
        headTabel2.addCell(PdfUtils.createNoBorderCell(contract.getSellerEnName() == null ? "" : contract.getSellerEnName() , PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT)) ;

        /**
         * 商品明细
         */
        Paragraph itemPara = PdfUtils.createParagraph();
        PdfUtils.paragraphAddChunk(itemPara, "1、	货品明细", PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(itemPara, "Goods details:", PdfUtils.textEnfont);

        PdfPTable itemTable = PdfUtils.createTable(7, Element.ALIGN_LEFT) ;
        itemTable.setWidths(new float[] {50f, 50f, 55f, 55f, 28f, 40f, 50f});

        String currency = contract.getCurrency();

        itemTable.addCell(PdfUtils.createCell("品牌" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("品名" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("条形码" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("规格" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("数量" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("单价(" + currency + ")" , PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("总价(" + currency + ")" , PdfUtils.textKaifont));

        itemTable.addCell(PdfUtils.createCell("Brand", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Goods Description", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Bar Code", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Specification", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Quantity", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Unit Value", PdfUtils.textEnfont));
        itemTable.addCell(PdfUtils.createCell("Total Value", PdfUtils.textEnfont));

        Integer num = 0 ;
        BigDecimal total = new BigDecimal(0) ;
        for (PurchaseOrderItemModel item : itemList) {
            itemTable.addCell(PdfUtils.createCell(item.getBrandName() == null ? "" : item.getBrandName(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getGoodsName() == null ? "" : item.getGoodsName(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getBarcode() == null ? "" : item.getBarcode(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getSpec() == null ? "" : item.getSpec(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getNum() == null ? "" : item.getNum().toString(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getPrice() == null ? "" : item.getPrice().setScale(4, BigDecimal.ROUND_HALF_DOWN).toString(), PdfUtils.textKaifont));
            itemTable.addCell(PdfUtils.createCell(item.getAmount() == null ? "" : item.getAmount().toString(), PdfUtils.textKaifont));

            if(item.getNum() != null) {
                num += item.getNum() ;
            }

            if(item.getAmount() != null) {
                total = total.add(item.getAmount()) ;
            }

        }

        itemTable.addCell(PdfUtils.createCell("合计 Total:", PdfUtils.textKaifont, Element.ALIGN_CENTER, 4));
        itemTable.addCell(PdfUtils.createCell(num.toString(), PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell("/", PdfUtils.textKaifont));
        itemTable.addCell(PdfUtils.createCell(total.toString(), PdfUtils.textKaifont));

        Paragraph para2_cn = PdfUtils.createParagraph("2、运输方式：", PdfUtils.textKaifont);
        para2_cn.setSpacingAfter(0f);
        Paragraph para2_en = PdfUtils.createParagraph("Means of Transportation:", PdfUtils.textEnfont);
        para2_en.setSpacingBefore(0f);

        String meansOfTransportation = contract.getMeansOfTransportation();
        for(DerpBasic means : DERP_ORDER.purchasereContract_tranList) {

            if(StringUtils.isNotBlank(meansOfTransportation) &&
                    meansOfTransportation.indexOf(means.getKey().toString()) > -1) {
                PdfUtils.paragraphAddChunk(para2_cn, "■", PdfUtils.textKaifont);
                PdfUtils.paragraphAddChunk(para2_en, "■", PdfUtils.textKaifont);
            }else {
                PdfUtils.paragraphAddChunk(para2_cn, "□", PdfUtils.textKaifont);
                PdfUtils.paragraphAddChunk(para2_en, "□", PdfUtils.textKaifont);
            }

            if(PdfUtils.isChinese(means.getValue())) {
                PdfUtils.paragraphAddChunk(para2_cn, means.getValue() + PdfUtils.SPACE_CN, PdfUtils.textKaifont) ;
            }else {
                PdfUtils.paragraphAddChunk(para2_cn, means.getValue() + PdfUtils.TAB, PdfUtils.textEnfont) ;
            }

            PdfUtils.paragraphAddChunk(para2_en, means.getKey().toString() + PdfUtils.TAB, PdfUtils.textEnfont) ;
        }

        /**
         * 始发地
         */

        Element elemente3 = null;

        if(StringUtils.isNotBlank(meansOfTransportation) && meansOfTransportation.indexOf(DERP_ORDER.PURCHASERECONTRACT_TRAN_DAP) > -1) {
            Paragraph paragraph3 = PdfUtils.createParagraph() ;

            String deliveryAddress = contract.getDeliveryAddress();
            if(StringUtils.isBlank(deliveryAddress)) {
                deliveryAddress = "" ;
            }

            String deliveryAddressEn = contract.getDeliveryAddressEn();
            if(StringUtils.isBlank(deliveryAddressEn)) {
                deliveryAddressEn = "" ;
            }

            PdfUtils.paragraphAddChunk(paragraph3, "3、发货地址：" + deliveryAddress + PdfUtils.LINEFEED, PdfUtils.textKaifont);
            PdfUtils.paragraphAddChunk(paragraph3, "Delivery Address: ", PdfUtils.textEnfont);

            if(PdfUtils.isChinese(deliveryAddressEn)) {
                PdfUtils.paragraphAddChunk(paragraph3, deliveryAddressEn, PdfUtils.textKaifont);
            }else{
                PdfUtils.paragraphAddChunk(paragraph3, deliveryAddressEn, PdfUtils.textEnfont);
            }

            elemente3 = paragraph3 ;

        }else if(StringUtils.isNotBlank(meansOfTransportation) &&
                meansOfTransportation.indexOf(DERP_ORDER.PURCHASERECONTRACT_TRAN_EXW) > -1) {
            Paragraph paragraph3 = PdfUtils.createParagraph() ;

            String pickingUpAddress = contract.getPickingUpAddress();
            if(StringUtils.isBlank(pickingUpAddress)) {
                pickingUpAddress = "" ;
            }

            String pickingUpAddressEn = contract.getPickingUpAddressEn();
            if(StringUtils.isBlank(pickingUpAddressEn)) {
                pickingUpAddressEn = "" ;
            }

            PdfUtils.paragraphAddChunk(paragraph3, "3、提货地址：" + pickingUpAddress + PdfUtils.LINEFEED, PdfUtils.textKaifont);
            PdfUtils.paragraphAddChunk(paragraph3, "Picking up Address: ", PdfUtils.textEnfont);

            if(PdfUtils.isChinese(pickingUpAddressEn)) {
                PdfUtils.paragraphAddChunk(paragraph3, pickingUpAddressEn, PdfUtils.textKaifont);
            }else{
                PdfUtils.paragraphAddChunk(paragraph3, pickingUpAddressEn, PdfUtils.textEnfont);
            }

            elemente3 = paragraph3 ;

        }else {


            PdfPTable pdfTable3 = PdfUtils.createTable(new float[] {9f, 44f, 85f, 10f, 44f, 85f}) ;

            PdfPCell cell = PdfUtils.createNoBorderCell("3、", PdfUtils.textKaifont);
            cell.setPaddingLeft(-2f);

            String loadingCnPort = contract.getLoadingCnPort();
            if(StringUtils.isBlank(loadingCnPort)) {
                loadingCnPort = "" ;
            }

            String loadingEnPort = contract.getLoadingEnPort();
            if(StringUtils.isBlank(loadingEnPort)) {
                loadingEnPort = "" ;
            }

            String destinationdCn = contract.getDestinationdCn() ;
            if(StringUtils.isBlank(destinationdCn)) {
                destinationdCn = "" ;
            }

            String destinationdEn = contract.getDestinationdEn() ;
            if(StringUtils.isBlank(destinationdEn)) {
                destinationdEn = "" ;
            }

            pdfTable3.addCell(cell);
            pdfTable3.addCell(PdfUtils.createNoBorderCell("始发地（港）：", PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            pdfTable3.addCell(PdfUtils.createNoBorderCell(loadingCnPort, PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            pdfTable3.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textKaifont));
            pdfTable3.addCell(PdfUtils.createNoBorderCell("目的地（港）：", PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            pdfTable3.addCell(PdfUtils.createNoBorderCell(destinationdCn, PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));

            pdfTable3.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textEnfont));
            pdfTable3.addCell(PdfUtils.createNoBorderCell("Loading Port: ", PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT));

            if(PdfUtils.isChinese(loadingEnPort)) {
                pdfTable3.addCell(PdfUtils.createNoBorderCell(loadingEnPort, PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            }else {
                pdfTable3.addCell(PdfUtils.createNoBorderCell(loadingEnPort, PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            }

            pdfTable3.addCell(PdfUtils.createNoBorderCell("", PdfUtils.textEnfont));
            pdfTable3.addCell(PdfUtils.createNoBorderCell("Destination: ", PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT));

            if(PdfUtils.isChinese(destinationdEn)) {
                pdfTable3.addCell(PdfUtils.createNoBorderCell(destinationdEn, PdfUtils.textKaifont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            }else {
                pdfTable3.addCell(PdfUtils.createNoBorderCell(destinationdEn, PdfUtils.textEnfont, Element.ALIGN_TOP, Element.ALIGN_LEFT));
            }

            elemente3 = pdfTable3 ;
        }

        /**
         * 交货日期
         */
        sb = new StringBuffer() ;
        sbSub = new StringBuffer() ;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日") ;
        Timestamp deliveryDate = contract.getDeliveryDate() ;
        String deliveryDateStr = "" ;
        if(deliveryDate != null) {
            deliveryDateStr = sdf.format(contract.getDeliveryDate()) ;
        }


        sb.append("4、交货日期：" + deliveryDateStr +"(若此交货日期与甲方与最终买方签署的框架采购合同不一致的,以此交货期为准)") ;

        sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) ;
        if(deliveryDate != null) {
            deliveryDateStr = sdf.format(contract.getDeliveryDate()) ;
        }

        sbSub.append("Delivery date: " + deliveryDateStr + " (If this delivery date is in conflict with the delivery date stipulated in the Purchase Frame Contract signed by the Buyer and the ultimate buyer, this one will prevail.)") ;

        Paragraph para4 = PdfUtils.createParagraph();
        PdfUtils.paragraphAddChunk(para4, sb.toString() + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para4, sbSub.toString(), PdfUtils.textEnfont);

        /**
         * 付款方式
         */
        sb = new StringBuffer() ;
        sbSub = new StringBuffer() ;
        sb.append("5、付款方式：") ;
        sbSub.append("Terms of payment: ") ;

        String paymentMethodText = contract.getPaymentMethodText();
        if(StringUtils.isBlank(paymentMethodText)) {
            paymentMethodText = "" ;
        }

        String paymentMethodTextEn = contract.getPaymentMethodTextEn();
        if(StringUtils.isBlank(paymentMethodTextEn)) {
            paymentMethodTextEn = "" ;
        }

        if("1".equals(contract.getPaymentMethod())) {
            sb.append("双方确认本订单之日起" + paymentMethodText + "个工作日内甲方向乙方支付订单总金额100%货款") ;
            sbSub.append("TT 100% within " + paymentMethodTextEn +" working days from the date of this order.") ;
        }else if("3".equals(contract.getPaymentMethod())) {
            sb.append(paymentMethodText) ;
            sbSub.append(paymentMethodText) ;
        }else if("2".equals(contract.getPaymentMethod())) {
            String[] arr = paymentMethodText.split(";", -1);
            String[] arrEn = paymentMethodTextEn.split(";", -1);

            if(arr.length > 0) {
                sb.append("卖方确认本订单之日起"+ arr[0] +
                        "个工作日内，买方支付订单总金额的"+arr[1]+
                        "货款："+arr[2]+"，货物到仓后买方验收合格且双方确认理货数据无误后在"+
                        arr[3]+"个工作日内支付剩余"+arr[4]+"货款:"+arr[5]+"。") ;
            }else {
                sb.append("卖方确认本订单之日起"+PdfUtils.TAB+
                        "个工作日内，买方支付订单总金额的"+PdfUtils.TAB+
                        "货款："+PdfUtils.TAB+"，货物到仓后买方验收合格且双方确认理货数据无误后在"+
                        PdfUtils.TAB+"个工作日内支付剩余"+PdfUtils.TAB+"货款:"+PdfUtils.TAB+"。") ;
            }

            if(arrEn.length > 0) {
                sbSub.append("TT "+ arrEn[0] + PdfUtils.SPACE + arrEn[1] +" within "+ arrEn[2] +
                        " working days from the date of this order, TT " + arrEn[3] + PdfUtils.SPACE + arrEn[4] +
                        " within "+ arrEn[5] +" working days subject to the satisfaction of the following conditions: (1) delivery of goods to the designated warehouse, (2) the quality of goods is satisfied by the buyer after inspection, and (3) both parties’ confirmation on the quantity of delivered goods.") ;
            }else {
                sbSub.append("TT " + PdfUtils.SPACE8 + "within "+ PdfUtils.TAB +" working days from the date of this order, TT "+ PdfUtils.SPACE8 +
                        " within "+ PdfUtils.TAB  +" working days subject to the satisfaction of the following conditions: (1) delivery of goods to the designated warehouse, (2) the quality of goods is satisfied by the buyer after inspection, and (3) both parties’ confirmation on the quantity of delivered goods.") ;
            }

        }

        Paragraph para5 = PdfUtils.createParagraph();
        PdfUtils.paragraphAddChunk(para5, sb.toString() + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para5, sbSub.toString(), PdfUtils.textEnfont);

        /**
         * 特别约定
         */
        String specialAgreement = contract.getSpecialAgreement();
        if(StringUtils.isBlank(specialAgreement)) {
            specialAgreement = "" ;
        }

        String specialAgreementEn = contract.getSpecialAgreementEn();
        if(StringUtils.isBlank(specialAgreementEn)) {
            specialAgreementEn = "" ;
        }

        Paragraph para6 = PdfUtils.createParagraph();
        PdfUtils.paragraphAddChunk(para6, "6、特别约定（本条与本订单约定的内容存在冲突的，以本条约定的为准）：【"
                + specialAgreement + "】" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para6, "Special agreement: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para6, "【", PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para6, specialAgreementEn, PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para6, "】", PdfUtils.textKaifont);

        /**
         * 收款账户
         */

        String accountNo = contract.getAccountNo();
        if(StringUtils.isBlank(accountNo)) {
            accountNo = "" ;
        }

        String beneficiaryName = contract.getBeneficiaryName();
        if(StringUtils.isBlank(beneficiaryName)) {
            beneficiaryName = "" ;
        }

        String beneficiaryBankName = contract.getBeneficiaryBankName();
        if(StringUtils.isBlank(beneficiaryBankName)) {
            beneficiaryBankName = "" ;
        }

        String bankAddress = contract.getBankAddress();
        if(StringUtils.isBlank(bankAddress)) {
            bankAddress = "" ;
        }

        String swiftCode = contract.getSwiftCode();
        if(StringUtils.isBlank(swiftCode)) {
            swiftCode = "" ;
        }

        String currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;

        Paragraph para7 = PdfUtils.createParagraph();

        PdfUtils.paragraphAddChunk(para7, "7、乙方（供货方）收款账户：" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Banking information of the seller:" + PdfUtils.LINEFEED, PdfUtils.textEnfont);

        PdfUtils.paragraphAddChunk(para7, "账号： " + accountNo + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "采购币种：" + currencyLabel + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "账户：" + beneficiaryName + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "开户行：" + beneficiaryBankName + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "地址：" + bankAddress + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Swift Code: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, swiftCode + PdfUtils.LINEFEED, PdfUtils.textEnfont);

        PdfUtils.paragraphAddChunk(para7, PdfUtils.LINEFEED + PdfUtils.LINEFEED, PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, "Account No.: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, accountNo + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Currency: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, currency + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Beneficiary Name: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, beneficiaryName + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Beneficiary Bank Name: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, beneficiaryBankName + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Bank Address: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, bankAddress + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para7, "Swift Code: ", PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para7, swiftCode + PdfUtils.LINEFEED, PdfUtils.textEnfont);


        /**
         * 盖章
         */
        String buyerAuthorizedSignature = contract.getBuyerAuthorizedSignature();
        if(StringUtils.isBlank(buyerAuthorizedSignature)) {
            buyerAuthorizedSignature = "" ;
        }

        String buyerAuthorizedSignatureEn = contract.getBuyerAuthorizedSignatureEn();
        if(StringUtils.isBlank(buyerAuthorizedSignatureEn)) {
            buyerAuthorizedSignatureEn = "" ;
        }

        String sellerAuthorizedSignature = contract.getSellerAuthorizedSignature();
        if(StringUtils.isBlank(sellerAuthorizedSignature)) {
            sellerAuthorizedSignature = "" ;
        }

        String sellerAuthorizedSignatureEn = contract.getSellerAuthorizedSignatureEn();
        if(StringUtils.isBlank(sellerAuthorizedSignatureEn)) {
            sellerAuthorizedSignatureEn = "" ;
        }

        Paragraph para8 = PdfUtils.createParagraph();

        PdfUtils.paragraphAddChunk(para8, "甲方：" + buyerAuthorizedSignature + PdfUtils.SPACE6_CN + "（盖章）" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para8, "Buyer: " + buyerAuthorizedSignatureEn + PdfUtils.SPACE8 + "(Common Seal)" + PdfUtils.LINEFEED, PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para8, "授权代表签字：" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para8, "Authorized signature: " + PdfUtils.LINEFEED + PdfUtils.LINEFEED, PdfUtils.textEnfont);

        PdfUtils.paragraphAddChunk(para8, "乙方：" + sellerAuthorizedSignature + PdfUtils.SPACE6_CN + "（盖章）" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para8, "Seller: " + sellerAuthorizedSignatureEn + PdfUtils.SPACE8 + "(Common Seal)" + PdfUtils.LINEFEED, PdfUtils.textEnfont);
        PdfUtils.paragraphAddChunk(para8, "授权代表签字：" + PdfUtils.LINEFEED, PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para8, "Authorized signature: " + PdfUtils.LINEFEED + PdfUtils.LINEFEED, PdfUtils.textEnfont);




        /**
         * 签订日期
         */
        Paragraph para9 = PdfUtils.createParagraph();

        PdfUtils.paragraphAddChunk(para9, PdfUtils.LINEFEED + "签订日期：" , PdfUtils.textKaifont);
        PdfUtils.paragraphAddChunk(para9, PdfUtils.LINEFEED + "Date: " , PdfUtils.textEnfont);

        document.add(titlePara) ;
        document.add(headTabel1) ;
        document.add(headTabel2) ;
        document.add(itemPara) ;
        document.add(itemTable) ;
        document.add(para2_cn) ;
        document.add(para2_en) ;
        document.add(elemente3) ;
        document.add(para4) ;
        document.add(para5) ;
        document.add(para6) ;
        document.add(para7) ;
        document.add(para8) ;
        document.add(para9) ;

        document.close();

        return bos;
    }

    @Override
    public List<PurchaseOrderItemModel> getPurchaseContractItem(Long id) throws SQLException {
        PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel() ;
        itemModel.setPurchaseOrderId(id);

        List<PurchaseOrderItemModel> list = purchaseOrderItemDao.list(itemModel);

        for (PurchaseOrderItemModel purchaseOrderItemModel : list) {

            Map<String, Object> queryMap = new HashMap<String, Object>() ;

            queryMap.put("merchandiseId", purchaseOrderItemModel.getGoodsId()) ;
            MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(queryMap);

            if(merchandiseInfoMogo == null) {
                continue ;
            }

            purchaseOrderItemModel.setBrandName("");

            queryMap.clear();
            purchaseOrderItemModel.setSpec(merchandiseInfoMogo.getSpec());

            Long brandId = merchandiseInfoMogo.getBrandId();

            if(brandId != null) {

                queryMap.put("brandId", brandId) ;

                BrandMongo brand = brandMongoDao.findOne(queryMap);

                if(brand == null) {
                    continue ;
                }

                purchaseOrderItemModel.setBrandName(brand.getName());

            }

        }

        return list;
    }

    @Override
    public Map<String, Object> sumContractTotal(List<PurchaseOrderItemModel> itemList) {

        Integer total = 0 ;
        BigDecimal totalAccount = new BigDecimal(0) ;

        for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {

            if(purchaseOrderItemModel.getNum() != null) {
                total += purchaseOrderItemModel.getNum() ;
            }

            if(purchaseOrderItemModel.getAmount() != null) {
                totalAccount = totalAccount.add(purchaseOrderItemModel.getAmount()) ;
            }

        }

        Map<String, Object> map = new HashMap<String, Object>() ;
        map.put("total", total) ;
        map.put("totalAccount", totalAccount) ;

        return map;
    }

    @Override
    public Timestamp getAttributionDate(Long id) throws SQLException {

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        sumitRequiredVerification(purchaseOrder) ;

        if(DERP_ORDER.PURCHASEORDER_STATUS_002.equals(purchaseOrder.getStatus())) {

            /*
             *	添加供应链必填项校验，抛出异常
             */
            supplyChainRequiredVerification(purchaseOrder) ;
        }

        Timestamp attributionDate = purchaseOrder.getAttributionDate();

        if(attributionDate == null) {
            attributionDate = TimeUtils.getNow() ;
        }

        return attributionDate;
    }

    /**
     * 提交状态下校验
     * @param model
     */
    private void sumitRequiredVerification(PurchaseOrderModel model) {

        boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId())
                .addObject(model.getCurrency())
                .addObject(model.getPoNo()).addObject(model.getAttributionDate())
                .addObject(model.getBuId()).empty();

        if (isNull) {
            // 输入信息不完整
            throw new DerpException("采购订单基本信息，必填项缺少，请返回【上一步】填写") ;
        }

    }

    /**
     * 采购订单编辑页“车次”、“标准箱TEU”、“托数”、“承运商”、“运输方式”、“提单号”、“装货港”、“卸货港”、“目的地名称”
     * 字段调整至合同页点击保存并审核后校验
     * @param purchaseOrder
     */
    private void supplyChainRequiredVerification(PurchaseOrderModel purchaseOrder) {

        if(purchaseOrder.getDepotId() != null){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("depotId", purchaseOrder.getDepotId());
            DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

            // 中转仓不校验
            if(DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
                return ;
            }
        }

        //1、运输方式必填
		/*
		 * if(StringUtils.isBlank(purchaseOrder.getTransport())) { throw new
		 * DerpException("采购订单运输方式必填，请返回【上一步】填写") ; }
		 */

    }

    @Override
    public boolean updateAttributionDate(PurchaseOrderModel model) throws Exception {

        Long orderId = model.getId();

        PurchaseOrderModel tempModel = purchaseOrderDao.searchById(orderId);

        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
        closeAccountsMongo.setMerchantId(tempModel.getMerchantId());
        closeAccountsMongo.setDepotId(tempModel.getDepotId());
        closeAccountsMongo.setBuId(tempModel.getBuId());
        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
        String maxCloseAccountsMonth = "";
        if (StringUtils.isNotBlank(maxdate)) {
            // 获取该月份下月时间
            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
        }
        if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
            // 关账下个月日期大于 入库日期
            if (model.getAttributionDate().getTime() < Timestamp
                    .valueOf(maxCloseAccountsMonth).getTime()) {
                throw new RuntimeException("归属时间必须大于关账日期/月结日期") ;
            }
        }

        int rows = purchaseOrderDao.modify(model);

        if(rows > 0) {
            return true ;
        }

        return false;
    }

    /**
     * 获取弹框里的所有可转销的采购单
     * @param json
     * @param merchantId
     * @param topidealCode
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<PurchaseOrderDTO> getOwnPurchaseOrder(String json,Long merchantId,String topidealCode) throws SQLException {
        //解析json
        Long customerId = null;
        String purchaseOrderCodeStr = null;
        String poNoStr = null;
        // 页面上查询条件
        if(StringUtils.isNotBlank(json)){
            JSONObject jsonObj = JSONObject.fromObject(json);
            String customerStr = (String)jsonObj.get("customerId");
            customerId = StringUtils.isNotBlank(customerStr)==true?Long.valueOf(customerStr):null;// 商家
            purchaseOrderCodeStr = (String) jsonObj.get("purchaseOrderCodeStr");
            poNoStr = (String) jsonObj.get("poNoStr");
        }
        // 根据当前登录用户的卓志编码查询供应商表（对应供应商表的主数据客户ID），得到对应供应商id,然后再查询供应商为该id的采购单
        Map<String , Object> params = new HashMap<String ,Object>() ;
        params.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2);// 类型：供应商
        params.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);// 1-使用中
        params.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1);//客户类型 1内部
        params.put("mainId", topidealCode);//主数据客户id(卓志编码)
        List<CustomerInfoMogo> customerInfoList = customerInfoMongoDao.findAll(params);// 正常情况下只有一条
        if (customerInfoList == null || customerInfoList.size()==0) {
            throw new RuntimeException("主数据客户id："+topidealCode+"客户信息表没找到对应供应商信息") ;
        }else if(customerInfoList.size()>1){
            LOGGER.info("主数据客户id:"+topidealCode+"在客户信息表找到多条供应商");
            throw new RuntimeException("主数据客户id:"+topidealCode+"在客户信息表找到多条供应商") ;
        }
        params.clear();
        CustomerInfoMogo customerInfoMogo = customerInfoList.get(0);
        params.put("customerId",customerInfoMogo.getCustomerid());
        //	params.put("merchantId",merchantId);
        params.put("status","1");
        List<CustomerMerchantRelMongo> customerMerchantRelList = customerMerchantRelMongoDao.findAll(params);
        if(customerMerchantRelList==null || customerMerchantRelList.size()==0){
            throw new RuntimeException("供应商:"+customerInfoMogo.getName()+"在客户信息关联表没找到对应信息") ;
        }
        List<Long> supplierIdList = new ArrayList<>();
        for (int i = 0; i <customerMerchantRelList.size(); i++) {
            supplierIdList.add(customerMerchantRelList.get(i).getCustomerId());// 供应商ID
        }

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
        purchaseOrderDTO.setSupplierIdList(supplierIdList);//供应商
        //对应页面的客户
        purchaseOrderDTO.setMerchantId(customerId);
        purchaseOrderDTO.setPoNo(poNoStr);
        if(StringUtils.isNotBlank(purchaseOrderCodeStr)){
            Set<String> codeSet = new HashSet<>();
            String[] codeSplit = purchaseOrderCodeStr.split(",");
            for (int i = 0; i < codeSplit.length; i++) {
                codeSet.add(codeSplit[i]);
            }
            List purchaseOrderCodeList = new ArrayList(codeSet);
            purchaseOrderDTO.setCodeList(purchaseOrderCodeList);// 采购订单号
        }
        return purchaseOrderDao.getOwnPurchaseOrder(purchaseOrderDTO);
    }

    /**
     * 商品标准条码查该公司主体该销售出库仓下对应的商品选品限制的对应商品
     * @param purchaseId
     * @param merchantId
     * @return
     */
    @Override
    public String checkGoodsInfo(Long purchaseId,Long outDepotId,Long merchantId) throws Exception {
        PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);// 采购订单
        // 查询商家
        Map<String, Object> merchantMap = new HashMap<String, Object>();
        merchantMap.put("merchantId", purchaseOrderModel.getMerchantId());// 采购单的商家id
        MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);

        Map<String , Object> cusParams = new HashMap<String ,Object>() ;
        cusParams.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1);// 类型：客户
        cusParams.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);// 1-使用中
        cusParams.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1);//客户类型 1内部
        cusParams.put("mainId", merchant.getTopidealCode());//主数据客户id(采购单商家的卓志编码)
        List<CustomerInfoMogo> customerList = customerInfoMongoDao.findAll(cusParams);
        if (customerList == null || customerList.size() == 0) {
            LOGGER.info("卓志编码:"+merchant.getTopidealCode()+"客户信息表没找到对应信息");
            return "卓志编码:"+merchant.getTopidealCode()+"客户信息表没找到对应信息" ;
        }else if(customerList.size()>1){
            LOGGER.info("卓志编码:"+merchant.getTopidealCode()+"客户信息表找到多条对应信息");
            return "卓志编码:"+merchant.getTopidealCode()+"客户信息表找到多条对应信息" ;
        }
        cusParams.clear();
        CustomerInfoMogo customerInfoMogo = customerList.get(0);
        cusParams.put("customerId",customerInfoMogo.getCustomerid());
        cusParams.put("merchantId",merchantId);// 当前登录的公司
        cusParams.put("status","1");
        CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(cusParams);
        if(customerMerchantRelMongo==null){
            LOGGER.info("客户:"+customerInfoMogo.getName()+"在客户信息关联表没找到对应信息");
            return "客户:"+customerInfoMogo.getName()+"在客户信息关联表没找到对应信息";
        }
        // 通过仓库id获取仓库信息
        Map<String, Object> depotInfo_params = new HashMap<String, Object>();
        depotInfo_params.put("depotId", outDepotId);// 销售出库仓
        DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);
        if (depotInfo == null) {
            return "仓库不存在";
        }
        // 采购单商品
        PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
        purchaseOrderItemModel.setPurchaseOrderId(purchaseId);
        List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(purchaseOrderItemModel);

        // 查询入库仓库公司关联信息
        Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
        depotMerchantRelParam.put("merchantId", merchantId);
        depotMerchantRelParam.put("depotId", outDepotId);	//销售出库仓
        DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
        if(inDepotMerchantRelMongo == null){
            LOGGER.info("销售出库仓ID："+outDepotId+",未查到该公司下入库仓库信息");
            return "销售出库仓ID："+outDepotId+",未查到该公司下入库仓库信息";
        }
        //检查相同SKU是否存在多条
        if(!checkRepeatGoods(purchaseId)){
            throw new DerpException("单据存在多条相同SKU!") ;
        }
        for (int i = 0; i <itemList.size(); i++) {
            PurchaseOrderItemModel itemModel = itemList.get(i);
            // 商品货号+采购订单的公司查询商品信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("goodsNo", itemModel.getGoodsNo());
            params.put("merchantId", purchaseOrderModel.getMerchantId());// 采购单公司
            MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(params);

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
            paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
            paramMap.put("merchantId", merchantId);
            List<MerchandiseInfoMogo> inMerchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap, outDepotId);

            if(inMerchandiseList == null || inMerchandiseList.size()==0){
                return "标准条码："+merchandiseInfoMogo.getCommbarcode()+"在"+depotInfo.getName()+"查找不到对应商品";
            }else if(inMerchandiseList.size()>1){
                return "标准条码："+merchandiseInfoMogo.getCommbarcode()+"在"+depotInfo.getName()+"查找到多个对应商品，请留意！";
            }
        }
        return "确定生成销售单吗?";
    }



    @Override
    public String getFileTempCode(Long tempId) throws SQLException {

        if(tempId == null) {
            return null ;
        }

        FileTempModel fileTempModel = fileTempDao.searchById(tempId) ;

        if(fileTempModel != null) {
            return fileTempModel.getCode() ;
        }

        return null;
    }

    @Override
    public Map<String, Object> getBayerContractByPurchaseId(Long id) throws Exception {

        Map<String, Object> map = new HashMap<>() ;

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject() ;
        JSONArray goodsList = new JSONArray() ;

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setId(id);

        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        List<PurchaseOrderItemModel> itemList = purchaseOrder.getItemList();

        BigDecimal totalNum = new BigDecimal(0) ;
        BigDecimal totalAccount = new BigDecimal(0) ;

        for (int i = 0 ; i < itemList.size(); i ++) {

            com.alibaba.fastjson.JSONObject itemJson = new com.alibaba.fastjson.JSONObject() ;

            PurchaseOrderItemModel item = itemList.get(i);

            Map<String, Object> queryMerchantMap = new HashMap<>() ;

            queryMerchantMap.put("merchandiseId", item.getGoodsId()) ;

            MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(queryMerchantMap);

            String brand = "" ;
            String skus = "" ;
            String goodsEngLishName = "" ;
            if(merchandiseInfo != null) {

                goodsEngLishName = merchandiseInfo.getEnglishGoodsName() ;

                if(StringUtils.isBlank(goodsEngLishName)) {
                    goodsEngLishName = "" ;
                }

                skus = merchandiseInfo.getSpec() ;

                Map<String, Object> queryBrandMap = new HashMap<>() ;

                queryBrandMap.put("brandId", merchandiseInfo.getBrandId()) ;
                BrandMongo brandMongo = brandMongoDao.findOne(queryBrandMap);

                if(brandMongo != null) {
                    brand = brandMongo.getEnglishBrandName() ;
                }

            }

            totalNum = totalNum.add(new BigDecimal(item.getNum())) ;
            totalAccount = totalAccount.add(item.getAmount()) ;

            int index = i + 1;

            BigDecimal price = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal amount = item.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);

            itemJson.put("index", index) ;
            itemJson.put("brand", brand) ;
            itemJson.put("productName", goodsEngLishName) ;
            itemJson.put("skus", skus) ;
            itemJson.put("barcode", item.getBarcode()) ;
            itemJson.put("num", StrUtils.intFormatString(item.getNum())) ;
            itemJson.put("price", StrUtils.doubleFormatString(price.doubleValue())) ;
            itemJson.put("amount", StrUtils.doubleFormatString(amount.doubleValue())) ;
            itemJson.put("goodsNo",item.getGoodsNo());
            itemJson.put("purchaseItemId",item.getId());
            goodsList.add(itemJson) ;
        }

        json.put("poNo", purchaseOrder.getPoNo()) ;
        json.put("currency", purchaseOrder.getCurrency()) ;
        json.put("totalNum", StrUtils.intFormatString(totalNum.intValue())) ;
        json.put("totalAccount", StrUtils.doubleFormatString(totalAccount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())) ;

        /******查询是否存在文件模版记录********/
        Map<String, Object> fileTempDataQueryMap = new HashMap<String, Object>() ;
        fileTempDataQueryMap.put("code", purchaseOrder.getCode()) ;

        FileTempDataMongo fileTempData = fileTempDataMongoDao.findOne(fileTempDataQueryMap);

        if(fileTempData != null) {
            String dataJson = fileTempData.getDataJson();

            json = com.alibaba.fastjson.JSONObject.parseObject(dataJson) ;
            json.put("poNo", purchaseOrder.getPoNo()) ;
            json.put("currency", purchaseOrder.getCurrency()) ;
            json.put("totalNum", StrUtils.intFormatString(totalNum.intValue())) ;
            json.put("totalAccount", StrUtils.doubleFormatString(totalAccount.doubleValue())) ;

            goodsList = (JSONArray)json.remove("goodsList") ;
            Map<String,List<PurchaseOrderItemModel>> itemMap = itemList.stream().collect(Collectors.groupingBy(i-> i.getGoodsNo()+"_"+StrUtils.doubleFormatString(i.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
            goodsList = comparePDFItem(itemMap, goodsList, "Bayer") ;

            json.put("code", purchaseOrder.getCode()) ;
            map.put("detail", json) ;
            map.put("goodsList", goodsList) ;

            return map ;
        }
        /******查询是否存在文件模版记录********/

        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", purchaseOrder.getMerchantId()) ;
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        json.put("code", purchaseOrder.getCode()) ;
        json.put("buyerEnName", merchant.getEnglishName()) ;
        json.put("buyerAddress", merchant.getEnglishRegisteredAddress()) ;

        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", purchaseOrder.getSupplierId()) ;
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params); ;

        if (customer != null) {
            json.put("sellerEnName", customer.getEnName()) ;
            json.put("sellerAddress", customer.getEnBusinessAddress()) ;
        }

        map.put("detail", json) ;
        map.put("goodsList", goodsList) ;

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void modifyJSONPurchaseContract(Map<String, Object> map, User user) throws Exception {

        Set<String> keys = map.keySet();

        JSONObject mongoJSON = new JSONObject() ;

        JSONObject dataJson = new JSONObject() ;
        JSONArray goodList = new JSONArray() ;

        for (String key : keys) {

            if(key.indexOf(".") > -1) {
                String[] org_param = key.split("\\.") ;

                if(org_param[0].indexOf("detail") > -1) {

                    dataJson.put(org_param[1], map.get(key)) ;

                }else if(org_param[0].indexOf("goods") > -1) {

                    List<String> vals = (ArrayList<String>)map.get(key) ;

                    if(vals.isEmpty()) {
                        continue ;
                    }

                    for (int i = 0 ; i < vals.size(); i ++) {

                        JSONObject goods = null ;

                        if(goodList.isEmpty() ||
                                goodList.size() < vals.size()) {
                            goods = new JSONObject() ;
                            goods.put(org_param[1], vals.get(i)) ;

                            goodList.add(goods) ;

                        }else {
                            goods = (JSONObject)goodList.get(i) ;
                            goods.put(org_param[1], vals.get(i)) ;
                        }

                    }
                }

            }else {
                mongoJSON.put(key, map.get(key)) ;
            }

        }

        dataJson.put("goodsList", goodList) ;


        Map<String, Object> removeMap = new HashMap<String, Object>();
        removeMap.put("code", mongoJSON.get("code")) ;
        removeMap.put("fileTempCode", mongoJSON.get("fileTempCode")) ;
        fileTempDataMongoDao.remove(removeMap);

        String pdfFileName = getPDFFileName(String.valueOf(mongoJSON.get("code")));

        String basePath = ApolloUtilDB.orderBasepath+"/puchaseContract/"+user.getMerchantId();

        File file = new File(basePath) ;

        if(!file.exists()) {
            file.mkdirs() ;
        }

        pdfFileName = basePath+"/"+ pdfFileName ;

        mongoJSON.put("path", pdfFileName) ;
        mongoJSON.put("dataJson", dataJson.toString()) ;
        fileTempDataMongoDao.insertJson(mongoJSON.toString());

        ByteArrayOutputStream bos = getTempDataFileStream(mongoJSON.getString("code"), mongoJSON.getString("fileTempCode"), "horizontal");
        FileOutputStream fileOut = new FileOutputStream(pdfFileName);
        bos.writeTo(fileOut);
        fileOut.close();

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, mongoJSON.getString("code"), "合同编辑", null ,null);

    }

    /**
     * 获取宝洁合同
     */
    @Override
    public Map<String, Object> getPGContractByPurchaseId(Long id) throws Exception {
        Map<String, Object> map = new HashMap<>() ;

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject() ;
        JSONArray goodsList = new JSONArray() ;

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setId(id);

        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;
        List<PurchaseOrderItemModel> itemList = purchaseOrder.getItemList();

        for (int i = 0 ; i < itemList.size(); i ++) {

            com.alibaba.fastjson.JSONObject itemJson = new com.alibaba.fastjson.JSONObject() ;

            PurchaseOrderItemModel item = itemList.get(i);
            itemJson.put("goodsNo",item.getGoodsNo());
            itemJson.put("price",StrUtils.doubleFormatString(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            itemJson.put("description", item.getGoodsName()) ;
            itemJson.put("pgCode", item.getGoodsNo()) ;
            itemJson.put("orderQty", item.getNum()) ;
            itemJson.put("purchaseItemId",item.getId());

            goodsList.add(itemJson) ;
        }

        json.put("poNo", purchaseOrder.getPoNo()) ;

        /******查询是否存在文件模版记录********/
        Map<String, Object> fileTempDataQueryMap = new HashMap<String, Object>() ;
        fileTempDataQueryMap.put("code", purchaseOrder.getCode()) ;

        FileTempDataMongo fileTempData = fileTempDataMongoDao.findOne(fileTempDataQueryMap);

        if(fileTempData != null) {
            String dataJson = fileTempData.getDataJson();

            json = com.alibaba.fastjson.JSONObject.parseObject(dataJson) ;
            json.put("poNo", purchaseOrder.getPoNo()) ;

            goodsList = (JSONArray)json.remove("goodsList") ;
            Map<String,List<PurchaseOrderItemModel>> itemMap = itemList.stream().collect(Collectors.groupingBy(i-> i.getGoodsNo()+"_"+StrUtils.doubleFormatString(i.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
            goodsList = comparePDFItem(itemMap, goodsList, "pg") ;

            json.put("code", purchaseOrder.getCode()) ;
            map.put("detail", json) ;
            map.put("goodsList", goodsList) ;

            return map ;
        }
        /******查询是否存在文件模版记录********/

        json.put("code", purchaseOrder.getCode()) ;

        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", purchaseOrder.getSupplierId()) ;
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params);

        if(customer != null) {

            String distributionChannel = "" ;
            String soldToCode = "" ;
            String shipToCode = "" ;
            String deliveryPlant = "" ;

            if("1000001022".equals(customer.getMainId())) {
                distributionChannel = "03" ;
                soldToCode = "2002793734" ;
                shipToCode = "2002793735" ;
                deliveryPlant = "6063" ;
            }else if("1000001024".equals(customer.getMainId())) {
                distributionChannel = "01" ;
                soldToCode = "151327668" ;
                shipToCode = "152349201" ;
                deliveryPlant = "5740" ;
            }

            json.put("distributionChannel", distributionChannel) ;
            json.put("soldToCode", soldToCode) ;
            json.put("shipToCode", shipToCode) ;
            json.put("deliveryPlant", deliveryPlant) ;

        }

        map.put("detail", json) ;
        map.put("goodsList", goodsList) ;

        return map;
    }

    /**
     * 获取美赞合同
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getMeadContractByPurchaseId(Long id) throws Exception {
        Map<String, Object> map = new HashMap<>() ;

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject() ;
        JSONArray goodsList = new JSONArray() ;

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setId(id);

        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder) ;

        List<PurchaseOrderItemModel> itemList = purchaseOrder.getItemList();

        BigDecimal totalNum = new BigDecimal(0) ;
        BigDecimal totalAccount = new BigDecimal(0) ;

        for (int i = 0 ; i < itemList.size(); i ++) {

            com.alibaba.fastjson.JSONObject itemJson = new com.alibaba.fastjson.JSONObject() ;

            PurchaseOrderItemModel item = itemList.get(i);

            totalNum = totalNum.add(new BigDecimal(item.getNum())) ;
            totalAccount = totalAccount.add(item.getAmount()) ;

            itemJson.put("goodsNo",item.getGoodsNo());
            itemJson.put("goodsName", item.getGoodsName()) ;
            itemJson.put("barcode", item.getBarcode()) ;
            itemJson.put("num", StrUtils.intFormatString(item.getNum())) ;
            itemJson.put("price", StrUtils.doubleFormatString(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())) ;
            itemJson.put("amount", StrUtils.doubleFormatString(item.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())) ;

            Map<String, Object> queryMerchantMap = new HashMap<>() ;
            queryMerchantMap.put("merchandiseId", item.getGoodsId()) ;

            MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(queryMerchantMap);

            if(merchandiseInfo != null) {
                itemJson.put("sapCode", merchandiseInfo.getFactoryNo()) ;
            }
            itemJson.put("purchaseItemId",item.getId());
            goodsList.add(itemJson) ;
        }

        json.put("totalNum", StrUtils.intFormatString(totalNum.intValue())) ;
        json.put("totalAccount", StrUtils.doubleFormatString(totalAccount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())) ;
        json.put("poNo", purchaseOrder.getPoNo()) ;
        json.put("currency", purchaseOrder.getCurrency()) ;

        /******查询是否存在文件模版记录********/
        Map<String, Object> fileTempDataQueryMap = new HashMap<String, Object>() ;
        fileTempDataQueryMap.put("code", purchaseOrder.getCode()) ;

        FileTempDataMongo fileTempData = fileTempDataMongoDao.findOne(fileTempDataQueryMap);

        if(fileTempData != null) {
            String dataJson = fileTempData.getDataJson();

            json = com.alibaba.fastjson.JSONObject.parseObject(dataJson) ;
            json.put("poNo", purchaseOrder.getPoNo()) ;
            json.put("currency", purchaseOrder.getCurrency()) ;
            json.put("totalNum", StrUtils.intFormatString(totalNum.intValue())) ;
            json.put("totalAccount", StrUtils.doubleFormatString(totalAccount.doubleValue())) ;

            goodsList = (JSONArray)json.remove("goodsList") ;
            Map<String,List<PurchaseOrderItemModel>> itemMap = itemList.stream().collect(Collectors.groupingBy(i-> i.getGoodsNo()+"_"+StrUtils.doubleFormatString(i.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
            goodsList = comparePDFItem(itemMap, goodsList, "mead") ;

            json.put("code", purchaseOrder.getCode()) ;
            map.put("detail", json) ;
            map.put("goodsList", goodsList) ;

            return map ;
        }
        /******查询是否存在文件模版记录********/

        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", purchaseOrder.getMerchantId()) ;
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        json.put("code", purchaseOrder.getCode()) ;
        json.put("buyerEnName", merchant.getEnglishName()) ;
        json.put("buyerAddress", merchant.getEnglishRegisteredAddress()) ;
        json.put("deliveryDate", TimeUtils.format(new Date(), "yyyyMMdd")) ;
        json.put("linkman", "李守信") ;
        json.put("tel", "020-62262843") ;
        json.put("moblie", "13802726587") ;
        json.put("title", "Social") ;

        Map<String, Object> depot_params = new HashMap<String, Object>();
        depot_params.put("depotId", purchaseOrder.getDepotId()) ;

        DepotInfoMongo depot = depotInfoMongoDao.findOne(depot_params);
        if(depot != null) {
            json.put("deliveryAddress", depot.getAddress()) ;
        }

        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", purchaseOrder.getSupplierId()) ;
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params); ;

        if (customer != null) {
            json.put("sellerEnName", customer.getEnName()) ;
        }

        map.put("detail", json) ;
        map.put("goodsList", goodsList) ;

        return map;
    }

    /**
     * 根据模版导出pdf
     */
    @SuppressWarnings("unchecked")
    @Override
    public ByteArrayOutputStream getTempDataFileStream(String orderCode, String fileTempCode, String direction) throws Exception {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("code", orderCode) ;
        queryMap.put("fileTempCode", fileTempCode) ;
        FileTempDataMongo fileMongo = fileTempDataMongoDao.findOne(queryMap) ;

        String dataJson = fileMongo.getDataJson();
        Map<String, Object> dataMap = com.alibaba.fastjson.JSONObject.parseObject(dataJson, Map.class);

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setCode(orderCode);

        purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder) ;

        /*
         * 获取文件模版
         */
        FileTempModel tempModel = new FileTempModel() ;
        tempModel.setCode(fileTempCode);
        tempModel = fileTempDao.searchByModel(tempModel) ;

        String contentBody = tempModel.getContentBody();

        contentBody = FreemakerUtils.formatReplacementHorizontalHtml(contentBody) ;
        contentBody = FreemakerUtils.genHtmlStrWithTemplate(dataMap, contentBody) ;
        contentBody = FreemakerUtils.ConvertLineBreaks(contentBody) ;

        Map<String, Object> merchant_params = new HashMap<String, Object>();
        merchant_params.put("merchantId", purchaseOrder.getMerchantId()) ;
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos = PdfUtils.html2PdfWithWaterPrint(contentBody, merchant.getEnglishName(), direction);

        return bos;
    }

    @Override
    public String getPDFFileName(String orderCode) throws SQLException {

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setCode(orderCode);

        purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder) ;

        String fileName = purchaseOrder.getMerchantName() ;
        fileName += "采购合同-" + purchaseOrder.getCode() + ".pdf" ;

        return fileName;
    }

    private JSONArray comparePDFItem(Map<String,List<PurchaseOrderItemModel>> itemMap, JSONArray itemArray, String tempCode) throws Exception {
        JSONArray itemTempArray = new JSONArray() ;
        int index = 0;
        for(int j = 0; j < itemArray.size(); j++) {
            boolean isNewItem = true;
            com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) itemArray.get(j);
            String goodsNo = (String) json.get("goodsNo");
            String price = (String) json.get("price");
            String goodsNoPrice = goodsNo + "_" + price;
            if (itemMap.get(goodsNoPrice) == null) {
                continue;
            }
           PurchaseOrderItemModel item = itemMap.get(goodsNoPrice).get(0);
            if (json.get("barcode") != null ||  json.get("pgCode") != null){
                isNewItem = false;
                if (json.get("pgCode") != null) {
                    json.put("orderQty", item.getNum());
                }
            }
            if(isNewItem) {
                json = new com.alibaba.fastjson.JSONObject() ;
                if("Bayer".equals(tempCode)) {
                    Map<String, Object> queryMerchantMap = new HashMap<>() ;
                    queryMerchantMap.put("merchandiseId", item.getGoodsId()) ;
                    MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(queryMerchantMap);

                    String brand = "" ;
                    String skus = "" ;
                    String goodsEngLishName = "" ;
                    if(merchandiseInfo != null) {
                        goodsEngLishName = merchandiseInfo.getEnglishGoodsName() ;

                        if(StringUtils.isBlank(goodsEngLishName)) {
                            goodsEngLishName = "" ;
                        }
                        skus = merchandiseInfo.getSpec() ;

                        Map<String, Object> queryBrandMap = new HashMap<>() ;
                        queryBrandMap.put("brandId", merchandiseInfo.getBrandId()) ;
                        BrandMongo brandMongo = brandMongoDao.findOne(queryBrandMap);

                        if(brandMongo != null) {
                            brand = brandMongo.getEnglishBrandName() ;
                        }

                    }
                    json.put("index", index++) ;
                    json.put("brand", brand) ;
                    json.put("productName", goodsEngLishName) ;
                    json.put("skus", skus) ;

                }else if("pg".equals(tempCode)) {
                    json.put("description", item.getGoodsName()) ;
                    json.put("pgCode", item.getGoodsNo()) ;
                    json.put("orderQty", item.getNum()) ;
                }else if("mead".equals(tempCode)) {
                    Map<String, Object> queryMerchantMap = new HashMap<>() ;
                    queryMerchantMap.put("merchandiseId", item.getGoodsId()) ;
                    MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(queryMerchantMap);
                    if(merchandiseInfo != null) {
                        json.put("sapCode", merchandiseInfo.getFactoryNo()) ;
                    }
                }
            }

            json.put("goodsName", item.getGoodsName()) ;
            json.put("barcode", item.getBarcode()) ;
            json.put("num", item.getNum()) ;
            json.put("price", item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString()) ;
            json.put("amount", item.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)) ;
            json.put("purchaseItemId",item.getId());
            itemTempArray.add(json) ;
        }

        return itemTempArray ;
    }

    @Override
    public String exportTempDataFile(String orderCode, String fileTempCode, User user) throws Exception {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("code", orderCode) ;
        queryMap.put("fileTempCode", fileTempCode) ;
        FileTempDataMongo fileMongo = fileTempDataMongoDao.findOne(queryMap) ;

        String path = fileMongo.getPath();

        if(StringUtils.isBlank(path)) {

            String pdfFileName = getPDFFileName(String.valueOf(orderCode));

            String basePath = ApolloUtilDB.orderBasepath+"/puchaseContract/"+user.getMerchantId();

            File file = new File(basePath) ;

            if(!file.exists()) {
                file.mkdirs() ;
            }

            path = basePath+"/"+ pdfFileName ;
            ByteArrayOutputStream bos = getTempDataFileStream(orderCode, fileTempCode, PdfUtils.HORIZONTAL);
            FileOutputStream fileOut = new FileOutputStream(path);
            bos.writeTo(fileOut);
            fileOut.close();

            Map<String, Object> updateMap = new HashMap<String, Object>() ;
            updateMap.put("path", path) ;
            fileTempDataMongoDao.update(queryMap, updateMap);
        }

        return path;
    }

    @Override
    public List<TradeLinkConfigModel> getTradeLinkList(Long purchaseOrderId) throws SQLException {

        PurchaseOrderModel purchase = purchaseOrderDao.searchById(purchaseOrderId);

        TradeLinkConfigModel tradeLinkConfigModel = new TradeLinkConfigModel();
        tradeLinkConfigModel.setStartPointMerchantId(purchase.getMerchantId());
        tradeLinkConfigModel.setStartPointBuId(purchase.getBuId());
        tradeLinkConfigModel.setStartSupplierId(purchase.getSupplierId());

        List<TradeLinkConfigModel> tradeList = tradeLinkConfigDao.list(tradeLinkConfigModel);

        return tradeList;
    }

    @Override
    public PurchaseLinkInfoDTO getPurchaseLinkDtoByPurchaseId(Long id, Long purchaseTradeLinkId) throws SQLException {


        PurchaseLinkInfoDTO purchaseLinkInfoDTO = null ;

        if(id != null) {
            purchaseLinkInfoDTO = purchaseLinkInfoDao.getPurchaseLinkDtoByPurchaseId(id) ;
        }else if(purchaseTradeLinkId != null) {
            purchaseLinkInfoDTO = purchaseLinkInfoDao.getPurchaseLinkDtoById(purchaseTradeLinkId) ;
        }

        if(purchaseLinkInfoDTO == null) {

            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

            PurchaseContractModel purchaseContractModel = new PurchaseContractModel() ;
            purchaseContractModel.setOrderId(id);
            purchaseContractModel = purchaseContractDao.searchByModel(purchaseContractModel) ;

            purchaseLinkInfoDTO = new PurchaseLinkInfoDTO() ;
            purchaseLinkInfoDTO.setInfoAuditDate(purchaseOrderModel.getAttributionDate());
            purchaseLinkInfoDTO.setInfoCurrency(purchaseOrderModel.getCurrency());
            //purchaseLinkInfoDTO.setInfoLbxNo(purchaseOrderModel.getLbxNo());
            purchaseLinkInfoDTO.setPurchaseOrderCode(purchaseOrderModel.getCode());
            purchaseLinkInfoDTO.setPurchaseOrderId(purchaseOrderModel.getId());
            purchaseLinkInfoDTO.setStartPointPoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoDTO.setOnePoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoDTO.setTwoPoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoDTO.setThreePoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoDTO.setFourPoNo(purchaseOrderModel.getPoNo());

            if(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3.equals(purchaseOrderModel.getBusinessModel())) {
                purchaseLinkInfoDTO.setInfoBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_3);
            }else {
                purchaseLinkInfoDTO.setInfoBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_1);
            }


            if(purchaseContractModel != null) {
                purchaseLinkInfoDTO.setConDeliveryDate(purchaseContractModel.getDeliveryDate());
                purchaseLinkInfoDTO.setConDestinationdCn(purchaseContractModel.getDestinationdCn());
                purchaseLinkInfoDTO.setConDestinationdEn(purchaseContractModel.getDestinationdEn());
                purchaseLinkInfoDTO.setConLoadingCnPort(purchaseContractModel.getLoadingCnPort());
                purchaseLinkInfoDTO.setConLoadingEnPort(purchaseContractModel.getLoadingEnPort());
                purchaseLinkInfoDTO.setConMeansOfTransportation(purchaseContractModel.getMeansOfTransportation());
                purchaseLinkInfoDTO.setConPaymentMethod(purchaseContractModel.getPaymentMethod());
                purchaseLinkInfoDTO.setConPaymentMethodText(purchaseContractModel.getPaymentMethodText());
                purchaseLinkInfoDTO.setConPaymentMethodTextEn(purchaseContractModel.getPaymentMethodTextEn());
                purchaseLinkInfoDTO.setConSpecialAgreement(purchaseContractModel.getSpecialAgreement());
                purchaseLinkInfoDTO.setConSpecialAgreementEn(purchaseContractModel.getSpecialAgreementEn());
            }
        }

        return purchaseLinkInfoDTO;
    }

    @Override
    public Long saveOrUpdateLinkInfo(PurchaseLinkInfoModel model, User user) throws SQLException {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;

        if(model.getStartPointDepotId() != null) {
            queryMap.put("depotId", model.getStartPointDepotId()) ;

            DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

            model.setStartPointDepotName(depot.getName());
        }

        if(model.getOneDepotId() != null) {
            queryMap.put("depotId", model.getOneDepotId()) ;

            DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

            model.setOneDepotName(depot.getName());
        }

        if(model.getTwoDepotId() != null) {
            queryMap.put("depotId", model.getTwoDepotId()) ;

            DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

            model.setTwoDepotName(depot.getName());
        }

        if(model.getThreeDepotId() != null) {
            queryMap.put("depotId", model.getThreeDepotId()) ;

            DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

            model.setThreeDepotName(depot.getName());
        }

        if(model.getId() == null) {

            model.setCreateDate(TimeUtils.getNow());
            model.setCreateName(user.getName());
            model.setCreater(user.getId());

            purchaseLinkInfoDao.save(model) ;

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, model.getPurchaseOrderCode(), "新增交易链路信息", null, null);

        }else {
            model.setModifyDate(TimeUtils.getNow());
            model.setModifierUsername(user.getName());
            model.setModifier(user.getId());

            purchaseLinkInfoDao.modify(model) ;

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, model.getPurchaseOrderCode(), "编辑交易链路信息", null, null);
        }

        return model.getId() ;
    }

    /**
     * 构造交易链路所有单据
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<String, Object> generatePreOrder(Long id, User user) throws SQLException {

        String unineCode = CodeGeneratorUtil.getCode(8);

        Map<String, Object> map = new LinkedHashMap<String, Object>() ;

        List<String> errorStrList = new ArrayList<String>() ;

        PurchaseLinkInfoDTO purchaseLinkInfoDTO = purchaseLinkInfoDao.getPurchaseLinkDtoById(id) ;

        PurchaseOrderModel queryPurchaseModel = new PurchaseOrderModel() ;
        queryPurchaseModel.setId(purchaseLinkInfoDTO.getPurchaseOrderId());
        queryPurchaseModel = purchaseOrderDao.getDetails(queryPurchaseModel) ;


        Map<String, List> tempMap = null ;

        /**
         * 创建起点公司--》客户1销售订单 开始
         */
        SaleOrderModel startSaleOrder = linkCreateSaleOrder(queryPurchaseModel, purchaseLinkInfoDTO.getOneCustomerId(),
                purchaseLinkInfoDTO.getTwoCustomerId(), purchaseLinkInfoDTO.getOneIdValueType(), purchaseLinkInfoDTO.getOneType(),
                purchaseLinkInfoDTO, purchaseLinkInfoDTO.getStartPointPoNo(), purchaseLinkInfoDTO.getStartPointPremiumRate(),
                user, errorStrList, unineCode);

        if(startSaleOrder == null) {
            map.put("errorList", errorStrList) ;

            return map ;
        }

        String startMerchantName = startSaleOrder.getMerchantName();

        tempMap = (Map<String, List>)map.get(startMerchantName);
        if(tempMap == null) {
            tempMap = new HashMap<String, List>() ;
        }

        List saleOrderList = tempMap.get("saleOrderList");
        if(saleOrderList == null) {
            saleOrderList = new ArrayList<SaleOrderModel>() ;
        }

        if(startSaleOrder != null) {
            saleOrderList.add(startSaleOrder) ;
        }

        tempMap.put("saleOrderList", saleOrderList) ;
        map.put(startMerchantName, tempMap) ;

        /**
         * 创建起点公司--》客户1销售订单 结束
         */

        /**
         * 判断是否客户1是否内部公司，若是生成向起点公司采购商品的 采购订单
         */
        PurchaseOrderModel oneCustomerPurchase = null ;
        SaleOrderModel oneCustomerSale = null ;
        if("1".equals(purchaseLinkInfoDTO.getOneType())) {
            oneCustomerPurchase = linkCreatePurchaseOrder(purchaseLinkInfoDTO.getOneCustomerId(),
                    purchaseLinkInfoDTO.getTwoCustomerId(), purchaseLinkInfoDTO.getStartPointMerchantId(),
                    purchaseLinkInfoDTO.getOneBuId(), purchaseLinkInfoDTO.getOneBuName(),
                    purchaseLinkInfoDTO.getOnePoNo() ,
                    purchaseLinkInfoDTO.getOneDepotId(), purchaseLinkInfoDTO.getOneDepotName(),
                    purchaseLinkInfoDTO,
                    startSaleOrder, user, errorStrList, unineCode, queryPurchaseModel);

            if(oneCustomerPurchase != null) {
                tempMap = (Map<String, List>)map.get(oneCustomerPurchase.getMerchantName());
                if(tempMap == null) {
                    tempMap = new HashMap<String, List>() ;
                }

                List purchaseList = tempMap.get("purchaseList");
                if(purchaseList == null) {
                    purchaseList = new ArrayList<PurchaseOrderModel>() ;
                }

                purchaseList.add(oneCustomerPurchase) ;
                tempMap.put("purchaseList", purchaseList) ;

                //生成文件
                PurchaseContractModel oneContract = linkCreateContract(oneCustomerPurchase, purchaseLinkInfoDTO);

                List purchaseContractList = tempMap.get("purchaseContractList");
                if(purchaseContractList == null) {
                    purchaseContractList = new ArrayList<PurchaseContractModel>() ;
                }

                purchaseContractList.add(oneContract) ;
                tempMap.put("purchaseContractList", purchaseContractList) ;
                map.put(oneCustomerPurchase.getMerchantName(), tempMap) ;

                if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(oneCustomerPurchase.getStatus())) {
                    oneCustomerSale = linkCreateSaleOrder(oneCustomerPurchase, purchaseLinkInfoDTO.getTwoCustomerId(),
                            purchaseLinkInfoDTO.getThreeCustomerId(), purchaseLinkInfoDTO.getTwoIdValueType(), purchaseLinkInfoDTO.getTwoType(),
                            purchaseLinkInfoDTO,
                            purchaseLinkInfoDTO.getOnePoNo(), purchaseLinkInfoDTO.getOnePremiumRate(), user, errorStrList, unineCode);

                    if(oneCustomerSale != null) {
                        tempMap = (Map<String, List>)map.get(oneCustomerSale.getMerchantName());
                        if(tempMap == null) {
                            tempMap = new HashMap<String, List>() ;
                        }

                        saleOrderList = tempMap.get("saleOrderList");
                        if(saleOrderList == null) {
                            saleOrderList = new ArrayList<SaleOrderModel>() ;
                        }

                        saleOrderList.add(oneCustomerSale) ;

                        tempMap.put("saleOrderList", saleOrderList) ;
                        map.put(oneCustomerPurchase.getMerchantName(), tempMap) ;
                    }
                }
            }
        }

        PurchaseOrderModel twoCustomerPurchase = null ;
        SaleOrderModel twoCustomerSale = null ;
        if("1".equals(purchaseLinkInfoDTO.getTwoType())) {

            SaleOrderModel tempSale = oneCustomerSale != null ? oneCustomerSale : startSaleOrder ;

            twoCustomerPurchase = linkCreatePurchaseOrder(purchaseLinkInfoDTO.getTwoCustomerId(),
                    purchaseLinkInfoDTO.getThreeCustomerId(), purchaseLinkInfoDTO.getOneCustomerId(),
                    purchaseLinkInfoDTO.getTwoBuId(), purchaseLinkInfoDTO.getTwoBuName(),
                    purchaseLinkInfoDTO.getTwoPoNo() ,
                    purchaseLinkInfoDTO.getTwoDepotId(), purchaseLinkInfoDTO.getTwoDepotName(),
                    purchaseLinkInfoDTO,
                    tempSale, user, errorStrList, unineCode, queryPurchaseModel);

            if(twoCustomerPurchase != null) {
                tempMap = (Map<String, List>)map.get(twoCustomerPurchase.getMerchantName());
                if(tempMap == null) {
                    tempMap = new HashMap<String, List>() ;
                }

                List purchaseList = tempMap.get("purchaseList");
                if(purchaseList == null) {
                    purchaseList = new ArrayList<PurchaseOrderModel>() ;
                }

                purchaseList.add(twoCustomerPurchase) ;
                tempMap.put("purchaseList", purchaseList) ;

                //生成文件
                PurchaseContractModel twoContract = linkCreateContract(twoCustomerPurchase, purchaseLinkInfoDTO);

                List purchaseContractList = tempMap.get("purchaseContractList");
                if(purchaseContractList == null) {
                    purchaseContractList = new ArrayList<PurchaseContractModel>() ;
                }

                purchaseContractList.add(twoContract) ;
                tempMap.put("purchaseContractList", purchaseContractList) ;
                map.put(twoCustomerPurchase.getMerchantName(), tempMap) ;

                if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(twoCustomerPurchase.getStatus())) {
                    twoCustomerSale = linkCreateSaleOrder(twoCustomerPurchase, purchaseLinkInfoDTO.getThreeCustomerId(),
                            purchaseLinkInfoDTO.getFourCustomerId(), purchaseLinkInfoDTO.getThreeIdValueType(), purchaseLinkInfoDTO.getThreeType(),
                            purchaseLinkInfoDTO,
                            purchaseLinkInfoDTO.getTwoPoNo(), purchaseLinkInfoDTO.getTwoPremiumRate(), user, errorStrList, unineCode);

                    if(twoCustomerSale != null) {
                        tempMap = (Map<String, List>)map.get(twoCustomerSale.getMerchantName());
                        if(tempMap == null) {
                            tempMap = new HashMap<String, List>() ;
                        }

                        saleOrderList = tempMap.get("saleOrderList");
                        if(saleOrderList == null) {
                            saleOrderList = new ArrayList<SaleOrderModel>() ;
                        }

                        saleOrderList.add(twoCustomerSale) ;

                        tempMap.put("saleOrderList", saleOrderList) ;
                        map.put(twoCustomerPurchase.getMerchantName(), tempMap) ;
                    }
                }
            }

        }

        PurchaseOrderModel threeCustomerPurchase = null ;
        SaleOrderModel threeCustomerSale = null ;
        if("1".equals(purchaseLinkInfoDTO.getThreeType())) {

            SaleOrderModel tempSale = twoCustomerSale != null ? twoCustomerSale : oneCustomerSale != null ? oneCustomerSale : startSaleOrder ;

            threeCustomerPurchase = linkCreatePurchaseOrder(purchaseLinkInfoDTO.getThreeCustomerId(),
                    purchaseLinkInfoDTO.getFourCustomerId(), purchaseLinkInfoDTO.getTwoCustomerId(),
                    purchaseLinkInfoDTO.getThreeBuId(), purchaseLinkInfoDTO.getThreeBuName(),
                    purchaseLinkInfoDTO.getThreePoNo(),
                    purchaseLinkInfoDTO.getThreeDepotId(), purchaseLinkInfoDTO.getThreeDepotName(),
                    purchaseLinkInfoDTO,
                    tempSale, user, errorStrList, unineCode, queryPurchaseModel);

            if(threeCustomerPurchase != null) {
                tempMap = (Map<String, List>)map.get(threeCustomerPurchase.getMerchantName());
                if(tempMap == null) {
                    tempMap = new HashMap<String, List>() ;
                }

                List purchaseList = tempMap.get("purchaseList");
                if(purchaseList == null) {
                    purchaseList = new ArrayList<PurchaseOrderModel>() ;
                }

                purchaseList.add(threeCustomerPurchase) ;
                tempMap.put("purchaseList", purchaseList) ;
                map.put(threeCustomerPurchase.getMerchantName(), tempMap) ;

                //生成文件
                PurchaseContractModel threeContract = linkCreateContract(threeCustomerPurchase, purchaseLinkInfoDTO);

                List purchaseContractList = tempMap.get("purchaseContractList");
                if(purchaseContractList == null) {
                    purchaseContractList = new ArrayList<PurchaseContractModel>() ;
                }

                purchaseContractList.add(threeContract) ;
                tempMap.put("purchaseContractList", purchaseContractList) ;
                map.put(threeCustomerPurchase.getMerchantName(), tempMap) ;

                if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(threeCustomerPurchase.getStatus())) {
                    threeCustomerSale = linkCreateSaleOrder(threeCustomerPurchase, purchaseLinkInfoDTO.getFourCustomerId(),
                            null , purchaseLinkInfoDTO.getFourIdValueType(), purchaseLinkInfoDTO.getFourType(),
                            purchaseLinkInfoDTO,
                            purchaseLinkInfoDTO.getThreePoNo(), purchaseLinkInfoDTO.getThreePremiumRate(), user, errorStrList, unineCode);

                    if(threeCustomerSale != null) {
                        tempMap = (Map<String, List>)map.get(threeCustomerSale.getMerchantName());
                        if(tempMap == null) {
                            tempMap = new HashMap<String, List>() ;
                        }

                        saleOrderList = tempMap.get("saleOrderList");
                        if(saleOrderList == null) {
                            saleOrderList = new ArrayList<SaleOrderModel>() ;
                        }

                        saleOrderList.add(threeCustomerSale) ;

                        tempMap.put("saleOrderList", saleOrderList) ;
                        map.put(threeCustomerPurchase.getMerchantName(), tempMap) ;
                    }
                }
            }

        }

        PurchaseOrderModel fourCustomerPurchase = null ;
        if("1".equals(purchaseLinkInfoDTO.getFourType())) {

            SaleOrderModel tempSale = threeCustomerSale != null ? threeCustomerSale :
                    twoCustomerSale != null ? twoCustomerSale :
                            oneCustomerSale != null ? oneCustomerSale : startSaleOrder ;

            fourCustomerPurchase = linkCreatePurchaseOrder(purchaseLinkInfoDTO.getFourCustomerId(),
                    null, purchaseLinkInfoDTO.getThreeCustomerId(),
                    purchaseLinkInfoDTO.getFourBuId(), purchaseLinkInfoDTO.getFourBuName(),
                    purchaseLinkInfoDTO.getFourPoNo(),
                    purchaseLinkInfoDTO.getFourDepotId(), purchaseLinkInfoDTO.getFourDepotName(),
                    purchaseLinkInfoDTO,
                    tempSale, user, errorStrList, unineCode, queryPurchaseModel);


            if(fourCustomerPurchase != null) {
                tempMap = (Map<String, List>)map.get(fourCustomerPurchase.getMerchantName());
                if(tempMap == null) {
                    tempMap = new HashMap<String, List>() ;
                }

                List purchaseList = tempMap.get("purchaseList");
                if(purchaseList == null) {
                    purchaseList = new ArrayList<PurchaseOrderModel>() ;
                }

                purchaseList.add(fourCustomerPurchase) ;
                tempMap.put("purchaseList", purchaseList) ;
                map.put(fourCustomerPurchase.getMerchantName(), tempMap) ;

                //生成文件
                PurchaseContractModel fourContract = linkCreateContract(fourCustomerPurchase, purchaseLinkInfoDTO);

                List purchaseContractList = tempMap.get("purchaseContractList");
                if(purchaseContractList == null) {
                    purchaseContractList = new ArrayList<PurchaseContractModel>() ;
                }

                purchaseContractList.add(fourContract) ;
                tempMap.put("purchaseContractList", purchaseContractList) ;
                map.put(fourCustomerPurchase.getMerchantName(), tempMap) ;
            }

        }

        if(!errorStrList.isEmpty()) {
            map.put("errorList", errorStrList) ;
        }else {

            JSONObject goodsInfoJson = null ;
            PurchaseLinkInfoModel purchaseLinkInfo = purchaseLinkInfoDao.searchById(id);
            if(StringUtils.isNotBlank(purchaseLinkInfo.getGoodsInfo())) {

                goodsInfoJson = (JSONObject)JSONObject.fromObject(purchaseLinkInfo.getGoodsInfo()) ;

            }

            //存在金额调整，修改金额
            for (String merchantName : map.keySet()) {

                Map<String, Object> merchantOrderMap = (Map<String, Object>)map.get(merchantName);

                List<SaleOrderModel> saleList = (List<SaleOrderModel>)merchantOrderMap.get("saleOrderList");

                if(saleList != null && goodsInfoJson != null) {
                    // 修改金额

                    for(int i = 0 ; i < saleList.size(); i++) {

                        SaleOrderModel saleOrderModel = saleList.get(i) ;

                        String key = saleOrderModel.getMerchantName() + "_saleOrder_"  + i ;

                        Object arrObj = goodsInfoJson.get(key);

                        if(arrObj != null) {

                            List<SaleOrderItemModel> tempItemList = saleOrderModel.getItemList() ;

                            net.sf.json.JSONArray itemArr = net.sf.json.JSONArray.fromObject(arrObj) ;

                            BigDecimal totalAmount = new BigDecimal(0) ;

                            for (SaleOrderItemModel itemModel : tempItemList) {
                                for (Object itemObj : itemArr) {
                                    JSONObject itemJSON = JSONObject.fromObject(itemObj);

                                    if(itemModel.getGoodsId().longValue()
                                            == itemJSON.getLong("goodsId")) {

                                        String amountStr = itemJSON.getString("amount");
                                        BigDecimal amount = new BigDecimal(amountStr);
                                        BigDecimal num = new BigDecimal(itemModel.getNum());
                                        BigDecimal price = amount.divide(num, 8, BigDecimal.ROUND_HALF_UP);

                                        itemModel.setAmount(amount);
                                        itemModel.setPrice(price);

                                        totalAmount = totalAmount.add(amount) ;

                                        break ;
                                    }

                                }
                            }

                            saleOrderModel.setTotalAmount(totalAmount);
                            saleOrderModel.setItemList(tempItemList);
                        }
                    }

                }

                List<PurchaseOrderModel> purchaseList = (List<PurchaseOrderModel>)merchantOrderMap.get("purchaseList");

                // 修改金额
                if(purchaseList!=null && goodsInfoJson != null) {

                    for(int i = 0 ; i < purchaseList.size(); i++) {
                        PurchaseOrderModel purchaseOrderModel = purchaseList.get(i) ;

                        String key = purchaseOrderModel.getMerchantName() + "_purchaseOrder_"  + i ;

                        Object arrObj = goodsInfoJson.get(key);

                        if(arrObj != null) {

                            List<PurchaseOrderItemModel> tempItemList = purchaseOrderModel.getItemList() ;

                            net.sf.json.JSONArray itemArr = net.sf.json.JSONArray.fromObject(arrObj) ;

                            BigDecimal totalAmount = new BigDecimal(0) ;

                            for (PurchaseOrderItemModel itemModel : tempItemList) {
                                for (Object itemObj : itemArr) {
                                    JSONObject itemJSON = JSONObject.fromObject(itemObj);

                                    if(itemModel.getGoodsId().longValue()
                                            == itemJSON.getLong("goodsId")) {

                                        String amountStr = itemJSON.getString("amount");
                                        BigDecimal amount = new BigDecimal(amountStr);
                                        BigDecimal num = new BigDecimal(itemModel.getNum());
                                        BigDecimal price = amount.divide(num, 8, BigDecimal.ROUND_HALF_UP);

                                        itemModel.setAmount(amount);
                                        itemModel.setPrice(price);

                                        totalAmount.add(amount) ;

                                        break ;
                                    }

                                }
                            }

                            purchaseOrderModel.setItemList(tempItemList);
                        }
                    }

                }
            }
        }

        return map;
    }

    /**
     * 链路生成采购订单
     * @param customerId
     * @param nextCustomer
     * @param lastcustomertId
     * @param curentBuId
     * @param currentBuName
     * @param poNo
     * @param depotId
     * @param depotName
     * @param purchaseLinkInfoDTO
     * @param saleModel
     * @param user
     * @param errorStrList
     * @param unineCode
     * @param sourceOrder
     * @return
     */
    private PurchaseOrderModel linkCreatePurchaseOrder(Long customerId, Long nextCustomer, Long lastcustomertId,
                                                       Long curentBuId, String currentBuName , String poNo,
                                                       Long depotId, String depotName,
                                                       PurchaseLinkInfoDTO purchaseLinkInfoDTO, SaleOrderModel saleModel,
                                                       User user, List<String> errorStrList, String unineCode, PurchaseOrderModel sourceOrder) {
        Map<String, Object> queryCustomerMap = new HashMap<String, Object>() ;
        Map<String, Object> queryMerchantMap = new HashMap<String, Object> () ;

        queryCustomerMap.put("customerid", customerId) ;
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "1") ;

        CustomerInfoMogo customer = customerInfoMongoDao.findOne(queryCustomerMap);

        if(customer != null) {
            Long innerMerchantId = customer.getInnerMerchantId();
            queryMerchantMap.put("merchantId", innerMerchantId);
        }else {
            queryMerchantMap.put("merchantId", customerId);
        }

        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMerchantMap);

        /**
         * 查询上一步内部公司对应的供应商ID
         */
        queryCustomerMap.clear();
        queryCustomerMap.put("innerMerchantId", lastcustomertId) ;
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(queryCustomerMap) ;

        if(supplier == null) {
            queryCustomerMap.clear();
            queryCustomerMap.put("customerid", lastcustomertId) ;
            queryCustomerMap.put("status", "1") ;
            queryCustomerMap.put("cusType", "1") ;

            CustomerInfoMogo lastCustomer = customerInfoMongoDao.findOne(queryCustomerMap) ;
            /**上游销售单客户未找到，错误提示**/
            if(lastCustomer == null) {
                errorStrList.add("新增采购订单失败，公司：" + merchant.getName() + "上游销售单客户未找到或未启用" ) ;

                return null ;
            }

            String mainId = lastCustomer.getMainId();

            queryCustomerMap.clear();
            queryCustomerMap.put("mainId", mainId) ;
            queryCustomerMap.put("status", "1") ;
            queryCustomerMap.put("cusType", "2") ;

            supplier = customerInfoMongoDao.findOne(queryCustomerMap) ;

            /**供应商依然未找到，错误提示**/
            if(supplier == null) {
                errorStrList.add("新增采购订单失败，公司：" + merchant.getName() + "未找到上级供应商信息或未启用" ) ;

                return null ;
            }
        }

        queryCustomerMap.clear();
        queryCustomerMap.put("merchantId", merchant.getMerchantId());
        queryCustomerMap.put("customerId", supplier.getCustomerid());
        CustomerMerchantRelMongo purCustomerRel = customerMerchantRelMongoDao.findOne(queryCustomerMap) ;

        if(purCustomerRel == null) {
            errorStrList.add("新增采购订单失败，公司：" + merchant.getName() + "未找到公司供应商关联" ) ;

            return null ;
        }
        Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>() ;
        depotMerchantRelMap.put("merchantId", merchant.getMerchantId()) ;
        depotMerchantRelMap.put("depotId", purchaseLinkInfoDTO.getOneDepotId()) ;
        DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

        if(depotMerchantRel == null) {
            errorStrList.add(merchant.getName() + "与" + purchaseLinkInfoDTO.getOneDepotName() + "未建立公司仓库关联") ;
            return null ;
        }

        PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
        /**源订单信息*/
        purchaseModel.setContractNo(sourceOrder.getContractNo());
        purchaseModel.setDeliveryAddr(sourceOrder.getDeliveryAddr());
        purchaseModel.setArriveDepotDate(sourceOrder.getArriveDepotDate());
        purchaseModel.setRemark(sourceOrder.getRemark());
        purchaseModel.setChannel(sourceOrder.getChannel());
        purchaseModel.setBoxNum(sourceOrder.getBoxNum());
        purchaseModel.setDestinationPortName(sourceOrder.getDestinationPortName());
        purchaseModel.setTallyingUnit(sourceOrder.getTallyingUnit());
        purchaseModel.setPayRules(sourceOrder.getPayRules());
        purchaseModel.setBlNo(sourceOrder.getBlNo());
        purchaseModel.setImExpPort(sourceOrder.getImExpPort());
        purchaseModel.setShipper(sourceOrder.getShipper());
        purchaseModel.setMark(sourceOrder.getMark());
        purchaseModel.setArriveDate(sourceOrder.getArriveDate());
        purchaseModel.setDestinationName(sourceOrder.getDestinationName());
        //purchaseModel.setPaymentDate(sourceOrder.getPaymentDate());
        purchaseModel.setPackType(sourceOrder.getPackType());
        purchaseModel.setInvoiceNo(sourceOrder.getInvoiceNo());
        purchaseModel.setTradeTerms(sourceOrder.getTradeTerms());
        purchaseModel.setPaymentProvision(sourceOrder.getPaymentProvision());
        /**源订单信息*/
        purchaseModel.setMerchantId(merchant.getMerchantId());
        purchaseModel.setMerchantName(merchant.getName());
        purchaseModel.setTopidealCode(merchant.getTopidealCode());
        purchaseModel.setBuId(curentBuId);
        //purchaseModel.setPaySubject(DERP_ORDER.PURCHASEORDER_PAYSUBJECT_2) ;
        purchaseModel.setBuName(currentBuName);
        purchaseModel.setDepotId(depotId);
        purchaseModel.setDepotName(depotName);
        purchaseModel.setSupplierId(purCustomerRel.getCustomerId());
        purchaseModel.setSupplierName(purCustomerRel.getName());
        purchaseModel.setAttributionDate(purchaseLinkInfoDTO.getInfoAuditDate());
        //purchaseModel.setLbxNo(purchaseLinkInfoDTO.getInfoLbxNo());
        purchaseModel.setCurrency(purchaseLinkInfoDTO.getInfoCurrency());
        purchaseModel.setTgtCurrency(merchant.getAccountCurrency());
        purchaseModel.setCarrier(purchaseLinkInfoDTO.getInfoCarrier());
        purchaseModel.setPoNo(poNo);
        purchaseModel.setGrossWeight(purchaseLinkInfoDTO.getInfoGrossWeight());
        purchaseModel.setTrainNumber(purchaseLinkInfoDTO.getInfoTrainNumber());
        //purchaseModel.setTransport(purchaseLinkInfoDTO.getInfoTransport());
        purchaseModel.setTorrNum(purchaseLinkInfoDTO.getInfoTorrNum());
        purchaseModel.setLadingBill(purchaseLinkInfoDTO.getInfoLadingBill());
        purchaseModel.setUnloadPort(purchaseLinkInfoDTO.getInfoUnloadPort());
        purchaseModel.setLoadPort(purchaseLinkInfoDTO.getInfoLoadPort());
        purchaseModel.setShipDate(purchaseLinkInfoDTO.getInfoShipDate());
        purchaseModel.setStandardCaseTeu(purchaseLinkInfoDTO.getInfoStandardCaseTeu());
        purchaseModel.setCreateDate(TimeUtils.getNow());
        purchaseModel.setCreateName(user.getName());
        purchaseModel.setCreater(user.getId());
        purchaseModel.setLinkUniueCode(unineCode);
        purchaseModel.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
        purchaseModel.setAmountConfirmStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0);

        /**若源采购订单 为采销执行，链路所有采购订单都为采销执行*/
        if(DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(sourceOrder.getBusinessModel())) {
            purchaseModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3);
        }else{
            purchaseModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1);
        }

        if(nextCustomer != null) {
            purchaseModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_003);
        }else {
            purchaseModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
        }

        List<PurchaseOrderItemModel> purchaseItemList = new ArrayList<PurchaseOrderItemModel>() ;
        for (SaleOrderItemModel saleOrderItemModel : saleModel.getItemList()) {

            // 根据商品货号获取商品id
            Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();

            merchandiseInfo_params.put("goodsNo", saleOrderItemModel.getGoodsNo());
            merchandiseInfo_params.put("merchantId", saleModel.getMerchantId());
            merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params) ;

            String commbarcode = merchandiseInfo.getCommbarcode();

            merchandiseInfo_params.clear();
            merchandiseInfo_params.put("commbarcode", commbarcode);
            merchandiseInfo_params.put("merchantId", merchant.getMerchantId());
            merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

            List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfo_params, depotId);
            if(merchandiseList == null || merchandiseList.isEmpty()) {
                errorStrList.add("公司：" + merchant.getName()+" 出库仓："+ depotName+ "未找到标准条码为" + commbarcode + "的商品信息") ;
                continue ;
            }

            List<MerchandiseInfoMogo> filterList = merchandiseList.stream()
                    .filter((MerchandiseInfoMogo m) -> m.getGoodsNo().equals(saleOrderItemModel.getGoodsNo())).collect(Collectors.toList());

            MerchandiseInfoMogo tempMerchandise = null ;
            if(!filterList.isEmpty()) {
                tempMerchandise = filterList.get(0) ;
            }else {
                tempMerchandise = merchandiseList.get(0) ;
            }

            PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel() ;
            itemModel.setGoodsCode(tempMerchandise.getCode());
            itemModel.setGoodsId(tempMerchandise.getMerchandiseId());
            itemModel.setGoodsName(tempMerchandise.getName());
            itemModel.setGoodsNo(tempMerchandise.getGoodsNo());
            itemModel.setBarcode(tempMerchandise.getBarcode());
            itemModel.setAmount(saleOrderItemModel.getAmount());
            itemModel.setPrice(saleOrderItemModel.getPrice());
            itemModel.setNum(saleOrderItemModel.getNum());
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setTax(saleOrderItemModel.getTax());
            itemModel.setTaxAmount(saleOrderItemModel.getTaxAmount());
            itemModel.setTaxPrice(saleOrderItemModel.getTaxPrice());
            itemModel.setTaxRate(saleOrderItemModel.getTaxRate());
            itemModel.setFactoryNo(tempMerchandise.getFactoryNo());

            purchaseItemList.add(itemModel) ;
        }

        purchaseModel.setItemList(purchaseItemList);

        return purchaseModel ;
    }

    /**
     * 链路生成销售订单
     * @param purchaseModel
     * @param nextCustomerId
     * @param nextnextCustomerId
     * @param nextIdValueType
     * @param nextType
     * @param purchaseLinkInfoDTO
     * @param poNo
     * @param salesRate
     * @param user
     * @param errorStrList
     * @param unineCode
     * @return
     */
    private SaleOrderModel linkCreateSaleOrder(PurchaseOrderModel purchaseModel, Long nextCustomerId,
                                               Long nextnextCustomerId, String nextIdValueType, String nextType,
                                               PurchaseLinkInfoDTO purchaseLinkInfoDTO, String poNo,
                                               BigDecimal salesRate , User user, List<String> errorStrList, String unineCode){

        Map<String, Object> merchantQueryMap = new HashMap<String, Object>() ;
        merchantQueryMap.put("merchantId", purchaseModel.getMerchantId()) ;

        MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantQueryMap);

        Map<String, Object> queryDepotMap = new HashMap<String, Object>() ;
        Map<String, Object> queryCustomerMap = new HashMap<String, Object>() ;
        queryDepotMap.put("depotId", purchaseModel.getDepotId()) ;
        DepotInfoMongo depot = depotInfoMongoDao.findOne(queryDepotMap);

        SaleOrderModel saleOrder = new SaleOrderModel() ;
        saleOrder.setMerchantId(purchaseModel.getMerchantId());
        saleOrder.setMerchantName(purchaseModel.getMerchantName());
        saleOrder.setTopidealCode(purchaseModel.getTopidealCode());
        saleOrder.setBuId(purchaseModel.getBuId());
        saleOrder.setBuName(purchaseModel.getBuName());
        saleOrder.setOutDepotId(purchaseModel.getDepotId());
        saleOrder.setOutDepotName(purchaseModel.getDepotName());
        saleOrder.setOutDepotCode(depot.getCode());
        saleOrder.setPoNo(poNo);
        //saleOrder.setLbxNo(purchaseModel.getLbxNo());
        saleOrder.setCurrency(purchaseLinkInfoDTO.getInfoCurrency());
        saleOrder.setOrderType(DERP_ORDER.SALEORDER_ORDERTYPE_2);
        saleOrder.setCarrier(purchaseLinkInfoDTO.getInfoCarrier());
        saleOrder.setTransport(purchaseLinkInfoDTO.getInfoTransport());
        saleOrder.setTeu(purchaseLinkInfoDTO.getInfoStandardCaseTeu());
        saleOrder.setTrainno(purchaseLinkInfoDTO.getInfoTrainNumber());
        saleOrder.setTorusNumber(purchaseLinkInfoDTO.getInfoTorrNum());
        saleOrder.setCreateDate(TimeUtils.getNow());
        saleOrder.setCreateName(user.getName());
        saleOrder.setCreater(user.getId());
        saleOrder.setIsSameArea(DERP.ISSAMEAREA_0);
        saleOrder.setLinkUniueCode(unineCode);
        saleOrder.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
        saleOrder.setTradeTerms(purchaseModel.getTradeTerms());
        saleOrder.setPaymentTerms(purchaseModel.getPaymentProvision());

        queryCustomerMap.put("merchantId", purchaseModel.getMerchantId()) ;
        queryCustomerMap.put("status","1");//状态 1使用中,0已禁用

        if("1".equals(nextIdValueType)) {
            queryCustomerMap.put("customerId", nextCustomerId) ;
        }else {
            //若IdValueType 为“2” 是为公司ID
            Map<String, Object> tempCustomerMap = new HashMap<String, Object>() ;

            tempCustomerMap.put("innerMerchantId", nextCustomerId) ;
            tempCustomerMap.put("status", "1") ;
            tempCustomerMap.put("cusType", "1") ;

            CustomerInfoMogo tempCustomer = customerInfoMongoDao.findOne(tempCustomerMap);

            queryCustomerMap.put("customerId", tempCustomer.getCustomerid()) ;
        }

        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(queryCustomerMap);

        if(customerRel == null) {
            errorStrList.add("新增" + merchant.getName() + "销售订单失败，客户与该公司关联未找到。") ;

            return null ;
        }

        saleOrder.setCustomerId(customerRel.getCustomerId());
        saleOrder.setCustomerName(customerRel.getName());
        //  客户2为不为空，或者 客户2为空且客户1为公司  或者 客户2为空且客户1为内部客户，销售单类型只能为采销执行或整批结算，，销售了些为已审核
        if(nextnextCustomerId != null
                || (nextnextCustomerId == null && "2".equals(nextIdValueType))
                || (nextnextCustomerId == null && "1".equals(nextIdValueType) && "1".equals(nextType))) {

            /**若有下级客户，若链路采购订单为采销执行，销售单也为采销执行*/
            if(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3.equals(purchaseModel.getBusinessModel())) {
                saleOrder.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3);
            }else{
                saleOrder.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1);
            }

            saleOrder.setState(DERP_ORDER.SALEORDER_STATE_003);
        }else{
            saleOrder.setBusinessModel(purchaseLinkInfoDTO.getInfoBusinessModel());
            saleOrder.setState(DERP_ORDER.SALEORDER_STATE_001);
        }

        //计算加价比例
        BigDecimal rate = new BigDecimal(1) ;
        if(salesRate != null) {
            rate = rate.add(salesRate.divide(new BigDecimal(100)));
        }

        Integer totalNum = 0 ;
        BigDecimal totalAmount = new BigDecimal(0) ;

        List<SaleOrderItemModel> startSaleOrderItemList = new ArrayList<SaleOrderItemModel>() ;
        for(PurchaseOrderItemModel purchaseItem : purchaseModel.getItemList()) {
            //查询商品信息
            Map<String, Object> queryMerchantMap = new HashMap<>() ;
            queryMerchantMap.put("merchandiseId", purchaseItem.getGoodsId()) ;
            MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(queryMerchantMap);

            SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;
            saleOrderItemModel.setBarcode(purchaseItem.getBarcode());
            saleOrderItemModel.setGoodsCode(purchaseItem.getGoodsCode());
            saleOrderItemModel.setGoodsId(purchaseItem.getGoodsId());
            saleOrderItemModel.setGoodsName(purchaseItem.getGoodsName());
            saleOrderItemModel.setGoodsNo(purchaseItem.getGoodsNo());
//			saleOrderItemModel.setGrossWeight(purchaseItem.getGrossWeight());
//			saleOrderItemModel.setGrossWeightSum(purchaseItem.getGrossWeightSum());
//			saleOrderItemModel.setNetWeight(purchaseItem.getNetWeight());
//			saleOrderItemModel.setNetWeightSum(purchaseItem.getNetWeightSum());
            saleOrderItemModel.setNum(purchaseItem.getNum());
            saleOrderItemModel.setCreateDate(TimeUtils.getNow());
            saleOrderItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());

            BigDecimal price = purchaseItem.getPrice();
            price = price.multiply(rate).setScale(8, BigDecimal.ROUND_HALF_UP) ;
            saleOrderItemModel.setPrice(price);

            BigDecimal amount = purchaseItem.getAmount();
            amount = amount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP) ;
            saleOrderItemModel.setAmount(amount);

            totalNum += purchaseItem.getNum() ;
            totalAmount = totalAmount.add(amount) ;

            Double taxRate = purchaseItem.getTaxRate();

            if(taxRate != null) {
                //税率
                saleOrderItemModel.setTaxRate(taxRate);

                //含税单价
                BigDecimal taxPrice = purchaseItem.getTaxPrice();
                taxPrice = taxPrice.multiply(rate).setScale(8, BigDecimal.ROUND_HALF_UP) ;

                saleOrderItemModel.setTaxPrice(taxPrice);

                //含税金额
                BigDecimal taxAmount = purchaseItem.getTaxAmount();
                taxAmount = taxAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP) ;

                saleOrderItemModel.setTaxAmount(taxAmount);

                //税额
                BigDecimal tax = purchaseItem.getTax();
                tax = tax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP) ;

                saleOrderItemModel.setTax(tax);
            }


            Map<String, Object> params = new HashMap<String,Object>();
            params.put("goodsNo", purchaseItem.getGoodsNo());
            params.put("merchantId", purchaseModel.getMerchantId());
            MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params);

            if(outGoods == null){

                errorStrList.add("公司：" + purchaseModel.getMerchantName() + "未查到商品货号：" + purchaseItem.getGoodsNo() + "商品") ;

                continue;
            }

            // 拿备案价格
            BigDecimal filingPrice = outGoods.getFilingPrice();

            Double ratio = 1.00 ;
            BrandSuperiorMongo brandSuperiorModel = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(outGoods.getMerchandiseId());

            if(brandSuperiorModel != null
                    && brandSuperiorModel.getPriceDeclareRatio() != null) {
                ratio = brandSuperiorModel.getPriceDeclareRatio() ;
            }

            saleOrderItemModel.setDeclarePrice(new BigDecimal(ratio).multiply(filingPrice));

            startSaleOrderItemList.add(saleOrderItemModel) ;
        }

        saleOrder.setTotalNum(totalNum);
        saleOrder.setTotalAmount(totalAmount);

        saleOrder.setItemList(startSaleOrderItemList);

        return saleOrder ;
    }

    /**
     * 采购链路创建合同
     * @param purchaseModel
     * @param purchaseLinkInfoDTO
     * @return
     */
    private PurchaseContractModel linkCreateContract(PurchaseOrderModel purchaseModel, PurchaseLinkInfoDTO purchaseLinkInfoDTO) {
        PurchaseContractModel purchaseContractModel = new PurchaseContractModel() ;

        if(purchaseModel != null) {

            Map<String, Object> merchanto_params = new HashMap<String, Object>();
            merchanto_params.put("merchantId", purchaseModel.getMerchantId());
            MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);
            if(merchantMogo != null) {
                purchaseContractModel.setBuyerCnName(merchantMogo.getFullName());
                purchaseContractModel.setBuyerEnName(merchantMogo.getEnglishName());
                purchaseContractModel.setBuyerAuthorizedSignature(merchantMogo.getFullName());
                purchaseContractModel.setBuyerAuthorizedSignatureEn(merchantMogo.getEnglishName());
            }

            Map<String, Object> customer_params = new HashMap<String, Object>();
            customer_params.put("customerid", purchaseModel.getSupplierId()) ;
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params); ;

            if (customer != null) {
                purchaseContractModel.setSellerCnName(customer.getName());
                purchaseContractModel.setSellerEnName(customer.getEnName());
                purchaseContractModel.setSellerAuthorizedSignature(customer.getName());
                purchaseContractModel.setSellerAuthorizedSignatureEn(customer.getEnName());
                purchaseContractModel.setBeneficiaryBankName(customer.getDepositBank());
                purchaseContractModel.setBankAddress(customer.getBankAddress());
                purchaseContractModel.setBeneficiaryName(customer.getBeneficiaryName());
                purchaseContractModel.setAccountNo(customer.getBankAccount());
                purchaseContractModel.setSwiftCode(customer.getSwiftCode());
            }

            purchaseContractModel.setOrderId(purchaseModel.getId());
            purchaseContractModel.setCurrency(purchaseModel.getCurrency());
            purchaseContractModel.setPurchaseOrderNo(purchaseModel.getPoNo());
            purchaseContractModel.setDestinationdCn(purchaseLinkInfoDTO.getConDestinationdCn());
            purchaseContractModel.setDestinationdEn(purchaseLinkInfoDTO.getConDestinationdEn());
            purchaseContractModel.setLoadingCnPort(purchaseLinkInfoDTO.getConLoadingCnPort());
            purchaseContractModel.setLoadingEnPort(purchaseLinkInfoDTO.getConLoadingEnPort());
            purchaseContractModel.setMeansOfTransportation(purchaseLinkInfoDTO.getConMeansOfTransportation());
            purchaseContractModel.setPaymentMethod(purchaseLinkInfoDTO.getConPaymentMethod());
            purchaseContractModel.setPaymentMethodText(purchaseLinkInfoDTO.getConPaymentMethodText());
            purchaseContractModel.setPaymentMethodTextEn(purchaseLinkInfoDTO.getConPaymentMethodTextEn());
            purchaseContractModel.setSpecialAgreement(purchaseLinkInfoDTO.getConSpecialAgreement());
            purchaseContractModel.setSpecialAgreementEn(purchaseLinkInfoDTO.getConSpecialAgreementEn());
            purchaseContractModel.setDeliveryDate(purchaseLinkInfoDTO.getConDeliveryDate());
            purchaseContractModel.setItemList(purchaseModel.getItemList());
        }

        return purchaseContractModel;
    }

    /**
     * 若链路业务模式选择预售单，生成的销售订单转为预售单
     * @param saleModel
     * @return
     * @throws Exception
     */
    private Map<String, Object> linkCreatePreSaleOrder(SaleOrderModel saleModel, User user) throws Exception {

        PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel() ;

        preSaleOrderModel.setBuId(saleModel.getBuId());
        preSaleOrderModel.setBuName(saleModel.getBuName());
        preSaleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSD));
        preSaleOrderModel.setBusinessModel(DERP_ORDER.PRESALEORDER_BUSINESSMODEL_1);
        preSaleOrderModel.setState(DERP_ORDER.PRESALEORDER_STATE_001);
        preSaleOrderModel.setCurrency(saleModel.getCurrency());
        preSaleOrderModel.setCustomerId(saleModel.getCustomerId());
        preSaleOrderModel.setCustomerName(saleModel.getCustomerName());
        preSaleOrderModel.setMerchantId(saleModel.getMerchantId());
        preSaleOrderModel.setMerchantName(saleModel.getMerchantName());
        preSaleOrderModel.setOutDepotId(saleModel.getOutDepotId());
        preSaleOrderModel.setOutDepotName(saleModel.getOutDepotName());
        preSaleOrderModel.setPoNo(saleModel.getPoNo());
        preSaleOrderModel.setTallyingUnit(saleModel.getTallyingUnit());
        preSaleOrderModel.setRemark(saleModel.getRemark());
        preSaleOrderModel.setCreateDate(TimeUtils.getNow());
        preSaleOrderModel.setCreateName(user.getName());
        preSaleOrderModel.setCreater(user.getId());

        List<SaleOrderItemModel> itemList = saleModel.getItemList();

        List<PreSaleOrderItemModel> preItemList = new ArrayList<PreSaleOrderItemModel>() ;

        for (SaleOrderItemModel itemModel : itemList) {
            PreSaleOrderItemModel preItem = new PreSaleOrderItemModel() ;

            preItem.setAmount(itemModel.getAmount());
            preItem.setBarcode(itemModel.getBarcode());
            preItem.setCommbarcode(itemModel.getCommbarcode());
            preItem.setGoodsCode(itemModel.getGoodsCode());
            preItem.setGoodsId(itemModel.getGoodsId());
            preItem.setGoodsName(itemModel.getGoodsName());
            preItem.setGoodsNo(itemModel.getGoodsNo());
            preItem.setNum(itemModel.getNum());
            preItem.setPrice(itemModel.getPrice());
            preItem.setCreateDate(TimeUtils.getNow());

            preItemList.add(preItem) ;
        }

        Map<String, Object> returnMap = new LinkedHashMap<String, Object>() ;
        returnMap.put("preOrder", preSaleOrderModel) ;
        returnMap.put("preOrderList", preItemList) ;

        return returnMap ;
    }

    @Override
    public boolean getPurchasePriceManage(Long buId, Long supplierId, User user) {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("buId", buId) ;
        queryMap.put("merchantId", user.getMerchantId()) ;
        queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;

        MerchantBuRelMongo merchantBuRel = merchantBuRelMongoDao.findOne(queryMap);

        if(StringUtils.isBlank(merchantBuRel.getPurchasePriceManage())
                || DERP_SYS.PURCHASE_PRICE_MANAGE_0.equals(merchantBuRel.getPurchasePriceManage())) {
            return false ;
        }

        queryMap = new HashMap<String, Object>() ;
        queryMap.put("customerId", supplierId) ;
        queryMap.put("merchantId", user.getMerchantId()) ;
        queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(queryMap);

        if(customerRel != null
                && DERP_SYS.PURCHASE_PRICE_MANAGE_1.equals(customerRel.getPurchasePriceManage())) {
            return true ;
        }

        return false;
    }

    @Override
    public com.alibaba.fastjson.JSONObject checkGoodsDepotData(Long goodsId, User user) {
        com.alibaba.fastjson.JSONObject jsonObject =new com.alibaba.fastjson.JSONObject();

        Map<String, Object> merchandiseParam = new HashMap<>();
        merchandiseParam.put("merchandiseId", goodsId);
        MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);

        if(merchandiseInfoMogo!=null) {
            Integer torrToUnit = merchandiseInfoMogo.getTorrToUnit();
            Integer boxToUnit = merchandiseInfoMogo.getBoxToUnit();

            jsonObject.put("torrToUnit", torrToUnit) ;
            jsonObject.put("boxToUnit", boxToUnit) ;
        }

        return jsonObject;
    }

    @Override
    public List<Map<Long, net.sf.json.JSONArray>> getPurchaseByGoodsId(User user,Long depotId,String torrToUnit,Long buId, Long supplierId,
                                                                       String currency, String goodIds, Long stockLocationTypeId) throws Exception {
        List<Long> list = StrUtils.parseIds(goodIds);

        //返回值类型
        List<Map<Long, net.sf.json.JSONArray>> listMap=new ArrayList<>();


        //仓库
        Map<String, Object> queryDepotMap = new HashMap<String, Object>() ;
        queryDepotMap.put("depotId",depotId) ;
        DepotInfoMongo depot = depotInfoMongoDao.findOne(queryDepotMap);

        for(Long goodsId:list){
            //存储采购价格
            Set<BigDecimal> supplierMerchandisePriceList=new HashSet<>();

            Map<String, Object> smPriceMap = new HashMap<String, Object>() ;
            smPriceMap.put("merchantId", user.getMerchantId()) ;
            smPriceMap.put("customerId", supplierId) ;

            Map<String, Object> params2 = new HashMap<String,Object>();
            params2.put("merchandiseId", goodsId);
            MerchandiseInfoMogo productInfo = merchandiseInfoMogoDao.findOne(params2);
            if(productInfo==null){
                throw new DerpException("商品不存在");
            }
            smPriceMap.put("commbarcode", productInfo.getCommbarcode());
            smPriceMap.put("buId",buId);
            smPriceMap.put("currency", currency) ;
            smPriceMap.put("state", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003) ;
            if(stockLocationTypeId != null && stockLocationTypeId.intValue() != 0){
                smPriceMap.put("stockLocationTypeId", stockLocationTypeId) ;
            }

            List<SupplierMerchandisePriceMongo> smList = supplierMerchandisePriceMongoDao.findAll(smPriceMap);

            if(!smList.isEmpty()) {
                smList = smList.stream().sorted(Comparator.comparing(SupplierMerchandisePriceMongo :: getAuditDate).reversed()).collect(Collectors.toList()) ;

                for (SupplierMerchandisePriceMongo tempMongo : smList) {

                    String effectiveDate = tempMongo.getEffectiveDate();
                    String expiryDate = tempMongo.getExpiryDate();

                    if(TimeUtils.parse(effectiveDate, "yyyy-MM-dd").getTime() <=TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()
                            && TimeUtils.parse(expiryDate, "yyyy-MM-dd").getTime() >= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()) {
                        supplierMerchandisePriceList.add(tempMongo.getSupplyPrice());
                    }
                }
            }

            Integer torrUnit =null;
            if(depot!=null) {
                //仓库为海外仓，则获取商品的箱托，单价*箱托
                if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {

                    if (DERP.ORDER_TALLYINGUNIT_00.equals(torrToUnit)) {
                        torrUnit = productInfo.getTorrToUnit();

                        if (torrUnit == null || torrUnit == 0) {
                            throw new DerpException("货号：" + productInfo.getGoodsNo() + "托转件为空");
                        }
                    }

                    if (DERP.ORDER_TALLYINGUNIT_01.equals(torrToUnit)) {
                        torrUnit=productInfo.getBoxToUnit();
                        if (torrUnit == null || torrUnit == 0) {
                            throw new DerpException("货号：" + productInfo.getGoodsNo() + "箱转件为空");
                        }
                    }
                }
            }

            //单价*箱托
            Set<BigDecimal> setPrice = new HashSet<>();
            for(BigDecimal priceList:supplierMerchandisePriceList){
                BigDecimal price=priceList;
                if(torrUnit!=null){
                    price=price.multiply(new BigDecimal(torrUnit));
                }
                setPrice.add(price);
            }
            Map map=new HashMap();
            map.put("id",goodsId);
            map.put("price", net.sf.json.JSONArray.fromObject(setPrice));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * 生成链路中订单
     * @param map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> saveLinkOrder(Map<String, Object> map, Map<String, Object> batchMap,
                                             User user, Long purchaseTradeLinkId) throws Exception {

        Map<String, Object> merchantIdOrderIdsMap = new LinkedHashMap<String, Object>() ;

        String batchNo = (String)batchMap.get("batchNo");
        Date productionDate = (Date)batchMap.get("productionDate") ;
        Date overdueDate = (Date)batchMap.get("overdueDate") ;

        PurchaseLinkInfoModel purchaseLinkInfo = purchaseLinkInfoDao.searchById(purchaseTradeLinkId);
        Long purchaseOrderId = purchaseLinkInfo.getPurchaseOrderId();

        PurchaseOrderModel queryOrderModel = new PurchaseOrderModel() ;
        queryOrderModel.setId(purchaseOrderId);
        PurchaseOrderModel details = purchaseOrderDao.getDetails(queryOrderModel);

        List<PurchaseOrderItemModel> purchaseItemList = details.getItemList();

        for (String merchantName : map.keySet()) {
            Map<String, Object> merchantOrderMap = (Map<String, Object>)map.get(merchantName);

            Map<String, Object> orderIdMap = new HashMap<String, Object>() ;
            List<Long> saleOrderIds = new ArrayList<Long>() ;
            List<Long> purchaseOrderIds = new ArrayList<Long>() ;
            List<Long> preSaleOrderIds = new ArrayList<Long>() ;
            Long merchantId = null ;

            /**
             * 单个商家生成销售订单
             */
            List<SaleOrderModel> saleList = (List<SaleOrderModel>)merchantOrderMap.get("saleOrderList");

            if(saleList != null) {

                String currentDate = TimeUtils.format(new Date(), "yyyy-MM-dd");

                for (int i = 0 ; i < saleList.size(); i++) {

                    SaleOrderModel saleOrderModel = saleList.get(i) ;

                    merchantId = saleOrderModel.getMerchantId() ;

                    //若对应销售订单业务模式为"0" 生成预售单
                    if("0".equals(saleOrderModel.getBusinessModel())) {
                        Map<String, Object> preModelMap = linkCreatePreSaleOrder(saleOrderModel, user);

                        PreSaleOrderModel preModel = (PreSaleOrderModel)preModelMap.get("preOrder");
                        List<PreSaleOrderItemModel> preItemList = (List<PreSaleOrderItemModel>)preModelMap.get("preOrderList");

                        Long orderId = preSaleOrderDao.save(preModel);

                        for (PreSaleOrderItemModel preSaleOrderItemModel : preItemList) {
                            preSaleOrderItemModel.setOrderId(orderId);

                            preSaleOrderItemDao.save(preSaleOrderItemModel) ;
                        }

                        preSaleOrderIds.add(orderId) ;

                        continue ;
                    }

                    saleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));
                    saleOrderModel.setCreateDate(TimeUtils.getNow());
                    saleOrderModel.setCreateName(user.getName());
                    saleOrderModel.setCreater(user.getId());
                    saleOrderModel.setAuditDate(TimeUtils.getNow());
                    saleOrderModel.setAuditName(user.getName());
                    saleOrderModel.setAuditor(user.getId());
                    saleOrderModel.setReceiveDate(TimeUtils.parse(currentDate, "yyyy-MM-dd"));
                    saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
                    Long salesId = saleOrderDao.save(saleOrderModel);

                    //自动生成日志
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5,
                            saleOrderModel.getCode(), "交易链路新增", null, null);

                    if(DERP_ORDER.SALEORDER_STATE_003.equals(saleOrderModel.getState())) {

                        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5,
                                saleOrderModel.getCode(), "提交", null, null);

                        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5,
                                saleOrderModel.getCode(), "审核", null, null);
                    }

                    List<SaleOrderItemModel> itemList = saleOrderModel.getItemList();

                    for (SaleOrderItemModel saleOrderItemModel : itemList) {
                        saleOrderItemModel.setOrderId(salesId);
                        saleOrderItemDao.save(saleOrderItemModel) ;
                    }

                    saleOrderIds.add(salesId) ;
                }
            }

            /**
             * 单个商家生成采购订单
             */
            List<PurchaseOrderModel> purchaseList = (List<PurchaseOrderModel>)merchantOrderMap.get("purchaseList");
            List<PurchaseContractModel> purchaseContractList = (List<PurchaseContractModel>)merchantOrderMap.get("purchaseContractList");

            if(purchaseList != null) {
                for (int i = 0 ; i < purchaseList.size() ; i ++) {

                    PurchaseOrderModel purchaseOrderModel = purchaseList.get(i);

                    merchantId = purchaseOrderModel.getMerchantId() ;

                    //获取仓库信息
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("depotId", purchaseOrderModel.getDepotId());
                    DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

                    if (depot == null) {
                        throw new DerpException("仓库不存在");
                    }

                    purchaseOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
                    purchaseOrderModel.setCreateDate(TimeUtils.getNow());
                    purchaseOrderModel.setCreateName(user.getName());
                    purchaseOrderModel.setCreater(user.getId());
                    purchaseOrderModel.setAuditDate(TimeUtils.getNow());
                    purchaseOrderModel.setAuditName(user.getName());
                    purchaseOrderModel.setAuditer(user.getId());
                    Long purchaseId = purchaseOrderDao.save(purchaseOrderModel);

                    //自动生成日志
                    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1,
                            purchaseOrderModel.getCode(), "交易链路新增", null, null);

                    if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())) {

                        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1,
                                purchaseOrderModel.getCode(), "提交", null, null);

                        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1,
                                purchaseOrderModel.getCode(), "审核", null, null);
                    }

                    List<PurchaseOrderItemModel> itemList = purchaseOrderModel.getItemList();
                    for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {
                        purchaseOrderItemModel.setPurchaseOrderId(purchaseId);

                        //原采购订单表体成分设值
                        for (PurchaseOrderItemModel tempModel : purchaseItemList) {

                            if(tempModel.getGoodsNo().equals(purchaseOrderItemModel.getGoodsNo())) {
                                purchaseOrderItemModel.setConstituentRatio(tempModel.getConstituentRatio());

                                break ;
                            }

                        }

                        purchaseOrderItemDao.save(purchaseOrderItemModel) ;

                        if(DERP_ORDER.PURCHASEORDER_STATUS_001.equals(purchaseOrderModel.getStatus())) {
                            continue ;
                        }
                    }

                    PurchaseContractModel purchaseContractModel = purchaseContractList.get(i);
                    purchaseContractModel.setOrderId(purchaseId);
                    purchaseContractModel.setCreateDate(TimeUtils.getNow());

                    modifyPurchaseContract(purchaseContractModel, user);

                    purchaseOrderIds.add(purchaseId) ;
                }
            }

            orderIdMap.put("saleOrderIds", saleOrderIds) ;
            orderIdMap.put("purchaseOrderIds", purchaseOrderIds) ;
            orderIdMap.put("preSaleOrderIds", preSaleOrderIds) ;

            if(merchantId != null) {
                merchantIdOrderIdsMap.put(String.valueOf(merchantId), orderIdMap) ;
            }
        }

        /**出库入库放到同一个事务处理**/
        saveLinkInOutDepot(merchantIdOrderIdsMap, batchNo, productionDate,  overdueDate, user) ;

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, details.getCode(), "生成采购链路订单", null, null);

        return merchantIdOrderIdsMap ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveLinkInOutDepot(Map<String, Object> merchantIdOrderIdsMap, String batchNo,
                                   Date produceDate, Date overdueDate, User user) throws Exception {

        List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<>() ;

        //数值游标，判断是否最后商家
        int index = 0 ;

        for (String merchantIdStr : merchantIdOrderIdsMap.keySet()) {

            ++ index ;

            Long merchantId = Long.valueOf(merchantIdStr);

            Map<String, Object> orderIdsMap = (Map<String, Object>)merchantIdOrderIdsMap.get(merchantIdStr);
            List<Long> saleOrderIds = (List<Long>)orderIdsMap.get("saleOrderIds");
            List<Long> purchaseList = (List<Long>)orderIdsMap.get("purchaseOrderIds");

            Map<String, Object> merchanto_params = new HashMap<String, Object>();
            merchanto_params.put("merchantId", merchantId);
            MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);

            String dateStr = TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

            /**采购入库**/

            List<Long> warehouseIdsList = new ArrayList<Long>() ;
            for (Long purchaseTempId : purchaseList) {

                PurchaseOrderModel tempOrder = purchaseOrderDao.searchById(purchaseTempId);

                if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(tempOrder.getStatus())) {

                    /**若末级采购订单对应仓库进出口指令为是，跳过不自动入库*/
                    Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>() ;
                    depotMerchantRelMap.put("merchantId", tempOrder.getMerchantId()) ;
                    depotMerchantRelMap.put("depotId", tempOrder.getDepotId()) ;
                    DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

                    if(merchantIdOrderIdsMap.size() == index
                            && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())) {
                        continue ;
                    }

                    warehouseIdsList.add(purchaseTempId) ;
                }

            }

            List<InvetAddOrSubtractRootJson> jsonPurchaseList = saveTradelinkInWarehouse(warehouseIdsList, user,dateStr, batchNo, produceDate, overdueDate);

//            for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonPurchaseList) {
//                commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
//            }

            jsonList.addAll(jsonPurchaseList) ;

            /**销售出库**/
            List<Long> outDepotIdsList = new ArrayList<Long>() ;
            for (Long saleOrderTempId : saleOrderIds) {
                SaleOrderModel tempOrder = saleOrderDao.searchById(saleOrderTempId);

                if(DERP_ORDER.SALEORDER_STATE_003.equals(tempOrder.getState())) {
                    outDepotIdsList.add(saleOrderTempId) ;
                }
            }

            List<InvetAddOrSubtractRootJson> jsonSaleList = saveTradelinkOutWarehouse(outDepotIdsList, user.getId(),
                    merchantMogo.getTopidealCode(), merchantId, dateStr,
                    batchNo, produceDate, overdueDate) ;

            jsonList.addAll(jsonSaleList) ;
        }

        pushInventory(jsonList);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getLinkOrderCode(Map<String, Object> merchantIdOrderIdsMap) throws SQLException {

        List<String> codes = new ArrayList<String>() ;

        for (String merchantIdStr : merchantIdOrderIdsMap.keySet()) {

            Map<String, Object> orderIdsMap = (Map<String, Object>)merchantIdOrderIdsMap.get(merchantIdStr);
            List<Long> saleOrderIds = (List<Long>)orderIdsMap.get("saleOrderIds");
            List<Long> purchaseList = (List<Long>)orderIdsMap.get("purchaseOrderIds");
            List<Long> preSaleOrderIds = (List<Long>)orderIdsMap.get("preSaleOrderIds");

            for (Long purchaseId : purchaseList) {
                PurchaseOrderModel purchaseModel = purchaseOrderDao.searchById(purchaseId);

                codes.add(purchaseModel.getCode() + ";" + DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, purchaseModel.getStatus())) ;
            }

            for (Long saleId : saleOrderIds) {
                SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleId) ;

                codes.add(saleOrderModel.getCode() + ";" + DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_stateList, saleOrderModel.getState())) ;
            }

            for (Long preSaleId : preSaleOrderIds) {
                PreSaleOrderModel preSaleModel = preSaleOrderDao.searchById(preSaleId);

                codes.add(preSaleModel.getCode() + ";" + DERP_ORDER.getLabelByKey(DERP_ORDER.preSaleOrder_stateList, preSaleModel.getState())) ;
            }
        }

        return codes ;
    }

    @Override
    public Map<String, Object> getAmountAdjustmentDetail(Long id) throws SQLException {

        Map<String, Object> map = new HashMap<String, Object>() ;

        PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
        purchaseModel.setId(id); ;

        /** 获取采购单*/
        purchaseModel = purchaseOrderDao.getDetails(purchaseModel) ;

        if(StringUtils.isNotBlank(purchaseModel.getWriteOffStatus())) {

            String writeOffStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, purchaseModel.getWriteOffStatus());

            throw new DerpException("采购订单：" + purchaseModel.getCode() + "红冲状态为:" + writeOffStatusLabel +"，不予修改操作") ;
        }


        /** 获取采购单明细*/
        List<PurchaseOrderItemModel> itemList = purchaseModel.getItemList();

        map.put("order", purchaseModel) ;
        map.put("itemList", itemList) ;


        return map;
    }

    @Override
    public void saveAmountAdjustment(PurchaseOrderModel model, String itemListStr, User user) throws SQLException {

        PurchaseOrderModel tempModel = purchaseOrderDao.searchById(model.getId());

        //对于标识“确认不通过”且进行了金额修改操作保存成功后，需更新确认标识为“待确认”
        if(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_1.equals(tempModel.getAmountConfirmStatus())) {
            model.setAmountConfirmStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0);
        }

        model.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_1);
        model.setModifyDate(TimeUtils.getNow());
        model.setAmountAdjustmentDate(TimeUtils.getNow());
        model.setAmountAdjustmenter(user.getId());
        model.setAmountAdjustmentName(user.getName());
        purchaseOrderDao.modify(model) ;

        PurchaseOrderModel purchaseModel = purchaseOrderDao.searchById(model.getId());

        List<PurchaseOrderItemModel> itemList = JSONArray.parseArray(itemListStr, PurchaseOrderItemModel.class) ;
//        Map<String,List<PurchaseOrderItemModel>> itemMap = itemList.stream().collect(Collectors.groupingBy(i -> i.getGoodsNo()+"_"+i.getPrice()));
//        for(String key: itemMap.keySet()){
//            List<PurchaseOrderItemModel> tempItemList = itemMap.get(key);
//            if(tempItemList != null && tempItemList.size() > 1){
//                throw new DerpException("相同商品货号："+key.split("_")[0]+" 单价不允许相同") ;
//            }
//        }
        for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {

            if(purchaseOrderItemModel.getTaxAmount() != null && purchaseOrderItemModel.getAmount() != null) {
                BigDecimal tax = purchaseOrderItemModel.getTaxAmount().subtract(purchaseOrderItemModel.getAmount());

                purchaseOrderItemModel.setTax(tax);
            }

            purchaseOrderItemModel.setModifyDate(TimeUtils.getNow());

            purchaseOrderItemDao.modify(purchaseOrderItemModel) ;

        }

        //获取卓烨贸易 公司信息
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", "ERP26143500022");
        MerchantInfoMongo merchantMongo = merchantMongoDao.findOne(params);

        //获取母婴美赞臣组 事业部信息
        params.clear();
        params.put("code", "E080600");
        BusinessUnitMongo MYbusinessUnitMongo = businessUnitMongoDao.findOne(params);
        //母婴美赞臣分销组
        params.clear();
        params.put("code", "E080602");
        BusinessUnitMongo FXbusinessUnitMongo = businessUnitMongoDao.findOne(params);
        /**
         * 采购订单金额修改时触发更新采购单表t_purchase_order_item的本位币单价tgt_price、本位币金额tgt_amount，判断若为“卓烨主体+母婴美赞臣组”的采购单时，无需触发更新。
         */
        if(!(merchantMongo.getMerchantId().equals(tempModel.getMerchantId()) && (MYbusinessUnitMongo.getBusinessUnitId().equals(tempModel.getBuId())
                || FXbusinessUnitMongo.getBusinessUnitId().equals(tempModel.getBuId())))){
            /**查询是否存在汇率，存在修改本位币金额，单价*/
            List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseModel.getId());

            for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {
                commonBusinessService.saveRate(purchaseModel, purchaseWarehouseModel.getInboundDate());
            }
        }
        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(tempModel.getBuId());
        emailUserDTO.setBuName(tempModel.getBuName());
        emailUserDTO.setMerchantId(tempModel.getMerchantId());
        emailUserDTO.setMerchantName(tempModel.getMerchantName());
        emailUserDTO.setOrderCode(tempModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_10);
        emailUserDTO.setSupplier(tempModel.getSupplierName());
        emailUserDTO.setPoNum(tempModel.getPoNo());
        emailUserDTO.setStatus("金额未确认");
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setReviewerId(tempModel.getAmountConfirmer());
        emailUserDTO.setSubmitId(tempModel.getSubmiter()==null?null:Arrays.asList(tempModel.getSubmiter().toString()));
        emailUserDTO.setAuditorId(tempModel.getAuditer());
        emailUserDTO.setModifyId(tempModel.getAmountAdjustmenter());
        String auditMethod = tempModel.getAuditMethod();
        String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
        emailUserDTO.setAuditMethod(auditMethodLabel);

        List list=new ArrayList();
        list.add(String.valueOf(user.getId()));
        if(tempModel.getAmountConfirmer()!=null){
            list.add(tempModel.getAmountConfirmer());
        }
        emailUserDTO.setUserId(list);

        JSONObject json = JSONObject.fromObject(emailUserDTO) ;

        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------采购发送邮件发送失败-------", json.toString());
        }

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, tempModel.getCode(), "修改金额", null, null);
    }


    /**
     * 通过列表中对象的某个字段进行去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void savePurchaseDelivery(PurchaseWarehouseForm form, User user) throws Exception {

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(form.getPurchaseId());

        if(purchaseOrder == null) {
            throw new DerpException("采购订单不存在") ;
        }
        Timestamp inboundDate = TimeUtils.parse(form.getInboundDate(), "yyyy-MM-dd");

        Integer days = TimeUtils.daysBetween(new Date(), inboundDate) ;

        if(days > 0) {
            throw new DerpException("入库时间不能晚于当前时间") ;
        }

        // 判断归属日期是否小于 关账日期/月结日期
        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
        closeAccountsMongo.setMerchantId(purchaseOrder.getMerchantId());
        closeAccountsMongo.setDepotId(purchaseOrder.getDepotId());
        closeAccountsMongo.setBuId(purchaseOrder.getBuId());

        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
        String maxCloseAccountsMonth = "";
        if (StringUtils.isNotBlank(maxdate)) {
            // 获取该月份下月时间
            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
        }
        if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
            // 关账下个月日期大于 入库日期
            if (inboundDate.getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                throw new DerpException("入库时间必须大于关账日期/月结日期");
            }
        }


        if(form.getDepotId() == null){
            throw new DerpException("入库仓库不能为空");
        }
        // 获取仓库信息
        Map<String, Object> depotMap = new HashMap<String, Object>() ;
        depotMap.put("depotId", form.getDepotId()) ;

        DepotInfoMongo depot = depotInfoMongoDao.findOne(depotMap);
        if(depot==null){
            throw new DerpException("未找到启用的入库仓库");
        }
        Map<String, Object> depotMerchantMap = new HashMap<String, Object>() ;
        depotMerchantMap.put("depotId", form.getDepotId()) ;
        depotMerchantMap.put("merchantId", user.getMerchantId()) ;
        DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantMap);
        if(depotMerchantRel == null) {
            throw new DerpException(user.getName() + "与" + depot.getName() + "未建立公司仓库关联") ;
        }
        //入库仓进出口指令为”是“，只走接口，不允许进行打托入库
        if(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())){
            throw new DerpException("入库仓库："+depot.getName()+"进出口指令为”是“，不允许打托入库");
        }


        PurchaseWarehouseModel model = new PurchaseWarehouseModel();
        model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 入库单号
        model.setDepotId(depot.getDepotId());// 仓库iD
        model.setDepotName(depot.getName());// 仓库名称
        model.setCreater(user.getId());// 创建人
        model.setMerchantId(user.getMerchantId());// 商家id
        model.setMerchantName(user.getMerchantName());// 商家名称
        model.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
        model.setContractNo(purchaseOrder.getContractNo());// 合同号
        model.setBusinessModel(purchaseOrder.getBusinessModel()); // 设置业务主体
        model.setBuId(purchaseOrder.getBuId()); // 事业部
        model.setBuName(purchaseOrder.getBuName());
        model.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
        model.setInboundDate(inboundDate);
        model.setCurrency(purchaseOrder.getCurrency());
        if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())){
            model.setTallyingUnit(purchaseOrder.getTallyingUnit());// 海外仓理货单位
        }

        Long orderId = purchaseWarehouseDao.save(model);


        // 新增入库单与采购单关系
        WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
        relModel.setWarehouseId(orderId);
        relModel.setPurchaseOrderId(purchaseOrder.getId());
        warehouseOrderRelDao.save(relModel);

        int tallyZeroNum = 0 ;

        Map<Long,Long> itemIdMap = new HashMap<>();
        List<DeclareOrderDeliveryItemForm> itemList = form.getItemList();
        for(DeclareOrderDeliveryItemForm item : itemList){
            if(item.getPurchaseItemId() == null){
                throw new DerpException("关联的采购明细id为空，请及时维护");
            }
            if(item.getGoodsId()==null){
                throw new DerpException("商品id为空，条形码:"+item.getBarcode()+"，未关联入库仓库，请及时维护");
            }
            Map<String,Object> param = new HashMap<>();
            param.put("merchantId", user.getMerchantId());
            param.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            param.put("merchandiseId", item.getGoodsId());
            List<MerchandiseInfoMogo> goodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(param, depot.getDepotId());
            if(goodsList == null || goodsList.size() < 1){
                throw new DerpException("条形码:"+item.getBarcode()+"，未关联入库仓库，请及时维护");
            }

        }

        Map<Long, List<DeclareOrderDeliveryItemForm>> itemMap = itemList.stream().collect(Collectors.groupingBy(DeclareOrderDeliveryItemForm::getPurchaseItemId));
        for (Long purchaseItemId : itemMap.keySet()) {
            PurchaseOrderItemModel queryItemModel = purchaseOrderItemDao.searchById(purchaseItemId);
            List<DeclareOrderDeliveryItemForm> queryItemList = itemMap.get(purchaseItemId);
            DeclareOrderDeliveryItemForm itemForm = queryItemList.get(0);
            Integer normalSum = queryItemList.stream().map(DeclareOrderDeliveryItemForm::getNormalNum).reduce(0,Integer::sum);
            Integer expireSum = queryItemList.stream().map(DeclareOrderDeliveryItemForm::getExpireNum).reduce(0,Integer::sum);
            Integer wornSum = queryItemList.stream().map(DeclareOrderDeliveryItemForm::getWornNum).reduce(0,Integer::sum);

            Integer tallyingNum = normalSum + expireSum + wornSum;

            if (tallyingNum <= 0) {
                tallyZeroNum++;
                continue;
            }
            //根据采购明细id获取已入库数量
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("purchaseItemId", queryItemModel.getId());
            paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
            List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
            Integer warehouseNum = 0;
            if(numList != null && numList.size() > 0){
                BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
                warehouseNum = queryNum.intValue();
            }
            Integer unWarehouseNum = queryItemModel.getNum() - warehouseNum;//待入库数量
            if(unWarehouseNum < tallyingNum) {
                throw new DerpException("商品条形码：" + queryItemModel.getBarcode() + ",采购单价："+queryItemModel.getPrice()+"入库数量不能超过待入库数量") ;
            }
            Integer lackNum = queryItemModel.getNum() - tallyingNum ;

            paramMap.clear();
            paramMap.put("merchandiseId", itemForm.getGoodsId());
            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);

            PurchaseWarehouseItemModel warehouseItem = new PurchaseWarehouseItemModel() ;
            warehouseItem.setWarehouseId(orderId);
            warehouseItem.setGoodsId(merchandise.getMerchandiseId());// 商品id
            warehouseItem.setGoodsNo(merchandise.getGoodsNo());// 商品货号
            warehouseItem.setGoodsName(merchandise.getName());// 商品名称
            warehouseItem.setBarcode(merchandise.getBarcode());
            warehouseItem.setMultiNum(0);
            warehouseItem.setLackNum(lackNum);
            warehouseItem.setTallyingNum(tallyingNum);
            warehouseItem.setNormalNum(itemForm.getNormalNum());
            if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
                warehouseItem.setTallyingUnit(purchaseOrder.getTallyingUnit());
            }
            warehouseItem.setPurchaseNum(queryItemModel.getNum());
            warehouseItem.setCreateDate(TimeUtils.getNow());
            warehouseItem.setPurchaseItemId(queryItemModel.getId());//采购订单表体Id
//            Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
//            queryGoodsMap.put("merchandiseId", queryItemModel.getGoodsId()) ;
//            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryGoodsMap);

            Double grossWeight = merchandise.getGrossWeight();// 毛重
            Double netWeight = merchandise.getNetWeight();// 净重

            if (grossWeight != null) {
                grossWeight = grossWeight * tallyingNum;
            }

            if (netWeight != null) {
                netWeight = netWeight * tallyingNum;
            }

            warehouseItem.setGrossWeight(grossWeight);// 毛重
            warehouseItem.setNetWeight(netWeight);// 净重

            warehouseItem.setLength(merchandise.getLength());// 长
            warehouseItem.setWidth(merchandise.getWidth());// 宽
            warehouseItem.setVolume(merchandise.getVolume());// 体积
            warehouseItem.setHeight(merchandise.getHeight());// 高

            Long itemId = purchaseWarehouseItemDao.save(warehouseItem);

            itemIdMap.put(purchaseItemId,itemId);
        }
        if(tallyZeroNum == itemMap.keySet().size()) {
            throw new DerpException("入库失败，至少一个货号入库数量大于0") ;
        }
        //批次信息
        Map<String, List<DeclareOrderDeliveryItemForm>> batchItemMap = itemList.stream().filter(i-> (i.getNormalNum() + i.getWornNum() + i.getExpireNum()) > 0)
                .collect(Collectors.groupingBy(i-> i.getPurchaseItemId()+"_"+(StringUtils.isBlank(i.getBatchNo())? "":i.getBatchNo() )
                 +"_"+(StringUtils.isBlank(i.getProductionDate())? "":i.getProductionDate()) +"_"+(StringUtils.isBlank(i.getOverdueDate())? "":i.getOverdueDate())));

        for(String batchKey : batchItemMap.keySet()){
            Long purchaseItemId = Long.valueOf(batchKey.split("_")[0]);
            PurchaseOrderItemModel queryItemModel = purchaseOrderItemDao.searchById(purchaseItemId);
            List<DeclareOrderDeliveryItemForm> queryItemList = batchItemMap.get(batchKey);
            DeclareOrderDeliveryItemForm itemForm = queryItemList.get(0);
            Integer normalSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getNormalNum).sum();
            Integer expireSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getExpireNum).sum();
            Integer wornSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getWornNum).sum();

            PurchaseWarehouseBatchModel batchModel = new PurchaseWarehouseBatchModel() ;
            batchModel.setGoodsId(itemForm.getGoodsId());
            batchModel.setItemId(itemIdMap.get(purchaseItemId));
            batchModel.setNormalNum(normalSum);
            batchModel.setExpireNum(expireSum);
            batchModel.setWornNum(wornSum);
            batchModel.setCreateDate(TimeUtils.getNow());

            if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())) {
                if(StringUtils.isBlank(itemForm.getBatchNo()) ){
                    throw new DerpException("入库仓库入库批次强校验，批次信息不能为空") ;
                }
                if(StringUtils.isBlank(itemForm.getProductionDate()) ){
                    throw new DerpException("入库仓库入库批次强校验，生效日期不能为空") ;
                }
                if(StringUtils.isBlank(itemForm.getOverdueDate()) ){
                    throw new DerpException("入库仓库入库批次强校验，失效日期不能为空") ;
                }

            }

            if(StringUtils.isNotBlank(itemForm.getProductionDate())) {
                batchModel.setProductionDate(TimeUtils.parse(itemForm.getProductionDate(), "yyyy-MM-dd"));
            }
            if(StringUtils.isNotBlank(itemForm.getOverdueDate())) {
                batchModel.setOverdueDate(TimeUtils.parse(itemForm.getOverdueDate(), "yyyy-MM-dd"));
            }
            batchModel.setBatchNo(itemForm.getBatchNo());
            purchaseWarehouseBatchDao.save(batchModel) ;
        }


//        commonBusinessService.saveAutoPurchaseAnalysis(purchaseWarehouse.getCode());

        InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
        inventoryRoot.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTopic());
        inventoryRoot.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTags());
        Map<String, Object> customParam = new HashMap<String, Object>();
        inventoryRoot.setCustomParam(customParam);
        // 增加库存

        inventoryRoot.setMerchantId(model.getMerchantId().toString());
        inventoryRoot.setMerchantName(model.getMerchantName());
        inventoryRoot.setBusinessNo(purchaseOrder.getCode());
        inventoryRoot.setTopidealCode(purchaseOrder.getTopidealCode());
        inventoryRoot.setDepotId(model.getDepotId().toString());
        inventoryRoot.setDepotName(model.getDepotName());
        inventoryRoot.setOrderNo(model.getCode());
        inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
        inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
        inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
        inventoryRoot.setBuId(model.getBuId().toString());
        inventoryRoot.setBuName(model.getBuName());

        Map<String, Object> depotInfo_params = new HashMap<String, Object>();
        depotInfo_params.put("depotId", model.getDepotId());// 根据仓库id
        DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
        if (mongo != null) {
            inventoryRoot.setDepotCode(mongo.getCode());
            inventoryRoot.setDepotType(mongo.getType());
            inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
        }

        // 获取当前年月
        inventoryRoot.setOwnMonth(TimeUtils.format(model.getInboundDate(), "yyyy-MM"));
        inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(model.getInboundDate()));

        int depot_flag = 0;
        if (mongo != null && mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
            depot_flag = 1;
        }

        List<InvetAddOrSubtractGoodsListJson> inventoryItemList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
        List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(orderId);
        for (PurchaseWarehouseBatchModel bModel : batchList) {

            // 坏货数量
            if (bModel.getWornNum() != null && bModel.getWornNum() > 0) {
                InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user,DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
                listJson.setNum(bModel.getWornNum());
                listJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）

                if (bModel.getOverdueDate() != null) {
                    int daysBetween = TimeUtils.daysBetween(bModel.getOverdueDate(), model.getInboundDate());

                    if (daysBetween > 0) {
                        listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
                    } else {
                        listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
                    }

                } else {
                    listJson.setIsExpire(DERP.ISEXPIRE_1);
                }
                listJson.setStockLocationTypeId(purchaseOrder.getStockLocationTypeId() == null ? "" :purchaseOrder.getStockLocationTypeId().toString());
                listJson.setStockLocationTypeName(purchaseOrder.getStockLocationTypeName());

                inventoryItemList.add(listJson);
            }

            // 过期数量
            if (bModel.getExpireNum() != null && bModel.getExpireNum() > 0) {
                InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user, DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
                listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
                listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
                listJson.setNum(bModel.getExpireNum());
                listJson.setStockLocationTypeId(purchaseOrder.getStockLocationTypeId() == null ? "" :purchaseOrder.getStockLocationTypeId().toString());
                listJson.setStockLocationTypeName(purchaseOrder.getStockLocationTypeName());
                inventoryItemList.add(listJson);
            }
            // 正常数量
            if(bModel.getNormalNum() != null && bModel.getNormalNum() > 0){
                InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user, DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
                listJson.setNum(bModel.getNormalNum());
                listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
                listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
                listJson.setStockLocationTypeId(purchaseOrder.getStockLocationTypeId() == null ? "" :purchaseOrder.getStockLocationTypeId().toString());
                listJson.setStockLocationTypeName(purchaseOrder.getStockLocationTypeName());
                inventoryItemList.add(listJson);
            }

        }

        inventoryRoot.setGoodsList(inventoryItemList);

        rocketMQProducer.send(JSONObject.fromObject(inventoryRoot).toString(),
                MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
                MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrder.getCode(), "上架入库", null, null);
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, model.getCode(), "上架入库", null, null);
    }

    /**
     * 确认入库构建传输对象
     * @throws Exception
     */
    private InvetAddOrSubtractGoodsListJson generateTransportGoods(PurchaseWarehouseBatchModel bModel, int depot_flag, User user, String operateType) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
        listJson.setGoodsId(bModel.getGoodsId().toString());
        listJson.setGoodsNo(bModel.getGoodsNo());
        listJson.setGoodsName(bModel.getGoodsName());
        listJson.setBatchNo(bModel.getBatchNo());
        listJson.setBarcode(bModel.getBarcode());
        listJson.setOperateType(operateType);// 字符串 0 调减 1调增

        if (bModel.getProductionDate() != null) {
            listJson.setProductionDate(sdf.format(bModel.getProductionDate()));
        }

        if (bModel.getOverdueDate() != null) {
            listJson.setOverdueDate(sdf.format(bModel.getOverdueDate()));
        }

        // 海外仓必填
        if (depot_flag == 1) {
            if (bModel.getTallyingUnit() != null) {
                // 托盘
                if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
                    listJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
                } else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
                    listJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
                } else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
                    listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
                }
            } else {
                listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
            }
        }

        return listJson;
    }

    @Override
    public void pushInventory(List<InvetAddOrSubtractRootJson> jsonList) throws SQLException {

        for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {

            String warehouseCode = invetAddOrSubtractRootJson.getOrderNo();

            if(warehouseCode.startsWith(DERP.UNIQUEID_PREFIX_CGRK)){

                PurchaseWarehouseModel queryModel = new PurchaseWarehouseModel() ;
                queryModel.setCode(warehouseCode);
                queryModel = purchaseWarehouseDao.searchByModel(queryModel) ;


                PurchaseWarehouseModel updateModel = new PurchaseWarehouseModel() ;
                updateModel.setId(queryModel.getId());
                updateModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);

                purchaseWarehouseDao.modify(updateModel);

            }

            try {
                SendResult k=rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
                        MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
                        MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
                System.out.println(k);

                Thread.sleep(800L);

            } catch (Exception e) {
                LOGGER.error("----------------------" + warehouseCode
                        + "采购订单中转仓入库增加库存失败----------------------");
            }

        }


    }

    @Override
    public Long saveJBPurchaseLinkByPurchaseId(Long id, User user, Long tradeLinkId) throws SQLException {

        PurchaseLinkInfoDTO purchaseLinkInfoDTO = purchaseLinkInfoDao.getPurchaseLinkDtoByPurchaseId(id) ;
        Long purchaseLinkInfoId = null ;

        if(purchaseLinkInfoDTO == null) {

            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

            PurchaseContractModel purchaseContractModel = new PurchaseContractModel() ;
            purchaseContractModel.setOrderId(id);
            purchaseContractModel = purchaseContractDao.searchByModel(purchaseContractModel) ;

            PurchaseLinkInfoModel purchaseLinkInfoModel = new PurchaseLinkInfoModel() ;
            purchaseLinkInfoModel.setInfoAuditDate(purchaseOrderModel.getAttributionDate());
            purchaseLinkInfoModel.setInfoBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_1);
            purchaseLinkInfoModel.setInfoCarrier(purchaseOrderModel.getCarrier());
            purchaseLinkInfoModel.setInfoCurrency(purchaseOrderModel.getCurrency());
            purchaseLinkInfoModel.setInfoGrossWeight(purchaseOrderModel.getGrossWeight());
            purchaseLinkInfoModel.setInfoLadingBill(purchaseOrderModel.getLadingBill());
            //purchaseLinkInfoModel.setInfoLbxNo(purchaseOrderModel.getLbxNo());
            purchaseLinkInfoModel.setInfoLoadPort(purchaseOrderModel.getLoadPort());
            purchaseLinkInfoModel.setInfoShipDate(purchaseOrderModel.getShipDate());
            purchaseLinkInfoModel.setInfoStandardCaseTeu(purchaseOrderModel.getStandardCaseTeu());
            purchaseLinkInfoModel.setInfoTorrNum(purchaseOrderModel.getTorrNum());
            purchaseLinkInfoModel.setInfoTrainNumber(purchaseOrderModel.getTrainNumber());
            purchaseLinkInfoModel.setInfoUnloadPort(purchaseOrderModel.getUnloadPort());
            //purchaseLinkInfoModel.setInfoTransport(purchaseOrderModel.getTransport());
            purchaseLinkInfoModel.setPurchaseOrderCode(purchaseOrderModel.getCode());
            purchaseLinkInfoModel.setPurchaseOrderId(purchaseOrderModel.getId());
            purchaseLinkInfoModel.setCreateDate(TimeUtils.getNow());
            purchaseLinkInfoModel.setCreateName(user.getName());
            purchaseLinkInfoModel.setCreater(user.getId());
            purchaseLinkInfoModel.setMerchantId(purchaseOrderModel.getMerchantId());
            purchaseLinkInfoModel.setMerchantName(purchaseOrderModel.getMerchantName());
            purchaseLinkInfoModel.setTradeLinkId(tradeLinkId);
            purchaseLinkInfoModel.setStartPointPoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoModel.setOnePoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoModel.setTwoPoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoModel.setThreePoNo(purchaseOrderModel.getPoNo());
            purchaseLinkInfoModel.setFourPoNo(purchaseOrderModel.getPoNo());

            if(purchaseContractModel != null) {
                purchaseLinkInfoModel.setConDeliveryDate(purchaseContractModel.getDeliveryDate());
                purchaseLinkInfoModel.setConDestinationdCn(purchaseContractModel.getDestinationdCn());
                purchaseLinkInfoModel.setConDestinationdEn(purchaseContractModel.getDestinationdEn());
                purchaseLinkInfoModel.setConLoadingCnPort(purchaseContractModel.getLoadingCnPort());
                purchaseLinkInfoModel.setConLoadingEnPort(purchaseContractModel.getLoadingEnPort());
                purchaseLinkInfoModel.setConMeansOfTransportation(purchaseContractModel.getMeansOfTransportation());
                purchaseLinkInfoModel.setConPaymentMethod(purchaseContractModel.getPaymentMethod());
                purchaseLinkInfoModel.setConPaymentMethodText(purchaseContractModel.getPaymentMethodText());
                purchaseLinkInfoModel.setConPaymentMethodTextEn(purchaseContractModel.getPaymentMethodTextEn());
                purchaseLinkInfoModel.setConSpecialAgreement(purchaseContractModel.getSpecialAgreement());
                purchaseLinkInfoModel.setConSpecialAgreementEn(purchaseContractModel.getSpecialAgreementEn());
            }

            TradeLinkConfigModel tradeLinkConfig = tradeLinkConfigDao.searchById(tradeLinkId);

            if(tradeLinkConfig.getStartPointMerchantId() != null) {
                purchaseLinkInfoModel.setStartPointDepotId(purchaseOrderModel.getDepotId());
                purchaseLinkInfoModel.setStartPointDepotName(purchaseOrderModel.getDepotName());
                purchaseLinkInfoModel.setStartPointPoNo(purchaseOrderModel.getPoNo());
            }

            if(tradeLinkConfig.getOneCustomerId() != null) {
                purchaseLinkInfoModel.setOneDepotId(purchaseOrderModel.getDepotId());
                purchaseLinkInfoModel.setOneDepotName(purchaseOrderModel.getDepotName());
                purchaseLinkInfoModel.setOnePoNo(purchaseOrderModel.getPoNo());
            }

            if(tradeLinkConfig.getTwoCustomerId() != null) {
                purchaseLinkInfoModel.setTwoDepotId(purchaseOrderModel.getDepotId());
                purchaseLinkInfoModel.setTwoDepotName(purchaseOrderModel.getDepotName());
                purchaseLinkInfoModel.setTwoPoNo(purchaseOrderModel.getPoNo());
            }

            if(tradeLinkConfig.getThreeCustomerId() != null) {
                purchaseLinkInfoModel.setThreeDepotId(purchaseOrderModel.getDepotId());
                purchaseLinkInfoModel.setThreeDepotName(purchaseOrderModel.getDepotName());
                purchaseLinkInfoModel.setThreePoNo(purchaseOrderModel.getPoNo());
            }

            if(tradeLinkConfig.getFourCustomerId() != null) {
                purchaseLinkInfoModel.setFourDepotId(purchaseOrderModel.getDepotId());
                purchaseLinkInfoModel.setFourDepotName(purchaseOrderModel.getDepotName());
                purchaseLinkInfoModel.setFourPoNo(purchaseOrderModel.getPoNo());
            }

            purchaseLinkInfoId = purchaseLinkInfoDao.save(purchaseLinkInfoModel);

        }else {
            purchaseLinkInfoId = purchaseLinkInfoDTO.getId() ;
        }

        return purchaseLinkInfoId ;
    }

    /**
     * 采购链路销售出库
     * @return
     * @throws Exception
     */
    public List<InvetAddOrSubtractRootJson> saveTradelinkOutWarehouse(List<Long> list, Long userId, String topidealCode, Long merchantId, String outDepotDateStr,
                                                                      String batchNo, Date produceDate, Date overduceDate) throws Exception {

        List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<InvetAddOrSubtractRootJson>() ;

        for (Object id : list) {
            //获取销售订单信息
            SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(id.toString()));
            if (saleOrder == null) {
                throw new RuntimeException("出库失败，销售订单不存在");
            }
            //检查是否已存在销售出库单
            SaleOutDepotModel saleOutDepotTemp = new SaleOutDepotModel();
            saleOutDepotTemp.setSaleOrderId(saleOrder.getId());
            List<SaleOutDepotModel> OutDepotTempList = saleOutDepotDao.list(saleOutDepotTemp);
            if (OutDepotTempList != null && OutDepotTempList.size() > 0) {
                throw new RuntimeException("销售订单" + saleOrder.getCode() + "已存在出库单");
            }
            //获取仓库信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("depotId", saleOrder.getOutDepotId());
            DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

            if (depot == null) {
                throw new RuntimeException("出库失败，仓库不存在");
            }
            //单据状态不是已审核
            if (!DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState())) {
                throw new RuntimeException("出库失败，单据状态不为：已审核");
            }
            // 获取最大的关账日/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(saleOrder.getMerchantId());
            closeAccountsMongo.setDepotId(saleOrder.getOutDepotId());
            closeAccountsMongo.setBuId(saleOrder.getBuId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
            String maxCloseAccountsMonth = "";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
                maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 出库时间(出库时间不可小于关账日期/结转日期)
                if (Timestamp.valueOf(outDepotDateStr).getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                    throw new RuntimeException("出库时间必须大于关账日期/月结日期");
                }
            }
            //获取商品信息
            SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
            saleOrderItem.setOrderId(saleOrder.getId());
            List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(saleOrderItem);

            // 生成销售出库单
            SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
            saleOutDepotModel.setCreateDate(TimeUtils.getNow());
            saleOutDepotModel.setCreater(userId);
            saleOutDepotModel.setCustomerId(saleOrder.getCustomerId());
            saleOutDepotModel.setCustomerName(saleOrder.getCustomerName());
            saleOutDepotModel.setDeliverDate(TimeUtils.parseFullTime(outDepotDateStr));    // 将中转仓出库时间填入相应销售出库单的发货时间字段中
            saleOutDepotModel.setMerchantId(saleOrder.getMerchantId());
            saleOutDepotModel.setMerchantName(saleOrder.getMerchantName());
            saleOutDepotModel.setSaleOrderCode(saleOrder.getCode());
            saleOutDepotModel.setOutDepotId(saleOrder.getOutDepotId());
            saleOutDepotModel.setOutDepotName(saleOrder.getOutDepotName());
            saleOutDepotModel.setPoNo(saleOrder.getPoNo());
            saleOutDepotModel.setSaleOrderId(saleOrder.getId());
            saleOutDepotModel.setSaleType(saleOrder.getBusinessModel());
            saleOutDepotModel.setStatus(DERP_ORDER.SALEORDER_STATE_027);//出库中
            saleOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));
            saleOutDepotModel.setBuId(saleOrder.getBuId());
            saleOutDepotModel.setBuName(saleOrder.getBuName());

            saleOutDepotDao.save(saleOutDepotModel);

            for (SaleOrderItemModel saleItem : saleOrderItemList) {
                SaleOutDepotItemModel item = new SaleOutDepotItemModel();
                item.setCreateDate(TimeUtils.getNow());
                item.setCreater(userId);
                item.setGoodsCode(saleItem.getGoodsCode());
                item.setGoodsId(saleItem.getGoodsId());
                item.setGoodsName(saleItem.getGoodsName());
                item.setGoodsNo(saleItem.getGoodsNo());
                item.setCommbarcode(saleItem.getCommbarcode());    // 标准条码
                item.setBarcode(saleItem.getBarcode());
                item.setTransferNum(saleItem.getNum());
                item.setSaleNum(saleItem.getNum());
                item.setSaleOutDepotId(saleOutDepotModel.getId());
                item.setSaleItemId(saleItem.getId());
                if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())) {
                    item.setTransferBatchNo(batchNo);
                    item.setProductionDate(produceDate);
                    item.setOverdueDate(overduceDate);
                }

                saleOutDepotItemDao.save(item);

                SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
                saleAnalysisModel.setOrderId(saleOrder.getId());
                saleAnalysisModel.setGoodsId(item.getGoodsId());
                saleAnalysisModel.setMerchantId(saleOrder.getMerchantId());    // 公司ID
                saleAnalysisModel.setMerchantName(saleOrder.getMerchantName());    // 公司名称
                saleAnalysisModel.setBuId(saleOrder.getBuId()); // 事业部Id
                saleAnalysisModel.setBuName(saleOrder.getBuName());// 事业部名称
                saleAnalysisModel.setSaleType(saleOrder.getBusinessModel());    // 销售类型
                saleAnalysisModel.setBuId(saleOrder.getBuId()); // 事业部Id
                saleAnalysisModel.setBuName(saleOrder.getBuName());// 事业部名称
                saleAnalysisModel = saleAnalysisDao.searchByModel(saleAnalysisModel);
                if (saleAnalysisModel != null) {
                    saleAnalysisModel.setIsEnd("1");
                    saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
                    saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
                    saleAnalysisModel.setOutDepotNum(saleItem.getNum());
                    saleAnalysisModel.setSurplus(0);
                    saleAnalysisModel.setEndDate(TimeUtils.getNow());
                    saleAnalysisDao.modify(saleAnalysisModel);
                } else {
                    saleAnalysisModel = new SaleAnalysisModel();
                    // 生成差异分析信息
                    saleAnalysisModel.setCreateDate(TimeUtils.getNow());
                    saleAnalysisModel.setCreater(userId);
                    saleAnalysisModel.setCustomerId(saleOrder.getCustomerId());
                    saleAnalysisModel.setCustomerName(saleOrder.getCustomerName());
                    saleAnalysisModel.setEndDate(TimeUtils.getNow());
                    saleAnalysisModel.setGoodsId(saleItem.getGoodsId());
                    saleAnalysisModel.setGoodsName(saleItem.getGoodsName());
                    saleAnalysisModel.setGoodsNo(saleItem.getGoodsNo());
                    saleAnalysisModel.setIsEnd("1");
                    saleAnalysisModel.setOrderCode(saleOrder.getCode());
                    saleAnalysisModel.setOrderId(saleOrder.getId());
                    saleAnalysisModel.setOutDepotNum(saleItem.getNum());
                    saleAnalysisModel.setSaleNum(saleItem.getNum());
                    saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
                    saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
                    saleAnalysisModel.setSurplus(0);
                    saleAnalysisModel.setMerchantId(saleOrder.getMerchantId());    // 公司ID
                    saleAnalysisModel.setMerchantName(saleOrder.getMerchantName());    // 公司名称
                    saleAnalysisModel.setBuId(saleOrder.getBuId()); // 事业部Id
                    saleAnalysisModel.setBuName(saleOrder.getBuName());// 事业部名称
                    saleAnalysisModel.setSaleType(saleOrder.getBusinessModel());    // 销售类型
                    saleAnalysisModel.setBuId(saleOrder.getBuId()); // 事业部Id
                    saleAnalysisModel.setBuName(saleOrder.getBuName());// 事业部名称
                    saleAnalysisModel.setSaleItemId(saleItem.getId());
                    saleAnalysisDao.save(saleAnalysisModel);
                }
            }

            //扣减销售出库库存量
            String saleOutDepotCode = saleOutDepotModel.getCode();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now1 = sdf.format(new Date());
            InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
            invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_ORDER_STOCK_OUT_PUSH_BACK.getTopic());
            invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_ORDER_STOCK_OUT_PUSH_BACK.getTags());
            invetAddOrSubtractRootJson.setMerchantId(saleOrder.getMerchantId().toString());
            invetAddOrSubtractRootJson.setMerchantName(saleOrder.getMerchantName());
            invetAddOrSubtractRootJson.setTopidealCode(topidealCode);

            Map<String, Object> customParam = new HashMap<String, Object>();
            customParam.put("saleId", saleOrder.getId());
            invetAddOrSubtractRootJson.setCustomParam(customParam);

            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", saleOrder.getOutDepotId());// 根据仓库id
            DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);

            invetAddOrSubtractRootJson.setDepotId(saleOrder.getOutDepotId().toString());
            invetAddOrSubtractRootJson.setDepotName(saleOrder.getOutDepotName());
            invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
            invetAddOrSubtractRootJson.setDepotType(mongo.getType());
            invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
            invetAddOrSubtractRootJson.setOrderNo(saleOutDepotCode);
            invetAddOrSubtractRootJson.setBusinessNo(saleOrder.getCode());
            invetAddOrSubtractRootJson.setBuId(String.valueOf(saleOrder.getBuId()));// 事业部
            invetAddOrSubtractRootJson.setBuName(saleOrder.getBuName());
            invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_003);

            // 中转仓出库时根据销售类型设置出库单类型是购销、采购执行，调库存扣减接口时单据类型传采购执行出
            if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrder.getBusinessModel()) ||
                    DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrder.getBusinessModel())) {
                invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
            } else if (DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrder.getBusinessModel())) {
                invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021);
            }

            invetAddOrSubtractRootJson.setSourceDate(now1);// 单据时间
            invetAddOrSubtractRootJson.setDivergenceDate(outDepotDateStr);    // 出入时间
            // 获取当前年月
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
            Date outDepotDate = TimeUtils.StringToDate(outDepotDateStr);
            String outDepotDateStr2 = sdf2.format(outDepotDate);
            invetAddOrSubtractRootJson.setOwnMonth(outDepotDateStr2);// 归属月份
            List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
            for (SaleOrderItemModel item : saleOrderItemList) {
                InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

                invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
                invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
                invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
                invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
                invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());


                if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(mongo.getBatchValidation())) {
                    invetAddOrSubtractGoodsListJson.setBatchNo(batchNo);
                    invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(produceDate, "yyyy-MM-dd"));
                    invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(overduceDate, "yyyy-MM-dd"));
                }

                invetAddOrSubtractGoodsListJson.setIsExpire("1");
                invetAddOrSubtractGoodsListJson.setType("0");
                invetAddOrSubtractGoodsListJson.setNum(item.getNum());
                invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
                invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
            }

            invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

            jsonList.add(invetAddOrSubtractRootJson) ;
        }

        return jsonList ;
    }

    @Override
    public void saveFirstOrderStatusAndIdepot(Long purchaseTradeLinkId,
                                              String batchNo, Date produceDate, Date overdueDate, User user) throws Exception {

        PurchaseLinkInfoModel tradeModel = purchaseLinkInfoDao.searchById(purchaseTradeLinkId);

        Long purchaseOrderId = tradeModel.getPurchaseOrderId();

        PurchaseOrderModel purchaseOrder = new PurchaseOrderModel() ;
        purchaseOrder.setId(purchaseOrderId);
        purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);

        if(purchaseOrder != null) {

            /**若待审核，自动审核*/
            if(DERP_ORDER.PURCHASEORDER_STATUS_001.equals(purchaseOrder.getStatus())) {

                Map<String, Object> depotParams = new HashMap<String, Object>() ;
                depotParams.put("depotId", purchaseOrder.getDepotId()) ;

                DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);

                auditPurchaseOrder(purchaseOrder, depot, user, DERP_ORDER.PURCHASEORDER_STATUS_003);

                purchaseOrder = purchaseOrderDao.searchById(purchaseOrderId);
            }

            /**若已审核，自动入库*/
            if(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())) {

                List<Long> idList = new ArrayList<Long>() ;
                idList.add(purchaseOrder.getId()) ;

                WarehouseOrderRelModel queryModel = new WarehouseOrderRelModel() ;
                queryModel.setPurchaseOrderId(purchaseOrder.getId());

                List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(queryModel);

                //若有采购入库单跳过
                if(!relList.isEmpty()){
                    return ;
                }

                List<InvetAddOrSubtractRootJson> jsonList = saveTradelinkInWarehouse(idList, user, TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),
                        batchNo, produceDate, overdueDate);

//                for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {
//                    commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
//                }

                pushInventory(jsonList) ;
            }


        }

    }

    @Override
    public Map<String, Object> getTradeLinkBatchNo(Long purchaseTradeLinkId) throws SQLException {

        PurchaseLinkInfoModel tradeModel = purchaseLinkInfoDao.searchById(purchaseTradeLinkId);

        Long purchaseOrderId = tradeModel.getPurchaseOrderId();
//
//        PurchaseAnalysisModel queryModel = new PurchaseAnalysisModel() ;
//        queryModel.setOrderId(purchaseOrderId);
//        queryModel = purchaseAnalysisDao.list(queryModel).get(0) ;
        String batchNo = "";
        Date productionDate = null ;
        Date overdueDate = null ;

        WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
        relModel.setPurchaseOrderId(purchaseOrderId);
        List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);
        if(relList != null && relList.size() > 0){
            relModel = relList.get(0);
            List<PurchaseWarehouseBatchModel> warehoustBatchList = purchaseWarehouseBatchDao.getGoodsAndBatch(relModel.getWarehouseId());
            if(warehoustBatchList != null && warehoustBatchList.size() > 0){
                PurchaseWarehouseBatchModel warehouseModel = warehoustBatchList.get(0);
                batchNo = warehouseModel.getBatchNo();
                productionDate = warehouseModel.getProductionDate() ;
                overdueDate = warehouseModel.getOverdueDate() ;
            }
        }

        Map<String, Object> returnMap = new HashMap<String, Object>() ;
        returnMap.put("batchNo", batchNo) ;
        returnMap.put("productionDate", productionDate) ;
        returnMap.put("overdueDate", overdueDate) ;

        return returnMap;
    }

    @Override
    public void createSdOrderCheck(Long id) throws SQLException {

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        if(purchaseOrder == null) {
            throw new DerpException("采购订单不存在") ;
        }

        if(!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
                || DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrder.getStatus()))) {

            String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, purchaseOrder.getStatus());

            throw new DerpException("当前采购单状态为:"+ statusLabel +"，不能创建SD") ;
        }

        if(StringUtils.isNotBlank(purchaseOrder.getWriteOffStatus())) {

            String writeOffStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, purchaseOrder.getWriteOffStatus());

            throw new DerpException("当前采购单红冲状态为:"+ writeOffStatusLabel +"，不能创建SD") ;
        }

        List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(id);

        if(!warehouseList.isEmpty()) {
            PurchaseWarehouseModel purchaseWarehouseModel = warehouseList.get(0);

            // 判断归属日期是否小于 关账日期/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(purchaseOrder.getMerchantId());
            closeAccountsMongo.setDepotId(purchaseOrder.getDepotId());
            closeAccountsMongo.setBuId(purchaseOrder.getBuId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
            String maxCloseAccountsMonth = "";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
                maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 入库日期
                if (purchaseWarehouseModel.getInboundDate().getTime() < Timestamp
                        .valueOf(maxCloseAccountsMonth).getTime()) {
                    throw new DerpException("采购单入库所在月份已关账，不能创建SD");
                }
            }

        }

        SdPurchaseConfigModel querySdPurchaseConfigModel = new SdPurchaseConfigModel() ;
        querySdPurchaseConfigModel.setMerchantId(purchaseOrder.getMerchantId());
        querySdPurchaseConfigModel.setSupplierId(purchaseOrder.getSupplierId());
        querySdPurchaseConfigModel.setBuId(purchaseOrder.getBuId());
        querySdPurchaseConfigModel.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);

        querySdPurchaseConfigModel = sdPurchaseConfigDao.getLastestModel(querySdPurchaseConfigModel) ;

        if(querySdPurchaseConfigModel == null) {
            throw new DerpException("该供应商不存在SD配置");
        }
    }

    @Override
    public boolean toSaleBeforeCheck(Long id) throws SQLException {

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        SaleOrderModel querySaleOrder = new SaleOrderModel() ;
        querySaleOrder.setPoNo(purchaseOrder.getPoNo());
        querySaleOrder.setMerchantId(purchaseOrder.getMerchantId());

        List<SaleOrderModel> saleList = saleOrderDao.list(querySaleOrder);

        if(!saleList.isEmpty()) {
            return true ;
        }

        return false;
    }

    @Override
    public PurchaseOrderModel getCustomerBuIdPrice(String ids, Long customerId, User user) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));

        PurchaseOrderModel queryModel = new PurchaseOrderModel();
        queryModel.setId(Long.valueOf(idList.get(0)));
        queryModel = purchaseOrderDao.getDetails(queryModel);

        if (queryModel == null) {
            throw new DerpException("采购订单不存在");
        }

//        Map<String,Object> map=new HashMap<String, Object>();
//        map.put("depotId", queryModel.getDepotId());
//        DepotInfoMongo depot = depotInfoMongoDao.findOne(map);

        Map<String, PurchaseOrderItemModel> returnItemMap = new HashMap<String, PurchaseOrderItemModel>();
        List<PurchaseOrderItemModel> itemList = queryModel.getItemList();

        //获取客户的税率
        Map<String,Object> customerQueryMap = new HashMap<String, Object>() ;
        customerQueryMap.put("merchantId", user.getMerchantId()) ;
        customerQueryMap.put("customerRelId", customerId) ;
        customerQueryMap.put("status", "1") ;
        CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(customerQueryMap);
        Double rate=0.0;
        if(customer!=null){
            rate=customer.getRate().doubleValue();
        }

        //判断是否启用销售价格
        boolean customerPriceManage = saleOrderService.getSalePriceManage(queryModel.getBuId(), customerId, user);

        for (PurchaseOrderItemModel itemModel : itemList) {
            BigDecimal customerPrice  = itemModel.getPrice();
            BigDecimal amount = itemModel.getAmount();

            //判断是否启用销售价格
            if (customerPriceManage) {
                Map<String, Object> smPriceMap = new HashMap<String, Object>();
                smPriceMap.put("merchantId", queryModel.getMerchantId());
                smPriceMap.put("customerId",customerId);
                smPriceMap.put("buId",queryModel.getBuId());
                Map<String, Object> params2 = new HashMap<String,Object>();
                params2.put("merchandiseId", itemModel.getGoodsId());
                MerchandiseInfoMogo productInfo = merchandiseInfoMogoDao.findOne(params2);

                if(productInfo==null){
                    throw new RuntimeException("没有查到商品的标准条码,商品编码："+ itemModel.getGoodsCode());
                }
                smPriceMap.put("commbarcode", productInfo.getCommbarcode());
                smPriceMap.put("currency", queryModel.getCurrency());
                smPriceMap.put("status", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);

                List<CustomerSalePriceMongo> smList = customerSalePriceMongoDao.findAll(smPriceMap);

                smList = smList.stream().sorted(Comparator.comparing(CustomerSalePriceMongo::getAuditDate).reversed()).collect(Collectors.toList());

                //存储多个销售价格
                List<BigDecimal> salePrice=new ArrayList<>();

                for (CustomerSalePriceMongo tempMongo : smList) {

                    String effectiveDate = tempMongo.getEffectiveDate();
                    String expiryDate = tempMongo.getExpiryDate();

                    //判断销售价格是否在生效日期内
                    if (TimeUtils.parse(effectiveDate, "yyyy-MM-dd").getTime() <= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()
                            && TimeUtils.parse(expiryDate, "yyyy-MM-dd").getTime() >= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()) {
                        salePrice.add(tempMongo.getSalePrice());
                    }
                }

                //若开启了销售价格，并且仓库为海外仓，则获取商品的箱托，单价*箱托
//                if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
//
//                    if (DERP.ORDER_TALLYINGUNIT_00.equals(queryModel.getTallyingUnit())) {
//
//                        Integer torrToUnit = productInfo.getTorrToUnit();
//
//                        if (torrToUnit == null || torrToUnit == 0) {
//                            throw new DerpException("货号：" + productInfo.getGoodsNo() + "托转件为空");
//                        }
//
//                        itemModel.setToUnitNum(new BigDecimal(torrToUnit));
//                    }
//
//                    if (DERP.ORDER_TALLYINGUNIT_01.equals(queryModel.getTallyingUnit())) {
//
//                        Integer boxToUnit = productInfo.getBoxToUnit();
//
//                        if (boxToUnit == null || boxToUnit == 0) {
//                            throw new DerpException("货号：" + productInfo.getGoodsNo() + "箱转件为空");
//                        }
//                        itemModel.setToUnitNum(new BigDecimal(boxToUnit));
//                    }
//                }

                //单价*箱托
                Set<BigDecimal> setPrice = new HashSet<>();
                for(BigDecimal priceList:salePrice){
                    BigDecimal price=priceList;
                    if(itemModel.getToUnitNum()!=null){
                        price=price.multiply(itemModel.getToUnitNum());
                    }
                    setPrice.add(price);
                }
                itemModel.setPriceJOSN(net.sf.json.JSONArray.fromObject(setPrice));
            }


            PurchaseOrderItemModel tempItem = returnItemMap.get(itemModel.getGoodsNo());

            if (tempItem != null) {
                Integer num = tempItem.getNum();
                num += itemModel.getNum();

                BigDecimal price = tempItem.getPrice();
                if(price!=null){
                    amount = price.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                tempItem.setNum(num);
            } else {
                if(customerPrice!=null){
                    amount = customerPrice.multiply(new BigDecimal(itemModel.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                tempItem = itemModel;
            }
            //税额=采购金额(不含税)*税率
            BigDecimal tax=new BigDecimal(0);
            if(amount!=null){
                tax=amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            tempItem.setTax(tax);
            tempItem.setTaxRate(rate);
            tempItem.setPrice(customerPrice);
            tempItem.setAmount(amount);
            tempItem.setPriceJOSN(itemModel.getPriceJOSN());
            returnItemMap.put(itemModel.getGoodsNo(), tempItem);
        }

        List<PurchaseOrderItemModel> tempList = new ArrayList<>(returnItemMap.values()) ;

        queryModel.setItemList(tempList);

        return queryModel;
    }

    @Override
    public Long saveSaleOrder(List<Long> ids, Long customerId,String poNo, String items, User user) throws Exception {

    	PurchaseOrderModel purchaseModel = purchaseOrderDao.searchById(ids.get(0));

//        Map<String, Object> queryDepotMap = new HashMap<String, Object>() ;
//        queryDepotMap.put("depotId", purchaseModel.getDepotId()) ;
//        DepotInfoMongo depot = depotInfoMongoDao.findOne(queryDepotMap);
//        if(depot == null){
//            throw new DerpException("仓库不存在");
//        }

        Map<String, Object> queryCustomerMap = new HashMap<String, Object>() ;
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "1") ;
        queryCustomerMap.put("customerid", customerId) ;

        CustomerInfoMogo tempCustomer = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempCustomer == null) {
            throw new DerpException("客户不存在") ;
        }
        if(StringUtils.isBlank(poNo)) {
            throw new DerpException("po号为空") ;
        }

        SaleOrderModel saleOrder = new SaleOrderModel() ;
        saleOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));
        saleOrder.setMerchantId(purchaseModel.getMerchantId());
        saleOrder.setMerchantName(purchaseModel.getMerchantName());
        saleOrder.setTopidealCode(purchaseModel.getTopidealCode());
        saleOrder.setBuId(purchaseModel.getBuId());
        saleOrder.setCustomerId(customerId);
        saleOrder.setCustomerName(tempCustomer.getName());
        saleOrder.setBuName(purchaseModel.getBuName());
//        saleOrder.setOutDepotId(purchaseModel.getDepotId());
//        saleOrder.setOutDepotName(purchaseModel.getDepotName());
//        saleOrder.setOutDepotCode(depot.getCode());
        saleOrder.setBusinessModel(DERP_ORDER.SALEANALYSIS_SALETYPE_1);
        saleOrder.setPoNo(purchaseModel.getPoNo());
        saleOrder.setCurrency(purchaseModel.getCurrency());
        saleOrder.setOrderType(DERP_ORDER.SALEORDER_ORDERTYPE_2);
        saleOrder.setCarrier(purchaseModel.getCarrier());
        //saleOrder.setTransport(purchaseModel.getTransport());
        saleOrder.setTeu(purchaseModel.getStandardCaseTeu());
        saleOrder.setTrainno(purchaseModel.getTrainNumber());
        saleOrder.setTorusNumber(purchaseModel.getTorrNum());
        saleOrder.setCreateDate(TimeUtils.getNow());
        saleOrder.setCreateName(user.getName());
        saleOrder.setCreater(user.getId());
        saleOrder.setIsSameArea(DERP.ISSAMEAREA_0);
        saleOrder.setState(DERP_ORDER.SALEORDER_STATE_008);
        saleOrder.setPoNo(poNo);;
//        if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
//            saleOrder.setTallyingUnit(purchaseModel.getTallyingUnit());
//        }
        saleOrder.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);

        Long saleId = saleOrderDao.save(saleOrder);

        JSONArray itemArray = JSONArray.parseArray(items) ;

        BigDecimal totalAmount = new BigDecimal(0) ;
        Integer totalNum = 0 ;

//		BigDecimal totalBillWeight = new BigDecimal(0) ;

        for (Object object : itemArray) {
            com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)com.alibaba.fastjson.JSONObject.toJSON(object) ;

            if(json.get("goodsNo") == null){
                throw new DerpException("请补全货号") ;
            }
            String goodsNo = json.getString("goodsNo") ;

            if(json.get("amount") == null) {
                throw new DerpException("请补全销售金额") ;
            }
            Double amount = json.getDouble("amount") ;

            if(json.get("price") == null) {
                throw new DerpException("请补全销售单价") ;
            }
            Double price = json.getDouble("price") ;

            if(json.get("num") == null) {
                throw new DerpException("请补全数量") ;
            }
            Integer num = json.getInteger("num") ;

            Map<String, Object> goodsParams = new HashMap<String, Object>();
            goodsParams.put("goodsNo", goodsNo);
            goodsParams.put("merchantId", purchaseModel.getMerchantId());
            goodsParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsParams);

//			Double grossWeight = goods.getGrossWeight();
//			Double netWeight = goods.getNetWeight();
//			Double grossWeightSum = 0.0 ;
//			Double netWeightSum = 0.0 ;

//			if(grossWeight != null){
//				BigDecimal grossWeightSumBd = new BigDecimal(grossWeight).multiply(new BigDecimal(num)).setScale(5, BigDecimal.ROUND_UP);
//				grossWeightSum = grossWeightSumBd.doubleValue() ;
//			}
//
//			if(netWeight != null){
//				BigDecimal netWeightSumBd = new BigDecimal(netWeight).multiply(new BigDecimal(num)).setScale(5, BigDecimal.ROUND_UP);
//				netWeightSum = netWeightSumBd.doubleValue() ;
//			}

            SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;

            saleOrderItemModel.setOrderId(saleId);
            saleOrderItemModel.setAmount(new BigDecimal(amount));
            saleOrderItemModel.setPrice(new BigDecimal(price));
            saleOrderItemModel.setGoodsId(goods.getMerchandiseId());
            saleOrderItemModel.setGoodsCode(goods.getGoodsCode());
            saleOrderItemModel.setGoodsName(goods.getName());
            saleOrderItemModel.setGoodsNo(goods.getGoodsNo());
            saleOrderItemModel.setBarcode(goods.getBarcode());
            saleOrderItemModel.setNum(num);
//			saleOrderItemModel.setGrossWeight(grossWeight);
//			saleOrderItemModel.setGrossWeightSum(grossWeightSum);
//			saleOrderItemModel.setNetWeight(netWeight);
//			saleOrderItemModel.setNetWeightSum(netWeightSum);
            saleOrderItemModel.setCommbarcode(goods.getCommbarcode());
            saleOrderItemModel.setCreateDate(TimeUtils.getNow());
            if(json.get("tax")!=null){
                saleOrderItemModel.setTax(new BigDecimal(json.get("tax").toString()));
            }
            if(json.get("taxRate")!=null){
                saleOrderItemModel.setTaxRate(Double.valueOf(json.get("taxRate").toString()));
            }

            saleOrderItemDao.save(saleOrderItemModel) ;

            totalNum += num ;
            totalAmount = totalAmount.add(new BigDecimal(amount)) ;
//			totalBillWeight = totalBillWeight.add(new BigDecimal(grossWeightSum)) ;
        }

        SaleOrderModel updateModel = new SaleOrderModel() ;
        updateModel.setId(saleId);
        updateModel.setTotalNum(totalNum);
        updateModel.setTotalAmount(totalAmount);
//		updateModel.setBillWeight(totalBillWeight.doubleValue());

        saleOrderDao.modify(updateModel) ;

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseModel.getCode(), "生成销售订单", null, null);

        return saleId ;
    }

    @Override
    public Map<String, Object> checkInnerMerchantSaleOrder(Long id) throws SQLException {

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        if(purchaseOrder == null) {
            throw new DerpException("采购订单不存在") ;
        }

        Map<String, Object> returnMap = new HashMap<String, Object>() ;

        Long supplierId = purchaseOrder.getSupplierId();

        Map<String, Object> queryCustomerMap = new HashMap<String, Object>() ;
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        queryCustomerMap.put("customerid", supplierId) ;

        CustomerInfoMogo tempSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

        /**若不是内部公司，无须创建销售订单*/
        if(tempSupplier.getInnerMerchantId() == null
                || tempSupplier.getInnerMerchantId() == 0) {

            returnMap.put("flag", false) ;

            return returnMap ;
        }

        /**查询当前公司对应客户，与供应商对应内部公司是否存在销售订单*/
        queryCustomerMap.clear();
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "1") ;
        queryCustomerMap.put("innerMerchantId", purchaseOrder.getMerchantId()) ;
        queryCustomerMap.put("mainId", purchaseOrder.getTopidealCode()) ;

        CustomerInfoMogo tempCustomer = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempCustomer == null) {
            returnMap.put("flag", false) ;

            return returnMap ;
        }

        SaleOrderModel querySaleModel = new SaleOrderModel() ;
        querySaleModel.setMerchantId(tempSupplier.getInnerMerchantId());
        querySaleModel.setCustomerId(tempCustomer.getCustomerid());
        querySaleModel.setPoNo(purchaseOrder.getPoNo());

        List<SaleOrderModel> saleList = saleOrderDao.list(querySaleModel);

        if(saleList.isEmpty()) {
            returnMap.put("flag", true) ;
            returnMap.put("merchantName", tempSupplier.getInnerMerchantName()) ;

            return returnMap ;
        }

        returnMap.put("flag", false) ;

        return returnMap ;
    }

    @Override
    public void saveInnerMerchantSaleOrders(Long id, Long outDepotId, Long buId, User user) throws Exception {

        PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
        purchaseModel.setId(id);
        purchaseModel = purchaseOrderDao.getDetails(purchaseModel);

        if(purchaseModel == null) {
            throw new DerpException("采购订单不存在") ;
        }

        Long supplierId = purchaseModel.getSupplierId();

        Map<String, Object> queryCustomerMap = new HashMap<String, Object>() ;
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        queryCustomerMap.put("customerid", supplierId) ;

        CustomerInfoMogo tempSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

        /**若不是内部公司，无须创建销售订单*/
        if(tempSupplier.getInnerMerchantId() == null) {
            throw new DerpException("供应商不存在") ;
        }

        queryCustomerMap.clear();
        queryCustomerMap.put("merchantId", tempSupplier.getInnerMerchantId()) ;

        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryCustomerMap);

        /**查询当前公司对应客户，与供应商对应内部公司是否存在销售订单*/
        queryCustomerMap.clear();
        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "1") ;
        queryCustomerMap.put("innerMerchantId", purchaseModel.getMerchantId()) ;
        queryCustomerMap.put("mainId", purchaseModel.getTopidealCode()) ;

        CustomerInfoMogo tempCustomer = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempCustomer == null) {
            throw new DerpException("客户列表未查询到内部公司：" + purchaseModel.getMerchantName() ) ;
        }

        //获取仓库信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("depotId", outDepotId);
        DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

        if (depot == null) {
            throw new DerpException("仓库不存在");
        }

        Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>() ;
        depotMerchantRelMap.put("merchantId", tempSupplier.getInnerMerchantId()) ;
        depotMerchantRelMap.put("depotId", depot.getDepotId()) ;
        DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

        if(depotMerchantRel == null) {
            throw new DerpException("商家仓库关联不存在");
        }

        Map<String, Object> relParams = new HashMap<String, Object>() ;
        relParams.put("buId", buId);
        relParams.put("merchantId", tempSupplier.getInnerMerchantId());
        relParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);

        if(merchantBuRelMongo == null) {
            throw new DerpException("公司事业部关联不存在或已禁用");
        }

        relParams = new HashMap<String, Object>() ;
        relParams.put("buId", merchantBuRelMongo.getBuId());
        relParams.put("merchantId", tempSupplier.getInnerMerchantId());
        relParams.put("depotId", depot.getDepotId()) ;
        MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(relParams);

        if(merchantDepotBuRelMongo == null) {
            throw new DerpException("入库仓库未绑定该事业部");
        }

        relParams.clear();
        relParams.put("customerId", tempCustomer.getCustomerid()) ;
        relParams.put("merchantId", merchant.getMerchantId()) ;
        relParams.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(relParams);
        if(customerRel == null) {
            throw new DerpException("客户：" + tempCustomer.getName() + "未与公司：" + merchant.getName() + "关联");
        }
        //检查相同SKU是否存在多条
        if(!checkRepeatGoods(id)){
            throw new DerpException("单据存在多条相同SKU!") ;
        }

        SaleOrderModel saleOrder = new SaleOrderModel() ;
        saleOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));
        saleOrder.setMerchantId(merchant.getMerchantId());
        saleOrder.setMerchantName(merchant.getName());
        saleOrder.setTopidealCode(merchant.getTopidealCode());
        saleOrder.setBuId(merchantBuRelMongo.getBuId());
        saleOrder.setCustomerId(tempCustomer.getCustomerid());
        saleOrder.setCustomerName(tempCustomer.getName());
        saleOrder.setBuName(merchantBuRelMongo.getBuName());
        saleOrder.setOutDepotId(depot.getDepotId());
        saleOrder.setOutDepotName(depot.getName());
        saleOrder.setOutDepotCode(depot.getCode());
        saleOrder.setBusinessModel(DERP_ORDER.SALEANALYSIS_SALETYPE_1);
        saleOrder.setPoNo(purchaseModel.getPoNo());
       // saleOrder.setLbxNo(purchaseModel.getLbxNo());
        saleOrder.setCurrency(purchaseModel.getCurrency());
        saleOrder.setOrderType(DERP_ORDER.SALEORDER_ORDERTYPE_2);
        saleOrder.setBillWeight(purchaseModel.getGrossWeight());
        saleOrder.setCarrier(purchaseModel.getCarrier());
        //saleOrder.setTransport(purchaseModel.getTransport());
        saleOrder.setTeu(purchaseModel.getStandardCaseTeu());
        saleOrder.setTrainno(purchaseModel.getTrainNumber());
        saleOrder.setTorusNumber(purchaseModel.getTorrNum());
        saleOrder.setCreateDate(TimeUtils.getNow());
        saleOrder.setCreateName(user.getName());
        saleOrder.setCreater(user.getId());
        saleOrder.setIsSameArea(DERP.ISSAMEAREA_0);
        saleOrder.setState(DERP_ORDER.PRESALEORDER_STATE_001);
        if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
            saleOrder.setTallyingUnit(purchaseModel.getTallyingUnit());
        }
        saleOrder.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);

        Long saleId = saleOrderDao.save(saleOrder);

        BigDecimal totalAmount = new BigDecimal(0) ;
        Integer totalNum = 0 ;

        List<PurchaseOrderItemModel> itemList = purchaseModel.getItemList();

        for (PurchaseOrderItemModel purchaseItem : itemList) {

            Map<String, Object> goodsParams = new HashMap<String, Object>();
            goodsParams.put("merchandiseId", purchaseItem.getGoodsId());
            MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsParams);

            // 根据商品货号获取商品id
            Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();

            merchandiseInfoParams.put("commbarcode", goods.getCommbarcode());
            merchandiseInfoParams.put("merchantId", merchant.getMerchantId());
            merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            List<MerchandiseInfoMogo> goodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams,outDepotId);
            if (goodsList ==null || goodsList.isEmpty()) {
                throw new DerpException("对应商家商品货号不存在，出库仓"+depot.getName() +"，标准条码：" + goods.getCommbarcode());
            }
            MerchandiseInfoMogo merchandiseInfoMogo = goodsList.get(0);

            SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;

            saleOrderItemModel.setOrderId(saleId);
            saleOrderItemModel.setAmount(purchaseItem.getAmount());
            saleOrderItemModel.setPrice(purchaseItem.getPrice());
            saleOrderItemModel.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
            saleOrderItemModel.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
            saleOrderItemModel.setGoodsName(merchandiseInfoMogo.getName());
            saleOrderItemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
            saleOrderItemModel.setBarcode(merchandiseInfoMogo.getBarcode());
            saleOrderItemModel.setNum(purchaseItem.getNum());
            if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
                saleOrderItemModel.setTallyingUnit(purchaseItem.getUnit());
            }
            saleOrderItemModel.setCommbarcode(goods.getCommbarcode());
            saleOrderItemModel.setCreateDate(TimeUtils.getNow());

            saleOrderItemDao.save(saleOrderItemModel) ;

            totalNum += purchaseItem.getNum() ;
            totalAmount = totalAmount.add(purchaseItem.getAmount()) ;

        }

        SaleOrderModel updateModel = new SaleOrderModel() ;
        updateModel.setId(saleId);
        updateModel.setTotalNum(totalNum);
        updateModel.setTotalAmount(totalAmount);

        saleOrderDao.modify(updateModel) ;

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseModel.getCode(), "生成内部销售订单", null, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void modifyJBLinkPurchaseOrder(Long purchaseTradeLinkId, Map<String, Object> merchantIdOrderIdsMap, User user) throws Exception {
        PurchaseLinkInfoModel purhchaseLinkInfo = purchaseLinkInfoDao.searchById(purchaseTradeLinkId);

        if(purhchaseLinkInfo.getTradeLinkId() == 1L) {

            String bxTopidealCode = "0000138" ;

            Map<String, Object> queryMap = new HashMap<String, Object>() ;

            queryMap.put("topidealCode", bxTopidealCode) ;

            MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMap);

            Map<String, Object> orderIdsMap = (Map<String, Object>)merchantIdOrderIdsMap.get(String.valueOf(merchant.getMerchantId()));
            List<Long> purchaseList = (List<Long>)orderIdsMap.get("purchaseOrderIds");

            for (Long purchaseId : purchaseList) {

                PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
                purchaseModel.setId(purchaseId);
                purchaseModel = purchaseOrderDao.getDetails(purchaseModel) ;

                Map<String, Object> depotMap = new HashMap<String, Object>() ;
                depotMap.put("depotId", purchaseModel.getDepotId()) ;

                DepotInfoMongo depot = depotInfoMongoDao.findOne(depotMap);

                auditPurchaseOrder(purchaseModel, depot, user, DERP_ORDER.PURCHASEORDER_STATUS_003);
            }

        }
    }

    @Override
    public void saveConfirmAmountAdjustment(PurchaseOrderModel model, User user) throws SQLException {

        if(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_1.equals(model.getAmountConfirmStatus())) {
            model.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
        }

        model.setModifyDate(TimeUtils.getNow());
        model.setAmountConfirmerDate(TimeUtils.getNow());
        model.setAmountConfirmer(user.getId());
        model.setAmountConfirmerName(user.getName());
        purchaseOrderDao.modify(model) ;

        model = purchaseOrderDao.searchById(model.getId()) ;
        //确认通过不发邮件
        if(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_2.equals(model.getAmountConfirmStatus())){
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, model.getCode(), "金额确认", "确认通过", "确认通过");
            return ;
        }



        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(model.getBuId());
        emailUserDTO.setBuName(model.getBuName());
        emailUserDTO.setMerchantId(model.getMerchantId());
        emailUserDTO.setMerchantName(model.getMerchantName());
        emailUserDTO.setOrderCode(model.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_11);
        emailUserDTO.setSupplier(model.getSupplierName());
        emailUserDTO.setPoNum(model.getPoNo());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setStatus("金额确认不通过");
        List<String> list=new ArrayList<>();
        list.add(String.valueOf(user.getId()));
        list.add(model.getAmountAdjustmenter()!=null?String.valueOf(model.getAmountAdjustmenter()):null);
        emailUserDTO.setUserId(list);
        emailUserDTO.setAuditorId(model.getAuditer());
        emailUserDTO.setSubmitId(model.getSubmiter()==null?null:Arrays.asList(model.getSubmiter().toString()));
        emailUserDTO.setModifyId(model.getAmountAdjustmenter());
        emailUserDTO.setReviewerId(model.getAmountConfirmer());
        String auditMethod = model.getAuditMethod();
        String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
        emailUserDTO.setAuditMethod(auditMethodLabel);

        JSONObject json = JSONObject.fromObject(emailUserDTO) ;

        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------采购发送邮件发送失败-------", json.toString());
        }

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, model.getCode(), "金额确认", "确认不通过", "确认不通过");
    }

    @Override
    public void toSaleStepBeforeCheck(Long id) throws Exception {
        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        if(!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus()) ||
                DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrder.getStatus()))) {
            throw new DerpException("采购订单状态必须为【已审核】【已入库】");
        }

        //检查相同SKU是否存在多条
        if(!checkRepeatGoods(id)){
            throw new DerpException("单据存在多条相同SKU!") ;
        }
    }

    @Override
    public void saveSaleStepGoodsInfo(Long purchaseTradeLinkId, String goodsInfoJson) throws SQLException {

        PurchaseLinkInfoModel linkModel = new PurchaseLinkInfoModel() ;
        linkModel.setId(purchaseTradeLinkId);
        linkModel.setGoodsInfo(goodsInfoJson);
        linkModel.setModifyDate(TimeUtils.getNow());

        purchaseLinkInfoDao.modify(linkModel) ;
    }

    @Override
    public Map<String, Object> checkTradeLink(Long id) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>() ;
        if(!checkRepeatGoods(id)){
            return returnMap;
        }

        PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(id);

        TradeLinkConfigModel queryModel = new TradeLinkConfigModel() ;
        queryModel.setStartPointMerchantId(purchaseOrder.getMerchantId());
        queryModel.setStartPointBuId(purchaseOrder.getBuId());
        queryModel.setStartSupplierId(purchaseOrder.getSupplierId());

        List<TradeLinkConfigModel> tradeLinkList = tradeLinkConfigDao.list(queryModel);

        if(tradeLinkList.isEmpty()) {
            returnMap.put("flag", false) ;
        }else {
            returnMap.put("flag", true) ;
            returnMap.put("tradeLinkList", tradeLinkList) ;
        }

        return returnMap;
    }

    @Override
    public String getCreateFinancingOrderCheck(List<Long> idList) throws SQLException {

        String currency = "" ;

        String financingNo = "" ;

        for (Long purchaseId : idList) {

            PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseId);

            if(!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
                    || DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrder.getStatus()))) {
                throw new DerpException("采购订单：" + purchaseOrder.getCode() + "不为【已审核】【已完结】状态") ;
            }

            if(StringUtils.isNotBlank(purchaseOrder.getWriteOffStatus())) {

                String writeOffStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, purchaseOrder.getWriteOffStatus());

                throw new DerpException("采购订单：" + purchaseOrder.getCode() + "红冲状态为:" + writeOffStatusLabel +"，不能生成融资代采") ;
            }

            if(StringUtils.isBlank(currency)) {
                currency = purchaseOrder.getCurrency() ;
            }

            if(!currency.equals(purchaseOrder.getCurrency())) {
                throw new DerpException("所选采购订单币种不一致") ;
            }

            if(StringUtils.isNotBlank(purchaseOrder.getFinancingNo())) {

                if(StringUtils.isNotBlank(financingNo)) {
                    financingNo += "," ;
                }

                financingNo += purchaseOrder.getFinancingNo();
            }

        }

        return financingNo ;

    }

    @Override
    public FinancingOrderDTO getFinancingOrder(List<Long> idList) throws Exception {

        String zpxTopidealCode = "1000000544" ;

        FinancingOrderDTO dto = new FinancingOrderDTO() ;
        Map<String , FinancingOrderItemDTO> itemMap = new LinkedHashMap<String, FinancingOrderItemDTO>() ;

        String currency = "" ;
        StringBuffer poSb = new StringBuffer() ;
        StringBuffer purchaseOrderSb = new StringBuffer() ;

        BigDecimal totalPurchaseAmount = new BigDecimal(0) ;

        for (Long purchaseId : idList) {

            PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
            queryModel.setId(purchaseId);

            PurchaseOrderModel details = purchaseOrderDao.getDetails(queryModel);

            //删除附件
            Map<String,Object> params = new HashMap<String,Object>() ;
            params.put("relationCode", dto.getCode()) ;

            attachmentInfoMongoDao.remove(params);

            currency = details.getCurrency() ;

            if(poSb.length() > 0) {
                poSb.append(",") ;
            }

            poSb.append(details.getPoNo()) ;

            if(purchaseOrderSb.length() > 0) {
                purchaseOrderSb.append(",") ;
            }

            purchaseOrderSb.append(details.getCode()) ;

            /**以当前选择的采购订单查询交易链路下创建的卓普信公司主体 的采购订单*/

            /**
             * 查找当前采购订单是否交易链路生成，若存在查询是否为卓普信之后的采购订单
             */

            List<PurchaseOrderItemModel> itemList = details.getItemList();

            PurchaseOrderModel zpModel = new PurchaseOrderModel() ;
            List<PurchaseOrderItemModel> zpxItemList = new ArrayList<>();

            if(StringUtils.isNotBlank(details.getLinkUniueCode())) {

                queryModel = new PurchaseOrderModel() ;
                queryModel.setLinkUniueCode(details.getLinkUniueCode()) ;
                queryModel.setTopidealCode(zpxTopidealCode);

                queryModel = purchaseOrderDao.getDetails(queryModel) ;

                if(queryModel != null
                        && details.getId() > queryModel.getId()) {

                    zpModel = queryModel ;

                    Long zpxOrderId = queryModel.getId();

                    PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
                    queryItemModel.setPurchaseOrderId(zpxOrderId);

                    zpxItemList = purchaseOrderItemDao.list(queryItemModel);
                }
            }

            /**按货号汇总商品*/
            for (PurchaseOrderItemModel item : itemList) {

                PurchaseOrderItemModel tempItem = item ;

                FinancingOrderItemDTO financingOrderItemDTO = itemMap.get(item.getGoodsNo());

                // 根据商品货号获取商品id
                Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
                queryGoodsMap.put("goodsNo", item.getGoodsNo()) ;
                queryGoodsMap.put("merchantId", details.getMerchantId()) ;
                queryGoodsMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

                MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryGoodsMap);

                /**若为空，以当前商家商品查询*/
                if(financingOrderItemDTO == null) {

                    financingOrderItemDTO = new FinancingOrderItemDTO() ;

                    if(merchandise.getCountyId() != null) {
                        Map<String, Object> queryCountryMap = new HashMap<String, Object>() ;
                        queryCountryMap.put("countryOriginId", merchandise.getCountyId()) ;

                        CountryOriginMongo country = countryOriginMongoDao.findOne(queryCountryMap);

                        if(country != null) {
                            financingOrderItemDTO.setOriginCountry(country.getName());
                        }
                    }

                    financingOrderItemDTO.setGoodsId(merchandise.getMerchandiseId());
                    financingOrderItemDTO.setGoodsName(item.getGoodsName());
                    financingOrderItemDTO.setGoodsNo(item.getGoodsNo());
                    financingOrderItemDTO.setQualityGuaranteeDates(merchandise.getShelfLifeDays());
                    financingOrderItemDTO.setPurchaseAmount(new BigDecimal(0));
                    financingOrderItemDTO.setPurchaseQuantity(0);
                    financingOrderItemDTO.setBarcode(item.getBarcode());
                    financingOrderItemDTO.setSpec(merchandise.getSpec());

                    JSONObject json = new JSONObject() ;
                    json.put("merchantId", details.getMerchantId()) ;
                    json.put("goodsId", merchandise.getMerchandiseId()) ;
                    json.put("goodsNo", merchandise.getGoodsNo()) ;
                    json.put("tag", "2") ;
                    json.put("code", queryModel.getCode()) ;

                    try{
                        rocketMQProducer.send(json.toString(), MQErpEnum.SEND_SAPIENCE_GOODS.getTopic(),
                                MQErpEnum.SEND_SAPIENCE_GOODS.getTags()) ;
                    }catch (Exception e){
                        LOGGER.info("同步卓普信商品异常");
                        LOGGER.error(e.getMessage(), e);
                    }

                }

                /**若采购链路存在卓普信采购订单，以卓普采购订单汇总数量和金额*/
                if(zpxItemList != null &&
                        !zpxItemList.isEmpty()){

                    for (PurchaseOrderItemModel zpItem: zpxItemList) {

                        Map<String, Object> queryZPGoodsMap = new HashMap<String, Object>() ;
                        queryZPGoodsMap.put("goodsNo", zpItem.getGoodsNo()) ;
                        queryZPGoodsMap.put("merchantId", zpModel.getMerchantId()) ;
                        queryZPGoodsMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

                        MerchandiseInfoMogo zpMerchandise = merchandiseInfoMogoDao.findOne(queryZPGoodsMap);

                        if(merchandise.getCommbarcode().equals(zpMerchandise.getCommbarcode())){
                            tempItem = zpItem ;

                            break ;
                        }

                    }

                }

                BigDecimal purchaseAmount = financingOrderItemDTO.getPurchaseAmount();
                Integer purchaseQuantity = financingOrderItemDTO.getPurchaseQuantity();

                financingOrderItemDTO.setPurchaseAmount(purchaseAmount.add(tempItem.getAmount()));
                financingOrderItemDTO.setPurchaseQuantity(purchaseQuantity + tempItem.getNum());

                purchaseAmount = financingOrderItemDTO.getPurchaseAmount();
                purchaseQuantity = financingOrderItemDTO.getPurchaseQuantity();

                totalPurchaseAmount = totalPurchaseAmount.add(tempItem.getAmount()) ;

                if(purchaseQuantity != 0) {
                    BigDecimal purchasePrice = purchaseAmount.divide(new BigDecimal(purchaseQuantity), 8, BigDecimal.ROUND_HALF_UP) ;
                    financingOrderItemDTO.setPurchasePrice(purchasePrice);
                }

                itemMap.put(financingOrderItemDTO.getGoodsNo(), financingOrderItemDTO) ;
            }

        }

        dto.setPurchaseAmount(totalPurchaseAmount);
        dto.setMarginPayableAmount(totalPurchaseAmount.multiply(new BigDecimal(0.2)).setScale(6, BigDecimal.ROUND_HALF_UP));
        dto.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DCRZ));
        dto.setInterestCurrency(currency);
        dto.setPoNo(poSb.toString());
        dto.setPurchaseOrders(purchaseOrderSb.toString());
        dto.setItemList(new ArrayList<FinancingOrderItemDTO>(itemMap.values()));

        return dto;
    }

    @Override
    public Map<String, Object> getGoodsProductInfo(Long goodsId) {

        Map<String, Object> returnMap = new HashMap<>() ;

        Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
        queryGoodsMap.put("merchandiseId", goodsId) ;

        MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryGoodsMap);

        if(merchandise.getCountyId() != null) {
            Map<String, Object> queryCountryMap = new HashMap<String, Object>() ;
            queryCountryMap.put("countryOriginId", merchandise.getCountyId()) ;

            CountryOriginMongo country = countryOriginMongoDao.findOne(queryCountryMap);

            if(country != null) {
                returnMap.put("originCountry", country.getName()) ;
            }
        }

        returnMap.put("qualityGuaranteeDates", merchandise.getShelfLifeDays()) ;
        returnMap.put("spec", merchandise.getSpec()) ;

        return returnMap ;

    }

    @Override
    public FinancingOrderDTO confirmSubmitSapience(FinancingOrderDTO dto, User user) throws Exception {

        FinancingApplyRequest request = new FinancingApplyRequest() ;

        request.setMethod(EpassAPIMethodEnum.SAPIENCE_E009_METHOD.getMethod());
        request.setSourceCode("10");
        request.setDebtorCode(user.getTopidealCode());
        request.setDebtorName(user.getMerchantName());
        request.setBillCurrencyCode(dto.getBillCurrencCode());
        request.setBorrowingDays(dto.getBorrowingDays());
        request.setContractNo(dto.getPoNo());
        request.setInStockWarehouseCode(dto.getDepotCode());
        request.setInStockWarehouseName(dto.getDepotName());
        request.setInvestorType(Integer.valueOf(dto.getInvestorType()));
        request.setProductCode(dto.getBusinessModel());
        request.setLoanApplyRemark(dto.getLoanApplyRemark());
        request.setSupplierCode(Integer.valueOf(dto.getSupplierCode()));
        request.setSupplierName(dto.getSupplierName());
        request.setPurchaseDiscountAmount(dto.getPurchaseDiscountAmount());
        request.setWarehouseAddress(dto.getWarehouseAddress());
        request.setPaymentTermName(dto.getPaymentTermName());
        request.setTradeMode(dto.getTradeMode());

        /**若为润佰公司，则将编码默认为2020120101，不取卓志主数据编码*/
        if("1000000626".equals(user.getTopidealCode())) {
            request.setDebtorCode("2020120101");
        }

        List<String> loanApprovalExpdeldates = new ArrayList<String>();
        Timestamp expDeliveryDate = dto.getExpDeliveryDate();

        if(expDeliveryDate != null) {
            String expDeliveryDateStr = TimeUtils.format(expDeliveryDate, "yyyy-MM-dd");

            expDeliveryDateStr = "{\"expDeliveryDate\": "+ expDeliveryDateStr +"}" ;

            loanApprovalExpdeldates.add(expDeliveryDateStr) ;

        }
        request.setLoanApprovalExpdeldates(loanApprovalExpdeldates);

        /**商品列表*/
        String items = dto.getItems();

        List<FinancingOrderItemDTO> itemList = JSONArray.parseArray(items, FinancingOrderItemDTO.class);
        List<Goods> goodsList = new ArrayList<Goods>() ;

        String purchaseOrders = dto.getPurchaseOrders();
        String[] purchaseCodeArr = purchaseOrders.split(",");

        for (FinancingOrderItemDTO financingOrderItemDTO : itemList) {

            Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
            queryGoodsMap.put("merchandiseId", financingOrderItemDTO.getGoodsId()) ;
            queryGoodsMap.put("merchantId", user.getMerchantId()) ;
            queryGoodsMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

            MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryGoodsMap);

            Goods goods = new Goods() ;

            Integer qualityGuaranteeDates = financingOrderItemDTO.getQualityGuaranteeDates();

            if(qualityGuaranteeDates == null
                    || qualityGuaranteeDates <= 0) {
                throw new DerpException("商品货号：" + merchandise.getGoodsNo() + "商品保质期不能为空或必须大于0" ) ;
            }

            if(merchandise.getCountyId() != null) {
                Map<String, Object> queryCountryMap = new HashMap<String, Object>() ;
                queryCountryMap.put("countryOriginId", merchandise.getCountyId()) ;

                CountryOriginMongo country = countryOriginMongoDao.findOne(queryCountryMap);

                if(country != null) {
                    goods.setOriginCountry(country.getName());
                }

            }

            if(merchandise.getProductId() != null) {
                Map<String, Object> queryCommbarcodeMap = new HashMap<String, Object>() ;
                queryCommbarcodeMap.put("brandId", merchandise.getBrandId()) ;

                BrandMongo brandMongo = brandMongoDao.findOne(queryCommbarcodeMap);

                if(brandMongo != null) {
                    financingOrderItemDTO.setExportSuperiorParentBrand(brandMongo.getName());
                }

            }

            if(merchandise.getUnit() != null){
                Map<String, Object> queryUnitMap = new HashMap<String, Object>() ;

                queryUnitMap.put("unitId", merchandise.getUnit()) ;

                UnitMongo unitMongo = unitMongoDao.findOne(queryUnitMap);

                if(unitMongo != null) {
                    financingOrderItemDTO.setExportUnit(unitMongo.getName());
                }
            }

            goods.setGoodsName(merchandise.getName());
            goods.setGoodsNo(merchandise.getGoodsNo());
            goods.setPurchaseAmount(financingOrderItemDTO.getPurchaseAmount());
            goods.setPurchaseQuantity(financingOrderItemDTO.getPurchaseQuantity());
            goods.setQualityGuaranteeDates(qualityGuaranteeDates);

            goodsList.add(goods) ;

            financingOrderItemDTO.setBarcode(merchandise.getBarcode());
            financingOrderItemDTO.setGoodsName(merchandise.getName());
            financingOrderItemDTO.setSpec(merchandise.getSpec());
            financingOrderItemDTO.setCommbarcode(merchandise.getCommbarcode());

            BigDecimal purchaseAmount = financingOrderItemDTO.getPurchaseAmount();
            BigDecimal purchasePrice = purchaseAmount.divide(new BigDecimal(financingOrderItemDTO.getPurchaseQuantity()), 5, BigDecimal.ROUND_UP) ;

            financingOrderItemDTO.setPurchasePrice(purchasePrice);

        }

        dto.setItemList(itemList);
        request.setGoodsList(goodsList);

        Map<String,Object> params = new HashMap<String,Object>() ;
        params.put("relationCode", dto.getCode()) ;
        params.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;

        /**附件列表*/
        List<AttachmentInfoMongo> atts = attachmentInfoMongoDao.findAll(params);
        List<FileUploadEntity> fileUploadEntityList = new ArrayList<FileUploadEntity>() ;

        for (AttachmentInfoMongo attachmentInfoMongo : atts) {

            FileUploadEntity fileUploadEntity = new FileUploadEntity() ;

            fileUploadEntity.setFileName(attachmentInfoMongo.getAttachmentName());
            fileUploadEntity.setFileUrl(attachmentInfoMongo.getAttachmentUrl());

            fileUploadEntityList.add(fileUploadEntity) ;
        }

        request.setFileUploadEntityList(fileUploadEntityList);

        JSONObject requestJson = JSONObject.fromObject(request);

        String info = SapienceUtils.sendSapience(requestJson);

        JSONObject responseJson = (JSONObject)JSONObject.fromObject(info) ;

        if(responseJson.get("result") == null) {

            if(responseJson.get("notes") != null) {
                throw new DerpException(responseJson.getString("notes")) ;
            }

            throw new DerpException("卓普信接口返回异常") ;
        }

        String result = responseJson.getString("result") ;

        JSONObject resultJson = (JSONObject)JSONObject.fromObject(result) ;

        String loanApplyNo = resultJson.getString("loanApplyNo");
        String listStr = resultJson.getString("fileDownloadList") ;

        net.sf.json.JSONArray fileDownloadList = net.sf.json.JSONArray.fromObject(listStr) ;

        String basePath = ApolloUtilDB.orderBasepath+"/tempFile/"+user.getMerchantId();
        ExecutorService pool = Executors.newFixedThreadPool(2) ;
        for (Object obj : fileDownloadList) {
            JSONObject fileJson = (JSONObject)obj ;

            String fileKey = fileJson.getString("fileKey") ;
            String fileName = fileJson.getString("fileName") ;

            /**启用多线程，调用3.10接口下载附件*/
            pool.execute(new Runnable() {

                @Override
                public void run() {

                    FinancingAttachmentDownloadRequest fileRequest = new FinancingAttachmentDownloadRequest() ;

                    fileRequest.setFileKey(fileKey);
                    fileRequest.setFileName(fileName);
                    fileRequest.setMethod(EpassAPIMethodEnum.SAPIENCE_E010_METHOD.getMethod());
                    fileRequest.setSourceCode("10");

                    JSONObject fileRequestJson = JSONObject.fromObject(fileRequest);

                    File tempFile = SapienceUtils.sendSapienceFileRequst(fileRequestJson, basePath, fileName);

                    try {

                        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

                        byte[] fileBytes = null;

                        FileInputStream in = new FileInputStream(tempFile);
                        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);

                        byte[] b = new byte[4096];
                        int n;

                        while ((n = in.read(b)) != -1) {
                            out.write(b, 0, n);
                        }
                        in.close();
                        out.close();
                        fileBytes = out.toByteArray();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
                        jsonObject.put("fileName",fileName);
                        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
                        jsonObject.put("fileExt",ext);
                        jsonObject.put("fileSize",String.valueOf(tempFile.length()));
                        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

                        JSONObject jsonObj = JSONObject.fromObject(resultJson);

                        if(jsonObj.getInt("code") != 200) {
                            throw new DerpException("蓝精灵上传文件异常") ;
                        }

                        for (String purchaseCode : purchaseCodeArr) {
                            //返回文件服务器存储URL
                            String url = jsonObj.getString("url") ;

                            //附件信息写入MongoDB
                            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
                            attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
                            attachmentInfoMongo.setAttachmentSize(tempFile.length()); 		//附件大小
                            attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
                            attachmentInfoMongo.setCreator(user.getId());			//上传者
                            attachmentInfoMongo.setCreatorName(user.getName());
                            attachmentInfoMongo.setCreateDate(new Date());
                            attachmentInfoMongo.setRelationCode(purchaseCode);              //关联订单编码
                            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
                            attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
                            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
                            attachmentInfoMongo.setModule("采购订单");

                            attachmentInfoMongoDao.insert(attachmentInfoMongo);

                        }

                        tempFile.delete() ;

                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                }
            });
        }

        for (String purchaseCode : purchaseCodeArr) {

            PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
            queryModel.setCode(purchaseCode);

            queryModel = purchaseOrderDao.searchByModel(queryModel) ;

            PurchaseOrderModel updateModel = new PurchaseOrderModel() ;
            updateModel.setId(queryModel.getId());
            updateModel.setFinancingNo(loanApplyNo);
            updateModel.setFinancingRemark(dto.getLoanApplyRemark());
            updateModel.setModifyDate(TimeUtils.getNow());

            purchaseOrderDao.modify(updateModel) ;

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseCode, "生成融资代采", null, null);

        }

        return dto ;
    }

    @Override
    public PurchaseOrderModel genSaleOrderByPurchaseIds(List<Long> idList,User user) throws Exception {

        Long buId = null ;
        Long depotId = null ;
        PurchaseOrderModel returnModel = null ;

        Map<String, PurchaseOrderItemModel> returnItemMap = new HashMap<String, PurchaseOrderItemModel>() ;

        for(Long id : idList){

            PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
            queryModel.setId(id);

            queryModel = purchaseOrderDao.getDetails(queryModel) ;

            if(queryModel == null){
                throw new DerpException("采购订单不存在") ;
            }

            if(buId == null){
                buId = queryModel.getBuId() ;
            }

//            if(queryModel.getDepotId() == null){
//                throw new DerpException("采购订单：" + queryModel.getCode() + "入库仓库为空") ;
//            }

            if(depotId == null){
                depotId = queryModel.getDepotId() ;
            }

            if(buId.longValue() != queryModel.getBuId().longValue()){
                throw new DerpException("所选采购订单事业部不一致") ;
            }

//            if(depotId.longValue() != queryModel.getDepotId().longValue()){
//                throw new DerpException("所选采购订单入库仓库不一致") ;
//            }

            if(returnModel == null){
                returnModel = queryModel ;
            }
            //检查相同SKU是否存在多条
            if(!checkRepeatGoods(id)){
                throw new DerpException("单据存在多条相同SKU!") ;
            }
            List<PurchaseOrderItemModel> itemList = queryModel.getItemList();

            for (PurchaseOrderItemModel itemModel: itemList) {

                PurchaseOrderItemModel tempItem = returnItemMap.get(itemModel.getGoodsNo());

                if(tempItem != null){

                    Integer num = tempItem.getNum();
                    num += itemModel.getNum() ;

                    BigDecimal price = tempItem.getPrice();
                    BigDecimal amount = price.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

                    tempItem.setAmount(amount);
                    tempItem.setNum(num);

                }else{
                    tempItem = itemModel ;
                }

                returnItemMap.put(itemModel.getGoodsNo(), tempItem) ;

            }

        }

        List<PurchaseOrderItemModel> tempList = new ArrayList<>(returnItemMap.values()) ;

        returnModel.setItemList(tempList);

        return returnModel;
    }

    @Override
    public Map<String, Object> getSdAmountAdjustmentDetail(Long id) throws SQLException {

        Map<String, Object> map = new HashMap<String, Object>() ;
        // 货号-SD类型 数量展示集合
        Map<String, Object> goodsNoTypeMap = new HashMap<String, Object>() ;

        PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
        purchaseModel.setId(id); ;

        /** 获取采购单*/
        purchaseModel = purchaseOrderDao.getDetails(purchaseModel) ;
        /** 获取采购单明细*/
        List<PurchaseOrderItemModel> itemList = purchaseModel.getItemList();

        //根据商家事业部供应商查询是否存在采购SD配置
        SdPurchaseConfigModel queryModel = new SdPurchaseConfigModel() ;
        queryModel.setMerchantId(purchaseModel.getMerchantId());
        queryModel.setSupplierId(purchaseModel.getSupplierId());
        queryModel.setBuId(purchaseModel.getBuId());
        queryModel.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);

        queryModel = sdPurchaseConfigDao.getLastestModel(queryModel) ;

        if(queryModel != null) {
            SdPurchaseConfigItemModel queryItemModel = new SdPurchaseConfigItemModel() ;
            queryItemModel.setConfigId(queryModel.getId());

            List<SdPurchaseConfigItemModel> configItemList = sdPurchaseConfigItemDao.list(queryItemModel);

            configItemList = configItemList.stream().sorted(Comparator.comparing(SdPurchaseConfigItemModel::getSdConfigSimpleType))
                    .collect(Collectors.toList()) ;

            List<SdPurchaseConfigItemModel> headerList = configItemList.stream().filter(distinctByKey(SdPurchaseConfigItemModel::getSdConfigName))
                    .sorted(Comparator.comparing(SdPurchaseConfigItemModel::getSdConfigSimpleType)).collect(Collectors.toList()) ;

            map.put("header", headerList) ;

            Map<Long, MerchandiseInfoMogo> goodsMap = new HashMap<>();
            BigDecimal totalAmount = new BigDecimal(0) ;
            for (SdPurchaseConfigItemModel sdPurchaseConfigItemModel : configItemList) {

                String configName = sdPurchaseConfigItemModel.getSdConfigName();

                for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {

                    String key = configName + "_" + purchaseOrderItemModel.getGoodsNo() + "_" + purchaseOrderItemModel.getId() ;
                    String keyProportion = configName + "_" + purchaseOrderItemModel.getGoodsNo() + "_proproportion" + "_" + purchaseOrderItemModel.getId() ;


                    BigDecimal amount = purchaseOrderItemModel.getAmount();
                    BigDecimal proportion = sdPurchaseConfigItemModel.getProportion();

                    goodsNoTypeMap.put(keyProportion, proportion) ;

                    amount = amount.multiply(proportion).setScale(2, BigDecimal.ROUND_HALF_UP) ;

                    /**若为多比例查询对应商品标准条码是否匹配，若不匹配，金额为0*/
                    if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(sdPurchaseConfigItemModel.getSdConfigSimpleType())) {
                        MerchandiseInfoMogo goods = goodsMap.get(purchaseOrderItemModel.getGoodsId());
                        if(goods == null){
                            Map<String, Object> goodsQueryMap = new HashMap<String, Object>() ;
                            goodsQueryMap.put("merchandiseId", purchaseOrderItemModel.getGoodsId()) ;
                            goods = merchandiseInfoMogoDao.findOne(goodsQueryMap);
                        }
//                        goodsQueryMap.clear();
//                        goodsQueryMap.put("commbarcode", goods.getCommbarcode()) ;
//                        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(goodsQueryMap);

                        if(StringUtils.isNotBlank(goods.getCommbarcode())) {
                            if(!goods.getCommbarcode().equals(sdPurchaseConfigItemModel.getCommbarcode())) {
                                continue ;
                            }
                        }else {
                            continue ;
                        }
                    }
                    goodsNoTypeMap.put(key, amount.setScale(2, BigDecimal.ROUND_HALF_UP)) ;

                    BigDecimal simpleNameAmount = (BigDecimal)goodsNoTypeMap.get(configName);
                    if(simpleNameAmount == null) {
                        simpleNameAmount = new BigDecimal(0) ;
                    }

                    simpleNameAmount = simpleNameAmount.add(amount) ;
                    goodsNoTypeMap.put(configName, simpleNameAmount.setScale(2, BigDecimal.ROUND_HALF_UP));

                    BigDecimal goodsNoAmount = (BigDecimal)goodsNoTypeMap.get(purchaseOrderItemModel.getGoodsNo()+ "_" + purchaseOrderItemModel.getId());
                    if(goodsNoAmount == null) {
                        goodsNoAmount = new BigDecimal(0) ;
                    }

                    goodsNoAmount = goodsNoAmount.add(amount) ;
                    goodsNoTypeMap.put(purchaseOrderItemModel.getGoodsNo()+ "_" + purchaseOrderItemModel.getId() , goodsNoAmount.setScale(2, BigDecimal.ROUND_HALF_UP));

                    totalAmount = totalAmount.add(amount) ;
                }

            }
            goodsNoTypeMap.put("totalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP)) ;
            map.put("amountMap", goodsNoTypeMap) ;
        }

        map.put("order", purchaseModel) ;
        map.put("itemList", itemList) ;

        return map;
    }

    @Override
    public PurchaseOrderDTO getUnInwarehoustNum(PurchaseOrderDTO dto) throws SQLException {

        List<PurchaseOrderItemModel> itemList = dto.getItemList();

        for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {
            //根据采购id、商品货号 获取已入库数量
            //根据采购明细id获取已入库数量
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("purchaseItemId", purchaseOrderItemModel.getId());
            paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
            List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
            Integer warehouseNum = 0;
            if(numList != null && numList.size() > 0){
                BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
                warehouseNum = queryNum.intValue();
            }
            purchaseOrderItemModel.setUnInwarehouseNum(purchaseOrderItemModel.getNum() - warehouseNum);

        }

        dto.setItemList(itemList);

        return dto;
    }

    private void generateZPXPurchaseInvoice(PurchaseContractModel zpxContractModel,
                                            Long zpSupplierId, FinancingOrderDTO dto,
                                            String basePath, String[] purchaseCodeArr,
                                            User user) throws Exception {

        Map<String,Object> queryCustomerMap = new HashMap<>() ;

        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        queryCustomerMap.put("customerid", zpSupplierId) ;

        CustomerInfoMogo tempSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempSupplier != null){

            dto.setExportSupplierBankAccount(tempSupplier.getBankAccount());
            dto.setExportSupplierBeneficiaryName(tempSupplier.getBeneficiaryName());
            dto.setExportSupplierName(tempSupplier.getChinaName());
            dto.setExportSupplierEnName(tempSupplier.getEnName());
            dto.setExportSupplierBankAddress(tempSupplier.getBankAddress());
            dto.setExportSupplierSwiftCode(tempSupplier.getSwiftCode());
            dto.setExportSupplierDepositBank(tempSupplier.getDepositBank()) ;
        }

        dto.setExportTransportation("■CIF □CFR □FOB □DAP □EXW □CIP □海运 □空运 □陆运");
        dto.setExportTransportationEn("■CIF □CFR □FOB □DAP □EXW □CIP □BY SEA □BY AIR □BY LAND");
        dto.setExportDestination("广州南沙");
        dto.setExportDestinationEn("GuangZhouNanSha");
        dto.setExportLoadingPort("广州南沙");
        dto.setExportLoadingPortEn("GuangZhouNanSha");

        if(zpxContractModel != null) {

            dto.setExportDestination(zpxContractModel.getDestinationdCn() == null ? "广州南沙" : zpxContractModel.getDestinationdCn());
            dto.setExportDestinationEn(zpxContractModel.getDestinationdEn() == null ? "GuangZhouNanSha" : zpxContractModel.getDestinationdEn());
            dto.setExportLoadingPort(zpxContractModel.getLoadingCnPort() == null ? "广州南沙" : zpxContractModel.getLoadingCnPort());
            dto.setExportLoadingPortEn(zpxContractModel.getLoadingEnPort() == null ? "GuangZhouNanSha" : zpxContractModel.getLoadingEnPort());

            Timestamp deliveryDate = zpxContractModel.getDeliveryDate();

            if(deliveryDate != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM.dd , yyyy", Locale.ENGLISH);

                dto.setExportDeliveryDate(TimeUtils.format(deliveryDate, "yyyy 年  MM月 dd日"));
                dto.setExportDeliveryDateEn(sdf.format(deliveryDate));
            }

            String meansOfTransportation = zpxContractModel.getMeansOfTransportation();

            if(StringUtils.isNotBlank(meansOfTransportation)) {

                String exportTransportation = dto.getExportTransportation();
                String exportTransportationEn = dto.getExportTransportationEn();

                exportTransportation = exportTransportation.replaceAll("■", "□") ;
                exportTransportationEn = exportTransportationEn.replaceAll("■", "□") ;

                String[] arr = meansOfTransportation.split(",");

                for (String tempStr : arr) {

                    exportTransportationEn = exportTransportationEn.replaceAll("□" + tempStr, "■" + tempStr) ;

                    String chinese = tempStr ;

                    if("BY SEA".equals(tempStr)) {
                        chinese = "海运" ;
                    }else if("BY AIR".equals(tempStr)) {
                        chinese = "空运" ;
                    }else if("BY LAND".equals(tempStr)) {
                        chinese = "陆运" ;
                    }

                    exportTransportation = exportTransportation.replaceAll("□" + chinese, "■" + chinese) ;
                }

                dto.setExportTransportation(exportTransportation);
                dto.setExportTransportationEn(exportTransportationEn);
            }

        }

        int exportIndex = 1 ;
        BigDecimal totalNum = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0) ;

        List<FinancingOrderItemDTO> itemList = dto.getItemList();

        for (FinancingOrderItemDTO item : itemList) {

            BigDecimal purchasePrice = item.getPurchasePrice();
            BigDecimal purchaseAmount = item.getPurchaseAmount();

            purchasePrice = purchasePrice.setScale(3, BigDecimal.ROUND_HALF_UP) ;
            purchaseAmount = purchaseAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

            item.setPurchasePrice(purchasePrice);
            item.setPurchaseAmount(purchaseAmount);

            //设置导出序号
            item.setExportIndex(String.valueOf(exportIndex ++));
            totalNum = totalNum.add(new BigDecimal(item.getPurchaseQuantity())) ;
            totalAmount = totalAmount.add(item.getPurchaseAmount()) ;

        }

        dto.setExportAmountIndex(String.valueOf(exportIndex));
        dto.setExportPurchaseAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dto.setExportPurchaseNum(totalNum.toString());
        dto.setExportPurchaseDate(TimeUtils.format(new Date(), "yyyy年MM月dd日"));
        dto.setPurchaseAmount(dto.getPurchaseAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setPurchaseDiscountAmount(dto.getPurchaseDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setItemList(itemList);

        Workbook workbook = DownloadExcelUtil.exportTemplateExcel("PURCHASEINVOCIE", dto);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        byte[] fileBytes = bos.toByteArray();

        InputStream is = new ByteArrayInputStream(fileBytes);

        File tempFile = new File(basePath + File.separator + "采购订单发票（至卓普信）" + ".xlsx") ;
        FileOutputStream fos = new FileOutputStream(tempFile) ;

        byte[] buffer = new byte[1024];
        int ch = 0;
        while ((ch = is.read(buffer)) != -1) {
            fos.write(buffer, 0, ch);
        }

        fos.close();
        is.close();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", "采购订单发票（至卓普信）.xlsx");
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt", "xlsx");
        jsonObject.put("fileSize",String.valueOf(tempFile.length()));
        String invoiceResultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(invoiceResultJson);

        if(jsonObj.getInt("code") != 200) {
            throw new DerpException("蓝精灵上传文件异常") ;
        }

        for (String purchaseCode : purchaseCodeArr) {
            //返回文件服务器存储URL
            String url = jsonObj.getString("url") ;

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
            attachmentInfoMongo.setAttachmentName("采购订单发票（至卓普信）.xlsx"); 		//附件名
            attachmentInfoMongo.setAttachmentSize(tempFile.length()); 		//附件大小
            attachmentInfoMongo.setAttachmentType("xlsx");		       	//附件类型
            attachmentInfoMongo.setCreator(user.getId());			//上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(purchaseCode);              //关联订单编码
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule("采购订单");

            attachmentInfoMongoDao.insert(attachmentInfoMongo);

        }

        tempFile.delete() ;
    }

    private void generateBXPurchaseInvoice(FinancingOrderDTO dto,
                                           String basePath,String[] purchaseCodeArr,
                                           User user) throws Exception {

        int exportIndex = 1 ;
        BigDecimal totalNum = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0) ;

        List<FinancingOrderItemDTO> itemList = dto.getItemList();

        for (FinancingOrderItemDTO item : itemList) {

            BigDecimal purchasePrice = item.getPurchasePrice();
            BigDecimal purchaseAmount = item.getPurchaseAmount();

            purchasePrice = purchasePrice.setScale(2, BigDecimal.ROUND_HALF_UP) ;
            purchaseAmount = purchaseAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

            item.setPurchasePrice(purchasePrice);
            item.setPurchaseAmount(purchaseAmount);

            //设置导出序号
            item.setExportIndex(String.valueOf(exportIndex ++));
            totalNum = totalNum.add(new BigDecimal(item.getPurchaseQuantity())) ;
            totalAmount = totalAmount.add(item.getPurchaseAmount()) ;

        }

        dto.setExportAmountIndex(String.valueOf(exportIndex));
        dto.setExportPurchaseAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dto.setExportPurchaseNum(totalNum.toString());
        dto.setExportPurchaseDate(TimeUtils.format(new Date(), "yyyy年MM月dd日"));
        dto.setPurchaseAmount(dto.getPurchaseAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setPurchaseDiscountAmount(dto.getPurchaseAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setExportPurchaseAmountCNStr(dto.getBillCurrencCodeLabel() + " " + NumberToCN.number2CNMontrayUnit(dto.getPurchaseAmount()));
        dto.setExportCreateDateStr(TimeUtils.format(new Date(), "yyyyMMdd"));
        dto.setItemList(itemList);

        Workbook workbook = DownloadExcelUtil.exportTemplateExcel("PURCHASEINVOCIEBX", dto);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        byte[] fileBytes = bos.toByteArray();

        InputStream is = new ByteArrayInputStream(fileBytes);

        File tempFile = new File(basePath + File.separator + "宝信采购订单发票.xlsx") ;
        FileOutputStream fos = new FileOutputStream(tempFile) ;

        byte[] buffer = new byte[1024];
        int ch = 0;
        while ((ch = is.read(buffer)) != -1) {
            fos.write(buffer, 0, ch);
        }

        fos.close();
        is.close();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", "宝信采购订单发票.xlsx");
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt", "xlsx");
        jsonObject.put("fileSize",String.valueOf(tempFile.length()));
        String invoiceResultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(invoiceResultJson);

        if(jsonObj.getInt("code") != 200) {
            throw new DerpException("蓝精灵上传文件异常") ;
        }

        for (String purchaseCode : purchaseCodeArr) {
            //返回文件服务器存储URL
            String url = jsonObj.getString("url") ;

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
            attachmentInfoMongo.setAttachmentName("宝信采购订单发票.xlsx"); 		//附件名
            attachmentInfoMongo.setAttachmentSize(tempFile.length()); 		//附件大小
            attachmentInfoMongo.setAttachmentType("xlsx");		       	//附件类型
            attachmentInfoMongo.setCreator(user.getId());			//上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(purchaseCode);              //关联订单编码
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule("采购订单");

            attachmentInfoMongoDao.insert(attachmentInfoMongo);

        }

        tempFile.delete() ;
    }

    private void generateZPXGYXPurchaseInvoice(PurchaseContractModel zpxContractModel,
                                               Long zpxSupplierId,
                                               List<PurchaseOrderModel> zpxLastPurchaseOrderList, FinancingOrderDTO dto,
                                               String basePath, String[] purchaseCodeArr,
                                               User user) throws Exception {

        Map<String,Object> queryCustomerMap = new HashMap<>() ;

        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        queryCustomerMap.put("customerid", zpxSupplierId) ;

        CustomerInfoMogo tempZPXSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempZPXSupplier != null) {

            dto.setExportSupplierName(tempZPXSupplier.getChinaName());
            dto.setExportSupplierEnName(tempZPXSupplier.getEnName());

        }

        PurchaseOrderModel zpxLastPurchaseOrder = zpxLastPurchaseOrderList.get(0);

        queryCustomerMap = new HashMap<>() ;

        queryCustomerMap.put("status", "1") ;
        queryCustomerMap.put("cusType", "2") ;
        queryCustomerMap.put("customerid", zpxLastPurchaseOrder.getSupplierId()) ;

        CustomerInfoMogo tempZPXLastSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

        if(tempZPXLastSupplier != null){

            dto.setExportJFSupplierEnName(tempZPXLastSupplier.getEnName());
            dto.setExportJFSupplierName(tempZPXLastSupplier.getChinaName());
            dto.setExportSupplierBankAccount(tempZPXLastSupplier.getBankAccount());
            dto.setExportSupplierBeneficiaryName(tempZPXLastSupplier.getBeneficiaryName());
            dto.setExportSupplierBankAddress(tempZPXLastSupplier.getBankAddress());
            dto.setExportSupplierSwiftCode(tempZPXLastSupplier.getSwiftCode());
            dto.setExportSupplierDepositBank(tempZPXLastSupplier.getDepositBank()) ;

        }

        dto.setExportTransportation("■CIF □CFR □FOB □DAP □EXW □CIP □海运 □空运  □陆运");
        dto.setExportTransportationEn("■CIF □CFR □FOB □DAP □EXW □CIP □BY SEA □BY AIR □BY LAND");
        dto.setExportDestination("广州南沙");
        dto.setExportDestinationEn("GuangZhouNanSha");
        dto.setExportLoadingPort("广州南沙");
        dto.setExportLoadingPortEn("GuangZhouNanSha");

        if(zpxContractModel != null) {

            dto.setExportDestination(zpxContractModel.getDestinationdCn() == null ? "广州南沙" : zpxContractModel.getDestinationdCn());
            dto.setExportDestinationEn(zpxContractModel.getDestinationdEn() == null ? "GuangZhouNanSha" : zpxContractModel.getDestinationdEn());
            dto.setExportLoadingPort(zpxContractModel.getLoadingCnPort() == null ? "广州南沙" : zpxContractModel.getLoadingCnPort());
            dto.setExportLoadingPortEn(zpxContractModel.getLoadingEnPort() == null ? "GuangZhouNanSha" : zpxContractModel.getLoadingEnPort());

            Timestamp deliveryDate = zpxContractModel.getDeliveryDate();

            if(deliveryDate != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM.dd , yyyy", Locale.ENGLISH);

                dto.setExportDeliveryDate(TimeUtils.format(deliveryDate, "yyyy 年  MM月 dd日"));
                dto.setExportDeliveryDateEn(sdf.format(deliveryDate));
            }

            String meansOfTransportation = zpxContractModel.getMeansOfTransportation();

            if(StringUtils.isNotBlank(meansOfTransportation)) {

                String exportTransportation = dto.getExportTransportation();
                String exportTransportationEn = dto.getExportTransportationEn();

                exportTransportation = exportTransportation.replaceAll("■", "□") ;
                exportTransportationEn = exportTransportationEn.replaceAll("■", "□") ;

                String[] arr = meansOfTransportation.split(",");

                for (String tempStr : arr) {

                    exportTransportationEn = exportTransportationEn.replaceAll("□" + tempStr, "■" + tempStr) ;

                    String chinese = tempStr ;

                    if("BY SEA".equals(tempStr)) {
                        chinese = "海运" ;
                    }else if("BY AIR".equals(tempStr)) {
                        chinese = "空运" ;
                    }else if("BY LAND".equals(tempStr)) {
                        chinese = "陆运" ;
                    }

                    exportTransportation = exportTransportation.replaceAll("□" + chinese, "■" + chinese) ;
                }

                dto.setExportTransportation(exportTransportation);
                dto.setExportTransportationEn(exportTransportationEn);
            }

        }

        int exportIndex = 1 ;
        BigDecimal totalNum = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0) ;

        List<FinancingOrderItemDTO> itemList = dto.getItemList();

        for (FinancingOrderItemDTO item : itemList) {

            String commbarcode = item.getCommbarcode();

            Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
            queryGoodsMap.put("commbarcode", commbarcode) ;
            queryGoodsMap.put("merchantId", zpxLastPurchaseOrder.getMerchantId()) ;
            queryGoodsMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

            List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findAll(queryGoodsMap);

            item.setPurchaseAmount(new BigDecimal(0));

            //汇总对应供应商采购订单金额
            for (PurchaseOrderModel tempModel : zpxLastPurchaseOrderList) {

                LOGGER.info("-------------------卓普信采购订单数量：-------------"+zpxLastPurchaseOrderList.size());
                LOGGER.info("-------------------卓普信采购订单：-------------"+zpxLastPurchaseOrderList);
                LOGGER.info("-------------------商品数量-------------"+merchandiseList.size());

                for (MerchandiseInfoMogo merchandise : merchandiseList) {

                    PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
                    queryItemModel.setPurchaseOrderId(tempModel.getId());
                    queryItemModel.setGoodsId(merchandise.getMerchandiseId());

                    List<PurchaseOrderItemModel> queryItemList = purchaseOrderItemDao.list(queryItemModel) ;

                    if(queryItemList == null || queryItemList.size() < 1) {
                        continue ;
                    }



                    BigDecimal amount = queryItemList.stream().map(PurchaseOrderItemModel :: getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal purchaseAmount = item.getPurchaseAmount() ;
                    purchaseAmount = purchaseAmount.add(amount) ;

                    item.setPurchaseAmount(purchaseAmount);

                    LOGGER.info("-------------------卓普信采购订单表体金额：-------------"+item.getPurchaseAmount());
                }

            }

            BigDecimal purchaseAmount = item.getPurchaseAmount();
            BigDecimal purchasePrice = purchaseAmount.divide(new BigDecimal(item.getPurchaseQuantity()), 3, BigDecimal.ROUND_HALF_UP);

            purchaseAmount = purchaseAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

            item.setPurchasePrice(purchasePrice);
            item.setPurchaseAmount(purchaseAmount);

            //设置导出序号
            item.setExportIndex(String.valueOf(exportIndex ++));
            totalNum = totalNum.add(new BigDecimal(item.getPurchaseQuantity())) ;
            totalAmount = totalAmount.add(item.getPurchaseAmount()) ;

        }

        dto.setExportAmountIndex(String.valueOf(exportIndex));
        dto.setExportPurchaseAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dto.setExportPurchaseNum(totalNum.toString());
        dto.setExportPurchaseDate(TimeUtils.format(new Date(), "yyyy年MM月dd日"));
        dto.setPurchaseAmount(dto.getPurchaseAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setPurchaseDiscountAmount(dto.getPurchaseDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP));

        dto.setItemList(itemList);

        Workbook workbook = DownloadExcelUtil.exportTemplateExcel("PURCHASEINVOCIEGYS", dto);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        byte[] fileBytes = bos.toByteArray();

        InputStream is = new ByteArrayInputStream(fileBytes);

        File tempFile = new File(basePath + File.separator + "采购订单发票（供应商端）" + ".xlsx") ;
        FileOutputStream fos = new FileOutputStream(tempFile) ;

        byte[] buffer = new byte[1024];
        int ch = 0;
        while ((ch = is.read(buffer)) != -1) {
            fos.write(buffer, 0, ch);
        }

        fos.close();
        is.close();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", "采购订单发票（供应商端）.xlsx");
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt", "xlsx");
        jsonObject.put("fileSize",String.valueOf(tempFile.length()));
        String invoiceResultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(invoiceResultJson);

        if(jsonObj.getInt("code") != 200) {
            throw new DerpException("蓝精灵上传文件异常") ;
        }

        for (String purchaseCode : purchaseCodeArr) {
            //返回文件服务器存储URL
            String url = jsonObj.getString("url") ;

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
            attachmentInfoMongo.setAttachmentName("采购订单发票（供应商端）.xlsx"); 		//附件名
            attachmentInfoMongo.setAttachmentSize(tempFile.length()); 		//附件大小
            attachmentInfoMongo.setAttachmentType("xlsx");		       	//附件类型
            attachmentInfoMongo.setCreator(user.getId());			//上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(purchaseCode);              //关联订单编码
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule("采购订单");

            attachmentInfoMongoDao.insert(attachmentInfoMongo);

        }

        tempFile.delete() ;
    }

    @Override
    public void delPurchaseFinanceInfo(String purchaseOrders) throws SQLException {

        String[] purchaseCodeArr = purchaseOrders.split(",");

        Map<String, Object> attMap = new HashMap<>() ;

        for (String purchaseCode : purchaseCodeArr) {

            PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
            queryModel.setCode(purchaseCode);

            queryModel = purchaseOrderDao.searchByModel(queryModel) ;

            if(StringUtils.isBlank(queryModel.getFinancingNo())) {
                continue ;
            }

            PurchaseOrderModel updateModel = new PurchaseOrderModel() ;
            updateModel.setId(queryModel.getId());

            purchaseOrderDao.updateFinanceInfo(updateModel) ;

            attMap.put("relationCode", queryModel.getCode()) ;

            attachmentInfoMongoDao.remove(attMap);
        }

    }

    @Override
    public void generateDeclareOrderCheck(List<Long> list, User user) throws SQLException {

        // 根据id集合查询采购订单集合
        List<PurchaseOrderModel> result = purchaseOrderDao.getSupplierIdAndDepotId(list);

        Long depot_id = null;// 仓库id
        Long supplier_id = null;// 供应商id
        //String pay_subject = null;
        Long buId = null ;

        for (PurchaseOrderModel purchaseOrderModel : result) {

            // 判断状态为 已审核
            if (!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())
                    || DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrderModel.getStatus()))) {
                throw new DerpException("采购订单状态必须为【已审核】【部分入库】") ;
            }

            if (DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_3.equals(purchaseOrderModel.getBusinessModel())) {
                throw new DerpException("采购订单业务模式不能为【采销执行】") ;
            }

            if(depot_id == null) {
                depot_id = purchaseOrderModel.getDepotId();
            }

            if(supplier_id == null) {
                supplier_id = purchaseOrderModel.getSupplierId();
            }

			/*
			 * if(pay_subject == null) { pay_subject = purchaseOrderModel.getPaySubject(); }
			 */

            if(buId == null) {
                buId = purchaseOrderModel.getBuId();
            }

            if (supplier_id.longValue() != purchaseOrderModel.getSupplierId()) {
                throw new DerpException("供应商必须一致") ;
            }

			/*
			 * if(!pay_subject.equals(purchaseOrderModel.getPaySubject())){ throw new
			 * DerpException("付款主体必须一致") ; }
			 */

            if(buId.longValue() != purchaseOrderModel.getBuId().longValue()){
                throw new DerpException("事业部必须一致") ;
            }

        }
    }

    @Override
    public void generateZPXInvoice(FinancingOrderDTO dto, User user) throws Exception {

        /**取采购单号保存融资单号和对应附件*/
        String zpxTopidealCode = "1000000544" ;
        //卓普信公司主体下采购订单的供应商
        Long zpSupplierId = null ;
        //卓普信公司主体下采购合同
        PurchaseContractModel zpxContractModel = new PurchaseContractModel();
        //卓普信公司上级采购订单
        List<PurchaseOrderModel> zpxLastPurchaseOrderList = new ArrayList<>() ;

        String basePath = ApolloUtilDB.orderBasepath+"/tempFile/"+user.getMerchantId();

        String purchaseOrders = dto.getPurchaseOrders();
        String[] purchaseCodeArr = purchaseOrders.split(",");

        for (String purchaseCode : purchaseCodeArr) {

            PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
            queryModel.setCode(purchaseCode);

            queryModel = purchaseOrderDao.searchByModel(queryModel) ;

            /**获取卓普信的供应商
             *
             * 判断是否存在链路编码
             */

            if(StringUtils.isNotBlank(queryModel.getLinkUniueCode())){

                PurchaseOrderModel zpxQueryModel = new PurchaseOrderModel() ;
                zpxQueryModel.setLinkUniueCode(queryModel.getLinkUniueCode()) ;
                zpxQueryModel.setTopidealCode(zpxTopidealCode);

                zpxQueryModel = purchaseOrderDao.getDetails(zpxQueryModel) ;

                if(zpxQueryModel != null
                        && queryModel.getId() > zpxQueryModel.getId()) {

                    zpSupplierId = zpxQueryModel.getSupplierId() ;

                    /**查找卓普信对应采购订单合同*/

                    PurchaseContractModel queryContractModel = new PurchaseContractModel();

                    queryContractModel.setOrderId(zpxQueryModel.getId());
                    zpxContractModel = purchaseContractDao.searchByModel(queryContractModel) ;

                }

            }

            if(zpSupplierId == null){
                zpSupplierId = queryModel.getSupplierId() ;
            }else {

                Map<String,Object> queryCustomerMap = new HashMap<>() ;

                queryCustomerMap.put("status", "1") ;
                queryCustomerMap.put("cusType", "2") ;
                queryCustomerMap.put("customerid", zpSupplierId) ;

                CustomerInfoMogo tempSupplier = customerInfoMongoDao.findOne(queryCustomerMap);

                /**判断卓普信对应供应商是否内部客户，若是查询对应公司下采购订单*/
                if(DERP_SYS.CUSTOMERINFO_TYPE_1.equals(tempSupplier.getType())) {

                    PurchaseOrderModel zpxLastQueryModel = new PurchaseOrderModel() ;
                    zpxLastQueryModel.setPoNo(queryModel.getPoNo()) ;
                    zpxLastQueryModel.setMerchantId(tempSupplier.getInnerMerchantId());

                    List<PurchaseOrderModel> tempList = purchaseOrderDao.list(zpxLastQueryModel);

                    //过滤已删除单据
                    tempList = tempList.stream().filter(temp -> !DERP.DEL_CODE_006.equals(temp.getStatus())).collect(Collectors.toList()) ;

                    if(!tempList.isEmpty()) {

                        PurchaseOrderModel zpxLastPurchaseOrder =new PurchaseOrderModel();
                        for(PurchaseOrderModel purchaseOrderModel:tempList){
                            zpxLastPurchaseOrder=purchaseOrderModel;
                        }

                        //PurchaseOrderModel zpxLastPurchaseOrder = tempList.get(0) ;

                        zpxLastPurchaseOrderList.add(zpxLastPurchaseOrder) ;
                    }

                }

            }
        }

        LOGGER.info("------------------zpxLastPurchaseOrderList集合---------------"+zpxLastPurchaseOrderList);

        if("0000138".equals(user.getTopidealCode())) {

            //生成宝信采购订单发票
            generateBXPurchaseInvoice(dto, basePath, purchaseCodeArr, user);

        }else {
            //生成至卓普信采购订单发票
            generateZPXPurchaseInvoice(zpxContractModel, zpSupplierId, dto,  basePath,
                    purchaseCodeArr, user);
            //判断是否存在卓普信上级采购订单
            if(!zpxLastPurchaseOrderList.isEmpty()) {

                generateZPXGYXPurchaseInvoice(zpxContractModel, zpSupplierId,
                        zpxLastPurchaseOrderList,
                        dto, basePath, purchaseCodeArr, user);

            }
        }
    }


    /**
     * 反审操作
     * @param id
     * @param remark
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getAuditCounterTrial(Long id, String remark,User user) throws SQLException {
        Map<String, Object> map=new HashMap<>();

        PurchaseOrderModel purchaseOrderModel=purchaseOrderDao.searchById(id);
        if(purchaseOrderModel==null){
            map.put("code","01");
            map.put("message","采购订单不存在");
            return map;
        }

        if(!(DERP_ORDER.PURCHASEORDER_ISGENERATE_0.equals(purchaseOrderModel.getIsGenerate())
                &&DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus()))){
            map.put("code","01");
            map.put("message","采购订单状态仅对已审核状态，且未生成预申报单的进行反审");
            return map;
        }

        WarehouseOrderRelModel WarehouseOrderRelModel=new WarehouseOrderRelModel();
        WarehouseOrderRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
        List<WarehouseOrderRelModel> list=warehouseOrderRelDao.list(WarehouseOrderRelModel);
        if(list.size()>0){
            map.put("code","01");
            map.put("message","采购订单状态已入库，不能进行反审操作");
            return map;
        }

        //修改采购单状态为“待提交”
        purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
        purchaseOrderModel.setModifyDate(TimeUtils.getNow());
        purchaseOrderModel.setModifier(user.getId());
        purchaseOrderDao.modify(purchaseOrderModel);

        //删除关联的采购勾稽数据
//        List ids=new ArrayList<>();
//        ids.add(purchaseOrderModel.getId());
//        purchaseAnalysisDao.deletePurchaseAnalysisByOrderId(ids);

        /**记录操作日志***/
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "反审", null, remark);

        map.put("code","00");
        map.put("message","操作成功");
        return map;
    }
    /**
	 * 批量驳回
	 */
	@Override
	public void batchRejection(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		List<String> sendEmailList=new ArrayList<String>();
		for(Long id : idList) {
			PurchaseOrderModel model = purchaseOrderDao.searchById(id);
			if(model == null){
				throw new RuntimeException("采购订单不存在");
			}

			if(!DERP_ORDER.PURCHASEORDER_STATUS_002.equals(model.getStatus())){
				throw new RuntimeException("采购订单:"+model.getCode()+"状态不为“待审核”");
			}

			model.setModifier(user.getId());
			model.setModifierUsername(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
			purchaseOrderDao.modify(model);
			//封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            emailUserDTO.setBuId(model.getBuId());
            emailUserDTO.setBuName(model.getBuName());
            emailUserDTO.setMerchantId(model.getMerchantId());
            emailUserDTO.setMerchantName(model.getMerchantName());
            emailUserDTO.setOrderCode(model.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
            emailUserDTO.setSupplier(model.getSupplierName());
            emailUserDTO.setPoNum(model.getPoNo());
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setStatus("已驳回");
            emailUserDTO.setUserId(Arrays.asList(String.valueOf(model.getSubmiter())));
            emailUserDTO.setSubmitId(model.getSubmiter()!=null?Arrays.asList(String.valueOf(model.getSubmiter())):null);
            String auditMethod = model.getAuditMethod();
            String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
            emailUserDTO.setAuditMethod(auditMethodLabel);
            JSONObject json = JSONObject.fromObject(emailUserDTO) ;
            sendEmailList.add(json.toString());

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, model.getCode(), "驳回", null, null);

		}
		for (String json : sendEmailList) {
			try {
                //发送邮件
                rocketMQProducer.send(json, MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            } catch (Exception e) {
                LOGGER.error("--------采购发送邮件发送失败-------", json);
            }
		}
	}

    /**
     * 新增编辑公共方法
     * @param model
     * @param user
     * @param operate
     * @return
     * @throws Exception
     */
	private PurchaseOrderModel saveOrModifyPurchaseOrder(PurchaseOrderModel model, User user, String operate) throws Exception {
        if(model.getBuId() == null){
            throw new DerpException("事业部不能为空") ;
        }
        // 获取该事业部信息
        Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
        merchantBuRelParam.put("merchantId", user.getMerchantId());
        merchantBuRelParam.put("buId", model.getBuId());
        merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
        if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
            throw new RuntimeException("事业部ID为：" + model.getBuId() + ",未查到该公司下事业部信息");
        }

        // 用户事业部
        boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), model.getBuId());
        if (!userRelateBu) {
            throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
        }

		PurchaseOrderModel tempPurchaseOrderModel = null;
		//String oldLbxNo = model.getLbxNo();
		String oldContractNo = model.getContractNo();
		//判断model的id是否存在，存在则编辑，不存在则新增
        Long id = model.getId();
        if (id == null) {
            // 采购订单编码
            model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
            model.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
            model.setAmountConfirmStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0);
            model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
        }else {
        	tempPurchaseOrderModel = purchaseOrderDao.searchById(model.getId());
        	oldContractNo = tempPurchaseOrderModel.getContractNo();
             if(StringUtils.isBlank(tempPurchaseOrderModel.getAmountAdjustmentStatus())) {
                 model.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
             }
             if(StringUtils.isBlank(tempPurchaseOrderModel.getAmountConfirmStatus())) {
                 model.setAmountConfirmStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0);
             }
        }

        // 通过商家id获取商家信息
        Map<String, Object> merchanto_params = new HashMap<String, Object>();
        merchanto_params.put("merchantId", model.getMerchantId());
        MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);
        if (merchantMogo == null) {
            throw new DerpException("商家不存在") ;
        }
        model.setMerchantName(merchantMogo.getName());// 商家名称
        model.setTopidealCode(merchantMogo.getTopidealCode());

        // 通过仓库id获取仓库信息
        DepotInfoMongo depotInfo = null;
        if (model.getDepotId() != null) {
            Map<String, Object> depotInfo_params = new HashMap<String, Object>();
            depotInfo_params.put("depotId", model.getDepotId());
            depotInfo = depotInfoMongoDao.findOne(depotInfo_params);
            if(depotInfo == null){
                throw new DerpException("仓库不存在") ;
            }
            // 校验公司-仓库-事业部的关联表
            Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
            merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
            merchantDepotBuRelParam.put("depotId", depotInfo.getDepotId()); // 出仓仓库
            merchantDepotBuRelParam.put("buId", model.getBuId());
            MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
            if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
                throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",入仓仓库："+ depotInfo.getDepotCode() + ",未查到该公司仓库事业部关联信息");
            }
            Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
            depotMerchantRelParam.put("merchantId", user.getMerchantId());
            depotMerchantRelParam.put("depotId", model.getDepotId());
            DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
            if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
                throw new RuntimeException("仓库ID为：" + model.getDepotId() + ",未查到该公司下入仓库信息");
            }
            model.setDepotName(depotInfo.getName());

        }


        if(model.getContractNo() != null && !"".equals(model.getContractNo())){
            if(oldContractNo!=null && !"".equals(oldContractNo)){
                Map<String, Object> contract_params = new HashMap<String, Object>();
                contract_params.put("contractNo", oldContractNo);
                contract_params.put("type", DERP.UNIQUEID_PREFIX_CGO);
                contract_params.put("orderCode", model.getCode());
                // 删除合同号
                contractNoMongoDao.remove(contract_params);
            }
            // 合同号写入mongodb
            ContractNoMongo contractNoMongo = new ContractNoMongo();
            contractNoMongo.setContractNo(model.getContractNo());
            contractNoMongo.setType(DERP.UNIQUEID_PREFIX_CGO);
            contractNoMongo.setOrderCode(model.getCode());
            contractNoMongoDao.insert(contractNoMongo);
        }

        if (model.getSupplierId() != null) {
            Map<String, Object> customer_params = new HashMap<String, Object>();
            customer_params.put("customerId", model.getSupplierId());
            customer_params.put("merchantId", model.getMerchantId());
            CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(customer_params);
            if (customer == null) {
                throw new DerpException("供应商不存在") ;
            }
            model.setSupplierName(customer.getName());// 供应商名称
        }

        //查询事业部名称
        model.setBuName(merchantBuRelMongo.getBuName());


        if("2".equals(operate)){
            if(model.getSupplierId() == null){
                throw new DerpException("保存失败，供应商不能为空") ;
            }
            if(model.getBusinessModel() == null){
                throw new DerpException("保存失败，业务模式不能为空") ;
            }
            if(model.getCurrency() == null){
                throw new DerpException("保存失败，币种不能为空") ;
            }
            if(StringUtils.isBlank(model.getPoNo()) ){
                throw new DerpException("保存失败，po号不能为空") ;
            }
            if(model.getAttributionDate() == null){
                throw new DerpException("保存失败，归属日期不能为空") ;
            }
            if(StringUtils.isBlank(model.getAuditMethod())){
                throw new DerpException("保存失败，审批方式不能为空") ;
            }
            if(StringUtils.isBlank(model.getPaymentProvision())){
                throw new DerpException("保存失败，结算条款不能为空") ;
            }
            if(StringUtils.isBlank(model.getTradeTerms())){
                throw new DerpException("保存失败，交付方式不能为空") ;
            }
            if(depotInfo != null && DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3.equals(model.getBusinessModel())
                    && (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())
                    || DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfo.getType()))){
                throw new DerpException("当入库仓库类别为保税仓、海外仓，可选业务模式为：购销、代销；") ;

            }
            //通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
            if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && model.getStockLocationTypeId() == null){
                throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空") ;
            }else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && model.getStockLocationTypeId() != null){
                throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写") ;
            }

            if(model.getItemList() == null || model.getItemList().size() < 1){
                throw new RuntimeException("商品信息不能为空") ;
            }
        }
        BuStockLocationTypeConfigMongo stockLocationMongo = null;
        if(model.getStockLocationTypeId() != null){
            Map<String,Object> stockLocationMap = new HashMap<>();
            stockLocationMap.put("buStockLocationTypeId", model.getStockLocationTypeId());
            stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
            stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

            if(stockLocationMongo == null){
                throw new RuntimeException("库位类型输入有误") ;
            }
        }
        if(stockLocationMongo != null){
            model.setStockLocationTypeName(stockLocationMongo.getName());
        }

        if(model.getInCustomsId() != null) {
            Map<String,Object> relParams = new HashMap<>();
            relParams.put("depotId", model.getDepotId());
            relParams.put("customsAreaId", model.getInCustomsId());
            DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(relParams);

            if(depotCustomsRel != null) {
                model.setInCustomsCode(depotCustomsRel.getCustomsAreaCode());
                model.setInPlatformCustoms(depotCustomsRel.getCustomsAreaName());
            }
        }

        int num = 0;
        if (id == null) {
            // 新增表头
            // 创建人
            model.setCreater(user.getId());
            // 创建人用户名
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            id = purchaseOrderDao.save(model);
        } else {
            // 编辑表头
            // 修改人
            model.setModifier(user.getId());
            model.setModifyDate(TimeUtils.getNow());
            // 修改人用户名
            model.setModifierUsername(user.getName());
            num = purchaseOrderDao.modifyWithNull(model);
        }
        //校验同一货号价格是否相同
        List<String> checkGoodsNoPrice = new ArrayList<String>();
        for (PurchaseOrderItemModel item : model.getItemList()) {
            if(item.getPrice() == null){
                continue;
            }
            if(item.getNum() == null || item.getNum().intValue() <= 0){
                throw new DerpException("条形码为："+item.getBarcode()+"的商品采购数量必须大于0") ;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("barcode", item.getBarcode());
            params.put("merchantId", user.getMerchantId());
            params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
            List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(params, model.getDepotId());
            if(merchandiseList == null || merchandiseList.size() < 1){
                throw new DerpException("在该公司仓库下，未找到条形码为："+item.getBarcode()+"的启用商品") ;
            }
            MerchandiseInfoMogo mogo = merchandiseList.get(0);
            if (mogo != null) {
                item.setGoodsCode(mogo.getGoodsCode());// 商品编码
                item.setGoodsName(mogo.getName());// 商品名称
                item.setGoodsNo(mogo.getGoodsNo());// 商品货号
                item.setBarcode(mogo.getBarcode());
            }
            String goodsNoPrice = item.getBarcode()+"_"+item.getPrice();
            if(checkGoodsNoPrice.contains(goodsNoPrice)){
                throw new DerpException("同一条形码："+item.getBarcode()+"，采购单价相同") ;
            }else{
                checkGoodsNoPrice.add(goodsNoPrice);
            }
        }
        List<Long> ids=new ArrayList<>();// 编辑删除表体ids 传入报表库报表库删除表体
        if (num > 0) {
            // 根据采购订单id查询采购订单表体
            PurchaseOrderItemModel purchaseOrderItemModel=new  PurchaseOrderItemModel();
            purchaseOrderItemModel.setPurchaseOrderId(model.getId());;
            List<PurchaseOrderItemModel> itemDelList = purchaseOrderItemDao.list(purchaseOrderItemModel);
            for (PurchaseOrderItemModel item : itemDelList) {
                ids.add(item.getId());
            }

            // 根据表头id删除表体，重新添加
            purchaseOrderDao.delById(model.getId());

        }
        // 封装表体
        for (PurchaseOrderItemModel item : model.getItemList()) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchandiseId", item.getGoodsId());
            MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
            if (mogo != null) {
                item.setGoodsCode(mogo.getGoodsCode());// 商品编码
                item.setGoodsName(mogo.getName());// 商品名称
                item.setGoodsNo(mogo.getGoodsNo());// 商品货号
                item.setFactoryNo(mogo.getFactoryNo());// 工厂编码
            }

            item.setPurchaseOrderId(id);
            item.setCreater(user.getId());// 创建人id
            item.setCreateName(user.getName());// 创建人用户名
            item.setCreateDate(TimeUtils.getNow());
            item.setModifyDate(TimeUtils.getNow());

            purchaseOrderItemDao.save(item);

        }

		return model;
	}

    //检查相同SKU是否存在多条
    public boolean checkRepeatGoods(Long id) throws Exception {
        //检查相同SKU是否存在多条
        PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
        purchaseOrderItemModel.setPurchaseOrderId(id);
        List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(purchaseOrderItemModel);
        if(itemList != null && itemList.size() > 0){
            Map<String, List<PurchaseOrderItemModel>> itemGroupByGoodsNoPriceMap = itemList.stream().collect(Collectors.groupingBy(i-> i.getGoodsNo()));
            for(String key : itemGroupByGoodsNoPriceMap.keySet()){
                List<PurchaseOrderItemModel> repeatGoodsList = itemGroupByGoodsNoPriceMap.get(key);
                if(repeatGoodsList.size() > 1){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkPoNo(String str){
        //输入时仅限中文、大小写字母、数字和“-”这4种字符
        String path="^[\\d\\w\\-\\u4e00-\\u9fa5]+";//定义匹配规则
        Pattern p=Pattern.compile(path);//实例化Pattern
        return p.matcher(str).matches();
    }

    @Override
    public Map importPurchaseGoods(List<List<Map<String, String>>> data, User user, PurchaseOrderDTO dto, Boolean purchasePriceManage) {
        List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
        List<Map<String, Object>> stringList = new ArrayList<Map<String, Object>>();
        Map<String, List<String>> purchasePriceMap = new HashMap<>();
        Map<String, MerchandiseInfoMogo> goodsMap = new HashMap<>();
        Integer success = 0;
        Integer pass = 0;
        Integer failure = 0;
        List<Map<String, String>> objects = data.get(0);
        Map<String, Integer> dataMap = new HashMap<>();
        for (int j = 0; j < objects.size(); j++) {
            Map<String, String> msg = new HashMap<String, String>();
            Map<String, String> map = objects.get(j);
            String barcode = map.get("条形码");
            if (StringUtils.isBlank(barcode)) {
                msg.put("row", String.valueOf(j + 1));
                msg.put("msg", "存在条形码为空的导入数据！");
                msgList.add(msg);
                failure += 1;
                continue;
            }
            barcode = barcode.trim();
            String num = map.get("采购数量");
            num = num.trim();
            if (num == null || "".equals(num)) {
                msg.put("row", String.valueOf(j + 1));
                msg.put("msg", "采购数量不能为空");
                msgList.add(msg);
                failure += 1;
                continue;
            }
            if (!StringUtils.isNumeric(num)) {
                msg.put("row", String.valueOf(j + 1));
                msg.put("msg", "采购数量只能是整数");
                msgList.add(msg);
                failure += 1;
                continue;
            }
            MerchandiseInfoMogo goods = goodsMap.get(barcode);
            if( goods == null){
                Map<String,Object> merchandiseInfoParams = new HashMap<>();
                merchandiseInfoParams.put("barcode", barcode);
                merchandiseInfoParams.put("merchantId", user.getMerchantId());
                merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
                List<MerchandiseInfoMogo> goodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams,dto.getDepotId());
                if(goodsList == null || goodsList.size() < 1){
                    msg.put("row", String.valueOf(j + 1));
                    msg.put("msg", "该公司入库仓下，条形码："+ barcode +"未找到启用商品");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                goods = goodsList.get(0);
                goodsMap.put(barcode, goods);

            }

            if(purchasePriceManage && !purchasePriceMap.containsKey(barcode)) {
                Integer torrUnit = 1;
                if(dto.getDepotId() != null) {
                    //仓库
                    Map<String, Object> queryDepotMap = new HashMap<String, Object>() ;
                    queryDepotMap.put("depotId", dto.getDepotId()) ;
                    DepotInfoMongo depot = depotInfoMongoDao.findOne(queryDepotMap);
                    //仓库为海外仓，则获取商品的箱托，单价*箱托
                    if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
                        if (DERP.ORDER_TALLYINGUNIT_00.equals(dto.getTallyingUnit())) {
                            torrUnit = goods.getTorrToUnit();

                            if (torrUnit == null || torrUnit == 0) {
                                msg.put("row", String.valueOf(j + 1));
                                msg.put("msg", "条形码："+barcode+"，货号：" + goods.getGoodsNo() + "托转件为空");
                                msgList.add(msg);
                                failure += 1;
                                continue;
                            }
                        }

                        if (DERP.ORDER_TALLYINGUNIT_01.equals(dto.getTallyingUnit())) {
                            torrUnit=  goods.getBoxToUnit();
                            if (torrUnit == null || torrUnit == 0) {
                                msg.put("row", String.valueOf(j + 1));
                                msg.put("msg", "条形码："+barcode+"，货号：" + goods.getGoodsNo() + "箱转件为空");
                                msgList.add(msg);
                                failure += 1;
                                continue;
                            }
                        }
                    }
                }

                Map<String, Object> smPriceMap = new HashMap<String, Object>() ;
                smPriceMap.put("merchantId", user.getMerchantId()) ;
                smPriceMap.put("customerId", dto.getSupplierId()) ;
                smPriceMap.put("buId", dto.getBuId()) ;
                smPriceMap.put("commbarcode", goods.getCommbarcode()) ;
                smPriceMap.put("currency", dto.getCurrency()) ;
                smPriceMap.put("state", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003) ;
                if(dto.getStockLocationTypeId() != null){
                    smPriceMap.put("stockLocationTypeId", dto.getStockLocationTypeId()) ;
                }

                List<SupplierMerchandisePriceMongo> smList = supplierMerchandisePriceMongoDao.findAll(smPriceMap);

                if(smList.isEmpty()) {
                    msg.put("row", String.valueOf(j + 1));
                    msg.put("msg", "该商家事业部已启用采购价格管理，该条形码对应的标准条码查询供应商价格管理无数据");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }

                smList = smList.stream().sorted(Comparator.comparing(SupplierMerchandisePriceMongo :: getAuditDate).reversed()).collect(Collectors.toList()) ;

                List<String> priceList = new ArrayList<String>();

                SupplierMerchandisePriceMongo purchasePriceMongo = null;
                for (SupplierMerchandisePriceMongo tempMongo : smList) {

                    String effectiveDate = tempMongo.getEffectiveDate();
                    String expiryDate = tempMongo.getExpiryDate();

                    if (TimeUtils.parse(effectiveDate, "yyyy-MM-dd").getTime() <= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()
                            && TimeUtils.parse(expiryDate, "yyyy-MM-dd").getTime() >= TimeUtils.parse(TimeUtils.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd").getTime()) {
                        purchasePriceMongo = tempMongo;

                        BigDecimal tempPrice = tempMongo.getSupplyPrice().multiply(new BigDecimal(torrUnit));//计算箱托之后的价格
                        String temPrice = tempPrice.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString();
                        if(!priceList.contains(temPrice)) {
                            priceList.add(temPrice);
                        }
                    }
                }

                if(purchasePriceMongo == null) {
                    msg.put("row", String.valueOf(j + 1));
                    msg.put("msg", "该商家-事业部-供应商已启用采购价格管理，该归属日期查询供应商价格管理无数据");
                    msgList.add(msg);
                    failure += 1;
                    continue;
                }
                if(priceList != null && priceList.size() > 0){
                    purchasePriceMap.put(barcode, priceList);
                }
            }

            //相同条形码累计
            if(dataMap.containsKey(barcode)){
                Integer tempNum = dataMap.get(barcode) + Integer.valueOf(num);
                dataMap.put(barcode, tempNum);
            }else{
                dataMap.put(barcode, Integer.valueOf(num));
            }
        }
        if (failure == 0) {
            for (String barcode : dataMap.keySet()) {
                Integer num = dataMap.get(barcode);
                MerchandiseInfoMogo goods = goodsMap.get(barcode);
                List<String> priceList = purchasePriceMap.get(barcode);

                BigDecimal taxRate = BigDecimal.ZERO;
                HashMap<String, Object> customerMerchantRelMap = new HashMap<>();
                customerMerchantRelMap.put("merchantId", user.getMerchantId());
                customerMerchantRelMap.put("customerId", dto.getSupplierId());
                customerMerchantRelMap.put("status", DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);
                CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(customerMerchantRelMap);
                if (customerMerchantRelMongo != null) {
                    taxRate = customerMerchantRelMongo.getRate();
                }

                Map<String, Object> stringMap = new HashMap<String, Object>();
                stringMap.put("goodsId", goods.getMerchandiseId());
                stringMap.put("goodsNo", goods.getGoodsNo());
                stringMap.put("goodsName", goods.getName());
                stringMap.put("goodsCode", goods.getGoodsCode());
                stringMap.put("barcode", barcode);
                stringMap.put("num", num);
                stringMap.put("taxRate", taxRate);
                stringMap.put("priceList", priceList);
                stringMap.put("factoryNo",goods.getFactoryNo());

                stringList.add(stringMap);
                success++;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msgList", msgList);
        map.put("stringList", stringList);
        map.put("success", success);
        map.put("pass", pass);
        map.put("failure", failure);
        return map;
    }

    @Override
    public boolean validateApply(Long id) throws Exception {

	    PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

	    if (purchaseOrderModel == null) {
	        throw new RuntimeException("采购订单不存在!");
        }

	    if (!DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())) {
            throw new RuntimeException("采购订单状态必须为【已入库】!");
        }

	    if (StringUtils.isNotBlank(purchaseOrderModel.getWriteOffStatus())) {
	        throw new RuntimeException("采购订单红冲状态为“待红冲”，”红冲中“或“已红冲”，请勿重复申请红冲！");
        }

	    //采购订单关联的入库单
        WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel();
	    warehouseOrderRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
        List<WarehouseOrderRelModel> relModels = warehouseOrderRelDao.list(warehouseOrderRelModel);

        List<Long> warehouseOrderIds = new ArrayList<>();
        for (WarehouseOrderRelModel relModel : relModels) {
            warehouseOrderIds.add(relModel.getWarehouseId());
        }

        List<PurchaseWarehouseModel> purchaseWarehouseModels = purchaseWarehouseDao.getListByIds(warehouseOrderIds);

        for (PurchaseWarehouseModel warehouseModel : purchaseWarehouseModels) {
            if (!DERP_ORDER.PURCHASEWAREHOUSE_STATE_012.equals(warehouseModel.getState())) {
                throw new RuntimeException("采购单存在【待入仓】【入仓中】状态的入库单");
            }
        }

        //采购单是否有关联的采购退订单
        PurchaseReturnRelModel purchaseReturnRelModel = new PurchaseReturnRelModel();
        purchaseReturnRelModel.setPurchaseId(purchaseOrderModel.getId());
        List<PurchaseReturnRelModel> purchaseReturnRelModels = purchaseReturnRelDao.list(purchaseReturnRelModel);

        if (purchaseReturnRelModels.size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void saveApplyWriteOff(Long id, String reason, User user) throws Exception {
        PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

        if (purchaseOrderModel == null) {
            throw new RuntimeException("采购订单不存在!");
        }

        if (!DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())) {
            throw new RuntimeException("采购订单状态必须为【已入库】!");
        }

        if (StringUtils.isNotBlank(purchaseOrderModel.getWriteOffStatus())) {
            throw new RuntimeException("采购订单红冲状态为“待红冲”，”红冲中“或“已红冲”，请勿重复申请红冲！");
        }

        PurchaseOrderModel updateModel = new PurchaseOrderModel();
        updateModel.setId(purchaseOrderModel.getId());
        updateModel.setWriteOffStatus(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001);
        purchaseOrderDao.modify(updateModel);

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "申请红冲", null, reason);

    }

    @Override
    public PurchaseWriteOffDTO validateAuditWriteOff(Long id, User user) throws Exception {
        PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

        if (purchaseOrderModel == null) {
            throw new RuntimeException("采购订单不存在!");
        }

        if (!DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())) {
            throw new RuntimeException("采购订单状态必须为【已入库】!");
        }

        if (!DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001.equals(purchaseOrderModel.getWriteOffStatus())) {
            throw new RuntimeException("采购订单的红冲状态不为待红冲！");
        }

        //采购订单关联的入库单
        WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel();
        warehouseOrderRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
        List<WarehouseOrderRelModel> relModels = warehouseOrderRelDao.list(warehouseOrderRelModel);

        List<Long> warehouseOrderIds = new ArrayList<>();
        for (WarehouseOrderRelModel relModel : relModels) {
            warehouseOrderIds.add(relModel.getWarehouseId());
        }

        Map<Long, Long> warehouseDepotMap = new HashMap<>();
        List<PurchaseWarehouseModel> purchaseWarehouseModels = purchaseWarehouseDao.getListByIds(warehouseOrderIds);

        for (PurchaseWarehouseModel warehouseModel : purchaseWarehouseModels) {
            if (!DERP_ORDER.PURCHASEWAREHOUSE_STATE_012.equals(warehouseModel.getState())) {
                throw new RuntimeException("采购单存在【待入仓】【入仓中】状态的入库单");
            }
            warehouseDepotMap.put(warehouseModel.getId(), warehouseModel.getDepotId());
        }

        OperateLogModel operateLogModel = new OperateLogModel();
        operateLogModel.setOperateAction("申请红冲");
        operateLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_1);
        operateLogModel.setRelCode(purchaseOrderModel.getCode());
        OperateLogModel latestOperate = operateLogDao.getLatestOperateLog(operateLogModel);

        PurchaseWriteOffDTO purchaseOrderDTO = new PurchaseWriteOffDTO();
        purchaseOrderDTO.setId(purchaseOrderModel.getId());
        purchaseOrderDTO.setCode(purchaseOrderModel.getCode());
        purchaseOrderDTO.setIsRelPurchaseReturn("0");
        purchaseOrderDTO.setReason(latestOperate.getOperateRemark());

        //采购单是否有关联的采购退订单
        PurchaseReturnRelModel purchaseReturnRelModel = new PurchaseReturnRelModel();
        purchaseReturnRelModel.setPurchaseId(purchaseOrderModel.getId());
        List<PurchaseReturnRelModel> purchaseReturnRelModels = purchaseReturnRelDao.list(purchaseReturnRelModel);

        if (purchaseReturnRelModels.size() > 0) {
            purchaseOrderDTO.setIsRelPurchaseReturn("1");
        }

        //关联的采购入库单表体
        List<PurchaseWriteOffItemDTO> warehouseItemModels = purchaseWarehouseItemDao.getDetailsByIds(warehouseOrderIds);

        Map<String, PurchaseWriteOffItemDTO> purchaseWriteOffItemDTOMap = new HashMap<>();

        for (PurchaseWriteOffItemDTO purchaseWriteOffItemDTO : warehouseItemModels) {

            Long depotId = warehouseDepotMap.get(purchaseWriteOffItemDTO.getWarehouseId());
            PurchaseWriteOffItemDTO newItemDTO = new PurchaseWriteOffItemDTO();
            newItemDTO.setDepotId(depotId);
            newItemDTO.setBatchNo(purchaseWriteOffItemDTO.getBatchNo());
            newItemDTO.setOverdueDate(purchaseWriteOffItemDTO.getOverdueDate());
            newItemDTO.setProductionDate(purchaseWriteOffItemDTO.getProductionDate());
            newItemDTO.setTallyingUnit(purchaseWriteOffItemDTO.getTallyingUnit());

            //批次+效期+理货单位+仓库+商品id+好坏品类型+是否过期
            String key = purchaseWriteOffItemDTO.getBatchNo() + "_" + purchaseWriteOffItemDTO.getOverdueDate()
                    + "_" + purchaseWriteOffItemDTO.getProductionDate() + "_" + purchaseWriteOffItemDTO.getTallyingUnit()
                    + "_" + depotId;


            key = key + "_" + purchaseWriteOffItemDTO.getGoodsId();
            newItemDTO.setGoodsId(purchaseWriteOffItemDTO.getGoodsId());
            newItemDTO.setGoodsNo(purchaseWriteOffItemDTO.getGoodsNo());


            //是否过期（0 过期 1 未过期）
            String isExpire = "1";
            //好坏品 0 正常品  1 残次品
            String type = "0";

            //过期品数量
            if (purchaseWriteOffItemDTO.getExpireNum() != null && purchaseWriteOffItemDTO.getExpireNum() > 0) {
                PurchaseWriteOffItemDTO tempDTO = new PurchaseWriteOffItemDTO();
                BeanUtils.copyProperties(newItemDTO, tempDTO);
                isExpire = "0";
                type = "1";
                key = key + "_" + isExpire + "_" + type;
                tempDTO.setIsExpire(isExpire);
                tempDTO.setType(type);
                Integer num = purchaseWriteOffItemDTO.getExpireNum();

                if (purchaseWriteOffItemDTOMap.containsKey(key)) {
                    num += purchaseWriteOffItemDTOMap.get(key).getNum();
                }

                tempDTO.setNum(num);
                purchaseWriteOffItemDTOMap.put(key, tempDTO);
            }

            //坏品数量
            if (purchaseWriteOffItemDTO.getWornNum() != null && purchaseWriteOffItemDTO.getWornNum() > 0) {
                type = "1";
                isExpire = "1";
                key = key + "_" + isExpire + "_" + type;

                PurchaseWriteOffItemDTO tempDTO = new PurchaseWriteOffItemDTO();
                BeanUtils.copyProperties(newItemDTO, tempDTO);
                tempDTO.setIsExpire(isExpire);
                tempDTO.setType(type);
                Integer num = purchaseWriteOffItemDTO.getWornNum();

                if (purchaseWriteOffItemDTOMap.containsKey(key)) {
                    num += purchaseWriteOffItemDTOMap.get(key).getNum();
                }

                tempDTO.setNum(num);
                purchaseWriteOffItemDTOMap.put(key, tempDTO);
            }

            //正常品数量
            if (purchaseWriteOffItemDTO.getNormalNum() != null && purchaseWriteOffItemDTO.getNormalNum() > 0) {
                type = "0";
                isExpire = "1";
                key = key + "_" + isExpire + "_" + type;

                PurchaseWriteOffItemDTO tempDTO = new PurchaseWriteOffItemDTO();
                BeanUtils.copyProperties(newItemDTO, tempDTO);
                tempDTO.setIsExpire(isExpire);
                tempDTO.setType(type);
                Integer num = purchaseWriteOffItemDTO.getNormalNum();

                if (purchaseWriteOffItemDTOMap.containsKey(key)) {
                    num += purchaseWriteOffItemDTOMap.get(key).getNum();
                }

                tempDTO.setNum(num);
                purchaseWriteOffItemDTOMap.put(key, tempDTO);
            }

        }

        List<PurchaseWriteOffItemDTO> newWriteOffItemDTOs = purchaseWriteOffItemDTOMap.values().stream().collect(Collectors.toList());
        purchaseOrderDTO.setItemDTOS(newWriteOffItemDTOs);

        return purchaseOrderDTO;
    }

    @Override
    public void saveAuditWriteOff(Long id, String auditResult, String attributiveDate, User user) throws Exception {
        PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(id);

        if (purchaseOrderModel == null) {
            throw new RuntimeException("采购订单不存在!");
        }

        if (!DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())) {
            throw new RuntimeException("采购订单状态必须为【已入库】!");
        }

        if (!DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001.equals(purchaseOrderModel.getWriteOffStatus())) {
            throw new RuntimeException("采购订单的红冲状态不为待红冲！");
        }

	    //驳回
	    if ("0".equals(auditResult)) {
            purchaseOrderModel.setWriteOffStatus(null);
            purchaseOrderDao.modifyWithNull(purchaseOrderModel);
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "审核红冲",  null, "驳回");
        }


	    //通过
        if ("1".equals(auditResult)) {
            //采购订单关联的入库单
            WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel();
            warehouseOrderRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
            List<WarehouseOrderRelModel> relModels = warehouseOrderRelDao.list(warehouseOrderRelModel);

            List<Long> warehouseOrderIds = new ArrayList<>();
            for (WarehouseOrderRelModel relModel : relModels) {
                warehouseOrderIds.add(relModel.getWarehouseId());
            }

            List<PurchaseWarehouseModel> purchaseWarehouseModels = purchaseWarehouseDao.getListByIds(warehouseOrderIds);

            for (PurchaseWarehouseModel warehouseModel : purchaseWarehouseModels) {
                if (!DERP_ORDER.PURCHASEWAREHOUSE_STATE_012.equals(warehouseModel.getState())) {
                    throw new RuntimeException("采购单关联入库单存在待入仓或入仓中的数据，请及时处理");
                }
            }

            //归属日期不能大于当前日期
            if (TimeUtils.formatDay().compareTo(attributiveDate) < 0) {
                throw new RuntimeException("归属日期不能大于当前日期!");
            }

            //最大的入库时间
            Timestamp inDepotTime = null;
            for (PurchaseWarehouseModel purchaseWarehouseModel : purchaseWarehouseModels) {
                if (inDepotTime == null || TimeUtils.timeComparisonSize(purchaseWarehouseModel.getInboundDate(), inDepotTime).equals("1")) {
                    inDepotTime = purchaseWarehouseModel.getInboundDate();
                }
            }

            //归属日期不能小于入库单入库时间
            if (attributiveDate.compareTo(TimeUtils.format(inDepotTime, "yyyy-MM-dd")) < 0) {
                throw new RuntimeException("归属日期不能小于入库单入库时间!");
            }

            // 获取最大的关账/月结日期
            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(user.getMerchantId());
            closeAccountsMongo.setBuId(purchaseOrderModel.getBuId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
            String maxCloseAccountsMonth = "";
            if (StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
                maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 入库日期必须大于关账日期
                if (attributiveDate.compareTo(maxCloseAccountsMonth) < 0) {
                   throw new RuntimeException("归属时间必须大于关账/月结日期");
                }
            }

            //修改状态为“红冲中”
            purchaseOrderModel.setWriteOffStatus(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_002);
            purchaseOrderModel.setWriteOffDate(TimeUtils.parseDay(attributiveDate));
            purchaseOrderDao.modify(purchaseOrderModel);

            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "审核红冲", null, "通过");

            List<PurchaseWarehouseModel> newPurchaseWarehouseModels = new ArrayList<>();
            //生成新的入库单
            for (PurchaseWarehouseModel warehouseModel : purchaseWarehouseModels) {
                PurchaseWarehouseModel newModel = new PurchaseWarehouseModel();
                BeanUtils.copyProperties(warehouseModel, newModel, new String[]{"id", "code", "warehouseDate"});
                newModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));
                newModel.setInboundDate(purchaseOrderModel.getWriteOffDate());
                newModel.setIsWriteOff(DERP_ORDER.PURCHASEWAREHOUSE_ISWRITEOFF_1);
                newModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);
                newModel.setOriWarehouseCode(warehouseModel.getCode());
                Long warehouseId = purchaseWarehouseDao.save(newModel);

                //采购入库单表体
                PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
                itemModel.setWarehouseId(warehouseModel.getId());
                List<PurchaseWarehouseItemModel> itemModels = purchaseWarehouseItemDao.list(itemModel);

                for (PurchaseWarehouseItemModel warehouseItemModel : itemModels) {

                    PurchaseWarehouseItemModel newItemModel = new PurchaseWarehouseItemModel();
                    BeanUtils.copyProperties(warehouseItemModel, newItemModel, new String[]{"id", "warehouseId"});
                    newItemModel.setWarehouseId(warehouseId);
                    Integer purchaseNum = warehouseItemModel.getPurchaseNum() == null ? 0: warehouseItemModel.getPurchaseNum();
                    Integer tallyingNum = warehouseItemModel.getTallyingNum() == null ? 0: warehouseItemModel.getTallyingNum();
                    Integer normalNum = warehouseItemModel.getNormalNum() == null ? 0: warehouseItemModel.getNormalNum();
                    Integer multiNum = warehouseItemModel.getMultiNum() == null ? 0: warehouseItemModel.getMultiNum();
                    Integer lackNum = warehouseItemModel.getLackNum() == null ? 0: warehouseItemModel.getLackNum();
                    newItemModel.setPurchaseNum(purchaseNum*(-1));
                    newItemModel.setTallyingNum(tallyingNum*(-1));
                    newItemModel.setNormalNum(normalNum*(-1));
                    newItemModel.setMultiNum(multiNum*(-1));
                    newItemModel.setLackNum(lackNum*(-1));
                    Long itemId = purchaseWarehouseItemDao.save(newItemModel);

                    //采购入库单批次
                    PurchaseWarehouseBatchModel purchaseWarehouseBatchModel = new PurchaseWarehouseBatchModel();
                    purchaseWarehouseBatchModel.setItemId(warehouseItemModel.getId());
                    List<PurchaseWarehouseBatchModel> batchModels = purchaseWarehouseBatchDao.list(purchaseWarehouseBatchModel);

                    for (PurchaseWarehouseBatchModel batchModel : batchModels) {
                        PurchaseWarehouseBatchModel newBatchModel = new PurchaseWarehouseBatchModel();
                        BeanUtils.copyProperties(batchModel, newBatchModel, new String[]{"id", "itemId"});
                        newBatchModel.setItemId(itemId);
                        Integer wornNum = batchModel.getWornNum() == null ? 0: batchModel.getWornNum();
                        Integer normalBatchNum = batchModel.getNormalNum() == null ? 0: batchModel.getNormalNum();
                        Integer expireNum = batchModel.getExpireNum() == null ? 0: batchModel.getExpireNum();
                        newBatchModel.setWornNum(wornNum*(-1));
                        newBatchModel.setNormalNum(normalBatchNum*(-1));
                        newBatchModel.setExpireNum(expireNum*(-1));
                        purchaseWarehouseBatchDao.save(newBatchModel);
                    }
                }

                //采购订单和采购入库单的关联关系
                WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
                relModel.setPurchaseOrderId(purchaseOrderModel.getId());
                relModel.setWarehouseId(warehouseId);
                warehouseOrderRelDao.save(relModel);

                newPurchaseWarehouseModels.add(newModel);
            }

            //推库存加减接口
            List<InventoryWriteOffRootJson> jsonList = new ArrayList<InventoryWriteOffRootJson>();
            for (PurchaseWarehouseModel model : newPurchaseWarehouseModels) {

                InventoryWriteOffRootJson inventoryRoot = new InventoryWriteOffRootJson();
                inventoryRoot.setBackTopic(MQPushBackOrderEnum.PURCHASE_WRITE_OFF_PUSH_BACK.getTopic());
                inventoryRoot.setBackTags(MQPushBackOrderEnum.PURCHASE_WRITE_OFF_PUSH_BACK.getTags());
                Map<String, Object> customParam = new HashMap<String, Object>();
                inventoryRoot.setCustomParam(customParam);
                // 减库存
                inventoryRoot.setMerchantId(model.getMerchantId().toString());
                inventoryRoot.setMerchantName(model.getMerchantName());
                inventoryRoot.setOrderNo(model.getCode());
                inventoryRoot.setSourceOrderNo(model.getOriWarehouseCode());
                inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(model.getInboundDate()));

                jsonList.add(inventoryRoot);

            }

            //推送库存加减接口
            for (InventoryWriteOffRootJson inventoryWriteOffRootJson : jsonList) {
                rocketMQProducer.send(JSONObject.fromObject(inventoryWriteOffRootJson).toString(),
                        MQInventoryEnum.INVENTORY_WRITE_OFF.getTopic(),
                        MQInventoryEnum.INVENTORY_WRITE_OFF.getTags());
            }

        }
    }

    @Override
    public ResponseBean getOAAuditPage(User user, String ids) throws Exception{
	    ResponseBean responseBean = new ResponseBean();

	    // 校验当前用户的员工编码是否存在
        if(StringUtils.isBlank(user.getCode())) {
            responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
            responseBean.setMessage("当前用户的员工编号不存在，请联系管理员");
            return responseBean;
        }

        // 4、必填项检查和数据规范检查，检查不通过，则提示：{XXX}、{XXX}、{XXX}必填，{xxx}数据录入有误
        String[] split = ids.split(",");
        List<Long> idList = new ArrayList<>();
        Arrays.stream(split).mapToLong(id -> Long.parseLong(id)).forEach(entity -> {
            idList.add(entity);
        });

        List<PurchaseOrderModel> purchaseOrderModels = purchaseOrderDao.listByIds(idList);
        if(purchaseOrderModels == null || purchaseOrderModels.isEmpty()) {
            responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
            responseBean.setMessage("找不到采购单数据，请联系管理员");
            return responseBean;
        }

        for (PurchaseOrderModel model : purchaseOrderModels) {

            // 判断：如果所选的采购订单的公司和事业部的采购审批方式为经分销，则提示：未配置，无法发起OA审批，请联系管理员配置。
            if(StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_PURCHASE_AUDIT_METHOD_2, model.getAuditMethod())) {
                responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
                responseBean.setMessage("未配置，无法发起OA审批，请联系管理员配置");
                return responseBean;
            }

            // 判断：所有采购单的状态都需为 未提交。
            if(!StringUtils.equals(model.getStatus(), DERP_ORDER.PURCHASEORDER_STATUS_001)) {
                responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
                responseBean.setMessage("采购单:" + model.getCode() + "状态不为未提交，无法发起OA审核");
                return responseBean;
            }

            // 公司、事业部、采购方式、供应商、业务模型、币种、PO号、归属日期、结算条款、交货方式、商品明细
            // 判断以上字段是否为空，为空则不予通过
            checkPurchaseOrderInfoBeforeCallOA(model);
        }

        // 校验商品明细中的必填项(采购数量、采购单价、采购总金额)
        List<PurchaseOrderItemModel> itemModel = purchaseOrderItemDao.getByOrderIds(idList);
        Map<String, List<PurchaseOrderItemModel>> itemMap = itemModel.stream().collect(Collectors.groupingBy(entity -> {
            String key = entity.getPurchaseOrderId().toString();
            return key;
        }));

        itemMap.entrySet().stream().forEach(entity -> {
            List<PurchaseOrderItemModel> value = entity.getValue();
            if(value == null || value.isEmpty()) {
                throw new DerpException("商品明细不能为空");
            }

            value.forEach(item -> {
                if(item.getNum() == null || item.getPrice() == null || item.getAmount() == null) {
                    throw new DerpException("商品明细采购数量、单价、总金额都不能为空");
                }
            });
        });

        // 3、判断：如果供应商、事业部、业务模式、收货方式、结算条款、采购方式、框架合同号、试单申请编号、采购币种、一致，并且状态都待提交，
        // 则可以合并发起OA审批，跳转到填写页面；如果不一致，则提示：所选的采购订单的供应商、事业部、业务模式、收货方式、结算条款、采购方式、框架合同号、试单申请编号、采购币种、状态不一致，不能同时发起。
        Map<String, List<PurchaseOrderModel>> collect = purchaseOrderModels.stream().collect(Collectors.groupingBy(entity -> {
            String key = entity.getSupplierId() + "_" + entity.getBuId() + "_" + entity.getBusinessModel()
                    + "_" + entity.getTradeTerms() + "_" + entity.getPaymentProvision() + "_"
                    + entity.getPurchaseMethod() + "_" + entity.getContractNo() + "_" + entity.getTryApplyCode()
                    + "_" + entity.getCurrency();
            return key;
        }));
        if(collect != null && collect.keySet().size() > 1) {
            responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
            responseBean.setMessage("所选的采购订单的供应商、事业部、业务模式、收货方式、结算条款、采购方式、框架合同号、试单申请编号、采购币种、状态不一致，不能同时发起");
            return responseBean;
        }

        StringBuilder codeBuilder = new StringBuilder();
        purchaseOrderModels.stream().forEach(entity -> {
            codeBuilder.append(entity.getCode()).append(",");
        });

        StringBuilder poBuilder = new StringBuilder();
        purchaseOrderModels.stream().forEach(entity -> {
            if(StringUtils.isNotBlank(entity.getPoNo())) {
                poBuilder.append(entity.getPoNo()).append(",");
            }
        });

        // 判断所选的采购订单的多个PO号是否存在当前主体生效历史采购订单中，
        // 生效状态的采购订单为：已提交、已审核、已入库、部分入库、红冲状态为待红冲
        PurchaseOrderDTO poSearchDTO = new PurchaseOrderDTO();
        poSearchDTO.setMerchantId(user.getMerchantId());
        poSearchDTO.setPoNo(poBuilder.substring(0, poBuilder.length() - 1));
        List<PurchaseOrderDTO> purchaseOrderDTOList = purchaseOrderDao.listByDTO(poSearchDTO);

        List<String> vaildStatus = Arrays.asList(DERP_ORDER.PURCHASEORDER_STATUS_001,
                DERP_ORDER.PURCHASEORDER_STATUS_002, DERP_ORDER.PURCHASEORDER_STATUS_003,
                DERP_ORDER.PURCHASEORDER_STATUS_005, DERP_ORDER.PURCHASEORDER_STATUS_007);
        List<String> writeOffStatus = Arrays.asList(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001,
                DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_002);

        Map<String, List<PurchaseOrderDTO>> poCollect = purchaseOrderDTOList.stream().collect(Collectors.groupingBy(entity -> {
            String key = entity.getPoNo() + "_" + entity.getSupplierId();
            return key;
        }));

        if(poCollect != null && !poCollect.isEmpty()) {
            // 判断相同PO号下，如果存在多个生效订单，则校验不予通过
            for (Map.Entry<String, List<PurchaseOrderDTO>> entry : poCollect.entrySet()) {
                // 小于等于1，则通过
                if(entry.getValue().size() <= 1) {
                    continue;
                }

                List<PurchaseOrderDTO> value = entry.getValue();
                int failNum = 0;
                for (PurchaseOrderDTO dto : value) {
                    if(vaildStatus.contains(dto.getStatus())) {
                        failNum++;
                    }

                    if(writeOffStatus.contains(dto.getWriteOffStatus())) {
                        failNum++;
                    }

                    // 已入库 + 已红冲 则可以通过
                    if(DERP_ORDER.PURCHASEORDER_STATUS_007.equals(dto.getStatus())
                            && DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_003.equals(dto.getWriteOffStatus())) {
                        failNum--;
                    }
                }

                if(failNum > 1) {
                    responseBean.setCode(MessageEnum.MESSAGE_10017.getCode());
                    responseBean.setMessage("此po：" + value.get(0).getPoNo() + ",已存在多个采购订单, 一个PO必须对应一个采购订单");
                    return responseBean;
                }
            }
        }

        PurchaseSaveOAAuditDTO resultDTO = new PurchaseSaveOAAuditDTO();
        resultDTO.setFrameContractFlag(false);

        PurchaseOrderModel model = purchaseOrderModels.get(0);

        String frameContractNo = model.getFrameContractNo();// 框架合同编号
        String tryApplyCode = model.getTryApplyCode();// 试单申请编号
        resultDTO.setFrameContractNo(frameContractNo);
        resultDTO.setTryApplyCode(tryApplyCode);

        if(StringUtils.isNotBlank(frameContractNo) && !StringUtils.equals("无", frameContractNo)) {
            // 获取框架合同信息
            PurchaseFrameContractDTO frameContractDTO = new PurchaseFrameContractDTO();
            frameContractDTO.setContractNo(model.getFrameContractNo());
            frameContractDTO = getFrameContractInfo(frameContractDTO);

            if(frameContractDTO != null) {
                resultDTO.setFrameContractFlag(true);
                resultDTO.setFrameContractNo(frameContractDTO.getContractNo());
                resultDTO.setBusinessManager(user.getCode());
                resultDTO.setContractVersion(frameContractDTO.getContractVersion());
                resultDTO.setMerchantId(frameContractDTO.getMerchantId());
                resultDTO.setMerchantName(frameContractDTO.getMerchantName());
                resultDTO.setOurContSignComy(frameContractDTO.getOurContSignComy());
                resultDTO.setSupplierName(frameContractDTO.getSupplierName());
                resultDTO.setCounterpartContSignComy(frameContractDTO.getCounterpartContSignComy());
                resultDTO.setSupplierType(frameContractDTO.getSupplierType());
                resultDTO.setContractStartTime(frameContractDTO.getContractStartTime());
                resultDTO.setContractEndTime(frameContractDTO.getContractEndTime());
                resultDTO.setBankChangeProvision(frameContractDTO.getBankAccChaAppoint());
                resultDTO.setSupplierDesc(frameContractDTO.getCounterpartDesc());
                resultDTO.setSupplierMerchandiseDesc(frameContractDTO.getSupProdDesc());
                resultDTO.setSealOrder(frameContractDTO.getSealOrder());
                resultDTO.setSealType(frameContractDTO.getSealType());
                resultDTO.setFrameContractDataId(frameContractDTO.getDataId());
                resultDTO.setComtyType(frameContractDTO.getComtyType());
                resultDTO.setPurchaseType(frameContractDTO.getPurchaseType());
                resultDTO.setFinanceManager(frameContractDTO.getFinanceManager());
                resultDTO.setOtherSupplierType(frameContractDTO.getOtherSupplier());
                resultDTO.setOtherComty(frameContractDTO.getOtherComtySource());
            }

            if(StringUtils.isNotBlank(tryApplyCode)){
                // 试单申请信息
                PurchaseTryApplyOrderDTO purchaseTryApplyOrderDTO = new PurchaseTryApplyOrderDTO();
                purchaseTryApplyOrderDTO.setOaBillCode(tryApplyCode);
                purchaseTryApplyOrderDTO = tryApplyOrderDao.getDetail(purchaseTryApplyOrderDTO);
                if(purchaseTryApplyOrderDTO != null) {
                    resultDTO.setTryApplyDataId(purchaseTryApplyOrderDTO.getDataId());
                }
            }
        }else if(StringUtils.isNotBlank(tryApplyCode)){
            // 试单申请信息
            PurchaseTryApplyOrderDTO purchaseTryApplyOrderDTO = new PurchaseTryApplyOrderDTO();
            purchaseTryApplyOrderDTO.setOaBillCode(tryApplyCode);
            purchaseTryApplyOrderDTO = tryApplyOrderDao.getDetail(purchaseTryApplyOrderDTO);

            if(purchaseTryApplyOrderDTO != null) {
                resultDTO.setBusinessManager(user.getCode());
                resultDTO.setMerchantId(purchaseTryApplyOrderDTO.getMerchantId());
                resultDTO.setMerchantName(purchaseTryApplyOrderDTO.getMerchantName());
                resultDTO.setOurContSignComy(purchaseTryApplyOrderDTO.getOurContSignComy());
                resultDTO.setSupplierName(purchaseTryApplyOrderDTO.getSupplierName());
                resultDTO.setCounterpartContSignComy(purchaseTryApplyOrderDTO.getCounterpartContSignComy());
                resultDTO.setSupplierType(purchaseTryApplyOrderDTO.getSupplierType());
                resultDTO.setSupplierDesc(purchaseTryApplyOrderDTO.getCounterpartDesc());
                resultDTO.setSupplierMerchandiseDesc(purchaseTryApplyOrderDTO.getSupProdDesc());
                resultDTO.setTryApplyDataId(purchaseTryApplyOrderDTO.getDataId());
                resultDTO.setComtyType(purchaseTryApplyOrderDTO.getComtyType());
                resultDTO.setPurchaseType(purchaseTryApplyOrderDTO.getPurchaseType());
                resultDTO.setOtherSupplierType(purchaseTryApplyOrderDTO.getOtherSupplier());
                resultDTO.setOtherComty(purchaseTryApplyOrderDTO.getOtherComty());
            }
        }


        // 获取采购信息
        Map<String, Object> statisticsItemMap = purchaseOrderItemDao.statisticsByIds(idList);
        resultDTO.setSkuNum(statisticsItemMap.get("countBarcode") == null ? 0 : (Long) statisticsItemMap.get("countBarcode") );
        resultDTO.setPurchaseTotalNum(statisticsItemMap.get("totalNum") == null ? BigDecimal.ZERO.longValue() : ((BigDecimal) statisticsItemMap.get("totalNum")).longValue());

        Double rate = new Double(1);
        if(!StringUtils.equals(DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY, model.getCurrency())) {
            String effectiveDate = TimeUtils.getLastDayOfMonth(TimeUtils.formatMonth(new Date()));
            Map<String, Object> queryRateMap = new HashMap<>() ;
            queryRateMap.put("origCurrencyCode", model.getCurrency()) ;//原币
            queryRateMap.put("tgtCurrencyCode", DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY) ;//目标币种
            queryRateMap.put("effectiveDate", effectiveDate);
            queryRateMap.put("status", "1") ;
            ExchangeRateMongo rateMongo = exchangeRateMongoDao.findLastRateByParams(queryRateMap);
            if(rateMongo != null) {
                rate = rateMongo.getRate();
            }
        }

        String appId = ApolloUtil.appId;
        BigDecimal amount = new BigDecimal(0);
        if(appId.equals("apollo-derp-nm")) {
            amount = statisticsItemMap.get("taxAmount") == null ? BigDecimal.ZERO : (BigDecimal) statisticsItemMap.get("taxAmount");
        }else {
            amount = statisticsItemMap.get("amount") == null ? BigDecimal.ZERO : (BigDecimal) statisticsItemMap.get("amount");
        }

        amount = amount.divide(new BigDecimal(10000)).setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal rmbAmount = new BigDecimal(rate).multiply(amount).setScale(6, BigDecimal.ROUND_HALF_UP);
        resultDTO.setPurchaseAmount(amount);
        resultDTO.setPurchaseRmbAmount(rmbAmount);

        resultDTO.setCodes(codeBuilder.substring(0, codeBuilder.length() - 1));
        resultDTO.setPoNo(poBuilder.substring(0, poBuilder.length() - 1));
        resultDTO.setPurchaseMethod(model.getPurchaseMethod());
        resultDTO.setTradeTerms(model.getTradeTerms());
        resultDTO.setPurchaseCurreny(model.getCurrency());
        resultDTO.setBuId(model.getBuId());
        resultDTO.setBuName(model.getBuName());

        // 结算信息
        if(StringUtils.equals(model.getPaymentProvision(), DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_1)) {
            resultDTO.setSettlementModel(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_1);
            resultDTO.setSettlementProvision(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_0);
            resultDTO.setHasPrepayment(true);
        }else if(StringUtils.equals(model.getPaymentProvision(), DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_2)) {
            resultDTO.setSettlementModel(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_1);
            resultDTO.setSettlementProvision(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_1);
            resultDTO.setHasPrepayment(true);
        }else if(StringUtils.equals(model.getPaymentProvision(), DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_3)) {
            resultDTO.setSettlementModel(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_0);
            resultDTO.setSettlementProvision(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_2);
            resultDTO.setHasPrepayment(false);
        }else if(StringUtils.equals(model.getPaymentProvision(), DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_4)) {
            resultDTO.setSettlementModel(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_2);
            resultDTO.setSettlementProvision(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_2);
            resultDTO.setHasPrepayment(false);
        }

        resultDTO.setPrePaymentCurrency(model.getCurrency());

        responseBean.setCode(MessageEnum.SUCCESS_10000.getCode());
        responseBean.setMessage(MessageEnum.SUCCESS_10000.getMessage());
        responseBean.setData(resultDTO);
        return responseBean;
    }

    /**
     * 发起OA审核前校验采购订单的数据
     * @param model
     */
    private void checkPurchaseOrderInfoBeforeCallOA(PurchaseOrderModel model) {
        if(model.getMerchantId() == null) {
            throw new DerpException("该采购单公司不能为空");
        }

        if(model.getBuId() == null) {
            throw new DerpException("事业部不能为空");
        }

        if(StringUtils.isBlank(model.getAuditMethod())) {
            throw new DerpException("审批方式不能为空");
        }

        if(model.getSupplierId() == null) {
            throw new DerpException("供应商不能为空");
        }

        if(StringUtils.isBlank(model.getBusinessModel())) {
            throw new DerpException("业务模式不能为空");
        }

        if(StringUtils.isBlank(model.getCurrency())) {
            throw new DerpException("币种不能为空");
        }

        if(StringUtils.isBlank(model.getPoNo())) {
            throw new DerpException("PO号不能为空");
        }

        if(model.getAttributionDate() == null) {
            throw new DerpException("归属月份不能为空");
        }

        if(StringUtils.isBlank(model.getPaymentProvision())) {
            throw new DerpException("结算条款不能为空");
        }

        if(StringUtils.isBlank(model.getTradeTerms())) {
            throw new DerpException("交货方式不能为空");
        }

//        int itemNum = purchaseOrderItemDao.countByOrderId(model.getId());
//        if(itemNum <= 0) {
//            throw new DerpException("商品明细不能为空");
//        }

        if(StringUtils.equals(DERP_ORDER.PURCHASEORDER_AUDITMETHOD_1, model.getAuditMethod())
            && (StringUtils.isBlank(model.getPurchaseMethod()) || StringUtils.isBlank(model.getFrameContractNo()))) {
            throw new DerpException("OA审批下采购方式与框架合同号都不能为空");
        }

        if(StringUtils.equals(DERP_ORDER.PURCHASEORDER_AUDITMETHOD_1, model.getAuditMethod())
                && StringUtils.equals(DERP_ORDER.PURCHASEORDER_PURCHASEMETHOD_3, model.getPurchaseMethod())
                && StringUtils.isBlank(model.getTryApplyCode())) {
            throw new DerpException("OA审批且采购方式为试单，则试单合同号不能为空");
        }
    }

    /**
     * 获取框架合同信息
     * @param frameContractDTO
     * @return
     */
    private PurchaseFrameContractDTO getFrameContractInfo(PurchaseFrameContractDTO frameContractDTO) {
        PurchaseFrameContractDTO purchaseFrameContractDTO = frameContractDao.getDetail(frameContractDTO);
        return purchaseFrameContractDTO;
    }

    @Override
    public List<Map<String, Object>> getPurchaseOrderTypeNum(PurchaseOrderDTO dto, User user) {
        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if(buIds.isEmpty()) {
                return null;
            }
            dto.setBuIds(buIds);
        }
        List<Map<String, Object>> mapList = purchaseOrderDao.getPurchaseOrderTypeNum(dto) ;

        Long total = new Long(0) ;

        for (Map<String, Object> object : mapList) {
            if(object.get("num") != null) {
                total += (Long) object.get("num") ;
            }
        }

        Map<String, Object> totalMap = new HashMap<String, Object>() ;
        totalMap.put("total", total) ;
        mapList.add(totalMap) ;

        return mapList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean callOAAudit(User user, PurchaseSaveOAAuditDTO dto) throws Exception {
        ResponseBean responseBean = new ResponseBean();

        boolean isNull = new EmptyCheckUtils().addObject(dto.getFrameContractFlag())
                .addObject(dto.getPurchaseMethod()).addObject(dto.getComtyType())
                .addObject(dto.getCodes()).addObject(dto.getTradeTerms())
                .addObject(dto.getCooperationModel()).addObject(dto.getPurchaseQuota())
                .addObject(dto.getSkuNum()).addObject(dto.getPurchaseTotalNum())
                .addObject(dto.getEsGrossMargin()).addObject(dto.getPurchaseCurreny())
                .addObject(dto.getPurchaseAmount()).addObject(dto.getPurchaseRmbAmount())
                .addObject(dto.getNewProductSingleAmount()).addObject(dto.getSupplyProductSingleAmount())
                .addObject(dto.getSettlementModel()).addObject(dto.getSettlementProvision())
                .addObject(dto.getBankChangeProvision())
                .addObject(dto.getCounterpartContSignComy()).addObject(dto.getSupplierDesc())
                .addObject(dto.getSealOrder()).addObject(dto.getSealType()).addObject(dto.getContractList())
                .empty();
        if (isNull) {
            //输入信息不完整
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        if(dto.getContractList().isEmpty()) {
            //输入信息不完整
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        String[] split = dto.getIds().split(",");
        dto.setCreater(user.getCode());

        /**推送OA*/
        WorkflowRequestInfoWrap model = new WorkflowRequestInfoWrap();
        model.setCreatorCode(user.getCode());
        model.setRequestLevel("0");
        model.setRequestName("商品采购执行合同（PO）评审表-" + user.getName() + "-" + TimeUtils.formatDay() + "-经分销");

        WorkflowBaseInfoWrap baseInfoWrap = new WorkflowBaseInfoWrap();
        baseInfoWrap.setWorkflowId(ApolloUtil.oaPurchaseWorkflowId);
        baseInfoWrap.setWorkflowName("商品采购执行合同（PO）评审表-" + user.getName() + "-" + TimeUtils.formatDay() + "-经分销");
        model.setBaseInfo(baseInfoWrap);
        WorkflowMainTableInfoWrap mainTableInfoWrap = convertTableInfoWrap(dto);
        model.setMainTableInfo(mainTableInfoWrap);

        /**保存外部唯一编号，避免重复推送OA*/
        String codes = dto.getCodes();
        if(StringUtils.isNotBlank(codes)) {
            String[] split1 = codes.split(",");
            for (String code : split1) {
                OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
                orderExternalCodeModel.setExternalCode(code);
                orderExternalCodeModel.setOrderSource(11);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 11 采购单
                try {
                    orderExternalCodeDao.save(orderExternalCodeModel);
                } catch (Exception e) {
                    throw new DerpException("已经推送oa（不能重复推送）") ;
                }
            }
        }

        // 发起OA审批流程
        long startTime = System.currentTimeMillis();
        String requestId = OAUtils.getOARequestId(model);
        if(StringUtils.isBlank(requestId)) {
            throw new RuntimeException("获取流程号失败:"+ codes);
        }
//        String requestId = UUID.randomUUID().toString();

        // 保存到唯一单号表
        saveContractToMongo(dto.getCodes(), requestId);

        // 更新采购单数据
        List<PurchaseOrderDTO> updateList = new ArrayList<>();
        for (String idStr : split) {
            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
            purchaseOrderDTO.setId(Long.valueOf(idStr));
            purchaseOrderDTO.setRequestId(requestId);
            purchaseOrderDTO.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_002);
            purchaseOrderDTO.setSubmiter(user.getId());
            purchaseOrderDTO.setSubmiterName(user.getName());
            purchaseOrderDTO.setSubmitDate(TimeUtils.getNow());
            updateList.add(purchaseOrderDTO);
        }

        // 批量更新
        purchaseOrderDao.batchUpdate(updateList);

        // 记录发送OA审批的报文日志
        CommonMQLog log = new CommonMQLog();
        log.setKeyword(codes);
        log.setPoint(OAAPIMethodEnum.PURCHASE_OA_METHOD_001.getPoint());
        log.setMethod(OAAPIMethodEnum.PURCHASE_OA_METHOD_001.getMethod());
        log.setKeywordName(OAAPIMethodEnum.PURCHASE_OA_METHOD_001.getKeyWordName());
        log.setStartDate(startTime);
        log.setTags(MQPushApiEnum.PUSH_API_LOG_COMMON.getTags());
        log.setTopics(MQPushApiEnum.PUSH_API_LOG_COMMON.getTopic());
        log.setMerchantName(dto.getMerchantName());
        log.setDesc("采购发起OA审批流程");
        //接口名称
        log.setModel(OAAPIMethodEnum.PURCHASE_OA_METHOD_001.getApiName());

        JSONObject jsonObject=JSONObject.fromObject(log);
        jsonObject.put("requestMessage", JSONObject.fromObject(model));
        //设置响应报文
        Map returnMap = new HashMap();
        returnMap.put("requestId", requestId);
        jsonObject.put("returnMessage", JSONObject.fromObject(returnMap));

        rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_API_LOG_COMMON.getTopic(),
                MQPushApiEnum.PUSH_API_LOG_COMMON.getTags());

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(dto.getBuId());
        emailUserDTO.setBuName(dto.getBuName());
        emailUserDTO.setMerchantId(dto.getMerchantId());
        emailUserDTO.setMerchantName(dto.getMerchantName());
        emailUserDTO.setOrderCode(dto.getCodes());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
        emailUserDTO.setSupplier(dto.getSupplierName());
        emailUserDTO.setPoNum(dto.getPoNo());
        emailUserDTO.setStatus("已提交");
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setAuditorId(user.getId());
        emailUserDTO.setSubmitId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setModifyId(user.getId());
        String auditMethod = DERP_ORDER.PURCHASEORDER_AUDITMETHOD_1;
        String auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_auditMethodList, auditMethod);
        emailUserDTO.setAuditMethod(auditMethodLabel);
        // 发送邮件
        sendEmail(emailUserDTO);
        /**记录操作日志*/
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, dto.getCodes(), "提交OA审核", null, null);

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @Override
    public List<AttachmentInfoMongo> listContract(User user, PurchaseOrderDTO dto) throws Exception{
        String basePath = ApolloUtilDB.orderBasepath+"/puchaseContract/" + dto.getMerchantId();

        String[] split = dto.getCodes().split(",");
        List<AttachmentInfoMongo> attachmentInfoMongoList = new ArrayList<>();

        // 获取本地服务器中的合同文件，上传蓝精灵服务器，并生产attachmentInfoMongoList返回前端显示
        for (String code : split) {
            String fileName = getPDFFileName(code) ;
            String pdfFileName = basePath+"/"+ fileName ;
            File file = new File(pdfFileName) ;

            if(file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                IOUtils.copy(fileInputStream, bos);
                String fileBase64Str = Base64.encodeBase64String(bos.toByteArray());
                String fileNameStr = file.getAbsolutePath() + File.pathSeparator + file.getName();
                String smurfsUrl = sendFileToSmurfs(fileBase64Str, fileNameStr);

                AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo();
                attachmentInfoMongo.setAttachmentUrl(smurfsUrl);
                attachmentInfoMongo.setAttachmentName(file.getName());
                attachmentInfoMongoList.add(attachmentInfoMongo);
            }
        }

        return attachmentInfoMongoList;
    }

    private void sendEmail(ReminderEmailUserDTO emailUserDTO){
        JSONObject json = JSONObject.fromObject(emailUserDTO);
        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
        }
    }

    /**
     * 保存到唯一单号表
     * @param orderCode
     * @param requestId
     */
    private void saveContractToMongo(String orderCode, String requestId) {
        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("orderCode", orderCode) ;

        ContractNoMongo contractNoMongo = contractNoMongoDao.findOne(queryMap);

        if(contractNoMongo == null) {
            contractNoMongo = new ContractNoMongo() ;
            contractNoMongo.setContractNo(requestId);
            contractNoMongo.setOrderCode(orderCode);
            contractNoMongo.setType(DERP.UNIQUE_ID_CGDH);
            contractNoMongoDao.insert(contractNoMongo);
        }else {
            contractNoMongo.setContractNo(requestId);
            JSONObject json = JSONObject.fromObject(contractNoMongo) ;
            contractNoMongoDao.update(queryMap, json) ;
        }
    }

    private String sendFileToSmurfs(String encodeBase64String, String fileNameStr) {
        String ext = fileNameStr.substring(fileNameStr.lastIndexOf(".")+1);
        String fileName = fileNameStr.substring(0,fileNameStr.lastIndexOf("."));

        //转base64
        JSONObject jsonObject = new JSONObject();//推送内容
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName",fileName);
        jsonObject.put("fileBytes", encodeBase64String);
        jsonObject.put("fileExt",ext);
        jsonObject.put("fileSize",String.valueOf(fileSize(encodeBase64String)));
        String url=null;
        try {
            String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
            System.out.println(resultMsg);
            JSONObject jsonObj = JSONObject.fromObject(resultMsg);
            url = jsonObj.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
            url=null;
        }
        return url;
    }

    /**
     *
     * @param
     * @return
     */
    public  Integer fileSize(String base64Str){
        String str = base64Str.substring(22); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
        Integer equalIndex= str.indexOf("=");//2.找到等号，把等号也去掉
        if(str.indexOf("=")>0) {
            str=str.substring(0, equalIndex);
        }
        Integer strLength=str.length();//3.原来的字符流大小，单位为字节
        Integer size=strLength-(strLength/8)*2;//4.计算后得到的文件流大小，单位为字节
        return size;
    }

    private WorkflowMainTableInfoWrap convertTableInfoWrap(PurchaseSaveOAAuditDTO dto) throws Exception{
        WorkflowMainTableInfoWrap tableInfoWrap = new WorkflowMainTableInfoWrap();
        tableInfoWrap.setTableFields(convertFieldWrapList(dto));
        return tableInfoWrap;
    }

    private List<WorkflowRequestTableFieldWrap> convertFieldWrapList(PurchaseSaveOAAuditDTO form) throws Exception{
        List<WorkflowRequestTableFieldWrap> fieldWrapList = new ArrayList<>();

        List<AttachmentInfoMongo> contractList = form.getContractList();
        StringBuilder urlBuilder = new StringBuilder();
        StringBuilder fileTypeBuilder = new StringBuilder();
        if(contractList != null && !contractList.isEmpty()) {
            for (AttachmentInfoMongo attachmentInfoMongo : contractList) {
                String attachmentUrl = attachmentInfoMongo.getAttachmentUrl();
                String attachmentName = attachmentInfoMongo.getAttachmentName();
                if(StringUtils.isNotBlank(attachmentUrl)) {
                    if(urlBuilder.length() > 0) {
                        urlBuilder.append("@file@").append(attachmentUrl);
                        fileTypeBuilder.append("@file@").append(attachmentName);
                    }else {
                        urlBuilder.append(attachmentUrl);
                        fileTypeBuilder.append("http:").append(attachmentName);
                    }
                }
            }
        }

        List<AttachmentInfoMongo> commonFileList = form.getCommonFileList();
        StringBuilder urlCommonBuilder = new StringBuilder();
        StringBuilder fileTypeCommonBuilder = new StringBuilder();
        if(commonFileList != null && !commonFileList.isEmpty()) {
            for (AttachmentInfoMongo attachmentInfoMongo : commonFileList) {
                if(urlCommonBuilder.length() > 0) {
                    urlCommonBuilder.append("@file@").append(attachmentInfoMongo.getAttachmentUrl());
                    fileTypeCommonBuilder.append("@file@").append(attachmentInfoMongo.getAttachmentName());
                }else {
                    urlCommonBuilder.append(attachmentInfoMongo.getAttachmentUrl());
                    fileTypeCommonBuilder.append("http:").append(attachmentInfoMongo.getAttachmentName());
                }

            }
        }

        // 获取客户信息
        Long customerId = form.getCustomerId();
        CustomerInfoMogo customer = new CustomerInfoMogo();
        if(customerId != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("customerid", customerId);
            customer = customerInfoMongoDao.findOne(map);
        }

        WorkflowRequestTableFieldWrap fieldWrap0 = convertFieldWrap("lyxt", "string", ApolloUtil.oaLyxt, null);
        WorkflowRequestTableFieldWrap fieldWrap1 = convertFieldWrap("zdr", "string", form.getCreater(), "P");
        WorkflowRequestTableFieldWrap fieldWrap2 = convertFieldWrap("ywy", "string", form.getBusinessManager(), "P");
        WorkflowRequestTableFieldWrap fieldWrap3 = convertFieldWrap("zdrq", "string", TimeUtils.format(new Date(), TimeUtils.YYYY_MM_DD), null);
        WorkflowRequestTableFieldWrap fieldWrap4 = convertFieldWrap("zdbm", "string", form.getBusinessManager(), "PD");
        WorkflowRequestTableFieldWrap fieldWrap5 = convertFieldWrap("sfwkjxddd", "string", (form.getFrameContractFlag() != null && form.getFrameContractFlag()) ? "0" : "1", null);
        WorkflowRequestTableFieldWrap fieldWrap6 = null;
        if(StringUtils.isNotBlank(form.getFrameContractDataId())) {
            fieldWrap6 = convertFieldWrap("kjhtbh", "string", form.getFrameContractDataId(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap7 = null;
        if(StringUtils.isNotBlank(form.getTryApplyDataId())) {
            fieldWrap7 = convertFieldWrap("lxsdbh", "string", form.getTryApplyDataId(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap8 = convertFieldWrap("cgbh", "string", form.getPoNo(), null);
        WorkflowRequestTableFieldWrap fieldWrap9 = convertFieldWrap("htmb", "string", form.getContractVersion(), null);
        WorkflowRequestTableFieldWrap fieldWrap10 = convertFieldWrap("htlx", "string", DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTTYPE_0, null);
        WorkflowRequestTableFieldWrap fieldWrap11 = convertFieldWrap("wsqyzt", "string", form.getOurContSignComy(), "MO");
        WorkflowRequestTableFieldWrap fieldWrap12 = convertFieldWrap("xdfmc", "string", form.getCounterpartContSignComy(), "E");
        WorkflowRequestTableFieldWrap fieldWrap13 = convertFieldWrap("gyslx", "string", form.getSupplierType(), null);
        WorkflowRequestTableFieldWrap fieldWrap14 = null;
        if(StringUtils.isNotBlank(form.getOtherSupplierType())) {
            fieldWrap14 = convertFieldWrap("qgys", "string", form.getOtherSupplierType(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap15 = convertFieldWrap("xyqsrq", "string", form.getContractStartTime(), null);
        WorkflowRequestTableFieldWrap fieldWrap16 = convertFieldWrap("xyjsrq", "string", form.getContractEndTime(), null);
        WorkflowRequestTableFieldWrap fieldWrap17 = convertFieldWrap("cglx", "string", form.getPurchaseType(), null);
        WorkflowRequestTableFieldWrap fieldWrap18 = convertFieldWrap("ywdydcwjl", "string", form.getFinanceManager(), null);
        WorkflowRequestTableFieldWrap fieldWrap19 = convertFieldWrap("splx", "string", form.getComtyType(), null);
        WorkflowRequestTableFieldWrap fieldWrap20 = null;
        if(StringUtils.isNotBlank(form.getOtherComty())) {
            fieldWrap20 = convertFieldWrap("qtsp", "string", form.getOtherComty(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap21 = convertFieldWrap("cgfs", "string", form.getPurchaseMethod(), null);
        WorkflowRequestTableFieldWrap fieldWrap22 = convertFieldWrap("jhfs", "string", form.getTradeTerms() == null ? null : DERP_ORDER.OA_DELIVERYTYPE_MAP.get(form.getTradeTerms()), null);
        WorkflowRequestTableFieldWrap fieldWrap23 = null;
        if(StringUtils.isNotBlank(form.getOtherTradeTerms())) {
            fieldWrap23 = convertFieldWrap("qtjhfs", "string", form.getOtherTradeTerms(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap24 = convertFieldWrap("hzms", "string", form.getCooperationModel(), null);
        WorkflowRequestTableFieldWrap fieldWrap25 = null;
        if(StringUtils.isNotBlank(form.getOtherCooperationModel())) {
            fieldWrap25 = convertFieldWrap("qthzms", "string", form.getOtherCooperationModel(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap26 = convertFieldWrap("cgkyedyrmb", "string", form.getPurchaseQuota() != null ? form.getPurchaseQuota().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        WorkflowRequestTableFieldWrap fieldWrap27 = convertFieldWrap("skuslg", "string", form.getSkuNum().toString(), null);
        WorkflowRequestTableFieldWrap fieldWrap28 = convertFieldWrap("cgzslj", "string", form.getPurchaseTotalNum().toString(), null);
        WorkflowRequestTableFieldWrap fieldWrap29 = convertFieldWrap("cgbzxz", "string", DERP_SYS.OA_CURRENCY_MAP.containsKey(form.getPurchaseCurreny()) ? DERP_SYS.OA_CURRENCY_MAP.get(form.getPurchaseCurreny()) : null, "C");
//        WorkflowRequestTableFieldWrap fieldWrap30 = convertFieldWrap("qcgbz", "string", null, null);
        WorkflowRequestTableFieldWrap fieldWrap31 = convertFieldWrap("cgje", "string", form.getPurchaseAmount() != null ? form.getPurchaseAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        WorkflowRequestTableFieldWrap fieldWrap32 = convertFieldWrap("cgjezsrmbyrmb", "string", form.getPurchaseRmbAmount() != null ? form.getPurchaseRmbAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        WorkflowRequestTableFieldWrap fieldWrap33 = convertFieldWrap("mll", "string", form.getEsGrossMargin() != null ? form.getEsGrossMargin().setScale(4, BigDecimal.ROUND_HALF_UP).toString() : null, "S");
        WorkflowRequestTableFieldWrap fieldWrap34 = convertFieldWrap("xpdccgjeyrmz", "string", form.getNewProductSingleAmount() != null ? form.getNewProductSingleAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        WorkflowRequestTableFieldWrap fieldWrap35 = convertFieldWrap("bhdccgjeyrmz", "string", form.getSupplyProductSingleAmount() != null ? form.getSupplyProductSingleAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        WorkflowRequestTableFieldWrap fieldWrap36 = null;
        if(customer != null) {
            fieldWrap36 = convertFieldWrap("khmc", "string", customer == null ? null : customer.getMainId(), "E");
        }
        WorkflowRequestTableFieldWrap fieldWrap37 = convertFieldWrap("khyfk", "string", (form.getHasCustomerPrepayment() != null && form.getHasCustomerPrepayment()) ? "0" : "1", null);
        WorkflowRequestTableFieldWrap fieldWrap38 = null;
        if(StringUtils.isNotBlank(form.getPreCustomerPaymentCurreny())) {
            fieldWrap38 = convertFieldWrap("khyfkbzxz", "string", DERP_SYS.OA_CURRENCY_MAP.containsKey(form.getPreCustomerPaymentCurreny()) ? DERP_SYS.OA_CURRENCY_MAP.get(form.getPreCustomerPaymentCurreny()) :null, "C");
        }

        WorkflowRequestTableFieldWrap fieldWrap40= null;
        if(form.getPreCustomerPaymentAmount() != null) {
            fieldWrap40 = convertFieldWrap("khyfkje", "string", form.getPreCustomerPaymentAmount() == null ? null : form.getPreCustomerPaymentAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString(), null);
        }
        WorkflowRequestTableFieldWrap fieldWrap41 = convertFieldWrap("jsfs", "string", form.getSettlementModel(), null);
        WorkflowRequestTableFieldWrap fieldWrap42 = convertFieldWrap("jstk", "string", form.getSettlementProvision(), null);
        WorkflowRequestTableFieldWrap fieldWrap43 = convertFieldWrap("jszqzrr", "string", form.getSettlementDate(), null);
        WorkflowRequestTableFieldWrap fieldWrap44 = convertFieldWrap("yfk", "string", form.getHasPrepayment()? "0" : "1", null);
        WorkflowRequestTableFieldWrap fieldWrap45 = convertFieldWrap("ydyhzhbgtk", "string", form.getBankChangeProvision(), null);
        WorkflowRequestTableFieldWrap fieldWrap46 = null;
        if(StringUtils.isNotBlank(form.getPrePaymentCurrency())){
            fieldWrap46 = convertFieldWrap("yfkbzxz", "string", DERP_SYS.OA_CURRENCY_MAP.containsKey(form.getPrePaymentCurrency()) ? DERP_SYS.OA_CURRENCY_MAP.get(form.getPrePaymentCurrency()):null, "C");
        }
//        WorkflowRequestTableFieldWrap fieldWrap47 = convertFieldWrap("qyfkbz", "string", form.getCreater(), null);
        WorkflowRequestTableFieldWrap fieldWrap48 = null;
        if(form.getPrePaymentAmount() != null) {
            fieldWrap48 = convertFieldWrap("yfkje", "string", form.getPrePaymentAmount() != null? form.getPrePaymentAmount().setScale(6, BigDecimal.ROUND_HALF_UP).toString() : null, null);
        }
        WorkflowRequestTableFieldWrap fieldWrap49 = convertFieldWrap("gysms", "string", form.getSupplierDesc(), null);
        WorkflowRequestTableFieldWrap fieldWrap50 = convertFieldWrap("gysspms", "string", form.getSupplierMerchandiseDesc(), null);
        WorkflowRequestTableFieldWrap fieldWrap51 = convertFieldWrap("yysx", "string", form.getSealOrder(), null);
        WorkflowRequestTableFieldWrap fieldWrap52 = convertFieldWrap("yylx", "string", form.getSealType(), null);
        WorkflowRequestTableFieldWrap fieldWrap53 = convertFieldWrap("wjsc", fileTypeBuilder.toString(), urlBuilder.toString(), null);
        WorkflowRequestTableFieldWrap fieldWrap54 = null;
        if(StringUtils.isNotBlank(fileTypeCommonBuilder)) {
            fieldWrap54 = convertFieldWrap("qfzxwjsc", fileTypeCommonBuilder.toString(), urlCommonBuilder.toString(), null);
        }

        fieldWrapList.add(fieldWrap0);
        fieldWrapList.add(fieldWrap1);
        fieldWrapList.add(fieldWrap2);
        fieldWrapList.add(fieldWrap3);
        fieldWrapList.add(fieldWrap4);
        fieldWrapList.add(fieldWrap5);
        if(fieldWrap6 != null) {
            fieldWrapList.add(fieldWrap6);
        }
        if(fieldWrap7 != null) {
            fieldWrapList.add(fieldWrap7);
        }
        fieldWrapList.add(fieldWrap8);
        fieldWrapList.add(fieldWrap9);
        fieldWrapList.add(fieldWrap10);
        fieldWrapList.add(fieldWrap11);
        fieldWrapList.add(fieldWrap12);
        fieldWrapList.add(fieldWrap13);
        if(fieldWrap14 != null) {
            fieldWrapList.add(fieldWrap14);
        }
        fieldWrapList.add(fieldWrap15);
        fieldWrapList.add(fieldWrap16);
        fieldWrapList.add(fieldWrap17);
        fieldWrapList.add(fieldWrap18);
        fieldWrapList.add(fieldWrap19);
        if(fieldWrap20 != null) {
            fieldWrapList.add(fieldWrap20);
        }
        fieldWrapList.add(fieldWrap21);
        fieldWrapList.add(fieldWrap22);
        if(fieldWrap23 != null) {
            fieldWrapList.add(fieldWrap23);
        }
        fieldWrapList.add(fieldWrap24);
        if(fieldWrap25 != null) {fieldWrapList.add(fieldWrap25);}
        fieldWrapList.add(fieldWrap26);
        fieldWrapList.add(fieldWrap27);
        fieldWrapList.add(fieldWrap28);
        fieldWrapList.add(fieldWrap29);
//        fieldWrapList.add(fieldWrap30);
        fieldWrapList.add(fieldWrap31);
        fieldWrapList.add(fieldWrap32);
        fieldWrapList.add(fieldWrap33);
        fieldWrapList.add(fieldWrap34);
        fieldWrapList.add(fieldWrap35);
        if(fieldWrap36 != null) {
            fieldWrapList.add(fieldWrap36);
        }
        fieldWrapList.add(fieldWrap37);
        if(fieldWrap38 != null) {
            fieldWrapList.add(fieldWrap38);
        }
//        fieldWrapList.add(fieldWrap39);
        if(fieldWrap40 != null) {fieldWrapList.add(fieldWrap40);}
        fieldWrapList.add(fieldWrap41);
        fieldWrapList.add(fieldWrap42);
        fieldWrapList.add(fieldWrap43);
        fieldWrapList.add(fieldWrap44);
        fieldWrapList.add(fieldWrap45);
        if(fieldWrap46 != null) {
            fieldWrapList.add(fieldWrap46);
        }
//        fieldWrapList.add(fieldWrap47);
        if(fieldWrap48 != null) {fieldWrapList.add(fieldWrap48);}
        fieldWrapList.add(fieldWrap49);
        fieldWrapList.add(fieldWrap50);
        fieldWrapList.add(fieldWrap51);
        fieldWrapList.add(fieldWrap52);
        fieldWrapList.add(fieldWrap53);
        if(fieldWrap54 != null) {
            fieldWrapList.add(fieldWrap54);
        }
        return fieldWrapList;
    }

    private WorkflowRequestTableFieldWrap convertFieldWrap(String columnName, String columnType, String columnValue, String conversionType){
        WorkflowRequestTableFieldWrap fieldWrap = new WorkflowRequestTableFieldWrap();
        fieldWrap.setColumnName(columnName);
        fieldWrap.setColumnType(columnType);
        fieldWrap.setColumnValue(columnValue);
        fieldWrap.setConversionType(conversionType);
        return fieldWrap;
    }

}
