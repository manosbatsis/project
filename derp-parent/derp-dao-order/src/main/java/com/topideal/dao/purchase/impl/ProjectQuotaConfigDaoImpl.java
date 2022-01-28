package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.ProjectQuotaConfigDao;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;
import com.topideal.mapper.purchase.ProjectQuotaConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ProjectQuotaConfigDaoImpl implements ProjectQuotaConfigDao {

    @Autowired
    private ProjectQuotaConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ProjectQuotaConfigModel> list(ProjectQuotaConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ProjectQuotaConfigModel model) throws SQLException {
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
    public int modify(ProjectQuotaConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ProjectQuotaConfigModel  searchByPage(ProjectQuotaConfigModel  model) throws SQLException{
        PageDataModel<ProjectQuotaConfigModel> pageModel=mapper.listByPage(model);
        return (ProjectQuotaConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ProjectQuotaConfigModel  searchById(Long id)throws SQLException {
        ProjectQuotaConfigModel  model=new ProjectQuotaConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ProjectQuotaConfigModel searchByModel(ProjectQuotaConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ProjectQuotaConfigDTO getListByPage(ProjectQuotaConfigDTO dto) {
        PageDataModel<ProjectQuotaConfigDTO> pageModel=mapper.getListByPage(dto);
        return (ProjectQuotaConfigDTO ) pageModel.getModel();
    }

    @Override
    public List<ProjectQuotaConfigModel> getLatestConfigList(Map<String, Object> queryMap) {
        return mapper.getLatestConfigList(queryMap);
    }
	@Override
	public List<ProjectQuotaConfigModel> getCoincidenceTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel) {
		return mapper.getCoincidenceTimeList(queryProjectQuatoConfigModel);
	}
	@Override
	public List<ProjectQuotaConfigModel> getInScopeTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel) {
		return mapper.getInScopeTimeList(queryProjectQuatoConfigModel);
	}
}