package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.JdPurchaseOrderDao;
import com.topideal.entity.vo.sale.JdPurchaseOrderModel;
import com.topideal.mapper.sale.JdPurchaseOrderMapper;
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
public class JdPurchaseOrderDaoImpl implements JdPurchaseOrderDao {

    @Autowired
    private JdPurchaseOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<JdPurchaseOrderModel> list(JdPurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(JdPurchaseOrderModel model) throws SQLException {
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
    public int modify(JdPurchaseOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public JdPurchaseOrderModel  searchByPage(JdPurchaseOrderModel  model) throws SQLException{
        PageDataModel<JdPurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (JdPurchaseOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public JdPurchaseOrderModel  searchById(Long id)throws SQLException {
        JdPurchaseOrderModel  model=new JdPurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public JdPurchaseOrderModel searchByModel(JdPurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 分页获取平台采购订单 
	 */
	@Override
	public List<JdPurchaseOrderModel> getPlatformPurchaseList(Map<String, Object> paramMap) throws Exception {
		return mapper.getPlatformPurchaseList(paramMap);
	}

}