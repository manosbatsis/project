package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessInBadDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessInBadDetailsModel;
import com.topideal.mapper.reporting.BuBusinessInBadDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuBusinessInBadDetailsDaoImpl implements BuBusinessInBadDetailsDao {

    @Autowired
    private BuBusinessInBadDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessInBadDetailsModel> list(BuBusinessInBadDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessInBadDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessInBadDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessInBadDetailsModel  searchByPage(BuBusinessInBadDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessInBadDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessInBadDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessInBadDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessInBadDetailsModel  model=new BuBusinessInBadDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessInBadDetailsModel searchByModel(BuBusinessInBadDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 删除(事业部业务经销存)来货残次
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delBuBusinessInBad(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessInBad(map);
	}
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)来货残次(导出)
	 */
	@Override
	public List<Map<String, Object>> listInBadDetailsMap(Map<String, Object> map) {
		return mapper.listInBadDetailsMap(map);
	}

}