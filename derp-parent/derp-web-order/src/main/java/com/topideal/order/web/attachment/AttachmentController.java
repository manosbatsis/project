package com.topideal.order.web.attachment;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.DownloadUtil;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.order.service.attachment.AttachmentService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * 附件管理控制器
 * @author gy
 *
 */
@Controller
@RequestMapping("attachment")
public class AttachmentController {
	
	private final static String[] EXT_NAMES = {"docx" , "xlsx" , "doc" , "xls" , "txt" , "pdf" , "jpg" , "png" , "bmp" , "jpeg" , "zip" , "rar", "eml","csv"}  ;
	
	@Autowired
	private AttachmentService attachmentService; 

	@RequestMapping("/toPage.html")
	public String toPage(Model model , String code) {
		model.addAttribute("code", code) ;
		
		return "/derp/attachment/attachment-details" ;
	}
	
	/**
	 * 上传附件action
	 * @param file
	 * @param code
	 * @return
	 */
	@RequestMapping("/uploadFiles.asyn")
	@ResponseBody
	public ViewResponseBean uploadFiles(@RequestParam(value = "file", required = false) MultipartFile file,String code) {
		if(file == null || StringUtils.isBlank(code)){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
		
		//判断是否超出限制文件大小
		Long fileSize = file.getSize();
		if( fileSize > 20 * 1024 * 1024L ) {
			ViewResponseBean responseBean=new ViewResponseBean();
	        responseBean.setState(413);
	        responseBean.setMessage("文件大小超出限制");
	        return responseBean;
		}
		
		//判断是否符合上传文件类型
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
			ViewResponseBean responseBean=new ViewResponseBean();
	        responseBean.setState(415);
	        responseBean.setMessage("不支持上传文件类型");
	        return responseBean;
		}
        
		User user= ShiroUtils.getUser(); 
		
		Map<String, Object> returnMap = null ;
		try {
			returnMap = attachmentService.uploadFile(file , code , user, true);
		} catch (Exception e) {
			ResponseFactory.error(StateCodeEnum.ERROR_303) ;
		} 
		
		return ResponseFactory.success(returnMap) ;
	}
	
	/**
	 * 控件列表查询附件信息
	 * @param code
	 * @return
	 */
	@RequestMapping("/listAttachment.asyn")
	@ResponseBody
	public ViewResponseBean listAttachment(String code) {
		
		if(StringUtils.isBlank(code)){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
		
		PageMongo<AttachmentInfoMongo> page = new PageMongo<>() ;
		
		page = attachmentService.listAttachment(code) ;
		
		return ResponseFactory.success(page) ;
	}
	
	/**
	 * 逻辑删除附件信息
	 * @param attachmentCodes
	 * @return
	 */
	@RequestMapping("/delAttachment.asyn")
	@ResponseBody
	public ViewResponseBean delAttachment(String attachmentCodes) {
		
		if(StringUtils.isBlank(attachmentCodes)){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
		
		String[] codeArr = attachmentCodes.split(",") ;
		
		try {
			attachmentService.delAttachment(codeArr) ;
		} catch (Exception e) {
			ResponseFactory.error(StateCodeEnum.ERROR_301) ;
		}
		
		return ResponseFactory.success() ;
	}
	
	/**
	 * 附件下载调用方法
	 * @param request
	 * @param response
	 * @param url
	 * @param fileName
	 */
	@RequestMapping("/downloadFile.asyn")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,String url , String fileName) {
		
		BufferedOutputStream bos = null;
		
		try {
		
			byte[] bytes = DownloadUtil.loadFileByteFromURL(url);
			
			response.setContentType("application/x-msdownload;");
	        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));

			bos = new BufferedOutputStream(response.getOutputStream());
		
			bos.write(bytes);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	        if (bos != null)
	            try {
	                bos.close();
	            } catch (IOException e) {
	                
	            }
	    }
	}
}
