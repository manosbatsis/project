package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.BillOutinDepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface BillOutinDepotDao extends BaseDao<BillOutinDepotModel> {


    /**
     * 首页——统计对应商家主体下所有代销客户的销售总量
     * @param params
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> countByCustomer(Map<String, Object> params) throws SQLException;

    /**
     * 首页——统计所有客户代销模式下销售top 10 的品牌
     * @param params
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getBillOutDepotTop10ByBrand(Map<String, Object> params) throws SQLException;






}
