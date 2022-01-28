/**  
 * @Copyright:Copyright Corporation 2016 
 * @Company:Guangdong Top Ideal SCM Service Group Co., Ltd. 
 * @Date 2016年9月24日 下午6:15:59  
 */
package com.topideal.common.tools;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** 
 * @desc: 文件导出工具类
 * @author Qin Dachang
 * @date 2016年9月24日 下午6:15:59  
 */
public class FileExportUtil {

	public static void exportExcelFile(HSSFWorkbook wb, HttpServletResponse response, HttpServletRequest request, String fileName){
		OutputStream out = null;
		try {
			setHeader(response, request, fileName +".pdf");
			out = response.getOutputStream();
            wb.write(out); 
		} catch (IOException e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void setHeader(HttpServletResponse response, HttpServletRequest request, String fileName) throws UnsupportedEncodingException {  
        response.reset();  
        // 设置为下载application/x-download  
        response.setContentType("application/x-download charset=UTF-8");  
        // 通常解决汉字乱码方法用URLEncoder.encode(...)  
        String filenamedisplay = URLEncoder.encode(fileName, "UTF-8");  
        filenamedisplay = filenamedisplay.replaceAll("\\+", "%20") ;
       /* if ("FF".equals(getBrowser(request))) {
            // 针对火狐浏览器处理方式不一样了  
            filenamedisplay = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }  */
        response.setHeader("Content-Disposition", "attachment;filename=" + filenamedisplay);  
    }  
  
    // 以下为服务器端判断客户端浏览器类型的方法  
    private static String getBrowser(HttpServletRequest request) {  
        String UserAgent = request.getHeader("USER-AGENT").toLowerCase();  
        if (UserAgent != null) {  
            if (UserAgent.indexOf("msie") >= 0)  
                return "IE";  
            if (UserAgent.indexOf("firefox") >= 0)  
                return "FF";  
            if (UserAgent.indexOf("safari") >= 0)  
                return "SF";  
        }  
        return null;
    }
    
    
	public static void export2007ExcelFile(SXSSFWorkbook wb, HttpServletResponse response, HttpServletRequest request, String fileName){
		OutputStream out = null;
		try {
			setHeader(response, request, fileName +".xlsx");
			out = response.getOutputStream();
            wb.write(out); 
		} catch (IOException e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
