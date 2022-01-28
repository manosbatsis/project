package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.InventoryAdjustmentDetailDao;
import com.topideal.entity.vo.sale.InventoryAdjustmentDetailModel;
import com.topideal.mapper.sale.InventoryAdjustmentDetailMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class InventoryAdjustmentDetailDaoImpl implements InventoryAdjustmentDetailDao {

    @Autowired
    private InventoryAdjustmentDetailMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryAdjustmentDetailModel> list(InventoryAdjustmentDetailModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryAdjustmentDetailModel model) throws SQLException {
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
    public int modify(InventoryAdjustmentDetailModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryAdjustmentDetailModel  searchByPage(InventoryAdjustmentDetailModel  model) throws SQLException{
        PageDataModel<InventoryAdjustmentDetailModel> pageModel=mapper.listByPage(model);
        return (InventoryAdjustmentDetailModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryAdjustmentDetailModel  searchById(Long id)throws SQLException {
        InventoryAdjustmentDetailModel  model=new InventoryAdjustmentDetailModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryAdjustmentDetailModel searchByModel(InventoryAdjustmentDetailModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer countByDetailModel(InventoryAdjustmentDetailModel model) {
        return mapper.countByDetailModel(model);
    }
	@Override
	public Integer getModelCount(Map<String, Object> map) {
		return mapper.getModelCount(map);
	}
}