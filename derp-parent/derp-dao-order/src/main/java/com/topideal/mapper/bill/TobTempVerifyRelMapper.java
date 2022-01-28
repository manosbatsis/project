package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.TobTempVerifyRelModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.mail.MailParseException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TobTempVerifyRelMapper extends BaseMapper<TobTempVerifyRelModel> {

    /**
     * 批量插入
     * */
    Integer batchSave(List<TobTempVerifyRelModel> list) throws SQLException;

    /**
     * 批量删除
     * */
    Integer batchDelByReceiveId(@Param("receiveBillId") Long receiveBillId) throws SQLException;

    /**
     * 根据指定tob暂估id集合查询关联核销应收账单
     * */
    List<Map<String, Object>> getRelReceiveBill(@Param("tobIds")List<Long> tobIds)throws SQLException;

    /**
     * 根据指定tob暂估明细id集合查询关联核销应收账单
     * */
    List<Map<String, Object>> getRelReceiveItemBill(@Param("tobItemIds")List<Long> tobItemIds, @Param("type") String type)throws SQLException;

    /**
     * 按不同的时间段区间， ”事业部+客户+销售币种“ ，结算状态非为 “已核销”进行汇总
     * @param queryMap
     * @return
     */
    Map<String, BigDecimal> getByTobItemAmount(Map<String, Object> queryMap);

    /**
     * 根据暂估明细id集合查询入账月份小于等于快照当月的核销记录
     * @param tobItemIds
     * @param month
     * @param type
     * @return
     * @throws SQLException
     */
    List<TobTempVerifyRelModel> getRelBeforeMonth(@Param("tobItemIds") List<Long> tobItemIds, @Param("month") String month, @Param("type") String type) throws SQLException;


}
