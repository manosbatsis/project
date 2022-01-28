package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseWarehouseBatchDao;
import com.topideal.entity.vo.order.PurchaseWarehouseBatchModel;
import com.topideal.mapper.order.PurchaseWarehouseBatchMapper;

/**
 * 采购入库单商品批次详情 daoImpl
 * @author lian_
 */
@Repository
public class PurchaseWarehouseBatchDaoImpl implements PurchaseWarehouseBatchDao {

    @Autowired
    private PurchaseWarehouseBatchMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> list(PurchaseWarehouseBatchModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseWarehouseBatchModel model) throws SQLException {
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
    public int modify(PurchaseWarehouseBatchModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseWarehouseBatchModel  searchByPage(PurchaseWarehouseBatchModel  model) throws SQLException{
        PageDataModel<PurchaseWarehouseBatchModel> pageModel=mapper.listByPage(model);
        return (PurchaseWarehouseBatchModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseWarehouseBatchModel  searchById(Long id)throws SQLException {
        PurchaseWarehouseBatchModel  model=new PurchaseWarehouseBatchModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseWarehouseBatchModel searchByModel(PurchaseWarehouseBatchModel model) throws SQLException {
		return mapper.get(model);
	}
}