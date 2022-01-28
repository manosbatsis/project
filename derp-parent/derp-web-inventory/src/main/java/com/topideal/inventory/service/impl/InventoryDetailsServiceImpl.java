package com.topideal.inventory.service.impl;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.*;
import com.topideal.entity.dto.InventoryDetailsDTO;
import com.topideal.entity.vo.*;
import com.topideal.inventory.service.InventoryDetailsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-商品收发明细-service实现类
 */
@Service
public class InventoryDetailsServiceImpl implements InventoryDetailsService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(InventoryDetailsServiceImpl.class);
	//商品收发明细dao
    @Autowired
    private InventoryDetailsDao inventoryDetailsDao;
    @Autowired
    private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;
    @Autowired
    private InventoryAddSubOrderDao inventoryAddSubOrderDao;
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;//月结账单
	@Autowired
	private InventoryBatchDao inventoryBatchDao;// 批次库存
	@Autowired
	private InventoryBatchBackupDao inventoryBatchBackupDao;// 批次库存备份表
	@Autowired
	private RMQProducer rocketMQProducer;//mq

    /**
	 * 商品收发明细列表（分页）
	 * @param model
	 * @return
	 */
    @Override
    public InventoryDetailsDTO listInventoryDetails(InventoryDetailsDTO model)throws SQLException {
        return inventoryDetailsDao.getlistByPage(model);
    }

    /**
     * 导出商品收发明细
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> exportInventoryDetailsMap(InventoryDetailsDTO model ) throws Exception {
		// TODO Auto-generated method stub
		return inventoryDetailsDao.exportInventoryDetailsMap(model);
	}

	@Override
	public void delInventoryDetail(String codes) throws Exception {
		String[] codeArr = codes.split(",");
		for(String code : codeArr) {
			LOGGER.info("库存数据回滚:"+ code);

			if (StringUtils.isBlank(code)) {
				throw new RuntimeException("库存数据回滚 单号不能为null");
			}
			//三、唯一单号校验
			InventoryAddSubOrderModel submodel=new InventoryAddSubOrderModel();
			submodel.setOrderNo(code);
			submodel.setSourceType("2");//来源类型 1-库存扣减 2-库存回滚
			submodel.setCreateDate(TimeUtils.getNow());
			try{
				inventoryAddSubOrderDao.save(submodel);
			}catch(Exception e){
				LOGGER.error("单号已存在,库存回滚失败："+code);
				throw new RuntimeException("单号已存在,库存回滚失败："+code);
			}

			InventoryDetailsModel inventoryDetailsModel =new InventoryDetailsModel();
			inventoryDetailsModel.setOrderNo(code);
			List<InventoryDetailsModel> list = inventoryDetailsDao.list(inventoryDetailsModel);
			if (list==null||list.size()<=0) {
//				// 没有加减明细 说明 库存扣减失败 进行如下处理 1.删除冻结冻结数据 2.推送业务删除电商订单单据
//				boolean dsddOperation = dsddOperation(code);
//				if (code.startsWith("DSDDTH"))throw new RuntimeException("根据库存数据回滚的单号没有查到加减明细数据,单号:"+code);// 电商订单退货抛异常
//				// 电商订单 没有查到加减明细 防止 从库存日志重推
//				if (code.startsWith("DSDD")||code.startsWith("ZDCK")||code.startsWith("ZDRK")){
//					InventoryAddSubOrderModel addSub=new InventoryAddSubOrderModel();
//					addSub.setOrderNo(code);
//					addSub.setSourceType("1");//来源类型 1-库存扣减 2-库存回滚
//					addSub.setCreateDate(TimeUtils.getNow());
//					try{
//						inventoryAddSubOrderDao.save(addSub);
//					}catch(Exception e){
//						LOGGER.info("库存回滚加减明细不存在防止从日志监控从推："+code);
//					}
//					return;// 电商订单回滚不抛异常
//
//				}
//				throw new RuntimeException("根据库存数据回滚的单号没有查到加减明细数据,单号:"+code);
				//出库中的单据没有加减明细，直接返回
				InventoryAddSubOrderModel addSub=new InventoryAddSubOrderModel();
				addSub.setOrderNo(code);
				addSub.setSourceType("1");//来源类型 1-库存扣减 2-库存回滚
				addSub.setCreateDate(TimeUtils.getNow());
				inventoryAddSubOrderDao.save(addSub);
				return;
			}
			// 核实是否月结 拿其中一条
			InventoryDetailsModel inventoryDetails = list.get(0);
			//1、检查商家、仓库、月份是否已结转
			MonthlyAccountModel account = new MonthlyAccountModel();
			account.setMerchantId(Long.valueOf(inventoryDetails.getMerchantId()));
			account.setDepotId(inventoryDetails.getDepotId());
			account.setSettlementMonth(inventoryDetails.getOwnMonth());
			List<MonthlyAccountModel> accountList = monthlyAccountDao.getMonthlyAccount(account);
			if(accountList!=null&&accountList.size()>0&&StringUtils.isNotBlank(accountList.get(0).getState())
					&&accountList.get(0).getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)){//状态：1未转结 2 已转结
				String message = "单号："+inventoryDetails.getOrderNo()+inventoryDetails.getDepotName()
						+inventoryDetails.getOwnMonth()+"已结转,不能回滚";
				LOGGER.error(message);
				throw new RuntimeException(message);
			}

			// 如果是扣减 用于统计看现在是否库存是否足够扣减回滚
			Map<String, Integer> reduceNumMap=new HashMap<>();
			// 算批次库存数量
			Map<String, Integer> inventoryBatchNumMap=new HashMap<>();

			//用于 批次库存不存在需要从备份表获取的 批次信息的id
			Map<String, String> inventoryBatchBackupIdMap=new HashMap<>();
			//(存在批次id为空的不能回滚) 数据校验
			for (InventoryDetailsModel model : list) {
				Long inventoryBatchId=model.getInventoryBatchId();
				String operateType = model.getOperateType();
				//批次id为空不能回滚
				if (inventoryBatchId==null) {
					throw new RuntimeException(model.getMerchantName()+"商家,单号orderNo:"+model.getOrderNo()+",商品货号:"+model.getGoodsNo()+"批次库存字段为空不能回滚");
				}
				//根据 批次库存id 查询批次库存表
				InventoryBatchModel inventoryBatchModel = inventoryBatchDao.searchById(inventoryBatchId);

				// 如果加减明细是调增 那么回滚调减 需要校验  库存明细是否足够回滚
				if (operateType.equals(DERP_INVENTORY.INVENTORY_OPERATETYPE_1)) {

					if (inventoryBatchModel==null) {
						throw new RuntimeException(model.getMerchantName()+"商家,单号orderNo:"+model.getOrderNo()+",商品货号:"+model.getGoodsNo()+"回滚要调减没有对应的批次库存信息不能回滚");
					}
					String key=inventoryBatchId.toString();
					Integer num = model.getNum();
					//
					if (reduceNumMap.containsKey(key)) {
						Integer reduceNum = reduceNumMap.get(key);
						reduceNum=reduceNum+num;
						reduceNumMap.put(key, reduceNum);
					}else {
						reduceNumMap.put(key, num);
					}
					// 调减批次库存量
					inventoryBatchNumMap.put(key, inventoryBatchModel.getSurplusNum());

				}

				// 批次库存备份表
				InventoryBatchBackupModel inventoryBatchBackupModel=null;
				if (inventoryBatchModel==null) {
					inventoryBatchBackupModel = inventoryBatchBackupDao.searchById(inventoryBatchId);
				}
				// 批次库存备份表 复制到 批次库存的数据
				if (inventoryBatchBackupModel!=null) {
					inventoryBatchBackupIdMap.put(inventoryBatchId.toString(), inventoryBatchId.toString());
				}
				// 如果批次库存和批次库存快照表都未空无法回滚
				if (inventoryBatchModel==null&&inventoryBatchBackupModel==null) {
					throw new RuntimeException(model.getMerchantName()+"商家,单号orderNo:"+model.getOrderNo()+",商品货号:"+model.getGoodsNo()+"在批次库存和批次库存备份表都没有对应的数据");
				}
			}

			// 校验批次库存是否足够扣减
			for (String key : reduceNumMap.keySet()) {
				Integer inventoryBatchNum = inventoryBatchNumMap.get(key);// 库存批次量
				Integer reduceNum = reduceNumMap.get(key);// 回滚要扣减的量
				// 如果扣减量大于批次库存的量
				if (reduceNum.intValue()>inventoryBatchNum.intValue()) {
					throw new RuntimeException("单号orderNo:"+code+"批次库存id:"+key+"加减明细的扣减量"+reduceNum+"大于目前批次库存的量"+inventoryBatchNum);
				}

			}

			// 用于存储批次库存备份表 回滚到
			List backupIds =new ArrayList<>();
			// 把批次库存快照数据复制到批次库存中 设置数量为0
			for (String key : inventoryBatchBackupIdMap.keySet()) {
				// 库存回滚 把批次库存备份中数据移回批次库存 把剩余数量设置为0
				inventoryBatchBackupDao.remveBackInventoryBatchBackup(Long.valueOf(key));
				backupIds.add(Long.valueOf(key));

			}
			// 删除已经移走批次库存备份表数据
			if (backupIds.size()>0) {
				inventoryBatchBackupDao.delete(backupIds);
			}

			// 数据回滚
			//
			List<Long> ids=new ArrayList<>();
			for (InventoryDetailsModel model2 : list) {
				Long inventoryBatchId=model2.getInventoryBatchId();
				String operateType = model2.getOperateType();
				//上面已经校验批次库存 是否存在了现在只要新增就好了
				//InventoryBatchModel inventoryBatchModel = inventoryBatchDao.searchById(inventoryBatchId);

				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(operateType)) {// 原来为增现在调减
					//扣减库存
					Map<String,Object>  parMap=new HashMap<String,Object>();
					parMap.put("onWayNum", model2.getNum());
					parMap.put("id", inventoryBatchId);
					int num =inventoryBatchDao.updateLowerGoodsNum(parMap);

				}
				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {// 原来调减现在调增
					Map<String,Object>  parMap=new HashMap<String,Object>();
					parMap.put("onWayNum", model2.getNum());
					parMap.put("id", inventoryBatchId);
					int num = inventoryBatchDao.updateAddGoodsNum(parMap);
				}
				// 加减明细id集合(用于批量删除)
				ids.add(model2.getId());
			}
			// 删除 加减明细

			for (InventoryDetailsModel model3 : list) {
				inventoryDetailsDao.delete(ids);
			}

			// 推送报表库删除 报表库加减明细
			/*JSONObject reportJSONObject=new JSONObject();
			reportJSONObject.put("ids", ids.toString());// 加减明细ids
			reportJSONObject.put("source", "1");// 1.来源库存加减明细 2.批次库存,3,采购订单表体 4,销售订单表体,5调拨订单表体
			reportJSONObject.put("describe", "根据加减明细ids删除报表加减明细(多个逗号隔开)");// 来源库存

			LOGGER.info("推送报表库删除报表库加减明细"+reportJSONObject.toString());
			// 推送报表消费端
			SendResult sendResult = rocketMQProducer.send(reportJSONObject.toString(),MQReportEnum.DEL_REPORT_DATAS.getTopic(),
					MQReportEnum.DEL_REPORT_DATAS.getTags());*/
			//回滚成功电商订单回滚操作
