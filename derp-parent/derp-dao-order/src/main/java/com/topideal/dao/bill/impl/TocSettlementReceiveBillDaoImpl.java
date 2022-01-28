package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocSettlementReceiveBillDao;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.mapper.bill.TocSettlementReceiveBillMapper;
import org.apache.ibatis.annotations.Param;
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
public class TocSettlementReceiveBillDaoImpl implements TocSettlementReceiveBillDao {

    @Autowired
    private TocSettlementReceiveBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocSettlementReceiveBillModel> list(TocSettlementReceiveBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocSettlementReceiveBillModel model) throws SQLException {
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
    public int modify(TocSettlementReceiveBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocSettlementReceiveBillModel  searchByPage(TocSettlementReceiveBillModel  model) throws SQLException{
        PageDataModel<TocSettlementReceiveBillModel> pageModel=mapper.listByPage(model);
        return (TocSettlementReceiveBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocSettlementReceiveBillModel  searchById(Long id)throws SQLException {
        TocSettlementReceiveBillModel  model=new TocSettlementReceiveBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocSettlementReceiveBillModel searchByModel(TocSettlementReceiveBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TocSettlementReceiveBillDTO listReceiveBillByPage(TocSettlementReceiveBillDTO dto) throws SQLException {
        PageDataModel<TocSettlementReceiveBillDTO> pageModel = mapper.listReceiveBillByPage(dto);
        return (TocSettlementReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public TocSettlementReceiveBillDTO searchDTOById(Long id) throws SQLException {
        return mapper.searchDTOById(id);
    }

    @Override
    public List<TocSettlementReceiveBillModel> getNcBackfillList() {
        return mapper.getNcBackfillList();
    }

    @Override
    public List<TocSettlementReceiveBillModel> getNcVoucherFillBackList() {
        return mapper.getNcVoucherFillBackList();
    }

    @Override
    public List<TocSettlementReceiveBillDTO> listForExport(TocSettlementReceiveBillDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public int updateWithNull(TocSettlementReceiveBillModel model) {
        return mapper.updateWithNull(model);
    }

    @Override
    public List<TocSettlementReceiveBillModel> listByIds(List<Long> ids) {
        return mapper.listByIds(ids);
    }

    @Override
    public void batchUpdateInvoiceStatus(List<Long> ids, String invoiceStatus) throws SQLException {
        mapper.batchUpdateInvoiceStatus(ids, invoiceStatus);
    }

    @Override
    public Map<String, Object> getRecevieByBillStatus(Map<String, Object> queryMap) throws SQLException {
        return mapper.getRecevieByBillStatus(queryMap);
    }

    @Override
    public List<Map<String, Object>> getSummarySettlement(Map<String, Object> queryMap) throws SQLException {
        return mapper.getSummarySettlement(queryMap);
    }


    @Override
    public List<Map<String, Object>> getItemBySearch(Map<String, Object> queryMap) {
        return mapper.getItemBySearch(queryMap);
    }

    @Override
    public List<TocSettlementReceiveBillModel> listTocBill(TocSettlementReceiveBillDTO dto) {
        return mapper.listTocBill(dto);
    }

    @Override
    public void batchUpdateBillStatus(List<Long> ids, String billStatus) throws SQLException {
        mapper.batchUpdateBillStatus(ids, billStatus);
    }

    @Override
    public List<TocSettlementReceiveBillModel> getReceiveBillList(Map<String, Object> map) throws SQLException {
        return mapper.getReceiveBillList(map);
    }

    @Override
    public List<TocSettlementReceiveBillModel> getZfAnd006ReceiveBill(List<String> receiveCodes) throws SQLException {
        return mapper.getZfAnd006ReceiveBill(receiveCodes);
    }

    @Override
    public List<TocSettlementReceiveBillModel> listByMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException {
        return mapper.listByMonthlySnapshot(merchantId, buId, month);
    }

    @Override
    public List<TocSettlementReceiveBillModel> listByAllVerifyMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException {
        return mapper.listByAllVerifyMonthlySnapshot(merchantId, buId, month);
    }
}