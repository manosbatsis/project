package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.InventoryLocationAdjustmentOrderDao;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderModel;
import com.topideal.mapper.sale.InventoryLocationAdjustmentOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

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

    @Override
    public InventoryLocationAdjustmentOrderDTO queryDTOListByPage(InventoryLocationAdjustmentOrderDTO dto) throws SQLException {
        PageDataModel<InventoryLocationAdjustmentOrderDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (InventoryLocationAdjustmentOrderDTO) pageModel.getModel();
    }
    @Override
    public int getTotal(InventoryLocationAdjustmentOrderDTO dto) throws SQLException {
        return mapper.getTotal(dto);
    }

    @Override
    public Integer getOrderCount(InventoryLocationAdjustmentOrderDTO dto) {
        return mapper.getOrderCount(dto);
    }

    @Override
    public List<InventoryLocationAdjustExportDTO> listDto(InventoryLocationAdjustmentOrderDTO dto) {
        return mapper.listDto(dto);
    }


    @Override
    public InventoryLocationAdjustmentOrderDTO searchDTOById(Long id) throws SQLException {
        InventoryLocationAdjustmentOrderDTO dto = new InventoryLocationAdjustmentOrderDTO();
        dto.setId(id);
        return mapper.getInventoryLocationDTOById(dto);
    }


}