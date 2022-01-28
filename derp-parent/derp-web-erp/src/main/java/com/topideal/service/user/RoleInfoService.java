package com.topideal.service.user;



import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/5/2.
 */
public interface RoleInfoService {



    /**
     * 用户列表
     * @param model  用户信息
     * @return
     * @throws SQLException
     */
	RoleInfoDTO listRoleInfo(RoleInfoDTO dto)throws SQLException;

    /**
     * 新增用户信息 
     * @param model
     * @return
     */
    Long saveRole(RoleInfoModel model)throws Exception;

    /**
     * 删除角色
     * @param ids
     * @return
     * @throws Exception
     */
    boolean delRole(List ids)throws Exception;

    /**
     * 绑定权限
     * @param ids
     * @return
     * @throws Exception
     */
    boolean addPermission(List<Long> ids, Long roleId)throws Exception;

    /**
     * 绑定用户
     * @param roleId   角色
     * @param userId   超管用户
     * @return
     * @throws SQLException
     */
     Map seachUserInfoByRoleId(Long roleId, Long userId, String userType)throws SQLException;
     /**
      * 获取用户
      * @param userId
      * @param userType
      * @param hasCodeFlag 员工编码是否要有
      * @return
      * @throws SQLException
      */
     Map getUserInfo(Long userId, String userType, Boolean hasCodeFlag)throws SQLException;
     
    /**
     * 绑定用户信息
     * @param ids
     * @param roleId
     * @return
     * @throws Exception
     */
     boolean addBindUser(List<Long> ids, Long roleId) throws Exception;

    /**
     * 判断该角色下是否已绑定用户
     * @param roleId
     * @return
     * @throws Exception
     */
     boolean isExistUsersByRoleId(Long roleId) throws SQLException;

    /**
     * 获取用户员工编号
     * @param hasCodeFlag 员工编码是否要有
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getUserInfoByCode(boolean hasCodeFlag) throws Exception;
}
