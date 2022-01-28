package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.vo.main.ReminderEmailConfigModel;

import java.sql.SQLException;


public interface ReminderEmailConfigDao extends BaseDao<ReminderEmailConfigModel> {


    /**
     * 分页查询列表
     * @param model
     * @return
     */
    public ReminderEmailConfigDTO listByPage(ReminderEmailConfigDTO model);


    /**
     * 删除
     * @param id
     * @return
     */
    public int deleteById(long id) throws SQLException;


    ReminderEmailConfigDTO getById(Long id);


}
