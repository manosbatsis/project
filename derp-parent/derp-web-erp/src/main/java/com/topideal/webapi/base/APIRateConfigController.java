package com.topideal.webapi.base;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.service.base.RateConfigService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 汇率管理配置 controller
 * @author 杨创
 */
@RestController
@RequestMapping("/webapi/system/rateConfig")
@Api(tags = "汇率配置列表")
public class APIRateConfigController {
	//
	@Autowired
	private RateConfigService rateConfigService;

	/**
	 * 访问列表页面
	 * */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/rate-config-list";
	}*/

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	/*@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model) throws SQLException {
		return "/derp/base/rate-config-add";
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
			@ApiImplicitParam(name = "origCurrencyCode", value = "原币种"),
			@ApiImplicitParam(name = "tgtCurrencyCode", value = "本币种"),
			@ApiImplicitParam(name = "dataSource", value = "数据来源")
	})
	@PostMapping(value="/listRateConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ExchangeRateConfigDTO> listRate(String origCurrencyCode,String tgtCurrencyCode,String dataSource,int begin,int pageSize) {
		try{
			// 响应结果集
			ExchangeRateConfigDTO dto=new ExchangeRateConfigDTO();
			dto.setOrigCurrencyCode(origCurrencyCode);
			dto.setTgtCurrencyCode(tgtCurrencyCode);
			dto.setDataSource(dataSource);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = rateConfigService.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "origCurrencyCode", value = "原币种" , required = true),
			@ApiImplicitParam(name = "tgtCurrencyCode", value = "本币种", required = true),
			@ApiImplicitParam(name = "dataSource", value = "数据来源", required = true)
	})
	@PostMapping(value="/saveRateConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveRate(String token,String origCurrencyCode,String tgtCurrencyCode,String dataSource ) {		
		try {
			User user = ShiroUtils.getUserByToken(token);
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(tgtCurrencyCode)
					.addObject(origCurrencyCode)
					.addObject(dataSource)
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			ExchangeRateConfigModel queryModel=new ExchangeRateConfigModel();
			queryModel.setOrigCurrencyCode(origCurrencyCode);
			queryModel.setTgtCurrencyCode(tgtCurrencyCode);
			queryModel = rateConfigService.serchByModel(queryModel);
			if (queryModel!=null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10012);
			}
			ExchangeRateConfigModel model =new ExchangeRateConfigModel();
			model.setOrigCurrencyCode(origCurrencyCode);
			model.setTgtCurrencyCode(tgtCurrencyCode);
			model.setDataSource(dataSource);
			model.setOrigCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getOrigCurrencyCode()));
			model.setTgtCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getTgtCurrencyCode()));
			model.setDataSource(model.getDataSource());
			model.setCreateName(user.getName());
			model.setCreater(user.getId());
			boolean b = rateConfigService.saveRate(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开" , required = true)
	})
	@PostMapping(value="/delRateConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delRateConfig(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = rateConfigService.delRateConfig(list);
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
	@GetMapping(value="/export.asyn")
	public void exportTransferOrder(String origCurrencyCode,String tgtCurrencyCode,String dataSource, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		try {
			//表头信息
			ExchangeRateConfigModel dto=new ExchangeRateConfigModel();
			dto.setOrigCurrencyCode(origCurrencyCode);
			dto.setTgtCurrencyCode(tgtCurrencyCode);
			dto.setDataSource(dataSource);
			List<Map<String, Object>> rateConfigList = rateConfigService.listForExport(dto);
			for (Map<String, Object> map : rateConfigList) {
				String dataSourceMap = (String) map.get("data_source");
				dataSourceMap = DERP_SYS.getLabelByKey(DERP_SYS.exchangeRateConfig_dataSourceList, dataSourceMap);
				map.put("data_source", dataSourceMap);
			}

			//表头信息
			String sheetName = "汇率配置";
			String[] columns = {"原币","本币","数据来源","创建时间","创建人"};
			String[] keys = {"orig_currency_name","tgt_currency_code","data_source","create_date","create_name"};

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, rateConfigList);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}



}
