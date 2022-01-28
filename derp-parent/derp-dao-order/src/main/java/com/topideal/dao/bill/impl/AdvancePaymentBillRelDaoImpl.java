package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvancePaymentBillRelDao;
import com.topideal.entity.vo.bill.AdvancePaymentBillRelModel;
import com.topideal.mapper.bill.AdvancePaymentBillRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvancePaymentBillRelDaoImpl implements AdvancePaymentBillRelDao {

    @Autowired
    private AdvancePaymentBillRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvancePaymentBillRelModel> list(AdvancePaymentBillRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvancePaymentBillRelModel model) throws SQLException {
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
    public int modify(AdvancePaymentBillRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvancePaymentBillRelModel  searchByPage(AdvancePaymentBillRelModel  model) throws SQLException{
        PageDataModel<AdvancePaymentBillRelModel> pageModel=mapper.listByPage(model);
        return (AdvancePaymentBillRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvancePaymentBillRelModel  searchById(Long id)throws SQLException {
        AdvancePaymentBillRelModel  model=new AdvancePaymentBillRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvancePaymentBillRelModel searchByModel(AdvancePaymentBillRelModel model) throws SQLException {
		return mapper.get(model);
	}

}