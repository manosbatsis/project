package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryReceiveBillCostItemDao;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemModel;
import com.topideal.mapper.bill.TocTemporaryReceiveBillCostItemMapper;
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
public class TocTemporaryReceiveBillCostItemDaoImpl implements TocTemporaryReceiveBillCostItemDao {

    @Autowired
    private TocTemporaryReceiveBillCostItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryReceiveBillCostItemModel> list(TocTemporaryReceiveBillCostItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryReceiveBillCostItemModel model) throws SQLException {
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
    public int modify(TocTemporaryReceiveBillCostItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryReceiveBillCostItemModel  searchByPage(TocTemporaryReceiveBillCostItemModel  model) throws SQLException{
        PageDataModel<TocTemporaryReceiveBillCostItemModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryReceiveBillCostItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryReceiveBillCostItemModel  searchById(Long id)throws SQLException {
        TocTemporaryReceiveBillCostItemModel  model=new TocTemporaryReceiveBillCostItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryReceiveBillCostItemModel searchByModel(TocTemporaryReceiveBillCostItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TocTemporaryReceiveBillCostItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public int countTempBillNum(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        return mapper.countTempBillNum(dto);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemModel> getItemListPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        return mapper.getItemListPage(dto);
    }

    @Override
    public List<String> getOrderCodesPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        return mapper.getOrderCodesPage(dto);
    }

    @Override
    public List<String> getExternalCodesPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        return mapper.getExternalCodesPage(dto);
    }

    @Override
    public void batchUpdate(List<TocTemporaryReceiveBillCostItemModel> billItemModelList) {
        mapper.batchUpdate(billItemModelList);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemModel> getItemListByOrderList(List<String> orderCodes, String type, Long merchantId,Long buId, String storePlatformCode) throws SQLException {
        return mapper.getItemListByOrderList(orderCodes, type, merchantId, buId, storePlatformCode);
    }

    @Override
    public TocTemporaryReceiveBillCostItemDTO getListByPage(TocTemporaryReceiveBillCostItemDTO dto) {
        PageDataModel<TocTemporaryReceiveBillCostItemDTO> pageModel=mapper.getListByPage(dto);
        return (TocTemporaryReceiveBillCostItemDTO ) pageModel.getModel();
    }

    @Override
    public List<Map<String, Object>> listForExportTempCostItemPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        return mapper.listForExportTempCostItemPage(dto);
    }

    @Override
    public Integer deleteByModel(TocTemporaryReceiveBillCostItemModel model) throws SQLException {
        return mapper.deleteByModel(model);
    }

    @Override
    public void updateVerifyCostItems(List<String> orderCodes, String type, Long merchantId, Long buId, String storePlatformCode, Long settlementId) throws SQLException {
        mapper.updateVerifyCostItems(orderCodes, type, merchantId, buId, storePlatformCode, settlementId);
    }

    @Override
    public List<Map<String, Object>> countByBillIds(List<Long> billIds) {
        return mapper.countByBillIds(billIds);
    }

    @Override
    public Integer countPunchNum(Long billId, String punchType) {
        return mapper.countPunchNum(billId, punchType);
    }

    @Override
    public void updateEndItemBill(Long billId, String punchType) {
        mapper.updateEndItemBill(billId, punchType);
    }

    @Override
    public List<TocTemporaryCostBillDTO> summaryByDTO(TocTemporaryReceiveBillCostItemDTO dto) {
        return mapper.summaryByDTO(dto);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemDTO> sumCostGroupByModel(TocTemporaryReceiveBillCostItemModel queryItemModel) {
        return mapper.sumCostGroupByModel(queryItemModel);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemDTO> getCostDiffItems(Map<String, Object> params) {
        return mapper.getCostDiffItems(params);
    }

    @Override
    public void batchUpdateVerifyCostItemsByDiffItem(List<TocTemporaryReceiveBillCostItemDTO> diffItems) {
	    mapper.batchUpdateVerifyCostItemsByDiffItem(diffItems);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemDTO> getUnVerifyCostItem(Map<String, Object> params) {
        return mapper.getUnVerifyCostItem(params);
    }

    @Override
    public void batchUpdateVerifyCostItemsByDiffItem2(Map<String, Object> params) {
        mapper.batchUpdateVerifyCostItemsByDiffItem2(params);
    }

    @Override
    public TocTemporaryReceiveBillCostItemDTO getCostDiffListByPage(TocTemporaryReceiveBillCostItemDTO dto) {
        PageDataModel<TocTemporaryReceiveBillCostItemDTO> pageModel=mapper.getCostDiffListByPage(dto);
        return (TocTemporaryReceiveBillCostItemDTO ) pageModel.getModel();
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemDTO> getUnVerifyCostWithoutSettlementItem(Map<String, Object> params) {
        return mapper.getUnVerifyCostWithoutSettlementItem(params);
    }

    @Override
    public void updateWithoutSettleItems(List<TocTemporaryReceiveBillCostItemDTO> billItemModelList) {
        mapper.updateWithoutSettleItems(billItemModelList);
    }

    @Override
    public void deletebyModelExcludeAdjustment(TocTemporaryReceiveBillCostItemModel alreadyItemModel) {
        mapper.deletebyModelExcludeAdjustment(alreadyItemModel);
    }

    @Override
    public void deleteByMap(Map<String, Object> param) {
        mapper.deleteByMap(param);
    }

    @Override
    public List<TocTemporaryReceiveBillCostItemDTO> getItemListByMap(Map<String, Object> param) {
        return mapper.getItemListByMap(param);
    }

    @Override
    public void updateAdjustmentByDTO(TocTemporaryReceiveBillCostItemDTO entity) {
        mapper.updateAdjustmentByDTO(entity);
    }

    @Override
    public void updateByMap(Map<String, Object> param) {
        mapper.updateByMap(param);
    }

}