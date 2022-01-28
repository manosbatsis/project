package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.ReminderEmailConfigDTO;
import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import com.topideal.entity.vo.main.ReminderEmailRelModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface ReminderEmailRelMapper extends BaseMapper<ReminderEmailRelModel> {

    /*根据表头id查询表体*/
    List<ReminderEmailRelDTO> getConfigById(long configId);

    /*根据表头id和类型查询*/
    List<ReminderEmailRelModel> getConfigIdAndType(@Param("configId") long configId,@Param("type") String type);
}
