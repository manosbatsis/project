package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TobTemporaryReceiveBillItemDao extends BaseDao<TobTemporaryReceiveBillItemModel> {

    /**
     * 批量插入tob暂估核销明细
     * */
    Integer batchSave(List<TobTemporaryReceiveBillItemModel> list) throws SQLException;

    /**
     * 根据tob暂估表头id删除明细
     * */
    int delItemsByBillIds(List<Long> billIds) throws SQLException;

    /**
     * 统计tob暂估核销明细的应收金额
     */
    BigDecimal listItemPriceByIds(TobTemporaryReceiveBillItemMonthlyDTO itemMonthlyDTO) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillItemDTO listToBTempItemByPage(TobTemporaryReceiveBillItemDTO dto) throws SQLException;

    /**
     * 统计导出To B暂估明细数量
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer getTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception;

    /**
     * 获取状态为非“已核销“且应收明细“部分核销、未核销”的tob暂估明细
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillItemDTO> listToBeVerifyItems(TobTemporaryReceiveBillDTO dto) throws Exception;

    /**
     * 获取指定tob暂估的暂估明细
     * @param billIds
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillItemModel> listByBillIds(List<Long> billIds) throws Exception;


    /**
     * 根据公司、事业部、刷新月份查询询应收结算状态为“已上架未结算”/“部分结算”且上架时间小于等于快照月份的应收暂估明细
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws Exception
     */
    List<TobTemporaryReceiveBillItemModel> listNonVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception;

    /**
     * 根据公司、事业部、刷新月份查询应收结算状态为“已结算”且上架时间小于等于快照月份、核销记录中核销月份有大于等于快照月份的应收暂估明细
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws Exception
     */
    List<TobTemporaryReceiveBillItemModel> listAllVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception;


}
