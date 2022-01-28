package com.topideal.webapi.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.service.main.NoticeService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.NoticeAddForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 *  公告管理控制器
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/system/notice")
@Api(tags = "公告管理")
public class APINoticeController {
	
	private final static String[] EXT_NAMES = { "jpg", "png", "bmp", "jpeg", "gif", "JPG", "PNG", "BMP", "JPEG","GIF"}  ; 
	
	@Autowired
	private NoticeService noticeService ;

	/*@RequestMapping("toPage.html")
	public String toPage() {
		return "/derp/main/notice-list" ;
	}*/
	
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(Long id) throws SQLException {
		try {
			if(id != null) {
				NoticeModel detail = noticeService.searchById(id) ;
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,detail);//成功
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "type", value = "公告类型"),
			@ApiImplicitParam(name = "status", value = "状态"),			
			@ApiImplicitParam(name = "publishStartDateStr", value = "发布时间开始"),
			@ApiImplicitParam(name = "publishEndDateStr", value = "发布时间结束")	
	})
	@PostMapping(value="/listNotice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean listNotice(String type,String status,String publishStartDateStr,
			String publishEndDateStr,int begin,int pageSize) {
		try {
			NoticeDTO dto=new NoticeDTO();
			dto.setType(type);
			dto.setStatus(status);
			dto.setPublishStartDateStr(publishStartDateStr);
			dto.setPublishEndDateStr(publishEndDateStr);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = noticeService.listNotice(dto) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 保存或修改
	 */
	@ApiOperation(value = "新增或修改")
	@PostMapping(value="/saveOrModify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrModify(NoticeAddForm form) {
		try {
			NoticeModel model=new NoticeModel();
			model.setTitle(form.getTitle());
			model.setContentBody(form.getContentBody());
			model.setType(form.getType());
			model.setId(form.getId());
			if(model.getTitle().length() > 50) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"标题不能超过50字");
			}
			noticeService.saveOrModity(model) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 图片上传
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "图片上传")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/uploadFile.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
		
		//判断是否符合上传文件类型
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
	        return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"不支持上传文件类型");
		}
		
		try {
			Map<String, Object> map = noticeService.uploadFile(file);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);//成功
		} catch (IOException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	@ApiOperation(value = "获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "type", value = "公告类型"),
			@ApiImplicitParam(name = "status", value = "状态"),			
			@ApiImplicitParam(name = "publishStartDateStr", value = "发布时间开始"),
			@ApiImplicitParam(name = "publishEndDateStr", value = "发布时间结束")	
	})
	@PostMapping(value="/getDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getDetail(Long id) {
		try {
			NoticeDTO dto = noticeService.searchDTOById(id) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	/**
	 * 修改状态 发布，删除
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "修改状态 发布，删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "status", value = "状态", required = true)			
	})
	@PostMapping(value="/modifyStatus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyStatus(Long id,String status) {
		try {
			NoticeModel model=new NoticeModel();
			model.setId(id);
			model.setStatus(status);
			noticeService.modifyStatus(model) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 *  根据登录用户获取公告
	 */
	@ApiOperation(value = "根据登录用户获取公告")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "pageNo", value = "pageNo", required = true)	
	})
	@PostMapping(value="/getNoticeByUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getNoticeByUser(String token,String pageNo) {

		try {
			User user = ShiroUtils.getUserByToken(token);
			List<NoticeDTO> noticeList = noticeService.getNoticeByUser(user,pageNo);
			Integer account = noticeService.getNoticeAccountByUser(user) ;
			Integer unReadAmount = noticeService.getUnReadAmountByUser(user) ;
			
			Map<String , Object> resultMap = new HashMap<String, Object>() ;
			resultMap.put("noticeList", noticeList) ;
			resultMap.put("unReadAmount", unReadAmount) ;
			resultMap.put("account", account) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	/**
	 * 修改已读状态
	 */
	@ApiOperation(value = "修改公告为已读状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "pageNo", value = "noticeId", required = true)	
	})
	@PostMapping(value="/changeReadStatus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean changeReadStatus(String token,Long noticeId) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			NoticeUserRelModel relModel=new NoticeUserRelModel();
			relModel.setNoticeId(noticeId);
			relModel.setUserId(user.getId());			
			noticeService.changeReadStatus(relModel);
			Integer unReadAmount = noticeService.getUnReadAmountByUser(user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,unReadAmount);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	

	@ApiOperation(value = "获取上一条，下一条公告")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "公告id", required = true)	
	})
	@PostMapping(value="/getAroundNotice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getAroundNotice(Long id) {		
		try {
			Map<String , Object> map = noticeService.getAroundNotice(id) ;			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
}
