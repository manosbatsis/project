package com.topideal.dao.main.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerSalePriceDao;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.vo.main.CustomerSalePriceModel;
import com.topideal.mapper.main.CustomerSalePriceMapper;
import com.topideal.mongo.dao.CustomerSalePriceMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户销售价格表 daoImpl
 * @author lchenxing
 */
@Repository
public class CustomerSalePriceDaoImpl implements CustomerSalePriceDao {

    @Autowired
    private CustomerSalePriceMapper mapper;
    @Autowired
    private CustomerSalePriceMongoDao mongo;
	
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
        if(num>0){

			JSONObject jsonObject = JSONObject.fromObject(model);

			jsonObject.put("customerSalePriceId", model.getId());
			jsonObject.remove("id");
			jsonObject.remove("expiryDate");
			jsonObject.remove("effectiveDate");
			jsonObject.remove("auditDate");
			jsonObject.put("expiryDate", TimeUtils.format(model.getExpiryDate(), "yyyy-MM-dd"));
			jsonObject.put("effectiveDate", TimeUtils.format(model.getEffectiveDate(), "yyyy-MM-dd"));
			if(model.getAuditDate() != null) {				
				jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
			}

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
    	for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerSalePriceId", (Long) ids.get(i));
			mongo.remove(params);
		}
    	return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(CustomerSalePriceModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
    	int num = mapper.update(model);
		if(num > 0){			
			// 修改mongodb 客户、供应商信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerSalePriceId", model.getId());
			if(!DERP.DEL_CODE_006.equals(model.getStatus())) {	
				CustomerSalePriceModel customer = new CustomerSalePriceModel();
				customer.setId(model.getId());
				customer = mapper.get(customer);
				JSONObject jsonObject = JSONObject.fromObject(customer);
				
				jsonObject.put("customerSalePriceId", model.getId());
				jsonObject.remove("id");
				jsonObject.remove("expiryDate");
				jsonObject.remove("effectiveDate");
				jsonObject.remove("auditDate");
				jsonObject.put("expiryDate", TimeUtils.format(customer.getExpiryDate(), "yyyy-MM-dd"));
				jsonObject.put("effectiveDate", TimeUtils.format(customer.getEffectiveDate(), "yyyy-MM-dd"));
				if(model.getAuditDate() != null) {				
					jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
				}
				mongo.update(params, jsonObject);
			}else {
				mongo.remove(params);
			}

		}
    	
        return num;
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
	public CustomerSalePriceDTO getListByPage(CustomerSalePriceDTO dto) throws SQLException {
		PageDataModel<CustomerSalePriceDTO> pageModel=mapper.getListByPage(dto);
        return (CustomerSalePriceDTO ) pageModel.getModel();
	}
	
	/**
	 * 弹框 客户列表
	 */
	@Override
	public CustomerSalePriceModel getCustomerByPage(CustomerSalePriceModel model) throws SQLException {
		PageDataModel<CustomerSalePriceModel> pageModel = mapper.getBrandByPage(model);
		return (CustomerSalePriceModel) pageModel.getModel();
	}
	/**
	 * 获取详情
	 */
	@Override
	public CustomerSalePriceModel getDetails(CustomerSalePriceModel model) throws SQLException {
		return mapper.getDetails(model);
	}
	
	/**
	 * 
	 */
	@Override
	public List<CustomerSalePriceModel> getCodeById(Long id) throws SQLException {
		return mapper.getCodeById(id);
	}
	@Override
	public int batchDelById(List<Long> ids) throws SQLException {
		return mapper.batchDelById(ids);
	}
	/**
	 * 根据 商家id和客户id和商品货号查询非本id下客户销售价格表信息
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<CustomerSalePriceModel> findByIDAndGoodsNo(CustomerSalePriceModel model) throws SQLException {
		return mapper.findByIDAndGoodsNo(model);
	}
	/**
	 * 根据id单个删除
	 */
	@Override
	public int delete(Long id) throws SQLException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerSalePriceId", id);
		mongo.remove(params);
		return mapper.del(id);
	}

	@Override
	public List<Map<String, Object>> getCountStatus(CustomerSalePriceDTO dtoSalePrice) {
		return mapper.getCountStatus(dtoSalePrice);
	}

	@Override
	public CustomerSalePriceDTO searchByDTO(CustomerSalePriceDTO customerSalePriceDTO) {
		return mapper.searchByDTO(customerSalePriceDTO);
	}

	/**
	 * 导出
	 */
	@Override
	public List<CustomerSalePriceDTO> getExportList(CustomerSalePriceDTO dto) throws SQLException {
		return mapper.getExportList(dto);
	}
	
	
}
