package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseInvoiceItemDao;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceItemModel;
import com.topideal.mapper.purchase.PurchaseInvoiceItemMapper;
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
public class PurchaseInvoiceItemDaoImpl implements PurchaseInvoiceItemDao {

    @Autowired
    private PurchaseInvoiceItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseInvoiceItemModel> list(PurchaseInvoiceItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseInvoiceItemModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(PurchaseInvoiceItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseInvoiceItemModel  searchByPage(PurchaseInvoiceItemModel  model) throws SQLException{
        PageDataModel<PurchaseInvoiceItemModel> pageModel=mapper.listByPage(model);
        return (PurchaseInvoiceItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseInvoiceItemModel  searchById(Long id)throws SQLException {
        PurchaseInvoiceItemModel  model=new PurchaseInvoiceItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseInvoiceItemModel searchByModel(PurchaseInvoiceItemModel model) throws SQLException {
		return mapper.get(model);
	}
   
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public PurchaseInvoiceItemModel getInvoiceNum(Map<String, Object> queryMap) {
		return mapper.getInvoiceNum(queryMap);
	}

	@Override
	public List<PurchaseInvoiceItemDTO> getPurchaseInvoiceItemModel(PurchaseInvoiceItemDTO purchaseInvoiceItemModel) {
		return mapper.getPurchaseInvoiceItemModel(purchaseInvoiceItemModel);
	}

	@Override
	public void batchDelByInvoiceIds(List<Long> invoiceIds) {
		mapper.batchDelByInvoiceIds(invoiceIds);
	}


}