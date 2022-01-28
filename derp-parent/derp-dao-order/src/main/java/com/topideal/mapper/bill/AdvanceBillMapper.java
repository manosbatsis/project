package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyAdvanceDTO;
import com.topideal.entity.vo.bill.AdvanceBillModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AdvanceBillMapper extends BaseMapper<AdvanceBillModel> {
	//批量更新应收账单状态
    void batchUpdateInvoiceStatus(@Param("ids") List<Long> ids,@Param("invoiceStatus") String invoiceStatus) throws SQLException;
    /**
     * 根据ids查询预收账单信息
     */
    List<AdvanceBillDTO> listBillByRelIds(@Param("ids") List<Long> ids);
	 /**
     * 预售单导出
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportAdvanceBillMap (AdvanceBillDTO dto)throws SQLException;
    
	 /**
     * 预售单导出
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportAdvanceBillItemMap (AdvanceBillDTO dto)throws SQLException;
    
    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    List<AdvanceBillDTO> listAdvanceBill(AdvanceBillDTO dto);
    /**
     * 分页总量
     * @param dto
     * @return
     */
	int listAdvanceBillCount(AdvanceBillDTO dto) throws SQLException;

    /**
     * 分页查询单据类型为销售的单据
     * 单据状态过滤掉”待提交、待审核“状态的单据
     * @param dto
     * @return
     */
    PageDataModel<AdvanceBillDataDTO> listAddSaleOrderByPage(AdvanceBillDatasDTO dto);


    /**
     * 应收核销勾稽预收单分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<ReceiveBillVerifyAdvanceDTO> listVerifyAdvanceByPage(ReceiveBillVerifyAdvanceDTO dto) throws Exception;

    /**
     * 账单id集合查询预收账单
     * @param ids 账单id集合
     * @return
     */
    List<AdvanceBillModel> listByIds(@Param("ids") List<Long> ids);


    /**
     * 根据id查看DTO
     * @param id
     * @return
     */
    AdvanceBillDTO getAdvanceById(Long id);

    /**
     * NC状态回填
     * @return
     */
    List<AdvanceBillModel> getNcBackfillList();


    /**
     * 凭证查询
     * @return
     */
    List<AdvanceBillModel> getNcVoucherFillBackList();

    /**
     * 根据id更新预收单的单据状态
     */
    int batchUpdateByIds(@Param("ids") List<Long> ids, @Param("billStatus") String billStatus) ;

}
