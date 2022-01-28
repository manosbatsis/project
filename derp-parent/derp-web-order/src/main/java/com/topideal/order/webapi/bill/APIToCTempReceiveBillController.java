package com.topideal.order.webapi.bill;


import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.ToCTempReceiveBillService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.web.bill.ToCTempReceiveBillController;
import com.topideal.order.webapi.bill.form.TocTemporaryReceiveBillForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webapi/order/tocTempReceiveBill")
@Api(tags = "To C暂估应收")
public class APIToCTempReceiveBillController {
    private static final Logger LOG = Logger.getLogger(ToCTempReceiveBillController.class);

    @Autowired
    private ToCTempReceiveBillService toCTempReceiveBillService;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @ApiOperation(value = "获取暂估收入分页数据")
    @PostMapping(value = "/listToCTempReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocTemporaryReceiveBillDTO> listToCTempReceiveBill(TocTemporaryReceiveBillForm form) {
        TocTemporaryReceiveBillDTO dto = new TocTemporaryReceiveBillDTO();
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto=toCTempReceiveBillService.listTocTempReceiveBillByPage(user,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "获取暂估费用分页数据")
    @PostMapping(value = "/listToCTempCostReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocTemporaryCostBillDTO> listToCTempCostReceiveBill(TocTemporaryReceiveBillForm form) {
        try {
            TocTemporaryCostBillDTO dto = new TocTemporaryCostBillDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form,dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto=toCTempReceiveBillService.listTocTempCostReceiveBillByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "获取暂估收入详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "暂估id", required = true)})
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getDetail(Long billId, String token){
        try {
            TocTemporaryReceiveBillDTO dto = toCTempReceiveBillService.getDetails(billId);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "获取暂估费用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "暂估id", required = true)})
    @PostMapping(value = "/getCostDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getCostDetail(Long billId, String token){
        try {
            TocTemporaryCostBillDTO dto = toCTempReceiveBillService.getCostDetails(billId);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "根据账单id获取暂估应收明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "id", required = true),
            @ApiImplicitParam(name = "begin", value = "begin", required = true),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", required = true)})
    @PostMapping(value = "/listToCTempReceiveItem.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocTemporaryReceiveBillItemDTO> listToCTempReceiveItem(String token,Long billId,Integer begin,Integer pageSize) {
        TocTemporaryReceiveBillItemDTO dto=new TocTemporaryReceiveBillItemDTO();
        try{
            User user=ShiroUtils.getUserByToken(token);
            dto.setPageSize(pageSize);
            dto.setBegin(begin);
            dto.setBillId(billId);
            // 响应结果集
            dto = toCTempReceiveBillService.listToCTempReceiveItem(user,dto);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "根据账单id获取暂估费用明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "id", required = true),
            @ApiImplicitParam(name = "begin", value = "begin", required = true),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", required = true)})
    @PostMapping(value = "/listToCTempReceiveCostItem.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocTemporaryReceiveBillCostItemDTO> listToCTempReceiveCostItem(String token,Long billId,Integer begin,Integer pageSize) {
        TocTemporaryReceiveBillCostItemDTO dto = new TocTemporaryReceiveBillCostItemDTO();
        try{
            User user=ShiroUtils.getUserByToken(token);
            dto.setBillId(billId);
            dto.setPageSize(pageSize);
            dto.setBegin(begin);
            // 响应结果集
            dto = toCTempReceiveBillService.listToCTempReceiveCostItem(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }

    }

    @ApiOperation(value = "暂估收入导出")
    @PostMapping("/exportBill.asyn")
    private ResponseBean exportBill(TocTemporaryReceiveBillForm form) throws Exception {

        User user = ShiroUtils.getUserByToken(form.getToken());

        Map<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(form.getMonthStart()) && StringUtils.isBlank(form.getMonthEnd())) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择导出月份");
        }

        int i = TimeUtils.monthsBetween(form.getMonthStart(), form.getMonthEnd(), "yyyy-MM");
        if(i > 12) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "最多只能导出12个月份的数据");
        }

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前用户该月份下没有关联的事业部");
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            TocTemporaryReceiveBillDTO dto = new TocTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setBuList(buList);

            List<TocTemporaryReceiveBillDTO> billDTOS = toCTempReceiveBillService.listForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            List<Long> billIds = billDTOS.stream().map(e -> e.getId()).collect(Collectors.toList());

            String fileNameMonth = form.getMonthStart() + "_" + form.getMonthEnd();
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEM);
            taskModel.setTaskName("ToC暂估收入明细");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(fileNameMonth);
            taskModel.setState("1");
            taskModel.setRemark(fileNameMonth + "ToC暂估收入明细导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", fileNameMonth);
            paramJson.put("billIds", StringUtils.join(billIds.toArray(), ","));
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

    @ApiOperation(value = "暂估费用导出")
    @PostMapping("/exportCostBill.asyn")
    private ResponseBean exportCostBill(TocTemporaryReceiveBillForm form) throws Exception {

        Map<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(form.getMonthStart()) && StringUtils.isBlank(form.getMonthEnd())) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择导出月份");
        }

        int i = TimeUtils.monthsBetween(form.getMonthStart(), form.getMonthEnd(), "yyyy-MM");
        if(i > 12) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "最多只能导出12个月份的数据");
        }

        User user = ShiroUtils.getUserByToken(form.getToken());

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前用户该月份下没有关联的事业部");
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            TocTemporaryCostBillDTO dto = new TocTemporaryCostBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setBuList(buList);

            List<TocTemporaryCostBillDTO> billDTOS = toCTempReceiveBillService.listCostForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            List<Long> billIds = billDTOS.stream().map(e -> e.getId()).collect(Collectors.toList());

            String fileNameMonth = form.getMonthStart() + "_" + form.getMonthEnd();
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOST);
            taskModel.setTaskName("ToC暂估费用明细");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(fileNameMonth);
            taskModel.setState("1");
            taskModel.setRemark(fileNameMonth + "ToC暂估费用明细导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", fileNameMonth);
            paramJson.put("billIds", StringUtils.join(billIds.toArray(), ","));
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }


    @ApiOperation(value = "统计导出数量")
    @PostMapping("/getBillCount.asyn")
    private ResponseBean getBillCount(TocTemporaryReceiveBillForm form) {
        TocTemporaryReceiveBillItemDTO dto=new  TocTemporaryReceiveBillItemDTO();
        Map<String,Object> data=new HashMap<String,Object>();
        try{
            User user=ShiroUtils.getUserByToken(form.getToken());
            if ("0".equals(form.getType())) {
                TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
                BeanUtils.copyProperties(dto, itemDTO);
                itemDTO.setBillIds(form.getIds());
                Integer itemNum = toCTempReceiveBillService.countTempBillNum(user,itemDTO);
                data.put("total",itemNum);
            }

            if ("1".equals(form.getType())) {
                TocTemporaryReceiveBillCostItemDTO costItemDTO = new TocTemporaryReceiveBillCostItemDTO();
                BeanUtils.copyProperties(costItemDTO, costItemDTO);
                Integer costNum = toCTempReceiveBillService.countTempCostBillNum(user,costItemDTO);
                data.put("total",costNum);
            }

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);
    }

    @ApiOperation(value = "累计暂估应收导出")
    @PostMapping("/exportTempBill.asyn")
    private ResponseBean exportTempBill(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{

        User user = ShiroUtils.getUserByToken(form.getToken());

        Map<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(form.getMonthStart()) && StringUtils.isBlank(form.getMonthEnd())) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择导出月份");
        }

        int i = TimeUtils.monthsBetween(form.getMonthStart(), form.getMonthEnd(), "yyyy-MM");
        if(i > 12) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "最多只能导出12个月份的数据");
        }

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前用户该月份下没有关联的事业部");
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            TocTemporaryReceiveBillDTO dto = new TocTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setBuList(buList);

            List<TocTemporaryReceiveBillDTO> billDTOS = toCTempReceiveBillService.listForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            List<Long> billIds = billDTOS.stream().map(e -> e.getId()).collect(Collectors.toList());

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEMTOTAL);
            taskModel.setTaskName("ToC累计暂估应收");
            taskModel.setMerchantId(user.getMerchantId());
            String month = form.getMonthStart() + "_" + form.getMonthEnd();
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark(month + "ToC累计暂估收入明细导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", month);
            paramJson.put("billIds", StringUtils.join(billIds.toArray(), ","));
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }


    @ApiOperation(value = "累计暂估费用导出")
    @PostMapping("/exportTempCostBill.asyn")
    private ResponseBean exportTempCostBill(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{

        User user = ShiroUtils.getUserByToken(form.getToken());

        Map<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(form.getMonthStart()) && StringUtils.isBlank(form.getMonthEnd())) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择导出月份");
        }

        int i = TimeUtils.monthsBetween(form.getMonthStart(), form.getMonthEnd(), "yyyy-MM");
        if(i > 12) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "最多只能导出12个月份的数据");
        }

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前用户该月份下没有关联的事业部");
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            TocTemporaryCostBillDTO dto = new TocTemporaryCostBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setBuList(buList);

            List<TocTemporaryCostBillDTO> billDTOS = toCTempReceiveBillService.listCostForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            List<Long> billIds = billDTOS.stream().map(e -> e.getId()).collect(Collectors.toList());

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOSTTOTAL);
            taskModel.setTaskName("ToC暂估费用累计暂估");
            taskModel.setMerchantId(user.getMerchantId());
            String month = form.getMonthStart() + "_" + form.getMonthEnd();
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark(month + "ToC暂估费用累计暂估导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", month);
            paramJson.put("billIds", StringUtils.join(billIds.toArray(), ","));
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }



    @ApiOperation(value = "刷新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "暂估id，多个以“，“隔开", required = true),
            @ApiImplicitParam(name = "type", value = "暂估类型 0-收入 1-费用", required = true)})
    @PostMapping("/refreshBill.asyn")
    private ResponseBean refreshBill(String ids,String token, String type) {
        Map<String,Object> data = new HashMap<String,Object>();
        try{
            // 响应结果集
            boolean isNull = new EmptyCheckUtils().addObject(ids).addObject(type).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            data = toCTempReceiveBillService.refreshBills(ids, type);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);
    }


    @ApiOperation(value = "暂估收入完结核销列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "暂估收入id", required = true),
            @ApiImplicitParam(name = "type", value = "暂估类型 0-收入 1-费用", required = true)})
    @PostMapping("/listEndReceiveBill.asyn")
    private ResponseBean listEndReceiveBill(String token, Long id, String type) {
        try{
            // 响应结果集
            boolean isNull = new EmptyCheckUtils().addObject(id).addObject(type).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, Integer> result = toCTempReceiveBillService.endReceiveBillNum(id, type);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }

    }

    @ApiOperation(value = "暂估完结核销")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "暂估收入id", required = true),
            @ApiImplicitParam(name = "type", value = "暂估类型 0-收入 1-费用", required = true),
            @ApiImplicitParam(name = "isEndPunch", value = "勾选红冲类型 0-待核销为0 1-待核销不为0 2-两者都勾选", required = true)})
    @PostMapping("/endReceiveBill.asyn")
    private ResponseBean endReceiveBill(String token, Long id, String isEndPunch, String type) {
        try{
            // 响应结果集
            boolean isNull = new EmptyCheckUtils().addObject(id).addObject(isEndPunch).addObject(type).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            toCTempReceiveBillService.endReceiveBill(id, isEndPunch, type);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }

    }

