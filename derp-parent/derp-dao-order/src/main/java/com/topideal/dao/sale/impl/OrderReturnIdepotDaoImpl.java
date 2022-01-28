package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderReturnIdepotDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;
import com.topideal.mapper.sale.OrderReturnIdepotMapper;
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
    	model.setModifyDate(TimeUtils.getNow());
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
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	/*@Override
	public OrderReturnIdepotModel listOrderAndItemByPage(OrderReturnIdepotModel model) throws SQLException {
		 PageDataModel<OrderReturnIdepotModel> pageModel=mapper.listOrderAndItemByPage(model);
	        return (OrderReturnIdepotModel ) pageModel.getModel();
	}*/
	/**
	 * 根据条件获取电商订单信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	/*@Override
	public int queryList(OrderReturnIdepotModel model) throws SQLException {
		return mapper.queryList(model);
	}*/
	/**
	 * 根据条件获取需要导出的数据
	 * @return
	 */
	/*@Override
	public List<Map<String, Object>> getExportOrderMap(OrderReturnIdepotModel model) throws SQLException {
		return mapper.getExportOrderMap(model);
	}*/
	/**
	 * 根据时间、商家id获取每天的数量
	 * @param startDate
	 * @param endDate
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderDayList(String startDate, String endDate, Long merchantId) {
		return mapper.getOrderDayList(startDate,endDate,merchantId);
	}
	/**
	 * 根据日期获取日期之前的数量
	 * @param date
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<OrderReturnIdepotModel> getOrderListforDate(String date, Long merchantId) {
		return mapper.getOrderListforDate(date,merchantId);
	}
	
	
	/**
	 * 根据条件获取电商订单数量
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	/*@Override
	public Integer queryListCount(OrderReturnIdepotModel model) throws SQLException {
		return mapper.queryListCount(model);
	}*/
	
	/**
     * 获取列表数据（表头）-分页
     * @param OrderReturnIdepotModel
     */
/*    @Override
    public OrderReturnIdepotModel  newListByPage(OrderReturnIdepotModel  model) throws SQLException{
        PageDataModel<OrderReturnIdepotModel> pageModel=mapper.newListByPage(model);
        return (OrderReturnIdepotModel ) pageModel.getModel();
    }*/
	@Override
	public int getImportOrderCountByIds(List<Long> ids) throws Exception {
		return mapper.getImportOrderCountByIds(ids) ;
	}
	
	/**
     * 分页查询
     * @param dto
     */
    @Override
    public OrderReturnIdepotDTO  searchDTOByPage(OrderReturnIdepotDTO  dto) throws SQLException{
        PageDataModel<OrderReturnIdepotDTO> pageModel=mapper.listDTOByPage(dto);
        return (OrderReturnIdepotDTO ) pageModel.getModel();
    }
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OrderReturnIdepotDTO  searchDTOById(Long id)throws SQLException {
    	OrderReturnIdepotDTO  dto=new OrderReturnIdepotDTO ();
    	dto.setId(id);
        return mapper.getOrderReturnIdepotDTOById(dto);
    }
	/**
     * 获取列表数据（表头）-分页
     * @param OrderReturnIdepotModel
     */
    @Override
    public OrderReturnIdepotDTO  newDTOListByPage(OrderReturnIdepotDTO  dto) throws SQLException{
        PageDataModel<OrderReturnIdepotDTO> pageModel=mapper.newDTOListByPage(dto);
        return (OrderReturnIdepotDTO ) pageModel.getModel();
    }
	/**
	 * 根据条件获取电商订单数量
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public Integer queryDTOListCount(OrderReturnIdepotDTO dto) throws SQLException {
		return mapper.queryDTOListCount(dto);
	}
	
	/**
	 * 根据条件获取退货电商订单信息
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public int queryDTOList(OrderReturnIdepotDTO dto) throws SQLException {
		return mapper.queryDTOList(dto);
	}
	/**
	 * 根据条件获取需要导出的数据
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getExportOrderDTOMap(OrderReturnIdepotDTO dto) throws SQLException {
		return mapper.getExportOrderDTOMap(dto);
	}

    @Override
    public List<OrderReturnIdepotDTO> listRefundOrderDTO(OrderReturnIdepotDTO dto) {
        return mapper.listRefundOrderDTO(dto);
    }

	@Override
	public List<Map<String, Object>> countOrderReturnIdepotByDTO(OrderReturnIdepotDTO dto) throws SQLException {
		return mapper.countOrderReturnIdepotByDTO(dto);
	}

	@Override
	public Integer batchUpdateStatus(String isGenerate, List<Long> ids) {
		return mapper.batchUpdateStatus(isGenerate, ids);
	}

	@Override
	public Integer batchUpdateStatusByCode(String isGenerate, List<String> codes) {
		return mapper.batchUpdateStatusByCode(isGenerate, codes);
	}

	@Override
	public List<OrderReturnIdepotDTO> selectDTOListByMap(Map<String, Object> paramMap) {
		return mapper.selectDTOListByMap(paramMap);
	}
	@Override
	public Integer batchSave(List<OrderReturnIdepotModel> list) throws SQLException {
		return mapper.batchSave(list);
	}
	@Override
	public List<OrderReturnIdepotDTO> listDTO(OrderReturnIdepotDTO dto) {
		return mapper.listDTO(dto);
	}

	@Override
	public Long statisticsDistinctByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO) {
		return mapper.statisticsDistinctByDTO(orderReturnIdepotDTO);
	}

	@Override
	public List<OrderReturnIdepotDTO> listDistinctOrderByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO) {
		return mapper.listDistinctOrderByDTO(orderReturnIdepotDTO);
	}

}