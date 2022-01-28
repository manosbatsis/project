package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorHistoryInventoryDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.mapper.ConsumeMonitorHistoryInventoryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorHistoryInventoryDaoImpl implements ConsumeMonitorHistoryInventoryDao {

    @Autowired
    private ConsumeMonitorHistoryInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorHistoryInventoryModel> list(ConsumeMonitorHistoryInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorHistoryInventoryModel model) throws SQLException {
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
    public int modify(ConsumeMonitorHistoryInventoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorHistoryInventoryModel  searchByPage(ConsumeMonitorHistoryInventoryModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorHistoryInventoryModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorHistoryInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorHistoryInventoryModel  searchById(Long id)throws SQLException {
        ConsumeMonitorHistoryInventoryModel  model=new ConsumeMonitorHistoryInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorHistoryInventoryModel searchByModel(ConsumeMonitorHistoryInventoryModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public void delByCreateTime(String createTime){
		mapper.delByCreateTime(createTime);
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
}