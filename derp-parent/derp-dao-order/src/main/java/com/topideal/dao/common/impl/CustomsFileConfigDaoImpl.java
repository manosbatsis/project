package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.CustomsFileConfigDao;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.vo.common.CustomsFileConfigModel;
import com.topideal.mapper.common.CustomsFileConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CustomsFileConfigDaoImpl implements CustomsFileConfigDao {

    @Autowired
    private CustomsFileConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomsFileConfigModel> list(CustomsFileConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomsFileConfigModel model) throws SQLException {
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
    public int modify(CustomsFileConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomsFileConfigModel  searchByPage(CustomsFileConfigModel  model) throws SQLException{
        PageDataModel<CustomsFileConfigModel> pageModel=mapper.listByPage(model);
        return (CustomsFileConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomsFileConfigModel  searchById(Long id)throws SQLException {
        CustomsFileConfigModel  model=new CustomsFileConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomsFileConfigModel searchByModel(CustomsFileConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 查询所有信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public CustomsFileConfigDTO listDTOByPage(CustomsFileConfigDTO dto) throws SQLException {
		 PageDataModel<CustomsFileConfigDTO> pageModel=mapper.listDTOByPage(dto);
	     return (CustomsFileConfigDTO) pageModel.getModel();
	}
	/**
	 * 获取数量
	 * @param dto
	 * @return
	 */
	@Override
	public Integer getCountOrder(CustomsFileConfigDTO dto) throws SQLException {		
		return mapper.getCountOrder(dto);
	}
	@Override
	public List<CustomsFileConfigDTO> getExportTemplate(CustomsFileConfigDTO dto) {
		return mapper.getExportTemplate(dto);
	}

}