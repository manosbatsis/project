package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface BusinessUnitMapper extends BaseMapper<BusinessUnitModel> {

	BusinessUnitModel judgeQuote(BusinessUnitModel model);

	PageDataModel<BusinessUnitDTO> searchDTOByPage(BusinessUnitDTO dto);

    /**查询编码/名称是否已存在 */
	List<BusinessUnitDTO> getcheckCodeName(BusinessUnitDTO model);

	List<BusinessUnitDTO> listDTO(BusinessUnitDTO dto) throws SQLException ;

	/**
	 * 模糊查询
	 * @param businessUnitDTO
	 * @return
	 */
    List<BusinessUnitDTO> listDTOByLike(BusinessUnitDTO businessUnitDTO);
}
