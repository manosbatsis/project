package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.BatchValidationInfoDao;
import com.topideal.entity.vo.main.BatchValidationInfoModel;
import com.topideal.mapper.main.BatchValidationInfoMapper;
import com.topideal.mongo.dao.BatchValidationInfoMongoDao;

import net.sf.json.JSONObject;

/**
 *批次效期强校验明细 daoImpl
 * @author lchenxing
 */
@Repository
public class BatchValidationInfoDaoImpl implements BatchValidationInfoDao {

    @Autowired
    private BatchValidationInfoMapper mapper;
    @Autowired
	private BatchValidationInfoMongoDao mongo;
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BatchValidationInfoModel> list(BatchValidationInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BatchValidationInfoModel model) throws SQLException {

        int num = mapper.insert(model);

        if(num==1){
        	if (num == 1) {
    			// 存储到MONGODB
    			JSONObject jsonObject = JSONObject.fromObject(model);
    			jsonObject.put("batchValidationId", model.getId());

    			if(model.getModifyDate() != null){
    				jsonObject.put("modifyDate", model.getModifyDate().getTime());
    			}

    			if(model.getEffectiveTime() != null){
    				jsonObject.put("effectiveTime", model.getEffectiveTime().getTime());
    			}

    			if(model.getCreateDate() != null){
    				jsonObject.put("createDate", model.getCreateDate().getTime());
    			}

    			jsonObject.remove("id");

    			mongo.insertJson(jsonObject.toString());

    			return model.getId();
    		}
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {

		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("batchValidationId",(Long)ids.get(i));
			mongo.remove(params);
		}

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BatchValidationInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
		int num = mapper.update(model);

		if(num > 0){

			BatchValidationInfoModel batchValidationInfoModel= new BatchValidationInfoModel();
			batchValidationInfoModel.setId(model.getId());
			batchValidationInfoModel = mapper.get(batchValidationInfoModel);

			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("batchValidationId", model.getId());

			JSONObject jsonObject=JSONObject.fromObject(batchValidationInfoModel);
			jsonObject.put("reptileId",model.getId());

			if(model.getModifyDate() != null){
				jsonObject.put("modifyDate", model.getModifyDate().getTime());
			}

			if(model.getEffectiveTime() != null){
				jsonObject.put("effectiveTime", model.getEffectiveTime().getTime());
			}

			if(model.getCreateDate() != null){
				jsonObject.put("createDate", model.getCreateDate().getTime());
			}

			jsonObject.remove("id");

			mongo.update(params, jsonObject);

		}

		return num ;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BatchValidationInfoModel  searchByPage(BatchValidationInfoModel  model) throws SQLException{
        PageDataModel<BatchValidationInfoModel> pageModel=mapper.listByPage(model);
        return (BatchValidationInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BatchValidationInfoModel  searchById(Long id)throws SQLException {
        BatchValidationInfoModel  model=new BatchValidationInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BatchValidationInfoModel searchByModel(BatchValidationInfoModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 9011 弹框列表查询
	 */
	@Override
	public List<BatchValidationInfoModel> getListById(BatchValidationInfoModel model) throws SQLException {
		return mapper.getListById(model);
	}
}