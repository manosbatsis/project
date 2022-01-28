package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.LocationAdjustmentOrderDao;
import com.topideal.entity.vo.order.LocationAdjustmentOrderModel;
import com.topideal.mapper.order.LocationAdjustmentOrderMapper;
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
public class LocationAdjustmentOrderDaoImpl implements LocationAdjustmentOrderDao {

    @Autowired
    private LocationAdjustmentOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<LocationAdjustmentOrderModel> list(LocationAdjustmentOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(LocationAdjustmentOrderModel model) throws SQLException {
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
    public int modify(LocationAdjustmentOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public LocationAdjustmentOrderModel  searchByPage(LocationAdjustmentOrderModel  model) throws SQLException{
        PageDataModel<LocationAdjustmentOrderModel> pageModel=mapper.listByPage(model);
        return (LocationAdjustmentOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public LocationAdjustmentOrderModel  searchById(Long id)throws SQLException {
        LocationAdjustmentOrderModel  model=new LocationAdjustmentOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public LocationAdjustmentOrderModel searchByModel(LocationAdjustmentOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> map) {
		return mapper.getLocationAdjustmentOrder(map);
	}

}