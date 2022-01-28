package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.mapper.system.ExchangeRateMapper;

import javax.swing.text.TabableView;

/**
 * 汇率管理表 daoImpl
 * @author lian_
 */
@Repository
public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Autowired
    private ExchangeRateMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ExchangeRateModel> list(ExchangeRateModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ExchangeRateModel model) throws SQLException {
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
    public int modify(ExchangeRateModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ExchangeRateModel  searchByPage(ExchangeRateModel  model) throws SQLException{
        PageDataModel<ExchangeRateModel> pageModel=mapper.listByPage(model);
        return (ExchangeRateModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ExchangeRateModel  searchById(Long id)throws SQLException {
        ExchangeRateModel  model=new ExchangeRateModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ExchangeRateModel searchByModel(ExchangeRateModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据对象查询汇率表
	 */
	@Override
	public ExchangeRateModel getByDateOrigCurrency(ExchangeRateModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getByDateOrigCurrency(model);
	}
	/**
	 *  获取最近 小于账单日期 汇率
	 */
	@Override
	public List<ExchangeRateModel> getRecentExchangeRateList(ExchangeRateModel model) throws SQLException {
		return mapper.getRecentExchangeRateList(model);
	}

	@Override
	public List<ExchangeRateModel> getRecentRateList(ExchangeRateModel model) throws SQLException {
		return mapper.getRecentRateList(model);
	}
	@Override
	public List<ExchangeRateModel> getNullRateList() {
		return mapper.getNullRateList();
	}

	@Override
	public ExchangeRateModel getLatestRate(String month, String origCurrencyCode, String tgtCurrencyCode) {
		return mapper.getLatestRate(month, origCurrencyCode, tgtCurrencyCode);
	}

}