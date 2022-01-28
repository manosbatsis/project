package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.TakesStockItemDao;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.mapper.TakesStockItemMapper;

/**盘点详情表
 * @author lchenxing
 */
@Repository
public class TakesStockItemDaoImpl implements TakesStockItemDao {

    @Autowired
    private TakesStockItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TakesStockItemModel> list(TakesStockItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TakesStockItemModel model) throws SQLException {
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
    public int modify(TakesStockItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TakesStockItemModel  searchByPage(TakesStockItemModel  model) throws SQLException{
        PageDataModel<TakesStockItemModel> pageModel=mapper.listByPage(model);
        return (TakesStockItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TakesStockItemModel  searchById(Long id)throws SQLException {
        TakesStockItemModel  model=new TakesStockItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TakesStockItemModel searchByModel(TakesStockItemModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据盘点申请id删除详情
	 * */
    public void delByTakesStockId(Long takesStockId){
    	mapper.delByTakesStockId(takesStockId);
    }
    
    /**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}
}
