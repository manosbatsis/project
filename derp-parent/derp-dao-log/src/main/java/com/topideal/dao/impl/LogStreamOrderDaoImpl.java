package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogStreamOrderDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.entity.vo.LogStreamOrderModel;
import com.topideal.mapper.LogStreamOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogStreamOrderDaoImpl implements LogStreamOrderDao {

    @Autowired
    private LogStreamOrderMapper mapper;
	
    /**
   	 * 列表查询
   	 * @param model
   	 */
   	@Override
   	public List<LogStreamOrderModel> list(LogStreamOrderModel model) throws SQLException {
   		return mapper.list(model);
   	}
   	/**
   	 * 新增
   	 * @param model
   	 */
       @Override
       public Long save(LogStreamOrderModel model) throws SQLException {
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
       public int modify(LogStreamOrderModel  model) throws SQLException {
           return mapper.update(model);
       }
       
   	/**
        * 分页查询
        * @param model
        */
       @Override
       public LogStreamOrderModel  searchByPage(LogStreamOrderModel  model) throws SQLException{
           PageDataModel<LogStreamOrderModel> pageModel=mapper.listByPage(model);
           return (LogStreamOrderModel) pageModel.getModel();
       }
       
       /**
        * 通过id查询实体类信息
        * @param id
        */
       @Override
       public LogStreamOrderModel  searchById(Long id)throws SQLException {
           LogStreamOrderModel  model=new LogStreamOrderModel ();
           model.setId(id);
           return mapper.get(model);
       }
       
         /**
        	* 通过条件查询实体类信息
        	* @param model
        	* */
   	@Override
   	public LogStreamOrderModel searchByModel(LogStreamOrderModel model) throws SQLException {
   		return mapper.get(model);
   	}
   	
   	/**
	 * 统计前一天各接口每个时段的数量
	 */
	@Override
	public List<Map<String, Object>> getNumGroupCount(String startDate, String endDate) {
		return mapper.getNumGroupCount(startDate, endDate);
	}

	/**
	 * 获取某接口某时段的最大耗时、最小耗时、平均耗时
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Map<String, Object> getConsumingByDate(String startDate, String endDate, String point) {
		return mapper.getConsumingByDate(startDate, endDate, point);
	}
	/**
	 * 根据时间获取接口的总单量
	 * @param startDate
	 * @param endDate
	 * @param point
	 * @return
	 */
	@Override
	public Map<String, Object> getTotalNumByDate(String startDate, String endDate, String point) {
		return mapper.getTotalNumByDate(startDate, endDate, point);
	}
	
	/**
	 * 根据月份，获取之前的数据
	 * 
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public LogStreamOrderModel getListByMonthByPage(LogStreamOrderModel model) throws SQLException {
		PageDataModel<LogStreamOrderModel> pageModel = mapper.getListByMonthByPage(model);
		return (LogStreamOrderModel) pageModel.getModel();
	}
	
	/**整合web*/
	@Override
	public LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException {
		PageDataModel<LogStreamMqDTO> pageModel = mapper.getListByPage(model);
		return (LogStreamMqDTO) pageModel.getModel();
	}
}