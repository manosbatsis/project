package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SdSaleTypeDao;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.mapper.common.SdSaleTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdSaleTypeDaoImpl implements SdSaleTypeDao {

    @Autowired
    private SdSaleTypeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdSaleTypeModel> list(SdSaleTypeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdSaleTypeModel model) throws SQLException {
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
    public int modify(SdSaleTypeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdSaleTypeModel  searchByPage(SdSaleTypeModel  model) throws SQLException{
        PageDataModel<SdSaleTypeModel> pageModel=mapper.listByPage(model);
        return (SdSaleTypeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdSaleTypeModel  searchById(Long id)throws SQLException {
        SdSaleTypeModel  model=new SdSaleTypeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdSaleTypeModel searchByModel(SdSaleTypeModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SdSaleTypeDTO listDTOByPage(SdSaleTypeDTO dto) throws SQLException {
		PageDataModel<SdSaleTypeDTO> pageModel = mapper.listDTOByPage(dto);
		return (SdSaleTypeDTO) pageModel.getModel();
	}
	/**
	 * 获取SD类型列表
	 */
	@Override
	public List<SdSaleTypeDTO> listDTO(SdSaleTypeDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
}