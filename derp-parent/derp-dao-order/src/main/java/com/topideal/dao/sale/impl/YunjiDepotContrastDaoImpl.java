package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.YunjiDepotContrastDao;
import com.topideal.entity.vo.sale.YunjiDepotContrastModel;
import com.topideal.mapper.sale.YunjiDepotContrastMapper;

/**
 * 云集仓库对照表 daoImpl
 * @author lian_
 */
@Repository
public class YunjiDepotContrastDaoImpl implements YunjiDepotContrastDao {

    @Autowired
    private YunjiDepotContrastMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiDepotContrastModel> list(YunjiDepotContrastModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiDepotContrastModel> listDesc(YunjiDepotContrastModel model) throws SQLException {
		return mapper.listDesc(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiDepotContrastModel model) throws SQLException {
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
    public int modify(YunjiDepotContrastModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiDepotContrastModel  searchByPage(YunjiDepotContrastModel  model) throws SQLException{
        PageDataModel<YunjiDepotContrastModel> pageModel=mapper.listByPage(model);
        return (YunjiDepotContrastModel ) pageModel.getModel();
    }
    /**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiDepotContrastModel  searchGetByPage(YunjiDepotContrastModel  model) throws SQLException{
    	PageDataModel<YunjiDepotContrastModel> pageModel=mapper.getListByPage(model);
    	return (YunjiDepotContrastModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiDepotContrastModel  searchById(Long id)throws SQLException {
        YunjiDepotContrastModel  model=new YunjiDepotContrastModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiDepotContrastModel searchByModel(YunjiDepotContrastModel model) throws SQLException {
		return mapper.get(model);
	}
}