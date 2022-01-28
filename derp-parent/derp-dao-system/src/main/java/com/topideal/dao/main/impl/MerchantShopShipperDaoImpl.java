package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchantShopShipperDao;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.entity.vo.main.MerchantShopShipperModel;
import com.topideal.mapper.main.MerchantShopShipperMapper;
import com.topideal.mongo.dao.MerchantShopShipperMongoDao;

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
public class MerchantShopShipperDaoImpl implements MerchantShopShipperDao {

    @Autowired
    private MerchantShopShipperMapper mapper;
    
    @Autowired
    private MerchantShopShipperMongoDao shopShipperMongoDao;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantShopShipperModel> list(MerchantShopShipperModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantShopShipperModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("shipperId",model.getId());
			jsonObject.remove("id");
			shopShipperMongoDao.insertJson(jsonObject.toString());
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
			params.put("shipperId",(Long)ids.get(i));
			shopShipperMongoDao.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchantShopShipperModel  model) throws SQLException {
    	int num = mapper.update(model);
    	if(num>0) {
    		MerchantShopShipperModel shopShipperModel= new MerchantShopShipperModel();
    		shopShipperModel.setId(model.getId());
    		shopShipperModel = mapper.get(shopShipperModel);
			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("shipperId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(shopShipperModel);
			jsonObject.put("shipperId",model.getId());
			jsonObject.remove("id");

			shopShipperMongoDao.update(params,jsonObject);
    	}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchantShopShipperModel  searchByPage(MerchantShopShipperModel  model) throws SQLException{
        PageDataModel<MerchantShopShipperModel> pageModel=mapper.listByPage(model);
        return (MerchantShopShipperModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchantShopShipperModel  searchById(Long id)throws SQLException {
        MerchantShopShipperModel  model=new MerchantShopShipperModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantShopShipperModel searchByModel(MerchantShopShipperModel model) throws SQLException {
		return mapper.get(model);
	}
	public List<MerchantShopShipperDTO> listDTO(MerchantShopShipperModel model){
		return mapper.listDTO(model);
	}

	@Override
	public List<MerchantShopShipperDTO> listForExportShipper(MerchantShopRelDTO dto) {
		return mapper.listForExportShipper(dto);
	}
}