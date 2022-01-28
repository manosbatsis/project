/*
 * package com.topideal.report.web.reporting;
 * 
 * import com.alibaba.rocketmq.client.producer.SendResult; import
 * com.topideal.api.smurfs.SmurfsUtils; import
 * com.topideal.common.constant.DERP; import
 * com.topideal.common.constant.DERP_REPORT; import
 * com.topideal.common.enums.MQReportEnum; import
 * com.topideal.common.system.auth.User; import
 * com.topideal.common.system.mq.RMQProducer; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.TimeUtils; import
 * com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel; import
 * com.topideal.mongo.entity.FileTaskMongo; import
 * com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
 * import com.topideal.report.service.reporting.FileTaskService; import
 * com.topideal.report.shiro.ShiroUtils; import net.sf.json.JSONObject; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import org.springframework.util.StringUtils;
 * import org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import java.text.SimpleDateFormat; import java.util.*;
 * 
 *//**
	 * 新财务经分销汇总报表
	 */
/*
 * @Controller
 * 
 * @RequestMapping("/buFinance") public class
 * BuFinanceInventorySummaryController {
 * 
 * @Autowired public BuFinanceInventorySummaryService service;
 * 
 * @Autowired public FinanceInventorySummaryService
 * financeInventorySummaryService;
 * 
 * @Autowired private FileTaskService fileTaskService; // mq
 * 
 * @Autowired private RMQProducer rocketMQProducer;
 * 
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model,String
 * month)throws Exception { if (StringUtils.isEmpty(month)) { SimpleDateFormat
 * sdf = new SimpleDateFormat("yyyy-MM"); model.addAttribute("now",
 * sdf.format(new Date())); } else { model.addAttribute("now", month); } return
 * "derp/reporting/buFinanceInventorySummary-list"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/buFinanceSummaryList.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * financeSummaryList(BuFinanceInventorySummaryModel model, String brandIds) {
 * try{ // 响应结果集 User user=ShiroUtils.getUser();
 * model.setMerchantId(user.getMerchantId()); List<Long> parentBrandIds=new
 * ArrayList<Long>(); if(!StringUtils.isEmpty(brandIds)){ parentBrandIds=new
 * ArrayList<Long>(); String[] Ids = brandIds.split(","); for(String id:Ids){
 * if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id)); }
 * model.setParentBrandIds(parentBrandIds); } model =
 * service.getListByPage(model); }catch(Exception e){ e.printStackTrace();
 * return ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(model); }
 * 
 *//**
	 * 刷新仓库、月汇总报表
	 */
