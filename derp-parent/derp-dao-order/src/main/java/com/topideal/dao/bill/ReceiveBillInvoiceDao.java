package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface ReceiveBillInvoiceDao extends BaseDao<ReceiveBillInvoiceModel> {


    ReceiveBillInvoiceDTO searchDTOByPage(ReceiveBillInvoiceDTO dto);

    /**
     * 根据条件查询导出
     */
    List<Map<String,Object>> listForExport(ReceiveBillInvoiceDTO dto);

    List<ReceiveBillInvoiceDTO> listDTO(ReceiveBillInvoiceDTO dto) throws SQLException;

    /**
     * 根据关联平台结算单查询
     */
    List<ReceiveBillInvoiceModel> searchByRelCodes(List<String> statementCodes) throws SQLException;

    ReceiveBillInvoiceDTO searchByDTO(ReceiveBillInvoiceDTO dto) throws SQLException;
}
