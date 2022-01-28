package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceDecreaseSaleNoshelfDao;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;
import com.topideal.mapper.reporting.BuFinanceDecreaseSaleNoshelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceDecreaseSaleNoshelfDaoImpl implements BuFinanceDecreaseSaleNoshelfDao {

    @Autowired
    private BuFinanceDecreaseSaleNoshelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceDecreaseSaleNoshelfModel> list(BuFinanceDecreaseSaleNoshelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceDecreaseSaleNoshelfModel model) throws SQLException {
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
    public int modify(BuFinanceDecreaseSaleNoshelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceDecreaseSaleNoshelfModel  searchByPage(BuFinanceDecreaseSaleNoshelfModel  model) throws SQLException{
        PageDataModel<BuFinanceDecreaseSaleNoshelfModel> pageModel=mapper.listByPage(model);
        return (BuFinanceDecreaseSaleNoshelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceDecreaseSaleNoshelfModel  searchById(Long id)throws SQLException {
        BuFinanceDecreaseSaleNoshelfModel  model=new BuFinanceDecreaseSaleNoshelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceDecreaseSaleNoshelfModel searchByModel(BuFinanceDecreaseSaleNoshelfModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/***
	 * 清除商家 仓库 月份 (事业部财务经销存)本期减少销售在途
	 */
	@Override
	public int delBuFinanceDecreaseSaleNoshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceDecreaseSaleNoshelf(map);
	}
	/**
	 * 获取本期减少销售在途导出
	 */
	@Override
	public List<BuFinanceDecreaseSaleNoshelfModel> getdecreaseSaleNoshelfExport(Map<String, Object> map)throws SQLException {
		return mapper.getdecreaseSaleNoshelfExport(map);
	}

}