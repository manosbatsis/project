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
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.VipPoBillVerificationService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.VipPoBillVerificationForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/report/vipPoBillVerification")
@Api(tags = "唯品PO核销报表")
public class APIVipPoBillVerificationController {

    @Autowired
    private VipPoBillVerificationService vipPoBillVerificationService ;

    // mq
    @Autowired
    private RMQProducer rocketMQProducer;

    @Autowired
    private FileTaskService fileTaskService;


    /**
     * 访问详情页面
     * @param form
     * @return
     */
    @ApiOperation(value = "访问详情页面")
    @PostMapping(value="toDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toDetail(VipPoBillVerificationForm form) {

        try {
            User user= ShiroUtils.getUserByToken(form.getToken());

            //获取详情实体
            VipPoBillVerificationModel vipPoBillVerificationModel = new VipPoBillVerificationModel() ;
            vipPoBillVerificationModel.setMerchantId(user.getMerchantId());
            vipPoBillVerificationModel.setPo(form.getPo());
            vipPoBillVerificationModel.setCommbarcode(form.getCommbarcode());
            vipPoBillVerificationModel.setDepotId(form.getDepotId());

            vipPoBillVerificationModel = vipPoBillVerificationService.searchByModel(vipPoBillVerificationModel) ;

            VipPoBillVerificationDTO dtoBillVerificationDTO = new VipPoBillVerificationDTO();
            BeanUtils.copyProperties(vipPoBillVerificationModel, dtoBillVerificationDTO);

            //商品名称
            String goodsName = vipPoBillVerificationService.getGoodsNameByCommbarcode(form.getCommbarcode(), user.getMerchantId()) ;

            //获取客户名
            String customerName = vipPoBillVerificationService.getCustomerName(vipPoBillVerificationModel) ;

            Map<String,Object> map=new HashMap<>();
            map.put("detail", dtoBillVerificationDTO);
            map.put("customerName", customerName);
            map.put("goodsName", goodsName);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 访问详情页面
     * @param form
     * @return
     */
    @ApiOperation(value = "访问详情页面")
    @PostMapping(value="getDetailsByType.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getDetailsByType(VipPoBillVerificationForm form) {

        try {
            User user= ShiroUtils.getUserByToken(form.getToken());

            //获取唯品代销仓ID
            Long depotId = vipPoBillVerificationService.getVipDepotId() ;

            VipPoBillVerificationModel vipPoBillVerificationModel = new VipPoBillVerificationModel();
            vipPoBillVerificationModel.setMerchantId(user.getMerchantId());
            vipPoBillVerificationModel.setPo(form.getPo());
            vipPoBillVerificationModel.setCommbarcode(form.getCommbarcode());
            vipPoBillVerificationModel.setDepotId(depotId);

            //获取详情实体
            VipPoBillVerificationDTO dto = new VipPoBillVerificationDTO() ;
            dto.setMerchantId(user.getMerchantId());
            dto.setPo(form.getPo());
            dto.setCommbarcode(form.getCommbarcode());
            dto.setDepotId(depotId);

            vipPoBillVerificationModel = vipPoBillVerificationService.searchByModel(vipPoBillVerificationModel) ;

            //获取上架明细
            if("shelf".equals(form.getType())) {
                List<VipShelfDetailsDTO> shelfDetails = vipPoBillVerificationService.getShelfDetails(dto) ;
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,shelfDetails);
            }


            //获取销售出库明细
            if("saleOut".equals(form.getType())) {
                List<VipOutDepotDetailsDTO> saleOutDetails = vipPoBillVerificationService.getSaleOutDetails(dto) ;
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleOutDetails);
            }

            //获取销售退货明细
            if("returnOdepot".equals(form.getType())) {
                List<VipSaleReturnOdepotDetailsDTO> saleReturnOdepotDetails = vipPoBillVerificationService.getSaleReturnOdepotDetails(dto) ;
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleReturnOdepotDetails);
            }

            //获取国检抽样明细
            if("nationalInspection".equals(form.getType())) {
                List<VipAdjustmentInventoryDetailsDTO> nationalInspectionDetails = vipPoBillVerificationService.getNationalInspectionDetails(dto);
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,nationalInspectionDetails);
            }

