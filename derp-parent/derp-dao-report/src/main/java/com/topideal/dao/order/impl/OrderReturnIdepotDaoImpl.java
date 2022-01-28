package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.OrderReturnIdepotDao;
import com.topideal.entity.vo.order.OrderReturnIdepotModel;
import com.topideal.mapper.order.OrderReturnIdepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrderReturnIdepotDaoImpl implements OrderReturnIdepotDao {

    @Autowired
    private OrderReturnIdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderReturnIdepotModel> list(OrderReturnIdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderReturnIdepotModel model) throws SQLException {
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
    public int modify(OrderReturnIdepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderReturnIdepotModel  searchByPage(OrderReturnIdepotModel  model) throws SQLException{
        PageDataModel<OrderReturnIdepotModel> pageModel=mapper.listByPage(model);
        return (OrderReturnIdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderReturnIdepotModel  searchById(Long id)throws SQLException {
        OrderReturnIdepotModel  model=new OrderReturnIdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderReturnIdepotModel searchByModel(OrderReturnIdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public OrderReturnIdepotModel getVeriNotDelList(OrderReturnIdepotModel orderReturnIdepotModel) {
		return mapper.getVeriNotDelList(orderReturnIdepotModel);
	}

}