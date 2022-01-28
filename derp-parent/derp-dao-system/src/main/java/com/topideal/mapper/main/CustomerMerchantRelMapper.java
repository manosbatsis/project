package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 客户商家关联表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface CustomerMerchantRelMapper extends BaseMapper<CustomerMerchantRelModel> {


    CustomerMerchantRelModel searchDetail(CustomerMerchantRelModel model);
    /**
     * 获取商家客户关系表数据
     * @param model
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getCustomerMerchantRelList(Map<String, Object> map)throws SQLException;
    /**
     * 获取客户商家关系表数据
     */
    List<CustomerMerchantRelModel>getCustMerListByUser(Map<String, Object>map)throws SQLException;
}

