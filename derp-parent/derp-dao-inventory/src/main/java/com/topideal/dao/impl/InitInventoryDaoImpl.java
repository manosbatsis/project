package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InitInventoryDao;
import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.entity.vo.InitInventoryModel;
import com.topideal.mapper.InitInventoryMapper;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * 期初库存 impl
 * 
 * @author lchenxing
 */
@Repository
public class InitInventoryDaoImpl implements InitInventoryDao {

	@Autowired
	private InitInventoryMapper mapper; // 期初库存mapper
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InitInventoryModel> list(InitInventoryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InitInventoryModel
	 */
	@Override
	public Long save(InitInventoryModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(InitInventoryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InitInventoryModel
	 */
	@Override
	public InitInventoryModel searchByPage(InitInventoryModel model) throws SQLException {
		PageDataModel<InitInventoryModel> pageModel = mapper.listByPage(model);
		return (InitInventoryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InitInventoryModel searchById(Long id) throws SQLException {
		InitInventoryModel model = new InitInventoryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InitInventoryModel searchByModel(InitInventoryModel model) throws SQLException {
		return mapper.get(model);
	}

	
	@Override
	public List<InitInventoryModel> countInitInventory(InitInventoryModel model) throws SQLException {
		List<InitInventoryModel>  initInventoryList = mapper.countInitInventory(model);
		List<InitInventoryModel>  list = new ArrayList<InitInventoryModel>();
		for(InitInventoryModel initInventory:initInventoryList){
			//获取出仓仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", initInventory.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			initInventory.setDepotName(depot.getName());
			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId", initInventory.getGoodsId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
			initInventory.setGoodsName(goods.getName());
			initInventory.setBarcode(goods.getBarcode());
			list.add(initInventory);
		}
		return list;
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

	/**
     * 分页查询返回DTO实体
     * @param model
     * @return
     */
	@Override
	public InitInventoryDTO searchDTOByPage(InitInventoryDTO model) throws SQLException {
		PageDataModel<InitInventoryDTO> pageModel = mapper.listDTOByPage(model);
		return (InitInventoryDTO) pageModel.getModel();
	}

	/**
	  * 1 获取事业期初  
	  * 按商家, 仓库, 事业部,调增调减类型, 商品 ,好坏品 ,效期
	 */
	@Override
	public List<Map<String, Object>> getBuInitInventory(Map<String, Object> map) throws SQLException {
		return mapper.getBuInitInventory(map);
	}

}