/*
 * @RequestMapping("/buFlashInventorSummary.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * flashInventorSummary(BuFinanceInventorySummaryModel model ) { Map<String,
 * Object> retMap = new HashMap<String, Object>(); try{
 * 
 * User user=ShiroUtils.getUser(); model.setMerchantId(user.getMerchantId());
 * if(StringUtils.isEmpty(model.getMonth())){ retMap.put("code", "01");
 * retMap.put("message", "请输月份"); return ResponseFactory.success(retMap); }
 * 
 * //检查商家月份是否已关账 状态 029-未关账 030-已关账 Map<String, Object> statusMap = new
 * HashMap<String, Object>(); statusMap.put("merchantId",user.getMerchantId());
 * statusMap.put("month", model.getMonth()); // 页面刷新当事业部为空时 查询是否存在029 未关账的 如果存在
 * 就刷新 /当事业部不为空时 查询是否有关账如果关账 就不刷新 if (model.getBuId()!=null) {
 * statusMap.put("status", DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
 * statusMap.put("buId", model.getBuId()); String
 * status=service.getSummaryStatus(statusMap);
 * if(!StringUtils.isEmpty(status)&&status.equals(DERP_REPORT.
 * FINANCEINVENTORYSUMMARY_STATUS_030)){ retMap.put("code", "01");
 * retMap.put("message", "事业部本月已关账"); return ResponseFactory.success(retMap); }
 * }else { statusMap.put("status",
 * DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029); String
 * status=service.getSummaryStatus(statusMap); if (StringUtils.isEmpty(status)
 * || DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029.equals(status)) { } else {
 * retMap.put("code", "01"); retMap.put("message", "所有事业部本月已关账"); return
 * ResponseFactory.success(retMap); } }
 * 
 * 
 * //检查本商家是否存在未完成的刷新任务 FileTaskMongo taskModel = new FileTaskMongo();
 * taskModel.setTaskType("SXSYBCW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表
 * SXCW-刷新财务报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表 SXSYBCW-刷新事业部财务经销存
 * SXZHD-刷新自核对 SXYW 刷新业务,SXSYBYW 刷新事业部业务',
 * taskModel.setMerchantId(user.getMerchantId()); taskModel.setState("2");//任务状态
 * 1-待执行 2-执行中 3-已完成 4-失败 taskModel.setOwnMonth(model.getMonth()); //
 * 查询是否存在执行中的任务 List<FileTaskMongo> taskList =
 * fileTaskService.getListByParam(taskModel); if (taskList != null &&
 * taskList.size() > 0) { retMap.put("code", "01"); retMap.put("message",
 * "存在执行中的任务,稍后再刷新"); return ResponseFactory.success(retMap); }
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setTaskName("刷新事业部财务报表"); taskModel.setUsername(user.getName());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
 * 
 * //页面触发同步如果没指定日期则默认当天 if(StringUtils.isEmpty(selectTime)){ selectTime =
 * TimeUtils.formatDay(); } //发送mq消息 Map<String, Object> body = new
 * HashMap<String, Object>(); body.put("merchantId", user.getMerchantId());
 * body.put("depotId", model.getDepotId() == null ? "" :
 * String.valueOf(model.getDepotId())); body.put("month", model.getMonth());
 * //body.put("selectTime", selectTime);//同步数据日期 //body.put("syn",syn);//是否同步数据
 * true-是 ，其他-否 if (model.getBuId()!=null) {
 * body.put("buId",model.getBuId().toString()); }
 * 
 * JSONObject json = JSONObject.fromObject(body);
 * System.out.println(json.toString()); SendResult result
 * =rocketMQProducer.send(json.toString(),
 * MQReportEnum.BU_FINANCE_SUMMARY.getTopic(),
 * MQReportEnum.BU_FINANCE_SUMMARY.getTags()); System.out.println(result);
 * if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
 * retMap.put("code", "01"); retMap.put("message", "刷新消息发送失败"); return
 * ResponseFactory.success(retMap); } retMap.put("code", "00");
 * retMap.put("message", "成功"); }catch(Exception e){ e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(retMap); }
 * 
 *//**
	 * 创建报表生成任务
	 */
/*
 * @RequestMapping("/createTask.asyn")
 * 
 * @ResponseBody private ViewResponseBean createTask(String ownMonth,Long
 * buId,String buName){ Map<String, Object> retMap = new HashMap<String,
 * Object>(); retMap.put("code", "00"); retMap.put("message", "成功"); try{
 * if(StringUtils.isEmpty(ownMonth)){ retMap.put("code", "01");
 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
 * User user = ShiroUtils.getUser(); String buNameStr = ""; if
 * (!StringUtils.isEmpty(buId)) { buNameStr = "(" + buName + ")"; } //
 * FileTaskMongo model = new FileTaskMongo();
 * model.setMerchantId(user.getMerchantId()); model.setOwnMonth(ownMonth);
 * model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW); List<FileTaskMongo>
 * listByParam = fileTaskService.getListByParam(model);
 * 
 * if (listByParam.size() > 0) { retMap.put("code", "01"); retMap.put("message",
 * "正在刷新  请稍后再导出报表 "); return ResponseFactory.success(retMap); }
 * 
 * FileTaskMongo taskModel = new FileTaskMongo();
 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWJXC);//任务类型
 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表
 * taskModel.setTaskName("事业部财务进销存汇总报表" + buNameStr);
 * taskModel.setMerchantId(user.getMerchantId());
 * taskModel.setOwnMonth(ownMonth); taskModel.setState("1");//任务状态 1-待执行 2-执行中
 * 3-已完成 4-失败 taskModel.setRemark("事业部财务进销存报表" + ownMonth);
 * taskModel.setUsername(user.getName());
 * 
 * JSONObject paramJson = new JSONObject(); paramJson.put("merchant_id",
 * user.getMerchantId()); paramJson.put("own_month", ownMonth); if (buId !=
 * null) { paramJson.put("buId", buId.toString()); }
 * 
 * System.out.println(paramJson.toString());
 * taskModel.setParam(paramJson.toString());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
 * 
 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
 * "网络故障"); e.printStackTrace(); } return ResponseFactory.success(retMap); }
 * 
 *//**
	 * 创建报表sd生成任务
	 */
