package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PurchaseSdOrderSditemDao;
import com.topideal.entity.vo.order.PurchaseSdOrderSditemModel;
import com.topideal.mapper.order.PurchaseSdOrderSditemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseSdOrderSditemDaoImpl implements PurchaseSdOrderSditemDao {

    @Autowired
    private PurchaseSdOrderSditemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseSdOrderSditemModel> list(PurchaseSdOrderSditemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseSdOrderSditemModel model) throws SQLException {
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
    public int modify(PurchaseSdOrderSditemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseSdOrderSditemModel  searchByPage(PurchaseSdOrderSditemModel  model) throws SQLException{
        PageDataModel<PurchaseSdOrderSditemModel> pageModel=mapper.listByPage(model);
        return (PurchaseSdOrderSditemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseSdOrderSditemModel  searchById(Long id)throws SQLException {
        PurchaseSdOrderSditemModel  model=new PurchaseSdOrderSditemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseSdOrderSditemModel searchByModel(PurchaseSdOrderSditemModel model) throws SQLException {
		return mapper.get(model);
	}
}