package com.topideal.dao.base.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.BrandSuperiorDao;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.mapper.base.BrandSuperiorMapper;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BrandSuperiorDaoImpl implements BrandSuperiorDao {

    @Autowired
    private BrandSuperiorMapper mapper;
    @Autowired
    private BrandSuperiorMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BrandSuperiorModel> list(BrandSuperiorModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BrandSuperiorModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;			
			json.put("brandSuperiorId", model.getId());//母品牌id			
			mongo.insertJson(json.toString());
			
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
    	//从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("brandSuperiorId",(Long)ids.get(i));
			mongo.remove(params);
		}
    	return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BrandSuperiorModel  model) throws SQLException {
        int num =mapper.update(model);
		if(num > 0) {
			BrandSuperiorModel brandSuperior = new BrandSuperiorModel();
			brandSuperior.setId(model.getId());
			brandSuperior = mapper.get(brandSuperior);
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("brandSuperiorId", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(brandSuperior);
			jsonObject.put("brandSuperiorId",model.getId());
			jsonObject.remove("id");
			
			mongo.update(params, jsonObject);
		}
    	
    	return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BrandSuperiorModel  searchByPage(BrandSuperiorModel  model) throws SQLException{
        PageDataModel<BrandSuperiorModel> pageModel=mapper.listByPage(model);
        return (BrandSuperiorModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BrandSuperiorModel  searchById(Long id)throws SQLException {
        BrandSuperiorModel  model=new BrandSuperiorModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BrandSuperiorModel searchByModel(BrandSuperiorModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BrandSuperiorDTO getListByPage(BrandSuperiorDTO dto) throws SQLException {
        PageDataModel<BrandSuperiorDTO> pageModel = mapper.getListByPage(dto);
	    return (BrandSuperiorDTO)pageModel.getModel();
    }

    /**
     * 获取下拉
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectBean> getSelectBean() throws SQLException {
        return mapper.getSelectBean();
    }

	@Override
	public BrandSuperiorModel getByCommbarcode(String commbarcode) throws SQLException {
		return mapper.getByCommbarcode(commbarcode);
	}
}