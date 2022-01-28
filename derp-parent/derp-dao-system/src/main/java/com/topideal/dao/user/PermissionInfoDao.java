package com.topideal.dao.user;

import com.topideal.common.system.bean.TreeBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;

import java.sql.SQLException;
import java.util.List;


public interface PermissionInfoDao extends BaseDao<PermissionInfoModel>{
    /**
     * 查询一级 、二级菜单
     * @return
     * @throws SQLException
     */
    List<TreeBean> listTree() throws SQLException;

    /**
     * 一级、二级菜单、钮按
     * @return
     * @throws SQLException
     */
     List<TreeBean> listTreeAll(Long userId)throws SQLException;

    /**
     * 菜单树结构
     * @param userId
     * @return
     * @throws SQLException
     */
    List<TreeBean> treeMenuList(Long userId) throws SQLException;

	PermissionInfoDTO searchDTOByPage(PermissionInfoDTO dto);

    int modifyWithNull(PermissionInfoModel model)throws SQLException;



}

