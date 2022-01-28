package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.mapper.sale.MaterialOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MaterialOrderDaoImpl implements MaterialOrderDao {

    @Autowired
    private MaterialOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MaterialOrderModel> list(MaterialOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MaterialOrderModel model) throws SQLException {
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
    public int modify(MaterialOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MaterialOrderModel  searchByPage(MaterialOrderModel  model) throws SQLException{
        PageDataModel<MaterialOrderModel> pageModel=mapper.listByPage(model);
        return (MaterialOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MaterialOrderModel  searchById(Long id)throws SQLException {
        MaterialOrderModel  model=new MaterialOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
  /**
 	* 根据商家实体类查询商品
 	* @param model
 	* */
	@Override
	public MaterialOrderModel searchByModel(MaterialOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 按条件查询列表 分页
	 */
	@Override
	public MaterialOrderDTO listDTOByPage(MaterialOrderDTO dto) throws SQLException {		
		PageDataModel<MaterialOrderDTO> pageModel = mapper.listDTOByPage(dto);
		return (MaterialOrderDTO)pageModel.getModel();
	}
	/**
	 * 按条件查询列表
	 */
	@Override
	public List<MaterialOrderDTO> listDTO(MaterialOrderDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
	/**
	 * 逻辑删除
	 */
	@Override
	public int delMaterialOrder(List ids) throws SQLException {		
		return mapper.delMaterialOrder(ids);
	}
	/**
	 * 根据id查询信息
	 */
	@Override
	public MaterialOrderDTO getMaterialOrderDTO(MaterialOrderDTO dto) throws SQLException {		
		return mapper.getMaterialOrderDTO(dto);
	}

}