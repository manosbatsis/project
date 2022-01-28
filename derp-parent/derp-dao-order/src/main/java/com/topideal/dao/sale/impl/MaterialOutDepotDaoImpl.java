package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.mapper.sale.MaterialOutDepotMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MaterialOutDepotDaoImpl implements MaterialOutDepotDao {

    @Autowired
    private MaterialOutDepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MaterialOutDepotModel> list(MaterialOutDepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MaterialOutDepotModel model) throws SQLException {
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
    public int modify(MaterialOutDepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MaterialOutDepotModel  searchByPage(MaterialOutDepotModel  model) throws SQLException{
        PageDataModel<MaterialOutDepotModel> pageModel=mapper.listByPage(model);
        return (MaterialOutDepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MaterialOutDepotModel  searchById(Long id)throws SQLException {
        MaterialOutDepotModel  model=new MaterialOutDepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MaterialOutDepotModel searchByModel(MaterialOutDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 列表 分页
	 */
	@Override
	public MaterialOutDepotDTO listDTOByPage(MaterialOutDepotDTO dto) throws SQLException {
		PageDataModel<MaterialOutDepotDTO> pageModel=mapper.listDTOByPage(dto);
        return (MaterialOutDepotDTO) pageModel.getModel();
	}
	/**
	 * 查询列表
	 */
	@Override
	public List<MaterialOutDepotDTO> listDTO(MaterialOutDepotDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
	/**
	 * 根据条件查询
	 */
	@Override
	public MaterialOutDepotDTO getDetailDTO(MaterialOutDepotDTO dto) throws SQLException {
		return mapper.getDetailDTO(dto);
	}
}