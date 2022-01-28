package com.topideal.order.web.filetemp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempCustomerRelModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;

import net.sf.json.JSONObject;

/**
 * 模版文件控制器
 * @author Guobs
 *
 */
@Controller
@RequestMapping("/fileTemp")
public class FileTempController {
	
	@Autowired
	FileTempService fileTempService ;
	
	@RequestMapping("toPage.html")
	public String toPage() {
		return "/derp/filetemp/filetemp-list" ;
	}
	
	@RequestMapping("toEditPage.html")
	public String toEditPage(Model model , String id) throws SQLException {
		
		if(!"null".equals(id) && StringUtils.isNotBlank(id)) {
			FileTempDTO detail = fileTempService.searchById(Long.valueOf(id)) ;
			model.addAttribute("detail", detail) ;
		}
		
		return "/derp/filetemp/filetemp-edit" ;
	}
	
	@RequestMapping("listFiletemp.asyn")
	@ResponseBody
	public ViewResponseBean listFiletemp(FileTempDTO dto, String customerIds) {
		try {
			User user = ShiroUtils.getUser();

//			if(!"1".equals(user.getUserType())){
//				dto.setMerchantId(user.getMerchantId());
//			}
			
			List<Long> customerIdList = new ArrayList<>();
			if (StringUtils.isNotBlank(customerIds)) {
				String[] split = customerIds.split(",");
				for (String str : split) {
					customerIdList.add(Long.valueOf(str));
				}
				dto.setCustomerIds(customerIdList);
			}
			dto = fileTempService.listFiletemp(dto) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 保存或修改
	 */
	@RequestMapping("saveOrModify.asyn")
	@ResponseBody
	public ViewResponseBean saveOrModify(FileTempModel model) {
		
		try {
			User user = ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model.setMerchantName(user.getMerchantName());
			fileTempService.saveOrModity(model) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}
		
		return ResponseFactory.success() ;
	}
	

	/**
	 * 获取模板适用客户
	 */
	@RequestMapping("/getRelCustomer.asyn")
	@ResponseBody
	public ViewResponseBean getRelCustomer(String fileId) {
		List<FileTempCustomerRelModel> resultList = new ArrayList<>();
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(fileId).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			resultList = fileTempService.listCustomerRel(Long.valueOf(fileId)) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}

		return ResponseFactory.success(resultList) ;
	}

	@RequestMapping("/saveCustomerRel.asyn")
	@ResponseBody
	public ViewResponseBean saveCustomerRel(String json) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			// 实例化JSON对象
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("customerRelList",Map.class);
			FileTempDTO dto = (FileTempDTO) JSONObject.toBean(jsonData, FileTempDTO.class,classMap);
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).empty();
			if (isNull) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			retMap = fileTempService.saveCustomerRel(dto);
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFactory.success(retMap);
	}

	@RequestMapping("/listFileTempInfo.asyn")
	@ResponseBody
	public ViewResponseBean listFileTempInfo(FileTempDTO dto, String customerIds) {
		try {
			User user = ShiroUtils.getUser();
			
			if(!"1".equals(user.getUserType())
					&& !"1".equals(dto.getType())){
				dto.setMerchantId(user.getMerchantId());
			}
			
			List<FileTempDTO> fileTempDTOs = fileTempService.listFileTempInfo(dto, customerIds) ;
			return ResponseFactory.success(fileTempDTOs);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
	}

	@RequestMapping("/toViewPage.html")
	public String toViewPage(Model model , String id) throws SQLException {

		if(!"null".equals(id) && StringUtils.isNotBlank(id)) {
			FileTempDTO detail = fileTempService.searchById(Long.valueOf(id)) ;
			model.addAttribute("detail", detail) ;
		}
		return "/derp/filetemp/filetemp-view" ;
	}

	/**
	 * 获取模板下拉
	 */
	@RequestMapping("/getFileTemp.asyn")
	@ResponseBody
	public ViewResponseBean getFileTemp(FileTempModel model) {
		List<FileTempModel> resultList = new ArrayList<>();
		try {
			resultList = fileTempService.getFileTemp(model);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}

		return ResponseFactory.success(resultList) ;
	}
	
}
