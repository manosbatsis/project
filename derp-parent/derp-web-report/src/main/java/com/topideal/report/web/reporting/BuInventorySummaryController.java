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
 * com.topideal.dao.system.BusinessUnitDao; import
 * com.topideal.entity.dto.BuInventorySummaryDTO; import
 * com.topideal.entity.dto.BuInventorySummaryItemDTO; import
 * com.topideal.entity.vo.inventory.MonthlyAccountModel; import
 * com.topideal.entity.vo.reporting.BuInventorySummaryModel; import
 * com.topideal.entity.vo.system.BusinessUnitModel; import
 * com.topideal.mongo.entity.FileTaskMongo; import
 * com.topideal.report.service.reporting.BuInventorySummaryService; import
 * com.topideal.report.service.reporting.FileTaskService; import
 * com.topideal.report.service.reporting.MonthlyAccountService; import
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
	 * 经分销汇总报表
	 */
/*
 * @Controller
 * 
 * @RequestMapping("/buSummary") public class BuInventorySummaryController {
 * 
 * //经销存汇总报表
 * 
 * @Autowired public BuInventorySummaryService buInventorySummaryService; // mq
 * 
 * @Autowired private RMQProducer rocketMQProducer;
 * 
 * @Autowired private FileTaskService fileTaskService;
 * 
 * @Autowired private MonthlyAccountService monthlyAccountService;
 * 
 * @Autowired BusinessUnitDao businessUnitDao ;
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model)throws
 * Exception { SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
 * model.addAttribute("now", sdf.format(new Date())); User
 * user=ShiroUtils.getUser(); model.addAttribute("userType",
 * user.getUserType()); //'用户类型 1 平台用户 2 商家（超管理） 3 商家用户' return
 * "derp/reporting/buInventorSummary-list"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/buInventorSummaryList.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * inventorSummaryList(BuInventorySummaryDTO model,String brandIds,String
 * depotIds) { try{ // 响应结果集 User user=ShiroUtils.getUser();
 * model.setMerchantId(user.getMerchantId()); List<Long> parentBrandIds=new
 * ArrayList<Long>(); if(!StringUtils.isEmpty(brandIds)){ parentBrandIds=new
 * ArrayList<Long>(); String[] Ids = brandIds.split(","); for(String id:Ids){
 * if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id)); }
 * model.setParentBrandIds(parentBrandIds); } List<Long> depotListIds=new
 * ArrayList<Long>(); if (!StringUtils.isEmpty(depotIds)) { depotListIds=new
 * ArrayList<Long>(); String[] Ids = depotIds.split(","); for(String id:Ids){
 * if(!StringUtils.isEmpty(id)) depotListIds.add(Long.valueOf(id)); }
 * model.setDepotListIds(depotListIds); }
 * if(!StringUtils.isEmpty(model.getOwnMonth())){ model =
 * buInventorySummaryService.listByPage(model); } }catch(Exception e){
 * e.printStackTrace(); return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
 * } return ResponseFactory.success(model); }
 *//**
	 * 查询月结状态
	 */
/*
 * @RequestMapping("/getMonthlyAccount.asyn")
 * 
 * @ResponseBody private ViewResponseBean getMonthlyAccount(String depotIds,
 * String settlementMonth) { MonthlyAccountModel model=new
 * MonthlyAccountModel(); try{ // 多个仓库查询不查月结状态 List<Long> depotListIds=new
 * ArrayList<Long>(); if (!StringUtils.isEmpty(depotIds)) { String[] Ids =
 * depotIds.split(","); for(String id:Ids){ if(!StringUtils.isEmpty(id))
 * depotListIds.add(Long.valueOf(id)); } } // 响应结果集 User
 * user=ShiroUtils.getUser(); model.setMerchantId(user.getMerchantId());
 * if(depotListIds.size()>1||depotListIds.size()<1||StringUtils.isEmpty(
 * settlementMonth)){ }else { //查询月结表头
 * model.setMerchantId(user.getMerchantId());
 * model.setDepotId(depotListIds.get(0));
 * model.setSettlementMonth(settlementMonth); model =
 * monthlyAccountService.searchByModel(model); } }catch(Exception e){
 * e.printStackTrace(); return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
 * } return ResponseFactory.success(model); }
 * 
 *//**
	 * 详情
	 */
/*
 * @RequestMapping("/toDetailPage.html") public String toDetailPage(Model
 * model,BuInventorySummaryItemDTO entity) throws Exception{
 * 
 * List<BuInventorySummaryItemDTO> listItem =
 * buInventorySummaryService.listBuItem(entity); model.addAttribute("listItem",
 * listItem); return "derp/reporting/buInventorSummaryDetail-list"; }
 *//**
	 * 刷新仓库、月汇总报表
	 */
