package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.CustomerMerchantRelDao;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.mapper.main.CustomerMerchantRelMapper;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;

import net.sf.json.JSONObject;

/**
 * 客户商家关联表 impl
 * @author lchenxing
 */
@Repository
public class CustomerMerchantRelDaoImpl implements CustomerMerchantRelDao {

    @Autowired
    private CustomerMerchantRelMapper mapper;
    // 客户、供应商 关联mongo
 	@Autowired
 	private CustomerMerchantRelMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerMerchantRelModel> list(CustomerMerchantRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 *
	 */
    @Override
    public Long save(CustomerMerchantRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){

        	// 存储到MONGODB
			JSONObject jsonObject = JSONObject.fromObject(model);
			jsonObject.put("customerRelId", model.getId());
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());

			return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     */
    @Override
    public int delete(List ids) throws SQLException {
    	// 先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerRelId", (Long) ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     */
    @Override
    public int modify(CustomerMerchantRelModel  model) throws SQLException {
    	
    	int rows = mapper.update(model);
    	
    	if(rows > 0){
    		CustomerMerchantRelModel customerRel = new CustomerMerchantRelModel();
    		customerRel.setId(model.getId());
    		customerRel = mapper.get(customerRel);
			// 修改mongodb 客户、供应商信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerRelId", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(customerRel);
			jsonObject.put("customerRelId", model.getId());
			jsonObject.remove("id");
			mongo.update(params, jsonObject);
		}
    	
        return rows;
    }
    
	/**
     * 分页查询
     */
    @Override
    public CustomerMerchantRelModel  searchByPage(CustomerMerchantRelModel  model) throws SQLException{
        PageDataModel<CustomerMerchantRelModel> pageModel=mapper.listByPage(model);
        return (CustomerMerchantRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     */
    @Override
    public CustomerMerchantRelModel  searchById(Long id)throws SQLException {
        CustomerMerchantRelModel  model=new CustomerMerchantRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* */
	@Override
	public CustomerMerchantRelModel searchByModel(CustomerMerchantRelModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public CustomerMerchantRelModel searchDetail(CustomerMerchantRelModel model) {
		return mapper.searchDetail(model);
    }
	@Override
	public List<Map<String, Object>> getCustomerMerchantRelList(Map<String, Object> map)
			throws SQLException {
		return mapper.getCustomerMerchantRelList( map);
	}
	/**
	 * 查询商家客户关系表数据
	 */
	@Override
	public List<CustomerMerchantRelModel> getCustMerListByUser(Map<String, Object> map) throws SQLException {		
		return mapper.getCustMerListByUser(map);
	}
}
