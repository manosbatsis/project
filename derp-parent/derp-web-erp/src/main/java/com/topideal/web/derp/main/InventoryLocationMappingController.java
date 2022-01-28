//package com.topideal.web.derp.main;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.log4j.Logger;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.bean.SelectBean;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtil;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
//import com.topideal.entity.vo.main.InventoryLocationMappingModel;
//import com.topideal.service.main.InventoryLocationMappingService;
//import com.topideal.shiro.ShiroUtils;
//
///**
// * 库位映射表
// * @author Gy
// *
// */
//
//@Controller
//@RequestMapping("/inventoryLocationMapping")
//public class InventoryLocationMappingController {
//
//	private static final Logger LOG = Logger.getLogger(InventoryLocationMappingController.class) ;
//
//	private static final String[] MAIN_COLUMNS = {"公司", "原货号", "商品名称", "库位货号", "库位类型","是否指定唯一", "创建人", "创建时间",
//			"修改人", "修改时间"} ;
//
//	private static final String[] MAIN_KEYS = {"merchantName", "originalGoodsNo", "goodsName", "goodsNo", "typeLabel","isorRappointLabel", "createName", "createDate",
//					"modifyName", "modifyDate"} ;
//
//	@Autowired
//	InventoryLocationMappingService service ;
//
//	/**
//	 * 访问列表页面
//	 * @throws SQLException
//	 */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws SQLException {
//
//		User user = ShiroUtils.getUser();
//        List<SelectBean> merchantList = service.getMerchantList(user);
//        model.addAttribute("merchantList", merchantList);
//
//		return "/derp/main/inven-location-mapping-list";
//	}
//
//	@RequestMapping("/listInventoryLocationMapping.asyn")
//	@ResponseBody
//	public ViewResponseBean listInventoryLocationMapping(InventoryLocationMappingDTO dto) {
//
//		try {
//			dto = service.listInventoryLocationMapping(dto) ;
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//		}
//
//		return ResponseFactory.success(dto) ;
//	}
//
//	@RequestMapping("/exportInventoryLocationMapping.asyn")
//	@ResponseBody
//	public void exportInventoryLocationMapping(HttpServletResponse response,
//			HttpServletRequest request, InventoryLocationMappingDTO dto, String ids) throws Exception {
//
//		List<InventoryLocationMappingDTO> exportList = service.exportInventoryLocationMapping(dto) ;
//
//		//生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("库位映射表", MAIN_COLUMNS, MAIN_KEYS, exportList) ;
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, "库位映射表导出");
//	}
//
//	@RequestMapping("/saveOrModify.asyn")
//	@ResponseBody
//	public ViewResponseBean saveOrModify(InventoryLocationMappingModel model) {
//
//		try {
//			User user = ShiroUtils.getUser();
//			service.saveOrModify(model,user) ;
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
//		}
//
//		return ResponseFactory.success() ;
//	}
//
//	/**
//	 * 访问详情页面
//	 * @throws SQLException
//	 */
//	@RequestMapping("/detail.asyn")
//	@ResponseBody
//	public ViewResponseBean detail(String id) throws SQLException {
//
//		InventoryLocationMappingModel model;
//		try {
//			model = service.seachById(Long.valueOf(id));
//		} catch (NumberFormatException e) {
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
//		}
//
//		return ResponseFactory.success(model) ;
//	}
//
//	/**
//	 * 删除
//	 * @throws SQLException
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/deleteInventoryLocationMapping.asyn")
//	@ResponseBody
//	public ViewResponseBean deleteInventoryLocationMapping(String ids) throws SQLException {
//
//		try {
//
//			// 校验id是否正确
//			boolean isRight = StrUtils.validateIds(ids);
//			if (!isRight) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//
//			List<Long> list = StrUtils.parseIds(ids);
//			boolean b = service.deleteInventoryLocationMapping(list);
//			if (!b) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//		}
//
//		return ResponseFactory.success() ;
//	}
//
//	/**
//	 * 访问导入页面
//	 * @throws SQLException
//	 */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage() throws SQLException {
//		return "/derp/main/inven-location-mapping-import";
//	}
//
//	/**
//	 * 导入
//	 * @param file
//	 * @param session
//	 * @return
//	 * @throws IOException
//	 */
//	@SuppressWarnings("rawtypes")
//	@RequestMapping("importInventoryLocationMapping.asyn")
//	@ResponseBody
//	public ViewResponseBean importInventoryLocationMapping(@RequestParam(value = "file", required = false) MultipartFile file,
//			HttpSession session) throws IOException {
//		Map resultMap = new HashMap();// 返回的结果集
//
//		if (file == null) {
//			// 输入信息不完整
//			return ResponseFactory.error(StateCodeEnum.ERROR_303);
//		}
//
//		try {
//			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
//
//			if (data == null) {// 数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//			User user= ShiroUtils.getUser();
//
//			resultMap = service.importInventoryLocationMapping(data , user) ;
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//
//		return ResponseFactory.success(resultMap);
//	}
//
//	@RequestMapping("getOriginalGoodsId.asyn")
//	@ResponseBody
//	public ViewResponseBean getOriginalGoodsId(InventoryLocationMappingDTO dto) throws IOException {
//		try {
//			User user = ShiroUtils.getUser();
//			dto = service.getOriginalGoodsId(user,dto) ;
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//
//		return ResponseFactory.success(dto);
//	}
//	@RequestMapping("/updateIsorRappoint.asyn")
//	@ResponseBody
//	private	ViewResponseBean updateIsorRappoint(Long id){
//		Map<String, Object> updateIsorRappointMap= new HashMap<>();
//		try {
//			updateIsorRappointMap = service.updateNotSettlement(id);
//	        return ResponseFactory.success(updateIsorRappointMap);
//     }catch(Exception e){
//    	 e.printStackTrace();
//         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//     }
//
//	}
//
//}
