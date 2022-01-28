package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.OrderHistoryDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderHistoryDTO;
import com.topideal.entity.vo.sale.OrderHistoryModel;
import com.topideal.mapper.sale.OrderHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OrderHistoryDaoImpl implements OrderHistoryDao {

    @Autowired
    private OrderHistoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderHistoryModel> list(OrderHistoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OrderHistoryModel model) throws SQLException {
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
    public int modify(OrderHistoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OrderHistoryModel  searchByPage(OrderHistoryModel  model) throws SQLException{
        PageDataModel<OrderHistoryModel> pageModel=mapper.listByPage(model);
        return (OrderHistoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderHistoryModel  searchById(Long id)throws SQLException {
        OrderHistoryModel  model=new OrderHistoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OrderHistoryModel searchByModel(OrderHistoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public OrderHistoryDTO newDtoListByPage(OrderHistoryDTO dto) throws SQLException {
        PageDataModel<OrderHistoryDTO> pageModel = mapper.newDtoListByPage(dto);
        return (OrderHistoryDTO) pageModel.getModel();
    }
    /**
     * 根据条件获取电商订单数量
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public Integer queryDtoListCount(OrderDTO dto) throws SQLException {
        return mapper.queryDtoListCount(dto);
    }
    /**
     * 根据条件获取需要导出的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> getExportOrderMap(OrderDTO dto) throws SQLException {
        return mapper.getExportOrderMap(dto);
    }

    @Override
    public OrderHistoryDTO searchDtoById(OrderHistoryDTO dto) {
        return mapper.searchDtoById(dto);
    }

    @Override
    public Integer getExportOrderCount(OrderDTO dto) throws SQLException {
        return mapper.getExportOrderCount(dto);
    }
}