package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 电商订单表体 mapper
 * @author lian_
 */
@MyBatisRepository
public interface OrderItemMapper extends BaseMapper<OrderItemModel> {
	
	/**
	 * 根据表头ID获取详细信息(包括商品批次信息)
	 * @param id
	 * @return
	 */
	List<OrderItemModel> listItemByOrderId(Long id);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	List<OrderItemDTO> listItemDTO(OrderItemDTO dto);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);

	List<OrderItemDTO> listItemByOrderDTO(@Param("orderIds") List<Long> orderIds);

	/*根据电商订单id集合和事业部id查询关联的电商订单表体*/
	List<OrderItemDTO> listItemByOrderIdsAndBuId(@Param("orderIds")List<Long> orderIds,@Param("buId") Long buId);

	/*根据电商订单id集合查询对应的结算金额最高的商品明细*/
	List<OrderItemModel> getMaxPriceByOrderId(@Param("orderIds")List<Long> orderIds, @Param("buIdFlag") Boolean buIdFlag);

	/*根据电商订单统计事业部*/
	List<Long> listBuByOrder(OrderDTO dto);
	//批量新增
	Integer batchSave(List<OrderItemModel> list) throws SQLException;
	//批量更新
	void batchUpdate(List<OrderItemModel> list) throws SQLException;
}

