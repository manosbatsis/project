package com.topideal.dao.transfer;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderVo;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.entity.vo.transfer.TransferOrderModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨订单 dao
 * @author lian_
 *
 */
public interface TransferOrderDao extends BaseDao<TransferOrderModel>{
		

	/**
	 * 模糊查询
	 * @return
	 * @throws SQLException
	 */
	TransferOrderDTO getById(Long id) throws SQLException;

    /**
     * 条件过滤查询，会查询出表体、商品
     * @param transferOrder
     * @return
     */
	TransferOrderModel getDetails(TransferOrderModel transferOrder);
	/**
	 * 将调拨实体类映射成Vo实体类
	 * @param id
	 * @param merchantId
	 * @return
	 */
	TransferOrderVo searchVoById(Long id, Long merchantId);

	/**
	 * 调入调出流水列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	TransferOutInBean  listTransferOutInPage(TransferOutInBean model);

	public TransferOrderModel getByMap(Map<String, Object> map);
	/**
     * 根据调拨单id统计调出商品数量
     * */
    public List<Map<String, Object>> getItemSumByIds(List<Long> Ids);
    
	/**
	 * 分页查询dto
	 * @Param
	 * @return
	 */
    public TransferOrderDTO listDtoByPage(TransferOrderDTO dto) throws SQLException;

	/**
	 * 根据商家、调入调出仓库、po单号、商品货号、调拨入库单据状态为“已入库”统计调拨入量
	 * @param map
	 * @return
	 */
	public Integer getTransferInNumByMap(Map<String, Object> map);

	/**
	 * 根据商家、调入调出仓库、po单号、商品货号、调拨出库单据状态为“已出库”统计调拨出量
	 * @param map
	 * @return
	 */
	public Integer getTransferOutNumByMap(Map<String, Object> map);

	/**
	 * 根据条件统计导出调拨订单数量
	 */
	public Long countForExport(TransferOrderDTO dto);

	/**
	 * 根据条件查询导出调拨订单
	 */
	List<Map<String, Object>> listForExport(TransferOrderDTO dto);

	/**
	 * 根据条件查询导出调拨订单表体
	 */
	public List<Map<String, Object>> listForExportItem(TransferOrderDTO dto);

	/**
	 * 根据Map查询List
	 * @param param
	 * @return
	 */
    List<TransferOrderDTO> listDTObyMap(Map<String, Object> param);

	/**
	 * 更新数据，为空也更新
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int modifyWithNull(TransferOrderModel model) throws SQLException;
}

