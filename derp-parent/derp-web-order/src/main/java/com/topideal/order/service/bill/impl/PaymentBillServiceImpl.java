package com.topideal.order.service.bill.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc07.Details;
import com.topideal.api.nc.nc07.ReceiveBillJsonRoot;
import com.topideal.api.nc.nc08.ReceiveHcInvalidRoot;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.purchase.PurchaseInvoiceDao;
import com.topideal.dao.purchase.PurchaseInvoiceItemDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.bill.PaymentBillService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.webService.OAUtils;
import com.topideal.webService.oa.o_01.CreatRequestIdRequest;
import com.topideal.webService.oa.o_01.CtreatRequestIdItemRequest;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentBillServiceImpl implements PaymentBillService {

    /* 打印日志 */
    protected Logger LOGGER = LoggerFactory.getLogger(PaymentBillServiceImpl.class);

    @Autowired
	private PaymentBillDao paymentBillDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	@Autowired
    private SettlementConfigDao settlementConfigDao ;
	@Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private AdvancePaymentBillItemDao advancePaymentBillItemDao ;
    @Autowired
    private AdvancePaymentBillDao advancePaymentBillDao ;
    @Autowired
    private AdvancePaymentRecordItemDao advancePaymentRecordItemDao ;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private OperateLogDao operateLogDao ;
    @Autowired
    private PaymentItemDao paymentItemDao ;
    @Autowired
    private PaymentCostItemDao paymentCostItemDao ;
    @Autowired
    private PaymentSummaryDao paymentSummaryDao ;
    @Autowired
    private PaymentVerificateItemDao paymentVerificateItemDao ;
    @Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private PaymentPurchaseRelDao paymentPurchaseRelDao ;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao ;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao ;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao ;
    @Autowired
    private ContractNoMongoDao contractNoMongoDao ;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao ;
    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
    @Autowired
    private AdvancePaymentBillRelDao advancePaymentBillRelDao ;
    @Autowired
    private CommonBusinessService commonBusinessService ;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private PurchaseInvoiceDao purchaseInvoiceDao;
    @Autowired
    private PurchaseInvoiceItemDao purchaseInvoiceItemDao;
	//外部单号
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao ;

	@SuppressWarnings("unchecked")
	@Override
	public PaymentBillDTO listPaymentBill(PaymentBillDTO dto, User user) {
		
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

		 if(StringUtils.isBlank(dto.getPoNo())) {
             dto = paymentBillDao.getListByPage(dto) ;
             return dto;
         }

        PaymentBillDTO result = paymentBillDao.getListByPageWithItem(dto) ;
        if(result != null && result.getList() != null && result.getList().size() > 0) {
            return result;
        }

        result = paymentBillDao.getListByPageWithCostItem(dto) ;
        return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user) throws SQLException {
		
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
		
		dto = purchaseOrderDao.getPaymentListByPage(dto) ;
		
		List<PurchaseOrderDTO> list = dto.getList();
		
		for (PurchaseOrderDTO d : list) {
			// 设值采购总金额
			PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
			itemModel.setPurchaseOrderId(d.getId());
			List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(itemModel);
			BigDecimal amount = new BigDecimal(0);
			
			for (int i = 0; i < itemList.size(); i++) {
				PurchaseOrderItemModel item = itemList.get(i);
				
				if(item.getAmount()==null) {
                    continue;
				}
				
				amount = amount.add(item.getAmount());
			}
			
			d.setGoodsAmount(amount);
		}
		
		return dto;
	}

	@Override
	public void getCheckData(List<Long> idList) throws SQLException {
		Long supplierId = null ;
        Long buId = null ;
        String currency = null ;
        String businessModel=null;

        for (Long purchaseId: idList) {

            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);

            if(!(DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrderModel.getStatus())
                || DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())
                || DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())
                || DERP_ORDER.PURCHASEORDER_STATUS_002.equals(purchaseOrderModel.getStatus()))){
                throw new DerpException("只能选择 【待审核】【已审核】【部分入库】【已入库】的采购订单") ;
            }

            if(supplierId == null){
                supplierId = purchaseOrderModel.getSupplierId() ;
            }

            if(buId == null){
                buId = purchaseOrderModel.getBuId() ;
            }

            if(currency == null){
                currency = purchaseOrderModel.getCurrency() ;
            }

            if(businessModel==null){
                businessModel=purchaseOrderModel.getBusinessModel();
            }

            if(supplierId != null && supplierId.longValue() != purchaseOrderModel.getSupplierId().longValue()){

                throw new DerpException("所选采购单不为相同供应商") ;

            }

            if(buId != null && buId.longValue() != purchaseOrderModel.getBuId().longValue()){

                throw new DerpException("所选采购单不为相同事业部") ;

            }

            if(currency != null && !currency.equals(purchaseOrderModel.getCurrency())){
                throw new DerpException("所选采购单不为相同币种") ;
            }

            if(businessModel != null && !businessModel.equals(purchaseOrderModel.getBusinessModel())){
                throw new DerpException("所选采购单不为相同类型的采购单") ;
            }
        }
		
	}

	@Override
	public PaymentBillDTO getAddPageInfo(List<Long> idList,String type) throws Exception {
		
		String currency = null;
        Long supplierId = null ;
        Long merchantId = null ;
        String merchantName = null ;
        Long buId = null ;
        String buName = null ;

        PaymentBillDTO dto = new PaymentBillDTO() ;
        List<PaymentItemDTO> itemList = new ArrayList<PaymentItemDTO>() ;
        List<PaymentVerificateItemDTO> veriItemList = new ArrayList<PaymentVerificateItemDTO>() ;

        BigDecimal totalAmount = new BigDecimal(0);
        //可用核销金额，汇总时等于总金额
        BigDecimal veriTotalAmount = new BigDecimal(0);
        
        //汇总分类集合
        Map<String, PaymentSummaryDTO> summaryMap = new HashMap<>() ;

        //单据类型为采购发票单时，根据采购单+货号汇总数量、金额（含税）、税额
        Map<String,PurchaseInvoiceItemDTO> itemMap=new HashMap<>();
        //单据类型为采购发票单时，限制采购单只汇总一次核销记录
        Map<String,PurchaseInvoiceModel> orderMap=new HashMap<>();
        //记录采购单+po号的唯一
        Map<String,PaymentItemDTO> poOrderMap=new HashMap<>();


        StringBuffer invoiceCode = new StringBuffer();

        for (Long purchaseId:idList) {

            //根据采购发票id查询采购订单发票，获取采购单
            PurchaseInvoiceModel purchaseInvoiceModel=null;
            if(DERP_ORDER.PAYMENT_BILL_TYPE_STATE_2.equals(type)){
                purchaseInvoiceModel=purchaseInvoiceDao.searchById(purchaseId);
                if(purchaseInvoiceModel!=null){
                    purchaseId=purchaseInvoiceModel.getPurchaseOrderId();

                    invoiceCode.append(purchaseInvoiceModel.getCode()).append(",");
                }
            }

            //查询采购单
            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);

            if(currency == null) {
            	currency = purchaseOrderModel.getCurrency() ;
            }
            
            if(supplierId == null) {
            	supplierId = purchaseOrderModel.getSupplierId() ;
            }
            
            if(merchantId == null) {
            	merchantId = purchaseOrderModel.getMerchantId() ;
            }
            
            if(merchantName == null) {
            	merchantName = purchaseOrderModel.getMerchantName() ;
            }
            
            if(buId == null) {
            	buId = purchaseOrderModel.getBuId() ;
            }
            
            if(buName == null) {
            	buName = purchaseOrderModel.getBuName() ;
            }

            SettlementConfigModel queryModel = new SettlementConfigModel() ;

            //判断采购单的业务模式是否为“采销执行”
            if(DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_3.equals(purchaseOrderModel.getBusinessModel())){
                dto.setIsEndownment(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_0);
            }else{
                dto.setIsEndownment(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_1);
            }
            
            if(DERP_ORDER.PURCHASEORDER_STATUS_002.equals(purchaseOrderModel.getStatus())
            		|| DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())
            		|| DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrderModel.getStatus())) {
            	
            	queryModel.setProjectName("品牌经销在途");
            	
            }else if(DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrderModel.getStatus())) {
            	queryModel.setProjectName("经销库存");
            }
            
            if(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3.equals(purchaseOrderModel.getBusinessModel())) {
            	queryModel.setProjectName("商品采购-采销-B");
            }
            
            queryModel = settlementConfigDao.searchByModel(queryModel) ;

            //如果单据类型为采购发票单，则获取采购发票表体
            if(DERP_ORDER.PAYMENT_BILL_TYPE_STATE_2.equals(type)){
                PurchaseInvoiceItemDTO itemInvoice=new PurchaseInvoiceItemDTO();
                itemInvoice.setPurchaseInvoiceId(purchaseInvoiceModel.getId());
                List<PurchaseInvoiceItemDTO> purchaseInvoiceItemModel=purchaseInvoiceItemDao.getPurchaseInvoiceItemModel(itemInvoice);

                for (PurchaseInvoiceItemDTO itemModel : purchaseInvoiceItemModel) {

                    //封装表体
                    PaymentItemDTO itemDto = new PaymentItemDTO();

                    itemDto.setPurchaseCode(purchaseOrderModel.getCode());
                    itemDto.setPurchaseId(purchaseOrderModel.getId());
                    itemDto.setGoodsId(itemModel.getGoodsId());
                    itemDto.setGoodsName(itemModel.getGoodsName());
                    itemDto.setGoodsNo(itemModel.getGoodsNo());
                    itemDto.setNum(itemModel.getNum());
                    itemDto.setPurchaseAmount(itemModel.getAmount());
                    itemDto.setPurchaseTaxAmount(itemModel.getTaxAmount());
                    itemDto.setTax(itemModel.getTax());
                    itemDto.setPoNo(itemModel.getPoNo());
                    itemDto.setType(itemModel.getType());

                    if (queryModel != null) {
                        itemDto.setProjectId(queryModel.getId());
                        itemDto.setProjectName(queryModel.getProjectName());
                        itemDto.setParentProjectId(queryModel.getParentId());
                        itemDto.setParentProjectName(queryModel.getParentProjectName());
                    }


                    //计算汇总
                    PaymentSummaryDTO paymentSummaryDTO = summaryMap.get(itemDto.getProjectName());

                    if (paymentSummaryDTO == null) {
                        paymentSummaryDTO = new PaymentSummaryDTO();

                        if (queryModel != null) {
                            paymentSummaryDTO.setProjectId(queryModel.getId());
                            paymentSummaryDTO.setProjectName(queryModel.getProjectName());
                            paymentSummaryDTO.setParentProjectId(queryModel.getParentId());
                            paymentSummaryDTO.setParentProjectName(queryModel.getParentProjectName());
                        }

                        paymentSummaryDTO.setSettlementAmount(new BigDecimal(0));
                        paymentSummaryDTO.setSettlementTaxAmount(new BigDecimal(0));
                        paymentSummaryDTO.setCurrency(currency);
                        paymentSummaryDTO.setTax(new BigDecimal(0));
                    }

                    BigDecimal settlementAmount = paymentSummaryDTO.getSettlementAmount();
                    BigDecimal settlementTaxAmount = paymentSummaryDTO.getSettlementTaxAmount();
                    BigDecimal tax = paymentSummaryDTO.getTax();


                    //汇总表体，过滤掉汇总的记录
                    if("0".equals(itemDto.getType())){

                        if (itemModel.getTaxAmount() != null) {
                            totalAmount = totalAmount.add(itemModel.getTaxAmount());
                            veriTotalAmount = veriTotalAmount.add(itemModel.getTaxAmount());
                        }

                        //根据采购单+货号的维度，获取已经存在的
                        if (itemMap.containsKey(purchaseOrderModel.getCode() + "_" + itemModel.getGoodsId())) {
                            PurchaseInvoiceItemDTO item = itemMap.get(purchaseOrderModel.getCode() + "_" + itemModel.getGoodsId());

                            //计算结算金额（不含税）、结算金额（含税）、税额
                            settlementAmount = settlementAmount.add(itemModel.getAmount());
                            settlementTaxAmount = settlementTaxAmount.add(itemModel.getTaxAmount() == null ? new BigDecimal(0) : itemModel.getTaxAmount());
                            tax = tax.add(itemModel.getTax() == null ? new BigDecimal(0) : itemModel.getTax());

                            //循环获取以及存在的采购单，重新计算数量、采购总金额、采购含税总金额、以及税额
                            for(PaymentItemDTO itemP:itemList){
                                if(itemP.getPurchaseCode().equals(purchaseOrderModel.getCode())&& itemP.getGoodsId().longValue()==itemModel.getGoodsId().longValue()) {
                                    itemP.setNum(item.getNum() + itemDto.getNum());
                                    itemP.setPurchaseAmount(item.getAmount().add(itemModel.getAmount()));
                                    itemP.setPurchaseTaxAmount(item.getTaxAmount().add(itemModel.getTaxAmount() == null ? new BigDecimal(0) : itemModel.getTaxAmount()));
                                    itemP.setTax(item.getTax().add(itemModel.getTax() == null ? new BigDecimal(0) : itemModel.getTax()));
                                    break;
                                }
                            }
                        } else {
                            settlementAmount = settlementAmount.add(itemModel.getAmount());
                            settlementTaxAmount = settlementTaxAmount.add(itemModel.getTaxAmount() == null ? new BigDecimal(0) : itemModel.getTaxAmount());
                            tax = tax.add(itemModel.getTax() == null ? new BigDecimal(0) : itemModel.getTax());
                            itemList.add(itemDto);
                        }
                        paymentSummaryDTO.setSettlementAmount(settlementAmount);
                        paymentSummaryDTO.setSettlementTaxAmount(settlementTaxAmount);
                        paymentSummaryDTO.setTax(tax);

                        //存储唯一的采购单+货号 汇总
                        itemMap.put(purchaseOrderModel.getCode() + "_" + itemModel.getGoodsId(), itemModel);

                        summaryMap.put(itemDto.getProjectName(), paymentSummaryDTO);

                    }else{
                        //将根据po汇总的记录进行计算
                        String poStr=purchaseOrderModel.getCode()+"_"+purchaseOrderModel.getPoNo();
                        //如果包含相同的采购单+po号，则重新计算
                        if(poOrderMap.containsKey(poStr)) {
                            for (PaymentItemDTO itemP : itemList) {//标识是否为合计行
                                if("1".equals(itemP.getType())){
                                    if (itemP.getPurchaseCode().equals(purchaseOrderModel.getCode()) && itemP.getPoNo().equals(itemModel.getPoNo())) {
                                        itemP.setNum(itemP.getNum() + itemDto.getNum());
                                        itemP.setPurchaseAmount(itemP.getPurchaseAmount().add(itemModel.getAmount()));
                                        itemP.setPurchaseTaxAmount(itemP.getPurchaseTaxAmount().add(itemModel.getTaxAmount() == null ? new BigDecimal(0) : itemModel.getTaxAmount()));
                                        itemP.setTax(itemP.getTax().add(itemModel.getTax() == null ? new BigDecimal(0) : itemModel.getTax()));
                                        break;
                                    }
                                }
                            }
                        }else{
                            itemList.add(itemDto);
                            poOrderMap.put(poStr,itemDto);
                        }
                    }
                }
            }else{
                //查询采购单表体
                PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
                queryItemModel.setPurchaseOrderId(purchaseId);

                //List<PurchaseOrderItemModel> tempItemList = purchaseOrderItemDao.list(queryItemModel);

                List<PurchaseOrderItemModel> tempItemList = purchaseOrderItemDao.getPurchaseOrderItem(queryItemModel);

                for (PurchaseOrderItemModel itemModel : tempItemList) {

                    PaymentItemDTO itemDto = new PaymentItemDTO() ;

                    itemDto.setPurchaseCode(purchaseOrderModel.getCode());
                    itemDto.setPurchaseId(purchaseOrderModel.getId());
                    itemDto.setGoodsId(itemModel.getGoodsId());
                    itemDto.setGoodsName(itemModel.getGoodsName());
                    itemDto.setGoodsNo(itemModel.getGoodsNo());
                    itemDto.setNum(itemModel.getNum());
                    itemDto.setPurchaseAmount(itemModel.getAmount());
                    itemDto.setPurchaseTaxAmount(itemModel.getTaxAmount());
                    itemDto.setTax(itemModel.getTax());
                    itemDto.setPoNo(itemModel.getPoNo());
                    itemDto.setType(itemModel.getType());

                    if(queryModel != null) {
                        itemDto.setProjectId(queryModel.getId());
                        itemDto.setProjectName(queryModel.getProjectName());
                        itemDto.setParentProjectId(queryModel.getParentId());
                        itemDto.setParentProjectName(queryModel.getParentProjectName());
                    }

                    if("0".equals(itemDto.getType())) {
                        if (itemModel.getTaxAmount() != null) {
                            totalAmount = totalAmount.add(itemModel.getTaxAmount());
                            veriTotalAmount = veriTotalAmount.add(itemModel.getTaxAmount());
                        }

                        //计算汇总
                        PaymentSummaryDTO paymentSummaryDTO = summaryMap.get(itemDto.getProjectName());

                        if (paymentSummaryDTO == null) {
                            paymentSummaryDTO = new PaymentSummaryDTO();

                            if (queryModel != null) {
                                paymentSummaryDTO.setProjectId(queryModel.getId());
                                paymentSummaryDTO.setProjectName(queryModel.getProjectName());
                                paymentSummaryDTO.setParentProjectId(queryModel.getParentId());
                                paymentSummaryDTO.setParentProjectName(queryModel.getParentProjectName());
                            }

                            paymentSummaryDTO.setSettlementAmount(new BigDecimal(0));
                            paymentSummaryDTO.setSettlementTaxAmount(new BigDecimal(0));
                            paymentSummaryDTO.setCurrency(currency);
                            paymentSummaryDTO.setTax(new BigDecimal(0));
                        }

                        BigDecimal settlementAmount = paymentSummaryDTO.getSettlementAmount();
                        BigDecimal settlementTaxAmount = paymentSummaryDTO.getSettlementTaxAmount();
                        BigDecimal tax = paymentSummaryDTO.getTax();


                        settlementAmount = settlementAmount.add(itemModel.getAmount());
                        settlementTaxAmount = settlementTaxAmount.add(itemModel.getTaxAmount() == null ? new BigDecimal(0) : itemModel.getTaxAmount());
                        tax = tax.add(itemModel.getTax() == null ? new BigDecimal(0) : itemModel.getTax());

                        paymentSummaryDTO.setSettlementAmount(settlementAmount);
                        paymentSummaryDTO.setSettlementTaxAmount(settlementTaxAmount);
                        paymentSummaryDTO.setTax(tax);

                        summaryMap.put(itemDto.getProjectName(), paymentSummaryDTO);

                    }
                    itemList.add(itemDto) ;
                }
            }

            //如果单据类型为采购发票单，则根据采购订单id汇总一次核销记录
            if(DERP_ORDER.PAYMENT_BILL_TYPE_STATE_2.equals(type)){
                if(orderMap.containsKey(String.valueOf(purchaseId))){
                    continue;
                }
                orderMap.put(String.valueOf(purchaseId),purchaseInvoiceModel);
            }

            
            /**根据采购订单查询对应预付单*/
            AdvancePaymentBillRelModel relModel = new AdvancePaymentBillRelModel() ;
            relModel.setPurchaseOrderId(purchaseId);
            
            relModel = advancePaymentBillRelDao.searchByModel(relModel) ;
            
            if(relModel == null) {
            	continue ;
            }
            
            AdvancePaymentBillItemModel queryAdvancePaymentItemModel = new AdvancePaymentBillItemModel() ;
            
            queryAdvancePaymentItemModel.setAdvancePaymentId(relModel.getAdvancePaymentId());
            queryAdvancePaymentItemModel.setPurchaseId(purchaseId);
            
            queryAdvancePaymentItemModel = advancePaymentBillItemDao.searchByModel(queryAdvancePaymentItemModel) ;
            
            if(queryAdvancePaymentItemModel == null
            		|| queryAdvancePaymentItemModel.getVerificateAmount() == null
            		|| queryAdvancePaymentItemModel.getVerificateAmount().compareTo(new BigDecimal(0)) <= 0) {
            	continue ;
            }
            
            AdvancePaymentBillModel advancePayment = advancePaymentBillDao.searchById(queryAdvancePaymentItemModel.getAdvancePaymentId());
            
            if(!advancePayment.getBillStatus().equals(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_06)) {
            	continue ;
            }
            
            PaymentVerificateItemDTO veriItemDto = new PaymentVerificateItemDTO() ;
            
            veriItemDto.setBillStatus(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_1);
            veriItemDto.setRelCode(advancePayment.getCode());
            veriItemDto.setVerificateAmount(queryAdvancePaymentItemModel.getVerificateAmount());
            
            AdvancePaymentRecordItemModel queryRecodrItemModel = new AdvancePaymentRecordItemModel() ;
            queryAdvancePaymentItemModel.setAdvancePaymentId(advancePayment.getId());
            
            List<AdvancePaymentRecordItemModel> recordList = advancePaymentRecordItemDao.list(queryRecodrItemModel);
            
            if(recordList != null && !recordList.isEmpty()) {
            	queryRecodrItemModel = recordList.get(0) ;
            	
            	if(queryRecodrItemModel != null) {
            		veriItemDto.setDrawee(queryRecodrItemModel.getCreatorName());
            		veriItemDto.setDraweeId(queryRecodrItemModel.getCreatorId());
            		veriItemDto.setPaymentDate(queryRecodrItemModel.getPaymentDate());
            		veriItemDto.setSerialNo(queryRecodrItemModel.getSerialNo());
            	}
            	
            }
            
            veriItemList.add(veriItemDto) ;
        }
        
        /**以核销金额倒核本次核销记录*/
        for (PaymentVerificateItemDTO paymentVerificateItemDTO : veriItemList) {

            BigDecimal verificateAmount = paymentVerificateItemDTO.getVerificateAmount();

            if(veriTotalAmount.compareTo(verificateAmount) >= 0) {
                paymentVerificateItemDTO.setCurrentVerificateAmount(verificateAmount);

                veriTotalAmount = veriTotalAmount.subtract(verificateAmount) ;

            }else {
                paymentVerificateItemDTO.setCurrentVerificateAmount(veriTotalAmount);

                verificateAmount = verificateAmount.subtract(veriTotalAmount) ;
                paymentVerificateItemDTO.setVerificateAmount(verificateAmount);

                veriTotalAmount = veriTotalAmount.subtract(veriTotalAmount) ;
            }

        }


        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", supplierId);
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customer_params);

        dto.setSupplierId(supplierId);
        dto.setSupplierName(supplier.getName());
        dto.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_YFZD));
        dto.setPayableAmount(totalAmount);
        dto.setPayableAmountStr(NumberToCN.number2CNMontrayUnit(totalAmount));
        dto.setPaymentAmount(totalAmount.subtract(veriTotalAmount));
        dto.setSurplusAmount(veriTotalAmount);
        dto.setMerchantId(merchantId);
        dto.setMerchantName(merchantName);
        dto.setBuId(buId);
        dto.setBuName(buName);
        dto.setCurrency(currency);
        // dto.setDepositBank(supplier.getDepositBank());
        //dto.setBankAccount(supplier.getBankAccount());
        //dto.setBankAddress(supplier.getBankAddress());
        //dto.setBeneficiaryName(supplier.getBeneficiaryName());
        //dto.setSwiftCode(supplier.getSwiftCode());
        
        //添加汇总
        dto.setPaymentSummaryList(new ArrayList<>(summaryMap.values()));
        //提交应付明细
        dto.setItemList(itemList);
        //核销记录
        dto.setVerificateItemList(veriItemList);

        //若单据类型为采购发票，则获取采购发票的附件上传到对于的应付单
        if(invoiceCode.toString().length()>0){
            for(String code:invoiceCode.toString().split(",")) {
                Map<String, Object> attQueryMap = new HashMap<String, Object>();
                attQueryMap.put("relationCode", code);
                List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                for(AttachmentInfoMongo attachamentInfo:attList){
                    attachamentInfo.setRelationCode(dto.getCode());
                    attachamentInfo.setCreateDate(new Date());
                    attachamentInfo.setModule("应付账单");
                    attachamentInfo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
                    attachmentInfoMongoDao.insert(attachamentInfo);
                }
            }
        }

		return dto;
	}

	@Override
	public Long saveOrModifyPaymentBill(PaymentBillDTO dto, User user) throws Exception {
		
		PaymentBillModel model = new PaymentBillModel() ;
		
		BeanUtils.copyProperties(dto, model);
		
		// 通过商家id获取商家信息
        Map<String, Object> merchanto_params = new HashMap<String, Object>();
        merchanto_params.put("merchantId", model.getMerchantId());
        MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);

        if (merchantMogo == null) {
            throw new DerpException("商家不存在") ;
        }
        if (model.getId()!=null) {
        	PaymentBillModel paymentBillModel = paymentBillDao.searchById(model.getId());
        	if(paymentBillModel == null) {
    			throw new DerpException("应付单号不存在") ;
    		}
    		if (DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentBillModel)) {
    			throw new DerpException("应付单号状态是审核中 不能审核") ;
    		}
		}
        
        model.setMerchantName(merchantMogo.getName());

        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", model.getSupplierId());
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params);

        if (customer == null) {
            throw new DerpException("供应商不存在") ;
        }

        model.setSupplierName(customer.getName());

        //查询事业部名称
        Map<String, Object> relParams = new HashMap<String, Object>() ;
        relParams.put("merchantId", model.getMerchantId());
        relParams.put("buId", model.getBuId());
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
        if(merchantBuRelMongo != null) {
            model.setBuName(merchantBuRelMongo.getBuName());
        }

        String operateAction = "" ;
        
        if(model.getId() == null){
            model.setCreateDate(TimeUtils.getNow());
            model.setCreater(user.getName());
            model.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00);
            model.setNcStatus(DERP_ORDER.PAYMENT_BILL_NCSTATUS_10);
            model.setPrintingState(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_0);
            paymentBillDao.save(model) ;
            
            operateAction = "新增" ;
            
        }else{
            model.setModifyDate(TimeUtils.getNow());

            paymentBillDao.modify(model) ;
            
            operateAction = "编辑" ;
        }
        
        /**记录操作日志*/
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, model.getCode(), operateAction, null, null);
		
		if(dto.getId() != null) {
			
			/**删除应付明细*/
			PaymentItemModel queryItemModel = new PaymentItemModel() ;
			queryItemModel.setPaymentId(model.getId());
			
			List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
			
			List<Long> ids = itemList.stream().map(PaymentItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentItemDao.delete(ids) ;
			}
			
			/**删除费用明细*/
			PaymentCostItemModel queryCostItemModel = new PaymentCostItemModel() ;
			queryCostItemModel.setPaymentId(model.getId());
			
			List<PaymentCostItemModel> costList = paymentCostItemDao.list(queryCostItemModel);
			
			ids = costList.stream().map(PaymentCostItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentCostItemDao.delete(ids) ;
			}
			
			/**删除统计*/
			PaymentSummaryModel querySummaryModel = new PaymentSummaryModel() ;
			querySummaryModel.setPaymentId(model.getId());
			
			List<PaymentSummaryModel> summaryList = paymentSummaryDao.list(querySummaryModel);
			
			ids = summaryList.stream().map(PaymentSummaryModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentSummaryDao.delete(ids) ;
			}
			
			/**删除核销*/
			PaymentVerificateItemModel queryVeriModel = new PaymentVerificateItemModel() ;
			queryVeriModel.setPaymentId(model.getId());
			
			List<PaymentVerificateItemModel> veriList = paymentVerificateItemDao.list(queryVeriModel);
			
			ids = veriList.stream().map(PaymentVerificateItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentVerificateItemDao.delete(ids) ;
			}
			
			/**删除采购关联关系*/
			PaymentPurchaseRelModel queryRelModel = new PaymentPurchaseRelModel() ;
			queryRelModel.setPaymentId(model.getId());
			
			List<PaymentPurchaseRelModel> relList = paymentPurchaseRelDao.list(queryRelModel);
			
			ids = relList.stream().map(PaymentPurchaseRelModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentPurchaseRelDao.delete(ids) ;
			}
		}
		
		List<PaymentItemDTO> itemList = dto.getItemList() != null ? dto.getItemList() : new ArrayList<>();
		
		Timestamp billDate = null ;
		
		for (PaymentItemDTO paymentItemDTO : itemList) {
			
			PaymentItemModel itemModel = new PaymentItemModel() ;
			
			BeanUtils.copyProperties(paymentItemDTO, itemModel);
			
			Long projectId = paymentItemDTO.getProjectId();

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);

            if(settlementConfigModel != null){
                itemModel.setProjectName(settlementConfigModel.getProjectName());
            }
            
            Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", paymentItemDTO.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
			if (mogo != null) {
				itemModel.setGoodsName(mogo.getName());// 商品名称
				itemModel.setGoodsNo(mogo.getGoodsNo());// 商品货号
			}

            params = new HashMap<String, Object>();
            params.put("brandSuperiorId", paymentItemDTO.getSuperiorParentBrandId());

            if(paymentItemDTO.getGoodsId() != null) {
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(paymentItemDTO.getGoodsId());
                if(brandSuperior != null) {
                    itemModel.setSuperiorParentBrandCode(brandSuperior.getNcCode());
                    itemModel.setSuperiorParentBrandId(brandSuperior.getBrandSuperiorId());
                    itemModel.setSuperiorParentBrandName(brandSuperior.getName());
                }
            }

			itemModel.setPaymentId(model.getId());
			itemModel.setCreateDate(TimeUtils.getNow());
			
			paymentItemDao.save(itemModel) ;

			PaymentPurchaseRelModel saveModel = new PaymentPurchaseRelModel() ;
			saveModel.setPurchaseId(paymentItemDTO.getPurchaseId());
			saveModel.setPaymentId(model.getId());
			saveModel.setCreateDate(TimeUtils.getNow());
			
			paymentPurchaseRelDao.save(saveModel) ;
			
			PurchaseOrderModel purchase = purchaseOrderDao.searchById(paymentItemDTO.getPurchaseId());
			
			if(billDate == null) {
				billDate = purchase.getAttributionDate() ;
			}
			
		}
		
		List<PaymentCostItemDTO> costList = dto.getCostList() != null ? dto.getCostList() : new ArrayList<>();
		
		for (PaymentCostItemDTO paymentCostItemDTO : costList) {
			
			PaymentCostItemModel costItemModel = new PaymentCostItemModel() ;
			
			BeanUtils.copyProperties(paymentCostItemDTO, costItemModel);
			
			Long projectId = paymentCostItemDTO.getProjectId();

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);

            if(settlementConfigModel != null){
            	costItemModel.setProjectName(settlementConfigModel.getProjectName());
            }
            
            Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", paymentCostItemDTO.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
			if (mogo != null) {
				costItemModel.setGoodsName(mogo.getName());// 商品名称
				costItemModel.setGoodsNo(mogo.getGoodsNo());// 商品货号
			}
			
			params = new HashMap<String, Object>();
			params.put("brandSuperiorId", paymentCostItemDTO.getSuperiorParentBrandId());
			
			BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
			
			costItemModel.setSuperiorParentBrandCode(brandSuperior.getNcCode());
			costItemModel.setSuperiorParentBrandId(paymentCostItemDTO.getSuperiorParentBrandId());
			costItemModel.setSuperiorParentBrandName(brandSuperior.getName());
			
            costItemModel.setPaymentId(model.getId());
            costItemModel.setCreateDate(TimeUtils.getNow());
            
            paymentCostItemDao.save(costItemModel) ;
		}
		
		List<PaymentSummaryDTO> paymentSummaryList = dto.getPaymentSummaryList() != null ? dto.getPaymentSummaryList() : new ArrayList<>();
		
		for (PaymentSummaryDTO paymentSummaryDTO : paymentSummaryList) {
			
			PaymentSummaryModel summaryModel = new PaymentSummaryModel() ;
			
			BeanUtils.copyProperties(paymentSummaryDTO, summaryModel);
			
			Long projectId = paymentSummaryDTO.getProjectId();

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);

            if(settlementConfigModel != null){
            	summaryModel.setProjectName(settlementConfigModel.getProjectName());
            	summaryModel.setParentProjectId(settlementConfigModel.getParentId());
            	summaryModel.setParentProjectName(settlementConfigModel.getParentProjectName());
            }
            
            summaryModel.setPaymentId(model.getId());
            summaryModel.setCreateDate(TimeUtils.getNow());
            
            paymentSummaryDao.save(summaryModel) ;
            
		}
		
		List<PaymentVerificateItemDTO> verificateItemList = dto.getVerificateItemList() != null ? dto.getVerificateItemList() : new ArrayList<>();
		
		for (PaymentVerificateItemDTO paymentVerificateItemDTO : verificateItemList) {
			
			PaymentVerificateItemModel itemModel = new PaymentVerificateItemModel() ;
			
			BeanUtils.copyProperties(paymentVerificateItemDTO, itemModel);
			
			itemModel.setPaymentId(model.getId());
			itemModel.setCreateDate(TimeUtils.getNow());
			
			paymentVerificateItemDao.save(itemModel) ;
			
		}
		
		PaymentBillModel updateModel = new PaymentBillModel() ;
		updateModel.setId(model.getId());
		updateModel.setBillDate(billDate);
		
		paymentBillDao.modify(updateModel) ;
		
		return model.getId();
	}

	@Override
	public void submitInvalidBill(List<Long> list, String invalidRemark, User user) throws SQLException {
		
		for (Long paymentId : list) {
			
			PaymentBillModel payment = paymentBillDao.searchById(paymentId);
			
			if(!(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03.equals(payment.getBillStatus())
					|| DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07.equals(payment.getBillStatus()))) {
				throw new DerpException("仅对【待付款】【部分付款】状态下的应付帐单可操作“作废”") ;
			}
			
			//判断是否存在NC的核销记录
			PaymentVerificateItemModel queryModel = new PaymentVerificateItemModel() ;
			queryModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_2);
			queryModel.setPaymentId(paymentId);
			
			List<PaymentVerificateItemModel> veriList = paymentVerificateItemDao.list(queryModel);
			
			if(!veriList.isEmpty()) {
				throw new DerpException("该单据存在NC单据核销记录，不可作废") ;
			}
				
			payment.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_05);
			payment.setModifierId(user.getId());
			payment.setModifier(user.getName());
			payment.setModifyDate(TimeUtils.getNow());
			
			paymentBillDao.modify(payment) ;

            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            emailUserDTO.setBuId(payment.getBuId());
            emailUserDTO.setBuName(payment.getBuName());
            emailUserDTO.setMerchantId(payment.getMerchantId());
            emailUserDTO.setMerchantName(payment.getMerchantName());
            emailUserDTO.setOrderCode(payment.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16);
            emailUserDTO.setSupplier(payment.getSupplierName());
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setCurrency(payment.getCurrency());
            emailUserDTO.setAmount(payment.getPayableAmount().toString());
            emailUserDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
            emailUserDTO.setSubmitId(Arrays.asList(getOperateId(payment,"提交审核")==null?null:getOperateId(payment,"提交审核").toString()));
            emailUserDTO.setCreateId(getOperateId(payment,"新增"));


            JSONObject json = JSONObject.fromObject(emailUserDTO) ;

            try {
                //发送邮件
                rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            } catch (Exception e) {
                LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
            }

			
			/**记录操作日志*/
			
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, payment.getCode(), "提交作废", null, invalidRemark);
		}
		
	}

	@Override
	public PaymentBillDTO getDetail(long id) throws SQLException {
		
		PaymentBillModel paymentBill = paymentBillDao.searchById(id);
		PaymentBillDTO dto = new PaymentBillDTO() ;
		
		BeanUtils.copyProperties(paymentBill, dto);
		
		if(paymentBill.getPayableAmount() != null) {
			dto.setPayableAmountStr(NumberToCN.number2CNMontrayUnit(paymentBill.getPayableAmount()));
		}

		PaymentItemModel itemModel=new PaymentItemModel();
		itemModel.setPaymentId(paymentBill.getId());
        List<PaymentItemModel> itemList=paymentItemDao.list(itemModel);
        for(PaymentItemModel payment:itemList){
            PurchaseOrderModel purchaseOrderModel=purchaseOrderDao.searchById(payment.getPurchaseId());
            //判断采购单的业务模式是否为“采销执行”
            if(DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_3.equals(purchaseOrderModel.getBusinessModel())){
                dto.setIsEndownment(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_0);
            }else{
                dto.setIsEndownment(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_1);
            }
            break;
        }
		return dto;
	}

	@Override
	public void delPaymentBill(User user, List<Long> list) throws SQLException {
		
		for (Long paymentId : list) {
			
			PaymentBillModel paymentModel = paymentBillDao.searchById(paymentId);
			
			if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00.equals(paymentModel.getBillStatus())
					|| DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02.equals(paymentModel.getBillStatus()))) {
				throw new DerpException("仅对【待提交】 【已驳回】的应付帐单可操作删除") ;
			}
			
			paymentModel.setBillStatus(DERP.DEL_CODE_006);
			paymentModel.setModifyDate(TimeUtils.getNow());
			
			paymentBillDao.modify(paymentModel) ;
			
			/**删除应付明细*/
			PaymentItemModel queryItemModel = new PaymentItemModel() ;
			queryItemModel.setPaymentId(paymentId);
			
			List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
			
			List<Long> ids = itemList.stream().map(PaymentItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentItemDao.delete(ids) ;
			}
			
			
			/**删除费用明细*/
			PaymentCostItemModel queryCostItemModel = new PaymentCostItemModel() ;
			queryCostItemModel.setPaymentId(paymentId);
			
			List<PaymentCostItemModel> costList = paymentCostItemDao.list(queryCostItemModel);
			
			ids = costList.stream().map(PaymentCostItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentCostItemDao.delete(ids) ;
			}
			
			/**删除统计*/
			PaymentSummaryModel querySummaryModel = new PaymentSummaryModel() ;
			querySummaryModel.setPaymentId(paymentId);
			
			List<PaymentSummaryModel> summaryList = paymentSummaryDao.list(querySummaryModel);
			
			ids = summaryList.stream().map(PaymentSummaryModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentSummaryDao.delete(ids) ;
			}
			
			/**删除核销*/
			PaymentVerificateItemModel queryVeriModel = new PaymentVerificateItemModel() ;
			queryVeriModel.setPaymentId(paymentId);
			
			List<PaymentVerificateItemModel> veriList = paymentVerificateItemDao.list(queryVeriModel);
			
			ids = veriList.stream().map(PaymentVerificateItemModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentVerificateItemDao.delete(ids) ;
			}
			
			/**删除采购关联关系*/
			PaymentPurchaseRelModel queryRelModel = new PaymentPurchaseRelModel() ;
			queryRelModel.setPaymentId(paymentId);
			
			List<PaymentPurchaseRelModel> relList = paymentPurchaseRelDao.list(queryRelModel);
			
			ids = relList.stream().map(PaymentPurchaseRelModel::getId).collect(Collectors.toList());
			
			if(!ids.isEmpty()) {
				paymentPurchaseRelDao.delete(ids) ;
			}
			
			/**记录操作日志*/
			
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, paymentModel.getCode(), "删除", null, null);
		}
	}

	@Override
	public void saveVerificate(PaymentVerificateItemDTO dto, User user) throws SQLException {
		
		PaymentBillModel paymentModel = paymentBillDao.searchById(dto.getPaymentId());
		
		if(!(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03.equals(paymentModel.getBillStatus())
				|| DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07.equals(paymentModel.getBillStatus()))) {
			throw new DerpException("仅当单据状态为【待付款】【部分付款】时可操作核销") ;
		}
		
		BigDecimal surplusAmount = paymentModel.getSurplusAmount();
		BigDecimal currentVerificateAmount = dto.getCurrentVerificateAmount();
		
		if(currentVerificateAmount.compareTo(surplusAmount) > 0) {
			throw new DerpException("本次付款金额不能大于应付账单待核销金额") ;
		}
		
		PaymentVerificateItemModel itemModel = new PaymentVerificateItemModel() ;
		
		BeanUtils.copyProperties(dto, itemModel);
		
		itemModel.setCreateDate(TimeUtils.getNow());
		paymentVerificateItemDao.save(itemModel) ;
		
		surplusAmount = surplusAmount.subtract(currentVerificateAmount) ;
		
		if(surplusAmount.compareTo(new BigDecimal(0)) == 0) {
			paymentModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_04);
		}else {
			paymentModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07);
		}
		
		BigDecimal payableAmount = paymentModel.getPayableAmount();
		BigDecimal paymentAmount = payableAmount.subtract(surplusAmount) ;
		
		paymentModel.setPaymentAmount(paymentAmount);
		paymentModel.setSurplusAmount(surplusAmount);
		paymentModel.setModifyDate(TimeUtils.getNow());
		
		paymentBillDao.modify(paymentModel) ;


        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(paymentModel.getBuId());
        emailUserDTO.setBuName(paymentModel.getBuName());
        emailUserDTO.setMerchantId(paymentModel.getMerchantId());
        emailUserDTO.setMerchantName(paymentModel.getMerchantName());
        emailUserDTO.setOrderCode(paymentModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4);
        emailUserDTO.setSupplier(paymentModel.getSupplierName());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setCurrency(paymentModel.getCurrency());
        emailUserDTO.setAmount(paymentModel.getPayableAmount().toString());
        emailUserDTO.setVeriAmount(currentVerificateAmount.toString());
        emailUserDTO.setUnVeriAmount(surplusAmount.toString());
        emailUserDTO.setUserId(Arrays.asList(user.getId().toString()));
        emailUserDTO.setAuditorId(getOperateId(paymentModel,"提交审核"));
        emailUserDTO.setCancelId(getOperateId(paymentModel,"提交作废"));
        emailUserDTO.setSubmitId(Arrays.asList(getOperateId(paymentModel,"提交审核")==null?null:getOperateId(paymentModel,"提交审核").toString()));
        emailUserDTO.setCreateId(getOperateId(paymentModel,"新增"));

        JSONObject json = JSONObject.fromObject(emailUserDTO) ;
        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
        }
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, paymentModel.getCode(), "核销", null, null);
	}

	@Override
	public List<PaymentItemDTO> getPaymentItems(Long id) throws SQLException {

	    //获取 应付明细
        PaymentItemDTO dto=new PaymentItemDTO();
	    dto.setPaymentId(id);
        List<PaymentItemDTO> dtoList=paymentItemDao.getPaymentItemDto(dto);
		
		/*PaymentItemModel queryItemModel = new PaymentItemModel() ;
		queryItemModel.setPaymentId(id);
		
		List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
		List<PaymentItemDTO> dtoList = new ArrayList<PaymentItemDTO>() ;
		
		for (PaymentItemModel paymentItemModel : itemList) {
			PaymentItemDTO dto = new PaymentItemDTO() ;
			
			BeanUtils.copyProperties(paymentItemModel, dto);
			
			dtoList.add(dto) ;
		}*/
		
		return dtoList;
	}

	@Override
	public List<OperateLogDTO> listOperateLog(Long id) throws SQLException {
		
		PaymentBillModel paymentModel = paymentBillDao.searchById(id);
		
		if(paymentModel == null) {
			throw new DerpException("预付单号不存在") ;
		}
		
		/**查询操作记录*/
		OperateLogModel queryOperateLogModel = new OperateLogModel() ;
		queryOperateLogModel.setRelCode(paymentModel.getCode());
		
		List<OperateLogModel> operateList = operateLogDao.list(queryOperateLogModel);
		List<OperateLogDTO> operateDtoList = new ArrayList<OperateLogDTO>() ;
		
		for (OperateLogModel operateLogModel : operateList) {
			
			OperateLogDTO dtoModel = new OperateLogDTO() ;
			BeanUtils.copyProperties(operateLogModel, dtoModel);
			
			if(dtoModel.getOperateDate() != null) {
				dtoModel.setOperateDateStr(TimeUtils.format(dtoModel.getOperateDate(), "yyyy-MM-dd HH:mm:ss"));
			}
			
			operateDtoList.add(dtoModel) ;
			
		}
		
		return operateDtoList;
		
	}

	@Override
	public List<PaymentCostItemDTO> getCostItems(Long id) throws SQLException {
		
		PaymentCostItemModel queryCostItem = new PaymentCostItemModel() ;
		queryCostItem.setPaymentId(id);
		
		List<PaymentCostItemModel> costList = paymentCostItemDao.list(queryCostItem);
		List<PaymentCostItemDTO> costDtoList = new ArrayList<>() ;
		
		for (PaymentCostItemModel itemModel : costList) {
			
			PaymentCostItemDTO dto = new PaymentCostItemDTO() ;
			
			BeanUtils.copyProperties(itemModel, dto);
			
			costDtoList.add(dto) ;
			
		}
		
		return costDtoList;
	}

	@Override
	public List<PaymentVerificateItemDTO> getVerificateList(Long id) throws SQLException {
		
		PaymentVerificateItemModel queryModel = new PaymentVerificateItemModel() ;
		queryModel.setPaymentId(id);
		
		List<PaymentVerificateItemModel> veriList = paymentVerificateItemDao.list(queryModel);
		List<PaymentVerificateItemDTO> dtoList = new ArrayList<PaymentVerificateItemDTO>() ;
		
		for (PaymentVerificateItemModel paymentVerificateItemModel : veriList) {
			
			PaymentVerificateItemDTO dto = new PaymentVerificateItemDTO() ;
			
			BeanUtils.copyProperties(paymentVerificateItemModel, dto);
			
			dtoList.add(dto) ;
		}
		
		return dtoList;
	}

	@Override
	public void auditPayment(User user, Long id, String invalidRemark, String isPassed) throws SQLException {
		
		PaymentBillModel paymentModel = paymentBillDao.searchById(id);
		
		if(paymentModel == null) {
			throw new DerpException("应付单号不存在") ;
		}
		
		if(!(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())
				|| DERP_ORDER.PAYMENT_BILL_BILLSTATUS_05.equals(paymentModel.getBillStatus()))) {
			throw new DerpException("仅当单据状态为【审核中】【待作废】时可操作审核") ;
		}
		
		if(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())
				&& DERP_ORDER.PAYMENT_BILL_AUDIT_METHOD_1.equals(paymentModel.getAuditMethod())) {
			throw new DerpException("该应付单审批方式为：OA审批") ;
		}
		
		String billStatus = null ;
		String result = null ;
		String msg = "" ;
		
		/***
		 * 1、应付审核
		 *（1）通过时，更新应付账单状态为“待付款”；
		 *	再判断 “已付款总额”是否等于“应付总额”，若等于则更新应付账单状态为“已付款”，
		 *	若 “已付款金额”小于“应付账款”且不为0，则更新应付账单状态为“部分付款”；
		 *（2）驳回时，更新应付账单状态为“已驳回”；
		 *
		 *
		 * 2、作废审核
		 *（1）通过时，更新应付账单状态为“已作废”；
		 *	且取消对应采购订单与应付账单的关联关系；
		 *	取消应付账单与已有勾稽的预付款单的关联记录，释放预付款单的待核销金额；
		 *
		 *	若NC同步状态为“已同步”则触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口 [业务系统-->前置接口层]     /external/settlement/concancel。若NC同步状态为“未同步”则不触发接口。
		 *（2）驳回时，更新应付账单状态为“待付款”；
		 */
		
		/**若为审核*/
		if(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())) {
			
			msg = "审核" ;
			
			if("1".equals(isPassed)) {
				billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03 ;
				result = "审核通过" ;
				
				if(paymentModel.getPaymentAmount().compareTo(paymentModel.getPayableAmount()) == 0) {
					billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_04 ;
				}else if(paymentModel.getPaymentAmount().compareTo(new BigDecimal(0)) > 0) {
					billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07 ;
				}
				
			}else {
				billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_02 ;
				result = "审核驳回" ;
			}
			
		}else if(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_05.equals(paymentModel.getBillStatus())) {
			
			msg = "作废" ;
			
			if("1".equals(isPassed)) {
				
				billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_06 ;
				result = "作废通过" ;
				
				/**若NC同步状态为“已同步”则触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口 [业务系统-->前置接口层]     
	             * /external/settlement/concancel。若NC同步状态为“未同步”则不触发接口
	             * */
	            if(!DERP_ORDER.PAYMENT_BILL_NCSTATUS_10.equals(paymentModel.getNcStatus())) {
	            	//同步NC
		            ReceiveHcInvalidRoot invalidRoot = new ReceiveHcInvalidRoot();
		            invalidRoot.setConfirmBillId(paymentModel.getCode());
		            invalidRoot.setSourceCode(ApolloUtil.ncSourceType);
		            invalidRoot.setState("1");
		            invalidRoot.setCancelTime(TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		            invalidRoot.setRemark(invalidRemark);
		            JSONObject json = JSONObject.fromObject(invalidRoot);
		            String clearText = json.toString();
		            //提交NC
		            String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_08.getUri(), clearText);

		            JSONObject resultJson = JSONObject.fromObject(ncResult);

		            if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
		                throw new DerpException(resultJson.getString("msg"));
		            }
	            }
				
				/**删除关联关系*/
				PaymentPurchaseRelModel queryRelItem = new PaymentPurchaseRelModel() ;
				queryRelItem.setPaymentId(id);
	            
	            List<PaymentPurchaseRelModel> relList = paymentPurchaseRelDao.list(queryRelItem);
	            
	            List<Long> relIds = relList.stream().map(PaymentPurchaseRelModel::getId).collect(Collectors.toList());
	            
	            if(relIds != null && !relIds.isEmpty()){
	            	paymentPurchaseRelDao.delete(relIds) ;
	            }
				
	            /**删除核销记录*/
	            PaymentVerificateItemModel queryItemModel = new PaymentVerificateItemModel() ;
	            queryItemModel.setPaymentId(id);
	            
	            List<PaymentVerificateItemModel> veriList = paymentVerificateItemDao.list(queryItemModel);
	            
	            List<Long> ids = veriList.stream().map(PaymentVerificateItemModel::getId).collect(Collectors.toList());
	            
	            if(ids != null && !ids.isEmpty()){
	            	paymentVerificateItemDao.delete(ids) ;
	            }
	            
	            
			}else {
				billStatus = DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03 ;
				result = "作废驳回" ;
			}
			
		}
		
		PaymentBillModel updateModel = new PaymentBillModel() ;
		updateModel.setId(id);
		updateModel.setBillStatus(billStatus);
		updateModel.setModifierId(user.getId());
		updateModel.setModifier(user.getName());
		updateModel.setBillStatus(billStatus);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		if(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_06.equals(billStatus)) {
			updateModel.setPaymentAmount(new BigDecimal(0));
			updateModel.setSurplusAmount(paymentModel.getPayableAmount());
		}
		
		paymentBillDao.modify(updateModel) ;

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;
        emailUserDTO.setBuId(paymentModel.getBuId());
        emailUserDTO.setBuName(paymentModel.getBuName());
        emailUserDTO.setMerchantId(paymentModel.getMerchantId());
        emailUserDTO.setMerchantName(paymentModel.getMerchantName());
        emailUserDTO.setOrderCode(paymentModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8));

        if(msg.equals("作废")){
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6);
        }else{
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
        }

        emailUserDTO.setSupplier(paymentModel.getSupplierName());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setCurrency(paymentModel.getCurrency());
        emailUserDTO.setStatus(isPassed.equals("1")?"已通过":"已驳回");
        emailUserDTO.setAmount(paymentModel.getPayableAmount().toString());
        emailUserDTO.setUserId(Arrays.asList(user.getId().toString()));
        emailUserDTO.setCancelId(getOperateId(paymentModel,"提交作废"));
        emailUserDTO.setSubmitId(Arrays.asList(getOperateId(paymentModel,"提交审核")==null?null:getOperateId(paymentModel,"提交审核").toString()));
        emailUserDTO.setAuditorId(getOperateId(paymentModel,"提交审核"));
        emailUserDTO.setVerificationId(getOperateId(paymentModel,"核销"));
        emailUserDTO.setCreateId(getOperateId(paymentModel,"新增"));
        emailUserDTO.setInvalidRemark(invalidRemark);

        JSONObject json = JSONObject.fromObject(emailUserDTO) ;

        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
        }

		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, paymentModel.getCode(), msg, result, invalidRemark);
	}

	private Long getOperateId(PaymentBillModel paymentModel,String msg) throws SQLException {
        OperateLogModel operateLogModel=new OperateLogModel();
        operateLogModel.setModule( DERP_ORDER.OPERATE_LOG_MODULE_3);
        operateLogModel.setRelCode(paymentModel.getCode());
        operateLogModel.setOperateAction(msg);

        OperateLogDTO dto=new OperateLogDTO();
        List<OperateLogDTO> logModels=commonBusinessService.getOperateLogList(operateLogModel);
        logModels = logModels.stream().sorted(Comparator.comparing(OperateLogDTO::getOperateDate).reversed()).collect(Collectors.toList()) ;
        if(logModels.size() > 0){
            dto = logModels.get(0);
        }
        return dto.getOperateId();
    }

	@SuppressWarnings("unchecked")
	@Override
	public PaymentVerificateItemDTO getVeriAdvancePaymentList(AdvancePaymentBillDTO billDTO) throws SQLException {
		
		List<AdvancePaymentBillItemDTO> veriAdvancePaymentList = advancePaymentBillItemDao.getVeriAdvancePaymentList(billDTO);
		List<PaymentVerificateItemDTO> veriList = new ArrayList<PaymentVerificateItemDTO>() ;
		
		for (AdvancePaymentBillItemDTO advancePaymentBillItemDTO : veriAdvancePaymentList) {
			
			PaymentVerificateItemDTO paymentVerificateItemDTO = new PaymentVerificateItemDTO() ;
			
			paymentVerificateItemDTO.setRelCode(advancePaymentBillItemDTO.getAdvancePaymentCode());
			paymentVerificateItemDTO.setBillStatus(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_1);
			paymentVerificateItemDTO.setVerificateAmount(advancePaymentBillItemDTO.getVerificateAmount());
			paymentVerificateItemDTO.setPurchaseCode(advancePaymentBillItemDTO.getPurchaseCode());
			paymentVerificateItemDTO.setPrepaymentAmount(advancePaymentBillItemDTO.getPrepaymentAmount());
			
			AdvancePaymentRecordItemModel queryModel = new AdvancePaymentRecordItemModel() ;
			queryModel.setAdvancePaymentId(advancePaymentBillItemDTO.getAdvancePaymentId());
			List<AdvancePaymentRecordItemModel> itemList = advancePaymentRecordItemDao.list(queryModel);
			
			if(!itemList.isEmpty()) {
				
				AdvancePaymentRecordItemModel advancePaymentRecordItemModel = itemList.get(0);
				
				paymentVerificateItemDTO.setDrawee(advancePaymentRecordItemModel.getCreatorName());
				paymentVerificateItemDTO.setDraweeId(advancePaymentRecordItemModel.getCreatorId());
				paymentVerificateItemDTO.setPaymentDate(advancePaymentRecordItemModel.getPaymentDate());
				paymentVerificateItemDTO.setSerialNo(advancePaymentRecordItemModel.getSerialNo());
			}
			
			veriList.add(paymentVerificateItemDTO) ;
		}
		
		PaymentVerificateItemDTO dto = new PaymentVerificateItemDTO() ;
		dto.setList(veriList);
		
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modifyAuditMethod(User user, Long id, String auditMethod) throws Exception {
		
		PaymentBillModel paymentModel = paymentBillDao.searchById(id);
				
		if(paymentModel == null) {
			throw new DerpException("应付单号不存在") ;
		}
		if (DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel)) {
			throw new DerpException("应付单号状态是审核中 不能审核") ;
		}
		
		boolean empty = new EmptyCheckUtils().addObject(paymentModel.getBankAccount())
			.addObject(paymentModel.getBeneficiaryName())
			.addObject(paymentModel.getDepositBank())
			.addObject(paymentModel.getCurrency())
			.addObject(paymentModel.getPaymentReason())
			.addObject(paymentModel.getExpectedPaymentDate())
			.addObject(paymentModel.getCurrency())
			.addObject(paymentModel.getPayableAmount())
			.empty();
		
		if(empty) {
			throw new DerpException("必填项为空，请检查") ;
		}
		
		PaymentItemModel queryModel = new PaymentItemModel() ;
		
		queryModel.setPaymentId(paymentModel.getId());
		List<PaymentItemModel> itemList = paymentItemDao.list(queryModel);
		
		if(itemList == null 
				|| itemList.isEmpty()) {
			throw new DerpException("应付明细为空，请检查") ;
		}
		
		PaymentCostItemModel queryCostModel = new PaymentCostItemModel() ;
		
		queryCostModel.setPaymentId(paymentModel.getId());
		List<PaymentCostItemModel> itemCostList = paymentCostItemDao.list(queryCostModel);
		
		PaymentBillModel updateModel = new PaymentBillModel() ;
		
		/**推送OA审批**/
		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1.equals(auditMethod)) {
			
			// 通过商家id获取商家信息
	        Map<String, Object> merchanto_params = new HashMap<String, Object>();
	        merchanto_params.put("merchantId", paymentModel.getMerchantId());
	        MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);
			
			Map<String, Object> customer_params = new HashMap<String, Object>();
	        customer_params.put("customerid", paymentModel.getSupplierId());
	        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customer_params);
			
	        Map<String, Object> relParams = new HashMap<String, Object>() ;
			relParams.put("buId", paymentModel.getBuId());
			relParams.put("merchantId", user.getMerchantId());
			relParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
			if(merchantBuRelMongo == null) {
				throw new DerpException("商家事业部关联不存在") ;
			}
			
			String sqsj = TimeUtils.format(new Date(), "yyyy-MM-dd");
			String title = "采购请款-供应商:" + supplier.getName() + "-" + user.getName() + "-" + sqsj ;
	        
			CreatRequestIdRequest model = new CreatRequestIdRequest() ;
			
			model.setWbxtdjh(paymentModel.getCode());
			model.setCwzz(merchantMogo.getTopidealCode());
			model.setSqr(user.getName());
			model.setSqsj(sqsj);
			model.setGysxz("1");
			model.setGys(supplier.getMainId());
			model.setSyb(merchantBuRelMongo.getBuCode());
			model.setSkyhzh(paymentModel.getBeneficiaryName());
			model.setSkyhzh1(paymentModel.getBankAccount());
			model.setSkyhkhh(paymentModel.getDepositBank());
			model.setSwiftcode(paymentModel.getSwiftCode());
			model.setJsbz(paymentModel.getCurrency());
			model.setQkyy(paymentModel.getPaymentReason());
			model.setTitle(title);
			
			BigDecimal surplusAmount = paymentModel.getSurplusAmount() ;
			surplusAmount = surplusAmount.setScale(2) ;
			
			model.setYbjehz(surplusAmount.toString());
			model.setYbjedx(NumberToCN.number2CNMontrayUnit(surplusAmount));
			
			Timestamp expectedPaymentDate = paymentModel.getExpectedPaymentDate() ;
			model.setYjfksj(TimeUtils.format(expectedPaymentDate, "yyyy-MM-dd"));
			
			//获取附件
			Map<String, Object> queryAttMap = new HashMap<String, Object>() ;
			queryAttMap.put("relationCode", paymentModel.getCode()) ;
            queryAttMap.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;

			List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(queryAttMap);
			
			StringBuffer sb = new StringBuffer() ;
			for (AttachmentInfoMongo attachmentInfoMongo : attList) {
				
				sb.append("<p><a href=\""+attachmentInfoMongo.getAttachmentUrl()+"\" target=\"_blank\">" + attachmentInfoMongo.getAttachmentName()+ "</a></p>") ;
			}
			
			if(sb.length() > 0) {
				model.setFj(sb.toString());
			}
			
			List<CtreatRequestIdItemRequest> requestItemList = new ArrayList<CtreatRequestIdItemRequest>() ;
			//费项汇总map
			Map<String, CtreatRequestIdItemRequest> costMap = new HashMap<>() ;
			
			//应付明细
			for (PaymentItemModel itemModel : itemList) {
				
				BigDecimal itemAmount = itemModel.getSettlementAmount();
				
				if(itemAmount == null) {
					throw new DerpException("本期结算金额（不含税）为空") ;
				}
				
				itemAmount = itemAmount.setScale(2) ;
				
				BigDecimal settlementTax = itemModel.getSettlementTax();
				
				if(settlementTax == null) {
					throw new DerpException("本期结算税额为空") ;
				}
				
				settlementTax = settlementTax.setScale(2) ;
				
				BigDecimal hsAmount = new BigDecimal(0) ;
				hsAmount = itemAmount.add(settlementTax) ;
				
				PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(itemModel.getPurchaseId());

				model.setSfdz(paymentModel.getEndowmentState());
				
				String poNo = purchaseOrder.getPoNo() ;
				String projectName = itemModel.getProjectName();
				
				String key = poNo + "_" + projectName ;
				
				CtreatRequestIdItemRequest item = costMap.get(key) ;
				
				if(item == null) {
					
					item = new CtreatRequestIdItemRequest() ;
					
					item.setZy("应付" + projectName);
					item.setPoh(purchaseOrder.getPoNo());
					SettlementConfigModel settlementConfig = 
							settlementConfigDao.searchById(itemModel.getProjectId());
					
					if(settlementConfig == null) {
						throw new DerpException("根据费项ID：" + itemModel.getProjectId() + 
								"费项名：" + projectName + "查询费项配置不存在") ;
					}
					
					ReceivePaymentSubjectModel ncSubject = 
							receivePaymentSubjectDao.searchById(settlementConfig.getPaymentSubjectId());
					
					item.setSzfx(ncSubject.getCode());
					
					BigDecimal jebhs = new BigDecimal(0) ;
					BigDecimal se = new BigDecimal(0) ;
					BigDecimal jehs = new BigDecimal(0) ;
					
					item.setJebhs(jebhs.toString());
					item.setSe(se.toString());
					item.setJehs(jehs.toString());
					
				}
				
				String jebhsStr = item.getJebhs();
				String seStr = item.getSe();
				String jehsStr = item.getJehs();
				
				BigDecimal jebhs = new BigDecimal(jebhsStr) ;
				BigDecimal se = new BigDecimal(seStr) ;
				BigDecimal jehs = new BigDecimal(jehsStr) ;
				
				jebhs = jebhs.add(itemAmount) ;
				se = se.add(settlementTax) ;
				jehs = jehs.add(hsAmount) ;
				
				item.setJebhs(jebhs.toString());
				item.setSe(se.toString());
				item.setJehs(jehs.toString());
				
				costMap.put(key, item) ;
			}
			
			//费用明细
			for (PaymentCostItemModel costItemModel : itemCostList) {
				
				if(costItemModel.getCostAmount() == null) {
					throw new DerpException("费用金额（不含税）为空") ;
				}
				
				if(costItemModel.getTax() == null) {
					throw new DerpException("税额为空") ;
				}
				
				CtreatRequestIdItemRequest item = costMap.get(costItemModel.getProjectName()) ;
				
				if(item == null) {
					item = new CtreatRequestIdItemRequest() ;
					
					item.setZy("应付" + costItemModel.getProjectName());
					
					SettlementConfigModel settlementConfig = 
							settlementConfigDao.searchById(costItemModel.getProjectId());
					
					if(settlementConfig == null) {
						throw new DerpException("根据费项ID：" + costItemModel.getProjectId() + 
								"费项名：" + costItemModel.getProjectName() + "查询费项配置不存在") ;
					}
					
					ReceivePaymentSubjectModel ncSubject = 
							receivePaymentSubjectDao.searchById(settlementConfig.getPaymentSubjectId());
					
					item.setSzfx(ncSubject.getCode());
					
					BigDecimal jebhs = new BigDecimal(0) ;
					BigDecimal se = new BigDecimal(0) ;
					BigDecimal jehs = new BigDecimal(0) ;
					
					item.setJebhs(jebhs.toString());
					item.setSe(se.toString());
					item.setJehs(jehs.toString());
					
				}
				
				String jebhsStr = item.getJebhs();
				String seStr = item.getSe();
				String jehsStr = item.getJehs();
				
				BigDecimal jebhs = new BigDecimal(jebhsStr) ;
				BigDecimal se = new BigDecimal(seStr) ;
				BigDecimal jehs = new BigDecimal(jehsStr) ;
				
				//根据补扣款加减金额
				if("1".equals(costItemModel.getType())) {
					
					jebhs = jebhs.add(costItemModel.getCostAmount()) ;
					se = se.add(costItemModel.getTax()) ;
					jehs = jebhs.add(se) ;
					
				}else if("0".equals(costItemModel.getType())) {
					
					jebhs = jebhs.subtract(costItemModel.getCostAmount()) ;
					se = se.subtract(costItemModel.getTax()) ;
					jehs = jebhs.add(se) ;
					
				}
				
				item.setJebhs(jebhs.toString());
				item.setSe(se.toString());
				item.setJehs(jehs.toString());
				
				costMap.put(costItemModel.getProjectName(), item) ;
				
			}
			
			requestItemList.addAll(new ArrayList<>(costMap.values())) ;
			
			model.setItemList(requestItemList); 
			
			/**推送OA*/
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(paymentModel.getCode());
			orderExternalCodeModel.setOrderSource(9);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 
			try {
				orderExternalCodeDao.save(orderExternalCodeModel);
			} catch (Exception e) {
				throw new DerpException("已经推送oa（不能重复推送）") ;
			}
			
			String result = OAUtils.getOARequestId(model, user.getCode());
			//保存OA单号
			updateModel.setRequestId(result);
			
			//保存到唯一单号表
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("orderCode", paymentModel.getCode()) ;
			
			ContractNoMongo contractNoMongo = contractNoMongoDao.findOne(queryMap);
			
			if(contractNoMongo == null) {
				contractNoMongo = new ContractNoMongo() ;
				
				contractNoMongo.setContractNo(result);
				contractNoMongo.setOrderCode(paymentModel.getCode());
				contractNoMongo.setType(DERP.UNIQUE_ID_YFZD);
				
				contractNoMongoDao.insert(contractNoMongo); 
				
			}else {
				
				contractNoMongo.setContractNo(result);
				
				JSONObject json = JSONObject.fromObject(contractNoMongo) ;
				
				contractNoMongoDao.update(queryMap, json) ;
				
			}
			
		}
		
		updateModel.setId(paymentModel.getId());
		updateModel.setAuditMethod(auditMethod);
		updateModel.setModifierId(user.getId());
		updateModel.setModifier(user.getName());
		updateModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		paymentBillDao.modify(updateModel) ;

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(paymentModel.getBuId());
        emailUserDTO.setBuName(paymentModel.getBuName());
        emailUserDTO.setMerchantId(paymentModel.getMerchantId());
        emailUserDTO.setMerchantName(paymentModel.getMerchantName());
        emailUserDTO.setOrderCode(paymentModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
        emailUserDTO.setSupplier(paymentModel.getSupplierName());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setCurrency(paymentModel.getCurrency());
        emailUserDTO.setAmount(paymentModel.getPayableAmount().toString());
        emailUserDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setCreateId(getOperateId(paymentModel,"新增"));

        JSONObject json = JSONObject.fromObject(emailUserDTO) ;

        try {
            //发送邮件
            rocketMQProducer.send(json.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        } catch (Exception e) {
            LOGGER.error("--------应付发送邮件发送失败-------", json.toString());
        }

        /**记录操作日志*/
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, paymentModel.getCode(), "提交审核", null, null);
	}

	@Override
	public void syncNC(Long id, User user) throws Exception {
		
		PaymentBillModel paymentBill = paymentBillDao.searchById(id);
		
		if(paymentBill == null) {
			throw new DerpException("应付账单不存在") ;
		}
		
		Map<String, Object> customerParam = new HashMap<>();
        customerParam.put("customerid", paymentBill.getSupplierId());
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customerParam);
        
        customerParam = new HashMap<>();
        customerParam.put("customerId", paymentBill.getSupplierId());
        customerParam.put("merchantId", paymentBill.getMerchantId());
        
        CustomerMerchantRelMongo customerMerchantRel = customerMerchantRelMongoDao.findOne(customerParam);
        
        //查询事业部
        Map<String, Object> relParams = new HashMap<String, Object>() ;
        relParams.put("merchantId", paymentBill.getMerchantId());
        relParams.put("buId", paymentBill.getBuId());
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
		
        //获取明细
		PaymentItemModel queryItemModel = new PaymentItemModel() ;
		queryItemModel.setPaymentId(id);
		
		List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
		
		//获取费用
		PaymentCostItemModel queryCostItemModel = new PaymentCostItemModel() ;
		queryCostItemModel.setPaymentId(id);
		
		List<PaymentCostItemModel> costList = paymentCostItemDao.list(queryCostItemModel);
		
		PaymentSummaryModel querySummaryModel = new PaymentSummaryModel() ;
		querySummaryModel.setPaymentId(id);
		
		List<PaymentSummaryModel> summaryList = paymentSummaryDao.list(querySummaryModel);
		
		Map<String, Details> sumMap = new LinkedHashMap<String, Details>() ;
		
		//表头
        ReceiveBillJsonRoot root = new ReceiveBillJsonRoot() ;
        root.setConfirmBillId(paymentBill.getCode());
        root.setSourceCode(ApolloUtil.ncSourceType);
        root.setType("2");
        root.setCorCcode(user.getTopidealCode());
        root.setCusCcode(supplier.getMainId());
        root.setYearMonth(TimeUtils.format(paymentBill.getBillDate(), "yyyyMM"));
        root.setCurrency(paymentBill.getCurrency());
        root.setChecked("1");
        root.setInvoiced("");
        root.setRemark("");
        
        /**汇总应付汇总 ”结算金额（含税）“合计*/
        BigDecimal settlementTaxAmount = summaryList.stream().map(PaymentSummaryModel::getSettlementTaxAmount)
        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        /**汇总应付汇总 ”税额“合计*/
        BigDecimal taxAmount = summaryList.stream().map(PaymentSummaryModel::getTax)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        root.setTotalAmount(settlementTaxAmount.setScale(2, BigDecimal.ROUND_HALF_UP)) ;
        root.setTaxAmount(taxAmount.setScale(2, BigDecimal.ROUND_HALF_UP)) ;
        
        OperateLogModel queryModel = new OperateLogModel() ;
        queryModel.setRelCode(paymentBill.getCode());
        
        List<OperateLogModel> operateList = operateLogDao.list(queryModel);
        
        operateList = operateList.stream().sorted(Comparator.comparing(OperateLogModel::getOperateDate).reversed())
        		.collect(Collectors.toList());
        
        if(!operateList.isEmpty()) {
        	
        	for (OperateLogModel tempModel : operateList) {
				
        		if(StringUtils.isNotBlank(tempModel.getOperateResult())
        				&& tempModel.getOperateResult().contains("通过")) {
        			
        			root.setCreated(TimeUtils.format(tempModel.getOperateDate(), "yyyy-MM-dd HH:mm:ss"));
        			
        			break ;
        		}
        		
			}
        	
        }
		
		Integer index = 1; 
		
		Map<String, BrandSuperiorMongo> brandSuperiorCacheMap = new HashMap<>() ;
		
		for (PaymentItemModel paymentItemModel : itemList) {
			
			String projectName = paymentItemModel.getProjectName();
			String superiorParentBrandName = "" ;
			String ncCode = "" ;
			
			Long goodsId = paymentItemModel.getGoodsId();
			
			Map<String, Object> queryMap = new HashMap<>() ;
			queryMap.put("merchandiseId", goodsId) ;
			
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryMap);
			
			if(merchandise != null) {
				
				String commbarcode = merchandise.getCommbarcode();
				
				BrandSuperiorMongo brandSuperiorMongo = brandSuperiorCacheMap.get(commbarcode);
				
				if(brandSuperiorMongo == null) {
					queryMap.clear();
					queryMap.put("commbarcode", commbarcode) ;
					
					CommbarcodeMongo commbarcodeMongo = commbarcodeMongoDao.findOne(queryMap);
					
					if(commbarcodeMongo != null) {
						Long commBrandParentId = commbarcodeMongo.getCommBrandParentId();
						
						queryMap.clear();
						queryMap.put("brandParentId", commBrandParentId) ;
						
						BrandParentMongo brandParent = brandParentMongoDao.findOne(queryMap);
						
						if(brandParent != null) {
							Long superiorParentBrandId = brandParent.getSuperiorParentBrandId();
							
							queryMap.clear();
							queryMap.put("brandSuperiorId", superiorParentBrandId) ;
							
							brandSuperiorMongo = brandSuperiorMongoDao.findOne(queryMap);
							
							brandSuperiorCacheMap.put(commbarcode, brandSuperiorMongo) ;
						}
					}
				}
				
				superiorParentBrandName = brandSuperiorMongo == null ? "" : brandSuperiorMongo.getName() ;
				ncCode = brandSuperiorMongo == null ? "" : brandSuperiorMongo.getNcCode() ;
			}
			
			String key = projectName + "__" + superiorParentBrandName ;
			
			Details details = sumMap.get(key);
			
			if(details == null) {
				
				index += 1 ;
				
				details = new Details() ;
				
				details.setSindex(String.valueOf(index));
				details.setCurrency(paymentBill.getCurrency());
				details.setBrandCode(ncCode);
				details.setAbstracts(TimeUtils.format(paymentBill.getBillDate(), "MM月") + projectName);
				details.setAmount(new BigDecimal(0));
				details.setTax(new BigDecimal(0));
				
				if(customerMerchantRel != null) {
					
					BigDecimal tempRate = customerMerchantRel.getRate() ;
					tempRate = tempRate.multiply(new BigDecimal(100)) ;
					
					String rateStr = tempRate.stripTrailingZeros().toPlainString() + "%";
					
					details.setRate(rateStr);
				}
				
				if(merchantBuRelMongo != null) {
					details.setDetpCode(merchantBuRelMongo.getBuCode());
				}
				
				//收支项目编码
				SettlementConfigModel settlementConfig = 
						settlementConfigDao.searchById(paymentItemModel.getProjectId());
				
				if(settlementConfig == null) {
					throw new DerpException("根据费项ID：" + paymentItemModel.getProjectId() + 
							"费项名：" + paymentItemModel.getProjectName() + "查询费项配置不存在") ;
				}
				
				details.setInExpCode(settlementConfig.getInExpCode());
			}
			
			BigDecimal amount = details.getAmount();
			BigDecimal tax = details.getTax();
			
			amount = amount.add(paymentItemModel.getSettlementAmount().add(paymentItemModel.getSettlementTax())) ;
			tax = tax.add(paymentItemModel.getSettlementTax()) ;
			
			details.setAmount(amount);
			details.setTax(tax);
			
			sumMap.put(key, details) ;
		}
		
		for (PaymentCostItemModel costItemModel : costList) {
			
			String projectName = costItemModel.getProjectName();
			String superiorParentBrandName = costItemModel.getSuperiorParentBrandName() ;
			String ncCode = "" ;
			
			String key = projectName + "__" + superiorParentBrandName ;
			
			Details details = sumMap.get(key);
			
			if(details == null) {
				
				index += 1 ;
				
				details = new Details() ;
				
				details.setSindex(String.valueOf(index));
				details.setCurrency(paymentBill.getCurrency());
				details.setBrandCode(ncCode);
				details.setAbstracts(TimeUtils.format(paymentBill.getBillDate(), "MM") + projectName);
				details.setAmount(new BigDecimal(0));
				details.setTax(new BigDecimal(0));
				
				if(customerMerchantRel != null) {
					
					BigDecimal tempRate = customerMerchantRel.getRate() ;
					tempRate = tempRate.multiply(new BigDecimal(100)) ;
					
					String rateStr = tempRate.stripTrailingZeros().toPlainString() + "%";
					
					details.setRate(rateStr);
				}
				
				if(merchantBuRelMongo != null) {
					details.setDetpCode(merchantBuRelMongo.getBuCode());
				}
				
				//收支项目编码
				SettlementConfigModel settlementConfig = 
						settlementConfigDao.searchById(costItemModel.getProjectId());
				
				if(settlementConfig == null) {
					throw new DerpException("根据费项ID：" + costItemModel.getProjectId() + 
							"费项名：" + costItemModel.getProjectName() + "查询费项配置不存在") ;
				}
				
				details.setInExpCode(settlementConfig.getInExpCode());
			}
			
			BigDecimal amount = details.getAmount();
			BigDecimal tax = details.getTax();
			
			if(costItemModel.getType().equals("1")) {
				amount = amount.add(costItemModel.getCostAmount()).add(costItemModel.getTax()) ;
				tax = tax.add(costItemModel.getTax()) ;
			}else if(costItemModel.getType().equals("0")) {
				amount = amount.subtract(costItemModel.getCostAmount()).subtract(costItemModel.getTax()) ;
				tax = tax.subtract(costItemModel.getTax()) ;
			}
			
			details.setAmount(amount);
			details.setTax(tax);
			
			sumMap.put(key, details) ;
		}
		
		root.setDetails(new ArrayList<>(sumMap.values()));
		
		JSONObject json = JSONObject.fromObject(root);
        String clearText = json.toString() ;
        
        //提交NC
        String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_07.getUri(), clearText);

        JSONObject resultJson =JSONObject.fromObject(ncResult);
        
        if (resultJson.containsKey("code") && resultJson.getString("code").equals("1001")) {
        	
            PaymentBillModel updateModel = new PaymentBillModel() ;
            updateModel.setId(paymentBill.getId());
            updateModel.setNcStatus(DERP_ORDER.PAYMENT_BILL_NCSTATUS_11);
            updateModel.setNcSynDate(TimeUtils.getNow());
            updateModel.setSynchronizerId(user.getId());
            updateModel.setSynchronizer(user.getName());
            
            paymentBillDao.modify(updateModel) ;
            
        } else if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
            throw new DerpException(resultJson.getString("msg"));
        } else {
            throw new DerpException(resultJson.toString());
        }
        
        /**记录操作日志*/
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, paymentBill.getCode(), "同步NC", null, null);
	}

	@Override
	public String getDetailPoNo(Long id) throws SQLException {
		
		String poNos = null ;
		
		/**查询付款明细*/
		PaymentItemModel queryItemModel = new PaymentItemModel() ;
		queryItemModel.setPaymentId(id);
		
		List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
		Set<String> poSet = new TreeSet<>() ;
		
		for (PaymentItemModel paymentItemModel : itemList) {
			
			Long purchaseId = paymentItemModel.getPurchaseId();
			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseId);
			
			String poNo = purchaseOrder.getPoNo();
			
			if(StringUtils.isBlank(poNo)) {
				continue ;
			}
			
			poSet.add(poNo) ;
			
		}
		
		poNos = StringUtils.join(poSet, ",") ;
		
		return poNos;
	}

	@Override
	public List<PaymentSummaryDTO> getPaymentSummary(Long id) throws SQLException {
		
		PaymentSummaryModel queryModel = new PaymentSummaryModel() ;
		queryModel.setPaymentId(id);
		
		List<PaymentSummaryModel> list = paymentSummaryDao.list(queryModel);
		List<PaymentSummaryDTO> dtoList = new ArrayList<PaymentSummaryDTO>() ;
		
		for (PaymentSummaryModel paymentSummaryModel : list) {
			
			PaymentSummaryDTO dto = new PaymentSummaryDTO() ;
			BeanUtils.copyProperties(paymentSummaryModel, dto);
			
			dtoList.add(dto) ;
		}
		
		return dtoList;
	}

	@Override
	public List<PaymentPrintSummaryDTO> getListPrintSummary(Long id) throws SQLException {
		
		PaymentBillModel paymentBill = paymentBillDao.searchById(id);
		
		if(paymentBill == null) {
			throw new DerpException("应付账单不存在") ;
		}
		
		//获取明细
		PaymentItemModel queryItemModel = new PaymentItemModel() ;
		queryItemModel.setPaymentId(id);
		
		List<PaymentItemModel> itemList = paymentItemDao.list(queryItemModel);
		
		//获取费用
		PaymentCostItemModel queryCostItemModel = new PaymentCostItemModel() ;
		queryCostItemModel.setPaymentId(id);
		
		List<PaymentCostItemModel> costList = paymentCostItemDao.list(queryCostItemModel);
		
		Map<String, PaymentPrintSummaryDTO> sumMap = new LinkedHashMap<String, PaymentPrintSummaryDTO>() ;
		
		Integer index = 0; 
		
		Map<String, BrandSuperiorMongo> brandSuperiorCacheMap = new HashMap<>() ;
		
		for (PaymentItemModel paymentItemModel : itemList) {
			
			String projectName = paymentItemModel.getProjectName();
			String superiorParentBrandName = "" ;
			
			Long goodsId = paymentItemModel.getGoodsId();
			
			Map<String, Object> queryMap = new HashMap<>() ;
			queryMap.put("merchandiseId", goodsId) ;
			
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryMap);
			
			if(merchandise != null) {
				
				String commbarcode = merchandise.getCommbarcode();
				
				BrandSuperiorMongo brandSuperiorMongo = brandSuperiorCacheMap.get(commbarcode);
				
				if(brandSuperiorMongo == null) {
					queryMap.clear();
					queryMap.put("commbarcode", commbarcode) ;
					
					CommbarcodeMongo commbarcodeMongo = commbarcodeMongoDao.findOne(queryMap);
					
					if(commbarcodeMongo != null) {
						Long commBrandParentId = commbarcodeMongo.getCommBrandParentId();
						
						queryMap.clear();
						queryMap.put("brandParentId", commBrandParentId) ;
						
						BrandParentMongo brandParent = brandParentMongoDao.findOne(queryMap);
						
						if(brandParent != null) {
							Long superiorParentBrandId = brandParent.getSuperiorParentBrandId();
							
							queryMap.clear();
							queryMap.put("brandSuperiorId", superiorParentBrandId) ;
							
							brandSuperiorMongo = brandSuperiorMongoDao.findOne(queryMap);
							
							brandSuperiorCacheMap.put(commbarcode, brandSuperiorMongo) ;
						}
					}
				}
				
				superiorParentBrandName = brandSuperiorMongo == null ? "" : brandSuperiorMongo.getName() ;
			}
			
			String key = projectName + "__" + superiorParentBrandName ;
			
			PaymentPrintSummaryDTO details = sumMap.get(key);
			
			if(details == null) {
				
				index += 1 ;
				
				details = new PaymentPrintSummaryDTO() ;
				
				details.setIndex(String.valueOf(index));
				details.setAbstractName(superiorParentBrandName + projectName);
				details.setSuperiorParentBrandName(superiorParentBrandName);
				details.setAmount(new BigDecimal(0));
				
				//收支项目编码
				SettlementConfigModel settlementConfig = 
						settlementConfigDao.searchById(paymentItemModel.getProjectId());
				
				if(settlementConfig == null) {
					throw new DerpException("根据费项ID：" + paymentItemModel.getProjectId() + 
							"费项名：" + paymentItemModel.getProjectName() + "查询费项配置不存在") ;
				}
				
				ReceivePaymentSubjectModel ncSubject = 
						receivePaymentSubjectDao.searchById(settlementConfig.getPaymentSubjectId());
				
				details.setNcName(ncSubject.getName());
			}
			
			BigDecimal amount = details.getAmount();
			amount = amount.add(paymentItemModel.getSettlementAmount()) ;
			
			details.setAmount(amount);
			
			sumMap.put(key, details) ;
		}
		
		for (PaymentCostItemModel costItemModel : costList) {
			
			String projectName = costItemModel.getProjectName();
			String superiorParentBrandName = costItemModel.getSuperiorParentBrandName() ;
			
			String key = projectName + "__" + superiorParentBrandName ;
			
			PaymentPrintSummaryDTO details = sumMap.get(key);
			
			if(details == null) {
				
				index += 1 ;
				
				details = new PaymentPrintSummaryDTO() ;
				
				details.setIndex(String.valueOf(index));
				details.setAbstractName(superiorParentBrandName + projectName);
				details.setSuperiorParentBrandName(superiorParentBrandName);
				details.setAmount(new BigDecimal(0));
				
				//收支项目编码
				SettlementConfigModel settlementConfig = 
						settlementConfigDao.searchById(costItemModel.getProjectId());
				
				if(settlementConfig == null) {
					throw new DerpException("根据费项ID：" + costItemModel.getProjectId() + 
							"费项名：" + costItemModel.getProjectName() + "查询费项配置不存在") ;
				}
				
				ReceivePaymentSubjectModel ncSubject = 
						receivePaymentSubjectDao.searchById(settlementConfig.getPaymentSubjectId());
				
				details.setNcName(ncSubject.getName());
			}
			
			BigDecimal amount = details.getAmount();
			
			if(costItemModel.getType().equals("1")) {
				amount = amount.add(costItemModel.getCostAmount()) ;
			}else if(costItemModel.getType().equals("0")) {
				amount = amount.subtract(costItemModel.getCostAmount()) ;
			}
			
			details.setAmount(amount);
			
			sumMap.put(key, details) ;
		}
		
		return new ArrayList<>(sumMap.values());
	}

	@Override
	public PaymentBillDTO changeMerchantInfo(PaymentBillDTO detail) {
		
		Long merchantId = detail.getMerchantId();
		
		Map<String, Object> queryMap = new HashMap<>() ;
		
		queryMap.put("merchantId", merchantId) ;
		
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMap);
		
		detail.setMerchantName(merchant.getFullName());
		
		return detail;
	}

	@Override
	public void updatePrintingInfo(Long id, User user) throws SQLException {
		
		PaymentBillModel payment = paymentBillDao.searchById(id);
		
		PaymentBillModel updateModel = new PaymentBillModel() ;
		
		updateModel.setId(id);
		updateModel.setPrintingState(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_1);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		paymentBillDao.modify(updateModel) ;
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_3, 
				payment.getCode(), "打印", null, null);
		
	}

    @Override
    public String createPaymentBillExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        PaymentBillDTO dto = (PaymentBillDTO) JSONObject.toBean(jsonData, PaymentBillDTO.class);

        basePath = basePath + "/" + task.getTaskType() + "/" + task.getMerchantId() + "/" + task.getOwnMonth();

        System.out.println("商家Id=" + task.getMerchantId() + ",月份=" + task.getOwnMonth() + "，类型：应付账单---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        String sheetFirstName = "应付账单列表";
        String[] firstColumns = {"应付账单号", "公司", "事业部", "供应商", "币种", "应付金额", "待付款金额", "已付款金额", "预计付款日期", "账单状态", "打印状态", "收款账号", "收款银行", "收款账户", "NC单据", "NC状态", "会计期间"
                , "创建人", "创建时间"};
        String[] firstKeys = {"code", "merchantName", "buName", "supplierName", "currency", "payableAmount", "surplusAmount", "paymentAmount", "expectedPaymentDate", "billStatusLabel", "printingStateLabel"
                , "bankAccount", "depositBank", "beneficiaryName", "ncCode", "ncStatusLabel", "period", "creater", "createDate"};

        String sheetSecondName = "应付明细";
        String[] secondColumns = {"应付账单号", "采购单号", "PO号", "母品牌", "商品货号", "商品名称", "费项名称", "采购数量", "采购金额（不含税）", "本期结算金额（不含税）"};
        String[] secondKeys = {"paymentCode", "purchaseCode", "poNo", "superiorParentBrandName", "goodsNo", "goodsName", "projectName", "num", "purchaseAmount","settlementAmount"};

        String sheetThirdName = "费用明细";
        String[] thirdColumns = {"应付账单号", "费项名称", "类型", "PO号", "商品货号", "商品名称", "母品牌", "数量", "费用金额（不含税）"};
        String[] thirdKeys = {"paymentCode", "projectName", "typeLabel","poNo", "goodsNo", "goodsName", "superiorParentBrandName", "num", "costAmount"};

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(task.getUserId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return null;
            }

            dto.setBuIds(buIds);
        }

        //获取表头
        List<PaymentBillDTO> paymentBillDTOList = paymentBillDao.listForExport(dto);
        if(paymentBillDTOList == null || paymentBillDTOList.isEmpty()) {
            return null;
        }

        Map<Long, String> idAndCodeMap = new HashMap<>();
        StringBuilder ids = new StringBuilder();
        paymentBillDTOList.stream().forEach(entity -> {
            if(StringUtils.isBlank(entity.getPrintingState())) {
                entity.setPrintingState(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_0);
            }
            idAndCodeMap.put(entity.getId(), entity.getCode());
            ids.append(entity.getId()).append(",");
        });

        int pageSize = 5000; //每页5000
        PaymentItemDTO itemDTO = new PaymentItemDTO();
        itemDTO.setPaymentIds(ids.substring(0, ids.length() -1));
        int exportItemCount = paymentItemDao.countByDTO(itemDTO);

        PaymentCostItemDTO costItemDTO = new PaymentCostItemDTO();
        costItemDTO.setPaymentIds(ids.substring(0, ids.length() -1));
        int exportCostItemCount = paymentCostItemDao.countByDTO(costItemDTO);

        List<PaymentItemDTO> paymentItemDTOList = new ArrayList<>();
        List<PaymentCostItemDTO> paymentCostItemDTOList = new ArrayList<>();
        for (int i = 0 ; i < exportItemCount; ) {
            int pageSub = (i + pageSize) < exportItemCount ? (i + pageSize) : exportItemCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            paymentItemDTOList.addAll(paymentItemDao.listForExport(itemDTO));
            i = pageSub;
        }

        paymentItemDTOList.stream().forEach(entity -> {
            entity.setPaymentCode(idAndCodeMap.get(entity.getPaymentId()));
        });

        for (int i = 0 ; i < exportCostItemCount; ) {
            int pageSub = (i + pageSize) < exportCostItemCount ? (i + pageSize) : exportCostItemCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            paymentCostItemDTOList.addAll(paymentCostItemDao.listForExport(costItemDTO));
            i = pageSub;
        }
        paymentCostItemDTOList.stream().forEach(entity -> {
            entity.setPaymentCode(idAndCodeMap.get(entity.getPaymentId()));
        });

        //生成表格
        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();

        ExportExcelSheet firstSheet = ExcelUtilXlsx.createSheet(sheetFirstName, firstColumns, firstKeys, paymentBillDTOList);
        sheets.add(firstSheet);

        ExportExcelSheet secondSheet = ExcelUtilXlsx.createSheet(sheetSecondName, secondColumns, secondKeys, paymentItemDTOList);
        sheets.add(secondSheet);

        ExportExcelSheet thirdSheet = ExcelUtilXlsx.createSheet(sheetThirdName, thirdColumns, thirdKeys, paymentCostItemDTOList);
        sheets.add(thirdSheet);

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
        //导出文件
        String fileName = task.getRemark() + ".xlsx";

        if (isExist) {
            //删除目录下的子文件
            DownloadExcelUtil.deleteFile(basePath);
            isExist = false;
        }

        //创建目录
        new File(basePath).mkdirs();

        FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
        wb.write(fileOut);
        fileOut.close();

        paymentItemDTOList.clear();
        paymentCostItemDTOList.clear();

        return basePath;
    }

    @Override
    public int countByDTO(PaymentBillDTO dto) {
        return paymentBillDao.countByDTO(dto);
    }

    @Override
    public List<ExportExcelSheet> exportPaymentBillExcel(User user, PaymentBillDTO dto) {
        String sheetFirstName = "应付账单列表";
        String[] firstColumns = {"应付账单号", "公司", "事业部", "供应商", "币种", "应付金额", "待付款金额", "已付款金额", "预计付款日期", "账单状态", "打印状态", "收款账号", "收款银行", "收款账户", "NC单据", "NC状态", "会计期间"
                , "创建人", "创建时间"};
        String[] firstKeys = {"code", "merchantName", "buName", "supplierName", "currency", "payableAmount", "surplusAmount", "paymentAmount", "expectedPaymentDate", "billStatusLabel", "printingStateLabel"
                , "bankAccount", "depositBank", "beneficiaryName", "ncCode", "ncStatusLabel", "period", "creater", "createDate"};

        String sheetSecondName = "应付明细";
        String[] secondColumns = {"应付账单号", "采购单号", "PO号", "母品牌", "商品货号", "商品名称", "费项名称", "采购数量", "采购金额（不含税）", "本期结算金额（不含税）"};
        String[] secondKeys = {"paymentCode", "purchaseCode", "poNo", "superiorParentBrandName", "goodsNo", "goodsName", "projectName", "num", "purchaseAmount","settlementAmount"};

        String sheetThirdName = "费用明细";
        String[] thirdColumns = {"应付账单号", "费项名称", "类型", "PO号", "商品货号", "商品名称", "母品牌", "数量", "费用金额（不含税）"};
        String[] thirdKeys = {"paymentCode", "projectName", "typeLabel","poNo", "goodsNo", "goodsName", "superiorParentBrandName", "num", "costAmount"};

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return null;
            }

            dto.setBuIds(buIds);
        }

        //获取表头
        List<PaymentBillDTO> paymentBillDTOList = paymentBillDao.listForExport(dto);
        if(paymentBillDTOList == null || paymentBillDTOList.isEmpty()) {
            return null;
        }

        Map<Long, String> idAndCodeMap = new HashMap<>();
        StringBuilder ids = new StringBuilder();
        paymentBillDTOList.stream().forEach(entity -> {
            if(StringUtils.isBlank(entity.getPrintingState())) {
                entity.setPrintingState(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_0);
            }
            idAndCodeMap.put(entity.getId(), entity.getCode());
            ids.append(entity.getId()).append(",");
        });

        int pageSize = 5000; //每页5000
        PaymentItemDTO itemDTO = new PaymentItemDTO();
        itemDTO.setPaymentIds(ids.substring(0, ids.length() -1));
        int exportItemCount = paymentItemDao.countByDTO(itemDTO);

        PaymentCostItemDTO costItemDTO = new PaymentCostItemDTO();
        costItemDTO.setPaymentIds(ids.substring(0, ids.length() -1));
        int exportCostItemCount = paymentCostItemDao.countByDTO(costItemDTO);

        List<PaymentItemDTO> paymentItemDTOList = new ArrayList<>();
        List<PaymentCostItemDTO> paymentCostItemDTOList = new ArrayList<>();
        for (int i = 0 ; i < exportItemCount; ) {
            int pageSub = (i + pageSize) < exportItemCount ? (i + pageSize) : exportItemCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            paymentItemDTOList.addAll(paymentItemDao.listForExport(itemDTO));
            i = pageSub;
        }

        paymentItemDTOList.stream().forEach(entity -> {
            entity.setPaymentCode(idAndCodeMap.get(entity.getPaymentId()));
        });

        for (int i = 0 ; i < exportCostItemCount; ) {
            int pageSub = (i + pageSize) < exportCostItemCount ? (i + pageSize) : exportCostItemCount;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            paymentCostItemDTOList.addAll(paymentCostItemDao.listForExport(costItemDTO));
            i = pageSub;
        }
        paymentCostItemDTOList.stream().forEach(entity -> {
            entity.setPaymentCode(idAndCodeMap.get(entity.getPaymentId()));
        });

        //生成表格
        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();

        ExportExcelSheet firstSheet = ExcelUtilXlsx.createSheet(sheetFirstName, firstColumns, firstKeys, paymentBillDTOList);
        sheets.add(firstSheet);
        paymentBillDTOList = null;

        ExportExcelSheet secondSheet = ExcelUtilXlsx.createSheet(sheetSecondName, secondColumns, secondKeys, paymentItemDTOList);
        sheets.add(secondSheet);
        paymentItemDTOList = null;

        ExportExcelSheet thirdSheet = ExcelUtilXlsx.createSheet(sheetThirdName, thirdColumns, thirdKeys, paymentCostItemDTOList);
        sheets.add(thirdSheet);
        paymentCostItemDTOList = null;

        return sheets;
    }

}
