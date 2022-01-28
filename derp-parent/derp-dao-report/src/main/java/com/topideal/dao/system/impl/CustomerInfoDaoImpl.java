package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.CustomerInfoDao;
import com.topideal.entity.vo.system.CustomerInfoModel;
import com.topideal.mapper.system.CustomerInfoMapper;

@Repository
public class CustomerInfoDaoImpl implements CustomerInfoDao {

	@Autowired
	private CustomerInfoMapper mapper;

	@Override
	public List<SelectBean> listAllCustomer() throws SQLException {
		return mapper.listAllCustomer();
	}

	/**
	 * 新增
	 * @param model
	 */
	@Override
	public Long save(CustomerInfoModel model) throws SQLException {
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
	public int modify(CustomerInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
//		// TODO Auto-generated method stub
//		return 0;
	}

	/**
     * 分页查询
     * @param model
     */
	@Override
	public CustomerInfoModel searchByPage(CustomerInfoModel model) throws SQLException {
		PageDataModel<CustomerInfoModel> pageModel=mapper.listByPage(model);
        return (CustomerInfoModel ) pageModel.getModel();
	}

    /**
     * 通过id查询实体类信息
     * @param id
     */
	@Override
	public CustomerInfoModel searchById(Long id) throws SQLException {
		CustomerInfoModel  model=new CustomerInfoModel ();
        model.setId(id);
        return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerInfoModel> list(CustomerInfoModel model) throws SQLException {
		return mapper.list(model);
	}

    /**
   	* 根据商家实体类查询商品
   	* @param model
   	* */
	@Override
	public CustomerInfoModel searchByModel(CustomerInfoModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<CustomerInfoModel> listAllCustomerByMerchant(Long merchantId) throws SQLException {
		return mapper.listAllCustomerByMerchant(merchantId);
	}

	@Override
	public CustomerInfoModel getDetails(CustomerInfoModel model) throws SQLException {
		return mapper.getDetails(model);
	}

}
