package com.topideal.dao.base;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.OperateSysLogDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;

import java.util.List;


public interface OperateSysLogDao extends BaseDao<OperateSysLogModel>{
		

    public List<OperateSysLogDTO> getOperateSysLog(OperateSysLogDTO dto);


    /**
     * 批量插入
     * @param logList
     */
    int batchSave(List<OperateSysLogModel> logList);
}
