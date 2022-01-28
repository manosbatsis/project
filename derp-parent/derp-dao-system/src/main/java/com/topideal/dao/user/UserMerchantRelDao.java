package com.topideal.dao.user;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;

import java.sql.SQLException;
import java.util.List;


public interface UserMerchantRelDao extends BaseDao<UserMerchantRelModel> {

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
    public int delAllByUserId(Long userId ) throws SQLException;







}
