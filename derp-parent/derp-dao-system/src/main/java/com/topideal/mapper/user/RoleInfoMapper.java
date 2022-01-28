package com.topideal.mapper.user;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface RoleInfoMapper extends BaseMapper<RoleInfoModel> {

	PageDataModel<RoleInfoDTO> searchDTOByPage(RoleInfoDTO dto);



}

