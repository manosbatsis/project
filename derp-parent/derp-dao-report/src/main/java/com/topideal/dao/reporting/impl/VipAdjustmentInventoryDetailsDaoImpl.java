package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.VipAdjustmentInventoryDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipAdjustmentInventoryDetailsDao;
import com.topideal.entity.vo.reporting.VipAdjustmentInventoryDetailsModel;
import com.topideal.mapper.reporting.VipAdjustmentInventoryDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipAdjustmentInventoryDetailsDaoImpl implements VipAdjustmentInventoryDetailsDao {

    @Autowired
    private VipAdjustmentInventoryDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipAdjustmentInventoryDetailsModel> list(VipAdjustmentInventoryDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
    @Override
    public List<VipAdjustmentInventoryDetailsDTO> listDTO(VipAdjustmentInventoryDetailsDTO dto){
        return mapper.listDTO(dto);
    }
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipAdjustmentInventoryDetailsModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(VipAdjustmentInventoryDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipAdjustmentInventoryDetailsModel  searchByPage(VipAdjustmentInventoryDetailsModel  model) throws SQLException{
        PageDataModel<VipAdjustmentInventoryDetailsModel> pageModel=mapper.listByPage(model);
        return (VipAdjustmentInventoryDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipAdjustmentInventoryDetailsModel  searchById(Long id)throws SQLException {
        VipAdjustmentInventoryDetailsModel  model=new VipAdjustmentInventoryDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipAdjustmentInventoryDetailsModel searchByModel(VipAdjustmentInventoryDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int deleteByModel(VipAdjustmentInventoryDetailsModel model) {
		return mapper.deleteByModel(model);
	}
	@Override
	public int batchSave(List<VipAdjustmentInventoryDetailsModel> list) {
		return mapper.batchInsert(list);
	}
	

}