package com.topideal.service.pushback.sale.impl;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSLoadingDeliveriesPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售装载交运库存扣减成功回推
 * @author 杨创
 * 2019/02/27
 */
@Service
public class XSLoadingDeliveriesPushBackServiceImpl implements XSLoadingDeliveriesPushBackService {


	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XSLoadingDeliveriesPushBackServiceImpl.class);
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售出库单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库订单商品
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 销售出库分析表
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;



	@Override
	@SystemServiceLog(point="13201153301",model="销售订单出库成功 库存回推",keyword="code")
	public void updateXSLoadingDeliveriesPushBackInfo(String json,String keys,String topics,String tags) throws Exception {
		/**
		 * 说明:只接受服务类型为10 的单
		 */
		Thread.sleep(100);
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");//出库单号

		// 根据 销售订单号编码查询销售出库清单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setSaleOrderCode(code);// 销售出库清单编码
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
		}
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setCode(saleOutDepotModel.getSaleOrderCode());
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if (saleOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "订单不存在,订单编号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "订单不存在,订单编号code:" + code);
		}
		/*if (null == saleOrderModel.getBuId()) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
		}*/

		// 根据销售出库单id查询销售出库订单商品
		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> list = saleOutDepotItemDao.list(saleOutDepotItemModel);
		Map<Long,Integer> totalOutNum = new HashMap<Long,Integer>();
		for (SaleOutDepotItemModel itemModel : list) {
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = saleOrderItemDao.searchById(itemModel.getSaleItemId());
			if (saleOrderItemModel==null) {
				LOGGER.error("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + itemModel.getGoodsNo());
				throw new RuntimeException("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + itemModel.getGoodsNo());
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",itemModel.getSaleItemId());
			Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);
			totalOutNum.put(itemModel.getId(), totalOutDepotNum);

			Integer transferNum = itemModel.getTransferNum()==null?0:itemModel.getTransferNum();// 好品数量
			Integer wornNum = itemModel.getWornNum()==null?0:itemModel.getWornNum();	// 坏品数量
			int totalNum = transferNum+wornNum;	// 总量
			//生成差异分析信息

			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setCreateDate(TimeUtils.getNow());
			saleAnalysisModel.setCustomerId(saleOrderModel.getCustomerId());
			saleAnalysisModel.setCustomerName(saleOrderModel.getCustomerName());
			saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
			saleAnalysisModel.setGoodsName(itemModel.getGoodsName());
			saleAnalysisModel.setGoodsNo(itemModel.getGoodsNo());
			saleAnalysisModel.setIsEnd("0");
			saleAnalysisModel.setOrderCode(saleOrderModel.getCode());
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			saleAnalysisModel.setOutDepotNum(totalNum);	// 好品+坏品
			saleAnalysisModel.setSaleNum(saleOrderItemModel.getNum());
			saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
			saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
			saleAnalysisModel.setSurplus(0);
			saleAnalysisModel.setMerchantId(saleOrderModel.getMerchantId());
			saleAnalysisModel.setMerchantName(saleOrderModel.getMerchantName());
			saleAnalysisModel.setBuId(saleOrderModel.getBuId()); // 事业部Id
			saleAnalysisModel.setBuName(saleOrderModel.getBuName());// 事业部名称
			saleAnalysisModel.setSaleType(saleOrderModel.getBusinessModel());//销售类型 1购销 2代销
			//差异分析表 海外仓添加理货单位
			saleAnalysisModel.setTallyingUnit(itemModel.getTallyingUnit());
			saleAnalysisModel.setSaleItemId(itemModel.getSaleItemId());
			saleAnalysisDao.save(saleAnalysisModel);
		}
		// 修改销售出库单
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);// 状态 017,待出库,018,已出库
		sOutDepotModel.setId(saleOutDepotModel.getId());
		saleOutDepotDao.modify(sOutDepotModel);

		// 修改销售订单 为已出库saleOrderModel
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
		sModel.setState(DERP_ORDER.SALEORDER_STATE_018);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
		sModel.setModifyDate(TimeUtils.getNow());	// 设置修改时间
		saleOrderDao.modify(sModel);


		// 如果是预售转销
		if ("1".equals(saleOrderModel.getOrderType())) {
			// 按商品分组查询销售出库单商品
			List<Map<String, Object>> itemMapList = saleOutDepotItemDao.getItemGroupByOutId(saleOutDepotModel.getId());
			List<PreSaleOrderCorrelationModel> preSaleList = preSaleOrderCorrelationDao.getPreSaleOrderCorList(saleOutDepotModel,saleOrderModel,itemMapList);
			for (PreSaleOrderCorrelationModel preSale : preSaleList) {
				preSaleOrderCorrelationDao.save(preSale);
			}
		}
		//根据销售订单id，更新对应商品差异分析结余量
		for(SaleOutDepotItemModel item : list){
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",item.getSaleItemId());
			Integer outDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//已出库数量

			SaleAnalysisModel saleAnalysisUpdateModel = new SaleAnalysisModel();
			saleAnalysisUpdateModel.setOrderId(saleOrderModel.getId());
			saleAnalysisUpdateModel.setGoodsId(item.getSaleItemId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisUpdateModel);
			for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
				saleAnalysis.setSurplus(item.getSaleNum() - outDepotNum);
				saleAnalysisDao.modify(saleAnalysis);
			}
		}

		if(saleOutDepotModel.getSaleDeclareOrderId()!=null) {
			//查询预申报单关联的所有出库单
			SaleOutDepotModel outModel = new SaleOutDepotModel();
			outModel.setSaleDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
			List<SaleOutDepotModel> saleOutList = saleOutDepotDao.list(outModel);
			//若预申报关联的所有出库单都已出库则更新预申报单状态为已出库
			boolean flagAll = true;
			boolean declareAllShlef = true;
			for(SaleOutDepotModel outDepotModel : saleOutList){
				if(saleOutDepotModel.getId().equals(outDepotModel.getId())) continue;
				if(outDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_017)
						||outDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_027)){
					flagAll = false;
				}
			}
			if(flagAll==true){
				//更新预申报单状态为已出库
				SaleDeclareOrderModel saleDeclare = new SaleDeclareOrderModel();
				saleDeclare.setId(saleOutDepotModel.getSaleDeclareOrderId());
				saleDeclare = saleDeclareOrderDao.searchByModel(saleDeclare);
				if(saleDeclare.getStatus().equals(DERP_ORDER.SALEDECLARE_STATUS_008)){
					saleDeclare.setStatus(DERP_ORDER.SALEDECLARE_STATUS_004);//已出库
					saleDeclare.setModifyDate(TimeUtils.getNow());
					saleDeclareOrderDao.modify(saleDeclare);
				}
			}
		}

	}

}
