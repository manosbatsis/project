package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvancePaymentBillDao;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillExportDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;
import com.topideal.mapper.bill.AdvancePaymentBillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvancePaymentBillDaoImpl implements AdvancePaymentBillDao {

    @Autowired
    private AdvancePaymentBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvancePaymentBillModel> list(AdvancePaymentBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvancePaymentBillModel model) throws SQLException {
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
    public int modify(AdvancePaymentBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvancePaymentBillModel  searchByPage(AdvancePaymentBillModel  model) throws SQLException{
        PageDataModel<AdvancePaymentBillModel> pageModel=mapper.listByPage(model);
        return (AdvancePaymentBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvancePaymentBillModel  searchById(Long id)throws SQLException {
        AdvancePaymentBillModel  model=new AdvancePaymentBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvancePaymentBillModel searchByModel(AdvancePaymentBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public AdvancePaymentBillDTO getListByPage(AdvancePaymentBillDTO dto) {
        PageDataModel<AdvancePaymentBillDTO> pageModel=mapper.getListByPage(dto);
        return (AdvancePaymentBillDTO ) pageModel.getModel();
    }
	@Override
	public List<AdvancePaymentBillExportDTO> getExportExcel(AdvancePaymentBillExportDTO exportDTO) {
		return mapper.getExportExcel(exportDTO) ;
	}
}