package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SdSaleVerifyConfigDao;
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;
import com.topideal.entity.vo.common.SdSaleVerifyConfigModel;
import com.topideal.mapper.common.SdSaleVerifyConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdSaleVerifyConfigDaoImpl implements SdSaleVerifyConfigDao {

    @Autowired
    private SdSaleVerifyConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdSaleVerifyConfigModel> list(SdSaleVerifyConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdSaleVerifyConfigModel model) throws SQLException {
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
    public int modify(SdSaleVerifyConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdSaleVerifyConfigModel  searchByPage(SdSaleVerifyConfigModel  model) throws SQLException{
        PageDataModel<SdSaleVerifyConfigModel> pageModel=mapper.listByPage(model);
        return (SdSaleVerifyConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdSaleVerifyConfigModel  searchById(Long id)throws SQLException {
        SdSaleVerifyConfigModel  model=new SdSaleVerifyConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdSaleVerifyConfigModel searchByModel(SdSaleVerifyConfigModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public SdSaleVerifyConfigDTO listDTOByPage(SdSaleVerifyConfigDTO dto) throws SQLException  {
		PageDataModel<SdSaleVerifyConfigDTO> pageModel = mapper.listDTOByPage(dto);
		return (SdSaleVerifyConfigDTO) pageModel.getModel();
	}
	@Override
	public SdSaleVerifyConfigDTO searchDetail(Long id) throws SQLException {
		return mapper.searchDetail(id);
	}
}