package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.CustomerBankDao;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.vo.main.CustomerBankModel;
import com.topideal.mapper.main.CustomerBankMapper;
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
public class CustomerBankDaoImpl implements CustomerBankDao {

    @Autowired
    private CustomerBankMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerBankModel> list(CustomerBankModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomerBankModel model) throws SQLException {
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
    public int modify(CustomerBankModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerBankModel  searchByPage(CustomerBankModel  model) throws SQLException{
        PageDataModel<CustomerBankModel> pageModel=mapper.listByPage(model);
        return (CustomerBankModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerBankModel  searchById(Long id)throws SQLException {
        CustomerBankModel  model=new CustomerBankModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomerBankModel searchByModel(CustomerBankModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<CustomerBankDTO> getCustomerBySupplierId(Long customerId, Long merchantId) {
        return mapper.getCustomerBySupplierId(customerId,merchantId);
    }
	@Override
	public int deleteByParam(Map<String, Object> map) throws SQLException {
		return mapper.deleteByParam(map);
	}
	@Override
	public List<CustomerBankDTO> getCustomerBankList(CustomerBankDTO dto) {
		return mapper.getCustomerBankList(dto);
	}
}