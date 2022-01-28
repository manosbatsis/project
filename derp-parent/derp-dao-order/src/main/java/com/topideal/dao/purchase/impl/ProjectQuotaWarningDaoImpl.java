package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.ProjectQuotaWarningDao;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningModel;
import com.topideal.mapper.purchase.ProjectQuotaWarningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ProjectQuotaWarningDaoImpl implements ProjectQuotaWarningDao {

    @Autowired
    private ProjectQuotaWarningMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ProjectQuotaWarningModel> list(ProjectQuotaWarningModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ProjectQuotaWarningModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ProjectQuotaWarningModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ProjectQuotaWarningModel  searchByPage(ProjectQuotaWarningModel  model) throws SQLException{
        PageDataModel<ProjectQuotaWarningModel> pageModel=mapper.listByPage(model);
        return (ProjectQuotaWarningModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ProjectQuotaWarningModel  searchById(Long id)throws SQLException {
        ProjectQuotaWarningModel  model=new ProjectQuotaWarningModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ProjectQuotaWarningModel searchByModel(ProjectQuotaWarningModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ProjectQuotaWarningDTO getListByPage(ProjectQuotaWarningDTO dto) {
        PageDataModel<ProjectQuotaWarningDTO> pageModel=mapper.getListByPage(dto);
        return (ProjectQuotaWarningDTO ) pageModel.getModel();
    }

    @Override
    public List<ProjectQuotaWarningDTO> exportProjectQuotaWarning(ProjectQuotaWarningDTO dto) {
        return mapper.exportProjectQuotaWarning(dto);
    }
}