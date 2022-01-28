package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.MaterialOrderItemDao;
import com.topideal.entity.vo.order.MaterialOrderItemModel;
import com.topideal.mapper.order.MaterialOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MaterialOrderItemDaoImpl implements MaterialOrderItemDao {

    @Autowired
    private MaterialOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MaterialOrderItemModel> list(MaterialOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MaterialOrderItemModel model) throws SQLException {
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
    public int modify(MaterialOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MaterialOrderItemModel  searchByPage(MaterialOrderItemModel  model) throws SQLException{
        PageDataModel<MaterialOrderItemModel> pageModel=mapper.listByPage(model);
        return (MaterialOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MaterialOrderItemModel  searchById(Long id)throws SQLException {
        MaterialOrderItemModel  model=new MaterialOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MaterialOrderItemModel searchByModel(MaterialOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
    
}