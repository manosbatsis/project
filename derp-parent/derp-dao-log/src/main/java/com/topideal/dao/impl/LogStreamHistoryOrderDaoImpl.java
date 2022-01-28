package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.LogStreamHistoryOrderDao;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamHistoryOrderModel;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.mapper.LogStreamHistoryOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogStreamHistoryOrderDaoImpl implements LogStreamHistoryOrderDao {

    @Autowired
    private LogStreamHistoryOrderMapper mapper;
	
    /**
   	 * 列表查询
   	 * @param model
   	 */
   	@Override
   	public List<LogStreamHistoryOrderModel> list(LogStreamHistoryOrderModel model) throws SQLException {
   		return mapper.list(model);
   	}
   	/**
   	 * 新增
   	 * @param model
   	 */
       @Override
       public Long save(LogStreamHistoryOrderModel model) throws SQLException {
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
       public int modify(LogStreamHistoryOrderModel  model) throws SQLException {
           return mapper.update(model);
       }
       
   	/**
        * 分页查询
        * @param model
        */
       @Override
       public LogStreamHistoryOrderModel  searchByPage(LogStreamHistoryOrderModel  model) throws SQLException{
           PageDataModel<LogStreamHistoryOrderModel> pageModel=mapper.listByPage(model);
           return (LogStreamHistoryOrderModel) pageModel.getModel();
       }
       
       /**
        * 通过id查询实体类信息
        * @param id
        */
       @Override
       public LogStreamHistoryOrderModel  searchById(Long id)throws SQLException {
           LogStreamHistoryOrderModel  model=new LogStreamHistoryOrderModel ();
           model.setId(id);
           return mapper.get(model);
       }
       
         /**
        	* 通过条件查询实体类信息
        	* @param model
        	* */
   	@Override
   	public LogStreamHistoryOrderModel searchByModel(LogStreamHistoryOrderModel model) throws SQLException {
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
	@Override
	public LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException {
		PageDataModel<LogStreamMqDTO> pageModel = mapper.getListByPage(model);
		return (LogStreamMqDTO) pageModel.getModel();
	}
}