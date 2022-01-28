package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.ApiExternalConfigDao;
import com.topideal.entity.vo.system.ApiExternalConfigModel;
import com.topideal.mapper.system.ApiExternalConfigMapper;

/** 
 * 对外接口配置表 daoImpl
 * @author lian_
 */
@Repository
public class ApiExternalConfigDaoImpl implements ApiExternalConfigDao {

    @Autowired
    private ApiExternalConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ApiExternalConfigModel> list(ApiExternalConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ApiExternalConfigModel model) throws SQLException {
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
    public int modify(ApiExternalConfigModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ApiExternalConfigModel  searchByPage(ApiExternalConfigModel  model) throws SQLException{
        PageDataModel<ApiExternalConfigModel> pageModel=mapper.listByPage(model);
        return (ApiExternalConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ApiExternalConfigModel  searchById(Long id)throws SQLException {
        ApiExternalConfigModel  model=new ApiExternalConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ApiExternalConfigModel searchByModel(ApiExternalConfigModel model) throws SQLException {
		return mapper.get(model);
	}
}