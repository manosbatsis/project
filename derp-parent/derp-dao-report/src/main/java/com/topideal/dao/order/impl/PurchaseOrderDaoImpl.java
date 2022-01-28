package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.entity.dto.PurchaseOrderDTO;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.mapper.order.PurchaseOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单 impl
 * @author zhanghx
 */
@Repository
public class PurchaseOrderDaoImpl implements PurchaseOrderDao {

	//采购订单mapper
    @Autowired
    private PurchaseOrderMapper mapper;
	
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseOrderModel model) throws SQLException {
    	model.setCreateDate(TimeUtils.getNow());
    	model.setModifyDate(TimeUtils.getNow());
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
    public int modify(PurchaseOrderModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseOrderModel  searchByPage(PurchaseOrderModel  model) throws SQLException{
        PageDataModel<PurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (PurchaseOrderModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseOrderModel  searchById(Long id)throws SQLException {
        PurchaseOrderModel  model=new PurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     * @param model
     * */
	@Override
	public PurchaseOrderModel searchByModel(PurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseOrderModel> list(PurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 根据id获取供应商
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseOrderModel> getSupplierIdById(Long id) throws SQLException {
		return mapper.getSupplierIdById(id);
	}

	@Override
	public List<Map<String, Object>> getDepotVeriClByParam(Map<String, Object> param) {
		return mapper.getDepotVeriClByParam(param);
	}

	/**
	 * 报表API获取采购订单
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<PurchaseOrderDTO> getRepotApiList(Map<String, Object> queryMap) {
		return mapper.getRepotApiList(queryMap);
	}

	@Override
	public Integer getRepotApiListCount(Map<String, Object> queryMap) {
		return mapper.getRepotApiListCount(queryMap);
	}

	@Override
	public List<PurchaseOrderDTO> getNoTgtAmountOrder() {
		return mapper.getNoTgtAmountOrder();
	}
	/**查询时间范围内有修改的采购单号
	 * */
	@Override
	public List<String> getPurchaseCodeList(Map<String,Object> map){
		return mapper.getPurchaseCodeList(map);
	}
	/**根据单号查询订单
	 * */
	@Override
	public List<Map<String,Object>> getPurchaseByCodeList(Map<String,Object> map){
		return mapper.getPurchaseByCodeList(map);
	}
	/**根据单号查询订单表体
	 * */
	@Override
	public List<Map<String,Object>> getPurchaseItemByCodeList(Map<String,Object> map){
		return mapper.getPurchaseItemByCodeList(map);
	}
}
