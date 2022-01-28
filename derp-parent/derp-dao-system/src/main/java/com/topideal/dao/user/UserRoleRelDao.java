package com.topideal.dao.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.user.UserRoleRelModel;


public interface UserRoleRelDao extends BaseDao<UserRoleRelModel>{


    /**
     * 自定义新增
     * @return
     * @throws SQLException
     */
    boolean addBindUser(List<Long> ids, Long roleId)throws SQLException;










}

