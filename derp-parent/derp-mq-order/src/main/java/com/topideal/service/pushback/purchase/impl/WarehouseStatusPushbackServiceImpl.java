package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.common.CommonBusinessService;
import com.topideal.service.pushback.purchase.WarehouseStatusPushbackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进仓状态回推
 *
 * @author zhanghx
 */
@Service
public class WarehouseStatusPushbackServiceImpl implements WarehouseStatusPushbackService {

	protected Logger logger = LoggerFactory.getLogger(WarehouseStatusPushbackServiceImpl.class);

	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	@Autowired
	private CommonBusinessService commonBusinessService;
	@Autowired
	private DeclareOrderDao declareOrderDao;// 预申报单
	@Autowired
	private DeclareOrderItemDao declareOrderItemDao;// 预申报单
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;// 预申报表体

	/**
	 * 采购 进仓状态回推
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201152900, model = DERP_LOG_POINT.POINT_13201152900_Label, keyword = "code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);

		// 获取入库单编码
		String code = rootJson.getCode();
		PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
		purchaseWarehouseModel.setCode(code);
		purchaseWarehouseModel = purchaseWarehouseDao.searchByModel(purchaseWarehouseModel);

		/**1.检查入库单*/
		if(purchaseWarehouseModel==null){
			logger.info("入库单未找到,订单编号" + code);
			throw new RuntimeException("入库单未找到,订单编号" + code);
		}
		if(!purchaseWarehouseModel.getState().equals(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028)){
			logger.info("入库单状态："+purchaseWarehouseModel.getState()+",非入库中结束");
			throw new RuntimeException("入库单状态："+purchaseWarehouseModel.getState()+",非入库中结束");
		}
		/**2.修改入库单状态*/
		PurchaseWarehouseModel model = new PurchaseWarehouseModel();
		model.setId(purchaseWarehouseModel.getId());
		model.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);// 已入仓
		model.setWarehouseDate(TimeUtils.getNow());// 入仓时间
		purchaseWarehouseDao.modify(model);

		/**3.修改预申报状态、上架时间 申报单对应的所有入库单都已入库*/
		if(purchaseWarehouseModel.getDeclareOrderId()!=null){
			//根据预申报id获取各商品已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("declareOrderId", purchaseWarehouseModel.getDeclareOrderId());
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			Integer warehouseNum = 0;//预申报已入库数量
			for(Map<String, Object> numMap: numList){
				BigDecimal queryNum = (BigDecimal) numMap.get("num");
				warehouseNum += queryNum.intValue();
			}
			logger.debug("-------已入库量："+warehouseNum+"-------");
			//获取预申报数量
			DeclareOrderItemModel declareItemModel = new DeclareOrderItemModel();
			declareItemModel.setDeclareOrderId(purchaseWarehouseModel.getDeclareOrderId());
			List<DeclareOrderItemModel> declareItemList = declareOrderItemDao.list(declareItemModel);
			Integer declareNum = declareItemList.stream().filter(d->d.getNum() != null).mapToInt(DeclareOrderItemModel::getNum).sum();
			logger.debug("-------预申报量："+declareNum+"-------");
			boolean flag = true;//全部已入仓 true-是 false-否
			if(warehouseNum.intValue() != declareNum.intValue()){
				flag = false;
			}

            if(flag ==true){
				DeclareOrderModel declareModel = new DeclareOrderModel();
				declareModel.setId(purchaseWarehouseModel.getDeclareOrderId());
				declareModel.setShelfDate(purchaseWarehouseModel.getInboundDate());//上架时间
				declareModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_003);//已上架
				declareOrderDao.modify(declareModel);
			}

		}

		/**4.修改采购单状态*/
		WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
		relModel.setWarehouseId(purchaseWarehouseModel.getId());
		relModel = warehouseOrderRelDao.searchByModel(relModel);
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setId(relModel.getPurchaseOrderId());

		// 统计采购单剩余未入库量
		List<Map<String,Object>> itemGooodsNumList = purchaseWarehouseDao.getNoInWarehouseNum(purchaseOrderModel.getId());
		if(itemGooodsNumList!=null&&itemGooodsNumList.size()>0){
			purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_005);//部分入库
		}else {
			purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);//已入库
			purchaseOrderModel.setEndDate(TimeUtils.getNow());// 完结时间
			purchaseOrderModel.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
		}
		purchaseOrderDao.modify(purchaseOrderModel);

		PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
		purchaseOrder.setId(relModel.getPurchaseOrderId());
		purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);

		//获取本位币金额、单价
		if(purchaseOrder.getRate() == null) {
			commonBusinessService.saveRate(purchaseOrder, purchaseWarehouseModel.getInboundDate());
		}
		//发送金额调整邮件
		commonBusinessService.sendAmountComfirmMail(purchaseOrderModel.getId(), purchaseWarehouseModel.getId()) ;

	}

}
