package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepotMerchantRelDao;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.mapper.main.DepotMerchantRelMapper;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;

import net.sf.json.JSONObject;

/**
 * 仓库商家关联表 impl
 * 
 * @author lchenxing
 */
@Repository
public class DepotMerchantRelDaoImpl implements DepotMerchantRelDao {

	@Autowired
	private DepotMerchantRelMapper mapper;
	
	@Autowired
	private DepotMerchantRelMongoDao mongo ;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<DepotMerchantRelModel> list(DepotMerchantRelModel model)
			throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(DepotMerchantRelModel model) throws SQLException {
		int num = mapper.insert(model);

		if (num == 1) {
			
			//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.remove("id");

			mongo.insertJson(jsonObject.toString());
			
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
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(DepotMerchantRelModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());

		int num = mapper.update(model);

		if(num > 0){
			DepotMerchantRelModel depotMerchantRelModel = new DepotMerchantRelModel();
			depotMerchantRelModel.setId(model.getId());
			depotMerchantRelModel = mapper.get(depotMerchantRelModel);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("depotId",model.getDepotId());
			params.put("merchantId",model.getMerchantId());

			JSONObject jsonObject=JSONObject.fromObject(depotMerchantRelModel);
			jsonObject.remove("id");

			mongo.update(params, jsonObject);
		}

		return num ;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public DepotMerchantRelModel searchByPage(DepotMerchantRelModel model)
			throws SQLException {
		PageDataModel<DepotMerchantRelModel> pageModel = mapper
				.listByPage(model);
		return (DepotMerchantRelModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public DepotMerchantRelModel searchById(Long id) throws SQLException {
		DepotMerchantRelModel model = new DepotMerchantRelModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 * */
	@Override
	public DepotMerchantRelModel searchByModel(DepotMerchantRelModel model)
			throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据仓库id查询商家列表
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<DepotMerchantRelDTO> getMerchantByDepotId(Long id) throws SQLException {
		return mapper.getMerchantByDepotId(id);
	}

	/**
	 * 根据仓库id删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delByDepotId(Long id) throws SQLException {
		
		List<DepotMerchantRelDTO> depotMerchantList = this.getMerchantByDepotId(id);
		
		Map<String, Object> params = new HashMap<String,Object>();
		for (DepotMerchantRelDTO dto : depotMerchantList) {
			params.put("depotId",dto.getDepotId());
			params.put("merchantId",dto.getMerchantId());
			mongo.remove(params);
		}
		
		return mapper.delByDepotId(id);
	}

	@Override
	public DepotMerchantRelDTO getByDepotIdAndMerchantId(Long depotId, Long merchantId) throws SQLException {
		return mapper.getByDepotIdAndMerchantId(depotId, merchantId);
	}

	@Override
	public List<DepotMerchantRelDTO> getListByMerchantId(Long id) {
		return mapper.getListByMerchantId(id);
	}

	@Override
	public int delByMerchantId(Long id) {
		
		List<DepotMerchantRelDTO> depotMerchantList = this.getListByMerchantId(id);
		
		Map<String, Object> params = new HashMap<String,Object>();
		for (DepotMerchantRelDTO dto : depotMerchantList) {
			params.put("depotId",dto.getDepotId());
			params.put("merchantId",dto.getMerchantId());
			mongo.remove(params);
		}
		
		return mapper.delByMerchantId(id);
	}

}
