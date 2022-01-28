package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocSettlementReceiveBillItemDao;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.mapper.bill.TocSettlementReceiveBillItemMapper;
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
public class TocSettlementReceiveBillItemDaoImpl implements TocSettlementReceiveBillItemDao {

    @Autowired
    private TocSettlementReceiveBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocSettlementReceiveBillItemModel> list(TocSettlementReceiveBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocSettlementReceiveBillItemModel model) throws SQLException {
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
    public int modify(TocSettlementReceiveBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocSettlementReceiveBillItemModel  searchByPage(TocSettlementReceiveBillItemModel  model) throws SQLException{
        PageDataModel<TocSettlementReceiveBillItemModel> pageModel=mapper.listByPage(model);
        return (TocSettlementReceiveBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocSettlementReceiveBillItemModel  searchById(Long id)throws SQLException {
        TocSettlementReceiveBillItemModel  model=new TocSettlementReceiveBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocSettlementReceiveBillItemModel searchByModel(TocSettlementReceiveBillItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TocSettlementReceiveBillItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<Map<String, Object>> receiveTotalById(Long id) throws SQLException {
        return mapper.receiveTotalById(id);
    }

    @Override
    public TocSettlementReceiveBillItemDTO listReceiveItemByPage(TocSettlementReceiveBillItemDTO dto) throws SQLException {
        PageDataModel<TocSettlementReceiveBillItemDTO> pageModel = mapper.listReceiveItemByPage(dto);
        return (TocSettlementReceiveBillItemDTO ) pageModel.getModel();
    }

    @Override
    public int delItems(Long billId) throws SQLException {
        return mapper.delItems(billId);
    }

    @Override
    public BigDecimal getTotalReceivePrice(Long id) throws SQLException {
        return mapper.getTotalReceivePrice(id);
    }

    @Override
    public BigDecimal getTotalSettlementReceivePrice(Long id) throws SQLException {
        return mapper.getTotalSettlementReceivePrice(id);
    }

    @Override
    public List<TocSettlementReceiveBillItemModel> statisticsByBill(List<Long> billIds, String orderCode, Long goodsId) throws SQLException {
        return mapper.statisticsByBill(billIds, orderCode, goodsId);
    }

    @Override
    public List<TocSettlementReceiveBillItemModel> listByProject(List<Long> billIds) {
        return mapper.listByProject(billIds);
    }

    @Override
    public List<TocSettlementReceiveBillItemDTO> listForExportItem(Long id, Integer begin, Integer pageSize) throws SQLException {
        return mapper.listForExportItem(id, begin, pageSize);
    }

    @Override
    public int countByBillId(Long billId) throws SQLException {
        return mapper.countByBillId(billId);
    }

    @Override
    public List<TocSettlementReceiveBillItemModel> getItemListPage(TocSettlementReceiveBillItemModel model) throws SQLException {
        return mapper.getItemListPage(model);
    }

    @Override
    public List<Map<String, Object>> statisticsByOrderCodesAndBillIds(List<Long> billIds, List<String> orderCodes) throws SQLException {
        return mapper.statisticsByOrderCodesAndBillIds(billIds, orderCodes);
    }

    @Override
    public List<String> getOrderCodeList(Long billId, Integer begin, Integer pageSize) {
        return mapper.getOrderCodeList(billId, begin, pageSize);
    }

    @Override
    public int countAllByBillId(Long billId) throws SQLException {
        return mapper.countAllByBillId(billId);
    }

    @Override
    public List<Map<String, Object>> statisticsByBillIds(List<Long> billIds) {
        return mapper.statisticsByBillIds(billIds);
    }
	@Override
	public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap) {
		return mapper.getProjectWarnList(queryBillMap);
	}

    @Override
    public List<TocSettlementReceiveBillItemModel> listByPlatformProject(List<Long> billIds) {
        return mapper.listByPlatformProject(billIds);
    }

    @Override
    public int countByDTO(TocSettlementReceiveBillItemDTO itemDTO) {
        return mapper.countByDTO(itemDTO);
    }

    @Override
    public List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillItemDTO itemDTO) {
        return mapper.listForExportItemByDTO(itemDTO);
    }

    @Override
    public Map<String, Object> getTotalPriceById(Long id) {
        return mapper.getTotalPriceById(id);
    }

    @Override
    public List<String> getExternalCodeList(Long billId, Integer begin, Integer pageSize) {
        return mapper.getExternalCodeList(billId, begin, pageSize);
    }

    @Override
    public void batchCleanTempBillId(Map<String, Object> params) {
        mapper.batchCleanTempBillId(params);
    }

    @Override
    public void batchUpdateTempBillId(Map<String, Object> params) {
        mapper.batchUpdateTempBillId(params);
    }
}