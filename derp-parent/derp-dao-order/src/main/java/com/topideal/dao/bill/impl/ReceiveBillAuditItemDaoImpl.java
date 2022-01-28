package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillAuditItemDao;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.mapper.bill.ReceiveBillAuditItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillAuditItemDaoImpl implements ReceiveBillAuditItemDao {

    @Autowired
    private ReceiveBillAuditItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillAuditItemModel> list(ReceiveBillAuditItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillAuditItemModel model) throws SQLException {
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
    public int modify(ReceiveBillAuditItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillAuditItemModel  searchByPage(ReceiveBillAuditItemModel  model) throws SQLException{
        PageDataModel<ReceiveBillAuditItemModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillAuditItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillAuditItemModel  searchById(Long id)throws SQLException {
        ReceiveBillAuditItemModel  model=new ReceiveBillAuditItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillAuditItemModel searchByModel(ReceiveBillAuditItemModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public int delete(List ids) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.batchDel(ids);
	}

    @Override
    public int updateAuditItem(ReceiveBillAuditItemModel model) throws SQLException {
        return mapper.updateAuditItem(model);
    }

    @Override
    public Timestamp getMaxAuditDate(List<Long> billIds) throws SQLException {
        return mapper.getMaxAuditDate(billIds);
    }

    @Override
    public ReceiveBillAuditItemModel getLastAuditRecord(Long billId) {
        return mapper.getLastAuditRecord(billId);
    }
}