package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.OrderItemHistoryDao;
import com.topideal.entity.dto.sale.OrderItemHistoryDTO;
import com.topideal.entity.vo.sale.OrderItemHistoryModel;
import com.topideal.mapper.sale.OrderItemHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrderItemHistoryDaoImpl implements OrderItemHistoryDao {

    @Autowired
    private OrderItemHistoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderItemHistoryModel> list(OrderItemHistoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderItemHistoryModel model) throws SQLException {
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
    public int modify(OrderItemHistoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderItemHistoryModel  searchByPage(OrderItemHistoryModel  model) throws SQLException{
        PageDataModel<OrderItemHistoryModel> pageModel=mapper.listByPage(model);
        return (OrderItemHistoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderItemHistoryModel  searchById(Long id)throws SQLException {
        OrderItemHistoryModel  model=new OrderItemHistoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderItemHistoryModel searchByModel(OrderItemHistoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<OrderItemHistoryDTO> listItemDTO(OrderItemHistoryDTO dto) {
        return mapper.listItemDTO(dto);
    }
}