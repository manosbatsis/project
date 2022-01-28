package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.BrandParentDao;
import com.topideal.entity.vo.system.BrandParentModel;
import com.topideal.mapper.system.BrandParentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BrandParentDaoImpl implements BrandParentDao {

    @Autowired
    private BrandParentMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BrandParentModel> list(BrandParentModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BrandParentModel model) throws SQLException {
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
    public int modify(BrandParentModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BrandParentModel  searchByPage(BrandParentModel  model) throws SQLException{
        PageDataModel<BrandParentModel> pageModel=mapper.listByPage(model);
        return (BrandParentModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BrandParentModel  searchById(Long id)throws SQLException {
        BrandParentModel  model=new BrandParentModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BrandParentModel searchByModel(BrandParentModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BrandParentModel getBrandParentByGoodsId(Long goodsId) {
        return mapper.getBrandParentByGoodsId(goodsId);
    }
}