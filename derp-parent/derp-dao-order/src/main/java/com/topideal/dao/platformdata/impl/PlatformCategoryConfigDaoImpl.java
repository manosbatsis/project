package com.topideal.dao.platformdata.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.PlatformCategoryConfigDao;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.mapper.platformdata.PlatformCategoryConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformCategoryConfigDaoImpl implements PlatformCategoryConfigDao {

    @Autowired
    private PlatformCategoryConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformCategoryConfigModel> list(PlatformCategoryConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformCategoryConfigModel model) throws SQLException {
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
    public int modify(PlatformCategoryConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformCategoryConfigModel  searchByPage(PlatformCategoryConfigModel  model) throws SQLException{
        PageDataModel<PlatformCategoryConfigModel> pageModel=mapper.listByPage(model);
        return (PlatformCategoryConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformCategoryConfigModel  searchById(Long id)throws SQLException {
        PlatformCategoryConfigModel  model=new PlatformCategoryConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformCategoryConfigModel searchByModel(PlatformCategoryConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PlatformCategoryConfigDTO getListByPage(PlatformCategoryConfigDTO dto) {
        PageDataModel<PlatformCategoryConfigDTO> pageModel=mapper.getListByPage(dto);
        return (PlatformCategoryConfigDTO ) pageModel.getModel();
    }
	@Override
	public List<SelectBean> getSelectBean(PlatformCategoryConfigDTO dto) {
		return mapper.getSelectBean(dto);
	}
}