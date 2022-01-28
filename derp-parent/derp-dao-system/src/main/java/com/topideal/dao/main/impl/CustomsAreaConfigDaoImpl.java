package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.CustomsAreaConfigDao;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.mapper.main.CustomsAreaConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CustomsAreaConfigDaoImpl implements CustomsAreaConfigDao {

    @Autowired
    private CustomsAreaConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomsAreaConfigModel> list(CustomsAreaConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomsAreaConfigModel model) throws SQLException {
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
    public int modify(CustomsAreaConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomsAreaConfigModel  searchByPage(CustomsAreaConfigModel  model) throws SQLException{
        PageDataModel<CustomsAreaConfigModel> pageModel=mapper.listByPage(model);
        return (CustomsAreaConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomsAreaConfigModel  searchById(Long id)throws SQLException {
        CustomsAreaConfigModel  model=new CustomsAreaConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomsAreaConfigModel searchByModel(CustomsAreaConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public CustomsAreaConfigDTO listByPage(CustomsAreaConfigDTO model) {
        PageDataModel<CustomsAreaConfigDTO> pageModel = mapper.getListByPage(model);
        return (CustomsAreaConfigDTO) pageModel.getModel();
    }
	@Override
	public List<SelectBean> getCustomsSelectBean() throws Exception {
		return mapper.getCustomsSelectBean();
	}

    @Override
    public List<CustomsAreaConfigModel> listByLike(CustomsAreaConfigModel customsAreaConfigModel) {
        return mapper.listByLike(customsAreaConfigModel);
    }

}