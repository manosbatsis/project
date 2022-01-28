package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.CustomerMerchantRelDao;
import com.topideal.entity.vo.system.CustomerMerchantRelModel;
import com.topideal.mapper.system.CustomerMerchantRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CustomerMerchantRelDaoImpl implements CustomerMerchantRelDao {

    @Autowired
    private CustomerMerchantRelMapper mapper;
	
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
	 * @param model
	 */
    @Override
    public Long save(CustomerMerchantRelModel model) throws SQLException {
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
    public int modify(CustomerMerchantRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerMerchantRelModel  searchByPage(CustomerMerchantRelModel  model) throws SQLException{
        PageDataModel<CustomerMerchantRelModel> pageModel=mapper.listByPage(model);
        return (CustomerMerchantRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerMerchantRelModel  searchById(Long id)throws SQLException {
        CustomerMerchantRelModel  model=new CustomerMerchantRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomerMerchantRelModel searchByModel(CustomerMerchantRelModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<CustomerMerchantRelModel> getExtraCustomerList(Map<String, Object> queryMap) {
		return mapper.getExtraCustomerList(queryMap);
	}

}