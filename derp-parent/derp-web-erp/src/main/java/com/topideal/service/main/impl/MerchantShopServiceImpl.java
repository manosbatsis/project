package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandSuperiorDao;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.entity.vo.main.*;
import com.topideal.service.main.MerchantShopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家店铺业务层
 * @author lian_
 */
@Service
public class MerchantShopServiceImpl implements MerchantShopService {
	@Autowired
	private MerchantShopRelDao dao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家
	@Autowired
	private CustomerInfoDao customerInfoDao;//客户商家关系
	@Autowired
	private DepotInfoDao depotInfoDao;//仓库
	@Autowired
	private MerchantShopShipperDao shipperDao;//店铺货主公司
	@Autowired
	private BrandSuperiorDao brandSuperiorDao;
	@Autowired
	private MerchantBuRelDao merchantBuRelDao;
	@Autowired
	private BuStockLocationTypeConfigDao buStockLocationTypeConfigDao;
	
	/**
	 * 列表查询
	 */
	@Override
	public MerchantShopRelDTO getListByPage(MerchantShopRelDTO dto) throws SQLException {
		return dao.getListByPage(dto);
	}

	/**
	 * 查询商家下拉列表
	 */
	@Override
	public List<MerchantInfoModel> getMerchant() throws SQLException {
		return dao.getSelectMerchant();
	}


	/**
	 * 新增
	 */
	@Override

	public boolean saveShop(MerchantShopRelDTO model) throws Exception {
		
		//判断店铺编码是否已存在
		MerchantShopRelModel shopModel = new MerchantShopRelModel();
		shopModel.setShopCode(model.getShopCode());
		List<MerchantShopRelModel> list = dao.list(shopModel);
		if (list.size()>0) {
			throw new RuntimeException("该店铺编码已存在");
		}
		
		if(StringUtils.isNotBlank(model.getAppKey())) {
			shopModel = new MerchantShopRelModel();
			shopModel.setAppKey(model.getAppKey());
			
			List<MerchantShopRelModel> appKeyList = dao.list(shopModel) ;
			
			if(appKeyList.size() > 0) {
				throw new RuntimeException("该APP KEY已存在");
			}
		}
		/**
		 * 1、当店铺类型为“单主店”时，开店公司为下拉单选，必填项，可选枚举为系统公司主体；
		 * 2、当店铺类型为“外部店”时，开店公司为输入框，非必填，填入信息不做任何判断；
		 */
		MerchantShopRelModel saveShopModel = new MerchantShopRelModel();
		// 根据商家id获取 商家名称 和卓志编码
		MerchantInfoModel merchantInfo = null;
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(model.getStoreTypeCode())){
			merchantInfo = merchantInfoDao.searchById(model.getMerchantId());
			saveShopModel.setBuId(model.getBuId());//单主店才有开店事业部
			saveShopModel.setBuName(model.getBuName());

			MerchantBuRelDTO buRelDTO = new MerchantBuRelDTO();
			buRelDTO.setMerchantId(model.getMerchantId());
			buRelDTO.setBuId(model.getBuId().longValue());
			MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(buRelDTO);

			// 库位类型校验
			// 通过“开店公司+开店事业部”查询公司事业部是否启用了库位管理，若启用则必填，若禁用则非必填
			if(StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0, merchantBuRel.getStockLocationManage())) {
				if(model.getStockLocationTypeId() == null) {
					throw new DerpException("库位类型必填");
				}
			}

