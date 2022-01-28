package com.topideal.webapi.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepotCustomsRelDao;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.BatchValidationInfoModel;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.DepotMerchantRelService;
import com.topideal.service.main.DepotService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.DepotInfoForm;
import com.topideal.webapi.form.DepotMerchantRelForm;
import com.topideal.webapi.form.MerchantDepotBuRelForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理 控制层
 */
@RestController
@RequestMapping("/webapi/system/depot")
@Api(tags = "仓库管理")
public class APIDepotController {

	// 仓库信息service
	@Autowired
	private DepotService depotService;
	// 仓库商家关联表service
	@Autowired
	private DepotMerchantRelService depotMerchantRelService;
	@Autowired
	private DepotCustomsRelDao depotCustomsRelDao;
	

	/**
	 * 访问列表页面
	 */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/main/depot-list";
	}*/

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 */
	@ApiOperation(value = "访问新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toAddPage(Long id) throws SQLException {
		try {
			List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantBean);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		

	}

	/**
	 * 访问详情页面
	 */
	@ApiOperation(value = "访问详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
			resultMap.put("merchantBean", merchantBean);
			DepotInfoDTO depotInfo = depotService.searchDetail(id);
			resultMap.put("detail", depotInfo);
			List<BatchValidationInfoModel> batch = depotService.getListById(id);
			resultMap.put("batch",batch);
			DepotCustomsRelModel model = new DepotCustomsRelModel();
			model.setDepotId(id);
			List<DepotCustomsRelModel> depotCustomsRelModels = depotCustomsRelDao.list(model);
			resultMap.put("depotCustomsRelList",depotCustomsRelModels);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 访问编辑页面
	 */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage( Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<MerchantInfoModel> merchantBean = depotService.getSelectPoxy();
			resultMap.put("merchantBean", merchantBean);
			DepotInfoDTO depotInfo = depotService.searchDetail(id);
			resultMap.put("detail", depotInfo);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
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
			@ApiImplicitParam(name = "code", value = "仓库编号"),
			@ApiImplicitParam(name = "name", value = "仓库名称"),
			@ApiImplicitParam(name = "type", value = "仓库类别")
			
	})
	@PostMapping(value="/listDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listDepot(String code,String name,String type,int begin,int pageSize) {
		try {
			// 响应结果集
			DepotInfoDTO model=new DepotInfoDTO();
			model.setCode(code);
			model.setName(name);
			model.setType(type);
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model = depotService.listDepot(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 校验仓库类别
	 */
	@ApiOperation(value = "校验仓库类别")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)			
	})
	@PostMapping(value="/checkDepotType.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean checkDepotType(Long id) {
		int flag = 0;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			DepotInfoDTO model = depotService.searchDetail(id);
			if(model.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)){
				flag = 1;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);//成功
	}

	/**
	 * 新增
	 */
	@ApiOperation(value = "新增仓库",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/saveDepot.asyn")
	public ResponseBean saveDepot(DepotInfoForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			
			JSONArray jSONArray=new JSONArray();
			String json = form.getJson();
			if (StringUtils.isNotBlank(json)) {
				JSONObject jsonData = JSONObject.fromObject(json);
				jSONArray = (JSONArray) jsonData.get("itemList");
				Map<String, Object>map=new HashMap<>();
				for (Object object : jSONArray) {
					JSONObject jSONObject=(JSONObject) object;
					String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
					if (StringUtils.isBlank(customsAreaConfigId)) {
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"关区不能为空");//未知异常

					}
					if (map.containsKey(customsAreaConfigId)) {
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"关区不能为重复");//未知异常
					}
					map.put(customsAreaConfigId, customsAreaConfigId);
				}
			}
						
			
			// 判断仓库类别不为空
			boolean isNullType = new EmptyCheckUtils().addObject(form.getType()).empty();
			if (isNullType) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(form.getName()).addObject(form.getCode())
					.addObject(form.getType()).addObject(form.getISVDepotType()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			DepotInfoModel model=new DepotInfoModel();
			model.setAdminName(form.getAdminName());
			model.setCode(form.getCode());
			model.setAddress(form.getAddress());
			model.setName(form.getName());
			model.setInspectNo(form.getInspectNo());
			model.setType(form.getType());
			model.setCustomsNo(form.getCustomsNo());
			model.setIsTopBooks(form.getIsTopBooks());
			model.setDepotType(form.getDepotType());
			model.setTel(form.getTel());
			model.setStatus(form.getStatus());
			model.setCountry(form.getCountry());
			model.setMerchantId(form.getMerchantId());
			model.setDepotCode(form.getDepotCode());
			model.setBatchValidation(form.getBatchValidation());
			model.setISVDepotType(form.getISVDepotType());
			model.setWarehouseId(form.getWarehouseId());
			model.setCreater(user.getId());// 创建人id
			model.setIsMerchant(form.getIsMerchant());
			model.setIsValetManage(form.getIsValetManage());
			model.setMerchantId(form.getMerchantId());
			// 存储信息
			boolean b = depotService.saveDepot(user, model,jSONArray);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
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
			@ApiImplicitParam(name = "ids", value = "id集合多个用英文逗号隔开", required = true)			
	})
	@PostMapping(value="/delDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delDepot(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = depotService.delDepot(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 修改
	 */
	@ApiOperation(value = "修改仓库",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/modifyDepot.asyn")
	public ResponseBean modifyDepot(DepotInfoForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			
			JSONArray jSONArray=new JSONArray();
			String json = form.getJson();
			if (StringUtils.isNotBlank(json)) {
				JSONObject jsonData = JSONObject.fromObject(json);
				jSONArray = (JSONArray) jsonData.get("itemList");
				Map<String, Object>map=new HashMap<>();
				for (Object object : jSONArray) {
					JSONObject jSONObject=(JSONObject) object;
					String customsAreaConfigId = jSONObject.getString("customsAreaConfigId");
					if (StringUtils.isBlank(customsAreaConfigId)) {
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"关区不能为空");//未知异常

					}
					if (map.containsKey(customsAreaConfigId)) {
						return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"关区不能为重复");//未知异常
					}
					map.put(customsAreaConfigId, customsAreaConfigId);
				}
			}
			
			
				
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(form.getId());
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			DepotInfoModel model=new DepotInfoModel();
			model.setId(form.getId());
			model.setAdminName(form.getAdminName());
			model.setCode(form.getCode());
			model.setAddress(form.getAddress());
			model.setName(form.getName());
			model.setInspectNo(form.getInspectNo());
			model.setType(form.getType());
			model.setCustomsNo(form.getCustomsNo());
			model.setIsTopBooks(form.getIsTopBooks());
			model.setDepotType(form.getDepotType());
			model.setTel(form.getTel());
			model.setStatus(form.getStatus());
			model.setCountry(form.getCountry());
			model.setMerchantId(form.getMerchantId());
			model.setDepotCode(form.getDepotCode());
			model.setBatchValidation(form.getBatchValidation());
			model.setISVDepotType(form.getISVDepotType());
			model.setWarehouseId(form.getWarehouseId());
			model.setIsValetManage(form.getIsValetManage());
			model.setIsMerchant(form.getIsMerchant());
			model.setMerchantId(form.getMerchantId());
			model.setModifyDate(TimeUtils.getNow());
			if (null == model.getIsTopBooks() || "".equals(model.getIsTopBooks())) {
				model.setIsTopBooks("0");
			}
			if (null == model.getStatus() || "".equals(model.getStatus())) {
				model.setStatus("0");
			}
			boolean b = depotService.modifyDepot(user, model,jSONArray);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 导入页面
	 */
/*	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/goods/depot-import";
	}*/

	/**
	 * 导入仓库
	 */
	@ApiOperation(value = "导入仓库")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importDepot.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importDepot(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = depotService.importDepot(user, data, user.getId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取下拉列表
	 */
	@ApiOperation(value = "获取下拉列表",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/getSelectBean.asyn")
	public ResponseBean getSelectBean(DepotInfoForm form) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			DepotInfoModel model=new DepotInfoModel();
			model.setId(form.getId());
			model.setAdminName(form.getAdminName());
			model.setCode(form.getCode());
			model.setAddress(form.getAddress());
			model.setName(form.getName());
			model.setInspectNo(form.getInspectNo());
			model.setType(form.getType());
			model.setCustomsNo(form.getCustomsNo());
			model.setIsTopBooks(form.getIsTopBooks());
			model.setDepotType(form.getDepotType());
			model.setTel(form.getTel());
			model.setStatus(form.getStatus());
			model.setCountry(form.getCountry());
			model.setMerchantId(form.getMerchantId());
			model.setDepotCode(form.getDepotCode());
			model.setBatchValidation(form.getBatchValidation());
			model.setISVDepotType(form.getISVDepotType());
			model.setWarehouseId(form.getWarehouseId());
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			if("1".equals(user.getUserType())){
				result = depotService.getSelectBeanForAdmin(model);
			}else{
				model.setMerchantId(user.getMerchantId());
				result = depotService.getSelectBean(model);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 根据页面传入商家id、进出接口指令、统计存货跌价、选品限制、已入定出、已出定入获取此商家下仓库的下拉框
	 */
	@ApiOperation(value = "根据商家仓库配置加载仓库",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/getSelectBeanByMerchantRel.asyn")
	public ResponseBean<List<SelectBean>> getSelectBeanByMerchantRel(DepotMerchantRelForm form) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			DepotMerchantRelDTO dto = new DepotMerchantRelDTO();
			if(StringUtils.isNotBlank(form.getMerchantId())){
				dto.setMerchantId(Long.valueOf(form.getMerchantId()));
			}
			dto.setIsInOutInstruction(form.getIsInOutInstruction());
			dto.setIsInvertoryFallingPrice(form.getIsInvertoryFallingPrice());
			dto.setProductRestriction(form.getProductRestriction());
			dto.setIsInDependOut(form.getIsInDependOut());
			dto.setIsOutDependIn(form.getIsOutDependIn());
			dto.setIsTopBooks(form.getIsTopBooks());
			dto.setType(form.getType());
			dto.setIsValetManage(form.getIsValetManage());

			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			if("1".equals(user.getUserType()) && form.getMerchantId()==null) {
					return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10010);
			}else {
				if(dto.getMerchantId() == null) {
					dto.setMerchantId(user.getMerchantId());
				}
			}
			result = depotService.getSelectBeanByMerchantRel(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}
	
	/**
	 * 获取下拉列表
	 */
	@ApiOperation(value = "获取下拉列表",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/getSelectBeanByArea.asyn")
	public ResponseBean getSelectBeanByArea(DepotInfoForm form) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			DepotInfoDTO model=new DepotInfoDTO();
			
			model.setId(form.getId());
			model.setAdminName(form.getAdminName());
			model.setCode(form.getCode());
			model.setAddress(form.getAddress());
			model.setName(form.getName());
			model.setInspectNo(form.getInspectNo());
			model.setType(form.getType());
			model.setCustomsNo(form.getCustomsNo());
			model.setIsTopBooks(form.getIsTopBooks());
			model.setDepotType(form.getDepotType());
			model.setTel(form.getTel());
			model.setStatus(form.getStatus());
			model.setCountry(form.getCountry());
			model.setMerchantId(form.getMerchantId());
			model.setDepotCode(form.getDepotCode());
			model.setBatchValidation(form.getBatchValidation());
			model.setISVDepotType(form.getISVDepotType());
			model.setWarehouseId(form.getWarehouseId());
			model.setMerchantId(user.getMerchantId());
			result = depotService.getSelectBeanByArea(model);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	/**
	 * 根据ID获取详情
	 */
	@ApiOperation(value = "根据ID获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getDepotDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		DepotInfoDTO model = new DepotInfoDTO();
		try {
			model = depotService.searchDetail(id);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}

	/**
	 * 根据id获取仓库信息
	 */
	@ApiOperation(value = "根据ID获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getDepotById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getDepotById(Long id) {
		DepotInfoDTO model = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			model = depotService.searchDetail(id);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}
	/**
	 * 禁用/启用
	 * @param
	 * @return
	 */
	@ApiOperation(value = "禁用/启用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "status", value = "status", required = true)
	})
	@PostMapping(value="/auditDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditDepot(Long id,String status, String token) {
		try {
			User user = ShiroUtils.getUserByToken(token);

			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            DepotInfoModel model=new DepotInfoModel();
            model.setId(id);
            model.setStatus(status);
            model.setModifyDate(TimeUtils.getNow());
			boolean b = depotService.audit(user, model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
	/**
	 * 9011 弹框查询列表
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取变更记录列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getListById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<BatchValidationInfoModel>> getListById(Long id) {
		List<BatchValidationInfoModel> model = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			model = depotService.getListById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}
	/**
	 * 根据仓库Id和商家ID去仓库商家关联表查询相关信息
	 * */
	@ApiOperation(value = "禁用/启用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)
	})
	@PostMapping(value="/checkDepotMerchantRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean changeShopCodeLabel(String token,Long depotId) throws Exception {
		try {
			User user = ShiroUtils.getUserByToken(token);
			// 仓库商家关联表
			DepotMerchantRelDTO dto = depotMerchantRelService.getByDepotIdAndMerchantId(depotId, user.getMerchantId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}


	/**
	 * 根据页面传入商家id、事业部id、仓库类别、是否代客管理仓库、是否是代销仓获取此商家事业部下仓库的下拉框
	 */
	@ApiOperation(value = "获取商家事业部下仓库的下拉框")
	@PostMapping("/getSelectBeanByMerchantBuRel.asyn")
	public ResponseBean getSelectBeanByMerchantBuRel(MerchantDepotBuRelForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchantDepotBuRelDTO dto = new MerchantDepotBuRelDTO();
			dto.setMerchantId(form.getMerchantId());
			dto.setBuId(form.getBuId());
			dto.setType(form.getType());
			dto.setIsTopBooks(form.getIsTopBooks());
			dto.setIsValetManage(form.getIsValetManage());

			if("1".equals(user.getUserType())) {
				if(dto.getMerchantId() == null) {
					return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10010);
				}
			}else {
				if(dto.getMerchantId() == null) {
					dto.setMerchantId(user.getMerchantId());
				}
			}

			List<SelectBean> result = depotService.getSelectBeanByMerchantBuRel(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}
	/**
	 * 获取下拉列表
	 */
	@ApiOperation(value = "获取下拉列表")
	@PostMapping("/getSelectBeanByDTO.asyn")
	public ResponseBean<List<DepotInfoDTO>> getSelectBeanByDTO(DepotInfoForm form) {
		List<DepotInfoDTO> result = new ArrayList<DepotInfoDTO>();
		try {
			DepotInfoDTO dto = new DepotInfoDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			result = depotService.getSelectBeanByDTO(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
