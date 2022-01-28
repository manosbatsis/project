package com.topideal.dao.user.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.user.UserRoleRelDao;
import com.topideal.entity.vo.user.UserRoleRelModel;
import com.topideal.mapper.user.UserRoleRelMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 */
@Repository
public class UserRoleRelDaoImpl implements UserRoleRelDao {

    @Autowired
    private UserRoleRelMapper mapper;

    @Override
    public Long save(UserRoleRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }

    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }

    @Override
    public int modify(UserRoleRelModel  model) throws SQLException {
        return mapper.update(model);
    }

    @Override
    public UserRoleRelModel  searchByPage(UserRoleRelModel  model) throws SQLException{
        PageDataModel<UserRoleRelModel> pageModel=mapper.listByPage(model);
        return (UserRoleRelModel ) pageModel.getModel();
    }

    @Override
    public UserRoleRelModel  searchById(Long id) throws SQLException{
        UserRoleRelModel  model=new UserRoleRelModel ();
        model.setId(id);
        return mapper.get(model);
    }

    @Override
    public List<UserRoleRelModel> list(UserRoleRelModel model) throws SQLException {
        return mapper.list(model);
    }

    @Override
    public boolean addBindUser(List<Long> ids, Long roleId) throws SQLException {
        //删除之前的记录
        int num=mapper.delByRoleId(roleId);
        //不绑定用户
        if(ids==null){
            return true;
        }
        //新增
        for (Long  id : ids) {
            UserRoleRelModel model=new UserRoleRelModel();
            model.setRoleId(roleId);
            model.setUserId(id);
            mapper.insert(model);
        }
        return true;
    }

	@Override
	public UserRoleRelModel searchByModel(UserRoleRelModel model)
			throws SQLException {
		return mapper.get(model);
	}
}

