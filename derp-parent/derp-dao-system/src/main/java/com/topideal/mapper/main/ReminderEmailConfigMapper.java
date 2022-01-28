package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.vo.main.ReminderEmailConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface ReminderEmailConfigMapper extends BaseMapper<ReminderEmailConfigModel> {


    /**
     * 分页查询列表
     * @param model
     * @return
     */
    PageDataModel<ReminderEmailConfigDTO> getListByPage(ReminderEmailConfigDTO model);

    ReminderEmailConfigDTO getById(long id);
}
