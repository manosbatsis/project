package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.mapper.common.PlatformSettlementConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformSettlementConfigDaoImpl implements PlatformSettlementConfigDao {

    @Autowired
    private PlatformSettlementConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformSettlementConfigModel> list(PlatformSettlementConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformSettlementConfigModel model) throws SQLException {
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
    public int modify(PlatformSettlementConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformSettlementConfigModel  searchByPage(PlatformSettlementConfigModel  model) throws SQLException{
        PageDataModel<PlatformSettlementConfigModel> pageModel=mapper.listByPage(model);
        return (PlatformSettlementConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformSettlementConfigModel  searchById(Long id)throws SQLException {
        PlatformSettlementConfigModel  model=new PlatformSettlementConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformSettlementConfigModel searchByModel(PlatformSettlementConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 分页查询
	 */
	@Override
	public PlatformSettlementConfigDTO getPlatSettlementListByPage(PlatformSettlementConfigDTO dto)
			throws SQLException {
		  PageDataModel<PlatformSettlementConfigDTO> pageModel=mapper.getPlatSettlementListByPage(dto);
		return (PlatformSettlementConfigDTO ) pageModel.getModel();
	}
	/**
	 * 导出
	 */
	@Override
	public List<PlatformSettlementConfigDTO> exportSettlementList(PlatformSettlementConfigDTO dto)
			throws SQLException {		
		return mapper.exportSettlementList(dto);
	}

	@Override
	public List<PlatformSettlementConfigDTO> getSelectBean(String storePlatformCode) {
		return mapper.getSelectBean(storePlatformCode);
	}

}