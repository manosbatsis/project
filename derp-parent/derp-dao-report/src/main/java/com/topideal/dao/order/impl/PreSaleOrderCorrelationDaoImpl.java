package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PreSaleOrderCorrelationDao;
import com.topideal.entity.vo.order.PreSaleOrderCorrelationModel;
import com.topideal.mapper.order.PreSaleOrderCorrelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PreSaleOrderCorrelationDaoImpl implements PreSaleOrderCorrelationDao {

    @Autowired
    private PreSaleOrderCorrelationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PreSaleOrderCorrelationModel> list(PreSaleOrderCorrelationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PreSaleOrderCorrelationModel model) throws SQLException {
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
    public int modify(PreSaleOrderCorrelationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PreSaleOrderCorrelationModel  searchByPage(PreSaleOrderCorrelationModel  model) throws SQLException{
        PageDataModel<PreSaleOrderCorrelationModel> pageModel=mapper.listByPage(model);
        return (PreSaleOrderCorrelationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PreSaleOrderCorrelationModel  searchById(Long id)throws SQLException {
        PreSaleOrderCorrelationModel  model=new PreSaleOrderCorrelationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PreSaleOrderCorrelationModel searchByModel(PreSaleOrderCorrelationModel model) throws SQLException {
		return mapper.get(model);
	}
	
}