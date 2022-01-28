package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;

import java.util.List;
import java.util.Map;


public interface PurchaseSdOrderSditemDao extends BaseDao<PurchaseSdOrderSditemModel>{



    /**
     *  根据采购SD单号、po号、商品货号查询采购SD单明细
     * @param map
     * @return
     * @throws
     */
    List<PurchaseSdOrderSditemModel> getDetailsByReceive (Map<String, Object> map);







}
