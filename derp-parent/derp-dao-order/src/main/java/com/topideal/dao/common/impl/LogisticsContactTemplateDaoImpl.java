package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.LogisticsContactTemplateDao;
import com.topideal.entity.vo.common.LogisticsContactTemplateModel;
import com.topideal.mapper.common.LogisticsContactTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class LogisticsContactTemplateDaoImpl implements LogisticsContactTemplateDao {

    @Autowired
    private LogisticsContactTemplateMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<LogisticsContactTemplateModel> list(LogisticsContactTemplateModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(LogisticsContactTemplateModel model) throws SQLException {
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
    public int modify(LogisticsContactTemplateModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public LogisticsContactTemplateModel  searchByPage(LogisticsContactTemplateModel  model) throws SQLException{
        PageDataModel<LogisticsContactTemplateModel> pageModel=mapper.listByPage(model);
        return (LogisticsContactTemplateModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public LogisticsContactTemplateModel  searchById(Long id)throws SQLException {
        LogisticsContactTemplateModel  model=new LogisticsContactTemplateModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public LogisticsContactTemplateModel searchByModel(LogisticsContactTemplateModel model) throws SQLException {
		return mapper.get(model);
	}

}