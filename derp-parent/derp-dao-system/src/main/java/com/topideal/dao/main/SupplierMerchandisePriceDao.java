package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品价格表 dao
 * @author lianchenxing
 *
 */
public interface SupplierMerchandisePriceDao extends BaseDao<SupplierMerchandisePriceModel> {


	/**
	 * 列表查询(分页)
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SupplierMerchandisePriceDTO getListByPage(SupplierMerchandisePriceDTO dto) throws SQLException;
	/**
	 * 根据条件查询
	 * @return
	 */
	List<SupplierMerchandisePriceDTO> queryDTOList(SupplierMerchandisePriceDTO dto)throws SQLException;

	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SupplierMerchandisePriceDTO getDetails(Long id)throws SQLException;
	
	/**
	 * 采购订单，获取采购价格
	 * @param model
	 * @return
	 */
	SupplierMerchandisePriceModel getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model);

	/**
	 * 统计各个状态下的采购价格数量
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> statisticsStateNum(SupplierMerchandisePriceDTO dto);

	List<SupplierMerchandisePriceModel> listByIds(List<Long> ids);

	SupplierMerchandisePriceDTO searchDTOById(Long id);

	/**
	 * 根据“公司+事业部+条码+库存类型”分组查询审核日期最新的几率
	 * @param model
	 * @return
	 */
	List<SupplierMerchandisePriceModel> listGroupByStockType(SupplierMerchandisePriceModel model);
}

