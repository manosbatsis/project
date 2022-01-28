package com.topideal.mapper.bill;

import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ReceiveAgingReportItemMapper extends BaseMapper<ReceiveAgingReportItemModel> {

    /**
     * 根据表头id查看表体
     * @param id
     * @return
     */
    List<ReceiveAgingReportItemDTO> getAgingReportId(Long id);


    /**
     * 根据表头删除表体
     * @param ids
     * @return
     */
    int deleteReceiveAgingReportItem(@Param("idList") List<Long> ids);


    int countTempBillNum(ReceiveAgingReportItemDTO model);

    List<ReceiveAgingReportItemDTO> listForExportTempItemPage(ReceiveAgingReportItemDTO dto);
}
