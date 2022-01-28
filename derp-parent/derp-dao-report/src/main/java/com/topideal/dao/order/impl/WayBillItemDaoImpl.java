package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.WayBillItemDao;
import com.topideal.entity.dto.WayBillItemDTO;
import com.topideal.entity.vo.order.WayBillItemModel;
import com.topideal.mapper.order.WayBillItemMapper;

/**
 * 运单表体 daoImpl
 * @author lian_
 */
@Repository
public class WayBillItemDaoImpl implements WayBillItemDao {

    @Autowired
    private WayBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WayBillItemModel> list(WayBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(WayBillItemModel model) throws SQLException {
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
    public int modify(WayBillItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public WayBillItemModel  searchByPage(WayBillItemModel  model) throws SQLException{
        PageDataModel<WayBillItemModel> pageModel=mapper.listByPage(model);
        return (WayBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public WayBillItemModel  searchById(Long id)throws SQLException {
        WayBillItemModel  model=new WayBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public WayBillItemModel searchByModel(WayBillItemModel model) throws SQLException {
		return mapper.get(model);
	}
	

	/**
	 * 根据电商订单ids 获取获取运单信息和商品信息
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getList(List ids) throws SQLException {
		return mapper.getList(ids);
	}
	@Override
	public WayBillItemModel getWayBillItemByOrder(Map<String,Object> map) {
		return mapper.getWayBillItemByOrder(map);
	}
	/**
	 * 迁移数据到历史表
	 * */
	@Override
	public int synsMoveToHistory(Map<String,Object> map){
		return mapper.synsMoveToHistory(map);
	}
	/**
	 * 删除已迁移到历史表的数据
	 * */
	@Override
	public int delMoveToHistory(Map<String,Object> map){
		return mapper.delMoveToHistory(map);
	}
}