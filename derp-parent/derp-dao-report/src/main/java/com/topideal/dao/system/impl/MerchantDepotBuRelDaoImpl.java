package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.system.MerchantDepotBuRelDao;
import com.topideal.entity.vo.system.MerchantDepotBuRelModel;
import com.topideal.mapper.system.MerchantDepotBuRelMapper;
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
public class MerchantDepotBuRelDaoImpl implements MerchantDepotBuRelDao {

    @Autowired
    private MerchantDepotBuRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantDepotBuRelModel> list(MerchantDepotBuRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantDepotBuRelModel model) throws SQLException {
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
    public int modify(MerchantDepotBuRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchantDepotBuRelModel  searchByPage(MerchantDepotBuRelModel  model) throws SQLException{
        PageDataModel<MerchantDepotBuRelModel> pageModel=mapper.listByPage(model);
        return (MerchantDepotBuRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchantDepotBuRelModel  searchById(Long id)throws SQLException {
        MerchantDepotBuRelModel  model=new MerchantDepotBuRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantDepotBuRelModel searchByModel(MerchantDepotBuRelModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int deleteByMap(Map<String, Object> delMap) {
		return mapper.deleteByMap(delMap);
	}
	
	/**
	 * 获取事业部商家仓库中间表
	 */
	@Override
	public List<Map<String, Object>> getBuMerchAndDepotBuMap(Map<String, Object> map) {
		return mapper.getBuMerchAndDepotBuMap(map);
	}

	@Override
	public List<Map<String, Object>> getListByMap(Map<String, Object> map) {
		return mapper.getListByMap(map);
	}
	/**查询商家仓库、事业部统计存货跌价为是公司事业部状态为启用的仓库、事业部数据
	 * */
	@Override
	public List<Map<String, Object>> getDepotAndBuList(Map<String, Object> map){
		return mapper.getDepotAndBuList(map);
	}
}