package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mapper.BaseMapper;

/**
 * 爬虫商品对照表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface MerchandiseContrastMapper extends BaseMapper<MerchandiseContrastModel> {

	PageDataModel<MerchandiseContrastDTO>  searchDTOByPage(MerchandiseContrastDTO  dto);
	
	MerchandiseContrastDTO getDTODetails(MerchandiseContrastDTO dto) ;

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

