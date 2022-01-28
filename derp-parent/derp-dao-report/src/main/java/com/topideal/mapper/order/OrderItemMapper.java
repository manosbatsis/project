package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.OrderItemDTO;
import com.topideal.entity.vo.order.OrderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 电商订单表体 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface OrderItemMapper extends BaseMapper<OrderItemModel> {
	List<OrderItemDTO> listItemDTO(OrderItemDTO dto);

	/**
	 * 根据外部订单号和公司查询订单表体信息
	 */
	List<OrderItemModel> listItem(@Param("externalCode") String externalCode, @Param("merchantId") Long merchantId,
                                  @Param("goodsNo") String goodsNo);

	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String, Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String, Object> map);

	/**
	 * 统计销售总金额-商品优惠金额
	 * @param id
	 * @return
	 */
	Map<String, Object> sumDiscountAndDecTotalByOrderID(@Param("orderId") Long id);

	/**
	 * 根据订单ID和货号汇总对应商品数量
	 * @param sumItemMap
	 * @return
	 */
	OrderItemModel getPopAutoVeriSumGoodsItemByOrder(Map<String, Object> sumItemMap);
}

