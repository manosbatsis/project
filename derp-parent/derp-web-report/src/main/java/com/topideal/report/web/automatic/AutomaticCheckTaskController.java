package com.topideal.report.web.automatic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.report.service.automatic.AutomaticCheckTaskService;
import com.topideal.report.shiro.ShiroUtils;
/**
 * 自动检验任务
 **/
@Controller
@RequestMapping("/automaticCheckTask")
public class AutomaticCheckTaskController {
	
	@Autowired
	public AutomaticCheckTaskService automaticCheckTaskService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		User user= ShiroUtils.getUser(); 
		List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),null,null);
		model.addAttribute("shopList",shopList);
		return "derp/automatic/automaticCheckTask-list";
	}	
	
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listAutomaticCheckTask.asyn")
	@ResponseBody
	private ViewResponseBean listAutomaticCheckTask(AutomaticCheckTaskDTO dto) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			dto = automaticCheckTaskService.listAutomaticCheckTaskByPage(dto);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 改变电商平台时同时改变店铺的下拉选项
	 * */
	@RequestMapping("/changeShopCodeLabel.asyn")
	@ResponseBody
	public ViewResponseBean changeShopCodeLabel(String storePlatformCode,Integer outDepotId) throws Exception {
		User user= ShiroUtils.getUser(); 
		List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),storePlatformCode,outDepotId);
		Map<String, Object> map = new  HashMap<>();
		map.put("shopList", shopList);
		return ResponseFactory.success(map);
	}	
	/**
	 * POP核对任务创建
	 * */
	@RequestMapping("/toAddPOPPage.html")
	public String toAddPOPPage(Model model)  throws Exception {
		User user= ShiroUtils.getUser(); 
		List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),null,null);
		model.addAttribute("shopList",shopList);
		return "derp/automatic/automaticCheckTask-POP-import";
	}
	
	/**
	 * POP金额核对任务创建
	 * */
	@RequestMapping("/toAddpopAmountPage.html")
	public String toAddpopAmountPage(Model model)  throws Exception {
		return "derp/automatic/automaticCheckTask-popAmount-import";
	}

	/**
	 * 查看某个任务的详情
	 * */
	@RequestMapping("/listTaskDetailsById.html")
	@ResponseBody
	public ViewResponseBean listTaskDetailsById(String id)throws Exception{
		AutomaticCheckTaskDTO dto =new AutomaticCheckTaskDTO();
		try{
			Long taskId=null;
			if(StringUtils.isNotBlank(id)){
				taskId = Long.valueOf(id);
			}
			dto = automaticCheckTaskService.searchDetail(taskId);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 标识核对结果
	 * */
	@RequestMapping("/modifyCheckResult.asyn")
	@ResponseBody
	public ViewResponseBean modifyCheckResult(String ids,String checkResult,String remark, HttpSession session) {
		String msg=null;
		try{
			if (ids==null || checkResult==null) {
				  //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
            User user= ShiroUtils.getUser();
            msg = automaticCheckTaskService.modifyCheckResult(ids,checkResult,remark,user.getId(),user.getName());
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(RuntimeException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(msg);
	}
	/**
	 * 访问仓库检验导入页面
	 */
	@RequestMapping("toDepotCheckImportPage.html")
	public String toDepotCheckImportPage() {
		return "derp/automatic/automaticCheckTask-depot-import";
	}
	
	@RequestMapping("/getDepotByDataSource.asyn")
	@ResponseBody
	public ViewResponseBean getDepotByDataSource(String dataSource, String depotCode) {
		List<DepotInfoModel> depotList = null;
		try{
            User user= ShiroUtils.getUser();
            depotList = automaticCheckTaskService.getDepotByDataSource(dataSource, depotCode, user);
		}catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(depotList);
	}
	
	@RequestMapping("/saveCheckResult.asyn")
	@ResponseBody
	public ViewResponseBean saveCheckResult(AutomaticCheckTaskDTO dto , @RequestParam(value = "file", required = false)MultipartFile file) {
		String taskCode =null;
		try{
			if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_1.equals(dto.getTaskType())) {
				if (dto.getOutDepotId() ==null || dto.getStorePlatformCode() ==null || dto.getShopCode() ==null || 
						(dto.getCheckStartDate()==null && dto.getCheckEndDate()==null) || file == null ) {
					  //输入信息不完整
	                return ResponseFactory.error(StateCodeEnum.ERROR_303);
				}
			}else if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_2.equals(dto.getTaskType())) {
				if (dto.getOutDepotId() ==null || 
						(dto.getCheckStartDate()==null && dto.getCheckEndDate()==null)
						|| dto.getDataSource() == null || file == null ) {
					  //输入信息不完整
	                return ResponseFactory.error(StateCodeEnum.ERROR_303);
				}
			}else if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_3.equals(dto.getTaskType())) {
				if(file == null) {
					return ResponseFactory.error(StateCodeEnum.ERROR_303);
				}
			}
			
            User user= ShiroUtils.getUser();
            dto.setMerchantId(user.getMerchantId());
            dto.setMerchantName(user.getMerchantName());
            dto.setCreateName(user.getName());
            dto.setCreater(user.getId());
            
            taskCode = automaticCheckTaskService.saveCheckResult(dto , file);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(taskCode);
	}
	/**
	 * 导出
	 * */
	@RequestMapping("/downLoad.asyn")
	@ResponseBody
	private ViewResponseBean downLoad(Integer id,HttpServletResponse response)throws Exception{
		try{
            //查询该任务
			if(id==null){
				  //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			AutomaticCheckTaskDTO task = automaticCheckTaskService.searchDetail(id.longValue());
			if(StringUtils.isEmpty(task.getStorePath())){
				return null;
			}
    		File file = new File(task.getStorePath()); 
    		
    		 //转换中文否则可能会产生乱码
    		/**任务类型  1:POP流水核对 2:仓库流水核对*/
    		String downloadFilename =null;
    		if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_1)){
    			downloadFilename = URLEncoder.encode("POP流水核对.zip", "UTF-8");
    		}else if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_2)){
    			downloadFilename = URLEncoder.encode("仓库流水核对.zip", "UTF-8");
    		}else if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_3)){
    			downloadFilename = URLEncoder.encode("POP金额核对.zip", "UTF-8");
    		}
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
          	//获取模板,压缩到zip文件中
        	InputStream in = new FileInputStream(file);
        	zos.putNextEntry(new ZipEntry(file.getName()));
    	    byte[] buffer = new byte[1024];
    	    int r = 0;
    	    while ((r = in.read(buffer)) != -1) {
    	        zos.write(buffer, 0, r);
    	    }
    	    in.close();
            zos.flush();
            zos.close();
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	/**
	 * 删除
	 * */
	@RequestMapping("/delAutomaticCheckTask.asyn")
	@ResponseBody
	public ViewResponseBean delAutomaticCheckTask(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = automaticCheckTaskService.delAutomaticCheckTask(list);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(RuntimeException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
}
