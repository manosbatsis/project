package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.InventoryThingStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.service.InventoryUnfreezeOrLowerService;

import net.sf.json.JSONObject;

/**
 *  库存解冻和扣减接口（主要用于事物一致性）
 * 
 * @author 联想302 baols 2018-06-11
 */
@Service
public class InventoryUnfreezeOrLowerServiceImpl implements InventoryUnfreezeOrLowerService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryUnfreezeOrLowerServiceImpl.class);
	// 批次库存收发明细
	@Autowired
	private InventoryBatchDao inventoryBatchDao;
	// 月结账单
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;

	// 月结账单详情
	@Autowired
	private MonthlyAccountItemDao monthlyAccountItemDao;

	// 库存收发明细
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	
	// 冻结和解冻明细
	@Autowired
	private  InventoryFreezeDetailsDao  inventoryFreezeDetailsDao;

	
	
	

	/**
	 *   库存解冻和扣减
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point="13201302100",model="库存解冻和扣减",keyword="orderNo")
	public boolean synsInventoryUnfreezeOrLower(String json,String keys,String topics,String tags) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("========== 库存解冻和扣减===============>" + json);
		// 将字符串转成json
		int errCount=0;
		boolean falg = true;
		boolean lowerFlag = true;
		boolean addFlag = true;
		int delOrAddNum = 0;// 本次扣减量或增加量
		List<InventoryBatchModel> inventoryBatchModelList = null;
		List<MonthlyAccountModel> monthlyAccountModelList = null;
		List<MonthlyAccountItemModel>	monAccountItemModelList = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("goodsList", InvetAddOrSubtractGoodsListJson.class);
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = (InvetAddOrSubtractRootJson)JSONObject.toBean(jsonData, InvetAddOrSubtractRootJson.class,
					classMap);
			//校验数据格式是否正确
			falg = this.checkJsonData(invetAddOrSubtractRootJson);
			if (falg) {
				//校验接口数据是否重复推送
				falg=this.checkOrderNoAndOperateType(invetAddOrSubtractRootJson);
				if(falg){
					//校验解冻量是否小于当前冻结量
					falg=this.checkGoodsFreezeAmount(invetAddOrSubtractRootJson);
					if(falg){
						
						List<InvetAddOrSubtractGoodsListJson> goodsList = invetAddOrSubtractRootJson.getGoodsList();
						// 出入时间
						Date divergenceDate = format.parse(invetAddOrSubtractRootJson.getDivergenceDate());
						// 归属月份
						Date ownMonth = format.parse(invetAddOrSubtractRootJson.getOwnMonth());
						if (ownMonth.getTime() == divergenceDate.getTime()) {// 说明是归属月份和出入时间是同一个月份
							
							// 主要校验数据扣减量是否小于等于库存量
							falg = this.checkDeductionDateValid(invetAddOrSubtractRootJson);
							if (falg) {
								// 循环商品集合
								for (InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
									
									InventoryBatchModel inBatchModel = new InventoryBatchModel();
									inBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
									inBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
									inBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
									delOrAddNum = goodsListVo.getNum();// 当前商品扣减量
									if ("0".equals(goodsListVo.getOperateType())) {// 调减(扣减的都是好品)
										// 扣减库存
										lowerFlag = this.lowerInventory(invetAddOrSubtractRootJson, delOrAddNum, goodsListVo, inBatchModel,
												inventoryBatchModelList);
										if (!lowerFlag) {
											LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
													+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
													+ ",批次为：" + goodsListVo.getBatchNo() + "扣减库存失败！");
											throw new Exception("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
													+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
													+ ",批次为：" + goodsListVo.getBatchNo() + "扣减库存失败！");
										}
									} else if ("1".equals(goodsListVo.getOperateType())) {//解冻
										
										addFlag = this.addInventoryFreezeDetailsModel(invetAddOrSubtractRootJson, goodsListVo);
										if (!addFlag) {
											LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
													+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
													+ ",批次为：" + goodsListVo.getBatchNo() + "解冻库存失败！");
											throw new Exception("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
													+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
													+ ",批次为：" + goodsListVo.getBatchNo() + "解冻库存失败！");
										}
									}
									
								}
							}
							
						} else {// 不同月份
							
							// 校验归属月份是否结转和商品可用量是否够扣减
							falg = this.checkAvailableNum(invetAddOrSubtractRootJson);
							if (falg) {// 处理扣减
								// 主要校验数据扣减量小于等于批次库存量
								falg = this.checkDeductionDateValid(invetAddOrSubtractRootJson);
								if (falg) {
									// 循环商品集合
									for (InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
										InventoryBatchModel inBatchModel = new InventoryBatchModel();
										inBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
										inBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
										inBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
										delOrAddNum = goodsListVo.getNum();// 当前商品扣减量
										if ("0".equals(goodsListVo.getOperateType())) {// 调减(扣减的都是好品)
											// 扣减库存
											lowerFlag = this.lowerInventory(invetAddOrSubtractRootJson, delOrAddNum, goodsListVo, inBatchModel,
													inventoryBatchModelList);
											if (!lowerFlag) {
												errCount++;
												LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
														+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
														+ ",批次为：" + goodsListVo.getBatchNo() + "扣减库存失败！");
												throw new Exception("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
														+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
														+ ",批次为：" + goodsListVo.getBatchNo() + "扣减库存失败！");
											}
										} else if ("1".equals(goodsListVo.getOperateType())) {// 解冻
											
											addFlag = this.addInventoryFreezeDetailsModel(invetAddOrSubtractRootJson, goodsListVo);
											if (!addFlag) {
												errCount++;
												LOGGER.error("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
														+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
														+ ",批次为：" + goodsListVo.getBatchNo() + "解冻库存失败！");
												throw new Exception("单据号为：" + invetAddOrSubtractRootJson.getOrderNo() + ",仓库为："
														+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo()
														+ ",批次为：" + goodsListVo.getBatchNo() + "解冻库存失败！");
											}
										}
									}
									//批次库存明细和商品收发明细处理成功在操作月结详情表
									if(errCount==0){
										// 处理月结库存
										MonthlyAccountModel monthlyAccotModel = new MonthlyAccountModel();
										monthlyAccotModel
												.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
										monthlyAccotModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
										monthlyAccotModel.setSettlementMonth(invetAddOrSubtractRootJson.getOwnMonth());
										monthlyAccotModel.setState("1");//未结转
										monthlyAccountModelList = monthlyAccountDao
												.getMonthlyAccountListByMonth(monthlyAccotModel);
										for (MonthlyAccountModel monthlyAModel : monthlyAccountModelList) {
											// 循环商品集合
											for (InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
												delOrAddNum = goodsListVo.getNum();// 当前商品扣减量
												if ("0".equals(goodsListVo.getOperateType())) {
													
													this.inventoryMonthLower(monthlyAModel, invetAddOrSubtractRootJson, goodsListVo, delOrAddNum);
													
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		return falg;
	}

	
	/**
	 * 校验单据号和操作类型一致的时候，数据库只能一条
	 * @param InvetAddOrSubtractRootJson
	 * @return
	 */
	public boolean checkOrderNoAndOperateType(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) throws Exception{
		boolean falg=true;
		int failCount=0;
	        List<InvetAddOrSubtractGoodsListJson>	goodsList=invetAddOrSubtractRootJson.getGoodsList();
			for(InvetAddOrSubtractGoodsListJson goodsListVo:  goodsList){
				InventoryDetailsModel  model=new InventoryDetailsModel();
				model.setOrderNo(invetAddOrSubtractRootJson.getOrderNo());
				model.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
				model.setOperateType(goodsListVo.getOperateType());
				model=	inventoryDetailsDao.searchByModel(model);
				   if(model!=null){
			        	if("0".equals(goodsListVo.getOperateType())){
			        		failCount++;
			        		LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品名称："+goodsListVo.getGoodsName()+" 库存量已扣减，不能重复扣减");
			        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品名称："+goodsListVo.getGoodsName()+" 库存量已扣减，不能重复扣减");
			        	}else{
			        		failCount++;
			        		LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品名称："+goodsListVo.getGoodsName()+" 库存量已增加，不能重复增加");
			        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品名称："+goodsListVo.getGoodsName()+" 库存量已增加，不能重复增加");
			        	}
					}
			}
			if(failCount>0){
				falg=false;
			}
		
		return falg;
	}
	
	/**
	 * 校验归属月份和出入时间不一致的话，查询商品可用库存量
	 * 
	 * @param InvetAddOrSubtractRootJson
	 * @return
	 */
	public boolean checkAvailableNum(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) throws  Exception{
		int failCount = 0;
		boolean falg = true;
		int delOrAddNum = 0;// 本次扣减量或增加量

			// 查询当前归属月份是否有月结和月结状态
			MonthlyAccountModel monthlyAccountModel = new MonthlyAccountModel();
			monthlyAccountModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			monthlyAccountModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
			monthlyAccountModel.setSettlementMonth(invetAddOrSubtractRootJson.getOwnMonth());
			monthlyAccountModel = monthlyAccountDao.searchByModel(monthlyAccountModel);
			if (monthlyAccountModel != null) {
				// 未结转
				if ("1".endsWith(monthlyAccountModel.getState())) {
					for (InvetAddOrSubtractGoodsListJson goodsListVo : invetAddOrSubtractRootJson.getGoodsList()) {
						delOrAddNum = goodsListVo.getNum();// 当前商品扣减量
						if("0".equals(goodsListVo.getOperateType())){
							
							// 校验商品库存用量
							InventoryBatchModel inventoryBatchModel = new InventoryBatchModel();
							inventoryBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
							inventoryBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
							inventoryBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
							//inventoryBatchModel = inventoryBatchDao.listProductInventoryDetailsByPage(inventoryBatchModel);//查询该商品的可用库存
							if (inventoryBatchModel != null) {
								if (inventoryBatchModel.getAvailableNum() < delOrAddNum) {
									failCount++;
									LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，商品货号："+goodsListVo.getGoodsNo()+",扣减量大于当前可用库存");
									throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，商品货号："+goodsListVo.getGoodsNo()+",扣减量大于当前可用库存");
								}
							} else {
								failCount++;
								LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，商品货号："+goodsListVo.getGoodsNo()+",查无可用库存量");
								throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，商品货号："+goodsListVo.getGoodsNo()+",查无可用库存量");
							}
						}
					}

				} else {
					failCount++;
					LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，归属月份为："+invetAddOrSubtractRootJson.getOwnMonth()+",月结账单已结转 ，不能进行库存调整");
					throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，归属月份为："+invetAddOrSubtractRootJson.getOwnMonth()+",月结账单已结转，不能进行库存调整 ");
				}
			} else {
				failCount++;
				LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，归属月份为："+invetAddOrSubtractRootJson.getOwnMonth()+",未生成月结账单 ，不能进行库存调整");
				throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+"，归属月份为："+invetAddOrSubtractRootJson.getOwnMonth()+",未生成月结账单，不能进行库存调整 ");
			}
			if (failCount > 0) {
				falg = false;
			}

		return falg;

	}

	/**
	 * 获取归属月份后面还有几个月份
	 * 
	 * @param InvetAddOrSubtractRootJson
	 * @return
	 */
	public int getMonthlyAccountListByMonthNum(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson)throws  Exception {
		List<MonthlyAccountModel> monthlyAccountModelList = null;
		int num = 0;
			// 查询当前归属月份是否有月结和月结状态
			MonthlyAccountModel monthlyAccountModel = new MonthlyAccountModel();
			monthlyAccountModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			monthlyAccountModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
			monthlyAccountModel.setSettlementMonth(invetAddOrSubtractRootJson.getOwnMonth());
			monthlyAccountModelList = monthlyAccountDao.getMonthlyAccountListByMonth(monthlyAccountModel);
			// 循环商品集合
			if (monthlyAccountModelList != null && monthlyAccountModelList.size() > 0) {
				num = monthlyAccountModelList.size();
			}

		return num;
	}

	/**
	 * 数据非空校验
	 * 
	 * @param InvetAddOrSubtractRootJson
	 * @return
	 * @throws Exception
	 */
	public boolean checkJsonData(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) throws  Exception{
		boolean flag = true;
		int errorNum=0;  

			if (StringUtils.isBlank(invetAddOrSubtractRootJson.getMerchantId())) {
				flag = false;
				LOGGER.error("商家id为空");
				throw new Exception("商家id为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getMerchantName())) {
				flag = false;
				LOGGER.error("商家名称为空");
				throw new Exception("商家名称为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getTopidealCode())) {
				flag = false;
				LOGGER.error("商家编码为空");
				throw new Exception("商家编码为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotId())) {
				flag = false;
				LOGGER.error("仓库id为空");
				throw new Exception("仓库id为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotCode())) {
				flag = false;
				LOGGER.error("仓库编码为空");
				throw new Exception("仓库编码为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getIsTopBooks())) {
				flag = false;
				LOGGER.error("是否代销仓为空");
				throw new Exception("是否代销仓为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDepotName())) {
				flag = false;
				LOGGER.error("仓库名称为空");
				throw new Exception("仓库名称为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getOrderNo())) {
				flag = false;
				LOGGER.error("来源单据号为空");
				throw new Exception("来源单据号为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSource())) {
				flag = false;
				LOGGER.error("单据为空");
				throw new Exception("单据为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSourceType())) {
				flag = false;
				LOGGER.error("单据类型为空");
				throw new Exception("单据类型为空");
			} else if (StringUtils.isBlank(invetAddOrSubtractRootJson.getOwnMonth())) {
				flag = false;
				LOGGER.error("归属月份为空");
				throw new Exception("归属月份为空");
			}
			if (StringUtils.isBlank(invetAddOrSubtractRootJson.getDivergenceDate())) {
				flag = false;
				LOGGER.error("出入时间为空");
				throw new Exception("出入时间为空");
			}

			if (StringUtils.isBlank(invetAddOrSubtractRootJson.getSourceDate())) {
				flag = false;
				LOGGER.error("单据日期为空");
				throw new Exception("单据日期为空");
			}
           
			// 校验商品信息
			List<InvetAddOrSubtractGoodsListJson> goodsListVo = invetAddOrSubtractRootJson.getGoodsList();
			if (goodsListVo != null && goodsListVo.size() > 0) {

				for (InvetAddOrSubtractGoodsListJson goods : goodsListVo) {
					if (StringUtils.isBlank(goods.getGoodsId())) {
						errorNum++;
						LOGGER.error("商品id为空");
						throw new Exception("商品id为空");
					} else if (StringUtils.isBlank(goods.getGoodsName())) {
						errorNum++;
						LOGGER.error("商品名称为空");
						throw new Exception("商品名称为空");
					} else if (StringUtils.isBlank(goods.getGoodsNo())) {
						errorNum++;
						LOGGER.error("商品货号为空");
						throw new Exception("商品货号为空");
					} else if (StringUtils.isBlank(goods.getOperateType())) {
						errorNum++;
						LOGGER.error("操作类型为空");
						throw new Exception("操作类型为空");
					}else if (StringUtils.isBlank(goods.getOperateType())) {
						if("0".equals(goods.getOperateType())||"1".equals(goods.getOperateType())){
							
						}else{
							errorNum++;
							LOGGER.error("操作类型只能传0或1");
							throw new Exception("操作类型只能传0或1");
						}
					}else if(goods.getNum()<=0){//库存数里不能小于0
						errorNum++;
						LOGGER.error("库存数里不能小于等于0");
						throw new Exception("库存数里不能小于等于0");
				    }
					
					if(StringUtils.isNotBlank(goods.getIsExpire())){
						if("0".equals(goods.getIsExpire())||"1".equals(goods.getIsExpire())){
							
						}else{
							LOGGER.error("是否过期字段为无效数字");
							throw new Exception("是否过期字段为无效数字");
						}
					}
					
				}
			} else {
				errorNum++;
				LOGGER.error("商品信息为空");
				throw new Exception("商品信息为空");
			}
			if(errorNum>0){
				flag = false;
				LOGGER.error("库存扣减数据校验不通过");
        		throw new Exception("库存扣减数据校验不通过");
			}
		return flag;
	}

	/**
	 * 减库存量动作
	 * 
	 * @return
	 */
	public boolean lowerInventory(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson, int delOrAddNum,
			InvetAddOrSubtractGoodsListJson goodsListVo, InventoryBatchModel inBatchModel,
			List<InventoryBatchModel> inventoryBatchModelList) throws Exception {
		 boolean saveFlag = true;
		 int countFail=0;
		 int lessNum=0;//每次减少的量
		// 如果接口没有传扣减商品类型，默认扣减好品的库存数里
		if (StringUtils.isNotBlank(goodsListVo.getType())) {
			inBatchModel.setType(goodsListVo.getType());
		} else {
			inBatchModel.setType("0");// 好品
		}
		// 如果接口没有传扣减商品类型为好品，并且是非过期品
		if (StringUtils.isNotBlank(goodsListVo.getIsExpire())) {
			inBatchModel.setIsExpire(goodsListVo.getIsExpire());
		} else {
			inBatchModel.setIsExpire("1");// 非过期
		}
		if ("d".equals(invetAddOrSubtractRootJson.getDepotType())) {// 在途仓（出入无批次效期）

			inBatchModel = inventoryBatchDao.searchByModel(inBatchModel);
			if (inBatchModel != null) {
				if (delOrAddNum == inBatchModel.getSurplusNum()) {// 接口需扣减的数量大于当前批次的库存数量
					delOrAddNum = delOrAddNum - inBatchModel.getSurplusNum();
					lessNum=inBatchModel.getSurplusNum();
					if (delOrAddNum == 0) {
						int num = inventoryBatchDao.delInventoryBatch(inBatchModel.getId());
						if (num > 0) {
							// 保存商品收发明细
							saveFlag = this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inBatchModel,lessNum);
							if (!saveFlag) {
								countFail++;
								LOGGER.error(" 保存商品收发明细失败");
								throw new Exception(" 保存商品收发明细失败");
							}
						} else {
							countFail++;
							LOGGER.error("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
							throw new Exception("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
						}
					}
				} else if (delOrAddNum < inBatchModel.getSurplusNum()) {// 接口需扣减的数量小于当前批次的库存数量
					lessNum=delOrAddNum;
					delOrAddNum = inBatchModel.getSurplusNum() - delOrAddNum;
					inBatchModel.setSurplusNum(delOrAddNum);
					int num = inventoryBatchDao.modify(inBatchModel);
					if (num > 0) {
						// 保存商品收发明细
						saveFlag = this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inBatchModel,lessNum);
						if (!saveFlag) {
							countFail++;
							LOGGER.error(" 保存商品收发明细失败");
							throw new Exception(" 保存商品收发明细失败");
						}
					} else {
						countFail++;
						LOGGER.error("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
						throw new Exception("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
					}
				}
			}
		} else {// 除在途仓其他仓都是（先失效先出）
			if (StringUtils.isNotBlank(goodsListVo.getBatchNo())) {
				inBatchModel.setBatchNo(goodsListVo.getBatchNo());
			}
			inventoryBatchModelList = inventoryBatchDao.listOrbyOverdueDate(inBatchModel);// 按失效日的靠前的进行排序查询
			if (inventoryBatchModelList != null && inventoryBatchModelList.size() > 0) {
				for (InventoryBatchModel inventoryBatchModel : inventoryBatchModelList) {
					if (delOrAddNum > inventoryBatchModel.getSurplusNum()) {// 接口需扣减的数量大于当前批次的库存数量
						delOrAddNum = delOrAddNum - inventoryBatchModel.getSurplusNum();
						lessNum=inventoryBatchModel.getSurplusNum();
						int num = inventoryBatchDao.delInventoryBatch(inventoryBatchModel.getId());
						if (num > 0) {
							// 保存商品收发明细
							saveFlag = this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inventoryBatchModel,lessNum);
							if (!saveFlag) {
								countFail++;
								LOGGER.error(" 保存商品收发明细失败");
								throw new Exception(" 保存商品收发明细失败");
							}
						} else {
							countFail++;
							LOGGER.error("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
							throw new Exception("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
						}
					} else if (delOrAddNum <=inventoryBatchModel.getSurplusNum()) {// 接口需扣减的数量小于当前批次的库存数量
						lessNum=delOrAddNum;//扣减的数里
						int delNum=inventoryBatchModel.getSurplusNum() - delOrAddNum;
						if(delNum==0){
							int num = inventoryBatchDao.delInventoryBatch(inventoryBatchModel.getId());
							if (num > 0) {
								// 保存商品收发明细
								saveFlag = this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inventoryBatchModel,lessNum);
								if (!saveFlag) {
									countFail++;
									LOGGER.error(" 保存商品收发明细失败");
									throw new Exception(" 保存商品收发明细失败");
								}
							  } else {
								countFail++;
								LOGGER.error("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
								throw new Exception("批次库存信息id为："+inBatchModel.getId()+"库存量删除失败");
							}
						}else{
							inventoryBatchModel.setSurplusNum(delNum);
							int num = inventoryBatchDao.modify(inventoryBatchModel);
							if (num > 0) {
								// 保存商品收发明细
								saveFlag = this.saveInventoryDetails(invetAddOrSubtractRootJson, goodsListVo,inventoryBatchModel,lessNum);
								if (!saveFlag) {
									countFail++;
									LOGGER.error(" 保存商品收发明细失败");
									throw new Exception(" 保存商品收发明细失败");
								}
							} else {
								countFail++;
								LOGGER.error("批次库存信息id为："+inBatchModel.getId()+"库存量更新失败");
								throw new Exception("批次库存信息id为："+inBatchModel.getId()+"库存量更新失败");
							}
					   }
						break; //跳出当前循环
					}
				}
			} 
		}
		if(countFail>0){
			saveFlag=false;
		}
		return saveFlag;
	}

	/**
	 * 释放冻结量
	 * 
	 * @return
	 */
	public boolean addInventoryFreezeDetailsModel(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,  InvetAddOrSubtractGoodsListJson goodsListVo)throws  Exception {
		boolean saveFlag = true;
		
		//保存库存冻结明细
		InventoryFreezeDetailsModel inFreezeDetailsModel=new InventoryFreezeDetailsModel();
		inFreezeDetailsModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
		inFreezeDetailsModel.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
		inFreezeDetailsModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
		inFreezeDetailsModel.setDepotName(invetAddOrSubtractRootJson.getDepotName());
		inFreezeDetailsModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
		inFreezeDetailsModel.setGoodsNo(goodsListVo.getGoodsNo());
		inFreezeDetailsModel.setGoodsName(goodsListVo.getGoodsName());
		inFreezeDetailsModel.setNum(goodsListVo.getNum());
		inFreezeDetailsModel.setOrderNo(invetAddOrSubtractRootJson.getOrderNo());
		inFreezeDetailsModel.setSource(invetAddOrSubtractRootJson.getSource());
		inFreezeDetailsModel.setStatus(invetAddOrSubtractRootJson.getSourceType());
		inFreezeDetailsModel.setSourceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getSourceDate()));
		inFreezeDetailsModel.setStatusName(InventoryStatusEnum.getInventoryStatusEnumValue(invetAddOrSubtractRootJson.getSourceType()));
		inFreezeDetailsModel.setOperateType(goodsListVo.getOperateType());
		inFreezeDetailsModel.setCreateDate(TimeUtils.getNow());
		inFreezeDetailsModel.setDivergenceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getDivergenceDate()));
		inFreezeDetailsModel.setUnit(goodsListVo.getUnit());
		Long id=	inventoryFreezeDetailsDao.save(inFreezeDetailsModel);
		if(id==null){
			saveFlag=false;
			LOGGER.error("订单为："+invetAddOrSubtractRootJson.getOrderNo()+"商品货号为："+goodsListVo.getGoodsNo()+"解冻失败");
			throw new Exception("订单为："+invetAddOrSubtractRootJson.getOrderNo()+"商品货号为："+goodsListVo.getGoodsNo()+"解冻失败");
		}
		return saveFlag;
	}



	/**
	 * 保存库存收发明细 
	 * 
	 * @param inventoryBatchModel
	 * @param delOrAddNum
	 * @param InvetAddOrSubtractRootJson
	 * @param goodsListVo
	 * @return
	 */
	public boolean saveInventoryDetails(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson, InvetAddOrSubtractGoodsListJson goodsListVo,InventoryBatchModel inBatchModel,int lessNum) throws  Exception{
		boolean falg = true;
		
			InventoryDetailsModel indetailModel = new InventoryDetailsModel();
			indetailModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			indetailModel.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
			indetailModel.setIsTopBooks(invetAddOrSubtractRootJson.getIsTopBooks());
			indetailModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
			indetailModel.setDepotName(invetAddOrSubtractRootJson.getDepotName());
			indetailModel.setDepotCode(invetAddOrSubtractRootJson.getDepotCode());
			indetailModel.setTopidealCode(invetAddOrSubtractRootJson.getTopidealCode());
			indetailModel.setDepotType(invetAddOrSubtractRootJson.getDepotType());
			indetailModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
			indetailModel.setGoodsName(goodsListVo.getGoodsName());
			if(StringUtils.isNotBlank(goodsListVo.getBatchNo())){
				indetailModel.setBatchNo(goodsListVo.getBatchNo());
			}else{
				indetailModel.setBatchNo(inBatchModel.getBatchNo());
			}
			if("0".equals(goodsListVo.getOperateType())){//先失效先出（扣减）
				indetailModel.setNum(lessNum);
				indetailModel.setProductionDate(inBatchModel.getProductionDate());
				indetailModel.setOverdueDate(inBatchModel.getOverdueDate());
			}else{
				indetailModel.setNum(goodsListVo.getNum());
				indetailModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
				indetailModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
			}
			indetailModel.setOrderNo(invetAddOrSubtractRootJson.getOrderNo());
			indetailModel.setSource(invetAddOrSubtractRootJson.getSource());
			indetailModel.setSourceType(invetAddOrSubtractRootJson.getSourceType());

			if(InventoryStatusEnum.XH.getKey().equals(invetAddOrSubtractRootJson.getSourceType())){//销毁
				indetailModel.setThingStatus(InventoryThingStatusEnum.XH.getKey());
			}else if (InventoryStatusEnum.CGRK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 采购入库
				indetailModel.setThingStatus(InventoryThingStatusEnum.CGRK.getKey());
			} else if (InventoryStatusEnum.XSCK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售出库
				indetailModel.setThingStatus(InventoryThingStatusEnum.XSCK.getKey());
			} else if (InventoryStatusEnum.XSTHRK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售退货入库
				indetailModel.setThingStatus(InventoryThingStatusEnum.XSTHRK.getKey());
			} else if (InventoryStatusEnum.XSTHCK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 销售退货出库
				indetailModel.setThingStatus(InventoryThingStatusEnum.XSTHCK.getKey());
			}else if (InventoryStatusEnum.DSDDCK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 电商订单出库
				indetailModel.setThingStatus(InventoryThingStatusEnum.DSDDCK.getKey());
			} else if (InventoryStatusEnum.DBRK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 调拨入库
				indetailModel.setThingStatus(InventoryThingStatusEnum.DBRK.getKey());
			} else if (InventoryStatusEnum.DBCK.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 调拨出库
				indetailModel.setThingStatus(InventoryThingStatusEnum.DBCK.getKey());
			} else if (InventoryStatusEnum.PDJGD.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 盘点结果单
				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.PYC.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.PYR.getKey());
				}

			} else if (InventoryStatusEnum.QTCR.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 其他入其他出
				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.QTC.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.QTR.getKey());
				}

			} else if (InventoryStatusEnum.YJSY.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 月结损益

				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.YJPK.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.YJPY.getKey());
				}
			} else if (InventoryStatusEnum.HHHZ.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 好坏品互转
				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.HHPC.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.HHPR.getKey());
				}
			} else if (InventoryStatusEnum.XQTZ.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 效期调整
				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.XXTZC.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.XXTZR.getKey());
				}
			} else if (InventoryStatusEnum.HHBG.getKey().equals(invetAddOrSubtractRootJson.getSourceType())) {// 货号变更
				if ("0".equals(goodsListVo.getOperateType())) {// 减
					indetailModel.setThingStatus(InventoryThingStatusEnum.HHBGC.getKey());
				} else {// 增
					indetailModel.setThingStatus(InventoryThingStatusEnum.HHBGR.getKey());
				}
			}
			
			indetailModel.setThingName(InventoryThingStatusEnum.getInventoryThingStatusEnumValue(indetailModel.getThingStatus()));
			indetailModel.setSourceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getSourceDate()));
			indetailModel.setCreateDate(TimeUtils.getNow());
			indetailModel.setUnit(goodsListVo.getUnit());
			indetailModel.setOperateType(goodsListVo.getOperateType());
			indetailModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
			indetailModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
			
			if(StringUtils.isNotBlank(goodsListVo.getType())){
				indetailModel.setType(goodsListVo.getType());
			}else{
				indetailModel.setType("0");//好品
			}
			if(StringUtils.isNotBlank(goodsListVo.getIsExpire())){
				indetailModel.setIsExpire(goodsListVo.getIsExpire());
			}else{
				indetailModel.setIsExpire("1");//非过期
			}
			indetailModel.setOwnMonth(invetAddOrSubtractRootJson.getOwnMonth());
			indetailModel.setDivergenceDate(TimeUtils.parseFullTime(invetAddOrSubtractRootJson.getDivergenceDate()));
			indetailModel.setGoodsNo(goodsListVo.getGoodsNo());
			Long id = inventoryDetailsDao.save(indetailModel);
			if (id == null) {
				falg = false;
				LOGGER.error("新增商品收发明细失败");
				throw new Exception("新增商品收发明细失败");
			}
		return falg;
	}

	/***
	 * 保存批次库存信息
	 * 
	 * @param InvetAddOrSubtractRootJson
	 * @param goodsListVo
	 * @return
	 */
	public boolean saveInventoryBatch(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson, InvetAddOrSubtractGoodsListJson goodsListVo)throws  Exception {
		boolean falg = true;
		
			InventoryBatchModel inventoryBatchModel = new InventoryBatchModel();
			inventoryBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
			inventoryBatchModel.setMerchantName(invetAddOrSubtractRootJson.getMerchantName());
			inventoryBatchModel.setIsTopBooks(invetAddOrSubtractRootJson.getIsTopBooks());
			inventoryBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
			inventoryBatchModel.setDepotName(invetAddOrSubtractRootJson.getDepotName());
			inventoryBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
			inventoryBatchModel.setGoodsName(goodsListVo.getGoodsName());
			inventoryBatchModel.setBatchNo(goodsListVo.getBatchNo());
			inventoryBatchModel.setSurplusNum(goodsListVo.getNum());
			inventoryBatchModel.setCreateDate(TimeUtils.getNow());
			inventoryBatchModel.setUnit(goodsListVo.getUnit());
			if (StringUtils.isNotBlank(goodsListVo.getProductionDate())) {
				inventoryBatchModel.setProductionDate(TimeUtils.strToSqlDate(goodsListVo.getProductionDate()));
				
			}
			if (StringUtils.isNotBlank(goodsListVo.getOverdueDate())) {
				inventoryBatchModel.setOverdueDate(TimeUtils.strToSqlDate(goodsListVo.getOverdueDate()));
			}
			inventoryBatchModel.setTopidealCode(invetAddOrSubtractRootJson.getTopidealCode());
			inventoryBatchModel.setDepotCode(invetAddOrSubtractRootJson.getDepotCode());
			inventoryBatchModel.setDepotType(invetAddOrSubtractRootJson.getDepotType());
			inventoryBatchModel.setType(goodsListVo.getType());
			if(StringUtils.isNotBlank(goodsListVo.getType())){
				inventoryBatchModel.setType(goodsListVo.getType());
			}else{
				inventoryBatchModel.setType("0");//好品
			}
			if(StringUtils.isNotBlank(goodsListVo.getIsExpire())){
				inventoryBatchModel.setIsExpire(goodsListVo.getIsExpire());
			}else{
				inventoryBatchModel.setIsExpire("1");//非过期
			}
			inventoryBatchModel.setGoodsNo(goodsListVo.getGoodsNo());
			inventoryBatchModel.setOwnMonth(invetAddOrSubtractRootJson.getOwnMonth());
			Long id = inventoryBatchDao.save(inventoryBatchModel);
			if (id == null) {
				falg = false;
				LOGGER.error("新增批次库存明细失败");
				throw new Exception("新增批次库存明细失败");
			}
		return falg;
	}



	/**
	 * 校验 接口数据是否有效可以进行扣减逻辑（查询商品批次库存量）
	 * 
	 * @param InvetAddOrSubtractRootJson
	 * @return
	 */
	public boolean checkDeductionDateValid(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) throws  Exception{
		boolean falg = true;
		int failNum = 0;
		int delOrAddNum = 0;
	    int surplusNum=0;
		List<InventoryBatchModel> inventoryBatchModelList = null;
		
			List<InvetAddOrSubtractGoodsListJson> goodsList = invetAddOrSubtractRootJson.getGoodsList();
			if (goodsList != null && goodsList.size() > 0) {
				for (InvetAddOrSubtractGoodsListJson goodsListVo : goodsList) {
					if ("0".equals(goodsListVo.getOperateType())) {
						delOrAddNum = goodsListVo.getNum();// 当前商品需要扣减的量
						InventoryBatchModel inventoryBatchModel = new InventoryBatchModel();
						inventoryBatchModel.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
						inventoryBatchModel.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
						inventoryBatchModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));

						if (StringUtils.isNotBlank(goodsListVo.getType())) {
							inventoryBatchModel.setType(goodsListVo.getType());
						} else {
							inventoryBatchModel.setType("0");// 好品
						}
						if (StringUtils.isNotBlank(goodsListVo.getIsExpire())) {
							inventoryBatchModel.setIsExpire(goodsListVo.getIsExpire());
						} else {
							inventoryBatchModel.setIsExpire("1");// 非过期
						}
						if ("d".equals(invetAddOrSubtractRootJson.getDepotType())) {// 在途仓（无批次效期出库）
							inventoryBatchModel = inventoryBatchDao.searchByModel(inventoryBatchModel);
							if (inventoryBatchModel != null) {
								if (delOrAddNum > inventoryBatchModel.getSurplusNum()) {// 当前商品扣减量大于库存量
									failNum++;
									LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  扣减量大于实际库存量");
					        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  扣减量大于实际库存量");
								}
							} else {
								failNum++;
								LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  无实际库存量");
				        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  无实际库存量");
							}
						} else {// 除在途仓其他仓库都是按先失效先出
								inventoryBatchModelList = inventoryBatchDao.listOrbyOverdueDate(inventoryBatchModel);
								if (inventoryBatchModelList != null && inventoryBatchModelList.size() > 0) {
									for (InventoryBatchModel batchModel : inventoryBatchModelList) {
										surplusNum+=batchModel.getSurplusNum();
									}
									//当前出库的量和数据库存在的总库存量进行比较
									if (delOrAddNum >surplusNum) {// 该商品单据出库不够扣减
										failNum++;
										LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  扣减量大于实际库存量");
						        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  扣减量大于实际库存量");
									}
								} else {
									failNum++;
									LOGGER.error("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  无实际库存量");
					        		throw new Exception("单据号为："+invetAddOrSubtractRootJson.getOrderNo()+",商品货号："+goodsListVo.getGoodsNo()+"  无实际库存量");
								}
						}
					}
				}
			}
			// 说明当前单据存在问题不进行扣减
			if (failNum > 0) {
				falg = false;
			}
		return falg;
	}
	
	
	
	/**
	 *  校验当前解冻量是否小于 数据库中冻结量 
	 * @param InventoryFreezeRootJson
	 * @return
	 * @throws Exception
	 */
	public boolean checkGoodsFreezeAmount(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) throws Exception{
	    int countFail=0;
	    boolean falg=true;
		
		Map<String, Integer>  returnMap=this.countProductGroupNum(invetAddOrSubtractRootJson);
		if(returnMap.isEmpty()){

		}else{
			for (Map.Entry<String, Integer> entry : returnMap.entrySet()) {
					InventoryFreezeDetailsModel model = new InventoryFreezeDetailsModel();
					model.setMerchantId(Long.valueOf(invetAddOrSubtractRootJson.getMerchantId()));
					model.setDepotId(Long.valueOf(invetAddOrSubtractRootJson.getDepotId()));
					model.setGoodsId(Long.valueOf(entry.getKey()));
					InventoryFreezeDetailsModel inventoryFreezeModel = inventoryFreezeDetailsDao
							.getInventoryFreezeNum(model);
					if (inventoryFreezeModel != null) {
						if (entry.getValue()> inventoryFreezeModel.getNum()) {// 当前数据库的冻结量小于接口传过的解冻量
							countFail++;
							LOGGER.error("商家为：" + invetAddOrSubtractRootJson.getMerchantName() + " 仓库为："
									+ invetAddOrSubtractRootJson.getDepotName() + " 商品id：" + entry.getKey()
									+ " 当前数据库的冻结量小于需要解冻的量");
							throw new Exception("商家为：" + invetAddOrSubtractRootJson.getMerchantName() + " 仓库为："
									+ invetAddOrSubtractRootJson.getDepotName() + " 商品id：" + entry.getKey()
									+ " 当前数据库的冻结量小于需要解冻的量");
						}
					} else {
						countFail++;
						LOGGER.error("商家为：" + invetAddOrSubtractRootJson.getMerchantName() + " 仓库为："
								+ invetAddOrSubtractRootJson.getDepotName() + "  商品id：" + entry.getKey()
								+ " 当前数据库没有冻结量");
						throw new Exception("商家为：" + invetAddOrSubtractRootJson.getMerchantName() + " 仓库为："
								+ invetAddOrSubtractRootJson.getDepotName() + "  商品id：" + entry.getKey()
								+ " 当前数据库没有冻结量");
					}
				
			}
		}

		if (countFail > 0) {
			falg = false;
		}
		return falg;
	}
	
	
	
	/**
	 * 分组统计将相同商品id的解冻数据进行累加
	 * @param inventoryFreezeRootJson
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> countProductGroupNum(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson)throws Exception{
		int countNum=0;
		Map<String,Integer> reurnMap=new HashMap<String, Integer>();
		List<InvetAddOrSubtractGoodsListJson>   goodsList=	invetAddOrSubtractRootJson.getGoodsList();
		if(goodsList!=null&&goodsList.size()>0){
			for(InvetAddOrSubtractGoodsListJson  freeGoodsJson:goodsList){
				if("1".equals(freeGoodsJson.getOperateType())){//解冻
					if(reurnMap.containsKey(freeGoodsJson.getGoodsId())){
						countNum=reurnMap.get(freeGoodsJson.getGoodsId())+freeGoodsJson.getNum();
						reurnMap.put(freeGoodsJson.getGoodsId(), countNum);
					}else{
						reurnMap.put(freeGoodsJson.getGoodsId(), freeGoodsJson.getNum());
					}
				}
			}
		}
		return reurnMap;
	}
	
	
	/**
	 *  扣减月结库存量
	 * @param monthlyAModel
	 * @param invetAddOrSubtractRootJson
	 * @param goodsListVo
	 * @param delOrAddNum
	 * @throws Exception
	 */
	public void inventoryMonthLower(MonthlyAccountModel monthlyAModel,InvetAddOrSubtractRootJson invetAddOrSubtractRootJson,InvetAddOrSubtractGoodsListJson goodsListVo,int delOrAddNum)throws Exception{
		List<MonthlyAccountItemModel>	monAccountItemModelList = null;
		// 调减(扣减的都是好品)
		// 扣减处理月结详情如果在月结库存详情找不到或者库存量不够扣减就不处理（挂起）

		MonthlyAccountItemModel monAccountItemModel = new MonthlyAccountItemModel();
		monAccountItemModel.setMonthlyAccountId(monthlyAModel.getId());
		monAccountItemModel.setGoodsId(Long.valueOf(goodsListVo.getGoodsId()));
		// 如果接口没有传扣减商品类型，默认扣减好品的库存数里
		if (StringUtils.isNotBlank(goodsListVo.getType())) {
			monAccountItemModel.setType(goodsListVo.getType());
		} else {
			monAccountItemModel.setType("0");// 好品
		}
		
		if ("d".equals(invetAddOrSubtractRootJson.getDepotType())) {// 在途仓

			monAccountItemModel = monthlyAccountItemDao
					.searchByModel(monAccountItemModel);
			if (monAccountItemModel != null) {
				if (monAccountItemModel.getSurplusNum() >delOrAddNum) {
				
					delOrAddNum = monAccountItemModel.getSurplusNum() - delOrAddNum;
					monAccountItemModel.setSurplusNum(delOrAddNum);
					int num = monthlyAccountItemDao.modify(monAccountItemModel);
					if (num > 0) {

					} else {
						LOGGER.error("更新结算月份为：" + monthlyAModel.getSettlementMonth()+"仓库为："+invetAddOrSubtractRootJson.getDepotName()
								+ ",商品货号为：" + goodsListVo.getGoodsNo()+ " 失败！");
						throw new Exception("更新结算月份为："
								+ monthlyAModel.getSettlementMonth() + ",商品货号为："+"仓库为："+invetAddOrSubtractRootJson.getDepotName()
								+ goodsListVo.getGoodsNo() + " 失败！");
					}
				}else if(monAccountItemModel.getSurplusNum()==delOrAddNum){
					int num=monthlyAccountItemDao.delMonthlyAccountItem(monAccountItemModel.getId());
					if(num>0){
						
					}else{
						LOGGER.error("删除结算月份为：" + monthlyAModel.getSettlementMonth()+"仓库为："+invetAddOrSubtractRootJson.getDepotName()
						+ ",商品货号为：" + goodsListVo.getGoodsNo()+ " 失败！");
				throw new Exception("删除结算月份为："
						+ monthlyAModel.getSettlementMonth() + ",商品货号为："+"仓库为："+invetAddOrSubtractRootJson.getDepotName()
						+ goodsListVo.getGoodsNo() + " 失败！");
					}
				} else {
					LOGGER.error("结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth()
							+ ",仓库为：" + invetAddOrSubtractRootJson.getDepotName()
							+ ",商品货号为：" + goodsListVo.getGoodsNo() + " 当前 扣减库存量 大于月结详情的库存量！");
					throw new Exception("结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth() + ",仓库为："
							+ invetAddOrSubtractRootJson.getDepotName() + ",商品货号为：" + goodsListVo.getGoodsNo() + " 当前 扣减库存量 大于月结详情的库存量！");
				}

			} else {
				LOGGER.error("结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth()
						+ ",仓库为：" + invetAddOrSubtractRootJson.getDepotName()
						+ ",商品货号为：" + goodsListVo.getGoodsNo() + "在月结详情不存在！");
				throw new Exception(
						"结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth() + ",仓库为："
								+ invetAddOrSubtractRootJson.getDepotName()
								+ ",商品货号为：" + goodsListVo.getGoodsNo() + "在月结详情不存在！");
			}

		} else {// 是否是代销仓
            if(StringUtils.isNotBlank(goodsListVo.getBatchNo())){
            	monAccountItemModel.setBatchNo(goodsListVo.getBatchNo());
            }

		  monAccountItemModelList = monthlyAccountItemDao.listOrbyOverdueDate(monAccountItemModel);
			if (monAccountItemModelList != null&&monAccountItemModelList.size()>0) {//月结库存先失效先出
				for(MonthlyAccountItemModel itemModel:monAccountItemModelList){
					 if(delOrAddNum>itemModel.getSurplusNum() ){
						delOrAddNum =delOrAddNum- itemModel.getSurplusNum();
						int num = monthlyAccountItemDao.delMonthlyAccountItem(itemModel.getId());
						if (num > 0) {
						} else {
							LOGGER.error("更新结算月份为：" + monthlyAModel.getSettlementMonth()
									+ ",商品货号为：" + goodsListVo.getGoodsNo() + ",批次为："
									+ goodsListVo.getBatchNo() + " 失败！");
							throw new Exception("更新结算月份为："
									+ monthlyAModel.getSettlementMonth() + ",商品货号为："
									+ goodsListVo.getGoodsNo() + ",批次为："
									+ goodsListVo.getBatchNo() + " 失败！");
						}
					} else if (delOrAddNum<=itemModel.getSurplusNum() ) {
						int delNum=itemModel.getSurplusNum() - delOrAddNum;
						if(delNum==0){
							int num = monthlyAccountItemDao.delMonthlyAccountItem(itemModel.getId());
							if (num > 0) {
							} else {
								LOGGER.error("更新结算月份为：" + monthlyAModel.getSettlementMonth()
										+ ",商品货号为：" + goodsListVo.getGoodsNo() + ",批次为："
										+ goodsListVo.getBatchNo() + " 失败！");
								throw new Exception("更新结算月份为："
										+ monthlyAModel.getSettlementMonth() + ",商品货号为："
										+ goodsListVo.getGoodsNo() + ",批次为："
										+ goodsListVo.getBatchNo() + " 失败！");
							}
						}else{
							itemModel.setSurplusNum(delNum);
							int num = monthlyAccountItemDao.modify(itemModel);
							if (num > 0) {
							} else {
								LOGGER.error("更新结算月份为：" + monthlyAModel.getSettlementMonth()
										+ ",商品货号为：" + goodsListVo.getGoodsNo() + ",批次为："
										+ goodsListVo.getBatchNo() + " 失败！");
								throw new Exception("更新结算月份为："
										+ monthlyAModel.getSettlementMonth() + ",商品货号为："
										+ goodsListVo.getGoodsNo() + ",批次为："
										+ goodsListVo.getBatchNo() + " 失败！");
							}
						}
						break;
					}
				}
			} else {
				LOGGER.error("结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth()
				+ ",仓库为：" + invetAddOrSubtractRootJson.getDepotName()
				+ ",商品货号为：" + goodsListVo.getGoodsNo() + "在月结详情不存在！");
		       throw new Exception(
				"结算月份为：" + invetAddOrSubtractRootJson.getOwnMonth() + ",仓库为："
						+ invetAddOrSubtractRootJson.getDepotName()
						+ ",商品货号为：" + goodsListVo.getGoodsNo() + "在月结详情不存在！");
			}
		}
	}
	
	
	
	
	

}
