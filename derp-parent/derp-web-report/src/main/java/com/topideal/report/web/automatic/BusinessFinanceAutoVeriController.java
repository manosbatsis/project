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
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.automatic.BusinessFinanceAutoVeriService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.MerchantInfoService;
import com.topideal.report.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 业财自核对
 * @Author: Chen Yiluan
 * @Date: 2020/05/19 14:46
 **/
@Controller
@RequestMapping("/businessFinanceAutoVeri")
public class BusinessFinanceAutoVeriController {

    @Autowired
    private BusinessFinanceAutoVeriService service;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private FileTaskService fileTaskService;
    /**
     * 访问列表页面
     * */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        String month = TimeUtils.getLastMonth(new Date());
        model.addAttribute("month", month) ;
        return "derp/automatic/businessFinanceAutoVerification-list";
    }


    /**
     * 获取分页数据
     * */
    @RequestMapping("/listBusinessFinanceAutoVerification.asyn")
    @ResponseBody
    private ViewResponseBean listBusinessFinanceAutoVerification(BusinessFinanceAutomaticVerificationDTO dto) {
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
     */
    @RequestMapping("/exportAutoVerification.asyn")
    public void exportAutoVerification(BusinessFinanceAutomaticVerificationDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());

        //导出信息
        List<BusinessFinanceAutomaticVerificationItemModel> exportList = service.listForExport(dto);

        String sheetName = "未对平数据";
        String[] columns = {"公司", "事业部", "报表月份", "标准条码", "商品名称", "事业部业务进销存", "事业部财务进销存"
                ,"事业部财务期末结转量", "累计销售在途量", "累计调拨在途量", "最近刷新时间"};
        String[] keys = {"merchantName", "buName" ,"month","commbarcode","goodsName","buInventorySummaryEndNum",
                "buFinanceSummaryEndNum","buFinanceSummaryNum",
                "addSaleNoshelfNum", "addTransferNoshelfNum", "createDate"};

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, exportList) ;
        //导出文件
        String fileName = "业财自核对";
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }


    /**
     * 刷新
     */
    @RequestMapping("/refreshAutoVerification.asyn")
    @ResponseBody
    public ViewResponseBean refreshAutoVerification(BusinessFinanceAutomaticVerificationDTO dto, String refresh) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try{

            User user= ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("month",dto.getMonth());
            if (dto.getBuId()!=null) {
            	body.put("buId",dto.getBuId().toString());
			}
            if ("true".equals(refresh)) {
                //查询执行中的任务 存在则结束本次运行
                FileTaskMongo paramModel = new FileTaskMongo();
                paramModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                paramModel.setMerchantId(user.getMerchantId());
                paramModel.setOwnMonth(dto.getMonth());
                paramModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
                List<FileTaskMongo> taskList2 = fileTaskService.getListByParamTask(paramModel);
                if(taskList2!=null&&taskList2.size()>0){
                    retMap.put("code", "01");
                    retMap.put("message", "存在执行中的任务，请稍后刷新");
                    return ResponseFactory.success(retMap);
                }
                FileTaskMongo fileTaskModel = new FileTaskMongo();
                fileTaskModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                fileTaskModel.setMerchantId(user.getMerchantId());
                fileTaskModel.setOwnMonth(dto.getMonth());
                fileTaskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);
                List<FileTaskMongo> taskList1 = fileTaskService.getListByParamTask(fileTaskModel);
                if(taskList1!=null&&taskList1.size()>0){
                    retMap.put("code", "01");
                    retMap.put("message", "存在待执行的任务，请稍后刷新");
                    return ResponseFactory.success(retMap);
                }
                //页面触发同步默认当天
                String selectTime = TimeUtils.formatDay();
                //查询当前公司下对应的仓库的关账日期
                List<DepotInfoModel> depotList = merchantInfoService.depotListByMerchant(user.getMerchantId());
                StringBuffer depots = new StringBuffer();
                String depotIds = null;
                for (DepotInfoModel depot : depotList) {
                    depots.append(depot.getId().toString()).append(",");
                }
                if (!StringUtils.isEmpty(depots.toString())) {
                    depotIds = depots.substring(0, depots.toString().length()-1);
                }

                body.put("refresh",refresh);//是否刷新报表 true-是 ，其他-否
                body.put("depotIds", depotIds);
                body.put("selectTime",selectTime);
            }

            //更新数据把数据清空
            dto.setMerchantId(user.getMerchantId());
            service.updateAutomaticVeri(dto);

            JSONObject json = JSONObject.fromObject(body);
            System.out.println(json.toString());
            SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.INVENTORY_FINANCE_AUTO_VERI.getTopic(), MQReportEnum.INVENTORY_FINANCE_AUTO_VERI.getTags());
            System.out.println(result);
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
                retMap.put("code", "01");
                retMap.put("message", "刷新消息发送失败");
                return ResponseFactory.success(retMap);
            }
            retMap.put("code", "00");
            retMap.put("message", "成功");
            if("true".equals(refresh)) {
                FileTaskMongo taskModel = new FileTaskMongo();
                taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
                taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_SXZHD);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
                taskModel.setTaskName("业财自核对");
                taskModel.setMerchantId(user.getMerchantId());
                taskModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
                taskModel.setRemark("业财自核对");
                taskModel.setUsername(user.getName());
                taskModel.setOwnMonth(dto.getMonth());
                taskModel.setCreateDate(TimeUtils.formatFullTime());
                taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
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
