package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.FirstCustomsInfoDao;
import com.topideal.entity.vo.purchase.FirstCustomsInfoModel;
import com.topideal.mapper.purchase.FirstCustomsInfoMapper;

/**
 * 一线清关资料(预申报单) daoImpl
 * @author lchenxing
 */
@Repository
public class FirstCustomsInfoDaoImpl implements FirstCustomsInfoDao {

    @Autowired
    private FirstCustomsInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<FirstCustomsInfoModel> list(FirstCustomsInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(FirstCustomsInfoModel model) throws SQLException {
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
    public int modify(FirstCustomsInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public FirstCustomsInfoModel  searchByPage(FirstCustomsInfoModel  model) throws SQLException{
        PageDataModel<FirstCustomsInfoModel> pageModel=mapper.listByPage(model);
        return (FirstCustomsInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public FirstCustomsInfoModel  searchById(Long id)throws SQLException {
        FirstCustomsInfoModel  model=new FirstCustomsInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public FirstCustomsInfoModel searchByModel(FirstCustomsInfoModel model) throws SQLException {
		return mapper.get(model);
	}
    
}
