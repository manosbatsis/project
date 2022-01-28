package com.topideal.order.service.platformdata.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.dao.sale.PlatformMerchandiseInfoDao;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.entity.vo.sale.PlatformMerchandiseInfoModel;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.order.service.platformdata.PlatformMerchandiseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 京东商品管理
 */
@Service
public class PlatformMerchandiseServiceImpl implements PlatformMerchandiseService {

	@Autowired
	public PlatformMerchandiseInfoDao platformMerchandiseInfoDao;
	@Autowired
	public ReptileConfigMongoDao reptileConfigMongoDao;
	/**
	 * 分页查询
	 */
	@Override
	public PlatformMerchandiseInfoDTO getListByPage(PlatformMerchandiseInfoDTO dto) throws Exception {
		return platformMerchandiseInfoDao.searchDTOByPage(dto);
	}

    @Override
    public List<PlatformMerchandiseInfoDTO> getList(PlatformMerchandiseInfoDTO dto) throws Exception {
        return platformMerchandiseInfoDao.listByDto(dto);
    }


    /**平台商品导入
	 * */
	public Map savePlatformMerchandiseImport(List<Map<String, String>> data,Long merchantId) throws Exception{
		Map retMap = new HashMap();
		List<ImportMessage> errorList = new ArrayList<>();
		int success = 0;//检查通过数量
		int failure = 0;//检查失败数量
		List<PlatformMerchandiseInfoModel> modelList = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			String userCode = map.get("归属账号");
			String wareId = map.get("商品编码");
			String goodsName = map.get("商品名称");
			String upc = map.get("条形码");
			String brand = map.get("品牌");
			String unit = map.get("单位");
			String cartonSizeStr = map.get("箱规");
			//检查归属账号
			if(checkIsNullOrNot(i, userCode, "归属账号不能为空", errorList)) {
				failure += 1;
				continue;
			}
			if(checkIsNullOrNot(i, wareId, "商品编码不能为空", errorList)) {
				failure += 1;
				continue;
			}
			if(checkIsNullOrNot(i, goodsName, "商品名称不能为空", errorList)) {
				failure += 1;
				continue;
			}
			//查询爬虫配置
			Map<String,Object> reptileMap = new HashMap<>();
			reptileMap.put("loginName",userCode);
			List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(reptileMap);
			if(reptileConfigList==null||reptileConfigList.size()<=0){
				ImportMessage message = new ImportMessage();
				message.setRows(i + 2);
				message.setMessage("爬虫配置未查询到归属账号");
				errorList.add(message);
				failure += 1;
				continue;
			}
			ReptileConfigMongo reptileConfigMongo = null;
			for(ReptileConfigMongo tempConfig : reptileConfigList){
				if(merchantId.longValue()==tempConfig.getMerchantId().longValue()){
					reptileConfigMongo = tempConfig;
					break;
				}
			}
			if(reptileConfigMongo==null){
				ImportMessage message = new ImportMessage();
				message.setRows(i + 2);
				message.setMessage("归属账号与当前登录公司不匹配"+userCode);
				errorList.add(message);
				failure += 1;
				continue;
			}


			//检查商品编码在本账号下是否已存在
			PlatformMerchandiseInfoModel platformMerchandiseTemp = new PlatformMerchandiseInfoModel();
			platformMerchandiseTemp.setWareId(wareId);
			platformMerchandiseTemp.setMerchantId(merchantId);
			//platformMerchandiseTemp.setUserCode(userCode);
			platformMerchandiseTemp = platformMerchandiseInfoDao.searchByModel(platformMerchandiseTemp);
			if(platformMerchandiseTemp!=null){
				ImportMessage message = new ImportMessage();
				message.setRows(i + 2);
				message.setMessage("归属账号下商品已存在");
				errorList.add(message);
				failure += 1;
				continue;
			}
			Integer cartonSize = 0;
			if(StringUtils.isNotBlank(cartonSizeStr)) {
				try {
					cartonSize = Integer.valueOf(cartonSizeStr);
				} catch (Exception e) {
				}
				if (cartonSize <= 0) {
					cartonSize = null;
					ImportMessage message = new ImportMessage();
					message.setRows(i + 2);
					message.setMessage("箱规要大于0");
					errorList.add(message);
					failure += 1;
					continue;
				}
			}

			PlatformMerchandiseInfoModel platformMerchandise = new PlatformMerchandiseInfoModel();
			platformMerchandise.setWareId(wareId);
			platformMerchandise.setName(goodsName);
			platformMerchandise.setBrand(brand);
			platformMerchandise.setUnit(unit);
			platformMerchandise.setUpc(upc);
			platformMerchandise.setPlatform(DERP.getLabelByKey(DERP.crawler_typeList,reptileConfigMongo.getPlatformType()));
			platformMerchandise.setCrawlerType(reptileConfigMongo.getPlatformType());
			platformMerchandise.setUserCode(userCode);
			platformMerchandise.setMerchantId(reptileConfigMongo.getMerchantId());
			platformMerchandise.setMerchantName(reptileConfigMongo.getMerchantName());
			platformMerchandise.setCartonSize(cartonSize);
			modelList.add(platformMerchandise);
		}

