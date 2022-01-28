package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.LocationAdjustmentOrderDao;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.LocationAdjustmentOrderModel;
import com.topideal.mapper.sale.LocationAdjustmentOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

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
    public LocationAdjustmentOrderDTO queryDTOListByPage(LocationAdjustmentOrderDTO dto) throws SQLException {
        PageDataModel<LocationAdjustmentOrderDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (LocationAdjustmentOrderDTO) pageModel.getModel();
    }

    @Override
    public Integer batchSave(List<LocationAdjustmentOrderModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public Integer countByDTO(LocationAdjustmentOrderDTO dto) throws SQLException {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<LocationAdjustmentOrderDTO> listByDTO(LocationAdjustmentOrderDTO dto) throws SQLException {
        return mapper.listByDTO(dto);
    }

    @Override
    public Integer batchUpdateStatus(List<Long> ids, String status) throws SQLException {
        return mapper.batchUpdateStatus(ids, status);
    }
}