/*
 * @RequestMapping("/createSdTask.asyn")
 * 
 * @ResponseBody private ViewResponseBean createSdTask(String ownMonth,Long
 * buId,String buName){ Map<String, Object> retMap = new HashMap<String,
 * Object>(); retMap.put("code", "00"); retMap.put("message", "成功"); try{ if
 * (StringUtils.isEmpty(ownMonth)) { retMap.put("code", "01");
 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
 * User user = ShiroUtils.getUser(); String buNameStr = ""; if
 * (!StringUtils.isEmpty(buId)) { buNameStr = "(" + buName + ")"; }
 * FileTaskMongo model = new FileTaskMongo();
 * model.setMerchantId(user.getMerchantId()); model.setOwnMonth(ownMonth);
 * model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW); List<FileTaskMongo>
 * listByParam = fileTaskService.getListByParam(model);
 * 
 * if (listByParam.size() > 0) { retMap.put("code", "01"); retMap.put("message",
 * "正在刷新 请稍后再导出报表 "); return ResponseFactory.success(retMap); } FileTaskMongo
 * taskModel = new FileTaskMongo();
 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SDSYBCWJXC);//任务类型
 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表
 * taskModel.setTaskName("SD事业部财务进销存汇总报表" + buNameStr);
 * taskModel.setMerchantId(user.getMerchantId());
 * taskModel.setOwnMonth(ownMonth); taskModel.setState("1");//任务状态 1-待执行 2-执行中
 * 3-已完成 4-失败 taskModel.setRemark("SD事业部财务进销存报表" + ownMonth);
 * taskModel.setUsername(user.getName());
 * 
 * JSONObject paramJson = new JSONObject(); paramJson.put("merchant_id",
 * user.getMerchantId()); paramJson.put("own_month", ownMonth); if (buId !=
 * null) { paramJson.put("buId", buId.toString()); }
 * System.out.println(paramJson.toString());
 * taskModel.setParam(paramJson.toString());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
 * 
 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
 * "网络故障"); e.printStackTrace(); } return ResponseFactory.success(retMap); }
 * 
 * 
 *//**
	 * 创建报表总账生成任务
	 */
/*
 * @RequestMapping("/createAllAccountTask.asyn")
 * 
 * @ResponseBody private ViewResponseBean createAllAccountTask(String
 * ownMonth,Long buId,String buName){ Map<String, Object> retMap = new
 * HashMap<String, Object>(); retMap.put("code", "00"); retMap.put("message",
 * "成功"); try{ if (StringUtils.isEmpty(ownMonth)) { retMap.put("code", "01");
 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
 * User user = ShiroUtils.getUser(); String buNameStr = ""; if
 * (!StringUtils.isEmpty(buId)) { buNameStr = "(" + buName + ")"; }
 * FileTaskMongo model = new FileTaskMongo();
 * model.setMerchantId(user.getMerchantId()); model.setOwnMonth(ownMonth);
 * model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW); List<FileTaskMongo>
 * listByParam = fileTaskService.getListByParam(model);
 * 
 * if (listByParam.size() > 0) { retMap.put("code", "01"); retMap.put("message",
 * "正在刷新 请稍后再导出报表 "); return ResponseFactory.success(retMap); } FileTaskMongo
 * taskModel = new FileTaskMongo();
 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZZ);//任务类型
 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表
 * taskModel.setTaskName("事业部财务总账汇总" + buNameStr);
 * taskModel.setMerchantId(user.getMerchantId());
 * taskModel.setOwnMonth(ownMonth); taskModel.setState("1");//任务状态 1-待执行 2-执行中
 * 3-已完成 4-失败 taskModel.setRemark("事业部财务总账报表导出" + ownMonth);
 * taskModel.setUsername(user.getName());
 * 
 * JSONObject paramJson = new JSONObject(); paramJson.put("merchant_id",
 * user.getMerchantId()); paramJson.put("own_month", ownMonth); if (buId !=
 * null) paramJson.put("buId", buId); System.out.println(paramJson.toString());
 * taskModel.setParam(paramJson.toString());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
 * 
 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
 * "网络故障"); e.printStackTrace(); } return ResponseFactory.success(retMap); }
 * 
 *//**
	 * 暂估应收
	 */
