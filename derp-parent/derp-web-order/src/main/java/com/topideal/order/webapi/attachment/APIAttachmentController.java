package com.topideal.order.webapi.attachment;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.DownloadUtil;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.order.service.attachment.AttachmentService;
import com.topideal.order.shiro.ShiroUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 附件管理控制器
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/order/attachment")
@Api(tags = "attachment-附件管理")
public class APIAttachmentController {

	private final static String[] EXT_NAMES = {"docx" , "xlsx" , "doc" , "xls" , "txt" , "pdf" , "jpg" , "png" , "bmp" , "jpeg" , "zip" , "rar", "eml","csv"}  ;

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 上传附件action
	 * @param file
	 * @param code
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@PostMapping("/uploadFiles.asyn")
	@ApiOperation(value = "上传附件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "附件关联单号", required = true)
	})
	public ResponseBean<Map<String, Object>> uploadFiles(
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true)  MultipartFile file,
			@RequestParam(value = "token", required = true) String token,
			String code) {

		if(file == null || StringUtils.isBlank(code)){
            //输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
        }

		//判断是否超出限制文件大小
		Long fileSize = file.getSize();
		if( fileSize > 20 * 1024 * 1024L ) {
	        return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "文件大小超出限制");
		}

		//判断是否符合上传文件类型
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
	        return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "不支持上传文件类型");
		}


		try {
			User user= ShiroUtils.getUserByToken(token) ;

			Map<String, Object> returnMap = null ;
			returnMap = attachmentService.uploadFile(file , code , user, true);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, returnMap) ;

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 控件列表查询附件信息
	 * @param code
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/listAttachment.asyn")
	@ApiOperation(value = "列表查询附件信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "附件关联单号,多个code,隔开", required = true)
	})
	public ResponseBean<PageMongo> listAttachment(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "code", required = true)String code) {

		if(StringUtils.isBlank(code)){
            //输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
        }

		PageMongo<AttachmentInfoMongo> page = new PageMongo<>() ;

		page = attachmentService.listAttachment(code) ;

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, page) ;
	}

	/**
	 * 逻辑删除附件信息
	 * @param attachmentCodes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/delAttachment.asyn")
	@ApiOperation(value = "逻辑删除附件信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "attachmentCodes", value = "附件关联单号,多条以','隔开", required = true)
	})
	public ResponseBean delAttachment(String attachmentCodes,
			@RequestParam(value = "token", required = true) String token) {

		if(StringUtils.isBlank(attachmentCodes)){
            //输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
        }

		String[] codeArr = attachmentCodes.split(",") ;

		try {
			attachmentService.delAttachment(codeArr) ;
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
	}

	/**
	 * 附件下载调用方法
	 * @param request
	 * @param response
	 * @param url
	 * @param fileName
	 */
	@GetMapping("/downloadFile.asyn")
	@ApiOperation(value = "附件下载调用方法")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "url", value = "附件地址", required = true),
			@ApiImplicitParam(name = "fileName", value = "附件名称", required = true),
	})
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			String url , String fileName) {

		if(StringUtils.isBlank(url)
				|| StringUtils.isBlank(fileName)){
            //输入信息不完整
			return ;
        }

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


	/**
	 * 附件批量下载调用方法
	 * @param response
	 * @param orderCode
	 * @param attachmentCodes
	 */
	@GetMapping("/downloadBatchFile.asyn")
	@ApiOperation(value = "附件下载调用方法")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "attachmentCodes", value = "附件编码，多个以英文逗号隔开", required = true),
			@ApiImplicitParam(name = "orderCode", value = "单据单号", required = true),
	})
	public void downloadBatchFile(HttpServletRequest request, HttpServletResponse response, String attachmentCodes, String orderCode) {

		if(StringUtils.isBlank(attachmentCodes) || StringUtils.isBlank(orderCode)){
			//输入信息不完整
			return ;
		}

		ZipOutputStream zos = null;
		try {
			List<AttachmentInfoMongo> attachmentInfoMongos = attachmentService.listByAttachmentCodes(attachmentCodes, orderCode);
			//转换中文否则可能会产生乱码
			String downloadFilename = URLEncoder.encode(orderCode + "附件.zip", "UTF-8");
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			// 设置在下载框默认显示的文件名
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			zos = new ZipOutputStream(response.getOutputStream());

			for (AttachmentInfoMongo attachment : attachmentInfoMongos) {
				byte[] bytes = DownloadUtil.loadFileByteFromURL(attachment.getAttachmentUrl());
				InputStream in = new ByteArrayInputStream(bytes);
				zos.putNextEntry(new ZipEntry(attachment.getAttachmentName()));
				byte[] buffer = new byte[1024];
				int r = 0;
				while ((r = in.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null)
				try {
					zos.flush();
					zos.close();
				} catch (IOException e) {

				}
		}

	}

	/**
	 * 上传附件action
	 * @param file
	 * @param code
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@PostMapping("/uploadFilesWithoutSave.asyn")
	@ApiOperation(value = "上传附件不存Mongo")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "附件关联单号", required = true)
	})
	public ResponseBean<Map<String, Object>> uploadFilesWithoutSave(
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true)  MultipartFile file,
			@RequestParam(value = "token", required = true) String token,
			String code) {

		if(file == null || StringUtils.isBlank(code)){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
		}

		//判断是否超出限制文件大小
		Long fileSize = file.getSize();
		if( fileSize > 20 * 1024 * 1024L ) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "文件大小超出限制");
		}

		//判断是否符合上传文件类型
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!Arrays.asList(EXT_NAMES).contains(ext.toLowerCase())) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "不支持上传文件类型");
		}


		try {
			User user= ShiroUtils.getUserByToken(token) ;

			Map<String, Object> returnMap = null ;
			returnMap = attachmentService.uploadFile(file , code , user, false);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, returnMap) ;

		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
}
