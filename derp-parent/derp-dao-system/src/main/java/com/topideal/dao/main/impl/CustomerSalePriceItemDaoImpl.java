package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerSalePriceItemDao;
import com.topideal.entity.vo.main.CustomerSalePriceItemModel;
import com.topideal.mapper.main.CustomerSalePriceItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * 客户销售价格表体 daoImpl
 * @author lchenxing
 */
@Repository
public class CustomerSalePriceItemDaoImpl implements CustomerSalePriceItemDao {

    @Autowired
    private CustomerSalePriceItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CustomerSalePriceItemModel> list(CustomerSalePriceItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CustomerSalePriceItemModel model) throws SQLException {
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
    public int modify(CustomerSalePriceItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CustomerSalePriceItemModel  searchByPage(CustomerSalePriceItemModel  model) throws SQLException{
        PageDataModel<CustomerSalePriceItemModel> pageModel=mapper.listByPage(model);
        return (CustomerSalePriceItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CustomerSalePriceItemModel  searchById(Long id)throws SQLException {
        CustomerSalePriceItemModel  model=new CustomerSalePriceItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CustomerSalePriceItemModel searchByModel(CustomerSalePriceItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 */
	@Override
	public void delBesidesIds(List<Long> itemIds, Long id) {
		 mapper.delBesidesIds(itemIds, id);
	}
	/**
	 * 列表查询
	 */
	@Override
	public CustomerSalePriceItemModel getListByPage(CustomerSalePriceItemModel model) throws SQLException {
		PageDataModel<CustomerSalePriceItemModel> pageModel=mapper.getListByPage(model);
        return (CustomerSalePriceItemModel ) pageModel.getModel();
	}
	/**
	 * 详情页
	 */
	@Override
	public CustomerSalePriceItemModel getDetails(CustomerSalePriceItemModel model) throws SQLException {
		return mapper.getDetails(model);
	}
	@Override
	public int deleteBySaleId(Long id) throws SQLException {
		return 0;
	}

}
