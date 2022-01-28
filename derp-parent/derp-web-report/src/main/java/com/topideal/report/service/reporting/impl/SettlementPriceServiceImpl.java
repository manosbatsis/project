package com.topideal.report.service.reporting.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.SettlementPriceDao;
import com.topideal.dao.reporting.SettlementPriceRecordDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.dto.ImportErrorMessage;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceItemDTO;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.report.service.reporting.SettlementPriceService;
import com.topideal.report.shiro.ShiroUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 结算价格 serviceImpl
 */
@Service
public class SettlementPriceServiceImpl implements SettlementPriceService{
	@Autowired
	private SettlementPriceDao dao;
	// 商品信息dao
	@Autowired
	private MerchandiseInfoDao merchandiseDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private SettlementPriceRecordDao settlementPriceRecordDao;

	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	
	/**
	 * 分页
	 */
	@Override
	public SettlementPriceModel listPrice(SettlementPriceModel model) throws SQLException {
		return dao.searchByPage(model);
	}
	@Override
	public Map<String, Object> saveSettlementPrice(Long buId,List<SettlementPriceItemDTO> itemList, Long merchantId, String merchantName, User user) throws Exception {
		Map<String, Object> resultMap=new HashMap<>();

		try{
			//存储需要保存的数据
			List<SettlementPriceModel> priceList= new ArrayList<SettlementPriceModel>();

			if(buId==null){
				resultMap.put("code","01");
				resultMap.put("message","事业部不能为空");
				return resultMap;
			}
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("buId", buId) ;
			queryMap.put("merchantId", merchantId) ;
			queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
			MerchantBuRelMongo merchantMel = merchantBuRelMongoDao.findOne(queryMap);
			if(merchantMel == null) {
				resultMap.put("code","01");
				resultMap.put("message","公司事业部关联不存在");
				return resultMap;
			}

			for(SettlementPriceItemDTO item:itemList){
				//获取最早未关账日期
				String startDate = item.getStartDate();
				if(startDate.compareTo(item.getEffectiveDate())>=1){//关账最早日期大于生效日期
					resultMap.put("code","01");
					resultMap.put("message","条码:"+item.getBarcode()+"生效日期小于未关账最早的日期。");
					return resultMap;
				}
				//查询记录是否已存在
				SettlementPriceModel tempSprice = new SettlementPriceModel();
				tempSprice.setMerchantId(merchantId);
				tempSprice.setBarcode(item.getBarcode());
				tempSprice.setBuId(buId);

				List<SettlementPriceModel> list= dao.list(tempSprice);
				if(list != null && list.size() > 0){//记录已存在
					resultMap.put("code","01");
					resultMap.put("message","条码:"+item.getBarcode()+"已存在标准成本单价记录。");
					return resultMap;
				}

				//组装数据
				SettlementPriceModel sprice = new SettlementPriceModel();
				sprice.setMerchantId(merchantId);
				sprice.setMerchantName(merchantName);
				sprice.setGoodsId(item.getGoodsId());
				sprice.setGoodsName(item.getGoodsName());
				sprice.setGoodsNo(item.getGoodsNo());
				sprice.setGoodsSpec(item.getGoodsSpec());
				sprice.setBarcode(item.getBarcode());
				sprice.setIsGroup(item.getIsGroup());
				if(item.getBrandId()!=null){
					sprice.setBrandId(item.getBrandId());
				}
				sprice.setBrandName(item.getBrandName());
				if(item.getUnitId()!=null){
					sprice.setUnitId(item.getUnitId());
				}
				sprice.setUnitName(item.getUnitName());

				MerchandiseInfoModel merchandise = merchandiseDao.searchById(item.getGoodsId());
				if(merchandise != null) {					
					sprice.setProductTypeId(merchandise.getProductTypeId());
					sprice.setProductTypeName(merchandise.getProductTypeName());
					sprice.setCountyId(merchandise.getCountyId());
					sprice.setCountyName(merchandise.getCountyName());
				}
				
				sprice.setCreater(user.getName());
				sprice.setCreaterId(user.getId());
				sprice.setCreateDate(TimeUtils.getNow());
				sprice.setModifier(user.getName());
				sprice.setModifierId(user.getId());
				sprice.setModifyDate(TimeUtils.getNow());
				sprice.setCurrency(item.getCurrency());
				sprice.setPrice(item.getPrice());
				sprice.setEffectiveDate(item.getEffectiveDate());
				sprice.setProductId(item.getProductId());
				sprice.setBuId(merchantMel.getBuId());
				sprice.setBuName(merchantMel.getBuName());
				sprice.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_013);

				//添加到保存的集合
				priceList.add(sprice);
			}
			//保存数据
			for(SettlementPriceModel price: priceList){
				Long id = dao.save(price);
				SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel();
				spRecordModel.setCreateDate(TimeUtils.getNow());
				spRecordModel.setModifier(price.getModifier());
				spRecordModel.setModifierId(price.getModifierId());
				spRecordModel.setModifyDate(price.getModifyDate());
				spRecordModel.setCurrency(price.getCurrency());
				spRecordModel.setEffectiveDate(price.getEffectiveDate());
				spRecordModel.setPrice(price.getPrice());
				spRecordModel.setSettlementPriceId(id);
				spRecordModel.setBuId(merchantMel.getBuId());
				spRecordModel.setBuName(merchantMel.getBuName());
				spRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_013);
				settlementPriceRecordDao.save(spRecordModel);
			}
			resultMap.put("code","00");
			resultMap.put("message","保存成功！");
		}catch (Exception e) {
		    e.printStackTrace();
		}
		return resultMap;
	}


	/**
	 * 新增
	 * @return
	 */
	@Override
	public boolean saveSettlementPrice(String json, Long merchantId, String merchantName, User user) throws Exception {
		//存储需要保存的数据
		List<SettlementPriceModel> priceList= new ArrayList<SettlementPriceModel>();
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		
		if(jsonObj.get("buId") == null) {
			throw new RuntimeException("事业部不能为空");
		}
		
		Long buId = jsonObj.getLong("buId");
		
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", buId) ;
		queryMap.put("merchantId", merchantId) ;
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
		
		MerchantBuRelMongo merchantMel = merchantBuRelMongoDao.findOne(queryMap);
		
		if(merchantMel == null) {
			throw new RuntimeException("公司事业部关联不存在");
		}
		
		//解析商品
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		for(int i=0;i<itemArr.size();i++){  
			JSONObject job = itemArr.getJSONObject(i); 
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = job.getString("goodsNo");
			String barcode = job.getString("barcode");
			String goodsName = job.getString("goodsName");
			String brandId = job.getString("brandId");
			String brandName = job.getString("brandName");
			String goodsSpec = job.getString("goodsSpec");
			String currency = job.getString("currency");
			BigDecimal price = new BigDecimal(job.getString("price"));
			String effectiveDate = job.getString("effectiveDate");
			String isGroup = job.getString("isGroup");
			String unitId = job.getString("unitId");
			String unitName = job.getString("unitName");
			String productId = job.getString("productId");
			
			//获取最早未关账日期
			String startDate = job.getString("startDate");
			if(startDate.compareTo(effectiveDate)>=1){//关账最早日期大于生效日期
				throw new RuntimeException("条码:"+barcode+"生效日期小于未关账最早的日期。");
			}
			
			SettlementPriceModel tempSprice = new SettlementPriceModel();
			tempSprice.setMerchantId(merchantId);
			tempSprice.setBarcode(barcode);
			tempSprice.setBuId(buId);
			
			List<SettlementPriceModel> list= dao.list(tempSprice);
			if(list != null && list.size() > 0){//记录已存在
				throw new RuntimeException("条码:"+barcode+"已存在标准成本单价记录。");
			}
			
			//组装数据
			SettlementPriceModel sprice = new SettlementPriceModel();
			sprice.setMerchantId(merchantId);
			sprice.setMerchantName(merchantName);
			sprice.setGoodsId(goodsId);
			sprice.setGoodsName(goodsName);
			sprice.setGoodsNo(goodsNo);
			sprice.setGoodsSpec(goodsSpec);
			sprice.setBarcode(barcode);
			sprice.setIsGroup(isGroup);
			if(StringUtils.isNotBlank(brandId)){
				sprice.setBrandId(Long.valueOf(brandId));
			}
			sprice.setBrandName(brandName);
			if(StringUtils.isNotBlank(unitId)){
				sprice.setUnitId(Long.valueOf(unitId));
			}
			sprice.setUnitName(unitName);
			
			MerchandiseInfoModel merchandise = merchandiseDao.searchById(goodsId);
			if(merchandise != null) {					
				sprice.setProductTypeId(merchandise.getProductTypeId());
				sprice.setProductTypeName(merchandise.getProductTypeName());
				sprice.setCountyId(merchandise.getCountyId());
				sprice.setCountyName(merchandise.getCountyName());
			}
			sprice.setCreater(user.getName());
			sprice.setCreaterId(user.getId());
			sprice.setCreateDate(TimeUtils.getNow());
			sprice.setModifier(user.getName());
			sprice.setModifierId(user.getId());
			sprice.setModifyDate(TimeUtils.getNow());
			sprice.setCurrency(currency);
			sprice.setPrice(price);
			sprice.setEffectiveDate(effectiveDate);
			sprice.setProductId(Long.valueOf(productId));
			sprice.setBuId(merchantMel.getBuId());
			sprice.setBuName(merchantMel.getBuName());
			sprice.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_013);
			
			
			//添加到保存的集合
			priceList.add(sprice);
		}
		//保存数据
		for(SettlementPriceModel price: priceList){
			Long id = dao.save(price);
			SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel();
			spRecordModel.setCreateDate(TimeUtils.getNow());
			spRecordModel.setModifier(price.getModifier());
			spRecordModel.setModifierId(price.getModifierId());
			spRecordModel.setModifyDate(price.getModifyDate());
			spRecordModel.setCurrency(price.getCurrency());
			spRecordModel.setEffectiveDate(price.getEffectiveDate());
			spRecordModel.setPrice(price.getPrice());
			spRecordModel.setSettlementPriceId(id);
			spRecordModel.setBuId(merchantMel.getBuId());
			spRecordModel.setBuName(merchantMel.getBuName());
			spRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_013);
			settlementPriceRecordDao.save(spRecordModel);
		}
        return true;
	}
	

	/**
	 * 导入结算价格信息--有问题，获取不到单位名称、原产国名称
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map importPrice(Map<Integer, List<List<Object>>> data, User user) throws SQLException, ParseException {
		//存储错误信息
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		//存储需要保存的数据
		List<SettlementPriceModel> priceList= new ArrayList<SettlementPriceModel>();
		//调价原因数据
		List<String> reasonList = new ArrayList<String>() ;
		//用于校验唯一
		Map<String,Object> onlyMap = new HashMap<String,Object>();
		
		Long merchantId = user.getMerchantId() ;
		String merchantName = user.getMerchantName() ;
		
		int success = 0;
		int pass = 0;
		int failure = 0;
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				try {
					
					String buCode = map.get("事业部") ;
					String barcode = map.get("商品条码");
					String currency = map.get("币种");
					String price = map.get("价格");
					String effectiveDate = map.get("生效年月");
					String reason = map.get("调价原因");
					
					if(StringUtils.isBlank(buCode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("事业部不能为空");
						resultList.add(message);
						failure += 1;
						continue;				
					}
					
					buCode = buCode.trim() ;

					Map<String, Object> queryMap = new HashMap<String, Object>() ;
					queryMap.put("buCode", buCode);
					queryMap.put("merchantId", merchantId) ;
					queryMap.put("status", DERP_ORDER.MERCHANDISECONTRAST_STATUS_1) ;
					
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap) ;
					
					if(merchantBuRelMongo == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("公司事业部不存在");
						resultList.add(message);
						failure += 1;
						continue;	
					}
					
					boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
					if(!isRelate) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("用户无权限操作该事业部");
						resultList.add(message);
						failure += 1;
						continue;	
					}
					
					if(StringUtils.isBlank(barcode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("商品条码不能为空");
						resultList.add(message);
						failure += 1;
						continue;				
					}
					if(StringUtils.isBlank(currency)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("币种不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
			        if(StringUtils.isBlank(currencyLabel)){
			        	ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("币种有误，请参考码表后填写");
						resultList.add(message);
						failure += 1;
						continue;
			        }
					if(StringUtils.isBlank(price)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");  
			        Matcher isNum = pattern.matcher(price);  
			        if(!isNum.matches()){
			        	ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格必须为正数");
						resultList.add(message);
						failure += 1;
						continue;
			        }
					if(StringUtils.isBlank(effectiveDate)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("生效年月不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					Pattern pattern1 = Pattern.compile("^(\\d{1,4}-)(0[1-9]|1[0-2])$");  
			        Matcher isNum1 = pattern1.matcher(effectiveDate);  
			        if(!isNum1.matches()){
			        	ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("生效年月的格式有误，格式应为：yyyy-MM");
						resultList.add(message);
						failure += 1;
						continue;
			        }
			        if(StringUtils.isBlank(reason)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("调价原因不能为空");
						resultList.add(message);
						failure += 1;
						continue;				
					}
			        //获取最大已关账日期
					String startDate = getMaxMonthByParam(merchantId);
					if(StringUtils.isNotBlank(startDate)){
						if(startDate.compareTo(effectiveDate)>=1){//关账最早日期大于生效日期
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j + 1);
							message.setMessage("条码:"+barcode+"生效日期小于未关账最早的日期。");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}
					
					String key = merchantId+ ";" + buCode + ";" +barcode ;
					
					if(onlyMap.containsKey(key)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("条码:"+barcode+"在表格中存在多条记录。");
						resultList.add(message);
						failure += 1;
						continue;
					}
					
					SettlementPriceModel sprice = new SettlementPriceModel();
					sprice.setMerchantId(merchantId);
					sprice.setBarcode(barcode);
					sprice.setBuId(merchantBuRelMongo.getBuId());
					List<SettlementPriceModel> list= dao.list(sprice);
					if(list != null && list.size() > 0){//记录已存在
						sprice = list.get(0) ;
						//若状态待审核，导入失败
						if(DERP_REPORT.SETTLEMENTPRICE_STATUS_001.equals(sprice.getStatus())) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j + 1);
							message.setMessage("条码:"+barcode+"为:待审核，导入失败");
							resultList.add(message);
							failure += 1;
							continue;
						}
					}else {
						sprice = new SettlementPriceModel();
					}
					
					MerchandiseInfoModel merchandise = merchandiseDao.getListForSettlment(merchantId,barcode);
					if(merchandise == null){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("该商品条码获取不到商品信息");
						resultList.add(message);
						failure += 1;
						continue;
					}
					
					//组装数据
					sprice.setMerchantId(merchantId);
					sprice.setMerchantName(merchantName);
					sprice.setGoodsId(merchandise.getId());
					sprice.setGoodsName(merchandise.getName());
					sprice.setGoodsNo(merchandise.getGoodsNo());
					sprice.setGoodsSpec(merchandise.getSpec());
					sprice.setBarcode(barcode);
					sprice.setIsGroup(merchandise.getIsGroup());
					sprice.setBrandId(merchandise.getBrandId());
					sprice.setBrandName(merchandise.getBrandName());
					sprice.setUnitId(merchandise.getUnit());
					sprice.setUnitName(merchandise.getUnitName());
					sprice.setProductTypeId(merchandise.getProductTypeId());
					sprice.setProductTypeName(merchandise.getProductTypeName());
					sprice.setCountyId(merchandise.getCountyId());
					sprice.setCountyName(merchandise.getCountyName());
					sprice.setCurrency(currency);
					sprice.setPrice(new BigDecimal(price));
					sprice.setEffectiveDate(effectiveDate);
					sprice.setBuId(merchantBuRelMongo.getBuId());
					sprice.setBuName(merchantBuRelMongo.getBuName());
					
					//添加到保存的集合
					priceList.add(sprice) ;
					reasonList.add(reason) ;
					//记录唯一标识
					onlyMap.put(key, 1);
				} catch (Exception e) {
					e.printStackTrace();
					failure += 1;
				}
			}
		}
		//保存数据
		if(failure==0){
			for(int i = 0 ; i < priceList.size() ; i ++){
				SettlementPriceModel price = priceList.get(i) ;
				
				Long id = null ;
				String status =DERP_REPORT.SETTLEMENTPRICE_STATUS_013;
				if(price.getId() != null) {
					
					SettlementPriceModel tempModel = dao.searchById(price.getId()) ;
					
					price.setStatus(status);
					price.setModifier(user.getName());
					price.setModifierId(user.getId());
					price.setModifyDate(TimeUtils.getNow());
					dao.modify(price) ;
					
					if(DERP_REPORT.SETTLEMENTPRICE_STATUS_013.equals(tempModel.getStatus())) {
						SettlementPriceRecordModel lastRecordModel = new SettlementPriceRecordModel();
						lastRecordModel.setSettlementPriceId(price.getId());
						lastRecordModel = settlementPriceRecordDao.getLatestItem(lastRecordModel) ;
						
						if(lastRecordModel != null) {
							lastRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_021);
							settlementPriceRecordDao.modify(lastRecordModel) ;
						}
					}
					
					id = price.getId() ;
				}else {
					price.setCreater(user.getName());
					price.setCreaterId(user.getId());
					price.setCreateDate(TimeUtils.getNow());
					price.setStatus(status);
					id = dao.save(price);
				}
				
				String reason = reasonList.get(i) ;
				SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel();
				spRecordModel.setCreateDate(price.getCreateDate());
				spRecordModel.setModifier(price.getModifier());
				spRecordModel.setModifierId(price.getModifierId());
				spRecordModel.setModifyDate(price.getModifyDate());
				spRecordModel.setCurrency(price.getCurrency());
				spRecordModel.setEffectiveDate(price.getEffectiveDate());
				spRecordModel.setPrice(price.getPrice());
				spRecordModel.setSettlementPriceId(id);
				spRecordModel.setAdjustPriceResult(reason);
				spRecordModel.setStatus(status);
				spRecordModel.setBuId(price.getBuId());
				spRecordModel.setBuName(price.getBuName());
				
				settlementPriceRecordDao.save(spRecordModel);
				if (id != null) {
					success += 1;
				} else {
					failure += 1;
				}
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}
	
	/**
	 * 把两个数组组成一个map
	 * @param keys 键数组
	 * @param values 值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public boolean delPrice(List<Long> ids) throws SQLException {
		List<Long> idsAll = new ArrayList<Long>();
		for(Long id:ids){//根据id获取所有记录
			SettlementPriceModel settlementPrice = dao.searchById(id);
			SettlementPriceModel model = new SettlementPriceModel();
			model.setMerchantId(settlementPrice.getMerchantId());
			model.setBarcode(settlementPrice.getBarcode());
			List<SettlementPriceModel> list = dao.list(model);
			for(SettlementPriceModel spModel : list){
				idsAll.add(spModel.getId());
			}
		}
		int num = dao.delete(idsAll);
		if (num >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public SettlementPriceModel searchDetail(Long id) throws SQLException {
		return dao.searchById(id);
	}

	@Override
	public boolean modifySettlementPrice(List<SettlementPriceItemDTO> itemList, User user, Long buId,Long id) throws Exception {
		//存储需要保存/修改的数据
		List<SettlementPriceModel> priceList= new ArrayList<SettlementPriceModel>();
		if(buId == null) {
			throw new RuntimeException("事业部不能为空");
		}
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", buId) ;
		queryMap.put("merchantId", user.getMerchantId()) ;
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;

		String adjustPriceResult="";//调价原因
		MerchantBuRelMongo merchantMel = merchantBuRelMongoDao.findOne(queryMap);
		if(merchantMel == null) {
			throw new RuntimeException("公司事业部关联不存在");
		}
		if(itemList==null ||itemList.size() <=0){
			throw new RuntimeException("表体信息不存在");
		}
		for(SettlementPriceItemDTO dto:itemList){
			//获取最早未关账日期
			String startDate = dto.getStartDate();
			if(startDate.compareTo(dto.getEffectiveDate())>=1){//关账最早日期大于生效日期
				throw new RuntimeException("条码:"+dto.getBarcode()+"生效日期小于未关账最早的日期。");
			}
			adjustPriceResult=dto.getAdjustPriceResult();
			//组装数据
			SettlementPriceModel sprice = new SettlementPriceModel();
			sprice.setId(Long.valueOf(id));
			sprice.setMerchantId(user.getMerchantId());
			sprice.setMerchantName(user.getMerchantName());
			sprice.setGoodsId(dto.getGoodsId());
			sprice.setGoodsName(dto.getGoodsName());
			sprice.setGoodsNo(dto.getGoodsNo());
			sprice.setGoodsSpec(dto.getGoodsSpec());
			sprice.setBarcode(dto.getBarcode());
			sprice.setIsGroup(dto.getIsGroup());
			if(dto.getBrandId()!=null){
				sprice.setBrandId(dto.getBrandId());
			}
			sprice.setBrandName(dto.getBrandName());
			if(dto.getUnitId()!=null){
				sprice.setUnitId(dto.getUnitId());
			}
			sprice.setUnitName(dto.getUnitName());
			MerchandiseInfoModel merchandise = merchandiseDao.searchById(dto.getGoodsId());
			if(merchandise != null) {					
				sprice.setProductTypeId(merchandise.getProductTypeId());
				sprice.setProductTypeName(merchandise.getProductTypeName());
				sprice.setCountyId(merchandise.getCountyId());
				sprice.setCountyName(merchandise.getCountyName());
			}
			sprice.setProductTypeId(merchandise.getProductTypeId());
			sprice.setProductTypeName(merchandise.getProductTypeName());
			sprice.setCountyId(merchandise.getCountyId());
			sprice.setCountyName(merchandise.getCountyName());
			sprice.setCurrency(dto.getCurrency());
			sprice.setPrice(dto.getPrice());
			sprice.setEffectiveDate(dto.getEffectiveDate());
			if(dto.getProductId()!=null){
				sprice.setProductId(dto.getProductId());
			}
			//添加到保存的集合
			priceList.add(sprice);
		}

		//修改、保存数据
		for(SettlementPriceModel price: priceList){
			if(price.getId() != null){

				SettlementPriceModel tempModel = dao.searchById(price.getId());

				String status = tempModel.getStatus() ;
				//若状态为待审核，编辑失败
				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_001.equals(status)) {
					throw new RuntimeException("当前状态为：待审核") ;
				}

				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_033.equals(status)
						|| DERP_REPORT.SETTLEMENTPRICE_STATUS_032.equals(status)) {
					status = DERP_REPORT.SETTLEMENTPRICE_STATUS_013;
				}

				price.setStatus(status);
				price.setModifyDate(TimeUtils.getNow());
				price.setModifier(user.getName());
				price.setModifierId(user.getId());
				dao.modify(price);

				/**
				 * 设置最后一条待提交记录为已失效
				 */

				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_013.equals(tempModel.getStatus())) {
					SettlementPriceRecordModel lastRecordModel = new SettlementPriceRecordModel();
					lastRecordModel.setSettlementPriceId(price.getId());
					lastRecordModel = settlementPriceRecordDao.getLatestItem(lastRecordModel) ;

					if(lastRecordModel != null) {
						lastRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_021);
						settlementPriceRecordDao.modify(lastRecordModel) ;
					}
				}
				SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel();
				spRecordModel.setCreateDate(TimeUtils.getNow());
				spRecordModel.setModifier(price.getModifier());
				spRecordModel.setModifierId(price.getModifierId());
				spRecordModel.setModifyDate(TimeUtils.getNow());
				spRecordModel.setCurrency(price.getCurrency());
				spRecordModel.setEffectiveDate(price.getEffectiveDate());
				spRecordModel.setPrice(price.getPrice());
				spRecordModel.setSettlementPriceId(price.getId());
				spRecordModel.setAdjustPriceResult(adjustPriceResult);
				spRecordModel.setStatus(status);
				spRecordModel.setBuId(merchantMel.getBuId());
				spRecordModel.setBuName(merchantMel.getBuName());

				settlementPriceRecordDao.save(spRecordModel);
			}
		}
		return true;
	}


	/**
	 * 编辑
	 */
	@Override
	public boolean modifySettlementPrice(String json, Long merchantId, String merchantName, User user) throws Exception {
		//存储需要保存/修改的数据
		List<SettlementPriceModel> priceList= new ArrayList<SettlementPriceModel>();
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		String adjustPriceResult =null;
		
		if(jsonObj.get("buId") == null) {
			throw new RuntimeException("事业部不能为空");
		}
		
		Long buId = jsonObj.getLong("buId");
		
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", buId) ;
		queryMap.put("merchantId", merchantId) ;
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
		
		MerchantBuRelMongo merchantMel = merchantBuRelMongoDao.findOne(queryMap);
		
		if(merchantMel == null) {
			throw new RuntimeException("公司事业部关联不存在");
		}
		
		//解析商品
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		for(int i=0;i<itemArr.size();i++){  
			JSONObject job = itemArr.getJSONObject(i); 
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			String id =job.getString("id");
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = job.getString("goodsNo");
			String barcode = job.getString("barcode");
			String goodsName = job.getString("goodsName");
			String brandId = job.getString("brandId");
			String brandName = job.getString("brandName");
			String goodsSpec = job.getString("goodsSpec");
			String currency = job.getString("currency");
			BigDecimal price = new BigDecimal(job.getString("price"));
			String effectiveDate = job.getString("effectiveDate");
			String isGroup = job.getString("isGroup");
			String unitId = job.getString("unitId");
			String unitName = job.getString("unitName");
			String productId = job.getString("productId");
			adjustPriceResult = job.getString("adjustPriceResult");	// 调价原因
			//获取最早未关账日期
			String startDate = job.getString("startDate");
			if(startDate.compareTo(effectiveDate)>=1){//关账最早日期大于生效日期
				throw new RuntimeException("条码:"+barcode+"生效日期小于未关账最早的日期。");
			}
			//组装数据
			SettlementPriceModel sprice = new SettlementPriceModel();
			sprice.setId(Long.valueOf(id));
			sprice.setMerchantId(merchantId);
			sprice.setMerchantName(merchantName);
			sprice.setGoodsId(goodsId);
			sprice.setGoodsName(goodsName);
			sprice.setGoodsNo(goodsNo);
			sprice.setGoodsSpec(goodsSpec);
			sprice.setBarcode(barcode);
			sprice.setIsGroup(isGroup);
			
			if(StringUtils.isNotBlank(brandId)){
				sprice.setBrandId(Long.valueOf(brandId));
			}
			sprice.setBrandName(brandName);
			if(StringUtils.isNotBlank(unitId)){
				sprice.setUnitId(Long.valueOf(unitId));
			}
			sprice.setUnitName(unitName);
			MerchandiseInfoModel merchandise = merchandiseDao.searchById(goodsId);
			if(merchandise != null) {					
				sprice.setProductTypeId(merchandise.getProductTypeId());
				sprice.setProductTypeName(merchandise.getProductTypeName());
				sprice.setCountyId(merchandise.getCountyId());
				sprice.setCountyName(merchandise.getCountyName());
			}
			sprice.setCurrency(currency);
			sprice.setPrice(price);
			sprice.setEffectiveDate(effectiveDate);
			if(StringUtils.isNotBlank(productId)){
				sprice.setProductId(Long.valueOf(productId));
			}
			//添加到保存的集合
			priceList.add(sprice);
		}
		//修改、保存数据
		for(SettlementPriceModel price: priceList){
			if(price.getId() != null){
				
				SettlementPriceModel tempModel = dao.searchById(price.getId());
				
				String status = tempModel.getStatus() ;
				//若状态为待审核，编辑失败
				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_001.equals(status)) {
					throw new RuntimeException("当前状态为：待审核") ;
				}
				
				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_033.equals(status)
						|| DERP_REPORT.SETTLEMENTPRICE_STATUS_032.equals(status)) {
					status = DERP_REPORT.SETTLEMENTPRICE_STATUS_013;
				}
				
				price.setStatus(status);
				price.setModifyDate(TimeUtils.getNow());
				price.setModifier(user.getName());
				price.setModifierId(user.getId());
				dao.modify(price);
				
				/**
				 * 设置最后一条待提交记录为已失效
				 */
				
				if(DERP_REPORT.SETTLEMENTPRICE_STATUS_013.equals(tempModel.getStatus())) {
					SettlementPriceRecordModel lastRecordModel = new SettlementPriceRecordModel();
					lastRecordModel.setSettlementPriceId(price.getId());
					lastRecordModel = settlementPriceRecordDao.getLatestItem(lastRecordModel) ;
					
					if(lastRecordModel != null) {
						lastRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_021);
						settlementPriceRecordDao.modify(lastRecordModel) ;
					}
				}
				
				SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel();
				spRecordModel.setCreateDate(TimeUtils.getNow());
				spRecordModel.setModifier(price.getModifier());
				spRecordModel.setModifierId(price.getModifierId());
				spRecordModel.setModifyDate(TimeUtils.getNow());
				spRecordModel.setCurrency(price.getCurrency());
				spRecordModel.setEffectiveDate(price.getEffectiveDate());
				spRecordModel.setPrice(price.getPrice());
				spRecordModel.setSettlementPriceId(price.getId());
				spRecordModel.setAdjustPriceResult(adjustPriceResult);
				spRecordModel.setStatus(status);
				spRecordModel.setBuId(merchantMel.getBuId());
				spRecordModel.setBuName(merchantMel.getBuName());
				
				settlementPriceRecordDao.save(spRecordModel);
			}
		}
        return true;
	}

	/**
	 * 列表查询
	 */
	@Override
	public List<SettlementPriceModel> list(SettlementPriceModel model) throws SQLException {
		return dao.list(model);
	}
	/**
	 * 获取已关账的最大日期
	 * @param merchantId
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String getMaxMonthByParam(Long merchantId) throws Exception {
		
		//String maxMonth = financeInventorySummaryDao.getMaxMonthByParam(merchantId);
		// 查询最大关账月份 此处已经不用注释掉了
		String maxMonth=null;
		if(StringUtils.isNotBlank(maxMonth)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
			Date date = sdf.parse(maxMonth);
			
			Calendar cal = Calendar.getInstance() ;
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			
			date = cal.getTime() ;
			
			maxMonth = sdf.format(date) ;
		}else {
			maxMonth = "" ;
		}
		
		return maxMonth;
	}
	/**
	 * 获取变更记录分页数据
	 */
	@Override
	public SettlementPriceRecordModel listRecordPrice(SettlementPriceRecordModel model) throws SQLException {
		return settlementPriceRecordDao.searchByPage(model);
	}
	/**
	 * 根据条件获取成本单价列表以及修改记录的信息
	 * @return
	 */
	@Override
	public List<SettlementPriceDTO> listSettlementPrice(User user,SettlementPriceDTO dto) {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				return new ArrayList<SettlementPriceDTO>();
			}
			
			dto.setBuIds(buIds);
		}
		
		return dao.queryList(dto);
	}

	/**
	 * 根据商品ID查看组合商品的详情
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public List<MerchandiseInfoModel> getAllGroupMerchandiseByGroupId(Long goodsId) throws SQLException {
		return merchandiseDao.getAllGroupMerchandiseByGroupId(goodsId);
	}*/

	@SuppressWarnings("rawtypes")
	@Override
	public boolean saveAudit(List list, User user) throws Exception{
		List<SettlementPriceModel> priceList = dao.searchByIds(list) ;
		
		SettlementPriceModel tempModel = null ;
		for (SettlementPriceModel settlementPriceModel : priceList) {
			
			if(!DERP_REPORT.SETTLEMENTPRICE_STATUS_013.equals(settlementPriceModel.getStatus())) {
				throw new RuntimeException("提交失败，商品货号："+settlementPriceModel.getGoodsNo() + " 成本单价状态非待提交") ;
			}
			
			tempModel = new SettlementPriceModel() ;
			tempModel.setId(settlementPriceModel.getId());
			tempModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_001);
			tempModel.setModifier(user.getName());
			tempModel.setModifierId(user.getId());
			tempModel.setModifyDate(TimeUtils.getNow());
			
			dao.modify(tempModel) ;
			
			SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel() ;
			spRecordModel.setSettlementPriceId(settlementPriceModel.getId()) ;
			spRecordModel = settlementPriceRecordDao.getLatestItem(spRecordModel) ;
			
			if(spRecordModel != null) {
				spRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_001);
				spRecordModel.setModifier(user.getName());
				spRecordModel.setModifierId(user.getId());
				spRecordModel.setModifyDate(tempModel.getModifyDate());
				settlementPriceRecordDao.modify(spRecordModel) ;
			}
		}
		
		return true ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SettlementPriceExamineDTO listExamineList(SettlementPriceExamineDTO model, User user) {
		
		if(model.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				model.setList(new ArrayList<>());
				model.setTotal(0);
				return model;
			}
			
			model.setBuIds(buIds);
		}
		
		List<SettlementPriceExamineDTO> list = dao.listExamineList(model) ;
		Integer total = dao.countExamineList(model) ;
		
		for (SettlementPriceExamineDTO settlementPriceExamineDTO : list) {
			SettlementPriceRecordModel temp = new SettlementPriceRecordModel();
			temp.setSettlementPriceId(settlementPriceExamineDTO.getId());
			temp.setEffectiveDate(settlementPriceExamineDTO.getEffectiveDate());
			
			BigDecimal historyPrice = settlementPriceRecordDao.getHistoryPrice(temp) ;
			
			settlementPriceExamineDTO.setHistoryPrice(historyPrice);
		}
			
		model.setTotal(total);
		model.setList(list);
		
		return model;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean examine(List list, String status, User user) throws Exception{
		List<SettlementPriceModel> priceList = dao.searchByIds(list) ;
		
		SettlementPriceModel tempModel = null ;
		for (SettlementPriceModel settlementPriceModel : priceList) {
			
			if(!DERP_REPORT.SETTLEMENTPRICE_STATUS_001.equals(settlementPriceModel.getStatus())) {
				throw new RuntimeException("提交失败，商品货号："+settlementPriceModel.getGoodsNo() + " 成本单价状态非待审核") ;
			}
			
			String startDate = getMaxMonthByParam(user.getMerchantId()) ;
			if(StringUtils.isNotBlank(startDate)&&startDate.compareTo(settlementPriceModel.getEffectiveDate())>=1){//关账最早日期大于生效日期
				throw new RuntimeException("条码:"+settlementPriceModel.getBarcode()+"生效日期小于未关账最早的日期。");
			}
			
			tempModel = new SettlementPriceModel() ;
			tempModel.setId(settlementPriceModel.getId());
			tempModel.setStatus(status);
			tempModel.setExaminer(user.getName());
			tempModel.setExaminerId(user.getId());
			tempModel.setExamineDate(TimeUtils.getNow());
			
			dao.modify(tempModel) ;
			
			/**
			 * 审核通过时，过去已生效的设为已报废
			 */
			if(DERP_REPORT.SETTLEMENTPRICE_STATUS_032.equals(status)) {
				SettlementPriceRecordModel ysxRecordModel = new SettlementPriceRecordModel() ;
				ysxRecordModel.setSettlementPriceId(settlementPriceModel.getId()) ;
				ysxRecordModel.setEffectiveDate(settlementPriceModel.getEffectiveDate());
				ysxRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_032);
				ysxRecordModel = settlementPriceRecordDao.searchByModel(ysxRecordModel) ;
				
				if(ysxRecordModel != null) {
					ysxRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_021) ;
					settlementPriceRecordDao.modify(ysxRecordModel) ;
				}
			}
			
			SettlementPriceRecordModel spRecordModel = new SettlementPriceRecordModel() ;
			spRecordModel.setSettlementPriceId(settlementPriceModel.getId()) ;
			spRecordModel.setEffectiveDate(settlementPriceModel.getEffectiveDate());
			spRecordModel.setStatus(DERP_REPORT.SETTLEMENTPRICE_STATUS_001);
			spRecordModel = settlementPriceRecordDao.searchByModel(spRecordModel) ;
			
			if(spRecordModel != null) {
				spRecordModel.setStatus(status);
				spRecordModel.setExaminer(user.getName());
				spRecordModel.setExaminerId(user.getId());
				spRecordModel.setExamineDate(tempModel.getExamineDate());
				settlementPriceRecordDao.modify(spRecordModel) ;
			}
		}
		
		return true ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SettlementPriceDTO listPriceDTO(User user,SettlementPriceDTO dto) {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			
			dto.setBuIds(buIds);
		}
		
		return dao.listPriceDTO(dto) ;
	}

	@Override
	public SettlementPriceRecordDTO listRecordPriceDTO(SettlementPriceRecordDTO dto) throws SQLException{
		return settlementPriceRecordDao.listRecordPriceDTO(dto);
	}



}
