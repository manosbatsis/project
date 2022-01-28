package com.topideal.order.service.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;

public interface SaleSdOrderService {
	/**
	 * 获取列表信息 分页
	 * @param dto
	 * @return
	 */
	SaleSdOrderDTO listDTOByPage(SaleSdOrderDTO dto,User user) throws Exception;
	
	/**
	 * 获取详情信息
	 * @param dto
	 * @return
	 */
	SaleSdOrderDTO searchDetail(Long id) throws Exception;
	
	/**
	 * 根据orderId获取表体信息
	 * @param dto
	 * @return
	 */
	List<SaleSdOrderItemDTO> searchItemDetail(Long sdOrderId, String orderType) throws Exception;
	
	/**
	 * 获取导出数据
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> exportSaleSdOrder(SaleSdOrderDTO dto,User user,String operate) throws Exception;
	
	/**
	 * 逻辑删除销售sd单
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	void delSaleSdOrder(String ids , User user) throws Exception;
	/**
	 * 编辑保存
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	String saveSaleSdOrder(SaleSdOrderDTO dto , User user) throws Exception;

	/**
	 * 导入销售SD
	 * @param data
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importSaleSdOrder(List<List<Map<String, String>>> data, User user) throws Exception;

}
