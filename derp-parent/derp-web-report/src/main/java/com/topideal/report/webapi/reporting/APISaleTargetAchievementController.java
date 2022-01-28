package com.topideal.report.webapi.reporting;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.rocketmq.client.producer.SendResult;
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
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.report.service.reporting.SaleTargetAchievementService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SaleTargetAchievementForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/webapi/report/saleTargetAchievement")
@Api(tags = "销售目标达成率")
public class APISaleTargetAchievementController {

    private static final String[] GX_COLS = {"事业部", "统计区间", "标准条码", "商品条码", "商品名称", "标准品牌", "购销量",
            "计划量", "购销完成率(%)", "电商订单量", "计划量", "电商完成率(%)", "总销售量", "总销售计划数", "总完成率(%)"} ;
    private static final String[] GX_KEY = {"buName", "month", "commbarcode", "barcodes", "goodsName", "brandParent", "toBNum",
            "toBTarget", "toBRate", "toCNum", "toCTarget", "toCRate", "num", "target", "rate"} ;

    private static final String[] DS_COLS = {"事业部", "统计区间", "标准条码", "商品条码", "商品名称", "标准品牌",
            "电商平台", "销售量", "计划量", "销售完成率(%)"} ;
    private static final String[] DS_KEY = {"buName", "month", "commbarcode", "barcodes", "goodsName", "brandParent",
            "storePlatformNameLabel", "num", "target", "rate"} ;

    private static final String[] DP_COLS = {"事业部", "统计区间", "标准条码", "商品条码", "商品名称", "标准品牌",
            "店铺名称", "销售量", "计划量", "销售完成率(%)"} ;
    private static final String[] DP_KEY = {"buName", "month", "commbarcode", "barcodes", "goodsName", "brandParent",
            "shopName", "num", "target", "rate"} ;


    @Autowired
    private SaleTargetAchievementService saleTargetAchievementService;

    @Autowired
    private RMQProducer rocketMQProducer;

