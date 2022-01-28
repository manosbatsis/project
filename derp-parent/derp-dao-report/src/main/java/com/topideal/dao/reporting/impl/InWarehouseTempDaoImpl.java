package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.InWarehouseTempDao;
import com.topideal.entity.vo.reporting.InWarehouseTempModel;
import com.topideal.mapper.reporting.InWarehouseTempMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class InWarehouseTempDaoImpl implements InWarehouseTempDao {

    @Autowired
    private InWarehouseTempMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InWarehouseTempModel> list(InWarehouseTempModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InWarehouseTempModel model) throws SQLException {
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
    public int modify(InWarehouseTempModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InWarehouseTempModel  searchByPage(InWarehouseTempModel  model) throws SQLException{
        PageDataModel<InWarehouseTempModel> pageModel=mapper.listByPage(model);
        return (InWarehouseTempModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InWarehouseTempModel  searchById(Long id)throws SQLException {
        InWarehouseTempModel  model=new InWarehouseTempModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InWarehouseTempModel searchByModel(InWarehouseTempModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<InWarehouseTempModel> getInWarehouseTempDetail(Map<String, Object> queryMap) {
		return mapper.getInWarehouseTempDetail(queryMap);
	}

}