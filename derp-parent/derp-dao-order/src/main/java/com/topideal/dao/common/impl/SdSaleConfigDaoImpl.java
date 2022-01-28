package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigModel;
import com.topideal.mapper.common.SdSaleConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdSaleConfigDaoImpl implements SdSaleConfigDao {

    @Autowired
    private SdSaleConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdSaleConfigModel> list(SdSaleConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdSaleConfigModel model) throws SQLException {
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
    public int modify(SdSaleConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdSaleConfigModel  searchByPage(SdSaleConfigModel  model) throws SQLException{
        PageDataModel<SdSaleConfigModel> pageModel=mapper.listByPage(model);
        return (SdSaleConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdSaleConfigModel  searchById(Long id)throws SQLException {
        SdSaleConfigModel  model=new SdSaleConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdSaleConfigModel searchByModel(SdSaleConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SdSaleConfigDTO listDTOByPage(SdSaleConfigDTO dto) throws SQLException {
		PageDataModel<SdSaleConfigDTO> pageModel = mapper.listDTOByPage(dto);
		return (SdSaleConfigDTO) pageModel.getModel();
	}
	@Override
	public SdSaleConfigDTO queryExistConfig(Map<String, Object> map) throws SQLException {
		return mapper.queryExistConfig(map);
	}
	@Override
	public List<SdSaleConfigDTO> listDTO(SdSaleConfigDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}
}