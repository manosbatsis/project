package com.topideal.inventory.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.tools.ResponseFactory;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import net.sf.json.JSONObject;

/**
 * 库存模块外部接口
 * @author baols
 * 2018/6/26
 */
@Controller
@RequestMapping("/queryAvailability")
public class QueryAvailabilityController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryAvailabilityController.class);
	
	

	// 批次库存明细service
	@Autowired
	private InventoryBatchService inventoryBatchService;
	
	
	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;//mongdb仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;//商品
	
	
	/**
	 * 库存可用量接口
	 * @param session
	 * @param depotId
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/getAvailableNum.asyn")
	@ResponseBody
	public JSONObject getAvailableNum (HttpSession session,Long depotId,Long goodsId,String depotCode,String unit,String type,String isExpire,String batchNo){
		LOGGER.info("==============库存可用量接口=============开始");
		LOGGER.info("==============库存可用量接口=============开始"+"depotId:"+depotId+"goodsId:"+goodsId+"depotCode:"+depotCode+"unit"+ unit+"type:"+ type+"isExpire:"+ isExpire+"batchNo:"+ batchNo);
		String messgae="";
		List<InventoryBatchModel> inventoryBatchModelList=null;
		try {
			User user= ShiroUtils.getUser();
			
			
			
			// 添加了3个字段 type (0 正常品  1 残次品)  isExpire(0 过期 1 未过期) batchNo 对应仓储库存调整单其他出和月结损益(出)
			// 月结损益出和其他出不会是黄埔仓 所有班查询放到此处 
			/*if((StringUtils.isNotBlank(type)||StringUtils.isNotBlank(isExpire))
					&&("1".equals(type)||"0".equals(isExpire))) {*/
			if((StringUtils.isNotBlank(type)||StringUtils.isNotBlank(isExpire))
					&&("1".equals(type)||"0".equals(isExpire))) {
				// 盘点仓库是否批次效期强校验
				Map<String, Object> depotInfoMap=new HashMap<>();
				depotInfoMap.put("depotId", depotId);			
				DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
				String depotName = "";
				if(depotInfoMongo != null) {
					depotName = depotInfoMongo.getName();
				}	
				Map<String, Object> merchandiseMap=new HashMap<>();
				merchandiseMap.put("merchandiseId", goodsId);
				merchandiseMap.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
				String merchandiseNo = "";
				if(merchandiseMongo != null) {
					merchandiseNo = merchandiseMongo.getGoodsNo();
				}
				// 如果非批次效期强校验,就查询所有带批次和不带批次总量
				if ("0".equals(depotInfoMongo.getBatchValidation())) {
					batchNo=null;
				}
				// 根据商家,仓库,商品,理货单位,批次,好坏品,过期品 查询库存余量 
				Map<String, Object> badOrExpiredSurplusNumMap = inventoryBatchService.getBadOrExpiredSurplusNum(user.getMerchantId(),depotId,goodsId,unit,type,isExpire,batchNo);					
				if (badOrExpiredSurplusNumMap==null) {
					LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+",好坏品类型type:"+type+",批次batchNo:"+batchNo+",过期品品类型isExpire:"+isExpire+",单位:"+unit+"===无可用库存量");
					messgae+="仓库名称："+depotName+"，商品货号 为："+merchandiseNo+",好坏品类型type:"+type+",批次batchNo:"+batchNo+",过期品品类型isExpire:"+isExpire+",单位:"+unit+"===无可用库存量";
					return ResponseFactory.inventoryError(messgae);
				}else {
					Object surplusNumObject = badOrExpiredSurplusNumMap.get("surplus_num");
					LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+Integer.valueOf(surplusNumObject.toString()));
					return ResponseFactory.inventorySuccess("num",Integer.valueOf(surplusNumObject.toString()));
				}
				
			}
			
			if(StringUtils.isNotBlank(depotCode)){
				//黄埔仓返回写死库存值
				if("WMS_360_06".equals(depotCode)||"618".equals(depotCode)){
					return ResponseFactory.inventorySuccess("num",99999999);
				}else{
					//空值校验
					boolean isNull = new EmptyCheckUtils().
							addObject(depotId).
							addObject(goodsId).
							empty();
					if(isNull){
						LOGGER.info("goodsId或depotId 字段值为空.商家："+user.getMerchantId());
						//输入信息不完整
						return ResponseFactory.inventoryError("goodsId或depotId 字段值为空");
					}
					
					Map<String, Object> depotInfoMap=new HashMap<>();
					depotInfoMap.put("depotId", depotId);			
					DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
					String depotName = "";
					if(depotInfoMongo != null) {
						depotName = depotInfoMongo.getName();
					}	
					Map<String, Object> merchandiseMap=new HashMap<>();
					merchandiseMap.put("merchandiseId", goodsId);
					merchandiseMap.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
					String merchandiseNo = "";
					if(merchandiseMongo != null) {
						merchandiseNo = merchandiseMongo.getGoodsNo();
					}
					
					inventoryBatchModelList= inventoryBatchService.getAvailableNum(user.getMerchantId(),depotId,goodsId,unit);
					if(inventoryBatchModelList!=null&&inventoryBatchModelList.size()>0){
						InventoryBatchModel inventBatchModel=	inventoryBatchModelList.get(0);
						if(inventBatchModel.getAvailableNum()!=null){
							LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+inventBatchModel.getAvailableNum());
							return ResponseFactory.inventorySuccess("num",inventBatchModel.getAvailableNum());
						}else{
							LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===可用库存为空");
							messgae+="仓库名称："+depotName+"，商品货号 为："+merchandiseNo+"===无可用库存量";
							return ResponseFactory.inventoryError(messgae);
						}
					}else{
						LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===无可用库存");
						messgae+="仓库id："+depotName+"，商品id 为："+merchandiseNo+"===无可用库存量";
						return ResponseFactory.inventoryError(messgae);
					}
				}
			}else{
				//空值校验
				boolean isNull = new EmptyCheckUtils().
						addObject(depotId).
						addObject(goodsId).
						empty();
				if(isNull){
					LOGGER.info("goodsId或depotId 字段值为空.商家："+user.getMerchantId());
					//输入信息不完整
					return ResponseFactory.inventoryError("goodsId或depotId 字段值为空");
				}
				Map<String, Object> depotInfoMap=new HashMap<>();
				depotInfoMap.put("depotId", depotId);			
				DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);//调出仓库信息
				String depotName = "";
				if(depotInfoMongo != null) {
					depotName = depotInfoMongo.getName();
				}	
				Map<String, Object> merchandiseMap=new HashMap<>();
				merchandiseMap.put("merchandiseId", goodsId);
				merchandiseMap.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(merchandiseMap);//商品信息
				String merchandiseNo = "";
				if(merchandiseMongo != null) {
					merchandiseNo = merchandiseMongo.getGoodsNo();
				}
				
				
				inventoryBatchModelList= inventoryBatchService.getAvailableNum(user.getMerchantId(),depotId,goodsId,unit);
				if(inventoryBatchModelList!=null&&inventoryBatchModelList.size()>0){
					InventoryBatchModel inventBatchModel=	inventoryBatchModelList.get(0);
					if(inventBatchModel.getAvailableNum()!=null){
						LOGGER.info("==============库存可用量接口============成功，库存可用数里=="+inventBatchModel.getAvailableNum());
						return ResponseFactory.inventorySuccess("num",inventBatchModel.getAvailableNum());
					}else{
						LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===可用库存为空");
						messgae+="仓库名称："+depotName+"，商品货号 为："+merchandiseNo+"===无可用库存量";
						return ResponseFactory.inventoryError(messgae);
					}
				}else{
					LOGGER.info("==============库存可用量接口=========仓库id："+depotId+"，商品id 为："+goodsId+"===无可用库存");
					messgae+="仓库名称："+depotName+"，商品货号 为："+merchandiseNo+"===无可用库存量";
					return ResponseFactory.inventoryError(messgae);
				}
			}
		} catch (Exception e) {
			LOGGER.error("======库存可用量接口异常====", e.getMessage());
			return ResponseFactory.inventoryError(e.getMessage());
		}
		
	}
	
	
	/**
	 * 批次库存明细好品和坏品已过期商品数量
	 * @param session
	 * @param depotId
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/countInventoryAmount.asyn")
	@ResponseBody
	public JSONObject countInventoryAmount (HttpSession session){
		InventoryBatchModel inventoryBatchModel=new InventoryBatchModel();
		try {
			User user= ShiroUtils.getUser();
			InventoryBatchModel model =new InventoryBatchModel();
			if(!"1".equals(user.getUserType())){
				model.setMerchantId(user.getMerchantId());
			}
			inventoryBatchModel=inventoryBatchService.countInventoryAmount(model);
			if(inventoryBatchModel!=null&&inventoryBatchModel.getSurplusNum()!=null){
				return ResponseFactory.inventorySuccess("num",inventoryBatchModel.getSurplusNum());
			}else{
				return ResponseFactory.inventoryError("无过期品数量");
			}
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseFactory.inventoryError(e.getMessage());
		}

	}
	
	
	/**
	 *  未结转的月结账单
	 * */
	@RequestMapping("/listMonthlyAccount.asyn")
	@ResponseBody
	private ViewResponseBean listMonthlyAccount(HttpSession session,MonthlyAccountModel model) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			if(!"1".equals(user.getUserType())){
				model.setMerchantId(user.getMerchantId());
			}
			model.setState("1");//未结转
			model = monthlyAccountService.listMonthlyAccount(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	

}
