package com.topideal.service.user.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.user.UserBuRelDao;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.service.user.UserBuRelService;

@Service
public class UserBuRelServiceImpl implements UserBuRelService{

	@Autowired
	private UserBuRelDao userBuRelDao ;

	@Override
	public List<UserBuRelModel> getBuByUserId(Long id) throws SQLException {
		
		UserBuRelModel queryModel = new UserBuRelModel() ;
		queryModel.setUserId(id);
		
		List<UserBuRelModel> list = userBuRelDao.list(queryModel);
		
		return list;
	}
}
