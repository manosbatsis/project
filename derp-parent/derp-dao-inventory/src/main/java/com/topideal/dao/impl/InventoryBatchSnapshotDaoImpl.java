package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryBatchSnapshotDao;
import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
import com.topideal.mapper.InventoryBatchSnapshotMapper;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * 库存批次快照表 daoImpl
 * 
 * @author lchenxing
 */
@Repository
public class InventoryBatchSnapshotDaoImpl implements InventoryBatchSnapshotDao {

	@Autowired
	private InventoryBatchSnapshotMapper mapper;
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
	public List<InventoryBatchSnapshotModel> list(InventoryBatchSnapshotModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(InventoryBatchSnapshotModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
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
	public int modify(InventoryBatchSnapshotModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public InventoryBatchSnapshotModel searchByPage(InventoryBatchSnapshotModel model) throws SQLException {
		PageDataModel<InventoryBatchSnapshotModel> pageModel = mapper.listByPage(model);
		return (InventoryBatchSnapshotModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public InventoryBatchSnapshotModel searchById(Long id) throws SQLException {
		InventoryBatchSnapshotModel model = new InventoryBatchSnapshotModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public InventoryBatchSnapshotModel searchByModel(InventoryBatchSnapshotModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public InventoryBatchSnapshotModel getlistByPage(InventoryBatchSnapshotModel model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<InventoryBatchSnapshotModel> pageModel = mapper.listByPage(model);
		InventoryBatchSnapshotModel inventoryBatchSnapshotModel = (InventoryBatchSnapshotModel) pageModel.getModel();
		List<InventoryBatchSnapshotModel> ibsList = new ArrayList<InventoryBatchSnapshotModel>();
		List<InventoryBatchSnapshotModel> list = inventoryBatchSnapshotModel.getList();
		for(InventoryBatchSnapshotModel ibsModel:list){
			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId", ibsModel.getGoodsId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
			ibsModel.setGoodsName(goods.getName());
			//获取出仓仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", ibsModel.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			ibsModel.setDepotName(depot.getName());
			ibsList.add(ibsModel);
		}
		inventoryBatchSnapshotModel.setList(ibsList);
		return inventoryBatchSnapshotModel;
	}

	@Override
	public List<Map<String, Object>> exportInventoryBatchSnapshotModelMap(InventoryBatchSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = mapper.exportInventoryBatchSnapshotModelMap(model);
		for(Map<String, Object> map:list){
			if(map.containsKey("goods_id") && map.get("goods_id") != null){
				Long goods_id = (Long) map.get("goods_id");
				//获取商品信息
				Map<String, Object> params = new HashMap<String,Object>();
				params.put("merchandiseId", goods_id);
				MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
				if(goods != null){
					map.put("goods_id", goods.getMerchandiseId());
				}
			}
			if(map.containsKey("depot_id") && map.get("depot_id") != null){
				Long depot_id = (Long) map.get("depot_id");
				//获取出仓仓库信息
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("depotId", depot_id);
				DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
				if(depot != null){
					map.put("depot_id", depot.getDepotId());
				}
			}
			exportList.add(map);
		}
		return exportList;
	}

	@Override
	public int updateInventoryBatchSnapshotAvailableNum(Map<String, Object> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.updateInventoryBatchSnapshotAvailableNum(map);
	}

	/*@Override
	public List<InventoryBatchSnapshotModel> countInventoryBatchSnapshotByGoodsStock(InventoryBatchSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.countInventoryBatchSnapshotByGoodsStock(model);
	}*/

	@Override
	public InventoryBatchSnapshotModel selectByOne(InventoryBatchSnapshotModel model) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectByOne(model);
	}

	/**
	 * 分页
	 * @return
	 * @throws Exception
	 */
	@Override
	public InventoryBatchSnapshotDTO getListByPage(InventoryBatchSnapshotDTO model) throws SQLException {
		PageDataModel<InventoryBatchSnapshotDTO> pageModel = mapper.getListByPage(model);
		return (InventoryBatchSnapshotDTO) pageModel.getModel();
	}
	
	@Override
	public void delData() {
		mapper.delData();
	}

}
