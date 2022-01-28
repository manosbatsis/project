package com.topideal.order.service.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.dao.sale.MerchandiseContrastItemDao;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.sale.MerchandiseContrastService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 爬虫商品对照表 
 * @author lian_
 */
@Service
public class MerchandiseContrastServiceImpl implements MerchandiseContrastService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(MerchandiseContrastServiceImpl.class);
	@Autowired
	private MerchandiseContrastDao dao;

	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao;
	
	// 商品信息dao
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private  OperateLogDao operateLogDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	/**
	 * 分页查询
	 */
	@Override
	public MerchandiseContrastDTO getListByPage(MerchandiseContrastDTO dto) throws SQLException {
		return dao.searchDTOByPage(dto);
	}

	/**
	 * 详情
	 */
	@Override
	public MerchandiseContrastDTO searchDetail(Long id) throws SQLException {
		return dao.searchDTOById(id);
	}
	
	/**
	 * 新增/修改
	 */
	@Override
	public Map<String, Object> saveContrast(String json,User user) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();

		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		String id = jsonObj.getString("id");
		String platformName = jsonObj.getString("platformName");
		String platformUsername = jsonObj.getString("platformUsername");
		String crawlerGoodsNo = jsonObj.getString("crawlerGoodsNo");
		String crawlerGoodsName = (String) jsonObj.get("crawlerGoodsName");
		String merchantName = (String) jsonObj.get("merchantName");
		Long merchantId = Long.valueOf((String) jsonObj.get("merchantId"));
		Long buId = Long.valueOf((String) jsonObj.get("buId"));
		String buName = (String) jsonObj.get("buName");
		String status = (String) jsonObj.get("status");
		String type = (String) jsonObj.get("type");
		//检查必填字段
		if(StringUtils.isEmpty(platformName)){
			retMap.put("code", "01");
	    	retMap.put("message", "请输入平台名称");
	    	return retMap;
		}
		if(StringUtils.isEmpty(platformUsername)){
			retMap.put("code", "01");
	    	retMap.put("message", "请输入平台用户名");
	    	return retMap;
		}
		if(StringUtils.isEmpty(crawlerGoodsNo)){
			retMap.put("code", "01");
	    	retMap.put("message", "请输入爬虫商品货号");
	    	return retMap;
		}
		if(StringUtils.isEmpty(crawlerGoodsName)){
			retMap.put("code", "01");
	    	retMap.put("message", "请输入爬虫商品名称");
	    	return retMap;
		}
		if(StringUtils.isEmpty(merchantId)){
			retMap.put("code", "01");
	    	retMap.put("message", "请选择公司");
	    	return retMap;
		}
		if(StringUtils.isEmpty(buId)){
			retMap.put("code", "01");
			retMap.put("message", "请选择事业部");
			return retMap;
		}
		if(StringUtils.isEmpty(type)){
			retMap.put("code", "01");
			retMap.put("message", "请选择平台类型");
			return retMap;
		}

		Map<String, Object> buRelParam = new HashMap<>();
		buRelParam.put("merchantId", merchantId);
		buRelParam.put("buId", buId);
		MerchantBuRelMongo buRelMongo = merchantBuRelMongoDao.findOne(buRelParam);
		if(buRelMongo == null){
			retMap.put("code", "01");
			retMap.put("message", "公司下没有关联该事业部");
			return retMap;
		}

		//平台+爬虫sku+公司只能存在一条记录
		MerchandiseContrastModel exist = new MerchandiseContrastModel();
		exist.setType(type);
		exist.setCrawlerGoodsNo(crawlerGoodsNo);
		exist.setMerchantId(merchantId);
		if (!StringUtils.isEmpty(id)) {
			exist.setId(Long.valueOf(id));
		}
		exist = dao.isExistModel(exist);
		if (exist != null) {
			retMap.put("code", "01");
			retMap.put("message", "相同平台+爬虫商品货号+公司已存在相同数据");
			return retMap;
		}


		// 保存表头数据
		MerchandiseContrastModel merchandiseContrastModel = new MerchandiseContrastModel();
		merchandiseContrastModel.setPlatformName(platformName);
		merchandiseContrastModel.setPlatformUsername(platformUsername);
		merchandiseContrastModel.setCrawlerGoodsNo(crawlerGoodsNo);
		merchandiseContrastModel.setCrawlerGoodsName(crawlerGoodsName);
		merchandiseContrastModel.setMerchantId(merchantId);
		merchandiseContrastModel.setMerchantName(merchantName);
		merchandiseContrastModel.setBuId(buId);
		merchandiseContrastModel.setBuName(buName);
		merchandiseContrastModel.setStatus(status);
		merchandiseContrastModel.setType(type);
		if (!"".equals(id)){
			merchandiseContrastModel.setId(Long.valueOf(id));
			merchandiseContrastModel.setUpdateUsername(user.getName());
			merchandiseContrastModel.setModifyDate(TimeUtils.getNow());
			dao.modify(merchandiseContrastModel);

		}else {
			merchandiseContrastModel.setCreateUsername(user.getName());
			merchandiseContrastModel.setCreateDate(TimeUtils.getNow());			
			dao.save(merchandiseContrastModel);
		}

		//解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		ArrayList<String> goodsList = new ArrayList<>();
		for (int j = 0; j < itemArr.size(); j++) {
			JSONObject job = itemArr.getJSONObject(j);
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String numStr = (String) job.get("num");
			String price = (String) job.get("price");
			
			Map<String, Object> parmMap=new HashMap<>();
			parmMap.put("merchandiseId", goodsId);
			parmMap.put("goodsNo", goodsNo);
			parmMap.put("merchantId", merchantId);
			
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(parmMap);
			if (merchandiseInfoMogo==null) {
				retMap.put("code", "01");
				retMap.put("message", "没有查询到商品信息："+ goodsNo);
				return retMap;
			}
			if (goodsList.contains(goodsNo)){
				retMap.put("code", "01");
				retMap.put("message", "商品货号不能重复,货号为："+ goodsNo);
				return retMap;
			}
			if ("".equals(numStr.trim())){
				retMap.put("code", "01");
				retMap.put("message", "数量必填！");
				return retMap;
			}
			if ("".equals(price.trim())){
				retMap.put("code", "01");
				retMap.put("message", "销售标准单价必填！");
				return retMap;
			}
			if (Integer.valueOf(numStr)<=0){
				retMap.put("code", "01");
				retMap.put("message", "数量需大于0！");
				return retMap;
			}
			if (new BigDecimal(price).compareTo(BigDecimal.ZERO)==-1){
				retMap.put("code", "01");
				retMap.put("message", "销售标准单价不能小于0！");
				return retMap;
			}
			
			goodsList.add(goodsNo);
		}
		if (!"".equals(id)){
			MerchandiseContrastItemModel model = new MerchandiseContrastItemModel();
			model.setContrastId(Long.valueOf(id));
			List<MerchandiseContrastItemDTO> itemList = merchandiseContrastItemDao.getContrastItemByContrastId(model);
			String OperateRemark="删除表体";
			for(MerchandiseContrastItemDTO item:itemList){
				OperateRemark=OperateRemark+"商品货号:"+item.getGoodsNo()+"数量:"+item.getNum()+"单价:"+item.getPrice()+";";								
			}

			Long count = merchandiseContrastItemDao.deleteByContrastId(Long.valueOf(id));
			//保存操作日志
			OperateLogModel saveLogModel = new OperateLogModel() ;
			saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_13);
			saveLogModel.setOperateDate(TimeUtils.getNow());
			saveLogModel.setOperater(user.getName());
			saveLogModel.setOperateId(user.getId());			
			saveLogModel.setOperateRemark(OperateRemark);
			saveLogModel.setRelCode(merchandiseContrastModel.getId().toString());// 没有单号  存id
			saveLogModel.setCreateDate(TimeUtils.getNow());			
			operateLogDao.save(saveLogModel) ;
		}
		String OperateRemark="新增表体";

		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			if (job.isNullObject() || job.isEmpty()) {
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String barcode = (String) job.get("barcode");
			String numStr = (String) job.get("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();

			// 保存表体数据
			MerchandiseContrastItemModel contrastItemModel = new MerchandiseContrastItemModel();
			contrastItemModel.setGoodsId(goodsId);
			contrastItemModel.setGoodsNo(goodsNo);
			contrastItemModel.setGoodsName(goodsName);
			contrastItemModel.setBarcode(barcode);
			contrastItemModel.setNum(num);
			contrastItemModel.setPrice(new BigDecimal(price));
			contrastItemModel.setContrastId(merchandiseContrastModel.getId());
			contrastItemModel.setCreateDate(TimeUtils.getNow());
			contrastItemModel.setModifyDate(TimeUtils.getNow());
			merchandiseContrastItemDao.save(contrastItemModel);
			
			OperateRemark=OperateRemark+"商品货号:"+goodsNo+"数量:"+numStr+"单价:"+price+";";								

		}

		//保存操作日志
		OperateLogModel saveLogModel = new OperateLogModel() ;
		saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_13);
		saveLogModel.setOperateDate(TimeUtils.getNow());
		saveLogModel.setOperater(user.getName());
		saveLogModel.setOperateId(user.getId());			
		saveLogModel.setOperateRemark(OperateRemark);
		saveLogModel.setRelCode(merchandiseContrastModel.getId().toString());// 没有单号  存id
		saveLogModel.setCreateDate(TimeUtils.getNow());			
		operateLogDao.save(saveLogModel) ;

	    retMap.put("code", "00");
	    retMap.put("message", "成功");
		return retMap;
	}

	/**
	 * 批量删除
	 */
	@Override
	public boolean delContrast(List<Long> list) throws SQLException {
		int num = dao.delete(list);
	    if(num >= 1){
	        return true;
	    }
	    return false;
	}

	/**
	 * 编辑
	 */
	@Override
	public boolean modifyContrast(MerchandiseContrastModel model) throws SQLException {
		int num = dao.modify(model);
		if (num >= 1) {
			return true;
		}
		return false;
	}
    
	/**
	 * 更具商家id、货号查询商品
	 */
	@Override
	public List<MerchandiseInfoMogo> getMerchandiseList(Long merchantId,String goodsNo) throws SQLException {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId", merchantId);
		paramMap.put("goodsNo", goodsNo);
		paramMap.put("status", "1");//启用
		
		List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findAll(paramMap);
	   
		return merchandiseList;
	}

	@Override
	public List<Map<String, Object>> listForExport(MerchandiseContrastDTO dto) throws SQLException {
		List<Map<String, Object>> mapList = dao.listForExport(dto);
		for (Map<String, Object> map : mapList) {
			String status = (String) map.get("status");
			map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.merchandiseContrast_statusList, status));
		}
		return mapList;
	}

}
