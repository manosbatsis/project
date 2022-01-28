package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceAddPurchaseNotshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceAddPurchaseNotshelfDetailsModel;
import com.topideal.mapper.reporting.BuFinanceAddPurchaseNotshelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceAddPurchaseNotshelfDetailsDaoImpl implements BuFinanceAddPurchaseNotshelfDetailsDao {

    @Autowired
    private BuFinanceAddPurchaseNotshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceAddPurchaseNotshelfDetailsModel> list(BuFinanceAddPurchaseNotshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceAddPurchaseNotshelfDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceAddPurchaseNotshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceAddPurchaseNotshelfDetailsModel  searchByPage(BuFinanceAddPurchaseNotshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceAddPurchaseNotshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceAddPurchaseNotshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceAddPurchaseNotshelfDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceAddPurchaseNotshelfDetailsModel  model=new BuFinanceAddPurchaseNotshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceAddPurchaseNotshelfDetailsModel searchByModel(BuFinanceAddPurchaseNotshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计采购在途明细
	 */
	@Override
	public int delBuFinanceAddPurchaseNotshelfDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceAddPurchaseNotshelfDetails(map);
	}
	@Override
	public List<BuFinanceAddPurchaseNotshelfDetailsModel> getFinanceSaleNotshelfList(Map<String, Object> map) throws SQLException {
		return mapper.getFinanceSaleNotshelfList(map);
	}
	/**
	 *  总账导出 获取财务经销存本期在途
	 */
	@Override
	public List<Map<String, Object>> getAllAccountFinNoshelf(Map<String, Object> map) throws SQLException {
		return mapper.getAllAccountFinNoshelf(map);
	}
	@Override
	public List<BuFinanceAddPurchaseNotshelfDetailsModel> getAddPurchaseNotshelfExport(Map<String, Object> map) throws SQLException {
		return mapper.getAddPurchaseNotshelfExport(map);
	}

}