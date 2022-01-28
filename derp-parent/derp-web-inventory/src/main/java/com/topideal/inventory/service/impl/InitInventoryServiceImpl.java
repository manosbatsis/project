package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InitInventoryDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.entity.dto.InitInventoryVo;
import com.topideal.entity.vo.InitInventoryModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.inventory.service.InitInventoryService;
import com.topideal.inventory.webapi.form.InitInventorySaveForm;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 库存-期初库存service实现类
 */
@Service
public class InitInventoryServiceImpl implements InitInventoryService {

	//库存信息dao
    @Autowired
    private InitInventoryDao initInventoryDao;

    //库存表-批次库存明细dao
    @Autowired
    private InventoryBatchDao inventoryBatchDao;
    //仓库MongoDao
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    //商品MongoDao
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;//商家仓库关系表
	@Autowired
	private BrandMongoDao brandMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;	

    /**
	 * 库存信息列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InitInventoryDTO listInitInventory(InitInventoryDTO model)throws SQLException {
        return initInventoryDao.searchDTOByPage(model);
    }

    /**
	 * 根据库存信息id删除库存信息(支持批量)
	 * @param ids
	 * @return
	 */
    @Override
    public boolean delInitInventory(List ids)throws SQLException{
        int num = initInventoryDao.delete(ids);
        if(num >= 1){
            return true;
        }
        return false;
    }

    
    /**
     * 导入库存信息
     */
	@Override
	public Map importInitInventory11(Map<Integer, List<List<Object>>> data,Long userId,Long merchantId,String merchantName,String topidealCode) throws Exception{
		Map<String, Object> retMap = new HashMap<>();
		List<Map<String, String>> msgList = new ArrayList<>();// 存检查失败消息
		int failure = 0;// 统计检查失败数量
		List<InitInventoryVo> initInventoryVolList=new ArrayList<InitInventoryVo>();
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
				try{
					Map<String, String> msg = new HashMap<String, String>();// 失败消息
					//必填字段的校验
					String depotCode = map.get("仓库自编码");
					if(depotCode == null || "".equals(depotCode)){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行仓库自编码为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String buCode = map.get("事业部编码");
					if(buCode == null || "".equals(buCode)){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"事业部编码为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					
					String goodsNo = map.get("商品货号");
					if(goodsNo == null || "".equals(goodsNo)){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行商品货号为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					
					String type = map.get("库存类型");
					if(type == null || "".equals(type)){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行库存类型为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}else{
						if("0".equals(type)||"1".equals(type)){
							
						}else{
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行库存类型填写错误");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}
					String batchNo = map.get("批次号");		
					String initNum = map.get("库存数量");

					String productionDate = map.get("生产日期");
					if(StringUtils.isNotBlank(productionDate)){
						if(!this.isValidDate(productionDate)){
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行生产日期格式填写错误");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

					String overdueDate = map.get("有效期至");
					if(StringUtils.isNotBlank(overdueDate)){
						if(!this.isValidDate(overdueDate)){
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行有效期至格式填写错误");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}
					
					if(StringUtils.isNotBlank(productionDate)&&StringUtils.isNotBlank(overdueDate)){
						if(TimeUtils.strToSqlDate(productionDate).getTime()>TimeUtils.strToSqlDate(overdueDate).getTime()){
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行生产日期大于有效期至");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

					//获取仓库信息
					Map<String ,Object> depotMap=new HashMap<String ,Object>();
					
					depotMap.put("depotCode", depotCode);
					DepotInfoMongo 	 depotInfoMongo	=depotInfoMongoDao.findOne(depotMap);
					if(depotInfoMongo == null){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行仓库编码不存在");
						msgList.add(msg);
						failure += 1;
						continue;
			    	}
					// 根据商家仓库查询商家仓库配置表
					Map<String, Object> dMerRelParams =new HashMap<String, Object>();
					dMerRelParams.put("merchantId", merchantId);
					dMerRelParams.put("depotId", depotInfoMongo.getDepotId());
					DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
					if(depotMerchantRel == null){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg","第"+j+"没有查到对应的商家仓配置表信息");
						msgList.add(msg);
						failure += 1;
						continue;
			    	}
					Map<String, Object> merchantBuRelParams =new HashMap<String, Object>();
					merchantBuRelParams.put("merchantId", merchantId);
					merchantBuRelParams.put("buCode", buCode);
					List<MerchantBuRelMongo> merchantBuRelList = merchantBuRelMongoDao.findAll(merchantBuRelParams);
					if (merchantBuRelList.size()==0) {
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"没有查询到该商家下的事业部");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if (merchantBuRelList.size()>1) {
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"该商家下查询到多条事业部");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelList.get(0);
		
					//根据商家和仓库 查询期初库存
					InitInventoryModel initInventoryModel =new InitInventoryModel();
					initInventoryModel.setMerchantId(merchantId);
					initInventoryModel.setDepotId(depotInfoMongo.getDepotId());
					List<InitInventoryModel> initInventoryList = initInventoryDao.list(initInventoryModel);
					if (initInventoryList.size()>0) {
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行仓库编码:"+depotCode+"该仓库期初已存在不能导入");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					
					if ("1".equals(depotInfoMongo.getBatchValidation())||"2".equals(depotInfoMongo.getBatchValidation())) {
						if (StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)||StringUtils.isBlank(batchNo)) {
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行仓库编码:"+depotCode+"设置了批次效期强校验,批次效期不能为空");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						
					}
					// 海外仓理货单位必填校验
					String unit = map.get("理货单位");
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
						if (StringUtils.isBlank(unit)) {
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行 海外仓理货单位不能为空");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						if (!("0".equals(unit)||"1".equals(unit)||"2".equals(unit))) {
							msg.put("row", String.valueOf(i + 1));
							msg.put("msg", "第"+j+"行 海外仓理货单位值不对");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						
					}
					//获取商品信息
					int count=0;
					Map<String ,Object> merchandiseInfoMap=new HashMap<String ,Object>();
					merchandiseInfoMap.put("goodsNo", goodsNo);
					merchandiseInfoMap.put("merchantId", merchantId);
					/**选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制 */
					if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
						merchandiseInfoMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);// 是否备案(1-是，0-否)
					}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())){						
						merchandiseInfoMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);//外仓唯一码 0-是 1-否
					}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_3.equals(depotMerchantRel.getProductRestriction())){
						// 无限制
					}
					
					MerchandiseInfoMogo 	merchandiseInfoMogo=merchandiseInfoMogoDao.findOne(merchandiseInfoMap);				
					if(merchandiseInfoMogo == null){
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", "第"+j+"行商品货号数据不存在");
						msgList.add(msg);
						failure += 1;
						continue;
			    	}
					
	
					//注入数据
					InitInventoryVo model = new InitInventoryVo();
					model.setBuId(merchantBuRelMongo.getBuId());
					model.setBuName(merchantBuRelMongo.getBuName());
					model.setMerchantId(merchantId);
					model.setMerchantName(merchantName);
					model.setDepotId(depotInfoMongo.getDepotId());
					model.setDepotCode(depotInfoMongo.getCode());
					model.setIsTopBooks(depotInfoMongo.getIsTopBooks());
					model.setDepotName(depotInfoMongo.getName());
					model.setDepotType(depotInfoMongo.getType());
					model.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
					model.setGoodsNo(goodsNo);
					model.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
					model.setGoodsName(merchandiseInfoMogo.getName());
					model.setBatchNo(batchNo);
					model.setBarcode(merchandiseInfoMogo.getBarcode());
					model.setProductionDate(productionDate);
					model.setOverdueDate(overdueDate);
					model.setType(type);
					model.setBarcode(merchandiseInfoMogo.getBarcode());
					model.setCommbarcode(merchandiseInfoMogo.getCommbarcode());// 标准条码 
					if(StringUtils.isNotBlank(initNum)){
						model.setInitNum(Integer.parseInt(initNum));
					}
					model.setCreater(userId);
					model.setTopidealCode(topidealCode);
					model.setSource("1");
					//理货单位 0 托盘 1箱  2 件
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {						
						model.setUnit(unit);
					}
					
					initInventoryVolList.add(model);
				}catch(Exception e){
					e.printStackTrace();
					failure+=1;
				}
			}
		}

		Map map =new HashMap();
		map.put("msgList",msgList);
		map.put("list",initInventoryVolList);
		map.put("success",initInventoryVolList.size());
		map.put("failure",failure);

		return map;
	}
    

    /**
     * 导入库存信息
     */
	@Override
	public Map importInitInventory(Map<Integer, List<List<Object>>> data,Long userId,Long merchantId,String merchantName,String topidealCode) throws Exception{
		int failure = 0;
		String errorMessage="";
		List<InitInventoryVo> initInventoryVolList=new ArrayList<InitInventoryVo>();
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
				try{
					//必填字段的校验
					String depotCode = map.get("仓库自编码");
					if(depotCode == null || "".equals(depotCode)){
						failure+=1;
						errorMessage+="第"+j+"行仓库自编码为空";
						continue;
					}
					String buCode = map.get("事业部编码");
					if(buCode == null || "".equals(buCode)){
						failure+=1;
						errorMessage+="第"+j+"事业部编码为空";
						continue;
					}
					
					String goodsNo = map.get("商品货号");
					if(goodsNo == null || "".equals(goodsNo)){
						failure+=1;
						errorMessage+="第"+j+"行商品货号为空";
						continue;
					}
					
					String type = map.get("库存类型");
					if(type == null || "".equals(type)){
						failure+=1;
						errorMessage+="第"+j+"行库存类型为空";
						continue;
					}else{
						if("0".equals(type)||"1".equals(type)){
							
						}else{
							failure+=1;
							errorMessage+="第"+j+"行库存类型填写错误";
							continue;
						}
					}
					String batchNo = map.get("批次号");		
					String initNum = map.get("库存数量");

					String productionDate = map.get("生产日期");
					if(StringUtils.isNotBlank(productionDate)){
						if(!this.isValidDate(productionDate)){
							failure+=1;
							errorMessage+="第"+j+"行生产日期格式填写错误";
							continue;
						}
					}

					String overdueDate = map.get("有效期至");
					if(StringUtils.isNotBlank(overdueDate)){
						if(!this.isValidDate(overdueDate)){
							failure+=1;
							errorMessage+="第"+j+"行有效期至格式填写错误";
							continue;
						}
					}
					
					if(StringUtils.isNotBlank(productionDate)&&StringUtils.isNotBlank(overdueDate)){
						if(TimeUtils.strToSqlDate(productionDate).getTime()>TimeUtils.strToSqlDate(overdueDate).getTime()){
							failure+=1;
							errorMessage+="第"+j+"行生产日期大于有效期至";
							continue;
						}
					}

					//获取仓库信息
					Map<String ,Object> depotMap=new HashMap<String ,Object>();
					
					depotMap.put("depotCode", depotCode);
					DepotInfoMongo 	 depotInfoMongo	=depotInfoMongoDao.findOne(depotMap);
					if(depotInfoMongo == null){
						failure+=1;
						errorMessage+="第"+j+"行仓库编码不存在";
						continue;
			    	}
					// 根据商家仓库查询商家仓库配置表
					Map<String, Object> dMerRelParams =new HashMap<String, Object>();
					dMerRelParams.put("merchantId", merchantId);
					dMerRelParams.put("depotId", depotInfoMongo.getDepotId());
					DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(dMerRelParams);
					if(depotMerchantRel == null){
						failure+=1;
						errorMessage+="第"+j+"没有查到对应的商家仓配置表信息";
						continue;
			    	}
					Map<String, Object> merchantBuRelParams =new HashMap<String, Object>();
					merchantBuRelParams.put("merchantId", merchantId);
					merchantBuRelParams.put("buCode", buCode);
					List<MerchantBuRelMongo> merchantBuRelList = merchantBuRelMongoDao.findAll(merchantBuRelParams);
					if (merchantBuRelList.size()==0) {
						failure+=1;
						errorMessage+="第"+j+"没有查询到该商家下的事业部";
						continue;
					}
					if (merchantBuRelList.size()>1) {
						failure+=1;
						errorMessage+="第"+j+"该商家下查询到多条事业部";
						continue;
					}
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelList.get(0);
		
					//根据商家和仓库 查询期初库存
					InitInventoryModel initInventoryModel =new InitInventoryModel();
					initInventoryModel.setMerchantId(merchantId);
					initInventoryModel.setDepotId(depotInfoMongo.getDepotId());
					List<InitInventoryModel> initInventoryList = initInventoryDao.list(initInventoryModel);
					if (initInventoryList.size()>0) {
						failure+=1;
						errorMessage+="第"+j+"行仓库编码:"+depotCode+"该仓库期初已存在不能导入";
						continue;
					}
					
					if ("1".equals(depotInfoMongo.getBatchValidation())||"2".equals(depotInfoMongo.getBatchValidation())) {
						if (StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)||StringUtils.isBlank(batchNo)) {
							failure+=1;
							errorMessage+="第"+j+"行仓库编码:"+depotCode+"设置了批次效期强校验,批次效期不能为空";
							continue;
						}
						
					}
					// 海外仓理货单位必填校验
					String unit = map.get("理货单位");
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
						if (StringUtils.isBlank(unit)) {
							failure+=1;
							errorMessage+="第"+j+"行 海外仓理货单位不能为空";
							continue;
						}
						if (!("0".equals(unit)||"1".equals(unit)||"2".equals(unit))) {
							failure+=1;
							errorMessage+="第"+j+"行 海外仓理货单位值不对";
							continue;
						}
						
					}
					//获取商品信息
					int count=0;
					Map<String ,Object> merchandiseInfoMap=new HashMap<String ,Object>();
					merchandiseInfoMap.put("goodsNo", goodsNo);
					merchandiseInfoMap.put("merchantId", merchantId);
					/**选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制 */
					if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
						merchandiseInfoMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);// 是否备案(1-是，0-否)
					}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())){						
						merchandiseInfoMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);//外仓唯一码 0-是 1-否
					}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_3.equals(depotMerchantRel.getProductRestriction())){
						// 无限制
					}
					
					MerchandiseInfoMogo 	merchandiseInfoMogo=merchandiseInfoMogoDao.findOne(merchandiseInfoMap);				
					if(merchandiseInfoMogo == null){
						failure+=1;
						errorMessage+="第"+j+"行商品货号数据不存在";
						continue;
			    	}
					
	
					//注入数据
					InitInventoryVo model = new InitInventoryVo();
					model.setBuId(merchantBuRelMongo.getBuId());
					model.setBuName(merchantBuRelMongo.getBuName());
					model.setMerchantId(merchantId);
					model.setMerchantName(merchantName);
					model.setDepotId(depotInfoMongo.getDepotId());
					model.setDepotCode(depotInfoMongo.getCode());
					model.setIsTopBooks(depotInfoMongo.getIsTopBooks());
					model.setDepotName(depotInfoMongo.getName());
					model.setDepotType(depotInfoMongo.getType());
					model.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
					model.setGoodsNo(goodsNo);
					model.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
					model.setGoodsName(merchandiseInfoMogo.getName());
					model.setBatchNo(batchNo);
					model.setBarcode(merchandiseInfoMogo.getBarcode());
					model.setProductionDate(productionDate);
					model.setOverdueDate(overdueDate);
					model.setType(type);
					model.setBarcode(merchandiseInfoMogo.getBarcode());
					model.setCommbarcode(merchandiseInfoMogo.getCommbarcode());// 标准条码 
					if(StringUtils.isNotBlank(initNum)){
						model.setInitNum(Integer.parseInt(initNum));
					}
					model.setCreater(userId);
					model.setTopidealCode(topidealCode);
					model.setSource("1");
					//理货单位 0 托盘 1箱  2 件
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {						
						model.setUnit(unit);
					}
					
					initInventoryVolList.add(model);
				}catch(Exception e){
					e.printStackTrace();
					failure+=1;
				}
			}
		}
        if(StringUtils.isBlank(errorMessage)){
        	errorMessage+="导入成功";
		}
		Map map =new HashMap();
		map.put("success",initInventoryVolList.size());
		map.put("list",initInventoryVolList);
		map.put("failure",failure);
		map.put("errorMessage",errorMessage);
		
		return map;
	}
	
	/**
     * 把两个数组组成一个map
     * @param keys   键数组
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

	
	@Override
	public void confirmInitInventory() throws SQLException{
	//	initInventoryDao.confirminitInventory();
		
	}

	
	
	/**
	 * 批量审核期初
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public boolean auditInitInventory(List<Long> list) throws SQLException {
		int num = 0;
		List<InventoryBatchModel> batchModelList=null;
		InventoryBatchModel invBatchModel=null;
		for (Long id : list) {
			// 根据id获取信息
			InitInventoryModel initInventoryModel = initInventoryDao.searchById(id);
			// 判断状态是否为 待审核 '1、未确认  2、已确认'
			if (DERP_INVENTORY.INITINVENTORY_STATE_1.equals(initInventoryModel.getState())) {
				
				InventoryBatchModel  parmtBatchModel=new InventoryBatchModel();
				parmtBatchModel.setMerchantId(initInventoryModel.getMerchantId());
/*				parmtBatchModel.setTopidealCode(initInventoryModel.getTopidealCode());
				parmtBatchModel.setMerchantName(initInventoryModel.getMerchantName());*/
				parmtBatchModel.setDepotId(initInventoryModel.getDepotId());
				parmtBatchModel.setGoodsId(initInventoryModel.getGoodsId());
				//parmtBatchModel.setGoodsNo(initInventoryModel.getGoodsNo());
				if(StringUtils.isNotBlank(initInventoryModel.getBatchNo())){
					parmtBatchModel.setBatchNo(initInventoryModel.getBatchNo());
				}
				if(initInventoryModel.getProductionDate()!=null){
					parmtBatchModel.setProductionDate(initInventoryModel.getProductionDate());
				}
				if(initInventoryModel.getOverdueDate()!=null){
					parmtBatchModel.setOverdueDate(initInventoryModel.getOverdueDate());
				}
				if(StringUtils.isNotBlank(initInventoryModel.getType())){
					parmtBatchModel.setType(initInventoryModel.getType());
				}else{
					parmtBatchModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//库存类型 0 好品  1 坏品
				}
			//	parmtBatchModel.setSurplusNum(initInventoryModel.getInitNum());
				invBatchModel=inventoryBatchDao.getInventoryBatchModelMapByOne(parmtBatchModel);
				if(invBatchModel!=null){
						//invBatchModel=	batchModelList.get(0);
					    Map<String,Object>  parMap=new HashMap<String,Object>();
						parMap.put("onWayNum", initInventoryModel.getInitNum());
						parMap.put("id", invBatchModel.getId());
				        int saveNum=  inventoryBatchDao.updateAddGoodsNum(parMap);
					    if(saveNum>0){
					    	initInventoryModel.setState(DERP_INVENTORY.INITINVENTORY_STATE_2);// 已审核
							num += initInventoryDao.modify(initInventoryModel);
					    }
				}else{
					InventoryBatchModel  inventoryBatchModel=new InventoryBatchModel();
					inventoryBatchModel.setMerchantId(initInventoryModel.getMerchantId());
					inventoryBatchModel.setTopidealCode(initInventoryModel.getTopidealCode());
					inventoryBatchModel.setMerchantName(initInventoryModel.getMerchantName());
					inventoryBatchModel.setDepotId(initInventoryModel.getDepotId());
					inventoryBatchModel.setDepotCode(initInventoryModel.getDepotCode());
					inventoryBatchModel.setDepotType(initInventoryModel.getDepotType());
					inventoryBatchModel.setDepotName(initInventoryModel.getDepotName());
					inventoryBatchModel.setIsTopBooks(initInventoryModel.getIsTopBooks());
					inventoryBatchModel.setGoodsId(initInventoryModel.getGoodsId());
					inventoryBatchModel.setGoodsName(initInventoryModel.getGoodsName());
					inventoryBatchModel.setGoodsNo(initInventoryModel.getGoodsNo());
					inventoryBatchModel.setBatchNo(initInventoryModel.getBatchNo());
					inventoryBatchModel.setBarcode(initInventoryModel.getBarcode());
					inventoryBatchModel.setProductionDate(initInventoryModel.getProductionDate());
					inventoryBatchModel.setOverdueDate(initInventoryModel.getOverdueDate());
					inventoryBatchModel.setSurplusNum(initInventoryModel.getInitNum());
					inventoryBatchModel.setType(initInventoryModel.getType());
					if(inventoryBatchModel.getOverdueDate()!=null){
					    Date date=new Date();
						if(inventoryBatchModel.getOverdueDate().getTime()>=date.getTime()){
							inventoryBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
						}else{
							inventoryBatchModel.setIsExpire(DERP.ISEXPIRE_0);//已过期
						}
					}else{
						inventoryBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
					}
					inventoryBatchModel.setCreateDate(TimeUtils.getNow());
					inventoryBatchModel.setCreater(initInventoryModel.getCreater());
					inventoryBatchModel.setUnit(initInventoryModel.getUnit());
					inventoryBatchModel.setLpn(initInventoryModel.getLpn());
					inventoryBatchModel.setTopidealCode(initInventoryModel.getTopidealCode());
					//查询商品
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("merchandiseId", initInventoryModel.getGoodsId());
					MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(paramMap);
					
					//查询品牌
					BrandMongo brandMongo = null;
					if(merchandiseMogo!=null && merchandiseMogo.getBrandId()!=null){
						paramMap = new HashMap<String, Object>();
						paramMap.put("brandId", merchandiseMogo.getBrandId());
						brandMongo = brandMongoDao.findOne(paramMap);
					}
					if(brandMongo!=null){
						inventoryBatchModel.setBrandId(brandMongo.getBrandId());
						inventoryBatchModel.setBrandName(brandMongo.getName());
					}
					Long ids=inventoryBatchDao.save(inventoryBatchModel);
				    if(ids!=null){
						initInventoryModel.setState(DERP_INVENTORY.INITINVENTORY_STATE_2);// 已审核
						num += initInventoryDao.modify(initInventoryModel);
					}
				}
			}
		}
		if (num == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 确认和保存期初
	 * @param jsonObj
	 * @return
	 * @throws SQLException
	 */
	
	@Override
	public Map saveInitInventory(String jsonObj) throws SQLException {
		// TODO Auto-generated method stub

		List<InitInventoryModel> initList=new ArrayList<>();//存储期初
		List<InventoryBatchModel>saveInventoryList=new ArrayList<>();//新增批次库存
		List<Map<String, Object>> updeteInventoryMapList=new ArrayList<>();//修改

		JSONArray json = JSONArray.fromObject(jsonObj);
		List<InitInventoryVo> list = JSONArray.toList(json, new InitInventoryVo(), new JsonConfig());
		if (list != null && list.size() > 0) {
			for (InitInventoryVo vo : list) {
				InitInventoryModel model = new InitInventoryModel();
				model.setBuId(vo.getBuId());
				model.setBuName(vo.getBuName());
				model.setMerchantId(vo.getMerchantId());
				model.setMerchantName(vo.getMerchantName());
				model.setDepotId(vo.getDepotId());
				model.setDepotCode(vo.getDepotCode());
				model.setDepotName(vo.getDepotName());
				model.setDepotType(vo.getDepotType());
				// 海外仓必单位
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
					model.setUnit(vo.getUnit());
				}
				model.setIsTopBooks(vo.getIsTopBooks());
				model.setGoodsId(vo.getGoodsId());
				model.setGoodsNo(vo.getGoodsNo());
				model.setGoodsCode(vo.getGoodsCode());
				model.setBarcode(vo.getBarcode());
				model.setGoodsName(vo.getGoodsName());
				model.setBatchNo(vo.getBatchNo());
				if(StringUtils.isNotBlank(vo.getProductionDate())){
					model.setProductionDate(TimeUtils.strToSqlDate(vo.getProductionDate()));
				}
				if(StringUtils.isNotBlank(vo.getOverdueDate())){
					model.setOverdueDate(TimeUtils.strToSqlDate(vo.getOverdueDate()));
				}
				model.setType(vo.getType());
				model.setBarcode(vo.getBarcode());
				model.setInitNum(vo.getInitNum());
				model.setCreateDate(TimeUtils.getNow());
				model.setCreater(vo.getCreater());
				model.setTopidealCode(vo.getTopidealCode());
				model.setSource(DERP_INVENTORY.INITINVENTORY_SOURCE_1);
				model.setState(DERP_INVENTORY.INITINVENTORY_STATE_2);
				model.setBarcode(vo.getBarcode());
				model.setCommbarcode(vo.getCommbarcode());
				initList.add(model);// 新增期初的数据

				InventoryBatchModel inventoryBatchModel=new InventoryBatchModel();
				inventoryBatchModel.setMerchantId(model.getMerchantId());
				inventoryBatchModel.setDepotId(model.getDepotId());
				inventoryBatchModel.setGoodsId(model.getGoodsId());
				inventoryBatchModel.setGoodsNo(model.getGoodsNo());
				if(StringUtils.isNotBlank(model.getBatchNo())){
					inventoryBatchModel.setBatchNo(model.getBatchNo());
				}
				if(model.getProductionDate()!=null){
					inventoryBatchModel.setProductionDate(model.getProductionDate());
				}
				if(model.getOverdueDate()!=null){
					inventoryBatchModel.setOverdueDate(model.getOverdueDate());
				}
				if(StringUtils.isNotBlank(model.getType())){
					inventoryBatchModel.setType(model.getType());
				}else{
					inventoryBatchModel.setType(DERP_INVENTORY.INVENTORY_TYPE_0);//0 正常品  1 残次品
				}
				// 海外仓理货单位查询如果理货单位是空 默认是件
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
					inventoryBatchModel.setUnit(vo.getUnit());
				}
				inventoryBatchModel=inventoryBatchDao.getInventoryBatchModelMapByOne(inventoryBatchModel);
				if(inventoryBatchModel!=null){//说明在批次库存明细中只存在					
					Map<String, Object> parMap = new HashMap<String, Object>();						
					parMap.put("onWayNum", model.getInitNum());
					parMap.put("id", inventoryBatchModel.getId());						
					updeteInventoryMapList.add(parMap);	//修改批次库存的数据					
				}else{
					//查询商品
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("merchandiseId", model.getGoodsId());
					MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(paramMap);
					
					//查询品牌
					BrandMongo brandMongo = null;
					if(merchandiseMogo!=null && merchandiseMogo.getBrandId()!=null){
						paramMap = new HashMap<String, Object>();
						paramMap.put("brandId", merchandiseMogo.getBrandId());
						brandMongo = brandMongoDao.findOne(paramMap);
					}
					InventoryBatchModel inBatchModel = new InventoryBatchModel();
					inBatchModel.setMerchantId(model.getMerchantId());
					inBatchModel.setMerchantName(model.getMerchantName());
					inBatchModel.setDepotId(model.getDepotId());
					inBatchModel.setDepotCode(model.getDepotCode());
					inBatchModel.setDepotName(model.getDepotName());
					inBatchModel.setDepotType(model.getDepotType());
					inBatchModel.setIsTopBooks(model.getIsTopBooks());
					inBatchModel.setGoodsId(model.getGoodsId());
					inBatchModel.setGoodsNo(model.getGoodsNo());
					inBatchModel.setGoodsName(model.getGoodsName());
					inBatchModel.setBatchNo(model.getBatchNo());
					inBatchModel.setBarcode(model.getBarcode());
					inBatchModel.setCommbarcode(model.getCommbarcode());
					inBatchModel.setOverdueDate(model.getOverdueDate());
					inBatchModel.setProductionDate(model.getProductionDate());
					inBatchModel.setType(model.getType());
					inBatchModel.setCreateDate(TimeUtils.getNow());
					inBatchModel.setSurplusNum(model.getInitNum());
					inBatchModel.setTopidealCode(model.getTopidealCode());
					inBatchModel.setCreater(model.getCreater());
					if(inBatchModel.getOverdueDate()!=null){
						Date date=new Date();
						if(inBatchModel.getOverdueDate().getTime()>=date.getTime()){//导入的有效期和当前时间比较
							inBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
						}else{
							inBatchModel.setIsExpire(DERP.ISEXPIRE_0);//已过期
						}
					}else{
						inBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
					}
					// 海外仓理货单位查询如果理货单位是空 默认是件
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
						inBatchModel.setUnit(vo.getUnit());
					}
					if(brandMongo!=null){
						inBatchModel.setBrandId(brandMongo.getBrandId());
						inBatchModel.setBrandName(brandMongo.getName());
					}
					saveInventoryList.add(inBatchModel);												
				}				
			}
		}
		int initSuccess = 0;//期初新增成功
		int initFailure = 0;//期初新增失败
		int innventorySuccess = 0;//库存新增成功
		int innventoryFailure = 0;//库存新增失败
		int updteSuccess=0; // 库存修改成功
		int updteFailure=0;//库存修改失败
		//新增期初
		for (InitInventoryModel model : initList) {
			Long id = initInventoryDao.save(model);
			if (id!=null) {
				initSuccess=initSuccess+1;
			}else {
				initFailure=initFailure+1;
			}
		}
		// 新增批次库存
		for (InventoryBatchModel inBatchModel : saveInventoryList) {
			 Long batchId = inventoryBatchDao.save(inBatchModel);
			if (batchId!=null) {
				innventorySuccess=innventorySuccess+1;
			}else {
				innventoryFailure=innventoryFailure+1;
			}
		}
		//修改
		for (Map<String, Object> parMap : updeteInventoryMapList) {
			int num = inventoryBatchDao.updateAddGoodsNum(parMap);
			if (num>0) {
				updteSuccess=updteSuccess+1;
			}else {
				updteFailure=updteFailure+1;
			}
		}
	
		
		
		Map map =new HashMap();
		map.put("initSuccess",initSuccess);
		map.put("initFailure",initFailure);
		map.put("innventorySuccess",innventorySuccess);
		map.put("innventoryFailure",innventoryFailure);
		map.put("updteSuccess",updteSuccess);
		map.put("updteFailure",updteFailure);
		
		return  map;
	}
	
	/**
	 * 确认和保存期初
	 * @param jsonObj
	 * @return
	 * @throws SQLException
	 */
	
	@Override
	public Map saveInitInventory11(InitInventorySaveForm form) throws SQLException {
		// TODO Auto-generated method stub

		List<InitInventoryModel> initList=new ArrayList<>();//存储期初
		List<InventoryBatchModel>saveInventoryList=new ArrayList<>();//新增批次库存
		List<Map<String, Object>> updeteInventoryMapList=new ArrayList<>();//修改

		 List<InitInventoryVo> list = form.getList();
		if (list != null && list.size() > 0) {
			for (InitInventoryVo vo : list) {
				InitInventoryModel model = new InitInventoryModel();
				model.setBuId(vo.getBuId());
				model.setBuName(vo.getBuName());
				model.setMerchantId(vo.getMerchantId());
				model.setMerchantName(vo.getMerchantName());
				model.setDepotId(vo.getDepotId());
				model.setDepotCode(vo.getDepotCode());
				model.setDepotName(vo.getDepotName());
				model.setDepotType(vo.getDepotType());
				// 海外仓必单位
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
					model.setUnit(vo.getUnit());
				}
				model.setIsTopBooks(vo.getIsTopBooks());
				model.setGoodsId(vo.getGoodsId());
				model.setGoodsNo(vo.getGoodsNo());
				model.setGoodsCode(vo.getGoodsCode());
				model.setBarcode(vo.getBarcode());
				model.setGoodsName(vo.getGoodsName());
				model.setBatchNo(vo.getBatchNo());
				if(StringUtils.isNotBlank(vo.getProductionDate())){
					model.setProductionDate(TimeUtils.strToSqlDate(vo.getProductionDate()));
				}
				if(StringUtils.isNotBlank(vo.getOverdueDate())){
					model.setOverdueDate(TimeUtils.strToSqlDate(vo.getOverdueDate()));
				}
				model.setType(vo.getType());
				model.setBarcode(vo.getBarcode());
				model.setInitNum(vo.getInitNum());
				model.setCreateDate(TimeUtils.getNow());
				model.setCreater(vo.getCreater());
				model.setTopidealCode(vo.getTopidealCode());
				model.setSource(DERP_INVENTORY.INITINVENTORY_SOURCE_1);
				model.setState(DERP_INVENTORY.INITINVENTORY_STATE_2);
				model.setBarcode(vo.getBarcode());
				model.setCommbarcode(vo.getCommbarcode());
				initList.add(model);// 新增期初的数据

				InventoryBatchModel inventoryBatchModel=new InventoryBatchModel();
				inventoryBatchModel.setMerchantId(model.getMerchantId());
				inventoryBatchModel.setDepotId(model.getDepotId());
				inventoryBatchModel.setGoodsId(model.getGoodsId());
				inventoryBatchModel.setGoodsNo(model.getGoodsNo());
				if(StringUtils.isNotBlank(model.getBatchNo())){
					inventoryBatchModel.setBatchNo(model.getBatchNo());
				}
				if(model.getProductionDate()!=null){
					inventoryBatchModel.setProductionDate(model.getProductionDate());
				}
				if(model.getOverdueDate()!=null){
					inventoryBatchModel.setOverdueDate(model.getOverdueDate());
				}
				if(StringUtils.isNotBlank(model.getType())){
					inventoryBatchModel.setType(model.getType());
				}else{
					inventoryBatchModel.setType(DERP_INVENTORY.INVENTORY_TYPE_0);//0 正常品  1 残次品
				}
				// 海外仓理货单位查询如果理货单位是空 默认是件
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
					inventoryBatchModel.setUnit(vo.getUnit());
				}
				inventoryBatchModel=inventoryBatchDao.getInventoryBatchModelMapByOne(inventoryBatchModel);
				if(inventoryBatchModel!=null){//说明在批次库存明细中只存在					
					Map<String, Object> parMap = new HashMap<String, Object>();						
					parMap.put("onWayNum", model.getInitNum());
					parMap.put("id", inventoryBatchModel.getId());						
					updeteInventoryMapList.add(parMap);	//修改批次库存的数据					
				}else{
					//查询商品
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("merchandiseId", model.getGoodsId());
					MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(paramMap);
					
					//查询品牌
					BrandMongo brandMongo = null;
					if(merchandiseMogo!=null && merchandiseMogo.getBrandId()!=null){
						paramMap = new HashMap<String, Object>();
						paramMap.put("brandId", merchandiseMogo.getBrandId());
						brandMongo = brandMongoDao.findOne(paramMap);
					}
					InventoryBatchModel inBatchModel = new InventoryBatchModel();
					inBatchModel.setMerchantId(model.getMerchantId());
					inBatchModel.setMerchantName(model.getMerchantName());
					inBatchModel.setDepotId(model.getDepotId());
					inBatchModel.setDepotCode(model.getDepotCode());
					inBatchModel.setDepotName(model.getDepotName());
					inBatchModel.setDepotType(model.getDepotType());
					inBatchModel.setIsTopBooks(model.getIsTopBooks());
					inBatchModel.setGoodsId(model.getGoodsId());
					inBatchModel.setGoodsNo(model.getGoodsNo());
					inBatchModel.setGoodsName(model.getGoodsName());
					inBatchModel.setBatchNo(model.getBatchNo());
					inBatchModel.setBarcode(model.getBarcode());
					inBatchModel.setCommbarcode(model.getCommbarcode());
					inBatchModel.setOverdueDate(model.getOverdueDate());
					inBatchModel.setProductionDate(model.getProductionDate());
					inBatchModel.setType(model.getType());
					inBatchModel.setCreateDate(TimeUtils.getNow());
					inBatchModel.setSurplusNum(model.getInitNum());
					inBatchModel.setTopidealCode(model.getTopidealCode());
					inBatchModel.setCreater(model.getCreater());
					if(inBatchModel.getOverdueDate()!=null){
						Date date=new Date();
						if(inBatchModel.getOverdueDate().getTime()>=date.getTime()){//导入的有效期和当前时间比较
							inBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
						}else{
							inBatchModel.setIsExpire(DERP.ISEXPIRE_0);//已过期
						}
					}else{
						inBatchModel.setIsExpire(DERP.ISEXPIRE_1);//未过期
					}
					// 海外仓理货单位查询如果理货单位是空 默认是件
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(vo.getDepotType())) {
						inBatchModel.setUnit(vo.getUnit());
					}
					if(brandMongo!=null){
						inBatchModel.setBrandId(brandMongo.getBrandId());
						inBatchModel.setBrandName(brandMongo.getName());
					}
					saveInventoryList.add(inBatchModel);												
				}				
			}
		}
		int initSuccess = 0;//期初新增成功
		int initFailure = 0;//期初新增失败
		int innventorySuccess = 0;//库存新增成功
		int innventoryFailure = 0;//库存新增失败
		int updteSuccess=0; // 库存修改成功
		int updteFailure=0;//库存修改失败
		//新增期初
		for (InitInventoryModel model : initList) {
			Long id = initInventoryDao.save(model);
			if (id!=null) {
				initSuccess=initSuccess+1;
			}else {
				initFailure=initFailure+1;
			}
		}
		// 新增批次库存
		for (InventoryBatchModel inBatchModel : saveInventoryList) {
			 Long batchId = inventoryBatchDao.save(inBatchModel);
			if (batchId!=null) {
				innventorySuccess=innventorySuccess+1;
			}else {
				innventoryFailure=innventoryFailure+1;
			}
		}
		//修改
		for (Map<String, Object> parMap : updeteInventoryMapList) {
			int num = inventoryBatchDao.updateAddGoodsNum(parMap);
			if (num>0) {
				updteSuccess=updteSuccess+1;
			}else {
				updteFailure=updteFailure+1;
			}
		}
	
		
		
		Map map =new HashMap();
		map.put("initSuccess",initSuccess);
		map.put("initFailure",initFailure);
		map.put("innventorySuccess",innventorySuccess);
		map.put("innventoryFailure",innventoryFailure);
		map.put("updteSuccess",updteSuccess);
		map.put("updteFailure",updteFailure);
		
		return  map;
	}
	
	
	public  boolean isValidDate(String str) {
	      boolean convertSuccess=true;
	       // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       try {
	       // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
	       //   format.setLenient(false);
	          format.parse(str);
	       } catch (ParseException e) {
	          // e.printStackTrace();
	        // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	           convertSuccess=false;
	       } 
	       return convertSuccess;
	}

}
