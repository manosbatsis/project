package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;
import com.topideal.entity.vo.bill.AccountingReminderConfigModel;


public interface AccountingReminderConfigDao extends BaseDao<AccountingReminderConfigModel> {


    AccountingReminderConfigDTO getListByPage(AccountingReminderConfigDTO dto);

    /**
     * 获取审核时间最新的账期配置
     **/
    AccountingReminderConfigModel getLatestAuditDetail(AccountingReminderConfigModel model);
}
