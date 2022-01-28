package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTemporaryReceiveBillDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.mapper.bill.TobTemporaryReceiveBillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TobTemporaryReceiveBillDaoImpl implements TobTemporaryReceiveBillDao {

    @Autowired
    private TobTemporaryReceiveBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTemporaryReceiveBillModel> list(TobTemporaryReceiveBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTemporaryReceiveBillModel model) throws SQLException {
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
    public int modify(TobTemporaryReceiveBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTemporaryReceiveBillModel  searchByPage(TobTemporaryReceiveBillModel  model) throws SQLException{
        PageDataModel<TobTemporaryReceiveBillModel> pageModel=mapper.listByPage(model);
        return (TobTemporaryReceiveBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTemporaryReceiveBillModel  searchById(Long id)throws SQLException {
        TobTemporaryReceiveBillModel  model=new TobTemporaryReceiveBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTemporaryReceiveBillModel searchByModel(TobTemporaryReceiveBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TobTemporaryReceiveBillDTO listToBTempBillByPage(TobTemporaryReceiveBillDTO dto) throws SQLException {
        PageDataModel<TobTemporaryReceiveBillDTO> pageModel = mapper.listToBTempBillByPage(dto);
        return (TobTemporaryReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public List<TobTemporaryReceiveBillDTO> listForExport(TobTemporaryReceiveBillDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public void batchUpdate(List<TobTemporaryReceiveBillModel> models) {
        mapper.batchUpdate(models);
    }

    @Override
    public TobTemporaryReceiveBillDTO searchDTOById(Long id) {
        return mapper.searchDTOById(id);
    }

    @Override
    public List<TobTemporaryReceiveBillDTO> listBillByRelIds(List<Long> ids) {
        return mapper.listBillByRelIds(ids);
    }

    @Override
    public List<TobTemporaryReceiveBillDTO> listBySearchQuery(Map<String, Object> searchQueryMap) {
        return mapper.listBySearchQuery(searchQueryMap);
    }

    @Override
    public List<Map<String, Object>> getItemBySearch(Map<String, Object> searchQueryMap) {
        return mapper.getItemBySearch(searchQueryMap);
    }

    @Override
    public Map<String, BigDecimal> getTocTemprayCostBillDTO(Map<String, Object> searchQueryMap) {
        return mapper.getTocTemprayCostBillDTO(searchQueryMap);
    }

    @Override
    public List<TobTemporaryReceiveBillDTO> listByDto(TobTemporaryReceiveBillDTO receiveBillDTO) {
        return mapper.listByDto(receiveBillDTO);
    }

    @Override
    public List<TobTemporaryReceiveBillDTO> listByShelfCodes(List<String> shelfCodes, String isWriteOff, Long merchantId) {
        return mapper.listByShelfCodes(shelfCodes, isWriteOff, merchantId);
    }

    @Override
    public void batchUpdateStatus(List<Long> ids, String status, String rebateStatus) {
        mapper.batchUpdateStatus(ids, status, rebateStatus);
    }

}