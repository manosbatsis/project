package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.AccountingReminderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface AccountingReminderItemMapper extends BaseMapper<AccountingReminderItemModel> {

    /**
     * 根据“事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数；
      * @return
     */
   public Integer getByBuIdCustomerId(@Param("buId") Long buId,@Param("customerId") Long customerId,@Param("merchantId")Long merchantId);

}
