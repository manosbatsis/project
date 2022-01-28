package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.TransferInDepotItemDao;
import com.topideal.entity.vo.order.TransferInDepotItemModel;
import com.topideal.mapper.order.TransferInDepotItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库表体 daoImpl
 * @author lian_
 */
@Repository
public class TransferInDepotItemDaoImpl implements TransferInDepotItemDao {

    @Autowired
    private TransferInDepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TransferInDepotItemModel> list(TransferInDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TransferInDepotItemModel model) throws SQLException {
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
    public int modify(TransferInDepotItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TransferInDepotItemModel  searchByPage(TransferInDepotItemModel  model) throws SQLException{
        PageDataModel<TransferInDepotItemModel> pageModel=mapper.listByPage(model);
        return (TransferInDepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TransferInDepotItemModel  searchById(Long id)throws SQLException {
        TransferInDepotItemModel  model=new TransferInDepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override  
	public TransferInDepotItemModel searchByModel(TransferInDepotItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据调拨ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getList(List ids) throws SQLException {
		return mapper.getList(ids);
	}
	@Override
	public Integer getVIPInDepotAccount(Map<String, Object> queryMap) {
		return mapper.getVIPInDepotAccount(queryMap);
	}
	@Override
	public List<Map<String, Object>> getVIPInDepotDetails(Map<String, Object> queryMap) {
		return mapper.getVIPInDepotDetails(queryMap);
	}

}