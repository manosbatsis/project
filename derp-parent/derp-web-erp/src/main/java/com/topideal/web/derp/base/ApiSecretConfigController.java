package com.topideal.web.derp.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiSecretConfigService;
import com.topideal.service.main.MerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * 接口配置 控制层
 * @author zhanghx
 */
@RequestMapping("/api")
@Controller
public class ApiSecretConfigController {

	//接口配置
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;
	//商家信息
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/api-list";
	}
	
	/**
	 * 访问新增页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model) throws Exception {
		List<SelectBean> list = merchantInfoService.getSelectBean(new MerchantInfoModel());
		model.addAttribute("merchantList", list);
		return "/derp/base/api-add";
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
		ApiSecretConfigDTO apiModel = apiSecretConfigService.getDetails(id);
		model.addAttribute("detail", apiModel);
		List<SelectBean> list = merchantInfoService.getSelectBean(new MerchantInfoModel());
		model.addAttribute("merchantList", list);
		return "/derp/base/api-edit";
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
		ApiSecretConfigDTO apiModel = apiSecretConfigService.getDetails(id);
		model.addAttribute("detail", apiModel);
		return "/derp/base/api-details";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/listApi.asyn")
	@ResponseBody
	private ViewResponseBean listApi(ApiSecretConfigDTO dto) {
		try{
			// 响应结果集
			dto = apiSecretConfigService.getListByPage(dto);
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
	@RequestMapping("/saveApi.asyn")
	@ResponseBody
	public ViewResponseBean saveApi(ApiSecretConfigModel model) {
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
			boolean b = apiSecretConfigService.saveApiSecret(model);
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
	 * 编辑
	 * @param model
	 * @return
	 */
	@RequestMapping("/modifyApi.asyn")
	@ResponseBody
	public ViewResponseBean modifyApi(ApiSecretConfigModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			boolean b = apiSecretConfigService.modify(model);
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
	@RequestMapping("/auditApi.asyn")
	@ResponseBody
	public ViewResponseBean auditApi(ApiSecretConfigModel model) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			boolean b = apiSecretConfigService.modify(model);
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
	@RequestMapping("/delApi.asyn")
	@ResponseBody
	public ViewResponseBean delApi(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<Long> list = StrUtils.parseIds(ids);
            boolean b = apiSecretConfigService.delApiSecretConfig(list);
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
	
}
