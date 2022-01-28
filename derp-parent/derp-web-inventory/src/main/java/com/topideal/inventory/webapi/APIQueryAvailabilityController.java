package com.topideal.inventory.webapi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.QueryAvailabilityForm;
import com.topideal.inventory.webapi.form.QueryAvailabilityItem;
import com.topideal.inventory.webapi.form.QueryAvailabilityResponseDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 库存模块外部接口
 * @author baols
 * 2018/6/26
 */

@RestController
@RequestMapping("/webapi/inventory/queryAvailability")
@Api(tags = "库存模块外部接口")
public class APIQueryAvailabilityController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(APIQueryAvailabilityController.class);
	
	

	// 批次库存明细service
	@Autowired
	private InventoryBatchService inventoryBatchService;
	
	
	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;//mongdb仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;//商品

	
	
	
	
	
	/**
	 * 校验库存数据
	 * */
	@ApiOperation(value = "校验库存数据")
	@PostMapping(value="/checkInventoryNum.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<List<QueryAvailabilityResponseDTO>> saveCustomer(@RequestBody QueryAvailabilityForm form) {
				
	try {		
		User user = ShiroUtils.getUserByToken(form.getToken());
			
		List<QueryAvailabilityItem> itemList = form.getItemList();
		if (itemList==null||itemList.size()==0) {
			throw new RuntimeException("商品信息itemList不能为空");
		}
		Map<String, List<QueryAvailabilityItem> >inventoryTypeMap=new HashMap<String, List<QueryAvailabilityItem>>();

		// 不同仓库的信息拆分
		Map<String, List<QueryAvailabilityItem>>depotItemListMap=new  HashMap<String, List<QueryAvailabilityItem>>();
		for (QueryAvailabilityItem item : itemList) {			
			if (item.getDepotId()==null) {
				throw new RuntimeException("仓库id不能为空");
			}
			String inventoryType = item.getInventoryType();		
			if (StringUtils.isBlank(inventoryType)) throw new RuntimeException("查询库存类型不能为空");
			if (!("1".equals(inventoryType)||"2".equals(inventoryType))) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"查询库存类型只能为1,2");
			}
			String depotInventoryTypekey=item.getDepotId()+","+inventoryType;
			//根据仓库区分商品
			if (depotItemListMap.containsKey(depotInventoryTypekey)) {
				List<QueryAvailabilityItem> list = depotItemListMap.get(depotInventoryTypekey);
				list.add(item);
				depotItemListMap.put(depotInventoryTypekey, list);
			}else {
				List<QueryAvailabilityItem> list=new ArrayList<QueryAvailabilityItem>();
				list.add(item);
				depotItemListMap.put(depotInventoryTypekey, list);
			}
		}
		
		
		List<QueryAvailabilityResponseDTO> responseDTOALLList =new ArrayList<QueryAvailabilityResponseDTO>();
		Set<String> keySet = depotItemListMap.keySet();	
		for (String depotInventoryTypekey : keySet) {
			List<QueryAvailabilityItem> itemNewList = depotItemListMap.get(depotInventoryTypekey);
			String[] depotInventoryTypeArr = depotInventoryTypekey.split(",");
			Long depotId=Long.valueOf(depotInventoryTypeArr[0]);
			String inventoryType =depotInventoryTypeArr[1];
			//查询仓库
			Map<String, Object> depotInfoMap=new HashMap<>();
			depotInfoMap.put("depotId", depotId);			
			DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
			if (depotInfoMongo==null) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"根据仓库id:"+depotId+"没有查询到仓库信息");
			}
			// 传输信息数据校验		
			checkdata(itemNewList,depotInfoMongo,inventoryType);
			
			if ("1".equals(inventoryType)) {	
				List<QueryAvailabilityResponseDTO>responseDTOList = checkSurplusNum(user, depotInfoMongo,itemNewList);
				if (responseDTOList.size()>0) responseDTOALLList.addAll(responseDTOList);
			}else if ("2".equals(inventoryType)) {//查询可用量
				List<QueryAvailabilityResponseDTO>responseDTOList =checkAvailableNum(user, depotInfoMongo,itemNewList);
				if (responseDTOList.size()>0) responseDTOALLList.addAll(responseDTOList);
			}
			
		}
		

		// 查询库存余量
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTOALLList);//成功
	} catch (Exception e) {
		e.printStackTrace();
		return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
	}
		
		
	}
	
	/**
	 * 可用量查寻接口
	 * @param user
	 * @param depotInfoMongo
	 * @param itemNewList
	 * @return
	 * @throws Exception 
	 */
	private List<QueryAvailabilityResponseDTO> checkAvailableNum(User user, DepotInfoMongo depotInfoMongo,List<QueryAvailabilityItem> itemNewList) throws Exception {
		Map<String, Map<String, Object>> goodsMap=new  HashMap<String, Map<String,Object>>();		
		// 相同商品  单位  是否过期 好坏品
		for (QueryAvailabilityItem item : itemNewList) {
			Long goodsId = item.getGoodsId();
			String goodsNo = item.getGoodsNo();
			Integer okNum = item.getOkNum();
			
			
			String unit ="";
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
				unit = item.getTallyingUnit();
				if (DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_2;
				}
				if (DERP.ORDER_TALLYINGUNIT_01.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_1;
				}
				if (DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_0;
				}
			}
			String isExpire=DERP.ISEXPIRE_1;//是否过期  （0 是 1 否）
			String overdueDate = item.getOverdueDate();	
			if (StringUtils.isNotBlank(overdueDate)) {
				isExpire = TimeUtils.timeComparisonSize(TimeUtils.StringToTimestamp(overdueDate),TimeUtils.getNow());
			}
			if (okNum!=null&&okNum.intValue()>0) {
				
				String type=DERP_INVENTORY.INVENTORY_TYPE_0;// 0-正常品  1-残次品
				String goodsKey=goodsId+","+type+","+unit+isExpire;
				if (goodsMap.containsKey(goodsKey)) {
					Map<String, Object> map = goodsMap.get(goodsKey);
					int numMap = (int) map.get("num");
					map.put("num", okNum.intValue()+numMap);
					goodsMap.put(goodsKey, map);
				}else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("num", okNum.intValue());
					map.put("goodsId", goodsId);
					map.put("type", type);
					map.put("unit", unit);
					map.put("isExpire", isExpire);
					map.put("goodsNo", goodsNo);
					goodsMap.put(goodsKey, map);
				}
			}
					
		}
		List<QueryAvailabilityResponseDTO>responseDTOList=new ArrayList<QueryAvailabilityResponseDTO>();
		// 校验库存余量
		Set<String> keySet = goodsMap.keySet();
		for (String goodsKey : keySet) {
			Map<String, Object> map = goodsMap.get(goodsKey);
			int num = (int) map.get("num");
			String goodsNo = (String) map.get("goodsNo");
			Long goodsId=(Long)map.get("goodsId");
			String unit=(String)map.get("unit");
			
			List<InventoryBatchModel> inventoryBatchModelList = inventoryBatchService.getAvailableNum(user.getMerchantId(),depotInfoMongo.getDepotId(),goodsId,unit);
			int surplusNum=0;
			QueryAvailabilityResponseDTO responseDTO=new QueryAvailabilityResponseDTO();
			responseDTO.setDepotName(depotInfoMongo.getName());
			responseDTO.setGoodsNo(goodsNo);
			String unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, (String)map.get("unit"));
			responseDTO.setUnitLabel(unitLabel);
			String isExpireLabel = DERP.getLabelByKey(DERP.isExpireList, (String)map.get("isExpire"));
			responseDTO.setIsExpireLabel(isExpireLabel);			
			String typeLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, (String)map.get("type"));
			responseDTO.setTypeLabel(typeLabel);	
			responseDTO.setNum(num);
			
			responseDTO.setBatchNo((String)map.get("batchNo"));
			
			if (inventoryBatchModelList.size()==0) {
				responseDTO.setMassage("没有查询到库存可用量");
				responseDTOList.add(responseDTO);
				continue;
			}
			InventoryBatchModel inventBatchModel=	inventoryBatchModelList.get(0);
			surplusNum = inventBatchModel.getAvailableNum();
			responseDTO.setSurplusNum(surplusNum);
			if (num>surplusNum) {
				responseDTO.setMassage("出库量大于库存可用量");
				responseDTOList.add(responseDTO);
				continue;
			}
		}
		return responseDTOList;
		
	
	}


	/**
	 * 校验数据
	 * @param form
	 */
	private void checkdata(List<QueryAvailabilityItem> itemNewList,DepotInfoMongo depotInfoMongo,String inventoryType) throws Exception {

		for (QueryAvailabilityItem item : itemNewList) {
			if (item.getGoodsId()==null) {
				throw new RuntimeException("商品goodsId不能为空");
			}
			if (StringUtils.isBlank(item.getGoodsNo())) {
				throw new RuntimeException("商品goodsNo不能为空");
			}			
			if (item.getOkNum()==null&&item.getBadNum()==null&&item.getExpireNum()==null) {
				throw new RuntimeException("商品好品出库数量okNum,坏品出库badNum,过期品expireNum不能同时为空");
			}
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
				if (StringUtils.isBlank(item.getTallyingUnit())) {
					throw new RuntimeException("商品理货单位unit：不能为空");
				}
				if (!(DERP.ORDER_TALLYINGUNIT_02.equals(item.getTallyingUnit())||
						DERP.ORDER_TALLYINGUNIT_01.equals(item.getTallyingUnit())||
						DERP.ORDER_TALLYINGUNIT_00.equals(item.getTallyingUnit()))) {
					throw new RuntimeException("商品理货单位unit");
				}
			}
			//余量类型才校验是批次效期
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())&&"1".equals(inventoryType)) {
				if (StringUtils.isBlank(item.getBatchNo())) {
					throw new RuntimeException("商品批次batchNo：不能为空");
				}
				if (StringUtils.isBlank(item.getProductionDate())) {
					throw new RuntimeException("商品生产日期productionDate：不能为空");
				}
				if (StringUtils.isBlank(item.getOverdueDate())) {
					throw new RuntimeException("商品失效日期overdueDate：不能为空");
				}				
				
			}
			
			
			
		}

		
	}



	/**
	 * 查询库存余量
	 * @param form
	 * @param depotInfoMongo
	 * @throws SQLException 
	 */
	private List<QueryAvailabilityResponseDTO> checkSurplusNum(User user,DepotInfoMongo depotInfoMongo ,List<QueryAvailabilityItem> itemNewList) throws SQLException {
					
		Map<String, Map<String, Object>> goodsMap=new  HashMap<String, Map<String,Object>>();		
		// 相同商品  单位  是否过期 好坏品
		for (QueryAvailabilityItem item : itemNewList) {
			Long goodsId = item.getGoodsId();
			String goodsNo = item.getGoodsNo();
			Integer okNum = item.getOkNum();
			if (okNum!=null)okNum=Math.abs(okNum);
			Integer badNum = item.getBadNum();
			if (badNum!=null)badNum=Math.abs(badNum);
			Integer expireNum = item.getExpireNum();
			if (expireNum!=null)expireNum=Math.abs(expireNum);
			String unit ="";
			String batchNo="";
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
				unit = item.getTallyingUnit();
				if (DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_2;
				}
				if (DERP.ORDER_TALLYINGUNIT_01.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_1;
				}
				if (DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
					unit=DERP.INVENTORY_UNIT_0;
				}
			}
			String isExpire=DERP.ISEXPIRE_1;//是否过期  （0 是 1 否）
			//批次效期强校验
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())) {	
				batchNo=item.getBatchNo();							
			}
			String overdueDate = item.getOverdueDate();	
			if (StringUtils.isNotBlank(overdueDate)) {
				isExpire = TimeUtils.timeComparisonSize(TimeUtils.StringToTimestamp(overdueDate),TimeUtils.getNow());
			}
			if (okNum!=null&&okNum.intValue()>0) {
				
				String type=DERP_INVENTORY.INVENTORY_TYPE_0;// 0-正常品  1-残次品
				String goodsKey=goodsId+","+type+","+unit+isExpire+batchNo;
				if (goodsMap.containsKey(goodsKey)) {
					Map<String, Object> map = goodsMap.get(goodsKey);
					int numMap = (int) map.get("num");
					map.put("num", okNum.intValue()+numMap);
					goodsMap.put(goodsKey, map);
				}else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("num", okNum.intValue());
					map.put("goodsId", goodsId);
					map.put("type", type);
					map.put("unit", unit);
					map.put("isExpire", isExpire);
					map.put("batchNo", batchNo);
					map.put("goodsNo", goodsNo);
					goodsMap.put(goodsKey, map);
				}
			}
			
			
			
			// 商品效期合并处理
			if (badNum!=null&&badNum.intValue()>0) {
				
				String type=DERP_INVENTORY.INVENTORY_TYPE_1;// 0-正常品  1-残次品
				String goodsKey=goodsId+","+type+","+unit+isExpire+batchNo;
				if (goodsMap.containsKey(goodsKey)) {
					Map<String, Object> map = goodsMap.get(goodsKey);
					int numMap = (int) map.get("num");
					map.put("num", badNum.intValue()+numMap);
					goodsMap.put(goodsKey, map);
				}else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("num", badNum.intValue());
					map.put("goodsId", goodsId);
					map.put("type", type);
					map.put("unit", unit);
					map.put("isExpire", isExpire);
					map.put("batchNo", batchNo);
					map.put("goodsNo", goodsNo);
					goodsMap.put(goodsKey, map);
				}	
			}
			// 商品效期合并处理
			if (expireNum!=null&&expireNum.intValue()>0) {
				isExpire=DERP.ISEXPIRE_0;//是否过期  （0 是 1 否）
				String type=DERP_INVENTORY.INVENTORY_TYPE_1;// 0-正常品  1-残次品
				String goodsKey=goodsId+","+type+","+unit+isExpire+batchNo;
				if (goodsMap.containsKey(goodsKey)) {
					Map<String, Object> map = goodsMap.get(goodsKey);
					int numMap = (int) map.get("num");
					map.put("num", badNum.intValue()+numMap);
					goodsMap.put(goodsKey, map);
				}else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("num", badNum.intValue());
					map.put("goodsId", goodsId);
					map.put("type", type);
					map.put("unit", unit);
					map.put("isExpire", isExpire);
					map.put("batchNo", batchNo);
					map.put("goodsNo", goodsNo);
					goodsMap.put(goodsKey, map);
				}	
			}								
		}
		List<QueryAvailabilityResponseDTO>responseDTOList=new ArrayList<QueryAvailabilityResponseDTO>();
		// 校验库存余量
		Set<String> keySet = goodsMap.keySet();
		for (String goodsKey : keySet) {
			Map<String, Object> map = goodsMap.get(goodsKey);
			int num = (int) map.get("num");
			String goodsNo = (String) map.get("goodsNo");
			InventoryBatchModel inventoryBatch=new InventoryBatchModel();
			inventoryBatch.setMerchantId(user.getMerchantId());
			inventoryBatch.setDepotId(depotInfoMongo.getDepotId());
			inventoryBatch.setGoodsId((Long)map.get("goodsId"));
			inventoryBatch.setUnit((String)map.get("unit"));
			inventoryBatch.setType((String)map.get("type"));
			inventoryBatch.setBatchNo((String)map.get("batchNo"));
			inventoryBatch.setIsExpire((String)map.get("isExpire"));
			List<InventoryBatchModel> inventoryBatchList = inventoryBatchService.getList(inventoryBatch);
			int surplusNum=0;
			for (InventoryBatchModel inventory : inventoryBatchList) {
				surplusNum=surplusNum+inventory.getSurplusNum().intValue();
			}
			QueryAvailabilityResponseDTO responseDTO=new QueryAvailabilityResponseDTO();
			responseDTO.setDepotName(depotInfoMongo.getName());
			responseDTO.setGoodsNo(goodsNo);
			String unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, (String)map.get("unit"));
			responseDTO.setUnitLabel(unitLabel);
			String isExpireLabel = DERP.getLabelByKey(DERP.isExpireList, (String)map.get("isExpire"));
			responseDTO.setIsExpireLabel(isExpireLabel);			
			String typeLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, (String)map.get("type"));
			responseDTO.setTypeLabel(typeLabel);	
			responseDTO.setNum(num);
			responseDTO.setSurplusNum(surplusNum);
			responseDTO.setBatchNo((String)map.get("batchNo"));
			if (inventoryBatchList.size()==0) {				
				responseDTO.setMassage("没有查询到库存余量");
				responseDTOList.add(responseDTO);
				continue;
			}
			if (num>surplusNum) {
				responseDTO.setMassage("出库量大于库存余量");
				responseDTOList.add(responseDTO);
				continue;
			}
			
		}
		return responseDTOList;
		
		
		
		
		
		
	}




	/**
	 * 库存可用量接口
	 * @param session
	 * @param depotId
	 * @param goodsId
	 * @return
	 */
	@ApiOperation(value = "库存可用量接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "goodsId", value = "商品id"),
			@ApiImplicitParam(name = "depotCode", value = "仓库编码"),
			@ApiImplicitParam(name = "unit", value = "单位（ 0 托盘 1箱  2件）"),
			@ApiImplicitParam(name = "type", value = "好坏品 0 正常品  1 残次品'"),
			@ApiImplicitParam(name = "isExpire", value = "是否过期（0 过期 1 未过期）"),
			@ApiImplicitParam(name = "batchNo", value = "批次")
	})
	@PostMapping(value="/getAvailableNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getAvailableNum (HttpSession session,String token,Long depotId,Long goodsId,String depotCode,String unit,String type,String isExpire,String batchNo){
		LOGGER.info("==============库存可用量接口=============开始");
		LOGGER.info("==============库存可用量接口=============开始"+"depotId:"+depotId+"goodsId:"+goodsId+"depotCode:"+depotCode+"unit"+ unit+"type:"+ type+"isExpire:"+ isExpire+"batchNo:"+ batchNo);
		String messgae="";
		List<InventoryBatchModel> inventoryBatchModelList=null;
		try {
			User user = ShiroUtils.getUserByToken(token);
			// 添加了3个字段 type (0 正常品  1 残次品)  isExpire(0 过期 1 未过期) batchNo 对应仓储库存调整单其他出和月结损益(出)
			// 月结损益出和其他出不会是黄埔仓 所有班查询放到此处 
			/*if((StringUtils.isNotBlank(type)||StringUtils.isNotBlank(isExpire))
					&&("1".equals(type)||"0".equals(isExpire))) {*/
			if((StringUtils.isNotBlank(type)||StringUtils.isNotBlank(isExpire))
					&&("1".equals(type)||"0".equals(isExpire))) {
				// 盘点仓库是否批次效期强校验
				Map<String, Object> depotInfoMap=new HashMap<>();
				depotInfoMap.put("depotId", depotId);			
				DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
				String depotName = "";
				if(depotInfoMongo != null) {
					depotName = depotInfoMongo.getName();
				}
				
				Map<String, Object> merchandiseMap=new HashMap<>();
				merchandiseMap.put("merchandiseId", goodsId);
				merchandiseMap.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
				String merchandiseNo = "";
				if(merchandiseMongo != null) {
					merchandiseNo = merchandiseMongo.getGoodsNo();
				}
				// 如果非批次效期强校验,就查询所有带批次和不带批次总量
				if ("0".equals(depotInfoMongo.getBatchValidation())) {
					batchNo=null;
				}
				// 根据商家,仓库,商品,理货单位,批次,好坏品,过期品 查询库存余量 
				Map<String, Object> badOrExpiredSurplusNumMap = inventoryBatchService.getBadOrExpiredSurplusNum(user.getMerchantId(),depotId,goodsId,unit,type,isExpire,batchNo);					
				if (badOrExpiredSurplusNumMap==null) {
					LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+",好坏品类型type:"+type+",批次batchNo:"+batchNo+",过期品品类型isExpire:"+isExpire+",单位:"+unit+"===无可用库存量");
					messgae+="仓库："+depotName+"，商品货号为："+merchandiseNo+",好坏品类型："+("0".equals(type)?"好品":"坏品")+",批次:"+batchNo+",过期品品类型:"+isExpire+",单位:"+unit+"===无可用库存量";
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),messgae);//成功
				}else {
					Object surplusNumObject = badOrExpiredSurplusNumMap.get("surplus_num");
					LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+Integer.valueOf(surplusNumObject.toString()));
					return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,Integer.valueOf(surplusNumObject.toString()));//成功
				}
				
			}
			
			if(StringUtils.isNotBlank(depotCode)){
				//黄埔仓返回写死库存值
				if("WMS_360_06".equals(depotCode)||"618".equals(depotCode)){
					return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,99999999);//成功
				}else{
					//空值校验
					boolean isNull = new EmptyCheckUtils().
							addObject(depotId).
							addObject(goodsId).
							empty();
					if(isNull){
						LOGGER.info("goodsId或depotId 字段值为空.商家："+user.getMerchantId());
						//输入信息不完整
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"goodsId或depotId 字段值为空");//成功
					}
					
					Map<String, Object> depotInfoMap=new HashMap<>();
					depotInfoMap.put("depotId", depotId);			
					DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
					String depotName = "";
					if(depotInfoMongo != null) {
						depotName = depotInfoMongo.getName();
					}
					
					Map<String, Object> merchandiseMap=new HashMap<>();
					merchandiseMap.put("merchandiseId", goodsId);	
					merchandiseMap.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
					String merchandiseNo = "";
					if(merchandiseMongo != null) {
						merchandiseNo = merchandiseMongo.getGoodsNo();
					}
					
					inventoryBatchModelList= inventoryBatchService.getAvailableNum(user.getMerchantId(),depotId,goodsId,unit);
					if(inventoryBatchModelList!=null&&inventoryBatchModelList.size()>0){
						InventoryBatchModel inventBatchModel=	inventoryBatchModelList.get(0);
						if(inventBatchModel.getAvailableNum()!=null){
							LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+inventBatchModel.getAvailableNum());
							return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,inventBatchModel.getAvailableNum());//成功				
						}else{
							LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===可用库存为空");
							messgae+="仓库："+depotName+"，商品货号为："+merchandiseNo+"===无可用库存量";
							return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),messgae);//成功

						}
					}else{
						LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===无可用库存");
						messgae+="仓库："+depotName+"，商品货号为："+merchandiseNo+"===无可用库存量";
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),messgae);//成功

					}
				}
			}else{
				//空值校验
				boolean isNull = new EmptyCheckUtils().
						addObject(depotId).
						addObject(goodsId).
						empty();
				if(isNull){
					LOGGER.info("goodsId或depotId 字段值为空.商家："+user.getMerchantId());
					//输入信息不完整
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"goodsId或depotId 字段值为空");//成功

				}
				Map<String, Object> depotInfoMap=new HashMap<>();
				depotInfoMap.put("depotId", depotId);			
				DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
				String depotName = "";
				if(depotInfoMongo != null) {
					depotName = depotInfoMongo.getName();
				}
				
				Map<String, Object> merchandiseMap=new HashMap<>();
				merchandiseMap.put("merchandiseId", goodsId);
				merchandiseMap.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
				String merchandiseNo = "";
				if(merchandiseMongo != null) {
					merchandiseNo = merchandiseMongo.getGoodsNo();
				}				
				
				inventoryBatchModelList= inventoryBatchService.getAvailableNum(user.getMerchantId(),depotId,goodsId,unit);
				if(inventoryBatchModelList!=null&&inventoryBatchModelList.size()>0){
					InventoryBatchModel inventBatchModel=	inventoryBatchModelList.get(0);
					if(inventBatchModel.getAvailableNum()!=null){
						LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+inventBatchModel.getAvailableNum());				
						return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,inventBatchModel.getAvailableNum());//成功				

					}else{
						LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===可用库存为空");
						messgae+="仓库："+depotName+"，商品货号为："+merchandiseNo+"===无可用库存量";
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),messgae);//成功

					}
				}else{
					LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===无可用库存");
					messgae+="仓库："+depotName+"，商品货号为："+merchandiseNo+"===无可用库存量";
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),messgae);//成功
				}
			}
		} catch (Exception e) {
			LOGGER.error("======库存可用量接口异常====", e.getMessage());
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	

}
