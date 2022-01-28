package com.topideal.report.web.automatic;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
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
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月结自动校验
 * @author gy
 *
 */
@Controller
@RequestMapping("/monthlyAccountAutoVeri")
public class MonthlyAccountAutoVeriController {

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
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
    	
		String month = TimeUtils.getLastMonth(new Date()) ;
		
		model.addAttribute("month", month) ;
    	
        return "derp/automatic/monthlyAccountAutoVerification-list";
    }


    /**
     * 获取分页数据
     * */
    @RequestMapping("/listMonthlyAccountAutoVeri.asyn")
    @ResponseBody
    private ViewResponseBean listMonthlyAccontAutoVeri(MonthlyAccountAutomaticVerificationDTO dto) {
        try{
            // 响应结果集
            User user= ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            dto = service.listAutomaticVeriByPage(dto);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 根据查询条件导出数据
     * @throws Exception 
     */
    @RequestMapping("/exportAutoVerification.asyn")
    public void exportAutoVerification(MonthlyAccountAutomaticVerificationDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= ShiroUtils.getUser();
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
	@RequestMapping("/flash.asyn")
	@ResponseBody
	private ViewResponseBean flash(MonthlyAccountAutomaticVerificationModel model, String syn) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			
			User user = ShiroUtils.getUser() ;
			
			if("true".equals(syn)) {
                //查询执行中的任务 存在则结束本次运行
                FileTaskMongo paramModel = new FileTaskMongo();
                paramModel.setMerchantId(user.getMerchantId());
                paramModel.setOwnMonth(model.getMonth());
                paramModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                List<FileTaskMongo> taksList = fileTaskService.getListByParamTask(paramModel);
                if (taksList != null && taksList.size() > 0) {
                    for (FileTaskMongo fileTaskModel : taksList) {
                        if (fileTaskModel.getDepotId() == null
                                || fileTaskModel.getDepotId() == model.getDepotId()) {
                            retMap.put("code", "01");
                            retMap.put("message", "存在执行中的任务，请稍后刷新");
                            return ResponseFactory.success(retMap);
                        }

                    }
                }

                paramModel = new FileTaskMongo();
                paramModel.setMerchantId(user.getMerchantId());
                paramModel.setOwnMonth(model.getMonth());
                paramModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                taksList = fileTaskService.getListByParamTask(paramModel);
                if (taksList != null && taksList.size() > 0) {
                    for (FileTaskMongo fileTaskModel : taksList) {
                        if (fileTaskModel.getDepotId() == null
                                || fileTaskModel.getDepotId() == model.getDepotId()) {
                            retMap.put("code", "01");
                            retMap.put("message", "存在执行中的任务，请稍后刷新");
                            return ResponseFactory.success(retMap);
                        }

                    }
                }
	    		
			}
			
			model.setMerchantId(user.getMerchantId());
			service.modifyNullValue(model) ;
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			body.put("syn", syn);//是否同步数据 true-是 ，其他-否
			body.put("depotId", model.getDepotId()) ;
			body.put("month", model.getMonth()) ;
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			String selectTime = sdf.format(new Date()) ;
			body.put("selectTime", selectTime) ;
			
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTopic(), MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				retMap.put("code", "01");
				retMap.put("message", "刷新消息发送失败");
				return ResponseFactory.success(retMap);
			}
			retMap.put("code", "00");
			retMap.put("message", "成功");
			
			if("true".equals(syn)) {
                FileTaskMongo taskModel = new FileTaskMongo();
                taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
                taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
                taskModel.setTaskName("月结自动校验");
                taskModel.setMerchantId(user.getMerchantId());
                taskModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                taskModel.setRemark("月结自动校验");
                taskModel.setUsername(user.getName());
                taskModel.setDepotId(model.getDepotId());
                taskModel.setOwnMonth(model.getMonth());
                taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);

                JSONObject paramJson = new JSONObject();
                paramJson.put("depotId", model.getDepotId());
                paramJson.put("month", model.getMonth());
                paramJson.put("merchantId", user.getMerchantId());
                paramJson.put("merchantName", user.getMerchantName());
                taskModel.setParam(paramJson.toString());
                taskModel.setCreateDate(TimeUtils.formatFullTime());
                taskModel.setUserId(user.getId());
                fileTaskService.save(taskModel);
            }
			
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(retMap);
	}
}
