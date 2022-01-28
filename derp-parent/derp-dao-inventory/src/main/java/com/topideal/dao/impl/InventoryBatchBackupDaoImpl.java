package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryBatchBackupDao;
import com.topideal.entity.vo.InventoryBatchBackupModel;
import com.topideal.mapper.InventoryBatchBackupMapper;

/**
 * 库存表-批次库存明细_备份 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryBatchBackupDaoImpl implements InventoryBatchBackupDao {

    @Autowired
    private InventoryBatchBackupMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryBatchBackupModel> list(InventoryBatchBackupModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryBatchBackupModel model) throws SQLException {
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
    public int modify(InventoryBatchBackupModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryBatchBackupModel  searchByPage(InventoryBatchBackupModel  model) throws SQLException{
        PageDataModel<InventoryBatchBackupModel> pageModel=mapper.listByPage(model);
        return (InventoryBatchBackupModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryBatchBackupModel  searchById(Long id)throws SQLException {
        InventoryBatchBackupModel  model=new InventoryBatchBackupModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryBatchBackupModel searchByModel(InventoryBatchBackupModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 库存回滚 把批次库存备份中数据移回批次库存 把剩余数量设置为0
	 */
	@Override
	public int remveBackInventoryBatchBackup(Long id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.remveBackInventoryBatchBackup(id);
	}
	
	/**
	 * 获取最小的创建日期(首次上架日期)
	 */
	@Override
	public InventoryBatchBackupModel getMinDate(InventoryBatchBackupModel model) throws Exception {
		return mapper.getMinDate(model);
	}
    
}
