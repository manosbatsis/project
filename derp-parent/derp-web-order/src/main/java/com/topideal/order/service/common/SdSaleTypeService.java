package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;

import java.util.List;

public interface SdSaleTypeService {
	/**
	 * 查询列表信息 分页
	 * @param dto
	 * @return
	 */
	SdSaleTypeDTO listDTOByPage(SdSaleTypeDTO dto) throws Exception;
	/**
	 * 保存
	 * @param dto
	 * @throws Exception
	 */
	void saveSdSaleType(SdSaleTypeModel model,User user) throws Exception;

	/**
	 * 获取信息
	 * @param model
	 * @return
	 * @throws Exception
	 */
	SdSaleTypeDTO searchById(Long id) throws Exception;
	/**
	 * 获取SD类型下拉
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<SdSaleTypeDTO> getSdSaleTypeList(SdSaleTypeDTO model) throws Exception;

}
