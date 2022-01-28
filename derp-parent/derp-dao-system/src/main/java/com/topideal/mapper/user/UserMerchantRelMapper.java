package com.topideal.mapper.user;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface UserMerchantRelMapper extends BaseMapper<UserMerchantRelModel> {

    /**
     * 查询用户绑定的公司下拉列表
     *
     * @return
     * @throws SQLException
     */
    public List<SelectBean> getUserSelectBean(UserMerchantRelModel model) throws SQLException;

    /**
     * 查询用户绑定的公司信息
     *
     * @return
     * @throws SQLException
     */
    public List<MerchantInfoModel> getUserMerchantList(UserMerchantRelModel model) throws SQLException;

    /**
     * 根据用户id删除用户公司关系
     */
    public int delAllByUserId(@Param("userId") Long userId) throws SQLException;
}
