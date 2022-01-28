package com.topideal.dao.bill.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillCostItemDao;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.mapper.bill.ReceiveBillCostItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillCostItemDaoImpl implements ReceiveBillCostItemDao {

    @Autowired
    private ReceiveBillCostItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillCostItemModel> list(ReceiveBillCostItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillCostItemModel model) throws SQLException {
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
    public int modify(ReceiveBillCostItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillCostItemModel  searchByPage(ReceiveBillCostItemModel  model) throws SQLException{
        PageDataModel<ReceiveBillCostItemModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillCostItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillCostItemModel  searchById(Long id)throws SQLException {
        ReceiveBillCostItemModel  model=new ReceiveBillCostItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillCostItemModel searchByModel(ReceiveBillCostItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<ReceiveBillCostItemDTO> itemListByBillIds(List<Long> billId) throws SQLException {
        return mapper.itemListByBillIds(billId);
    }

    @Override
    public int delCostItem(Long billId) throws SQLException {
        return mapper.delCostItem(billId);
    }

    @Override
    public BigDecimal getTotalReceivePrice(Long billId) throws SQLException {
        return mapper.getTotalReceivePrice(billId);
    }

    @Override
    public List<Map<String, Object>> listInvoiceCostItem(List<Long> billIds) throws SQLException {
        return mapper.listInvoiceCostItem(billIds);
    }

    @Override
    public List<Map<String, Object>> listInvoiceCostItemByGoodsNo(List<Long> ids, Long merchantId, String source) throws SQLException {
        return mapper.listInvoiceCostItemByGoodsNo(ids,merchantId, source);
    }

    @Override
    public List<Map<String, Object>> listCostPrice(List<Long> ids) throws SQLException {
        return mapper.listCostPrice(ids);
    }

    @Override
    public Integer batchSave(List<ReceiveBillCostItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<ReceiveBillCostItemDTO> synNcItemByIds(List<Long> ids) throws SQLException {
        return mapper.synNcItemByIds(ids);
    }

    @Override
    public List<Map<String, Object>> listInvoiceCostItemByProject(List<Long> ids, Long projectId) throws SQLException {
        return mapper.listInvoiceCostItemByProject(ids,projectId);
    }

    @Override
    public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap) {
        return mapper.getProjectWarnList(queryBillMap);
    }

    @Override
    public List<Map<String, Object>> listByPoNoAndProject(List<Long> billIds) throws SQLException {
        return mapper.listByPoNoAndProject(billIds);
    }

    @Override
    public List<ReceiveBillCostItemDTO> listReceiveCostItem(ReceiveBillDTO dto) throws SQLException {
        return mapper.listReceiveCostItem(dto);
    }

    @Override
    public List<ReceiveBillCostItemDTO> getBeVerifyCostItems(List<Long> billIds, String billType) throws SQLException {
        return mapper.getBeVerifyCostItems(billIds, billType);
    }

    @Override
    public void batchUpdate(List<ReceiveBillCostItemDTO> itemDTOList) {
        mapper.batchUpdate(itemDTOList);
    }
}