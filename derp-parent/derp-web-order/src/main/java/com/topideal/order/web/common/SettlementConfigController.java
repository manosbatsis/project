package com.topideal.order.web.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费项配置
 */
@RequestMapping("/settlementConfig")
@Controller
public class SettlementConfigController {

	@Autowired
	private SettlementConfigService settlementConfigService;
	@Autowired
	private ReceivePaymentSubjectService receivePaymentSubjectService;
	
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/common/settlement-config-list";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/settlementConfigList.asyn")
	@ResponseBody
	private ViewResponseBean settlementConfigList(SettlementConfigDTO dto) {
		try{
			
			// 响应结果集
			dto = settlementConfigService.getSettlementListByPage(dto);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 获取nc下拉列表
	 * @param model
	 * */
	@RequestMapping("/ncReceivePayment.asyn")
	@ResponseBody
	public ViewResponseBean getNcReceivePayment(Model model,HttpSession session) throws Exception{	 		
		try{			
			List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
			return ResponseFactory.success(receivePaymentSubjectList);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}

	}
	
	/**
	 * 获取上级费用项下拉框
	 * @param model
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/parentProjectNameList.asyn")
	@ResponseBody
	public ViewResponseBean getParentProjectNameList() throws Exception{
		List<SettlementConfigModel> receivePaymentSubjectList=null;
		SettlementConfigModel settlementConfigModel =new SettlementConfigModel();		
		settlementConfigModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_1);
		settlementConfigModel.setStatus(DERP_ORDER.RECEIVE_PAYMENT_SUBJECT_TYPE_1);

		
		try{			
			receivePaymentSubjectList = settlementConfigService.getParentProjectNameList(settlementConfigModel);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(receivePaymentSubjectList);
	}
	
	/**
	 * 访问编详情
	 * @param model
	 * */
	@RequestMapping("/toDetail.asyn")
	@ResponseBody
	public ViewResponseBean toEditPage(Long id) throws Exception{
		//查询所有商家
		SettlementConfigDTO dto=null;
		try {
			dto = settlementConfigService.searchDetail(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
		return ResponseFactory.success(dto);
	}
	/**
	 * 新增
	 * */
	@RequestMapping("/saveSettlementConfig.asyn")
	@ResponseBody
	public ViewResponseBean saveSettlement(String json,HttpSession session) {
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
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			User user= ShiroUtils.getUser(); 
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
			retMap = settlementConfigService.saveSettlement(type,module,model,customerIdArr);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}
	
	/**
	 * 编辑
	 * */
	@RequestMapping("/modifySettlementConfig.asyn")
	@ResponseBody
	public ViewResponseBean modifySettlement(String json,Long settlementConfigId,HttpSession session) {
		
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
		
		
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			User user= ShiroUtils.getUser(); 
				 
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

			
			
			retMap=settlementConfigService.modifySettlement(type,module,model,customerIdArr,oldProjectName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 根据适用模块获取费项配置列表分页
	 * @param dto
	 * @return
	 */
	@RequestMapping("/settlementConfigListByModuleType.asyn")
	@ResponseBody
	private ViewResponseBean settlementConfigListByModuleType(SettlementConfigDTO dto) {
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(dto.getModuleType()).empty();
			if (isNull) {
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			// 响应结果集
			dto = settlementConfigService.getSettlementListByModuleTypePage(dto);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}


	/**
	 * 获取下拉框
	 * @param dto
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	private ViewResponseBean getSelectBean(SettlementConfigDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils()
					.addObject(dto.getLevel())
					.addObject(dto.getModuleType())
					.empty();
			if (isNull) {
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			// 响应结果集
			result = settlementConfigService.getSelectBean(dto);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(result);
	}

	
	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@RequestMapping("/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, SettlementConfigDTO dto) throws Exception{
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
	@RequestMapping("/isOrNotEnable.asyn")
	@ResponseBody
	private ViewResponseBean isOrNotEnable(Long id,String status) {
		try{
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils()
					.addObject(id)
					.addObject(status)
					.empty();
			if (isNull) {
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser(); 
			SettlementConfigModel model=new SettlementConfigModel();
			model.setId(id);
			model.setStatus(status);
			model.setModifierName(user.getName());
			model.setModifier(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			// 响应结果集
			boolean b = settlementConfigService.isOrNotEnable(model);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}


}
