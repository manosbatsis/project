package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyAdvanceDTO;
import com.topideal.entity.vo.bill.AdvanceBillModel;


public interface AdvanceBillDao extends BaseDao<AdvanceBillModel> {
    //批量更新关联预收账单的开票状态更新为“待开票”, 清空发票关联id
    void batchUpdateInvoiceStatus(List<Long> ids,String invoiceStatus) throws SQLException;
    /**
     * 根据ids查询预收账单信息
     */
    List<AdvanceBillDTO> listBillByRelIds(List<Long> ids);
	 /**
     * 预售单导出
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportAdvanceBillMap (AdvanceBillDTO dto)throws SQLException;
    
	 /**
     * 预售单体导出
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportAdvanceBillItemMap (AdvanceBillDTO dto)throws SQLException;
    /**
     * 分页查询
     * @param dto
     * @return
     */
    AdvanceBillDTO listAdvanceBillByPage(AdvanceBillDTO dto) throws SQLException ;

    /**
     * 分页查询单据类型为销售的单据
     * @param dto
     * @return
     */
    AdvanceBillDataDTO listAddSaleOrderByPage(AdvanceBillDatasDTO dto);

    /**
     * 应收核销勾稽预收单分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillVerifyAdvanceDTO listVerifyAdvanceByPage(ReceiveBillVerifyAdvanceDTO dto) throws Exception;


    /**
     * 账单id集合查询预收账单
     * @param ids 账单id集合
     * @return
     */
    List<AdvanceBillModel> listByIds(List<Long> ids);


    /**
     * 根据id查看DTO
     * @param id
     * @return
     */
    AdvanceBillDTO getAdvanceById(Long id);


    /**
     * NC状态回填
     * 查询状态为：已同步/待审核/待入erp/待入账/已入账的应收账单
     * @return
     */
    List<AdvanceBillModel> getNcBackfillList();

    /**
     * 凭证查询接口
     * 预收单当前账单状态不为”已作废“，且NC预收状态为“已入账”、“已关账”的预收单需查询接口状态；当该预收单已查询到凭证信息则不再继续获取；
     * 预收单当前账单状态为”已作废“，且NC预收状态为“已入账”、“已关账”、“已作废”、“已红冲”的预收单需查询接口状态；
     * @return
     */
    List<AdvanceBillModel> getNcVoucherFillBackList();

    /**
     * 根据id更新预收单的单据状态
     */
    int batchUpdateByIds(List<Long> ids, String billStatus) ;


}
