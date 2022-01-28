package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.CustomerSalePriceDao;
import com.topideal.entity.vo.system.CustomerSalePriceModel;
import com.topideal.mapper.system.CustomerSalePriceMapper;

/**
 * 客户销售价格表 daoImpl
 * @author lchenxing
 */
@Repository
public class CustomerSalePriceDaoImpl implements CustomerSalePriceDao {

    @Autowired
    private CustomerSalePriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerSalePriceModel> list(CustomerSalePriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomerSalePriceModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
    	return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(CustomerSalePriceModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerSalePriceModel  searchByPage(CustomerSalePriceModel  model) throws SQLException{
        PageDataModel<CustomerSalePriceModel> pageModel=mapper.listByPage(model);
        return (CustomerSalePriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerSalePriceModel  searchById(Long id)throws SQLException {
        CustomerSalePriceModel  model=new CustomerSalePriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomerSalePriceModel searchByModel(CustomerSalePriceModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 分页
	 */
	@Override
	public CustomerSalePriceModel getListByPage(CustomerSalePriceModel model) throws SQLException {
		PageDataModel<CustomerSalePriceModel> pageModel=mapper.listByPage(model);
        return (CustomerSalePriceModel ) pageModel.getModel();
	}
	
	/**
	 * 弹框 客户列表
	 */
	@Override
	public CustomerSalePriceModel getCustomerByPage(CustomerSalePriceModel model) {
		PageDataModel<CustomerSalePriceModel> pageModel = mapper.getBrandByPage(model);
		return (CustomerSalePriceModel) pageModel.getModel();
	}
	@Override
	public CustomerSalePriceModel selectByModel(CustomerSalePriceModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/* 
	 * 根据商家id,客户id,商品货号 获取价格
	 */
	@Override
	public CustomerSalePriceModel getMerchandisePrice(CustomerSalePriceModel model) throws SQLException {
		return mapper.getPrice(model);
	}
}
