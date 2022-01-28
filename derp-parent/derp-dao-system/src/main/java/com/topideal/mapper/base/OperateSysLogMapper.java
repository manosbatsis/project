package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.base.OperateSysLogDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface OperateSysLogMapper extends BaseMapper<OperateSysLogModel> {

    public List<OperateSysLogDTO> getOperateSysLog(OperateSysLogDTO dto);

    int batchSave(List<OperateSysLogModel> logList);
}
