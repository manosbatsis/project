package com.topideal.dao.user.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.mapper.user.UserMerchantRelMapper;
import com.topideal.mongo.dao.UserMerchantRelMongoDao;
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
public class UserMerchantRelDaoImpl implements UserMerchantRelDao {

    @Autowired
    private UserMerchantRelMapper mapper;

    @Autowired
    private UserMerchantRelMongoDao mongo ;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<UserMerchantRelModel> list(UserMerchantRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(UserMerchantRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            JSONObject jsonObject=JSONObject.fromObject(model);
            jsonObject.put("userMerchantRelId", model.getId()) ;
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

            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();

            userMerchantRelModel.setId(id);
            userMerchantRelModel = mapper.get(userMerchantRelModel);

            // 修改mongodb 客户、公司信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchantId", userMerchantRelModel.getMerchantId());
            params.put("userId", userMerchantRelModel.getId());

            mongo.remove(params);
        }

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(UserMerchantRelModel  model) throws SQLException {
        int num = mapper.update(model);
        if(num > 0){
            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
            userMerchantRelModel.setId(model.getId());
            userMerchantRelModel = mapper.get(userMerchantRelModel);
            // 修改mongodb 客户、供应商信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", model.getUserId());
            params.put("merchantId", model.getMerchantId()) ;

            JSONObject jsonObject = JSONObject.fromObject(userMerchantRelModel);
            jsonObject.put("userMerchantRelId", model.getId()) ;
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
    public UserMerchantRelModel  searchByPage(UserMerchantRelModel  model) throws SQLException{
        PageDataModel<UserMerchantRelModel> pageModel=mapper.listByPage(model);
        return (UserMerchantRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public UserMerchantRelModel  searchById(Long id)throws SQLException {
        UserMerchantRelModel  model=new UserMerchantRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public UserMerchantRelModel searchByModel(UserMerchantRelModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<SelectBean> getUserSelectBean(UserMerchantRelModel model) throws SQLException {
        return mapper.getUserSelectBean(model);
    }

    @Override
    public List<MerchantInfoModel> getUserMerchantList(UserMerchantRelModel model) throws SQLException {
        return mapper.getUserMerchantList(model);
    }

    @Override
    public int delAllByUserId(Long userId) throws SQLException {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        mongo.remove(params);
        return mapper.delAllByUserId(userId);
    }
}