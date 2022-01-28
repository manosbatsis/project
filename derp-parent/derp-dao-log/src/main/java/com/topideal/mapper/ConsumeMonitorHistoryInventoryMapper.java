package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


@MyBatisRepository
public interface ConsumeMonitorHistoryInventoryMapper extends BaseMapper<ConsumeMonitorHistoryInventoryModel> {

	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	void delByCreateTime(@Param("createTime") String createTime);
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
}
