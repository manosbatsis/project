/*
package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.InventoryLocationMappingDao;
import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
import com.topideal.entity.vo.main.InventoryLocationMappingModel;
import com.topideal.mapper.main.InventoryLocationMappingMapper;
import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 *//*

@Repository
public class InventoryLocationMappingDaoImpl implements InventoryLocationMappingDao {

    @Autowired
    private InventoryLocationMappingMapper mapper;
    @Autowired
    private InventoryLocationMappingMongoDao mongo ;
	
	*/
/**
	 * 列表查询
	 * @param model
	 *//*

	@Override
	public List<InventoryLocationMappingModel> list(InventoryLocationMappingModel model) throws SQLException {
		return mapper.list(model);
	}
	*/
/**
	 * 新增
	 * @param model
	 *//*

    @Override
    public Long save(InventoryLocationMappingModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	
        	InventoryLocationMappingModel tempModel = searchById(model.getId());
        	
        	//存储到MONGODB
			String json= JSONObject.fromObject(tempModel).toString();			
			JSONObject jsonObject=JSONObject.fromObject(json);
			jsonObject.put("inventoryLocationMappingId",model.getId());
			// 移除字段
			jsonObject.remove("id");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");			
			mongo.insertJson(jsonObject.toString());
        	
            return model.getId();
        }
        return null;
    }
    
	*/
/**
     * 删除
     * @param ids
     *//*

    @Override
    public int delete(List ids) throws SQLException {
    	
    	//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("inventoryLocationMappingId",(Long)ids.get(i));
			mongo.remove(params);
		}
    	
        return mapper.batchDel(ids);
    }
    
	*/
/**
     * 修改
     * @param model
     *//*

    @Override
    public int modify(InventoryLocationMappingModel  model) throws SQLException {
        int num = mapper.update(model);
        
        if(num > 0){
        	InventoryLocationMappingModel tempModel = searchById(model.getId());
        	
        	Map<String, Object> params = new HashMap<String,Object>();
    		params.put("inventoryLocationMappingId",model.getId());
    		
			JSONObject jsonObject=JSONObject.fromObject(tempModel);
			jsonObject.put("inventoryLocationMappingId",model.getId());
			
			jsonObject.remove("id");
			jsonObject.remove("createDate");
			jsonObject.remove("modifyDate");			
        	mongo.update(params,jsonObject);
        }
        
        return num ;
    }
    
	*/
/**
     * 分页查询
     * @param model
     *//*

    @Override
    public InventoryLocationMappingModel  searchByPage(InventoryLocationMappingModel  model) throws SQLException{
        PageDataModel<InventoryLocationMappingModel> pageModel=mapper.listByPage(model);
        return (InventoryLocationMappingModel ) pageModel.getModel();
    }
    
    */
/**
     * 通过id查询实体类信息
     * @param id
     *//*

    @Override
    public InventoryLocationMappingModel  searchById(Long id)throws SQLException {
        InventoryLocationMappingModel  model=new InventoryLocationMappingModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      */
/**
     	* 根据商家实体类查询商品
     	* @param model
     	* *//*

	@Override
	public InventoryLocationMappingModel searchByModel(InventoryLocationMappingModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public InventoryLocationMappingDTO getListByPage(InventoryLocationMappingDTO dto) {
		PageDataModel<InventoryLocationMappingDTO> pageModel=mapper.getListByPage(dto);
        return (InventoryLocationMappingDTO ) pageModel.getModel();
	}
	@Override
	public List<InventoryLocationMappingDTO> listDTO(InventoryLocationMappingDTO dto) {
		return mapper.listDTO(dto);
	}

}*/
