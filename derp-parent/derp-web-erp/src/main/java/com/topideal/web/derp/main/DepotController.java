package com.topideal.web.derp.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.BatchValidationInfoModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.DepotMerchantRelService;
import com.topideal.service.main.DepotService;
import com.topideal.shiro.ShiroUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理 控制层
 */
@RequestMapping("/depot")
@Controller
public class DepotController {

	// 仓库信息service
	@Autowired
	private DepotService depotService;
	// 仓库商家关联表service
	@Autowired
	private DepotMerchantRelService depotMerchantRelService;
	

	

	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/main/depot-list";
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long id) throws SQLException {
		List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
		model.addAttribute("merchantBean", merchantBean);
		return "/derp/main/depot-add";
	}

	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
		model.addAttribute("merchantBean", merchantBean);
		DepotInfoDTO depotInfo = depotService.searchDetail(id);
		model.addAttribute("detail", depotInfo);
		List<BatchValidationInfoModel> batch = depotService.getListById(id);
		model.addAttribute("batch",batch);
		DepotCustomsRelModel depotCustomsRel=new DepotCustomsRelModel();
		depotCustomsRel.setDepotId(id);
		return "/derp/main/depot-details";
	}

	/**
	 * 访问编辑页面
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
		model.addAttribute("merchantBean", merchantBean);
		DepotInfoDTO depotInfo = depotService.searchDetail(id);
		model.addAttribute("detail", depotInfo);		
		return "/derp/main/depot-edit";
	}

	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listDepot.asyn")
	@ResponseBody
	private ViewResponseBean listDepot(DepotInfoDTO model) {
		try {
			// 响应结果集
			model = depotService.listDepot(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 校验仓库类别
	 */
	@RequestMapping("/checkDepotType.asyn")
	@ResponseBody
	private ViewResponseBean checkDepotType(Long id) {
		int flag = 0;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			DepotInfoDTO model = depotService.searchDetail(id);
			if(model.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)){
				flag = 1;
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(flag);
	}

	/**
	 * 新增
	 */
	@RequestMapping("/saveDepot.asyn")
	@ResponseBody
	public ViewResponseBean saveDepot(DepotInfoModel model,String json) {
		try {
			User user = ShiroUtils.getUser();
			
			JSONArray jSONArray=new JSONArray();
			if (StringUtils.isNotBlank(json)) {
				JSONObject jsonData = JSONObject.fromObject(json);
				jSONArray = (JSONArray) jsonData.get("itemList");
				Map<String, Object>map=new HashMap<>();
				for (Object object : jSONArray) {
					JSONObject jSONObject=(JSONObject) object;
					String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
					if (StringUtils.isBlank(customsAreaConfigId)) {
						return ResponseFactory.error(StateCodeEnum.MESSAGE_10015);
					}
					if (map.containsKey(customsAreaConfigId)) {
						return ResponseFactory.error(StateCodeEnum.MESSAGE_10016);
					}
					map.put(customsAreaConfigId, customsAreaConfigId);
				}
			}
			
			// 判断仓库类别不为空
			boolean isNullType = new EmptyCheckUtils().addObject(model.getType()).empty();
			if (isNullType) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getName()).addObject(model.getCode())
					.addObject(model.getType()).addObject(model.getISVDepotType()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			model.setCreater(user.getId());// 创建人id
			// 存储信息
			boolean b = depotService.saveDepot(user, model,jSONArray);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delDepot.asyn")
	@ResponseBody
	public ViewResponseBean delDepot(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = depotService.delDepot(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/modifyDepot.asyn")
	@ResponseBody
	public ViewResponseBean modifyDepot(DepotInfoModel model,String json) {
		try {
			JSONArray jSONArray=new JSONArray();
			if (StringUtils.isNotBlank(json)) {
				JSONObject jsonData = JSONObject.fromObject(json);
				jSONArray = (JSONArray) jsonData.get("itemList");
				Map<String, Object>map=new HashMap<>();
				for (Object object : jSONArray) {
					JSONObject jSONObject=(JSONObject) object;
					String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
					if (StringUtils.isBlank(customsAreaConfigId)) {
						return ResponseFactory.error(StateCodeEnum.MESSAGE_10015);
					}
					if (map.containsKey(customsAreaConfigId)) {
						return ResponseFactory.error(StateCodeEnum.MESSAGE_10016);
					}
					map.put(customsAreaConfigId, customsAreaConfigId);
				}
			}
			
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			if (null == model.getIsTopBooks() || "".equals(model.getIsTopBooks())) {
				model.setIsTopBooks("0");
			}
			if (null == model.getStatus() || "".equals(model.getStatus())) {
				model.setStatus("0");
			}
			boolean b = depotService.modifyDepot(null, model,jSONArray);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 导入页面
	 */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/goods/depot-import";
	}

	/**
	 * 导入仓库
	 */
	@RequestMapping("/importDepot.asyn")
	@ResponseBody
	public ViewResponseBean importDepot(@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user = ShiroUtils.getUser();
			resultMap = depotService.importDepot(user, data, user.getId());
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 获取下拉列表
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(DepotInfoModel model) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			if("1".equals(user.getUserType())){
				result = depotService.getSelectBeanForAdmin(model);
			}else{
				model.setMerchantId(user.getMerchantId());
				result = depotService.getSelectBean(model);
			}
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
	 * 根据页面传入商家id、进出接口指令、统计存货跌价、选品限制、已入定出、已出定入获取此商家下仓库的下拉框
	 */
	@RequestMapping("/getSelectBeanByMerchantRel.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBeanByMerchantRel(DepotMerchantRelDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			
			User user = ShiroUtils.getUser() ;
			
			if("1".equals(user.getUserType())) {
				if(dto.getMerchantId() == null) {
					return ResponseFactory.error(StateCodeEnum.ERROR_302);
				}
			}else {
				if(dto.getMerchantId() == null) {
					dto.setMerchantId(user.getMerchantId());
				}
			}
			
			result = depotService.getSelectBeanByMerchantRel(dto);

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
	 * 获取下拉列表
	 */
	@RequestMapping("/getSelectBeanByArea.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBeanByArea(DepotInfoDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			result = depotService.getSelectBeanByArea(dto);
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
	 * 根据ID获取详情
	 */
	@RequestMapping("/getDepotDetails.asyn")
	@ResponseBody
	public ViewResponseBean getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		DepotInfoDTO model = new DepotInfoDTO();
		try {
			model = depotService.searchDetail(id);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 根据id获取仓库信息
	 */
	@RequestMapping("/getDepotById.asyn")
	@ResponseBody
	private ViewResponseBean getDepotById(Long id) {
		DepotInfoDTO model = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			model = depotService.searchDetail(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	/**
	 * 禁用/启用
	 * @param model
	 * @return
	 */
	@RequestMapping("/auditDepot.asyn")
	@ResponseBody
	public ViewResponseBean auditDepot(DepotInfoModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			boolean b = depotService.audit(null, model);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 9011 弹框查询列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/getListById.asyn")
	@ResponseBody
	private ViewResponseBean getListById(Long id) {
		List<BatchValidationInfoModel> model = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			model = depotService.getListById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	/**
	 * 根据仓库Id和商家ID去仓库商家关联表查询相关信息
	 * */
	@RequestMapping("/checkDepotMerchantRel.asyn")
	@ResponseBody
	public ViewResponseBean changeShopCodeLabel(Long depotId) throws Exception {
		User user= ShiroUtils.getUser(); 
		// 仓库商家关联表
		DepotMerchantRelDTO dto = depotMerchantRelService.getByDepotIdAndMerchantId(depotId, user.getMerchantId());
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 获取下拉列表
	 */
	@RequestMapping("/getSelectBeanByDTO.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBeanByDTO(DepotInfoDTO dto) {
		List<DepotInfoDTO> result = new ArrayList<DepotInfoDTO>();
		try {
			User user = ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			dto.setName(dto.getName());
			result = depotService.getSelectBeanByDTO(dto);
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
	 * 根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
	 */
	@RequestMapping("/getSelectBeanByMerchantBuRel.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBeanByMerchantBuRel(MerchantDepotBuRelDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {

			User user = ShiroUtils.getUser() ;

			if("1".equals(user.getUserType())) {
				if(dto.getMerchantId() == null) {
					return ResponseFactory.error(StateCodeEnum.ERROR_302);
				}
			}else {
				if(dto.getMerchantId() == null) {
					dto.setMerchantId(user.getMerchantId());
				}
			}

			result = depotService.getSelectBeanByMerchantBuRel(dto);

		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
}
