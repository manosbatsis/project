package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinancePurchaseNotshelfDao;
import com.topideal.entity.vo.reporting.BuFinancePurchaseNotshelfModel;
import com.topideal.mapper.reporting.BuFinancePurchaseNotshelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinancePurchaseNotshelfDaoImpl implements BuFinancePurchaseNotshelfDao {

    @Autowired
    private BuFinancePurchaseNotshelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinancePurchaseNotshelfModel> list(BuFinancePurchaseNotshelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinancePurchaseNotshelfModel model) throws SQLException {
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
    public int modify(BuFinancePurchaseNotshelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinancePurchaseNotshelfModel  searchByPage(BuFinancePurchaseNotshelfModel  model) throws SQLException{
        PageDataModel<BuFinancePurchaseNotshelfModel> pageModel=mapper.listByPage(model);
        return (BuFinancePurchaseNotshelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinancePurchaseNotshelfModel  searchById(Long id)throws SQLException {
        BuFinancePurchaseNotshelfModel  model=new BuFinancePurchaseNotshelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinancePurchaseNotshelfModel searchByModel(BuFinancePurchaseNotshelfModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 *  清除事业部商家 仓库 月份 (财务经销存)采购在途数据
	 */
	@Override
	public int delBuFinancePurchaseNotshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinancePurchaseNotshelf(map);
	}
	

}