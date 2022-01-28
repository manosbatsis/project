package com.topideal.service.impl;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.InventoryFreeUnfreeOrderDao;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.dao.InventoryFreezeDetailsHistoryDao;
import com.topideal.entity.vo.InventoryFreeUnfreeOrderModel;
import com.topideal.entity.vo.InventoryFreezeDetailsHistoryModel;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.service.InventoryFreezeRollBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 单据回滚重新冻结库存
 */
@Service
public class InventoryFreezeRollBackServiceImpl implements InventoryFreezeRollBackService {

	  /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFreezeRollBackServiceImpl.class);

	// 冻结库存明细
	@Autowired
	private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;
	//冻结解冻历史
	@Autowired
	private InventoryFreezeDetailsHistoryDao inventoryFreezeDetailsHistoryDao;
	// 库存冻结和解冻接口来源单据表
	@Autowired
	private InventoryFreeUnfreeOrderDao inventoryFreeUnfreeOrderDao;


	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_20400000001,model=DERP_LOG_POINT.POINT_20400000001_Label,keyword="orderNo")
	public boolean saveInventoryfreezeRollBack(String json, String keys, String topics, String tags) throws Exception{
		JSONObject jsonObj = JSONObject.fromObject(json);
		String orderNo = (String) jsonObj.get("orderNo");
		String businessNo = (String) jsonObj.get("businessNo");

		//跟怒业务单号，来源单号查询是否存在解冻记录
		InventoryFreezeDetailsModel inventoryFreezeDetailsModel = new InventoryFreezeDetailsModel();
		inventoryFreezeDetailsModel.setOrderNo(orderNo);
		inventoryFreezeDetailsModel.setBusinessNo(businessNo);
		inventoryFreezeDetailsModel.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);
		List<InventoryFreezeDetailsModel> freezeList = inventoryFreezeDetailsDao.list(inventoryFreezeDetailsModel);
		if(freezeList ==null || freezeList.size() < 1){	//冻结记录表没有数据需要查历史表
			//判断业务单号在历史表是否存在，存在表示已完全解冻，把历史表的数据重新移回冻结记录表
			InventoryFreezeDetailsHistoryModel inventoryFreezeDetailsHistoryModel = new InventoryFreezeDetailsHistoryModel();
			inventoryFreezeDetailsHistoryModel.setBusinessNo(businessNo);
			List<InventoryFreezeDetailsHistoryModel> freezeHistoryList = inventoryFreezeDetailsHistoryDao.list(inventoryFreezeDetailsHistoryModel);
			if(freezeHistoryList != null && freezeHistoryList.size() > 0){
				List<Long> ids = freezeHistoryList.stream().map(InventoryFreezeDetailsHistoryModel::getId).collect(Collectors.toList());
				int num = inventoryFreezeDetailsDao.insertBath(ids);
				if(num > 0){
					inventoryFreezeDetailsHistoryDao.delete(ids);
				}
			}
			freezeList = inventoryFreezeDetailsDao.list(inventoryFreezeDetailsModel);
		}
		//删除业务单解冻数据
		if(freezeList != null && freezeList.size() > 0){
			List<Long> ids = freezeList.stream().map(InventoryFreezeDetailsModel::getId).collect(Collectors.toList());
			inventoryFreezeDetailsDao.delete(ids);

			InventoryFreeUnfreeOrderModel model = new InventoryFreeUnfreeOrderModel();
			model.setOrderNo(orderNo);
			model.setBusinessNo(businessNo);
			model.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);
			List<InventoryFreeUnfreeOrderModel> unFreeList =  inventoryFreeUnfreeOrderDao.list(model);
			if(unFreeList !=null && unFreeList.size() > 0) {
				List<Long> unFreeIds = unFreeList.stream().map(InventoryFreeUnfreeOrderModel::getId).collect(Collectors.toList());
				inventoryFreeUnfreeOrderDao.delete(unFreeIds);
			}

		}
		return true;
	}
}
