package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;



@MyBatisRepository
public interface GroupMerchandiseContrastDetailMapper extends BaseMapper<GroupMerchandiseContrastDetailModel> {


    int batchDelByGroupId(@Param("groupMerchandiseId") Long groupMerchandiseId);
}
