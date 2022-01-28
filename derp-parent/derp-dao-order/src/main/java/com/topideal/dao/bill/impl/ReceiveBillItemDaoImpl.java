package com.topideal.dao.bill.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillItemDao;
import com.topideal.entity.vo.bill.ReceiveBillItemModel;
import com.topideal.mapper.bill.ReceiveBillItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillItemDaoImpl implements ReceiveBillItemDao {

    @Autowired
    private ReceiveBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillItemModel> list(ReceiveBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillItemModel model) throws SQLException {
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
    public int modify(ReceiveBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillItemModel  searchByPage(ReceiveBillItemModel  model) throws SQLException{
        PageDataModel<ReceiveBillItemModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillItemModel  searchById(Long id)throws SQLException {
        ReceiveBillItemModel  model=new ReceiveBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillItemModel searchByModel(ReceiveBillItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<ReceiveBillItemDTO> itemListGroupByBillId(List<Long> billIds) throws SQLException {
        return mapper.itemListGroupByBillId(billIds);
    }

    @Override
    public int delItems(Long billId, String dataSource) throws SQLException {
        return mapper.delItems(billId, dataSource);
    }

    @Override
    public BigDecimal getTotalReceivePrice(Long billId) throws SQLException {
        return mapper.getTotalReceivePrice(billId);
    }

    @Override
    public List<Map<String, Object>> listInvoiceItem(List<Long> ids, Long merchantId, String source) throws SQLException {
        return mapper.listInvoiceItem(ids, merchantId, source);
    }

    @Override
    public Integer batchSave(List<ReceiveBillItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<Map<String, Object>> listItemPrice(List<Long> ids) throws SQLException {
        return mapper.listItemPrice(ids);
    }

    @Override
    public List<ReceiveBillItemDTO> synNcItemByIds(List<Long> ids) throws SQLException {
        return mapper.synNcItemByIds(ids);
    }

    @Override
    public List<Map<String, Object>> listInvoiceItemGroupByParentBrand(List<Long> ids) throws SQLException {
        return mapper.listInvoiceItemGroupByParentBrand(ids);
    }

    @Override
    public List<Map<String, Object>> listInvoiceItems(List<Long> billIds) throws SQLException {
        return mapper.listInvoiceItems(billIds);
    }

    @Override
    public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap) {
        return mapper.getProjectWarnList(queryBillMap);
    }

    @Override
    public List<Map<String, Object>> listByPoNo(List<Long> billIds) throws SQLException {
        return mapper.listByPoNo(billIds);
    }

    @Override
    public List<ReceiveBillItemDTO> listReceiveItem(ReceiveBillDTO dto) throws SQLException {
        return mapper.listReceiveBilltem(dto);
    }

    @Override
    public List<ReceiveBillItemDTO> listVerifyItems(ReceiveBillModel model) throws SQLException {
        return mapper.listVerifyItems(model);
    }

    @Override
    public List<Map<String, Object>> verifyItems(List<String> relCodes)  {
        return mapper.verifyItems(relCodes);
    }

    @Override
    public List<Map<String, Object>> verifyItemList(String relCode) {
        return mapper.verifyItemList(relCode);
    }

    @Override
    public void batchUpdate(List<ReceiveBillItemDTO> itemDTOList) {
        mapper.batchUpdate(itemDTOList);
    }
}