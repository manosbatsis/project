package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货订单
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnOrderMapper extends BaseMapper<SaleReturnOrderModel> {
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	int delSaleReturnOrder(List ids) throws SQLException;
	/**
	 * 查询非删除状态的销售退货订单
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List <SaleReturnOrderModel> searchNotDelStatusSaleReturnOrder(SaleReturnOrderModel model)throws SQLException;
	/**
	 * 根据条件获取退货量
	 * @param map
	 * @return
	 */
	Integer getReturnCount(Map<String, Object> map);
    /**
     * 查询所有数据
     * @return
     */
    PageDataModel<SaleReturnOrderDTO> listDTOByPage(SaleReturnOrderDTO dto)throws SQLException;
    /**
     * 条件过滤查询
     * @return
     */
    SaleReturnOrderDTO getSaleReturnOrderById(SaleReturnOrderDTO dto)throws SQLException;
    
	/**
	 * 修改 销售退货订单 的 退出仓库id和仓库名称为null
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int modifyCulomNULL(@Param("id") Long id)throws SQLException;
	
	/**
	 * 查询所有数据 不分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List <SaleReturnOrderDTO> listDTO(SaleReturnOrderDTO dto)throws SQLException;

	int modifyWithNull(SaleReturnOrderModel  model) throws SQLException ;

	/**
	 * 查询类型为“退货退款”的购销销售退货订单，单据状态为已入仓/已完结
	 */
	List<SaleReturnOrderDTO> listToBTempDTO(SaleReturnOrderDTO dto);
	/**
	 * 批量更新销售退货订单是否生成暂估
	 */
	Integer batchUpdateStatus(@Param("isGenerate") String isGenerate, @Param("returnCodes") List<String> returnCodes);
}

