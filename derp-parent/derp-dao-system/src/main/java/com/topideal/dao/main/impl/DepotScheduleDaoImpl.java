package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepotScheduleDao;
import com.topideal.entity.vo.main.DepotScheduleModel;
import com.topideal.mapper.main.DepotScheduleMapper;
import com.topideal.mongo.dao.DepotScheduleMongoDao;

import net.sf.json.JSONObject;

/**
 * 仓库附表  daoImpl
 * @author lian_
 *
 */
@Repository
public class DepotScheduleDaoImpl implements DepotScheduleDao {

    @Autowired
    private DepotScheduleMapper mapper;
	
    @Autowired
	private DepotScheduleMongoDao mongo;  //仓库附表信息 mongo
    
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<DepotScheduleModel> list(DepotScheduleModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(DepotScheduleModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("depotScheduleId",model.getId());
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
    	//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("depotScheduleId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(DepotScheduleModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
    	int num = mapper.update(model);
    	if(num > 0){
    		DepotScheduleModel depotSche = new DepotScheduleModel();
    		depotSche.setId(model.getId());
    		depotSche = mapper.get(depotSche);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("depotScheduleId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(depotSche);
			jsonObject.put("depotScheduleId",model.getId());
			jsonObject.remove("id");

			mongo.update(params,jsonObject);
    	}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public DepotScheduleModel  searchByPage(DepotScheduleModel  model) throws SQLException{
        PageDataModel<DepotScheduleModel> pageModel=mapper.listByPage(model);
        return (DepotScheduleModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public DepotScheduleModel  searchById(Long id)throws SQLException {
        DepotScheduleModel  model=new DepotScheduleModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public DepotScheduleModel searchByModel(DepotScheduleModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据仓库id查询仓库附表下拉框
	 */
	@Override
	public List<SelectBean> loadSelectByDepotId(DepotScheduleModel model) throws SQLException {
		return mapper.loadSelectByDepotId(model);
	}
}