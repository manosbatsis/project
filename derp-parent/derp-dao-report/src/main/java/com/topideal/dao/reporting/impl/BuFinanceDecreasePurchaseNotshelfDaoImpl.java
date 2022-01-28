package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceDecreasePurchaseNotshelfDao;
import com.topideal.entity.vo.reporting.BuFinanceDecreasePurchaseNotshelfModel;
import com.topideal.mapper.reporting.BuFinanceDecreasePurchaseNotshelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceDecreasePurchaseNotshelfDaoImpl implements BuFinanceDecreasePurchaseNotshelfDao {

    @Autowired
    private BuFinanceDecreasePurchaseNotshelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceDecreasePurchaseNotshelfModel> list(BuFinanceDecreasePurchaseNotshelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceDecreasePurchaseNotshelfModel model) throws SQLException {
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
    public int modify(BuFinanceDecreasePurchaseNotshelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceDecreasePurchaseNotshelfModel  searchByPage(BuFinanceDecreasePurchaseNotshelfModel  model) throws SQLException{
        PageDataModel<BuFinanceDecreasePurchaseNotshelfModel> pageModel=mapper.listByPage(model);
        return (BuFinanceDecreasePurchaseNotshelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceDecreasePurchaseNotshelfModel  searchById(Long id)throws SQLException {
        BuFinanceDecreasePurchaseNotshelfModel  model=new BuFinanceDecreasePurchaseNotshelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceDecreasePurchaseNotshelfModel searchByModel(BuFinanceDecreasePurchaseNotshelfModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清除商家事业部 仓库 月份 (财务经销存)本期减少采购在途表
	 */
	@Override
	public int delBuFinanceDecreasePurchaseNotshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceDecreasePurchaseNotshelf(map);
	}
	@Override
	public List<BuFinanceDecreasePurchaseNotshelfModel> getDecreasePurchaseNotshelfExport(
			BuFinanceDecreasePurchaseNotshelfModel model) {
		return mapper.getDecreasePurchaseNotshelfExport(model);
	}

}