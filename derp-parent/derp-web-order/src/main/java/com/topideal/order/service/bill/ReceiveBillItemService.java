package com.topideal.order.service.bill;

import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.ReceiveBillItemModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillItemService {

    /**
     * 根据商品、po号、项目类型统计应收账单明细
     */
    List<ReceiveBillItemDTO> itemListGroupByBillId(List<Long> billIds) throws SQLException;
}
