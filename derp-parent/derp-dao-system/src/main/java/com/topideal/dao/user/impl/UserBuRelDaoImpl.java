package com.topideal.dao.user.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.mapper.user.UserBuRelMapper;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class UserBuRelDaoImpl implements UserBuRelDao {

    @Autowired
    private UserBuRelMapper mapper;
    
    @Autowired
    private UserBuRelMongoDao mongo ;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<UserBuRelModel> list(UserBuRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(UserBuRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("userBuRelId", model.getId()) ;
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

		// 先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {

			Long id = (Long)ids.get(i);

			UserBuRelModel userBuRelModel = new UserBuRelModel();

			userBuRelModel.setId(id);
			userBuRelModel = mapper.get(userBuRelModel);

			// 修改mongodb 客户、供应商信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("buId", userBuRelModel.getBuId());
			params.put("userId", userBuRelModel.getId());

			mongo.remove(params);
		}

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(UserBuRelModel  model) throws SQLException {

		int num = mapper.update(model);

		if(num > 0){
			UserBuRelModel userBuRelModel = new UserBuRelModel();
			userBuRelModel.setId(model.getId());
			userBuRelModel = mapper.get(userBuRelModel);
			// 修改mongodb 客户、供应商信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", model.getUserId());
			params.put("buId", model.getBuId()) ;

			JSONObject jsonObject = JSONObject.fromObject(userBuRelModel);
			jsonObject.put("userBuRelId", model.getId()) ;
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
    public UserBuRelModel  searchByPage(UserBuRelModel  model) throws SQLException{
        PageDataModel<UserBuRelModel> pageModel=mapper.listByPage(model);
        return (UserBuRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public UserBuRelModel  searchById(Long id)throws SQLException {
        UserBuRelModel  model=new UserBuRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public UserBuRelModel searchByModel(UserBuRelModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据用户ID删除
	 * @throws SQLException 
	 */
	@Override
	public int delByUserId(Long id) throws SQLException {
		
		List<UserBuRelModel> relList = this.getListByUserId(id);
		
		Map<String, Object> params = new HashMap<String,Object>();
		for (UserBuRelModel rel : relList) {
			params.put("userId",rel.getUserId());
			mongo.remove(params);
		}
		
		return mapper.delByUserId(id);
	}
	
	@Override
	public List<UserBuRelModel> getListByUserId(Long id) throws SQLException {
		
		UserBuRelModel queryModel = new UserBuRelModel() ;
		queryModel.setUserId(id);
		List<UserBuRelModel> relList = mapper.list(queryModel) ;
		
		return relList;
	}

}