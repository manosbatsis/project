package com.topideal.webapi.main;

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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
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
import com.topideal.webapi.form.CommbarcodeForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 标准条码控制器
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/system/commbarcode")
@Api(tags = "标准条码管理")
public class APICommbarcodeController {

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
	
	/*@RequestMapping("toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/main/commbarcode-list" ;
	}*/
	
	@ApiOperation(value = "标准条码分页数据")
	@PostMapping(value="/listCommbarcodes.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CommbarcodeDTO> listCommbarcodes(CommbarcodeForm form) {
		
		try {
			CommbarcodeDTO model=new CommbarcodeDTO();
			model.setCommbarcode(form.getCommbarcode());
			model.setCommBrandParentId(form.getCommBrandParentId());
			model.setBarcode(form.getBarcode());
			model.setGoodsName(form.getGoodsName());
			model.setGoodsNo(form.getGoodsNo());
			model.setMaintainStatus(form.getMaintainStatus());			
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());
			CommbarcodeDTO commbarcodeModel = commbarcodeService.listCommbarcodes(model) ;
		
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,commbarcodeModel);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean detail(Long id) {
		
		try {			
			Map<String , Object> map = new HashMap<String , Object>();
			CommbarcodeModel model=new CommbarcodeModel();
			model.setId(id);
			model = commbarcodeService.detail(model) ;
			
			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
			commbarcodeItemModel.setCommbarcodeId(model.getId());
			List<CommbarcodeItemModel> itemList = commbarcodeItemService.list(commbarcodeItemModel) ;
			
			map.put("detail", model) ;
			map.put("itemList", itemList) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);//成功
			
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 修改
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "commBrandParentId", value = "标准品牌Id", required = true),
			@ApiImplicitParam(name = "commBrandParentName", value = "标准品牌名称", required = true),
			@ApiImplicitParam(name = "commGoodsName", value = "标准商品名称", required = true),
			@ApiImplicitParam(name = "commTypeId", value = "标准二级分类id", required = true),
			@ApiImplicitParam(name = "commTypeName", value = "标准二级分类名称", required = true)			
	})
	@PostMapping(value="/modifyCommbarcode.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyCommbarcode(Long id,Long commBrandParentId,String commBrandParentName,
			String commGoodsName,Long commTypeId,String commTypeName) {		
		try {
			CommbarcodeModel model=new CommbarcodeModel();
			model.setId(id);
			model.setCommBrandParentId(commBrandParentId);
			model.setCommBrandParentName(commBrandParentName);
			model.setCommGoodsName(commGoodsName);
			model.setCommTypeId(commTypeId);
			model.setCommTypeName(commTypeName);
			int rows = commbarcodeService.modify(model);			
			if(rows > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 访问导入页面
	 * @throws SQLException 
	 */
	/*@RequestMapping("/toImportPage.html")
	public String toImportPage() throws SQLException {
		return "/derp/main/commbarcode-import";
	}*/
	
	/**
	 * 访问详情页面
	 * @throws SQLException 
	 */
	@ApiOperation(value = "详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)	
	})
	@PostMapping(value="/toDetailPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailPage(String id) throws SQLException {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			CommbarcodeDTO dto = new CommbarcodeDTO();
			dto.setId(Long.valueOf(id));
			dto = commbarcodeService.searchByDTO(dto);			
			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
			commbarcodeItemModel.setCommbarcodeId(dto.getId());
			List<CommbarcodeItemModel> list = commbarcodeItemService.list(commbarcodeItemModel);
			resultMap.put("detail" , dto);
			resultMap.put("itemList", list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
     * 下载导入模版
     * @throws IOException
     */
    @ApiOperation(value = "下载导入模版",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/downloadImportTemp.asyn")
    public ResponseBean downloadImportTemp(HttpServletResponse response, HttpServletRequest request,
                               String ids) throws Exception {
    	try {
    		String sheetName = "标准条码管理导入模板";
    		// 获取导出的信息
  	        List<CommbarcodeModel> result = commbarcodeService.getUnMaintainList();
  	        // 生成表格
  	        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, IMPORT_COLUMNS , IMPORT_KEYS , result);
  	        // 导出文件
  	        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);  
  	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
      
    }
    
    /**
     * 获取导出数量
     * @return
     */
    @ApiOperation(value = "获取导出数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id的集合,多个用英文逗号隔开")	
	})
	@PostMapping(value="/getExportCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getExportCount(String ids) {
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
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}		
		data.put("total", total);		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);//成功
    }
    
    /**
     * 下载导出
     * @throws IOException
     */
    @ApiOperation(value = "导出标准条码管理",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportCommbarcode.asyn")
    public void exportCommbarcode(HttpServletResponse response, HttpServletRequest request,
                               String ids) throws Exception {
    	try {
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
    		//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
    	
    }
	
	/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importCommbarcode.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importCommbarcode(String token,@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集				 		
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//文件不能为空
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = commbarcodeService.importCommbarcodes(data,user) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
	}
	

	@ApiOperation(value = "标准品牌下拉框")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "brandParent", value = "标准品牌名称")	
	})
	@PostMapping(value="/getBrandParent.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getBrandParent(String brandParent) {
		try {
			List<BrandParentModel> list = brandParentService.getBrandParentByFuzzyQuery(brandParent);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
}
