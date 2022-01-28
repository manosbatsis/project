package com.topideal.service.user;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.entity.vo.user.UserInfoModel;

/**
 * Created by weixiaolei on 2018/4/11.
 */
public interface UserInfoService {
		
	boolean saveUserBindRole(Long id,Long roleId)throws SQLException;
    /**
     * 用户信息详情
     * @param id
     * @return
     */
     UserInfoModel searchUserInfoDetail(Long id)throws SQLException;

    UserInfoModel searchUserInfo(UserInfoModel model) throws SQLException;

    List<UserInfoModel> list(UserInfoModel model) throws SQLException;
    /**
     * 用户列表
     * @param userInfoDTO  用户信息
     * @param userId    操作人id
     * @param parentId  父类id
     * @return
     * @throws SQLException
     */
     UserInfoDTO listUserInfo(UserInfoDTO userInfoDTO, String userType, Long userId, Long parentId)throws SQLException;

    /**
     * 删除用户信息
     * @param ids
     * @return
     */
     boolean delUser(List ids)throws SQLException;

    /**
     * 新增用户信息
     * @param dto
     * @param userType
     * @param userId
     * @param parentId
     * @return
     */
     ViewResponseBean saveUser(UserInfoDTO dto,String userType,Long userId,Long parentId)throws Exception;

    /**
     * 修改用户信息
     * @param userInfoModel
     * @return
     */
    ViewResponseBean modify(UserInfoDTO model)throws Exception;
    
    /**
     * 仅修改用户信息
     * @param model
     * @return
     * @throws Exception
     */
    int modifyUserInfo(UserInfoModel model)throws Exception;
    

    /**
     *重置密码
     * @param id
     * @param userId  操作人
     * @return
     * @throws SQLException
     */
    boolean resetPwd(Long id,Long userId)throws SQLException;

    /**
     * 修改密码
     * @return
     * @throws SQLException
     */
    Map<String,String> modifyPwd(Long id, String oldPwd, String password)throws SQLException;


    UserInfoDTO searchByUsername(String username);

    /**
     * 获取权限信息
     * @param id
     * @return
     */
    List<String> getBtnPersonInfo(long id);

    /**
     * 通过商家id ,获取关联商家信息
     * @param merchantId
     * @return
     * @throws Exception
     */
    public String searchRelMerchantIds(Long merchantId)throws Exception;


    UserInfoDTO searchDetail(Long id) throws SQLException;

    /**
     * 启用/停用用户
     * @param id
     * @return
     */
    boolean enableUser(Long id, String type)throws SQLException;

    /**
     * 获取用户关联事业部
     * @param id
     * @return
     * @throws SQLException 
     */
	List<UserBuRelModel> getBuRel(Long id) throws SQLException;

}
