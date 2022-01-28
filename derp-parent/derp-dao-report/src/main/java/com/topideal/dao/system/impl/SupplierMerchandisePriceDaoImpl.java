package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.SupplierMerchandisePriceDao;
import com.topideal.entity.vo.system.SupplierMerchandisePriceModel;
import com.topideal.mapper.system.SupplierMerchandisePriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SupplierMerchandisePriceDaoImpl implements SupplierMerchandisePriceDao {

    @Autowired
    private SupplierMerchandisePriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SupplierMerchandisePriceModel> list(SupplierMerchandisePriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SupplierMerchandisePriceModel model) throws SQLException {
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
    public int modify(SupplierMerchandisePriceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SupplierMerchandisePriceModel  searchByPage(SupplierMerchandisePriceModel  model) throws SQLException{
        PageDataModel<SupplierMerchandisePriceModel> pageModel=mapper.listByPage(model);
        return (SupplierMerchandisePriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SupplierMerchandisePriceModel  searchById(Long id)throws SQLException {
        SupplierMerchandisePriceModel  model=new SupplierMerchandisePriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SupplierMerchandisePriceModel searchByModel(SupplierMerchandisePriceModel model) throws SQLException {
		return mapper.get(model);
	}

}