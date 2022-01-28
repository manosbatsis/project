package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderReturnIdepotItemDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotItemModel;
import com.topideal.mapper.sale.OrderReturnIdepotItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrderReturnIdepotItemDaoImpl implements OrderReturnIdepotItemDao {

    @Autowired
    private OrderReturnIdepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderReturnIdepotItemModel> list(OrderReturnIdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderReturnIdepotItemModel model) throws SQLException {
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
    public int modify(OrderReturnIdepotItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderReturnIdepotItemModel  searchByPage(OrderReturnIdepotItemModel  model) throws SQLException{
        PageDataModel<OrderReturnIdepotItemModel> pageModel=mapper.listByPage(model);
        return (OrderReturnIdepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderReturnIdepotItemModel  searchById(Long id)throws SQLException {
        OrderReturnIdepotItemModel  model=new OrderReturnIdepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderReturnIdepotItemModel searchByModel(OrderReturnIdepotItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer getReturnNumByExternalCode(Map<String, Object> map) {
		return mapper.getReturnNumByExternalCode(map);
	}
	@Override
	public List<OrderReturnIdepotItemDTO> listOrderReturnIdepotItemDTO(OrderReturnIdepotItemDTO dto)
			throws SQLException {
		return mapper.listOrderReturnIdepotItemDTO(dto);
	}

    @Override
    public List<OrderReturnIdepotItemDTO> listByOrderIds(List<Long> returnIdepotIds) {
        return mapper.listByOrderIds(returnIdepotIds);
    }

    @Override
    public List<Long> listBuByOrder(OrderReturnIdepotDTO dto) {
        return mapper.listBuByOrder(dto);
    }

    @Override
    public List<OrderReturnIdepotItemModel> getMaxPriceByOrderId(List<Long> orderIds, Boolean buIdFlag) {
        return mapper.getMaxPriceByOrderId(orderIds, buIdFlag);
    }
    @Override
	public Integer batchSave(List<OrderReturnIdepotItemModel> list) throws SQLException {
		return mapper.batchSave(list);
	}

	@Override
	public void batchUpdate(List<OrderReturnIdepotItemModel> list) throws SQLException {
		mapper.batchUpdate(list);
	}
}