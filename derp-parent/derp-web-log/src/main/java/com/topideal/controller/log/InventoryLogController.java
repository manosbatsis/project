package com.topideal.controller.log;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.LogService;
import com.topideal.tools.CollectionEnum;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存日志-启用
 */
@RestController
@RequestMapping("/inventorylog")
public class InventoryLogController {

	/** 标题  */
	private static final String[] COLUMNS= {"接口编码","关键字","接口说明","请求时间","消费时间","耗时","状态","异常提示"};
	
	private static final String[] KEYS = {"point", "keyword","model", "startDate", "endDate", "differenceTime", "state", "expMsg"} ;
	
	@Autowired
	private LogService service;

	@RequestMapping("/toPage.html")
	public ModelAndView toPage(String point, String keyword, String id, String selectScope) throws Exception {
		ModelAndView modelAndView = new ModelAndView("mq-inventory");
		modelAndView.addObject("point", point);
		modelAndView.addObject("keyword", keyword);
		modelAndView.addObject("id", id);
		modelAndView.addObject("selectScope", selectScope);
		return modelAndView;
	}
	
	@RequestMapping("/toImportPage.html")
	public ModelAndView toImportPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("mq-inventory-import");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/list.asyn")
	public ViewResponseBean searchLogAll(PageMongo pageMongo, String keyword, String state, String point,
			String endDateStr, String id, String differenceTime, String selectScope) {
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			pageMongo = service.searchInventoryLog(pageMongo, keyword, state, point, endDateStr, id, differenceTime, selectScope);
			List list = new ArrayList();
			for (int i = 0; i < pageMongo.getList().size(); i++) {
				JSONObject jsonModel = (JSONObject) pageMongo.getList().get(i);
				if(jsonModel.toString().contains(":null")){
					System.out.println("此字段为null,手动修改显示");
					jsonModel= JSONObject.fromObject(jsonModel.toString().replace(":null", ":\"此字段为null,手动修改显示\""));
				}
				list.add(jsonModel);
			}
			pageMongo.setList(list);
			
			
			viewResponseBean.setData(pageMongo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}
		return ResponseFactory.success(pageMongo);
	}
	
	/**
	 * 统计数量
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count.asyn")
	public ViewResponseBean countLog(String keyword, String state, String point, String endDateStr,String differenceTime, String selectScope) {
		Long count = null;
		try {
			count = service.count(keyword, state, point, endDateStr, differenceTime, selectScope,CollectionEnum.MQ_INVENTORY_LOG.getCollectionName(),CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(count);
	}

	@ResponseBody
	@RequestMapping("/resetSend.asyn")
	public ViewResponseBean resetSend(String id) {
		if (StringUtils.isBlank(id)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			service.resetSend(id, CollectionEnum.MQ_INVENTORY_LOG.getCollectionName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}
		return ResponseFactory.success();
	}

	/**
	 * 批量推送
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchResetSend.asyn")
	public ViewResponseBean batchResetSend(String ids) {
		if (StringUtils.isBlank(ids)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<String> list = StrUtils.parseIdsToStr(ids);
		try {
			service.resetSend(list, CollectionEnum.MQ_INVENTORY_LOG.getCollectionName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 日志导出
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/exportLogs.asyn")
	public void exportRelation(HttpSession session, HttpServletResponse response, HttpServletRequest request, String keyword, String state, String point,
			String endDateStr,String differenceTime, String selectScope) throws Exception{
        String sheetName = "库存日志";
        List<Map<String, Object>> list = service.searchListLog(keyword, state, point, endDateStr,differenceTime,selectScope,CollectionEnum.MQ_INVENTORY_LOG.getCollectionName(),CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName());
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS, list) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 批量导入重推
	 * @param 
	 * @return map
	 * @throws IOException 
	 */
	@RequestMapping("/import.asyn")
	@ResponseBody
	public ViewResponseBean importRelation(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) throws IOException{
		Map resultMap = new HashMap();//返回的结果集
		try{
			if(file==null){
				//输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
			resultMap = service.importByKeyword(data, CollectionEnum.MQ_INVENTORY_LOG.getCollectionName());
		}catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		return ResponseFactory.success(resultMap);
	}
	
}
