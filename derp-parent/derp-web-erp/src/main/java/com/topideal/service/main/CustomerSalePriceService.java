package com.topideal.service.main;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.main.CustomerSalePriceCountDTO;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.vo.main.CustomerSalePriceModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 客户销售价格表 service
 * @author lian_
 */
public interface CustomerSalePriceService {
	
	

	List<CustomerSalePriceDTO> getExportList(CustomerSalePriceDTO dto,User user)throws SQLException;
	/**
	 * 客户销售价格列表
	 * @param model 
	 * @return
	 */
	CustomerSalePriceDTO listSalePrice(CustomerSalePriceDTO dto,User user) throws SQLException;

	/**
	 * 导入客户销售价格信息
	 * @param data 解析excel的客户销售价格数据
	 * @param merchantId
	 * @param id
	 * @return
	 */
	Map importPriceSale(Map<Integer, List<List<Object>>> data,User user)throws Exception;
	
	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	boolean delPriceSale(List<Long> ids) throws SQLException;
	
	/**
	 * 审核
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	Map<String,Object>  auditSMPrice(String ids,User user,String type) throws SQLException;

	/**
	 * 提交
	 * @return
	 */
	Map submitSMPrice(String ids,User user) throws SQLException;
	
	
	/**
	 * 根据id获取客户详情
	 * @param id
	 * @return
	 */
	CustomerSalePriceDTO searchDetail(Long id) throws SQLException;
	
    /**
     * 根据表头id获取表体信息
     * @param id
     * @return
     */
	List<CustomerSalePriceModel> listItemBySalePriceId(Long id)throws SQLException;
	/**
	 * 根据客户ID回显客户编码和主数据客户ID
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceModel> getCodeById(Long id) throws SQLException;
	
	/**
	 * 列表查询
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CustomerSalePriceModel> list(CustomerSalePriceModel model) throws SQLException;

	/**
	 * 获取状态数量
	 * @return
	 */
	CustomerSalePriceCountDTO getCountStatus(CustomerSalePriceDTO dto,User user);

	Map editSalePriceModel(CustomerSalePriceDTO dto,User user) throws SQLException;
	/**
	 * 申请作废
	 * @param user
	 * @param ids
	 * @param remark
	 * @return
	 */
	void submitInvalid(User user,String ids,String remark) throws Exception;

	/**
	 * 作废审核
	 * @param user
	 * @param ids
	 * @param auditResult
	 * @return
	 */
	void auditInvalid(User user,String ids,String auditResult) throws Exception;

	/**
	 * 新增
	 * @param user
	 * @param dto
	 * @return
	 */
    ResponseBean addCustomerPrice(User user, CustomerSalePriceDTO dto) throws Exception;

	/**
	 * 获取编码
	 * @param id
	 * @return
	 */
	String preGetCode(Long id) throws Exception;
}
