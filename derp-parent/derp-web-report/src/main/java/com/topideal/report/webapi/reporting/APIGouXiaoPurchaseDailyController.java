package com.topideal.report.webapi.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.dto.SalePurchasedailyDayDTO;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.CustomerService;
import com.topideal.report.service.reporting.MerchandiseCatService;
import com.topideal.report.service.reporting.SalePurchasedailyDayService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.GouXiaoPurchaseDailySearchForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采销日报明细
 */
@RestController
@RequestMapping("/webapi/report/gouXiaoPurchaseDaily")
@Api(tags = "采销日报明细")
public class APIGouXiaoPurchaseDailyController {

    @Autowired
    private SalePurchasedailyDayService salePurchasedailyDayService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private MerchandiseCatService merchandiseCatService;
    @Autowired
    private RMQProducer rocketMQProducer;

    /**
     * 访问列表页面
     */
    @ApiOperation(value = "访问列表页面")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
    @PostMapping(value = "/toPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toPage(String token) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            //获取客户信息列表
            List<SelectBean> listCustomer = customerService.listAllCustomer();
            //所有品类
            List<SelectBean> listMerchandiseCat = merchandiseCatService.getSelectBean();
            //所有品牌
            List<BrandModel> listBrand = brandService.getListBrandInfo();
            resultMap.put("listCustomer", listCustomer);
            resultMap.put("listMerchandiseCat", listMerchandiseCat);
            resultMap.put("listBrand", listBrand);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 获取分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listGouXiaoPurchaseDaily.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<BuFinanceInventorySummaryDTO> listGouXiaoPurchaseDaily(GouXiaoPurchaseDailySearchForm form) {
        try {
            SalePurchasedailyDayModel model = new SalePurchasedailyDayModel();
            BeanUtils.copyProperties(form, model);
            //获取用户信息
            User user = ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            //设置查询报表的时间
            String reportDate = model.getReportDate();
            if (StringUtils.isBlank(reportDate)) {
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
                model.setReportDate(reportDate);
            }
            long currentDate = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long searchDate = sdf.parse(reportDate + " 00:00:00").getTime();
            // 搜索时间必须小于当前时间
            if (searchDate >= currentDate) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, "搜索时间必须小于当前时间");
            }
            SalePurchasedailyDayDTO dto = salePurchasedailyDayService.listSalePurchaseDailyByPage(model);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 获取需要导出的购销采销日报的数量
     * @param form
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取需要导出的购销采销日报的数量")
    @PostMapping(value = "/getOrderCount.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getOrderCount(GouXiaoPurchaseDailySearchForm form) throws Exception {
        try {
            SalePurchasedailyDayModel model = new SalePurchasedailyDayModel();
            BeanUtils.copyProperties(form, model);

            Map<String, Object> data = new HashMap<String, Object>();
            User user = ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            //设置查询报表的时间
            String reportDate = model.getReportDate();
            if (StringUtils.isBlank(reportDate)) {
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
                model.setReportDate(reportDate);
            }
            // 响应结果集
            Long totalCount = salePurchasedailyDayService.getExportTotalCount(model);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, totalCount);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 导出购销采销日报
     * @param form
     * @param response
     * @param request
     */
    @ApiOperation(value = "导出购销采销日报", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value = "/exportGouXiaoPurchaseDaily.asyn")
    public void exportGouXiaoPurchaseDaily(GouXiaoPurchaseDailySearchForm form, HttpServletResponse response, HttpServletRequest request) {
        try {
            SalePurchasedailyDayModel model = new SalePurchasedailyDayModel();
            BeanUtils.copyProperties(form, model);
            User user = ShiroUtils.getUserByToken(form.getToken());
            model.setMerchantId(user.getMerchantId());
            //设置查询报表的时间
            String reportDate = model.getReportDate();
            if (StringUtils.isBlank(reportDate)) {
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
                model.setReportDate(reportDate);
            }
            // 响应结果集
            List<SalePurchasedailyDayDTO> result = salePurchasedailyDayService.exportSalePurchaseDailyDetails(model);
            String sheetName = "购销采销日报";
            String[] columns = {"客户", "品类", "品牌", "当日销售量", "当日销售额(RMB)", "当月销售量", "当月日均销量", "当月销售额(RMB)", "年度销售量", "年度销售额(RMB)"};
            String[] keys = {"customerName", "productTypeName", "brandName", "daySaleCount", "daySaleAmount", "monSaleCount", "monAvgCount", "monSaleAmount", "yearSaleCount", "yearSaleAmount"};
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 按日期刷新报表
     * @param token
     * @param reportDate
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "按日期刷新报表")
    @PostMapping(value = "/refreshDaily.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "reportDate", value = "日期", required = true)})
    private ResponseBean refreshDaily(String token,String reportDate, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (StringUtils.isBlank(reportDate)) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, "日期不能为空");
            }
            User user = ShiroUtils.getUserByToken(token);
            JSONObject json = new JSONObject();
            json.put("reportDate", reportDate);
            json.put("merchantId", user.getMerchantId());
            SendResult result = rocketMQProducer.send(json.toString(), MQReportEnum.GENERATE_SALE_PURCHASE.getTopic(), MQReportEnum.GENERATE_SALE_PURCHASE.getTags());
            System.out.println("MQ消息id：" + result.getMsgId());
            System.out.println(result);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 按日期时间段刷新报表(页面不开发入口，链接调用)
     * @param reportDates 格式：yyyy/MM/dd-yyyy/MM/dd
     * @return
     */
    @ApiOperation(value = "按日期时间段刷新报表(页面不开发入口，链接调用)")
    @PostMapping(value = "/refreshDailyByDateInterval.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "reportDate", value = "日期", required = true),
            @ApiImplicitParam(name = "merchantId", value = "商家id", required = true)})
    private ResponseBean refreshDailyByDateInterval(String token,String reportDates,String merchantId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (StringUtils.isBlank(reportDates)) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            String[] dates = reportDates.split("-");
            if(dates.length != 2){
                //参数异常
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            //日期排序，用于区分起始时间和结束时间
            String startDate = dates[0];
            String endDate = dates[1];
            if(startDate.compareTo(endDate)>0){
                startDate = dates[1];
                endDate = dates[0];
            }
            List<String> listDate = TimeUtils.getYearMonthDateList(startDate.replace("/", "-"), endDate.replace("/", "-"));
            for(String reportDate:listDate){
                JSONObject json = new JSONObject();
                json.put("reportDate", reportDate);
                if(StringUtils.isNotBlank(merchantId)){
                    json.put("merchantId", merchantId);
                }
                SendResult result = rocketMQProducer.send(json.toString(), MQReportEnum.GENERATE_SALE_PURCHASE.getTopic(),MQReportEnum.GENERATE_SALE_PURCHASE.getTags());
                System.out.println("MQ消息id："+result.getMsgId());
                System.out.println(result);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

}
