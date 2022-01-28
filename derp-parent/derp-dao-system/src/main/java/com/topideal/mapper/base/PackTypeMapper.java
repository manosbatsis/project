package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;

/**
 * 包装方式 mapper
 * @author lian_
 */
@MyBatisRepository
public interface PackTypeMapper extends BaseMapper<PackTypeModel> {


    List<PackTypeModel> listByLike(PackTypeModel packTypeModel);
}

