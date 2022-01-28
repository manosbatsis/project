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
 * com.topideal.entity.dto.InWarehouseDetailsDTO; import
 * com.topideal.entity.vo.reporting.InWarehouseDetailsModel; import
 * com.topideal.entity.vo.system.BusinessUnitModel; import
 * com.topideal.mongo.entity.FileTaskMongo; import
 * com.topideal.report.service.reporting.FileTaskService; import
 * com.topideal.report.service.reporting.InWareHouseDaysService; import
 * com.topideal.report.shiro.ShiroUtils; import net.sf.json.JSONObject; import
 * org.apache.commons.lang3.StringUtils; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.math.BigDecimal; import
 * java.sql.Timestamp; import java.text.SimpleDateFormat; import java.util.Date;
 * import java.util.HashMap; import java.util.List; import java.util.Map;
 * 
 * @Controller
 * 
 * @RequestMapping("inWarehouseDays") public class InWarehouseDaysController {
 * 
 * @Autowired InWareHouseDaysService inWareHouseDaysService;
 * 
 * @Autowired BusinessUnitDao businessUnitDao ; // mq
 * 
 * @Autowired private RMQProducer rocketMQProducer;
 * 
 * @Autowired private FileTaskService fileTaskService;
 * 
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model)throws
 * Exception {
 * 
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ; String month =
 * sdf.format(new Date());
 * 
 * //判断是否每月第一天 String currentMonthFirstDay =
 * TimeUtils.getCurrentMonthFirstDay(); String currentDate =
 * TimeUtils.format(new Date(), "yyyy-MM-dd");
 * 
 * if(currentMonthFirstDay.equals(currentDate)) { month =
 * TimeUtils.getLastMonth(new Date()) ; }
 * 
 * model.addAttribute("month", month) ;
 * 
 * 
 * return "derp/reporting/inWarehousedays-list"; }
 * 
 *//**
	 * 访问详情页面
	 */
/*
 * @RequestMapping("/toDetail.html") public String
 * toDetail(InWarehouseDetailsModel inWarehouseDetailsModel ,Model model)throws
 * Exception {
 * 
 * List<Map<String, Object>> detailList =
 * inWareHouseDaysService.getInwarehouseDetails(inWarehouseDetailsModel) ;
 * 
 * if(!detailList.isEmpty()) { Double warehouseAmontTotal = 0.0 ; Integer
 * warehouseNum = 0 ;
 * 
 * String month = null ; String buName = null ; String currency = null ;
 * Timestamp statisticsDate = null ;
 * 
 * for (Map<String, Object> map : detailList) { warehouseAmontTotal +=
 * ((BigDecimal)map.get("totalAmount")).setScale(2).doubleValue() ; warehouseNum
 * += ((BigDecimal)map.get("totalNum")).intValue() ;
 * 
 * if(StringUtils.isBlank(month)) { month = String.valueOf(map.get("month")) ; }
 * 
 * if(StringUtils.isBlank(buName)) { buName = String.valueOf(map.get("buName"))
 * ; }
 * 
 * if(StringUtils.isBlank(currency)) { currency =
 * String.valueOf(map.get("currency")) ; }
 * 
 * if(statisticsDate == null) { statisticsDate =
 * (Timestamp)map.get("statisticsDate") ; } }
 * 
 * model.addAttribute("totalAmount", warehouseAmontTotal) ;
 * model.addAttribute("totalNum", warehouseNum) ; model.addAttribute("month",
 * month) ; model.addAttribute("buName", buName) ;
 * model.addAttribute("currency", currency) ;
 * model.addAttribute("statisticsDate", statisticsDate) ;
 * model.addAttribute("detailList", detailList) ;
 * 
 * }
 * 
 * return "derp/reporting/inWarehousedays-details"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/listInWarehouseDays.asyn")
 * 
 * @ResponseBody public ViewResponseBean
 * listInWarehouseDays(InWarehouseDetailsDTO dto ) {
 * 
 * //如果月份为空，默认当前月份 if(dto.getMonth() == null) { SimpleDateFormat sdf = new
 * SimpleDateFormat("yyyy-MM") ; String month = sdf.format(new Date()) ;
 * 
 * dto.setMonth(month); }
 * 
 * try { dto = inWareHouseDaysService.listInWarehouseDays(dto) ; } catch
 * (Exception e) { e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_302) ; }
 * 
 * return ResponseFactory.success(dto) ; }
 * 
 *//**
	 * 刷新仓库、月汇总报表
	 */
