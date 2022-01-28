package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvanceBillDao;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyAdvanceDTO;
import com.topideal.entity.vo.bill.AdvanceBillModel;
import com.topideal.mapper.bill.AdvanceBillMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvanceBillDaoImpl implements AdvanceBillDao {

    @Autowired
    private AdvanceBillMapper mapper;
	
    @Override
    public void batchUpdateInvoiceStatus(List<Long> ids,String invoiceStatus) throws SQLException {
        mapper.batchUpdateInvoiceStatus(ids,invoiceStatus);
    }
    @Override
    public List<AdvanceBillDTO> listBillByRelIds(List<Long> ids) {
        return mapper.listBillByRelIds(ids);
    }
	@Override
	public List<Map<String, Object>> exportAdvanceBillMap(AdvanceBillDTO dto) throws SQLException {
		return mapper.exportAdvanceBillMap(dto);
	}
	
	@Override
	public List<Map<String, Object>> exportAdvanceBillItemMap(AdvanceBillDTO dto) throws SQLException {
		return mapper.exportAdvanceBillItemMap(dto);
	}
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvanceBillModel> list(AdvanceBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvanceBillModel model) throws SQLException {
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
    public int modify(AdvanceBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvanceBillModel  searchByPage(AdvanceBillModel  model) throws SQLException{
        PageDataModel<AdvanceBillModel> pageModel=mapper.listByPage(model);
        return (AdvanceBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvanceBillModel  searchById(Long id)throws SQLException {
        AdvanceBillModel  model=new AdvanceBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvanceBillModel searchByModel(AdvanceBillModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取分页数据
	 * @throws SQLException 
	 */
    @Override
    public AdvanceBillDTO listAdvanceBillByPage(AdvanceBillDTO dto) throws SQLException {
    	
         List<AdvanceBillDTO> listAdvanceBill= mapper.listAdvanceBill(dto);
         Integer total=mapper.listAdvanceBillCount(dto);
         dto.setTotal(total);
         dto.setList(listAdvanceBill);
         
        return dto;
    }

    @Override
    public AdvanceBillDataDTO listAddSaleOrderByPage(AdvanceBillDatasDTO dto) {
        PageDataModel<AdvanceBillDataDTO> pageModel = mapper.listAddSaleOrderByPage(dto);
        return (AdvanceBillDataDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveBillVerifyAdvanceDTO listVerifyAdvanceByPage(ReceiveBillVerifyAdvanceDTO dto) throws Exception {
        PageDataModel<ReceiveBillVerifyAdvanceDTO> pageModel = mapper.listVerifyAdvanceByPage(dto);
        return (ReceiveBillVerifyAdvanceDTO ) pageModel.getModel();
    }

    @Override
    public List<AdvanceBillModel> listByIds(List<Long> ids) {
        return mapper.listByIds(ids);
    }

    @Override
    public AdvanceBillDTO getAdvanceById(Long id) {
        return mapper.getAdvanceById(id);
    }

    @Override
    public List<AdvanceBillModel> getNcBackfillList() {
        return mapper.getNcBackfillList();
    }

    @Override
    public List<AdvanceBillModel> getNcVoucherFillBackList() {
        return mapper.getNcVoucherFillBackList();
    }

    @Override
    public int batchUpdateByIds(List<Long> ids, String billStatus) {
        return mapper.batchUpdateByIds(ids, billStatus);
    }
	

}