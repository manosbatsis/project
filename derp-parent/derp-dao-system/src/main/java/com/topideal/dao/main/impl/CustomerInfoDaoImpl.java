package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerInfoDao;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.mapper.main.CustomerInfoMapper;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * 
 * @author lchenxing
 */
@Repository
public class CustomerInfoDaoImpl implements CustomerInfoDao {

	// 客户、供应商
	@Autowired
	private CustomerInfoMapper mapper;
	// 客户、供应商 mongo
	@Autowired
	private CustomerInfoMongoDao mongo;

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(CustomerInfoModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			// 存储到MONGODB
			JSONObject jsonObject = JSONObject.fromObject(model);
			jsonObject.put("customerid", model.getId());
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		// 先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerid", (Long) ids.get(i));
			mongo.remove(params);
		}
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(CustomerInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int num = mapper.update(model);
		if(num > 0){
			CustomerInfoModel customer = new CustomerInfoModel();
			customer.setId(model.getId());
			customer = mapper.get(customer);
			// 修改mongodb 客户、供应商信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerid", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(customer);
			jsonObject.put("customerid", model.getId());
			jsonObject.remove("id");
			mongo.update(params, jsonObject);
		}
		return num;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public CustomerInfoModel searchByPage(CustomerInfoModel model) throws SQLException {
		PageDataModel<CustomerInfoModel> pageModel = mapper.listByPage(model);
		return (CustomerInfoModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public CustomerInfoModel searchById(Long id) throws SQLException {
		CustomerInfoModel model = new CustomerInfoModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据实体类查询客户
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public CustomerInfoModel searchByModel(CustomerInfoModel model) throws SQLException {
		return mapper.getDetails(model);
	}

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<CustomerInfoModel> list(CustomerInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 根据条件查询供应商下拉列表
	 * 
	 * @param id
	 */
	@Override
	public List<SelectBean> getSelectBeanBySupplier(Long id) throws SQLException {
		return mapper.getSelectBeanBySupplier(id);
	}

	/**
	 * 根据条件查询客户下拉列表
	 * 
	 * @param id
	 */
	/*@Override
	public List<SelectBean> getSelectBeanByCustomer(Long id) throws SQLException {
		return mapper.getSelectBeanByCustomer(id);
	}*/

	/**
	 * 根据商家ID获取客户下拉列表(必须是启用的状态)
	 * 
	 * @param dto
	 */
	@Override
	public List<SelectBean> getSelectBeanByMerchantId(CustomerInfoDTO dto) throws SQLException {
		return mapper.getSelectBeanByMerchantId(dto);
	}

	/**
	 * 获取详情
	 * 
	 * @param model
	 */
	@Override
	public CustomerInfoDTO getDetails(Long id) throws SQLException {
		return mapper.getDTODetails(id);
	}

	
	  /**
     *  导出客户信息
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<CustomerInfoDTO> exportList(CustomerInfoDTO model) throws SQLException {
		return mapper.exportCustomerMap(model);
	}
	
	  /**
     *  导出供应商信息
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<CustomerInfoDTO> exportSupplierList(CustomerInfoDTO model) throws SQLException {
		return mapper.exportSupplier(model);
	}

	/**
	 * 获取详情
	 */
	@Override
	public List<CustomerInfoModel> getDetailsList(CustomerInfoModel model) throws SQLException {
		return mapper.getDetailsList(model);
	}

	/*@Override
	public List<SelectBean> getNameById(Long id) throws SQLException {
		return mapper.getNameById(id);
	}*/

	/**
	 * 获取所有启用状态的供应商下拉框
	 */
	@Override
	public List<SelectBean> getAllSelectBeanBySupplier() throws SQLException {
		return mapper.getAllSelectBeanBySupplier();
	}

	@Override
	public CustomerInfoDTO searchDTOByPage(CustomerInfoDTO dto) throws SQLException {
		PageDataModel<CustomerInfoDTO> pageModel = mapper.getListByPage(dto);
		return (CustomerInfoDTO) pageModel.getModel();
	}

	@Override
	public CustomerInfoDTO getDetailsByRelId(Long relId) {
		return mapper.getDTODetailsByRelId(relId);
	}

	@Override
	public List<CustomerInfoDTO> getSelectBeanByDTO(CustomerInfoDTO dto) throws SQLException {
		return mapper.getSelectBeanByDTO(dto);
	}

	@Override
	public List<CustomerInfoDTO> getCustomerByMerchantId(CustomerInfoDTO dto) throws SQLException {
		return mapper.getCustomerByMerchantId(dto);
	}

	@Override
	public List<CustomerMerchantRelModel> getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) throws SQLException {
		return mapper.getSupplierMerchantRelByMainIdURL(dto);
	}

	@Override
	public CustomerInfoDTO getCustomerByMainMerchantId(CustomerInfoDTO dto) throws SQLException {
		return mapper.getCustomerByMainMerchantId(dto);
	}

	/**
	 * 获取客户或者供应商公共方法
	 */
	@Override
	public List<SelectBean> getCusOrSurpSelectBean(CustomerInfoDTO dto) throws SQLException {
		return mapper.getCusOrSurpSelectBean(dto);
	}


}
