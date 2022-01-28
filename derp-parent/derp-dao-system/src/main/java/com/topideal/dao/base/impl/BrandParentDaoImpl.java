package com.topideal.dao.base.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.mapper.base.BrandParentMapper;
import com.topideal.mongo.dao.BrandParentMongoDao;

import net.sf.json.JSONObject;

/**
 * 品牌父级表 daoImpl
 * 
 * @author lchenxing
 */
@Repository
public class BrandParentDaoImpl implements BrandParentDao {

	@Autowired
	private BrandParentMapper mapper;
	
	@Autowired
	private BrandParentMongoDao mongo ;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<BrandParentModel> list(BrandParentModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(BrandParentModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			
			JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;
			
			json.put("brandParentId", model.getId());//品牌id
			
			mongo.insertJson(json.toString());
			
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		
		//从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("brandParentId",(Long)ids.get(i));
			mongo.remove(params);
		}
		
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(BrandParentModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int num = mapper.update(model);
		
		if(num > 0) {
			BrandParentModel brandParent = new BrandParentModel();
			brandParent.setId(model.getId());
			brandParent = mapper.get(brandParent);

			Map<String, Object> params = new HashMap<String,Object>();
			params.put("brandParentId",model.getId());

			//修改品牌存储到MONGODB
			JSONObject jsonObject = JSONObject.fromObject(brandParent);
			jsonObject.put("brandParentId",model.getId());
			jsonObject.remove("id");
			
			mongo.update(params, jsonObject) ;
			
		}
		
		return num ;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public BrandParentModel searchByPage(BrandParentModel model) throws SQLException {
		PageDataModel<BrandParentModel> pageModel = mapper.listByPage(model);
		return (BrandParentModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public BrandParentModel searchById(Long id) throws SQLException {
		BrandParentModel model = new BrandParentModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public BrandParentModel searchByModel(BrandParentModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取下拉
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean(BrandParentModel model) throws SQLException {
		return mapper.getSelectBean(model);
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BrandParentDTO getListByPage(BrandParentDTO dto) throws SQLException {
		PageDataModel<BrandParentDTO> pageModel = mapper.getListByPage(dto);
		return (BrandParentDTO) pageModel.getModel();
	}

	@Override
	public List<BrandParentModel> getBrandParentByFuzzyQuery(String brandParent) throws SQLException{
		return mapper.getBrandParentByFuzzyQuery(brandParent) ;
	}

	/**
	 * 导出
	 */
	@Override
	public List<BrandParentDTO> exportbrandParentList(BrandParentDTO dto) throws SQLException {
		return mapper.exportbrandParentList(dto);
	}

}
