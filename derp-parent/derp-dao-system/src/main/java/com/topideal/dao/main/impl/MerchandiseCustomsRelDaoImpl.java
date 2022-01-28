package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchandiseCustomsRelDao;
import com.topideal.entity.vo.main.MerchandiseCustomsRelModel;
import com.topideal.mapper.main.MerchandiseCustomsRelMapper;
import com.topideal.mongo.dao.MerchandiseCustomsRelMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MerchandiseCustomsRelDaoImpl implements MerchandiseCustomsRelDao {

    @Autowired
    private MerchandiseCustomsRelMapper mapper;
    @Autowired
	private MerchandiseCustomsRelMongoDao mongo;//商品关区关系表
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseCustomsRelModel> list(MerchandiseCustomsRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseCustomsRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;			
			json.put("merchandiseCustomsRelId", model.getId());		
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
			params.put("merchandiseCustomsRelId",(Long)ids.get(i));
			mongo.remove(params);
		}
    	return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchandiseCustomsRelModel  model) throws SQLException {
    	int num = mapper.update(model);
    	if(num > 0) {
    		MerchandiseCustomsRelModel merchandiseCustomsRelModel = new MerchandiseCustomsRelModel();
    		merchandiseCustomsRelModel.setId(model.getId());
    		merchandiseCustomsRelModel = mapper.get(merchandiseCustomsRelModel);
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseCustomsRelId", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(merchandiseCustomsRelModel);
			jsonObject.put("merchandiseCustomsRelId",model.getId());
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
    public MerchandiseCustomsRelModel  searchByPage(MerchandiseCustomsRelModel  model) throws SQLException{
        PageDataModel<MerchandiseCustomsRelModel> pageModel=mapper.listByPage(model);
        return (MerchandiseCustomsRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseCustomsRelModel  searchById(Long id)throws SQLException {
        MerchandiseCustomsRelModel  model=new MerchandiseCustomsRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseCustomsRelModel searchByModel(MerchandiseCustomsRelModel model) throws SQLException {
		return mapper.get(model);
	}

}