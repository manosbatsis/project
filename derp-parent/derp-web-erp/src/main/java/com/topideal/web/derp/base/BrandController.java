package com.topideal.web.derp.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.service.base.BrandService;
import com.topideal.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌控制层
 * 
 * @author zhanghx
 */
@RequestMapping("/brand")
@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/brand-list";
	}

	/**
	 * 访问匹配页面
	 */
	/*@RequestMapping("/toMatchingPage.html")
	public String toMatchingPage(Model model) throws Exception {
		//获取匹配下拉
		model.addAttribute("brandList", brandParentService.getSelectBean());
		return "/derp/base/brand-matching";
	}*/
	
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/list.asyn")
	@ResponseBody
	private ViewResponseBean list(BrandDTO dto) {
		try {
			dto = brandService.listByPage(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 获取下拉列表
	 * 
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			List<Long> merchantIds = new ArrayList<Long>();
			merchantIds.add(user.getMerchantId());
			String relMerchantIds = user.getRelMerchantIds();
			if (!StringUtils.isEmpty(relMerchantIds)) {
				String[] relIdArr = relMerchantIds.split(",");
				if (relIdArr != null && relIdArr.length > 0) {
					for (String relId : relIdArr) {
						merchantIds.add(Long.valueOf(relId));
					}
				}
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchantIds", merchantIds);
			result = brandService.getSelectBeanByMerchant(paramMap);
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
	 * 获取详情
	 * @param model
	 * @return
	 */
	@RequestMapping("detail.asyn")
	@ResponseBody
	public ViewResponseBean detail(BrandModel model) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(model.getId()).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			model = brandService.getDetails(model.getId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
		}

		return ResponseFactory.success(model) ;
	}

	/**
	 * 编辑
	 */
	@RequestMapping("/modify.asyn")
	@ResponseBody
	private ViewResponseBean modify(BrandModel model) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(model.getId()).addObject(model.getParentId()).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user = ShiroUtils.getUser();
			brandService.modify(model,user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 根据查询条件导出调拨订单
	 */
	@RequestMapping("/exportBrand.asyn")
	public void exportBrand(BrandModel model, HttpServletResponse response, HttpServletRequest request) throws SQLException {
		//品牌信息
		List<Map<String,Object>> brandList = brandService.listForExport(model);

		//表头信息
		String sheetName = "品牌信息";
		String[] columns = {"品牌名称","中文品牌名称","英文品牌名称","标准品牌","操作人","修改时间"};
		String[] keys = {"name","chinaBrandName","englishBrandName","parentName","operatorName","modifyDate"};

		//生成表格
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, columns, keys, brandList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "品牌信息";
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
	}

	/**
	 * 弹窗选择 分页数据
	 */
	/*@RequestMapping("/getList.asyn")
	@ResponseBody
	private ViewResponseBean getList(BrandModel model, String unNeedIds) {
		try {
			List<Long> ids = null;
			if (!StringUtils.isEmpty(unNeedIds)) {
				ids = StrUtils.parseIds(unNeedIds);
				model.setIds(ids);
			}
			model = brandService.getBrandByPage(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}*/

	/**
	 * 弹窗选择 获取详情
	 */
	/*@RequestMapping("/getListByIds.asyn")
	@ResponseBody
	private ViewResponseBean getListByIds(String ids) {
		List<BrandModel> result = new ArrayList<BrandModel>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			result = brandService.getListByIds(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}*/
	
	/**
	 * 保存匹配
	 */
	/*@RequestMapping("/saveMatching.asyn")
	@ResponseBody
	private ViewResponseBean saveMatching(BrandModel model, String brandIds) {
		try {
			User user = ShiroUtils.getUser();
			String[] brand_ids = brandIds.split(",");
			brandService.saveMatching(model, brand_ids, user.getId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}*/
	
	/**
	 * 解绑
	 * @param ids 解绑的id
	 * @return
	 */
	/*@RequestMapping("/saveUnMatching.asyn")
	@ResponseBody
	private ViewResponseBean saveUnMatching(String ids) {
		try {
			User user = ShiroUtils.getUser();
			List<Long> list = StrUtils.parseIds(ids);
			brandService.saveUnMatching(list, user.getId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}*/
}
