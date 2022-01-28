package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.entity.dto.bill.PaymentBillDTO;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.mapper.bill.PaymentBillMapper;
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
public class PaymentBillDaoImpl implements PaymentBillDao {

    @Autowired
    private PaymentBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PaymentBillModel> list(PaymentBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PaymentBillModel model) throws SQLException {
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
    public int modify(PaymentBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PaymentBillModel  searchByPage(PaymentBillModel  model) throws SQLException{
        PageDataModel<PaymentBillModel> pageModel=mapper.listByPage(model);
        return (PaymentBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PaymentBillModel  searchById(Long id)throws SQLException {
        PaymentBillModel  model=new PaymentBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PaymentBillModel searchByModel(PaymentBillModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PaymentBillDTO getListByPage(PaymentBillDTO dto) {
		PageDataModel<PaymentBillDTO> pageModel=mapper.getListByPage(dto);
        return (PaymentBillDTO ) pageModel.getModel();
    }

    @Override
    public List<PaymentBillModel> getNcBackfillList() {
        return mapper.getNcBackfillList();
    }

    @Override
    public List<PaymentBillModel> getNcVoucherFillBackList() {
        return mapper.getNcVoucherFillBackList();
    }

    @Override
    public List<PaymentBillModel> listByDto(PaymentBillDTO dto) {
        return mapper.listByDto(dto);
    }

    @Override
    public void batchUpdate(List<PaymentBillModel> paymentBillModels) {
        mapper.batchUpdate(paymentBillModels);
    }
    
	@Override
	public Integer batchSave(List<PaymentBillModel> list) throws SQLException {
		return mapper.batchSave(list);
	}
	@Override
	public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap) {
		return mapper.getProjectWarnList(queryOrderMap);
	}

    @Override
    public int countByDTO(PaymentBillDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<PaymentBillDTO> listForExport(PaymentBillDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public PaymentBillDTO getListByPageWithItem(PaymentBillDTO dto) {
        PageDataModel<PaymentBillDTO> pageModel= mapper.getListByPageWithItem(dto);
        return (PaymentBillDTO ) pageModel.getModel();
    }

    @Override
    public PaymentBillDTO getListByPageWithCostItem(PaymentBillDTO dto) {
        PageDataModel<PaymentBillDTO> pageModel=  mapper.getListByPageWithCostItem(dto);
        return (PaymentBillDTO ) pageModel.getModel();
    }

}