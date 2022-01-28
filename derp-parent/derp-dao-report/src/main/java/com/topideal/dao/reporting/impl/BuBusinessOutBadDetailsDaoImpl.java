package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessOutBadDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessOutBadDetailsModel;
import com.topideal.mapper.reporting.BuBusinessOutBadDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuBusinessOutBadDetailsDaoImpl implements BuBusinessOutBadDetailsDao {

    @Autowired
    private BuBusinessOutBadDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessOutBadDetailsModel> list(BuBusinessOutBadDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessOutBadDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessOutBadDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessOutBadDetailsModel  searchByPage(BuBusinessOutBadDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessOutBadDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessOutBadDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessOutBadDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessOutBadDetailsModel  model=new BuBusinessOutBadDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessOutBadDetailsModel searchByModel(BuBusinessOutBadDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 删除
	 */
	@Override
	public int delBuBusinessOutBad(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessOutBad(map);
	}
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)出库残次(导出)
	 */
	@Override
	public List<Map<String, Object>> listOutBadDetailsMap(Map<String, Object> map) {
		return mapper.listOutBadDetailsMap(map);
	}

}