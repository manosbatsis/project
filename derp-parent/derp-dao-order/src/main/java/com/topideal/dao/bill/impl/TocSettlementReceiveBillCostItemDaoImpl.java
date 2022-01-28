package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocSettlementReceiveBillCostItemDao;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.mapper.bill.TocSettlementReceiveBillCostItemMapper;
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
public class TocSettlementReceiveBillCostItemDaoImpl implements TocSettlementReceiveBillCostItemDao {

    @Autowired
    private TocSettlementReceiveBillCostItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocSettlementReceiveBillCostItemModel> list(TocSettlementReceiveBillCostItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocSettlementReceiveBillCostItemModel model) throws SQLException {
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
    public int modify(TocSettlementReceiveBillCostItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocSettlementReceiveBillCostItemModel  searchByPage(TocSettlementReceiveBillCostItemModel  model) throws SQLException{
        PageDataModel<TocSettlementReceiveBillCostItemModel> pageModel=mapper.listByPage(model);
        return (TocSettlementReceiveBillCostItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocSettlementReceiveBillCostItemModel  searchById(Long id)throws SQLException {
        TocSettlementReceiveBillCostItemModel  model=new TocSettlementReceiveBillCostItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocSettlementReceiveBillCostItemModel searchByModel(TocSettlementReceiveBillCostItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TocSettlementReceiveBillCostItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<Map<String, Object>> deductionTotalById(Long id) throws SQLException {
        return mapper.deductionTotalById(id);
    }

    @Override
    public TocSettlementReceiveBillCostItemDTO listReceiveCostItemByPage(TocSettlementReceiveBillCostItemDTO dto) throws SQLException {
        PageDataModel<TocSettlementReceiveBillCostItemDTO> pageModel = mapper.listReceiveCostItemByPage(dto);
        return (TocSettlementReceiveBillCostItemDTO ) pageModel.getModel();
    }

    @Override
    public int delCostItems(Long billId, String dataSource) throws SQLException {
        return mapper.delCostItems(billId, dataSource);
    }

    @Override
    public BigDecimal getTotalReceivePrice(Long id) throws SQLException {
        return mapper.getTotalReceivePrice(id);
    }

    @Override
    public BigDecimal getTotalSettlementPrice(Long id) throws SQLException {
        return mapper.getTotalSettlementPrice(id);
    }

    @Override
    public List<TocSettlementReceiveBillCostItemModel> statisticsByBill(List<Long> billIds, String orderCode, Long goodsId) throws SQLException {
        return mapper.statisticsByBill(billIds, orderCode, goodsId);
    }

    @Override
    public List<TocSettlementReceiveBillCostItemModel> listByProject(List<Long> billIds) {
        return mapper.listByProject(billIds);
    }

    @Override
    public List<TocSettlementReceiveBillCostItemDTO> listForExportItem(Long billId, Integer begin, Integer pageSize) throws SQLException {
        return mapper.listForExportItem(billId, begin, pageSize);
    }

    @Override
    public int countByBillId(Long billId, String type) throws SQLException {
        return mapper.countByBillId(billId, type);
    }

    @Override
    public List<TocSettlementReceiveBillCostItemModel> getItemListPage(TocSettlementReceiveBillCostItemModel model) throws SQLException {
        return mapper.getItemListPage(model);
    }

    @Override
    public List<Map<String, Object>> statisticsByOrderCodesAndBillIds(List<Long> billIds, String type, List<String> orderCodes) throws SQLException {
        return mapper.statisticsByOrderCodesAndBillIds(billIds, type, orderCodes);
    }

    @Override
    public List<String> getOrderCodeList(Long billId, String type, Integer begin, Integer pageSize) {
        return mapper.getOrderCodeList(billId, type, begin, pageSize);
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
    public List<TocSettlementReceiveBillCostItemModel> listByPlatformProject(List<Long> billIds) {
        return mapper.listByPlatformProject(billIds);
    }

    @Override
    public void batchUpdateTempCostBillId(Map<String, Object> params) {
        mapper.batchUpdateTempCostBillId(params);
    }

    @Override
    public void batchCleanTempCostBillId(Map<String, Object> params) {
        mapper.batchCleanTempCostBillId(params);
    }

    @Override
    public List<TocSettlementReceiveBillCostItemDTO> getExtistTempByMap(Map<String, Object> params) {
        return mapper.getExtistTempByMap(params);
    }

    @Override
    public int countByDTO(TocSettlementReceiveBillItemDTO itemDTO) {
        return mapper.countByDTO(itemDTO);
    }

    @Override
    public List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillCostItemDTO costItemDTO) {
        return mapper.listForExportItemByDTO(costItemDTO);
    }
}