package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseWarehouseDao;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.common.CommonBusinessService;
import com.topideal.service.pushback.purchase.OnTheWayPushbackService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * 采购 中转仓入库 
 * @author zhanghx
 */
@Service
public class OnTheWayPushbackServiceImpl implements OnTheWayPushbackService {

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private WarehouseOrderRelDao warehouseOrderRelDao;
    @Autowired
    private PurchaseWarehouseDao purchaseWarehouseDao;
    @Autowired
    private CommonBusinessService commonBusinessService ;

	/**
	 * 在途仓入库 
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201152800, model = DERP_LOG_POINT.POINT_13201152800_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws SQLException {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		
		// 获取入库单编码
		String code = rootJson.getCode();
		// 获取入库单系信息
		PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
		purchaseWarehouse.setCode(code);
		purchaseWarehouse = purchaseWarehouseDao.searchByModel(purchaseWarehouse);
		// 获取勾稽关系
		WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
		relModel.setWarehouseId(purchaseWarehouse.getId());
		relModel = warehouseOrderRelDao.searchByModel(relModel);
		// 获取采购单信息
		PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());
		
		if (purchaseOrder.getRate() == null) {
			commonBusinessService.saveRate(purchaseOrder, purchaseWarehouse.getInboundDate());
		}
		
		// 修改入库单状态 
		PurchaseWarehouseModel pwModel = new PurchaseWarehouseModel();
		pwModel.setId(purchaseWarehouse.getId());
		pwModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);//已入仓
		pwModel.setWarehouseDate(TimeUtils.getNow());//入仓时间
		pwModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_2);
		purchaseWarehouseDao.modify(pwModel);
		
		// 修改采购单状态
		PurchaseOrderModel model = new PurchaseOrderModel();
		model.setId(purchaseOrder.getId());
		model.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
		model.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
		model.setEndDate(TimeUtils.getNow());
		purchaseOrderDao.modify(model);
		
		//勾稽已完结
		//commonBusinessService.modifyAnilysisEnd(model);
		
		//发送金额调整邮件
		commonBusinessService.sendAmountComfirmMail(purchaseOrder.getId(), purchaseWarehouse.getId()) ;
	}
	
}
