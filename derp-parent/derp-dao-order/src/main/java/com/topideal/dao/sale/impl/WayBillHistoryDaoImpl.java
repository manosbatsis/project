package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.WayBillHistoryDao;
import com.topideal.entity.vo.sale.WayBillHistoryModel;
import com.topideal.mapper.sale.WayBillHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class WayBillHistoryDaoImpl implements WayBillHistoryDao {

    @Autowired
    private WayBillHistoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WayBillHistoryModel> list(WayBillHistoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(WayBillHistoryModel model) throws SQLException {
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
    public int modify(WayBillHistoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public WayBillHistoryModel  searchByPage(WayBillHistoryModel  model) throws SQLException{
        PageDataModel<WayBillHistoryModel> pageModel=mapper.listByPage(model);
        return (WayBillHistoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public WayBillHistoryModel  searchById(Long id)throws SQLException {
        WayBillHistoryModel  model=new WayBillHistoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public WayBillHistoryModel searchByModel(WayBillHistoryModel model) throws SQLException {
		return mapper.get(model);
	}
}