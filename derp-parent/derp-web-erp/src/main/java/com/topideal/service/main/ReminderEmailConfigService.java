package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.webapi.form.ReminderEmailConfigForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReminderEmailConfigService {

    /**
     * 查询邮件提醒列表
     * @param model
     * @return
     * @throws SQLException
     */
    ReminderEmailConfigDTO getListByPage(ReminderEmailConfigDTO model) throws SQLException;

    /**
     * 删除
     * @throws SQLException
     */
    Map deleteReminderEmailConfig(long id) throws SQLException;


    /**
     * 新增
     * @param form
     * @return
     */
    Map<String,Object> saveReminderEmailConfig(User user,ReminderEmailConfigForm form) throws SQLException;

    /**
     * 修改
     * @param form
     * @return
     */
    Map<String,Object> updateReminderEmailConfig(User user,ReminderEmailConfigForm form) throws SQLException;

    ReminderEmailConfigDTO getById(long id);

    List<RoleInfoModel> getRoleList() throws SQLException;

    List<UserInfoModel> getUserList() throws SQLException;
}
