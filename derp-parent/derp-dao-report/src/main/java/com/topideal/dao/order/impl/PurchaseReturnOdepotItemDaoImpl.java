package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PurchaseReturnOdepotItemDao;
import com.topideal.entity.vo.order.PurchaseReturnOdepotItemModel;
import com.topideal.mapper.order.PurchaseReturnOdepotItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseReturnOdepotItemDaoImpl implements PurchaseReturnOdepotItemDao {

    @Autowired
    private PurchaseReturnOdepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseReturnOdepotItemModel> list(PurchaseReturnOdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseReturnOdepotItemModel model) throws SQLException {
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
    public int modify(PurchaseReturnOdepotItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseReturnOdepotItemModel  searchByPage(PurchaseReturnOdepotItemModel  model) throws SQLException{
        PageDataModel<PurchaseReturnOdepotItemModel> pageModel=mapper.listByPage(model);
        return (PurchaseReturnOdepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseReturnOdepotItemModel  searchById(Long id)throws SQLException {
        PurchaseReturnOdepotItemModel  model=new PurchaseReturnOdepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseReturnOdepotItemModel searchByModel(PurchaseReturnOdepotItemModel model) throws SQLException {
		return mapper.get(model);
	}

}