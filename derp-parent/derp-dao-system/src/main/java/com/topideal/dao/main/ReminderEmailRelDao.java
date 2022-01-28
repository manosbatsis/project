package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import com.topideal.entity.vo.main.ReminderEmailRelModel;

import java.util.List;


public interface ReminderEmailRelDao extends BaseDao<ReminderEmailRelModel> {


    /**
     * 根据邮件提醒id查询
     * @param configId
     * @return
     */
    List<ReminderEmailRelDTO> getConfigById(long configId);


    /**
     * 根据配置id和操作节点类型查询
     * @param configId
     * @param type
     * @return
     */
    List<ReminderEmailRelModel> getConfigIdAndType(long configId,String type);





}
