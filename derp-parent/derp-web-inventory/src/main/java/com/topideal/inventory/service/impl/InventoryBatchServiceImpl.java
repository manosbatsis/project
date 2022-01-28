package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import net.sf.json.JSONObject;

/**
 * 库存管理-批次库存明细-service实现类
 */
@Service
public class InventoryBatchServiceImpl implements InventoryBatchService {

	//批次库存明细信息dao
    @Autowired
    private InventoryBatchDao inventoryBatchDao;
    //商品
  	@Autowired
  	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
  	@Autowired
	private BrandMongoDao brandMongoDao;
    /**
	 * 批次库存明细列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public InventoryBatchDTO listInventoryBatch(InventoryBatchDTO model)throws SQLException {
    	String  result ="";  	                    
		if("1".equals(model.getValidityType())){//低于1/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/3);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		}else if("2".equals(model.getValidityType())){//高于1/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/3);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		}else if("3".equals(model.getValidityType())){//低于1/2
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		} else if("4".equals(model.getValidityType())){//高于1/2
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		} else if("5".equals(model.getValidityType())){//低于2/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)2/3); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		} else if("6".equals(model.getValidityType())){//高于2/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)2/3); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		} else if("7".equals(model.getValidityType())){//过期
			model.setValidityTime(Double.valueOf("0"));
			model.setValidityType("5");
		}   else if("8".equals(model.getValidityType())){//低于1/2+60
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2+60); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("8");
		}   else if("9".equals(model.getValidityType())){//高于1/2+60
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2+60); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("9");
		}  
    	return inventoryBatchDao.getInventoryBatchByPage(model);
    }

    
    /**
   	 * 效期预警列表（分页）
   	 * @param model 
   	 * @return
   	 */
	@Override
	public InventoryBatchDTO selectInventoryWarningByPage(InventoryBatchDTO model,String validityTypes) throws Exception {
		if (StringUtils.isNotBlank(validityTypes)) {
			List<String> validityTypeList = Arrays.asList(validityTypes.split(","));
			for (String validityType : validityTypeList) {
				// 设置startDouble的最小值
				if ("1".equals(validityType)) {
					model.setValidityType1("1");
				}
				if ("2".equals(validityType)) {
					model.setValidityType2("2");				
				}
				if ("3".equals(validityType)) {
					model.setValidityType3("3");	
				}
				if ("4".equals(validityType)) {
					model.setValidityType4("4");
				}
				if ("5".equals(validityType)) {
					model.setValidityType5("5");
				}
				if ("6".equals(validityType)) {
					model.setValidityType6("6");
				}
				if ("7".equals(validityType)) {
					model.setValidityType7("7");
				}

			}
			
		}
		return inventoryBatchDao.selectInventoryWarningByPage(model);
	}
	
