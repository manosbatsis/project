package com.topideal.order.web.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.order.service.common.PlatformSettlementConfigService;
import com.topideal.order.service.common.SettlementConfigService;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台费用映射表
 */
@RequestMapping("/platformSettlementConfig")
@Controller
public class PlatformSettlementConfigController {

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
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		//获取 已有维护且启用状态的二级费项名称 (下拉框)
		SettlementConfigModel settlementConfig= new SettlementConfigModel();
		settlementConfig.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
		settlementConfig.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
		List<SettlementConfigModel> settlementConfigList = settlementConfigService.getByModelList(settlementConfig);
		model.addAttribute("settlementConfigList", settlementConfigList);
		//获取NC收支费项科目表已有维护且启用状态的NC收支费项(下拉框)
		List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
		model.addAttribute("receivePaymentSubjectList", receivePaymentSubjectList);
		return "/derp/common/platform-settlement-config-list";
	}
	/**
	 * 新增/编辑获取下拉框
	 * @param model
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAllSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getNcReceivePayment(Model model,HttpSession session) throws Exception{	
		//获取 已有维护且启用状态的二级费项名称 (下拉框)
		Map<String, Object>map=new HashMap<>();
		try{			
			SettlementConfigModel settlementConfig= new SettlementConfigModel();
			settlementConfig.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
			settlementConfig.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
			List<SettlementConfigModel> selectBeanByList = settlementConfigService.getByModelList(settlementConfig);
			map.put("settlementConfigList", selectBeanByList);
			//获取NC收支费项科目表已有维护且启用状态的NC收支费项(下拉框)
			/*List<ReceivePaymentSubjectModel> receivePaymentSubjectList = receivePaymentSubjectService.getReceivePaymentSubjectList();
			map.put("receivePaymentSubjectList", receivePaymentSubjectList);*/
			return ResponseFactory.success(map);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}

	}
	@RequestMapping("/toDetail.asyn")
	@ResponseBody
	public ViewResponseBean toEditPage(Long id) throws Exception{
		//查询所有商家
		PlatformSettlementConfigModel model=new PlatformSettlementConfigModel();
		try {
			model.setId(id);
			model = platformSettlementConfigService.searchDetail(model);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
		return ResponseFactory.success(model);
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/platformSettlementConfigList.asyn")
	@ResponseBody
	private ViewResponseBean settlementConfigList(PlatformSettlementConfigDTO dto) {
		try{
			
			// 响应结果集
			dto = platformSettlementConfigService.getPlatSettlementListByPage(dto);
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 新增
	 * */
	@RequestMapping("/saveSettlementConfig.asyn")
	@ResponseBody
	public ViewResponseBean saveSettlement(PlatformSettlementConfigModel model,HttpSession session) {
				
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			User user= ShiroUtils.getUser(); 
			String name = model.getName();
			name=name.trim();
			model.setName(name);
			model.setCreater(user.getId());
			model.setCreaterName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			retMap = platformSettlementConfigService.saveSettlement(model);
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
	public ViewResponseBean modifySettlement(PlatformSettlementConfigModel model,String oldName,HttpSession session) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			User user= ShiroUtils.getUser(); 	
			String name = model.getName();
			name=name.trim();
			model.setName(name);
			model.setModifier(user.getId());
			model.setModifierName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			retMap=platformSettlementConfigService.modifySettlement(model,oldName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}





	
	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@RequestMapping("/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, PlatformSettlementConfigDTO dto) throws Exception{
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
			PlatformSettlementConfigModel model=new PlatformSettlementConfigModel();
			model.setId(id);
			model.setStatus(status);
			model.setModifierName(user.getName());
			model.setModifier(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			// 响应结果集
			boolean b = platformSettlementConfigService.isOrNotEnable(model);
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
	/**
	 * 跳转导入页面
	 * @return
	 */
	@RequestMapping("/importPage.html")
	public String toPlatformMerchandiseImportPage() {
		return "derp/common/platform-settlement-config-import";
	}
	/**
	 * 导入
	 */
	@RequestMapping("/import.asyn")
	@ResponseBody
	public ViewResponseBean platformMerchandiseImport(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = platformSettlementConfigService.savePlatformMerchandiseImport(data,user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

}
