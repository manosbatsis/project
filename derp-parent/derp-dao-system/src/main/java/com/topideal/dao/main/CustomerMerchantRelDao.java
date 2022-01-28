package com.topideal.dao.main;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;

/**
 * 客户商家关联表 dao
 * @author lian_
 *
 */
public interface CustomerMerchantRelDao extends BaseDao<CustomerMerchantRelModel> {


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

