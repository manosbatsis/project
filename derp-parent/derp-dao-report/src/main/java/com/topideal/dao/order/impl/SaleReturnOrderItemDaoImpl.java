package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnOrderItemDao;
import com.topideal.entity.vo.order.SaleReturnOrderItemModel;
import com.topideal.mapper.order.SaleReturnOrderItemMapper;

/**
 * 销售退货订单表体  daoImpl
 * @author lian_
 */
@Repository
public class SaleReturnOrderItemDaoImpl implements SaleReturnOrderItemDao {

    @Autowired
    private SaleReturnOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnOrderItemModel> list(SaleReturnOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleReturnOrderItemModel model) throws SQLException {
    	model.setCreateDate(TimeUtils.getNow());
    	model.setModifyDate(TimeUtils.getNow());
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
    public int modify(SaleReturnOrderItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleReturnOrderItemModel  searchByPage(SaleReturnOrderItemModel  model) throws SQLException{
        PageDataModel<SaleReturnOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleReturnOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleReturnOrderItemModel  searchById(Long id)throws SQLException {
        SaleReturnOrderItemModel  model=new SaleReturnOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleReturnOrderItemModel searchByModel(SaleReturnOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取销售退数量
	 * 
	 */
	@Override
	public Integer getSaleReturnAccount(Map<String, Object> queryMap) {
		return mapper.getSaleReturnAccount(queryMap);
	}
	
	/**
	 * 获取销售退明细
	 */
	@Override
	public List<Map<String, Object>> getVipSaleReturnOdepot(Map<String, Object> queryMap) {
		return mapper.getVipSaleReturnOdepot(queryMap);
	}

}