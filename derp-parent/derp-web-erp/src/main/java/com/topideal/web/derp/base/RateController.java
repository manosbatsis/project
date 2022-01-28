package com.topideal.web.derp.base;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.service.base.RateService;
import com.topideal.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 汇率管理 controller
 * @author lianchenxing
 */
@RequestMapping("/rate")
@Controller
public class RateController {
	//
	@Autowired
	private RateService rateService;

	@Autowired
	private RMQProducer rocketMQProducer;//MQ

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/rate-list";
	}
	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		ExchangeRateDTO reptileConfigModel = rateService.searchDetail(id);
		model.addAttribute("detail", reptileConfigModel);
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM");
		if (reptileConfigModel.getEffectiveDate() != null) {
			String effectiveDate = sft.format(reptileConfigModel.getEffectiveDate());
			model.addAttribute("effectiveDate", effectiveDate);
		}
		return "/derp/base/rate-details";
	}
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model) throws SQLException {
		return "/derp/base/rate-add";
	}
	
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(Model model) throws SQLException {
		return "/derp/base/rate-import";
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
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
	}
	
	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@RequestMapping("/listRate.asyn")
	@ResponseBody
	private ViewResponseBean listRate(ExchangeRateDTO dto) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotBlank(dto.getEffectiveDateStr())) {
				Date date = sdf.parse(dto.getEffectiveDateStr());
				dto.setEffectiveDate(date);
			}
			// 响应结果集
			dto = rateService.getListByPage(dto);
			/*List<ExchangeRateDTO> list = dto.getList();
			for (ExchangeRateDTO object : list) {
				object.setOrigCurrencyName(object.getOrigCurrencyName()+" 兑 "+object.getTgtCurrencyName());
			}*/
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 新增
	 * */
	@RequestMapping("/saveRate.asyn")
	@ResponseBody
	public ViewResponseBean saveRate(ExchangeRateModel model, String effectiveDateStr, String expiryDateStr) {
		User user = ShiroUtils.getUser();
		try {
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
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			if (rateService.have(model , null)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_311);
			}
			
			model.setOrigCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getOrigCurrencyCode()));
			model.setTgtCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getTgtCurrencyCode()));
			model.setDataSource(DERP_SYS.EXCHANGERATE_DATASOURCE_SGCJ);
			model.setCreateName(user.getName());
			model.setCreater(user.getId());
			model.setStatus("1");
			boolean b = rateService.saveRate(model);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyRate.asyn")
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
	}
	/**
	 * 根据ID获取商品详情
	 */
	@RequestMapping("/getReptileDetails.asyn")
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
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delRate.asyn")
	@ResponseBody
	public ViewResponseBean delRate(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = rateService.delRate(list);
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
	}
	
	/**
	 * 根据查询条件导出
	 */
	@RequestMapping("/exportRate.asyn")
	public void exportTransferOrder(ExchangeRateDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
		//表头信息
		List<Map<String,Object>> rateList = rateService.listForExport(dto);

		//表头信息
		String sheetName = "汇率信息";
		String[] columns = {"原币","本币","即时汇率","平均汇率","汇率日期","数据来源","创建时间"};
		String[] keys = {"origCurrencyName","tgtCurrencyName","rate","avgRate","rateDate","dataSource","createTime"};

		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, rateList);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, "汇率信息");
	}


	/**
	 * 指定时间抓取汇率信息
	 */
	@RequestMapping("/getCurrencyRates.asyn")
	@ResponseBody
	public ViewResponseBean getCurrencyRates(String selectDate, String isPeriod) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
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
			return ResponseFactory.success();
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_301, e);
		}
	}

}
