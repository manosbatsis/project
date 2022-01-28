package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.OperateLogModel;


public interface OperateLogDao extends BaseDao<OperateLogModel> {


    /**
     * 获取最近一条操作记录
     * @param operateLogModel
     * @return
     */
    OperateLogModel getLatestOperateLog(OperateLogModel operateLogModel);








}
