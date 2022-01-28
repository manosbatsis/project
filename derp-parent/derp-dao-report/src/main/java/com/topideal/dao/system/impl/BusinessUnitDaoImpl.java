package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mapper.system.BusinessUnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BusinessUnitDaoImpl implements BusinessUnitDao {

    @Autowired
    private BusinessUnitMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BusinessUnitModel> list(BusinessUnitModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BusinessUnitModel model) throws SQLException {
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
    public int modify(BusinessUnitModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BusinessUnitModel  searchByPage(BusinessUnitModel  model) throws SQLException{
        PageDataModel<BusinessUnitModel> pageModel=mapper.listByPage(model);
        return (BusinessUnitModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BusinessUnitModel  searchById(Long id)throws SQLException {
        BusinessUnitModel  model=new BusinessUnitModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BusinessUnitModel searchByModel(BusinessUnitModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<BusinessUnitModel> getListByMap(Map<String, Object> map) {
        return mapper.getListByMap(map);
    }
}