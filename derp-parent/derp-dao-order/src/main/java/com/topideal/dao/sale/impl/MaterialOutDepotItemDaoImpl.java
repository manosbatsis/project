package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.MaterialOutDepotItemDao;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.mapper.sale.MaterialOutDepotItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MaterialOutDepotItemDaoImpl implements MaterialOutDepotItemDao {

    @Autowired
    private MaterialOutDepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MaterialOutDepotItemModel> list(MaterialOutDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MaterialOutDepotItemModel model) throws SQLException {
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
    public int modify(MaterialOutDepotItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MaterialOutDepotItemModel  searchByPage(MaterialOutDepotItemModel  model) throws SQLException{
        PageDataModel<MaterialOutDepotItemModel> pageModel=mapper.listByPage(model);
        return (MaterialOutDepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MaterialOutDepotItemModel  searchById(Long id)throws SQLException {
        MaterialOutDepotItemModel  model=new MaterialOutDepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MaterialOutDepotItemModel searchByModel(MaterialOutDepotItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<MaterialOutDepotItemDTO> getDetailDTO(MaterialOutDepotItemDTO dto) throws SQLException {
		return mapper.getDetailDTO(dto);
	}
}