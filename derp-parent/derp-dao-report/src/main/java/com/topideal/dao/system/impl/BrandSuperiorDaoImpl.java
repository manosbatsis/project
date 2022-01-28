package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.BrandSuperiorDao;
import com.topideal.entity.vo.system.BrandSuperiorModel;
import com.topideal.mapper.system.BrandSuperiorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BrandSuperiorDaoImpl implements BrandSuperiorDao {

    @Autowired
    private BrandSuperiorMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BrandSuperiorModel> list(BrandSuperiorModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BrandSuperiorModel model) throws SQLException {
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
    public int modify(BrandSuperiorModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BrandSuperiorModel  searchByPage(BrandSuperiorModel  model) throws SQLException{
        PageDataModel<BrandSuperiorModel> pageModel=mapper.listByPage(model);
        return (BrandSuperiorModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BrandSuperiorModel  searchById(Long id)throws SQLException {
        BrandSuperiorModel  model=new BrandSuperiorModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BrandSuperiorModel searchByModel(BrandSuperiorModel model) throws SQLException {
		return mapper.get(model);
	}

}