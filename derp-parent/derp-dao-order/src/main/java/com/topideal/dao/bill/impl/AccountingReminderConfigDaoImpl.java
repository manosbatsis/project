package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AccountingReminderConfigDao;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;
import com.topideal.entity.vo.bill.AccountingReminderConfigModel;
import com.topideal.mapper.bill.AccountingReminderConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AccountingReminderConfigDaoImpl implements AccountingReminderConfigDao {

    @Autowired
    private AccountingReminderConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AccountingReminderConfigModel> list(AccountingReminderConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AccountingReminderConfigModel model) throws SQLException {
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
    public int modify(AccountingReminderConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AccountingReminderConfigModel  searchByPage(AccountingReminderConfigModel  model) throws SQLException{
        PageDataModel<AccountingReminderConfigModel> pageModel=mapper.listByPage(model);
        return (AccountingReminderConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AccountingReminderConfigModel  searchById(Long id)throws SQLException {
        AccountingReminderConfigModel  model=new AccountingReminderConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AccountingReminderConfigModel searchByModel(AccountingReminderConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public AccountingReminderConfigDTO getListByPage(AccountingReminderConfigDTO dto) {

        PageDataModel<AccountingReminderConfigDTO> pageModel=mapper.getListByPage(dto);
        return (AccountingReminderConfigDTO ) pageModel.getModel();

    }

    @Override
    public AccountingReminderConfigModel getLatestAuditDetail(AccountingReminderConfigModel model) {
        return mapper.getLatestAuditDetail(model);
    }
}