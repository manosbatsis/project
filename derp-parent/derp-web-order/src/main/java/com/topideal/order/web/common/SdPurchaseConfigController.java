package com.topideal.order.web.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.order.service.common.SdPurchaseConfigService;
import com.topideal.order.service.common.SdTypeService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购sd配置
 * @author gy
 *
 */

@Controller
@RequestMapping("/sdPurchaseConfig")
public class SdPurchaseConfigController {
	
	private static final Logger LOG = Logger.getLogger(SdPurchaseConfigController.class) ;
	
	@Autowired
	private SdPurchaseConfigService sdPurchaseConfigService;
	@Autowired
	private SdTypeService sdTypeService ;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/common/sd-purchase-config-list";
	}
	
	/**
	 * 访问新增详情
	 * @param model
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage() throws Exception{
		return "/derp/common/sd-purchase-config-add" ;
	}
	
	/**
	 * 访问编辑详情
	 * @param model
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Long id, Model model) throws Exception{
		
		SdPurchaseConfigDTO dto = sdPurchaseConfigService.getDTOById(id) ;
		List<SdPurchaseConfigItemModel> itemList = sdPurchaseConfigService.getItemById(id) ;
		
		SdTypeConfigModel queryModel = new SdTypeConfigModel() ;
		queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
		List<SdTypeConfigModel> singleList = sdTypeService.getList(queryModel);
		
		queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
		List<SdTypeConfigModel> multiList = sdTypeService.getList(queryModel);
		
		model.addAttribute("details", dto) ;
		model.addAttribute("itemList", itemList) ;
		model.addAttribute("singleList", singleList) ;
		model.addAttribute("multiList", multiList) ;
		
		return "/derp/common/sd-purchase-config-edit" ;
	}
	
	/**
	 * 访问复制详情
	 * @param model
	 * */
	@RequestMapping("/toCopyPage.html")
	public String toCopyPage(Long id, Model model) throws Exception{
		
		SdPurchaseConfigDTO dto = sdPurchaseConfigService.getDTOById(id) ;
		List<SdPurchaseConfigItemModel> itemList = sdPurchaseConfigService.getItemById(id) ;
		
		SdTypeConfigModel queryModel = new SdTypeConfigModel() ;
		queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
		List<SdTypeConfigModel> singleList = sdTypeService.getList(queryModel);
		
		queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
		List<SdTypeConfigModel> multiList = sdTypeService.getList(queryModel);
		
		List<SelectBean> brandParentList = sdPurchaseConfigService.getBrandParent() ;
		
		dto.setId(null);
		
		model.addAttribute("details", dto) ;
		model.addAttribute("itemList", itemList) ;
		model.addAttribute("singleList", singleList) ;
		model.addAttribute("multiList", multiList) ;
		model.addAttribute("brandParentList", brandParentList) ;
		
		return "/derp/common/sd-purchase-config-edit" ;
	}
	
	/**
	 * 访问详情
	 * @param model
	 * */
	@RequestMapping("/toDetail.html")
	public String toDetailPage(Long id, Model model) throws Exception{
		
		SdPurchaseConfigDTO dto = sdPurchaseConfigService.getDTOById(id) ;
		List<SdPurchaseConfigItemModel> itemList = sdPurchaseConfigService.getItemById(id) ;
		
		model.addAttribute("details", dto) ;
		model.addAttribute("itemList", itemList) ;
		
		return "/derp/common/sd-purchase-config-details" ;
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/sdPurchaseConfigList.asyn")
	@ResponseBody
	private ViewResponseBean sdTypeConfigList(SdPurchaseConfigDTO dto) {
		try{
			// 响应结果集
			dto = sdPurchaseConfigService.getSdPurchaseConfigListByPage(dto);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	@RequestMapping("saveOrModify.asyn")
	@ResponseBody
	public ViewResponseBean saveOrModify(SdPurchaseConfigModel model, @RequestParam(value="itemList")String itemList) {
		try{
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getMerchantId())
					.addObject(model.getSupplierId())
					.addObject(model.getEffectiveTime())
					.addObject(model.getInvalidTime())
					.addObject(model.getBuId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user = ShiroUtils.getUser();
			// 响应结果集
			sdPurchaseConfigService.saveOrModify(model, itemList, user) ;
		}catch(DerpException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 删除
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delOrders.asyn")
	@ResponseBody
	private ViewResponseBean delOrders(String ids) {
		try{
			if(StringUtils.isBlank(ids)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<Long> list = (List<Long>)StrUtils.parseIds(ids);
			
			// 响应结果集
			sdPurchaseConfigService.delOrders(list);
		}catch(DerpException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		return ResponseFactory.success();
	}
	
	@RequestMapping("/importSdPurchaseConfig.asyn")
	@ResponseBody
	public ViewResponseBean importSdPurchaseConfig(@RequestParam(value = "file", required = false) MultipartFile file, 
			HttpSession session) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			resultMap = sdPurchaseConfigService.importSdPurchaseConfig(data) ;
			
			return ResponseFactory.success(resultMap) ;
			
		}catch(DerpException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304);
		}
	}
	
}
