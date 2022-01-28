package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationDao;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationModel;
import com.topideal.mapper.automatic.BusinessFinanceAutomaticVerificationMapper;
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
public class BusinessFinanceAutomaticVerificationDaoImpl implements BusinessFinanceAutomaticVerificationDao {

    @Autowired
    private BusinessFinanceAutomaticVerificationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BusinessFinanceAutomaticVerificationModel> list(BusinessFinanceAutomaticVerificationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BusinessFinanceAutomaticVerificationModel model) throws SQLException {
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
    public int modify(BusinessFinanceAutomaticVerificationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BusinessFinanceAutomaticVerificationModel  searchByPage(BusinessFinanceAutomaticVerificationModel  model) throws SQLException{
        PageDataModel<BusinessFinanceAutomaticVerificationModel> pageModel=mapper.listByPage(model);
        return (BusinessFinanceAutomaticVerificationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BusinessFinanceAutomaticVerificationModel  searchById(Long id)throws SQLException {
        BusinessFinanceAutomaticVerificationModel  model=new BusinessFinanceAutomaticVerificationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BusinessFinanceAutomaticVerificationModel searchByModel(BusinessFinanceAutomaticVerificationModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BusinessFinanceAutomaticVerificationDTO listAutomaticVeriByPage(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException {
        PageDataModel<BusinessFinanceAutomaticVerificationDTO> pageModel = mapper.listAutomaticVeriByPage(dto);
        return (BusinessFinanceAutomaticVerificationDTO) pageModel.getModel();
    }

    @Override
    public void deleteByMap(Map<String, Object> params) {
        mapper.deleteByMap(params);
    }

    @Override
    public void updateAutomaticVeri(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException {
        mapper.updateAutomaticVeri(dto);
    }

}