package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceAddSaleNoshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceAddSaleNoshelfDetailsModel;
import com.topideal.mapper.reporting.BuFinanceAddSaleNoshelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceAddSaleNoshelfDetailsDaoImpl implements BuFinanceAddSaleNoshelfDetailsDao {

    @Autowired
    private BuFinanceAddSaleNoshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceAddSaleNoshelfDetailsModel> list(BuFinanceAddSaleNoshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceAddSaleNoshelfDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceAddSaleNoshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceAddSaleNoshelfDetailsModel  searchByPage(BuFinanceAddSaleNoshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceAddSaleNoshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceAddSaleNoshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceAddSaleNoshelfDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceAddSaleNoshelfDetailsModel  model=new BuFinanceAddSaleNoshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceAddSaleNoshelfDetailsModel searchByModel(BuFinanceAddSaleNoshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计销售在途明细表
	 */
	@Override
	public int delBuFinanceAddSaleNoshelfDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceAddSaleNoshelfDetails(map);
	}
	@Override
	public List<BuFinanceAddSaleNoshelfDetailsModel> getBuFinanceAddSaleNoshelfDetails(Map<String, Object> map) {
		return mapper.getBuFinanceAddSaleNoshelfDetails(map);
	}
	@Override
	public List<BuFinanceAddSaleNoshelfDetailsModel> getAddSaleNoshelfExport(Map<String, Object> map)throws SQLException {
		return mapper.getAddSaleNoshelfExport(map);
	}
}