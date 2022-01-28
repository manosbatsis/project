package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售出库 dao
 * @author lian_
 *
 */
public interface SaleOutDepotDao extends BaseDao<SaleOutDepotModel>{
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotModel queryListByPage(SaleOutDepotModel model)throws SQLException ;
	/**
	 * 获取销售订单的商品的累计出库数量
	 * @param saleOrderId
	 * @param goodsId
	 * @return
	 */
	Integer getTotalNumByOrderGoods(Map<String, Object> paramMap);
	/**
	 * 根据条件获取销售出库清单
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotModel> queryList(SaleOutDepotModel model) throws SQLException;
	/**
	 * 根据条件获取出库明细（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotModel queryDetailsListByPage(SaleOutDepotModel model)throws SQLException;
	/**
	 * 根据条件获取出库明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotModel> queryDetailsList(SaleOutDepotModel model)throws SQLException;

	/**
	 * 根据条件获取出库总量
	 * @param map
	 * @return
	 */
	Integer getOutDepotCount(Map<String, Object> map);

	/**
	 * 查询手动导入数据
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int getImportOrderCountByIds(List<Long> list) throws Exception;
	/**
	 * 根据销售订单ID和商品货号获取该商品的出库数量
	 * @param saleOutDepotModel
	 * @return
	 */
	Integer getTransferNum(Map<String, Object> map);

	SaleOutDepotDTO searchDtoById(Long id)throws SQLException;
	/**
	 * 根据条件查询（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotDTO queryDTOListByPage(SaleOutDepotDTO dto)throws SQLException ;
	/**
	 * 根据条件获取销售出库清单
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotDTO> queryDTOList(SaleOutDepotDTO dto) throws SQLException;
	/**
	 * 根据条件获取出库明细（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotDTO queryDTODetailsListByPage(SaleOutDepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取出库明细
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotDTO> queryDTODetailsList(SaleOutDepotDTO dto)throws SQLException;
	/**
	 * 获取未上架或部分上架的记录
	 * @return
	 */
	List<SaleOutDepotModel> listSaleOutNoShelfRecord(Map<String, Object> map)throws SQLException;
	/**
	 * 根据某个销售订单ID修改销售出库单PO号
	 * @param saleOrderId
	 * @return
	 */
	int modifyBySaleOrderId(SaleOutDepotModel saleOutDepotModel);

	/**
	 * 根据销售订单编号和条码获取出库数量
	 * @param saleOutDepotDTO
	 * @return
	 */
	List<SaleOutDepotItemDTO> getSaleOutDepotCount(Map<String,Object> map);

	/**根据销售单号统计出库单商品未上架量*/
	List<Map<String,Object>> getGoodsNotShelNumList(Map<String,Object> map);
	/**根据销售单号查询销售数量-出库量 按商品分组统计未出库量*/
	List<Map<String,Object>> getGoodsNotOutNumList(Map<String,Object> map);

	/**
	 * 获取红冲以外的出库单
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotDTO> listSaleOutDepot(SaleOutDepotDTO dto) throws SQLException;
}

