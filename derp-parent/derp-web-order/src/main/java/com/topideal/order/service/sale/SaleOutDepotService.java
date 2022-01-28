package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 销售出库service
 * */
public interface SaleOutDepotService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotDTO listSaleOutDepotByPage(SaleOutDepotDTO dto, User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取详细信息
	 * @param id
	 * @return
	 */
	List<SaleOutDepotItemDTO> listItemBySaleOutDepotId(Long id)throws SQLException;
	/**
	 * 审核
	 * @param list
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> auditSaleOutDepot(List list, Long userId, String userName, Long merchantId, String merchantName,String topidealCode)throws Exception;
	/**
	 * 获取选中出库单的所有商品和对应数量（相同商品数量叠加）
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	Map<String,Integer> getOrderGoodsInfo(List<Long> ids)throws Exception;

	/**
	 * 根据条件获取销售出库清单
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SaleOutDepotDTO> listSaleOutDepot(SaleOutDepotDTO dto, User user) throws SQLException;
	/**
	 * 根据条件获取出库明细（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleOutDepotDTO listSaleOutDepotDetailsByPage(SaleOutDepotDTO dto, User user)throws SQLException;
	/**
	 * 根据条件获取出库明细
	 * @param model
	 * @return
	 */
	List<SaleOutDepotDTO> listSaleOutDepotDetails(SaleOutDepotDTO dto, User user)throws SQLException;

	/**
	 * 根据条件查询saleOutDepotModel
	 * @param saleOutDepotModel
	 * @return
	 * @throws Exception
	 */
	List<SaleOutDepotModel> listSaleOutDepotModel(SaleOutDepotModel saleOutDepotModel) throws Exception;
	/**
	 * 获取上架明细分页数据
	 * @param model
	 * @return
	 */
	SaleShelfDTO listSaleOutShelfByPage(SaleShelfDTO dto) throws SQLException;

	/**
	 * 手动导入销售出库单
	 * @param user
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importSaleOutDepot(User user, List<List<Map<String, String>>> data) throws Exception;
	/**
	 * 根据ID删除手动导入订单
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int delImportOrder(List<Long> list) throws Exception;

	/**
	 * 根据ID删除手动导入订单
	 * @param list
	 * @return
	 * @throws Exception
	 */
	void delSaleOutOrder(Long saleOutOrderId, User user, String token) throws Exception;

}
