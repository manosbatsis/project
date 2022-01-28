package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PreSaleOrderDao;
import com.topideal.entity.vo.order.PreSaleOrderModel;
import com.topideal.mapper.order.PreSaleOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PreSaleOrderDaoImpl implements PreSaleOrderDao {

    @Autowired
    private PreSaleOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PreSaleOrderModel> list(PreSaleOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PreSaleOrderModel model) throws SQLException {
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
    public int modify(PreSaleOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PreSaleOrderModel  searchByPage(PreSaleOrderModel  model) throws SQLException{
        PageDataModel<PreSaleOrderModel> pageModel=mapper.listByPage(model);
        return (PreSaleOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PreSaleOrderModel  searchById(Long id)throws SQLException {
        PreSaleOrderModel  model=new PreSaleOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PreSaleOrderModel searchByModel(PreSaleOrderModel model) throws SQLException {
		return mapper.get(model);
	}
}