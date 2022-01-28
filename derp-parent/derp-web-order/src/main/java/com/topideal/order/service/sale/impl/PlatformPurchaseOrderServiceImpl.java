package com.topideal.order.service.sale.impl;


import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.PlatformPurchaseOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlatformPurchaseOrderServiceImpl implements PlatformPurchaseOrderService{

	@Autowired
	private PlatformPurchaseOrderDao platformPurchaseOrderDao ;

	@Autowired
	private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao ;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private LbxNoMongoDao lbxNoMongoDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	// 销售单_po关联表
	@Autowired
	private SalePoRelDao salePoRelDao;

	@SuppressWarnings("unchecked")
	@Override
	public PlatformPurchaseOrderDTO listPlatformPurchaseOrder(PlatformPurchaseOrderDTO dto) throws SQLException {

		dto = platformPurchaseOrderDao.getListByPage(dto) ;

		List<PlatformPurchaseOrderDTO> list = (List<PlatformPurchaseOrderDTO>)dto.getList();

		for (PlatformPurchaseOrderDTO platformPurchaseOrderDTO : list) {

			PlatformPurchaseOrderItemModel item = new PlatformPurchaseOrderItemModel() ;

			item.setOrderId(platformPurchaseOrderDTO.getId());
			List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(item);

			platformPurchaseOrderDTO.setSkusNum(itemList.size());
		}

		dto.setList(list);
		return dto;
	}

/*	@Override
	public void modifyStatus(List<Long> list, String status) throws SQLException {

		User user = ShiroUtils.getUser();

		for (Long id : list) {

			PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao.searchById(id);
			PlatformPurchaseOrderModel updateOrder = new PlatformPurchaseOrderModel();
			updateOrder.setId(id);
			updateOrder.setStatus(status);

			switch (status) {
			case DERP_ORDER.PLATFORM_PURCHASE_STATUS_1:

				if(!DERP_ORDER.PLATFORM_PURCHASE_STATUS_2.equals(platformPurchaseOrder.getStatus())) {
					throw new RuntimeException("存在不是待转销售的单据，请重新选择") ;
				}

				break;
			case DERP_ORDER.PLATFORM_PURCHASE_STATUS_2:

				if(!DERP_ORDER.PLATFORM_PURCHASE_STATUS_1.equals(platformPurchaseOrder.getStatus())) {
					throw new RuntimeException("存在不是待转销售的单据，请重新选择") ;
				}

				updateOrder.setSubmitDate(TimeUtils.getNow());
				updateOrder.setSubmiter(user.getId());
				updateOrder.setSubmitName(user.getName());

				break;

			default:
				break;
			}

			platformPurchaseOrderDao.modifyWherSubmit(updateOrder) ;

		}
	}*/

	@Override
	public Map<String, Object> checkOrderToSales(List<Long> list) throws SQLException {

		Map<String, Object> map = new HashMap<String, Object>() ;
		map.put("status", "00") ;

		Long customerId = null ;
		String currency = null ;
		String depotName = null ;
		Map<String, BigDecimal> commbarcodePriceMap = new HashMap<>() ;

		for (int i = 0 ; i < list.size(); i ++) {

			Long id = list.get(i);
			PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao.searchById(id);

			/*if(!DERP_ORDER.PLATFORM_PURCHASE_STATUS_2.equals(platformPurchaseOrder.getStatus())) {
				throw new RuntimeException("存在不是待转销售的单据，请重新选择") ;
			}*/

			PlatformPurchaseOrderItemModel queryModel = new PlatformPurchaseOrderItemModel() ;
			queryModel.setOrderId(id);
			List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(queryModel);

			if(i == 0) {
				customerId = platformPurchaseOrder.getCustomerId() ;
				currency = platformPurchaseOrder.getCurrency() ;
				depotName = platformPurchaseOrder.getPlatformDepotName() ;

				for (PlatformPurchaseOrderItemModel item : itemList) {
					commbarcodePriceMap.put(item.getPlatformBarcode(), item.getPrice()) ;
				}

			}else {
				if(customerId.longValue() != platformPurchaseOrder.getCustomerId().longValue()) {
					throw new RuntimeException("一次仅能对相同客户的订单转销售，请重新选择") ;
				}

				if(StringUtils.isBlank(currency)
						|| StringUtils.isBlank(platformPurchaseOrder.getCurrency())) {
					throw new RuntimeException("存在币种为空的单据，请重新选择") ;
				}

				if(!currency.equals(platformPurchaseOrder.getCurrency())) {
					throw new RuntimeException("币种不同，不能合并转单") ;
				}

//				for (PlatformPurchaseOrderItemModel item : itemList) {
//
//					BigDecimal price = commbarcodePriceMap.get(item.getPlatformBarcode());
//
//					if(price != null && price.compareTo(item.getPrice()) != 0) {
//						throw new RuntimeException("同个商品存在不同价格，不能合并转单") ;
//					}else {
//						commbarcodePriceMap.put(item.getPlatformBarcode(), item.getPrice()) ;
//					}
//
//				}

				if(!depotName.equals(platformPurchaseOrder.getPlatformDepotName())) {
					map.put("status", "01") ;
					map.put("msg", "客户存在不同仓库的订单，是否合并转销售？") ;
				}
			}
		}

		return map ;
	}

	@Override
	public PlatformPurchaseOrderDTO searchDTOById(Long id) throws SQLException {

		PlatformPurchaseOrderDTO dto = platformPurchaseOrderDao.searchDTOById(id) ;

		PlatformPurchaseOrderItemModel queryModel = new PlatformPurchaseOrderItemModel() ;
		queryModel.setOrderId(id);

		List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(queryModel);
		dto.setItemList(itemList);

		return dto;
	}

	@Override
	public List<PlatformPurchaseOrderExportDTO> getExportList(PlatformPurchaseOrderExportDTO dto) {
		return platformPurchaseOrderDao.getExportList(dto);
	}

	@Override
	public PlatformPurchaseOrderDTO getPlatformPurchaseOrderItems(List<Long> list) throws SQLException {

		List<PlatformPurchaseOrderItemDTO> listItemDto = new ArrayList<PlatformPurchaseOrderItemDTO>() ;

		PlatformPurchaseOrderDTO platformPurchaseOrderDTO = new PlatformPurchaseOrderDTO();

		for (Long platformId : list) {
			PlatformPurchaseOrderModel platform = platformPurchaseOrderDao.searchById(platformId);

			if(platformPurchaseOrderDTO.getMerchantId() == null){
				platformPurchaseOrderDTO.setMerchantId(platform.getMerchantId());
			}
			if(platformPurchaseOrderDTO.getCustomerId() == null){
				platformPurchaseOrderDTO.setCustomerId(platform.getCustomerId());
			}
			if(platformPurchaseOrderDTO.getCurrency() == null){
				platformPurchaseOrderDTO.setCurrency(platform.getCurrency());
			}

			PlatformPurchaseOrderItemModel queryModel = new PlatformPurchaseOrderItemModel() ;
			queryModel.setOrderId(platform.getId());

			List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(queryModel);

			for (PlatformPurchaseOrderItemModel item : itemList) {
				PlatformPurchaseOrderItemDTO itemDTO = new PlatformPurchaseOrderItemDTO() ;

				itemDTO.setId(item.getId());
				itemDTO.setPoNo(platform.getPoNo());
				itemDTO.setPlatformGoodsName(item.getPlatformGoodsName());
				itemDTO.setPlatformGoodsNo(item.getPlatformGoodsNo());
				itemDTO.setPlatformBarcode(item.getPlatformBarcode());
				itemDTO.setNum(item.getNum());
				itemDTO.setPrice(item.getPrice());
				itemDTO.setPlatformGoodsNo(item.getPlatformGoodsNo());
				itemDTO.setSaleNum(item.getNum());

				listItemDto.add(itemDTO) ;
			}
		}
		platformPurchaseOrderDTO.setItemDTOList(listItemDto);

		return platformPurchaseOrderDTO;
	}

	@Override
	public List<PlatformPurchaseOrderDTO> listPlatformPurchaseOrderByCodeAndPoNo(PlatformPurchaseOrderDTO dto) {
		return platformPurchaseOrderDao.listPlatformPurchaseOrderByCodeAndPoNo(dto);
	}

	@Override
	public Map<Long, List<MerchandiseInfoMogo>> getSaleItems(List<PlatformPurchaseOrderItemDTO> itemList, Long outDepotId, User user) throws Exception {
		Map<Long, List<MerchandiseInfoMogo>> resultMap = new HashMap<>();

		Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>();
		depotMerchantRelMap.put("merchantId", user.getMerchantId());
		depotMerchantRelMap.put("depotId", Long.valueOf(outDepotId));
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

		if (depotMerchantRel == null) {
			throw new RuntimeException("商家仓库关联不存在");
		}
		Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
		merchandiseInfoParams.put("merchantId", user.getMerchantId());
		merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

		for(PlatformPurchaseOrderItemDTO itemDTO : itemList) {
			List<MerchandiseInfoMogo> resultList = new ArrayList<>();
			/**
			 * 出库仓库变更时，根据条码存查询该公司+仓库的选品限制，确定转销售单的货号
			 * 1、判断平台采购单的条形码是否存在多个（用;符合分割），若有多个条形码，则用多个条形码一并获取货号
			 * 2、若有多个货号满足条件，取其中一个
			 */
			String platformBarcode = itemDTO.getPlatformBarcode();
			if (platformBarcode.indexOf(";") > -1) {
				String[] barcodes = platformBarcode.split(";");
				for (String barcode : barcodes) {
					merchandiseInfoParams.put("barcode", barcode.trim());
					List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams, outDepotId);
					if (merchandiseList != null && !merchandiseList.isEmpty()) {
						resultList.addAll(merchandiseList);
						break;
					}
				}
			} else {
				merchandiseInfoParams.put("barcode", platformBarcode.trim());
				List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams, outDepotId);
				if (merchandiseList != null && !merchandiseList.isEmpty()) {
					resultList.addAll(merchandiseList);
				}
			}
			if(resultList.size() < 1){
				/**
				 * 若通过条形码找不到转销的货号，则查询外仓备案商品列表获取经分销货号，条件：平台采购单.货号=外仓备案商品列表.平台商品货号，外仓备案商品列表.公司=当前公司，若查到，则取外仓备案商品列表.经分货号
				 * 判断经分货号是否有关联出库仓库：若关联的货号有一条，则下拉框直接带出货号；若关联的货号有多条，则下拉框默认带出其中一条，用户可再次修改。若没有关联仓库的货号，则显示“未关联出库仓库”。
				 */
				Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
				merchandiseExternalParams.put("goodsNo", itemDTO.getPlatformGoodsNo());
				merchandiseExternalParams.put("merchantId", user.getMerchantId());
				List<MerchandiseExternalWarehouseMongo> externalMerchandiseList =  merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);

				merchandiseInfoParams.clear();
				merchandiseInfoParams.put("merchantId", user.getMerchantId());
				merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
				for(MerchandiseExternalWarehouseMongo externalGoods : externalMerchandiseList){
					if(externalGoods.getDerpMerchandisId() == null || externalGoods.getDerpMerchandisId() == 0){
						continue;
					}
					merchandiseInfoParams.put("merchandiseId", externalGoods.getDerpMerchandisId());
					List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams,outDepotId);
					if(merchandiseList != null && merchandiseList.size() > 0){
						resultList.addAll(merchandiseList);
					}
				}
			}
			resultMap.put(itemDTO.getId(),resultList);
		}
		return resultMap;
	}
	@Override
	public List<String> checkBeforeConfirm(List<PlatformPurchaseOrderItemDTO> itemList, Long outDepotId, Long buId ,User user) throws Exception {
		List<String> messageList = new ArrayList<String>();
		if(buId == null) {
			messageList.add("请选择事业部");
		}

		if(outDepotId == null) {
			messageList.add("请选择出库仓");
		}

		Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>();
		depotMerchantRelMap.put("merchantId", user.getMerchantId());
		depotMerchantRelMap.put("depotId", Long.valueOf(outDepotId));
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

		if (depotMerchantRel == null) {
			messageList.add("商家仓库关联不存在");
		}
		Map<String, Object> outDepotMap = new HashMap<String, Object>();
		outDepotMap.put("status", DERP_SYS.DEPOTINFO_STATUS_1);
		outDepotMap.put("depotId", Long.valueOf(outDepotId));
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(outDepotMap);

		Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
		merchandiseInfoParams.put("merchantId", user.getMerchantId());
		merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
		int nullNum = 0;
		for(PlatformPurchaseOrderItemDTO itemDTO : itemList) {
			//检查有转销数量的商品
			if(itemDTO.getSaleNum() != null) {
				if(StringUtils.isBlank(itemDTO.getSaleGoodsNo())) {
					messageList.add("存在转销数量不为空，转销货号为空的商品");
				}
				merchandiseInfoParams.put("goodsNo", itemDTO.getSaleGoodsNo());
				List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams, outDepotId);

				if (merchandiseList == null || merchandiseList.isEmpty()) {
					messageList.add("出库仓："+outDepot.getName()+"未找到关联的转销售货号，条形码为："+itemDTO.getPlatformBarcode());
				}
				if(merchandiseList != null && merchandiseList.size() > 0){
					PlatformPurchaseOrderItemModel itemModel = new PlatformPurchaseOrderItemModel();
					MerchandiseInfoMogo merchandiseInfo = merchandiseList.get(0);
					itemModel.setSaleGoodsNo(merchandiseInfo.getGoodsNo());
					itemModel.setSaleGoodsName(merchandiseInfo.getName());
					itemModel.setSaleGoodsId(merchandiseInfo.getMerchandiseId());
					itemModel.setSaleBarcode(merchandiseInfo.getBarcode());
					itemModel.setSaleNum(itemDTO.getSaleNum());

					itemModel.setId(itemDTO.getId());
					platformPurchaseOrderItemDao.modify(itemModel);
				}

			}else {
				nullNum++;
			}
		}
		if(nullNum == itemList.size()) {
			messageList.add("请输入转销数量");
		}

		return  messageList;
	}

	@Override
	public void savePlatFormPurchaseToSales(SaleOrderDTO dto, User user) throws Exception {
		if(dto.getBuId() == null) {
			throw new RuntimeException("事业部不能为空");
		}
		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", user.getMerchantId());
		merchantBuRelParam.put("buId", dto.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" + dto.getBuId() + ",未查到该公司下事业部信息");
		}

		// 用户事业部
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), dto.getBuId());
		if (!userRelateBu) {
			throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
		}
		if (dto.getOutDepotId() == null) {
			throw new RuntimeException("保存失败，请选择出仓仓库");
		}
		// 获取出仓仓库信息
		DepotInfoMongo outDepot = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", dto.getOutDepotId());
		outDepot = depotInfoMongoDao.findOne(params);

		if (null != outDepot) {
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId()); // 出仓仓库
			merchantDepotBuRelParam.put("buId", dto.getBuId());
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
				throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",出仓仓库："
						+ outDepot.getDepotCode() + ",未查到该公司仓库事业部关联信息");
			}
		}

		// 仓库公司关联表
		if (outDepot != null) {
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", user.getMerchantId());
			depotMerchantRelParam.put("depotId", dto.getOutDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
				throw new RuntimeException("仓库ID为：" + dto.getOutDepotId() + ",未查到该公司下调出仓库信息");
			}
		}
		if(dto.getBusinessModel() == null) {
			throw new RuntimeException("销售类型不能为空");
		}
		if(dto.getCurrency() == null) {
			throw new RuntimeException("币种不能为空");
		}
		if(dto.getTradeTerms() == null) {
			throw new RuntimeException("贸易条款不能为空");
		}
		if(dto.getPaymentTerms() == null) {
			throw new RuntimeException("付款条约不能为空");
		}
		if(dto.getTransport() == null) {
			throw new RuntimeException("运输方式不能为空");
		}

		DepotInfoMongo inDepot = null;
		if (dto.getInDepotId() != null) {
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", dto.getInDepotId());
			inDepot = depotInfoMongoDao.findOne(params1);
		}
		if (DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())) {
			if (org.apache.commons.lang.StringUtils.isBlank(dto.getIsSameArea())) {
				throw new RuntimeException("保存失败，是否同关区不能为空");
			} else {
				// 是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库；
				if (DERP.ISSAMEAREA_1.equals(dto.getIsSameArea())) {
					if (dto.getInDepotId() == null) {
						throw new RuntimeException("保存失败，入库仓库不能为空");
					} else if (inDepot == null) {
						throw new RuntimeException("保存失败，没有找到入库仓库信息");
					} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
						throw new RuntimeException("保存失败，入库仓库仅能为备查库");
					}
				}
				// 是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
				else if (DERP.ISSAMEAREA_0.equals(dto.getIsSameArea()) && DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
					if (dto.getInDepotId() == null) {
						throw new RuntimeException("保存失败，入库仓库不能为空");
					} else if (inDepot == null) {
						throw new RuntimeException("保存失败，没有找到入库仓库信息");
					} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
						throw new RuntimeException("保存失败，入库仓库只能选备查库");
					}
				}
			}
		} else if (DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())
				&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
			// 当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
			if (dto.getInDepotId() == null) {
				throw new RuntimeException("保存失败，入库仓库不能为空");
			} else if (inDepot == null) {
				throw new RuntimeException("保存失败，没有找到入库仓库信息");
			} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
				throw new RuntimeException("保存失败，入库仓库只能选备查库");
			}
		} else if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())) {
			// 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
				if (dto.getInDepotId() == null) {
					throw new RuntimeException("保存失败，入库仓库不能为空");
				} else if (inDepot == null) {
					throw new RuntimeException("保存失败，没有找到入库仓库信息");
				} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
					throw new RuntimeException("保存失败，入库仓库只能选备查库");
				}
			}
			if(StringUtils.isBlank(dto.getTallyingUnit())){
				throw new RuntimeException("保存失败，出库仓为海外仓，理货单位不能为空");
			}
		}

		if (org.apache.commons.lang.StringUtils.isNotBlank(dto.getLbxNo())) {// lbx不为空，需要校验唯一
			boolean flag = saleOrderDao.lbxNoIsExist(dto.getLbxNo(), dto.getId());
			if (flag) {
				throw new RuntimeException("保存失败，lbx单号已存在");
			}
		}
		//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
		if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() == null){
			throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空") ;
		}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() != null){
			throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写") ;
		}

		BuStockLocationTypeConfigMongo stockLocationMongo = null;
		if(dto.getStockLocationTypeId() != null){
			Map<String,Object> stockLocationMap = new HashMap<>();
			stockLocationMap.put("buStockLocationTypeId", dto.getStockLocationTypeId());
			stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);
			if(stockLocationMongo == null){
				throw new RuntimeException("库位类型输入有误") ;
			}
		}

		// -----------------------菜鸟仓参数校验---------------------------------
		if (org.apache.commons.lang.StringUtils.isNotBlank(dto.getLbxNo()) && outDepot.getName().contains("菜鸟")) {
			Map<String, Object> lbx_params = new HashMap<String, Object>();
			lbx_params.put("lbxNo", dto.getLbxNo());
			lbx_params.put("type", "XSO");
			// 删除lbx号
			lbxNoMongoDao.remove(lbx_params);

			LbxNoMongo lbxNoMongo = new LbxNoMongo();
			lbxNoMongo.setLbxNo(dto.getLbxNo());
			lbxNoMongo.setType("XSO");
			// 往mongodb插入lbx号
			lbxNoMongoDao.insert(lbxNoMongo);
		}

		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("customerid", dto.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);
		// 保存表头数据
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setMerchantId(user.getMerchantId());
		saleOrderModel.setMerchantName(user.getMerchantName());
		saleOrderModel.setOrderType("2");// 单据标识 1-预售转销 2-非预售转销
		saleOrderModel.setPreSaleOrderRelCode(dto.getPreSaleOrderRelCode());// 关联的预售单号
		saleOrderModel.setBuId(dto.getBuId()); // 事业部ID
		saleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		saleOrderModel.setBusinessModel(dto.getBusinessModel());
		saleOrderModel.setCustomerId(dto.getCustomerId());
		saleOrderModel.setCustomerName(customer != null ? customer.getName() : "");
		saleOrderModel.setLbxNo(dto.getLbxNo());
		saleOrderModel.setPoNo(dto.getPoNo());
		saleOrderModel.setOutDepotId(dto.getOutDepotId());
		saleOrderModel.setTransport(dto.getTransport());
		saleOrderModel.setPaymentTerms(dto.getPaymentTerms());
		saleOrderModel.setTradeTerms(dto.getTradeTerms());
		saleOrderModel.setApprovalNo(dto.getApprovalNo());
		saleOrderModel.setRemark(dto.getRemark());
		saleOrderModel.setTallyingUnit(dto.getTallyingUnit());
		if (outDepot != null) {
			saleOrderModel.setOutDepotName(outDepot.getName());
			saleOrderModel.setOutDepotCode(outDepot.getCode());
			saleOrderModel.setInspectNo(outDepot.getInspectNo());
			saleOrderModel.setCustomsNo(outDepot.getCustomsNo());
		}
		if (inDepot != null) {
			saleOrderModel.setInDepotId(dto.getInDepotId());
			saleOrderModel.setInDepotName(inDepot.getName());
			saleOrderModel.setInDepotCode(inDepot.getCode());
		}
		if(stockLocationMongo != null){
			saleOrderModel.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
			saleOrderModel.setStockLocationTypeName(stockLocationMongo.getName());
		}
		saleOrderModel.setIsSameArea(dto.getIsSameArea());
		saleOrderModel.setTallyingUnit(dto.getTallyingUnit());
		saleOrderModel.setCurrency(dto.getCurrency());
		// 调入客户卓志编码
		String inCustomerTopNo = "";
		if (customer != null) {
			inCustomerTopNo = customer.getMainId();
		}
		// 同关区
		boolean isSame = outDepot != null && !org.apache.commons.lang.StringUtils.isEmpty(outDepot.getCustomsNo()) && inDepot != null
				&& !org.apache.commons.lang.StringUtils.isEmpty(inDepot.getCustomsNo())
				&& outDepot.getCustomsNo().equals(inDepot.getCustomsNo());

		// 1.调出调入公司不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移 服务类型：G0：库内调拨服务
		if (!user.getTopidealCode().equals(inCustomerTopNo) && inDepot != null
				&& outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("40");
			saleOrderModel.setServeTypes("G0");
		}
		// 2.调出调入公司不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓 服务类型：E0：区内调拨调出服务
		else if (!user.getTopidealCode().equals(inCustomerTopNo) && isSame && !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("50");
			saleOrderModel.setServeTypes("E0");
		}
		// 3.调出调入公司相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：10： 账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo) && isSame && !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("10");
			saleOrderModel.setServeTypes("E0");
		}
		// 4.调出调入公司相同，出入仓库不同关区，出仓和入库仓库编码不同-业务场景：10 ：账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo) && !(isSame || (StringUtils.isEmpty(outDepot.getCustomsNo()) && inDepot != null
				&& org.apache.commons.lang.StringUtils.isEmpty(inDepot.getCustomsNo()))) && !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("10");
			saleOrderModel.setServeTypes("E0");
		}
		saleOrderModel.setOrderSource(DERP_ORDER.SALEORDER_ORDERSOURCE_1);
		saleOrderModel.setTopidealCode(user.getTopidealCode());
		saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
		saleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_008);// 待提交
		saleOrderModel.setCreateDate(TimeUtils.getNow());
		saleOrderModel.setCreater(user.getId());
		saleOrderModel.setCreateName(user.getName());

		Map<Long,List<SaleOrderItemModel>> itemMap = new HashMap<>();
		Map<Long,Integer> numMap = new HashMap<>();
		Map<Long,BigDecimal> amountMap = new HashMap<>();
		Map<Long,String> poNoMap = new HashMap<>();
