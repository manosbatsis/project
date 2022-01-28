package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleReturnDeclareOrderItemDao;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderItemModel;
import com.topideal.mapper.sale.SaleReturnDeclareOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleReturnDeclareOrderItemDaoImpl implements SaleReturnDeclareOrderItemDao {

    @Autowired
    private SaleReturnDeclareOrderItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnDeclareOrderItemModel> list(SaleReturnDeclareOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleReturnDeclareOrderItemModel model) throws SQLException {
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
    public int modify(SaleReturnDeclareOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleReturnDeclareOrderItemModel  searchByPage(SaleReturnDeclareOrderItemModel  model) throws SQLException{
        PageDataModel<SaleReturnDeclareOrderItemModel> pageModel=mapper.listByPage(model);
        return (SaleReturnDeclareOrderItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleReturnDeclareOrderItemModel  searchById(Long id)throws SQLException {
        SaleReturnDeclareOrderItemModel  model=new SaleReturnDeclareOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleReturnDeclareOrderItemModel searchByModel(SaleReturnDeclareOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

}