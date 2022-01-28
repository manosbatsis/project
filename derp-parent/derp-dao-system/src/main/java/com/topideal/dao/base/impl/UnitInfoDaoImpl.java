package com.topideal.dao.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.mapper.base.UnitInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 *单位表impl
 *@author lchenxing
 */
@Repository
public class UnitInfoDaoImpl implements UnitInfoDao {

    @Autowired
    private UnitInfoMapper mapper;    //单位表

	/**
	 * 新增
	 * @param UnitInfoModel
	 */
    @Override
    public Long save(UnitInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     *@param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     *@param List
     */
    @Override
    public int modify(UnitInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param UnitInfoModel
     */
    @Override
    public UnitInfoModel  searchByPage(UnitInfoModel  model)throws SQLException {
        PageDataModel<UnitInfoModel> pageModel=mapper.listByPage(model);
        return (UnitInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public UnitInfoModel  searchById(Long id) throws SQLException{
        UnitInfoModel  model=new UnitInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
   /**
    * 根据单位实体类查询单位
    * @param UnitInfoModel
    */
	@Override
	public UnitInfoModel searchByModel(UnitInfoModel model) throws SQLException{
		return mapper.get(model);
	}
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<UnitInfoModel> list(UnitInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return mapper.getSelectBean();
	}

    @Override
    public List<UnitInfoModel> listByLike(UnitInfoModel unitInfoModel) {
        return mapper.listByLike(unitInfoModel);
    }
}

