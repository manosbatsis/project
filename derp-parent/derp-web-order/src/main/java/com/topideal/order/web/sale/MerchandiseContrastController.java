package com.topideal.order.web.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.sale.MerchandiseContrastItemService;
import com.topideal.order.service.sale.MerchandiseContrastService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬虫商品对照表
 */
@RequestMapping("/contrast")
@Controller
public class MerchandiseContrastController {

	@Autowired
	private MerchandiseContrastService merchandiseContrastService;
	@Autowired
    private MerchandiseContrastItemService merchandiseContrastItemService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/sale/merchandise-contrast-list";
	}
	
	/**
	 * 获取分页数据
	 * @param dto
	 * @return
	 */
	@RequestMapping("/contrastList.asyn")
	@ResponseBody
	private ViewResponseBean contrastList(MerchandiseContrastDTO dto) {
		try{
			// 响应结果集
			dto = merchandiseContrastService.getListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 访问新增页面
	 * @param model
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model,HttpSession session) throws Exception{
		 
		return "/derp/sale/merchandise-contrast-add";
	}
	/**
	 * 访问新增页面
	 * @param model
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model,Long id) throws Exception{
		//查询所有商家
		MerchandiseContrastDTO dto = merchandiseContrastService.searchDetail(id);
		MerchandiseContrastItemModel contrastItemModel = new MerchandiseContrastItemModel();
		contrastItemModel.setContrastId(dto.getId());
		List<MerchandiseContrastItemModel> itemModel = merchandiseContrastItemService.searchDetail(contrastItemModel);
		model.addAttribute("item",itemModel);
		model.addAttribute("detail", dto);
		return "/derp/sale/merchandise-contrast-edit";
	}
	/**
	 * 新增/修改
	 * */
	@RequestMapping("/saveContrast.asyn")
	@ResponseBody
	public ViewResponseBean saveContrast(String json,HttpSession session) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser(); 
			retMap = merchandiseContrastService.saveContrast(json, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}
	
	
	
	/**
	 * 根据ID获取商品详情
	 * @throws SQLException 
	 */
	@RequestMapping("/detail.html")
	public String detail(Model model,Long id) throws SQLException {		
		MerchandiseContrastDTO dto = merchandiseContrastService.searchDetail(id);
		MerchandiseContrastItemModel contrastItemModel = new MerchandiseContrastItemModel();
		contrastItemModel.setContrastId(id);
		List<MerchandiseContrastItemDTO> itemModels = merchandiseContrastItemService.getContrastItemByContrastId(contrastItemModel);
		model.addAttribute("detail", dto);
		model.addAttribute("itemModels",itemModels);
		return "/derp/sale/merchandise-contrast-detail";
		
	}
	
	/**
	 * 根据商家ID、货号查询商品
	 */
	@RequestMapping("/getByMerchantAndGoodsNo.asyn")
	@ResponseBody
	public ViewResponseBean getByMerchantAndGoodsNo(Long merchantId,String goodsNo) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			List<MerchandiseInfoMogo>  merchandiseList = merchandiseContrastService.getMerchandiseList(merchantId,goodsNo);
		    if(merchandiseList==null||merchandiseList.size()<=0){
		    	retMap.put("code", "01");
		    	retMap.put("message", "未找到商品,请输入正确的货号");
		    }else if(merchandiseList.size()>1){
		    	retMap.put("code", "01");
		    	retMap.put("message", "货号在此商家下存在多个商品请禁用不使用的商品");
		    }else{
		    	retMap.put("code", "00");
		    	retMap.put("message", "查询成功");
		    	retMap.put("merchandise", merchandiseList.get(0));
		    }
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}


	/**
	 * 根据查询条件导出
	 */
	@RequestMapping("/exportContrast.asyn")
	public void exportContrast(MerchandiseContrastDTO dto, HttpServletResponse response, HttpServletRequest request) throws SQLException {
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

		//表头信息
		List<Map<String,Object>> list = merchandiseContrastService.listForExport(dto);

		//表头信息
		String sheetName = "爬虫商品对照信息";
		String[] columns = {"平台名称","用户名","爬虫商品货号","爬虫商品名称","公司名称","事业部","商品货号","商品名称","商品数量","标准销售单价","状态"};
		String[] keys = {"platform_name","platform_username","crawler_goods_no","crawler_goods_name",
				"merchant_name","bu_name","goods_no","goods_name","num","price","status"};

		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, list);
		//导出文件
		String fileName = "爬虫商品对照信息"+ TimeUtils.formatDay();
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
	}

}
