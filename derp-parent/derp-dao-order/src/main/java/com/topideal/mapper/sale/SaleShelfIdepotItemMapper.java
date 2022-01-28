package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;
import com.topideal.entity.vo.sale.SaleShelfIdepotItemModel;
import com.topideal.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface SaleShelfIdepotItemMapper extends BaseMapper<SaleShelfIdepotItemModel> {

	/**唯品4.0-获取商家+po+货号上架入库总量*/
	Integer getshelfInNum(Map<String, Object> map);

	int delItemByShelfIdepotIds(@Param("list") List<Long> shelfIdepotIdList) throws SQLException;

	List<SaleShelfIdepotItemDTO> listDTO(SaleShelfIdepotItemDTO dto);

	List<SaleShelfIdepotItemDTO> getItemsGroupByBatch(Map<String, Object> paramMap);
}
