package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvancePaymentBillItemDao;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillItemModel;
import com.topideal.mapper.bill.AdvancePaymentBillItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvancePaymentBillItemDaoImpl implements AdvancePaymentBillItemDao {

    @Autowired
    private AdvancePaymentBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvancePaymentBillItemModel> list(AdvancePaymentBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvancePaymentBillItemModel model) throws SQLException {
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
    public int modify(AdvancePaymentBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvancePaymentBillItemModel  searchByPage(AdvancePaymentBillItemModel  model) throws SQLException{
        PageDataModel<AdvancePaymentBillItemModel> pageModel=mapper.listByPage(model);
        return (AdvancePaymentBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvancePaymentBillItemModel  searchById(Long id)throws SQLException {
        AdvancePaymentBillItemModel  model=new AdvancePaymentBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvancePaymentBillItemModel searchByModel(AdvancePaymentBillItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<AdvancePaymentBillItemDTO> getVeriAdvancePaymentList(AdvancePaymentBillDTO billDTO) {
		List<AdvancePaymentBillItemDTO> list=mapper.getVeriAdvancePaymentList(billDTO);
        return list ;
	}

}