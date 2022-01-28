package com.topideal.mapper;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.InventoryBatchBackupModel;

/**
 * 库存表-批次库存明细_备份 mapper
 * @author lian_
 */
@MyBatisRepository
public interface InventoryBatchBackupMapper extends BaseMapper<InventoryBatchBackupModel> {

	/**
	 * 库存回滚 把批次库存备份中数据移回批次库存 把剩余数量设置为0
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int remveBackInventoryBatchBackup(@Param("id")Long id)throws Exception;
	
	/**
	 * 获取最小的创建日期(首次上架日期)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	InventoryBatchBackupModel   getMinDate(InventoryBatchBackupModel model)throws Exception;

}

