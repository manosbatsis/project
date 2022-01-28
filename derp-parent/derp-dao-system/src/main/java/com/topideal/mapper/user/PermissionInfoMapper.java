package com.topideal.mapper.user;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PermissionInfoMapper extends BaseMapper<PermissionInfoModel> {

    /**
     * 查询一级、二级菜单
     * @return
     * @throws SQLException
     */
    List<TreeBean> treeList() throws SQLException;

    /**
     * 查询一级、二级菜单、按钮
     * @param ids 权限id
     * @return
     * @throws SQLException
     */
    List<TreeBean> treeListAll(@Param("ids") List<Long> ids) throws SQLException;


    /**
     * 菜单树结构
     * @param userId
     * @return
     * @throws SQLException
     */
    List<TreeBean> treeMenuList(@Param("userId") Long userId) throws SQLException;



    /**
     * 获取权限ID信息
     * @param userId
     * @return
     * @throws SQLException
     */
    List<Long> getIdByUserId(@Param("userId") Long userId)throws SQLException;

    PageDataModel<PermissionInfoDTO> searchDTOByPage(PermissionInfoDTO dto);

    int modifyWithNull(PermissionInfoModel model)throws SQLException;


}

