package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryFreeUnfreeOrderDao;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.dao.InventoryFreezeDetailsHistoryDao;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryFreeUnfreeOrderModel;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.service.InventoryFreezeAddOrLowerService;
import com.topideal.system.JsonUtils;

import net.sf.json.JSONObject;

/**
 * 冻结和释放冻结  MQ
 * @author 联想302
 *  baols   2018/06/11
 */
@Service
public class InventoryFreezeAddOrLowerServiceImpl implements InventoryFreezeAddOrLowerService {

	  /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeAddOrLowerServiceImpl.class);

	// 冻结库存明细
	@Autowired
	private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;
	//批次库存明细
	@Autowired
	private InventoryBatchDao  inventoryBatchDao;

	// 库存冻结和解冻接口来源单据表
	@Autowired
	private  InventoryFreeUnfreeOrderDao inventoryFreeUnfreeOrderDao;
	//冻结解冻历史
	@Autowired
	private InventoryFreezeDetailsHistoryDao inventoryFreezeDetailsHistoryDao;
	
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201301800,model=DERP_LOG_POINT.POINT_13201301800_Label,keyword="orderNo")
	public boolean synsAddOrLowerInventoryfreeze(String json, String keys, String topics, String tags) throws Exception {
		//一、json报文转实体
		//1、表头信息
		InventoryFreezeRootJson inventoryFreezeRootJson =
				(InventoryFreezeRootJson) JsonUtils.toBean(JSONObject.fromObject(json), InventoryFreezeRootJson.class,
						JsonUtils.getMappingMap("goodsList", InventoryFreezeGoodsListJson.class));
		//2、商品信息
		List<InventoryFreezeGoodsListJson>   goodsList=	inventoryFreezeRootJson.getGoodsList();
		//二、报文参数校验
		checkJsonParam(inventoryFreezeRootJson);
		
		//反审单据，删除库存冻结唯一标识表
		if("1".equals(inventoryFreezeRootJson.getIsReverse())) {
			InventoryFreeUnfreeOrderModel model = new InventoryFreeUnfreeOrderModel();
			model.setOrderNo(inventoryFreezeRootJson.getOrderNo());
			model.setBusinessNo(inventoryFreezeRootJson.getBusinessNo());
			List<InventoryFreeUnfreeOrderModel> unFreeList =  inventoryFreeUnfreeOrderDao.list(model);
			if(unFreeList !=null && unFreeList.size() > 0) {
				List<Long> unFreeIds = unFreeList.stream().map(InventoryFreeUnfreeOrderModel::getId).collect(Collectors.toList());
				inventoryFreeUnfreeOrderDao.delete(unFreeIds);				
			}
		}else {//三、非反审单据，去重校验
			InventoryFreeUnfreeOrderModel model = new InventoryFreeUnfreeOrderModel();
			model.setOrderNo(inventoryFreezeRootJson.getOrderNo());
			model.setBusinessNo(inventoryFreezeRootJson.getBusinessNo());
			model.setOperateType(inventoryFreezeRootJson.getOperateType());
			model.setCreateDate(TimeUtils.getNow());
			Long idFree = inventoryFreeUnfreeOrderDao.save(model);
			if(idFree==null){
				LOGGER.error("单据号为：" + inventoryFreezeRootJson.getOrderNo() + " 保存失败");
				throw new RuntimeException("单据号为：" + inventoryFreezeRootJson.getOrderNo() + "   保存失败");
			}			
		}
		//四、业务校验
		//1、分组统计将相同商品id的解冻数据进行累加
		Map<String,Integer> goodsMap=new HashMap<String, Integer>();
		for(InventoryFreezeGoodsListJson  freeGoodsJson:goodsList){
			String unit = "";
			if(StringUtils.isNotBlank(freeGoodsJson.getUnit())){
				unit = freeGoodsJson.getUnit();
			}
			if(goodsMap.containsKey(freeGoodsJson.getGoodsId()+"_"+unit)){
				goodsMap.put(freeGoodsJson.getGoodsId()+"_"+unit,
						goodsMap.get(freeGoodsJson.getGoodsId()+"_"+unit)+freeGoodsJson.getNum());
			}else{
				goodsMap.put(freeGoodsJson.getGoodsId()+"_"+unit, freeGoodsJson.getNum());
			}
		}

		//业务类型 0 冻结  1 解冻
		String operateType=inventoryFreezeRootJson.getOperateType();
		//2、校验冻结量是否小于库存可用量
		if(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0.equals(operateType)){
			for (Map.Entry<String, Integer> entry : goodsMap.entrySet()) {
				InventoryBatchModel ibmodel =new  InventoryBatchModel();
				//商家
				ibmodel.setMerchantId(Long.valueOf(inventoryFreezeRootJson.getMerchantId()));
				//库存
				ibmodel.setDepotId(Long.valueOf(inventoryFreezeRootJson.getDepotId()));
				//商品id
				String[] keyArr = entry.getKey().split("_");
				String goodsId = keyArr[0];
				String unit = keyArr.length>1?keyArr[1]:null;
				ibmodel.setGoodsId(Long.valueOf(goodsId));
				ibmodel.setUnit(unit);
				InventoryBatchModel inventoryBatchPageModel=inventoryBatchDao.getProductInventoryDetailsByOne(ibmodel);
				if(inventoryBatchPageModel==null){
					LOGGER.error("商家为："+inventoryFreezeRootJson.getMerchantName()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id 为："+goodsId+" 当前数据库没有可用量");
					throw new RuntimeException("商家为："+inventoryFreezeRootJson.getMerchantName()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id 为："+goodsId+" 当前数据库没有可用量");
				}
				//获取可用量
				int availableNum=inventoryBatchPageModel.getAvailableNum();
				//当前数据库的冻结量小于接口传过的解冻量
				if(entry.getValue()>availableNum) {
					LOGGER.error("商家为：" + inventoryFreezeRootJson.getMerchantName() + " 仓库为：" + inventoryFreezeRootJson.getDepotName() + " 商品id 为：" + goodsId + " 当前数据库的可用量小于冻结量");
					throw new RuntimeException("商家为：" + inventoryFreezeRootJson.getMerchantName() + " 仓库为：" + inventoryFreezeRootJson.getDepotName() + " 商品id为：" + goodsId + " 当前数据库的可用量小于冻结量");
				}
			}
			//3、解冻时，校验解冻商品的冻结量与解冻量 2018-11-13
		}else{
			for(InventoryFreezeGoodsListJson goods:goodsList){
				InventoryFreezeDetailsModel ifdmodel=new InventoryFreezeDetailsModel();
				//业务单号
				ifdmodel.setBusinessNo(inventoryFreezeRootJson.getBusinessNo());
				//商品id
				ifdmodel.setGoodsId(Long.valueOf(goods.getGoodsId()));
				String unit = null;
				if(StringUtils.isNotBlank(goods.getUnit())){
					unit = goods.getUnit();
				}
				ifdmodel.setUnit(unit);
				List<InventoryFreezeDetailsModel> modelList= inventoryFreezeDetailsDao.listFreezeDetails(ifdmodel);
				//无冻结记录
				if(modelList==null||modelList.size()<1){
					LOGGER.error(DERP.MQ_FAILTYPE_02 + "商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 没有冻结记录");
					throw new RuntimeException(DERP.MQ_FAILTYPE_02 + "商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 没有冻结记录");
				}
				Integer totalFreezeNum = 0;//冻结数量
				Integer totalUnFreezeNum = 0;//解冻数量
				for(InventoryFreezeDetailsModel inventoryFreezeDetails:modelList){
					if(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0.equals(inventoryFreezeDetails.getOperateType())){
						totalFreezeNum+=inventoryFreezeDetails.getNum();
					}else if(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1.equals(inventoryFreezeDetails.getOperateType())){
						totalUnFreezeNum+=inventoryFreezeDetails.getNum();
					}
				}
				//无冻结记录
				if(totalFreezeNum == 0){
					LOGGER.error(DERP.MQ_FAILTYPE_02 + "商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 没有冻结记录");
					throw new RuntimeException(DERP.MQ_FAILTYPE_02 + "商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 没有冻结记录");
				}
				//总的解冻量
				totalUnFreezeNum+=goods.getNum();
				if(totalFreezeNum < totalUnFreezeNum){//判断冻结量是否小于解冻量
					LOGGER.error("商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 的解冻量大于冻结量");
					throw new RuntimeException("商家为："+inventoryFreezeRootJson.getMerchantName()+"单号为："+inventoryFreezeRootJson.getBusinessNo()+" 仓库为："+inventoryFreezeRootJson.getDepotName()+" 商品id："+goods.getGoodsId()+" 的解冻量大于冻结量");
				}
			}
		}		
		//五、保存冻结明细
		for(InventoryFreezeGoodsListJson  goodsListVo:goodsList){
			//保存库存冻结明细
			InventoryFreezeDetailsModel inFreezeDetailsModel=new InventoryFreezeDetailsModel();
			inFreezeDetailsModel.setMerchantId(Long.valueOf(inventoryFreezeRootJson.getMerchantId()));
			inFreezeDetailsModel.setMerchantName(inventoryFreezeRootJson.getMerchantName());
			inFreezeDetailsModel.setDepotId(Long.valueOf(inventoryFreezeRootJson.getDepotId()));
			inFreezeDetailsModel.setDepotName(inventoryFreezeRootJson.getDepotName());
			inFreezeDetailsModel.setDepotType(inventoryFreezeRootJson.getDepotType());
			inFreezeDetailsModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
			inFreezeDetailsModel.setGoodsNo(goodsListVo.getGoodsNo());
			inFreezeDetailsModel.setGoodsName(goodsListVo.getGoodsName());
			inFreezeDetailsModel.setNum(goodsListVo.getNum());
			inFreezeDetailsModel.setOrderNo(inventoryFreezeRootJson.getOrderNo());
			inFreezeDetailsModel.setBusinessNo(inventoryFreezeRootJson.getBusinessNo());
			inFreezeDetailsModel.setSource(inventoryFreezeRootJson.getSource());
			inFreezeDetailsModel.setStatus(inventoryFreezeRootJson.getSourceType());
			inFreezeDetailsModel.setSourceDate(TimeUtils.parseFullTime(inventoryFreezeRootJson.getSourceDate()));
			inFreezeDetailsModel.setStatusName(InventoryStatusEnum.getInventoryStatusEnumValue(inventoryFreezeRootJson.getSourceType()));
			inFreezeDetailsModel.setOperateType(inventoryFreezeRootJson.getOperateType());
			inFreezeDetailsModel.setCreateDate(TimeUtils.getNow());
			inFreezeDetailsModel.setDivergenceDate(TimeUtils.parseFullTime(goodsListVo.getDivergenceDate()));
			if(StringUtils.isNotBlank(goodsListVo.getUnit())){
				inFreezeDetailsModel.setUnit(goodsListVo.getUnit());
			}
			Long id=inventoryFreezeDetailsDao.save(inFreezeDetailsModel);
			//保存失败
			if(id==null){
				LOGGER.error("订单为："+inventoryFreezeRootJson.getOrderNo()+"商品货号为："+goodsListVo.getGoodsNo()+"冻结或者解冻失败");
				throw new RuntimeException("订单为："+inventoryFreezeRootJson.getOrderNo()+"商品货号为："+goodsListVo.getGoodsNo()+"冻结或者解冻失败");
			}
		}
		/**
		 * 当类型是解冻时  如果 冻结量和解冻量相同 把冻结解冻移到历史表
		 */
		Map<String, Integer> freeze0Map=new HashMap<>();// 冻结数据
		Map<String, Integer> freeze1Map=new HashMap<>();// 解冻数据
		
		if (DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1.equals(operateType)) {
			InventoryFreezeDetailsModel freezeDetailsModel =new InventoryFreezeDetailsModel();
			freezeDetailsModel.setBusinessNo(inventoryFreezeRootJson.getBusinessNo());
			List<InventoryFreezeDetailsModel> freezeDetailsList = inventoryFreezeDetailsDao.list(freezeDetailsModel);
			//用于存放 要移除的 冻结解冻id
			List<Long> delIds=new ArrayList<Long>();
			for (InventoryFreezeDetailsModel detailsModel : freezeDetailsList) {
				Long merchantId = detailsModel.getMerchantId();
				Long depotId = detailsModel.getDepotId();				
				Long goodsId = detailsModel.getGoodsId();
				String unit = "";
				// 0 托盘 1箱  2 件  当为2 件是转成空
				if (StringUtils.isNotBlank(detailsModel.getUnit())&&!"2".equals(detailsModel.getUnit())) {
					unit=detailsModel.getUnit();
				}
				//冻结解冻key
				String strKey= merchantId+","+depotId+","+goodsId+","+unit;
				if (DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0.equals(detailsModel.getOperateType())) {					
					if (freeze0Map.containsKey(strKey)) {
						Integer goodsNum = freeze0Map.get(strKey);
						freeze0Map.put(strKey, goodsNum+detailsModel.getNum());
					}else {
						freeze0Map.put(strKey, detailsModel.getNum());
					}
				}
				
				if (DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1.equals(detailsModel.getOperateType())) {					
					// 解冻
					if (freeze1Map.containsKey(strKey)) {
						Integer goodsNum = freeze1Map.get(strKey);
						freeze1Map.put(strKey, goodsNum+detailsModel.getNum());
					}else {
						freeze1Map.put(strKey, detailsModel.getNum());
					}
				}
				// 保存要删除的ids
				delIds.add(detailsModel.getId());
						
			}
			// 判断冻结解冻是否要移到 历史表中
			String isNotRemove="1";// 0不移走  1 移走
			// 判断 冻结商品和解冻商品是否相同 并且数量是否相同
			if (freeze0Map.size()>0&&freeze1Map.size()>0&&freeze0Map.size()==freeze1Map.size()) {
				Set<String> freeze0Set = freeze0Map.keySet();
				for (String strKey : freeze0Set) {
					Integer freeze0Num = freeze0Map.get(strKey);// 冻结数量
					Integer freeze1Num = freeze1Map.get(strKey);// 解冻数量
					// 由于解冻 已经校验了 解冻商品必须在冻结商品里面 此处不做商品量相同校验
					if (freeze0Num.intValue()!=freeze1Num.intValue()) {
						isNotRemove="0";
					}
				}
			}else {
				isNotRemove="0";
			}
			// 移走
			if ("1".equals(isNotRemove)) {
				// 向冻结解冻历史表 添加数据
				int num = inventoryFreezeDetailsHistoryDao.insertBathHistory(delIds);
				if (num>0) {
					// 删除冻结解冻数据
					inventoryFreezeDetailsDao.delete(delIds);
				}
				
			}
		}
		return true;
	}

	/**
	 *  报文参数校验
	 * @param inventoryFreezeRootJson
	 * @return
	 * @throws Exception
	 */
	private void  checkJsonParam(InventoryFreezeRootJson inventoryFreezeRootJson)throws RuntimeException {
		//1、校验表头参数
		if(StringUtils.isBlank(inventoryFreezeRootJson.getMerchantId())){
			LOGGER.error("商家id为空");
			throw new RuntimeException("商家id为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getMerchantName())){
			LOGGER.error("商家名称为空");
			throw new RuntimeException("商家名称为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getDepotId())){
			LOGGER.error("仓库id为空");
			throw new RuntimeException("仓库id为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getDepotName())){
			LOGGER.error("仓库名称为空");
			throw new RuntimeException("仓库名称为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getOrderNo())){
			LOGGER.error("来源单据号为空");
			throw new RuntimeException("来源单据号为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getSource())){
			LOGGER.error("单据为空");
			throw new RuntimeException("单据为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getSourceType())){
			LOGGER.error("单据类型为空");
			throw new RuntimeException("单据类型为空");
		}else  if(StringUtils.isBlank(inventoryFreezeRootJson.getSourceDate())){
			LOGGER.error("单据日期为空");
			throw new RuntimeException("单据日期为空");
		}else if(StringUtils.isBlank(inventoryFreezeRootJson.getOperateType())){
			LOGGER.error("操作类型为空");
			throw new RuntimeException("操作类型为空");
		}else if(!StrUtils.stringReg(inventoryFreezeRootJson.getOperateType(),"[01]")){
			LOGGER.error("操作类型只能为 0 或1");
			throw new RuntimeException("操作类型只能为 0 或1");
		}
		//2、校验表体参数
		List<InventoryFreezeGoodsListJson> goodsListVo=inventoryFreezeRootJson.getGoodsList();
		if(goodsListVo==null||goodsListVo.size()<1){
			LOGGER.error("商品信息为空");
			throw new RuntimeException("商品信息为空");
		}
		for(InventoryFreezeGoodsListJson goods:goodsListVo){
			if(StringUtils.isBlank(goods.getGoodsId())){
				LOGGER.error("商品Id为空");
				throw new RuntimeException("商品Id为空");
			}else 	if(StringUtils.isBlank(goods.getGoodsName())){
				LOGGER.error("商品名称为空");
				throw new RuntimeException("商品名称为空");
			}else  if(StringUtils.isBlank(goods.getGoodsNo())){
				LOGGER.error("商品货号为空");
				throw new RuntimeException("商品货号为空");
			}else  if(StringUtils.isBlank(goods.getDivergenceDate())){
				LOGGER.error("变更时间为空");
				throw new RuntimeException("变更时间为空");
			}else if(goods.getNum()<0){//库存数里不能小于0
				LOGGER.error("库存数量不能小于0");
				throw new RuntimeException("库存数量不能小于0");
			}
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inventoryFreezeRootJson.getDepotType())&& StringUtils.isBlank(goods.getUnit())){
				LOGGER.error("海外仓商品的理货单位不能为空");
				throw new RuntimeException("海外仓商品的理货单位不能为空");
			}
		}
	}
	
	

}
