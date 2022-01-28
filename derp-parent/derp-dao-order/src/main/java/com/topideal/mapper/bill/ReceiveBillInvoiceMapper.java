package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveBillInvoiceMapper extends BaseMapper<ReceiveBillInvoiceModel> {

    PageDataModel<ReceiveBillInvoiceDTO> searchDTOByPage(ReceiveBillInvoiceDTO dto);

    /**
     * 根据条件查询导出
     */
    List<Map<String,Object>> listForExport(ReceiveBillInvoiceDTO dto);

    List<ReceiveBillInvoiceDTO> listDTO(ReceiveBillInvoiceDTO dto) throws SQLException;

    /**
     * 根据关联平台结算单查询
     */
    List<ReceiveBillInvoiceModel> searchByRelCodes(@Param("statementCodes") List<String> statementCodes) throws SQLException;

    ReceiveBillInvoiceDTO searchByDTO(ReceiveBillInvoiceDTO dto) throws SQLException;
}
