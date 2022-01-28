package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;

import java.sql.SQLException;

public interface AccountingReminderConfigService {

    /**
     * 分页查询
     * @param dto
     * @return
     */
    AccountingReminderConfigDTO getListByPage(AccountingReminderConfigDTO dto);

    /**
     * 新增或编辑
     * @param dto
     * @param user
     * @return
     */
    Long saveOrUpdateAccountingReminderConfig(AccountingReminderConfigDTO dto, User user) throws SQLException;

    /**
     * 审核
     * @param id
     * @param user
     */
    void auditAccountingReminderConfig(Long id, User user) throws SQLException;

    /**
     * 删除
     * @param id
     * @param user
     */
    void delAccountingReminderConfig(String ids) throws SQLException;

    /**
     * 根据ID导出
     * @param id
     * @return
     */
    AccountingReminderConfigDTO getAccountingReminderConfig(Long id) throws SQLException;
}
