package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;

import java.util.List;


public interface ReceiveBillOperateDao extends BaseDao<ReceiveBillOperateModel> {


    /**
     * 根据操作节点查询最新一条改操作节点记录
     **/
    ReceiveBillOperateModel getLatestOperate(String operateNode, List<Long> billIds);







}
