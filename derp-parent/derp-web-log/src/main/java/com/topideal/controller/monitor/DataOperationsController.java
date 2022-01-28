package com.topideal.controller.monitor;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.enums.ExcelTemplateEnum;
import com.topideal.service.DataOperationsService;

import net.sf.json.JSONObject;

/**
 * 经分销数据维护Controller
 */
@RestController
@RequestMapping("/dataoperations")
public class DataOperationsController {
	
	@Autowired
	private DataOperationsService dataOperationsService;
	
	
	
	
	@RequestMapping("/toPage.html")
	public ModelAndView toPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("data-operations-list");
		return modelAndView;
	}
	
	/**
	 * 获取回滚数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getRollbackList.asyn")
    public ViewResponseBean getRollbackList(String orderCodes) throws Exception {
		// json转化
		try {
			Map<String, Object> resultMap = dataOperationsService.getRollbackList(orderCodes);	
			return ResponseFactory.success(resultMap) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
    }
	/**
	 * 查询电商订单金额更新失败日志
	 */
	@RequestMapping("/getAmountCoverLogList.asyn")
	public ViewResponseBean getAmountCoverLogList(String indexCode) throws Exception {
		// json转化
		try {
			Map<String, Object> resultMap = dataOperationsService.getAmountCoverLogList(indexCode);
			return ResponseFactory.success(resultMap) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}

	}

	/**
	 * 综合运维
	 * @param json
	 * @param topic
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/synOperationSendMQ.asyn")
    public JSONObject synOperationSendMQ(String json,String topic, String tags) throws Exception {
		JSONObject respondJSON =new JSONObject();
		respondJSON.put("status","00");//00 成功,01失败
		respondJSON.put("message", "成功");
		JSONObject sendMQJSON =new JSONObject();

		// json转化
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			jsonObject.get("tag");
			Thread.currentThread().sleep(500);
			System.out.println(jsonObject);
			
			System.out.println("topic:"+topic+"  tags:"+tags);
			
			
			// 推送mq
			boolean flag = dataOperationsService.sendMS(json,topic,tags);	
			return respondJSON;
		} catch (Exception e) {
			e.printStackTrace();
			respondJSON.put("status","01");//00 成功,01失败
			respondJSON.put("message", "推送失败");
			return	respondJSON;
		}
		
    }

	
	/**
	 * 数据运维
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sendMS.asyn")
    public JSONObject sendMS(String json,String topic, String tags) throws Exception {
		JSONObject respondJSON =new JSONObject();
		respondJSON.put("status","00");//00 成功,01失败
		respondJSON.put("message", "成功");
		
		
		// json转化
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			Thread.currentThread().sleep(500);
			if (StringUtils.isBlank(topic)) {
				respondJSON.put("status","01");
				respondJSON.put("message", "topic 不能为空");
				return respondJSON;
			}
			if (StringUtils.isBlank(tags)) {
				respondJSON.put("status","01");
				respondJSON.put("message", "tags 不能为空");
				return respondJSON;
			}
			/*if (jsonObject.get("code")==null||StringUtils.isBlank(jsonObject.getString("code"))) {
				respondJSON.put("status","01");
				respondJSON.put("message", "json报文  code 不能为空");
				return respondJSON;
			}*/
			
			// 推送库存
			boolean flag = dataOperationsService.sendMS(json,topic,tags);	
			return respondJSON;
		} catch (Exception e) {
			e.printStackTrace();
			respondJSON.put("status","01");//00 成功,01失败
			respondJSON.put("message", "推送失败");
			return	respondJSON;
		}
		
    }

	@SuppressWarnings("rawtypes")
	@RequestMapping("importData.asyn")
	public ViewResponseBean importData(@RequestParam(value = "file", required = false) MultipartFile file , String type) {
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}

			Map resultMap = null ;
			if("order".equals(type)) {
				Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
				if (data == null) {// 数据为空
					return ResponseFactory.error(StateCodeEnum.ERROR_302);
				}
				resultMap = dataOperationsService.importOrder100Data(data) ;
            }else if("inventoryRollback".equals(type)) {
				Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
				if (data == null) {// 数据为空
					return ResponseFactory.error(StateCodeEnum.ERROR_302);
				}
            	resultMap = dataOperationsService.importInventoryRollbackData(data);
            }else if("amountCover".equals(type)) {
				// 获取源文件
				List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
				resultMap = dataOperationsService.importAmountCover(data);
			}
			
			return ResponseFactory.success(resultMap) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}

	/*@RequestMapping("/download.asyn")
	@ResponseBody
	public ViewResponseBean downloadTemplate(HttpServletResponse response, String type) {
		try{
            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode("模板.zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
          	//获取模板,压缩到zip文件中
            int tempNum = 101 ;
            if("order".equals(type)) {
            	tempNum = 101 ;
            }else if("inventoryRollback".equals(type)) {
            	tempNum = 103 ;
            }
            
        	InputStream in = ExcelTemplateEnum.getFile(ExcelTemplateEnum.getPath(Integer.valueOf(tempNum)));
        	zos.putNextEntry(new ZipEntry(ExcelTemplateEnum.getDesc(Integer.valueOf(tempNum))));
    	    byte[] buffer = new byte[1024];
    	    int r = 0;
    	    while ((r = in.read(buffer)) != -1) {
    	        zos.write(buffer, 0, r);
    	    }
    	    in.close();
            zos.flush();
            zos.close();
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}*/
	/**新版下载单个模板文件
	 * excel
	 * */
	@RequestMapping("/download.asyn")
	@ResponseBody
	public ViewResponseBean downloadFile(String type,HttpServletResponse response) {
		try {
			int tempNum = 101 ;
			if("order".equals(type)) {
				tempNum = 101 ;
			}else if("inventoryRollback".equals(type)) {
				tempNum = 103 ;
			}else if("amountCover".equals(type)) {
				tempNum = 104 ;
			}

			String fileName = URLEncoder.encode(ExcelTemplateEnum.getDesc(Integer.valueOf(tempNum)), "UTF-8");// 文件名
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
			InputStream in = ExcelTemplateEnum.getFile(ExcelTemplateEnum.getPath(Integer.valueOf(tempNum)));
			BufferedInputStream inb = new BufferedInputStream(in);
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];

			int i = inb.read(buffer);
			while (i != -1) {
				out.write(buffer, 0, i);
				i = inb.read(buffer);
			}
			out.close();
			inb.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}
		return null;
	}
}
