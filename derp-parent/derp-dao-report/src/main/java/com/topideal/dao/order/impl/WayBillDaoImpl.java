package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.WayBillDao;
import com.topideal.entity.vo.order.WayBillModel;
import com.topideal.mapper.order.WayBillMapper;

/**
 * 运单表 daoImpl
 * @author lian_
 */
@Repository
public class WayBillDaoImpl implements WayBillDao {

    @Autowired
    private WayBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WayBillModel> list(WayBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(WayBillModel model) throws SQLException {
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
    public int modify(WayBillModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public WayBillModel  searchByPage(WayBillModel  model) throws SQLException{
        PageDataModel<WayBillModel> pageModel=mapper.listByPage(model);
        return (WayBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public WayBillModel  searchById(Long id)throws SQLException {
        WayBillModel  model=new WayBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public WayBillModel searchByModel(WayBillModel model) throws SQLException {
		return mapper.get(model);
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