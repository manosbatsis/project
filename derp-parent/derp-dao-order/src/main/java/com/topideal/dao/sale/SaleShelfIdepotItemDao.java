package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;
import com.topideal.entity.vo.sale.SaleShelfIdepotItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface SaleShelfIdepotItemDao extends BaseDao<SaleShelfIdepotItemModel> {

	/**唯品4.0-获取商家+po+货号上架入库总量*/
	Integer getshelfInNum(Map<String, Object> map);

	int delItemByShelfIdepotIds(List<Long> shelfIdepotIdList) throws SQLException;

	List<SaleShelfIdepotItemDTO> listDTO(SaleShelfIdepotItemDTO dto);

	List<SaleShelfIdepotItemDTO> getItemsGroupByBatch(Map<String, Object> paramMap);


}
