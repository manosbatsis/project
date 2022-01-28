package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PlatformCostOrderItemDao;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.mapper.bill.PlatformCostOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformCostOrderItemDaoImpl implements PlatformCostOrderItemDao {

    @Autowired
    private PlatformCostOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformCostOrderItemModel> list(PlatformCostOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformCostOrderItemModel model) throws SQLException {
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
    public int modify(PlatformCostOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformCostOrderItemModel  searchByPage(PlatformCostOrderItemModel  model) throws SQLException{
        PageDataModel<PlatformCostOrderItemModel> pageModel=mapper.listByPage(model);
        return (PlatformCostOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformCostOrderItemModel  searchById(Long id)throws SQLException {
        PlatformCostOrderItemModel  model=new PlatformCostOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformCostOrderItemModel searchByModel(PlatformCostOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PlatformCostOrderItemDTO listPlatformCostOrderDTOByPage(PlatformCostOrderItemDTO dto) throws SQLException {
        PageDataModel<PlatformCostOrderItemDTO> pageModel = mapper.listPlatformCostOrderDTOByPage(dto);
        return (PlatformCostOrderItemDTO) pageModel.getModel();
    }
}