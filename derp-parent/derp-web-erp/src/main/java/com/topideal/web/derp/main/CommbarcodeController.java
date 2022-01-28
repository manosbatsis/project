package com.topideal.web.derp.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.service.base.BrandParentService;
import com.topideal.service.main.CommbarcodeItemService;
import com.topideal.service.main.CommbarcodeService;
import com.topideal.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准条码控制器
 * @author gy
 *
 */
@Controller
@RequestMapping("commbarcode")
public class CommbarcodeController {

	private static final String[] IMPORT_COLUMNS = { "标准条码","标准品牌"};
	private static final String[] IMPORT_KEYS = {"commbarcode" , "commBrandParentName"} ;
	
	private static final String[] EXPORT_COLUMNS = { "标准条码","母品牌","标准品牌","标准品类","标准品名","授权方","分类","创建时间"};
	private static final String[] EXPORT_KEYS = {"commbarcode" ,"brandSuperiorName", "commBrandParentName" ,"commTypeName" ,"commGoodsName" ,"authorizerLabel" ,"typeLabel" ,"createDate"} ;
	private static final String[] EXPORT_ITEM_COLUMNS = { "标准条码","条形码","商品货号","商品名称"};
	private static final String[] EXPORT_ITEM_KEYS = {"commbarcode" , "barcode" ,"goodsNo" ,"goodsName"} ;
	
	@Autowired
	private BrandParentService brandParentService ;
	@Autowired
	private CommbarcodeService commbarcodeService ;
	@Autowired
	private CommbarcodeItemService commbarcodeItemService ;
	
	@RequestMapping("toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/main/commbarcode-list" ;
	}
	
	@RequestMapping("listCommbarcodes.asyn")
	@ResponseBody
	public ViewResponseBean listCommbarcodes(CommbarcodeDTO model) {
		
		try {
			CommbarcodeDTO commbarcodeModel = commbarcodeService.listCommbarcodes(model) ;
		
			return ResponseFactory.success(commbarcodeModel) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	@RequestMapping("detail.asyn")
	@ResponseBody
	public ViewResponseBean detail(CommbarcodeModel model) {
		
		try {
			
			Map<String , Object> map = new HashMap<String , Object>() ;
			
			model = commbarcodeService.detail(model) ;
			
			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
			commbarcodeItemModel.setCommbarcodeId(model.getId());
			List<CommbarcodeItemModel> itemList = commbarcodeItemService.list(commbarcodeItemModel) ;
			
			map.put("detail", model) ;
			map.put("itemList", itemList) ;
			
			return ResponseFactory.success(map) ;
			
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	/**
	 * 修改
	 * @param model
	 * @return
	 */
	@RequestMapping("modifyCommbarcode.asyn")
	@ResponseBody
	public ViewResponseBean modifyCommbarcode(CommbarcodeModel model) {
		
		try {
			int rows = commbarcodeService.modify(model);
			
			if(rows > 0) {
				return ResponseFactory.success() ;
			}else {
				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
			}
			
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
		}
	}
	
	/**
	 * 访问导入页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws SQLException {
		return "/derp/main/commbarcode-import";
	}
	
	/**
	 * 访问详情页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toDetailPage.html")
	public String toDetailPage(String id , Model model) throws SQLException {
		
		CommbarcodeDTO dto = new CommbarcodeDTO();
		dto.setId(Long.valueOf(id));
		dto = commbarcodeService.searchByDTO(dto) ;
		
		CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
		commbarcodeItemModel.setCommbarcodeId(dto.getId());
		List<CommbarcodeItemModel> list = commbarcodeItemService.list(commbarcodeItemModel);
		
		model.addAttribute("detail" , dto) ;
		model.addAttribute("itemList", list) ;
		
		return "/derp/main/commbarcode-details";
	}
	
	/**
     * 下载导入模版
     * @throws IOException
     */
    @RequestMapping("/downloadImportTemp.asyn")
    public void downloadImportTemp(HttpServletResponse response, HttpServletRequest request,
                               String ids) throws Exception {
        String sheetName = "标准条码管理导入模板";
        // 获取导出的信息
        List<CommbarcodeModel> result = commbarcodeService.getUnMaintainList();
        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, IMPORT_COLUMNS , IMPORT_KEYS , result);
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }
    
    /**
     * 获取导出数量
     * @return
     */
    @RequestMapping("/getExportCount.asyn")
	@ResponseBody
    public ViewResponseBean getExportCount(String ids) {
    	int total = 0 ;
    	Map<String, Object> data = new HashMap<String, Object>();
    	
		try {
			if(StringUtils.isNotBlank(ids)) {
				String[] idArr = ids.split(",");
				total = idArr.length ;
			}else {
				List<CommbarcodeModel> list = commbarcodeService.list(new CommbarcodeModel());
				total = list.size() ;
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
		data.put("total", total);
		
		return ResponseFactory.success(data) ;
    }
    
    /**
     * 下载导出
     * @throws IOException
     */
    @RequestMapping("/exportCommbarcode.asyn")
    public void exportCommbarcode(HttpServletResponse response, HttpServletRequest request,
                               String ids) throws Exception {
    	List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
    	String mainSheetName = "标准条码管理";
        
    	// 获取导出的信息
        List<CommbarcodeDTO> result = commbarcodeService.getExportCommbarcodes(ids);
		String sheetName = "标准条码管理";
		ExportExcelSheet mianSheet = ExcelUtilXlsx.createSheet(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
		sheetList.add(mianSheet) ;
		
		List<CommbarcodeDTO> itemResult = commbarcodeService.getExportList(ids); 
		String itemSheetName = "商品信息";
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, EXPORT_ITEM_COLUMNS, EXPORT_ITEM_KEYS , itemResult);
		sheetList.add(itemSheet) ;
        
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
    }
	
	/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("importCommbarcode.asyn")
	@ResponseBody
	public ViewResponseBean importCommbarcode(@RequestParam(value = "file", required = false) MultipartFile file,
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
			resultMap = commbarcodeService.importCommbarcodes(data , user) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
		return ResponseFactory.success(resultMap);
	}
	
	@RequestMapping("getBrandParent.asyn")
	@ResponseBody
	public ViewResponseBean getBrandParent(String brandParent) {
		try {
			List<BrandParentModel> list = brandParentService.getBrandParentByFuzzyQuery(brandParent);
			return ResponseFactory.success(list) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
	}
}
