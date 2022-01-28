package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.SaleShelfIdepotItemDao;
import com.topideal.entity.vo.order.SaleShelfIdepotItemModel;
import com.topideal.mapper.order.SaleShelfIdepotItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleShelfIdepotItemDaoImpl implements SaleShelfIdepotItemDao {

    @Autowired
    private SaleShelfIdepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleShelfIdepotItemModel> list(SaleShelfIdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleShelfIdepotItemModel model) throws SQLException {
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
    public int modify(SaleShelfIdepotItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleShelfIdepotItemModel  searchByPage(SaleShelfIdepotItemModel  model) throws SQLException{
        PageDataModel<SaleShelfIdepotItemModel> pageModel=mapper.listByPage(model);
        return (SaleShelfIdepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleShelfIdepotItemModel  searchById(Long id)throws SQLException {
        SaleShelfIdepotItemModel  model=new SaleShelfIdepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleShelfIdepotItemModel searchByModel(SaleShelfIdepotItemModel model) throws SQLException {
		return mapper.get(model);
	}

}