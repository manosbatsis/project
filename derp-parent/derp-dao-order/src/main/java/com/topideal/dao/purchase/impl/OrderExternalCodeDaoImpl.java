package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.mapper.purchase.OrderExternalCodeMapper;

/**
 * 电商订单外部单号来源表
 * @author lchenxing
 */
@Repository
public class OrderExternalCodeDaoImpl implements OrderExternalCodeDao {

    @Autowired
    private OrderExternalCodeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderExternalCodeModel> list(OrderExternalCodeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderExternalCodeModel model) throws SQLException {
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
    public int modify(OrderExternalCodeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderExternalCodeModel  searchByPage(OrderExternalCodeModel  model) throws SQLException{
        PageDataModel<OrderExternalCodeModel> pageModel=mapper.listByPage(model);
        return (OrderExternalCodeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderExternalCodeModel  searchById(Long id)throws SQLException {
        OrderExternalCodeModel  model=new OrderExternalCodeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderExternalCodeModel searchByModel(OrderExternalCodeModel model) throws SQLException {
		return mapper.get(model);
	}

}
