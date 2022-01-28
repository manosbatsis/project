package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;

/**
 * 爬虫商品对照表 dao
 * @author lian_
 *
 */
public interface MerchandiseContrastDao extends BaseDao<MerchandiseContrastModel>{
		
	 
	MerchandiseContrastDTO  searchDTOByPage(MerchandiseContrastDTO  dto) throws SQLException;

	MerchandiseContrastDTO searchDTOById(Long id) throws SQLException;

	/**
	 * 根据平台+爬虫货号+系统商品货号判断该商品对照表是否存在
	 */
	MerchandiseContrastModel isExistModel(MerchandiseContrastModel model) throws SQLException;

	/**
	 * 根据条件查询导出
	 */
	List<Map<String, Object>> listForExport(MerchandiseContrastDTO dto) throws SQLException;

	/**查询sku对应的经销货号*/
	List<MerchandiseContrastDTO> listContrastAndItem(MerchandiseContrastModel model);

}

