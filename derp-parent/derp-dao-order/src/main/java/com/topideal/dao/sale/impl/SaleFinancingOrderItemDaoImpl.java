package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleFinancingOrderItemDao;
import com.topideal.entity.dto.sale.SaleFinancingOrderItemDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderItemModel;
import com.topideal.mapper.sale.SaleFinancingOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleFinancingOrderItemDaoImpl implements SaleFinancingOrderItemDao {

    @Autowired
    private SaleFinancingOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleFinancingOrderItemModel> list(SaleFinancingOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleFinancingOrderItemModel model) throws SQLException {
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
    public int modify(SaleFinancingOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleFinancingOrderItemModel  searchByPage(SaleFinancingOrderItemModel  model) throws SQLException{
        PageDataModel<SaleFinancingOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleFinancingOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleFinancingOrderItemModel  searchById(Long id)throws SQLException {
        SaleFinancingOrderItemModel  model=new SaleFinancingOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleFinancingOrderItemModel searchByModel(SaleFinancingOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<SaleFinancingOrderItemDTO> listByDTO(SaleFinancingOrderItemDTO dto) throws SQLException {
		return mapper.listByDTO(dto);
	}

}