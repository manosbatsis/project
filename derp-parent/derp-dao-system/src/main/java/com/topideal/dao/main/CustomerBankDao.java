package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.vo.main.CustomerBankModel;


public interface CustomerBankDao extends BaseDao<CustomerBankModel> {


    /**
     * 根据商家+客户查询客户银行信息
     * @param customerId
     * @param merchantId
     * @return
     */
    public List<CustomerBankDTO> getCustomerBySupplierId(Long customerId, Long merchantId);

    /**
     * 获取客户银行信息
     * @param dto
     * @return
     */
    public List<CustomerBankDTO> getCustomerBankList(CustomerBankDTO dto);
    

	/**
	 * 根据条件删除 客户银行信息
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int deleteByParam(Map<String, Object> map)throws SQLException;






}
