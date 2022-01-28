package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.mapper.main.BusinessUnitMapper;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
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
public class BusinessUnitDaoImpl implements BusinessUnitDao {

    @Autowired
	private BusinessUnitMapper mapper;
    @Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BusinessUnitModel> list(BusinessUnitModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BusinessUnitModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
			//mongo添加数据
			JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;
			json.put("businessUnitId", model.getId());//母品牌id
			businessUnitMongoDao.insertJson(json.toString());

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
		//从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("businessUnitId",(Long)ids.get(i));
			businessUnitMongoDao.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BusinessUnitModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
		int count=mapper.update(model);
		if(count==1){
			//修改Mongo的数据
			BusinessUnitModel businessUnitModel=new BusinessUnitModel();
			businessUnitModel.setId(model.getId());;
			businessUnitModel= mapper.get(businessUnitModel);

			Map<String, Object> params = new HashMap<String,Object>();
			params.put("businessUnitId", model.getId());

			JSONObject jsonObject = JSONObject.fromObject(businessUnitModel);
			jsonObject.put("businessUnitId",model.getId());
			jsonObject.remove("id");

			businessUnitMongoDao.update(params, jsonObject);
		}
        return count;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BusinessUnitModel  searchByPage(BusinessUnitModel  model) throws SQLException{
        PageDataModel<BusinessUnitModel> pageModel=mapper.listByPage(model);
        return (BusinessUnitModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BusinessUnitModel  searchById(Long id)throws SQLException {
        BusinessUnitModel  model=new BusinessUnitModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BusinessUnitModel searchByModel(BusinessUnitModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public BusinessUnitModel judgeQuote(BusinessUnitModel model) {
		return mapper.judgeQuote(model);
	}
	@Override
	public BusinessUnitDTO searchDTOByPage(BusinessUnitDTO dto) {
		PageDataModel<BusinessUnitDTO> pageModel=mapper.searchDTOByPage(dto) ;
		return (BusinessUnitDTO ) pageModel.getModel();
	}
	/**查询编码/名称是否已存在 */
	@Override
	public List<BusinessUnitDTO> getcheckCodeName(BusinessUnitDTO model) {
		return mapper.getcheckCodeName(model);
	}

	@Override
	public List<BusinessUnitDTO> listDTO(BusinessUnitDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}

	@Override
	public List<BusinessUnitDTO> listDTOByLike(BusinessUnitDTO businessUnitDTO) {
		return mapper.listDTOByLike(businessUnitDTO);
	}
}