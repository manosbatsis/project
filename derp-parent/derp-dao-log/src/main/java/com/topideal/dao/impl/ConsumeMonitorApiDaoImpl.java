package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorApiDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorApiModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.mapper.ConsumeMonitorApiMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorApiDaoImpl implements ConsumeMonitorApiDao {

    @Autowired
    private ConsumeMonitorApiMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorApiModel> list(ConsumeMonitorApiModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorApiModel model) throws SQLException {
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
    public int modify(ConsumeMonitorApiModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorApiModel  searchByPage(ConsumeMonitorApiModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorApiModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorApiModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorApiModel  searchById(Long id)throws SQLException {
        ConsumeMonitorApiModel  model=new ConsumeMonitorApiModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorApiModel searchByModel(ConsumeMonitorApiModel model) throws SQLException {
		return mapper.get(model);
	}
    
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorApiModel getListByMonth(ConsumeMonitorApiModel model) throws SQLException {
		PageDataModel<ConsumeMonitorApiModel> pageModel = mapper.getListByMonthByPage(model);
		return (ConsumeMonitorApiModel) pageModel.getModel();  
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
	public Long getTreatedNum(String sDate, String eDate) {
		return mapper.getTreatedNum(sDate, eDate);
	}
	@Override
	public Long getUntreatedNum(String sDate, String eDate) {
		return mapper.getUntreatedNum(sDate, eDate);
	}
	@Override
	public List<Map<String, Object>> getErrorNumGroupByType(String sDate, String eDate) {
		return mapper.getErrorNumGroupByType(sDate, eDate);
	}
	@Override
	public Integer getUnTreatNumByMap(Map<String, Object> queryMap) {
		return mapper.getUnTreatNumByMap(queryMap);
	}
	@Override
	public Long getSuccNum(String sDate, String eDate) {
		return mapper.getSuccNum(sDate, eDate);
	}
}