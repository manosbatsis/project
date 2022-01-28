package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorHistoryApiDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryApiModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.mapper.ConsumeMonitorHistoryApiMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorHistoryApiDaoImpl implements ConsumeMonitorHistoryApiDao {

    @Autowired
    private ConsumeMonitorHistoryApiMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorHistoryApiModel> list(ConsumeMonitorHistoryApiModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorHistoryApiModel model) throws SQLException {
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
    public int modify(ConsumeMonitorHistoryApiModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorHistoryApiModel  searchByPage(ConsumeMonitorHistoryApiModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorHistoryApiModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorHistoryApiModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorHistoryApiModel  searchById(Long id)throws SQLException {
        ConsumeMonitorHistoryApiModel  model=new ConsumeMonitorHistoryApiModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorHistoryApiModel searchByModel(ConsumeMonitorHistoryApiModel model) throws SQLException {
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