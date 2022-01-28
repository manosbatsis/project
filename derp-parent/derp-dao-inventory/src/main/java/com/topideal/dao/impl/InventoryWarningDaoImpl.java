package com.topideal.dao.impl;


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryWarningDao;
import com.topideal.entity.vo.InventoryWarningModel;
import com.topideal.mapper.InventoryWarningMapper;

/**
 * 库存预警 impl
 * @author lchenxing
 */
@Repository
public class InventoryWarningDaoImpl implements InventoryWarningDao {

    @Autowired
    private InventoryWarningMapper mapper;  //库存预警
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryWarningModel> list(InventoryWarningModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param InventoryWarningModel
	 */
    @Override
    public Long save(InventoryWarningModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(InventoryWarningModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param InventoryWarningModel
     */
    @Override
    public InventoryWarningModel  searchByPage(InventoryWarningModel  model) throws SQLException{
        PageDataModel<InventoryWarningModel> pageModel=mapper.listByPage(model);
        return (InventoryWarningModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public InventoryWarningModel  searchById(Long id)throws SQLException {
        InventoryWarningModel  model=new InventoryWarningModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public InventoryWarningModel searchByModel(InventoryWarningModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
