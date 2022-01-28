package com.topideal.order.webapi.purchase;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningExportDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;
import com.topideal.order.service.purchase.ProjectQuotaWarningService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.ProjectQuotaWarningForm;
import com.topideal.order.webapi.purchase.form.ProjectQuotaWarningItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目额度预警
 **/

@RestController
@RequestMapping("/webapi/order/projectQuotaWarning")
@Api(tags = "项目额度配置预警")
public class APIProjectQuotaWarningController {

	private static final Logger LOG = Logger.getLogger(APIProjectQuotaWarningController.class) ;

    @Autowired
    private ProjectQuotaWarningService service;
	@Autowired
	private RMQProducer rocketMQProducer;

	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ProjectQuotaWarningDTO> getListByPage(ProjectQuotaWarningForm form){

		try{

			ProjectQuotaWarningDTO dto = new ProjectQuotaWarningDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			dto = service.getListByPage(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "商品明细列表分页")
	@PostMapping(value="/getItemListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ProjectQuotaWarningItemDTO> getItemListByPage(ProjectQuotaWarningItemForm form){

		try{

			ProjectQuotaWarningItemDTO dto = new ProjectQuotaWarningItemDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			dto = service.getItemListByPage(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "根据ID获取详情")
	@PostMapping(value="/getProjectQuotaWarningById.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<ProjectQuotaWarningDTO> getProjectQuotaWarningById(@RequestParam(value = "token", required = true) String token,
													  @RequestParam(value = "id", required = true)Long id){

		try{

			ProjectQuotaWarningDTO dto = service.getProjectQuotaWarningById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "刷新")
	@PostMapping(value="/flashProjectQuotaWarningById.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<ProjectQuotaWarningDTO> flashProjectQuotaWarningById(@RequestParam(value = "token", required = true) String token,
																		   @RequestParam(value = "id", required = true)Long id){

		try{

			Map<String, Object> retMap = new HashMap<String, Object>();

			ProjectQuotaWarningDTO dto = service.getProjectQuotaWarningById(id);

			Map<String, Object> body = new HashMap<String, Object>();
			body.put("buId", dto.getBuId());
			body.put("superiorBrandId", dto.getSuperiorParentBrandId()) ;

			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_PROJUCET_QUATO_WARNING.getTopic(),
					MQOrderEnum.TIMER_PROJUCET_QUATO_WARNING.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				retMap.put("code", "01");
				retMap.put("message", "刷新消息发送失败");
			}else{
				retMap.put("code", "00");
				retMap.put("message", "成功");
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@GetMapping("/exportProjectQuotaWarning.asyn")
	@ApiOperation(value = "导出", notes = "支持查询和勾选")
	public void exportProjectQuotaWarning(HttpServletResponse response, HttpServletRequest request,
										  ProjectQuotaWarningForm form) throws Exception {

		ProjectQuotaWarningDTO dto = new ProjectQuotaWarningDTO() ;
		BeanUtils.copyProperties(form, dto);

		List<ProjectQuotaWarningDTO> dtoList = service.exportProjectQuotaWarning(dto) ;

		String[] keys = {"buName", "superiorParentBrand", "projectQuota", "purchaseAmount", "salesCollectedAmount",
				"availableAmount", "currency", "createDate"} ;

		String[] cols = {"事业部", "母品牌", "项目总额度", "采购金额", "累计销售已回款金额",
				"可用额度", "币种", "数据刷新时间"} ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("项目额度预警", cols, keys, dtoList);

		FileExportUtil.export2007ExcelFile(wb, response, request, "项目额度预警");

	}

	@GetMapping("/exportProjectQuotaWarningDetail.asyn")
	@ApiOperation(value = "导出明细")
	public void exportProjectQuotaWarningDetail(HttpServletResponse response, HttpServletRequest request,
												ProjectQuotaWarningItemForm form) throws Exception {
		ProjectQuotaWarningItemDTO dto = new ProjectQuotaWarningItemDTO();
		BeanUtils.copyProperties(form, dto);

		User user = ShiroUtils.getUserByToken(form.getToken());

		dto.setType(null);

		List<ProjectQuotaWarningExportDTO> allList = service.exportProjectQuotaWarningDetail(dto);
		Map<String, List<ProjectQuotaWarningExportDTO>> groupListByType =
				allList.stream().collect(Collectors.groupingBy(ProjectQuotaWarningExportDTO::getType));

		List<ProjectQuotaWarningExportDTO> purchaseList = groupListByType.get(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_1);
		List<ProjectQuotaWarningExportDTO> exportYearList = groupListByType.get(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2);

		if(purchaseList == null) {
			purchaseList = new ArrayList<>();
		}
		if(exportYearList == null) {
			exportYearList = new ArrayList<>();
		}

		String[] purchaseKeys = {"merchantName", "code", "poNo", "customerName", "statusName",
				"orderCreateDate", "num", "currency", "amount", "rate", "rateDate", "quotaCurrency", "occupationAmount"};

		String[] purchaseCols = {"公司", "采购单号", "PO号", "供应商", "单据状态",
				"创建日期", "采购数量", "采购币种", "采购金额", "折算汇率", "汇率日期", "额度币种", "占用金额"};

		String[] saleReturnKeys = {"merchantName", "code", "typeLabel", "customerName", "orderCreateDate",
				"num", "currency", "amount", "rate", "rateDate", "quotaCurrency", "occupationAmount"};

		String[] saleReturnCols = {"公司", "应收账单号", "账单类型", "客户", "创建日期", "结算数量", "结算币种",
				"结算金额", "折算汇率", "汇率日期", "额度币种", "回款金额"};

		//生成表格
		ExportExcelSheet monthSheet = ExcelUtilXlsx.createSheet("采购订单明细", purchaseCols, purchaseKeys, purchaseList);
		ExportExcelSheet yearSheet = ExcelUtilXlsx.createSheet("销售已回款明细", saleReturnCols, saleReturnKeys, exportYearList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
		sheets.add(monthSheet);
		sheets.add(yearSheet);

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "项目额度导出明细";
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
}
