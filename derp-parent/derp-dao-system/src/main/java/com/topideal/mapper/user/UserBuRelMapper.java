package com.topideal.mapper.user;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface UserBuRelMapper extends BaseMapper<UserBuRelModel> {

	int delByUserId(Long id);



}
