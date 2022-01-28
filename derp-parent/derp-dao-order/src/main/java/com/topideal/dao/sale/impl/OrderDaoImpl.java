package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.mapper.sale.OrderMapper;

/**
 * 电商订单表 impl
 * @author lchenxing
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private OrderMapper mapper; //电商订单表
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OrderModel> list(OrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param OrderModel
	 */
    @Override
    public Long save(OrderModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(OrderModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param OrderModel
     */
    @Override
    public OrderModel  searchByPage(OrderModel  model) throws SQLException{
        PageDataModel<OrderModel> pageModel=mapper.listByPage(model);
        return (OrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public OrderModel  searchById(Long id)throws SQLException {
        OrderModel  model=new OrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public OrderModel searchByModel(OrderModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public OrderModel listOrderAndItemByPage(OrderModel model) throws SQLException {
		 PageDataModel<OrderModel> pageModel=mapper.listOrderAndItemByPage(model);
	        return (OrderModel ) pageModel.getModel();
	}
	/**
	 * 根据条件获取电商订单信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public int queryList(OrderModel model) throws SQLException {
		return mapper.queryList(model);
	}
	/**
	 * 根据条件获取需要导出的数据
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getExportOrderMap(OrderDTO dto) throws SQLException {
		return mapper.getExportOrderMap(dto);
	}

	/**
	 * 根据外部单查询电商订单
	 * @param listExternalCode
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<OrderModel> selectByExternalCode(List listExternalCode,Long merchantId) throws SQLException {
		List<OrderModel> selectByExternalCode = mapper.selectByExternalCode(listExternalCode,merchantId);
		return selectByExternalCode;
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
     * 获取列表数据（表头）-分页
     * @param OrderModel
     */
    @Override
    public OrderDTO  newDtoListByPage(OrderDTO  dto) throws SQLException{
        PageDataModel<OrderDTO> pageModel=mapper.newDtoListByPage(dto);
        return (OrderDTO ) pageModel.getModel();
    }
	@Override
	public int getImportOrderCountByIds(List<Long> ids) throws Exception {
		return mapper.getImportOrderCountByIds(ids) ;
	}
	@Override
	public Integer getGoodNumByExternalCodeAndGoodsId(Map<String, Object> map) {
		return mapper.getGoodNumByExternalCodeAndGoodsId(map);
	}
	@Override
	public OrderDTO searchDtoById(OrderDTO dto) {
		return mapper.searchDtoById(dto);
	}
	@Override
	public List<OrderDTO> businessUnitRecordExport(OrderDTO dto) throws SQLException {
		return mapper.businessUnitRecordExport(dto);
	}
	@Override
	public OrderDTO listBusinessUnitRecordByPage(OrderDTO dto) throws SQLException {
		PageDataModel<OrderDTO> pageModel =  mapper.listBusinessUnitRecordByPage(dto);
		 return (OrderDTO ) pageModel.getModel();
	}
	/**
	 * 迁移数据到历史表
	 * */
	@Override
	public int synsMoveToHistory(Map<String,Object> map){
		return mapper.synsMoveToHistory(map);
	}
	/**
	 * 删除已迁移到历史表的数据
	 * */
	@Override
	public int delMoveToHistory(Map<String,Object> map){
		return mapper.delMoveToHistory(map);
	}
	/**
	 *  获取税费运费不为0电商订单
	 */
	@Override
	public List<Long> getApportionOrder(String startDate,String endDate) {
		return mapper.getApportionOrder(startDate, endDate);
	}

	@Override
	public List<OrderDTO> listOrderByDTO(OrderDTO dto) throws SQLException {
		return mapper.listOrderByDTO(dto);
	}

	@Override
	public List<Map<String, Object>> countOrderByDTO(OrderDTO dto) throws SQLException {
		return mapper.countOrderByDTO(dto);
	}

	@Override
	public List<OrderModel> selectByExOrderId(List listExOrderIds, Long merchantId) throws SQLException {
		return mapper.selectByExOrderId(listExOrderIds, merchantId);
	}

	@Override
	public List<Map<String, Object>> countOrderByCostDTO(OrderDTO dto) throws SQLException {
		return mapper.countOrderByCostDTO(dto);
	}

	@Override
	public Integer batchUpdateStatus(String isGenerate, List<Long> ids) {
		return mapper.batchUpdateStatus(isGenerate, ids);
	}

	@Override
	public Integer batchUpdateStatusByCode(String isGenerate, List<String> codes) {
		return mapper.batchUpdateStatusByCode(isGenerate,codes);
	}

	@Override
	public Integer getExportOrderCount(OrderDTO dto) throws SQLException {
		return mapper.getExportOrderCount(dto);
	}

	@Override
	public Integer batchSave(List<OrderModel> list) throws SQLException {
		return mapper.batchSave(list);
	}

	@Override
	public List<OrderDTO> selectDTOListByMap(Map<String, Object> paramMap) {
		return mapper.selectDTOListByMap(paramMap);
	}

	@Override
	public int queryInvailDtoListCount(OrderDTO orderDTO) {
		return mapper.queryInvailDtoListCount(orderDTO);
	}

	@Override
	public OrderDTO listInvailDTOByPage(OrderDTO orderDTO) {
		PageDataModel<OrderDTO> pageModel =  mapper.listInvailDTOByPage(orderDTO);
		return (OrderDTO ) pageModel.getModel();
	}
	@Override
	public List<OrderDTO> listDTO(OrderDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}

}
