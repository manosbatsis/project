package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.OrealPurchaseOrderItemDao;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderItemModel;
import com.topideal.mapper.platformdata.OrealPurchaseOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrealPurchaseOrderItemDaoImpl implements OrealPurchaseOrderItemDao {

    @Autowired
    private OrealPurchaseOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrealPurchaseOrderItemModel> list(OrealPurchaseOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrealPurchaseOrderItemModel model) throws SQLException {
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
    public int modify(OrealPurchaseOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrealPurchaseOrderItemModel  searchByPage(OrealPurchaseOrderItemModel  model) throws SQLException{
        PageDataModel<OrealPurchaseOrderItemModel> pageModel=mapper.listByPage(model);
        return (OrealPurchaseOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrealPurchaseOrderItemModel  searchById(Long id)throws SQLException {
        OrealPurchaseOrderItemModel  model=new OrealPurchaseOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrealPurchaseOrderItemModel searchByModel(OrealPurchaseOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

}