package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchantDepotBuRelDao;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.mapper.main.MerchantDepotBuRelMapper;
import com.topideal.mongo.dao.MerchantDepotBuRelMongoDao;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
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
    @Autowired
    private MerchantDepotBuRelMongoDao mongo ;
	
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
        if (num == 1) {
			
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchantDepotBuRelId", model.getId()) ;
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());
			
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

		// 先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchantDepotBuRelId", (Long) ids.get(i));
			mongo.remove(params);
		}

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchantDepotBuRelModel  model) throws SQLException {

		int num = mapper.update(model);

		if(num > 0){
			MerchantDepotBuRelModel relModel = new MerchantDepotBuRelModel();
			relModel.setId(model.getId());
			relModel = mapper.get(relModel);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchantDepotBuRelId", relModel.getId());

			JSONObject jsonObject=JSONObject.fromObject(relModel);
			jsonObject.put("merchantDepotBuRelId",relModel.getId());
			jsonObject.remove("id");

			mongo.update(params,jsonObject);
		}

        return num ;
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
    public List<SelectBean> getSelectBean(MerchantDepotBuRelModel model) throws SQLException {
        return mapper.getSelectBean(model);
    }
	@Override
	public String getSelectInfoByMerchantId(MerchantDepotBuRelModel model) {
		return mapper.getSelectInfoByMerchantId(model);
	}
	@Override
	public int delByModel(MerchantDepotBuRelModel delModel) throws SQLException {
		
		List<MerchantDepotBuRelModel> list = this.list(delModel);
		Map<String, Object> params = new HashMap<String,Object>();
		for (MerchantDepotBuRelModel model : list) {
			params.put("depotId",model.getDepotId());
			params.put("merchantId",model.getMerchantId());
			mongo.remove(params);
		}
		
		return mapper.delByModel(delModel);
	}
	@Override
	public String getBuNameByMerchantDepot(MerchantDepotBuRelModel model) {
		return mapper.getBuNameByMerchantDepot(model);
	}
}