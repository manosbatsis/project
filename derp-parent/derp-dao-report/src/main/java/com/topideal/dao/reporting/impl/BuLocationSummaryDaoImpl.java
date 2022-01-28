package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuLocationSummaryDao;
import com.topideal.entity.dto.BuLocationSummaryDTO;
import com.topideal.entity.vo.reporting.BuLocationSummaryModel;
import com.topideal.mapper.reporting.BuLocationSummaryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuLocationSummaryDaoImpl implements BuLocationSummaryDao {

    @Autowired
    private BuLocationSummaryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuLocationSummaryModel> list(BuLocationSummaryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuLocationSummaryModel model) throws SQLException {
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
    public int modify(BuLocationSummaryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuLocationSummaryModel  searchByPage(BuLocationSummaryModel  model) throws SQLException{
        PageDataModel<BuLocationSummaryModel> pageModel=mapper.listByPage(model);
        return (BuLocationSummaryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuLocationSummaryModel  searchById(Long id)throws SQLException {
        BuLocationSummaryModel  model=new BuLocationSummaryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuLocationSummaryModel searchByModel(BuLocationSummaryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BuLocationSummaryDTO listDTOByPage(BuLocationSummaryDTO dto) {
        PageDataModel<BuLocationSummaryDTO> pageModel=mapper.listDTOByPage(dto);
        return (BuLocationSummaryDTO ) pageModel.getModel();
    }
    /**
     * 删除(事业部业务经销存)事业部库位经销存总报表
     * @param map
     * @return
     * @throws SQLException
     */
	@Override
	public int delBuLocationSummary(Map<String, Object> map) throws SQLException {
		return mapper.delBuLocationSummary(map);
	}
	@Override
	public List<Map<String, Object>> getLocationSummaryInit(Map<String, Object> map) {
		return mapper.getLocationSummaryInit(map);
	}
	/**
	 * 业务经销存 库位进销存汇总表 导出
	 */
	@Override
	public List<Map<String, Object>> getLocationSummaryListForMap(Map<String, Object> map) {
		return mapper.getLocationSummaryListForMap(map);
	}
	
	
	
}