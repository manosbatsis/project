package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomerBankMerchantRelMapper extends BaseMapper<CustomerBankMerchantRelModel> {

	/**
	 * 根据条件删除 商家客户银行信息
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int deleteByParam(Map<String, Object> map)throws SQLException;


}
