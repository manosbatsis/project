package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.CustomerBankMerchantRelDao;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;
import com.topideal.mapper.main.CustomerBankMerchantRelMapper;
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
public class CustomerBankMerchantRelDaoImpl implements CustomerBankMerchantRelDao {

    @Autowired
    private CustomerBankMerchantRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerBankMerchantRelModel> list(CustomerBankMerchantRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomerBankMerchantRelModel model) throws SQLException {
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
    public int modify(CustomerBankMerchantRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerBankMerchantRelModel  searchByPage(CustomerBankMerchantRelModel  model) throws SQLException{
        PageDataModel<CustomerBankMerchantRelModel> pageModel=mapper.listByPage(model);
        return (CustomerBankMerchantRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerBankMerchantRelModel  searchById(Long id)throws SQLException {
        CustomerBankMerchantRelModel  model=new CustomerBankMerchantRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomerBankMerchantRelModel searchByModel(CustomerBankMerchantRelModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据条件删除 商家客户银行信息
	 */
	@Override
	public int deleteByParam(Map<String, Object> map) throws SQLException {		
		return mapper.deleteByParam(map);
	}

}