package com.topideal.service.user.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.user.PermissionInfoDao;
import com.topideal.dao.user.RolePermissionRelDao;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.entity.vo.user.RolePermissionRelModel;
import com.topideal.service.user.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/5/3.
 */
@Service
public class PermissionServiceImpl implements PermissionService{

    @Autowired
    private PermissionInfoDao permissionInfoDao;

    @Autowired
    private RolePermissionRelDao rolePermissionRelDao;

    @Override
    public PermissionInfoDTO listPermissionInfo(PermissionInfoDTO dto) throws SQLException {
        return permissionInfoDao.searchDTOByPage(dto);
    }

    @Override
    public boolean savePermission(PermissionInfoModel model) throws Exception {
        if (StringUtils.isNotBlank(model.getPermission())) {
            PermissionInfoModel exist = new PermissionInfoModel();
            exist.setPermission(model.getPermission());
            exist = permissionInfoDao.searchByModel(exist);
            if (exist != null) {
                throw new RuntimeException("授权码不能重复");
            }
        }
        //保存
        Long id= permissionInfoDao.save(model);
        if(id!=null){
            return true;
        }
        return false;
    }

    @Override
    public List<TreeBean> listTree() throws SQLException{
        return permissionInfoDao.listTree();
    }

    @Override
    public List<TreeBean> listTreeAll(Long userId) throws SQLException {
        return permissionInfoDao.listTreeAll(userId);
    }

    @Override
    public boolean delPermission(List ids) throws SQLException {
        int num=permissionInfoDao.delete(ids);
        if(num>=1){
            return true;
        }
        return false;
    }

    @Override
    public List<RolePermissionRelModel> searchRelByRoleId(Long roleId)throws Exception{
        RolePermissionRelModel model=new RolePermissionRelModel();
        model.setRoleId(roleId);
        return rolePermissionRelDao.list(model);
    }


    @Override
    public List<TreeBean> treeMenuList(Long userId) throws Exception {
        return permissionInfoDao.treeMenuList(userId);
    }

    @Override
    public void modifyPermission(PermissionInfoModel model, User user) throws Exception {
        if (StringUtils.isNotBlank(model.getPermission())) {
            PermissionInfoModel exist = new PermissionInfoModel();
            exist.setPermission(model.getPermission());
            List<PermissionInfoModel> existList = permissionInfoDao.list(exist);
            if (existList != null) {
                for(PermissionInfoModel queryExist : existList){
                    if(queryExist.getId().longValue() != model.getId().longValue()){
                        throw new RuntimeException("授权码不能重复");
                    }
                }
            }
        }
        PermissionInfoModel tempModel = permissionInfoDao.searchById(model.getId());
        if(tempModel != null){
            model.setIcon(tempModel.getIcon());
            model.setIsEnabled(tempModel.getIsEnabled());
            model.setSpathid(tempModel.getSpathid());
        }

        model.setModifier(user.getId());
        model.setModifyDate(TimeUtils.getNow());
        //修改
       permissionInfoDao.modifyWithNull(model);

    }

    @Override
    public PermissionInfoModel detail(Long id) throws Exception {
        PermissionInfoModel model = permissionInfoDao.searchById(id);
        if(model == null){
            throw new RuntimeException("该权限不存在");
        }
        return model;
    }
}
