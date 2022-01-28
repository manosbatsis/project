package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.AgreementCurrencyConfigItemDao;
import com.topideal.entity.vo.sale.AgreementCurrencyConfigItemModel;
import com.topideal.mapper.sale.AgreementCurrencyConfigItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AgreementCurrencyConfigItemDaoImpl implements AgreementCurrencyConfigItemDao {

    @Autowired
    private AgreementCurrencyConfigItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AgreementCurrencyConfigItemModel> list(AgreementCurrencyConfigItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AgreementCurrencyConfigItemModel model) throws SQLException {
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
    public int modify(AgreementCurrencyConfigItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AgreementCurrencyConfigItemModel  searchByPage(AgreementCurrencyConfigItemModel  model) throws SQLException{
        PageDataModel<AgreementCurrencyConfigItemModel> pageModel=mapper.listByPage(model);
        return (AgreementCurrencyConfigItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AgreementCurrencyConfigItemModel  searchById(Long id)throws SQLException {
        AgreementCurrencyConfigItemModel  model=new AgreementCurrencyConfigItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AgreementCurrencyConfigItemModel searchByModel(AgreementCurrencyConfigItemModel model) throws SQLException {
		return mapper.get(model);
	}

}