package com.topideal.order.service.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;

public interface MaterialOutDepotService {
	/**
	 * 列表 分页
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	MaterialOutDepotDTO listDTOByPage(MaterialOutDepotDTO dto,User user) throws Exception;

	/**
	 * 根据领料出库单id 获取表体信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<MaterialOutDepotItemDTO> getItemDetail(Long orderId) throws Exception;
	
	/**
	 * 根据条件查询数据
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<MaterialOutDepotDTO> listOrderDTO(MaterialOutDepotDTO dto,User user) throws Exception;
	
	/**
	 * 根据id查询信息
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	MaterialOutDepotDTO searchDetail(Long id) throws Exception;
	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> importMaterialOutDepot(List<List<Map<String, String>>> data, User user) throws Exception;
	/**
	 * 审核
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	void auditMaterialOutDepot(String ids, User user) throws Exception;
	/**
	 * 删除
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	void delMaterialOutDepot(String ids, User user) throws Exception;
}
