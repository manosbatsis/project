package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryInvoicingDao;
import com.topideal.entity.vo.InventoryInvoicingModel;
import com.topideal.mapper.InventoryInvoicingMapper;

/**
 * 进销存汇总表 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryInvoicingDaoImpl implements InventoryInvoicingDao {

    @Autowired
    private InventoryInvoicingMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryInvoicingModel> list(InventoryInvoicingModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryInvoicingModel model) throws SQLException {
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
    public int modify(InventoryInvoicingModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryInvoicingModel  searchByPage(InventoryInvoicingModel  model) throws SQLException{
        PageDataModel<InventoryInvoicingModel> pageModel=mapper.listByPage(model);
        return (InventoryInvoicingModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryInvoicingModel  searchById(Long id)throws SQLException {
        InventoryInvoicingModel  model=new InventoryInvoicingModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryInvoicingModel searchByModel(InventoryInvoicingModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
