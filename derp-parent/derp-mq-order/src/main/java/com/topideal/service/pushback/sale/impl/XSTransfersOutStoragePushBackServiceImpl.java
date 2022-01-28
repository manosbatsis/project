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
import com.topideal.dao.sale.PreSaleOrderCorrelationDao;
import com.topideal.dao.sale.SaleAnalysisDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.entity.vo.sale.PreSaleOrderCorrelationModel;
import com.topideal.entity.vo.sale.SaleAnalysisModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSTransfersOutStoragePushBackService;

import net.sf.json.JSONObject;

*//**
 * 销售调拨出库加减成功回推 2019/02/27 杨创
 *//*
@Service
public class XSTransfersOutStoragePushBackServiceImpl implements XSTransfersOutStoragePushBackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(XSTransfersOutStoragePushBackServiceImpl.class);

	@Autowired
	private SaleOrderDao saleOrderDao;// 销售订单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;// 销售出库表
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;// 销售出库订单商品
	@Autowired
	private SaleOrderItemDao saleOrderItemDao; // 销售订单表体
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;// 销售出库分析表
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	@Override
	@SystemServiceLog(point="13201153500",model="销售订单调拨出库库存回推",keyword="code")
	public boolean updatexSTransfersOutStoragePushBackInfo(String json, String keys, String topics, String tags)
			throws Exception {
		Thread.sleep(1000);
		// 说明 无论销售出库是否是菜鸟仓 code 都是传的销售订单的code
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		// 根据销售订单号查询销售订单
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setCode(code);
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if (saleOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,订单号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,订单号code:" + code);
		}
		if (null == saleOrderModel.getBuId()) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单订单号code:" + code+"事业部信息值为空");
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单订单号code:" + code+"事业部信息值为空");
		}

		// 根据销售订单的code 查询销售出库单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setSaleOrderCode(code);
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号:" + code);
		}

		// 根据销售出库单id查询销售出库商品
		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());

		List<SaleOutDepotItemModel> list = saleOutDepotItemDao.list(saleOutDepotItemModel);
		Map<Long, Integer> totalOutNum = new HashMap<Long, Integer>();
		for (SaleOutDepotItemModel itemModel : list) {
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			saleOrderItemModel.setGoodsId(itemModel.getGoodsId());
			saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);
			if (saleOrderItemModel == null) {
				LOGGER.error("销售订单商品为空,订单号:code" + code);
				throw new RuntimeException("销售订单商品为空,订单号:code" + code);
			}
			Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(saleOrderModel.getId(),
					itemModel.getGoodsId());
			totalOutNum.put(itemModel.getId(), totalOutDepotNum);
			// 生成差异分析信息
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
			saleAnalysisModel = saleAnalysisDao.searchByModel(saleAnalysisModel);
			Integer transferNum = itemModel.getTransferNum()==null?0:itemModel.getTransferNum();// 好品数量
			Integer wornNum = itemModel.getWornNum()==null?0:itemModel.getWornNum();	// 坏品数量
			Integer totalNum = transferNum+wornNum;
			if (saleAnalysisModel != null) {
				if (saleAnalysisModel.getOutDepotNum() != null) {
					totalNum = totalNum += saleAnalysisModel.getOutDepotNum();
				}
				saleAnalysisModel.setIsEnd("0");
				saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
				saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
				saleAnalysisModel.setOutDepotNum(totalNum);
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum()-totalNum);
				saleAnalysisDao.modify(saleAnalysisModel);
			} else {
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
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum()-totalNum);
				saleAnalysisModel.setMerchantId(saleOrderModel.getMerchantId());
				saleAnalysisModel.setMerchantName(saleOrderModel.getMerchantName());
				saleAnalysisModel.setSaleType(saleOrderModel.getBusinessModel());// 销售类型
				saleAnalysisModel.setBuId(saleOrderModel.getBuId()); // 事业部Id
				saleAnalysisModel.setBuName(saleOrderModel.getBuName());// 事业部名称
				saleAnalysisDao.save(saleAnalysisModel);
			}
		}

		// 修改销售出库单
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setId(saleOutDepotModel.getId());
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);// 状态 017,待出库 ,018,已出库
		saleOutDepotDao.modify(sOutDepotModel);

		// 修改销售订单 为已出库saleOrderModel
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
		sModel.setState(DERP_ORDER.SALEORDER_STATE_018);// 订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
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
		
		return true;
	}

}
*/