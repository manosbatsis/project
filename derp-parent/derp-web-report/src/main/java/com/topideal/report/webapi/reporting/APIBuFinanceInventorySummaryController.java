package com.topideal.report.webapi.reporting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
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
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuFinanceInventoryResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 事业部财务经分销汇总报表
 */
@RestController
@RequestMapping("/webapi/report/buFinance")
@Api(tags = "事业部财务经分销汇总报表")
public class APIBuFinanceInventorySummaryController {
	private static final Logger logger = LoggerFactory.getLogger(APIBuFinanceInventorySummaryController.class);
	@Autowired
	public BuFinanceInventorySummaryService service;
	/*@Autowired	
	public FinanceInventorySummaryService financeInventorySummaryService;*/
	@Autowired
	private FileTaskService fileTaskService;
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;

	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "月份")
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BuFinanceInventoryResponseDTO> toPage(Model model,String month)throws Exception  {
		BuFinanceInventoryResponseDTO responseDTO=new BuFinanceInventoryResponseDTO();
		try {
			if (StringUtils.isEmpty(month)) {			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				responseDTO.setNow(sdf.format(new Date()));
			} else {
				responseDTO.setNow(month);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * */	
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "month", value = "年月", required = true),	
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "typeId", value = "二级分类id"),
			@ApiImplicitParam(name = "barcode", value = "条码"),
			@ApiImplicitParam(name = "brandIds", value = "品牌id 多个用英文逗号隔开")
			
	})
	@PostMapping(value="/buFinanceSummaryList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean financeSummaryList(String token,Integer begin,Integer pageSize,String month,
			Long depotId,Long buId,Long typeId,String barcode, 
			String brandIds) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUserByToken(token);
			BuFinanceInventorySummaryDTO model= new BuFinanceInventorySummaryDTO();
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model.setMonth(month);
			model.setDepotId(depotId);
			model.setBuId(buId);
			model.setTypeId(typeId);
			model.setBarcode(barcode);
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
			model = service.getListByPage(user,model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 刷新报表
	 * */
	
	@ResponseBody
	@ApiOperation(value = "刷新报表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "年月", required = true),	
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "buId", value = "事业部id")	
	})
	@PostMapping(value="/buFlashInventorSummary.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean flashInventorSummary(String token,String month, Long depotId, Long  buId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			
			User user=ShiroUtils.getUserByToken(token);
			BuFinanceInventorySummaryModel model=new BuFinanceInventorySummaryModel();
			model.setMerchantId(user.getMerchantId());
			if(StringUtils.isEmpty(month)){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"请输月份");
			}
			
			//检查商家月份是否已关账 状态 029-未关账 030-已关账
			Map<String, Object> statusMap = new HashMap<String, Object>();
        	statusMap.put("merchantId",user.getMerchantId());
        	statusMap.put("month", month);
        	// 页面刷新当事业部为空时 查询是否存在029 未关账的 如果存在 就刷新  /当事业部不为空时 查询是否有关账如果关账 就不刷新
        	if (buId!=null) {
        		statusMap.put("status", DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
        		statusMap.put("buId", buId);
        		String status=service.getSummaryStatus(statusMap);
        		if(!StringUtils.isEmpty(status)&&status.equals(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030)){
    				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"事业部本月已关账");
    			}
			}else {
                statusMap.put("status", DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029);
                String status = service.getSummaryStatus(statusMap);
                if (StringUtils.isEmpty(status) || DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029.equals(status)) {
                } else {
                    return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "所有事业部本月已关账");
                }
            }


            //检查本商家是否存在未完成的刷新任务
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType("SXSYBCW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表  SXSYBCW-刷新事业部财务经销存  SXZHD-刷新自核对  SXYW 刷新业务,SXSYBYW 刷新事业部业务',
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setOwnMonth(month);
            // 查询是否存在执行中的任务
            List<FileTaskMongo> taskList = fileTaskService.getListByParam(taskModel);
            if (taskList != null && taskList.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "存在执行中的任务,稍后再刷新");
            }
            taskModel.setTaskName("刷新事业部财务报表");
            taskModel.setUsername(user.getName());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);

            //页面触发同步如果没指定日期则默认当天
			/*if(StringUtils.isEmpty(selectTime)){
			   selectTime = TimeUtils.formatDay();
			}*/
            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("depotId", depotId == null ? "" : String.valueOf(depotId));
			body.put("month", month);
			//body.put("selectTime", selectTime);//同步数据日期
			//body.put("syn",syn);//是否同步数据 true-是 ，其他-否
			if (buId!=null) {
				body.put("buId",buId.toString());
			}
			JSONObject json = JSONObject.fromObject(body);
			logger.info("json"+json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.BU_FINANCE_SUMMARY.getTopic(), MQReportEnum.BU_FINANCE_SUMMARY.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"刷新消息发送失败");
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
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createTask(String token,String ownMonth,Long buId,String buName){

		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新  请稍后再导出报表");
            }
                       
            
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWJXC);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("事业部财务进销存汇总报表" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("事业部财务进销存报表" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) {
                paramJson.put("buId", buId.toString());
            }

            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 创建报表sd生成任务
	 * */
	@ResponseBody
	@ApiOperation(value = "SD进销存导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createSdTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createSdTask(String token,String ownMonth,Long buId,String buName){
		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新 请稍后再导出报表");
            }
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SDSYBCWJXC);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("SD事业部财务进销存汇总报表" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("SD事业部财务进销存报表" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) {
                paramJson.put("buId", buId.toString());
            }
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	/**
	 * 创建报表总账生成任务
	 * */
	@ResponseBody
	@ApiOperation(value = "总账导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createAllAccountTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createAllAccountTask(String token,String ownMonth,Long buId,String buName){
		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新 请稍后再导出报表");
            }
            
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZZ);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("事业部财务总账汇总" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("事业部财务总账报表导出" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) paramJson.put("buId", buId);
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 暂估应收
	 * */
	@ResponseBody
	@ApiOperation(value = "暂估PDF导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createTempEstimatePdfTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createTempEstimatePdfTask(String token,String ownMonth,Long buId,String buName){

		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新  请稍后再导出报表");
            }
            
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZGYS);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("事业部财务暂估应收" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("事业部财务暂估应收导出" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) paramJson.put("buId", buId);
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	
	/**
	 * 创建利息经销存导出任务
	 * */
	@ResponseBody
	@ApiOperation(value = "创建利息经销存导出任务")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createInterestTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createInterestTask(String token,String ownMonth,Long buId,String buName){
		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新  请稍后再导出报表");
            }
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWLX);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("事业部财务利息进销存汇总报表" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("事业部财务利息进销存汇总报表" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) {
                paramJson.put("buId", buId.toString());
            }
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 创建利息经销存导出任务
	 * */
	@ResponseBody
	@ApiOperation(value = "创建美赞成本差异导出任务")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ownMonth", value = "年月", required = true),	
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "buName", value = "事业部名称")
	})
	@PostMapping(value="/createCostDifferenceTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean createCostDifferenceTask(String token,String ownMonth,Long buId,String buName){
		try {
            if (StringUtils.isEmpty(ownMonth)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择归属月份");
            }
            User user = ShiroUtils.getUserByToken(token);
            String buNameStr = "";
            if (!StringUtils.isEmpty(buId)) {
                buNameStr = "(" + buName + ")";
            }
            FileTaskMongo model = new FileTaskMongo();
            model.setMerchantId(user.getMerchantId());
            model.setOwnMonth(ownMonth);
            model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW);
            model.setState("2");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            List<FileTaskMongo> listByParam = fileTaskService.getListByParam(model);
            if (listByParam.size() > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "正在刷新  请稍后再导出报表");
            }
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCW_MZCYCBDC);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表
            taskModel.setTaskName("事业部财务美赞成本差异报表" + buNameStr);
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(ownMonth);
            taskModel.setState("1");//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("事业部财务美赞成本差异报表" + ownMonth);
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("merchant_id", user.getMerchantId());
            paramJson.put("own_month", ownMonth);
            if (buId != null) {
                paramJson.put("buId", buId.toString());
            }
            System.out.println(paramJson.toString());
            taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
}
