package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.bill.ReceiveAgingReportItemDao;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportItemModel;
import com.topideal.order.service.bill.ReceiveAgingReportItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangrenya
 **/
@Service
public class ReceiveAgingReportItemServiceImpl  implements ReceiveAgingReportItemService {

    @Autowired
    private ReceiveAgingReportItemDao receiveAgingReportItemDao;

    @Override
    public List<ReceiveAgingReportItemDTO> receiveAgingReportItem(ReceiveAgingReportItemDTO item) throws SQLException {
        List<ReceiveAgingReportItemDTO> dtoList=new ArrayList<>();
        int exportItemNum = receiveAgingReportItemDao.countTempBillNum(item);
        int pageSize = 2000;
        for (int i = 0; i< exportItemNum; ) {
            int pageSub = (i + pageSize) < exportItemNum ? (i + pageSize) : exportItemNum;
            item.setBegin(i);
            item.setPageSize(pageSize);
            List<ReceiveAgingReportItemDTO> itemDTOS = receiveAgingReportItemDao.listForExportTempItemPage(item);
            for(ReceiveAgingReportItemDTO itemDTO:itemDTOS){
                itemDTO.setChannelType(DERP.getLabelByKey(DERP_ORDER.receiveAging_channelTypeList, itemDTO.getChannelType()));
                itemDTO.setOrderType(DERP.getLabelByKey(DERP_ORDER.receiveAging_orderTypeList, itemDTO.getOrderType()));
            }
            dtoList.addAll(itemDTOS);
            i = pageSub;
        }
        return dtoList;
    }
}
