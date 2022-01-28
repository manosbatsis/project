package com.topideal.order.webapi.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.order.service.common.SettlementConfigService;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.SettlementConfigBeanForm;
import com.topideal.order.webapi.common.form.SettlementConfigByPageForm;
import com.topideal.order.webapi.common.form.SettlementConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/webapi/order/settlementConfig")
@Api(tags = "费项配置")
public class APISettlementConfigController {
    @Autowired
    private SettlementConfigService settlementConfigService;
    @Autowired
    private ReceivePaymentSubjectService receivePaymentSubjectService;

	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
    @ApiOperation(value = "费项配置分页数据")
    @PostMapping(value = "/settlementConfigList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SettlementConfigDTO> settlementConfigList(SettlementConfigByPageForm form) {
		try{
			SettlementConfigDTO dto=new SettlementConfigDTO();
			BeanUtils.copyProperties(form,dto);
			// 响应结果集
			dto = settlementConfigService.getSettlementListByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
		}catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
    
	/**
	 * 获取nc下拉列表
	 * @param model
	 * */
	@ApiOperation(value = "获取nc下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/ncReceivePayment.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<ReceivePaymentSubjectModel>> getNcReceivePayment() throws Exception{	 		
		try{			
			List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receivePaymentSubjectList);//成功
		}catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}

	}
    
    
	/**
	 * 获取上级费用项下拉框
	 * @param model
	 * @param session
	 * @return
	 * @throws Exception
	 */

	@ApiOperation(value = "获取上级费用项下拉框")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/parentProjectNameList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SettlementConfigModel>> getParentProjectNameList() throws Exception{
		

		try{		
		List<SettlementConfigModel> receivePaymentSubjectList=null;
		SettlementConfigModel settlementConfigModel =new SettlementConfigModel();		
		settlementConfigModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_1);
		settlementConfigModel.setStatus(DERP_ORDER.RECEIVE_PAYMENT_SUBJECT_TYPE_1);	
		receivePaymentSubjectList = settlementConfigService.getParentProjectNameList(settlementConfigModel);
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receivePaymentSubjectList);//成功
		}catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	
	/**
	 * 访问编详情
	 * @param model
	 * */
	@ApiOperation(value = "访问编详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SettlementConfigDTO> toEditPage(Long id) throws Exception{
		//查询所有商家
		SettlementConfigDTO dto=null;
		try {
			dto = settlementConfigService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
		} catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "费项配置新增json", required = true)
	})
	@PostMapping(value="/saveSettlementConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveSettlement(String json,String token) {
		try{
			JSONArray jsonArr = JSONArray.fromObject(json);
			JSONObject jsonData = (JSONObject) jsonArr.get(0);
			//JSONObject jsonData = JSONObject.fromObject(json);
			String level = jsonData.getString("level");
			Long parentProjectId =null;
			if (jsonData.get("parentProjectId")!=null&&StringUtils.isNotBlank(jsonData.getString("parentProjectId"))
					&&"2".equals(level)) {
				parentProjectId = Long.valueOf(jsonData.getString("parentProjectId"));
			}
			String inExpCode = null;
			if (jsonData.get("inExpCode")!=null&&StringUtils.isNotBlank(jsonData.getString("inExpCode"))) {
				inExpCode=jsonData.getString("inExpCode");
			}
			
			String parentProjectNameText = jsonData.getString("parentProjectNameText");
			String projectName = jsonData.getString("projectName");
			String receivePaymentSubjectId = jsonData.getString("receivePaymentSubjectId");
			String receivePaymentSubjectText = jsonData.getString("receivePaymentSubjectText");
			String suitableCustomer = jsonData.getString("suitableCustomer");
			String status = jsonData.getString("status");
			String type = jsonData.getString("type");
			String module = jsonData.getString("module");
			
			JSONArray customerIdArr=null;
			if ("2".equals(suitableCustomer)) {
				customerIdArr = jsonData.getJSONArray("customerIdStr");
			}		
			
			User user= ShiroUtils.getUserByToken(token);
			SettlementConfigModel model= new SettlementConfigModel();	
			model.setInExpCode(inExpCode);			
			model.setLevel(level);
			if (parentProjectId!=null) {
				model.setParentId(parentProjectId);
				model.setParentProjectName(parentProjectNameText);	
			}
			model.setProjectName(projectName);
			model.setPaymentSubjectId(Long.valueOf(receivePaymentSubjectId));
			model.setPaymentSubjectName(receivePaymentSubjectText);
			model.setStatus(status);
			model.setSuitableCustomer(suitableCustomer);
			model.setCreater(user.getId());
			model.setCreaterName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			Map<String, Object>  retMap = settlementConfigService.saveSettlement(type,module,model,customerIdArr);
			String code = (String) retMap.get("code");
			String message = (String) retMap.get("message");
			if (!"00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}

	}
	
	/**
	 * 编辑
	 * */
	@ApiOperation(value = "编辑")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "费项配置编辑json", required = true),
		@ApiImplicitParam(name = "settlementConfigId", value = "费项配置id", required = true),
	})
	@PostMapping(value="/modifySettlementConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifySettlement(String json,Long settlementConfigId,String token) {
		try{
			JSONArray jsonArr = JSONArray.fromObject(json);
			JSONObject jsonData = (JSONObject) jsonArr.get(0);
			String inExpCode = null;
			if (jsonData.get("inExpCode")!=null&&StringUtils.isNotBlank(jsonData.getString("inExpCode"))) {
				inExpCode=jsonData.getString("inExpCode");
			}
			String id = jsonData.getString("id");
			String level = jsonData.getString("level");
			Long parentProjectId =null;
			if (jsonData.get("parentProjectId")!=null&&StringUtils.isNotBlank(jsonData.getString("parentProjectId"))
					&&"2".equals(level)) {
				parentProjectId = Long.valueOf(jsonData.getString("parentProjectId"));
			}
			String parentProjectNameText = jsonData.getString("parentProjectNameText");
			String projectName = jsonData.getString("projectName");
			String oldProjectName = jsonData.getString("oldProjectName");
			String receivePaymentSubjectId = jsonData.getString("receivePaymentSubjectId");
			String receivePaymentSubjectText = jsonData.getString("receivePaymentSubjectText");
			String suitableCustomer = jsonData.getString("suitableCustomer");
			String status = jsonData.getString("status");
			String type = jsonData.getString("type");
			String module = jsonData.getString("module");
			JSONArray customerIdArr=null;
			if ("2".equals(suitableCustomer)) {
				customerIdArr = jsonData.getJSONArray("customerIdStr");
			}	
			
			User user= ShiroUtils.getUserByToken(token);
				 
			SettlementConfigModel model= new SettlementConfigModel();
			model.setId(Long.valueOf(id));
			model.setInExpCode(inExpCode);	
			model.setLevel(level);
			if (parentProjectId!=null) {
				model.setParentId(parentProjectId);
				model.setParentProjectName(parentProjectNameText);
			}
			model.setType(type);
			model.setProjectName(projectName);
			model.setPaymentSubjectId(Long.valueOf(receivePaymentSubjectId));
			model.setPaymentSubjectName(receivePaymentSubjectText);
			model.setStatus(status);
			model.setSuitableCustomer(suitableCustomer);
			model.setModifier(user.getId());
			model.setModifierName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			Map<String, Object> retMap=settlementConfigService.modifySettlement(type,module,model,customerIdArr,oldProjectName);
			String code = (String) retMap.get("code");
			String message = (String) retMap.get("message");
			if (!"00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}

	}
	
    
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "根据适用模块获取费项配置列表分页")
    @PostMapping(value = "/settlementConfigListByModuleType.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean settlementConfigListByModuleType(SettlementConfigForm form) {
        SettlementConfigDTO dto=new SettlementConfigDTO();
        try {
            BeanUtils.copyProperties(form,dto);
            boolean isNull = new EmptyCheckUtils().addObject(dto.getModuleType()).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            dto = settlementConfigService.getSettlementListByModuleTypePage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 获取下拉框
     * @param form
     * @return
     */
    @ApiOperation(value = "获取下拉框")
    @PostMapping(value = "/getSelectBean.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getSelectBean(SettlementConfigBeanForm form) {
        List<SelectBean> result = new ArrayList<SelectBean>();
        try{
            SettlementConfigDTO dto=new SettlementConfigDTO();
            BeanUtils.copyProperties(form,dto);
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils()
                    .addObject(dto.getLevel())
                    .empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            result = settlementConfigService.getSelectBean(dto);
        }catch(Exception e){
        	e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }
    
	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request,SettlementConfigByPageForm form ) throws Exception{
	
		SettlementConfigDTO dto=new SettlementConfigDTO();
		BeanUtils.copyProperties(form,dto);
		/** 标题  */
		String[] COLUMNS= {"本级编码","本级费项名称","上级费项名称","主数据编码","NC收支费项","适用客户","适用类型","状态","编辑人","最近编辑时间"};
		String[] KEYS= {"projectCode","projectName","parentProjectName","inExpCode","paymentSubjectName","customerNames","","statusLabel" ,"modifierName","modifyDate"};

		List<SettlementConfigDTO> list = settlementConfigService.exportSettlementList(dto);															
		String sheetName = "费用项导出信息";
	
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
	@ApiOperation(value = "启用禁用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id", required = true),
		@ApiImplicitParam(name = "status", value = "状态", required = true)
	})
	@PostMapping(value="/isOrNotEnable.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean isOrNotEnable(Long id,String status,String token) {
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils()
					.addObject(id)
					.addObject(status)
					.empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//
			}
			User user= ShiroUtils.getUserByToken(token);
			SettlementConfigModel model=new SettlementConfigModel();
			model.setId(id);
			model.setStatus(status);
			model.setModifierName(user.getName());
			model.setModifier(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			// 响应结果集
			boolean b = settlementConfigService.isOrNotEnable(model);
			if (!b) {
	            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			 return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e){
			e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}

	}
	
	
    /**
     * 获取NC下拉框
     * @param form
     * @return
     */
    @ApiOperation(value = "获取NC下拉框")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "费项id", required = false)
	})
    @PostMapping(value = "/getNCSelectBean.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getNCSelectBean(String token , Long id) {
        List<SelectBean> result = new ArrayList<SelectBean>();
        try{
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {//id为空 获取全部NC信息
            	List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
            	for(ReceivePaymentSubjectModel model : receivePaymentSubjectList) {
            		SelectBean selectBean = new SelectBean();
                	selectBean.setValue(model.getId().toString());
                	selectBean.setLabel(model.getName());
                	result.add(selectBean);  
            	}
            	
            }else {//费项id不为空，获取对应NC信息
            	SettlementConfigDTO dto = settlementConfigService.searchDetail(id);
            	SelectBean selectBean = new SelectBean();
            	selectBean.setValue(dto.getPaymentSubjectId().toString());
            	selectBean.setLabel(dto.getPaymentSubjectName());
            	result.add(selectBean);            	
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }

}
