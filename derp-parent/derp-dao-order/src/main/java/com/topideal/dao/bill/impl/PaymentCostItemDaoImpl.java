package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PaymentCostItemDao;
import com.topideal.entity.dto.bill.PaymentCostItemDTO;
import com.topideal.entity.vo.bill.PaymentCostItemModel;
import com.topideal.mapper.bill.PaymentCostItemMapper;
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
public class PaymentCostItemDaoImpl implements PaymentCostItemDao {

    @Autowired
    private PaymentCostItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PaymentCostItemModel> list(PaymentCostItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PaymentCostItemModel model) throws SQLException {
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
    public int modify(PaymentCostItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PaymentCostItemModel  searchByPage(PaymentCostItemModel  model) throws SQLException{
        PageDataModel<PaymentCostItemModel> pageModel=mapper.listByPage(model);
        return (PaymentCostItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PaymentCostItemModel  searchById(Long id)throws SQLException {
        PaymentCostItemModel  model=new PaymentCostItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PaymentCostItemModel searchByModel(PaymentCostItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap) {
		return mapper.getProjectWarnList(queryOrderMap);
	}

    @Override
    public int countByDTO(PaymentCostItemDTO costItemDTO) {
        return mapper.countByDTO(costItemDTO);
    }

    @Override
    public List<PaymentCostItemDTO> listForExport(PaymentCostItemDTO costItemDTO) {
        return mapper.listForExport(costItemDTO);
    }

}