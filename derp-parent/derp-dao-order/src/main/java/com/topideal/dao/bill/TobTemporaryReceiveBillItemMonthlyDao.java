package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemMonthlyModel;

import java.sql.SQLException;
import java.util.List;


public interface TobTemporaryReceiveBillItemMonthlyDao extends BaseDao<TobTemporaryReceiveBillItemMonthlyModel> {

    /**
     * 根据条件删除
     * @param itemMonthlyModel
     * @return
     * @throws SQLException
     */
    int deleteByModel(TobTemporaryReceiveBillItemMonthlyModel itemMonthlyModel) throws SQLException;


    /**
     * 批量插入
     * */
    Integer batchSave(List<TobTemporaryReceiveBillItemMonthlyModel> list) throws SQLException;


    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillItemMonthlyDTO listToBTempRevenueMonthlyByPage(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException;


    List<TobTemporaryReceiveBillItemMonthlyDTO> listByDto(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException;

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 “Tob 暂估核销表-收入月结快照” 已核销金额
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillItemMonthlyDTO> getMonthlyVerify(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException;


}