		if(failure>0){
			retMap.put("success", success);
			retMap.put("failure", failure);
			retMap.put("errorList", errorList);
			return retMap;
		}
		//批量插入
		List<PlatformMerchandiseInfoModel> tempList = new ArrayList<>();
		for(int i = 0;i < modelList.size();i++){
			tempList.add(modelList.get(i));
			success++;
			//满1000插入一次
			if(success%1000==0){
				platformMerchandiseInfoDao.insertBatch(tempList);
				tempList = new ArrayList<>();
				continue;
			}
		}
		platformMerchandiseInfoDao.insertBatch(tempList);

		retMap.put("success", success);
		retMap.put("failure", failure);
		retMap.put("errorList", errorList);
		return retMap;
	}
	/**箱规导入
	 * */
	public Map updateCartonSizeImport(List<Map<String, String>> data,Long merchantId) throws Exception{
		Map retMap = new HashMap();
		List<ImportMessage> errorList = new ArrayList<>();
		int success = 0;//检查通过数量
		int failure = 0;//检查失败数量
		List<PlatformMerchandiseInfoModel> modelList = new ArrayList<>();
		for(int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			String wareId = map.get("商品编码");
			String cartonSizeStr = map.get("箱规");

			if(checkIsNullOrNot(i, wareId, "商品编码不能为空", errorList)) {
				failure += 1;
				continue;
			}
			if(checkIsNullOrNot(i, cartonSizeStr, "箱规不能为空", errorList)) {
				failure += 1;
				continue;
			}
			int cartonSize = 0;
			try{
				cartonSize = Integer.valueOf(cartonSizeStr);
			}catch (Exception e){ }
			if(cartonSize<=0){
				ImportMessage message = new ImportMessage();
				message.setRows(i + 1);
				message.setMessage("箱规要大于0");
				errorList.add(message);
				failure += 1;
				continue;
			}
			//检查商品是否存在
			PlatformMerchandiseInfoModel platformMerchandise = new PlatformMerchandiseInfoModel();
			platformMerchandise.setMerchantId(merchantId);
			platformMerchandise.setWareId(wareId);
			List<PlatformMerchandiseInfoModel> listTempModel = platformMerchandiseInfoDao.list(platformMerchandise);
			if(listTempModel==null||listTempModel.size()<1){
				ImportMessage message = new ImportMessage();
				message.setRows(i + 1);
				message.setMessage("未查到商品编码"+wareId);
				errorList.add(message);
				failure += 1;
				continue;
			}
			for(PlatformMerchandiseInfoModel tempModel:listTempModel){
				PlatformMerchandiseInfoModel updateModel = new PlatformMerchandiseInfoModel();
				updateModel.setId(tempModel.getId());
				updateModel.setCartonSize(cartonSize);
				modelList.add(updateModel);
			}

		}

		if(failure>0){
			retMap.put("success", success);
			retMap.put("failure", failure);
			retMap.put("errorList", errorList);
			return retMap;
		}
		//更新
		for(int i = 0;i < modelList.size();i++){
			PlatformMerchandiseInfoModel tempModel = modelList.get(i);
			platformMerchandiseInfoDao.modify(tempModel);
		    success++;
		}
		retMap.put("success", success);
		retMap.put("failure", failure);
		retMap.put("errorList", errorList);
		return retMap;
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
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportMessage> resultList) {
		if (org.apache.commons.lang3.StringUtils.isBlank(content)) {
			ImportMessage message = new ImportMessage();
			message.setRows(index + 2);
			message.setMessage(msg);
			resultList.add(message);
			return true;
		} else {
			return false;
		}
	}

}
