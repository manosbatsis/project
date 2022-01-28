package com.topideal.web.derp.main;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.base.TariffRateConfigService;
import com.topideal.service.main.DepotMerchantRelService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.main.MerchantRelService;
import com.topideal.shiro.ShiroUtils;

/**
 * 公司管理 控制层
 */
@RequestMapping("/merchant")
@Controller
public class MerchantController {

	// 公司信息
	@Autowired
	private MerchantInfoService merchantInfoService;
	// 子公司
	@Autowired
	private MerchantRelService merchantRelService;
	@Autowired
	private DepotMerchantRelService depotMerchantRelService;
	@Autowired
	private MerchantBuRelService merchantBuRelService;
	@Autowired
	private TariffRateConfigService tariffRateConfigService;
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/main/merchant-list";
	}

	/**
	 * 访问新增页面
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage() throws Exception {
		return "/derp/main/merchant-add";
	}

	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		//公司明细
		MerchantInfoDTO merchant = merchantInfoService.searchDetail(id);
		model.addAttribute("detail", merchant);
		//代理公司
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(id);
		List<MerchantRelModel> list = merchantRelService.list(merchantRel);
		model.addAttribute("list", list);
		//仓库关联关系
		List<DepotMerchantRelDTO> depotMerchantRelList = depotMerchantRelService.getListByMerchantId(id);
		model.addAttribute("depotMerchantRelList", depotMerchantRelList);
		//公司事业部
		MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel() ;
		merchantBuRelModel.setMerchantId(id);
		List<SelectBean> merchantBuRelList = merchantBuRelService.getSelectBean(merchantBuRelModel);
		model.addAttribute("merchantBuRelList", merchantBuRelList);
		
		return "/derp/main/merchant-edit";
	}
	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		
		//公司明细
		MerchantInfoDTO merchant = merchantInfoService.searchDetail(id);
		model.addAttribute("detail", merchant);
		//代理公司
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(id);
		List<MerchantRelModel> list = merchantRelService.list(merchantRel);
		model.addAttribute("list", list);
		//仓库关联关系
		List<DepotMerchantRelDTO> depotMerchantRelList = depotMerchantRelService.getListByMerchantId(id);
		model.addAttribute("depotMerchantRelList", depotMerchantRelList);
		return "/derp/main/merchant-details";
	}
	/**
	 * 获取分页数据
	 * @param model  公司信息
	 * @return
	 */
	@RequestMapping("/listMerchant.asyn")
	@ResponseBody
	private ViewResponseBean listMerchant(MerchantInfoDTO dto) {
		try{
			// 响应结果集
			dto = merchantInfoService.listMerchantInfoPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(MerchantInfoModel model) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = merchantInfoService.getSelectBean(model);
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
	 * 新增
	 * @param model  公司信息
	 * @return
	 */
	@RequestMapping("/saveMerchant.asyn")
	@ResponseBody
	public ViewResponseBean saveMerchant(MerchantInfoModel model, String ids, String depotids,
			String isInOutInstructions, String isInvertoryFallingPrices, String productRestrictions, 
			String isInDependOuts, String isOutDependIns,String contractCodes) {
		try {
			User user= ShiroUtils.getUser();
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getTopidealCode())
					.addObject(model.getRegisteredType())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			// 公司编码
			String code = TimeUtils.format(new Date(), "ddHHmmsssss");
			model.setCode("ERP" + code);
			// 存储创建人
			model.setCreater(user.getId());
			/*
			 * boolean b = merchantInfoService.saveMerchantInfo(model, ids,
			 * depotids,isInOutInstructions, isInvertoryFallingPrices, productRestrictions,
			 * isInDependOuts, isOutDependIns,contractCodes); if (!b) { return
			 * ResponseFactory.error(StateCodeEnum.ERROR_301); }
			 */
		}catch (RuntimeException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_315, e);
		}catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 修改  公司信息
	 * @param model
	 * @return
	 */
	@RequestMapping("/modifyMerchant.asyn")
	@ResponseBody
	public ViewResponseBean modifyMerchant(MerchantInfoModel model, String ids, String depotids,
			String isInOutInstructions, String isInvertoryFallingPrices, String productRestrictions, 
			String isInDependOuts, String isOutDependIns, String depotBuRels,String contractCodes) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getTopidealCode())
					.addObject(model.getRegisteredType())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			/*
			 * boolean b = merchantInfoService.modifyMerchantInfo(model, ids,
			 * depotids,isInOutInstructions, isInvertoryFallingPrices, productRestrictions,
			 * isInDependOuts, isOutDependIns, depotBuRels,contractCodes); if (!b) { return
			 * ResponseFactory.error(StateCodeEnum.ERROR_301); }
			 */
		}catch (RuntimeException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_315, e);
		}catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 删除  公司信息
	 * @param ids
	 * @return
	 */
	/*
	 * @RequestMapping("/delMerchant.asyn")
	 * 
	 * @ResponseBody public ViewResponseBean delMerchant(String ids) { try{
	 * //校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if(!isRight){
	 * //输入信息不完整 return ResponseFactory.error(StateCodeEnum.ERROR_303); } List<Long>
	 * list = StrUtils.parseIds(ids); boolean b =
	 * merchantInfoService.delMerchant(list); if(!b){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_301); } }catch(SQLException e){
	 * return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
	 * }catch(NullPointerException e){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
	 * ResponseFactory.success(); }
	 */

	/**
	 * 根据ID获取商品详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/getMerchantInfoDetails.asyn")
	@ResponseBody
	public ViewResponseBean getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		MerchantInfoDTO model = new MerchantInfoDTO();
		try {
			model = merchantInfoService.searchDetail(id);
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
	 * 获取关联公司id
	 * @return
	 */
	@RequestMapping("/getRelMerchantIds.asyn")
	@ResponseBody
	public ViewResponseBean getRelMerchantIds(Long merchantId) {
		List<MerchantRelModel>  merchantRelModelList=null;
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(merchantId).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			MerchantRelModel  model=new MerchantRelModel();
			model.setMerchantId(merchantId);
			merchantRelModelList=merchantRelService.getRelMerchantIds(model);
			if(merchantRelModelList!=null&&merchantRelModelList.size()>0){
				StringBuffer str=new  StringBuffer();
				for(MerchantRelModel merchantRelModel:merchantRelModelList){
					str.append(merchantRelModel.getProxyMerchantId()).append(",");
				}
				str.deleteCharAt(str.length()-1);
				return ResponseFactory.success(str.toString());
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(null);
	}
	
	/**
	 * 获取当前公司的代理公司及自己下拉列表
	 * @return
	 */
	@RequestMapping("/getMerchantSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getMerchantSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			MerchantInfoModel model = new MerchantInfoModel();
			if(!"1".equals(user.getUserType())){
				model.setId(user.getMerchantId());
			}
			result = merchantInfoService.getSelectBeanById(model);
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
	 * 获取当前用户需绑定的公司下拉列表
	 * 1.后台管理员创建普通用户：查询所有公司
	 * 2.普通用户创建普通用户：查询该普通用户下绑定的公司
	 * @return
	 */
	@RequestMapping("/getUserMerchantSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getUserMerchantSelectBean(MerchantInfoModel model) {
		User user = ShiroUtils.getUser();
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			if (user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)) {
				result = merchantInfoService.getSelectBean(model);
			} else {
				UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
				userMerchantRelModel.setUserId(user.getId());
				result = merchantInfoService.getUserSelectBean(userMerchantRelModel);
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
	
	@RequestMapping("/getMerchantList.asyn")
	@ResponseBody
	public ViewResponseBean getMerchantList(String merchantIds) {
		User user = ShiroUtils.getUser();

		try {
			Map resultMap=new HashMap<>();
			List<Long> list = StrUtils.parseIds(merchantIds);
			List<MerchantInfoModel> result=merchantInfoService.getMerchantList(list);
			List<TariffRateConfigModel> tariffRateConfigList = tariffRateConfigService.getTariffRateConfigList(new TariffRateConfigModel());
			ArrayList<DerpBasic> settlementTypeList = DERP_SYS.customerMerchantRel_settlementTypeList;
			ArrayList<DerpBasic> accountPeriodList = DERP_SYS.customerInfo_accountPeriodList;
			resultMap.put("result", result);
			resultMap.put("settlementTypeList", settlementTypeList);
			resultMap.put("accountPeriodList", accountPeriodList);
			resultMap.put("tariffRateConfigList", tariffRateConfigList);
			return ResponseFactory.success(resultMap);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		

	}
	
}