            //获取唯品红冲明细
            if("hc".equals(form.getType())) {
                List<VipAdjustmentInventoryDetailsDTO> hcDetails = vipPoBillVerificationService.getVipHcDetails(dto);
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,hcDetails);
            }

            //获取唯品报废明细
            if("scrap".equals(form.getType())) {
                List<VipAdjustmentInventoryDetailsDTO> scrapDetails = vipPoBillVerificationService.getVipScrapDetails(dto);
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,scrapDetails);
            }

            //获取唯品盘点明细
            if("takeStock".equals(form.getType())) {
                List<VipTakesStockResultsDetailsDTO> stockDetails = vipPoBillVerificationService.getVipTakesStockResultsDetails(dto);
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,stockDetails);
            }

            //获取唯品调拨明细
            if("transfer".equals(form.getType())) {
                List<VipTransferDepotDetailsDTO> transferDetails = vipPoBillVerificationService.getVipTransferDetails(dto);
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,transferDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }



    /**
     * 获取分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value="listVipPoBillVeriList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<VipPoBillVerificationDTO> listVipPoBillVeriList(VipPoBillVerificationForm form) throws IOException {

        User user=ShiroUtils.getUserByToken(form.getToken());

        VipPoBillVerificationDTO dto=new VipPoBillVerificationDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());

        try {
            dto = vipPoBillVerificationService.listVipPoBillVeriList(dto, user) ;
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }


    /**
     * 获取PO列表分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取PO列表分页数据")
    @PostMapping(value="listVipPoBillVeriPoList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<VipPoBillVerificationDTO> listVipPoBillVeriPoList(VipPoBillVerificationForm form) throws IOException {

        User user=ShiroUtils.getUserByToken(form.getToken());

        VipPoBillVerificationDTO dto=new VipPoBillVerificationDTO();
        dto.setMerchantId(user.getMerchantId());
        dto.setPo(form.getPo());
        dto.setStatus(form.getStatus());
        dto.setBegin(form.getBegin());
        dto.setPageSize(form.getPageSize());
        try {
            dto = vipPoBillVerificationService.listVipPoBillVeriPoList(dto) ;
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }


    /**
     * 刷新仓库、月汇总报表
     * @param form
     * @return
     */
    @ApiOperation(value = "刷新仓库、月汇总报表")
    @PostMapping(value="flashVipPoBillVeris.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean flashVipPoBillVeris(VipPoBillVerificationForm form) throws IOException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        VipPoBillVerificationModel model = new VipPoBillVerificationModel();
        BeanUtils.copyProperties(form,model);
        try{

            User user=ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            //body.put("syn",syn);//是否同步数据 true-是 ，其他-否
            body.put("fromPage", "true") ;

            if(model.getPo() != null) {
                body.put("poNo", model.getPo());
            }

            JSONObject json = JSONObject.fromObject(body);
            System.out.println(json.toString());
            SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.VIP_PO_BILL_VERI.getTopic(), MQReportEnum.VIP_PO_BILL_VERI.getTags());
            System.out.println(result);
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
                retMap.put("code", "01");
                retMap.put("message", "刷新消息发送失败");
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
            }
            retMap.put("code", "00");
            retMap.put("message", "成功");
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
    }



    /**
     * 导出
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping("/exportMain.asyn")
    public void exportMain(HttpServletResponse response, HttpServletRequest request, VipPoBillVerificationForm form) throws Exception {

        User user=ShiroUtils.getUserByToken(form.getToken());

        VipPoBillVerificationDTO model = new VipPoBillVerificationDTO();
        model.setCommbarcode(form.getCommbarcode());
        model.setPo(form.getPo());
        model.setDepotId(form.getDepotId());
        model.setDays(form.getDays());
        model.setCustomerId(form.getCustomerId());
        model.setStatus(form.getStatus());
        model.setMerchantId(user.getMerchantId());
        //设置页数为0，导出所有列表数据
        model.setPageSize(0);
        // 商品总表响应结果集
        VipPoBillVerificationDTO mainModel = vipPoBillVerificationService.listVipPoBillVeriList(model, user) ;
        List<VipPoBillVerificationModel> mainResult = mainModel.getList();

        String fileName = "唯品ByPo核销汇总表";
        String mainSheetName = "唯品by po核销总表" ;
        String[] mainKey = { "merchantName" , "buName", "po" ,"customerName", "poDate" , "commbarcode", "goodsName", "brandParent", "superiorParentBrand", "currency", "salePrice", "unsettledAccount", "inventoryAmount",
                "shelfTotalAccount","shelfTotalAmount"  , "shelfDamagedAccount", "firstShelfDate" , "billTotalAccount" , "billRecentDate" , "outDepotTotalAccont" ,
                "nationalInspectionSampleAccount" , "saleReturnAccount" , "vipHcAccount" , "inventorySurplusAccount" , "inventoryDeficientAccount" ,"scrapAccount" , "transferInAccount", "transferOutAccount", "days" , "modifyDate"} ;
        String[] mainColumns = { "商家", "事业部", "PO号","客户名称", "PO时间", "标准条码", "商品名称", "标准品牌", "母品牌", "销售币种", "销售单价", "库存量" ,"库存金额",
                "上架总量", "上架总金额","上架残损量", "首次上架时间", "账单总量", "最近账单时间" ,"销售出库总量" ,"国检抽样" ,"销售退数量" ,"唯品红冲数量" ,"盘盈数量" , "盘亏数量" ,"唯品报废数量" , "调拨入库", "调拨出库", "天数" ,"更新时间"};

        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, mainResult);
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }


    /**
     * 导出PO明细表
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出PO明细表",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping("/exportDetails.asyn")
    public ResponseBean exportDetails(HttpServletResponse response, HttpServletRequest request, VipPoBillVerificationForm form) throws Exception {

        VipPoBillVerificationDTO model = new VipPoBillVerificationDTO();
	    model.setCommbarcode(form.getCommbarcode());
	    model.setPo(form.getPo());
	    model.setDepotId(form.getDepotId());
	    model.setDays(form.getDays());
	    model.setCustomerId(form.getCustomerId());
	    model.setStatus(form.getStatus());

        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("code", "00");
        retMap.put("message", "成功");
        try{
            User user=ShiroUtils.getUserByToken(form.getToken());

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_VIPHX);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
            taskModel.setTaskName("唯品PO核销明细表");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("唯品PO核销明细表");
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("po", model.getPo());
            paramJson.put("days", model.getDays());
            paramJson.put("commbarcode", model.getCommbarcode());
            paramJson.put("status", model.getStatus());
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("merchantName", user.getMerchantName());
            paramJson.put("buId", model.getBuId());
            paramJson.put("customerId", model.getCustomerId());
            paramJson.put("userId", user.getId());
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
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
    }




    /**
     * 获取未结算量
     * @param form
     * @return
     */
    @ApiOperation(value = "获取未结算量")
    @PostMapping(value="countUnsettledAccount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean countUnsettledAccount(VipPoBillVerificationForm form) throws IOException {

        User user=ShiroUtils.getUserByToken(form.getToken());

        VipPoBillVerificationModel dto=new VipPoBillVerificationModel();
        dto.setMerchantId(user.getMerchantId());

        Integer unsettledAccount = 0 ;

        try {
            unsettledAccount = vipPoBillVerificationService.countUnsettledAccount(dto) ;
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,unsettledAccount);
    }




    /**
     * 修改是否完结状态
     * @param form
     * @return
     */
    @ApiOperation(value = "修改是否完结状态")
    @PostMapping(value="changeStatus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean changeStatus(VipPoBillVerificationForm form) throws IOException {

        User user=ShiroUtils.getUserByToken(form.getToken());

        VipPoBillVerificationModel dto=new VipPoBillVerificationModel();
        dto.setMerchantId(user.getMerchantId());
        dto.setOperator(user.getName());
        dto.setOperatorId(user.getId());
        dto.setPo(form.getPo());

        int rows = 0 ;

        try {
            rows = vipPoBillVerificationService.modifyStatus(dto) ;
            if(rows > 0) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,rows);
            }else{
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

    }



    /**
     * 获取数据截止时间
     * @return
     */
    @ApiOperation(value = "获取数据截止时间")
    @PostMapping(value="getDataTime.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getDataTime(String token) throws IOException {

        User user = ShiroUtils.getUserByToken(token);
        Map<String, Object> resultMap = null;
        try {
            resultMap = vipPoBillVerificationService.getDataTime(user.getMerchantId());

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }





}
