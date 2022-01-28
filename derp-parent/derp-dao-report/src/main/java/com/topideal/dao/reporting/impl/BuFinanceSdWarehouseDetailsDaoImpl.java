package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceSdWarehouseDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceSdWarehouseDetailsModel;
import com.topideal.mapper.reporting.BuFinanceSdWarehouseDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceSdWarehouseDetailsDaoImpl implements BuFinanceSdWarehouseDetailsDao {

    @Autowired
    private BuFinanceSdWarehouseDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceSdWarehouseDetailsModel> list(BuFinanceSdWarehouseDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceSdWarehouseDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceSdWarehouseDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceSdWarehouseDetailsModel  searchByPage(BuFinanceSdWarehouseDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceSdWarehouseDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceSdWarehouseDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceSdWarehouseDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceSdWarehouseDetailsModel  model=new BuFinanceSdWarehouseDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceSdWarehouseDetailsModel searchByModel(BuFinanceSdWarehouseDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)SD采购入库明细数据
	 */
	@Override
	public int delBuFinanceSdWarehouseDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceSdWarehouseDetails(map);
	}
	/**
	 * 获取(事业部财务经销存)采购入库SD明细 导出数据
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceSdWarehouseDetailsMap(Map<String, Object> paramMap)
			throws SQLException {
		return mapper.getBuFinanceSdWarehouseDetailsMap(paramMap);
	}
	/**
	 * 获取导出表头的后面各个每个商品类型金额
	 */
	@Override
	public List<Map<String, Object>> getBuOrderGoodsAmont(Map<String, Object> paramMap) throws SQLException{
		return mapper.getBuOrderGoodsAmont(paramMap);
	}
	
	/**
	 * 获取导出表头的后面各个类型
	 */
	@Override
	public List<Map<String, Object>> getBuOrderGoodsAmontName(Map<String, Object> paramMap) throws SQLException{
		return mapper.getBuOrderGoodsAmontName(paramMap);
	}
	



}