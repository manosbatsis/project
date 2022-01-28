package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseCorrelationDao;
import com.topideal.entity.vo.purchase.PurchaseCorrelationModel;
import com.topideal.mapper.purchase.PurchaseCorrelationMapper;

/**
 * @author lchenxing
 */
@Repository
public class PurchaseCorrelationDaoImpl implements PurchaseCorrelationDao {

    @Autowired
    private PurchaseCorrelationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseCorrelationModel> list(PurchaseCorrelationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseCorrelationModel model) throws SQLException {
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
    public int modify(PurchaseCorrelationModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseCorrelationModel  searchByPage(PurchaseCorrelationModel  model) throws SQLException{
        PageDataModel<PurchaseCorrelationModel> pageModel=mapper.listByPage(model);
        return (PurchaseCorrelationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseCorrelationModel  searchById(Long id)throws SQLException {
        PurchaseCorrelationModel  model=new PurchaseCorrelationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseCorrelationModel searchByModel(PurchaseCorrelationModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据入库单集合id获取数据
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseCorrelationModel> getDetailsByIds(List list) throws SQLException {
		return mapper.getDetailsByIds(list);
	}
    
}
