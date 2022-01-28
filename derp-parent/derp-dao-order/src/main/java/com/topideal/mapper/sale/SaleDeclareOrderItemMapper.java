package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.sale.SaleDeclareOrderItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleDeclareOrderItemMapper extends BaseMapper<SaleDeclareOrderItemModel> {

	/**
	 * 根据orderId删除表体信息
	 * @param orderId
	 * @throws SQLException
	 */
	int delByOrderId(Long orderId) throws SQLException;
	/**
	 *  获取销售订单对应商品的总申报数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
    Integer getToTalDeclareNum (Map<String, Object> map) throws SQLException;
    /**
     * 查询所有数据
     * @return
     */
    List<SaleDeclareOrderItemDTO> listDTO(SaleDeclareOrderItemDTO dto)throws SQLException;
    /**
	 *  获取销售商品弹窗
	 * @param map
	 * @return
	 * @throws SQLException
	 */
    PageDataModel<SaleDeclareOrderDTO> getSalePopupListByPage (SaleDeclareOrderItemDTO dto) throws SQLException;


	/**
	 * 查询所有数据 按价格排序
	 * @return
	 */
	List<SaleDeclareOrderItemModel> listOrderByPrice(SaleDeclareOrderItemModel model)throws SQLException;
}
