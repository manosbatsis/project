package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.dto.sale.MaterialOrderItemDTO;

import java.util.List;
import java.util.Map;

public interface MaterialOrderService {
	/**
	 * 列表 分页
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	MaterialOrderDTO listDTOByPage(MaterialOrderDTO dto,User user) throws Exception;

	/**
	 * 根据表头id 获取表体信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<MaterialOrderItemDTO> getItemDetail(Long orderId) throws Exception;
	/**
	 * 保存
	 * @param json
	 * @param user
	 * @return
	 */
	String saveOrModifyOrder(String json,User user) throws Exception;
	/**
	 * 审核
	 * @param json
	 * @param user
	 * @return
	 */
	String auditMaterialOrder(String json,User user) throws Exception;
	/**
	 * 根据条件查询数据
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<MaterialOrderDTO> listMaterialOrder(MaterialOrderDTO dto,User user) throws Exception;

	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delMaterialOrder(List ids) throws Exception;
	/**
	 * 根据id查询信息
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	MaterialOrderDTO searchDetail(Long id) throws Exception;
	/**
     * 导出清关资料
     * @return
     */
    Map<String, Object> exportDepotCustomsDeclares(Long id, List<Long> fileTempIds,String type) throws Exception;

    Map importMaterialOrder(List<List<Map<String, String>>> data, User user) throws Exception;

}
