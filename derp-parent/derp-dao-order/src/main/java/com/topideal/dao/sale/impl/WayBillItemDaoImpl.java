package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.WayBillItemDao;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.entity.vo.sale.WayBillItemModel;
import com.topideal.mapper.sale.WayBillItemMapper;

/**
 * 运单表体 impl
 * @author lchenxing
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
	 * @param WayBillItemModel
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
    public int modify(WayBillItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param WayBillItemModel
     */
    @Override
    public WayBillItemModel  searchByPage(WayBillItemModel  model) throws SQLException{
        PageDataModel<WayBillItemModel> pageModel=mapper.listByPage(model);
        return (WayBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public WayBillItemModel  searchById(Long id)throws SQLException {
        WayBillItemModel  model=new WayBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public WayBillItemModel searchByModel(WayBillItemModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public List<WayBillItemDTO> listWayBillItemByOrderId(Long id) {
		return mapper.listWayBillItemByOrderId(id);
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
