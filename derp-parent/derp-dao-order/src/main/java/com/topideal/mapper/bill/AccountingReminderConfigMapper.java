package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;
import com.topideal.entity.vo.bill.AccountingReminderConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AccountingReminderConfigMapper extends BaseMapper<AccountingReminderConfigModel> {


    PageDataModel<AccountingReminderConfigDTO> getListByPage(AccountingReminderConfigDTO dto);

    /**
     * 获取审核时间最新的账期配置
     **/
    AccountingReminderConfigModel getLatestAuditDetail(AccountingReminderConfigModel model);
}
