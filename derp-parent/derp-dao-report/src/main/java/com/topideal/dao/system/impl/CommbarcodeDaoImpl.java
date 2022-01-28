package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.CommbarcodeDao;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.mapper.system.CommbarcodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CommbarcodeDaoImpl implements CommbarcodeDao {

    @Autowired
    private CommbarcodeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CommbarcodeModel> list(CommbarcodeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CommbarcodeModel model) throws SQLException {
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
    public int modify(CommbarcodeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CommbarcodeModel  searchByPage(CommbarcodeModel  model) throws SQLException{
        PageDataModel<CommbarcodeModel> pageModel=mapper.listByPage(model);
        return (CommbarcodeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CommbarcodeModel  searchById(Long id)throws SQLException {
        CommbarcodeModel  model=new CommbarcodeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CommbarcodeModel searchByModel(CommbarcodeModel model) throws SQLException {
		return mapper.get(model);
	}

}