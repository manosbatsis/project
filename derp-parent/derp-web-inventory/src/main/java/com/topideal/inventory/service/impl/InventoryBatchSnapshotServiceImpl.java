package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchSnapshotDao;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
import com.topideal.inventory.service.InventoryBatchSnapshotService;

/**
 * 库存批次快照
 * @author 联想302
 *
 */
@Service
public class InventoryBatchSnapshotServiceImpl implements InventoryBatchSnapshotService {

	@Autowired
	private InventoryBatchSnapshotDao  inventoryBatchSnapshotDao;
	
	@Autowired
	private InventoryRealTimeSnapshotDao realTimeSnapshotDao;
	
	@Override
	public InventoryBatchSnapshotDTO listInventoryBatchSnapshotModel(InventoryBatchSnapshotDTO model)
			throws SQLException {
		InventoryBatchSnapshotDTO batchSnapshotModel=inventoryBatchSnapshotDao.getListByPage(model);
		List<InventoryBatchSnapshotDTO> list = batchSnapshotModel.getList();
		if(list!=null&&list.size()>0){
			for(InventoryBatchSnapshotDTO batch:list){
				//从实时库存快照表获取仓库现存量字段 商家id、仓库id、商品id、批次号、效期、好/坏品、理货单位、快照日期
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchant_id", batch.getMerchantId());
				paramMap.put("depot_id", batch.getDepotId());
				paramMap.put("goods_id", batch.getGoodsId());
				//菜鸟仓
				if(!batch.getDepotCode().matches("WMS_360_01|WMS_360_02")){
				    paramMap.put("batch_no", batch.getBatchNo());
				}
				paramMap.put("production_date", batch.getProductionDate());
				paramMap.put("overdue_date", batch.getOverdueDate());
				paramMap.put("stock_type", batch.getType());//库存类型  0 正常品  1 残次品 
				//经分销库存单位： 0 托盘 1箱  2 件  实时库存单位：  00：托盘，01：箱 , 02：件
				if(StringUtils.isBlank(batch.getUnit())
						||batch.getUnit().equals("2")){
					paramMap.put("unit", "02");
				}else if(batch.getUnit().equals("1")){
					paramMap.put("unit", "01");
				}else if(batch.getUnit().equals("0")){
					paramMap.put("unit", "00");
				}
				paramMap.put("create_date", TimeUtils.formatDay(batch.getCreateDate()));
				Integer qty = realTimeSnapshotDao.getQtyNumBatch(paramMap);
				if(qty!=null&&qty.intValue()>0) batch.setAvailableNum(qty);
			}
		}
		batchSnapshotModel.setList(list);
		return batchSnapshotModel;
	}

	@Override
	public List<Map<String, Object>> exportInventoryBatchSnapshotModelMap(InventoryBatchSnapshotModel model)
			throws Exception {
		List<Map<String, Object>> list = inventoryBatchSnapshotDao.exportInventoryBatchSnapshotModelMap(model);
		if(list!=null&&list.size()>0){
			for(Map<String, Object> map:list){
				//从实时库存快照表获取仓库现存量字段 商家id、仓库id、商品id、批次号、效期、好/坏品、理货单位、快照日期
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchant_id", map.get("merchant_id"));
				paramMap.put("depot_id", map.get("depot_id"));
				paramMap.put("goods_id", map.get("goods_id"));
				//菜鸟仓
				String depotCode = (String) map.get("depot_code");
				if(!depotCode.matches("WMS_360_01|WMS_360_02")){
					paramMap.put("batch_no", map.get("batch_no"));
				}
			
				paramMap.put("production_date", map.get("production_date"));
				paramMap.put("overdue_date", map.get("overdue_date"));
				paramMap.put("stock_type", map.get("type_num"));//库存类型  0 正常品  1 残次品 
				//经分销库存单位： 0 托盘 1箱  2 件  实时库存单位：  00：托盘，01：箱 , 02：件
				String unit = (String)map.get("unit_num");
				if(StringUtils.isBlank(unit)
						||unit.equals("2")){
					paramMap.put("unit", "02");
				}else if(unit.equals("1")){
					paramMap.put("unit", "01");
				}else if(unit.equals("0")){
					paramMap.put("unit", "00");
				}
				paramMap.put("create_date", map.get("create_date"));
				Integer qty = realTimeSnapshotDao.getQtyNumBatch(paramMap);
				if(qty!=null&&qty.intValue()>0) map.put("available_num", qty);
			}
		}
		return list;
	}

}
