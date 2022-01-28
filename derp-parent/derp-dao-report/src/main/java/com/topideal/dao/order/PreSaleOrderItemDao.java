package com.topideal.dao.order;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.PreSaleOrderItemModel;

import java.util.Map;

public interface PreSaleOrderItemDao extends BaseDao<PreSaleOrderItemModel> {

    int deleteByMap(Map<String, Object> delMap);

}
