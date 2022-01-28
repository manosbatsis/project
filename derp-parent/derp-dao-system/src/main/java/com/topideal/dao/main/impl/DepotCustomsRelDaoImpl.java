package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.DepotCustomsRelDao;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.mapper.main.DepotCustomsRelMapper;
import com.topideal.mongo.dao.DepotCustomsRelMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class DepotCustomsRelDaoImpl implements DepotCustomsRelDao {

    @Autowired
    private DepotCustomsRelMapper mapper;
    @Autowired
    private DepotCustomsRelMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<DepotCustomsRelModel> list(DepotCustomsRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(DepotCustomsRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;			
			json.put("depotCustomsRelId", model.getId());		
			mongo.insertJson(json.toString());
			
			return model.getId();
        }
        return Long.valueOf(num);
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
			params.put("depotCustomsRelId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(DepotCustomsRelModel  model) throws SQLException {

    	int num =mapper.update(model);

    	if(num > 0) {
    		DepotCustomsRelModel depotCustomsRelModel = new DepotCustomsRelModel();
    		depotCustomsRelModel.setId(model.getId());
    		depotCustomsRelModel = mapper.get(depotCustomsRelModel);
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("depotCustomsRelId", model.getId());

			JSONObject json = JSONObject.fromObject(depotCustomsRelModel);
			json.remove("id") ;
			json.put("depotCustomsRelId", model.getId());//id
			
			mongo.update(params, json);
		}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public DepotCustomsRelModel  searchByPage(DepotCustomsRelModel  model) throws SQLException{
        PageDataModel<DepotCustomsRelModel> pageModel=mapper.listByPage(model);
        return (DepotCustomsRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public DepotCustomsRelModel  searchById(Long id)throws SQLException {
        DepotCustomsRelModel  model=new DepotCustomsRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public DepotCustomsRelModel searchByModel(DepotCustomsRelModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<SelectBean> getSelectBean(DepotCustomsRelModel model) throws SQLException {		
		return mapper.getSelectBean(model);
	}
	

}