package com.topideal.order.service.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseInvoiceDao;
import com.topideal.dao.purchase.PurchaseInvoiceItemDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceItemModel;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseInvoiceService;
import com.topideal.order.service.purchase.PurchaseOrderService;
import org.apache.axis.utils.IDKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.IvParameterSpec;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService{

	@Autowired
	private PurchaseInvoiceDao purchaseInvoiceDao ;

	@Autowired
	private PurchaseInvoiceItemDao purchaseInvoiceItemDao ;

	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;

	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;

	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;

	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;

	@Autowired
	private CommonBusinessService commonBusinessService ;

	@Override
	public PurchaseInvoiceDTO getInvocieByPurchaseId(Long purchaseId) throws Exception {

		PurchaseOrderModel purchaseModel = new PurchaseOrderModel() ;
		purchaseModel.setId(purchaseId);

		purchaseModel = purchaseOrderDao.getDetails(purchaseModel) ;

		if(purchaseModel == null) {
			throw new DerpException("根据采购订单ID未找到采购订单") ;
		}

		List<PurchaseInvoiceItemDTO> itemList = new ArrayList<PurchaseInvoiceItemDTO>() ;

		PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
		queryItemModel.setPurchaseOrderId(purchaseId);

		List<PurchaseOrderItemModel> purchaseItemList = purchaseOrderItemDao.list(queryItemModel);

		for (PurchaseOrderItemModel purchaseOrderItemModel : purchaseItemList) {

			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("goodsId", purchaseOrderItemModel.getGoodsId()) ;
			queryMap.put("purchaseOrderId", purchaseId) ;

			PurchaseInvoiceItemModel countModel = purchaseInvoiceItemDao.getInvoiceNum(queryMap) ;

			Integer num = purchaseOrderItemModel.getNum() ;
			if(countModel != null
					&& countModel.getNum() != null) {
				num -= countModel.getNum() ;
			}

			PurchaseInvoiceItemDTO itemDto = new PurchaseInvoiceItemDTO() ;

			itemDto.setPurchaseAmount(purchaseOrderItemModel.getAmount());
			itemDto.setPurchaseNum(purchaseOrderItemModel.getNum());
			itemDto.setPurchasePrice(purchaseOrderItemModel.getPrice());
			itemDto.setGoodsId(purchaseOrderItemModel.getGoodsId());
			itemDto.setGoodsName(purchaseOrderItemModel.getGoodsName());
			itemDto.setGoodsNo(purchaseOrderItemModel.getGoodsNo());
			itemDto.setNum(num);
			itemDto.setTaxRate(purchaseOrderItemModel.getTaxRate());
			itemDto.setPurchaseItemId(purchaseOrderItemModel.getId());
			itemDto.setBarcode(purchaseOrderItemModel.getBarcode());
			itemDto.setFactoryNo(purchaseOrderItemModel.getFactoryNo());

			itemList.add(itemDto) ;

		}

		PurchaseInvoiceDTO dto = new PurchaseInvoiceDTO() ;

		dto.setPurchaseOrderId(purchaseModel.getId());
		dto.setPurchaseOrderCode(purchaseModel.getCode());
		dto.setSupplierId(purchaseModel.getSupplierId());
		dto.setSupplierName(purchaseModel.getSupplierName());
		dto.setBuId(purchaseModel.getBuId());
		dto.setBuName(purchaseModel.getBuName());
		dto.setMerchantId(purchaseModel.getMerchantId());
		dto.setMerchantName(purchaseModel.getMerchantName());
		dto.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_CGFP));
		dto.setMerchantId(purchaseModel.getMerchantId());
		dto.setMerchantName(purchaseModel.getMerchantName());
		dto.setPoNo(purchaseModel.getPoNo());
		dto.setCurrency(purchaseModel.getCurrency());

		dto.setItemList(itemList);

		return dto;
	}

	@Override
	public void saveInvoice(PurchaseInvoiceDTO dto, User user) throws SQLException {

		// 校验月结关账时间
		FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(dto.getMerchantId());
		closeAccountsMongo.setBuId(dto.getBuId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		String maxCloseAccountsMonth="";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
			maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth) &&
				dto.getDrawInvoiceDate() != null) {
			// 付款日期必须大于关账日期
			if (dto.getDrawInvoiceDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new DerpException("发票日期必须大于关账日期") ;
			}
		}

		if(dto.getInvoiceDate() != null) {

			if (StringUtils.isNotBlank(maxCloseAccountsMonth) &&
					dto.getInvoiceDate() != null) {
				// 付款日期必须大于关账日期
				if (dto.getInvoiceDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new DerpException("收到发票日期必须大于关账日期") ;
				}
			}

		}
		//更新采购订单信息
		PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(dto.getPurchaseOrderId()) ;

		//新增发票信息
		PurchaseInvoiceModel saveModel = new PurchaseInvoiceModel() ;

		BeanUtils.copyProperties(dto, saveModel);

		saveModel.setCreateDate(TimeUtils.getNow());
		saveModel.setCreateName(user.getName());
		saveModel.setCreater(user.getId());
		saveModel.setBusinessModel(purchaseOrderModel.getBusinessModel());

		purchaseInvoiceDao.save(saveModel) ;

		List<PurchaseInvoiceItemDTO> itemList = dto.getItemList();

		for (PurchaseInvoiceItemDTO purchaseInvoiceItemDTO : itemList) {

			if(purchaseInvoiceItemDTO.getAmount() == null
					|| purchaseInvoiceItemDTO.getTaxAmount() == null) {
				throw new DerpException("发票总金额（不含税）或发票总金额（含税）不能为空") ;
			}
			if(purchaseInvoiceItemDTO.getPurchaseItemId() == null){
				throw new DerpException("采购明细id为空，请及时维护") ;
			}

			PurchaseOrderItemModel purchaseItem = purchaseOrderItemDao.searchById(purchaseInvoiceItemDTO.getPurchaseItemId());

			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("purchaseItemId", purchaseInvoiceItemDTO.getPurchaseItemId());
			PurchaseInvoiceItemModel countModel = purchaseInvoiceItemDao.getInvoiceNum(queryMap) ;

			Integer num = purchaseItem.getNum() ;
			if(countModel != null
					&& countModel.getNum() != null) {
				num -= countModel.getNum() ;
			}

			if(purchaseInvoiceItemDTO.getNum() > num) {
				throw new DerpException("本次开票数量不能大于可开票数量") ;
			}

			PurchaseInvoiceItemModel itemModel = new PurchaseInvoiceItemModel() ;

			BeanUtils.copyProperties(purchaseInvoiceItemDTO, itemModel);

			itemModel.setCreateDate(TimeUtils.getNow());
			itemModel.setPurchaseInvoiceId(saveModel.getId());
			itemModel.setFactoryNo(purchaseItem.getFactoryNo());

			purchaseInvoiceItemDao.save(itemModel) ;
		}


		if(purchaseOrderModel == null) {
			throw new DerpException("根据采购订单ID未找到采购订单") ;
		}

		if(purchaseOrderModel.getInvoiceDate() == null) {
			purchaseOrderModel.setInvoiceDate(dto.getInvoiceDate());
		}

		if(purchaseOrderModel.getDrawInvoiceDate() == null) {
			purchaseOrderModel.setDrawInvoiceDate(dto.getDrawInvoiceDate()) ;
		}

		if(StringUtils.isBlank(purchaseOrderModel.getInvoiceName())) {
			purchaseOrderModel.setInvoiceName(dto.getPayName());
		}

		if(StringUtils.isBlank(purchaseOrderModel.getInvoiceNo())) {
			purchaseOrderModel.setInvoiceNo(dto.getInvoiceNo());
		}

		if (StringUtils.isBlank(purchaseOrderModel.getMailStatus())) {
			purchaseOrderModel.setMailStatus(DERP_ORDER.PURCHASEORDER_MAILSTATUS_0);//0 未发送邮件 1,发送邮件1次  2 发送邮件2次
		}

		if(StringUtils.isBlank(purchaseOrderModel.getBillStatus())) {
			purchaseOrderModel.setBillStatus(DERP_ORDER.PURCHASEORDER_BILLSTATUS_1);
		}

		if(purchaseOrderModel.getCargoCuttingDate() == null) {
			purchaseOrderModel.setCargoCuttingDate(dto.getDrawInvoiceDate());
		}

		purchaseOrderModel.setModifyDate(TimeUtils.getNow());

		purchaseOrderDao.modify(purchaseOrderModel) ;

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1,
				purchaseOrderModel.getCode(), "收到发票", null, null);
	}

	@Override
	public PurchaseInvoiceDTO getDetailsById(Long id) throws SQLException {

		PurchaseInvoiceModel model = purchaseInvoiceDao.searchById(id);

		if(model == null) {
			throw new DerpException("根据ID未找到采购发票") ;
		}

		PurchaseInvoiceDTO dto = new PurchaseInvoiceDTO() ;

		BeanUtils.copyProperties(model, dto);

		PurchaseInvoiceItemModel queryItemModel = new PurchaseInvoiceItemModel() ;

		queryItemModel.setPurchaseInvoiceId(id);

		List<PurchaseInvoiceItemDTO> dtoItemList = new ArrayList<PurchaseInvoiceItemDTO>() ;
		List<PurchaseInvoiceItemModel> itemList = purchaseInvoiceItemDao.list(queryItemModel);

		for (PurchaseInvoiceItemModel purchaseInvoiceItemModel : itemList) {

			PurchaseInvoiceItemDTO itemDto = new PurchaseInvoiceItemDTO() ;

			BeanUtils.copyProperties(purchaseInvoiceItemModel, itemDto);

			dtoItemList.add(itemDto) ;

		}

		dto.setItemList(dtoItemList);

		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseInvoiceDTO getListByPage(PurchaseInvoiceDTO dto, User user) {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuIds(buIds);
		}

		dto = purchaseInvoiceDao.getListByPage(dto) ;
		List<PurchaseInvoiceDTO> list=dto.getList();

		for(PurchaseInvoiceDTO invoice:list){
			//设置发票总金额（不含税）和发票总金额（含税）
			Map<String,Object> queryMap=new HashMap<>();
			queryMap.put("id",invoice.getId());
			PurchaseInvoiceItemModel itemModel=purchaseInvoiceItemDao.getInvoiceNum(queryMap);
			if(itemModel!=null){
				invoice.setAmount(itemModel.getAmount());
				invoice.setTaxAmount(itemModel.getTaxAmount());
			}
		}

		return dto ;
	}

	@Override
	public void delInvoice(String ids, User user) throws SQLException {

		List<Long> idList = StrUtils.parseIds(ids);

		//采购发票
		List<PurchaseInvoiceModel> invoiceModels = purchaseInvoiceDao.getInvoiceByIds(idList);

		List<Long> purchaseOrderIds = new ArrayList<>();
		for (PurchaseInvoiceModel purchaseInvoiceModel : invoiceModels) {

			//检查发票日期是否为财务报表已关账的月份
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setBuId(purchaseInvoiceModel.getBuId());
			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				if (purchaseInvoiceModel.getDrawInvoiceDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new DerpException("发票日期必须大于关账日期") ;
				}
			}

			purchaseOrderIds.add(purchaseInvoiceModel.getPurchaseOrderId());
		}

		//采购订单
		List<PurchaseOrderModel> purchaseOrderModels = purchaseOrderDao.listByIds(purchaseOrderIds);

		for (PurchaseOrderModel purchaseOrderModel : purchaseOrderModels) {
			if (StringUtils.isNotBlank(purchaseOrderModel.getWriteOffStatus())) {
				throw new RuntimeException("不能删除采购订单的红冲状态为待红冲或已红冲的数据");
			}

			if (!DERP_ORDER.PURCHASEORDER_BILLSTATUS_1.equals(purchaseOrderModel.getBillStatus())) {
				throw new RuntimeException("不能删除采购订单的付款状态为已付款的数据");
			}
		}

		//删除发票信息
		purchaseInvoiceDao.delete(idList);
		purchaseInvoiceItemDao.batchDelByInvoiceIds(idList);

		for (PurchaseOrderModel purchaseOrderModel : purchaseOrderModels) {
			PurchaseInvoiceModel invoiceModel = new PurchaseInvoiceModel();
			invoiceModel.setPurchaseOrderId(purchaseOrderModel.getId());

			List<PurchaseInvoiceModel> invoices = purchaseInvoiceDao.list(invoiceModel);
			if (invoices.isEmpty()) {
				purchaseOrderModel.setBillStatus(null);
				purchaseOrderDao.modifyWithNull(purchaseOrderModel);
			}
		}

	}
}
