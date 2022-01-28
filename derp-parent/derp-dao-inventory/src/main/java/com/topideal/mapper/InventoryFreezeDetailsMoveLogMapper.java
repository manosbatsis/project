package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.InventoryFreezeDetailsMoveLogModel;

/**
 * 库存冻结明细移动日志 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InventoryFreezeDetailsMoveLogMapper extends BaseMapper<InventoryFreezeDetailsMoveLogModel> {

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
