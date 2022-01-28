package com.topideal.dao.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryGoodsSnapshotDao;
import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
import com.topideal.mapper.InventoryGoodsSnapshotMapper;

/**
 * 库存商品快照表 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryGoodsSnapshotDaoImpl implements InventoryGoodsSnapshotDao {

    @Autowired
    private InventoryGoodsSnapshotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryGoodsSnapshotModel> list(InventoryGoodsSnapshotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryGoodsSnapshotModel model) throws SQLException {
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
    public int modify(InventoryGoodsSnapshotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryGoodsSnapshotModel  searchByPage(InventoryGoodsSnapshotModel  model) throws SQLException{
        PageDataModel<InventoryGoodsSnapshotModel> pageModel=mapper.listByPage(model);
        return (InventoryGoodsSnapshotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryGoodsSnapshotModel  searchById(Long id)throws SQLException {
        InventoryGoodsSnapshotModel  model=new InventoryGoodsSnapshotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryGoodsSnapshotModel searchByModel(InventoryGoodsSnapshotModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	@Override
	public InventoryGoodsSnapshotDTO getlistByPage(InventoryGoodsSnapshotDTO model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<InventoryGoodsSnapshotDTO> pageModel= mapper.getlistByPage(model);
		return (InventoryGoodsSnapshotDTO) pageModel.getModel();
	}
	
	
	@Override
	public List<Map<String, Object>> exportInventoryGoodsSnapshotModelMap(InventoryGoodsSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventoryGoodsSnapshotModelMap(model);
	}
	
	
	@Override
	public int updateInventoryGoodsSnapshotAvailableNum(Map<String, Object> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.updateInventoryGoodsSnapshotAvailableNum(map);
	}
	
	@Override
	public void delData() {
		mapper.delData();
	}
	@Override
	public List<InventoryGoodsSnapshotModel> getInventoryGoodsSnapshotList(InventoryGoodsSnapshotModel model)
			throws Exception {
		return mapper.getInventoryGoodsSnapshotList(model);
	}

}
