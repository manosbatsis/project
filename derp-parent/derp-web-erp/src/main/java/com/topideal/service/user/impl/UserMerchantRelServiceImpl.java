package com.topideal.service.user.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.user.UserMerchantRelService;

@Service
public class UserMerchantRelServiceImpl implements UserMerchantRelService{

	@Autowired
	private UserMerchantRelDao userMerchantRelDao ;

	@Override
	public List<UserMerchantRelModel> getUserMerchantRelList(UserMerchantRelModel model) throws SQLException {
		return userMerchantRelDao.list(model);
	}

	
}
