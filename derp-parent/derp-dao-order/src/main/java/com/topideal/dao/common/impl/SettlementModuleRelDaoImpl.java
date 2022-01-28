package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SettlementModuleRelDao;
import com.topideal.entity.vo.common.SettlementModuleRelModel;
import com.topideal.mapper.common.SettlementModuleRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SettlementModuleRelDaoImpl implements SettlementModuleRelDao {

    @Autowired
    private SettlementModuleRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementModuleRelModel> list(SettlementModuleRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementModuleRelModel model) throws SQLException {
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
    public int modify(SettlementModuleRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementModuleRelModel  searchByPage(SettlementModuleRelModel  model) throws SQLException{
        PageDataModel<SettlementModuleRelModel> pageModel=mapper.listByPage(model);
        return (SettlementModuleRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementModuleRelModel  searchById(Long id)throws SQLException {
        SettlementModuleRelModel  model=new SettlementModuleRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementModuleRelModel searchByModel(SettlementModuleRelModel model) throws SQLException {
		return mapper.get(model);
	}

}