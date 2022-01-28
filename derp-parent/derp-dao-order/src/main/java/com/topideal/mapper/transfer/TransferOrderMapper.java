package com.topideal.mapper.transfer;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderVo;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨订单 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferOrderMapper extends BaseMapper<TransferOrderModel> {

	/**
	 * 模糊查询
	 * @return
	 * @throws SQLException
	 */
	TransferOrderDTO  getById(Long id) throws SQLException;
	/**
     * 条件过滤查询，会查询出表体、商品
     * @return
     */
	TransferOrderModel getDetails(TransferOrderModel model);
	
	TransferOrderModel getByMap(Map<String, Object> map);
	
	/**
	 * 将调拨实体类映射成Vo实体类
	 * @param id
	 * @param merchantId
	 * @return
	 */
	TransferOrderVo getVo(TransferOrderModel model);
	/**
	 * 调入调出流水列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	PageDataModel<TransferOutInBean> listTransferOutInByPage(TransferOutInBean model);
	
	  /**
     * 查询所有数据
     * @return
     */
    PageDataModel<TransferOrderModel> getListByPage(TransferOrderModel model)throws SQLException;
    /**
     * 根据调拨单id统计调出商品数量
     * */
    public List<Map<String, Object>> getItemSumByIds(List<Long> Ids);

	/**
	 * 查询所有数据
	 * @return
	 */
	PageDataModel<TransferOrderDTO> getDtoListByPage(TransferOrderDTO dto)throws SQLException;

	/**
	 * 根据商家、调入调出仓库、po单号、商品货号、调拨入库单据状态为“已入库”统计调拨入量
	 * @param map
	 * @return
	 */
	Integer getTransferInNumByMap(Map<String, Object> map);

	/**
	 * 根据商家、调入调出仓库、po单号、商品货号、调拨出库单据状态为“已出库”统计调拨出量
	 * @param map
	 * @return
	 */
	Integer getTransferOutNumByMap(Map<String, Object> map);

	/**
	 * 根据条件统计导出调拨订单数量
	 */
	Long countForExport(TransferOrderDTO dto);

	/**
	 * 根据条件查询导出调拨订单
	 */
	List<Map<String, Object>> listForExport(TransferOrderDTO dto);

	/**
	 * 根据条件查询导出调拨订单表体
	 */
	List<Map<String, Object>> listForExportItem(TransferOrderDTO dto);

    List<TransferOrderDTO> listDTObyMap(Map<String, Object> param);

	/**
	 * 更新数据，为空也更新
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int modifyWithNull(TransferOrderModel model) throws SQLException;
}

