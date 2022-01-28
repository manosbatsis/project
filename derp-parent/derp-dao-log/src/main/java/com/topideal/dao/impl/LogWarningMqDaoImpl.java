package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogWarningMqDao;
import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;
import com.topideal.mapper.LogWarningMqMapper;

/**
 * @author lchenxing
 */
@Repository
public class LogWarningMqDaoImpl implements LogWarningMqDao {

	@Autowired
	private LogWarningMqMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<LogWarningMqModel> list(LogWarningMqModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(LogWarningMqModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(LogWarningMqModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public LogWarningMqModel searchByPage(LogWarningMqModel model) throws SQLException {
		PageDataModel<LogWarningMqModel> pageModel = mapper.listByPage(model);
		return (LogWarningMqModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public LogWarningMqModel searchById(Long id) throws SQLException {
		LogWarningMqModel model = new LogWarningMqModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public LogWarningMqModel searchByModel(LogWarningMqModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public LogWarningMqDTO getListByPage(LogWarningMqDTO dto) throws SQLException {
		PageDataModel<LogWarningMqDTO> pageModel = mapper.getListByPage(dto);
		return (LogWarningMqDTO) pageModel.getModel();
	}

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public LogWarningMqModel getListByMonthByPage(LogWarningMqModel model) throws SQLException {
		PageDataModel<LogWarningMqModel> pageModel = mapper.getListByMonthByPage(model);
		return (LogWarningMqModel) pageModel.getModel();
	}
	/**整合web*/
	@Override
	public List<LogWarningMqModel> getList() {
		// TODO Auto-generated method stub
		return mapper.getList();
	}
	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public void delByCreateTime(String createTime){
		 mapper.delByCreateTime(createTime);
	}
}
