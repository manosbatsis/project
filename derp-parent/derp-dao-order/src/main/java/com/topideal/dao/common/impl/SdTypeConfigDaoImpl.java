package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SdTypeConfigDao;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.mapper.common.SdTypeConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdTypeConfigDaoImpl implements SdTypeConfigDao {

    @Autowired
    private SdTypeConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdTypeConfigModel> list(SdTypeConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdTypeConfigModel model) throws SQLException {
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
    public int modify(SdTypeConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdTypeConfigModel  searchByPage(SdTypeConfigModel  model) throws SQLException{
        PageDataModel<SdTypeConfigModel> pageModel=mapper.listByPage(model);
        return (SdTypeConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdTypeConfigModel  searchById(Long id)throws SQLException {
        SdTypeConfigModel  model=new SdTypeConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdTypeConfigModel searchByModel(SdTypeConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SdTypeConfigDTO getSdTypeConfigListByPage(SdTypeConfigDTO dto) {
		PageDataModel<SdTypeConfigModel> pageModel=mapper.getListByPage(dto);
        return (SdTypeConfigDTO ) pageModel.getModel();
	}

}