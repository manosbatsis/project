package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.FileTempBakDao;
import com.topideal.entity.vo.common.FileTempBakModel;
import com.topideal.mapper.common.FileTempBakMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class FileTempBakDaoImpl implements FileTempBakDao {

    @Autowired
    private FileTempBakMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<FileTempBakModel> list(FileTempBakModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(FileTempBakModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(FileTempBakModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public FileTempBakModel  searchByPage(FileTempBakModel  model) throws SQLException{
        PageDataModel<FileTempBakModel> pageModel=mapper.listByPage(model);
        return (FileTempBakModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public FileTempBakModel  searchById(Long id)throws SQLException {
        FileTempBakModel  model=new FileTempBakModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public FileTempBakModel searchByModel(FileTempBakModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
}