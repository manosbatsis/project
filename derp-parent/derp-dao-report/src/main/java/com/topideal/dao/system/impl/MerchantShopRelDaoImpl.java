package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.MerchantShopRelDao;
import com.topideal.entity.vo.system.MerchantShopRelModel;
import com.topideal.mapper.system.MerchantShopRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MerchantShopRelDaoImpl implements MerchantShopRelDao {

    @Autowired
    private MerchantShopRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantShopRelModel> list(MerchantShopRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantShopRelModel model) throws SQLException {
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
    public int modify(MerchantShopRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchantShopRelModel  searchByPage(MerchantShopRelModel  model) throws SQLException{
        PageDataModel<MerchantShopRelModel> pageModel=mapper.listByPage(model);
        return (MerchantShopRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchantShopRelModel  searchById(Long id)throws SQLException {
        MerchantShopRelModel  model=new MerchantShopRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantShopRelModel searchByModel(MerchantShopRelModel model) throws SQLException {
		return mapper.get(model);
	}

}