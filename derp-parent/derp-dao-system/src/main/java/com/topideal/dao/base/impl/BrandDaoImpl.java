package com.topideal.dao.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.mapper.base.BrandMapper;
import com.topideal.mongo.dao.BrandMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌表 impl
 * 
 * @author lchenxing
 */
@Repository
public class BrandDaoImpl implements BrandDao {

	@Autowired
	private BrandMapper mapper; // 品牌表
	@Autowired
	private BrandMongoDao brandMongoDao;   //品牌mongo

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(BrandModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			//将品牌存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("brandId",model.getId());
			jsonObject.remove("id");

			// 新增到mongo
			brandMongoDao.insertJson(jsonObject.toString());
		}
		return model.getId();
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
			params.put("brandId",(Long)ids.get(i));
			brandMongoDao.remove(params);
		}
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * @param model
	 */
	@Override
	public int modify(BrandModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int num = mapper.update(model);
		if(num > 0){

			BrandModel brand = new BrandModel();
			brand.setId(model.getId());
			brand = mapper.get(brand);
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("brandId",model.getId());

			//修改品牌存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(brand);
			jsonObject.put("brandId",model.getId());
			jsonObject.remove("id");

			brandMongoDao.update(params,jsonObject);
		}
		
		 return num;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public BrandModel searchByPage(BrandModel model) throws SQLException {
		PageDataModel<BrandModel> pageModel = mapper.listByPage(model);
		return (BrandModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public BrandModel searchById(Long id) throws SQLException {
		BrandModel model = new BrandModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据品牌实体类查询品牌
	 * 
	 * @param model
	 */
	@Override
	public BrandModel searchByModel(BrandModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<BrandModel> list(BrandModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 查询品牌表下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return mapper.getSelectBean();
	}

	/**
	 * 查询品牌表下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBeanByMerchant(Map<String, Object> paramMap) throws SQLException {
		return mapper.getSelectBeanByMerchant(paramMap);
	}

	/**
	 * 根据父类id获取品牌集合
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public List<BrandModel> getListByParentId(Long parentId) throws SQLException {
		return mapper.getListByParentId(parentId);
	}*/

	/**
	 * 查询下拉列表，去除已经匹配的
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public List<SelectBean> getSelectBeanByNoMatching() throws SQLException {
		return mapper.getSelectBeanByNoMatching();
	}*/

	/**
	 * 弹窗 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public BrandModel getBrandByPage(BrandModel model) throws SQLException {
		PageDataModel<BrandModel> pageModel = mapper.getBrandByPage(model);
		return (BrandModel) pageModel.getModel();
	}*/

	/**
	 * 根据ids获取信息
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<BrandModel> getListByIds(List list) throws SQLException {
		return mapper.getListByIds(list);
	}

	/**
	 * 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BrandDTO getListByPage(BrandDTO dto) throws SQLException {
		PageDataModel<BrandDTO> pageModel = mapper.getListByPage(dto);
		return (BrandDTO) pageModel.getModel();
	}

	@Override
	public int count(BrandModel model) {
		return mapper.count(model);
	}

	@Override
	public List<BrandModel> listByLike(BrandModel brandModel) {
		return mapper.listByLike(brandModel);
	}

	/**
	 * 解绑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public int updateUnMatching(BrandModel model) throws SQLException {
		return mapper.updateUnMatching(model);
	}*/

}
