package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.TmallscmPurchaseOrderDao;
import com.topideal.entity.vo.sale.TmallscmPurchaseOrderModel;
import com.topideal.mapper.sale.TmallscmPurchaseOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TmallscmPurchaseOrderDaoImpl implements TmallscmPurchaseOrderDao {

    @Autowired
    private TmallscmPurchaseOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TmallscmPurchaseOrderModel> list(TmallscmPurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TmallscmPurchaseOrderModel model) throws SQLException {
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
    public int modify(TmallscmPurchaseOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TmallscmPurchaseOrderModel  searchByPage(TmallscmPurchaseOrderModel  model) throws SQLException{
        PageDataModel<TmallscmPurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (TmallscmPurchaseOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TmallscmPurchaseOrderModel  searchById(Long id)throws SQLException {
        TmallscmPurchaseOrderModel  model=new TmallscmPurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TmallscmPurchaseOrderModel searchByModel(TmallscmPurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 天猫平台采购订单数据
	 */
	@Override
	public List<TmallscmPurchaseOrderModel> getTmallPlatformPurchaseList(Map<String, Object> paramMap) throws Exception {
		return mapper.getTmallPlatformPurchaseList(paramMap);
	}

}