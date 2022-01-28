package com.topideal.order.service.bill;

import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;

import java.util.List;
import java.util.Map;

/**
 * 预收账单核销记录
 */
public interface AdvanceBillVerifyItemService {

    /**
     * 根据预收账单id查看核销记录
     * @param id
     * @return
     */
    List<AdvanceBillVerifyItemDTO> getAdvanceById(Long id);



    /**
     * 保存核销
     * @return
     */
    Map<String,Object> saveVerifyItem(String token, AdvanceBillVerifyItemDTO dto);
}
