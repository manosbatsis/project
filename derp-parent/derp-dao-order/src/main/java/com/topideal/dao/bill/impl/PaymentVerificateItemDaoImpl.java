package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PaymentVerificateItemDao;
import com.topideal.entity.vo.bill.PaymentVerificateItemModel;
import com.topideal.mapper.bill.PaymentVerificateItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PaymentVerificateItemDaoImpl implements PaymentVerificateItemDao {

    @Autowired
    private PaymentVerificateItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PaymentVerificateItemModel> list(PaymentVerificateItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PaymentVerificateItemModel model) throws SQLException {
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
    public int modify(PaymentVerificateItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PaymentVerificateItemModel  searchByPage(PaymentVerificateItemModel  model) throws SQLException{
        PageDataModel<PaymentVerificateItemModel> pageModel=mapper.listByPage(model);
        return (PaymentVerificateItemModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     *
     * @param id
     */
    @Override
    public PaymentVerificateItemModel searchById(Long id) throws SQLException {
        PaymentVerificateItemModel model = new PaymentVerificateItemModel();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     *
     * @param model
     */
    @Override
    public PaymentVerificateItemModel searchByModel(PaymentVerificateItemModel model) throws SQLException {
        return mapper.get(model);
    }

    @Override
    public Integer batchSave(List<PaymentVerificateItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }
    
	@Override
	public void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException {
		mapper.batchUpdatePaymentId(list,paymentId);		
	}
}