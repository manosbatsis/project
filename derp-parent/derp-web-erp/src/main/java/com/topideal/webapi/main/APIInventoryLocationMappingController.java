/*
package com.topideal.webapi.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
import com.topideal.entity.vo.main.InventoryLocationMappingModel;
import com.topideal.service.main.InventoryLocationMappingService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.InventoryLocationMappingDetailForm;
import com.topideal.webapi.form.InventoryLocationMappingForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

*/
/**
 * 库位映射表
 * @author Gy
 *
 *//*


@RestController
@RequestMapping("/webapi/system/inventoryLocationMapping")
@Api(tags = "库位映射 ")
public class APIInventoryLocationMappingController {
	
	private static final Logger LOG = Logger.getLogger(APIInventoryLocationMappingController.class) ;

	private static final String[] MAIN_COLUMNS = {"公司", "原货号", "商品名称", "库位货号", "库位类型","是否指定唯一", "创建人", "创建时间",
			"修改人", "修改时间"} ;
	
	private static final String[] MAIN_KEYS = {"merchantName", "originalGoodsNo", "goodsName", "goodsNo", "typeLabel","isorRappointLabel", "createName", "createDate",
					"modifyName", "modifyDate"} ;
	
	@Autowired
	InventoryLocationMappingService service ;
	
	*/
/**
	 * 访问列表页面
	 * @throws SQLException 
	 *//*

	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token) throws SQLException {
		try {
			User user = ShiroUtils.getUserByToken(token);
	        List<SelectBean> merchantList = service.getMerchantList(user);
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantList);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	

	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id"),
			@ApiImplicitParam(name = "originalGoodsNo", value = "原货号"),
			@ApiImplicitParam(name = "type", value = "库位类型")
	})
	@PostMapping(value="/listInventoryLocationMapping.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean listInventoryLocationMapping(Long merchantId,String originalGoodsNo,
			String type,int begin,int pageSize) {		
		try {
			InventoryLocationMappingDTO dto=new InventoryLocationMappingDTO();
			dto.setMerchantId(merchantId);
			dto.setOriginalGoodsNo(originalGoodsNo);
			dto.setType(type);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = service.listInventoryLocationMapping(dto) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportInventoryLocationMapping.asyn")
	public ResponseBean exportInventoryLocationMapping(HttpServletResponse response, 
			HttpServletRequest request,Long merchantId,String originalGoodsNo,
					String type, String ids) throws Exception {
		try {
			InventoryLocationMappingDTO dto=new InventoryLocationMappingDTO();
			dto.setMerchantId(merchantId);
			dto.setOriginalGoodsNo(originalGoodsNo);
			dto.setType(type);
			List<InventoryLocationMappingDTO> exportList = service.exportInventoryLocationMapping(dto);
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("库位映射表", MAIN_COLUMNS, MAIN_KEYS, exportList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, "库位映射表导出");
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}		
	}
	
	@ApiOperation(value = "新增/修改")
	@PostMapping(value="/saveOrModify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrModify(InventoryLocationMappingForm form) {		
		try {
			InventoryLocationMappingModel model=new InventoryLocationMappingModel();
			model.setMerchantId(form.getMerchantId());
			model.setOriginalGoodsNo(form.getOriginalGoodsNo());
			model.setGoodsNo(form.getGoodsNo());
			model.setType(form.getType());
			model.setId(form.getId());			
			User user = ShiroUtils.getUserByToken(form.getToken());
			service.saveOrModify(model,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	*/
/**
	 * 访问详情页面
	 * @throws SQLException 
	 *//*

	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean detail(String id) throws SQLException {
		
		InventoryLocationMappingModel model;
		try {
			model = service.seachById(Long.valueOf(id));
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	*/
/**
	 * 删除
	 * @throws SQLException 
	 *//*

	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/deleteInventoryLocationMapping.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean deleteInventoryLocationMapping(String ids) throws SQLException {
		
		try {
			
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = service.deleteInventoryLocationMapping(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	*/
/**
	 * 访问导入页面
	 * @throws SQLException 
	 *//*

	*/
/*@RequestMapping("/toImportPage.html")
	public String toImportPage() throws SQLException {
		return "/derp/main/inven-location-mapping-import";
	}*//*

	
	*/
/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 *//*


	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importInventoryLocationMapping.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importInventoryLocationMapping(String token,@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集		
		if (file == null) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try {
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
		
			resultMap = service.importInventoryLocationMapping(data , user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	

	@ApiOperation(value = "获取原货号")
	@PostMapping(value="/getOriginalGoodsId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getOriginalGoodsId(InventoryLocationMappingDetailForm form) throws IOException {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			InventoryLocationMappingDTO dto=new InventoryLocationMappingDTO();
			dto.setGoodsNo(form.getGoodsNo());
			dto.setId(form.getId());
			dto.setIds(form.getIds());
			dto.setMerchantId(form.getMerchantId());
			dto.setMerchantName(form.getMerchantName());
			dto.setGoodsId(form.getGoodsId());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setGoodsName(form.getGoodsName());
			dto.setOriginalGoodsNo(form.getOriginalGoodsNo());
			dto.setType(form.getType());
			dto.setOriginalGoodsId(form.getOriginalGoodsId());
			dto.setIsorRappoint(form.getIsorRappoint());
			dto = service.getOriginalGoodsId(user,dto) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}

	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/updateIsorRappoint.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private	ResponseBean updateIsorRappoint(Long id){
		Map<String, Object> updateIsorRappointMap= new HashMap<>();
		try {
			updateIsorRappointMap = service.updateNotSettlement(id);		  
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,updateIsorRappointMap);//成功
     }catch(Exception e){
		e.printStackTrace();
		return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
     }

	}
	
}
*/
