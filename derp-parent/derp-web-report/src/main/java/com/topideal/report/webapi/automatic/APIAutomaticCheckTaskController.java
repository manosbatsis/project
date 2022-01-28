package com.topideal.report.webapi.automatic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.topideal.common.tools.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.report.service.automatic.AutomaticCheckTaskService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.AutomaticCheckTaskForm;
import com.topideal.report.webapi.form.AutomaticCheckTaskSaveCheckForm;
import com.topideal.report.webapi.form.AutomaticCheckTaskToAddResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping("/webapi/report/automaticCheckTask")
@Api(tags = "自动检验任务")
public class APIAutomaticCheckTaskController {
	
	@Autowired
	public AutomaticCheckTaskService automaticCheckTaskService;
	
	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchantShopRelMongo> toPage(String token) throws Exception {

		try {
			User user=ShiroUtils.getUserByToken(token); 
			List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),null,null);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,shopList);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}	
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
    @PostMapping(value="/listAutomaticCheckTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<AutomaticCheckTaskDTO> listAutomaticCheckTask(AutomaticCheckTaskForm form) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUserByToken(form.getToken()); 
			AutomaticCheckTaskDTO dto=new AutomaticCheckTaskDTO();
			dto.setMerchantId(user.getMerchantId());
			dto.setTaskType(form.getTaskType());
			dto.setCreateName(form.getCreateName());
			dto.setCheckStartDate(form.getCheckStartDate());
			dto.setCheckEndDate(form.getCheckEndDate());
			dto.setCheckResult(form.getCheckResult());
			dto.setState(form.getState());
			dto.setCreateDate(TimeUtils.parseDay(form.getCreateDate()));
			dto.setOutDepotId(form.getOutDepotId());
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setShopCode(form.getShopCode());
			dto.setTaskCode(form.getTaskCode());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());

			
			dto = automaticCheckTaskService.listAutomaticCheckTaskByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		//return ResponseFactory.success();
	}
	/**
	 * 改变电商平台时同时改变店铺的下拉选项
	 * */
	@ApiOperation(value = "改变电商平台时同时改变店铺的下拉选项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "storePlatformCode", value = "平台编码"),
			@ApiImplicitParam(name = "outDepotId", value = "仓库id")
	})
	@PostMapping(value="/changeShopCodeLabel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AutomaticCheckTaskToAddResponseDTO> changeShopCodeLabel(String token,String storePlatformCode,Integer outDepotId) throws Exception {
		try{
			User user=ShiroUtils.getUserByToken(token); 
			List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),storePlatformCode,outDepotId);
			AutomaticCheckTaskToAddResponseDTO responseDTO=new  AutomaticCheckTaskToAddResponseDTO();
			responseDTO.setShopList(shopList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		
	}	
	/**
	 * POP核对任务创建
	 * */
	@ApiOperation(value = "跳转POP核对任务创建")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toAddPOPPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AutomaticCheckTaskToAddResponseDTO> toAddPOPPage(String token)  throws Exception {
		try{
			User user=ShiroUtils.getUserByToken(token); 
			List<MerchantShopRelMongo> shopList = automaticCheckTaskService.getMerchantShopRelList(user.getMerchantId(),null,null);
			AutomaticCheckTaskToAddResponseDTO responseDTO=new  AutomaticCheckTaskToAddResponseDTO();
			responseDTO.setShopList(shopList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	


	/**
	 * 查看某个任务的详情
	 * */
	@ApiOperation(value = "查看某个任务的详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/listTaskDetailsById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<AutomaticCheckTaskDTO> listTaskDetailsById(String id)throws Exception{
		AutomaticCheckTaskDTO dto =new AutomaticCheckTaskDTO();
		try{
			Long taskId=null;
			if(StringUtils.isNotBlank(id)){
				taskId = Long.valueOf(id);
			}
			dto = automaticCheckTaskService.searchDetail(taskId);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 标识核对结果
	 * */
	@ApiOperation(value = " 标识核对结果")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id 多余英文逗号隔开",required = true),
			@ApiImplicitParam(name = "checkResult", value = "核对结果 0:未对平 1:已对平",required = true),
			@ApiImplicitParam(name = "remark", value = "核对备注")
	})
	@PostMapping(value="/modifyCheckResult.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyCheckResult(String token,String ids,String checkResult,String remark) {
		String msg=null;
		try{
			User user=ShiroUtils.getUserByToken(token); 
			if (ids==null || checkResult==null) {
				  //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			msg = automaticCheckTaskService.modifyCheckResult(ids,checkResult,remark,user.getId(),user.getName());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取仓库信息(商家维度)
	 * @param dataSource
	 * @param depotCode
	 * @return
	 */
	@ApiOperation(value = "获取仓库信息(商家维度)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "dataSource", value = "数据源 1：GSS报表 2：菜鸟后台"),
			@ApiImplicitParam(name = "depotCode", value = "仓库编码")
	})
	@PostMapping(value="/getDepotByDataSource.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<DepotInfoModel>> getDepotByDataSource(String token,String dataSource, String depotCode) {
		try{
			User user=ShiroUtils.getUserByToken(token); 
			List<DepotInfoModel>  depotList = automaticCheckTaskService.getDepotByDataSource(dataSource, depotCode, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,depotList);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	

	@ApiOperation(value = "生成核对数据")
	@PostMapping(value="/saveCheckResult.asyn",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseBean saveCheckResult(AutomaticCheckTaskSaveCheckForm form , @RequestParam(value = "file", required = false)MultipartFile file) {

        try{
			if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_1.equals(form.getTaskType())) {
				if (form.getOutDepotId() ==null || form.getStorePlatformCode() ==null || form.getShopCode() ==null || 
						(form.getCheckStartDate()==null && form.getCheckEndDate()==null) || file == null ) {
					  //输入信息不完整
					 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
				}
			}else if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_2.equals(form.getTaskType())) {
				if (form.getOutDepotId() ==null || 
						(form.getCheckStartDate()==null && form.getCheckEndDate()==null)
						|| form.getDataSource() == null || file == null ) {
					  //输入信息不完整
					 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
				}
			}else if(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_3.equals(form.getTaskType())) {
				if(file == null) {
					 return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
				}
			}
			AutomaticCheckTaskDTO dto =new AutomaticCheckTaskDTO();
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setMerchantName(user.getMerchantName());
            dto.setCreateName(user.getName());
            dto.setCreater(user.getId());
            dto.setStorePlatformCode(form.getStorePlatformCode());
            dto.setShopCode(form.getShopCode());
            dto.setOutDepotId(form.getOutDepotId());
            dto.setCheckStartDate(form.getCheckStartDate());
            dto.setCheckEndDate(form.getCheckEndDate());
            dto.setTaskType(form.getTaskType());
            dto.setDataSource(form.getDataSource());
           
            String  taskCode = automaticCheckTaskService.saveCheckResult(dto , file);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,taskCode);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        
	}
	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id")
	})
	@GetMapping(value="/downLoad.asyn")	
	private ResponseBean downLoad(Integer id,HttpServletResponse response)throws Exception{
		try{
            //查询该任务
			if(id==null){
				  //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
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
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
            
        }

	}
	/**
	 * 删除
	 * */

	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "ids", required = true)
	})
	@PostMapping(value="/delAutomaticCheckTask.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delAutomaticCheckTask(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            List list = StrUtils.parseIds(ids);
            boolean b = automaticCheckTaskService.delAutomaticCheckTask(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常            	
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
}
