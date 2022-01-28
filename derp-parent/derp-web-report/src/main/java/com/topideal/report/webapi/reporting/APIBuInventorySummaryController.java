package com.topideal.report.webapi.reporting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.BuInventorySummaryDTO;
import com.topideal.entity.dto.BuInventorySummaryItemDTO;
import com.topideal.entity.vo.inventory.MonthlyAccountModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.BuInventorySummaryService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.MonthlyAccountService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuInventoryResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 事业部业务经销存
 */
@RestController
@RequestMapping("/webapi/report/buSummary")
@Api(tags = "事业部业务经销存")
public class APIBuInventorySummaryController {
	
	//经销存汇总报表
	@Autowired
	public BuInventorySummaryService buInventorySummaryService;
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;
	
	@Autowired
	private FileTaskService fileTaskService;
	
	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BuInventoryResponseDTO> toPage(String token)throws Exception  {
		BuInventoryResponseDTO responseDTO=new BuInventoryResponseDTO();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			responseDTO.setNow(sdf.format(new Date()));
			User user=ShiroUtils.getUserByToken(token);
			responseDTO.setUserType(user.getUserType());//'用户类型  1 平台用户  2 商家（超管理）  3 商家用户'
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	/**
	 * 获取分页数据
	 * */
	@ResponseBody
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "productName", value = "商品名称"),
			@ApiImplicitParam(name = "barcode", value = "条码"),
			@ApiImplicitParam(name = "brandIds", value = "品牌id 多个用英文逗号隔开"),
			@ApiImplicitParam(name = "depotIds", value = "仓库id  多个用英文逗号隔开"),
			@ApiImplicitParam(name = "goodsNo", value = "货号")
			
	})
	@PostMapping(value="/buInventorSummaryList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BuInventorySummaryDTO> inventorSummaryList(String token,Integer begin,Integer pageSize,String ownMonth,
			Long buId,String productName,String barcode,String goodsNo,String brandIds,String depotIds) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUserByToken(token);
			BuInventorySummaryDTO model=new BuInventorySummaryDTO();
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model.setOwnMonth(ownMonth);
			model.setBuId(buId);
			model.setProductName(productName);
			model.setBarcode(barcode);
			model.setGoodsNo(goodsNo);
			model.setMerchantId(user.getMerchantId());
			List<Long> parentBrandIds=new ArrayList<Long>();
			if(!StringUtils.isEmpty(brandIds)){
				parentBrandIds=new ArrayList<Long>();
				String[] Ids = brandIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id));
				}
				model.setParentBrandIds(parentBrandIds);
			}
			List<Long> depotListIds=new ArrayList<Long>();
			if (!StringUtils.isEmpty(depotIds)) {
				depotListIds=new ArrayList<Long>();
				String[] Ids = depotIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) depotListIds.add(Long.valueOf(id));
				}
				model.setDepotListIds(depotListIds);
			}
			if(!StringUtils.isEmpty(model.getOwnMonth())){
				model = buInventorySummaryService.listByPage(user,model);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 查询月结状态
	 * */
	@ResponseBody
	@ApiOperation(value = "查询月结状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "settlementMonth", value = "结转月份"),	
			@ApiImplicitParam(name = "depotIds", value = "仓库id  多个用英文逗号隔开")
	})
	@PostMapping(value="/getMonthlyAccount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MonthlyAccountModel> getMonthlyAccount(String token,String depotIds, String settlementMonth) {
		MonthlyAccountModel model=new MonthlyAccountModel();
		try{
			// 多个仓库查询不查月结状态
			List<Long> depotListIds=new ArrayList<Long>();
			if (!StringUtils.isEmpty(depotIds)) {
				String[] Ids = depotIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) depotListIds.add(Long.valueOf(id));
				}
			}
			// 响应结果集
			User user=ShiroUtils.getUserByToken(token);
			model.setMerchantId(user.getMerchantId());
			if(depotListIds.size()>1||depotListIds.size()<1||StringUtils.isEmpty(settlementMonth)){				
			}else {
				//查询月结表头
				model.setMerchantId(user.getMerchantId());
				model.setDepotId(depotListIds.get(0));
				model.setSettlementMonth(settlementMonth);
				model = monthlyAccountService.searchByModel(model);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 详情
	 * @param id 
	 * */
	@ResponseBody
	@ApiOperation(value = "详情(此次改成了分页对接页面是通知后端改动)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id"),	
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "ownMonth", value = "月份"),
			@ApiImplicitParam(name = "goodsNo", value = "货号"),
			@ApiImplicitParam(name = "operateType", value = "加减类型"),
			@ApiImplicitParam(name = "unit", value = "单位")
	})
	@PostMapping(value="/toDetailPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailPage(String token,Integer begin,Integer pageSize,Long merchantId,
			Long depotId,Long buId,String ownMonth,String goodsNo,String operateType,String unit) throws Exception{
		// 到时改成分页
		try {
			BuInventorySummaryItemDTO entity=new BuInventorySummaryItemDTO();
			//entity.setBegin(begin);
			//entity.setPageSize(pageSize);
			entity.setMerchantId(merchantId);
			entity.setDepotId(depotId);
			entity.setBuId(buId);
			entity.setOwnMonth(ownMonth);
			entity.setGoodsNo(goodsNo);
			entity.setOperateType(operateType);
			entity.setUnit(unit);
			//entity listItem = buInventorySummaryService.listBuItem(entity);
		    return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,entity);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

		
		
		

	}
	/**
	 * 刷新仓库、月汇总报表
	 * */
	@ResponseBody
	@ApiOperation(value = "刷新报表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年份"),	
			@ApiImplicitParam(name = "depotIds", value = "仓库id  多个用英文逗号隔开"),
			@ApiImplicitParam(name = "flashForward", value = "是否强制刷新   false 不强制刷新"),
			@ApiImplicitParam(name = "buId", value = "事业部id")
	})
	@PostMapping(value="/buFlashInventorSummary.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean flashInventorSummary(String token,String ownMonth,String depotIds,String flashForward,Long buId) {
		
		try {
            User user = ShiroUtils.getUserByToken(token);

            if (StringUtils.isEmpty(depotIds) || StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择仓库和月份");//未知异常
            }

            //检查本商家是否存在未完成的刷新任务
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType("SXSYBYW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表  SXSYBCW-刷新事业部财务经销存  SXZHD-刷新自核对  SXYW 刷新业务,SXSYBYW 刷新事业部业务',
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setOwnMonth(ownMonth);
            List<FileTaskMongo> taskList = fileTaskService.getListByParam(taskModel);
            if (taskList != null && taskList.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"存在执行中的任务,稍后再刷新");//未知异常
            }
            taskModel.setTaskName("刷新事业部业务报表");
            taskModel.setUsername(user.getName());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("depotIds", depotIds);
            body.put("month", ownMonth);
            //body.put("syn",syn);
            //body.put("selectTime",selectTime);
            body.put("flashForward", flashForward);
            if (buId != null) {
				body.put("buId",buId.toString());
			}
			
			
			JSONObject json = JSONObject.fromObject(body);
			SendResult result = rocketMQProducer.send(json.toString(), MQReportEnum.BU_INVENTORY_SUMMARY.getTopic(), MQReportEnum.BU_INVENTORY_SUMMARY.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"刷新消息发送失败");//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	

	/**
	 * 创建报表生成任务
	 * */
	@ResponseBody
	@ApiOperation(value = "生成excel")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年份"),	
			@ApiImplicitParam(name = "depotIds", value = "仓库id  多个用英文逗号隔开"),
			@ApiImplicitParam(name = "depotNames", value = "仓库名称,多个用英文逗号隔开"),
			@ApiImplicitParam(name = "depotIdAlls", value = "所有仓库id 多个用英文逗号隔开"),
			@ApiImplicitParam(name = "depotNameAlls", value = "所有英文名称,多个用英文逗号隔开"),
			@ApiImplicitParam(name = "buId", value = "事业部id")
	})
	@PostMapping(value="/createTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createTask(String token,String ownMonth,String depotIds,String depotNames,String depotIdAlls,
			String depotNameAlls,Long buId){

		try{
			if(StringUtils.isEmpty(ownMonth)){
				 return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请选择归属月份");//未知异常
			 }
			String depotNamesMQ="";
            String depotIdsMQ = "";
            if (!StringUtils.isEmpty(depotIds)) {// 如果选择的ids 不为空
                depotIdsMQ = depotIds;
                depotNamesMQ = depotNames;
            } else {// 如果选中的仓库id为空
                depotNamesMQ = depotNameAlls;
                depotIdsMQ = depotIdAlls;
            }
            if (StringUtils.isEmpty(depotNamesMQ)) {
            	depotNamesMQ="";
			}
            User user = ShiroUtils.getUserByToken(token);
            //检查本商家是否存在未完成的刷新任务
            FileTaskMongo taskModelQuery = new FileTaskMongo();
            taskModelQuery.setTaskType("SXSYBYW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表  SXSYBCW-刷新事业部财务经销存  SXZHD-刷新自核对  SXYW 刷新业务,SXSYBYW 刷新事业部业务',
            taskModelQuery.setMerchantId(user.getMerchantId());
            taskModelQuery.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModelQuery.setOwnMonth(ownMonth);
            List<FileTaskMongo> taskList = fileTaskService.getListByParam(taskModelQuery);
            if (taskList != null && taskList.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新  请稍后再导出报表");//未知异常
            }
            
           

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType("SYBYWJXC");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表
            taskModel.setTaskName("事业部业务进销存汇总报表");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("depot_id", depotIdsMQ);
            paramJson.put("own_month", ownMonth);
            String buName="";
            if (buId != null) {
                paramJson.put("bu_id", buId);               
                BusinessUnitModel buModel=buInventorySummaryService.getBusinessUnit(buId);
                if (buModel!=null)buName=buModel.getName();
            }
            paramJson.put("bu_id", buId);
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
            taskModel.setRemark(ownMonth+buName+ depotNamesMQ + "事业部业务进销存报表");
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

}
