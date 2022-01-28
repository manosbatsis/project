package com.topideal.inventory.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.xmlbeans.impl.regex.REUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventoryGoodsSnapshotDao;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
import com.topideal.inventory.service.InventoryGoodsSnapshotService;

/**
 *  库存管理-库存商品快照-service实现类
 * @author 联想302
 *
 */
@Service
public class InventoryGoodsSnapshotServiceImpl implements InventoryGoodsSnapshotService {
    
	@Autowired
	private InventoryGoodsSnapshotDao inventoryGoodsSnapshotDao;
	@Autowired
	private InventoryRealTimeSnapshotDao realTimeSnapshotDao;
	
	@Override
	public InventoryGoodsSnapshotDTO listInventoryGoodsSnapshotModel(InventoryGoodsSnapshotDTO model)
			throws SQLException {
		InventoryGoodsSnapshotDTO snapshotModel = inventoryGoodsSnapshotDao.getlistByPage(model);
		List<InventoryGoodsSnapshotDTO> list = snapshotModel.getList();
		/*if(list!=null&&list.size()>0){
			for(InventoryGoodsSnapshotModel goodsBatch:list){
				//从实时库存快照表获取仓库现存量字段 商家id、仓库id、商品id、好/坏品、理货单位、快照日期
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchant_id", goodsBatch.getMerchantId());
				paramMap.put("depot_id", goodsBatch.getDepotId());
				paramMap.put("goods_id", goodsBatch.getGoodsId());
				//paramMap.put("stock_type", goodsBatch.getType());//库存类型  0 正常品  1 残次品 
				//经分销库存单位： 0 托盘 1箱  2 件  实时库存单位：  00：托盘，01：箱 , 02：件
				if(StringUtils.isBlank(goodsBatch.getUnit())
						||goodsBatch.getUnit().equals("2")){
					paramMap.put("unit", "02");
				}else if(goodsBatch.getUnit().equals("1")){
					paramMap.put("unit", "01");
				}else if(goodsBatch.getUnit().equals("0")){
					paramMap.put("unit", "00");
				}
				//是否过期（0 过期 1 未过期）
				//paramMap.put("is_expire", goodsBatch.getIsExpire());
				paramMap.put("create_date", TimeUtils.formatDay(goodsBatch.getCreateDate()));
				Integer qty = realTimeSnapshotDao.getQtyNumBatch(paramMap);
				if(qty!=null&&qty.intValue()>0) goodsBatch.setAvailableNum(qty);
			}
		}*/
		snapshotModel.setList(list);
		return snapshotModel;
	}

	@Override
	public List<Map<String, Object>> exportInventoryGoodsSnapshotModelMap(InventoryGoodsSnapshotModel model)
			throws Exception {
		List<Map<String, Object>> list = inventoryGoodsSnapshotDao.exportInventoryGoodsSnapshotModelMap(model);
		/*if(list!=null&&list.size()>0){
			for(Map<String, Object> map:list){
				//从实时库存快照表获取仓库现存量字段 商家id、仓库id、商品id、批次号、效期、好/坏品、理货单位、快照日期
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchant_id", map.get("merchant_id"));
				paramMap.put("depot_id", map.get("depot_id"));
				paramMap.put("goods_id", map.get("goods_id"));
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
		}*/
		return list;
	}

}
