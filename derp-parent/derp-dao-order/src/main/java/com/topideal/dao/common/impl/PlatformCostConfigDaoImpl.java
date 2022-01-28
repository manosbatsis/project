package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.PlatformCostConfigDao;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.PlatformCostConfigModel;
import com.topideal.mapper.common.PlatformCostConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformCostConfigDaoImpl implements PlatformCostConfigDao {

    @Autowired
    private PlatformCostConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformCostConfigModel> list(PlatformCostConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformCostConfigModel model) throws SQLException {
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
    public int modify(PlatformCostConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformCostConfigModel  searchByPage(PlatformCostConfigModel  model) throws SQLException{
        PageDataModel<PlatformCostConfigModel> pageModel=mapper.listByPage(model);
        return (PlatformCostConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformCostConfigModel  searchById(Long id)throws SQLException {
        PlatformCostConfigModel  model=new PlatformCostConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformCostConfigModel searchByModel(PlatformCostConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public PlatformCostConfigDTO listDTOByPage(PlatformCostConfigDTO dto) {
        PageDataModel<PlatformCostConfigDTO> pageModel = mapper.listDTOByPage(dto);
        return (PlatformCostConfigDTO) pageModel.getModel();
    }

    @Override
    public List<PlatformCostConfigDTO> getLatestList(PlatformCostConfigDTO dto) throws Exception {
        return mapper.getLatestList(dto);
    }

    @Override
    public PlatformCostConfigDTO getLatestDTO(PlatformCostConfigDTO dto) throws Exception {
        return mapper.getLatestDTO(dto);
    }

    @Override
    public PlatformCostConfigDTO searchByCheck(PlatformCostConfigDTO model) {
        return mapper.searchByCheck(model);
    }


}