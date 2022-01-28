package com.topideal.service.main;

import com.topideal.entity.dto.main.ReminderEmailRelDTO;

import java.util.List;
import java.util.Map;

public interface ReminderEmailRelService {

    /**
     * 根据表头id查询表体
     * @param configId
     * @return
     */
    public List<ReminderEmailRelDTO> getConfigById(Long configId);

}