    @ApiOperation(value = "获取存在的电商平台")
    @PostMapping(value="toDsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<List<String>>  toDsPage() {

        try {
            String month = TimeUtils.getLastMonth(new Date());

            List<String> platformList = saleTargetAchievementService.getExsitplatform() ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,platformList);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    @ApiOperation(value = "获取存在的电商店铺")
    @PostMapping(value="toDpPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<List<String>> toDpPage() {

        try {
            String month = TimeUtils.getLastMonth(new Date());

            List<String> shopNameList = saleTargetAchievementService.getExsitShopName() ;

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,shopNameList);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 分页查询
     * @param form
     * @return
     */
    @ApiOperation(value = "分页获取数据")
    @PostMapping(value="listSaleTargetAchievement.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<SaleTargetAchievementDTO> listSaleTargetAchievement(SaleTargetAchievementForm form) {

        try {
        	User user = ShiroUtils.getUserByToken(form.getToken());
            SaleTargetAchievementDTO dto=new SaleTargetAchievementDTO();
            BeanUtils.copyProperties(form, dto);

            if(StringUtils.isNotBlank(dto.getYear())) {
                String year = dto.getYear() ;

                dto.setStartMonth(year + "-01");
                dto.setEndMonth(year + "-12");
            }else if(StringUtils.isNotBlank(dto.getSeason())) {
                String season = dto.getSeason() ;
                String[] split = season.split("-");

                String year = split[0] ;

                String startMonth = "" ;
                String endMonth = "" ;

                if(split[1].indexOf("1") > -1) {
                    startMonth = "01" ;
                    endMonth = "03" ;
                }else if(split[1].indexOf("2") > -1) {
                    startMonth = "04" ;
                    endMonth = "06" ;
                }else if(split[1].indexOf("3") > -1) {
                    startMonth = "07" ;
                    endMonth = "09" ;
                }else if(split[1].indexOf("4") > -1) {
                    startMonth = "10" ;
                    endMonth = "12" ;
                }

                dto.setStartMonth(year + "-" + startMonth);
                dto.setEndMonth(year + "-" + endMonth);
            }

            dto = saleTargetAchievementService.getListByPage(user,dto) ;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 导出
     * @param response
     * @param request
     * @param form
     * @throws Exception
     */
    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping("/exportAmountList.asyn")
    public void exportAmountList(HttpServletResponse response, HttpServletRequest request, SaleTargetAchievementForm form) throws Exception {
        SaleTargetAchievementDTO dto=new SaleTargetAchievementDTO();
        BeanUtils.copyProperties(form, dto);
        User user = ShiroUtils.getUserByToken(form.getToken());
        if(StringUtils.isNotBlank(dto.getYear())) {
            String year = dto.getYear() ;

            dto.setStartMonth(year + "-01");
            dto.setEndMonth(year + "-12");
        }else if(StringUtils.isNotBlank(dto.getSeason())) {
            String season = dto.getSeason() ;
            String[] split = season.split("-");

            String year = split[0] ;

            String startMonth = "" ;
            String endMonth = "" ;

            if(split[1].indexOf("1") > -1) {
                startMonth = "01" ;
                endMonth = "03" ;
            }else if(split[1].indexOf("2") > -1) {
                startMonth = "04" ;
                endMonth = "06" ;
            }else if(split[1].indexOf("3") > -1) {
                startMonth = "07" ;
                endMonth = "09" ;
            }else if(split[1].indexOf("4") > -1) {
                startMonth = "10" ;
                endMonth = "12" ;
            }

            dto.setStartMonth(year + "-" + startMonth);
            dto.setEndMonth(year + "-" + endMonth);
        }

        List<SaleTargetAchievementDTO> exportList = saleTargetAchievementService.getExportList(user,dto) ;

        String fileName = null ;
        SXSSFWorkbook wb = null ;
        if(DERP_REPORT.SALE_TARGET_TYPE_1.equals(dto.getType())) {
            fileName = "销售类型达成率导出" ;
            wb = ExcelUtilXlsx.createSXSSExcelByObjList("销售类型达成率", GX_COLS, GX_KEY, exportList);
        }else if(DERP_REPORT.SALE_TARGET_TYPE_2.equals(dto.getType())) {
            fileName = "电商平台达成率导出" ;
            wb = ExcelUtilXlsx.createSXSSExcelByObjList("电商平台达成率", DS_COLS, DS_KEY, exportList);
        }else if(DERP_REPORT.SALE_TARGET_TYPE_3.equals(dto.getType())) {
            fileName = "店铺计划达成率导出" ;
            wb = ExcelUtilXlsx.createSXSSExcelByObjList("店铺计划达成率", DP_COLS, DP_KEY, exportList);
        }
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }


    /**
     * 刷新
     * @param form
     * @return
     */
    @ApiOperation(value = "刷新")
    @PostMapping(value="flashSaleTargetAchievement.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean flashSaleTargetAchievement(SaleTargetAchievementForm form) {

        Map<String, Object> retMap = new HashMap<String, Object>();
        SaleTargetAchievementDTO dto=new SaleTargetAchievementDTO();
        BeanUtils.copyProperties(form, dto);
        
        try{
        	User user = ShiroUtils.getUserByToken(form.getToken());
            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();

            if(dto.getBuId() != null) {
                body.put("buId", dto.getBuId());
            }

            StringBuffer monthSB = new StringBuffer("") ;
            if(StringUtils.isNotBlank(dto.getYear())) {
                String year = dto.getYear() ;

                for(int i = 1; i <= 12 ; i ++) {

                    String tempMonth = null ;
                    if(i < 10) {
                        tempMonth += "0" + i ;
                    }else {
                        tempMonth = String.valueOf(i) ;
                    }

                    monthSB = monthSB.append(year).append("-").append(tempMonth) ;

                    if(i != 12) {
                        monthSB = monthSB.append(",")  ;
                    }
                }

            }else if(StringUtils.isNotBlank(dto.getSeason())) {
                String season = dto.getSeason() ;
                String[] split = season.split("-");

                String year = split[0] ;

                if(split[1].indexOf("1") > -1) {
                    monthSB = monthSB.append(year + "-01,").append(year + "-02,")
                            .append(year + "-03") ;
                }else if(split[1].indexOf("2") > -1) {
                    monthSB = monthSB.append(year + "-04,").append(year + "-05,")
                            .append(year + "-06") ;
                }else if(split[1].indexOf("3") > -1) {
                    monthSB = monthSB.append(year + "-07,").append(year + "-08,")
                            .append(year + "-09") ;
                }else if(split[1].indexOf("4") > -1) {
                    monthSB = monthSB.append(year + "-10,").append(year + "-11,")
                            .append(year + "-12") ;
                }

            }else if(StringUtils.isNotBlank(dto.getMonth())) {
                monthSB = monthSB.append(dto.getMonth()) ;
            }

            body.put("month", monthSB.toString());
            body.put("syn",form.getSyn() == null ? "false" : form.getSyn());//是否同步数据 true-是 ，其他-否
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
            String selectTime = sdf.format(new Date()) ;
            body.put("selectTime", selectTime) ;

            JSONObject json = JSONObject.fromObject(body);
            System.out.println(json.toString());
            SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.SALES_TARGET_ACHIEVEMENT.getTopic(), MQReportEnum.SALES_TARGET_ACHIEVEMENT.getTags());
            System.out.println(result);
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
                retMap.put("code", "01");
                retMap.put("message", "刷新消息发送失败");
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
            }
            retMap.put("code", "00");
            retMap.put("message", "成功");

            dto = saleTargetAchievementService.getListByPage(user,dto) ;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


}
