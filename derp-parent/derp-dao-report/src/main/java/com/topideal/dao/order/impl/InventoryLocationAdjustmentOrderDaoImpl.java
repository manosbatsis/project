package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.InventoryLocationAdjustmentOrderDao;
import com.topideal.entity.vo.order.InventoryLocationAdjustmentOrderModel;
import com.topideal.mapper.order.InventoryLocationAdjustmentOrderMapper;
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
public class InventoryLocationAdjustmentOrderDaoImpl implements InventoryLocationAdjustmentOrderDao {

    @Autowired
    private InventoryLocationAdjustmentOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryLocationAdjustmentOrderModel> list(InventoryLocationAdjustmentOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryLocationAdjustmentOrderModel model) throws SQLException {
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
    public int modify(InventoryLocationAdjustmentOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryLocationAdjustmentOrderModel  searchByPage(InventoryLocationAdjustmentOrderModel  model) throws SQLException{
        PageDataModel<InventoryLocationAdjustmentOrderModel> pageModel=mapper.listByPage(model);
        return (InventoryLocationAdjustmentOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryLocationAdjustmentOrderModel  searchById(Long id)throws SQLException {
        InventoryLocationAdjustmentOrderModel  model=new InventoryLocationAdjustmentOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryLocationAdjustmentOrderModel searchByModel(InventoryLocationAdjustmentOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 获取库位调整单
	 */
	@Override
	public List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> param) {
		return mapper.getLocationAdjustmentOrder(param);
	}
	

}