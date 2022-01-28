package com.topideal.dao.base.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mapper.base.ApiSecretConfigMapper;
import com.topideal.mongo.dao.ApiSecretConfigMongoDao;

import net.sf.json.JSONObject;

/**
 * api密钥配置 impl
 * @author lchenxing
 */
@Repository
public class ApiSecretConfigDaoImpl implements ApiSecretConfigDao {

    @Autowired
    private ApiSecretConfigMapper mapper;
    @Autowired
    private ApiSecretConfigMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ApiSecretConfigModel> list(ApiSecretConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ApiSecretConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("apiSecretId",model.getId());
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());
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
    	//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("apiSecretId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ApiSecretConfigModel  model) throws SQLException {
    	int num = mapper.update(model);
    	if(num > 0){
    		ApiSecretConfigModel apiSecret = new ApiSecretConfigModel();
    		apiSecret.setId(model.getId());
    		apiSecret = mapper.get(apiSecret);
	    	//修改mongodb  api密钥配置 信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("apiSecretId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(apiSecret);
			jsonObject.put("apiSecretId",model.getId());
			jsonObject.remove("id");

			mongo.update(params,jsonObject);
    	}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ApiSecretConfigModel  searchByPage(ApiSecretConfigModel  model) throws SQLException{
        PageDataModel<ApiSecretConfigModel> pageModel=mapper.listByPage(model);
        return (ApiSecretConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ApiSecretConfigModel  searchById(Long id)throws SQLException {
        ApiSecretConfigModel  model=new ApiSecretConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ApiSecretConfigModel searchByModel(ApiSecretConfigModel model) throws SQLException {
		return mapper.get(model);
	}
    
	/**
	 * 分页查询
	 */
	@Override
	public ApiSecretConfigDTO getListByPage(ApiSecretConfigDTO dto) throws SQLException {
		PageDataModel<ApiSecretConfigDTO> pageModel = mapper.getListByPage(dto);
		return (ApiSecretConfigDTO) pageModel.getModel();
	}
	
	@Override
	public ApiSecretConfigDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}

}
