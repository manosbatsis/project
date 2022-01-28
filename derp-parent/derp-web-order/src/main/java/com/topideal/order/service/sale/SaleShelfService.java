package com.topideal.order.service.sale;

import com.topideal.entity.dto.sale.SaleShelfDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SaleShelfService {

    /**
	 * 根据ID获取明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SaleShelfDTO> listItemBySaleShelfId(Long id) throws SQLException;

	/**
	 * 通过销售订单id查询上架明细表
	 * @param id
	 * @return
	 */
    List<SaleShelfDTO> searchDetailByOrderId(Long id) throws Exception ;

	/**
	 * 通过销售订单id 和po查询上架明细
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getItemByPoNoAndOrderId(Long id, List<String> poNoList);
	/**
	 * 获取所有数据
	 * @param id
	 * @return
	 */
    List<SaleShelfDTO> listDTO(SaleShelfDTO dto) throws Exception ;
}
