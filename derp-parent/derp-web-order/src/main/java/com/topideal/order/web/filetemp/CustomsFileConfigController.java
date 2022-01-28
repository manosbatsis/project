package com.topideal.order.web.filetemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.order.service.filetemp.CustomsFileConfigService;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 清关单证模板配置控制器
 *
 */
@Controller
@RequestMapping("/customsFileConfig")
public class CustomsFileConfigController {
	
	@Autowired
	CustomsFileConfigService customsFileConfigService ;
	@Autowired
	FileTempService fileTempService;
	
	@RequestMapping("toPage.html")
	public String toPage() {
		return "/derp/filetemp/customs-file-config-list" ;
	}
	
	@RequestMapping("listCustomsFileConfig.asyn")
	@ResponseBody
	public ViewResponseBean listCustomsFileConfig(CustomsFileConfigDTO dto) {
		try {			
			dto = customsFileConfigService.listDTOByPage(dto) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 保存或修改
	 */
	@RequestMapping("saveOrModify.asyn")
	@ResponseBody
	public ViewResponseBean saveOrModify(String json) {		
		Map<String, String> result = new HashMap<String, String>();
		try {
			User user = ShiroUtils.getUser();
			result = customsFileConfigService.saveOrModity(json,user) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}
		
		return ResponseFactory.success(result) ;
	}
	

	/**
	 * 获取编辑页面关联仓库列表信息
	 */
	@RequestMapping("/listDepotRel.asyn")
	@ResponseBody
	public ViewResponseBean listDepotRel(Long fileId) {
		List<CustomsFileDepotRelDTO> resultList = new ArrayList<>();
		try {			
			resultList = customsFileConfigService.listDepotRel(fileId);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}

		return ResponseFactory.success(resultList) ;
	}
	/**
	 * 删除
	 */
	@RequestMapping("/delCustomsFileConfig.asyn")
	@ResponseBody
	public ViewResponseBean delCustomsFileConfig(String ids) {		
		try {
			 //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
			
            customsFileConfigService.delCustomsFileConfig(list);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}

		return ResponseFactory.success() ;
	}
	/**
	 * 获取导出清关模板
	 */
	@RequestMapping("/getExportTemplate.asyn")
	@ResponseBody
	public ViewResponseBean getExportTemplate(String json) {		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			
            if(StringUtils.isBlank(json)){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
           
            result = customsFileConfigService.getExportTemplate(json);
            
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}

		return ResponseFactory.success(result) ;
	}
}
