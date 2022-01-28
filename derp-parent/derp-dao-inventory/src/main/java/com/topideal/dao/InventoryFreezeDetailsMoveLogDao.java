package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.InventoryFreezeDetailsMoveLogModel;

/**
 * 库存冻结明细移动日志 dao
 * @author lian_
 *
 */
public interface InventoryFreezeDetailsMoveLogDao extends BaseDao<InventoryFreezeDetailsMoveLogModel>{
		
	/**
	 * 清空表
	 * @return
	 * @throws SQLException
	 */
    int delAll() throws SQLException;
    
    /**
     * 批量新增
     * @param list
     * @return
     * @throws SQLException
     */
    int insertBatch(List<InventoryFreezeDetailsMoveLogModel> list) throws SQLException;
    
    /**
     * 删除type=1
     * @return
     * @throws SQLException
     */
    int delByType() throws SQLException;

}
