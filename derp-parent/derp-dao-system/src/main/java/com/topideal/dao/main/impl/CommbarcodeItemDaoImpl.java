package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.mapper.main.CommbarcodeItemMapper;

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
    	model.setModifyDate(TimeUtils.getNow());
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