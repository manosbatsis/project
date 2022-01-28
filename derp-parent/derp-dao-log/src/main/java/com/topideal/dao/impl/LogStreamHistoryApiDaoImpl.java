package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogStreamHistoryApiDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamHistoryApiModel;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.mapper.LogStreamHistoryApiMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogStreamHistoryApiDaoImpl implements LogStreamHistoryApiDao {

    @Autowired
    private LogStreamHistoryApiMapper mapper;
	
    /**
   	 * 列表查询
   	 * @param model
   	 */
   	@Override
   	public List<LogStreamHistoryApiModel> list(LogStreamHistoryApiModel model) throws SQLException {
   		return mapper.list(model);
   	}
   	/**
   	 * 新增
   	 * @param model
   	 */
       @Override
       public Long save(LogStreamHistoryApiModel model) throws SQLException {
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
       public int modify(LogStreamHistoryApiModel  model) throws SQLException {
           return mapper.update(model);
       }
       
   	/**
        * 分页查询
        * @param model
        */
       @Override
       public LogStreamHistoryApiModel  searchByPage(LogStreamHistoryApiModel  model) throws SQLException{
           PageDataModel<LogStreamHistoryApiModel> pageModel=mapper.listByPage(model);
           return (LogStreamHistoryApiModel) pageModel.getModel();
       }
       
       /**
        * 通过id查询实体类信息
        * @param id
        */
       @Override
       public LogStreamHistoryApiModel  searchById(Long id)throws SQLException {
           LogStreamHistoryApiModel  model=new LogStreamHistoryApiModel ();
           model.setId(id);
           return mapper.get(model);
       }
       
         /**
        	* 通过条件查询实体类信息
        	* @param model
        	* */
   	@Override
   	public LogStreamHistoryApiModel searchByModel(LogStreamHistoryApiModel model) throws SQLException {
   		return mapper.get(model);
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
	/**整合web*/
	/**
   	 * 分页
   	 */
	@Override
	public LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException {
		PageDataModel<LogStreamMqDTO> pageModel = mapper.getListByPage(model);
		return (LogStreamMqDTO) pageModel.getModel();
	}
	
}