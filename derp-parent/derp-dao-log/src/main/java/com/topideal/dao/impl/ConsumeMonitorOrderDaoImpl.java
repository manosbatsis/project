package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;
import com.topideal.mapper.ConsumeMonitorOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorOrderDaoImpl implements ConsumeMonitorOrderDao {

    @Autowired
    private ConsumeMonitorOrderMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorOrderModel> list(ConsumeMonitorOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorOrderModel model) throws SQLException {
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
    public int modify(ConsumeMonitorOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorOrderModel  searchByPage(ConsumeMonitorOrderModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorOrderModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorOrderModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorOrderModel  searchById(Long id)throws SQLException {
        ConsumeMonitorOrderModel  model=new ConsumeMonitorOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorOrderModel searchByModel(ConsumeMonitorOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 获取本月已处理的失败数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Long getTreatedNum(String startDate, String endDate) {
		return mapper.getTreatedNum(startDate,endDate);
	}
	/**
	 * 获取本月未处理的失败数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Long getUntreatedNum(String startDate, String endDate) {
		return mapper.getUntreatedNum(startDate,endDate);
	}

	/**
	 * 根据状态统计各模块的数量
	 * @param status 多个以“,”隔开如：  "1","2","3"
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getGroupCountByStatus(String status,String startDate, String endDate) {
		return mapper.getGroupCountByStatus(status, startDate, endDate);
	}
	
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorOrderModel getListByMonth(ConsumeMonitorOrderModel model) throws SQLException {
		PageDataModel<ConsumeMonitorOrderModel> pageModel = mapper.getListByMonthByPage(model);
		return (ConsumeMonitorOrderModel) pageModel.getModel();  
	}
	
	/**
	 * 根据时间获取MQ消费失败的信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<ConsumeMonitorOrderModel> getConsumeFailList(String startDate, String endDate) {
		return mapper.getConsumeFailList(startDate,endDate);
	}
	
	/**整合web*/
	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorMqDTO getListByPage(ConsumeMonitorMqDTO model) throws SQLException {
		PageDataModel<ConsumeMonitorMqDTO> pageModel = mapper.getListByPage(model);
		return (ConsumeMonitorMqDTO) pageModel.getModel();
	}

	/**
	 * 获取数量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Long getCount(ConsumeMonitorMqModel model) throws SQLException {
		return mapper.getCount(model);
	}

	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException {
		return mapper.getExportList(model);
	}

	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorMqModel getExportListByPage(ConsumeMonitorMqModel model) throws SQLException {
		PageDataModel<ConsumeMonitorMqModel> pageModel = mapper.getExportListByPage(model);
		return (ConsumeMonitorMqModel) pageModel.getModel();
	}

	/**
	 * 查询错误的电商订单日志信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorMqDTO getErrorListByPage(ConsumeMonitorMqDTO model) throws SQLException {
		PageDataModel<ConsumeMonitorMqDTO> pageModel = mapper.getErrorListByPage(model);
		return (ConsumeMonitorMqDTO) pageModel.getModel();
	}

	/**
	 * 获取重推的所有信息
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException {
		return mapper.getErrorListByPush(model);
	}
	@Override
	public List<Map<String, Object>> getErrorTypeAccountByMap(Map<String, Object> querymap) {
		return mapper.getErrorTypeAccountByMap(querymap);
	}
}