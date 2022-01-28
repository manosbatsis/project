package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.InventoryAnalysisDao;
import com.topideal.entity.dto.ManageDepartmentInventoryCleanDTO;
import com.topideal.entity.vo.reporting.InventoryAnalysisModel;
import com.topideal.mapper.reporting.InventoryAnalysisMapper;
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
public class InventoryAnalysisDaoImpl implements InventoryAnalysisDao {

    @Autowired
    private InventoryAnalysisMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryAnalysisModel> list(InventoryAnalysisModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryAnalysisModel model) throws SQLException {
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
    public int modify(InventoryAnalysisModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryAnalysisModel  searchByPage(InventoryAnalysisModel  model) throws SQLException{
        PageDataModel<InventoryAnalysisModel> pageModel=mapper.listByPage(model);
        return (InventoryAnalysisModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryAnalysisModel  searchById(Long id)throws SQLException {
        InventoryAnalysisModel  model=new InventoryAnalysisModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public InventoryAnalysisModel searchByModel(InventoryAnalysisModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<InventoryAnalysisModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public List<Map<String, Object>> getInventoryAnalysisData(Map<String, Object> queryMap) throws SQLException {
        return mapper.getInventoryAnalysisData(queryMap);
    }

    @Override
    public void batchDelByDate(String month) throws SQLException {
        mapper.batchDelByDate(month);
    }

    @Override
    public List<Map<String, Object>> getInventoryAnalysisExportList(Map<String, Object> queryMap){
        return mapper.getInventoryAnalysisExportList(queryMap);
    }

    @Override
    public List<ManageDepartmentInventoryCleanDTO> getDepartmentInventoryCleanStatistic(Map<String, Object> paramMap) {
        return mapper.getDepartmentInventoryCleanStatistic(paramMap);
    }
}