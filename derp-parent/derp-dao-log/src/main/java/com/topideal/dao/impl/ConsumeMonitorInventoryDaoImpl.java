package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.mapper.ConsumeMonitorInventoryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorInventoryDaoImpl implements ConsumeMonitorInventoryDao {

    @Autowired
    private ConsumeMonitorInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorInventoryModel> list(ConsumeMonitorInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorInventoryModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 批量删除
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
    public int modify(ConsumeMonitorInventoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorInventoryModel  searchByPage(ConsumeMonitorInventoryModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorInventoryModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorInventoryModel  searchById(Long id)throws SQLException {
        ConsumeMonitorInventoryModel  model=new ConsumeMonitorInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorInventoryModel searchByModel(ConsumeMonitorInventoryModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取库存扣减失败需要重推的信息
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<ConsumeMonitorInventoryModel> getLowerFailList() {
		return mapper.getLowerFailList(); 
	}
	
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorInventoryModel getListByMonth(ConsumeMonitorInventoryModel model) throws SQLException {
		PageDataModel<ConsumeMonitorInventoryModel> pageModel = mapper.getListByMonthByPage(model);
		return (ConsumeMonitorInventoryModel) pageModel.getModel();  
	}
	
	/**整合web*/
	@Override
	public ConsumeMonitorMqDTO getListByPage(ConsumeMonitorMqDTO model) throws SQLException {
		PageDataModel<ConsumeMonitorMqDTO> pageModel = mapper.getListByPage(model);
		return (ConsumeMonitorMqDTO) pageModel.getModel();
	}
	@Override
	public Long getCount(ConsumeMonitorMqModel model) throws SQLException {
		return mapper.getCount(model);
	}
	@Override
	public List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException {
		return mapper.getExportList(model);
	}
	@Override
	public List<Map<String, Object>> getRollbackList(Map<String, Object> map) {
		return mapper.getRollbackList(map);
	}
	@Override
	public List<Map<String, Object>> getInventoryRollbackCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.getInventoryRollbackCount(map);
	}
	@Override
	public List<Map<String, Object>> getOrderRollbackCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.getOrderRollbackCount(map);
	}
}