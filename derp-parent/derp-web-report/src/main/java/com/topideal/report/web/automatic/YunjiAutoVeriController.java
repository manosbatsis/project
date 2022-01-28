package com.topideal.report.web.automatic;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.report.service.automatic.YunjiAutoVeriService;
import com.topideal.report.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/yunjiAutoVeri")
public class YunjiAutoVeriController {
	
	@Autowired
	private YunjiAutoVeriService yunjiAutoVeriService ;

	@RequestMapping("toPage.html")
	public String toPage(Model model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
		String month = sdf.format(new Date());
		
		model.addAttribute("month", month) ;
		
		return "derp/automatic/yunjiAutoVerification-list" ;
	}
	
	/**
	 *  获取列表信息
	 * @param dto
	 * @return
	 */
	@RequestMapping("listYunjiAutoVerification.asyn")
	@ResponseBody
	public ViewResponseBean listYunjiAutoVerification(YunjiAutomaticVerificationDTO dto) {
		
		try {
			User user = ShiroUtils.getUser();
			
			dto.setMerchantId(user.getMerchantId());
			dto = yunjiAutoVeriService.listYunjiAutoVerification(dto) ;
			
			return ResponseFactory.success(dto) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
	}
	
	@RequestMapping("getExportCount.asyn")
	@ResponseBody
	public ViewResponseBean getExportCount(YunjiAutomaticVerificationDTO dto) {
		try {
			User user = ShiroUtils.getUser();
			
			dto.setMerchantId(user.getMerchantId());
			int count = yunjiAutoVeriService.getExportCount(dto) ;
			return ResponseFactory.success(count) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
	}
	
	@RequestMapping("export.asyn")
	public void export(YunjiAutomaticVerificationDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());
		String fileName = "云集校验导出表";
		
		String mainsheet = "云集校验汇总表" ;
		String[] mainKeys = { "month", "settleId", "skuNo", "platformDeliveryAccount", 
				"platformReturnAccount", "goodsNo", "systemDeliveryAccount",
				"systemReturnAccount", "deliveryDifferent", "returnDifferent", "result"} ;
		String[] mainColumns = {"账单月份", "结算单号", "云集商品编码", "平台发货量", 
				"平台退货量", "商品货号", "系统出库量",
				"系统入库量", "出库差异", "入库差异", "原因"} ;
		List<YunjiAutomaticVerificationDTO> mainList = yunjiAutoVeriService.getExportList(dto) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainsheet, mainColumns, mainKeys, mainList);
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
}
