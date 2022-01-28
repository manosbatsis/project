package com.topideal.dao.inventory.impl;


import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.inventory.BuInventoryItemDao;

import com.topideal.entity.vo.inventory.BuInventoryItemModel;
import com.topideal.mapper.inventory.BuInventoryItemMapper;
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
public class BuInventoryItemDaoImpl implements BuInventoryItemDao {

    @Autowired
    private BuInventoryItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuInventoryItemModel> list(BuInventoryItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuInventoryItemModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }

    /**
     * 删除
     *
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
    public int modify(BuInventoryItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuInventoryItemModel  searchByPage(BuInventoryItemModel  model) throws SQLException{
        PageDataModel<BuInventoryItemModel> pageModel=mapper.listByPage(model);
        return (BuInventoryItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuInventoryItemModel  searchById(Long id)throws SQLException {
        BuInventoryItemModel  model=new BuInventoryItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuInventoryItemModel searchByModel(BuInventoryItemModel model) throws SQLException {
		return mapper.get(model);
	}
    /**
     * 关联表头查询事业部库存明细
     */
    public List<Map<String,Object>> getItemList(Map<String,Object> map) throws SQLException{
        return mapper.getItemList(map);
    }
}