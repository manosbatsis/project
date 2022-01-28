package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceSdAddPurchaseNotshelfDao;
import com.topideal.entity.vo.reporting.BuFinanceSdAddPurchaseNotshelfModel;
import com.topideal.mapper.reporting.BuFinanceSdAddPurchaseNotshelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceSdAddPurchaseNotshelfDaoImpl implements BuFinanceSdAddPurchaseNotshelfDao {

    @Autowired
    private BuFinanceSdAddPurchaseNotshelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceSdAddPurchaseNotshelfModel> list(BuFinanceSdAddPurchaseNotshelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceSdAddPurchaseNotshelfModel model) throws SQLException {
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
    public int modify(BuFinanceSdAddPurchaseNotshelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceSdAddPurchaseNotshelfModel  searchByPage(BuFinanceSdAddPurchaseNotshelfModel  model) throws SQLException{
        PageDataModel<BuFinanceSdAddPurchaseNotshelfModel> pageModel=mapper.listByPage(model);
        return (BuFinanceSdAddPurchaseNotshelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceSdAddPurchaseNotshelfModel  searchById(Long id)throws SQLException {
        BuFinanceSdAddPurchaseNotshelfModel  model=new BuFinanceSdAddPurchaseNotshelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceSdAddPurchaseNotshelfModel searchByModel(BuFinanceSdAddPurchaseNotshelfModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计SD采购在途明细
	 */
	@Override
	public int delBuFinanceSdAddPurchaseNotshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceSdAddPurchaseNotshelf(map);
	}
	@Override
	public List<Map<String, Object>> getBuFinanceSdAddPurchaseNotshelf(Map<String, Object> paramMap) throws SQLException {
		return mapper.getBuFinanceSdAddPurchaseNotshelf(paramMap);
	}
	@Override
	public List<Map<String, Object>> getBuOrderGoodsAmontName(Map<String, Object> paramMap) throws SQLException {
		return mapper.getBuOrderGoodsAmontName(paramMap);
	}
	@Override
	public List<Map<String, Object>> getBuOrderGoodsAmont(Map<String, Object> paramMap) throws SQLException {
		return mapper.getBuOrderGoodsAmont(paramMap);
	}

}