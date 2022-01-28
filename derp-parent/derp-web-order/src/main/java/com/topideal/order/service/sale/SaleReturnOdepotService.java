package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;


/**
 * 销售退货出库单 service
 * */
public interface SaleReturnOdepotService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleReturnOdepotDTO listSaleReturnOdepotByPage(SaleReturnOdepotDTO dto, User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleReturnOdepotDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<SaleReturnOdepotItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 根据条件获取销售退货出库信息
	 * @param model
	 * @return
	 */
	List<SaleReturnOdepotDTO> listSaleReturnOdepot(SaleReturnOdepotDTO dto, User user)throws SQLException;
	/**
	 * 保存出库打托信息
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean saveOdepotReport(String json, User user) throws Exception;
	/**
	 * 导入销售退出库单
	 * @param user
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importSaleReturnOdepot(User user, List<List<Map<String, String>>> data) throws Exception;
	/**
	 * 审核
	 * @param list
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> auditSaleReturnOdepot(List list, User user)throws Exception;
	
	/**
	 * 删除
	 * @param ids
	 * @throws Exception
	 */
	void delSaleReturnOdepot(String ids)throws Exception;
	
}
