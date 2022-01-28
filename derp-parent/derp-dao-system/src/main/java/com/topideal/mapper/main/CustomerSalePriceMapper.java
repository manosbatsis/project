package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.vo.main.CustomerSalePriceModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 客户销售价格表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface CustomerSalePriceMapper extends BaseMapper<CustomerSalePriceModel> {

	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceDTO> getExportList(CustomerSalePriceDTO dto)throws SQLException;

	/**
	 * 分页
	 */
	PageDataModel<CustomerSalePriceDTO> getListByPage(CustomerSalePriceDTO dto) throws SQLException;

	/**
	 * 弹框 客户列表
	 * @param model
	 * @return
	 */
	PageDataModel<CustomerSalePriceModel> getBrandByPage(CustomerSalePriceModel model);
	
	/**
	 * 获取详情
	 */
	CustomerSalePriceModel getDetails(CustomerSalePriceModel model) throws SQLException;

	 List<CustomerSalePriceModel> getCodeById(@Param("id") Long id) throws SQLException;
	 

	 int batchDelById(@Param("ids") List<Long> ids) throws SQLException;
	 
	 /**
		 * 根据 商家id和客户id和商品货号查询非本id下客户销售价格表信息
		 * @param model
		 * @return
		 * @throws SQLException
		 */
	 List<CustomerSalePriceModel> findByIDAndGoodsNo(CustomerSalePriceModel model)throws SQLException;

	/**
	 * 分别获取不同的状态
	 * @return
	 */
	List<Map<String, Object>> getCountStatus(CustomerSalePriceDTO dtoSalePric);

    CustomerSalePriceDTO searchByDTO(CustomerSalePriceDTO customerSalePriceDTO);
}

