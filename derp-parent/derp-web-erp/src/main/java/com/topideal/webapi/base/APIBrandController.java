package com.topideal.webapi.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.service.base.BrandService;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌控制层
 * 
 * @author 杨创
 */
@RestController
@RequestMapping("/webapi/system/brand")
@Api(tags = "品牌列表")
public class APIBrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * 访问列表页面
	 */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/brand-list";
	}*/

	
	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "品牌名称")
	})
	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BrandDTO> list(String name,int begin,int pageSize) {
		try {
			BrandDTO dto=new BrandDTO();
			dto.setName(name);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = brandService.listByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
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
			@ApiImplicitParam(name = "name", value = "品牌名称",required = true),
			@ApiImplicitParam(name = "brandParentId", value = "标准品牌",required = true),
			@ApiImplicitParam(name = "englishBrandName", value = "英文名称 "),
			@ApiImplicitParam(name = "chinaBrandName", value = "中文名称 ")
	})
	@PostMapping(value="/save.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean save(String token,String name, Long brandParentId,String englishBrandName,String chinaBrandName) {
		try {
			User user = ShiroUtils.getUserByToken(token);

			boolean isNull = new EmptyCheckUtils().addObject(name).addObject(brandParentId).empty();
			if(isNull) {
				//未知异常
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			BrandModel model= new  BrandModel();
			model.setCreateDate(TimeUtils.getNow());
			model.setCreater(user.getId());
			model.setCreaterName(user.getName());
//			model.setOperator(user.getId());
//			model.setOperatorName(user.getName());
//			model.setOperationDate(TimeUtils.getNow());
			model.setEnglishBrandName(englishBrandName);
			model.setChinaBrandName(chinaBrandName);
			model.setName(name);
			model.setParentId(brandParentId);
			ResponseBean responseBean = brandService.save(user, model);
			//成功
			return responseBean;
		}catch (Exception e) {
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 获取下拉列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBean(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			List<Long> merchantIds = new ArrayList<Long>();
			merchantIds.add(user.getMerchantId());
			String relMerchantIds = user.getRelMerchantIds();
			if (!StringUtils.isEmpty(relMerchantIds)) {
				String[] relIdArr = relMerchantIds.split(",");
				if (relIdArr != null && relIdArr.length > 0) {
					for (String relId : relIdArr) {
						merchantIds.add(Long.valueOf(relId));
					}
				}
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchantIds", merchantIds);
			result = brandService.getSelectBeanByMerchant(paramMap);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取详情
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BrandModel> detail(Long id) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			BrandModel model = brandService.getDetails(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (Exception e) {
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
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "parentId", value = "标准品牌id", required = true)
	})
	@PostMapping(value="/modify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean modify(String token, Long id,Long parentId) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(parentId).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user = ShiroUtils.getUserByToken(token);
			BrandModel model=new BrandModel();
			model.setId(id);
			model.setParentId(parentId);
			model.setModifyDate(TimeUtils.getNow());
			brandService.modify(model,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 根据查询条件导出品牌信息
	 */
	@ApiOperation(value = "导出品牌信息",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "name", value = "品牌名称")
	})
	@GetMapping(value="/exportBrand.asyn")
	public void exportBrand(String name, HttpServletResponse response, HttpServletRequest request) throws SQLException {
		//品牌信息
		BrandModel model=new BrandModel();
		model.setName(name);
		List<Map<String,Object>> brandList = brandService.listForExport(model);

		//表头信息
		String sheetName = "品牌信息";
		String[] columns = {"品牌名称","中文品牌名称","英文品牌名称","标准品牌","操作人","修改时间"};
		String[] keys = {"name","chinaBrandName","englishBrandName","parentName","operatorName","modifyDate"};

		//生成表格
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, columns, keys, brandList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "品牌信息";
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
	}


}
