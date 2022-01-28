package com.topideal.dao.inventory;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.inventory.InventoryDetailsModel;

/**
 * 库存收发明细 dao
 * @author lian_
 *
 */
public interface InventoryDetailsDao extends BaseDao<InventoryDetailsModel> {
		


	  /**
     * 统计事业部商家、仓库、条码在本月的入库
     * **/
	public List<Map<String, Object>> getBuBarcodeInStorageSum(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的出库
     * **/
	public List<Map<String, Object>> getBuBarcodeOutStorageSum(Map<String, Object> map);
	/**
     * 统计商家、仓库、条码在本月的出入库明细统计数量
     * **/
	public Integer getBarcodeOutInStorageDetailCount(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的出入库明细
     * **/
	public List<Map<String, Object>> getBuOutInStorageDetail(Map<String, Object> map);
	
	/**
     * 统计事业部商家、仓库、条码在本月的损益入库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitLossInStorageSum(Map<String, Object> map);
	

	/**
     * 统计商家、仓库、条码在本月的损益出库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitLossOutStorageSum(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的好品损益入库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitlossGoodInSum(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的好品损益出库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitlossGoodOutSum(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的坏品损益入库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitlossDamagedInSum(Map<String, Object> map);

	/**
     * 统计事业部商家、仓库、条码在本月的坏品损益出库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitlossDamagedOutSum(Map<String, Object> map);


	/**
	 * 统计商家、仓库、在本月的损益明细
	 */
	public List<InventoryDetailsModel> getBuProfitLossOutInStorageDetail(Map<String, Object> map);
	
	/**
	 * 来货残次
	 * @param map
	 * @return
	 */
	public List<InventoryDetailsModel> getInbadDetails(Map<String, Object> map);
	
	/**
	 * 来货残次
	 * @param map
	 * @return
	 */
	public List<InventoryDetailsModel> getOutbadDetails(Map<String, Object> map);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String, Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String, Object> map);
}

