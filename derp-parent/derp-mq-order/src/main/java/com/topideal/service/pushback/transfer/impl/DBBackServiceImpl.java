package com.topideal.service.pushback.transfer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.transfer.TransferInDepotDao;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.dao.transfer.TransferOutDepotDao;
import com.topideal.entity.vo.transfer.TransferInDepotModel;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.entity.vo.transfer.TransferOutDepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.transfer.DBBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DBBackServiceImpl implements DBBackService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DBBackServiceImpl.class);
	
	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨单
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;// 调拨出库单
	
	/**
	 * 调拨库存回调通知出库
	 * */
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201153100,model=DERP_LOG_POINT.POINT_13201153100_Label,keyword="code")
	public boolean synsOutDepotBack(String json,String keys,String topics,String tags) throws Exception {
		//睡眠100毫秒 解决以出定入场景出库通知回来时出库单事务还未提交
		 Thread.sleep(100);
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
	    // JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		
		//查询调拨出库单
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
		transferOutDepotModel.setCode(rootJson.getCode());
		transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
		if(transferOutDepotModel==null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "调拨出库单不存在"+rootJson.getCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "调拨出库单不存在"+rootJson.getCode());
		}
		//查询调拨单
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		transferOrderModel.setCode(transferOutDepotModel.getTransferOrderCode());
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
		if(transferOrderModel==null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "调拨单不存在"+transferOutDepotModel.getTransferOrderCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "调拨单不存在"+transferOutDepotModel.getTransferOrderCode());
		}
		//修改出库单状态
		transferOutDepotModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016);//已出仓
		transferOutDepotDao.modify(transferOutDepotModel);
		
		// 检查调拨入库单是否已存在 更新调拨单状态
		TransferInDepotModel inModel = new TransferInDepotModel();
		inModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferInDepotModel> list = transferInDepotDao.list(inModel);
		/*TransferInDepotModel inDepotModel = null;
		if(list!=null&&list.size()>0) inDepotModel = list.get(0);*/
		if(list!=null&&list.size()>0) {
			transferOrderModel.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_007);// 已完结
		} else {
			transferOrderModel.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_023);// 调拨已出库
		}
		transferOrderDao.modify(transferOrderModel);
		return true;
	}
	/**
	 * 调拨库存回调通知入库
	 * */
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201153000,model=DERP_LOG_POINT.POINT_13201153000_Label,keyword="code")
	public boolean synsInDepotBack(String json,String keys,String topics,String tags) throws Exception {
		//睡眠100毫秒 解决以出定入场景出入库通知同时回来时出入库单状态都是出库中或入库存中导致调拨单状态未更新成已完结
		 Thread.sleep(100);
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
	    // JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		
		//查询调拨入库单
		TransferInDepotModel tInDepotModel =new TransferInDepotModel();
		tInDepotModel.setCode(rootJson.getCode());
		tInDepotModel = transferInDepotDao.searchByModel(tInDepotModel);
		if(tInDepotModel==null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "调拨入库单不存在"+rootJson.getCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "调拨入库单不存在"+rootJson.getCode());
		}
		//查询调拨单
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		transferOrderModel.setCode(tInDepotModel.getTransferOrderCode());
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
		if(transferOrderModel==null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "调拨单不存在"+tInDepotModel.getTransferOrderCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "调拨单不存在"+tInDepotModel.getTransferOrderCode());
		}
		//修改调拨入库单 状态 
		tInDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_012);//已入仓
		transferInDepotDao.modify(tInDepotModel);
		
		// 检查调拨出库单是否已存在 更新调拨单状态
		TransferOutDepotModel outModel = new TransferOutDepotModel();
		outModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOutDepotModel> list = transferOutDepotDao.list(outModel);
		/*TransferOutDepotModel outDepotModel = null;
		if(list!=null&&list.size()>0) outDepotModel = list.get(0);*/
		if(list!=null&&list.size()>0) {
			transferOrderModel.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_007);// 已完结
		} else {
			transferOrderModel.setStatus(DERP_ORDER.TRANSFERORDER_STATUS_024);//调拨已入库
		}
		transferOrderDao.modify(transferOrderModel);
		return true;
	}
}
