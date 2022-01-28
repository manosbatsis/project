package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.TransferOrderItemDao;
import com.topideal.entity.vo.order.TransferOrderItemModel;
import com.topideal.mapper.order.TransferOrderItemMapper;

/**
 * 调拨订单表体 daoImpl
 * @author lchenxing
 */
@Repository
public class TransferOrderItemDaoImpl implements TransferOrderItemDao {

    @Autowired
    private TransferOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TransferOrderItemModel> list(TransferOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TransferOrderItemModel model) throws SQLException {
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
    public int modify(TransferOrderItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TransferOrderItemModel  searchByPage(TransferOrderItemModel  model) throws SQLException{
        PageDataModel<TransferOrderItemModel> pageModel=mapper.listByPage(model);
        return (TransferOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TransferOrderItemModel  searchById(Long id)throws SQLException {
        TransferOrderItemModel  model=new TransferOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TransferOrderItemModel searchByModel(TransferOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

}
