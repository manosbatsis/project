package com.topideal.web.derp.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.EmailConfigService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送配置信息控制层
 */
@RequestMapping("/email")
@Controller
public class EmailConfigController {
	
	@Autowired
	private EmailConfigService emailConfigService;// 邮件发送配置信息service
	@Autowired
	private MerchantInfoService merchantInfoService;// 商家信息
	@Autowired
	private CustomerService customerService;// 客户
	


	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) {
		try {
			// 获取商家的下拉框
			MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
			merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
			List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
			// 获取供应商下拉框
			List<SelectBean> customerSelectBean = customerService.getAllSelectBeanBySupplier();
			model.addAttribute("merchantSelectBean", merchantSelectBean);
			model.addAttribute("customerSelectBean", customerSelectBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "/derp/main/email-list";
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long id) throws SQLException {
		// 获取商家的下拉框
		MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
		merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
		List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
		model.addAttribute("merchantSelectBean", merchantSelectBean);
		return "/derp/main/email-add";
	}

	/**
	 * 校验此商家和供应商是否存在
	 */
	@RequestMapping("/getSelectBeanBySupplier.asyn")
	@ResponseBody
	private ViewResponseBean getSelectBeanBySupplier(Long merchantId) {
		List<SelectBean> customerSelectBean =null;
		try {
			customerSelectBean = customerService.getSelectBeanBySupplier(merchantId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResponseFactory.success(customerSelectBean);
	}

	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		// 查询邮件发送配置详情
		EmailConfigDTO detail = emailConfigService.searchDetail(id);
		model.addAttribute("detail", detail);	
		return "/derp/main/email-details";
	}

	/**
	 * 访问编辑页面
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		// 查询邮件发送配置详情
		EmailConfigDTO detail = emailConfigService.searchDetail(id);
		// 获取商家的下拉框
		MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
		merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
		List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
		// 根据商家id获取供应商列表
		List<SelectBean> customerSelectBean = customerService.getSelectBeanBySupplier(detail.getMerchantId());
		model.addAttribute("customerSelectBean", customerSelectBean);
		model.addAttribute("merchantSelectBean", merchantSelectBean);
		model.addAttribute("detail", detail);		
		return "/derp/main/email-edit";
	}

	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listEmail.asyn")
	@ResponseBody
	private ViewResponseBean listEmail(EmailConfigDTO dto) {
		try {
			// 响应结果集
			dto = emailConfigService.listEmail(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 校验此商家和供应商是否存在
	 */
	@RequestMapping("/checkEmailExist.asyn")
	@ResponseBody
	private ViewResponseBean checkEmailExist(Long id) {
		/*int flag = 0;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			DepotInfoModel model = depotService.searchDetail(id);
			if(model.getType().equals("a")){
				flag = 1;
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}*/
		//return ResponseFactory.success(flag);
		return null;
	}

	/**
	 * 新增
	 */
	@RequestMapping("/saveEmail.asyn")
	@ResponseBody
	public ViewResponseBean saveEmail(EmailConfigModel model) {
		Map<String, Object> saveEmail=new HashMap<>();
		try {
			User user = ShiroUtils.getUser();
			// 存储信息
			model.setStatus(DERP_SYS.EMAILCONFIG_STATUS_1);//'状态(1启用,0禁用)  新增默认启用
			saveEmail = emailConfigService.saveEmail(model);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		}
		return ResponseFactory.success(saveEmail);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delEmail.asyn")
	@ResponseBody
	public ViewResponseBean delEmail(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			
			boolean b=emailConfigService.delEmail(list);
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
	@RequestMapping("/modifyEmail.asyn")
	@ResponseBody
	public ViewResponseBean modifyEmail(EmailConfigModel model) {
		Map<String, Object> modifyEmail=new HashMap<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(model.getId());
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}

			modifyEmail = emailConfigService.modifyEmail(model);

		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(modifyEmail);
	}
	
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 */
	@RequestMapping("/auditEmail.asyn")
	@ResponseBody
	public ViewResponseBean auditEmail(Long id , String status) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			boolean b = emailConfigService.audit(id,status);
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
	
	

	
}
