package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderReturnIdepotBatchDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotBatchModel;
import com.topideal.mapper.sale.OrderReturnIdepotBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrderReturnIdepotBatchDaoImpl implements OrderReturnIdepotBatchDao {

    @Autowired
    private OrderReturnIdepotBatchMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderReturnIdepotBatchModel> list(OrderReturnIdepotBatchModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderReturnIdepotBatchModel model) throws SQLException {
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
    public int modify(OrderReturnIdepotBatchModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderReturnIdepotBatchModel  searchByPage(OrderReturnIdepotBatchModel  model) throws SQLException{
        PageDataModel<OrderReturnIdepotBatchModel> pageModel=mapper.listByPage(model);
        return (OrderReturnIdepotBatchModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderReturnIdepotBatchModel  searchById(Long id)throws SQLException {
        OrderReturnIdepotBatchModel  model=new OrderReturnIdepotBatchModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderReturnIdepotBatchModel searchByModel(OrderReturnIdepotBatchModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<OrderReturnIdepotBatchDTO> listOrderReturnBatchDTOById(List<Long> list) {
		return mapper.listOrderReturnBatchDTOById(list);
	}

}