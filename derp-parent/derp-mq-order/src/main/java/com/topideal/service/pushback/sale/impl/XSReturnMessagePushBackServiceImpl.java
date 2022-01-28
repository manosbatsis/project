/*package com.topideal.service.pushback.sale.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleAnalysisDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.entity.vo.sale.SaleAnalysisModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSReturnMessagePushBackService;

import net.sf.json.JSONObject;

*//**
 * 退运信息库存扣减成功回推
 * 2019/02/27
 * 杨创
 *//*
@Service
public class XSReturnMessagePushBackServiceImpl implements XSReturnMessagePushBackService {
	 打印日志 
	private static final Logger LOGGER = LoggerFactory.getLogger(XSReturnMessagePushBackServiceImpl.class);
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售出库清单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库清单对应的商品
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 销售出库分析表
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;

	
	
	@Override
	@SystemServiceLog(point="13201153340",model="销售订单退运信息库存回推",keyword="code")
	public boolean updateReturnMessagePushBackInfo(String json,String keys,String topics,String tags) throws Exception {
		Thread.sleep(50);		
		// 库存传来的是销售订单的code 不论是否是菜鸟仓的
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");	
				
		//获取销售订单
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setCode(code);
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);		
		if (saleOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,订单号:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,订单号:" + code);
		}
		if (null == saleOrderModel.getBuId()) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单订单号:" + code+"事业部信息值为空");
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单订单号:" + code+"事业部信息值为空");
		}
		
		// 根据销售订单id 查询销售出库单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();				
		saleOutDepotModel.setSaleOrderId(saleOrderModel.getId());
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售出库订单,订单号:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售出库订单,订单号:" + code);
		}
		
		
		// 根据销售出库单id查询销售出库订单商品
		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> list = saleOutDepotItemDao.list(saleOutDepotItemModel);	
		
		Map<Long,Integer> totalOutNum = new HashMap<Long,Integer>();
		for (SaleOutDepotItemModel itemModel : list) {
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			saleOrderItemModel.setGoodsId(itemModel.getGoodsId());
			saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);
			if (saleOrderItemModel==null) {
				LOGGER.error("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + itemModel.getGoodsNo());
				throw new RuntimeException("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + itemModel.getGoodsNo());
			}
			
			Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(saleOrderModel.getId(),itemModel.getGoodsId());
			totalOutNum.put(itemModel.getId(), totalOutDepotNum);
			//生成差异分析信息
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
			saleAnalysisModel = saleAnalysisDao.searchByModel(saleAnalysisModel);
			Integer transferNum = itemModel.getTransferNum()==null?0:itemModel.getTransferNum();// 好品数量
			Integer wornNum = itemModel.getWornNum()==null?0:itemModel.getWornNum();	// 坏品数量
			Integer totalNum = transferNum+wornNum;
			if(saleAnalysisModel != null){
				if(saleAnalysisModel.getOutDepotNum() != null){
					totalNum = totalNum+=saleAnalysisModel.getOutDepotNum();
				}
				saleAnalysisModel.setIsEnd("0");
				saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
				saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
				saleAnalysisModel.setOutDepotNum(totalNum);
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum()-totalNum);
				saleAnalysisDao.modify(saleAnalysisModel);
			}else{
				saleAnalysisModel = new SaleAnalysisModel();
				saleAnalysisModel.setCreateDate(TimeUtils.getNow());
				saleAnalysisModel.setCustomerId(saleOrderModel.getCustomerId());
				saleAnalysisModel.setCustomerName(saleOrderModel.getCustomerName());
				saleAnalysisModel.setEndDate(TimeUtils.getNow());
				saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
				saleAnalysisModel.setGoodsName(itemModel.getGoodsName());
				saleAnalysisModel.setGoodsNo(itemModel.getGoodsNo());
				saleAnalysisModel.setIsEnd("0");
				saleAnalysisModel.setOrderCode(saleOrderModel.getCode());
				saleAnalysisModel.setOrderId(saleOrderModel.getId());
				saleAnalysisModel.setOutDepotNum(totalNum);
				saleAnalysisModel.setSaleNum(saleOrderItemModel.getNum());
				saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
				saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum() - totalNum);
				saleAnalysisModel.setMerchantId(saleOrderModel.getMerchantId());
				saleAnalysisModel.setMerchantName(saleOrderModel.getMerchantName());
				saleAnalysisModel.setBuId(saleOrderModel.getBuId()); // 事业部Id
				saleAnalysisModel.setBuName(saleOrderModel.getBuName());// 事业部名称
				saleAnalysisModel.setSaleType(saleOrderModel.getBusinessModel());//销售类型 1购销 2代销
				saleAnalysisDao.save(saleAnalysisModel);
			}
		}
		// 修改销售出库单
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);// '状态 017,待出库,018,已出库'
		sOutDepotModel.setId(saleOutDepotModel.getId());
		saleOutDepotDao.modify(sOutDepotModel);		
		//判断是否能自动完结(不能影响其他流程的运转)
		boolean flag = true;//是否完结    true：完结    false：未完结
		try{
			//获取销售订单所有的商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			List<SaleOrderItemModel> sItemList = saleOrderItemDao.list(saleOrderItemModel);
			//获取销售订单对应的所有出库单
			SaleOutDepotModel saleOutDepot = new SaleOutDepotModel();
			saleOutDepot.setSaleOrderId(saleOrderModel.getId());
			List<SaleOutDepotModel> saleOutDepotList = saleOutDepotDao.list(saleOutDepot);
			//遍历，计算销售订单中所有商品与对应的出库单（1对多，数量累加）出库的数量一致
			for(SaleOrderItemModel item:sItemList){
				Integer outNum = 0;//某商品出库总量
				for(SaleOutDepotModel saleOutDepot1:saleOutDepotList){
					SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
					saleOutDepotItem.setSaleOutDepotId(saleOutDepot1.getId());
					saleOutDepotItem.setGoodsId(item.getGoodsId());
					List<SaleOutDepotItemModel> saleOutDepotItemList = saleOutDepotItemDao.list(saleOutDepotItem);
					for(SaleOutDepotItemModel sodItem:saleOutDepotItemList){
						outNum +=sodItem.getTransferNum();
					}
				}
				if(item.getNum() != outNum){
					flag = false;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		// 修改销售订单 为已出库saleOrderModel
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
		if(flag){
			sModel.setState(DERP_ORDER.SALEORDER_STATE_007);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
			sModel.setEndDate(TimeUtils.getNow());	// 设置已完结时间
			//修改差异分析表状态
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			for(SaleAnalysisModel saleAnalysis :saleAnalysisList){
				saleAnalysis.setIsEnd("1");
				saleAnalysis.setEndDate(TimeUtils.getNow());
				saleAnalysisDao.modify(saleAnalysis);
			}
		}else{
			// 修改销售订单 为已出库saleOrderModel
			SaleOrderModel sModel = new SaleOrderModel();
			sModel.setId(saleOrderModel.getId());
			sModel.setState(DERP_ORDER.SALEORDER_STATE_018);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
		}
		// 修改销售订单 为已出库saleOrderModel
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
		sModel.setState(DERP_ORDER.SALEORDER_STATE_018);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
		sModel.setModifyDate(TimeUtils.getNow());	// 设置修改时间
		saleOrderDao.modify(sModel);
		return true;
	}
}
*/