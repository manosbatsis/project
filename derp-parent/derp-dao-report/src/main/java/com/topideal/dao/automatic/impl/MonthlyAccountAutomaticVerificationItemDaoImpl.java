package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationItemDao;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.mapper.automatic.MonthlyAccountAutomaticVerificationItemMapper;
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
public class MonthlyAccountAutomaticVerificationItemDaoImpl implements MonthlyAccountAutomaticVerificationItemDao {

    @Autowired
    private MonthlyAccountAutomaticVerificationItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MonthlyAccountAutomaticVerificationItemModel> list(MonthlyAccountAutomaticVerificationItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MonthlyAccountAutomaticVerificationItemModel model) throws SQLException {
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
    public int modify(MonthlyAccountAutomaticVerificationItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MonthlyAccountAutomaticVerificationItemModel  searchByPage(MonthlyAccountAutomaticVerificationItemModel  model) throws SQLException{
        PageDataModel<MonthlyAccountAutomaticVerificationItemModel> pageModel=mapper.listByPage(model);
        return (MonthlyAccountAutomaticVerificationItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MonthlyAccountAutomaticVerificationItemModel  searchById(Long id)throws SQLException {
        MonthlyAccountAutomaticVerificationItemModel  model=new MonthlyAccountAutomaticVerificationItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MonthlyAccountAutomaticVerificationItemModel searchByModel(MonthlyAccountAutomaticVerificationItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int deleteByMap(Map<String, Object> params) {
		return mapper.deleteByMap(params);
	}
}