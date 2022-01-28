package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.LogisticsAttorneyDao;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.mapper.common.LogisticsAttorneyMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogisticsAttorneyDaoImpl implements LogisticsAttorneyDao {

    @Autowired
    private LogisticsAttorneyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<LogisticsAttorneyModel> list(LogisticsAttorneyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(LogisticsAttorneyModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
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
    public int modify(LogisticsAttorneyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public LogisticsAttorneyModel  searchByPage(LogisticsAttorneyModel  model) throws SQLException{
        PageDataModel<LogisticsAttorneyModel> pageModel=mapper.listByPage(model);
        return (LogisticsAttorneyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public LogisticsAttorneyModel  searchById(Long id)throws SQLException {
        LogisticsAttorneyModel  model=new LogisticsAttorneyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public LogisticsAttorneyModel searchByModel(LogisticsAttorneyModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int modifyWithNull(LogisticsAttorneyModel model) throws SQLException {
		return mapper.modifyWithNull(model);
	}

}