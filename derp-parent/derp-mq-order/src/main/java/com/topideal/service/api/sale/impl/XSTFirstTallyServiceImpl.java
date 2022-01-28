package com.topideal.service.api.sale.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.TallyingItemBatchDao;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.dao.purchase.TallyingOrderItemDao;
import com.topideal.dao.sale.SaleReturnDeclareOrderDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderModel;
import com.topideal.json.api.v2_8.SaleReturnFirstTallyBatchListJson;
import com.topideal.json.api.v2_8.SaleReturnFirstTallyGoodsListJson;
import com.topideal.json.api.v2_8.SaleReturnFirstTallyRootJson;
import com.topideal.service.api.sale.XSTFirstTallyService;

import net.sf.json.JSONObject;

/**
 * 销售退货 --初理货
 * 
 * @author 杨创 2018.07.26
 */
@Service
public class XSTFirstTallyServiceImpl implements XSTFirstTallyService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTFirstTallyServiceImpl.class);

	
	@Autowired
	private TallyingOrderDao tallyingOrderDao;// 理货单
	@Autowired
	private TallyingOrderItemDao tallyingOrderItemDao;// 理货单商品
	@Autowired
	private TallyingItemBatchDao itemBatchDao;// 理货单商品批次详情
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;// 销售退货订单
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120000, model = DERP_LOG_POINT.POINT_12203120000_Label,keyword = "entInboundId")
	public boolean saveTallyInfo(String json, String keys,String topics,String tags) throws Exception {

		// 实例化JSON对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleReturnFirstTallyGoodsListJson.class);
		classMap.put("receiveList",SaleReturnFirstTallyBatchListJson.class);
		// JSON对象转实体
		SaleReturnFirstTallyRootJson rootJson = (SaleReturnFirstTallyRootJson) JSONObject.toBean(jsonData,
				SaleReturnFirstTallyRootJson.class,classMap);

		/**************** 后面根据单号进行匹配 *******************/

		// 根据单号查询 销售退货单
		SaleReturnDeclareOrderModel declare= new SaleReturnDeclareOrderModel();
		declare.setCode(rootJson.getEntInboundId());// 销售退货预申报
		declare = saleReturnDeclareOrderDao.searchByModel(declare);

		if (null == declare) {
			LOGGER.error("销售退货预申报不存在 entInboundId"+rootJson.getEntInboundId());
			throw new RuntimeException("销售退货预申报不存在 entInboundId"+rootJson.getEntInboundId());
		}
		if (DERP_ORDER.SALERETURNORDER_STATUS_001.equals(declare.getStatus())) {
			LOGGER.error("订单状态为“待审核”,订单编号" + declare.getCode());
			throw new RuntimeException("订单状态为“待审核”,订单编号" + declare.getCode());
		}
		// 若销售退中事业部字段为空，则报错
		if(null == declare.getBuId()){
			LOGGER.error("销售退货预审报,订单编号" + declare.getCode()+"事业部的值为空");
			throw new RuntimeException("销售退货预审报,订单编号" + declare.getCode()+"事业部的值为空");
		}
		// 根据理货单编码查询理货单信息 查询已确认状态的理货单
		TallyingOrderModel tModel = new TallyingOrderModel();
		tModel.setCode(rootJson.getTallyingOrderCode());// 理货单编码
		tModel = tallyingOrderDao.searchByModel(tModel);
		
		// 理货单存在就补能在插入了
		if (null != tModel) {
			LOGGER.error("理货单已存在" + rootJson.getTallyingOrderCode());
			throw new RuntimeException("理货单已存在" + rootJson.getTallyingOrderCode());
		}

		TallyingOrderModel tallyingOrder = new TallyingOrderModel();
		tallyingOrder.setCode(rootJson.getTallyingOrderCode());// 理货单号
		// 采购
		tallyingOrder.setOrderId(declare.getId());// 订单id
		tallyingOrder.setDepotId(declare.getInDepotId());// 仓库id
		tallyingOrder.setDepotName(declare.getInDepotName());// 仓库名称
		tallyingOrder.setContractNo(declare.getContractNo());// 合同号
		tallyingOrder.setMerchantId(declare.getMerchantId());// 商家id
		tallyingOrder.setMerchantName(declare.getMerchantName());// 商家名称
		tallyingOrder.setType("3");// 类型 1 采购 2调拨3 销售退货
		tallyingOrder.setBuId(declare.getBuId()); // 事业部
		tallyingOrder.setBuName(declare.getBuName()); 

		tallyingOrder.setOrderCode(rootJson.getEntInboundId());// 订单单编号
		String operaDate = rootJson.getTallyingDate();
		if (operaDate.length() == 10) {
			operaDate = operaDate + " 00:00:00";
			tallyingOrder.setTallyingDate(Timestamp.valueOf(operaDate));// 理货时间
		} else {
			tallyingOrder.setTallyingDate(Timestamp.valueOf(operaDate));// 理货时间
		}
		tallyingOrder.setState(DERP_ORDER.TALLYINGORDER_STATE_009);// "009","待确认"
		
		// 向理货单中插入数据
		Long id = tallyingOrderDao.save(tallyingOrder);

		for (SaleReturnFirstTallyGoodsListJson goodsListJson : rootJson.getGoodsList()) {
			// 理货单商品详情类
			TallyingOrderItemModel tallyingOrderItemModel = new TallyingOrderItemModel();
			tallyingOrderItemModel.setPurchaseNum(goodsListJson.getPurchaseNum());// 采购数量
			tallyingOrderItemModel.setTallyingNum(goodsListJson.getTallyingNum());// 理货数量
			tallyingOrderItemModel.setNormalNum(goodsListJson.getTotoalNormalNum());// 正常数量
			tallyingOrderItemModel.setMultiNum(goodsListJson.getMultiNum());// 多货数量
			tallyingOrderItemModel.setLackNum(goodsListJson.getLackNum());// 缺货数量
			tallyingOrderItemModel.setGoodsId(goodsListJson.getGoodsId());// 商品id
			tallyingOrderItemModel.setGoodsNo(goodsListJson.getGoodsNo());// 商品货号
			tallyingOrderItemModel.setGoodsName(goodsListJson.getGoodsName());// 商品名称
			tallyingOrderItemModel.setBarcode(goodsListJson.getBarcode());// 货品条形码
			// 毛重
			tallyingOrderItemModel.setGrossWeight(goodsListJson.getGrossWeight());// 货品毛重
			// 净重
			tallyingOrderItemModel.setNetWeight(goodsListJson.getNetWeight());// 货品净重
			// 体积
			tallyingOrderItemModel.setVolume(goodsListJson.getVolume());// 货品体积
			// 长
			tallyingOrderItemModel.setLength(goodsListJson.getLength());// 货品
			// 宽
			tallyingOrderItemModel.setWidth(goodsListJson.getWidth());// 货品
			// 高
			tallyingOrderItemModel.setHeight(goodsListJson.getHeight());// 货品
			tallyingOrderItemModel.setTallyingOrderId(id);// 理货单id
			// 理货单商品条形码 待定
			// 向理货单商品详情插入数据
			Long itemId = tallyingOrderItemDao.save(tallyingOrderItemModel);

			// 根据商品编码查询商品信息
			for (SaleReturnFirstTallyBatchListJson batchListJson : goodsListJson.getReceiveList()) {
				// 理货单商品批次详情
				TallyingItemBatchModel itemBatchModel = new TallyingItemBatchModel();
				itemBatchModel.setItemId(itemId);// 理货单商品详情id
				itemBatchModel.setGoodsId(goodsListJson.getGoodsId());// 理货单商品详情id
				itemBatchModel.setBatchNo(batchListJson.getBatchNo());// 批次号
				itemBatchModel.setWornNum(batchListJson.getWornNum());// 坏货数量
				itemBatchModel.setExpireNum(batchListJson.getExpireNum());// 过期数量
				itemBatchModel.setNormalNum(batchListJson.getNormalNum());// 正常数量
				String producedDate = batchListJson.getProductionDate();
				if (StringUtils.isNotBlank(producedDate)) {
					if (producedDate.length() == 10) {
						producedDate = producedDate + " 00:00:00";
						itemBatchModel.setProductionDate(Timestamp.valueOf(producedDate));// 生产日期
					} else {
						itemBatchModel.setProductionDate(Timestamp.valueOf(producedDate));// 生产日期
					}
				}
				
				String expiredDate = batchListJson.getOverdueDate();
				if (StringUtils.isNotBlank(expiredDate)) {
					if (expiredDate.length() == 10) {
						expiredDate = expiredDate + " 00:00:00";
						itemBatchModel.setOverdueDate(Timestamp.valueOf(expiredDate));// 失效日期
					} else {
						itemBatchModel.setOverdueDate(Timestamp.valueOf(expiredDate));// 失效日期
					}
				}
				
				// 插入理货单批次
				itemBatchDao.save(itemBatchModel);
			}
		}

		return true;
	}

}
