package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceMoveDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceMoveDetailsModel;
import com.topideal.mapper.reporting.BuFinanceMoveDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceMoveDetailsDaoImpl implements BuFinanceMoveDetailsDao {

    @Autowired
    private BuFinanceMoveDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceMoveDetailsModel> list(BuFinanceMoveDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceMoveDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceMoveDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceMoveDetailsModel  searchByPage(BuFinanceMoveDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceMoveDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceMoveDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceMoveDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceMoveDetailsModel  model=new BuFinanceMoveDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceMoveDetailsModel searchByModel(BuFinanceMoveDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 删除(事业部财务经销存) 本期事业部移库明细
	 */
	@Override
	public int delBuFinanceMoveDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceMoveDetails(map);
	}
	
	/**
	 * 导出(事业部财务经分销)本期事业部移库明细
	 */
	@Override
	public List<Map<String, Object>> exportFinanceMoveDetailsList(Map<String, Object> map) throws SQLException {
		return mapper.exportFinanceMoveDetailsList(map);
	}
	
	@Override
	public int getExportBuFinanceMoveDetailsCount(Map<String, Object> map) throws SQLException {
		return mapper.getExportBuFinanceMoveDetailsCount(map);
	}
}