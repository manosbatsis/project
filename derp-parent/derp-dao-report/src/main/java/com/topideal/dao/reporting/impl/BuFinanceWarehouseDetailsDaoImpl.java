package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceWarehouseDetailsDao;
import com.topideal.entity.dto.BuFinanceWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;
import com.topideal.mapper.reporting.BuFinanceWarehouseDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceWarehouseDetailsDaoImpl implements BuFinanceWarehouseDetailsDao {

    @Autowired
    private BuFinanceWarehouseDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceWarehouseDetailsModel> list(BuFinanceWarehouseDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceWarehouseDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceWarehouseDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceWarehouseDetailsModel  searchByPage(BuFinanceWarehouseDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceWarehouseDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceWarehouseDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceWarehouseDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceWarehouseDetailsModel  model=new BuFinanceWarehouseDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceWarehouseDetailsModel searchByModel(BuFinanceWarehouseDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购入库明细数据
	 */
	@Override
	public int delBuDepotMonthFinanceWarehouseDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuDepotMonthFinanceWarehouseDetails(map);
	}
	/**
	 * 总账导出 获取财务经销存本期入库
	 */
	@Override
	public List<Map<String, Object>> getAllAccountFinPurWarehouse(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountFinPurWarehouse(map);
	}
	@Override
	public List<BuFinanceWarehouseDetailsModel> getWarehouseDetailExport(Map<String, Object> map)
			throws SQLException {
		return mapper.getWarehouseDetailExport(map);
	}
	
	/**
	 * 获取累计采购汇总表分页
	 */
	@Override
	public BuFinanceWarehouseDetailsDTO getListAddWarehouse(BuFinanceWarehouseDetailsDTO model)
			throws SQLException {
		List<BuFinanceWarehouseDetailsDTO>addList=mapper.getListAddWarehouse(model);
		int total= mapper.getListAddWarehouseCount(model);
		//获取最大创建时间
		BuFinanceWarehouseDetailsDTO maxModel=mapper.getMaxCreatDate(model);
		if (maxModel!=null) {
			model.setCreateDate(maxModel.getCreateDate());
		}
		model.setList(addList);
		model.setTotal(total);
        return model;
	}
	/**
	 * 累计采购汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<BuFinanceWarehouseDetailsDTO> getListAddExport(BuFinanceWarehouseDetailsDTO model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getListAddExport(model);
	}
	/**
	 * 财务进销存入账汇总表 (采购)
	 */
	@Override
	public List<Map<String, Object>> getPurchaseSummaryExpotr(Map<String, Object> paramMap) {
		return mapper.getPurchaseSummaryExpotr(paramMap);
	}
	/**
	 * 获取财务入库成本差异
	 */
	@Override
	public List<Map<String, Object>> getInCostDifferenceExport(Map<String, Object> map) throws SQLException {
		return mapper.getInCostDifferenceExport(map);
	}
	
	


}