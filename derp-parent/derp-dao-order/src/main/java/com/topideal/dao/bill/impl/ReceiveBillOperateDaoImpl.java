package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveBillOperateDao;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.mapper.bill.ReceiveBillOperateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveBillOperateDaoImpl implements ReceiveBillOperateDao {

    @Autowired
    private ReceiveBillOperateMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveBillOperateModel> list(ReceiveBillOperateModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveBillOperateModel model) throws SQLException {
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
    public int modify(ReceiveBillOperateModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveBillOperateModel  searchByPage(ReceiveBillOperateModel  model) throws SQLException{
        PageDataModel<ReceiveBillOperateModel> pageModel=mapper.listByPage(model);
        return (ReceiveBillOperateModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveBillOperateModel  searchById(Long id)throws SQLException {
        ReceiveBillOperateModel  model=new ReceiveBillOperateModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveBillOperateModel searchByModel(ReceiveBillOperateModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ReceiveBillOperateModel getLatestOperate(String operateNode, List<Long> billIds) {
        return mapper.getLatestOperate(operateNode, billIds);
    }
}