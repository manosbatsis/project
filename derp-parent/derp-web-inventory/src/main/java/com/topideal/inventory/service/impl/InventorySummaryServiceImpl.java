package com.topideal.inventory.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventorySummaryDao;
import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.entity.vo.InventorySummaryModel;
import com.topideal.inventory.service.InventorySummaryService;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * 库存管理-商品收发汇总-service实现类
 */
@Service
public class InventorySummaryServiceImpl implements InventorySummaryService {

	//商品收发汇总dao
    @Autowired
    private InventorySummaryDao inventorySummaryDao;
    //商品信息
  	@Autowired
  	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
  	//仓库
  	@Autowired
  	private DepotInfoMongoDao depotInfoMongoDao;

    /**
	 * 商品收发汇总列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InventorySummaryDTO listInventorySummaryByPage(InventorySummaryDTO model)throws Exception {
    	//获取分页数据（基础数据）
    	InventorySummaryDTO inventorySummary = inventorySummaryDao.listInventorySummaryByPage(model);
    	List<InventorySummaryDTO> list = new ArrayList<InventorySummaryDTO>();
    	List<InventorySummaryDTO> inventorySummaryList = inventorySummary.getList();
    	for(InventorySummaryDTO isModel:inventorySummaryList){
    		//获取本月期初库存(月结)
    		Integer openingInventoryNum = 0;
    		List<InventorySummaryModel> openingInventoryNumMap= inventorySummaryDao.getOpeningInventoryNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(openingInventoryNumMap != null && openingInventoryNumMap.size()>0){
    			if(openingInventoryNumMap.get(0).getOpeningInventoryNum() !=null){
    				openingInventoryNum = openingInventoryNumMap.get(0).getOpeningInventoryNum();
    			}
    		}else{//获取本月期初库存(期初)
    			List<InventorySummaryModel> initInventoryNumMap= inventorySummaryDao.getInitInventoryNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),isModel.getUnit());
    			if(initInventoryNumMap != null && initInventoryNumMap.size()>0 
    					&& initInventoryNumMap.get(0).getOpeningInventoryNum() != null){
    				openingInventoryNum = initInventoryNumMap.get(0).getOpeningInventoryNum();
    			}
    		}
    		//获取本月累计入库数量
    		Integer inDepotTotal = 0;
    		List<InventorySummaryModel> inDepotTotalNumMap = inventorySummaryDao.getInDepotTotal(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(inDepotTotalNumMap != null && inDepotTotalNumMap.size()>0 
					&& inDepotTotalNumMap.get(0).getInDepotTotal() != null){
    			inDepotTotal = inDepotTotalNumMap.get(0).getInDepotTotal();
    		}
    		//获取本月累计出库数量
    		Integer outDepotTotal = 0;
    		List<InventorySummaryModel> outDepotTotalNumMap = inventorySummaryDao.getOutDepotTotal(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(outDepotTotalNumMap != null && outDepotTotalNumMap.size()>0 
					&& outDepotTotalNumMap.get(0).getOutDepotTotal() != null){
    			outDepotTotal = outDepotTotalNumMap.get(0).getOutDepotTotal();
    		}
    		//获取电商在途量
    		Integer eOnwayNum = 0;
    		List<InventorySummaryModel> eOnwayNumMap = inventorySummaryDao.getEOnwayNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(eOnwayNumMap != null && eOnwayNumMap.size()>0 
					&& eOnwayNumMap.get(0).geteOnwayNum() != null){
    			eOnwayNum = eOnwayNumMap.get(0).geteOnwayNum();
    		}
    		//获取调拨出库在途量
    		Integer transferOutNum = 0;
    		List<InventorySummaryModel> transferOutNumMap = inventorySummaryDao.getTransferOutNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(transferOutNumMap != null && transferOutNumMap.size()>0 
					&& transferOutNumMap.get(0).getTransferOutNum() != null){
    			transferOutNum = transferOutNumMap.get(0).getTransferOutNum();
    		}
    		//获取销售在途量
    		Integer saleOnwayNum = 0;
    		List<InventorySummaryModel> saleOnwayNumMap = inventorySummaryDao.getSaleOnwayNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(saleOnwayNumMap != null && saleOnwayNumMap.size()>0 
					&& saleOnwayNumMap.get(0).getSaleOnwayNum() != null){
    			saleOnwayNum = saleOnwayNumMap.get(0).getSaleOnwayNum();
    		}
    		//120天的均销量
    		Integer averageNum = 0;
    		List<Map<String,Object>> averageNumMap = inventorySummaryDao.getAverageNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),isModel.getUnit());
    		if(averageNumMap != null && averageNumMap.size()>0 
					&& averageNumMap.get(0).get("average_num") != null){
    			averageNum = new BigDecimal(averageNumMap.get(0).get("average_num").toString()).intValue();
    		}
    		if(averageNum == 0){
    			averageNum = 1;
    		}
    		isModel.setOpeningInventoryNum(openingInventoryNum);
    		isModel.setInDepotTotal(inDepotTotal);
    		isModel.setOutDepotTotal(outDepotTotal);
    		isModel.seteOnwayNum(eOnwayNum);
    		isModel.setTransferOutNum(transferOutNum);
    		isModel.setSaleOnwayNum(saleOnwayNum);
    		Integer surplusNum = openingInventoryNum+inDepotTotal-outDepotTotal;
    		isModel.setSurplusNum(surplusNum);
    		Integer availableNum = surplusNum-eOnwayNum-transferOutNum-saleOnwayNum;
    		isModel.setAvailableNum(availableNum);
    		Integer turnoverDays = Math.round(availableNum/averageNum);
    		isModel.setTurnoverDays(turnoverDays);
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.DAY_OF_MONTH, turnoverDays);
    		Timestamp noSaleDate = new Timestamp(calendar.getTime().getTime());
    		isModel.setNoSaleDate(noSaleDate);
    		//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId", isModel.getGoodsId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
			if(goods != null){
				isModel.setGoodsName(goods.getName());
			}
			//获取出仓仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", isModel.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			if(depot != null){
				isModel.setDepotName(depot.getName());
			}
    		list.add(isModel);
    	}
    	inventorySummary.setList(list);
    	return inventorySummary;
    }

    /**
     * 导出商品收发汇总
     */
	@Override
	public List<Map<String, Object>> exportInventorySummaryMap(Long merchantId,Long depotId,String goodsNo,String currentMonth) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> returnMapList = new ArrayList<Map<String, Object>>();
		InventorySummaryModel model = new InventorySummaryModel();
		model.setMerchantId(merchantId);
		model.setDepotId(depotId);
		model.setGoodsNo(goodsNo);
		model.setCurrentMonth(currentMonth);
		model.setPageNo(1);
		model.setPageSize(99999999);
		List<InventorySummaryDTO> inventorySummaryModelList =inventorySummaryDao.exportInventorySummary(model);
    	for(InventorySummaryDTO isModel:inventorySummaryModelList){
    		//获取本月期初库存(月结)
    		Integer openingInventoryNum = 0;
    		List<InventorySummaryModel> openingInventoryNumMap= inventorySummaryDao.getOpeningInventoryNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(openingInventoryNumMap != null && openingInventoryNumMap.size()>0){
    			if(openingInventoryNumMap.get(0).getOpeningInventoryNum() !=null){
    				openingInventoryNum = openingInventoryNumMap.get(0).getOpeningInventoryNum();
    			}
    		}else{//获取本月期初库存(期初)
    			List<InventorySummaryModel> initInventoryNumMap= inventorySummaryDao.getInitInventoryNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),isModel.getUnit());
    			if(initInventoryNumMap != null && initInventoryNumMap.size()>0 
    					&& initInventoryNumMap.get(0).getOpeningInventoryNum() != null){
    				openingInventoryNum = initInventoryNumMap.get(0).getOpeningInventoryNum();
    			}
    		}
    		//获取本月累计入库数量
    		Integer inDepotTotal = 0;
    		List<InventorySummaryModel> inDepotTotalNumMap = inventorySummaryDao.getInDepotTotal(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(inDepotTotalNumMap != null && inDepotTotalNumMap.size()>0 
					&& inDepotTotalNumMap.get(0).getInDepotTotal() != null){
    			inDepotTotal = inDepotTotalNumMap.get(0).getInDepotTotal();
    		}
    		//获取本月累计出库数量
    		Integer outDepotTotal = 0;
    		List<InventorySummaryModel> outDepotTotalNumMap = inventorySummaryDao.getOutDepotTotal(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(outDepotTotalNumMap != null && outDepotTotalNumMap.size()>0 
					&& outDepotTotalNumMap.get(0).getOutDepotTotal() != null){
    			outDepotTotal = outDepotTotalNumMap.get(0).getOutDepotTotal();
    		}
    		//获取电商在途量
    		Integer eOnwayNum = 0;
    		List<InventorySummaryModel> eOnwayNumMap = inventorySummaryDao.getEOnwayNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(eOnwayNumMap != null && eOnwayNumMap.size()>0 
					&& eOnwayNumMap.get(0).geteOnwayNum() != null){
    			eOnwayNum = eOnwayNumMap.get(0).geteOnwayNum();
    		}
    		//获取调拨出库在途量
    		Integer transferOutNum = 0;
    		List<InventorySummaryModel> transferOutNumMap = inventorySummaryDao.getTransferOutNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(transferOutNumMap != null && transferOutNumMap.size()>0 
					&& transferOutNumMap.get(0).getTransferOutNum() != null){
    			transferOutNum = transferOutNumMap.get(0).getTransferOutNum();
    		}
    		//获取销售在途量
    		Integer saleOnwayNum = 0;
    		List<InventorySummaryModel> saleOnwayNumMap = inventorySummaryDao.getSaleOnwayNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),model.getCurrentMonth(),isModel.getUnit());
    		if(saleOnwayNumMap != null && saleOnwayNumMap.size()>0 
					&& saleOnwayNumMap.get(0).getSaleOnwayNum() != null){
    			saleOnwayNum = saleOnwayNumMap.get(0).getSaleOnwayNum();
    		}
    		//120天的均销量
    		Integer averageNum = 0;
    		List<Map<String,Object>> averageNumMap = inventorySummaryDao.getAverageNum(isModel.getMerchantId(),isModel.getDepotId(),isModel.getGoodsNo(),isModel.getUnit());
    		if(averageNumMap != null && averageNumMap.size()>0 
					&& averageNumMap.get(0).get("average_num") != null){
    			averageNum = new BigDecimal(averageNumMap.get(0).get("average_num").toString()).intValue();
    		}
    		if(averageNum == 0){
    			averageNum = 1;
    		}
    		Map<String, Object> paraMap = new HashMap<String, Object>();
			//获取出仓仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", isModel.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			if(depot != null){
				paraMap.put("depotName", depot.getName());
			}
			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId", isModel.getGoodsId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
			if(goods != null){
				paraMap.put("goodsName", goods.getName());
			}
			paraMap.put("merchantName", isModel.getMerchantName());
			paraMap.put("goodsNo", isModel.getGoodsNo());
			String unitName = "";
			if(isModel.getUnit()!=null && "0".equals(isModel.getUnit())){
				unitName = "托盘";
			}else if(isModel.getUnit()!=null && "1".equals(isModel.getUnit())){
				unitName = "箱";
			}else if(isModel.getUnit()!=null && "2".equals(isModel.getUnit())){
				unitName = "件";
			}
			paraMap.put("unit", unitName);
			paraMap.put("openingInventoryNum", openingInventoryNum);
			paraMap.put("inDepotTotal", inDepotTotal);
			paraMap.put("outDepotTotal", outDepotTotal);
			paraMap.put("saleOnwayNum", saleOnwayNum);
			paraMap.put("eOnwayNum", eOnwayNum);
			paraMap.put("transferOutNum", transferOutNum);
			Integer surplusNum = openingInventoryNum+inDepotTotal-outDepotTotal;
			paraMap.put("surplusNum", surplusNum);
			Integer availableNum = surplusNum-eOnwayNum-transferOutNum-saleOnwayNum;
			paraMap.put("availableNum", availableNum);
			Integer turnoverDays = Math.round(availableNum/averageNum);
			paraMap.put("turnoverDays", turnoverDays);
			Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.DAY_OF_MONTH, turnoverDays);
    		Timestamp noSaleDate = new Timestamp(calendar.getTime().getTime());
			paraMap.put("noSaleDate", noSaleDate);
			returnMapList.add(paraMap);
    	}
		return returnMapList;
	}

   

}
