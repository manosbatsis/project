package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售订单商品 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleOrderItemMapper extends BaseMapper<SaleOrderItemModel> {
	
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param orderId
	 */
	void delBesidesIds(@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	/**查询销售单商品总数量
	 * @param code 销售单号
	 * @param goodsId 商品id
	 * */
	Integer getGoodsNumBySum(Map<String, Object> map);
	
	/**
	 * 根据表头Id查询的数据（除itemIds之外的数据） 要删除销售商品id
	 * @param itemIds
	 * @param orderId
	 * @return
	 */
	List<SaleOrderItemModel> getListByBesidesIds (@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);
	
    /**
     * 查询所有数据
     * @return
     */
    List<SaleOrderItemDTO> listSaleOrderItemDTO(SaleOrderItemDTO dto)throws SQLException;

	/**
	 *  根据销售单号、po号、商品货号查询销售单明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<SaleOrderItemModel> getDetailsByReceive (Map<String, Object> map);
	/**
	 * 根据销售id 获取上架数量和上架金额
	 * @param orderId
	 * @return
	 */
	Map<String,Object> getShelfNumAndAmountByOrderId(Long orderId);
	
	int modifyWithNull(SaleOrderItemModel model) throws SQLException;
}

