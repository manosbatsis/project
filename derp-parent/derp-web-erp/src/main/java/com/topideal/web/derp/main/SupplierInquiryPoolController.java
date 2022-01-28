package com.topideal.web.derp.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.base.CountryOriginService;
import com.topideal.service.base.UnitInfoService;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.SupplierInquiryPoolService;
import com.topideal.shiro.ShiroUtils;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商询价池 控制层
 */
@RequestMapping("/supplierInquiryPool")
@Controller
public class SupplierInquiryPoolController {
	
	/** 标题  */
	private static final String[] COLUMNS= {"品类","商品名称","品牌","供应商","起订量","计量单位","供货价","原产国","规格型号","录入日期"};
	private static final String[] KEYS= {"merchandiseCatName","goodsName","brandName",
			"customerName","minimum","unit","supplyPrice","countryName","spec","createDate"};

	// 供应商询价池service
	@Autowired
	private SupplierInquiryPoolService sIPoolService;
	// 品牌信息service
	@Autowired
	private BrandService brandService;
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	// 商品分类
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	// 原产国service
	@Autowired
	private CountryOriginService countryOriginService;
	// 标准单位
	@Autowired
	private UnitInfoService unitInfoService;
	
	/**
	 * 访问列表页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model)throws Exception {
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		User user= ShiroUtils.getUser();
		List<SelectBean> supplierBean = customerService.getSelectBeanBySupplier(user.getMerchantId());
		model.addAttribute("supplierBean", supplierBean);
		List<SelectBean> productTypeBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
		model.addAttribute("productTypeBean", productTypeBean);
		List<SelectBean> countryBean = countryOriginService.getSelectBean();
		model.addAttribute("countryBean", countryBean);
		return "/derp/main/supplierInquiryPool-list";
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception {
		SupplierInquiryPoolDTO sIPoolModel = sIPoolService.searchDetail(id);
		model.addAttribute("detail", sIPoolModel);
		List<SelectBean> catBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
		model.addAttribute("catBean", catBean);
		List<SelectBean> productTypeBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
		model.addAttribute("productTypeBean", productTypeBean);
		List<SelectBean> unitBean = unitInfoService.getSelectBean();
		model.addAttribute("unitBean", unitBean);
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		List<SelectBean> countryBean = countryOriginService.getSelectBean();
		model.addAttribute("countryBean", countryBean);
		return "/derp/main/supplierInquiryPool-edit";
	}

	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception {
		SupplierInquiryPoolDTO sIPoolModel = sIPoolService.searchDetail(id);
		model.addAttribute("detail", sIPoolModel);
		return "/derp/main/supplierInquiryPool-details";
	}
	
	/**
	 * 导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(){
		return "/derp/main/supplierInquiryPool-import";
	}

	/**
	 * 获取分页数据
	 * @param model  供应商询价池信息
	 * @return
	 */
	@RequestMapping("/listSIPool.asyn")
	@ResponseBody
	private ViewResponseBean listSIPool(SupplierInquiryPoolDTO dto) {
		try{
			User user= ShiroUtils.getUser();
			//数据权限过滤
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = sIPoolService.listSIPool(dto);
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
	@RequestMapping("/delSIPool.asyn")
	@ResponseBody
	public ViewResponseBean delSIPool(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = sIPoolService.delSIPool(list);
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
	 * 批量导入
	 * @param file
	 * @return
	 */
	@RequestMapping("/importSIPool.asyn")
	@ResponseBody
	public ViewResponseBean importSIPool(@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();//返回的结果集
		try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
			User user= ShiroUtils.getUser();
			resultMap = sIPoolService.importSIPool(data,user.getMerchantId(),user.getId());
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
	}
	
	/**
	 * 修改
	 * @param model  供应商询价池信息
	 * @return
	 */
	@RequestMapping("/modifySIPool.asyn")
	@ResponseBody
	public ViewResponseBean modifySIPool(SupplierInquiryPoolModel model) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user= ShiroUtils.getUser();
            boolean b = sIPoolService.modifySIPool(model,user.getId());
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
	 * 导出供应商信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/exportSupplier.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request, SupplierInquiryPoolModel model) throws Exception{
        String sheetName = "供应商询价池导出";
        List<SupplierInquiryPoolModel> list = sIPoolService.exportList(model);
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
