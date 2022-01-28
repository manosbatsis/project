package com.topideal.report.web.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.FileTaskDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 报表文件任务列表
 */
@Controller
@RequestMapping("/fileTask")
public class FileTaskController {
	
	
	@Autowired
	public FileTaskService fileTaskService;
	

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model)throws Exception  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		model.addAttribute("now", sdf.format(new Date()));
		return "derp/reporting/fileTask-list";
	}

    /**
     * 获取分页数据
     */
    @RequestMapping("/fileTaskList.asyn")
    @ResponseBody
    private ViewResponseBean fileTaskList(FileTaskDTO dto) {
        try {
            // 响应结果集
            User user = ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            dto.setUserId(user.getId());

            dto = fileTaskService.listFileTask(dto);
            return ResponseFactory.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}

	}

    /**
     * 导出
     */
    @RequestMapping("/downLoad.asyn")
    @ResponseBody
    private ViewResponseBean downLoad(String code, HttpServletResponse response) throws Exception {
        try {
            //查询任务
            FileTaskMongo task = fileTaskService.searchByCode(code);
            if (StringUtils.isEmpty(task.getPath())) {
                return null;
            }
            File filePath = new File(task.getPath());

            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode(task.getRemark() + ".zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
    		File[] sourceFiles = filePath.listFiles();
            if(sourceFiles!=null&&sourceFiles.length>0){
	            for(File file : sourceFiles){
		          	//获取模板,压缩到zip文件中
	            	InputStream in = new FileInputStream(file);
	            	zos.putNextEntry(new ZipEntry(file.getName()));
	        	    byte[] buffer = new byte[1024];
	        	    int r = 0;
	        	    while ((r = in.read(buffer)) != -1) {
	        	        zos.write(buffer, 0, r);
	        	    }
	        	    in.close();
	            }
            }
            zos.flush();
            zos.close();
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	
}
