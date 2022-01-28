package com.topideal.dao.sale;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.vo.sale.SaleShelfIdepotModel;


public interface SaleShelfIdepotDao extends BaseDao<SaleShelfIdepotModel> {

	SaleShelfIdepotDTO getListByPage(SaleShelfIdepotDTO dto);

	SaleShelfIdepotDTO searchDTOById(Long id);

	Integer getOrderCount(SaleShelfIdepotDTO dto);

	List<Map<String, Object>> getExportList(SaleShelfIdepotDTO dto);
		
	List<SaleShelfIdepotDTO> listDTO(SaleShelfIdepotDTO dto);
}
