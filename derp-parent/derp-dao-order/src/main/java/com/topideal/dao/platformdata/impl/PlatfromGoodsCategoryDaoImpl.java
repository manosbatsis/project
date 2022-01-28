package com.topideal.dao.platformdata.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.platformdata.PlatfromGoodsCategoryDao;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;
import com.topideal.entity.vo.platformdata.PlatfromGoodsCategoryModel;
import com.topideal.mapper.platformdata.PlatfromGoodsCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatfromGoodsCategoryDaoImpl implements PlatfromGoodsCategoryDao {

    @Autowired
    private PlatfromGoodsCategoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatfromGoodsCategoryModel> list(PlatfromGoodsCategoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatfromGoodsCategoryModel model) throws SQLException {
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
    public int modify(PlatfromGoodsCategoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatfromGoodsCategoryModel  searchByPage(PlatfromGoodsCategoryModel  model) throws SQLException{
        PageDataModel<PlatfromGoodsCategoryModel> pageModel=mapper.listByPage(model);
        return (PlatfromGoodsCategoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatfromGoodsCategoryModel  searchById(Long id)throws SQLException {
        PlatfromGoodsCategoryModel  model=new PlatfromGoodsCategoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatfromGoodsCategoryModel searchByModel(PlatfromGoodsCategoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PlatfromGoodsCategoryDTO getListByPage(PlatfromGoodsCategoryDTO dto) {
        PageDataModel<PlatfromGoodsCategoryDTO> pageModel=mapper.getListByPage(dto);
        return (PlatfromGoodsCategoryDTO ) pageModel.getModel();
    }
}