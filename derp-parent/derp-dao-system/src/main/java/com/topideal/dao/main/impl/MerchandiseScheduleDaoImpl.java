package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchandiseScheduleDao;
import com.topideal.entity.vo.main.MerchandiseScheduleModel;
import com.topideal.mapper.main.MerchandiseScheduleMapper;
import com.topideal.mongo.dao.MerchandiseScheduleMongoDao;

import net.sf.json.JSONObject;

/**
 *商品附表 daoImpl
 * @author lchenxing
 */
@Repository
public class MerchandiseScheduleDaoImpl implements MerchandiseScheduleDao {

    @Autowired
    private MerchandiseScheduleMapper mapper;
    
    @Autowired
	private MerchandiseScheduleMongoDao scheduleMongoDao;// 商品附表
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseScheduleModel> list(MerchandiseScheduleModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseScheduleModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("scheduleId",model.getId());
			jsonObject.remove("id");
			scheduleMongoDao.insertJson(jsonObject.toString());
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
			params.put("scheduleId",(Long)ids.get(i));
			scheduleMongoDao.remove(params);
		}
    	return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchandiseScheduleModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	int num = mapper.update(model);
    	if(num > 0){
    		MerchandiseScheduleModel scheduleModel = new MerchandiseScheduleModel();
    		scheduleModel.setId(model.getId());
    		scheduleModel = mapper.get(scheduleModel);
			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("scheduleId",scheduleModel.getId());
			
			JSONObject jsonObject=JSONObject.fromObject(scheduleModel);
			jsonObject.put("scheduleId",scheduleModel.getId());
			jsonObject.remove("id");
			scheduleMongoDao.update(params,jsonObject);
    	}
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseScheduleModel  searchByPage(MerchandiseScheduleModel  model) throws SQLException{
        PageDataModel<MerchandiseScheduleModel> pageModel=mapper.listByPage(model);
        return (MerchandiseScheduleModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseScheduleModel  searchById(Long id)throws SQLException {
        MerchandiseScheduleModel  model=new MerchandiseScheduleModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseScheduleModel searchByModel(MerchandiseScheduleModel model) throws SQLException {
		return mapper.get(model);
	}
}
