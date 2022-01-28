package com.topideal.dao.user;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;


public interface RoleInfoDao extends BaseDao<RoleInfoModel>{

	RoleInfoDTO searchDTOByPage(RoleInfoDTO dto) throws SQLException;













}

