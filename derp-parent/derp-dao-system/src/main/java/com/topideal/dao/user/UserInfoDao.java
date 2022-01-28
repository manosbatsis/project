package com.topideal.dao.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.UserInfoModel;


public interface UserInfoDao extends BaseDao<UserInfoModel>{


    UserInfoDTO searchByUsername(String usernam);

    UserInfoModel searchUserInfo(UserInfoModel model) throws SQLException;
    /**
     * 获取权限信息
     * @param id
     * @return
     */
    List<String> getBtnPersonInfo(long id);
    
    UserInfoDTO getDetails(Long id )throws SQLException;

	UserInfoDTO searchDTOByPage(UserInfoDTO dto);

	/**
	 * 根据id集合查询用户
	 */
    List<UserInfoModel> listByIds(UserInfoDTO dto) throws SQLException;
}

