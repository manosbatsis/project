package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SettlementCustomerRelDao;
import com.topideal.entity.vo.common.SettlementCustomerRelModel;
import com.topideal.mapper.common.SettlementCustomerRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SettlementCustomerRelDaoImpl implements SettlementCustomerRelDao {

    @Autowired
    private SettlementCustomerRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementCustomerRelModel> list(SettlementCustomerRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementCustomerRelModel model) throws SQLException {
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
    public int modify(SettlementCustomerRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementCustomerRelModel  searchByPage(SettlementCustomerRelModel  model) throws SQLException{
        PageDataModel<SettlementCustomerRelModel> pageModel=mapper.listByPage(model);
        return (SettlementCustomerRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementCustomerRelModel  searchById(Long id)throws SQLException {
        SettlementCustomerRelModel  model=new SettlementCustomerRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementCustomerRelModel searchByModel(SettlementCustomerRelModel model) throws SQLException {
		return mapper.get(model);
	}
}