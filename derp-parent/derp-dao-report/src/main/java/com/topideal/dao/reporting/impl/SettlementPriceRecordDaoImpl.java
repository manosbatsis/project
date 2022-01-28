package com.topideal.dao.reporting.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SettlementPriceRecordDao;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.mapper.reporting.SettlementPriceRecordMapper;

/**
 * 结算价格记录表 daoImpl
 * @author lian_
 *
 */
@Repository
public class SettlementPriceRecordDaoImpl implements SettlementPriceRecordDao {

    @Autowired
    private SettlementPriceRecordMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SettlementPriceRecordModel> list(SettlementPriceRecordModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SettlementPriceRecordModel model) throws SQLException {
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
    public int modify(SettlementPriceRecordModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SettlementPriceRecordModel  searchByPage(SettlementPriceRecordModel  model) throws SQLException{
        PageDataModel<SettlementPriceRecordModel> pageModel=mapper.listByPage(model);
        return (SettlementPriceRecordModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SettlementPriceRecordModel  searchById(Long id)throws SQLException {
        SettlementPriceRecordModel  model=new SettlementPriceRecordModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SettlementPriceRecordModel searchByModel(SettlementPriceRecordModel model) throws SQLException {
		return mapper.get(model);
	}
	/**查询商家、货号生效日期内的结算价格 
	 */
	public SettlementPriceRecordModel getBarcodePrice(Map<String, Object> map){
		return mapper.getBarcodePrice(map);
	}
	/**查询事业部商家、货号生效日期内的结算价格 
	 */
	public SettlementPriceRecordModel getBuBarcodePrice(Map<String, Object> map){
		return mapper.getBuBarcodePrice(map);
	}
	
	@Override
	public SettlementPriceRecordModel getLatestItem(SettlementPriceRecordModel spRecordModel) {
		return mapper.getLatestItem(spRecordModel);
	}
	@Override
	public BigDecimal getHistoryPrice(SettlementPriceRecordModel temp) {
		return mapper.getHistoryPrice(temp);
	}
	@Override
	public SettlementPriceRecordDTO listRecordPriceDTO(SettlementPriceRecordDTO dto) {
		PageDataModel<SettlementPriceRecordDTO> pageModel=mapper.getListByPage(dto);
        return (SettlementPriceRecordDTO ) pageModel.getModel();
	}

}