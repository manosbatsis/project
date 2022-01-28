package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotItemDTO;

/**
 * 销售退货入库单 service
 * */
public interface SaleReturnIdepotService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleReturnIdepotDTO listSaleReturnIdepotByPage(SaleReturnIdepotDTO dto, User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleReturnIdepotDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<SaleReturnIdepotItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 根据条件获取销售退货入库
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleReturnIdepotDTO> listSaleReturnIdepot(SaleReturnIdepotDTO dto, User user)throws SQLException;
	/**
	 * 导入销售退入库单
	 * @param user
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importSaleReturnIdepot(User user, List<List<Map<String, String>>> data) throws Exception;
	/**
	 * 审核
	 * @param list
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> auditSaleReturnIdepot(List list, User user)throws Exception;
	
	/**
	 * 删除
	 * @param ids
	 * @throws Exception
	 */
	void delSaleReturnIdepot(String ids)throws Exception;
	/**
	 * 生成销售SD单
	 * @param ids
	 * @param user
	 * @throws Exception 
	 */
	public void generateSaleSdOrder(String ids , User user) throws Exception;
}
