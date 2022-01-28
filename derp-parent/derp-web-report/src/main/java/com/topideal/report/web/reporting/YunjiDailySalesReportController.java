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
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.MerchandiseCatService;
import com.topideal.report.service.reporting.YunjiDailySalesReportService;
import com.topideal.report.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *新云集采销日报明细
 *@author 杨创
 */
@RequestMapping("/yunjiDailySalesReport")
@Controller
public class YunjiDailySalesReportController {
	
	// 销售出库service
	@Autowired
	private BrandService brandService;
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	@Autowired
	private YunjiDailySalesReportService yunjiDailySalesReportService;// 云集采销日报
	@Autowired
    private RMQProducer rocketMQProducer;
	
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) {
		try {
			//所有品类
			List<SelectBean> listMerchandiseCat = merchandiseCatService.getSelectBean();
			model.addAttribute("listMerchandiseCat",listMerchandiseCat);
			//所有品牌
			List<BrandModel> listBrand = brandService.getListBrandInfo();
			model.addAttribute("listBrand",listBrand);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "derp/reporting/yunjiDailySalesReport-list";
	}

	/**
	 * 获取分页数据
	 * @param searchDate 搜索日期
	 * */
	@RequestMapping("/listYunjiDailySalesReport.asyn")
	@ResponseBody
	private ViewResponseBean listYunJiDailySalesReport(YunjiDailySalesReportModel model ,String sDate) {
		try {
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			// 日期搜索
			if (!StringUtils.isBlank(sDate)) {
				Timestamp snapshotDate = Timestamp.valueOf(sDate + " 00:00:00");
				model.setSnapshotDate(snapshotDate);
			}
			// 响应结果集
			model = yunjiDailySalesReportService.listDailySalesReportByPage(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 云集采销日报导出
	 * @param sDate
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/yunjiDailySalesReportExport.asyn")
	private void exportDailySalesReport(String sDate, HttpServletResponse response,HttpServletRequest request, YunjiDailySalesReportModel model) throws Exception {
		try {
			User user=ShiroUtils.getUser();
			model.setMerchantId((user.getMerchantId()));
			if (!StringUtils.isBlank(sDate)) {
				Timestamp snapshotDate = Timestamp.valueOf(sDate + " 00:00:00");
				model.setSnapshotDate(snapshotDate);
			}
			// 响应结果集
			List<YunjiDailySalesReportModel> result = yunjiDailySalesReportService.listDailySalesReportExport(model);
			String sheetName = "云集采销日报";
			String[] columns = { "商品货号", "条形码", "商品名称", "商品品牌", "二级分类", "当日销量", "当月销量", "年度销量","保税仓当日库存","退货仓当日库存", "快照时间" };
			String[] keys = {"goodsNo", "barcode", "goodsName", "brandName", "productTypeName", "daySaleNum", "monthSaleNum", "yearSaleNum", "dayInventoryNum", "dayReturnBinNum", "snapshotDate"} ;
			
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获取导出销售出库明细的数量
	 */
	@RequestMapping("/getDailySalesReportCount.asyn")
	@ResponseBody
	private ViewResponseBean getDailySalesReportCount(String sDate, HttpServletResponse response,HttpServletRequest request,YunjiDailySalesReportModel model) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			if (!StringUtils.isBlank(sDate)) {
				Timestamp snapshotDate = Timestamp.valueOf(sDate + " 00:00:00");
				model.setSnapshotDate(snapshotDate);
			}
			// 获取改条件下数量
			int num = yunjiDailySalesReportService.getDailySalesReportCount(model);
			data.put("total", num);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(data);
	}
	

	
	
	/**
	 * 临时mq
	 * 生成前一天数据
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping("/freshDailySalesByDay.asyn")
	@ResponseBody
	private ViewResponseBean freshDailySalesByDay(String sDate,HttpServletRequest request,HttpServletResponse response) throws IOException {
		String log = "";
		User user=ShiroUtils.getUser();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			String json = "";
			if (!StringUtils.isBlank(sDate)) {
				json = sDate;
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("snapshotDate", json);
			map.put("merchantId", user.getMerchantId().toString());
			JSONObject jsonDate = JSONObject.fromObject(map);
			
			SendResult result = rocketMQProducer.send(jsonDate.toString(), MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTopic(),MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTags());
			System.out.println(result);
			log = StateCodeEnum.SUCCESS.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			log = StateCodeEnum.ERROR_301.getMessage();
		}
		resultMap.put("log",log );
		return ResponseFactory.success(resultMap);
	}
	
	
	
}
