package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseInvoiceDao;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;
import com.topideal.mapper.purchase.PurchaseInvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseInvoiceDaoImpl implements PurchaseInvoiceDao {

    @Autowired
    private PurchaseInvoiceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseInvoiceModel> list(PurchaseInvoiceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseInvoiceModel model) throws SQLException {
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
    public int modify(PurchaseInvoiceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseInvoiceModel  searchByPage(PurchaseInvoiceModel  model) throws SQLException{
        PageDataModel<PurchaseInvoiceModel> pageModel=mapper.listByPage(model);
        return (PurchaseInvoiceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseInvoiceModel  searchById(Long id)throws SQLException {
        PurchaseInvoiceModel  model=new PurchaseInvoiceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseInvoiceModel searchByModel(PurchaseInvoiceModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public PurchaseInvoiceDTO getListByPage(PurchaseInvoiceDTO dto) {
		PageDataModel<PurchaseInvoiceDTO> pageModel=mapper.getListByPage(dto);
        return (PurchaseInvoiceDTO ) pageModel.getModel();
	}

	@Override
	public List<PurchaseInvoiceModel> getPurchaseInvoiceByPayDate() {
		return mapper.getPurchaseInvoiceByPayDate();
	}

	@Override
	public List<PurchaseInvoiceModel> getInvoiceByIds(List<Long> ids) {
		return mapper.getInvoiceByIds(ids);
	}
}