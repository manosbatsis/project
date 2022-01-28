package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.purchase.PurchaseWriteOffItemDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库商品列表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface PurchaseWarehouseItemMapper extends BaseMapper<PurchaseWarehouseItemModel> {

	/**
	 * 根据勾选的入库单id查询入库勾稽明细
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseItemModel> getDetails(List list) throws SQLException;

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	
	/**
	 * 查询入库单商品、数量按商品分组合并
	 * */
	public List<Map<String, Object>> getWarehouseItem(Map<String,Object> paramMap);

	/**
	 * 根据采购入库单id集合查询表体
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWriteOffItemDTO> getDetailsByIds(@Param("ids") List<Long> ids) throws SQLException;
}

