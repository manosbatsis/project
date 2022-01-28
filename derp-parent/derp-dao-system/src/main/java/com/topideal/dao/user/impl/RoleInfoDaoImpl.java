package com.topideal.dao.user.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.user.RoleInfoDao;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.mapper.user.RoleInfoMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 */
@Repository
public class RoleInfoDaoImpl implements RoleInfoDao {

    @Autowired
    private RoleInfoMapper mapper;

    @Override
    public Long save(RoleInfoModel model) throws SQLException {
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
    public int modify(RoleInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

    @Override
    public RoleInfoModel  searchByPage(RoleInfoModel  model) throws SQLException{
        PageDataModel<RoleInfoModel> pageModel=mapper.listByPage(model);
        return (RoleInfoModel ) pageModel.getModel();
    }

    @Override
    public RoleInfoModel  searchById(Long id) throws SQLException{
        RoleInfoModel  model=new RoleInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }

    @Override
    public List<RoleInfoModel> list(RoleInfoModel model) throws SQLException {
        return mapper.list(model);
    }

	@Override
	public RoleInfoModel searchByModel(RoleInfoModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public RoleInfoDTO searchDTOByPage(RoleInfoDTO dto) throws SQLException {
		PageDataModel<RoleInfoDTO> pageModel=mapper.searchDTOByPage(dto);
        return (RoleInfoDTO ) pageModel.getModel();
	}
}

