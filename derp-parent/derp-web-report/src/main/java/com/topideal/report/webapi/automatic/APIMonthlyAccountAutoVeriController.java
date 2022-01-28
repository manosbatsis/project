package com.topideal.report.webapi.automatic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.automatic.MonthlyAccountAutoVeriService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.APIMonthlyAccountAutoVeriResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 月结自动校验
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/report/monthlyAccountAutoVeri")
@Api(tags = "月结自动校验")
public class APIMonthlyAccountAutoVeriController {

    @Autowired
    private MonthlyAccountAutoVeriService service;
    // mq
 	@Autowired
    private RMQProducer rocketMQProducer;
 	@Autowired
 	private FileTaskService fileTaskService ;

    /**
     * 访问列表页面
     * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<APIMonthlyAccountAutoVeriResponseDTO> toPage() throws Exception {
		
		try {
			APIMonthlyAccountAutoVeriResponseDTO responseDTO=new APIMonthlyAccountAutoVeriResponseDTO();
			String month = TimeUtils.getLastMonth(new Date()) ;
			responseDTO.setMonth(month);
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
			@ApiImplicitParam(name = "status", value = "校验结果: 1-已对平 0-未对平")
			
	})
	@PostMapping(value="/listMonthlyAccountAutoVeri.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listMonthlyAccontAutoVeri(String token,Integer begin,Integer pageSize ,Long depotId,String month,String status) {
        try{
            // 响应结果集
            User user= ShiroUtils.getUserByToken(token);
            MonthlyAccountAutomaticVerificationDTO dto=new MonthlyAccountAutomaticVerificationDTO();
            dto.setDepotId(depotId);
            dto.setMonth(month);
            dto.setStatus(status);
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            dto = service.listAutomaticVeriByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 根据查询条件导出数据
     * @throws Exception 
     */
	@ApiOperation(value = "导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "年月", required = true),	
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "status", value = "校验结果: 1-已对平 0-未对平")
			
	})
	@GetMapping(value="/exportAutoVerification.asyn")
    public void exportAutoVerification(String token, Long depotId,String month,String status,
    		HttpServletResponse response, HttpServletRequest request) throws Exception {
		User user= ShiroUtils.getUserByToken(token);

        MonthlyAccountAutomaticVerificationDTO dto=new MonthlyAccountAutomaticVerificationDTO();
        dto.setDepotId(depotId);
        dto.setMonth(month);
        dto.setStatus(status);
        dto.setMerchantId(user.getMerchantId());
        //导出信息
        List<MonthlyAccountAutomaticVerificationItemModel> exportList = service.listForExport(dto);

        String sheetName = "未对平数据";
        String[] columns = {"公司","仓库名称", "报表月份", "条形码", "商品名称", "月结库存量", "事业部库存量","事业部业务进销存"
                ,"库存类型", "最近刷新时间"};
        String[] keys = {"merchantName", "depotName", "month", "barcode", "goodsName", "monthlyAccountSurplusNum", "buInventorySurplusNum",
                "buInventorySummaryEndNum", "type", "createDate"};

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, exportList) ;
        //导出文件
        String fileName = "月结自核对";
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }

    /**
	 * 刷新仓库、月汇总报表
	 * */
	@ApiOperation(value = "刷新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "年月", required = true),	
			@ApiImplicitParam(name = "depotId", value = "仓库id",required = true),
			@ApiImplicitParam(name = "syn", value = "true-刷新 ，其他-统计")
			
	})
	@PostMapping(value="/flash.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean flash(String token, Long depotId,String month,String syn) {
		
		try{
			
			if (StringUtils.isEmpty(month)||depotId==null) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"仓库id或者月份不能为空");
			}		
			User user= ShiroUtils.getUserByToken(token);
			
			if("true".equals(syn)) {
                //查询执行中的任务 存在则结束本次运行
                FileTaskMongo paramModel = new FileTaskMongo();
                paramModel.setMerchantId(user.getMerchantId());
                paramModel.setOwnMonth(month);
                paramModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                List<FileTaskMongo> taksList = fileTaskService.getListByParam(paramModel);
                if (taksList != null && taksList.size() > 0) {
                    for (FileTaskMongo fileTaskModel : taksList) {
                        if (fileTaskModel.getDepotId() == null
                                || fileTaskModel.getDepotId() == depotId) {
                			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"存在执行中的任务，请稍后刷新");
                        }

                    }
                }

                paramModel = new FileTaskMongo();
                paramModel.setMerchantId(user.getMerchantId());
                paramModel.setOwnMonth(month);
                paramModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                taksList = fileTaskService.getListByParam(paramModel);
                if (taksList != null && taksList.size() > 0) {
                    for (FileTaskMongo fileTaskModel : taksList) {
                        if (fileTaskModel.getDepotId() == null
                                || fileTaskModel.getDepotId() == depotId) {
                        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"存在执行中的任务，请稍后刷新");
                        }

                    }
                }
	    		
			}
			MonthlyAccountAutomaticVerificationModel model=new MonthlyAccountAutomaticVerificationModel();
			model.setMerchantId(user.getMerchantId());
			model.setMonth(month);
			model.setDepotId(depotId);
			service.modifyNullValue(model) ;
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			body.put("syn", syn);//是否同步数据 true-是 ，其他-否
			body.put("depotId", depotId) ;
			body.put("month", month) ;
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			String selectTime = sdf.format(new Date()) ;
			body.put("selectTime", selectTime) ;
			
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTopic(), MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"刷新消息发送失败");
			}
			
			if("true".equals(syn)) {
                FileTaskMongo taskModel = new FileTaskMongo();
                taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
                taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
                taskModel.setTaskName("月结自动校验");
                taskModel.setMerchantId(user.getMerchantId());
                taskModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                taskModel.setRemark("月结自动校验");
                taskModel.setUsername(user.getName());
                taskModel.setDepotId(depotId);
                taskModel.setOwnMonth(month);
                taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);

                JSONObject paramJson = new JSONObject();
                paramJson.put("depotId", depotId);
                paramJson.put("month", month);
                paramJson.put("merchantId", user.getMerchantId());
                paramJson.put("merchantName", user.getMerchantName());
                taskModel.setParam(paramJson.toString());
                taskModel.setCreateDate(TimeUtils.formatFullTime());
                taskModel.setUserId(user.getId());
                fileTaskService.save(taskModel);
            }
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
}
