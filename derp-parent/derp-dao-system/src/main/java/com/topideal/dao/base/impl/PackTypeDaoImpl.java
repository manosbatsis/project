package com.topideal.dao.base.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.PackTypeDao;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.mapper.base.PackTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PackTypeDaoImpl implements PackTypeDao {

    @Autowired
    private PackTypeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PackTypeModel> list(PackTypeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PackTypeModel model) throws SQLException {
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
    public int modify(PackTypeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PackTypeModel  searchByPage(PackTypeModel  model) throws SQLException{
        PageDataModel<PackTypeModel> pageModel=mapper.listByPage(model);
        return (PackTypeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PackTypeModel  searchById(Long id)throws SQLException {
        PackTypeModel  model=new PackTypeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PackTypeModel searchByModel(PackTypeModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<PackTypeModel> listByLike(PackTypeModel packTypeModel) {
        return mapper.listByLike(packTypeModel);
    }
}
