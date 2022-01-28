package com.topideal.order.service.bill;

import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author huangrenya
 **/
public interface ReceiveAgingReportItemService {

    List<ReceiveAgingReportItemDTO> receiveAgingReportItem(ReceiveAgingReportItemDTO dto) throws SQLException;

}
