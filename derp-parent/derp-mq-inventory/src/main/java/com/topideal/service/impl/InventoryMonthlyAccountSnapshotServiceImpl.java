package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.dao.MonthlyAccountSnapshotDao;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.service.InventoryMonthlyAccountSnapshotService;

import net.sf.json.JSONObject;

/**
 * 生成已经月结的月结账单快照
 * yangchuang  2019-08-12
 */
@Service
public class InventoryMonthlyAccountSnapshotServiceImpl implements InventoryMonthlyAccountSnapshotService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMonthlyAccountSnapshotServiceImpl.class);
	@Autowired
	private MonthlyAccountSnapshotDao monthlyAccountSnapshotDao;//月结账单快照
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;//月结账单
	@Autowired
	private MonthlyAccountItemDao monthlyAccountItemDao;//月结明细
	@Autowired
	private RMQProducer rocketMQProducer;//mq


	
	/**
	 * 生成已经月结的月结账单快照
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201305200, model = DERP_LOG_POINT.POINT_13201305200_Label)
	public boolean synsMonthlyAccountSnapshot(String json, String keys, String topics, String tags) throws Exception {
		LOGGER.info("生成已经月结的月结账单快照开始"+json);
		Thread.sleep(20000);// 睡眠20秒
		JSONObject jsonData = JSONObject.fromObject(json);
		Long merchantId = Long.valueOf(jsonData.getString("merchantId"));//商家id
		Long depotId = Long.valueOf(jsonData.getString("depotId"));// 仓库id
		String settlementMonth = jsonData.getString("settlementMonth");// 结转月份
		
		// 根据商家 仓库 结转月份 查询月结账单表
		MonthlyAccountModel monthlyAccountModel =new MonthlyAccountModel();
		monthlyAccountModel.setMerchantId(merchantId);
		monthlyAccountModel.setDepotId(depotId);
		monthlyAccountModel.setSettlementMonth(settlementMonth);
		List<MonthlyAccountModel> monthlyList = monthlyAccountDao.list(monthlyAccountModel);
		if (monthlyList==null||monthlyList.size()==0) {
			throw new RuntimeException("没有查询到月结账单");
		}
		monthlyAccountModel=monthlyList.get(0);
		if (DERP_INVENTORY.MONTHLYACCOUNT_STATE_1.equals(monthlyAccountModel.getState())) {
			throw new RuntimeException("月结账单状态是未转结");
		}
		
		// 根据月结id 查询月结明细
		MonthlyAccountItemModel monthlyAccountItemModel =new MonthlyAccountItemModel();
		monthlyAccountItemModel.setMonthlyAccountId(monthlyAccountModel.getId());
		List<MonthlyAccountItemModel> itemList = monthlyAccountItemDao.list(monthlyAccountItemModel);
		if (itemList==null||itemList.size()==0) {
			throw new RuntimeException("没有查询到月结账单商品明细");
		}

		// 根据 商家仓库 月结月份查询 月结快照表 结转状态
		MonthlyAccountSnapshotModel monthlyAccountSnapshotModel=new MonthlyAccountSnapshotModel();
		monthlyAccountSnapshotModel.setMerchantId(merchantId);
		monthlyAccountSnapshotModel.setDepotId(depotId);
		monthlyAccountSnapshotModel.setSettlementMonth(settlementMonth);
		monthlyAccountSnapshotModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2);//'状态：1未转结 2 已转结',
		List<MonthlyAccountSnapshotModel> itemSnapshotList = monthlyAccountSnapshotDao.list(monthlyAccountSnapshotModel);
		
		// 报表库要删除的月结商品明细ids
		List<Long> idsDel=new ArrayList();
		// 如果已结转的 月结快照已经存在 删除 并删除报表库
		if (itemSnapshotList.size()>0) {
			for (MonthlyAccountSnapshotModel itemSnapshot : itemSnapshotList) {
				idsDel.add(itemSnapshot.getId());
			}
			//删除已经月结的 月结商品明细
			monthlyAccountSnapshotDao.delete(idsDel);
		}
		
		List<MonthlyAccountSnapshotModel> monthlyAccountSnapshotList=new ArrayList<>();
		// 生成已经月结商品明细 
		for (MonthlyAccountItemModel item : itemList) {
			MonthlyAccountSnapshotModel snapshotModel=new MonthlyAccountSnapshotModel();
			snapshotModel.setMerchantId(monthlyAccountModel.getMerchantId());//商家ID
			snapshotModel.setMerchantName(monthlyAccountModel.getMerchantName());//商家名称
			snapshotModel.setSettlementMonth(monthlyAccountModel.getSettlementMonth());//结转月份
			snapshotModel.setDepotId(monthlyAccountModel.getDepotId());//仓库id
			snapshotModel.setDepotName(monthlyAccountModel.getDepotName()); //仓库名称
			snapshotModel.setGoodsId(item.getGoodsId());//商品id
			snapshotModel.setGoodsName(item.getGoodsName());//商品名称
			snapshotModel.setGoodsNo(item.getGoodsNo());//商品货号
			snapshotModel.setBatchNo(item.getBatchNo());//批次号
			snapshotModel.setProductionDate(item.getProductionDate());//生产日期
			snapshotModel.setOverdueDate(item.getOverdueDate());//失效日期
			snapshotModel.setSurplusNum(item.getSurplusNum());//库存余量
			snapshotModel.setAvailableNum(item.getAvailableNum());//现库存
			snapshotModel.setType(item.getType());//库存类型  0 正常品  1 残次品
			snapshotModel.setCreateDate(TimeUtils.getNow());//创建时间
			snapshotModel.setCreater(monthlyAccountModel.getCreater());//创建人
			snapshotModel.setUnit(item.getUnit());//库存单位
			snapshotModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2);//'状态：1未转结 2 已转结',
			snapshotModel.setCommbarcode(item.getCommbarcode());// 标准条码
			monthlyAccountSnapshotList.add(snapshotModel);

		}
		
		// 新增月结快照
		for (MonthlyAccountSnapshotModel monthlysnapshotModel : monthlyAccountSnapshotList) {
			Long save = monthlyAccountSnapshotDao.save(monthlysnapshotModel);
		}
		LOGGER.info("生成已经月结的月结账单快照结束");
		return true;
	}

	
	
	

	

}
