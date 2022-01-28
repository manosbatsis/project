package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface ReceiveBillOperateMapper extends BaseMapper<ReceiveBillOperateModel> {

    /**
     * 根据操作节点查询最新一条改操作节点记录
     **/
    ReceiveBillOperateModel getLatestOperate(@Param("operateNode") String operateNode, @Param("billIds") List<Long> billIds);

}
