package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveInvoicenoDao;
import com.topideal.entity.vo.bill.ReceiveInvoicenoModel;
import com.topideal.mapper.bill.ReceiveInvoicenoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveInvoicenoDaoImpl implements ReceiveInvoicenoDao {

    @Autowired
    private ReceiveInvoicenoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveInvoicenoModel> list(ReceiveInvoicenoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveInvoicenoModel model) throws SQLException {
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
    public int modify(ReceiveInvoicenoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveInvoicenoModel  searchByPage(ReceiveInvoicenoModel  model) throws SQLException{
        PageDataModel<ReceiveInvoicenoModel> pageModel=mapper.listByPage(model);
        return (ReceiveInvoicenoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveInvoicenoModel  searchById(Long id)throws SQLException {
        ReceiveInvoicenoModel  model=new ReceiveInvoicenoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveInvoicenoModel searchByModel(ReceiveInvoicenoModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Long getMaxValue(String invoiceNoPrefix) throws Exception {
        return mapper.getMaxValue(invoiceNoPrefix);
    }
}