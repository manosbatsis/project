package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryFreeUnfreeOrderDao;
import com.topideal.entity.vo.InventoryFreeUnfreeOrderModel;
import com.topideal.mapper.InventoryFreeUnfreeOrderMapper;

/**库存冻结和解冻接口来源单据表 impl
 * @author lchenxing
 */
@Repository
public class InventoryFreeUnfreeOrderDaoImpl implements InventoryFreeUnfreeOrderDao {

    @Autowired
    private InventoryFreeUnfreeOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryFreeUnfreeOrderModel> list(InventoryFreeUnfreeOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryFreeUnfreeOrderModel model) throws SQLException {
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
    public int modify(InventoryFreeUnfreeOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryFreeUnfreeOrderModel  searchByPage(InventoryFreeUnfreeOrderModel  model) throws SQLException{
        PageDataModel<InventoryFreeUnfreeOrderModel> pageModel=mapper.listByPage(model);
        return (InventoryFreeUnfreeOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryFreeUnfreeOrderModel  searchById(Long id)throws SQLException {
        InventoryFreeUnfreeOrderModel  model=new InventoryFreeUnfreeOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryFreeUnfreeOrderModel searchByModel(InventoryFreeUnfreeOrderModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
