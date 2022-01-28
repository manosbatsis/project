package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售出库表体 mapper
 * @author lian_
 */
@MyBatisRepository
public interface SaleOutDepotItemMapper extends BaseMapper<SaleOutDepotItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	List<SaleOutDepotItemDTO> listItemDTO(SaleOutDepotItemDTO dto) throws SQLException;
	
	List<SaleOutDepotItemDTO> listItemByOverdueDate(SaleOutDepotItemDTO dto) throws SQLException ;

	/**生成上架入库-查询出库单商品剩余可分配余量按失效日期升序*/
	List<Map<String,Object>> listCountSurplusNum(Map<String,Object> map);
	
	/**
	 * 分组查询销售出库单商品id
	 * @param saleOutDepotId
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getItemGroupByOutId(@Param("saleOutDepotId") Long saleOutDepotId)throws SQLException;
}

