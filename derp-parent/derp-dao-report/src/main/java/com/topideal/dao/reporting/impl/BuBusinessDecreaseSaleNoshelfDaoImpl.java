package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessDecreaseSaleNoshelfDao;
import com.topideal.entity.vo.reporting.BuBusinessDecreaseSaleNoshelfModel;
import com.topideal.mapper.reporting.BuBusinessDecreaseSaleNoshelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuBusinessDecreaseSaleNoshelfDaoImpl implements BuBusinessDecreaseSaleNoshelfDao {

    @Autowired
    private BuBusinessDecreaseSaleNoshelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessDecreaseSaleNoshelfModel> list(BuBusinessDecreaseSaleNoshelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessDecreaseSaleNoshelfModel model) throws SQLException {
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
    public int modify(BuBusinessDecreaseSaleNoshelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessDecreaseSaleNoshelfModel  searchByPage(BuBusinessDecreaseSaleNoshelfModel  model) throws SQLException{
        PageDataModel<BuBusinessDecreaseSaleNoshelfModel> pageModel=mapper.listByPage(model);
        return (BuBusinessDecreaseSaleNoshelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessDecreaseSaleNoshelfModel  searchById(Long id)throws SQLException {
        BuBusinessDecreaseSaleNoshelfModel  model=new BuBusinessDecreaseSaleNoshelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessDecreaseSaleNoshelfModel searchByModel(BuBusinessDecreaseSaleNoshelfModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 *  清除商家 仓库 月份 (事业部业务经分销)本期减少销售在途明细表
	 */
	@Override
	public int delBuBusinessDecreaseSaleNoshelf(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessDecreaseSaleNoshelf(map);
	}
	
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)本期减少在途明细表 (导出)
	 */
	@Override
	public List<Map<String, Object>> listBuDecreaseSaleNoshelfMap(Map<String, Object> map) {
		return mapper.listBuDecreaseSaleNoshelfMap(map);
	}

}