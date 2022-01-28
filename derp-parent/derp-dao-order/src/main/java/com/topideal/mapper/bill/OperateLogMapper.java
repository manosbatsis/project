package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface OperateLogMapper extends BaseMapper<OperateLogModel> {


    /**
     * 获取最近一条操作记录
     * @param operateLogModel
     * @return
     */
    OperateLogModel getLatestOperateLog(OperateLogModel operateLogModel);



}
