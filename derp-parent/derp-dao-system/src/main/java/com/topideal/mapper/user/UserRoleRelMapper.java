package com.topideal.mapper.user;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.user.UserRoleRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface UserRoleRelMapper extends BaseMapper<UserRoleRelModel> {

    /**
     * 通过角色ID删除关联关系
     * @param roleId
     * @return
     * @throws SQLException
     */
    int delByRoleId(Long roleId)throws SQLException;

}

