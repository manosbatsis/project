package com.topideal.web.derp.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.service.main.NoticeService;
import com.topideal.shiro.ShiroUtils;

/**
 *  公告管理控制器
 * @author gy
 *
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	private final static String[] EXT_NAMES = { "jpg", "png", "bmp", "jpeg", "gif", "JPG", "PNG", "BMP", "JPEG","GIF"}  ; 
	
	@Autowired
	private NoticeService noticeService ;

	@RequestMapping("toPage.html")
	public String toPage() {
		return "/derp/main/notice-list" ;
	}
	
	@RequestMapping("toEditPage.html")
	public String toEditPage(Model model , Long id) throws SQLException {
		
		if(id != null) {
			NoticeModel detail = noticeService.searchById(id) ;
			model.addAttribute("detail", detail) ;
		}
		
		return "/derp/main/notice-edit" ;
	}
	
	@RequestMapping("listNotice.asyn")
	@ResponseBody
	public ViewResponseBean listNotice(NoticeDTO dto) {
		try {
			dto = noticeService.listNotice(dto) ;
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
	public ViewResponseBean saveOrModify(NoticeModel model) {
		
		if(model.getTitle().length() > 50) {
			throw new RuntimeException("标题不能超过50字") ;
		}
		
		try {
			noticeService.saveOrModity(model) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 ,e) ;
		}
		
		return ResponseFactory.success() ;
	}
	
	/**
	 * 图片上传
	 * @param file
	 * @return
	 */
	@RequestMapping("uploadFile.aysn")
	@ResponseBody
	public Object uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
		
		//判断是否符合上传文件类型
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
			ViewResponseBean responseBean=new ViewResponseBean();
	        responseBean.setState(415);
	        responseBean.setMessage("不支持上传文件类型");
	        return responseBean;
		}
		
		try {
			Map<String, Object> map = noticeService.uploadFile(file);
			return map ;
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
	}
	
	@RequestMapping("getDetail.asyn")
	@ResponseBody
	public ViewResponseBean getDetail(Long id) {
		try {
			NoticeDTO dto = noticeService.searchDTOById(id) ;
			return ResponseFactory.success(dto) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
	}
	
	/**
	 * 修改状态 发布，删除
	 * @param model
	 * @return
	 */
	@RequestMapping("modifyStatus.asyn")
	@ResponseBody
	public ViewResponseBean modifyStatus(NoticeModel model) {
		try {
			noticeService.modifyStatus(model) ;
			return ResponseFactory.success();
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302 ,e) ;
		}
	}
	
	/**
	 *  根据登录用户获取公告
	 */
	@RequestMapping("getNoticeByUser.aysn")
	@ResponseBody
	public ViewResponseBean getNoticeByUser(String pageNo) {
		
		User user = ShiroUtils.getUser();
		
		try {
			List<NoticeDTO> noticeList = noticeService.getNoticeByUser(user,pageNo);
			Integer account = noticeService.getNoticeAccountByUser(user) ;
			Integer unReadAmount = noticeService.getUnReadAmountByUser(user) ;
			
			Map<String , Object> resultMap = new HashMap<String, Object>() ;
			resultMap.put("noticeList", noticeList) ;
			resultMap.put("unReadAmount", unReadAmount) ;
			resultMap.put("account", account) ;
			
			return ResponseFactory.success(resultMap) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302);
		}
		
	}
	
	/**
	 * 修改已读状态
	 */
	@RequestMapping("changeReadStatus.aysn")
	@ResponseBody
	public ViewResponseBean changeReadStatus(NoticeUserRelModel relModel) {
		
		User user = ShiroUtils.getUser();
		
		relModel.setUserId(user.getId());
		
		try {
			noticeService.changeReadStatus(relModel) ;
			Integer unReadAmount = noticeService.getUnReadAmountByUser(user);
			
			return ResponseFactory.success(unReadAmount) ;
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
	}
	
	@RequestMapping("getAroundNotice.asyn")
	@ResponseBody
	public ViewResponseBean getAroundNotice(Long id) {
		
		try {
			Map<String , Object> map = noticeService.getAroundNotice(id) ;
			
			return ResponseFactory.success(map) ;
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
	}
}
