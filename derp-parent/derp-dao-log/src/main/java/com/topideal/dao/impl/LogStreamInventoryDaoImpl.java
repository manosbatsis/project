package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogStreamInventoryDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamInventoryModel;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.mapper.LogStreamInventoryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogStreamInventoryDaoImpl implements LogStreamInventoryDao {

    @Autowired
    private LogStreamInventoryMapper mapper;
	
    /**
   	 * 列表查询
   	 * @param model
   	 */
   	@Override
   	public List<LogStreamInventoryModel> list(LogStreamInventoryModel model) throws SQLException {
   		return mapper.list(model);
   	}
   	/**
   	 * 新增
   	 * @param model
   	 */
       @Override
       public Long save(LogStreamInventoryModel model) throws SQLException {
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
       public int modify(LogStreamInventoryModel  model) throws SQLException {
           return mapper.update(model);
       }
       
   	/**
        * 分页查询
        * @param model
        */
       @Override
       public LogStreamInventoryModel  searchByPage(LogStreamInventoryModel  model) throws SQLException{
           PageDataModel<LogStreamInventoryModel> pageModel=mapper.listByPage(model);
           return (LogStreamInventoryModel) pageModel.getModel();
       }
       
       /**
        * 通过id查询实体类信息
        * @param id
        */
       @Override
       public LogStreamInventoryModel  searchById(Long id)throws SQLException {
           LogStreamInventoryModel  model=new LogStreamInventoryModel ();
           model.setId(id);
           return mapper.get(model);
       }
       
         /**
        	* 通过条件查询实体类信息
        	* @param model
        	* */
   	@Override
   	public LogStreamInventoryModel searchByModel(LogStreamInventoryModel model) throws SQLException {
   		return mapper.get(model);
   	}
   	
   	/**
	 * 根据月份，获取之前的数据
	 * 
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public LogStreamInventoryModel getListByMonthByPage(LogStreamInventoryModel model) throws SQLException {
		PageDataModel<LogStreamInventoryModel> pageModel = mapper.getListByMonthByPage(model);
		return (LogStreamInventoryModel) pageModel.getModel();
	}
	
	/**整合web*/
	@Override
	public LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException {
		PageDataModel<LogStreamMqDTO> pageModel = mapper.getListByPage(model);
		return (LogStreamMqDTO) pageModel.getModel();
	}
}