/*
 * @RequestMapping("/flashInWarehouseDays.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * flashInWarehouseDays(InWarehouseDetailsModel model) { Map<String, Object>
 * retMap = new HashMap<String, Object>(); try{
 * 
 * //发送mq消息 Map<String, Object> body = new HashMap<String, Object>();
 * 
 * if(model.getBuId() != null) { body.put("buId", model.getBuId()); }
 * 
 * body.put("month", model.getMonth() == null ?
 * "":String.valueOf(model.getMonth()));
 * 
 * JSONObject json = JSONObject.fromObject(body);
 * System.out.println(json.toString()); SendResult result
 * =rocketMQProducer.send(json.toString(),
 * MQReportEnum.IN_WAREHOUSE_DAYS.getTopic(),
 * MQReportEnum.IN_WAREHOUSE_DAYS.getTags()); System.out.println(result);
 * if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
 * retMap.put("code", "01"); retMap.put("message", "刷新消息发送失败"); return
 * ResponseFactory.success(retMap); } retMap.put("code", "00");
 * retMap.put("message", "成功"); }catch(Exception e){ e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(retMap); }
 * 
 *//**
	 * 导出在库天数明细
	 *//*
		 * @RequestMapping("/exportInWarehouseDetails.asyn")
		 * 
		 * @ResponseBody private ViewResponseBean exportInWarehouseDetails(
		 * InWarehouseDetailsModel model ,HttpServletRequest request
		 * ,HttpServletResponse response) throws Exception {
		 * 
		 * String month = model.getMonth() ;
		 * 
		 * if (StringUtils.isBlank(month)) { SimpleDateFormat sdf = new
		 * SimpleDateFormat("yyyy-MM") ; month = sdf.format(new Date()) ;
		 * 
		 * model.setMonth(month); }
		 * 
		 * Map<String, Object> retMap = new HashMap<String, Object>();
		 * retMap.put("code", "00"); retMap.put("message", "成功"); try { User user =
		 * ShiroUtils.getUser(); BusinessUnitModel businessUnit =
		 * businessUnitDao.searchById(model.getBuId());
		 * 
		 * FileTaskMongo taskModel = new FileTaskMongo();
		 * taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_ZKTS);//任务类型
		 * YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表 taskModel.setTaskName("在库天数统计表-" +
		 * businessUnit.getName()); taskModel.setMerchantId(user.getMerchantId());
		 * taskModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成
		 * 4-失败 taskModel.setRemark("在库天数统计表"); taskModel.setUsername(user.getName());
		 * taskModel.setOwnMonth(model.getMonth());
		 * 
		 * JSONObject paramJson = new JSONObject(); paramJson.put("month",
		 * model.getMonth()); paramJson.put("buId", model.getBuId());
		 * taskModel.setParam(paramJson.toString());
		 * taskModel.setCreateDate(TimeUtils.formatFullTime());
		 * taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
		 * taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
		 * taskModel.setUserId(user.getId()); fileTaskService.save(taskModel);
		 * 
		 * 
		 * } catch (Exception e) { retMap.put("code", "01"); retMap.put("message",
		 * "网络故障"); e.printStackTrace();
		 * 
		 * }
		 * 
		 * return ResponseFactory.success(retMap);
		 * 
		 * }
		 * 
		 * }
		 */