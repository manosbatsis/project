package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PaymentSummaryDao;
import com.topideal.entity.vo.bill.PaymentSummaryModel;
import com.topideal.mapper.bill.PaymentSummaryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PaymentSummaryDaoImpl implements PaymentSummaryDao {

    @Autowired
    private PaymentSummaryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PaymentSummaryModel> list(PaymentSummaryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PaymentSummaryModel model) throws SQLException {
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
    public int modify(PaymentSummaryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PaymentSummaryModel  searchByPage(PaymentSummaryModel  model) throws SQLException{
        PageDataModel<PaymentSummaryModel> pageModel=mapper.listByPage(model);
        return (PaymentSummaryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PaymentSummaryModel  searchById(Long id)throws SQLException {
        PaymentSummaryModel  model=new PaymentSummaryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PaymentSummaryModel searchByModel(PaymentSummaryModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public Integer batchSave(List<PaymentSummaryModel> list) throws SQLException {
		return mapper.batchSave(list);
	}
	@Override
	public void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException {
		mapper.batchUpdatePaymentId(list,paymentId);		
	}

}