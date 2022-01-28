package com.topideal.service.user;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.entity.vo.user.RolePermissionRelModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 权限管理
 * Created by weixiaolei on 2018/5/3.
 */
public interface PermissionService {

    /**
     * 用户列表
     * @param model  用户信息
     * @return
     * @throws SQLException
     */
	PermissionInfoDTO listPermissionInfo(PermissionInfoDTO dto)throws SQLException;

    /**
     * 新增用户信息
     * @param model
     * @return
     */
    boolean savePermission(PermissionInfoModel model)throws Exception;

    /**
     * 权限树结构  不包含按钮
     * @return
     * @throws SQLException
     */
    List<TreeBean> listTree()throws SQLException;

    /**
     * 权限树结构  包含按钮
     * @return
     * @throws SQLException
     */
    List<TreeBean> listTreeAll(Long userId)throws SQLException;


    /**
     * 删除权限信息
     * @return
     * @throws SQLException
     */
    boolean delPermission(List ids)throws SQLException;


    /**
     * 通过角色id查询权限信息
     * @return
     */
    List<RolePermissionRelModel> searchRelByRoleId(Long roleId)throws Exception;


    /**
     * 菜单权限列表
     * @return
     * @throws Exception
     */
    List<TreeBean> treeMenuList(Long userId)throws Exception;

    /**
     * 修改用户信息
     * @param model
     * @return
     */
    void modifyPermission(PermissionInfoModel model, User user)throws Exception;

    PermissionInfoModel detail(Long id) throws Exception ;

}
