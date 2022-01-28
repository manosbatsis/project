package com.topideal.service.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.user.UserMerchantRelModel;

public interface UserMerchantRelService {

	/**
	 * 根据用户获取关联事业部
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<UserMerchantRelModel> getUserMerchantRelList(UserMerchantRelModel model ) throws SQLException;

}
