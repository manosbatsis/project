package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceSaleDamagedDao;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;
import com.topideal.mapper.reporting.BuFinanceSaleDamagedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceSaleDamagedDaoImpl implements BuFinanceSaleDamagedDao {

    @Autowired
    private BuFinanceSaleDamagedMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceSaleDamagedModel> list(BuFinanceSaleDamagedModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceSaleDamagedModel model) throws SQLException {
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
    public int modify(BuFinanceSaleDamagedModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceSaleDamagedModel  searchByPage(BuFinanceSaleDamagedModel  model) throws SQLException{
        PageDataModel<BuFinanceSaleDamagedModel> pageModel=mapper.listByPage(model);
        return (BuFinanceSaleDamagedModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceSaleDamagedModel  searchById(Long id)throws SQLException {
        BuFinanceSaleDamagedModel  model=new BuFinanceSaleDamagedModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceSaleDamagedModel searchByModel(BuFinanceSaleDamagedModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销售残损明细表
	 */
	@Override
	public int delBuFinanceSaleDamaged(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceSaleDamaged(map);
	}
	/**
	 * 获取销售残损导出
	 */
	@Override
	public List<BuFinanceSaleDamagedModel> getsaleDamagedExport(Map<String, Object> map) throws SQLException {
		return mapper.getsaleDamagedExport(map);
	}


}