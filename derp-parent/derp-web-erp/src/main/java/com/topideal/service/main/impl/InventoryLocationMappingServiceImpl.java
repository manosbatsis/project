//package com.topideal.service.main.impl;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.bean.SelectBean;
//import com.topideal.common.tools.TimeUtils;
//import com.topideal.dao.main.InventoryLocationMappingDao;
//import com.topideal.dao.main.MerchandiseInfoDao;
//import com.topideal.dao.main.MerchantInfoDao;
//import com.topideal.entity.dto.main.ImportErrorMessage;
//import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
//import com.topideal.entity.vo.main.InventoryLocationMappingModel;
//import com.topideal.entity.vo.main.MerchandiseInfoModel;
//import com.topideal.entity.vo.main.MerchantInfoModel;
//import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;
//import com.topideal.mongo.dao.MerchantInfoMongoDao;
//import com.topideal.mongo.entity.InventoryLocationMappingMongo;
//import com.topideal.mongo.entity.MerchantInfoMongo;
//import com.topideal.service.main.InventoryLocationMappingService;
//
//@Service
//public class InventoryLocationMappingServiceImpl implements InventoryLocationMappingService{
//
//	@Autowired
//	private MerchantInfoDao merchantInfoDao ;
//	@Autowired
//    private MerchantInfoMongoDao merchantInfoMongoDao;
//	@Autowired
//	private InventoryLocationMappingDao inventoryLocationMappingDao ;
//	@Autowired
//	private MerchandiseInfoDao merchandiseInfoDao ;
//	@Autowired
//	private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao ;
//
//	@Override
//	public List<SelectBean> getMerchantList(User user) {
//		List<SelectBean> result = new ArrayList<SelectBean>();
//        Map<String, Object> param = new HashMap<>();
//        if (DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//            List<MerchantInfoMongo> merchantInfoMongos = merchantInfoMongoDao.findAll(param);
//            for (MerchantInfoMongo merchantInfoMongo : merchantInfoMongos) {
//                SelectBean bean = new SelectBean();
//                bean.setValue(merchantInfoMongo.getMerchantId().toString());
//                bean.setLabel(merchantInfoMongo.getName());
//                result.add(bean);
//            }
//        } else {
//            param.put("merchantId", user.getMerchantId());
//            MerchantInfoMongo mongo = merchantInfoMongoDao.findOne(param);
//            SelectBean bean = new SelectBean();
//            bean.setValue(mongo.getMerchantId().toString());
//            bean.setLabel(mongo.getName());
//            result.add(bean);
//        }
//        return result;
//	}
//
//	@Override
//	public InventoryLocationMappingModel seachById(Long id) throws SQLException {
//
//		InventoryLocationMappingModel model = inventoryLocationMappingDao.searchById(id);
//
//		return model;
//	}
//
//	@Override
//	public InventoryLocationMappingDTO listInventoryLocationMapping(InventoryLocationMappingDTO dto) {
//
//		dto = inventoryLocationMappingDao.getListByPage(dto) ;
//
//		return dto;
//	}
//
//	@Override
//	public int saveOrModify(InventoryLocationMappingModel model,User user) throws SQLException {
//
//
//		MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
//		queryModel.setMerchantId(model.getMerchantId());
//		queryModel.setGoodsNo(model.getOriginalGoodsNo());
//
//		queryModel = merchandiseInfoDao.searchByModel(queryModel) ;
//
//		if(queryModel == null) {
//			throw new RuntimeException("根据原货号查询商品不存在") ;
//		}
//		model.setOriginalGoodsId(queryModel.getId());
//
//		queryModel = new MerchandiseInfoModel() ;
//		queryModel.setMerchantId(model.getMerchantId());
//		queryModel.setGoodsNo(model.getGoodsNo());
//
//		queryModel = merchandiseInfoDao.searchByModel(queryModel) ;
//
//		if(queryModel == null) {
//			throw new RuntimeException("根据库位货号查询商品不存在") ;
//		}
//
//		MerchantInfoModel merchant = merchantInfoDao.searchById(model.getMerchantId());
//
//		InventoryLocationMappingModel mappingQueryModel = new InventoryLocationMappingModel() ;
//
//		if(model.getId() == null) {
//
//			mappingQueryModel.setMerchantId(model.getMerchantId());
//			mappingQueryModel.setGoodsNo(model.getGoodsNo());
//			mappingQueryModel.setOriginalGoodsNo(model.getOriginalGoodsNo());
//			mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//
//			if(mappingQueryModel != null) {
//				throw new RuntimeException("商家+原货号+库位货号不能存在多条记录") ;
//			}
//
//			if(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_2.equals(model.getType())
//					|| DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_3.equals(model.getType())) {
//				mappingQueryModel = new InventoryLocationMappingModel() ;
//				mappingQueryModel.setMerchantId(model.getMerchantId());
//				mappingQueryModel.setType(model.getType());
//				mappingQueryModel.setOriginalGoodsNo(model.getOriginalGoodsNo());
//				mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//
//				if(mappingQueryModel != null) {
//					throw new RuntimeException("商家+原货号+库位标识（赠品、sample）不可存在对应多个库位货号") ;
//				}
//			}
//
//			model.setMerchantName(merchant.getName());
//			model.setGoodsId(queryModel.getId());
//			model.setGoodsName(queryModel.getName());
//			model.setCreator(user.getId());
//			model.setCreateName(user.getName());
//			model.setCreateDate(TimeUtils.getNow());
//			inventoryLocationMappingDao.save(model) ;
//
//			return 1 ;
//		}else {
//
//			mappingQueryModel.setMerchantId(model.getMerchantId());
//			mappingQueryModel.setGoodsNo(model.getGoodsNo());
//			mappingQueryModel.setOriginalGoodsNo(model.getOriginalGoodsNo());
//			mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//			if (mappingQueryModel!=null
//				&&mappingQueryModel.getGoodsNo().equals(model.getGoodsNo())
//				&&mappingQueryModel.getOriginalGoodsNo().equals(model.getOriginalGoodsNo())) {// 说明只是对对类型进行调整
//
//			}else {// 报错
//				if(mappingQueryModel != null && mappingQueryModel.getId() != model.getId()) {
//					throw new RuntimeException("商家+原货号+库位货号不能存在多条记录") ;
//				}
//			}
//
//
//
//			if(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_2.equals(model.getType())
//					|| DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_3.equals(model.getType())) {
//				mappingQueryModel = new InventoryLocationMappingModel() ;
//				mappingQueryModel.setMerchantId(model.getMerchantId());
//				mappingQueryModel.setType(model.getType());
//				mappingQueryModel.setOriginalGoodsNo(model.getOriginalGoodsNo());
//				mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//
//				if(mappingQueryModel != null && mappingQueryModel.getId() != model.getId()) {
//					throw new RuntimeException("商家+原货号+库位标识（赠品、sample）不可存在对应多个库位货号") ;
//				}
//			}
//
//			model.setMerchantName(merchant.getName());
//			model.setGoodsId(queryModel.getId());
//			model.setGoodsName(queryModel.getName());
//			model.setModifier(user.getId());
//			model.setModifyName(user.getName());
//			model.setModifyDate(TimeUtils.getNow());
//
//			return inventoryLocationMappingDao.modify(model) ;
//		}
//
//	}
//
//	@Override
//	public boolean deleteInventoryLocationMapping(List<Long> list) throws SQLException {
//
//		int rows = inventoryLocationMappingDao.delete(list);
//
//		if(rows > 0) {
//			return true ;
//		}
//
//		return false;
//	}
//
//	@Override
//	public List<InventoryLocationMappingDTO> exportInventoryLocationMapping(InventoryLocationMappingDTO dto) {
//		return inventoryLocationMappingDao.listDTO(dto) ;
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public Map importInventoryLocationMapping(List<Map<String, String>> data, User user) throws SQLException {
//
//		List<InventoryLocationMappingModel> list = new ArrayList<InventoryLocationMappingModel>() ;
//
//		Set<String> merchantGoodsNoOriGoodsSet = new HashSet<String>() ;
//		Set<String> merchantOriGoodsTypeSet = new HashSet<String>() ;
//
//		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
//		int success = 0;
//		int pass = 0;
//		int failure = 0;
//
//		boolean flag = true ;
//
//		for(int j = 1; j <= data.size(); j++) {
//
//			Map<String, String> rows = data.get(j - 1) ;
//			String code = rows.get("公司编码");
//
//			if(checkIsNullOrNot(j, code, "公司编码不能为空", resultList)) {
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//			code = code.trim() ;
//
//			MerchantInfoModel queryModel = new MerchantInfoModel() ;
//			queryModel.setCode(code);
//
//			queryModel = merchantInfoDao.searchByModel(queryModel) ;
//
//			if(queryModel == null) {
//				setErrorMsg(j, "公司不存在", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//
//			String originalGoodsNo = rows.get("原货号");
//			if(checkIsNullOrNot(j, originalGoodsNo, "原货号不能为空", resultList)) {
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//			originalGoodsNo = originalGoodsNo.trim() ;
//
//			MerchandiseInfoModel originalGoods = new MerchandiseInfoModel() ;
//			originalGoods.setGoodsNo(originalGoodsNo);
//			originalGoods.setMerchantId(queryModel.getId());
//
//			originalGoods = merchandiseInfoDao.searchByModel(originalGoods) ;
//			if(originalGoods == null) {
//				setErrorMsg(j, "原货号查询商品不存在", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//
//			String goodsNo = rows.get("库位货号");
//			if(checkIsNullOrNot(j, goodsNo, "库位货号不能为空", resultList)) {
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//			goodsNo = goodsNo.trim() ;
//
//			MerchandiseInfoModel goods = new MerchandiseInfoModel() ;
//			goods.setGoodsNo(goodsNo);
//			goods.setMerchantId(queryModel.getId());
//
//			goods = merchandiseInfoDao.searchByModel(goods) ;
//			if(goods == null) {
//				setErrorMsg(j, "库位货号查询商品不存在", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//
//			String type = rows.get("库位类型");
//			if(checkIsNullOrNot(j, goodsNo, "库位类型不能为空", resultList)) {
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//			type = type.trim() ;
//
//
//			if(StringUtils.isBlank(DERP_SYS.getLabelByKey(DERP_SYS.invenlocaitonMapping_TypeList, type))) {
//				setErrorMsg(j, "库位类型不存在", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//
//			InventoryLocationMappingModel mappingQueryModel = new InventoryLocationMappingModel() ;
//			mappingQueryModel.setMerchantId(queryModel.getId());
//			mappingQueryModel.setGoodsNo(goodsNo);
//			mappingQueryModel.setOriginalGoodsNo(originalGoodsNo);
//
//			mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//
//			if(mappingQueryModel != null) {
//				setErrorMsg(j, "商家+原货号+库位货号不能存在多条记录", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//
//			//判断excel中商家+原货号+库位货号是否存在相同记录
//			String merchantOriGoodsNoGoodsNo = code + "__" + originalGoodsNo + "__" + goodsNo ;
//			boolean contains = merchantGoodsNoOriGoodsSet.contains(merchantOriGoodsNoGoodsNo);
//			if(contains) {
//				setErrorMsg(j, "excel中商家+原货号+库位货号不能存在多条记录", resultList);
//				flag &= false ;
//				failure += 1;
//				continue;
//			}
//			merchantGoodsNoOriGoodsSet.add(merchantOriGoodsNoGoodsNo) ;
//
//
//			if(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_2.equals(type)
//					|| DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_3.equals(type)) {
//				mappingQueryModel = new InventoryLocationMappingModel() ;
//
//				mappingQueryModel.setMerchantId(queryModel.getId());
//				mappingQueryModel.setType(type);
//				mappingQueryModel.setOriginalGoodsNo(originalGoodsNo);
//				mappingQueryModel = inventoryLocationMappingDao.searchByModel(mappingQueryModel) ;
//
//				if(mappingQueryModel != null) {
//					setErrorMsg(j, "商家+原货号+库位标识（赠品、sample）不可存在对应多个库位货号", resultList);
//					flag &= false ;
//					failure += 1;
//					continue;
//				}
//
//				//判断excel中商家+原货号+库位标识（赠品、sample）不可存在对应多个库位货号
//				String merchantOriGoodsType = code + "__" + originalGoodsNo + "__" + type ;
//				boolean typeContains = merchantOriGoodsTypeSet.contains(merchantOriGoodsType);
//				if(typeContains) {
//					setErrorMsg(j, "excel中商家+原货号+库位标识（赠品、sample）不可存在对应多个库位货号", resultList);
//					flag &= false ;
//					failure += 1;
//					continue;
//				}
//				merchantOriGoodsTypeSet.add(merchantOriGoodsType) ;
//
//			}
//
//			InventoryLocationMappingModel saveModel = new InventoryLocationMappingModel() ;
//			saveModel.setMerchantId(queryModel.getId());
//			saveModel.setMerchantName(queryModel.getName());
//			saveModel.setGoodsId(goods.getId());
//			saveModel.setGoodsName(goods.getName());
//			saveModel.setGoodsNo(goodsNo);
//			saveModel.setOriginalGoodsId(originalGoods.getId());
//			saveModel.setOriginalGoodsNo(originalGoodsNo);
//			saveModel.setType(type);
//			saveModel.setCreateDate(TimeUtils.getNow());
//			saveModel.setCreateName(user.getName());
//			saveModel.setCreator(user.getId());
//
//			list.add(saveModel) ;
//		}
//
//		if(flag) {
//			for (InventoryLocationMappingModel model : list) {
//				inventoryLocationMappingDao.save(model) ;
//			}
//
//			success = list.size() ;
//		}
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("success", success);
//		map.put("pass", pass);
//		map.put("failure", failure);
//		map.put("message", resultList);
//		return map;
//	}
//
//	/**
//	 * 判断输入字段是否为空
//	 * @param index
//	 * @param content
//	 * @param msg
//	 * @param resultList
//	 * @return
//	 */
//	private boolean checkIsNullOrNot(int index , String content ,
//			String msg ,List<ImportErrorMessage> resultList ) {
//
//		if(StringUtils.isBlank(content)) {
//			ImportErrorMessage message = new ImportErrorMessage();
//			message.setRows(index + 1);
//			message.setMessage(msg);
//			resultList.add(message);
//
//			return true ;
//
//		}else {
//			return false ;
//		}
//
//	}
//
//	/**
//	 * 错误时，设置错误内容
//	 * @param index
//	 * @param msg
//	 * @param resultList
//	 */
//	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
//		ImportErrorMessage message = new ImportErrorMessage();
//		message.setRows(index + 1);
//		message.setMessage(msg);
//		resultList.add(message);
//	}
//
//	@Override
//	public InventoryLocationMappingDTO getOriginalGoodsId(User user,InventoryLocationMappingDTO dto) throws Exception {
//
//		InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(user.getTopidealCode(), dto.getGoodsNo());
//
//		if(oriGoodsNoMappingModel != null) {
//			dto.setOriginalGoodsId(oriGoodsNoMappingModel.getOriginalGoodsId());
//			dto.setOriginalGoodsNo(oriGoodsNoMappingModel.getOriginalGoodsNo());
//		}
//
//		return dto;
//	}
//
//	/**
//	 * 修改 原货号指定出库货号
//	 */
//	@Override
//	public Map<String, Object> updateNotSettlement(Long id) throws Exception {
//	    Map<String,Object> retrunMap=new HashMap<String,Object>();
//	    retrunMap.put("status", "00");
//	    retrunMap.put("errorMessage", "修改成功");
//	    if (id==null) {
//	    	retrunMap.put("status", "01");
//		    retrunMap.put("errorMessage", "id不能为空");
//		    return retrunMap;
//		}
//	     InventoryLocationMappingModel inventoryLocationgModel = inventoryLocationMappingDao.searchById(id);
//	     if (inventoryLocationgModel==null||inventoryLocationgModel.getOriginalGoodsId()==null) {
//	    	 retrunMap.put("status", "01");
//			 retrunMap.put("errorMessage", "没有对应的库存商品/库存商品对应的原货号id为空");
//			 return retrunMap;
//		 }
//	     // 指定的出库货号只能是一条
//		InventoryLocationMappingModel inventoryLocationQuery= new InventoryLocationMappingModel();
//		inventoryLocationQuery.setOriginalGoodsId(inventoryLocationgModel.getOriginalGoodsId());
//		inventoryLocationQuery.setIsorRappoint("1");
//		inventoryLocationQuery.setMerchantId(inventoryLocationgModel.getMerchantId());
//		inventoryLocationQuery = inventoryLocationMappingDao.searchByModel(inventoryLocationQuery);
//		// 把原来的已指定 指定修改成未指定
//		if (inventoryLocationQuery!=null) {
//			InventoryLocationMappingModel updateIsorRappoint0= new InventoryLocationMappingModel();
//			updateIsorRappoint0.setId(inventoryLocationQuery.getId());
//			updateIsorRappoint0.setIsorRappoint("0");
//			updateIsorRappoint0.setModifyDate(TimeUtils.getNow());
//			inventoryLocationMappingDao.modify(updateIsorRappoint0);
//		}
//		// 修改为已指定
//		InventoryLocationMappingModel updateIsorRappoint0= new InventoryLocationMappingModel();
//		updateIsorRappoint0.setId(id);
//		updateIsorRappoint0.setIsorRappoint("1");
//		updateIsorRappoint0.setModifyDate(TimeUtils.getNow());
//		inventoryLocationMappingDao.modify(updateIsorRappoint0);
//
//		return retrunMap;
//	}
//
//}
