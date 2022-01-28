package com.topideal.web.derp.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.BusinessUnitService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantInfoService;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("merchantBuRel")
public class MerchantBuRelController {
	
	private static final String[] EXPORT_COLUMNS = { "公司编码", "公司简称", "事业部编码", "事业部名称", "状态","采购价格管理","销售价格管理", "创建时间"};
	private static final String[] EXPORT_KEYS = {"merchantCode", "merchantName", "buCode", "buName", "statusLabel","purchasePriceManageLabel","salePriceManageLabel", "createDate"} ;
	
	@Autowired
	BusinessUnitService businessUnitService ;
	@Autowired
	MerchantInfoService merchantInfoService ;
	@Autowired
	MerchantBuRelService merchantBuRelService ;

	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		
		MerchantInfoModel merchant = new MerchantInfoModel() ;
        List<SelectBean> merchantList = merchantInfoService.getSelectBean(merchant);
        model.addAttribute("merchantList", merchantList);
        
        List<BusinessUnitModel> businessUnitList = businessUnitService.getAllList();
        model.addAttribute("businessUnitList", businessUnitList);
		
		return "/derp/main/merchant-bu-rel-list";
	}
	
	/**
	 * 获取分页数据
	 * @param model  公司信息
	 * @return
	 */
	@RequestMapping("/listMerchantBuRel.asyn")
	@ResponseBody
	private ViewResponseBean listMerchantBuRel(MerchantBuRelDTO dto) {
		try{
			// 响应结果集
			dto = merchantBuRelService.listMerchantBuRelPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 保存
	 * @param model  公司信息
	 * @return
	 */
	@RequestMapping("/saveMerchantBuRel.asyn")
	@ResponseBody
	private ViewResponseBean saveMerchantBuRel(MerchantBuRelModel model) {
		try{
			merchantBuRelService.save(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 启用、禁用
	 * @param model  公司信息
	 * @return
	 */
	/*@RequestMapping("/changeStaus.asyn")
	@ResponseBody
	private ViewResponseBean changeStaus(MerchantBuRelModel model) {
		try{
			merchantBuRelService.changeStaus(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}*/
	
	@RequestMapping("/exportMerchantBuRel.asyn")
	@ResponseBody
	public void exportMerchantBuRel(MerchantBuRelDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
		// 响应结果集
		List<MerchantBuRelDTO> result = merchantBuRelService.getExportList(dto);
		
		String sheetName = "公司事业部导出";
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(MerchantBuRelModel model) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = merchantBuRelService.getSelectBean(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	/**
	 * 修改
	 * @param model  公司信息
	 * @return
	 */
	@RequestMapping("/modifyMerchantBuRel.asyn")
	@ResponseBody
	private ViewResponseBean modifyMerchantBuRel(MerchantBuRelModel model) {
		try{
			merchantBuRelService.modifyMerchantBuRel(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 获取详情
	 * @return
	 */
	@RequestMapping("/toDetailsPage.asyn")
	@ResponseBody
	public ViewResponseBean toDetailsPage(Model model, Long id) {
		MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();
		try {
			merchantBuRelModel = merchantBuRelService.searchDetail(id);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(merchantBuRelModel);
	}
}
