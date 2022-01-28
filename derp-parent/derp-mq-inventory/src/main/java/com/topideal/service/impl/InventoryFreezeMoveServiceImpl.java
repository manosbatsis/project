package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.dao.InventoryFreezeDetailsHistoryDao;
import com.topideal.dao.InventoryFreezeDetailsMoveLogDao;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.entity.vo.InventoryFreezeDetailsMoveLogModel;
import com.topideal.service.InventoryFreezeMoveService;

/**
 * 移动已完结冻结解冻数据到历史表
 * 
 * @author zhanghx
 */
@Service
public class InventoryFreezeMoveServiceImpl implements InventoryFreezeMoveService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeMoveServiceImpl.class);

	//冻结解冻
	@Autowired
	private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;
	//收发明细
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;
	//冻结解冻移动
	@Autowired
	private InventoryFreezeDetailsMoveLogDao inventoryFreezeDetailsMoveLogDao;
	//冻结解冻历史
	@Autowired
	private InventoryFreezeDetailsHistoryDao inventoryFreezeDetailsHistoryDao;

	/**
	 * 移动 1、订单的冻结解冻数据一致的情况，移到历史表，删除当前表冻结解冻数据。
	 *  2、订单有冻结、解冻并且解冻数量大于冻结数量的，存入异常日志记录。
	 * 3、有冻结无解冻检查是否有收发明细，有则存入异常日志记录。
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201304400, model = DERP_LOG_POINT.POINT_13201304400_Label)
	public void synsMove(String json, String keys, String topics, String tags) throws Exception {
		Long startTime = new Date().getTime();
		LOGGER.info("===============移动已完结冻结解冻数据到历史表 开始===========");
		//先清空记录日志
		inventoryFreezeDetailsMoveLogDao.delAll();
		LOGGER.info("===============清空移动记录日志 结束===========");
		InventoryFreezeDetailsModel model = new InventoryFreezeDetailsModel();
		Integer begin = 0;
		Integer pageSize = 1000;
		while (true) {
			//分页获取冻结表的单号（去重）
			List<InventoryFreezeDetailsMoveLogModel> insertList = new ArrayList<InventoryFreezeDetailsMoveLogModel>();
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model = inventoryFreezeDetailsDao.getOrderNoByPage(model);
			List<InventoryFreezeDetailsModel> list = model.getList();
			if (list == null || list.size() == 0) {
				break;
			}
			/*
			 *  移动逻辑:
			 *  1、订单的冻结解冻数据一致的情况，移到历史表，删除当前表冻结解冻数据。
	         *  2、订单有冻结、解冻并且解冻数量大于冻结数量的，存入异常日志记录。
	         *  3、有冻结无解冻检查是否有收发明细，有则存入异常日志记录。
			 */
			for(InventoryFreezeDetailsModel detailsModel : list){
				//用于记录冻结商品数量
				Map<Long,Integer> freezeNumMap = new HashMap<Long,Integer>();
				//用于记录解冻商品数量
				Map<Long,Integer> freezeNumMap1 = new HashMap<Long,Integer>();
				//订单总冻结数量
				Integer totalNum = 0;
				//订单总解冻数量
				Integer totalNum1 = 0;
				//获取同业务单号的所有冻结记录
				InventoryFreezeDetailsModel ifdModel = new InventoryFreezeDetailsModel();
				ifdModel.setBusinessNo(detailsModel.getBusinessNo());
				ifdModel.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_0);
				List<InventoryFreezeDetailsModel> freezeList = inventoryFreezeDetailsDao.list(ifdModel);
				//统计商品冻结的数量、订单总冻结数量
				for(InventoryFreezeDetailsModel ifd:freezeList){
					Integer num = ifd.getNum();
					totalNum+= num;
					if(freezeNumMap.containsKey(ifd.getGoodsId())){
						num += freezeNumMap.get(ifd.getGoodsId());
					}
					freezeNumMap.put(ifd.getGoodsId(), num);
				}
				//获取同业务单号的所有解冻记录
				InventoryFreezeDetailsModel ifdModel1 = new InventoryFreezeDetailsModel();
				ifdModel1.setBusinessNo(detailsModel.getBusinessNo());
				ifdModel1.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);
				List<InventoryFreezeDetailsModel> freezeList1 = inventoryFreezeDetailsDao.list(ifdModel1);
				//统计商品解冻的数量、订单总解冻数量
				for(InventoryFreezeDetailsModel ifd:freezeList1){
					Integer num = ifd.getNum();
					totalNum1 += num;
					if(freezeNumMap1.containsKey(ifd.getGoodsId())){
						num += freezeNumMap1.get(ifd.getGoodsId());
					}
					freezeNumMap1.put(ifd.getGoodsId(), num);
				}
				//冻结解冻记录为空
				if(freezeList.size() == 0 && freezeList1.size() == 0){
					continue;
				}
				//定义一个标识用于判断冻结数量（等于解冻数量 ：0，大于解冻数量 ：1，小于解冻数量 ： 2，没有解冻记录 ：3）
				String tag = "0";
				for(Map.Entry<Long,Integer> entry:freezeNumMap.entrySet()){
					if(!"0".equals(tag)){//不相等，直接结束
						break;
					}
					//商品id
					Long goodsId = entry.getKey();
					//商品冻结数量
					Integer num = entry.getValue();
					//商品没有解冻记录或没有解冻数量
					if(!freezeNumMap1.containsKey(goodsId) || freezeNumMap1.get(goodsId).intValue() <= 0){
						tag = "3";
						break;
					}
					//商品解冻数量
					Integer num1 = freezeNumMap1.get(goodsId);
					if(num.intValue() == num1.intValue()){//相等
						tag = "0";
					}else if(num.intValue() > num1.intValue()){//冻结数量大于解冻数量
						tag = "2";
					}else{//冻结数量小于解冻数量
						tag = "1";
					}
				}
				/*
				 * 冻结的商品数量匹配解冻数量相等，判断总冻结数量是否小于总解冻数量
				 * （检测商品没有冻结，但是有解冻的场景）
				 */
				if("0".equals(tag) && totalNum < totalNum1){
					tag = "1";
				}
				//比较冻结与解冻的数量是否相等,并写入冻结解冻移动记录
				if("0".equals(tag)){//相等
					// 记录需要移动的冻结、解冻信息
					InventoryFreezeDetailsMoveLogModel logModel = new InventoryFreezeDetailsMoveLogModel();
					logModel.setType("1");//类型  1需要移动
					logModel.setOrderNo(detailsModel.getOrderNo());//来源单据号
					logModel.setBusinessNo(detailsModel.getBusinessNo());//业务单号
					logModel.setMerchantId(detailsModel.getMerchantId());//商家iD
					logModel.setMerchantName(detailsModel.getMerchantName());//商家名称
					logModel.setDepotId(detailsModel.getDepotId());//仓库id
					logModel.setDepotName(detailsModel.getDepotName());//仓库名称
					insertList.add(logModel);
				}else if("1".equals(tag)){//判断解冻数量大于冻结数量的
					// 记录异常信息
					InventoryFreezeDetailsMoveLogModel logModel = new InventoryFreezeDetailsMoveLogModel();
					logModel.setType("2");//类型  2异常
					logModel.setOrderNo(detailsModel.getOrderNo());//来源单据号
					logModel.setBusinessNo(detailsModel.getBusinessNo());//业务单号
					logModel.setMerchantId(detailsModel.getMerchantId());//商家iD
					logModel.setMerchantName(detailsModel.getMerchantName());//商家名称
					logModel.setDepotId(detailsModel.getDepotId());//仓库id
					logModel.setDepotName(detailsModel.getDepotName());//仓库名称
					logModel.setExpMsg("解冻数量大于冻结数量");
					insertList.add(logModel);
				}else if("3".equals(tag)){//商品有冻结记录，没有解冻记录或没有解冻数量
					// 有冻结无解冻检查是否有收发明细，有则存入异常日志记录。
					for(InventoryFreezeDetailsModel freezeItem :freezeList){
						InventoryDetailsModel inventoryDetailsModel = new InventoryDetailsModel();
						inventoryDetailsModel.setBusinessNo(freezeItem.getBusinessNo());
						inventoryDetailsModel.setGoodsNo(freezeItem.getGoodsNo());
						Map<String, Object> map = inventoryDetailsDao.countDetailsByOrderNo(inventoryDetailsModel);
						Long inventoryDetailsNum = 0L;
						if (map != null) {
							inventoryDetailsNum =  (Long) map.get("num");
						}
						if(inventoryDetailsNum > 0){
							// 记录异常信息
							InventoryFreezeDetailsMoveLogModel logModel = new InventoryFreezeDetailsMoveLogModel();
							logModel.setType("2");//类型  2异常
							logModel.setOrderNo(freezeItem.getOrderNo());//来源单据号
							logModel.setBusinessNo(freezeItem.getBusinessNo());//业务单号
							logModel.setMerchantId(freezeItem.getMerchantId());//商家id
							logModel.setMerchantName(freezeItem.getMerchantName());//商家名称
							logModel.setDepotId(freezeItem.getDepotId());//仓库id
							logModel.setDepotName(freezeItem.getDepotName());//仓库名称
							logModel.setExpMsg("有冻结无解冻，有收发明细");
							insertList.add(logModel);
						}
					}
				}
			}
			// 新增到日志记录
			if(insertList!=null && insertList.size()>0){
				inventoryFreezeDetailsMoveLogDao.insertBatch(insertList);
			}
			begin += pageSize;
		}
		// 把状态为1移动的，新增到历史表
		inventoryFreezeDetailsHistoryDao.insertHistory();
		InventoryFreezeDetailsMoveLogModel moveLogModel = new InventoryFreezeDetailsMoveLogModel();
		moveLogModel.setType("1");
		List<InventoryFreezeDetailsMoveLogModel> moveLogList = inventoryFreezeDetailsMoveLogDao.list(moveLogModel);
		if(moveLogList!=null && moveLogList.size()>0){
			// 删除冻结
			inventoryFreezeDetailsDao.delByMoveLogType();
		}
	    // 删除状态为1的移动日志
		inventoryFreezeDetailsMoveLogDao.delByType();
		LOGGER.info("===============移动已完结冻结解冻数据到历史表 结束===========");
		Long endTime = new Date().getTime();
		LOGGER.info("===============共耗时："+((endTime-startTime)/1000)+"秒===========");
	}
}