//    @ApiOperation(value="暂估收入汇总导出")
//    @GetMapping("/exportSummaryOrder.asyn")
//    private void exportSummaryOrder(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{
//        try {
//            TocTemporaryReceiveBillDTO dto=new TocTemporaryReceiveBillDTO();
//            BeanUtils.copyProperties(form,dto);
//            User user = ShiroUtils.getUserByToken(form.getToken());
//
//            dto.setMerchantId(user.getMerchantId());
//            List<ExportExcelSheet> sheets = toCTempReceiveBillService.exportSummaryBill(dto, user);
//            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
//            //导出文件
//            String fileName = "ToC暂估收入汇总导出";
//            FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOG.error(e.getMessage());
//        }
//    }

    @ApiOperation(value="暂估收入汇总导出")
    @PostMapping("/exportSummaryOrder.asyn")
    private ResponseBean exportSummaryOrder(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>(4);
        retMap.put("code", "00");
        retMap.put("message", "成功");
        try {
            TocTemporaryReceiveBillDTO dto=new TocTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());

            if(StringUtils.isBlank(dto.getIds()) && StringUtils.isBlank(dto.getStorePlatformCode())
                    && (StringUtils.isBlank(dto.getMonthStart()) || StringUtils.isBlank(dto.getMonthEnd()))
            ) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请勾选记录或选择平台及账单月份");
            }

            List<TocTemporaryReceiveBillDTO> billDTOS = toCTempReceiveBillService.listForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEMSUMMARY);
            taskModel.setTaskName("ToC暂估收入汇总导出");
            taskModel.setMerchantId(user.getMerchantId());
            String month = TimeUtils.formatMonth(new Date());
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark("ToC暂估收入汇总导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("ids", dto.getIds());
            paramJson.put("customerId", dto.getCustomerId());
            paramJson.put("buId", dto.getBuId());
            paramJson.put("shopTypeCode", dto.getShopTypeCode());
            paramJson.put("settlementStatus", dto.getSettlementStatus());
            paramJson.put("shopCode", dto.getShopCode());
            paramJson.put("monthStart", dto.getMonthStart());
            paramJson.put("monthEnd", dto.getMonthEnd());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

//    @ApiOperation(value="暂估费用汇总导出")
//    @GetMapping("/exportCostSummaryOrder.asyn")
//    private void exportCostSummaryOrder(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{
//        try {
//            TocTemporaryCostBillDTO dto=new TocTemporaryCostBillDTO();
//            BeanUtils.copyProperties(form,dto);
//            User user = ShiroUtils.getUserByToken(form.getToken());
//
//            dto.setMerchantId(user.getMerchantId());
//            List<ExportExcelSheet> sheets = toCTempReceiveBillService.exportCostSummaryBill(dto, user);
//            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
//            //导出文件
//            String fileName = "ToC暂估费用汇总导出";
//            FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOG.error(e.getMessage());
//        }
//    }

    @ApiOperation(value="暂估费用汇总导出")
    @PostMapping("/exportCostSummaryOrder.asyn")
    private ResponseBean exportCostSummaryOrder(TocTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>(4);
        retMap.put("code", "00");
        retMap.put("message", "成功");
        try {
            TocTemporaryCostBillDTO dto=new TocTemporaryCostBillDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());

            if(StringUtils.isBlank(dto.getIds()) && StringUtils.isBlank(dto.getStorePlatformCode())
                    && (StringUtils.isBlank(dto.getMonthStart()) || StringUtils.isBlank(dto.getMonthEnd()))
            ) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请勾选记录或选择平台及账单月份");
            }

            List<TocTemporaryCostBillDTO> billDTOS = toCTempReceiveBillService.listCostForExport(dto, user);

            if (billDTOS == null || billDTOS.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOSTSUMMARY);
            taskModel.setTaskName("ToC暂估费用汇总导出");
            taskModel.setMerchantId(user.getMerchantId());
            String month = TimeUtils.formatMonth(new Date());
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark("ToC暂估费用汇总导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("ids", dto.getIds());
            paramJson.put("customerId", dto.getCustomerId());
            paramJson.put("buId", dto.getBuId());
            paramJson.put("shopTypeCode", dto.getShopTypeCode());
            paramJson.put("settlementStatus", dto.getSettlementStatus());
            paramJson.put("shopCode", dto.getShopCode());
            paramJson.put("monthStart", dto.getMonthStart());
            paramJson.put("monthEnd", dto.getMonthEnd());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

}
