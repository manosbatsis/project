package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.entity.vo.InventoryBatchModel;

import net.sf.json.JSONObject;
/**
 * 库存管理 -批次库存明细-service实现类
 */
public interface InventoryBatchService {
	// 根据对象查询批次库存
	List<InventoryBatchModel> getList(InventoryBatchModel model)throws SQLException;
	
	/**
	 * 批次库存明细列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryBatchDTO listInventoryBatch(InventoryBatchDTO model) throws SQLException;
	
	
	/**
	 *  统计批次库存效期预警
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchDTO	selectInventoryWarningByPage(InventoryBatchDTO model,String validityTypes ) throws Exception;


	/**
	 *   商品库存明细
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchDTO listProductInventoryDetailsByPage (InventoryBatchDTO model ) throws Exception;

	
	/**
	 * 每天定时扫描批次的过期日期如果已过期就把商品类型变为坏品
	 * @throws Exception
	 */
    void  updateInventoryBatchTypeQuartz()throws Exception;
    
    /**
     *  导出批次库存明细
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String,Object>>   exportInventoryBatchMap(InventoryBatchDTO model)throws Exception;
    
    
    
    /**
     *  导出商品库存明细
     * @param id
     * @return
     * @throws Exception
     */
    Map<String,Object>  exportProductInventoryDetailsMap(Long merchantId,String depotIds,String goodsNo)throws Exception;


    /**
     * 导出效期预警
     * @param list
     * @return
     */
    List<Map<String,Object>> exportInventoryWarningMap(InventoryBatchDTO model,String validityTypes)throws Exception;
    
    
    /**
     * 统计批次库存明细 好品和坏品 已过期的数量
     * @param merchantId
     * @return
     * @throws Exception
     */
    InventoryBatchModel countInventoryAmount(InventoryBatchModel model) throws Exception;

    /**
     * 根据商家、仓库、理货单位获取库存可用量
     * @param merchantId
     * @param depotId
     * @param goodsId
     * @param unit
     * @return
     */
	List<InventoryBatchModel> getAvailableNum(Long merchantId, Long depotId, Long goodsId, String unit);
	/**
	 * 根据商家,仓库,商品,理货单位,批次,好坏品,过期品 查询库存余量 
	 * @param merchantId
	 * @param depotId
	 * @param goodsId
	 * @param unit
	 * @param type
	 * @param isExpire
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBadOrExpiredSurplusNum(Long merchantId,Long depotId, Long goodsId,String unit,String type,String isExpire,String batchNo)throws Exception;
 	
	/**
	 * 根据仓库ID 查询仓库库存余量不为0的所有商品是否都有批次效期
	 * @param depotId
	 * @return
	 */
	JSONObject getBatchValidation(Long depotId)throws Exception;

	/**
	 * 根据仓库ID  判断仓库是否有批次库存来进行删除操作
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
//	JSONObject deleteDepot(Long depotId) throws Exception;





	
}
