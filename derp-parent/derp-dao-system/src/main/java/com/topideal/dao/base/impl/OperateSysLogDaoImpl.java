package com.topideal.dao.base.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.entity.dto.base.OperateSysLogDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.mapper.base.OperateSysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OperateSysLogDaoImpl implements OperateSysLogDao {

    @Autowired
    private OperateSysLogMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OperateSysLogModel> list(OperateSysLogModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OperateSysLogModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(OperateSysLogModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public OperateSysLogModel  searchByPage(OperateSysLogModel  model) throws SQLException{
        PageDataModel<OperateSysLogModel> pageModel=mapper.listByPage(model);
        return (OperateSysLogModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OperateSysLogModel  searchById(Long id)throws SQLException {
        OperateSysLogModel  model=new OperateSysLogModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OperateSysLogModel searchByModel(OperateSysLogModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

    @Override
    public List<OperateSysLogDTO> getOperateSysLog(OperateSysLogDTO dto) {
        return mapper.getOperateSysLog(dto);
    }

    @Override
    public int batchSave(List<OperateSysLogModel> logList) {
        return mapper.batchSave(logList);
    }
}