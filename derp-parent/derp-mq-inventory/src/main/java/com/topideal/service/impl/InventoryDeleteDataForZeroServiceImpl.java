package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchBackupDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.entity.vo.InventoryBatchBackupModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.service.InventoryDeleteDataForZeroService;

import net.sf.json.JSONObject;


/**
 *   删除批次库存余量为0的数据
 * @author baols
 *
 */
@Service
public class InventoryDeleteDataForZeroServiceImpl implements InventoryDeleteDataForZeroService {

	  /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDeleteDataForZeroServiceImpl.class);

	@Autowired
	private InventoryBatchDao  inventoryBatchDao;//批次库存明细
	
	@Autowired
	private InventoryBatchBackupDao  inventoryBatchBackupDao;
	@Autowired
	private RMQProducer rocketMQProducer;//mq
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201302400,model=DERP_LOG_POINT.POINT_13201302400_Label,keyword="orderNo")
	public boolean deleteInventoryDeleteDataForZero(String json,String keys,String topics,String tags) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("==========删除批次库存余量为0的数据接口=================>"+json);
	    
		List<Long> ids=new ArrayList<>();// 库存余量为0要删除的批次库存id
		InventoryBatchModel  model=new  InventoryBatchModel();
		List<InventoryBatchModel> inventoryBatchList=inventoryBatchDao.getInventoryBatchModelByZero(model);
		if(inventoryBatchList!=null&&inventoryBatchList.size()>0){
			for(InventoryBatchModel inventoryBatchModel:inventoryBatchList ){
					InventoryBatchBackupModel inventoryBatchBackupModel=new InventoryBatchBackupModel();
					inventoryBatchBackupModel.setId(inventoryBatchModel.getId());
					inventoryBatchBackupModel.setMerchantId(inventoryBatchModel.getMerchantId());
					inventoryBatchBackupModel.setMerchantName(inventoryBatchModel.getMerchantName());
					inventoryBatchBackupModel.setIsTopBooks(inventoryBatchModel.getIsTopBooks());
					inventoryBatchBackupModel.setDepotId(inventoryBatchModel.getDepotId());
					inventoryBatchBackupModel.setDepotName(inventoryBatchModel.getDepotName());
					inventoryBatchBackupModel.setGoodsId(inventoryBatchModel.getGoodsId());
					inventoryBatchBackupModel.setGoodsName(inventoryBatchModel.getGoodsName());
					inventoryBatchBackupModel.setBatchNo(inventoryBatchModel.getBatchNo());
					inventoryBatchBackupModel.setSurplusNum(inventoryBatchModel.getSurplusNum());
					inventoryBatchBackupModel.setCreateDate(inventoryBatchModel.getCreateDate());
					inventoryBatchBackupModel.setUnit(inventoryBatchModel.getUnit());
				    inventoryBatchBackupModel.setProductionDate(inventoryBatchModel.getProductionDate());
					inventoryBatchBackupModel.setOverdueDate(inventoryBatchModel.getOverdueDate());
					inventoryBatchBackupModel.setTopidealCode(inventoryBatchModel.getTopidealCode());
					inventoryBatchBackupModel.setDepotCode(inventoryBatchModel.getDepotCode());
					inventoryBatchBackupModel.setDepotType(inventoryBatchModel.getDepotType());
					inventoryBatchBackupModel.setType(inventoryBatchModel.getType());
					inventoryBatchBackupModel.setIsExpire(inventoryBatchModel.getIsExpire());
					inventoryBatchBackupModel.setGoodsNo(inventoryBatchModel.getGoodsNo());
					inventoryBatchBackupModel.setOwnMonth(inventoryBatchModel.getOwnMonth());
					inventoryBatchBackupModel.setLpn(inventoryBatchModel.getLpn());
					inventoryBatchBackupModel.setIsTopBooks(inventoryBatchModel.getIsTopBooks());
					inventoryBatchBackupModel.setBarcode(inventoryBatchModel.getBarcode());
					inventoryBatchBackupModel.setBrandId(inventoryBatchModel.getBrandId());
					inventoryBatchBackupModel.setBrandName(inventoryBatchModel.getBrandName());
					inventoryBatchBackupModel.setBackupDate(TimeUtils.getNow());//当前时间
					
					Long id = inventoryBatchBackupDao.save(inventoryBatchBackupModel);
					if(id!=null){
						ids.add(inventoryBatchModel.getId());
					   int num=	inventoryBatchDao.delInventoryBatch(inventoryBatchModel.getId());
					   if(num>0){
					   }else{
						   LOGGER.error("删除库存余量为null或为0的批次库存数据失败");
							throw new Exception("删除库存余量为null或为0的批次库存数据失败");
					   }
					}else{
						LOGGER.error("批次库存删除数据备份失败");
						throw new Exception("批次库存删除数据备份失败");
					}
				
			}
			
			// 通知报表删除批次为0的批次库存数据
			// 推送报表库删除 报表库批次库存
			/*JSONObject reportJSONObject=new JSONObject();
			reportJSONObject.put("ids", ids.toString());// 加减明细ids
			reportJSONObject.put("source", "2");// 1.来源库存加减明细 2.批次库存
			reportJSONObject.put("describe", "根据批次库存ids删除报表批次库存数据(多个逗号隔开)");// 来源库存
			LOGGER.info("推送报表库删除报表库批次库存数据"+reportJSONObject.toString());
			// 推送报表消费端
			SendResult sendResult = rocketMQProducer.send(reportJSONObject.toString(),MQReportEnum.DEL_REPORT_DATAS.getTopic(),
					MQReportEnum.DEL_REPORT_DATAS.getTags());*/
			
		}
		return true;
	}

	
 


}
