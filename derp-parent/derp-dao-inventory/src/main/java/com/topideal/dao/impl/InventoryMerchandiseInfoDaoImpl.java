package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryMerchandiseInfoDao;
import com.topideal.entity.vo.InventoryMerchandiseInfoModel;
import com.topideal.mapper.InventoryMerchandiseInfoMapper;

/**
 * 库存商品信息 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryMerchandiseInfoDaoImpl implements InventoryMerchandiseInfoDao {

    @Autowired
    private InventoryMerchandiseInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryMerchandiseInfoModel> list(InventoryMerchandiseInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryMerchandiseInfoModel model) throws SQLException {
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
    public int modify(InventoryMerchandiseInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryMerchandiseInfoModel  searchByPage(InventoryMerchandiseInfoModel  model) throws SQLException{
        PageDataModel<InventoryMerchandiseInfoModel> pageModel=mapper.listByPage(model);
        return (InventoryMerchandiseInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryMerchandiseInfoModel  searchById(Long id)throws SQLException {
        InventoryMerchandiseInfoModel  model=new InventoryMerchandiseInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryMerchandiseInfoModel searchByModel(InventoryMerchandiseInfoModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
