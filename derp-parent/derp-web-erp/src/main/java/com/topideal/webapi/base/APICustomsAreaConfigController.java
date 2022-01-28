package com.topideal.webapi.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.service.base.CustomsAreaConfigService;
import com.topideal.service.main.DepotService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.CustomsAreaConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 关区配置 Controller
 */
@RequestMapping("/webapi/system/customsArea")
@RestController
@Api(tags = "关区配置列表")
public class APICustomsAreaConfigController {

    @Autowired
    private CustomsAreaConfigService customsAreaConfigService;

	// 仓库信息service
	@Autowired
	private DepotService depotService;
    /**
     * 获取分页数据
     * @return
     */
    @ApiOperation(value = "获取列表分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "begin", value = "开始位置", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
            @ApiImplicitParam(name = "code", value = "关区代码"),
            @ApiImplicitParam(name = "name", value = "平台关区")
    })
    @PostMapping(value="/listCustomsArea.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<CustomsAreaConfigDTO> listCustomsArea(String token,String code,String name,int begin,int pageSize) throws Exception{
        CustomsAreaConfigDTO dto=new CustomsAreaConfigDTO();
        User user = ShiroUtils.getUserByToken(token);
        try {
            dto.setCode(code);
            dto.setName(name);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = customsAreaConfigService.getListByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 新增关区配置
     * @return
     */
    @ApiOperation(value = "新增关区配置信息")
    @PostMapping(value="/saveCustomsArea.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveCustomsArea(CustomsAreaConfigForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            CustomsAreaConfigModel model=new CustomsAreaConfigModel();
            model.setName(form.getName());
            model.setCode(form.getCode());
            model.setCreateName(user.getName());
            model.setCreater(user.getId());
            Map<String, Object> map = customsAreaConfigService.saveCustomsArea(model);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
    })
    @PostMapping(value="/delCustoms.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delCustoms(String ids) {
        try{
            Map<String,Object> map=customsAreaConfigService.delReptile(ids);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,message);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 根据查询条件导出
     */
    @ApiOperation(value = "导出关区配置信息",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportCustomsArea.asyn")
    public void exportCustomsArea(@RequestParam(value = "token",required = true)String token,
                                          @RequestParam(value = "code",required = false)String code,
                                          @RequestParam(value = "name",required = false)String name ,
                                          HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //表头信息
            CustomsAreaConfigModel customsAreaConfigModel=new CustomsAreaConfigModel();
            User user = ShiroUtils.getUserByToken(token);
            customsAreaConfigModel.setName(name);
            customsAreaConfigModel.setCode(code);
            List<CustomsAreaConfigModel> rateList = customsAreaConfigService.listForExport(customsAreaConfigModel);

            //表头信息
            String sheetName = "关区配置信息";
            String[] columns = {"关区代码","平台关区"};
            String[] keys = {"code","name"};

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList8(sheetName, columns, keys, rateList);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, "关区配置信息");
            //return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            //return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 导入
     * @param file
     * @throws IOException
     */
    @ApiOperation(value = "导入关区配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/importCustomsArea.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importCustomsArea(String token,
                                          @ApiParam(value = "上传的文件", required = true)
                                          @RequestParam(value = "file", required = true) MultipartFile file
    ) throws IOException {
        try {
            if (file == null) { // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
                    file.getOriginalFilename(), 1);
            if (data == null) {// 数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
            }
            User user = ShiroUtils.getUserByToken(token);
            Map resultMap = customsAreaConfigService.importCustomsArea(data , user);

            Integer success = (Integer)resultMap.get("success");
            Integer failure = (Integer)resultMap.get("failure");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
        } catch (SQLException e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    /**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getSelectBean.asyn")
	public ResponseBean<List<SelectBean>> getSelectBean(String token,Long depotId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			DepotCustomsRelModel model = new DepotCustomsRelModel();
			model.setDepotId(depotId);
			result = customsAreaConfigService.getSelectBean(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	 
	@ApiOperation(value = "获取关区下拉列表")
		@ApiImplicitParams({
				@ApiImplicitParam(name = "token", value = "令牌")
		})
	@PostMapping(value="/getCustomsSelectBean.asyn")
	public ResponseBean<List<SelectBean>> getCustomsSelectBean(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = customsAreaConfigService.getCustomsSelectBean();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	@ApiOperation(value = "获取仓库关区数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌")
	})
	@PostMapping(value="/getDepotCustomsRel.asyn")
	public ResponseBean<DepotCustomsRelModel> getDepotCustomsRel(String token,Long depotId) {
	try {
		DepotCustomsRelModel depotCustomsRel=new DepotCustomsRelModel();
		depotCustomsRel.setDepotId(depotId);
		List<DepotCustomsRelModel> depotCustomsRelList = depotService.getDepotCustomsRel(depotCustomsRel);
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,depotCustomsRelList);//成功
	} catch (Exception e) {
		e.printStackTrace();
		return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
	}
}
}
