package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.InventoryRealTimeSnapshotDao;
import com.topideal.entity.vo.reporting.InventoryRealTimeSnapshotModel;
import com.topideal.mapper.reporting.InventoryRealTimeSnapshotMapper;

/**
 * 实时库存快照表 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryRealTimeSnapshotDaoImpl implements InventoryRealTimeSnapshotDao {

    @Autowired
    private InventoryRealTimeSnapshotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryRealTimeSnapshotModel> list(InventoryRealTimeSnapshotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryRealTimeSnapshotModel model) throws SQLException {
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
    public int modify(InventoryRealTimeSnapshotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryRealTimeSnapshotModel  searchByPage(InventoryRealTimeSnapshotModel  model) throws SQLException{
        PageDataModel<InventoryRealTimeSnapshotModel> pageModel=mapper.listByPage(model);
        return (InventoryRealTimeSnapshotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryRealTimeSnapshotModel  searchById(Long id)throws SQLException {
        InventoryRealTimeSnapshotModel  model=new InventoryRealTimeSnapshotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryRealTimeSnapshotModel searchByModel(InventoryRealTimeSnapshotModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 检查商家指定日期是否存在快照
     * */
    public Integer countSnapshotByDate(Map<String, Object> map){
    	return mapper.countSnapshotByDate(map);
    }

    
    /**
     * 删除三个月前且不包含1号的数据 
     */
    @Override
    public void delData(){
    	mapper.delData();
    }

	@Override
	public List<Map<String, Object>> getInventoryFallingPriceReserves(Map<String, Object> queryMap) {
		return mapper.getInventoryFallingPriceReserves(queryMap);
	}
	@Override
	public List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> inventoryParams) {
		return mapper.getMonthlyAutoVeriListGroupByBarcode(inventoryParams);
	}
}
