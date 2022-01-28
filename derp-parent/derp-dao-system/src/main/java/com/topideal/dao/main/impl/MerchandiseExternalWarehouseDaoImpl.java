package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchandiseExternalWarehouseDao;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;
import com.topideal.entity.vo.main.MerchandiseExternalWarehouseModel;
import com.topideal.mapper.main.MerchandiseExternalWarehouseMapper;
import com.topideal.mongo.dao.MerchandiseExternalWarehouseMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MerchandiseExternalWarehouseDaoImpl implements MerchandiseExternalWarehouseDao {

    @Autowired
    private MerchandiseExternalWarehouseMapper mapper;
    @Autowired
    private MerchandiseExternalWarehouseMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseExternalWarehouseModel> list(MerchandiseExternalWarehouseModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseExternalWarehouseModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;			
			json.put("merchandiseExternalWarehouseId", model.getId());		
			mongo.insertJson(json.toString());
			
			return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchandiseExternalWarehouseModel  model) throws SQLException {
    	int num = mapper.update(model);
    	if(num > 0) {
    		MerchandiseExternalWarehouseModel merchandiseExternalWarehouseModel = new MerchandiseExternalWarehouseModel();
    		merchandiseExternalWarehouseModel.setId(model.getId());
    		merchandiseExternalWarehouseModel = mapper.get(merchandiseExternalWarehouseModel);
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseExternalWarehouseId", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(merchandiseExternalWarehouseModel);
			jsonObject.put("merchandiseExternalWarehouseId",model.getId());
			jsonObject.remove("id");
			
			mongo.update(params, jsonObject);
		}
    	return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseExternalWarehouseModel  searchByPage(MerchandiseExternalWarehouseModel  model) throws SQLException{
        PageDataModel<MerchandiseExternalWarehouseModel> pageModel=mapper.listByPage(model);
        return (MerchandiseExternalWarehouseModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseExternalWarehouseModel  searchById(Long id)throws SQLException {
        MerchandiseExternalWarehouseModel  model=new MerchandiseExternalWarehouseModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseExternalWarehouseModel searchByModel(MerchandiseExternalWarehouseModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int delete(List ids) throws SQLException {
		//从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseExternalWarehouseId",(Long)ids.get(i));
			mongo.remove(params);
		}
		return mapper.batchDel(ids);
	}

    @Override
    public MerchandiseExternalWarehouseDTO getListByPage(MerchandiseExternalWarehouseDTO dto) {
        PageDataModel<MerchandiseExternalWarehouseDTO> pageModel=mapper.getListByPage(dto);
        return (MerchandiseExternalWarehouseDTO ) pageModel.getModel() ;
    }

    @Override
    public MerchandiseExternalWarehouseDTO getDetailById(Long id) {
        return mapper.getDetailById(id);
    }

    @Override
    public List<MerchandiseExternalWarehouseDTO> exportList(MerchandiseExternalWarehouseDTO dto) throws SQLException {
        return mapper.exportMerchandiseExternalWarehouseMap(dto);
    }
}