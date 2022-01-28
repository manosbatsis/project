package com.topideal.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface UserInfoMapper extends BaseMapper<UserInfoModel> {

    /**
     * 获取权限信息
     * @param id
     * @return
     */
    List<String> getBtnPersonInfo(long id);

    /**
     * 通过用户名，获取用户信息
     * @param username
     * @return
     */
    UserInfoDTO searchUserByUsername(String username);

    UserInfoDTO getDetails(Long id)throws SQLException;

    PageDataModel<UserInfoDTO> searchDTOByPage(UserInfoDTO dto);

    List<UserInfoModel> listByIds(UserInfoDTO dto) throws SQLException;

}

