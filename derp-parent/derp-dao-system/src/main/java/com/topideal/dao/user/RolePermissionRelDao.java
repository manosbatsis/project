package com.topideal.dao.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.user.RolePermissionRelModel;


public interface RolePermissionRelDao extends BaseDao<RolePermissionRelModel>{

    /**
     * 自定义新增
     * @return
     * @throws SQLException
     */
    boolean addRolePermissionRel(List<Long> ids, Long roleId)throws SQLException;













}

