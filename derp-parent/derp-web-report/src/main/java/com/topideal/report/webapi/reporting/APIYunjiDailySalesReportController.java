package com.topideal.report.webapi.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.MerchandiseCatService;
import com.topideal.report.service.reporting.YunjiDailySalesReportService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.YunjiDailySalesReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *新云集采销日报明细
 *@author 杨创
 */
@RestController
@RequestMapping("/webapi/report/yunjiDailySalesReport")
@Api(tags = "云集采销日报明细")
public class APIYunjiDailySalesReportController {
	
	// 销售出库service
	@Autowired
	private BrandService brandService;
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	@Autowired
	private YunjiDailySalesReportService yunjiDailySalesReportService;// 云集采销日报
	@Autowired
    private RMQProducer rocketMQProducer;


	@ApiOperation(value = "获取商品品类下拉框")
	@PostMapping(value="/listMerchandiseCatSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
	private ResponseBean listMerchandiseCatSelectBean() {
		try{
			List<SelectBean> listMerchandiseCat = merchandiseCatService.getSelectBean();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,listMerchandiseCat);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "获取品牌下拉框")
	@PostMapping(value="/listBrandSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
	private ResponseBean listBrandSelectBean() {
		try{
			List<BrandModel> listBrand = brandService.getListBrandInfo();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,listBrand);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "获取分页数据")
	@PostMapping(value = "/listYunJiDailySalesReport.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listYunJiDailySalesReport(YunjiDailySalesReportForm form) {
		try {
			User user=ShiroUtils.getUserByToken(form.getToken());

			YunjiDailySalesReportModel model = new YunjiDailySalesReportModel();
			BeanUtils.copyProperties(form, model);
			model.setMerchantId(user.getMerchantId());
			// 日期搜索
			if (!StringUtils.isBlank(form.getsDate())) {
				Timestamp snapshotDate = Timestamp.valueOf(form.getsDate() + " 00:00:00");
				model.setSnapshotDate(snapshotDate);
			}
			// 响应结果集
			YunjiDailySalesReportModel yunjiDailySalesReportModel = yunjiDailySalesReportService.listDailySalesReportByPage(model);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,yunjiDailySalesReportModel);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "云集采销日报导出")
	@GetMapping(value = "/exportDailySalesReport.asyn")
	private void exportDailySalesReport(YunjiDailySalesReportForm form, HttpServletResponse response,HttpServletRequest request) throws Exception {
		try {
			User user=ShiroUtils.getUserByToken(form.getToken());
			YunjiDailySalesReportModel model = new YunjiDailySalesReportModel();
			BeanUtils.copyProperties(form, model);
			model.setMerchantId(user.getMerchantId());
			if (!StringUtils.isBlank(form.getsDate())) {
				Timestamp snapshotDate = Timestamp.valueOf(form.getsDate() + " 00:00:00");
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
	
	@ApiOperation(value = "获取导出销售出库明细的数量")
	@PostMapping(value="/getDailySalesReportCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getDailySalesReportCount(YunjiDailySalesReportForm form) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			User user=ShiroUtils.getUserByToken(form.getToken());
			YunjiDailySalesReportModel model = new YunjiDailySalesReportModel();
			BeanUtils.copyProperties(form, model);
			model.setMerchantId(user.getMerchantId());
			if (!StringUtils.isBlank(form.getsDate())) {
				Timestamp snapshotDate = Timestamp.valueOf(form.getsDate() + " 00:00:00");
				model.setSnapshotDate(snapshotDate);
			}
			// 获取改条件下数量
			int num = yunjiDailySalesReportService.getDailySalesReportCount(model);
			data.put("total", num);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, data);
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


	@ApiOperation(value = "刷新数据")
	@PostMapping(value="/freshDailySalesByDay.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean freshDailySalesByDay(YunjiDailySalesReportForm form) throws Exception {
		String log = "";
		User user = ShiroUtils.getUserByToken(form.getToken());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String json = "";
			if (!StringUtils.isBlank(form.getsDate())) {
				json = form.getsDate();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("snapshotDate", json);
			map.put("merchantId", user.getMerchantId().toString());
			JSONObject jsonDate = JSONObject.fromObject(map);

			SendResult result = rocketMQProducer.send(jsonDate.toString(), MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTopic(), MQReportEnum.YUNJI_DAILY_SALES_REPORT.getTags());
			System.out.println(result);
			log = StateCodeEnum.SUCCESS.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			log = StateCodeEnum.ERROR_301.getMessage();
		}
		resultMap.put("log", log);
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
	}

	
	
}
