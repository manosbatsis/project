package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchandiseDepotRelDao;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.main.MerchandiseDepotRelMapper;
import com.topideal.mongo.dao.MerchandiseDepotRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;

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
public class MerchandiseDepotRelDaoImpl implements MerchandiseDepotRelDao {

    @Autowired
    private MerchandiseDepotRelMapper mapper;
    @Autowired
	private MerchandiseDepotRelMongoDao merchandiseDepotRelMongoDao; //商品仓库关系表mongodb
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseDepotRelModel> list(MerchandiseDepotRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseDepotRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchandiseDepotRelId",model.getId());
			jsonObject.remove("id");

			merchandiseDepotRelMongoDao.insertJson(jsonObject.toString());
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
		//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseDepotRelId",(Long)ids.get(i));
			merchandiseDepotRelMongoDao.remove(params);
		}	
        return mapper.batchDel(ids); 
    }
    
    /**
     * 根据商品id删除mongdb
     */
    @Override
    public int deleteByGoodsId(List<Long> goodIdList) throws SQLException {
		//先从mongoDB删除
		for (int i = 0; i < goodIdList.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("goodsId",goodIdList.get(i));
			merchandiseDepotRelMongoDao.remove(params);
		}	
        return mapper.deleteByGoodsId(goodIdList); 
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchandiseDepotRelModel  model) throws SQLException {
    	int num = mapper.update(model);
    	if (num>0) {
    		MerchandiseDepotRelModel merchandiseDepotRel = new MerchandiseDepotRelModel();
    		merchandiseDepotRel.setId(model.getId());
    		merchandiseDepotRel = mapper.get(merchandiseDepotRel);
        	//修改mongodb 商家信息
    		Map<String, Object> params = new HashMap<String,Object>();
    		params.put("merchandiseDepotRelId",model.getId());

    		JSONObject jsonObject=JSONObject.fromObject(merchandiseDepotRel);
    		jsonObject.put("merchandiseDepotRelId",model.getId());
    		jsonObject.remove("id");
    		merchandiseDepotRelMongoDao.update(params,jsonObject);
		}    	
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseDepotRelModel  searchByPage(MerchandiseDepotRelModel  model) throws SQLException{
        PageDataModel<MerchandiseDepotRelModel> pageModel=mapper.listByPage(model);
        return (MerchandiseDepotRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseDepotRelModel  searchById(Long id)throws SQLException {
        MerchandiseDepotRelModel  model=new MerchandiseDepotRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseDepotRelModel searchByModel(MerchandiseDepotRelModel model) throws SQLException {
		return mapper.get(model);
	}

}