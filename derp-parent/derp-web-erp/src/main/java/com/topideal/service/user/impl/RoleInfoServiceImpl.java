package com.topideal.service.user.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.user.RoleInfoDao;
import com.topideal.dao.user.RolePermissionRelDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.dao.user.UserRoleRelDao;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserRoleRelModel;
import com.topideal.service.user.RoleInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/5/2.
 */
@Service
public class RoleInfoServiceImpl implements RoleInfoService {

    @Autowired
    private RoleInfoDao roleInfoDao;

    @Autowired
    private RolePermissionRelDao rolePermissionRelDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserRoleRelDao userRoleRelDao;


    @Override
    public RoleInfoDTO listRoleInfo(RoleInfoDTO dto) throws SQLException {
        return roleInfoDao.searchDTOByPage(dto);
    }

    @Override

    public Long saveRole(RoleInfoModel model) throws Exception {
        //保存用户信息
        Long id= roleInfoDao.save(model);

        return id;
    }

    @Override

    public boolean delRole(List ids) throws Exception {
        int num=roleInfoDao.delete(ids);
        if(num>=1){
            return true;
        }
        return false;
    }

    @Override

    public boolean addPermission(List<Long> ids, Long roleId) throws Exception {
        return rolePermissionRelDao.addRolePermissionRel(ids,roleId);
    }

    public Map seachUserInfoByRoleId(Long roleId,Long userId,String userType)throws SQLException{
        //结果集
        Map data=new HashMap();
        //所有用户信息
        List<Map> allUser=new ArrayList<Map>();
        //查询所有用户信息
        UserInfoModel model=new UserInfoModel();
        model.setDisable(DERP_SYS.USERINFO_DISABLE_0);
        if (!userType.equals(DERP_SYS.USERINFO_USERTYPE_1)) {
            model.setCreater(userId);
        }
        List<UserInfoModel> userInfoModelList =userInfoDao.list(model);
        for (UserInfoModel userInfoModel : userInfoModelList) {
            Map user = new HashMap();
            user.put("value",userInfoModel.getId());
            user.put("text",userInfoModel.getName());
            allUser.add(user);
        }

        UserRoleRelModel m2=new UserRoleRelModel();
        m2.setRoleId(roleId);
        List<UserRoleRelModel> userRoleRelModelList=userRoleRelDao.list(m2);
        List<String> bindUser = new ArrayList<String>();
        if(userRoleRelModelList!=null&&userRoleRelModelList.size()>0){
        	for(UserRoleRelModel userRole:userRoleRelModelList){
        		bindUser.add(String.valueOf(userRole.getUserId()));
        	}
        }

        if (bindUser != null && bindUser.size() > 0) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setIds(bindUser);
            userInfoDTO.setDisable(DERP_SYS.USERINFO_DISABLE_1);
            List<UserInfoModel> userList =userInfoDao.listByIds(userInfoDTO);
            for (UserInfoModel userInfoModel : userList) {
                Map user = new HashMap();
                user.put("value",userInfoModel.getId());
                user.put("text",userInfoModel.getName());
                allUser.add(user);
            }
        }

        data.put("allUser",allUser);
        data.put("bindUser",bindUser);
        return data;
    }
    public Map getUserInfo(Long userId,String userType, Boolean hasCodeFlag)throws SQLException{
        //结果集
        Map data=new HashMap();
        //所有用户信息
        List<Map> allUser=new ArrayList<Map>();
        //查询所有用户信息
        UserInfoModel model=new UserInfoModel();
        model.setDisable(DERP_SYS.USERINFO_DISABLE_0);
        if (!userType.equals(DERP_SYS.USERINFO_USERTYPE_1)) {
            model.setCreater(userId);
        }
        List<UserInfoModel> userInfoModelList =userInfoDao.list(model);
        for (UserInfoModel userInfoModel : userInfoModelList) {
            if(hasCodeFlag && StringUtils.isBlank(userInfoModel.getCode())) {
                continue;
            }
            Map user = new HashMap();
            user.put("value",userInfoModel.getId());
            user.put("text",userInfoModel.getName());
            allUser.add(user);
        }
        data.put("allUser",allUser);
        return data;
    }

    @Override
    public boolean addBindUser(List<Long> ids, Long roleId) throws Exception {
        return userRoleRelDao.addBindUser(ids,roleId);
    }

    @Override
    public boolean isExistUsersByRoleId(Long roleId) throws SQLException {
        UserRoleRelModel userRoleRelModel = new UserRoleRelModel();
        userRoleRelModel.setRoleId(roleId);
        List<UserRoleRelModel> userRoleRelModelList = userRoleRelDao.list(userRoleRelModel);
        if (userRoleRelModelList != null && userRoleRelModelList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getUserInfoByCode(boolean hasCodeFlag) throws Exception{
        //所有用户信息
        List<Map<String, Object>> allUser=new ArrayList<Map<String, Object>>();
        //查询所有用户信息
        UserInfoModel model=new UserInfoModel();
        model.setDisable(DERP_SYS.USERINFO_DISABLE_0);
        model.setAccountType(DERP_SYS.USERINFO_ACCOUNTTYPE_1);
        List<UserInfoModel> userInfoModelList =userInfoDao.list(model);
        for (UserInfoModel userInfoModel : userInfoModelList) {
            if(hasCodeFlag && StringUtils.isBlank(userInfoModel.getCode())) {
                continue;
            }
            Map user = new HashMap();
            user.put("value",userInfoModel.getCode());
            user.put("text",userInfoModel.getName());
            allUser.add(user);
        }
        return allUser;
    }
}
