package com.topideal.inventory.service;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryRealTimeDTO;
import com.topideal.entity.vo.InventoryRealTimeModel;
import com.topideal.mongo.entity.DepotInfoMongo;
/**
 * 库存 --实时库存service实现类
 */
public interface InventoryRealTimeService {
	

	/**
	 * 调用op接口获取库存信息
	 * @param depotId
	 * @param goodsId
	 * @param pageNo
	 * @param pageSize
	 * @param userInfoModel
	 * @return
	 */
	InventoryRealTimeDTO searchInventoryRealTime(Long merchantId,DepotInfoMongo depotMongo, String goodsNo, Integer pageNo, Integer pageSize,String merchantName,String topidealCode,String barcode)throws Exception;

	/**
	 * 导出实时库存
	 * @param model
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	 List<Map<String,Object>>  exportInventoryRealTimeMap(InventoryRealTimeDTO   model,String batchNo)throws Exception;
	 
	 
	 
	 
		/**
		 *  封装导出op和ofc实时库存信息
		 * @param depotId
		 * @param goodsId
		 * @param pageNo
		 * @param pageSize
		 * @param userInfoModel
		 * @return
		 */
	 InventoryRealTimeDTO searchInventoryRealTimeForExport(Long merchantId,DepotInfoMongo depotMongo, String goodsNo,String merchantName,String topidealCode)throws Exception;
     /**
 	 * 查询实时库存
 	 * */
     InventoryRealTimeDTO realTimeByPage(Long merchantId,InventoryRealTimeDTO model) throws Exception;

	/**
	 *  封装导出菜鸟实时库存信息
	 * @param merchantId
	 * @param merchantName
	 * @param depotMongo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryRealTimeDTO searchRookieInventoryRealForExport(Long merchantId,String merchantName,String topidealCode,DepotInfoMongo depotMongo, InventoryRealTimeDTO model)throws Exception;





}
