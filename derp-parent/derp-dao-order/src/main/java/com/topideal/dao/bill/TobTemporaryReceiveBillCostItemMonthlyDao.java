package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillCostItemMonthlyModel;

import java.sql.SQLException;
import java.util.List;


public interface TobTemporaryReceiveBillCostItemMonthlyDao extends BaseDao<TobTemporaryReceiveBillCostItemMonthlyModel> {


    /**
     * 根据条件删除
     * @param itemMonthlyModel
     * @return
     * @throws SQLException
     */
    int deleteByModel(TobTemporaryReceiveBillCostItemMonthlyModel itemMonthlyModel) throws SQLException;


    /**
     * 批量插入
     * */
    Integer batchSave(List<TobTemporaryReceiveBillCostItemMonthlyModel> list) throws SQLException;


    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillCostItemMonthlyDTO listToBTempCostMonthlyByPage(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException;


    List<TobTemporaryReceiveBillCostItemMonthlyDTO> listByDto(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException;

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 “Tob 暂估核销表-费用月结快照” 已核销金额
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillCostItemMonthlyDTO> getMonthlyVerify(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException;


}
