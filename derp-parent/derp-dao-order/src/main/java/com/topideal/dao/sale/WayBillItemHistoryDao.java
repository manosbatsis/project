package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.WayBillItemHistoryDTO;
import com.topideal.entity.vo.sale.WayBillItemHistoryModel;

import java.util.List;


public interface WayBillItemHistoryDao extends BaseDao<WayBillItemHistoryModel> {


    /**
     * 根据电商订单id获取运单表体信息
     * @param id
     * @return
     */
    List<WayBillItemHistoryDTO> listWayBillItemByOrderId(Long id);








}
