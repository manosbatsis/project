package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.vo.main.CustomerBankModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface CustomerBankMapper extends BaseMapper<CustomerBankModel> {
	
		/**
	 * 根据条件删除 客户银行信息
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int deleteByParam(Map<String, Object> map)throws SQLException;
    
    public List<CustomerBankDTO> getCustomerBySupplierId(@Param("customerId") Long customerId,@Param("merchantId") Long merchantId);

    /**
     * 获取客户银行信息
     * @param dto
     * @return
     */
    public List<CustomerBankDTO> getCustomerBankList(CustomerBankDTO dto);
    
}
