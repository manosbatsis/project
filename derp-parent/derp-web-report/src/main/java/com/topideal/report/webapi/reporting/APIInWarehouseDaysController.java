package com.topideal.report.webapi.reporting;

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
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.InWareHouseDaysService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.InWarehouseDetailsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/report/inWarehouseDays")
@Api(tags = "商品在库天数统计")
public class APIInWarehouseDaysController {
	
	@Autowired
	InWareHouseDaysService  inWareHouseDaysService;
	@Autowired
	BusinessUnitDao businessUnitDao ;
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;
	
	@Autowired
	private FileTaskService fileTaskService;

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")   	
   	@PostMapping(value="/toDetail.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> toDetail(InWarehouseDetailsForm form)throws Exception  {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			InWarehouseDetailsModel inWarehouseDetailsModel = new InWarehouseDetailsModel();
			BeanUtils.copyProperties(form, inWarehouseDetailsModel);
			List<Map<String, Object>> detailList = inWareHouseDaysService.getInwarehouseDetails(inWarehouseDetailsModel,null) ;
			
			if(!detailList.isEmpty()) {
				Double warehouseAmontTotal = 0.0 ;
				Integer warehouseNum = 0 ;
				
				String month = null ;
				String buName = null ;
				String currency = null ;
				Timestamp statisticsDate = null ;
				
				for (Map<String, Object> map : detailList) {
					warehouseAmontTotal +=  ((BigDecimal)map.get("totalAmount")).setScale(2).doubleValue() ;
					warehouseNum += ((BigDecimal)map.get("totalNum")).intValue() ;
					
					if(StringUtils.isBlank(month)) {
						month = String.valueOf(map.get("month")) ;
					}
					
					if(StringUtils.isBlank(buName)) {
						buName = String.valueOf(map.get("buName")) ;
					}
					
					if(StringUtils.isBlank(currency)) {
						currency = String.valueOf(map.get("currency")) ;
					}
					
					if(statisticsDate == null) {
						statisticsDate = (Timestamp)map.get("statisticsDate") ;
					}
				}
				
				resultMap.put("totalAmount", warehouseAmontTotal) ;
				resultMap.put("totalNum", warehouseNum) ;
				resultMap.put("month", month) ;
				resultMap.put("buName", buName) ;
				resultMap.put("currency", currency) ;
				resultMap.put("statisticsDate", statisticsDate) ;
				resultMap.put("detailList", detailList) ;				
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取列表分页数据")   	
   	@PostMapping(value="/listInWarehouseDays.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<InWarehouseDetailsDTO> listInWarehouseDays(InWarehouseDetailsForm form) {		
		InWarehouseDetailsDTO dto = new InWarehouseDetailsDTO();
		try {
			BeanUtils.copyProperties(form, dto);			
			//如果月份为空，默认当前月份
			if(dto.getMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date()) ;				
				dto.setMonth(month);
			}
			User user=ShiroUtils.getUserByToken(form.getToken());
			dto = inWareHouseDaysService.listInWarehouseDays(user,dto) ;
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 刷新仓库、月汇总报表
	 * */
	@ApiOperation(value = "刷新")
   	@PostMapping(value="/flashInWarehouseDays.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> flashInWarehouseDays(InWarehouseDetailsForm form) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			
			if(form.getBuId() != null) {
				body.put("buId", form.getBuId());
			}			
			body.put("month", form.getMonth() == null ? "":String.valueOf(form.getMonth()));
			
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.IN_WAREHOUSE_DAYS.getTopic(), MQReportEnum.IN_WAREHOUSE_DAYS.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"刷新消息发送失败");
			}
			retMap.put("code", "00");
			retMap.put("message", "成功");
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	
	/**
	 * 导出在库天数明细
	 * */
	@ApiOperation(value = "导出")
   	@PostMapping(value="/exportInWarehouseDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> exportInWarehouseDetails( InWarehouseDetailsForm form ,HttpServletRequest request ,HttpServletResponse response) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "成功");
		try{
			InWarehouseDetailsModel model = new InWarehouseDetailsModel();
			BeanUtils.copyProperties(form, model);
			String month = model.getMonth() ;

            if (StringUtils.isBlank(month)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                month = sdf.format(new Date());

                model.setMonth(month);
            }
            User user = ShiroUtils.getUserByToken(form.getToken());
            BusinessUnitModel businessUnit = businessUnitDao.searchById(model.getBuId());

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_ZKTS);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
            taskModel.setTaskName("在库天数统计表-" + businessUnit.getName());
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("在库天数统计表");
            taskModel.setUsername(user.getName());
            taskModel.setOwnMonth(model.getMonth());

            JSONObject paramJson = new JSONObject();
            paramJson.put("month", model.getMonth());
            paramJson.put("buId", model.getBuId());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
        } catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "网络故障");
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
		
	}
	
}
