package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.vo.main.CustomerSalePriceModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 客户销售价格表 dao
 * @author lian_
 */
public interface CustomerSalePriceDao extends BaseDao<CustomerSalePriceModel> {
	
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceDTO> getExportList(CustomerSalePriceDTO dto)throws SQLException;
		
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerSalePriceDTO getListByPage(CustomerSalePriceDTO dto) throws SQLException;

	/**
	 * 弹框 客户列表
	 * @param model
	 * @return
	 */
	CustomerSalePriceModel getCustomerByPage(CustomerSalePriceModel model) throws SQLException;

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerSalePriceModel getDetails(CustomerSalePriceModel model)throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceModel> getCodeById(Long id) throws SQLException;



	int batchDelById(List<Long> ids) throws SQLException;
	
	/**
	 * 根据 商家id和客户id和商品货号查询非本id下客户销售价格表信息
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceModel> findByIDAndGoodsNo(CustomerSalePriceModel model)throws SQLException;
	
	/**
	 * 单个删除
	 */
	
	int delete(Long id) throws SQLException;

	/**
	 * 分别获取不同的状态
	 * @return
	 */
	List<Map<String, Object>> getCountStatus(CustomerSalePriceDTO dtoSalePrice);

    CustomerSalePriceDTO searchByDTO(CustomerSalePriceDTO customerSalePriceDTO);
}

