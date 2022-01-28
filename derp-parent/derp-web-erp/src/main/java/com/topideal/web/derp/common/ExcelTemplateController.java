package com.topideal.web.derp.common;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.enums.ExcelTemplateEnum;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 模板下载
 */
@RequestMapping("/excelTemplate")
@Controller
public class ExcelTemplateController {
	
	private static final Logger LOG = Logger.getLogger(ExcelTemplateController.class) ;
	
	/**
	 * 模板下载
	 * @param ids
	 * @param response
	 * @return
	 */
	/*@RequestMapping("/download.asyn")
	@ResponseBody
	public ViewResponseBean downloadTemplate(String ids, HttpServletResponse response) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode("模板.zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            String[] idArr = ids.split(",");
            for(String id : idArr){
	          	//获取模板,压缩到zip文件中
            	InputStream in = ExcelTemplateEnum.getFile(ExcelTemplateEnum.getPath(Integer.valueOf(id)));
            	zos.putNextEntry(new ZipEntry(ExcelTemplateEnum.getDesc(Integer.valueOf(id))));
        	    byte[] buffer = new byte[1024];
        	    int r = 0;
        	    while ((r = in.read(buffer)) != -1) {
        	        zos.write(buffer, 0, r);
        	    }
        	    in.close();
            }
            zos.flush();
            zos.close();
        }catch(NullPointerException e){
        	LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}*/
	/**新版下载单个模板文件
	 * excel
	 * */
	@RequestMapping("/download.asyn")
	@ResponseBody
	public ViewResponseBean downloadFile(String ids, HttpServletResponse response) {
		try {
			String fileName = URLEncoder.encode(ExcelTemplateEnum.getDesc(Integer.valueOf(ids)), "UTF-8");// 文件名
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

			InputStream in = ExcelTemplateEnum.getFile(ExcelTemplateEnum.getPath(Integer.valueOf(ids)));
			BufferedInputStream inb = new BufferedInputStream(in);
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];

			int i = inb.read(buffer);
			while (i != -1) {
				out.write(buffer, 0, i);
				i = inb.read(buffer);
			}
			out.close();
			inb.close();
			in.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}
		return null;
	}
}