	/**
	 *   商品库存明细
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public InventoryBatchDTO listProductInventoryDetailsByPage(InventoryBatchDTO model) throws Exception {
		InventoryBatchDTO inventoryBatch = inventoryBatchDao.listProductInventoryDetailsByPage(model);
		List<InventoryBatchDTO> batchList= new ArrayList<InventoryBatchDTO>();
		List<InventoryBatchDTO> list= inventoryBatch.getList();
		for(InventoryBatchDTO inventoryBatchModel :list){
			//根据商品id、商家id获取商品信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", inventoryBatchModel.getGoodsId());
		/*	params.put("merchantId", inventoryBatchModel.getMerchantId());*/
			MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(params);
			if(merchandiseInfo != null){
				inventoryBatchModel.setBarcode(merchandiseInfo.getBarcode());
				inventoryBatchModel.setGoodsName(merchandiseInfo.getName());
				if (merchandiseInfo.getBrandId()!=null) {
					Map<String, Object> params2 = new HashMap<String,Object>();
					params2.put("brandId", merchandiseInfo.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(params2);
					if(brandMongo != null){
						inventoryBatchModel.setBrandName(brandMongo.getName());
					}
				}
			}
			batchList.add(inventoryBatchModel);
		}
		inventoryBatch.setList(batchList);
		return inventoryBatch;
	}

	
	/**
	 * 每天定时扫描批次的过期日期如果该商品已过期就变更为过期标识
	 * @throws Exception
	 */
	@Override
	public void updateInventoryBatchTypeQuartz() throws Exception {
		// TODO Auto-generated method stub
		InventoryBatchModel  model=new  InventoryBatchModel();
		List<InventoryBatchModel> inventoryBatchList=   inventoryBatchDao.list(model);
		if(inventoryBatchList!=null&&inventoryBatchList.size()>0){
			for(InventoryBatchModel inventoryBatchModel:inventoryBatchList ){
				if(inventoryBatchModel.getOverdueDate()!=null){
					if(inventoryBatchModel.getOverdueDate().getTime()<TimeUtils.getNow().getTime()){
						inventoryBatchModel.setIsExpire("1");
						inventoryBatchDao.modify(inventoryBatchModel);
					}
				}
			}
		}
	}

	
	/**
     *  导出批次库存明细
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> exportInventoryBatchMap(InventoryBatchDTO model) throws Exception {
		// TODO Auto-generated method stub
		String  result ="";  	                    
		if("1".equals(model.getValidityType())){//低于1/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/3);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		}else if("2".equals(model.getValidityType())){//高于1/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/3);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		}else if("3".equals(model.getValidityType())){//低于1/2
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		} else if("4".equals(model.getValidityType())){//高于1/2
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2);  
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		} else if("5".equals(model.getValidityType())){//低于2/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)2/3); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("1");
		} else if("6".equals(model.getValidityType())){//高于2/3
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)2/3); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("2");
		} else if("7".equals(model.getValidityType())){//过期
			model.setValidityTime(Double.valueOf("0"));
			model.setValidityType("5");
		}   else if("8".equals(model.getValidityType())){//低于1/2+60
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2+60); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("8");
		}   else if("9".equals(model.getValidityType())){//高于1/2+60
			DecimalFormat df = new DecimalFormat("0.00");   
			  result = df.format((float)1/2+60); 
			model.setValidityTime(Double.valueOf(result));
			model.setValidityType("9");
		}  
		return inventoryBatchDao.exportInventoryBatchMap(model);
	}

	
	 /**
     *  导出商品库存明细
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public Map<String,Object> exportProductInventoryDetailsMap(Long merchantId,String depotIds,String goodsNo)
			throws Exception {
		

		List<Long> depotIdsList=new ArrayList<Long>();
		if (StringUtils.isNotBlank(depotIds)&&!"null".equals(depotIds)) {
			List<String> asList = Arrays.asList(depotIds.split(","));
			for (String depotIdStr : asList) {
				if (StringUtils.isNotBlank(depotIdStr))depotIdsList.add(Long.valueOf(depotIdStr));					
			}
		}
		
		
		InventoryBatchDTO inventoryBatchModel = new InventoryBatchDTO();
		inventoryBatchModel.setMerchantId(merchantId);
		inventoryBatchModel.setDepotIdsList(depotIdsList);
		inventoryBatchModel.setGoodsNo(goodsNo);
		List<InventoryBatchDTO> inventoryBatchModelList0 =inventoryBatchDao.exportProductInventoryDetails(inventoryBatchModel);

		List<InventoryBatchDTO> inventoryBatchModelList1=inventoryBatchDao.getInventoryBatchExport(inventoryBatchModel);
		
		Map<String, Object>resultMap=new HashMap<>();
		resultMap.put("result0", inventoryBatchModelList0);
		resultMap.put("result1", inventoryBatchModelList1);
		
		return resultMap;
	}


	
	 /**
     * 导出效期预警
     * @param list
     * @return
     */
	@Override
	public List<Map<String, Object>> exportInventoryWarningMap(InventoryBatchDTO model,String validityTypes) throws Exception {
		String  result ="";
		if (StringUtils.isNotBlank(validityTypes)) {
			List<String> validityTypeList = Arrays.asList(validityTypes.split(","));
			for (String validityType : validityTypeList) {
				// 设置startDouble的最小值
				if ("1".equals(validityType)) {
					model.setValidityType1("1");
				}
				if ("2".equals(validityType)) {
					model.setValidityType2("2");				
				}
				if ("3".equals(validityType)) {
					model.setValidityType3("3");	
				}
				if ("4".equals(validityType)) {
					model.setValidityType4("4");
				}
				if ("5".equals(validityType)) {
					model.setValidityType5("5");
				}
				if ("6".equals(validityType)) {
					model.setValidityType6("6");
				}
				if ("7".equals(validityType)) {
					model.setValidityType7("7");
				}

			}
		}
		return inventoryBatchDao.exportInventoryWarningMap(model);
	}


	/**
     * 统计批次库存明细 好品和坏品 已过期的数量
     * @param merchantId
     * @return
     * @throws Exception
     */
	@Override
	public InventoryBatchModel countInventoryAmount(InventoryBatchModel model) throws Exception {
		// TODO Auto-generated method stub
		return inventoryBatchDao.countInventoryAmount(model);
	}


	@Override
	public List<InventoryBatchModel> getAvailableNum(Long merchantId, Long depotId, Long goodsId, String unit) {
		return inventoryBatchDao.getAvailableNum(merchantId,depotId,goodsId,unit);
	}

	/**
	 * 根据商家,仓库,商品,理货单位,批次,好坏品,过期品 查询库存余量 
	 * @param merchantId
	 * @param depotId
	 * @param goodsId
	 * @param unit
	 * @param type
	 * @param isExpire
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getBadOrExpiredSurplusNum(Long merchantId, Long depotId, Long goodsId, String unit,
			String type, String isExpire, String batchNo) throws Exception {
		
		return inventoryBatchDao.getBadOrExpiredSurplusNum(merchantId,depotId,goodsId,unit,type,isExpire,batchNo);
	}


	/**
	 * 根据仓库ID 查询仓库库存余量不为0的所有商品是否都有批次效期
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getBatchValidation(Long depotId) throws Exception {
		
		JSONObject jSONObject = new  JSONObject();	
		List<InventoryBatchModel> inventoryBatchList = inventoryBatchDao.getBatchValidation(depotId);
		if (inventoryBatchList.size()>0) {
			jSONObject.put("status", "0");// 0失败 1成功
			jSONObject.put("msg", "批次库存 存在商品是批次效期为空不能修改");//成功失败信息
			return jSONObject;
		}
		// 查询效期
//		InventoryBatchModel model = new InventoryBatchModel();
//		model.setDepotId(depotId);
//		InventoryBatchModel model1 = inventoryBatchDao.searchByModel(model);
//		if(model1.getProductionDate() == null && model1.getOverdueDate() == null){
//			jSONObject.put("status", "0");// 0失败 1成功
//			jSONObject.put("msg", "批次库存 存在商品是批次效期为空不能修改!");//成功失败信息
//			return jSONObject;
//		}
		jSONObject.put("status", "1");// 0失败 1成功
		jSONObject.put("msg", "成功");//成功失败信息
		return jSONObject;
	}


	@Override
	public List<InventoryBatchModel> getList(InventoryBatchModel model) throws SQLException {
		return inventoryBatchDao.list(model);
	}


	

     

}
