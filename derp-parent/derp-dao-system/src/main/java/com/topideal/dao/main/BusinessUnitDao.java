package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;

import java.sql.SQLException;
import java.util.List;


public interface BusinessUnitDao extends BaseDao<BusinessUnitModel> {

	BusinessUnitModel judgeQuote(BusinessUnitModel model);

	BusinessUnitDTO searchDTOByPage(BusinessUnitDTO dto);
		
	 /**查询编码/名称是否已存在 */
	List<BusinessUnitDTO> getcheckCodeName(BusinessUnitDTO model);

	List<BusinessUnitDTO> listDTO(BusinessUnitDTO dto) throws SQLException;

	/**
	 * list dto 模糊查询
	 * @param businessUnitDTO
	 * @return
	 */
    List<BusinessUnitDTO> listDTOByLike(BusinessUnitDTO businessUnitDTO);
}
