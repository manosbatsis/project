package com.topideal.web.derp.base;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.service.base.RateConfigService;
import com.topideal.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 汇率管理配置 controller
 * @author 杨创
 */
@RequestMapping("/rateConfig")
@Controller
public class RateConfigController {
	//
	@Autowired
	private RateConfigService rateConfigService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/rate-config-list";
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model) throws SQLException {
		return "/derp/base/rate-config-add";
	}
	

	

	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@RequestMapping("/listRateConfig.asyn")
	@ResponseBody
	private ViewResponseBean listRate(ExchangeRateConfigDTO dto) {
		try{
			// 响应结果集
			dto = rateConfigService.getListByPage(dto);
			List<ExchangeRateDTO> list = dto.getList();
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
	@RequestMapping("/saveRateConfig.asyn")
	@ResponseBody
	public ViewResponseBean saveRate(ExchangeRateConfigModel model) {
		User user = ShiroUtils.getUser();
		try {
			
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getTgtCurrencyCode())
					.addObject(model.getOrigCurrencyCode())
					.addObject(model.getDataSource())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			ExchangeRateConfigModel queryModel=new ExchangeRateConfigModel();
			queryModel.setOrigCurrencyCode(model.getOrigCurrencyCode());
			queryModel.setTgtCurrencyCode(model.getTgtCurrencyCode());
			queryModel = rateConfigService.serchByModel(queryModel);
			if (queryModel!=null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_319);
			}
			model.setOrigCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getOrigCurrencyCode()));
			model.setTgtCurrencyName(DERP.getLabelByKey(DERP.currencyCodeList, model.getTgtCurrencyCode()));
			model.setDataSource(model.getDataSource());
			model.setCreateName(user.getName());
			model.setCreater(user.getId());
			boolean b = rateConfigService.saveRate(model);
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
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delRateConfig.asyn")
	@ResponseBody
	public ViewResponseBean delRateConfig(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = rateConfigService.delRateConfig(list);
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
	@RequestMapping("/export.asyn")
	public void exportTransferOrder(ExchangeRateConfigModel dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
		//表头信息
		List<Map<String, Object>> rateConfigList = rateConfigService.listForExport(dto);
		for (Map<String, Object> map : rateConfigList) {
			String dataSource = (String) map.get("data_source");
			dataSource = DERP_SYS.getLabelByKey(DERP_SYS.exchangeRateConfig_dataSourceList, dataSource);
			map.put("data_source", dataSource);
		}

		//表头信息
		String sheetName = "汇率配置";
		String[] columns = {"原币","本币","数据来源","创建时间","创建人"};
		String[] keys = {"orig_currency_name","tgt_currency_code","data_source","create_date","create_name"};

		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, rateConfigList);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}



}
