package com.topideal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryBatchSnapshotDao;
import com.topideal.dao.InventoryGoodsSnapshotDao;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.InventoryGoodsBatchSnapshotService;

/**
 * 库存商品批次快照
 * @author 联想302 baols 2018/06/11
 * @modify zhanghx
 */
@Service
public class InventoryGoodsBatchSnapshotServiceImpl implements InventoryGoodsBatchSnapshotService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryGoodsBatchSnapshotServiceImpl.class);
	// 库存商品快照
	@Autowired
	private InventoryGoodsSnapshotDao inventoryGoodsSnapshotDao;

	// 库存商品快照
	@Autowired
	private InventoryBatchSnapshotDao inventoryBatchSnapshotDao;

	// 批次库存明细
	@Autowired
	private InventoryBatchDao inventoryBatchDao;

	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品		
	@Autowired
	private BrandMongoDao brandMongoDao;//品牌
	
	

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201302200, model = DERP_LOG_POINT.POINT_13201302200_Label)
	public boolean synsInventoryGoodsBatchSnapshot(String json, String keys, String topics, String tags)
			throws Exception {
		LOGGER.info("=============库存商品批次快照=================>" + json);
		
		InventoryBatchModel model = new InventoryBatchModel();
		List<InventoryBatchModel> inveBatchList = inventoryBatchDao.searchInventoryBatchModelByGoodsList(model);

		if (inveBatchList != null && inveBatchList.size() > 0) {
			// 保存库存批次快照
			for (InventoryBatchModel inveBatchModel : inveBatchList) {

				this.saveInventoryBatchSnapshotModel(inveBatchModel);
			}
			// 保存库存商品快照
			InventoryGoodsSnapshotModel inventoryGoodsSnapshotModel = new InventoryGoodsSnapshotModel();
			inventoryGoodsSnapshotModel.setCreateDate(TimeUtils.getNow());
			List<InventoryGoodsSnapshotModel> snapshotModelList = inventoryGoodsSnapshotDao.getInventoryGoodsSnapshotList(inventoryGoodsSnapshotModel);
			if (snapshotModelList != null && snapshotModelList.size() > 0) {
				for (InventoryGoodsSnapshotModel snapshotModel : snapshotModelList) {
					// 根据仓库id查询仓库
					Map<String, Object> depotInfoMap = new HashMap<>();					
					depotInfoMap.put("depotId", snapshotModel.getDepotId());// 仓库id
					DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);// 仓库信息
					// 防止下面报错
					if (depotInfoMongo==null) {
						depotInfoMongo=new DepotInfoMongo();
					}
					// 根据商品id查询商品
					Map<String, Object> merchandiseInfoMap = new HashMap<>();					
					merchandiseInfoMap.put("merchandiseId", snapshotModel.getGoodsId());// 商品id
					MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 商品信息
					if (merchandiseInfoMogo==null) {
						merchandiseInfoMogo=new MerchandiseInfoMogo();
					}
					
					// 查询品牌
					BrandMongo brandMongo=null;
					if (merchandiseInfoMogo.getBrandId()!=null) {
						Map<String, Object> brandMongoMap = new HashMap<>();					
						brandMongoMap.put("brandId", merchandiseInfoMogo.getBrandId());// 品牌id
						brandMongo = brandMongoDao.findOne(brandMongoMap);//
					}
					if (brandMongo==null) {
						brandMongo=new BrandMongo();
					}
					
					this.saveInventoryGoodsSnapshotModel(snapshotModel,depotInfoMongo,merchandiseInfoMogo,brandMongo);
				}
			}

		}

		return true;
	}

	/**
	 * 保存库存批次快照
	 * 
	 * @param inBatchModel
	 * @param availableNum
	 *            实时库存
	 * @return
	 * @throws Exception
	 */
	public boolean saveInventoryBatchSnapshotModel(InventoryBatchModel inBatchModel) throws Exception {
		boolean falg = true;

		InventoryBatchSnapshotModel inventoryBatchModel = new InventoryBatchSnapshotModel();
		inventoryBatchModel.setMerchantId(inBatchModel.getMerchantId());
		inventoryBatchModel.setMerchantName(inBatchModel.getMerchantName());
		inventoryBatchModel.setDepotId(inBatchModel.getDepotId());
		inventoryBatchModel.setDepotName(inBatchModel.getDepotName());
		inventoryBatchModel.setGoodsId(inBatchModel.getGoodsId());
		inventoryBatchModel.setGoodsName(inBatchModel.getGoodsName());
		inventoryBatchModel.setGoodsNo(inBatchModel.getGoodsNo());
		inventoryBatchModel.setBatchNo(inBatchModel.getBatchNo());
		inventoryBatchModel.setProductionDate(inBatchModel.getProductionDate());
		inventoryBatchModel.setOverdueDate(inBatchModel.getOverdueDate());
		inventoryBatchModel.setType(inBatchModel.getType());
		inventoryBatchModel.setUnit(inBatchModel.getUnit());
		inventoryBatchModel.setIsExpire(inBatchModel.getIsExpire());
		inventoryBatchModel.setLpn(inBatchModel.getLpn());
		inventoryBatchModel.setSurplusNum(inBatchModel.getSurplusNum());
		inventoryBatchModel.setCreateDate(TimeUtils.getNow());
		inventoryBatchModel.setTopidealCode(inBatchModel.getTopidealCode());
		inventoryBatchModel.setDepotCode(inBatchModel.getDepotCode());
		inventoryBatchModel.setFreezeNum(inBatchModel.getFreezeNum());
		inventoryBatchModel.setDepotType(inBatchModel.getDepotType());
		inventoryBatchModel.setIsTopBooks(inBatchModel.getIsTopBooks());
		inventoryBatchModel.setOwnMonth(inBatchModel.getOwnMonth());
		inventoryBatchModel.setBarcode(inBatchModel.getBarcode());
		inventoryBatchModel.setBrandId(inBatchModel.getBrandId());
		inventoryBatchModel.setBrandName(inBatchModel.getBrandName());
		inventoryBatchModel.setCommbarcode(inBatchModel.getCommbarcode());
		Long id = inventoryBatchSnapshotDao.save(inventoryBatchModel);
		if (id == null) {
			falg = false;
			LOGGER.error("新增库存批次快照失败");
			throw new Exception("新增库存批次快照细失败");
		}
		return falg;
	}

	/**
	 * 保存库存商品快照
	 * 
	 * @param inBatchModel
	 * @param availableNum
	 *            实时库存
	 * @return
	 * @throws Exception
	 */
	public boolean saveInventoryGoodsSnapshotModel(InventoryGoodsSnapshotModel inBatchModel,DepotInfoMongo depotInfoMongo,MerchandiseInfoMogo merchandiseInfoMogo,BrandMongo brandMongo) throws Exception {
		boolean falg = true;
		InventoryGoodsSnapshotModel inventoryBatchModel = new InventoryGoodsSnapshotModel();
		inventoryBatchModel.setMerchantId(inBatchModel.getMerchantId());
		inventoryBatchModel.setMerchantName(inBatchModel.getMerchantName());
		inventoryBatchModel.setTopidealCode(inBatchModel.getTopidealCode());
		inventoryBatchModel.setDepotId(inBatchModel.getDepotId());//仓库id
		inventoryBatchModel.setDepotName(depotInfoMongo.getName());//仓库名称
		inventoryBatchModel.setDepotType(depotInfoMongo.getType());//仓库类型
		inventoryBatchModel.setDepotCode(depotInfoMongo.getCode());//仓库编码
		inventoryBatchModel.setIsTopBooks(depotInfoMongo.getIsTopBooks());// 是否代销
		inventoryBatchModel.setGoodsId(inBatchModel.getGoodsId());// 商品id
		inventoryBatchModel.setGoodsName(merchandiseInfoMogo.getName());//商品名称
		inventoryBatchModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());//商品货号
		inventoryBatchModel.setBarcode(merchandiseInfoMogo.getBarcode());
		inventoryBatchModel.setSurplusNum(inBatchModel.getSurplusNum());// 库存数量
		inventoryBatchModel.setAvailableNum(inBatchModel.getAvailableNum());// 可用量
		inventoryBatchModel.setFreezeNum(inBatchModel.getFreezeNum());//冻结量
		inventoryBatchModel.setExpireNum(inBatchModel.getExpireNum());//过期数量
		inventoryBatchModel.setBadNum(inBatchModel.getBadNum());// 坏品数量		
		inventoryBatchModel.setCreateDate(TimeUtils.getNow());
		inventoryBatchModel.setUnit(inBatchModel.getUnit());//单位									
		//inventoryBatchModel.setOwnMonth(inBatchModel.getOwnMonth());		
		inventoryBatchModel.setBrandId(brandMongo.getBrandId());
		inventoryBatchModel.setBrandName(brandMongo.getName());
		Long id = inventoryGoodsSnapshotDao.save(inventoryBatchModel);
		if (id == null) {
			falg = false;
			LOGGER.error("新增库存商品快照失败");
			throw new Exception("新增库存商品快照细失败");
		}
		return falg;
	}

}
