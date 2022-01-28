package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售退货订单 service
 * */
public interface SaleReturnDeclareOrderService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleReturnDeclareOrderDTO listDTOByPage(SaleReturnDeclareOrderDTO dto, User user)throws Exception;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleReturnDeclareOrderDTO searchDetail(Long id) throws Exception;

	/**
	 * 销售退转预申报
	 * @param saleReturnIds
	 * @return
	 * @throws Exception
	 */
	SaleReturnDeclareOrderDTO saleReturnToDeclareOrder(String saleReturnIds) throws Exception;
	/**
	 * 根据ID获取商品明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SaleReturnDeclareOrderItemModel> getItemList(Long declareId) throws Exception;

	/**
	 * 新增
	 * @param json
	 * @param userId
	 * @param name
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	void addOrModifySaleReturnOrder(SaleReturnDeclareOrderDTO dto, User user) throws Exception;
	/**
	 * 修改
	 * @param json
	 * @param userId
	 * @param name
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	void auditSaleReturnOrder(SaleReturnDeclareOrderDTO dto, User user) throws Exception ;

	/**
	 * 删除
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	void delSaleReturnOrder(String ids ) throws Exception;

	/**
	 * 查询导出数据
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> listForExport(SaleReturnDeclareOrderDTO dto, User user) throws Exception;

}
