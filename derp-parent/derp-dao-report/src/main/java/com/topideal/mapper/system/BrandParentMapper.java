package com.topideal.mapper.system;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.BrandParentModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface BrandParentMapper extends BaseMapper<BrandParentModel> {

    BrandParentModel getBrandParentByGoodsId(@Param("goodsId") Long goodsId);

}
