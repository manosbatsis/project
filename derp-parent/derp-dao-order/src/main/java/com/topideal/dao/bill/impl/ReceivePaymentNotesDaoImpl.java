package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceivePaymentNotesDao;
import com.topideal.entity.vo.bill.ReceivePaymentNotesModel;
import com.topideal.mapper.bill.ReceivePaymentNotesMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceivePaymentNotesDaoImpl implements ReceivePaymentNotesDao {

    @Autowired
    private ReceivePaymentNotesMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceivePaymentNotesModel> list(ReceivePaymentNotesModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceivePaymentNotesModel model) throws SQLException {
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
    public int modify(ReceivePaymentNotesModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceivePaymentNotesModel  searchByPage(ReceivePaymentNotesModel  model) throws SQLException{
        PageDataModel<ReceivePaymentNotesModel> pageModel=mapper.listByPage(model);
        return (ReceivePaymentNotesModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceivePaymentNotesModel  searchById(Long id)throws SQLException {
        ReceivePaymentNotesModel  model=new ReceivePaymentNotesModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceivePaymentNotesModel searchByModel(ReceivePaymentNotesModel model) throws SQLException {
		return mapper.get(model);
	}

}