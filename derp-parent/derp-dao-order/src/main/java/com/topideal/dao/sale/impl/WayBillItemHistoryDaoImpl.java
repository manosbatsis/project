package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.WayBillItemHistoryDao;
import com.topideal.entity.dto.sale.WayBillItemHistoryDTO;
import com.topideal.entity.vo.sale.WayBillItemHistoryModel;
import com.topideal.mapper.sale.WayBillItemHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class WayBillItemHistoryDaoImpl implements WayBillItemHistoryDao {

    @Autowired
    private WayBillItemHistoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WayBillItemHistoryModel> list(WayBillItemHistoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(WayBillItemHistoryModel model) throws SQLException {
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
    public int modify(WayBillItemHistoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public WayBillItemHistoryModel  searchByPage(WayBillItemHistoryModel  model) throws SQLException{
        PageDataModel<WayBillItemHistoryModel> pageModel=mapper.listByPage(model);
        return (WayBillItemHistoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public WayBillItemHistoryModel  searchById(Long id)throws SQLException {
        WayBillItemHistoryModel  model=new WayBillItemHistoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public WayBillItemHistoryModel searchByModel(WayBillItemHistoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<WayBillItemHistoryDTO> listWayBillItemByOrderId(Long id) {
        return null;
    }
}