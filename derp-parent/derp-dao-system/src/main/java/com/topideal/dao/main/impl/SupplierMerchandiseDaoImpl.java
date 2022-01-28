package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.SupplierMerchandiseDao;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.entity.vo.main.SupplierMerchandiseModel;
import com.topideal.mapper.main.SupplierMerchandiseMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SupplierMerchandiseDaoImpl implements SupplierMerchandiseDao {

    @Autowired
    private SupplierMerchandiseMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SupplierMerchandiseModel> list(SupplierMerchandiseModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SupplierMerchandiseModel model) throws SQLException {
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
    public int modify(SupplierMerchandiseModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SupplierMerchandiseModel  searchByPage(SupplierMerchandiseModel  model) throws SQLException{
        PageDataModel<SupplierMerchandiseModel> pageModel=mapper.listByPage(model);
        return (SupplierMerchandiseModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SupplierMerchandiseModel  searchById(Long id)throws SQLException {
        SupplierMerchandiseModel  model=new SupplierMerchandiseModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SupplierMerchandiseModel searchByModel(SupplierMerchandiseModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 分页
	 */
	@Override
	public SupplierMerchandiseDTO searchDTOByPage(SupplierMerchandiseDTO dto) {
		PageDataModel<SupplierMerchandiseDTO> pageModel=mapper.searchDTOByPage(dto) ;
		return (SupplierMerchandiseDTO ) pageModel.getModel();
	}
	/**
	 * 导出
	 */
	@Override
	public List<SupplierMerchandiseDTO> exportList(SupplierMerchandiseDTO dto) throws SQLException {		
		return mapper.exportList(dto);
	}

}