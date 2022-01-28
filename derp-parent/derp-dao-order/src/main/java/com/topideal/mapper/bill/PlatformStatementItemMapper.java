package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.vo.bill.PlatformStatementItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PlatformStatementItemMapper extends BaseMapper<PlatformStatementItemModel> {

	/**
	 * 根据订单ID删除
	 * @param platformStatementIds
	 * @return
	 */
	Integer deleteByOrderId(@Param("orderId")Long orderId);

	/**
	 * 根据选择开票的平台结算单id根据商品和品牌维度统计开票数据
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> listInvoiceItem(@Param("ids") List<Long> ids,@Param("source") String source) throws SQLException;

	PageDataModel<PlatformStatementItemDTO> getListByPage(PlatformStatementItemDTO dto);


	/**
	 * 根据选择开票的平台结算单id根据商品和类型维度统计开票数据
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> listInvoiceItemByType(@Param("ids") List<Long> ids) throws SQLException;

	/**
	 * 根据平台结算单id 统计表体数量
	 **/
	Map<String, Object> statisticsByPlatformIdList(@Param("billId") Long billId) throws SQLException;
}
