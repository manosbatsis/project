package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportItemModel;

import java.sql.SQLException;
import java.util.List;


public interface ReceiveAgingReportItemDao extends BaseDao<ReceiveAgingReportItemModel> {
		

    /*
        根据表头id查询表体
     */
    public List<ReceiveAgingReportItemDTO> getAgingReportId(Long id);


    /**
     *
     * @param ids
     */
    void deleteReceiveAgingReportItem(List<Long> ids);


    int countTempBillNum(ReceiveAgingReportItemDTO model);

    List<ReceiveAgingReportItemDTO> listForExportTempItemPage(ReceiveAgingReportItemDTO dto) throws SQLException;

}
