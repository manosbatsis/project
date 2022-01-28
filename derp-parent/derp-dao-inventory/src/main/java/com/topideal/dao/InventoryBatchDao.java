package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.entity.vo.InventoryBatchModel;




public interface InventoryBatchDao extends BaseDao<InventoryBatchModel>{
		

	/**
	 *  统计批次库存效期预警
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchDTO	selectInventoryWarningByPage(InventoryBatchDTO model) throws Exception;
	
	
	  /**
     *  导出批次库存明细
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String,Object>>   exportInventoryBatchMap(InventoryBatchDTO model)throws Exception;

    
    

	/**
	 * 更加失效日期 ASC排序 查询先失效的库存量
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<InventoryBatchModel>  listOrbyOverdueDate(InventoryBatchModel model)throws Exception;

     /**
      * 更加主键id进行单个删除
      * @param id
      * @return
      * @throws Exception
      */
	int delInventoryBatch(Long id)throws Exception;
	
	/**
	 *   商品库存明细
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchDTO listProductInventoryDetailsByPage (InventoryBatchDTO model ) throws Exception;

	
	/**
	 * 查询所有的商家和对应的仓库
	 * @return
	 * @throws Exception
	 */
	List<InventoryBatchModel>	getAllMerchantOrDepot()throws Exception;
	
	
	  /**
     * 导出效期预警
     * @param list
     * @return
     */
    List<Map<String,Object>> exportInventoryWarningMap(InventoryBatchDTO model)throws Exception;
    
    
    /**
     * 导出商品库存明细sheet0
     * @param model
     * @return
     * @throws Exception
     */
	List<InventoryBatchDTO>	  exportProductInventoryDetails(InventoryBatchDTO model ) throws Exception;
	/**
	 * 商品库存明细sheet1
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<InventoryBatchDTO>	  getInventoryBatchExport(InventoryBatchDTO model ) throws Exception;
	
	/**
	 *  扣减库存数量
	 * @param model
	 * @return
	 * @throws Exception
	 */
   int	updateLowerGoodsNum(Map<String,Object> params) throws SQLException;
   
   /**
    *  增加库存数量
    * @param model
    * @return
    * @throws Exception
    */
   int  updateAddGoodsNum(Map<String,Object> params) throws SQLException;
   
   
   /**
    * 统计批次库存明细 好品和坏品 已过期的数量
    * @param merchantId
    * @return
    * @throws Exception
    */
   InventoryBatchModel countInventoryAmount(InventoryBatchModel model) throws Exception;
   
   
   /**
    * 扣减库存接口 查询单个批次库存明细 
    * @param model
    * @return
    * @throws Exception
    */
   InventoryBatchModel  searchByInventoryBatchModel(InventoryBatchModel model) throws SQLException;
	
   
   /**
    *  按商品批次效期维度进行库存余量统计
    * @param model
    * @return
    * @throws SQLException
    */
   List<InventoryBatchModel> searchInventoryBatchModelByGoodsList(InventoryBatchModel model) throws SQLException;
   
   /**
    * 查询一条商家库存明细的 可用量字段
    * @param model
    * @return
    * @throws SQLException
    */
   InventoryBatchModel  getProductInventoryDetailsByOne(InventoryBatchModel model) throws SQLException;
   
   /**
    * 更具传入条件统计所有的库存余量
    * @param model
    * @return
    * @throws SQLException
    */
   InventoryBatchModel  getInventoryBatchModelAllSurplusNum(InventoryBatchModel model) throws SQLException;
   
   /**
    *  查询库存余量为0 的数据
    * @param model
    * @return
    * @throws SQLException
    */
   List<InventoryBatchModel> getInventoryBatchModelByZero(InventoryBatchModel model) throws SQLException;
   
   
   /**
    * 批次库存列表分页
    * @param model
    * @return
    * @throws SQLException
    */
   InventoryBatchDTO	 getInventoryBatchByPage(InventoryBatchDTO model) throws SQLException;
	
	/**
	 * 用于生成月结账单查询批次汇总库存余量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<InventoryBatchModel> getInventoryBatchModelByGoodsBatchList(InventoryBatchModel model) throws SQLException;
	
	InventoryBatchModel getInventoryBatchModelMapByOne (InventoryBatchModel model) throws SQLException;
	
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);
	/**
	 * 校验仓库是否已经使用
	 * @param id
	 * @return
	 */
	Integer checkDepotIsUse(Long depotId);
	

	/**
     * 加库存接口 查询单个批次库存明细 
     * @param model
     * @return
     * @throws Exception
     */
	InventoryBatchModel searchInventoryBatchForAdd(InventoryBatchModel inBatchModel)throws SQLException ;

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
	 * @throws Exception
	 */
	List <InventoryBatchModel>  getBatchValidation(Long depotId)throws Exception;

	/**
	 *  获取批次库存已经过期 但是 过期状态还是未过期 的数据
	 * @return
	 * @throws Exception
	 */
	List<InventoryBatchModel>getIsexpireInventoryBatchlist()throws Exception;
	
	/**
	 * 获取最小的创建日期(首次上架日期)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchModel   getMinDate(InventoryBatchModel model)throws Exception;
	

}

