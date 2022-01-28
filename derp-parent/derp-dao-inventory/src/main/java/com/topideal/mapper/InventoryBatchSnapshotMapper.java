package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;

/**
 * 库存批次快照表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InventoryBatchSnapshotMapper extends BaseMapper<InventoryBatchSnapshotModel> {

	   /**
	  * 导出库存批次快照
	  * @param id
	  * @return
	  * @throws Exception
	  */
		List<Map<String,Object>> exportInventoryBatchSnapshotModelMap(InventoryBatchSnapshotModel model) throws Exception;
		
		
		/**
		 *  更新仓库现存量
		 * @param map
		 * @return
		 * @throws SQLException
		 */
		int updateInventoryBatchSnapshotAvailableNum(Map<String,Object>map)throws SQLException;

		/**
		 *  统计商品维度的库存量合仓库现存量
		 * @param model
		 * @return
		 * @throws Exception
		 */
/*		List<InventoryBatchSnapshotModel>countInventoryBatchSnapshotByGoodsStock(InventoryBatchSnapshotModel model) throws Exception;
		*/
		/**
		 *  查询更具当前年月日和商品库存信息查询唯一信息
		 * @param model
		 * @return
		 * @throws Exception
		 */
		InventoryBatchSnapshotModel	selectByOne(InventoryBatchSnapshotModel model) throws Exception;
		
		/**
		 * 分页
		 * @return
		 * @throws Exception
		 */
		PageDataModel<InventoryBatchSnapshotDTO> getListByPage(InventoryBatchSnapshotDTO model) throws SQLException;

	    /**
	     * 删除三个月前且不包含1号的数据 
	     */
	    public void delData();
		
}

