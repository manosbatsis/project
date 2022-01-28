package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售出库
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleOutDepotMapper extends BaseMapper<SaleOutDepotModel> {
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<SaleOutDepotModel> queryListByPage(SaleOutDepotModel model)throws SQLException;
	/**
	 * 获取销售订单的商品的累计出库数量
	 * @param saleOrderId
	 * @param goodsId
	 * @return
	 */
	Integer getTotalNumByOrderGoods(Map<String, Object> paramMap);

	/**
	 * 根据条件查询
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotModel> queryList(SaleOutDepotModel model)throws SQLException;
	/**
	 * 根据条件获取出库明细（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<SaleOutDepotModel> queryDetailsListByPage(SaleOutDepotModel model)throws SQLException;
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
	 * 导入上架
	 * @param data
	 * @param id
	 * @param name
	 * @param merchantId
	 * @param merchantName
	 * @param topidealCode
	 * @param relMerchantIds
	 * @return
	 * @throws SQLException
	 */
	Map importSaleOutShelf(Map<Integer, List<List<Object>>> data, Long id, String name, Long merchantId,
			String merchantName, String topidealCode,String relMerchantIds) throws SQLException;
	/**
	 * 根据ID查询手动导入订单总数
	 * @param ids
	 * @return
	 */
	int getImportOrderCountByIds(@Param("ids")List<Long> ids);
	/**
	 * 根据销售订单ID和商品货号获取该商品的出库数量
	 * @param map
	 * @return
	 */
	Integer getTransferNum(Map<String, Object> map);

	SaleOutDepotDTO searchDtoById(SaleOutDepotDTO dto);

	PageDataModel<SaleOutDepotDTO> queryDTOListByPage(SaleOutDepotDTO dto)throws SQLException ;

	 List<SaleOutDepotDTO> queryDTOList(SaleOutDepotDTO dto) throws SQLException;
	/**
	 * 根据条件获取出库明细（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	 PageDataModel<SaleOutDepotDTO> queryDTODetailsListByPage(SaleOutDepotDTO dto)throws SQLException;
	 List<SaleOutDepotDTO> queryDTODetailsList(SaleOutDepotDTO dto) throws SQLException ;

	List<SaleOutDepotModel> listSaleOutNoShelfRecord(Map<String, Object> map)throws SQLException;
	/**
	 * 根据某个销售订单ID修改销售出库单
	 * @return
	 */
	int modifyBySaleOrderId(SaleOutDepotModel saleOutDepotModel);

	/**
	 * 根据销售订单编号和条码获取出库数量
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

