package com.topideal.service.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.user.UserBuRelModel;

public interface UserBuRelService {

	/**
	 * 根据用户获取关联事业部
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<UserBuRelModel> getBuByUserId(Long id) throws SQLException;

}
