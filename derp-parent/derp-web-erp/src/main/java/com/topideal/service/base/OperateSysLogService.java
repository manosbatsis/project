package com.topideal.service.base;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.base.OperateSysLogDTO;

import java.sql.SQLException;
import java.util.List;

public interface OperateSysLogService {

    /**
     * 查询日志信息
     * @param dto
     * @return
     */
    public List<OperateSysLogDTO> getOperateSysLog(OperateSysLogDTO dto);


    /**
     * 保存日志
     * @param user 用户信息
     * @param module 模块
     * @param id 关联编码
     * @param operateAction 操作项
     * @param result 结果
     * @param remark 备注
     * @throws SQLException
     */
    void saveLog(User user, String module, Long id, String operateAction,
                 String result, String remark) throws SQLException;
}
