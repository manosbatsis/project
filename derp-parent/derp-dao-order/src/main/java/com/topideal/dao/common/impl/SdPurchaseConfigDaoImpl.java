package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SdPurchaseConfigDao;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.mapper.common.SdPurchaseConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SdPurchaseConfigDaoImpl implements SdPurchaseConfigDao {

    @Autowired
    private SdPurchaseConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SdPurchaseConfigModel> list(SdPurchaseConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SdPurchaseConfigModel model) throws SQLException {
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
    public int modify(SdPurchaseConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SdPurchaseConfigModel  searchByPage(SdPurchaseConfigModel  model) throws SQLException{
        PageDataModel<SdPurchaseConfigModel> pageModel=mapper.listByPage(model);
        return (SdPurchaseConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SdPurchaseConfigModel  searchById(Long id)throws SQLException {
        SdPurchaseConfigModel  model=new SdPurchaseConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SdPurchaseConfigModel searchByModel(SdPurchaseConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SdPurchaseConfigDTO getListByPage(SdPurchaseConfigDTO dto) {
		PageDataModel<SdPurchaseConfigDTO> pageModel=mapper.getListByPage(dto);
        return (SdPurchaseConfigDTO ) pageModel.getModel();
	}
	@Override
	public SdPurchaseConfigDTO searchDTO(SdPurchaseConfigDTO dto) {
		return mapper.searchDTO(dto);
	}
	@Override
	public SdPurchaseConfigModel getLastestModel(SdPurchaseConfigModel queryModel) {
		return mapper.getLastestModel(queryModel);
	}
}