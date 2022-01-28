package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.SaleTargetDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.CommbarcodeDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.dto.ImportErrorMessage;
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.entity.vo.reporting.SaleTargetModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.report.service.reporting.SaleTargetService;

@Service
public class SaleTargetServiceImpl implements SaleTargetService{

	@Autowired
	private BusinessUnitDao businessUnitDao ;
	@Autowired
	private SaleTargetDao saleTargetDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	
	@Override
	public List<BusinessUnitModel> getAllBuUnit() throws SQLException {
		return businessUnitDao.list(new BusinessUnitModel());
	}

	/**
	 * 导入
	 * @throws SQLException 
	 */
	@Override
	public Map<String, Object> importSaleTarget(List<Map<String, String>> saleTypeData, List<Map<String, String>> platformData, List<Map<String, String>> shopData, User user) throws SQLException {
		
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		
		//校验通过标识
		boolean flag = true ;
		//添加列表
		List<SaleTargetModel> saleTargetList = new ArrayList<SaleTargetModel>() ;
		
		//解析按销售类型计划
		for(int j = 1 ; j <= saleTypeData.size() ; j++) {
			Map<String, String> row = saleTypeData.get(j - 1);
			
			String buCode = row.get("事业部");
			if(checkIsNullOrNot(j, buCode, "按销售类型计划, 事业部不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			buCode = buCode.trim() ;
			
			String barcode = row.get("商品条码");
			if(checkIsNullOrNot(j, barcode, "按销售类型计划, 商品条码不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			barcode = barcode.trim() ;
			
			String month = row.get("销售计划月份");
			if(checkIsNullOrNot(j, barcode, "按销售类型计划, 销售计划月份不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			month = month.trim() ;
			
			String toBNum = row.get("To B销量");
			if(checkIsNullOrNot(j, toBNum, "按销售类型计划,To B销量不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			toBNum = toBNum.trim() ;
			
			String toCNum = row.get("To C销量");
			if(checkIsNullOrNot(j, toCNum, "按销售类型计划,To C销量不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			toCNum = toCNum.trim() ;
			
			BusinessUnitModel buModel = new BusinessUnitModel() ;
			buModel.setCode(buCode);
			
			buModel = businessUnitDao.searchByModel(buModel) ;
			if(buModel == null) {
				setErrorMsg(j, "按销售类型计划, 导入事业部不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), buModel.getId());
			if(!isRelate) {
				setErrorMsg(j, "按销售类型计划,用户无权限操作该事业部", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
			merchandiseInfoModel.setBarcode(barcode);
			
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(merchandiseInfoModel);
			if(merchandiseList.isEmpty()) {
				setErrorMsg(j, "按销售类型计划, 该商品条码没有查到对应商品", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isInteger(toBNum)
					|| !isInteger(toCNum) ) {
				setErrorMsg(j, "按销售类型计划, To B销量或To C销量非数字类型", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isMonth(month)) {
				setErrorMsg(j, "按销售类型计划, 销售计划月份格式有误yyyy-mm", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			//校验事业部+月份+计划类型 相同维度下是否存在数据
			SaleTargetModel queryModel = new SaleTargetModel() ;
			queryModel.setBuId(buModel.getId());
			queryModel.setMonth(month);
			queryModel.setType(DERP_REPORT.SALE_TARGET_TYPE_1);
			
			List<SaleTargetModel> saleTypeList = saleTargetDao.list(queryModel);
			if(saleTypeList.size() > 0) {
				setErrorMsg(j, "按销售类型计划, 该事业部+月份+计划类型 相同维度下已存在数据，请删除后再导入", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			merchandiseInfoModel = merchandiseList.get(0) ;
			String commbarcode = merchandiseInfoModel.getCommbarcode();
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
			commbarcodeModel.setCommbarcode(commbarcode);
			commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
			
			if(commbarcodeModel == null) {
				setErrorMsg(j, "按销售类型计划, 商品条码对应标准条码为空", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			SaleTargetModel saleTargetModel = new SaleTargetModel() ;
			saleTargetModel.setBarcode(barcode);
			saleTargetModel.setBuId(buModel.getId());
			saleTargetModel.setBuName(buModel.getName());
			saleTargetModel.setCommbarcode(commbarcode);
			saleTargetModel.setBrandParent(commbarcodeModel.getCommBrandParentName());
			saleTargetModel.setGoodsName(commbarcodeModel.getCommGoodsName());
			saleTargetModel.setMonth(month);
			saleTargetModel.setToBNum(Integer.valueOf(toBNum));
			saleTargetModel.setToCNum(Integer.valueOf(toCNum));
			saleTargetModel.setTypeName(commbarcodeModel.getCommTypeName());
			saleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_1);
			saleTargetModel.setCreatorId(user.getId());
			saleTargetModel.setCreator(user.getName());
			saleTargetModel.setCreateDate(TimeUtils.getNow());
			
			saleTargetList.add(saleTargetModel) ;
		}
		
		//解析按平台计划
		for(int j = 1 ; j <= platformData.size() ; j++) {
			Map<String, String> row = platformData.get(j - 1);
			
			String buCode = row.get("事业部");
			if(checkIsNullOrNot(j, buCode, "按平台计划, 事业部不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			buCode = buCode.trim() ;
			
			String barcode = row.get("商品条码");
			if(checkIsNullOrNot(j, barcode, "按平台计划, 商品条码不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			barcode = barcode.trim() ;
			
			String month = row.get("销售计划月份");
			if(checkIsNullOrNot(j, barcode, "按平台计划, 销售计划月份不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			month = month.trim() ;
			
			String storePlatformName = row.get("平台名称");
			if(checkIsNullOrNot(j, storePlatformName, "按平台计划,平台名称不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			storePlatformName = storePlatformName.trim() ;
			
			String storePlatformLabel = DERP.getLabelByKey(DERP.storePlatformList, storePlatformName);
			if(StringUtils.isBlank(storePlatformLabel)) {
				setErrorMsg(j, "按平台计划, 导入平台编码不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			String storePlatformNum = row.get("平台计划销量");
			if(checkIsNullOrNot(j, storePlatformNum, "按平台计划,平台计划销量不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			storePlatformNum = storePlatformNum.trim() ;
			
			BusinessUnitModel buModel = new BusinessUnitModel() ;
			buModel.setCode(buCode);
			
			buModel = businessUnitDao.searchByModel(buModel) ;
			if(buModel == null) {
				setErrorMsg(j, "按平台计划, 导入事业部不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), buModel.getId());
			if(!isRelate) {
				setErrorMsg(j, "按平台计划,用户无权限操作该事业部", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
			merchandiseInfoModel.setBarcode(barcode);
			
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(merchandiseInfoModel);
			if(merchandiseList.isEmpty()) {
				setErrorMsg(j, "按平台计划, 该商品条码没有查到对应商品", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isInteger(storePlatformNum)) {
				setErrorMsg(j, "按平台计划, 平台计划销量非数字类型", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isMonth(month)) {
				setErrorMsg(j, "按平台计划, 销售计划月份格式有误yyyy-mm", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			//校验事业部+月份+计划类型 相同维度下是否存在数据
			SaleTargetModel queryModel = new SaleTargetModel() ;
			queryModel.setBuId(buModel.getId());
			queryModel.setMonth(month);
			queryModel.setType(DERP_REPORT.SALE_TARGET_TYPE_2);
			
			List<SaleTargetModel> storePlatformList = saleTargetDao.list(queryModel);
			if(storePlatformList.size() > 0) {
				setErrorMsg(j, "按平台计划, 该事业部+月份+计划类型 相同维度下已存在数据，请删除后再导入", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			merchandiseInfoModel = merchandiseList.get(0) ;
			String commbarcode = merchandiseInfoModel.getCommbarcode();
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
			commbarcodeModel.setCommbarcode(commbarcode);
			commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
			
			if(commbarcodeModel == null) {
				setErrorMsg(j, "按平台计划, 商品条码对应标准条码为空", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			SaleTargetModel saleTargetModel = new SaleTargetModel() ;
			saleTargetModel.setBarcode(barcode);
			saleTargetModel.setBuId(buModel.getId());
			saleTargetModel.setBuName(buModel.getName());
			saleTargetModel.setCommbarcode(commbarcode);
			saleTargetModel.setBrandParent(commbarcodeModel.getCommBrandParentName());
			saleTargetModel.setGoodsName(commbarcodeModel.getCommGoodsName());
			saleTargetModel.setMonth(month);
			saleTargetModel.setTypeName(commbarcodeModel.getCommTypeName());
			saleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_2);
			saleTargetModel.setStorePlatformName(storePlatformName);
			saleTargetModel.setStorePlatformNum(Integer.valueOf(storePlatformNum));
			saleTargetModel.setCreatorId(user.getId());
			saleTargetModel.setCreator(user.getName());
			saleTargetModel.setCreateDate(TimeUtils.getNow());
			
			saleTargetList.add(saleTargetModel) ;
		}
		
		//解析按店铺计划
		for(int j = 1 ; j <= shopData.size() ; j++) {
			Map<String, String> row = shopData.get(j - 1);
			
			String buCode = row.get("事业部");
			if(checkIsNullOrNot(j, buCode, "按店铺计划, 事业部不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			buCode = buCode.trim() ;
			
			String barcode = row.get("商品条码");
			if(checkIsNullOrNot(j, barcode, "按店铺计划, 商品条码不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			barcode = barcode.trim() ;
			
			String month = row.get("销售计划月份");
			if(checkIsNullOrNot(j, barcode, "按店铺计划, 销售计划月份不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			month = month.trim() ;
			
			String shopUnityId = row.get("店铺名称");
			if(checkIsNullOrNot(j, shopUnityId, "按店铺计划,店铺名称不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			shopUnityId = shopUnityId.trim() ;
			
			String shopNum = row.get("店铺计划销量");
			if(checkIsNullOrNot(j, shopNum, "按店铺计划,店铺计划销量不能为空", resultList)) {
				failure += 1;
				flag &= false ;
				continue;
			}
			shopNum = shopNum.trim() ;
			
			BusinessUnitModel buModel = new BusinessUnitModel() ;
			buModel.setCode(buCode);
			
			buModel = businessUnitDao.searchByModel(buModel) ;
			if(buModel == null) {
				setErrorMsg(j, "按店铺计划, 导入事业部不存在", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), buModel.getId());
			if(!isRelate) {
				setErrorMsg(j, "按店铺计划,用户无权限操作该事业部", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
			merchandiseInfoModel.setBarcode(barcode);
			
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(merchandiseInfoModel);
			if(merchandiseList.isEmpty()) {
				setErrorMsg(j, "按店铺计划, 该商品条码没有查到对应商品", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isInteger(shopNum)) {
				setErrorMsg(j, "按店铺计划, 店铺计划销量非数字类型", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			if(!isMonth(month)) {
				setErrorMsg(j, "店铺计划销量, 销售计划月份格式有误yyyy-mm", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			//校验事业部+月份+计划类型 相同维度下是否存在数据
			SaleTargetModel queryModel = new SaleTargetModel() ;
			queryModel.setBuId(buModel.getId());
			queryModel.setMonth(month);
			queryModel.setType(DERP_REPORT.SALE_TARGET_TYPE_3);
			
			List<SaleTargetModel> storePlatformList = saleTargetDao.list(queryModel);
			if(storePlatformList.size() > 0) {
				setErrorMsg(j, "按店铺计划, 该事业部+月份+计划类型 相同维度下已存在数据，请删除后再导入", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			merchandiseInfoModel = merchandiseList.get(0) ;
			String commbarcode = merchandiseInfoModel.getCommbarcode();
			CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
			commbarcodeModel.setCommbarcode(commbarcode);
			commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
			
			if(commbarcodeModel == null) {
				setErrorMsg(j, "按店铺计划, 商品条码对应标准条码为空", resultList);
				failure += 1;
				flag &= false ;
				continue;
			}
			
			String shopName = "" ;
			String shopCode = "" ;
			if(!"social".equals(shopUnityId)) {
				
				Map<String, Object> queryMap = new HashMap<String, Object>() ;
				queryMap.put("shopUnifyId", shopUnityId) ;
				
				List<MerchantShopRelMongo> shopList = merchantShopRelMongoDao.findAll(queryMap);
				
				if(shopList.isEmpty()) {
					setErrorMsg(j, "按店铺计划, 根据店铺唯一ID查询店铺信息不存在", resultList);
					failure += 1;
					flag &= false ;
					continue;
				}
				
				shopName = shopList.get(0).getShopName() ;
				for (MerchantShopRelMongo tempModel : shopList) {
					
					if(StringUtils.isNotBlank(shopCode)) {
						shopCode += "," ;
					}
					
					shopCode += tempModel.getShopCode() ;
				}
				
			}else {
				shopName = "其他" ;
				shopCode = "social" ;
			}
			
			
			
			SaleTargetModel saleTargetModel = new SaleTargetModel() ;
			saleTargetModel.setBarcode(barcode);
			saleTargetModel.setBuId(buModel.getId());
			saleTargetModel.setBuName(buModel.getName());
			saleTargetModel.setCommbarcode(commbarcode);
			saleTargetModel.setBrandParent(commbarcodeModel.getCommBrandParentName());
			saleTargetModel.setGoodsName(commbarcodeModel.getCommGoodsName());
			saleTargetModel.setMonth(month);
			saleTargetModel.setTypeName(commbarcodeModel.getCommTypeName());
			saleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_3);
			saleTargetModel.setShopCode(shopCode);
			saleTargetModel.setShopName(shopName);
			saleTargetModel.setShopNum(Integer.valueOf(shopNum));
			saleTargetModel.setCreatorId(user.getId());
			saleTargetModel.setCreator(user.getName());
			saleTargetModel.setCreateDate(TimeUtils.getNow());
			
			saleTargetList.add(saleTargetModel) ;
		}
		
		if(flag) {
			
			int total = saleTargetList.size();
			int pageLimit = 5000 ;
			
			int pageNum = total / pageLimit ;
			
			if(total % pageLimit != 0) {
				pageNum += 1 ;
			}
			
			for(int page = 1 ; page <= pageNum ; page ++) {

				int start = (page - 1) * pageLimit ;
				int end = page * pageLimit ;

				if(end > total) {
					end = total ;
				}

				List<SaleTargetModel> subList = saleTargetList.subList(start, end);
				saleTargetDao.batchSave(subList) ;
			}

			success = total ;
			
		}else {
			pass = saleTargetList.size() ;
		}
		
		Map<String, Object> map = new HashMap<String, Object>() ;
		map.put("success", success);
		map.put("failure", failure);
		map.put("pass", pass);
		map.put("message", resultList);
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SaleTargetDTO listSaleTarget(SaleTargetDTO dto,User user) {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			
			dto.setBuIds(buIds);
		}
		
		List<SaleTargetDTO> list = saleTargetDao.getSaleTargetList(dto) ;
		Integer total = saleTargetDao.getCountSaleTargetList(dto) ;
		
		dto.setList(list);
		dto.setTotal(total);
		
		return dto;
	}

	/**
	 * 错误时，设置错误内容
	 * 
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index, String msg, List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
	
	/**
	 * 判断输入字段是否为空
	 * 
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportErrorMessage> resultList) {

		if (StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;

		} else {
			return false;
		}

	}
	
	/**
	 * 数字判断
	 * @param str
	 * @return
	 */
	private boolean isInteger(String str) { 
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$"); 
        return pattern.matcher(str).matches(); 
	}
	
	private boolean isMonth(String str) {
		Pattern pattern = Pattern.compile("^[\\d]{4}-[\\d]{2}$"); 
		return pattern.matcher(str).matches();
	}

	@Override
	public List<SaleTargetDTO> exportSaleTarget(String[] paramsArr,User user) {
		
		List<SaleTargetDTO> exportList = new ArrayList<SaleTargetDTO>() ;
		
		if(paramsArr.length == 0) {
			
			SaleTargetDTO dto = new SaleTargetDTO() ;
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				return exportList ;
			}
			
			dto.setBuIds(buIds);
			
			exportList = saleTargetDao.getExpotList(dto) ;
		}else {
			for (String params : paramsArr) {
				/**
				 * 前端合成参数
				 * var str = row.buId + "_" + row.month + "_" + row.type ;
				 */
				String[] arr = params.split("_");
				String buId = arr[0] ;
				String month = arr[1] ;
				String type = arr[2] ;
				
				SaleTargetDTO queryDto = new SaleTargetDTO() ;
				queryDto.setBuId(Long.valueOf(buId));
				queryDto.setMonth(month);
				queryDto.setType(type);
				
				List<SaleTargetDTO> tempList = saleTargetDao.getExpotList(queryDto) ;
				
				exportList.addAll(tempList) ;
			}
		}
		
		return exportList;
	}

	@Override
	public Integer delSaleTarget(String[] paramsArr) {
		
		Integer rows = 0 ;
		for (String params : paramsArr) {
			/**
			 * 前端合成参数
			 * var str = row.buId + "_" + row.month + "_" + row.type ;
			 */
			String[] arr = params.split("_");
			String buId = arr[0] ;
			String month = arr[1] ;
			String type = arr[2] ;
			
			SaleTargetDTO queryDto = new SaleTargetDTO() ;
			queryDto.setBuId(Long.valueOf(buId));
			queryDto.setMonth(month);
			queryDto.setType(type);
			
			rows += saleTargetDao.delSaleTarget(queryDto) ;
			
		}
		
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSaleTargetDetails(SaleTargetDTO dto) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>() ;
		
		List<SaleTargetDTO> listDto = saleTargetDao.listDto(dto) ;
		
		if(listDto.isEmpty()) {
			return returnMap ;
		}
		
		/**
		 * 根据类型查询返回对应数据
		 */
		if(DERP_REPORT.SALE_TARGET_TYPE_1.equals(dto.getType())) {
			returnMap.put("list", listDto) ;
		}else if(DERP_REPORT.SALE_TARGET_TYPE_2.equals(dto.getType())) {
			
			//平台编码集合
			Set<String> platformSet = new HashSet<String>() ;
			
			Map<String, Map<String, Object>> goodsMap = new HashMap<String, Map<String, Object>>() ;
			
			for (SaleTargetDTO saleTargetDTO : listDto) {
				String barcode = saleTargetDTO.getBarcode();
				
				Map<String, Object> tempMap = goodsMap.get(barcode);
				
				if(tempMap == null) {
					tempMap = new HashMap<String, Object>() ;
					
					tempMap.put("barcode", saleTargetDTO.getBarcode()) ;
					tempMap.put("goodsName", saleTargetDTO.getGoodsName()) ;
					tempMap.put("brandParent", saleTargetDTO.getBrandParent()) ;
					tempMap.put("platformCount", new HashMap<>()) ;
				}
				
				Map<String, Object> platformCountMap =(HashMap<String, Object>) tempMap.get("platformCount") ;
				
				String storePlatformNameLabel = saleTargetDTO.getStorePlatformNameLabel();
				
				platformSet.add(storePlatformNameLabel) ;
				
				Integer tempNum = (Integer)platformCountMap.get(storePlatformNameLabel);
				if(tempNum == null) {
					tempNum = 0 ;
				}
				
				tempNum += saleTargetDTO.getStorePlatformNum() ;
				
				platformCountMap.put(storePlatformNameLabel, tempNum) ;
				tempMap.put("platformCount", platformCountMap) ;
				goodsMap.put(barcode, tempMap) ;
			}
			
			for(Map<String, Object> goods : goodsMap.values() ) {
				Map<String, Object> platformCountMap = (Map<String, Object>)goods.get("platformCount") ;
				
				Map<String, Object> newPlatformCountMap = new LinkedHashMap<String, Object>() ;
				
				for (String platformLabel : platformSet) {
					
					Integer tempNum = (Integer)platformCountMap.get(platformLabel) ;
					
					if(tempNum == null) {
						tempNum = 0 ;
					}
					
					newPlatformCountMap.put(platformLabel, tempNum) ;
				}
				
				goods.put("platformCount", newPlatformCountMap) ;
			}
			
			List<Map<String, Object>> tempList = new ArrayList<>(goodsMap.values()) ;
			
			returnMap.put("platFormlist", platformSet) ;
			returnMap.put("list", tempList) ;
			
		}else if(DERP_REPORT.SALE_TARGET_TYPE_3.equals(dto.getType())) {
			//店铺编码集合
			Set<String> shopSet = new LinkedHashSet<String>() ;
			
			Map<String, Map<String, Object>> goodsMap = new HashMap<String, Map<String, Object>>() ;
			
			for (SaleTargetDTO saleTargetDTO : listDto) {
				String barcode = saleTargetDTO.getBarcode();
				
				Map<String, Object> tempMap = goodsMap.get(barcode);
				
				if(tempMap == null) {
					tempMap = new HashMap<String, Object>() ;
					
					tempMap.put("barcode", saleTargetDTO.getBarcode()) ;
					tempMap.put("goodsName", saleTargetDTO.getGoodsName()) ;
					tempMap.put("brandParent", saleTargetDTO.getBrandParent()) ;
					tempMap.put("shopCount", new HashMap<>()) ;
				}
				
				Map<String, Object> shopCountMap =(HashMap<String, Object>) tempMap.get("shopCount") ;
				
				String shopName = saleTargetDTO.getShopName();
				
				//其他数量排到最后
				if(!"其他".equals(shopName)) {
					shopSet.add(shopName) ;
				}
				
				Integer tempNum = (Integer)shopCountMap.get(shopName);
				if(tempNum == null) {
					tempNum = 0 ;
				}
				
				tempNum += saleTargetDTO.getShopNum() ;
				
				shopCountMap.put(shopName, tempNum) ;
				tempMap.put("shopCount", shopCountMap) ;
				goodsMap.put(barcode, tempMap) ;
			}
			shopSet.add("其他") ;
			
			for(Map<String, Object> goods : goodsMap.values() ) {
				Map<String, Object> shopCountMap = (Map<String, Object>)goods.get("shopCount") ;
				
				Map<String, Object> newShopCountMap = new LinkedHashMap<String, Object>() ;
				
				for (String shopName : shopSet) {
					
					Integer tempNum = (Integer)shopCountMap.get(shopName) ;
					
					if(tempNum == null) {
						tempNum = 0 ;
					}
					
					newShopCountMap.put(shopName, tempNum) ;
				}
				
				goods.put("shopCount", newShopCountMap) ;
			}
			
			List<Map<String, Object>> tempList = new ArrayList<>(goodsMap.values()) ;
			
			returnMap.put("shopNamelist", shopSet) ;
			returnMap.put("list", tempList) ;
		}
		
		SaleTargetDTO saleTargetDTO = listDto.get(0);
		returnMap.put("buName", saleTargetDTO.getBuName()) ;
		returnMap.put("typeLabel", saleTargetDTO.getTypeLabel()) ;
		returnMap.put("type", saleTargetDTO.getType()) ;
		returnMap.put("month", saleTargetDTO.getMonth()) ;
		
		return returnMap;
	}

}
