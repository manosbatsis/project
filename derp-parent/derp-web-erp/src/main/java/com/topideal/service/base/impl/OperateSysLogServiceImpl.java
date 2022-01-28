package com.topideal.service.base.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.entity.dto.base.OperateSysLogDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.service.base.OperateSysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OperateSysLogServiceImpl implements OperateSysLogService {
    @Autowired
    private OperateSysLogDao operateSysLogDao;

    @Override
    public List<OperateSysLogDTO> getOperateSysLog(OperateSysLogDTO dto) {
        return operateSysLogDao.getOperateSysLog(dto);
    }

    @Override
    public void saveLog(User user, String module, Long id, String operateAction, String result, String remark) throws SQLException {

        OperateSysLogModel saveModel = new OperateSysLogModel() ;

        saveModel.setModule(module);
        saveModel.setCreateDate(TimeUtils.getNow());
        saveModel.setOperateDate(TimeUtils.getNow());
        saveModel.setOperateId(user.getId());
        saveModel.setOperater(user.getName());
        saveModel.setOperateRemark(remark);
        saveModel.setOperateResult(result);
        saveModel.setOperateAction(operateAction);
        saveModel.setRelId(id);
        operateSysLogDao.save(saveModel) ;
    }
}
