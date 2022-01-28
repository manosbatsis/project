package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PurchaseSdOrderDao;
import com.topideal.entity.vo.order.PurchaseSdOrderModel;
import com.topideal.mapper.order.PurchaseSdOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseSdOrderDaoImpl implements PurchaseSdOrderDao {

    @Autowired
    private PurchaseSdOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseSdOrderModel> list(PurchaseSdOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseSdOrderModel model) throws SQLException {
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
    public int modify(PurchaseSdOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseSdOrderModel  searchByPage(PurchaseSdOrderModel  model) throws SQLException{
        PageDataModel<PurchaseSdOrderModel> pageModel=mapper.listByPage(model);
        return (PurchaseSdOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseSdOrderModel  searchById(Long id)throws SQLException {
        PurchaseSdOrderModel  model=new PurchaseSdOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseSdOrderModel searchByModel(PurchaseSdOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取采购调整sd单	
	 */
	@Override
	public List<Map<String, Object>> getTzPurchaseSdOrde(Map<String, Object> paramMap) {
		return mapper.getTzPurchaseSdOrde(paramMap);
	}
	/**
	 *  获取sd数据
	 */
	@Override
	public List<Map<String, Object>> getSdOrderItemList(Map<String, Object> map) {
		return mapper.getSdOrderItemList(map);
	}
}