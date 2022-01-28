package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.InventoryLocationAdjustmentOrderItemDao;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderItemModel;
import com.topideal.mapper.sale.InventoryLocationAdjustmentOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class InventoryLocationAdjustmentOrderItemDaoImpl implements InventoryLocationAdjustmentOrderItemDao {

    @Autowired
    private InventoryLocationAdjustmentOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryLocationAdjustmentOrderItemModel> list(InventoryLocationAdjustmentOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryLocationAdjustmentOrderItemModel model) throws SQLException {
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
    public int modify(InventoryLocationAdjustmentOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryLocationAdjustmentOrderItemModel  searchByPage(InventoryLocationAdjustmentOrderItemModel  model) throws SQLException{
        PageDataModel<InventoryLocationAdjustmentOrderItemModel> pageModel=mapper.listByPage(model);
        return (InventoryLocationAdjustmentOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryLocationAdjustmentOrderItemModel  searchById(Long id)throws SQLException {
        InventoryLocationAdjustmentOrderItemModel  model=new InventoryLocationAdjustmentOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryLocationAdjustmentOrderItemModel searchByModel(InventoryLocationAdjustmentOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<InventoryLocationAdjustmentOrderItemDTO> listInventoryLocationAdjustDTO(InventoryLocationAdjustmentOrderItemDTO dto) throws SQLException {
        return mapper.listInventoryLocationAdjustItemDTO(dto);
    }

    @Override
    public int delByIdBatch(List list) throws SQLException {
        return mapper.delByIdBatch(list);
    }

}