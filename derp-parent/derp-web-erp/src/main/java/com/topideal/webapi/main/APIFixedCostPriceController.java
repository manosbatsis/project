package com.topideal.webapi.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.service.main.FixedCostPriceService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.FixedCostPriceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/16 21:05
 * @Description: 固定成本价盘
 */
@RestController
@RequestMapping("/webapi/system/fixedCostPrice")
@Api(tags = "固定成本价盘管理")
public class APIFixedCostPriceController {

    private static final Logger LOG = Logger.getLogger(APIFixedCostPriceController.class);

    @Autowired
    private FixedCostPriceService service;

    @ApiOperation(value = "获取固定成本价盘分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listByPage(FixedCostPriceForm form) {
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(form.getToken())
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            FixedCostPriceDTO dto = new FixedCostPriceDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = service.listByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 导入
     * @param token
     * @param file
     * @return
     */
    @ApiOperation(value = "导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/importCostPrice.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean <UploadResponse> importCostPrice(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
        Map resultMap = new HashMap(); // 返回的结果集
        try {
            if (file == null) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcel(file.getInputStream(),
                    file.getOriginalFilename(), 1);
            // 数据为空
            if (data == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            resultMap = service.importCostPrice(data, user);
            Integer success = (Integer)resultMap.get("success");
            Integer failure = (Integer)resultMap.get("failure");
            Integer pass = (Integer)resultMap.get("pass");

            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "id集合,逗号分割", required = true)
    })
    @PostMapping("/delCostPrice.asyn")
    public ResponseBean delCostPrice(String token, String ids) {
        try {
            // 校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            service.delCostPrice(ids);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "单据id集合，多个用逗号隔开", required = true)
    })
    @PostMapping(value="/auditCostPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean auditCostPrice(String ids, String token) {
        try{
            if(ids == null || StringUtils.isBlank(ids)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            service.auditCostPrice(ids,user);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "导出")
    @GetMapping(value="/exportCostPrice.asyn")
    public void exportCostPrice(FixedCostPriceForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            FixedCostPriceDTO dto = new FixedCostPriceDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            int count = service.countByDTO(dto);

            if(count == 0) {
                return;
            }

            List<ExportExcelSheet> sheets = service.exportCostPrice(user,dto);

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, "固定成本价盘");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
