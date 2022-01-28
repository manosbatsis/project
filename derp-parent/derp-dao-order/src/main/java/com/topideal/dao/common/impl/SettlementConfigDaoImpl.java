package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.common.SettlementModuleRelModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.mapper.common.SettlementConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SettlementConfigDaoImpl implements SettlementConfigDao {

    @Autowired
    private SettlementConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementConfigModel> list(SettlementConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementConfigModel model) throws SQLException {
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
    public int modify(SettlementConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementConfigModel  searchByPage(SettlementConfigModel  model) throws SQLException{
        PageDataModel<SettlementConfigModel> pageModel=mapper.listByPage(model);
        return (SettlementConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementConfigModel  searchById(Long id)throws SQLException {
        SettlementConfigModel  model=new SettlementConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementConfigModel searchByModel(SettlementConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 获取分页数据
	 */
	@Override
	public SettlementConfigDTO getSettlementListByPage(SettlementConfigDTO dto) throws SQLException {	
		PageDataModel<SettlementConfigDTO> pageModel = mapper.getSettlementListByPage(dto);
		return (SettlementConfigDTO) pageModel.getModel();
	}

    @Override
    public SettlementConfigDTO getSettlementListByModuleTypePage(SettlementConfigDTO dto) throws SQLException {
        PageDataModel<SettlementConfigDTO> pageModel = mapper.getSettlementListByModuleTypeByPage(dto);
        return (SettlementConfigDTO) pageModel.getModel();
    }

    @Override
    public List<SelectBean> getSelectBean(SettlementConfigDTO dto) throws SQLException {
        return mapper.getSelectBean(dto);
    }
	@Override
	public SettlementConfigDTO searchDetail(SettlementConfigDTO dto) throws SQLException {
		return mapper.searchDetail(dto);
	}
	@Override
	public List<SettlementConfigDTO> exportSettlementList(SettlementConfigDTO dto) throws SQLException {
		return mapper.exportSettlementList(dto);
	}

    @Override
    public void update(SettlementConfigModel model) {
        mapper.modifySettlementConfig(model);
    }

    @Override
    public SettlementConfigDTO getDetailByCustomer(SettlementConfigDTO dto) throws SQLException {
        return mapper.getDetailByCustomer(dto);
    }


}