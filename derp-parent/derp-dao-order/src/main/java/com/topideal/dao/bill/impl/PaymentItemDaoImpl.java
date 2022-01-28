package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PaymentItemDao;
import com.topideal.entity.dto.bill.PaymentItemDTO;
import com.topideal.entity.vo.bill.PaymentItemModel;
import com.topideal.mapper.bill.PaymentItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PaymentItemDaoImpl implements PaymentItemDao {

    @Autowired
    private PaymentItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PaymentItemModel> list(PaymentItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PaymentItemModel model) throws SQLException {
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
    public int modify(PaymentItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PaymentItemModel  searchByPage(PaymentItemModel  model) throws SQLException{
        PageDataModel<PaymentItemModel> pageModel=mapper.listByPage(model);
        return (PaymentItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PaymentItemModel  searchById(Long id)throws SQLException {
        PaymentItemModel  model=new PaymentItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PaymentItemModel searchByModel(PaymentItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<PaymentItemDTO> getPaymentItemDto(PaymentItemDTO dto) {
        return mapper.getPaymentItemDto(dto);
    }
	@Override
	public Integer batchSave(List<PaymentItemModel> list) throws SQLException {
		return mapper.batchSave(list);
	}
	@Override
	public void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException {
		mapper.batchUpdatePaymentId(list,paymentId);		
	}

    @Override
    public List<PaymentItemDTO> listForExport(PaymentItemDTO itemDTO) {
        return mapper.listForExport(itemDTO);
    }

    @Override
    public int countByDTO(PaymentItemDTO itemDTO) {
        return mapper.countByDTO(itemDTO);
    }
}