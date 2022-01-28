package com.topideal.order.service.bill;

import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;

import java.util.List;

/**
 * 预收账单操作记录
 */
public interface AdvanceBillOperateItemService {

    /**
     * 根据预收账单id查看操作日志
     * @return
     */
    public List<AdvanceBillOperateItemDTO> listAdvanceBillOperateItem(Long advanceId);
}
