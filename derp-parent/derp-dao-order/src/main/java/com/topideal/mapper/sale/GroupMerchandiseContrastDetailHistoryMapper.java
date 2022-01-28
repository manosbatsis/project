package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailHistoryModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface GroupMerchandiseContrastDetailHistoryMapper extends BaseMapper<GroupMerchandiseContrastDetailHistoryModel> {

    int batchDelByGroupId(@Param("groupMerchandiseId") Long groupMerchandiseId);

}
