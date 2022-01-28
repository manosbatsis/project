package com.topideal.webapi.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.service.base.BrandParentService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 标准品牌控制层
 * @author 杨创
 */
@RestController
@RequestMapping("/webapi/system/brandParent")
@Api(tags = "标准品牌列表")
public class APIBrandParentController {

	@Autowired
	private BrandParentService brandParentService;


	/**
	 * 访问列表页面
	 * @throws SQLException 
	 */
	/*@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/base/brand-parent-list";
	}*/
	
	/**
	 * 访问导入页面
	 * @throws SQLException 
	 */
	/*@RequestMapping("/toImportPage.html")
	public String toImportPage() throws SQLException {
		return "/derp/base/brand-parent-import";
	}*/


	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "name", value = "标准品牌"),
			@ApiImplicitParam(name = "superiorParentBrandId", value = "上级母品牌id"),
			@ApiImplicitParam(name = "authorizer", value = "授权方"),
			@ApiImplicitParam(name = "type", value = "分类")
	})
	@GetMapping(value="/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, String name,
			Long superiorParentBrandId, String authorizer, String type) throws Exception{
		
		try {
			String sheetName = "标准品牌管理导出";
			/** 标题  */
			String[] COLUMNS= {"标准品牌","英文名称","上级母品牌名称","授权方","分类"};
			String[] KEYS= {"name","englishName","superiorParentBrand","authorizerLabel","typeLabel"};
			BrandParentDTO dto= new BrandParentDTO();
			dto.setName(name);
			dto.setSuperiorParentBrandId(superiorParentBrandId);
			dto.setAuthorizer(authorizer);
			dto.setType(type);
			List<BrandParentDTO> list = brandParentService.exportbrandParentList(dto);			
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
        	e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		
	}
	
	
	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "标准品牌"),
			@ApiImplicitParam(name = "superiorParentBrandId", value = "上级母品牌id"),
			@ApiImplicitParam(name = "authorizer", value = "授权方"),
			@ApiImplicitParam(name = "type", value = "分类")
	})
	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BrandParentDTO> list(String name,Long superiorParentBrandId,int begin,int pageSize,String authorizer, String type ) {
		try {	
			BrandParentDTO dto=new BrandParentDTO();
			dto.setName(name);
			dto.setSuperiorParentBrandId(superiorParentBrandId);
			dto.setAuthorizer(authorizer);
			dto.setType(type);			
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = brandParentService.listByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "name", value = "标准品牌名称",required = true),
			@ApiImplicitParam(name = "superiorParentBrandCodeEditOrAdd", value = "上级母品牌id",required = true),
			@ApiImplicitParam(name = "englishName", value = "英文名称 "),	
			@ApiImplicitParam(name = "authorizer", value = "授权方 "),
			@ApiImplicitParam(name = "type", value = "分类 ")
	})
	@PostMapping(value="/save.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean save(String token, String englishName,String name,String superiorParentBrandCodeEditOrAdd,String authorizer,String type) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			BrandParentModel model= new  BrandParentModel();
			model.setCreater(user.getId());
			model.setEnglishName(englishName);
			model.setName(name);
			if(StringUtils.isNotBlank(superiorParentBrandCodeEditOrAdd)) {
				model.setSuperiorParentBrandId(Long.valueOf(superiorParentBrandCodeEditOrAdd));
			}
			model.setAuthorizer(authorizer);
			model.setType(type);			
			brandParentService.save(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 编辑
	 */
	@ApiOperation(value = "编辑")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id",required = true),
			@ApiImplicitParam(name = "name", value = "标准品牌名称",required = true),
			@ApiImplicitParam(name = "superiorParentBrandCodeEditOrAdd", value = "上级母品牌id",required = true),
			@ApiImplicitParam(name = "oldName", value = "原来的标准品牌名称",required = true),
			@ApiImplicitParam(name = "englishName", value = "英文名称 "),	
			@ApiImplicitParam(name = "authorizer", value = "授权方 "),
			@ApiImplicitParam(name = "type", value = "分类 ")	
	})
	@PostMapping(value="/modify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean modify(Long id,String token,String englishName,String name,String oldName,String superiorParentBrandCodeEditOrAdd
			,String authorizer,String type) {
		try {
			User user = ShiroUtils.getUserByToken(token);	
			BrandParentModel model=new BrandParentModel();
			model.setId(id);
			model.setCreater(user.getId());
			if(StringUtils.isNotBlank(superiorParentBrandCodeEditOrAdd)) {
				model.setSuperiorParentBrandId(Long.valueOf(superiorParentBrandCodeEditOrAdd));
			}
			model.setEnglishName(englishName);
			model.setName(name);
			model.setAuthorizer(authorizer);
			model.setType(type);	
			brandParentService.modify(model, oldName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
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
			@ApiImplicitParam(name = "ids", value = "多个id用英文逗号隔开",required = true),
	})
	@PostMapping(value="/delete.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean delete(String ids) {
		try {
			List<Long> list = StrUtils.parseIds(ids);
			brandParentService.delete(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
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
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "superiorParentBrandId", value = "上级母品牌id",required = false),
		@ApiImplicitParam(name = "brandParentName", value = "标准品牌名称",required = false),
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBean(String token ,Long superiorParentBrandId,String brandParentName) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			BrandParentModel model = new BrandParentModel();
			model.setName(brandParentName);
			model.setSuperiorParentBrandId(superiorParentBrandId);
			
			result = brandParentService.getSelectBean(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取详情
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BrandParentModel> detail(Long id) {
		
		try {
			BrandParentModel model=new BrandParentModel();
			model.setId(id);
			model = brandParentService.detail(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importBrandParent.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importBrandParent(String token,
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true) MultipartFile file
			) throws IOException {
		
		
		try {
			Map resultMap = new HashMap();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = brandParentService.importBrandParent(data , user) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "根据标准条码获取标准品牌")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "commbarcode", value = "标准条码",required = true)
	})
	@PostMapping(value="/getBrandParent.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BrandParentModel> getBrandParent(String commbarcode) {
		try {
			BrandParentModel brandParentModel = brandParentService.getByCommbarcode(commbarcode);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,brandParentModel);//成功
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
}