/*
 * @RequestMapping("/createTempEstimatePdfTask.asyn")
 * 
 * @ResponseBody private ViewResponseBean createTempEstimatePdfTask(String
 * ownMonth,Long buId,String buName){ Map<String, Object> retMap = new
 * HashMap<String, Object>(); retMap.put("code", "00"); retMap.put("message",
 * "成功"); try{ if (StringUtils.isEmpty(ownMonth)) { retMap.put("code", "01");
 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
 * User user = ShiroUtils.getUser(); String buNameStr = ""; if
 * (!StringUtils.isEmpty(buId)) { buNameStr = "(" + buName + ")"; }
 * FileTaskMongo model = new FileTaskMongo();
 * model.setMerchantId(user.getMerchantId()); model.setOwnMonth(ownMonth);
 * model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW); List<FileTaskMongo>
 * listByParam = fileTaskService.getListByParam(model);
 * 
 * if (listByParam.size() > 0) { retMap.put("code", "01"); retMap.put("message",
 * "正在刷新  请稍后导出报表 "); return ResponseFactory.success(retMap); } FileTaskMongo
 * taskModel = new FileTaskMongo();
 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZGYS);//任务类型
 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表
 * taskModel.setTaskName("事业部财务暂估应收" + buNameStr);
 * taskModel.setMerchantId(user.getMerchantId());
 * taskModel.setOwnMonth(ownMonth); taskModel.setState("1");//任务状态 1-待执行 2-执行中
 * 3-已完成 4-失败 taskModel.setRemark("事业部财务暂估应收导出" + ownMonth);
 * taskModel.setUsername(user.getName());
 * 
 * JSONObject paramJson = new JSONObject(); paramJson.put("merchant_id",
 * user.getMerchantId()); paramJson.put("own_month", ownMonth); if (buId !=
 * null) paramJson.put("buId", buId); System.out.println(paramJson.toString());
 * taskModel.setParam(paramJson.toString());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
 * 
 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
 * "网络故障"); e.printStackTrace(); } return ResponseFactory.success(retMap); }
 * 
 * 
 *//**
	 * 创建利息经销存导出任务
	 *//*
		 * @RequestMapping("/createInterestTask.asyn")
		 * 
		 * @ResponseBody private ViewResponseBean createInterestTask(String
		 * ownMonth,Long buId,String buName){ Map<String, Object> retMap = new
		 * HashMap<String, Object>(); retMap.put("code", "00"); retMap.put("message",
		 * "成功"); try{ if(StringUtils.isEmpty(ownMonth)){ retMap.put("code", "01");
		 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
		 * User user = ShiroUtils.getUser(); String buNameStr = ""; if
		 * (!StringUtils.isEmpty(buId)) { buNameStr = "(" + buName + ")"; }
		 * 
		 * // FileTaskMongo model = new FileTaskMongo();
		 * model.setMerchantId(user.getMerchantId()); model.setOwnMonth(ownMonth);
		 * model.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXCW); List<FileTaskMongo>
		 * listByParam = fileTaskService.getListByParam(model);
		 * 
		 * if (listByParam.size() > 0) { retMap.put("code", "01"); retMap.put("message",
		 * "正在刷新 请稍后再导出报表 "); return ResponseFactory.success(retMap); }
		 * 
		 * FileTaskMongo taskModel = new FileTaskMongo();
		 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SYBCWLX);//任务类型
		 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表
		 * taskModel.setTaskName("事业部财务利息进销存汇总报表" + buNameStr);
		 * taskModel.setMerchantId(user.getMerchantId());
		 * taskModel.setOwnMonth(ownMonth); taskModel.setState("1");//任务状态 1-待执行 2-执行中
		 * 3-已完成 4-失败 taskModel.setRemark("事业部利息财务进销存报表" + ownMonth);
		 * taskModel.setUsername(user.getName());
		 * 
		 * JSONObject paramJson = new JSONObject(); paramJson.put("merchant_id",
		 * user.getMerchantId()); paramJson.put("own_month", ownMonth); if (buId !=
		 * null) { paramJson.put("buId", buId.toString()); }
		 * 
		 * System.out.println(paramJson.toString());
		 * taskModel.setParam(paramJson.toString());
		 * taskModel.setCreateDate(TimeUtils.formatFullTime());
		 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
		 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
		 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
		 * 
		 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
		 * "网络故障"); e.printStackTrace(); } return ResponseFactory.success(retMap); }
		 * 
		 * 
		 * }
		 */