/*
 * @RequestMapping("/buFlashInventorSummary.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * flashInventorSummary(BuInventorySummaryModel model,String depotIds,String
 * flashForward) { Map<String, Object> retMap = new HashMap<String, Object>();
 * try{
 * 
 * User user=ShiroUtils.getUser(); model.setMerchantId(user.getMerchantId()); if
 * (StringUtils.isEmpty(depotIds) || StringUtils.isEmpty(model.getOwnMonth())) {
 * retMap.put("code", "01"); retMap.put("message", "请输入仓库和月份"); return
 * ResponseFactory.success(retMap); } //页面触发同步如果没指定日期则默认当天
 * if(StringUtils.isEmpty(selectTime)){ selectTime = TimeUtils.formatDay(); }
 * //检查本商家是否存在未完成的刷新任务 FileTaskMongo taskModel = new FileTaskMongo();
 * taskModel.setTaskType("SXSYBYW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表
 * SXCW-刷新财务报表 SYBCWJXC- 事业部财务经销存 SYBYWJXC-进销存汇总报表 SXSYBCW-刷新事业部财务经销存
 * SXZHD-刷新自核对 SXYW 刷新业务,SXSYBYW 刷新事业部业务',
 * taskModel.setMerchantId(user.getMerchantId()); taskModel.setState("2");//任务状态
 * 1-待执行 2-执行中 3-已完成 4-失败 taskModel.setOwnMonth(model.getOwnMonth());
 * List<FileTaskMongo> taskList = fileTaskService.getListByParam(taskModel); if
 * (taskList != null && taskList.size() > 0) { retMap.put("code", "01");
 * retMap.put("message", "存在执行中的任务,稍后再刷新"); return
 * ResponseFactory.success(retMap); } taskModel.setTaskName("刷新事业部业务报表");
 * taskModel.setUsername(user.getName());
 * taskModel.setCreateDate(TimeUtils.formatFullTime());
 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel); //发送mq消息
 * Map<String, Object> body = new HashMap<String, Object>();
 * body.put("merchantId", user.getMerchantId()); body.put("depotIds", depotIds);
 * body.put("month", model.getOwnMonth()); //body.put("syn",syn);
 * //body.put("selectTime",selectTime); body.put("flashForward", flashForward);
 * if (model.getBuId() != null) { body.put("buId",model.getBuId().toString()); }
 * 
 * 
 * JSONObject json = JSONObject.fromObject(body); SendResult result =
 * rocketMQProducer.send(json.toString(),
 * MQReportEnum.BU_INVENTORY_SUMMARY.getTopic(),
 * MQReportEnum.BU_INVENTORY_SUMMARY.getTags()); System.out.println(result);
 * if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
 * retMap.put("code", "01"); retMap.put("message", "刷新消息发送失败"); return
 * ResponseFactory.success(retMap); } retMap.put("code", "00");
 * retMap.put("message", "成功"); }catch(Exception e){ e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(retMap); }
 * 
 * 
 *//**
	 * 创建报表生成任务
	 *//*
		 * @RequestMapping("/createTask.asyn")
		 * 
		 * @ResponseBody private ViewResponseBean createTask(BuInventorySummaryModel
		 * model,String depotIds,String depotNames,String depotIdAlls,String
		 * depotNameAlls){ Map<String, Object> retMap = new HashMap<String, Object>();
		 * retMap.put("code", "00"); retMap.put("message", "成功"); try{
		 * if(StringUtils.isEmpty(model.getOwnMonth())){ retMap.put("code", "01");
		 * retMap.put("message", "请选择归属月份"); return ResponseFactory.success(retMap); }
		 * String depotNamesMQ=""; String depotIdsMQ=""; if
		 * (!StringUtils.isEmpty(depotIds)) {// 如果选择的ids 不为空 depotIdsMQ=depotIds;
		 * depotNamesMQ=depotNames; }else {// 如果选中的仓库id为空 depotNamesMQ=depotNameAlls;
		 * depotIdsMQ = depotIdAlls; }
		 * 
		 * User user = ShiroUtils.getUser(); String taskName = "事业部业务进销存汇总报表"; if
		 * (model.getBuId() != null) { BusinessUnitModel businessUnitModel =
		 * businessUnitDao.searchById(model.getBuId()); taskName = taskName + "-" +
		 * businessUnitModel.getName(); }
		 * 
		 * FileTaskMongo taskModel = new FileTaskMongo();
		 * taskModel.setTaskType("SYBYWJXC");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表
		 * taskModel.setTaskName(taskName);
		 * taskModel.setMerchantId(user.getMerchantId());
		 * taskModel.setOwnMonth(model.getOwnMonth()); taskModel.setState("1");//任务状态
		 * 1-待执行 2-执行中 3-已完成 4-失败 taskModel.setRemark(model.getOwnMonth() + depotNamesMQ
		 * + "事业部业务进销存报表"); taskModel.setUsername(user.getName()); JSONObject paramJson
		 * = new JSONObject(); paramJson.put("merchant_id", user.getMerchantId());
		 * paramJson.put("depot_id", depotIdsMQ); paramJson.put("own_month",
		 * model.getOwnMonth()); paramJson.put("bu_id", model.getBuId());
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
		 * }
		 */