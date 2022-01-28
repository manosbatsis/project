package com.topideal.webapi.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.service.base.RateService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 汇率管理 controller
 * @author lianchenxing
 */
@RestController
@RequestMapping("/webapi/system/rate")
@Api(tags = "汇率管理列表")
public class APIRateController {
	//
	@Autowired
	private RateService rateService;

	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	/**
	 * 访问列表页面
	 * */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/rate-list";
	}*/
	/**
	 * 访问详情页面
	 */
/*	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			ExchangeRateDTO reptileConfigModel = rateService.searchDetail(id);
			resultMap.put("detail", reptileConfigModel);
			// 日期转换
			SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM");
			if (reptileConfigModel.getEffectiveDate() != null) {
				String effectiveDate = sft.format(reptileConfigModel.getEffectiveDate());
				resultMap.put("effectiveDate", effectiveDate);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}*/
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	/*@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model) throws SQLException {
		return "/derp/base/rate-add";
	}*/
	
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	/*@RequestMapping("/toImportPage.html")
	public String toImportPage(Model model) throws SQLException {
		return "/derp/base/rate-import";
	}*/
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		ExchangeRateDTO reptileConfigModel = rateService.searchDetail(id);
		model.addAttribute("detail", reptileConfigModel);
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM");
		if (reptileConfigModel.getEffectiveDate() != null) {
			String effectiveDate = sft.format(reptileConfigModel.getEffectiveDate());
			model.addAttribute("effectiveDate", effectiveDate);
		}
		return "/derp/base/rate-edit";
	}*/
	
	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "origCurrencyCode", value = "兑换币种"),
			@ApiImplicitParam(name = "tgtCurrencyCode", value = "兑"),
			@ApiImplicitParam(name = "effectiveDateStr", value = "汇率日期"),
			@ApiImplicitParam(name = "dataSource", value = "数据来源")
	})
	@PostMapping(value="/listRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ExchangeRateDTO> listRate(String origCurrencyCode,String tgtCurrencyCode,
			String effectiveDateStr,String dataSource,int begin,int pageSize) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ExchangeRateDTO dto=new ExchangeRateDTO();
			dto.setOrigCurrencyCode(origCurrencyCode);
			dto.setTgtCurrencyCode(tgtCurrencyCode);
			dto.setDataSource(dataSource);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			if (StringUtils.isNotBlank(effectiveDateStr)) {
				Date date = sdf.parse(effectiveDateStr);
				dto.setEffectiveDate(date);
			}
			
			// 响应结果集
			dto = rateService.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增汇率")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "origCurrencyCode", value = "兑换币种",required = true),
			@ApiImplicitParam(name = "tgtCurrencyCode", value = "记账币种",required = true),
			@ApiImplicitParam(name = "rate", value = "记账汇率",required = true),
			@ApiImplicitParam(name = "effectiveDateStr", value = "生效时间yyyy-MM-dd",required = true)
	})
	@PostMapping(value="/saveRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveRate(String token,String effectiveDateStr,String origCurrencyCode ,
			String tgtCurrencyCode,Double rate) {		
		try {
			User user = ShiroUtils.getUserByToken(token);
			ExchangeRateModel model=new ExchangeRateModel();
			model.setOrigCurrencyCode(origCurrencyCode);
			model.setTgtCurrencyCode(tgtCurrencyCode);
			model.setRate(rate);
			model.setStatus("1");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (effectiveDateStr != null && !"".equals(effectiveDateStr)) {
				Date date = sdf.parse(effectiveDateStr);
				model.setEffectiveDate(date);
			}
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getTgtCurrencyCode())
					.addObject(model.getOrigCurrencyCode())
					.addObject(model.getRate())
					.addObject(model.getEffectiveDate())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}			
			if (rateService.have(model , null)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10012);
			}
			
			model.setOrigCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getOrigCurrencyCode()));
			model.setTgtCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getTgtCurrencyCode()));
			model.setDataSource(DERP_SYS.EXCHANGERATE_DATASOURCE_SGCJ);
			model.setCreateName(user.getName());
			model.setCreater(user.getId());
			boolean b = rateService.saveRate(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 修改
	 * */
/*	@RequestMapping("/modifyRate.asyn")
	@ResponseBody
	public ViewResponseBean modifyReptile(ExchangeRateModel model, String effectiveDateStr, String expiryDateStr) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			User user= ShiroUtils.getUser();
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			if (effectiveDateStr != null && !"".equals(effectiveDateStr)) {
				Date date = sdf.parse(effectiveDateStr);
				model.setEffectiveDate(new Timestamp(date.getTime()));
			}
			
			if (rateService.have(model,model.getId())) {
				return ResponseFactory.error(StateCodeEnum.ERROR_311);
			}
			
//			model.setOrigCurrency(RateEnum.getId(model.getOrigCurrencyCode()));
			model.setOrigCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList,model.getOrigCurrencyCode()));
			
//			model.setTgtCurrency(RateEnum.getId(model.getTgtCurrencyCode()));
			model.setTgtCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getTgtCurrencyCode()));
			
            boolean b = rateService.modifyRate(model);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}*/
	/**
	 * 根据ID获取商品详情
	 */
	/*@RequestMapping("/getReptileDetails.asyn")
	@ResponseBody
	public ViewResponseBean getReptileDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		ExchangeRateDTO dto = new ExchangeRateDTO();
		try {
			dto = rateService.searchDetail(id);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}*/
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/delRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delRate(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
            	//输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = rateService.delRate(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 根据查询条件导出
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "origCurrencyCode", value = "兑换币种"),
		@ApiImplicitParam(name = "tgtCurrencyCode", value = "兑"),
		@ApiImplicitParam(name = "effectiveDateStr", value = "汇率日期"),
		@ApiImplicitParam(name = "dataSource", value = "数据来源")
	})
	@GetMapping(value="/exportRate.asyn")
	public void exportTransferOrder(String token ,String origCurrencyCode,String tgtCurrencyCode,
			String effectiveDateStr,String dataSource, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		try {
			//表头信息
			ExchangeRateDTO dto=new ExchangeRateDTO();
			dto.setOrigCurrencyCode(origCurrencyCode);
			dto.setTgtCurrencyCode(tgtCurrencyCode);
			dto.setDataSource(dataSource);

			
			dto.setEffectiveDateStr(effectiveDateStr);
			List<Map<String,Object>> rateList = rateService.listForExport(dto);

			//表头信息
			String sheetName = "汇率信息";
			String[] columns = {"原币","本币","即时汇率","平均汇率","汇率日期","数据来源","创建时间"};
			String[] keys = {"origCurrencyName","tgtCurrencyName","rate","avgRate","rateDate","dataSource","createTime"};

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, rateList);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, "汇率信息");
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}


	/**
	 * 指定时间抓取汇率信息
	 */
	@ApiOperation(value = "指定时间抓取汇率信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "selectDate", value = "指定日期yyyy-MM-dd"),
			@ApiImplicitParam(name = "isPeriod", value = "是否指定一段时间")
	})
	@PostMapping(value="/getCurrencyRates.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getCurrencyRates(String selectDate, String isPeriod) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject jsonData=new JSONObject();
		try {
			if (StringUtils.isBlank(selectDate)) {
				Calendar cal = Calendar.getInstance();
				Date time = cal.getTime();
				selectDate = sdf.format(time);
			}
			if ("true".equals(isPeriod)) {
				Date beginDate = sdf.parse("2020-01-01");
				Date endDate = sdf.parse(selectDate);
				Calendar calendar = Calendar.getInstance();
				while (!beginDate.equals(endDate)) {
					String catchDate = sdf.format(beginDate);
					jsonData.put("currencyDate", catchDate);
					rocketMQProducer.send(jsonData.toString(), MQErpEnum.GET_CURRENCY_RATE.getTopic(),MQErpEnum.GET_CURRENCY_RATE.getTags());
					calendar.setTime(beginDate);
					calendar.add(Calendar.DATE, 1); // 日期加1天
					beginDate = calendar.getTime();
				}
			} else {
				jsonData.put("currencyDate", selectDate);
				rocketMQProducer.send(jsonData.toString(), MQErpEnum.GET_CURRENCY_RATE.getTopic(),MQErpEnum.GET_CURRENCY_RATE.getTags());
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

}
