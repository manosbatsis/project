package com.topideal.order.service.bill;

import java.util.List;
import java.util.Map;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderExportDTO;
import com.topideal.mongo.entity.FileTaskMongo;

public interface PlatformStatementOrderService {

    /**查询开票数据（除唯品外）*/
    List<Map<String, Object>> listInvoiceItemInfos(List<Long> ids) throws Exception;

    /**查询唯品开票数据*/
    List<Map<String, Object>> listWPInvoiceItemInfos(List<Long> ids) throws Exception;

	/**
	 * 获取分页
	 * @param model
	 * @return
	 */
	PlatformStatementOrderDTO listPlatformStatementOrder(PlatformStatementOrderDTO dto) throws SQLException;

	/**
	 * 获取详情
	 * @param model
	 * @return
	 */
	PlatformStatementOrderDTO getDetails(Long id);

	/**
	 * 获取明细分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformStatementItemDTO listPlatformStatementItem(PlatformStatementItemDTO dto) throws SQLException;

	/**
	 * 导出
	 * @return
	 */
	String createPlatformSettlementOrderExcel(FileTaskMongo task, String basePath) throws Exception;

	/**
	 * 根据ids 查询平台结算单信息
	 * @param ids
	 * @return
	 */
	List<PlatformStatementOrderDTO> listPlatformStatementOrderByIds(List<Long> ids) throws Exception;

	/**
	 * 校验平台结算单是否满足开票条件
	 * @param ids
	 * @return
	 */
	Map<String, String> validate(List<Long> ids) throws SQLException;

	/**
	 * 生成toc应收账单
	 * @param ids
	 * @return
	 */
	Map<String, String> saveToCReceiveBill(List<Long> ids, User user) throws Exception;

	/**
	 * 生成发票html
	 * @param tempId 发票模板id
	 * @param ids 平台结算单id，多个以英文逗号隔开
	 * @param user
	 */
	String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception;

}
