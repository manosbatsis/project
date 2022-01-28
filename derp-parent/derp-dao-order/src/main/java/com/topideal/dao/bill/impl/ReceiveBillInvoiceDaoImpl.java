package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillInvoiceDao;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import com.topideal.mapper.bill.ReceiveBillInvoiceMapper;
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
public class ReceiveBillInvoiceDaoImpl implements ReceiveBillInvoiceDao {

    @Autowired
    private ReceiveBillInvoiceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillInvoiceModel> list(ReceiveBillInvoiceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillInvoiceModel model) throws SQLException {
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
    public int modify(ReceiveBillInvoiceModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillInvoiceModel  searchByPage(ReceiveBillInvoiceModel  model) throws SQLException{
        PageDataModel<ReceiveBillInvoiceModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillInvoiceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillInvoiceModel  searchById(Long id)throws SQLException {
        ReceiveBillInvoiceModel  model=new ReceiveBillInvoiceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillInvoiceModel searchByModel(ReceiveBillInvoiceModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ReceiveBillInvoiceDTO searchDTOByPage(ReceiveBillInvoiceDTO dto) {
        PageDataModel<ReceiveBillInvoiceDTO> pageModel= mapper.searchDTOByPage(dto);
        return (ReceiveBillInvoiceDTO)pageModel.getModel() ;
    }

    @Override
    public List<Map<String,Object>> listForExport(ReceiveBillInvoiceDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public List<ReceiveBillInvoiceDTO> listDTO(ReceiveBillInvoiceDTO dto) throws SQLException {
        return mapper.listDTO(dto);
    }

    @Override
    public List<ReceiveBillInvoiceModel> searchByRelCodes(List<String> statementCodes) throws SQLException {
        return mapper.searchByRelCodes(statementCodes);
    }

    @Override
    public ReceiveBillInvoiceDTO searchByDTO(ReceiveBillInvoiceDTO dto) throws SQLException {
        return mapper.searchByDTO(dto);
    }
}