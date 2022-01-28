package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CustomerQuotaConfigDao;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;
import com.topideal.mapper.sale.CustomerQuotaConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CustomerQuotaConfigDaoImpl implements CustomerQuotaConfigDao {

    @Autowired
    private CustomerQuotaConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerQuotaConfigModel> list(CustomerQuotaConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomerQuotaConfigModel model) throws SQLException {
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
    public int modify(CustomerQuotaConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerQuotaConfigModel  searchByPage(CustomerQuotaConfigModel  model) throws SQLException{
        PageDataModel<CustomerQuotaConfigModel> pageModel=mapper.listByPage(model);
        return (CustomerQuotaConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerQuotaConfigModel  searchById(Long id)throws SQLException {
        CustomerQuotaConfigModel  model=new CustomerQuotaConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
  /**
 	* 根据商家实体类查询商品
 	* @param model
 	* */
	@Override
	public CustomerQuotaConfigModel searchByModel(CustomerQuotaConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 获取分页信息
	 */
	@Override
	public CustomerQuotaConfigDTO listDTOByPage(CustomerQuotaConfigDTO dto) throws SQLException {
		PageDataModel<CustomerQuotaConfigDTO> pageModel = mapper.listDTOByPage(dto);
		return (CustomerQuotaConfigDTO)pageModel.getModel();
	}
	
	/**
	 * 查询生效范围内的所有信息
	 */
	@Override
	public List<CustomerQuotaConfigDTO> listEffectiveDTO(Map<String,Object> map) throws SQLException {
		return mapper.listEffectiveDTO(map);
	}
}