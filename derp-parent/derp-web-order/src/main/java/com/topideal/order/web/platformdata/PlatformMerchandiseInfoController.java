package com.topideal.order.web.platformdata;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.order.service.platformdata.PlatformMerchandiseService;
import com.topideal.order.shiro.ShiroUtils;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 京东商品管理
 **/
@RequestMapping("/platformMerchandise")
@Controller
public class PlatformMerchandiseInfoController {


    @Autowired
    private PlatformMerchandiseService platformMerchandiseService;

    /**
     * 访问列表页面
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {

        return "derp/platformdata/platform-merchandise-list";
    }

    /**
     * 获取分页数据
     */
    @RequestMapping("/listByPage.asyn")
    @ResponseBody
    private ViewResponseBean ListByPage(PlatformMerchandiseInfoDTO dto) {
        try {
            User user= ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            dto = platformMerchandiseService.getListByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

	/**
	 * 平台商品导入页面
	 */
	@RequestMapping("/toPlatformMerchandiseImportPage.html")
	public String toPlatformMerchandiseImportPage() {
		return "derp/platformdata/platform-merchandise-import";
	}
	/**
	 * 导入
	 */
	@RequestMapping("/platformMerchandiseImport.asyn")
	@ResponseBody
	public ViewResponseBean platformMerchandiseImport(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			String fileName = file.getOriginalFilename();
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = platformMerchandiseService.savePlatformMerchandiseImport(data,user.getMerchantId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
	/**
	 * 访问批量导入页面
	 */
	@RequestMapping("/toCartonSizeImportPage.html")
	public String toCartonSizeImportPage() {
		return "derp/platformdata/platformmerchandise-cartonsize-import";
	}
	/**
	 * 导入
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@RequestMapping("/cartonSizeImport.asyn")
	@ResponseBody
	public ViewResponseBean cartonSizeImport(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			String fileName = file.getOriginalFilename();
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			resultMap = platformMerchandiseService.updateCartonSizeImport(data,user.getMerchantId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
    /**
     * 导出
     * @throws IOException
     */
    @RequestMapping("/export.asyn")
    public void export(HttpServletResponse response, HttpServletRequest request,PlatformMerchandiseInfoDTO dto) throws Exception {
        String sheetName = "平台商品";
        String[] COLUMNS = { "平台", "归属账号","商品编码", "商品名称","条形码","品牌","单位","箱规"};
		String[] KEYS = {"crawlerTypeLabel", "userCode" , "wareId" , "name","upc","brand","unit","cartonSize"};
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());
        // 获取导出的信息
        List<PlatformMerchandiseInfoDTO> list = platformMerchandiseService.getList(dto);
        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list) ;
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }
}
