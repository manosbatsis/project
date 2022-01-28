package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnOrderDao;
import com.topideal.entity.vo.order.SaleReturnOrderModel;
import com.topideal.mapper.order.SaleReturnOrderMapper;

/**
 * 销售退货订单 impl
 * @author lchenxing
 */
@Repository
public class SaleReturnOrderDaoImpl implements SaleReturnOrderDao {

    @Autowired
    private SaleReturnOrderMapper mapper;  //销售退货订单
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnOrderModel> list(SaleReturnOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SaleReturnOrderModel
	 */
    @Override
    public Long save(SaleReturnOrderModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(SaleReturnOrderModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param SaleReturnOrderModel
     */
    @Override
    public SaleReturnOrderModel  searchByPage(SaleReturnOrderModel  model) throws SQLException{
        PageDataModel<SaleReturnOrderModel> pageModel=mapper.listByPage(model);
        return (SaleReturnOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SaleReturnOrderModel  searchById(Long id)throws SQLException {
        SaleReturnOrderModel  model=new SaleReturnOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public SaleReturnOrderModel searchByModel(SaleReturnOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public int delSaleReturnOrder(List ids) throws SQLException {
		return mapper.delSaleReturnOrder(ids);
	}

}