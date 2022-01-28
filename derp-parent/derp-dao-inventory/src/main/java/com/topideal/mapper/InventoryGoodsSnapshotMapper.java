package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;

/**
 * 库存商品快照表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InventoryGoodsSnapshotMapper extends BaseMapper<InventoryGoodsSnapshotModel> {
	
		
	 /**
	  * 分页查询所有数据
	  * @param model
	  * @return
	  * @throws Exception
	  */
    PageDataModel<InventoryGoodsSnapshotDTO> getlistByPage(InventoryGoodsSnapshotDTO model)throws SQLException;

	   /**
* 导出库存商品快照
* @param id
* @return
* @throws Exception
*/
	List<Map<String,Object>> exportInventoryGoodsSnapshotModelMap(InventoryGoodsSnapshotModel model) throws Exception;
	
	
	/**
	 * 更新 仓库现存量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int updateInventoryGoodsSnapshotAvailableNum(Map<String,Object> map) throws SQLException;
	
    /**
     * 删除三个月前且不包含1号的数据 
     */
    public void delData();
    /**
     * 生成库存商品库存快照表查询
     * @param model
     * @return
     * @throws Exception
     */
	List<InventoryGoodsSnapshotModel>getInventoryGoodsSnapshotList(InventoryGoodsSnapshotModel model) throws Exception;

}

