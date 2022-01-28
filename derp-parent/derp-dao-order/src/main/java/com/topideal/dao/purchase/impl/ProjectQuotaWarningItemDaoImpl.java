package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.ProjectQuotaWarningItemDao;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningItemModel;
import com.topideal.mapper.purchase.ProjectQuotaWarningItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ProjectQuotaWarningItemDaoImpl implements ProjectQuotaWarningItemDao {

    @Autowired
    private ProjectQuotaWarningItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ProjectQuotaWarningItemModel> list(ProjectQuotaWarningItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ProjectQuotaWarningItemModel model) throws SQLException {
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
    public int modify(ProjectQuotaWarningItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ProjectQuotaWarningItemModel  searchByPage(ProjectQuotaWarningItemModel  model) throws SQLException{
        PageDataModel<ProjectQuotaWarningItemModel> pageModel=mapper.listByPage(model);
        return (ProjectQuotaWarningItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ProjectQuotaWarningItemModel  searchById(Long id)throws SQLException {
        ProjectQuotaWarningItemModel  model=new ProjectQuotaWarningItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ProjectQuotaWarningItemModel searchByModel(ProjectQuotaWarningItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ProjectQuotaWarningItemDTO getItemListByPage(ProjectQuotaWarningItemDTO dto) {
        PageDataModel<ProjectQuotaWarningItemDTO> pageModel=mapper.getItemListByPage(dto);
        return (ProjectQuotaWarningItemDTO ) pageModel.getModel();
    }

    @Override
    public List<ProjectQuotaWarningItemDTO> exportProjectQuotaWarningDetail(ProjectQuotaWarningItemDTO dto) {
        return mapper.exportProjectQuotaWarningDetail(dto);
    }
}