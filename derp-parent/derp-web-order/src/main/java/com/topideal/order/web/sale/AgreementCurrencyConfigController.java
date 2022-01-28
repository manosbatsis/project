package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigExportDTO;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.AgreementCurrencyConfigService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 协议单价 controller
 * 
 */
@RequestMapping("/agreementCurrencyConfig")
@Controller
public class AgreementCurrencyConfigController {
	private static final String[] COLUMNS = { "公司","移入事业部","移出事业部",  "商品货号",
			"商品名称", "协议单价", "协议币种", "创建人", "创建时间"};

	private static final String[] KEYS = { "merchantName", "inBuName", "outBuName", "goodsNo", "goodsName",
			"price","currencyLabel", "createName", "createDate"};
	// 协议单价service
	@Autowired
	private AgreementCurrencyConfigService agreementCurrencyConfigService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model, HttpSession session) throws Exception {
		return "/derp/sale/agreement-currency-config-list";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listAgreementCurrencyConfig.asyn")
	@ResponseBody
	private ViewResponseBean listAgreementCurrencyConfig(AgreementCurrencyConfigDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家idloa
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = agreementCurrencyConfigService.listAgreementCurrencyByPage(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delAgreementCurrencyConfig.asyn")
	@ResponseBody
	public ViewResponseBean delAgreementCurrencyConfig(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = agreementCurrencyConfigService.delAgreementCurrencyConfig(list);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(RuntimeException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	/**
	 * 获取导出协议单价的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,AgreementCurrencyConfigDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<AgreementCurrencyConfigDTO> result = agreementCurrencyConfigService.listAgreementCurrencyConfig(dto,user);
			data.put("total",result.size());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出
	 * */
	@RequestMapping("/exportAgreementCurrencyConfig.asyn")
	@ResponseBody
	private void exportAgreementCurrencyConfig(HttpSession session, HttpServletResponse response, HttpServletRequest request, AgreementCurrencyConfigDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		String sheetName = "协议单价导出";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<AgreementCurrencyConfigExportDTO> result = agreementCurrencyConfigService.getDetailsByExport(dto,user);
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	/**
	 * 访问新增页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, HttpSession session)throws Exception{
		//初始化数据
		model.addAttribute("itemCount", 0);
		User user= ShiroUtils.getUser();
		model.addAttribute("merchantId", user.getMerchantId());
		model.addAttribute("merchantName", user.getMerchantName());
		return "/derp/sale/agreement-currency-config-add";
	}
	/**
	 * 提交
	 * */
	@RequestMapping("/addAgreementCurrencyConfig.asyn")
	@ResponseBody
	public ViewResponseBean addAgreementCurrencyConfig(String json,HttpSession session) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			msg = agreementCurrencyConfigService.saveAgreementCurrencyConfig(json,user.getId(),user.getName(), user.getTopidealCode());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}
	/**
	 * 访问导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(){
		return "/derp/sale/agreement-currency-config-import";
	}
	/**
	 * 导入协议单价
	 * */
	@RequestMapping("/importAgreementCurrencyConfig.asyn")
	@ResponseBody
	public ViewResponseBean importAgreementCurrencyConfig(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
		Map resultMap = new HashMap();//返回的结果集
		try{
			if(file==null){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = agreementCurrencyConfigService.saveImportAgreementCurrencyConfig(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
	}
}