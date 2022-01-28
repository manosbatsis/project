package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.CommbarcodeItemDao;
import com.topideal.entity.vo.system.CommbarcodeItemModel;
import com.topideal.mapper.system.CommbarcodeItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CommbarcodeItemDaoImpl implements CommbarcodeItemDao {

    @Autowired
    private CommbarcodeItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CommbarcodeItemModel> list(CommbarcodeItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CommbarcodeItemModel model) throws SQLException {
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
    public int modify(CommbarcodeItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CommbarcodeItemModel  searchByPage(CommbarcodeItemModel  model) throws SQLException{
        PageDataModel<CommbarcodeItemModel> pageModel=mapper.listByPage(model);
        return (CommbarcodeItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CommbarcodeItemModel  searchById(Long id)throws SQLException {
        CommbarcodeItemModel  model=new CommbarcodeItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CommbarcodeItemModel searchByModel(CommbarcodeItemModel model) throws SQLException {
		return mapper.get(model);
	}

}