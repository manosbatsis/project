package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


@MyBatisRepository
public interface ConsumeMonitorInventoryMapper extends BaseMapper<ConsumeMonitorInventoryModel> {

	/**
	 * 获取库存扣减失败需要重推的信息
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorInventoryModel> getLowerFailList();

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorInventoryModel> getListByMonthByPage(ConsumeMonitorInventoryModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	PageDataModel<ConsumeMonitorMqDTO> getListByPage(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 导出
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 统计数量
	 * @param model
	 * @return
	 */
	Long getCount(ConsumeMonitorMqModel model) throws SQLException;
	
	/**
	 * 获取失败库存数据
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> getRollbackList(Map<String, Object> map);
    
    /**
     * 库存回滚失败和成功条数
     * @param map
     * @return
     */
    List<Map<String, Object>> getInventoryRollbackCount(Map<String, Object> map);
    /**
     * 业务回滚成功失败条数
     * @param map
     * @return
     */
    List<Map<String, Object>> getOrderRollbackCount(Map<String, Object> map);
}
