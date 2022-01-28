package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryReceiveBillItemDao;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemModel;
import com.topideal.mapper.bill.TocTemporaryReceiveBillItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TocTemporaryReceiveBillItemDaoImpl implements TocTemporaryReceiveBillItemDao {

    @Autowired
    private TocTemporaryReceiveBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryReceiveBillItemModel> list(TocTemporaryReceiveBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryReceiveBillItemModel model) throws SQLException {
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
    public int modify(TocTemporaryReceiveBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryReceiveBillItemModel  searchByPage(TocTemporaryReceiveBillItemModel  model) throws SQLException{
        PageDataModel<TocTemporaryReceiveBillItemModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryReceiveBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryReceiveBillItemModel  searchById(Long id)throws SQLException {
        TocTemporaryReceiveBillItemModel  model=new TocTemporaryReceiveBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryReceiveBillItemModel searchByModel(TocTemporaryReceiveBillItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TocTemporaryReceiveBillItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public Integer deleteByModel(TocTemporaryReceiveBillItemModel model) throws SQLException {
        return mapper.deleteByModel(model);
    }

    @Override
    public TocTemporaryReceiveBillItemDTO getListByPage(TocTemporaryReceiveBillItemDTO dto) {
        PageDataModel<TocTemporaryReceiveBillItemDTO> pageModel=mapper.getListByPage(dto);
        return (TocTemporaryReceiveBillItemDTO ) pageModel.getModel();
    }

    @Override
    public int countBillNum(List<Long> ids, Long merchantId, List<Long> buIds) throws SQLException {
        return mapper.countBillNum(ids, merchantId, buIds);
    }

    @Override
    public int countTempBillNum(TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        return mapper.countTempBillNum(dto);
    }

    @Override
    public List<Map<String, Object>> listItemPrice(Long billId, List<Long> buIds) throws SQLException {
        return mapper.listItemPrice(billId, buIds);
    }

    @Override
    public List<TocTemporaryReceiveBillItemDTO> listForExportItem(List<Long> ids, List<Long> buIds, int begin, int pageSize) throws SQLException {
        return mapper.listForExportItem(ids, buIds, begin, pageSize);
    }

    @Override
    public void batchUpdate(List<TocTemporaryReceiveBillItemModel> billItemModelList) {
	    mapper.batchUpdate(billItemModelList);
    }

    @Override
    public Map<String, Object>  countByBillIdAndStatus(Long id, String settlementStatus) {
        return mapper.countByBillIdAndStatus(id, settlementStatus);
    }

    @Override
    public List<Map<String, Object>> listForExportTempItemPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        return mapper.listForExportTempItemPage(dto);
    }

    @Override
    public Timestamp getMaxSettlementDate(Long billId) throws SQLException {
        return mapper.getMaxSettlementDate(billId);
    }

    @Override
    public List<TocTemporaryReceiveBillItemModel> getItemListPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        return mapper.getItemListPage(dto);
    }

    @Override
    public List<String> getOrderCodesPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        return mapper.getOrderCodesPage(dto);
    }

    @Override
    public List<TocTemporaryReceiveBillItemModel> getItemListByOrderList(List<String> orderCodes, List<String> externalCodes, String settlementMark, Long merchantId, Long buId, String storePlatformCode) throws SQLException {
        return mapper.getItemListByOrderList(orderCodes, externalCodes, settlementMark, merchantId, buId, storePlatformCode);
    }

    @Override
    public void updateVerifyItems(List<String> orderCodes, Long merchantId, Long buId, String storePlatformCode, Long settlementId) throws SQLException {
        mapper.updateVerifyItems(orderCodes, merchantId, buId, storePlatformCode, settlementId);
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
    public List<TocTemporaryReceiveBillDTO> summaryByDTO(TocTemporaryReceiveBillItemDTO dto) {
        return mapper.summaryByDTO(dto);
    }

    @Override
    public List<String> getExternalCodesPage(TocTemporaryReceiveBillItemDTO itemDTO) {
        return mapper.getExternalCodesPage(itemDTO);
    }

}