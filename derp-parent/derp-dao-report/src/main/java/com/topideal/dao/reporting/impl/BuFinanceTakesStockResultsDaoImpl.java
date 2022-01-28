package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceTakesStockResultsDao;
import com.topideal.entity.vo.reporting.BuFinanceTakesStockResultsModel;
import com.topideal.mapper.reporting.BuFinanceTakesStockResultsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceTakesStockResultsDaoImpl implements BuFinanceTakesStockResultsDao {

    @Autowired
    private BuFinanceTakesStockResultsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceTakesStockResultsModel> list(BuFinanceTakesStockResultsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceTakesStockResultsModel model) throws SQLException {
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
    public int modify(BuFinanceTakesStockResultsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceTakesStockResultsModel  searchByPage(BuFinanceTakesStockResultsModel  model) throws SQLException{
        PageDataModel<BuFinanceTakesStockResultsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceTakesStockResultsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceTakesStockResultsModel  searchById(Long id)throws SQLException {
        BuFinanceTakesStockResultsModel  model=new BuFinanceTakesStockResultsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceTakesStockResultsModel searchByModel(BuFinanceTakesStockResultsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)盘盈盘亏明细数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delBuFinanceTakesStockResults(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceTakesStockResults(map);
	}
	/**
	 * 获取总账导出 盘盈盘亏明细表
	 */
	@Override
	public List<Map<String, Object>> getAllAccountGroupByType(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountGroupByType(map);
	}
	/**
	 * 获取盘点结果导出
	 */
	@Override
	public List<BuFinanceTakesStockResultsModel> getTakesStockResultExport(Map<String, Object> map)throws SQLException {
		return mapper.getTakesStockResultExport(map);
	}


}