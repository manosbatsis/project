package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleFinancingOrderDao;
import com.topideal.entity.dto.sale.SaleFinancingOrderDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderModel;
import com.topideal.mapper.sale.SaleFinancingOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleFinancingOrderDaoImpl implements SaleFinancingOrderDao {

    @Autowired
    private SaleFinancingOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleFinancingOrderModel> list(SaleFinancingOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleFinancingOrderModel model) throws SQLException {
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
    public int modify(SaleFinancingOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleFinancingOrderModel  searchByPage(SaleFinancingOrderModel  model) throws SQLException{
        PageDataModel<SaleFinancingOrderModel> pageModel=mapper.listByPage(model);
        return (SaleFinancingOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleFinancingOrderModel  searchById(Long id)throws SQLException {
        SaleFinancingOrderModel  model=new SaleFinancingOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleFinancingOrderModel searchByModel(SaleFinancingOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SaleFinancingOrderDTO searchByDTO(SaleFinancingOrderDTO dto) throws SQLException {
		return mapper.searchByDTO(dto);
	}

}