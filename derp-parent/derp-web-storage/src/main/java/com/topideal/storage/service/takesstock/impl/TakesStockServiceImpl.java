package com.topideal.storage.service.takesstock.impl;

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

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.TakesStockDao;
import com.topideal.dao.TakesStockItemDao;
import com.topideal.entity.dto.InstructGood;
import com.topideal.entity.dto.InstructRequestVo;
import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.dto.TakesStockFrom;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.entity.vo.TakesStockModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.storage.service.takesstock.TakesStockService;

import net.sf.json.JSONObject;

@Service
public class TakesStockServiceImpl implements TakesStockService{
	
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(TakesStockServiceImpl.class);
      
	@Autowired
	public TakesStockDao takesStockDao;
	
	//仓库信息service
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	
	@Autowired
	private TakesStockItemDao takesStockItemDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	/**
	 * 列表（分页）
	 * 
	 * @param dto
	 * @return
	 */
	public TakesStockDTO listTakesStockPage(TakesStockDTO dto) throws SQLException {
		return takesStockDao.searchDTOByPage(dto);
	}
	
	public Map<String, Object> saveTakesstock(TakesStockFrom model,Long userId,String userName,Long merchantId,String merchantName) throws Exception{
		
		//检查必填字段
		if(StringUtils.isEmpty(model.getDepotId())){
			return getMap("01", "请选择仓库");
		}
		if(StringUtils.isEmpty(model.getServerType())){
			return getMap("01", "请选服务类型");
		}
		if(StringUtils.isEmpty(model.getModel())){
			return getMap("01", "请选择业务场景");
		}
		if(StringUtils.isEmpty(model.getGoodsList())){
			return getMap("01", "请选择商品");
		}
		//查询仓库
		Map<String, Object> dparamMap = new HashMap<String, Object>();
		dparamMap.put("depotId", Long.valueOf(model.getDepotId()));
		DepotInfoMongo depot = depotInfoMongoDao.findOne(dparamMap);
		if(depot == null){
			return getMap("01", "仓库不存在");
		}

		TakesStockModel entity = new TakesStockModel();
		entity.setDepotId(depot.getDepotId());
		entity.setDepotName(depot.getName());
		entity.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDO));//盘点单号
		entity.setServerType(model.getServerType());
		entity.setModel(model.getModel());
		entity.setStatus(DERP_STORAGE.TAKESSTOCK_STATUS_013);//待提交
		entity.setMerchantId(merchantId);
		entity.setMerchantName(merchantName);
		entity.setRemark(model.getRemark());
		entity.setCreateUserId(userId);
		entity.setCreateUsername(userName);
		entity.setCreateTime(TimeUtils.getNow());
		//商品
		List<TakesStockItemModel> itemList = new ArrayList<TakesStockItemModel>();
		List<Map<String, Object>> goodsList = model.getGoodsList();
		for(Map<String, Object> good:goodsList){
			//查询商品
			Long goodsId = Long.valueOf((String)good.get("goodsId"));//商品id
			String goodsNo = (String)good.get("goodsNo");//货号
			String barcode = (String)good.get("barcode");//条形码
			Map<String, Object> mMap = new HashMap<String, Object>();
			mMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mMap);
			if(merchandise==null){
				return getMap("01", goodsNo+"商品不存在");
			}
			TakesStockItemModel stockItem = new TakesStockItemModel();
			stockItem.setGoodsId(merchandise.getMerchandiseId());
			stockItem.setGoodsCode(merchandise.getGoodsCode());
			stockItem.setGoodsNo(merchandise.getGoodsNo());
			stockItem.setGoodsName(merchandise.getName());
			stockItem.setBarcode(barcode);
			stockItem.setCreateTime(TimeUtils.getNow());
			itemList.add(stockItem);
		}
		//保存盘点申请
		Long id = takesStockDao.save(entity);
		for(TakesStockItemModel item:itemList){
			item.setTakesStockId(id);
			takesStockItemDao.save(item);
		}
		
		return getMap("00", "保存成功");
	}
	public Map<String, Object> getMap(String code,String message){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		return map;
	}
	
	public TakesStockModel queryById(String id) throws Exception{
		return takesStockDao.searchById(Long.valueOf(id));
	}
    public Map<String, Object> updateTakesstock(TakesStockFrom model) throws Exception{
		
		//检查必填字段
		if(StringUtils.isEmpty(model.getDepotId())){
			return getMap("01", "请选择仓库");
		}
		if(StringUtils.isEmpty(model.getServerType())){
			return getMap("01", "请选服务类型");
		}
		if(StringUtils.isEmpty(model.getModel())){
			return getMap("01", "请选择业务场景");
		}
		if(StringUtils.isEmpty(model.getGoodsList())){
			return getMap("01", "请选择商品");
		}
		//查询仓库
		Map<String, Object> dparamMap = new HashMap<String, Object>();
		dparamMap.put("depotId", Long.valueOf(model.getDepotId()));
		DepotInfoMongo depot = depotInfoMongoDao.findOne(dparamMap);
		if(depot == null){
			return getMap("01", "仓库不存在");
		}
		//查询盘点申请单
		TakesStockModel entity = takesStockDao.searchById(Long.valueOf(model.getId()));
		if(entity==null){
			return getMap("01", "盘点申请不存在");
		}

		entity.setDepotId(depot.getDepotId());
		entity.setDepotName(depot.getName());
		entity.setServerType(model.getServerType());
		entity.setModel(model.getModel());
		entity.setRemark(model.getRemark());
		//商品
		List<TakesStockItemModel> itemList = new ArrayList<TakesStockItemModel>();
		List<Map<String, Object>> goodsList = model.getGoodsList();
		for(Map<String, Object> good:goodsList){
			//查询商品
			Long goodsId = Long.valueOf((String)good.get("goodsId"));//商品id
			String goodsNo = (String)good.get("goodsNo");//货号
			String barcode = (String)good.get("barcode");//条形码
			Map<String, Object> mMap = new HashMap<String, Object>();
			mMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mMap);
			if(merchandise==null){
				return getMap("01", goodsNo+"商品不存在");
			}
			TakesStockItemModel stockItem = new TakesStockItemModel();
			stockItem.setTakesStockId(entity.getId());
			stockItem.setGoodsId(merchandise.getMerchandiseId());
			stockItem.setGoodsCode(merchandise.getGoodsCode());
			stockItem.setGoodsNo(merchandise.getGoodsNo());
			stockItem.setGoodsName(merchandise.getName());
			stockItem.setBarcode(barcode);
			stockItem.setCreateTime(TimeUtils.getNow());
			itemList.add(stockItem);
		}
		takesStockDao.modify(entity);
		
		//清空原有详情
		takesStockItemDao.delByTakesStockId(entity.getId());
		//插入新的详情
		for(TakesStockItemModel item:itemList){
			takesStockItemDao.save(item);
		}
		
		return getMap("00", "保存成功");
	}
    
    /**
	 * 根据id删除(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delTakesStockBatch(List<Long> ids) throws SQLException {
		int num = 0;
		for (Long id : ids) {
			// 根据id获取信息
			TakesStockModel model = takesStockDao.searchById(id);
			// 判断状态是否待提交
			if (DERP_STORAGE.TAKESSTOCK_STATUS_013.equals(model.getStatus())) {
				model.setStatus(DERP.DEL_CODE_006);//已删除
				num += takesStockDao.modify(model);
			}
		}
		if (num == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 提交发送盘点指令
	 * */
    @SuppressWarnings({ "unused"})
	public String updateSendtakesStock(Long userId,String name,String topidealCode,String ids) throws Exception{
    	StringBuffer failCode = new StringBuffer();//指令提交失败单号
    	
    	String[] takesIdArr = ids.split(",");
		for(int i=0;i<takesIdArr.length;i++){
				Long Id = Long.valueOf(takesIdArr[i]);
				//查询盘点申请
				TakesStockModel model = takesStockDao.searchById(Id);
				if(model==null){
					continue;
				}
				//检查状态
				if(model.getStatus().equals(DERP_STORAGE.TAKESSTOCK_STATUS_014)){
					continue;
				}
				//检查仓库
				Map<String, Object> dparam = new HashMap<String, Object>();
				dparam.put("depotId", model.getDepotId());
				DepotInfoMongo depot = depotInfoMongoDao.findOne(dparam);
				if(depot==null){
					failCode.append(model.getCode()+":盘点申请仓库不存在\n");
					continue;
				}
				
				//查询盘点详情
				TakesStockItemModel itemModel = new TakesStockItemModel();
				itemModel.setTakesStockId(model.getId());
				List<TakesStockItemModel> itemList = takesStockItemDao.list(itemModel);
				if(itemList==null||itemList.size()<1){
					failCode.append(model.getCode()+":盘点商品不存在\n");
					continue;
				}
				//发送盘点指令
				InstructRequestVo request = new InstructRequestVo();
				request.setOrder_id(model.getCode());//企业盘点单号
				request.setOrder_date(TimeUtils.format(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				request.setTrust_code(topidealCode);//委托单位		
				request.setElectric_code(topidealCode);//电商企业编码
				request.setWarehouse_id(depot.getCode());//仓库编码
				request.setServe_types(model.getServerType());//服务类型
				request.setBusi_scene(model.getModel());//业务场景
				
				List<InstructGood> goodList = new ArrayList<InstructGood>();
				for(int k=0;k<itemList.size();k++){
					TakesStockItemModel item = itemList.get(k);
					InstructGood good = new InstructGood();
					good.setIndex(k+1);
					good.setGoods_id(item.getGoodsNo());//商品货号
					goodList.add(good);
				}
				request.setGood_list(goodList);
				JSONObject jsonTakes = JSONObject.fromObject(request);
				jsonTakes.put("method",EpassAPIMethodEnum.EPASS_E05_METHOD.getMethod());
				jsonTakes.put("topideal_code",topidealCode);
				//发送盘点指令消息
				SendResult result = rocketMQProducer.send(jsonTakes.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
				System.out.println("发送盘点指令消息返回："+result.toString());
				if(result== null){
					failCode.append(model.getCode()+":发送盘点消息服务异常\n");
					continue;
				}
				if(!result.getSendStatus().name().equals("SEND_OK")){//SEND_OK-成功
					failCode.append(model.getCode()+":发送盘点指令失败\n");
					continue;
				}
				
				model.setSubmitUserId(userId);
				model.setSubmitUsername(name);
				model.setSubmitTime(TimeUtils.getNow());
				
				//更新状态
				model.setStatus(DERP_STORAGE.TAKESSTOCK_STATUS_014);//已提交
				takesStockDao.modify(model);
		}
    	
    	return failCode.toString();
    }

	@Override
	public TakesStockDTO queryDTOById(Long id) throws SQLException {
		return takesStockDao.getDtoDetail(id);
	}
}
