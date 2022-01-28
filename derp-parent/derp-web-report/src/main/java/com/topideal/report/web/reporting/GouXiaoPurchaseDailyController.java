package com.topideal.report.web.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.SalePurchasedailyDayDTO;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.CustomerService;
import com.topideal.report.service.reporting.MerchandiseCatService;
import com.topideal.report.service.reporting.SalePurchasedailyDayService;
import com.topideal.report.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *采销日报明细
 *@author longcheng.mao
 */
@RequestMapping("/gouXiaoPurchaseDaily")
@Controller
public class GouXiaoPurchaseDailyController {
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
	@RequestMapping("/toPage.html")
	public String toPage(Model model) {
		try {
			//获取客户信息列表
			List<SelectBean> listCustomer = customerService.listAllCustomer();
			model.addAttribute("listCustomer",listCustomer);
			//所有品类
			List<SelectBean> listMerchandiseCat = merchandiseCatService.getSelectBean();
			model.addAttribute("listMerchandiseCat",listMerchandiseCat);
			//所有品牌
			List<BrandModel> listBrand = brandService.getListBrandInfo();
			model.addAttribute("listBrand",listBrand);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "derp/reporting/gouXiaoPurchaseDaily-list";
	}

	/**
	 * 获取分页数据
	 * @param model
	 * */
	@RequestMapping("/listGouXiaoPurchaseDaily.asyn")
	@ResponseBody
	private ViewResponseBean listSalePurchaseDaily(SalePurchasedailyDayModel model ) {
		try{
			SalePurchasedailyDayDTO saleModel=new SalePurchasedailyDayDTO();

			//获取用户信息
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			//设置查询报表的时间
			String reportDate = model.getReportDate();
			if(StringUtils.isBlank(reportDate)){
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");  
		        Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.DATE,-1);
		        reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
		        model.setReportDate(reportDate);
			}
			long currentDate = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long searchDate = sdf.parse(reportDate + " 00:00:00").getTime();
			// 搜索时间必须小于当前时间
			if (searchDate >= currentDate) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302,new Exception("搜索时间必须小于当前时间"));
			}
			saleModel = salePurchasedailyDayService.listSalePurchaseDailyByPage(model);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 获取需要导出的购销采销日报的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount( HttpServletResponse response, HttpServletRequest request,SalePurchasedailyDayModel model) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			//设置查询报表的时间
			String reportDate = model.getReportDate();
			if(StringUtils.isBlank(reportDate)){
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");  
		        Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.DATE,-1);
		        reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
		        model.setReportDate(reportDate);
			}
			// 响应结果集
			Long totalCount = salePurchasedailyDayService.getExportTotalCount(model);
			data.put("total",totalCount);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	
	/**
	 * 导出购销采销日报
	 * */
	@RequestMapping("/exportGouXiaoPurchaseDaily.asyn")
	private void exportPurchaseDaily( HttpServletResponse response, HttpServletRequest request,SalePurchasedailyDayModel model) throws Exception{
		try {
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			//设置查询报表的时间
			String reportDate = model.getReportDate();
			if(StringUtils.isBlank(reportDate)){
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");  
		        Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.DATE,-1);
		        reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
		        model.setReportDate(reportDate);
			}
			// 响应结果集
			List<SalePurchasedailyDayDTO> result = salePurchasedailyDayService.exportSalePurchaseDailyDetails(model);
			String sheetName = "购销采销日报";
	        String[] columns={"客户","品类","品牌","当日销售量","当日销售额(RMB)","当月销售量","当月日均销量","当月销售额(RMB)","年度销售量","年度销售额(RMB)"};
	        String[] keys = {"customerName", "productTypeName", "brandName", "daySaleCount", "daySaleAmount", "monSaleCount", "monAvgCount", "monSaleAmount", "yearSaleCount", "yearSaleAmount"} ;
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 按日期刷新报表
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping("/refreshDaily.asyn")
	@ResponseBody
	private ViewResponseBean refreshDaily(String reportDate,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map map = new HashMap();
		try {
			if (StringUtils.isBlank(reportDate)) {
				//输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user=ShiroUtils.getUser();
			JSONObject json = new JSONObject();
			json.put("reportDate", reportDate);
			json.put("merchantId", user.getMerchantId());
			SendResult result = rocketMQProducer.send(json.toString(), MQReportEnum.GENERATE_SALE_PURCHASE.getTopic(),MQReportEnum.GENERATE_SALE_PURCHASE.getTags());
			System.out.println("MQ消息id："+result.getMsgId());
			System.out.println(result);
			map.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			 return ResponseFactory.error(StateCodeEnum.ERROR_301);
		}
		return ResponseFactory.success(map);
	}
	/**
	 * 按日期时间段刷新报表(页面不开发入口，链接调用)
	 * @param reportDates 格式：yyyy/MM/dd-yyyy/MM/dd
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping("/refreshDailyByDateInterval.asyn")
	@ResponseBody
	private ViewResponseBean refreshDailyByDateInterval(String reportDates,String merchantId,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map map = new HashMap();
		try {
			if (StringUtils.isBlank(reportDates)) {
				//输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			String[] dates = reportDates.split("-");
			if(dates.length != 2){
				//参数异常
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
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
			map.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			 return ResponseFactory.error(StateCodeEnum.ERROR_301);
		}
		return ResponseFactory.success(map);
	}
}
