package com.topideal.inventory.webapi;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.dto.MonthlyAccountItemDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.MonthlyAccountItemService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.MonthlyAccountItemResponseDTO;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 库存管理-月结账单商品明细-控制层  
 */
@RestController
@RequestMapping("/webapi/inventory/monthlyAccountItem")
@Api(tags = "库存管理月结账单商品明细控制层 ")
public class APIMonthlyAccountItemController {

	// 月结账单商品明细service
	@Autowired
	private MonthlyAccountItemService monthlyAccountItemService;

	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb


	
	/**
	 * 月结账单商品明细页面
	 * */
	@ApiOperation(value = "月结账单商品明细页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/toDetailPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean<MonthlyAccountItemResponseDTO>  toDetailPage(String token,String id)throws Exception  {
		try {
			User user= ShiroUtils.getUserByToken(token);
			MonthlyAccountDTO monthlyAccountModel=monthlyAccountService.searchDTOById(Long.valueOf(id));
			MonthlyAccountItemDTO  monthlyAccountItemModel=new MonthlyAccountItemDTO();
			monthlyAccountItemModel.setMonthlyAccountId(Long.valueOf(id));
			List<MonthlyAccountItemDTO>  monthlyAccountItemModelList= monthlyAccountItemService.searchlist(monthlyAccountItemModel,monthlyAccountModel.getDepotId(),monthlyAccountModel.getFirstDate());
			MonthlyAccountItemResponseDTO responseDTO=new MonthlyAccountItemResponseDTO();
			responseDTO.setUserType( user.getUserType());
			responseDTO.setMonthlyAccountModel(monthlyAccountModel);
			responseDTO.setMonthlyAccountItemModelList(monthlyAccountItemModelList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}
	
	
	/**
	 *  按库存量结转校验
	 * */
	@ApiOperation(value = "按库存量结转校验")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/checkMonthlySurplusNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean checkMonthlySurplusNum(String token,String id) {
		 MonthlyAccountModel    montAccountModel =null;
		 List<MonthlyAccountModel>  montAccountModelList=null;
		try{
			User user= ShiroUtils.getUserByToken(token);
            //校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if(isNull){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结ID为空");
            }
            MonthlyAccountModel monthlyAccountModel = monthlyAccountService.searchById(Long.valueOf(id));
            if(monthlyAccountModel==null){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");
            }
            //检查状态 1未转结 2 已转结
            if(!monthlyAccountModel.getState().equals("1")){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");
            }
        	montAccountModel=new MonthlyAccountModel();
        	montAccountModel.setMerchantId(monthlyAccountModel.getMerchantId());
        	montAccountModel.setDepotId(monthlyAccountModel.getDepotId());
        	montAccountModel.setState(monthlyAccountModel.getState());
        	montAccountModelList=monthlyAccountService.getMonthlyAccountListByMonth(montAccountModel);
    		montAccountModel=montAccountModelList.get(0);
    		if(!montAccountModel.getSettlementMonth().equals(monthlyAccountModel.getSettlementMonth())){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请先结转本仓库本月之前的月结");
    		}
    		//更新月结明细结转余量
    		MonthlyAccountItemModel model=new  MonthlyAccountItemModel();
            model.setMonthlyAccountId(Long.valueOf(id));
            List<MonthlyAccountItemModel> checkMonthlySurplusList = monthlyAccountItemService.checkMonthlySurplusNum(model);
            if (checkMonthlySurplusList.size()>0) {
            	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000.getCode(),"仓库余量和现存量 存在不等货号是否强制结转");
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
           
        }catch(Exception e){
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
	}
	
	
	/**
	 *  按库存量结转
	 * */
	@ApiOperation(value = "按库存量结转")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/confirmMonthlySurplusNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean confirmMonthlySurplusNum(String token,String id) {
		 MonthlyAccountModel    montAccountModel =null;
		 List<MonthlyAccountModel>  montAccountModelList=null;
		try{
			User user= ShiroUtils.getUserByToken(token);
            //校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if(isNull){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结ID为空");
            }
            MonthlyAccountModel monthlyAccountModel = monthlyAccountService.searchById(Long.valueOf(id));
            if(monthlyAccountModel==null){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");

            }
            //检查状态 1未转结 2 已转结
            if(!monthlyAccountModel.getState().equals("1")){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结状态非未结转");
            }
        	montAccountModel=new MonthlyAccountModel();
        	montAccountModel.setMerchantId(monthlyAccountModel.getMerchantId());
        	montAccountModel.setDepotId(monthlyAccountModel.getDepotId());
        	montAccountModel.setState(monthlyAccountModel.getState());
        	montAccountModelList=monthlyAccountService.getMonthlyAccountListByMonth(montAccountModel);
    		montAccountModel=montAccountModelList.get(0);
    		if(!montAccountModel.getSettlementMonth().equals(monthlyAccountModel.getSettlementMonth())){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请先结转本仓库本月之前的月结");
    		}
    		//更新月结明细结转余量
    		MonthlyAccountItemModel model=new  MonthlyAccountItemModel();
            model.setMonthlyAccountId(Long.valueOf(id));
            boolean b = monthlyAccountItemService.confirmMonthlySurplusNum(model);
            if(b==false){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"更新月结明细结转数量失败");
            }
            boolean falg=monthlyAccountService.updateMonthlyAccountState(monthlyAccountModel,user.getId(),user.getName());
            if(falg==false){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"更新月结状态失败");
            }else {// 月结成功 就发消息 新增 月结账单快照  状态为已完结数据
            	JSONObject jSONObject=new JSONObject();
				jSONObject.put("merchantId", monthlyAccountModel.getMerchantId());
				jSONObject.put("depotId", monthlyAccountModel.getDepotId());
				jSONObject.put("settlementMonth", monthlyAccountModel.getSettlementMonth());
				System.out.println("生成已结转月结账单快照"+jSONObject.toString());
				// 推送报表消费端
				SendResult sendResult = rocketMQProducer.send(jSONObject.toString(),MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTopic(),
						MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTags());								
				// 查询月结关账表数据 
				Map<String, Object> params=new HashMap<>();
				params.put("merchantId", monthlyAccountModel.getMerchantId());
				params.put("depotId", monthlyAccountModel.getDepotId());
				params.put("source", DERP.CLOSE_ACCOUNTS_SOURCE_2);
				FinanceCloseAccountsMongo closeAccounts = financeCloseAccountsMongoDao.findOne(params); 
				String accountstMonth=monthlyAccountModel.getSettlementMonth();
				if (closeAccounts!=null) {
					String month = closeAccounts.getMonth();
					if (Timestamp.valueOf(month+"-01 00:00:00").getTime()>Timestamp.valueOf(accountstMonth+"-01 00:00:00").getTime()) {
						accountstMonth=month;
					}
					//删除之前数据
					financeCloseAccountsMongoDao.remove(params);
				}
				
				// 向已经财务月结和关账mongdb中 插入已经月结的数据
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(monthlyAccountModel.getMerchantId());
				closeAccountsMongo.setMerchantName(monthlyAccountModel.getMerchantName());
				closeAccountsMongo.setDepotId(monthlyAccountModel.getDepotId());
				closeAccountsMongo.setDepotName(monthlyAccountModel.getDepotName());				
				closeAccountsMongo.setMonth(accountstMonth);			
				closeAccountsMongo.setStatus(monthlyAccountModel.getState());
				String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
				closeAccountsMongo.setCreateDateStr(createDateStr);
				closeAccountsMongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_2);//1.财务经销存关账 2.已经月结				
				JSONObject jsonObject = JSONObject.fromObject(closeAccountsMongo);
				//新增
				financeCloseAccountsMongoDao.insertJson(jsonObject.toString());
			}
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
	}
	
	
	/**
	 *  按现存量结转 (生成库存调整单)
	 * */

	@ApiOperation(value = "按现存量结转 (生成库存调整单)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/createMonthlyAvailableNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean createMonthlyAvailableNum(String token,String id) {
		MonthlyAccountModel moAccountModel = null;
		List<MonthlyAccountModel> montAccountModelList = null;
		String json=null;
		try{
			User user= ShiroUtils.getUserByToken(token);
            //校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if(isNull){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"id参数为空");
            }
            MonthlyAccountModel monthlyAccountModel = monthlyAccountService.searchById(Long.valueOf(id));
            if(monthlyAccountModel==null){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");
            }
            //检查状态 1未转结 2 已转结
            if(!monthlyAccountModel.getState().equals("1")){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结状态非未结转");
            }
        	moAccountModel=new MonthlyAccountModel();
        	moAccountModel.setMerchantId(monthlyAccountModel.getMerchantId());
        	moAccountModel.setDepotId(monthlyAccountModel.getDepotId());
        	moAccountModel.setState(monthlyAccountModel.getState());
        	montAccountModelList=monthlyAccountService.getMonthlyAccountListByMonth(moAccountModel);
    		moAccountModel=montAccountModelList.get(0);
    		if(!moAccountModel.getSettlementMonth().equals(monthlyAccountModel.getSettlementMonth())){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请先结转本仓库本月之前的月结");
    		}
			//获取需要创建库存调整单月结详情数据（如果是库存量等于仓库现存量就进行过滤）
			MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
			monthlyAccountItemModel.setMonthlyAccountId(Long.valueOf(id));
			List<Map<String,Object>> mapList = monthlyAccountItemService.createMonthlyAvailableNum(monthlyAccountModel.getDepotId(),monthlyAccountModel.getFirstDate(),monthlyAccountItemModel);
			if(mapList == null||mapList.size()<=0){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"库存余量与现存量无差异,请点击按计算库存量结转!");
		    }
        	///mapList 这里的数据推送仓储的库存调整单模块
	    	json=monthlyAccountService.producedAdjustmentInventory(user.getId(), user.getName(), monthlyAccountModel, mapList);
	    	//月结账单推送库存调整
	    	rocketMQProducer.send(json, MQStorageEnum.DERP_MONTH_INVENTORY_CARRY.getTopic(), MQStorageEnum.DERP_MONTH_INVENTORY_CARRY.getTags());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
	}

	/**
	 *  按现存量结转 (修改月结状态,同时更新期末库存值)
	 * */
	@ApiOperation(value = "按现存量结转 (修改月结状态,同时更新期末库存值)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/confirmMonthlyAvailableNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean confirmMonthlyAvailableNum(String token,String id) {
		MonthlyAccountModel montAccountModel =null;
		List<MonthlyAccountModel>  montAccountModelList=null;
		try{
			User user= ShiroUtils.getUserByToken(token);
            //校验id是否正确
			boolean isNull = new EmptyCheckUtils().
					addObject(id).
					empty();
            if(isNull){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结ID为空");
            }
            
            MonthlyAccountModel monthlyAccountModel = monthlyAccountService.searchById(Long.valueOf(id));
            if(monthlyAccountModel==null){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");
            }
            //检查状态 1未转结 2 已转结
            if(!monthlyAccountModel.getState().equals("1")){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结状态非未结转");
            }
        	montAccountModel=new MonthlyAccountModel();
        	montAccountModel.setMerchantId(monthlyAccountModel.getMerchantId());
        	montAccountModel.setDepotId(monthlyAccountModel.getDepotId());
        	montAccountModel.setState(monthlyAccountModel.getState());
        	montAccountModelList=monthlyAccountService.getMonthlyAccountListByMonth(montAccountModel);
    		montAccountModel=montAccountModelList.get(0);
    		//检查仓库本月之前是否有未结转的月份
    		if(!montAccountModel.getSettlementMonth().equals(monthlyAccountModel.getSettlementMonth())){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请先结转本仓库本月之前的月结");
    		}
    		//更新结转数量
			MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
			monthlyAccountItemModel.setMonthlyAccountId(Long.valueOf(id));
			boolean falg = monthlyAccountItemService.confirmMonthlyAvailableNum(monthlyAccountItemModel);
			if(falg==false){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"更新月结明细结转数量失败");
			}
			//更新月结库存状态
			falg=monthlyAccountService.updateMonthlyAccountState(monthlyAccountModel,user.getId(),user.getName());
			if(falg==false){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"更新月结状态失败");
			}else {// 月结成功 就发消息 新增 月结账单快照  状态为已完结数据
				JSONObject jSONObject=new JSONObject();
				jSONObject.put("merchantId", monthlyAccountModel.getMerchantId());
				jSONObject.put("depotId", monthlyAccountModel.getDepotId());
				jSONObject.put("settlementMonth", monthlyAccountModel.getSettlementMonth());
				System.out.println("生成已结转月结账单快照"+jSONObject.toString());
				// 推送报表消费端
				SendResult sendResult = rocketMQProducer.send(jSONObject.toString(),MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTopic(),
						MQInventoryEnum.INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT.getTags());
				
				
				// 查询月结关账表数据 
				Map<String, Object> params=new HashMap<>();
				params.put("merchantId", monthlyAccountModel.getMerchantId());
				params.put("depotId", monthlyAccountModel.getDepotId());
				params.put("source", DERP.CLOSE_ACCOUNTS_SOURCE_2);
				FinanceCloseAccountsMongo closeAccounts = financeCloseAccountsMongoDao.findOne(params); 
				String accountstMonth=monthlyAccountModel.getSettlementMonth();
				if (closeAccounts!=null) {
					String month = closeAccounts.getMonth();
					if (Timestamp.valueOf(month+"-01 00:00:00").getTime()>Timestamp.valueOf(accountstMonth+"-01 00:00:00").getTime()) {
						accountstMonth=month;
					}
					//删除之前数据
					financeCloseAccountsMongoDao.remove(params);
				}
				
				// 向已经财务月结和关账mongdb中 插入已经月结的数据
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(monthlyAccountModel.getMerchantId());
				closeAccountsMongo.setMerchantName(monthlyAccountModel.getMerchantName());
				closeAccountsMongo.setDepotId(monthlyAccountModel.getDepotId());
				closeAccountsMongo.setDepotName(monthlyAccountModel.getDepotName());				
				closeAccountsMongo.setMonth(accountstMonth);			
				closeAccountsMongo.setStatus(monthlyAccountModel.getState());
				String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
				closeAccountsMongo.setCreateDateStr(createDateStr);
				closeAccountsMongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_2);//1.财务经销存关账 2.已经月结				
				JSONObject jsonObject = JSONObject.fromObject(closeAccountsMongo);
				//新增
				financeCloseAccountsMongoDao.insertJson(jsonObject.toString());

				
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
	}
	/**
	 *  校验月结结转是否存在差异
	 * */
	@ApiOperation(value = "校验月结结转是否存在差异")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结账单id")
	})
	@PostMapping(value="/isDifference.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean isDifference(String id) {
		try{
            //校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if(isNull){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"月结ID为空");
            }
            MonthlyAccountModel monthlyAccountModel = monthlyAccountService.searchById(Long.valueOf(id));
            if(monthlyAccountModel==null){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"未找到月结");
            }
			MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
			monthlyAccountItemModel.setMonthlyAccountId(Long.valueOf(id));
			List<Map<String,Object>> mapList = monthlyAccountItemService.createMonthlyAvailableNum(monthlyAccountModel.getDepotId(),monthlyAccountModel.getEndDate(),monthlyAccountItemModel);
			String status = "false";
			if(mapList != null && mapList.size() > 0){
				status = "true";
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
	}
}
