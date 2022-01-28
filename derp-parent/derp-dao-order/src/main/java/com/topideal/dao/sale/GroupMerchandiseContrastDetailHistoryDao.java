package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailHistoryModel;


public interface GroupMerchandiseContrastDetailHistoryDao extends BaseDao<GroupMerchandiseContrastDetailHistoryModel> {

    //批量删除商品对照信息对应的更改记录
    int batchDelByGroupId(Long groupId);








}
