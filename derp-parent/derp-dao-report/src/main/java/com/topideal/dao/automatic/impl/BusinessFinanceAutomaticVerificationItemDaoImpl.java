package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationItemDao;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;
import com.topideal.mapper.automatic.BusinessFinanceAutomaticVerificationItemMapper;
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
public class BusinessFinanceAutomaticVerificationItemDaoImpl implements BusinessFinanceAutomaticVerificationItemDao {

    @Autowired
    private BusinessFinanceAutomaticVerificationItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BusinessFinanceAutomaticVerificationItemModel> list(BusinessFinanceAutomaticVerificationItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BusinessFinanceAutomaticVerificationItemModel model) throws SQLException {
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
    public int modify(BusinessFinanceAutomaticVerificationItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BusinessFinanceAutomaticVerificationItemModel  searchByPage(BusinessFinanceAutomaticVerificationItemModel  model) throws SQLException{
        PageDataModel<BusinessFinanceAutomaticVerificationItemModel> pageModel=mapper.listByPage(model);
        return (BusinessFinanceAutomaticVerificationItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BusinessFinanceAutomaticVerificationItemModel  searchById(Long id)throws SQLException {
        BusinessFinanceAutomaticVerificationItemModel  model=new BusinessFinanceAutomaticVerificationItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BusinessFinanceAutomaticVerificationItemModel searchByModel(BusinessFinanceAutomaticVerificationItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public void deleteByMap(Map<String, Object> params) {
        mapper.deleteByMap(params);
    }

    @Override
    public List<BusinessFinanceAutomaticVerificationItemModel> listForExport(BusinessFinanceAutomaticVerificationItemModel model) {
        return mapper.listForExport(model);
    }
}