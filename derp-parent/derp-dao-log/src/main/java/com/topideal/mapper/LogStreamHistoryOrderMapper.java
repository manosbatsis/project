package com.topideal.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamHistoryOrderModel;
import com.topideal.entity.vo.LogStreamMqModel;


@MyBatisRepository
public interface LogStreamHistoryOrderMapper extends BaseMapper<LogStreamHistoryOrderModel> {

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
	 * @throws SQLException
	 */
	PageDataModel<LogStreamMqDTO> getListByPage(LogStreamMqDTO model)throws SQLException ;

}
