package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收发票库service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillInvoiceService {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillInvoiceDTO listReceiveBillInvoiceByPage(ReceiveBillInvoiceDTO dto, User user) throws SQLException;

    /**
     * 根据条件查询导出
     */
    List<Map<String,Object>> listForExport(ReceiveBillInvoiceDTO dto, User user);

    /**
     * 根据发票关联应收单查询关联应收账单信息
     */
    List<ReceiveBillDTO> listBillByInvoiceId(String id) throws SQLException;

    ReceiveBillInvoiceDTO searchByDTO(ReceiveBillInvoiceDTO dto) throws SQLException;

    Map<String, String> modifyStatus(Long id, User user) throws Exception;

    /**
     * 获取关联应收单提交NC信息
     */
    List<ReceiveBillToNCDTO> listReceiveBillsToNCById(String id, String dataSource) throws SQLException;

    /**
     * 同步NC
     */
    Map<String, String> synNC(String id, String dataSource, User user) throws Exception;

    /**
     * 根据查询条件导出应收对账
     */
    List<Map<String,Object>> listForBillExport(ReceiveBillInvoiceDTO dto) throws Exception;

    //根据应收账单id 批量导出提交nc信息
    String exportNcBillPDF(List<Long> ids) throws Exception;

    /**
     * 校验发票是否有至少一个已同步的应收账单，若有则不予操作同步NC
     * @param id
     */
    Map<String, String> validateSynNC(String id) throws SQLException;

    /*替换发票文件*/
    Map<String, Object> replaceInvoiceFile(String id, MultipartFile file, User user) throws Exception;
}
