package com.topideal.order.webapi.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.order.service.common.PlatformSettlementConfigService;
import com.topideal.order.service.common.SettlementConfigService;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.PlatformSettlementConfigForm;
import io.swagger.annotations.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台费用映射表
 */
@RequestMapping("/webapi/order/platformSettlementConfig")
@RestController
@Api(tags = "平台费用映射表")
public class APIPlatformSettlementConfigController {

	@Autowired
	private PlatformSettlementConfigService platformSettlementConfigService;
	@Autowired
	private SettlementConfigService settlementConfigService;
	@Autowired
	private ReceivePaymentSubjectService receivePaymentSubjectService;


	/**
	 * 访问列表页面
	 * @throws SQLException
	 * */
	@ApiOperation(value = "访问列表页面")
	@PostMapping(value = "/toPage.html", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token) throws SQLException {
		try {
			Map<String,Object> result = new HashMap<>();
			//获取 已有维护且启用状态的二级费项名称 (下拉框)
			SettlementConfigModel settlementConfig= new SettlementConfigModel();
			settlementConfig.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
			settlementConfig.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
			List<SettlementConfigModel> settlementConfigList = settlementConfigService.getByModelList(settlementConfig);
			result.put("settlementConfigList", settlementConfigList);
			//获取NC收支费项科目表已有维护且启用状态的NC收支费项(下拉框)
			List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
			result.put("receivePaymentSubjectList", receivePaymentSubjectList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	/**
	 * 新增/编辑获取下拉框
	 * @param model
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "新增/编辑获取下拉框")
	@PostMapping(value ="/getAllSelectBean.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getNcReceivePayment() throws Exception{
		try{
			Map<String, Object>map=new HashMap<>();
			SettlementConfigModel settlementConfig= new SettlementConfigModel();
			settlementConfig.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
			settlementConfig.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
			List<SettlementConfigModel> selectBeanByList = settlementConfigService.getByModelList(settlementConfig);
			map.put("settlementConfigList", selectBeanByList);
			//获取NC收支费项科目表已有维护且启用状态的NC收支费项(下拉框)
			/*List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
			map.put("receivePaymentSubjectList", receivePaymentSubjectList);*/
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	@ApiOperation(value = "访问详情页面")
	@PostMapping(value ="/toDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(String token, Long id) throws Exception{
		//查询所有商家
		PlatformSettlementConfigModel model=new PlatformSettlementConfigModel();
		try {
			model.setId(id);
			model = platformSettlementConfigService.searchDetail(model);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);//成功
	}

	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value ="/platformSettlementConfigList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean settlementConfigList(PlatformSettlementConfigForm form) {
		PlatformSettlementConfigDTO dto = new PlatformSettlementConfigDTO();
		try{
			BeanUtils.copyProperties(form, dto);
			// 响应结果集
			dto = platformSettlementConfigService.getPlatSettlementListByPage(dto);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
	}

	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@PostMapping(value = "/saveSettlementConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveSettlement(PlatformSettlementConfigForm form) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			PlatformSettlementConfigModel model = new PlatformSettlementConfigModel();
			BeanUtils.copyProperties(form, model);

			User user= ShiroUtils.getUserByToken(form.getToken());
			String name = model.getName();
			name=name.trim();
			model.setName(name);
			model.setCreater(user.getId());
			model.setCreaterName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			retMap = platformSettlementConfigService.saveSettlement(model);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);//成功
	}

	/**
	 * 编辑
	 * */
	@ApiOperation(value = "编辑")
	@PostMapping(value = "/modifySettlementConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifySettlement(PlatformSettlementConfigForm form) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "编辑成功");
		try{
			PlatformSettlementConfigModel model = new PlatformSettlementConfigModel();
			BeanUtils.copyProperties(form, model);

			User user= ShiroUtils.getUserByToken(form.getToken());
			String name = model.getName();
			name=name.trim();
			model.setName(name);
			model.setModifier(user.getId());
			model.setModifierName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			retMap=platformSettlementConfigService.modifySettlement(model,form.getOldName());
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);//成功
	}






	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@ApiOperation(value = "导出客户信息",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value ="/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, PlatformSettlementConfigForm form) throws Exception{
		PlatformSettlementConfigDTO dto = new PlatformSettlementConfigDTO();

		BeanUtils.copyProperties(form, dto);
		/** 标题  */
		String[] COLUMNS= {"平台名称","平台费项名称","本级费项名称","NC收支费项","状态","编辑时间","编辑人"};
		String[] KEYS= {"storePlatformName","name","settlementConfigName","ncPaymentName","statusLabel","modifyDate","modifierName"};


		List<PlatformSettlementConfigDTO> list = platformSettlementConfigService.exportSettlementList(dto);
		String sheetName = "平台费用项映射";


		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 启用禁用
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "编辑")
	@PostMapping(value = "/isOrNotEnable.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean isOrNotEnable(PlatformSettlementConfigForm form) {
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getId())
					.addObject(form.getStatus())
					.empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user= ShiroUtils.getUserByToken(form.getToken());
			PlatformSettlementConfigModel model=new PlatformSettlementConfigModel();
			model.setId(form.getId());
			model.setStatus(form.getStatus());
			model.setModifierName(user.getName());
			model.setModifier(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			// 响应结果集
			boolean b = platformSettlementConfigService.isOrNotEnable(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);//未知异常
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
	}
	/**
	 * 导入
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@GetMapping(value = "/import.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean platformMerchandiseImport(String token,
												  @ApiParam(value = "上传的文件", required = true)
												  @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10014);//未知异常
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = platformSettlementConfigService.savePlatformMerchandiseImport(data,user);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
	}

}
