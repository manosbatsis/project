package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.AccountingReminderItemModel;

import java.util.List;
import java.util.Map;


public interface AccountingReminderItemDao extends BaseDao<AccountingReminderItemModel> {


    /**
     * 根据“事业部+客户”查询账期提醒配置表，汇总该“事业部+客户”的所有计划节点时效天数
     * @return
     */
   Integer getByBuIdCustomerId(Long buId,Long customerId,Long merchantId);








}
