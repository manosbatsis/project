package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.mapper.LogStreamApiMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogStreamApiDaoImpl implements LogStreamApiDao {

    @Autowired
    private LogStreamApiMapper mapper;
	
    /**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<LogStreamApiModel> list(LogStreamApiModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(LogStreamApiModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 批量删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(LogStreamApiModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public LogStreamApiModel  searchByPage(LogStreamApiModel  model) throws SQLException{
        PageDataModel<LogStreamApiModel> pageModel=mapper.listByPage(model);
        return (LogStreamApiModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public LogStreamApiModel  searchById(Long id)throws SQLException {
        LogStreamApiModel  model=new LogStreamApiModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public LogStreamApiModel searchByModel(LogStreamApiModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取本月份API接收请求数
	 */
	@Override
	public Long getRequestNum(String startDate, String endDate) {
		return mapper.getRequestNum(startDate, endDate);
	}
	
	/**
	 * 根据月份，获取之前的数据
	 * 
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public LogStreamApiModel getListByMonthByPage(LogStreamApiModel model) throws SQLException {
		PageDataModel<LogStreamApiModel> pageModel = mapper.getListByMonthByPage(model);
		return (LogStreamApiModel) pageModel.getModel();
	}
	
	/**整合web*/
	/**
	 * 分页
	 */
	@Override
	public LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException {
		PageDataModel<LogStreamMqDTO> pageModel = mapper.getListByPage(model);
		return (LogStreamMqDTO) pageModel.getModel();
	}
	@Override
	public Long getRequestSuccNum(String sDate, String eDate) {
		return mapper.getRequestSuccNum(sDate, eDate);
	}
}