package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.BillOutinDepotDao;
import com.topideal.entity.vo.order.BillOutinDepotModel;
import com.topideal.mapper.order.BillOutinDepotMapper;
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
public class BillOutinDepotDaoImpl implements BillOutinDepotDao {

    @Autowired
    private BillOutinDepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BillOutinDepotModel> list(BillOutinDepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BillOutinDepotModel model) throws SQLException {
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
    public int modify(BillOutinDepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BillOutinDepotModel  searchByPage(BillOutinDepotModel  model) throws SQLException{
        PageDataModel<BillOutinDepotModel> pageModel=mapper.listByPage(model);
        return (BillOutinDepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BillOutinDepotModel  searchById(Long id)throws SQLException {
        BillOutinDepotModel  model=new BillOutinDepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BillOutinDepotModel searchByModel(BillOutinDepotModel model) throws SQLException {
		return mapper.get(model);
	}


    @Override
    public List<Map<String, Object>> countByCustomer(Map<String, Object> params) throws SQLException {
        return mapper.countByCustomer(params);
    }

    @Override
    public List<Map<String, Object>> getBillOutDepotTop10ByBrand(Map<String, Object> params) throws SQLException {
        return mapper.getBillOutDepotTop10ByBrand(params);
    }
}