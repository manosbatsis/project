package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleCreditBillOrderItemDao;
import com.topideal.entity.vo.sale.SaleCreditBillOrderItemModel;
import com.topideal.mapper.sale.SaleCreditBillOrderItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleCreditBillOrderItemDaoImpl implements SaleCreditBillOrderItemDao {

    @Autowired
    private SaleCreditBillOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleCreditBillOrderItemModel> list(SaleCreditBillOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleCreditBillOrderItemModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(SaleCreditBillOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleCreditBillOrderItemModel  searchByPage(SaleCreditBillOrderItemModel  model) throws SQLException{
        PageDataModel<SaleCreditBillOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleCreditBillOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleCreditBillOrderItemModel  searchById(Long id)throws SQLException {
        SaleCreditBillOrderItemModel  model=new SaleCreditBillOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleCreditBillOrderItemModel searchByModel(SaleCreditBillOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public Integer getRedempNum(Map<String, Object> map) throws SQLException {
		return mapper.getRedempNum(map);
	}
}