package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.ReceiveBillVerifyItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveBillVerifyItemMapper extends BaseMapper<ReceiveBillVerifyItemModel> {

    BigDecimal getTotalVerifyPrice(@Param("billId") Long billId) throws SQLException;

    /**
     * 统计应收账单集合核销金额
     */
    List<Map<String, Object>> listVerifyPrice(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 根据tob应收账单删除核销关系
     */
    void delVerify(ReceiveBillVerifyItemModel verifyItemModel) throws SQLException;

    /**
     * 项目额度预警 回款日期需大于对应额度记录配置中的“期初开始日期的应收账单ID
     * @param queryBillMap
     * @return
     */
    //List<Long> getProjectQuatoReceiveId(Map<String, Object> queryBillMap);

    /**
     * 获取最晚的核销日期
     **/
    Timestamp getLatestVerifyDate(Long billId);

    /**
     * 批量保存核销明细
     **/
    Integer batchSave(@Param("list") List<ReceiveBillVerifyItemModel> list) throws SQLException;

    /**
     * 根据预收单id统计核销金额
     */
    List<Map<String, Object>> getTotalVerifyPriceByAdvanceId(@Param("advanceIds") List<Long> advanceIds) throws SQLException;
    
	/**
	 * 获取预收核销记录数据
	 * @param dto
	 * @return
	 */
	List<ReceiveBillVerifyItemDTO>getReceiveBillVerifyItem(ReceiveBillVerifyItemDTO dto)throws SQLException;

    /**
     * 查询应收账单收款核销记录中核销月份小于等于月结月份的已核销金额
     * @param billIds 应收账单id集合
     * @param month 月结月份
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getVerifyAmountByBillIds(@Param("billIds") List<Long> billIds, @Param("month") String month) throws SQLException;

    /**
     * 根据应收单id查询核销记录
     * @param billIds
     * @return
     */
    List<ReceiveBillVerifyItemModel> listByBillIds(@Param("billIds") List<Long> billIds);
}
