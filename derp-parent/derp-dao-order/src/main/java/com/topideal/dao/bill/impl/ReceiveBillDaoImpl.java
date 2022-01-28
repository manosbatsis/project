package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.mapper.bill.ReceiveBillMapper;
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
public class ReceiveBillDaoImpl implements ReceiveBillDao {

    @Autowired
    private ReceiveBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillModel> list(ReceiveBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillModel model) throws SQLException {
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
    public int modify(ReceiveBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillModel  searchByPage(ReceiveBillModel  model) throws SQLException{
        PageDataModel<ReceiveBillModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillModel  searchById(Long id)throws SQLException {
        ReceiveBillModel  model=new ReceiveBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillModel searchByModel(ReceiveBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ReceiveBillDTO listReceiveBillByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listReceiveBillByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public List<Map<String, Object>> receiveTotalById(Long id) throws SQLException {
        return mapper.receiveTotalById(id);
    }

    @Override
    public List<Map<String, Object>> deductionTotalById(Long id) throws SQLException {
        return mapper.deductionTotalById(id);
    }

    @Override
    public ReceiveBillDTO searchDTOById(Long id) throws SQLException {
        return mapper.searchDTOById(id);
    }
    /**
     * 查询已审核的应收账单
     */
	@Override
	public List<ReceiveBillModel> getReceiveBillList(Map<String, Object> map) throws SQLException {
		return mapper.getReceiveBillList(map);
	}
	  /**
     * 获取已经作废/删除的应收账单的收款核销
     */
	@Override
	public List<ReceiveBillModel> getZfAnd006ReceiveBill(List<String> receiveCodes) throws SQLException {
		return mapper.getZfAnd006ReceiveBill(receiveCodes);
	}
	@Override
	public List<Map<String, Object>> getSumReceivePrice(Long id) throws SQLException {
		return mapper.getSumReceivePrice(id);
	}
	@Override
	public List<Map<String, Object>> getSumCollectedPrice(Long id) throws SQLException {
		return mapper.getSumCollectedPrice(id);
	}

    @Override
    public List<Long> getBuList(List<Long> ids, Long merchantId) throws SQLException {
        return mapper.getBuList(ids, merchantId);
    }

    @Override
    public List<ReceiveBillDTO> listBillByRelIds(List<Long> ids) {
        return mapper.listBillByRelIds(ids);
    }

    @Override
    public void batchUpdateBillStatus(List<Long> ids, String billStatus, String invoiceStatus) throws SQLException {
        mapper.batchUpdateBillStatus(ids, billStatus, invoiceStatus);
    }

    @Override
    public ReceiveBillDTO listAddShelfOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddShelfOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveBillDTO listAddBillOutOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddBillOutOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveBillDTO listAddPreOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddPreOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveBillDTO listAddSaleOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddSaleOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public void batchUpdateInvoiceStatus(List<Long> ids, String invoiceStatus) throws SQLException {
        mapper.batchUpdateInvoiceStatus(ids, invoiceStatus);
    }
	@Override
	public List<ReceiveBillModel> getNcBackfillList() {
		return mapper.getNcBackfillList();
	}

    @Override
    public List<ReceiveBillModel> getRelBill(String code) {
        return mapper.getRelBill(code);
    }

    @Override
    public List<ReceiveBillDTO> listReceiveBill(ReceiveBillDTO dto) throws SQLException {
        return mapper.listReceiveBill(dto);
    }

    @Override
    public List<ReceiveBillModel> getNcVoucherFillBackList() {
        return mapper.getNcVoucherFillBackList();
    }

    @Override
    public ReceiveBillDTO listAddPurchaseSDOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddPurchaseSDOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public SaleReturnIdepotDTO listAddSaleReturnOrderByPage(SaleReturnIdepotDTO dto) throws SQLException {
        PageDataModel<SaleReturnIdepotDTO> pageModel = mapper.listAddSaleReturnOrderByPage(dto);
        return (SaleReturnIdepotDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveBillDTO listAddFinancingOrderByPage(ReceiveBillDTO dto) throws SQLException {
        PageDataModel<ReceiveBillDTO> pageModel = mapper.listAddFinancingOrderByPage(dto);
        return (ReceiveBillDTO ) pageModel.getModel();
    }

    /*@Override
    public List<Long> getProjectQuatoReceiveId(Map<String, Object> queryBillMap) {
        return mapper.getProjectQuatoReceiveId(queryBillMap);
    }*/
    /**
     * 待确认
     */
	@Override
	public List<Map<String, Object>> getNoConfirmAmount(Map<String, Object> map) throws SQLException {
		return mapper.getNoConfirmAmount(map);
	}
	/**
     * 待开票
     */
	@Override
	public List<Map<String, Object>> getNoInvoiceAmount(Map<String, Object> map) throws SQLException {
		return mapper.getNoInvoiceAmount(map);
	}
	/**
     * 待回款
     */
	@Override
	public List<Map<String, Object>> getNoReturnAmount(Map<String, Object> map) throws SQLException {
		return mapper.getNoReturnAmount(map);
	}

    /**
     * 已回款
     * @param map
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> getReturnAmount(Map<String, Object> map) throws SQLException {
        return mapper.getReturnAmount(map);
    }

    @Override
    public List<ReceiveBillModel> getAuditBills(ReceiveBillModel model) {
        return mapper.getAuditBills(model);
    }

    @Override
    public List<ReceiveBillModel> getBillsByCodesAndStatus(String receiveCodes, Long merchantId) {
        return mapper.getBillsByCodesAndStatus(receiveCodes, merchantId);
    }

    @Override
    public List<ReceiveBillModel> listDTO(ReceiveBillDTO dto) throws SQLException {
        return mapper.listDTO(dto);
    }

    @Override
    public List<ReceiveBillModel> listByMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException {
        return mapper.listByMonthlySnapshot(merchantId, buId, month);
    }

    @Override
    public List<ReceiveBillModel> listByAllVerifyMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException {
        return mapper.listByAllVerifyMonthlySnapshot(merchantId, buId, month);
    }
}