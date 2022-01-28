package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;

/**
 * 销售退货订单 dao
 * @author lian_
 *
 */
public interface SaleReturnOrderDao extends BaseDao<SaleReturnOrderModel>{
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
     * 分页查询
     * @param dto
     * @return
     */
	SaleReturnOrderDTO  searchDTOByPage(SaleReturnOrderDTO dto)throws SQLException;
	
    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
	SaleReturnOrderDTO searchDTOById(Long id)throws SQLException;
	/**
	 * 修改 销售退货订单 的 退出仓库id和仓库名称为null
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int modifyCulomNULL(Long id)throws SQLException;
	
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
	Integer batchUpdateStatus(String isGenerate, List<String> returnCodes);
}

