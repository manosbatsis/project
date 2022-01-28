package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocSettlementReceiveBillAuditItemDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.mapper.bill.TocSettlementReceiveBillAuditItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TocSettlementReceiveBillAuditItemDaoImpl implements TocSettlementReceiveBillAuditItemDao {

    @Autowired
    private TocSettlementReceiveBillAuditItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocSettlementReceiveBillAuditItemModel> list(TocSettlementReceiveBillAuditItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocSettlementReceiveBillAuditItemModel model) throws SQLException {
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
    public int modify(TocSettlementReceiveBillAuditItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocSettlementReceiveBillAuditItemModel  searchByPage(TocSettlementReceiveBillAuditItemModel  model) throws SQLException{
        PageDataModel<TocSettlementReceiveBillAuditItemModel> pageModel=mapper.listByPage(model);
        return (TocSettlementReceiveBillAuditItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocSettlementReceiveBillAuditItemModel  searchById(Long id)throws SQLException {
        TocSettlementReceiveBillAuditItemModel  model=new TocSettlementReceiveBillAuditItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocSettlementReceiveBillAuditItemModel searchByModel(TocSettlementReceiveBillAuditItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int updateAuditItem(TocSettlementReceiveBillAuditItemModel model) throws SQLException {
        return mapper.updateAuditItem(model);
    }

    @Override
    public Timestamp getMaxAuditDate(Long billId) throws SQLException {
        return mapper.getMaxAuditDate(billId);
    }
}