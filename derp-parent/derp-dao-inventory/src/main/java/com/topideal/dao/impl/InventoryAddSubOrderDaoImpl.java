package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryAddSubOrderDao;
import com.topideal.entity.vo.InventoryAddSubOrderModel;
import com.topideal.mapper.InventoryAddSubOrderMapper;

/**
 * @author lchenxing
 */
@Repository
public class InventoryAddSubOrderDaoImpl implements InventoryAddSubOrderDao {

    @Autowired
    private InventoryAddSubOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryAddSubOrderModel> list(InventoryAddSubOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryAddSubOrderModel model) throws SQLException {
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
    public int modify(InventoryAddSubOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryAddSubOrderModel  searchByPage(InventoryAddSubOrderModel  model) throws SQLException{
        PageDataModel<InventoryAddSubOrderModel> pageModel=mapper.listByPage(model);
        return (InventoryAddSubOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryAddSubOrderModel  searchById(Long id)throws SQLException {
        InventoryAddSubOrderModel  model=new InventoryAddSubOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryAddSubOrderModel searchByModel(InventoryAddSubOrderModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
