package com.topideal.service.api.purchase.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.DeclareOrderDao;
import com.topideal.dao.purchase.TallyingItemBatchDao;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.dao.purchase.TallyingOrderItemDao;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.json.api.v1_1.PurchaseFirstTallyBatchListJson;
import com.topideal.json.api.v1_1.PurchaseFirstTallyGoodsListJson;
import com.topideal.json.api.v1_1.PurchaseFirstTallyRootJson;
import com.topideal.service.api.purchase.PurchaseFirstTallyService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 初理货 业务
 * @author zhanghx
 * 2018/7/16
 */
@Service
public class PurchaseFirstTallyServiceImpl implements PurchaseFirstTallyService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseFirstTallyServiceImpl.class);

	@Autowired
	private DeclareOrderDao declareOrderDao;// 预申报单
	@Autowired
	private TallyingOrderDao tallyingOrderDao;// 理货单
	@Autowired
	private TallyingOrderItemDao tallyingOrderItemDao;// 理货单商品
	@Autowired
	private TallyingItemBatchDao itemBatchDao;// 理货单商品批次详情

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203110000, model = DERP_LOG_POINT.POINT_12203110000_Label,keyword="entInboundId")
	public boolean saveTallyInfo(String json, String keys,String topics,String tags) throws Exception {
		/**
		 * 说明: 举例 比如一个预申报单300件货 只会把300件货完全理完后再推初理货接口 不会出现(第一次推100件
		 * ,第二次又推200件的情况) 也就是说:一个预申报单只会对应一个 理货单状态是确认的理货单 一个确认状态的理货单只会对应一个入库申报单
		 */
		// 实例化JSON对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",PurchaseFirstTallyGoodsListJson.class);
		classMap.put("receiveList",PurchaseFirstTallyBatchListJson.class);
		// JSON对象转实体
		PurchaseFirstTallyRootJson rootJson = (PurchaseFirstTallyRootJson) JSONObject.toBean(jsonData, PurchaseFirstTallyRootJson.class, classMap);
		
		
		/**************** 后面根据单号进行匹配 *******************/

		// 根据预申报单查询采购单信息
		DeclareOrderModel declareModel = new DeclareOrderModel();
		declareModel.setCode(rootJson.getEntInboundId());// 预申报单编码
		//declareModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);//已审核
		declareModel.setMerchantId(rootJson.getMerchantId());
		declareModel = declareOrderDao.searchByModel(declareModel);

		if (null == declareModel) {
			LOGGER.error("预申报单编码不存在");
			throw new RuntimeException("预申报单编码不存在");
		}
		if(!(DERP_ORDER.DECLAREORDER_STATUS_003.equals(declareModel.getStatus()) || DERP_ORDER.DECLAREORDER_STATUS_004.equals(declareModel.getStatus()))) {
			LOGGER.error("预申报单状态不为待入仓或已上架："+declareModel.getStatus());
			throw new RuntimeException("预申报单状态不为待入仓或已上架");
		}

		// 根据理货单编码查询理货单信息
		TallyingOrderModel tModel = new TallyingOrderModel();
		tModel.setCode(rootJson.getTallyingOrderCode());// 理货单编码
		tModel.setMerchantId(rootJson.getMerchantId());
		tModel = tallyingOrderDao.searchByModel(tModel);
		// 理货单存在就补能在插入了
		if (null != tModel) {
			LOGGER.error("理货单已存在" + rootJson.getTallyingOrderCode());
			throw new RuntimeException("理货单已存在" + rootJson.getTallyingOrderCode());
		}

		TallyingOrderModel tallyingOrder = new TallyingOrderModel();
		tallyingOrder.setCode(rootJson.getTallyingOrderCode());// 理货单号
		// 采购
		tallyingOrder.setOrderId(declareModel.getId());// 订单id
		tallyingOrder.setDepotId(declareModel.getDepotId());// 仓库id
		tallyingOrder.setDepotName(declareModel.getDepotName());// 仓库名称
		tallyingOrder.setContractNo(declareModel.getContractNo());// 合同号
		tallyingOrder.setMerchantId(declareModel.getMerchantId());// 商家id
		tallyingOrder.setMerchantName(declareModel.getMerchantName());// 商家名称
		tallyingOrder.setType(DERP_ORDER.TALLYINGORDER_TYPE_1);// 类型 1 采购 2调拨
		tallyingOrder.setBuId(declareModel.getBuId());
		tallyingOrder.setBuName(declareModel.getBuName());

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

		// 根据商品编码查询商品信息
		for (PurchaseFirstTallyGoodsListJson goodsListJson : rootJson.getGoodsList()) {
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
			tallyingOrderItemModel.setTallyingUnit(goodsListJson.getTallyingUnit());//理货单位
			// 理货单商品条形码 待定
			// 向理货单商品详情插入数据
			Long itemId = tallyingOrderItemDao.save(tallyingOrderItemModel);
			
			// 根据商品编码查询商品信息
			for (PurchaseFirstTallyBatchListJson batchListJson : goodsListJson.getReceiveList()) {
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