//			boolean dsddOperation = dsddOperation(code);
		}

	}
	/**
	 * 电商订单回滚操作
	 * @param code
	 * @throws Exception
	 *
	 */
//	private boolean dsddOperation(String code) throws Exception {
//		// 如果是电商订单
//		if (!(code.startsWith("DSDD")||code.startsWith("ZDCK")||code.startsWith("ZDRK")))return true;
//		if (code.startsWith("DSDDTH"))return true;
//		// 1.删除冻结解冻数据
//		InventoryFreezeDetailsModel freezeModel= new InventoryFreezeDetailsModel();
//		freezeModel.setOrderNo(code);
//		List<InventoryFreezeDetailsModel> freezeList = inventoryFreezeDetailsDao.list(freezeModel);
//		List ids =new ArrayList<>();
//		for (InventoryFreezeDetailsModel model : freezeList) {
//			ids.add(model.getId());
//		}
//		if (ids.size()>0) {// 删除冻结解冻
//			inventoryFreezeDetailsDao.delete(ids);
//		}
//		//source 1 电商订单 2账单出入库
//		String source="1";
//		if (code.startsWith("DSDD")) {
//			source="1";
//		}else if (code.startsWith("ZDCK")||code.startsWith("ZDRK")) {
//			source="2";
//		}
//
//		//2.推送业务删除订单单据
//		JSONObject oderJSONObject=new JSONObject();
//		oderJSONObject.put("orderNo", code);// 加减明细ids
//		oderJSONObject.put("source", source);// 1 电商订单 2账单出入库
//		oderJSONObject.put("describe", "业务库订单订单回滚数据");
//
//		LOGGER.info("推送业务库订单订单回滚数据"+oderJSONObject.toString());
//		// 推送报表消费端
//		SendResult sendorderResult = rocketMQProducer.send(oderJSONObject.toString(),MQOrderEnum.ORDERDATA_ROLLBACK.getTopic(),
//				MQOrderEnum.ORDERDATA_ROLLBACK.getTags());
//
//		return true;
//	}


}
