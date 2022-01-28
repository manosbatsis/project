package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.SalePoRelModel;

import java.util.List;
import java.util.Map;


public interface SalePoRelDao extends BaseDao<SalePoRelModel> {


    int delbyPoNoAndOrderId(SalePoRelModel model);

    int getCountByOrderId(Long orderId);

    List<SalePoRelModel> getAllByOrderId (Long orderId,Long merchantId);

    List<SalePoRelModel> getAllByNoDelete(Map<String,Object> paramMap);

}
