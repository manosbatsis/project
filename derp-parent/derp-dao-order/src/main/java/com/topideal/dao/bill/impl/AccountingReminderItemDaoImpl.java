package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AccountingReminderItemDao;
import com.topideal.entity.vo.bill.AccountingReminderItemModel;
import com.topideal.mapper.bill.AccountingReminderItemMapper;
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
public class AccountingReminderItemDaoImpl implements AccountingReminderItemDao {

    @Autowired
    private AccountingReminderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AccountingReminderItemModel> list(AccountingReminderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AccountingReminderItemModel model) throws SQLException {
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
    public int modify(AccountingReminderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AccountingReminderItemModel  searchByPage(AccountingReminderItemModel  model) throws SQLException{
        PageDataModel<AccountingReminderItemModel> pageModel=mapper.listByPage(model);
        return (AccountingReminderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AccountingReminderItemModel  searchById(Long id)throws SQLException {
        AccountingReminderItemModel  model=new AccountingReminderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AccountingReminderItemModel searchByModel(AccountingReminderItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer getByBuIdCustomerId(Long buId,Long customerId,Long merchantId) {
        return mapper.getByBuIdCustomerId(buId,customerId,merchantId);
    }
}