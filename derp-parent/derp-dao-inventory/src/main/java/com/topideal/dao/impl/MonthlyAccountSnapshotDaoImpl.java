package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.MonthlyAccountSnapshotDao;
import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.mapper.MonthlyAccountSnapshotMapper;

/**
 * 月结账单快照 daoImpl
 * @author lchenxing
 */
@Repository
public class MonthlyAccountSnapshotDaoImpl implements MonthlyAccountSnapshotDao {

    @Autowired
    private MonthlyAccountSnapshotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MonthlyAccountSnapshotModel> list(MonthlyAccountSnapshotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MonthlyAccountSnapshotModel model) throws SQLException {
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
    public int modify(MonthlyAccountSnapshotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MonthlyAccountSnapshotModel  searchByPage(MonthlyAccountSnapshotModel  model) throws SQLException{
        PageDataModel<MonthlyAccountSnapshotModel> pageModel=mapper.listByPage(model);
        return (MonthlyAccountSnapshotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MonthlyAccountSnapshotModel  searchById(Long id)throws SQLException {
        MonthlyAccountSnapshotModel  model=new MonthlyAccountSnapshotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MonthlyAccountSnapshotModel searchByModel(MonthlyAccountSnapshotModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	@Override
	public MonthlyAccountSnapshotModel getlistByPage(MonthlyAccountSnapshotModel model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<MonthlyAccountSnapshotModel> pageModel=mapper.listByPage(model);
        return (MonthlyAccountSnapshotModel ) pageModel.getModel();
	}
	@Override
	public MonthlyAccountSnapshotDTO getlistDTOByPage(MonthlyAccountSnapshotDTO model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<MonthlyAccountSnapshotDTO> pageModel=mapper.listDTOByPage(model);
        return (MonthlyAccountSnapshotDTO ) pageModel.getModel();
	}
	
	@Override
	public List<Map<String, Object>> exportMonthlyAccountSnapshotModelMap(MonthlyAccountSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportMonthlyAccountSnapshotModelMap(model);
	}
    
}
