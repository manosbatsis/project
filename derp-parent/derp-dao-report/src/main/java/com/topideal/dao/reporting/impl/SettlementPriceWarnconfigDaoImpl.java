package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SettlementPriceWarnconfigDao;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;
import com.topideal.mapper.reporting.SettlementPriceWarnconfigMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SettlementPriceWarnconfigDaoImpl implements SettlementPriceWarnconfigDao {

    @Autowired
    private SettlementPriceWarnconfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementPriceWarnconfigModel> list(SettlementPriceWarnconfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementPriceWarnconfigModel model) throws SQLException {
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
    public int modify(SettlementPriceWarnconfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementPriceWarnconfigModel  searchByPage(SettlementPriceWarnconfigModel  model) throws SQLException{
        PageDataModel<SettlementPriceWarnconfigModel> pageModel=mapper.listByPage(model);
        return (SettlementPriceWarnconfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementPriceWarnconfigModel  searchById(Long id)throws SQLException {
        SettlementPriceWarnconfigModel  model=new SettlementPriceWarnconfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementPriceWarnconfigModel searchByModel(SettlementPriceWarnconfigModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SettlementPriceWarnconfigDTO getListByPage(SettlementPriceWarnconfigDTO dto) {
		PageDataModel<SettlementPriceWarnconfigDTO> page = mapper.getListByPage(dto);
		return (SettlementPriceWarnconfigDTO)page.getModel() ;
	}
	@Override
	public SettlementPriceWarnconfigDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}

}