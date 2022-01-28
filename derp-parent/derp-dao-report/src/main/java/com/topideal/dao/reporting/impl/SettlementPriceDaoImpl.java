package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SettlementPriceDao;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;
import com.topideal.mapper.reporting.SettlementPriceMapper;

/**
 * 结算价格 daoImpl
 * @author lian_
 */
@Repository
public class SettlementPriceDaoImpl implements SettlementPriceDao {

    @Autowired
    private SettlementPriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementPriceModel> list(SettlementPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementPriceModel model) throws SQLException {
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
    public int modify(SettlementPriceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementPriceModel  searchByPage(SettlementPriceModel  model) throws SQLException{
        PageDataModel<SettlementPriceModel> pageModel=mapper.listByPage(model);
        return (SettlementPriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementPriceModel  searchById(Long id)throws SQLException {
        SettlementPriceModel  model=new SettlementPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementPriceModel searchByModel(SettlementPriceModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据商家+商品条码查询存在的记录(剔除修改记录中的id)
	 * @param merchantId
	 * @param barcode
	 * @param ids
	 * @return
	 */
	@Override
	public List<SettlementPriceModel> getListNotInIds(Long merchantId, String barcode, String ids) {
		return mapper.getListNotInIds(merchantId,barcode,ids);
	}
	public SettlementPriceModel getPriceOne(Map<String, Object> paramMap){
		return mapper.getPriceOne(paramMap);
	}
	@Override
	public List<SettlementPriceDTO> queryList(SettlementPriceDTO dto) {
		return mapper.queryList(dto);
	}
	@Override
	public List<SettlementPriceModel> searchByIds(List list) {
		return mapper.searchByIds(list);
	}
	@Override
	public List<SettlementPriceExamineDTO> listExamineList(SettlementPriceExamineDTO model) {
		return mapper.listExamineList(model);
	}
	@Override
	public Integer countExamineList(SettlementPriceExamineDTO model) {
		return mapper.countExamineList(model);
	}
	@Override
	public SettlementPriceDTO listPriceDTO(SettlementPriceDTO dto) {
		PageDataModel<SettlementPriceDTO> pageModel=mapper.getListByPage(dto);
        return (SettlementPriceDTO ) pageModel.getModel();
	}
	@Override
	public List<SettlementPriceDTO> getListByEffectiveDate(Map<String, Object> map) {
		return mapper.getListByEffectiveDate(map);
	}
	
}