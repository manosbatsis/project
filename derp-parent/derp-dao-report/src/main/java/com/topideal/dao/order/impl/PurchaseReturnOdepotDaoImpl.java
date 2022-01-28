package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PurchaseReturnOdepotDao;
import com.topideal.entity.vo.order.PurchaseReturnOdepotModel;
import com.topideal.mapper.order.PurchaseReturnOdepotMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseReturnOdepotDaoImpl implements PurchaseReturnOdepotDao {

    @Autowired
    private PurchaseReturnOdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseReturnOdepotModel> list(PurchaseReturnOdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseReturnOdepotModel model) throws SQLException {
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
    public int modify(PurchaseReturnOdepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseReturnOdepotModel  searchByPage(PurchaseReturnOdepotModel  model) throws SQLException{
        PageDataModel<PurchaseReturnOdepotModel> pageModel=mapper.listByPage(model);
        return (PurchaseReturnOdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseReturnOdepotModel  searchById(Long id)throws SQLException {
        PurchaseReturnOdepotModel  model=new PurchaseReturnOdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseReturnOdepotModel searchByModel(PurchaseReturnOdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 新财务报表-查询采购退货出库数量
	 */

	/**
	 * 事业部新财务报表-查询采购退货出库数量
	 */
	@Override
	public List<Map<String, Object>> getBuPurchaseReturnOdepotNum(Map<String, Object> paramMap) {
		return mapper.getBuPurchaseReturnOdepotNum(paramMap);
	}

	/**
	 *  新财务报表-查询采购退货出库明细
	 */
	@Override
	public List<Map<String, Object>> getBuPurchaseReturnOdepot(Map<String, Object> paramMap) {
		return mapper.getBuPurchaseReturnOdepot(paramMap);
	}

}