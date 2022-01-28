package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.ReceiveBillVerifyItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.order.webapi.bill.form.ReceiveBillVerifyForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillVerifyItemService {

    List<ReceiveBillVerifyItemModel> listByBillId(Long billId) throws SQLException;

    Map<String, String> saveVerifyItem(ReceiveBillVerifyItemDTO dto, User user) throws Exception;

    Map<String, String> saveAdvanceVerifyItem(String advanceIds) throws Exception;

    Map<String, String> saveAPIVerifyItem(ReceiveBillVerifyForm form, User user) throws Exception;
}
