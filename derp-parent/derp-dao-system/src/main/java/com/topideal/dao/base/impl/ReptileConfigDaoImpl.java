package com.topideal.dao.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.ReptileConfigDao;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.base.ReptileConfigMapper;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**爬虫配置表 impl
 * @author lchenxing
 */
@Repository
public class ReptileConfigDaoImpl implements ReptileConfigDao {

    @Autowired
    private ReptileConfigMapper mapper;
    @Autowired
    private ReptileConfigMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReptileConfigModel> list(ReptileConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReptileConfigModel model) throws SQLException {

        int num=mapper.insert(model);

        if(num == 1){

			//存储到MONGODB
			JSONObject jsonObject = JSONObject.fromObject(model);
			jsonObject.put("reptileId",model.getId());
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
			params.put("reptileId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ReptileConfigModel  model) throws SQLException {
    	int num =  mapper.update(model);

    	if(num > 0){

    		ReptileConfigModel reptileConfig= new ReptileConfigModel();
    		reptileConfig.setId(model.getId());
    		reptileConfig = mapper.get(reptileConfig);

			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("reptileId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(reptileConfig);
			jsonObject.put("reptileId",model.getId());
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
    public ReptileConfigModel  searchByPage(ReptileConfigModel  model) throws SQLException{
        PageDataModel<ReptileConfigModel> pageModel=mapper.listByPage(model);
        return (ReptileConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReptileConfigModel  searchById(Long id)throws SQLException {
        ReptileConfigModel  model=new ReptileConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
   /**
 	* 根据商家实体类查询商品
 	* @param model
 	*/
	@Override
	public ReptileConfigModel searchByModel(ReptileConfigModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 分页
	 * @param model
	 */
	@Override
	public ReptileConfigDTO getListByPage(ReptileConfigDTO dto) throws SQLException {
		PageDataModel<ReptileConfigDTO> pageModel = mapper.getListByPage(dto);
		return (ReptileConfigDTO) pageModel.getModel();
	}
	/**
	 * 详情
	 * @param model
	 */
	@Override
	public ReptileConfigModel getDetails(ReptileConfigModel model) throws SQLException {
		return mapper.getDetails(model);
	}
	
	/**
	 * 查询客户下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean(Long merchantId) throws SQLException {
		return mapper.getSelectBean(merchantId);
	}
	
	/**
	 * 查询商家下拉列表
	 */
	@Override
	public List<MerchantInfoModel> getSelectMerchant() throws SQLException {
		return mapper.getSelectMerchant();
	}
	@Override
	public ReptileConfigDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}
    
}