//		//校验同一货号价格是否相同
		List<String> checkGoodsNoPrice = new ArrayList<String>();
		int index = 1;
		for (SaleOrderItemDTO item : dto.getItemList()) {
			PlatformPurchaseOrderItemModel platformPurchaseOrderItemModel = platformPurchaseOrderItemDao.searchById(item.getId());
			Long  platformPurchaseOrderId = platformPurchaseOrderItemModel.getOrderId();
			if(item.getNum() == null || item.getNum() <= 0) {
				continue;
			}
			if(StringUtils.isBlank(item.getPoNo())) {
				throw new RuntimeException("保存失败，po号不能为空");
			}
			if(item.getGoodsId() == null) {
				throw new RuntimeException("po号："+item.getPoNo()+"，商品不能为空");
			}
			if(item.getPrice() == null) {
				throw new RuntimeException("po号："+item.getPoNo()+"，销售单价（不含税）不能为空");
			}
			if(item.getAmount() == null) {
				throw new RuntimeException("po号："+item.getPoNo()+"，销售金额（不含税）不能为空");
			}
			if(item.getTaxRate() == null) {
				throw new RuntimeException("po号："+item.getPoNo()+"，税率不能为空");
			}

			String goodsNoPrice = item.getGoodsNo()+"_"+item.getPrice()+"_"+item.getPoNo();
			if(checkGoodsNoPrice.contains(goodsNoPrice)){
				throw new RuntimeException("po号："+item.getPoNo()+"同一商品货号："+item.getGoodsNo()+"，销售单价不允许相同") ;
			}else{
				checkGoodsNoPrice.add(goodsNoPrice);
			}

			Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
			merchandiseInfoParams.put("merchandiseId", item.getGoodsId());
			merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseInfoParams);
			// 注入数据
			SaleOrderItemModel itemModel = new SaleOrderItemModel();
			itemModel.setBarcode(merchandise.getBarcode());
			itemModel.setCommbarcode(merchandise.getCommbarcode());
			itemModel.setGoodsCode(merchandise.getGoodsCode());
			itemModel.setGoodsId(item.getGoodsId());
			itemModel.setGoodsName(merchandise.getName());
			itemModel.setGoodsNo(merchandise.getGoodsNo());
			itemModel.setNum(item.getNum());
			itemModel.setPrice(item.getPrice());
			itemModel.setAmount(item.getAmount());
			itemModel.setTaxAmount(item.getTaxAmount());// 销售总金额（含税）
			itemModel.setTaxRate(item.getTaxRate());// 税率
			itemModel.setTax(item.getTax());// 税额
			BigDecimal taxPrice = null;
			if(item.getTaxAmount() != null) {
				taxPrice = item.getTaxAmount().divide(new BigDecimal(item.getNum()), 8, RoundingMode.HALF_UP);
			}
			itemModel.setTaxPrice(taxPrice);// 销售单价（含税）
			if (item.getSeq() != null) {
				itemModel.setSeq(item.getSeq());
			} else {
				itemModel.setSeq(index++);
			}

			if(itemModel.getGoodsId() != null){
				// 获取商品信息
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchandiseId", item.getGoodsId());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
				List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap,dto.getOutDepotId());
				if (merchandiseList == null || merchandiseList.size() < 1) {
					throw new RuntimeException("出库仓："+ outDepot.getName() +"未找到商品货号为：" + item.getGoodsNo() + "的商品");
				}
			}
			itemModel.setCommbarcode(merchandise.getCommbarcode()); // 标准条码
			List<SaleOrderItemModel> itemList = itemMap.get(platformPurchaseOrderId);
			if(itemList == null){
				itemList = new ArrayList<>();
			}
			itemList.add(itemModel);
			itemMap.put(platformPurchaseOrderId,itemList);

			Integer num = numMap.get(item.getPoNo()) == null ? 0 : numMap.get(item.getPoNo());
			num += item.getNum();
			numMap.put(platformPurchaseOrderId, num);

			BigDecimal amount = amountMap.get(item.getPoNo()) == null ? BigDecimal.ZERO : amountMap.get(item.getPoNo());
			amount = amount.add(item.getAmount());
			amountMap.put(platformPurchaseOrderId, amount);

			//更新平台采购单表体 转销货号、转销数量
			platformPurchaseOrderItemModel.setSaleGoodsNo(merchandise.getGoodsNo());
			platformPurchaseOrderItemModel.setSaleGoodsName(merchandise.getName());
			platformPurchaseOrderItemModel.setSaleGoodsId(merchandise.getMerchandiseId());
			platformPurchaseOrderItemModel.setSaleBarcode(merchandise.getBarcode());
			platformPurchaseOrderItemModel.setSaleNum(itemModel.getNum());
			platformPurchaseOrderItemDao.modify(platformPurchaseOrderItemModel);

			if(!poNoMap.containsKey(platformPurchaseOrderId)){
				poNoMap.put(platformPurchaseOrderId, item.getPoNo());
			}
		}
		for(Long platformPurchaseOrderId : itemMap.keySet()){
			String poNo = poNoMap.get(platformPurchaseOrderId);
			List<SaleOrderItemModel> itemList = itemMap.get(platformPurchaseOrderId);
			if(itemList ==null || itemList.size() < 1){
				throw new RuntimeException("po号："+poNo+"，商品转销数量至少一条不为空");
			}
		}

		for(Long platformPurchaseOrderId : itemMap.keySet()){
			Integer num = numMap.get(platformPurchaseOrderId);
			BigDecimal amount = amountMap.get(platformPurchaseOrderId);
			String poNo = poNoMap.get(platformPurchaseOrderId);
			saleOrderModel.setId(null);
			saleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));// 销售单号;
			saleOrderModel.setPoNo(poNo);
			saleOrderModel.setTotalNum(num);
			saleOrderModel.setTotalAmount(amount);
			Long id = saleOrderDao.save(saleOrderModel);

			SalePoRelModel salePoRelModel = new SalePoRelModel();
			salePoRelModel.setPoNo(poNo);
			salePoRelModel.setState("0"); // 类型:0:销售订单1:销售退订单
			salePoRelModel.setOrderId(id);
			salePoRelModel.setOrderCode(saleOrderModel.getCode());
			salePoRelModel.setMerchantId(user.getMerchantId()); // 公司ID
			salePoRelModel.setMerchantName(user.getMerchantName()); // 公司名称
			salePoRelModel.setCreateDate(TimeUtils.getNow());
			salePoRelDao.save(salePoRelModel);

			List<SaleOrderItemModel> itemList = itemMap.get(platformPurchaseOrderId);

			for (SaleOrderItemModel itemModel : itemList) {
				itemModel.setId(null);
				itemModel.setOrderId(id);
				saleOrderItemDao.save(itemModel);
			}
			PlatformPurchaseOrderModel platformPurchaseOrderModel = platformPurchaseOrderDao.searchById(platformPurchaseOrderId);
			String saleCode =  "";
			if(StringUtils.isNotBlank(platformPurchaseOrderModel.getSaleCode())){
				saleCode =  platformPurchaseOrderModel.getSaleCode()+ "," + saleOrderModel.getCode();
			}else{
				saleCode = saleOrderModel.getCode();
			}
			platformPurchaseOrderModel.setSaleCode(saleCode);
			platformPurchaseOrderModel.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_3);
			platformPurchaseOrderModel.setResaler(user.getId());
			platformPurchaseOrderModel.setResaleName(user.getName());
			platformPurchaseOrderModel.setResaleDate(TimeUtils.getNow());
			platformPurchaseOrderDao.modify(platformPurchaseOrderModel);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "新增", null, "平台采购单转销售单");
		}
	}


}