			if(model.getStockLocationTypeId() != null) {
				BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = buStockLocationTypeConfigDao.searchById(model.getStockLocationTypeId());
				if(buStockLocationTypeConfigModel == null) {
					throw new DerpException("查询不到该库位类型");
				}
				saveShopModel.setStockLocationTypeId(buStockLocationTypeConfigModel.getId()); // 库位类型ID
				saveShopModel.setStockLocationTypeName(buStockLocationTypeConfigModel.getName());
			}
		}

		// 由于第e仓和跨境宝客户不是必填，订单100是必填
		if(model.getCustomerId()!=null){
			//根据客户id获取客户名称
			CustomerInfoModel customerInfo = customerInfoDao.searchById(model.getCustomerId());
			model.setCustomerName(customerInfo.getName());
		}
		// 更据仓库id 查询仓库 
		DepotInfoModel depotInfo = depotInfoDao.searchById(model.getDepotId());


		saveShopModel.setShopCode(model.getShopCode());
		saveShopModel.setShopName(model.getShopName());
		if(merchantInfo!=null){
			saveShopModel.setMerchantId(merchantInfo.getId());
			saveShopModel.setMerchantName(merchantInfo.getName());
			saveShopModel.setTopidealCode(merchantInfo.getTopidealCode());
		}else{
			saveShopModel.setMerchantName(model.getMerchantName());
		}
		saveShopModel.setStoreTypeCode(model.getStoreTypeCode());// 店铺类型
		saveShopModel.setStoreTypeName(DERP.getLabelByKey(DERP_SYS.merchantShopRel_storeTypeList,model.getStoreTypeCode()));
		saveShopModel.setShopUnifyId(model.getShopUnifyId());//店铺统一ID
		saveShopModel.setCustomerId(model.getCustomerId());
		saveShopModel.setCustomerName(model.getCustomerName());
		saveShopModel.setStatus(model.getStatus());
		saveShopModel.setDepotId(depotInfo.getId());
		saveShopModel.setDepotName(depotInfo.getName());
		saveShopModel.setStorePlatformCode(model.getStorePlatformCode());
		saveShopModel.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, model.getStorePlatformCode()));
		saveShopModel.setShopTypeCode(model.getShopTypeCode());// 运营类型
		saveShopModel.setShopTypeName(DERP.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, model.getShopTypeCode()));
		saveShopModel.setDataSourceCode(model.getDataSourceCode());
		saveShopModel.setDataSourceName(DERP.getLabelByKey(DERP.dataSourceList, model.getDataSourceCode()));
		saveShopModel.setOperator(model.getOperator());
		saveShopModel.setAppKey(model.getAppKey());
		saveShopModel.setAppSecret(model.getAppSecret());
		saveShopModel.setIsSycnMerchandise(model.getIsSycnMerchandise());
		saveShopModel.setSessionKey(model.getSessionKey());
		saveShopModel.setCreateDate(TimeUtils.getNow());
		saveShopModel.setModifyDate(TimeUtils.getNow());
		saveShopModel.setCurrency(model.getCurrency());
		//存储专营母品牌
		saveShopModel.setSuperiorParentBrandId(model.getSuperiorParentBrandId());
		if(model.getSuperiorParentBrandId()!=null){
			BrandSuperiorModel brandSuperiorModel=new BrandSuperiorModel();
			brandSuperiorModel.setId(model.getSuperiorParentBrandId());
			brandSuperiorModel=brandSuperiorDao.searchByModel(brandSuperiorModel);
			if(brandSuperiorModel!=null){
				saveShopModel.setSuperiorParentBrandName(brandSuperiorModel.getName());
				saveShopModel.setSuperiorParentBrandNcCode(brandSuperiorModel.getNcCode());
			}
		}

		Long shopId = dao.save(saveShopModel);
		
		//保存店铺货主公司
		List<Map<String, String>> shipperList = model.getShipperList();
		for(Map<String, String> map:shipperList){
			MerchantBuRelDTO dto = new MerchantBuRelDTO();
			MerchantShopShipperModel shipper = new MerchantShopShipperModel();
			shipper.setShopId(shopId);
			String merchantIdStr = map.get("merchantId");
			if(StringUtils.isNotBlank(merchantIdStr)){
				shipper.setMerchantId(Long.valueOf(map.get("merchantId")));
				shipper.setMerchantName(map.get("merchantName"));
				dto.setMerchantId(Long.valueOf(merchantIdStr));
			}
			String buId = map.get("buId");
			if(StringUtils.isNotBlank(buId)){
				shipper.setBuId(Long.valueOf(buId));
				shipper.setBuName(map.get("buName"));
				dto.setBuId(Long.valueOf(buId));
			}

			// 库位类型校验
			// 当店铺类型为“外部店”时，通过“货主公司+货主事业部”查询公司事业部是否启用了库位管理，若启用则必填，若禁用则非必填
			String stockLocationTypeIdStr = map.get("stockLocationTypeId");
			MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(dto);
			// 外部店
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(model.getStoreTypeCode())) {
				if(merchantBuRel != null && StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0, merchantBuRel.getStockLocationManage())) {
					if(StringUtils.isBlank(stockLocationTypeIdStr)) {
						throw new DerpException("公司:"+ map.get("merchantName") + ",事业部：" + map.get("buName") + "库位类型必填");
					}
				}
			}

			if(StringUtils.isNotBlank(stockLocationTypeIdStr)) {
				BuStockLocationTypeConfigModel shipperBuStockLocationTypeModel = buStockLocationTypeConfigDao.searchById(Long.valueOf(stockLocationTypeIdStr));
				if(shipperBuStockLocationTypeModel == null) {
					throw new DerpException("该库位类型不存在");
				}

				shipper.setStockLocationTypeId(shipperBuStockLocationTypeModel.getId());
				shipper.setStockLocationTypeName(shipperBuStockLocationTypeModel.getName());
			}

			shipper.setCreateDate(TimeUtils.getNow());
			shipper.setModifyDate(TimeUtils.getNow());
			shipperDao.save(shipper);
		}
		return true;
	}

	/**
	 * 修改
	 */
	@Override

	public boolean modifyShop(MerchantShopRelDTO model) throws Exception {
		//判断店铺编码是否已存在
		MerchantShopRelModel shopModel = new MerchantShopRelModel();
		shopModel.setId(model.getId());
		shopModel.setShopCode(model.getShopCode());	//店铺编码

		List<MerchantShopRelModel> list = dao.getcheckShopCode(shopModel);
		if(list!=null&&list.size()>0){
			throw new RuntimeException("该店铺编码已存在");
		}
		
		if(StringUtils.isNotBlank(model.getAppKey())) {
			shopModel = new MerchantShopRelModel();
			shopModel.setAppKey(model.getAppKey());
			
			List<MerchantShopRelModel> appKeyList = dao.list(shopModel) ;
			
			for (MerchantShopRelModel merchantShopRelModel : appKeyList) {
				if(!merchantShopRelModel.getShopCode().equals(model.getShopCode())) {
					throw new RuntimeException("该APP KEY已存在");
				}
			}
		}
		
		/**
		 * 1、当店铺类型为“单主店”时，开店公司为下拉单选，必填项，可选枚举为系统公司主体；
		 * 2、当店铺类型为“外部店”时，开店公司为输入框，非必填，填入信息不做任何判断；
		 */
		MerchantShopRelModel saveShopModel = new MerchantShopRelModel();
		// 根据商家id获取 商家名称 和卓志编码
		MerchantInfoModel merchantInfo =null;
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(model.getStoreTypeCode())){
			merchantInfo = merchantInfoDao.searchById(model.getMerchantId());
			saveShopModel.setBuId(model.getBuId());//单主店才有开店事业部
			saveShopModel.setBuName(model.getBuName());

			MerchantBuRelDTO buRelDTO = new MerchantBuRelDTO();
			buRelDTO.setMerchantId(model.getMerchantId());
			buRelDTO.setBuId(model.getBuId().longValue());
			MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(buRelDTO);

			// 库位类型校验
			// 通过“开店公司+开店事业部”查询公司事业部是否启用了库位管理，若启用则必填，若禁用则非必填
			if(StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0, merchantBuRel.getStockLocationManage())) {
				if(model.getStockLocationTypeId() == null) {
					throw new DerpException("库位类型必填");
				}
			}

			if(model.getStockLocationTypeId() != null) {
				BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = buStockLocationTypeConfigDao.searchById(model.getStockLocationTypeId());
				if(buStockLocationTypeConfigModel == null) {
					throw new DerpException("查询不到该库位类型");
				}
				saveShopModel.setStockLocationTypeId(buStockLocationTypeConfigModel.getId()); // 库位类型ID
				saveShopModel.setStockLocationTypeName(buStockLocationTypeConfigModel.getName());
			}
		}
		// 由于第e仓和跨境宝客户不是必填，订单100是必填
		if(model.getCustomerId()!=null){
			//根据客户id获取客户名称
			CustomerInfoModel customerInfo = customerInfoDao.searchById(model.getCustomerId());
			model.setCustomerName(customerInfo.getName());
		}
		// 更据仓库id 查询仓库 
		DepotInfoModel depotInfo = depotInfoDao.searchById(model.getDepotId());

		saveShopModel.setId(model.getId());
		saveShopModel.setShopCode(model.getShopCode());
		saveShopModel.setShopName(model.getShopName());
		if(merchantInfo!=null){
			saveShopModel.setMerchantId(merchantInfo.getId());
			saveShopModel.setMerchantName(merchantInfo.getName());
			saveShopModel.setTopidealCode(merchantInfo.getTopidealCode());
		}else{
			saveShopModel.setMerchantName(model.getMerchantName());
		}
		saveShopModel.setStoreTypeCode(model.getStoreTypeCode());// 店铺类型
		saveShopModel.setStoreTypeName(DERP.getLabelByKey(DERP_SYS.merchantShopRel_storeTypeList,model.getStoreTypeCode()));
		saveShopModel.setShopUnifyId(model.getShopUnifyId());//店铺统一ID
		saveShopModel.setCustomerId(model.getCustomerId());
		saveShopModel.setCustomerName(model.getCustomerName());
		saveShopModel.setStatus(model.getStatus());
		saveShopModel.setDepotId(depotInfo.getId());
		saveShopModel.setDepotName(depotInfo.getName());
		saveShopModel.setStorePlatformCode(model.getStorePlatformCode());
		saveShopModel.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, model.getStorePlatformCode()));
		saveShopModel.setShopTypeCode(model.getShopTypeCode());
		saveShopModel.setShopTypeName(DERP.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, model.getShopTypeCode()));
		saveShopModel.setDataSourceCode(model.getDataSourceCode());
		saveShopModel.setDataSourceName(DERP.getLabelByKey(DERP.dataSourceList, model.getDataSourceCode()));
		saveShopModel.setOperator(model.getOperator());
		saveShopModel.setModifyDate(TimeUtils.getNow());
		saveShopModel.setAppKey(model.getAppKey());
		saveShopModel.setAppSecret(model.getAppSecret());
		saveShopModel.setIsSycnMerchandise(model.getIsSycnMerchandise());
		saveShopModel.setSessionKey(model.getSessionKey());
		saveShopModel.setCurrency(model.getCurrency());
		//存储专营母品牌
		saveShopModel.setSuperiorParentBrandId(model.getSuperiorParentBrandId());
		if(model.getSuperiorParentBrandId()!=null){
			BrandSuperiorModel brandSuperiorModel=new BrandSuperiorModel();
			brandSuperiorModel.setId(model.getSuperiorParentBrandId());
			brandSuperiorModel=brandSuperiorDao.searchByModel(brandSuperiorModel);
			if(brandSuperiorModel!=null){
				saveShopModel.setSuperiorParentBrandName(brandSuperiorModel.getName());
				saveShopModel.setSuperiorParentBrandNcCode(brandSuperiorModel.getNcCode());
			}
		}
		dao.updateWithNull(saveShopModel);
		dao.modify(saveShopModel);
		
		//清空表体重新保存
		MerchantShopShipperModel shipper = new MerchantShopShipperModel();
		shipper.setShopId(saveShopModel.getId());
		List<MerchantShopShipperModel> oldshipperList = shipperDao.list(shipper);
		if(oldshipperList!=null&&oldshipperList.size()>0){
			List<Long> idsList = new ArrayList<Long>();
			for(MerchantShopShipperModel entity:oldshipperList){
				idsList.add(entity.getId());
			}
			shipperDao.delete(idsList);
		}
		//保存表体货主公司
		List<Map<String, String>> shipperList = model.getShipperList();
		for(Map<String, String> map:shipperList){
			MerchantBuRelDTO dto = new MerchantBuRelDTO();
			MerchantShopShipperModel saveModel = new MerchantShopShipperModel();
			saveModel.setShopId(saveShopModel.getId());
			String merchantIdStr = map.get("merchantId");
			if(StringUtils.isNotBlank(merchantIdStr)){
				saveModel.setMerchantId(Long.valueOf(map.get("merchantId")));
				saveModel.setMerchantName(map.get("merchantName"));
				dto.setMerchantId(Long.valueOf(merchantIdStr));
			}
			String buId  = map.get("buId");
			if(StringUtils.isNotBlank(buId)) {
			   saveModel.setBuId(Long.valueOf(buId));
			   saveModel.setBuName(map.get("buName"));
				dto.setBuId(Long.valueOf(buId));
			}

			// 库位类型校验
			// 当店铺类型为“外部店”时，通过“货主公司+货主事业部”查询公司事业部是否启用了库位管理，若启用则必填，若禁用则非必填
			String stockLocationTypeIdStr = map.get("stockLocationTypeId");
			MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(dto);
			// 外部店
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(model.getStoreTypeCode())) {
				if(merchantBuRel != null && StringUtils.equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0, merchantBuRel.getStockLocationManage())) {
					if(StringUtils.isBlank(stockLocationTypeIdStr)) {
						throw new DerpException("公司:"+ map.get("merchantName") + ",事业部：" + map.get("buName") + "库位类型必填");
					}
				}
			}

			if(StringUtils.isNotBlank(stockLocationTypeIdStr)) {
				BuStockLocationTypeConfigModel shipperBuStockLocationTypeModel = buStockLocationTypeConfigDao.searchById(Long.valueOf(stockLocationTypeIdStr));
				if(shipperBuStockLocationTypeModel == null) {
					throw new DerpException("该库位类型不存在");
				}

				saveModel.setStockLocationTypeId(shipperBuStockLocationTypeModel.getId());
				saveModel.setStockLocationTypeName(shipperBuStockLocationTypeModel.getName());
			}

			saveModel.setCreateDate(TimeUtils.getNow());
			saveModel.setModifyDate(TimeUtils.getNow());
			shipperDao.save(saveModel);
		}
		return true;
	}

	/**
	 * 批量删除
	 */
	@Override

	public boolean delShop(List<Long> list) throws Exception {
		int flag=0;
		for (Long id : list) {
			// 根据id获取信息
			MerchantShopRelModel shopModel = new MerchantShopRelModel();
			shopModel.setId(id);
			shopModel = dao.searchByModel(shopModel);
			// 判断状态是否为 已启用
			if (DERP_SYS.MERCHANTSHOPREL_STATUS_1.equals(shopModel.getStatus()) ) {
				flag = 1;
				break;
			}
		}
		if(flag == 1){
			throw new RuntimeException("操作失败，只能删除禁用状态！");
		}
		int num = dao.delete(list);
	    if(num >= 1){
	        return true;
	    }
	    return false;
	}
	/**
	 * 详情
	 */
	@Override
	public MerchantShopRelDTO searchDetail(Long id) throws SQLException {
		return dao.getDetails(id);
	}

	@Override
	public List<MerchantShopRelModel> getSelectMerchantShopRelBean(MerchantShopRelModel model) throws SQLException {
		return dao.getSelectMerchantShopRel(model);
	}

	@Override
	public List<MerchantShopRelModel> getListByModel(MerchantShopRelModel model) throws SQLException {
		return dao.list(model);
	}

	@Override
	public List<Map<String, Object>> getExportListByDTO(MerchantShopRelDTO dto) {
		List<MerchantShopRelDTO> exportList = dao.getExportList(dto);
		List<Map<String, Object>> exportMapList = new ArrayList<>();
		for (MerchantShopRelDTO merchantShopRelDTO : exportList) {
			Map<String, Object> exportMap = new HashMap<>();
			exportMap.put("storeTypeName", merchantShopRelDTO.getStoreTypeName());
			exportMap.put("shopUnifyId", merchantShopRelDTO.getShopUnifyId());
			exportMap.put("shopCode", merchantShopRelDTO.getShopCode());
			exportMap.put("shopName", merchantShopRelDTO.getShopName());
			exportMap.put("storePlatformName", merchantShopRelDTO.getStorePlatformName());
			exportMap.put("shopTypeName", merchantShopRelDTO.getShopTypeName());
			exportMap.put("merchantName", merchantShopRelDTO.getMerchantName());
			exportMap.put("buName", merchantShopRelDTO.getBuName());
			exportMap.put("customerName", merchantShopRelDTO.getCustomerName());
			exportMap.put("depotName", merchantShopRelDTO.getDepotName());
			exportMap.put("status", merchantShopRelDTO.getStatusLabel());
			exportMapList.add(exportMap);
		}
		return exportMapList;
	}

}
