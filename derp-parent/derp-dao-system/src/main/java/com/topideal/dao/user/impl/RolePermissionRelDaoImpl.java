package com.topideal.dao.user.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.user.RolePermissionRelDao;
import com.topideal.entity.vo.user.RolePermissionRelModel;
import com.topideal.mapper.user.RolePermissionRelMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 */
@Repository
public class RolePermissionRelDaoImpl implements RolePermissionRelDao {

    @Autowired
    private RolePermissionRelMapper mapper;

    @Override
    public Long save(RolePermissionRelModel model) throws SQLException {
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
    public int modify(RolePermissionRelModel  model) throws SQLException {
        return mapper.update(model);
    }

    @Override
    public RolePermissionRelModel  searchByPage(RolePermissionRelModel  model) throws SQLException{
        PageDataModel<RolePermissionRelModel> pageModel=mapper.listByPage(model);
        return (RolePermissionRelModel ) pageModel.getModel();
    }

    @Override
    public RolePermissionRelModel  searchById(Long id)throws SQLException {
        RolePermissionRelModel  model=new RolePermissionRelModel ();
        model.setId(id);
        return mapper.get(model);
    }

    @Override
    public List<RolePermissionRelModel> list(RolePermissionRelModel model) throws SQLException {
        return mapper.list(model);
    }

    @Override
    public boolean addRolePermissionRel(List<Long> ids, Long roleId) throws SQLException {
        //删除之前的记录
        int num=mapper.delByRoleId(roleId);
        //新增
        for (Long  id : ids) {
            RolePermissionRelModel  model=new RolePermissionRelModel();
            model.setRoleId(roleId);
            model.setPermissionId(id);
            mapper.insert(model);
        }
        return true;
    }

	@Override
	public RolePermissionRelModel searchByModel(RolePermissionRelModel model)
			throws SQLException {
		return mapper.get(model);
	}
}

