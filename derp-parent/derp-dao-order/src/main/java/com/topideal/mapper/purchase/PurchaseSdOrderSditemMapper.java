package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PurchaseSdOrderSditemMapper extends BaseMapper<PurchaseSdOrderSditemModel> {

    /**
     *  根据采购SD单号、po号、商品货号查询采购SD单明细
     * @param map
     * @return
     * @throws
     */
    List<PurchaseSdOrderSditemModel> getDetailsByReceive (Map<String, Object> map);

}
