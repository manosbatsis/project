package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnOdepotDao;
import com.topideal.entity.vo.order.SaleReturnOdepotModel;
import com.topideal.mapper.order.SaleReturnOdepotMapper;

/**
 * 销售退货出库 impl
 * @author lchenxing
 */
@Repository
public class SaleReturnOdepotDaoImpl implements SaleReturnOdepotDao {

    @Autowired
    private SaleReturnOdepotMapper mapper;  //销售退货出库
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnOdepotModel> list(SaleReturnOdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SaleReturnOdepotModel
	 */
    @Override
    public Long save(SaleReturnOdepotModel model) throws SQLException {
    	model.setCreateDate(TimeUtils.getNow());
    	model.setModifyDate(TimeUtils.getNow());
    	int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(SaleReturnOdepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param SaleReturnOdepotModel
     */
    @Override
    public SaleReturnOdepotModel  searchByPage(SaleReturnOdepotModel  model) throws SQLException{
        PageDataModel<SaleReturnOdepotModel> pageModel=mapper.listByPage(model);
        return (SaleReturnOdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SaleReturnOdepotModel  searchById(Long id)throws SQLException {
        SaleReturnOdepotModel  model=new SaleReturnOdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
     /**
     * 根据商家实体类查询商品
     * @param MerchandiseInfoModel
     * */
	@Override
	public SaleReturnOdepotModel searchByModel(SaleReturnOdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTime(paramMap);
	}
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTimeCount(paramMap);
	}
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes){
		return mapper.getOutDepotItemByCodes(codes);
	}
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode(paramMap);
	}
}
