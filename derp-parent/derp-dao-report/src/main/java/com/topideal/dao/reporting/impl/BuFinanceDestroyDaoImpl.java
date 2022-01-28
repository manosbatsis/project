package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceDestroyDao;
import com.topideal.entity.vo.reporting.BuFinanceDestroyModel;
import com.topideal.mapper.reporting.BuFinanceDestroyMapper;
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
public class BuFinanceDestroyDaoImpl implements BuFinanceDestroyDao {

    @Autowired
    private BuFinanceDestroyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceDestroyModel> list(BuFinanceDestroyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceDestroyModel model) throws SQLException {
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
    public int modify(BuFinanceDestroyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceDestroyModel  searchByPage(BuFinanceDestroyModel  model) throws SQLException{
        PageDataModel<BuFinanceDestroyModel> pageModel=mapper.listByPage(model);
        return (BuFinanceDestroyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceDestroyModel  searchById(Long id)throws SQLException {
        BuFinanceDestroyModel  model=new BuFinanceDestroyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceDestroyModel searchByModel(BuFinanceDestroyModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销毁明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delBuFinanceDestroy(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceDestroy(map);
	}
	/**
	 * 获取销毁明细导出
	 */
	@Override
	public List<BuFinanceDestroyModel> getDestroyExport(Map<String, Object> map) throws SQLException {
		return mapper.getDestroyExport(map);
	}

}