package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseWarehouseDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportBean;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库单
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface PurchaseWarehouseMapper extends BaseMapper<PurchaseWarehouseModel> {

	/**
	 * 获取采购入库单货单和采购入库单商品批次
	 */
	public Map<String, Object> getPurchaseItemBatch(PurchaseWarehouseModel model) throws SQLException;

	/**
     * 详情
     * @return model
     */
	public PurchaseWarehouseModel getDetails(PurchaseWarehouseModel model) throws SQLException;
	
	/**
	 * 入库明细导出
	 * @param ids
	 * @return
	 */
	List<PurchaseWarehouseExportDTO> getExportDetails(List list) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<PurchaseWarehouseDTO> getListByPage(PurchaseWarehouseDTO dto) throws SQLException;
	
	/**
	 * 根据预入库单id获取采购订单集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<String> getWarehouseListById(@Param("id") Long id) throws SQLException;
	
	/**
	 * 根据表头ids获取表体集合
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseExportBean> getItemDetails(List list) throws SQLException;
	
	/**
	 * 根据ids获取集合
	 * @param list
	 * @return
	 */
	List<PurchaseWarehouseModel> getListByIds(List<Long> list);

	PurchaseWarehouseDTO getDTODetails(PurchaseWarehouseDTO dto);

	/**
	 * 根据选择条件导出
	 * @param dto
	 * @return
	 */
	public List<PurchaseWarehouseExportDTO> getExportDetailsByDTO(PurchaseWarehouseDTO dto);

	/**
	 * 查询所有ixnx
	 * @param dto
	 * @return
	 */
	public List<PurchaseWarehouseDTO> listDTO (PurchaseWarehouseDTO dto);


	/**统计采购单未入库量
	 * purchaseId 采购单id
	 * */
	public List<Map<String,Object>> getNoInWarehouseNum(Long purchaseId);
}

