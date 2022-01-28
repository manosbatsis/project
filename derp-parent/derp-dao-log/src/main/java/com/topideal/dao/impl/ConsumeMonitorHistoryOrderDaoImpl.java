package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.ConsumeMonitorHistoryOrderDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryOrderModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.mapper.ConsumeMonitorHistoryOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ConsumeMonitorHistoryOrderDaoImpl implements ConsumeMonitorHistoryOrderDao {

    @Autowired
    private ConsumeMonitorHistoryOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ConsumeMonitorHistoryOrderModel> list(ConsumeMonitorHistoryOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ConsumeMonitorHistoryOrderModel model) throws SQLException {
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
    public int modify(ConsumeMonitorHistoryOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ConsumeMonitorHistoryOrderModel  searchByPage(ConsumeMonitorHistoryOrderModel  model) throws SQLException{
        PageDataModel<ConsumeMonitorHistoryOrderModel> pageModel=mapper.listByPage(model);
        return (ConsumeMonitorHistoryOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ConsumeMonitorHistoryOrderModel  searchById(Long id)throws SQLException {
        ConsumeMonitorHistoryOrderModel  model=new ConsumeMonitorHistoryOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 通过条件查询实体类信息
     	* @param model
     	* */
	@Override
	public ConsumeMonitorHistoryOrderModel searchByModel(ConsumeMonitorHistoryOrderModel model) throws SQLException {
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
	 * 获取重推的所有信息
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException {
		return mapper.getErrorListByPush(model);
	}
}