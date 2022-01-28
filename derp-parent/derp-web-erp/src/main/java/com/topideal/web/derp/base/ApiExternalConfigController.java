package com.topideal.web.derp.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.AppKeyGeneratorUtil;
import com.topideal.common.tools.AppSecretGeneratorUtil;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiExternalConfigService;
import com.topideal.service.main.MerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * 对外接口配置
 * @author lian_
 */
@RequestMapping("/apiExternal")
@Controller
public class ApiExternalConfigController {

	//外部接口配置
	@Autowired
	private ApiExternalConfigService service;
	//商家
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/api-external-list";
	}
	
	/**
	 * 访问新增页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, MerchantInfoModel merchant) throws Exception {
		return "/derp/base/api-external-add";
	}
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id, MerchantInfoModel merchant) throws Exception {
		ApiExternalConfigDTO apiModel = service.getDetails(id);
		model.addAttribute("detail", apiModel);
		return "/derp/base/api-external-edit";
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
		ApiExternalConfigDTO dto = service.getDetails(id);
		model.addAttribute("detail", dto);
		return "/derp/base/api-external-details";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/listApiExternal.asyn")
	@ResponseBody
	private ViewResponseBean listApiExternal(ApiExternalConfigDTO dto) {
		try{
			// 响应结果集
			dto = service.getListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveApiExternal.asyn")
	@ResponseBody
	public ViewResponseBean saveApiExternal(ApiExternalConfigModel model) {
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getMerchantId())
					.addObject(model.getAppKey())
					.addObject(model.getAppSecret())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			boolean b = service.saveApiExternal(model);
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
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delApiExternal.asyn")
	@ResponseBody
	public ViewResponseBean delApiExternal(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = service.delApiExternal(list);
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
	 * 编辑
	 * @param model
	 * @return
	 */
	@RequestMapping("/modifyApiExternal.asyn")
	@ResponseBody
	public ViewResponseBean modifyApiExternal(ApiExternalConfigModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            boolean b = service.modifyApiExternal(model);
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
	 * 禁用/启用
	 * @param model
	 * @return
	 */
	@RequestMapping("/auditApiExternal.asyn")
	@ResponseBody
	public ViewResponseBean auditApiExternal(ApiExternalConfigModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			boolean b = service.auditApiExternal(model);
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
	 * 得到随机app_key和秘钥
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppKeyAndAppSecret.asyn")
	@ResponseBody
	private ViewResponseBean getAppKeyAndAppSecret(String appKey, String appSecret) {
		ApiExternalConfigModel apiModel = new ApiExternalConfigModel();
		
		apiModel.setAppKey(AppKeyGeneratorUtil.KeyValue19()); //获取app_key
		apiModel.setAppSecret(AppSecretGeneratorUtil.KeyValue32());//获取秘钥
		
		return ResponseFactory.success(apiModel);
	}
	
	
}
