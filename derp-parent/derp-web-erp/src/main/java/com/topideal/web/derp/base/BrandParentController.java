package com.topideal.web.derp.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.service.base.BrandParentService;
import com.topideal.shiro.ShiroUtils;


/**
 * 标准品牌控制层
 * @author zhanghx
 */
@RequestMapping("/brandParent")
@Controller
public class BrandParentController {

	@Autowired
	private BrandParentService brandParentService;

	@RequestMapping("/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, BrandParentDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		String sheetName = "标准品牌管理导出";
		/** 标题  */
		String[] COLUMNS= {"标准品牌","英文名称","上级母品牌名称","授权方","分类"};
		String[] KEYS= {"name","englishName","superiorParentBrand","authorizerLabel","typeLabel"};
		List<BrandParentDTO> list = brandParentService.exportbrandParentList(dto);
		
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	

	/**
	 * 访问列表页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		
        
		return "/derp/base/brand-parent-list";
	}
	
	/**
	 * 访问导入页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws SQLException {
		return "/derp/base/brand-parent-import";
	}

	/**
	 * 获取分页数据
	 */
	@RequestMapping("/list.asyn")
	@ResponseBody
	private ViewResponseBean list(BrandParentDTO dto) {
		try {
			
			User user= ShiroUtils.getUser();						
            dto = brandParentService.listByPage(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 新增
	 */
	@RequestMapping("/save.asyn")
	@ResponseBody
	private ViewResponseBean save(BrandParentModel model,String superiorParentBrandCodeEditOrAdd) {
		try {
			User user= ShiroUtils.getUser(); 			
			model.setCreater(user.getId());
			if(StringUtils.isNotBlank(superiorParentBrandCodeEditOrAdd)) {
				model.setSuperiorParentBrandId(Long.valueOf(superiorParentBrandCodeEditOrAdd));
			}
			brandParentService.save(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping("/modify.asyn")
	@ResponseBody
	private ViewResponseBean modify(BrandParentModel model, String oldName,String superiorParentBrandCodeEditOrAdd) {
		try {
			User user= ShiroUtils.getUser(); 			
			model.setCreater(user.getId());
			if(StringUtils.isNotBlank(superiorParentBrandCodeEditOrAdd)) {
				model.setSuperiorParentBrandId(Long.valueOf(superiorParentBrandCodeEditOrAdd));
			}
			brandParentService.modify(model, oldName);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 删除
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete.asyn")
	@ResponseBody
	private ViewResponseBean delete(String ids) {
		try {
			List<Long> list = StrUtils.parseIds(ids);
			brandParentService.delete(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
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
			BrandParentModel model = new BrandParentModel();
			result = brandParentService.getSelectBean(model);
		} catch (Exception e) {
			e.printStackTrace();
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
	public ViewResponseBean detail(BrandParentModel model) {
		
		try {
			model = brandParentService.detail(model);
			
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
		}
		
		return ResponseFactory.success(model) ;
	}
	
	/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("importBrandParent.asyn")
	@ResponseBody
	public ViewResponseBean importBrandParent(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		
		if (file == null) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
				file.getOriginalFilename(), 1);
		if (data == null) {// 数据为空
			return ResponseFactory.error(StateCodeEnum.ERROR_302);
		}
		User user= ShiroUtils.getUser(); 
		
		try {
			resultMap = brandParentService.importBrandParent(data , user) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
		return ResponseFactory.success(resultMap);
	}


	@RequestMapping("/getBrandParent.asyn")
	@ResponseBody
	public ViewResponseBean getBrandParent(String commbarcode) {
		try {
			BrandParentModel brandParentModel = brandParentService.getByCommbarcode(commbarcode);
			return ResponseFactory.success(brandParentModel) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}

	}
}
