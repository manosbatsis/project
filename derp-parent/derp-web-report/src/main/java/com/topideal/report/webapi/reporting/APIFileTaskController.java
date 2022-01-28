package com.topideal.report.webapi.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.FileTaskDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.FileTaskSearchFrom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/webapi/report/fileTask")
@Api(tags = "报表任务列表")
public class APIFileTaskController {
    @Autowired
    public FileTaskService fileTaskService;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listFileTask.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<FileTaskDTO> listFileTask(FileTaskSearchFrom form) {
        try {
            FileTaskDTO dto = new FileTaskDTO();
            BeanUtils.copyProperties(form, dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setUserId(user.getId());
            FileTaskDTO task = fileTaskService.listFileTask(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, task);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @ApiOperation(value = "导出", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value = "/downLoadFileTask.asyn")
    public void downLoadFileTask(String token, String code, HttpServletResponse response, HttpServletRequest request) throws SQLException {
        try {
            //查询任务
            FileTaskMongo task = fileTaskService.searchByCode(code);
            if (StringUtils.isEmpty(task.getPath())) {
                return;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
