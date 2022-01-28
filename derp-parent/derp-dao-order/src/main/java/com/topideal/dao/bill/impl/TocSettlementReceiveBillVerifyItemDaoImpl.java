package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocSettlementReceiveBillVerifyItemDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.mapper.bill.TocSettlementReceiveBillVerifyItemMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TocSettlementReceiveBillVerifyItemDaoImpl implements TocSettlementReceiveBillVerifyItemDao {

    @Autowired
    private TocSettlementReceiveBillVerifyItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocSettlementReceiveBillVerifyItemModel> list(TocSettlementReceiveBillVerifyItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocSettlementReceiveBillVerifyItemModel model) throws SQLException {
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
    public int modify(TocSettlementReceiveBillVerifyItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocSettlementReceiveBillVerifyItemModel  searchByPage(TocSettlementReceiveBillVerifyItemModel  model) throws SQLException{
        PageDataModel<TocSettlementReceiveBillVerifyItemModel> pageModel=mapper.listByPage(model);
        return (TocSettlementReceiveBillVerifyItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocSettlementReceiveBillVerifyItemModel  searchById(Long id)throws SQLException {
        TocSettlementReceiveBillVerifyItemModel  model=new TocSettlementReceiveBillVerifyItemModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     *
     * @param model
     */
    @Override
    public TocSettlementReceiveBillVerifyItemModel searchByModel(TocSettlementReceiveBillVerifyItemModel model) throws SQLException {
        return mapper.get(model);
    }

    @Override
    public BigDecimal getAllPriceByBillIds(List<Long> idList) {
        return new BigDecimal(String.valueOf(mapper.getAllPriceByBillIds(idList)));
    }

    @Override
    public Integer batchSave(@Param("list") List<TocSettlementReceiveBillVerifyItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public Timestamp getLatestReceiveDate(Long billId) throws SQLException {
        return mapper.getLatestReceiveDate(billId);
    }

    @Override
    public List<Map<String, Object>> getVerifyAmountByBillIds(List<Long> billIds, String month) throws SQLException {
        return mapper.getVerifyAmountByBillIds(billIds, month);
    }
}