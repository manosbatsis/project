package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdPurchaseConfigDao;
import com.topideal.dao.common.SdPurchaseConfigItemDao;
import com.topideal.dao.common.SdTypeConfigDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.SdPurchaseConfigService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SdPurchaseConfigServiceImpl implements SdPurchaseConfigService {

	@Autowired
	private SdPurchaseConfigDao sdPurchaseConfigDao;
	@Autowired
	private SdPurchaseConfigItemDao sdPurchaseConfigItemDao;
	@Autowired
	private SdTypeConfigDao sdTypeConfigDao;
	@Autowired
	private BrandParentMongoDao brandParentMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao ;

	@Override
	public SdPurchaseConfigDTO getSdPurchaseConfigListByPage(SdPurchaseConfigDTO dto) {
		return sdPurchaseConfigDao.getListByPage(dto);
	}

	@Override
	public void saveOrModify(SdPurchaseConfigModel model, String itemList,User user) throws SQLException {

		Timestamp effectiveTime = model.getEffectiveTime();
		Timestamp invalidTime = model.getInvalidTime();
		
		if(effectiveTime.getTime() - invalidTime.getTime() > 0) {
			throw new DerpException("保存失败，失效时间不能小于生效时间");
		}

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("merchantId", model.getMerchantId());

		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMap);

		model.setMerchantName(merchant.getName());

		queryMap.clear();
		queryMap.put("merchantId", model.getMerchantId());
		queryMap.put("buId", model.getBuId());
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);
		if (merchantBuRelMongo != null) {
			model.setBuName(merchantBuRelMongo.getBuName());
		}

		queryMap.clear();
		queryMap.put("customerid", model.getSupplierId());
		queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);
		CustomerInfoMogo supplier = customerInfoMongoDao.findOne(queryMap);
		if (supplier != null) {
			model.setSupplierName(supplier.getName());
		}

		if (model.getId() == null) {
			model.setCreateDate(TimeUtils.getNow());
			model.setCreator(user.getName());
			model.setCreatorId(user.getId());

			if (model.getStatus() != null) {
				model.setExamineDate(TimeUtils.getNow());
				model.setExaminer(user.getName());
				model.setExaminerId(user.getId());
			} else {
				model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_0);
			}

			sdPurchaseConfigDao.save(model);
		} else {

			SdPurchaseConfigItemModel queryModel = new SdPurchaseConfigItemModel();
			queryModel.setConfigId(model.getId());

			List<SdPurchaseConfigItemModel> delItemList = sdPurchaseConfigItemDao.list(queryModel);
			List<Long> ids = delItemList.stream().map(SdPurchaseConfigItemModel::getId).collect(Collectors.toList());

			sdPurchaseConfigItemDao.delete(ids);

			if (model.getStatus() != null) {
				model.setExamineDate(TimeUtils.getNow());
				model.setExaminer(user.getName());
				model.setExaminerId(user.getId());
			}

			sdPurchaseConfigDao.modify(model);
		}

		JSONArray jsonItem = JSONArray.fromObject(itemList);
		for (Object object : jsonItem) {

			JSONObject json = JSONObject.fromObject(object);

			SdPurchaseConfigItemModel item = (SdPurchaseConfigItemModel) JSONObject.toBean(json,
					SdPurchaseConfigItemModel.class);
			
			if(item.getProportion() == null) {
				throw new DerpException("保存失败，比例不能为空");
			}
			
			
			if(doubleyn(item.getProportion().toString(), 5)) {
				throw new DerpException("保存失败，比例不能超过5位小数");
			}

			SdTypeConfigModel configModel = sdTypeConfigDao.searchById(item.getSdConfigId());

			SdPurchaseConfigItemModel queryModel = new SdPurchaseConfigItemModel();
			queryModel.setConfigId(model.getId());
			queryModel.setSdConfigId(item.getSdConfigId());

			if (DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(configModel.getType())) {

				queryMap = new HashMap<String, Object>();
				queryMap.put("commbarcode", item.getCommbarcode());

				CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(queryMap);
				
				if(commbarcode == null) {
					throw new DerpException("保存失败，标准条码不存在");
				}
				
				queryModel.setCommbarcode(commbarcode.getCommbarcode());
				
				item.setBrandParent(commbarcode.getCommBrandParentName());
				item.setGoodsName(commbarcode.getCommGoodsName());

			}

			queryModel = sdPurchaseConfigItemDao.searchByModel(queryModel);

			if (queryModel != null) {

				String type = DERP_ORDER.getLabelByKey(DERP_ORDER.sdtypeconfig_typeList, configModel.getType());

				throw new DerpException("保存失败，" + type + "SD类型【" + configModel.getSdTypeName() + "】存在多条");
			}

			item.setSdConfigName(configModel.getSdTypeName());
			item.setSdConfigSimpleName(configModel.getSdSimpleName());
			item.setSdConfigSimpleType(configModel.getType());
			item.setConfigId(model.getId());
			item.setCreateDate(TimeUtils.getNow());
			item.setCreator(user.getName());
			item.setCreatorId(user.getId());

			sdPurchaseConfigItemDao.save(item);
		}
	}

	@Override
	public SdPurchaseConfigDTO getDTOById(Long id) {

		SdPurchaseConfigDTO queryDto = new SdPurchaseConfigDTO();
		queryDto.setId(id);

		SdPurchaseConfigDTO dto = sdPurchaseConfigDao.searchDTO(queryDto);
		return dto;
	}

	@Override
	public List<SdPurchaseConfigItemModel> getItemById(Long id) throws SQLException {

		SdPurchaseConfigItemModel queryModel = new SdPurchaseConfigItemModel();
		queryModel.setConfigId(id);

		return sdPurchaseConfigItemDao.list(queryModel);
	}

	@Override
	public void delOrders(List<Long> list) throws SQLException {
		for (Long orderId : list) {

			SdPurchaseConfigModel configModel = sdPurchaseConfigDao.searchById(orderId);
			if (DERP_ORDER.SDPURCHASE_STATUS_1.equals(configModel.getStatus())) {
				throw new DerpException("删除失败，不能删除【已审核】配置");
			}

			SdPurchaseConfigItemModel queryModel = new SdPurchaseConfigItemModel();
			queryModel.setConfigId(orderId);

			List<SdPurchaseConfigItemModel> delItemList = sdPurchaseConfigItemDao.list(queryModel);
			List<Long> ids = delItemList.stream().map(SdPurchaseConfigItemModel::getId).collect(Collectors.toList());

			sdPurchaseConfigItemDao.delete(ids);
		}

		sdPurchaseConfigDao.delete(list);
	}

	@Override
	public List<SelectBean> getBrandParent() {

		List<SelectBean> selectList = new ArrayList<SelectBean>();
		List<BrandParentMongo> brandParentList = brandParentMongoDao.findAll(new HashMap<>());

		for (BrandParentMongo brandParentMongo : brandParentList) {
			SelectBean select = new SelectBean();
			select.setLabel(brandParentMongo.getName());
			select.setValue(brandParentMongo.getBrandParentId().toString());

			selectList.add(select);
		}

		return selectList;
	}

	@Override
	public Map<String, Object> importSdPurchaseConfig(List<Map<String, String>> data) throws SQLException {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		List<Map<String, String>> configItemList = new ArrayList<Map<String, String>>();

		Map<String, String> cacheMap = new LinkedHashMap<String, String>() ;

		// 表头
		for (int j = 1; j <= data.size(); j++) {

			Map<String, String> map = data.get(j - 1);

			String sdConfigName = map.get("SD类型");

			if (checkIsNullOrNot(j, sdConfigName, "SD类型不能为空", resultList)) {
				failure += 1;
				continue;
			}

			sdConfigName = sdConfigName.trim();

			String commbarcode = map.get("标准条码");

			if (checkIsNullOrNot(j, commbarcode, "标准条码不能为空", resultList)) {
				failure += 1;
				continue;
			}

			commbarcode = commbarcode.trim();

			String proportionStr = map.get("比例");

			if (checkIsNullOrNot(j, proportionStr, "比例不能为空", resultList)) {
				failure += 1;
				continue;
			}

			proportionStr = proportionStr.trim();
			
			if(!isNumber(proportionStr)) {
				setErrorMsg(j, "比例非数字或小数", resultList);

				failure += 1;
				continue;
			}
			
			if(proportionStr.trim().indexOf(".") > -1 && doubleyn(proportionStr, 5)) {
				setErrorMsg(j, "导入的比例不能超过5位小数", resultList);

				failure += 1;
				continue;
			}
			
			Map<String, Object> commbarcodeMap = new HashMap<String, Object>() ;
			commbarcodeMap.put("commbarcode", commbarcode) ;
			
			CommbarcodeMongo commbarcodeMongo = commbarcodeMongoDao.findOne(commbarcodeMap);
			
			if(commbarcodeMongo == null) {
				setErrorMsg(j, "导入商品标准条码不存在", resultList);

				failure += 1;
				continue;
			}

			SdTypeConfigModel queryModel = new SdTypeConfigModel();

			queryModel.setSdTypeName(sdConfigName);
			queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
			queryModel.setStatus(DERP_ORDER.SDTYPECONFIG_STATUS_1);

			queryModel = sdTypeConfigDao.searchByModel(queryModel);

			if (queryModel == null) {
				setErrorMsg(j, "导入SD类型:" + sdConfigName + "不为多比例类型", resultList);

				failure += 1;
				continue;
			}
			
			String key = sdConfigName + "__" + commbarcode ;

			if(cacheMap.containsKey(key)) {
				setErrorMsg(j, "导入SD类型:"+ sdConfigName +"标准条码:" + commbarcode + " 不可存在多行记录", resultList);

				failure += 1;
				continue;
			}

			cacheMap.put(key, key) ;

			Map<String, String> itemMap = new HashMap<String, String>();
			itemMap.put("brandParent",commbarcodeMongo.getCommBrandParentName());
			itemMap.put("proportion",new BigDecimal(proportionStr).toString());
			itemMap.put("commbarcode",commbarcode);
			itemMap.put("goodsName",commbarcodeMongo.getCommGoodsName());
			itemMap.put("sdConfigId",queryModel.getId().toString());
			itemMap.put("sdConfigName",sdConfigName);
			itemMap.put("sdConfigSimpleName",queryModel.getSdSimpleName());
			itemMap.put("sdConfigSimpleType",DERP_ORDER.SDTYPECONFIG_TYPE_2);
			configItemList.add(itemMap);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(failure == 0) {
			map.put("configItemList", configItemList) ;
			success = data.size() ;
		}
		
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		
		return map;
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
	 * 判断小数位
	 * @param str
	 * @param dousize
	 * @return
	 */
	private boolean doubleyn(String str, int dousize) {
		
		int fourplace = str.trim().length() - str.trim().indexOf(".") - 1;
		if (fourplace <= dousize) {
			return false;
		} else {
			return true;
		}
		
	}
	
	/**
	 * 判断是否小数
	 * @param str
	 * @return
	 */
	private boolean isNumber(String str){
		
		if(StringUtils.isBlank(str)) {
			return false ;
		}
		
        String reg = "\\d+(\\.\\d+)?";
        return str.matches(reg);
    }

}
