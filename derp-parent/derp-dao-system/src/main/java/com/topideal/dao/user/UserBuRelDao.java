package com.topideal.dao.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.user.UserBuRelModel;


public interface UserBuRelDao extends BaseDao<UserBuRelModel>{

	int delByUserId(Long id) throws SQLException;
		
	List<UserBuRelModel> getListByUserId(Long userId) throws SQLException ;









}
