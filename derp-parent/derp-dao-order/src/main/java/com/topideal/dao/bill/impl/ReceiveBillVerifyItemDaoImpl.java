package com.topideal.dao.bill.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillVerifyItemDao;
import com.topideal.entity.dto.bill.ReceiveBillVerifyItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.mapper.bill.ReceiveBillVerifyItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillVerifyItemDaoImpl implements ReceiveBillVerifyItemDao {

    @Autowired
    private ReceiveBillVerifyItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillVerifyItemModel> list(ReceiveBillVerifyItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillVerifyItemModel model) throws SQLException {
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
    public int modify(ReceiveBillVerifyItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillVerifyItemModel  searchByPage(ReceiveBillVerifyItemModel  model) throws SQLException{
        PageDataModel<ReceiveBillVerifyItemModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillVerifyItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillVerifyItemModel  searchById(Long id)throws SQLException {
        ReceiveBillVerifyItemModel  model=new ReceiveBillVerifyItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillVerifyItemModel searchByModel(ReceiveBillVerifyItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BigDecimal getTotalVerifyPrice(Long billId) throws SQLException {
        return mapper.getTotalVerifyPrice(billId);
    }

    @Override
    public List<Map<String, Object>> listVerifyPrice(List<Long> ids) throws SQLException {
        return mapper.listVerifyPrice(ids);
    }

    @Override
    public void delVerify(ReceiveBillVerifyItemModel verifyItemModel) throws SQLException {
        mapper.delVerify(verifyItemModel);
    }

  /*  @Override
    public List<Long> getProjectQuatoReceiveId(Map<String, Object> queryBillMap) {
        return mapper.getProjectQuatoReceiveId(queryBillMap);
    }*/

    @Override
    public Timestamp getLatestVerifyDate(Long billId) {
        return mapper.getLatestVerifyDate(billId);
    }

    @Override
    public Integer batchSave(List<ReceiveBillVerifyItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<Map<String, Object>> getTotalVerifyPriceByAdvanceId(List<Long> advanceIds) throws SQLException {
        return mapper.getTotalVerifyPriceByAdvanceId(advanceIds);
    }

    @Override
    public List<Map<String, Object>> getVerifyAmountByBillIds(List<Long> billIds, String month) throws SQLException {
        return mapper.getVerifyAmountByBillIds(billIds, month);
    }

    @Override
    public List<ReceiveBillVerifyItemModel> listByBillIds(List<Long> billIds) {
        return mapper.listByBillIds(billIds);
    }

    @Override
	public List<ReceiveBillVerifyItemDTO> getReceiveBillVerifyItem(ReceiveBillVerifyItemDTO dto)throws SQLException {		
		return mapper.getReceiveBillVerifyItem(dto);
	}

}