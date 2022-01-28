package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinancePurchaseDamagedDao;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;
import com.topideal.mapper.reporting.BuFinancePurchaseDamagedMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinancePurchaseDamagedDaoImpl implements BuFinancePurchaseDamagedDao {

    @Autowired
    private BuFinancePurchaseDamagedMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinancePurchaseDamagedModel> list(BuFinancePurchaseDamagedModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinancePurchaseDamagedModel model) throws SQLException {
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
    public int modify(BuFinancePurchaseDamagedModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinancePurchaseDamagedModel  searchByPage(BuFinancePurchaseDamagedModel  model) throws SQLException{
        PageDataModel<BuFinancePurchaseDamagedModel> pageModel=mapper.listByPage(model);
        return (BuFinancePurchaseDamagedModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinancePurchaseDamagedModel  searchById(Long id)throws SQLException {
        BuFinancePurchaseDamagedModel  model=new BuFinancePurchaseDamagedModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinancePurchaseDamagedModel searchByModel(BuFinancePurchaseDamagedModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购残损明细表
	 */
	@Override
	public int delBuFinancePurchaseDamaged(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinancePurchaseDamaged(map);
	}
	@Override
	public List<BuFinancePurchaseDamagedModel> getPurchaseDamagedExport(Map<String, Object> map)throws SQLException {
		return mapper.getPurchaseDamagedExport(map);
	}

}