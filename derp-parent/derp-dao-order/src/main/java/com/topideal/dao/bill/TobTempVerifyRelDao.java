package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.TobTempVerifyRelModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TobTempVerifyRelDao extends BaseDao<TobTempVerifyRelModel>{


    /**
     * 批量插入
     * */
    Integer batchSave(List<TobTempVerifyRelModel> list) throws SQLException;



    /**
     * 批量删除
     * */
    Integer batchDelByReceiveId(Long receiveBillId) throws SQLException;


    /**
     * 根据指定tob暂估id集合查询关联核销应收账单
     * */
    List<Map<String, Object>> getRelReceiveBill(List<Long> tobIds)throws SQLException;

    /**
     * 根据指定tob暂估明细id集合查询关联核销应收账单
     * */
    List<Map<String, Object>> getRelReceiveItemBill(List<Long> tobItemIds, String type)throws SQLException;
    /**
     * 根据tob暂估核销表体id查询核销金额
     * @param queryMap
     * @return
     * @throws SQLException
     */
    Map<String, BigDecimal> getByTobItemId(Map<String, Object> queryMap) throws SQLException;

    /**
     * 根据暂估明细id集合查询入账月份小于等于快照当月的核销记录
     * @param tobItemIds
     * @param month
     * @param type
     * @return
     * @throws SQLException
     */
    List<TobTempVerifyRelModel> getRelBeforeMonth(List<Long> tobItemIds, String month, String type) throws SQLException;

}
