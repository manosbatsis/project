package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchantBrandRelDao;
import com.topideal.entity.vo.main.MerchantBrandRelModel;
import com.topideal.mapper.main.MerchantBrandRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * 商家品牌关联表 daoImpl
 * @author lchenxing
 */
@Repository
public class MerchantBrandRelDaoImpl implements MerchantBrandRelDao {

    @Autowired
    private MerchantBrandRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantBrandRelModel> list(MerchantBrandRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantBrandRelModel model) throws SQLException {
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
    public int modify(MerchantBrandRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchantBrandRelModel  searchByPage(MerchantBrandRelModel  model) throws SQLException{
        PageDataModel<MerchantBrandRelModel> pageModel=mapper.listByPage(model);
        return (MerchantBrandRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchantBrandRelModel  searchById(Long id)throws SQLException {
        MerchantBrandRelModel  model=new MerchantBrandRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantBrandRelModel searchByModel(MerchantBrandRelModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
