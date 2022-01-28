package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.vo.sale.SaleShelfIdepotModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleShelfIdepotMapper extends BaseMapper<SaleShelfIdepotModel> {

	PageDataModel<SaleShelfIdepotDTO> getListByPage(SaleShelfIdepotDTO dto);

	SaleShelfIdepotDTO searchDTOById(Long id);

	Integer getOrderCount(SaleShelfIdepotDTO dto);

	List<Map<String, Object>> getExportList(SaleShelfIdepotDTO dto);

	List<SaleShelfIdepotDTO> listDTO(SaleShelfIdepotDTO dto);

}
