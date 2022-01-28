package com.topideal.dao.user.impl;

import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.user.PermissionInfoDao;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.mapper.user.PermissionInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 */
@Repository
public class PermissionInfoDaoImpl implements PermissionInfoDao {

    @Autowired
    private PermissionInfoMapper mapper;

    @Override
    public Long save(PermissionInfoModel model) throws SQLException {
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
    public int modify(PermissionInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

    @Override
    public PermissionInfoModel  searchByPage(PermissionInfoModel  model) throws SQLException{
        PageDataModel<PermissionInfoModel> pageModel=mapper.listByPage(model);
        return (PermissionInfoModel ) pageModel.getModel();
    }

    @Override
    public PermissionInfoModel  searchById(Long id) throws SQLException{
        PermissionInfoModel  model=new PermissionInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }


    @Override
    public List<TreeBean> listTree()throws SQLException{
        return mapper.treeList();
    }

    @Override
    public List<TreeBean> listTreeAll(Long userId)throws SQLException{
        if(userId!=null){
            List<Long> ids=mapper.getIdByUserId(userId);
            return mapper.treeListAll(ids);
        }else{
            return mapper.treeListAll(null);
        }

    }

    @Override
    public List<PermissionInfoModel> list(PermissionInfoModel model) throws SQLException {
        return mapper.list(model);
    }

	@Override
	public PermissionInfoModel searchByModel(PermissionInfoModel model)
			throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<TreeBean> treeMenuList( Long userId) throws SQLException {
        return mapper.treeMenuList(userId);
    }

	@Override
	public PermissionInfoDTO searchDTOByPage(PermissionInfoDTO dto) {
		PageDataModel<PermissionInfoDTO> pageModel=mapper.searchDTOByPage(dto);
        return (PermissionInfoDTO ) pageModel.getModel();
	}

    @Override
    public int modifyWithNull(PermissionInfoModel model) throws SQLException {
        return mapper.modifyWithNull(model);
    }
}

