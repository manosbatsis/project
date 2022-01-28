package com.topideal.inventory.webapi;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.entity.dto.InitInventoryVo;
import com.topideal.inventory.service.InitInventoryService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.BInitInventorySaveResponseDTO;
import com.topideal.inventory.webapi.form.InitInventoryForm;
import com.topideal.inventory.webapi.form.InitInventorySaveForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 库存-期初库存控制层
 */
@RestController
@RequestMapping("/webapi/inventory/initInventory")
@Api(tags = "库存-期初库存控制层")
public class APIInitInventoryController {

	// 库存信息service
	@Autowired
	private InitInventoryService initInventoryService;



	

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listInitInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InitInventoryDTO> listInitInventory(InitInventoryForm form) {
		try{
			InitInventoryDTO dto=new InitInventoryDTO();
			BeanUtils.copyProperties(form, dto);			
			User user= ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto = initInventoryService.listInitInventory(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
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
			@ApiImplicitParam(name = "ids", value = "id是集合 多个用英文逗号隔开")
	})
	@PostMapping(value="/delInitInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean delInitInventory(String token,String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = initInventoryService.delInitInventory(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

	}

	/**
	 * 导入页面
	 * */

	@ApiOperation(value = " 导入页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toImportPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean toImportPage(String token){
		try {
			User user= ShiroUtils.getUserByToken(token);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,user.getMerchantId());
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 导入库存期初（自己或代理商家下的商品）
	 * */
	@ApiOperation(value = "导入库存期初")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importInitInventory.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importInitInventory(String token,@RequestParam(value = "file", required = false) MultipartFile file) {		
		try{
            if(file==null){
                //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			Map resultMap = initInventoryService.importInitInventory11(data,user.getId(),user.getMerchantId(),user.getMerchantName(),user.getTopidealCode());
		//	List<Map<String, String>> msgList = (List<Map<String, String>>) resultMap.get("msgList");// 检查失败消息
			Integer success = (Integer) resultMap.get("success");// 检查成功条数
			Integer failure = (Integer) resultMap.get("failure");// 检查失败条数
			List<InitInventoryVo> list = (List<InitInventoryVo>) resultMap.get("list");// 检查失败条数
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			uploadResponse.setData(list);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	

	/**
	 * 确认库存期初
	 * */
	/*
	 * @RequestMapping("/confirmInitInventory.asyn")
	 * 
	 * @ResponseBody public ViewResponseBean confirminitInventory() { try {
	 * initInventoryService.confirmInitInventory(); } catch (SQLException e) {
	 * return ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
	 * (NullPointerException e) { return
	 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
	 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
	 * ResponseFactory.success(); }
	 */
	
	/**
	 * 确认期初建账
	 * */
	/*
	 * @RequestMapping("/toConfirmPage.asyn")
	 * 
	 * @ResponseBody public ViewResponseBean toConfirmPage(String ids) { try{
	 * //校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if(!isRight){
	 * //输入信息不完整 return ResponseFactory.error(StateCodeEnum.ERROR_303); } List list
	 * = StrUtils.parseIds(ids); boolean b =
	 * initInventoryService.auditInitInventory(list); if(!b){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_301); } }catch(SQLException e){
	 * return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
	 * }catch(NullPointerException e){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
	 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
	 * ResponseFactory.success(); }
	 */
	
	
	/**
	 * 确认和保存
	 * */

	@ApiOperation(value = "确认和保存")
	@PostMapping(value="/saveInitInventory.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<BInitInventorySaveResponseDTO> saveInitInventory(@RequestBody InitInventorySaveForm form ){
		
		
		try{
			List<InitInventoryVo> list = form.getList();
            if(list==null||list.size()==0){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            Map   resultMap= initInventoryService.saveInitInventory11(form);
            Integer initSuccess = (Integer) resultMap.get("initSuccess");
            Integer initFailure = (Integer) resultMap.get("initFailure");
            Integer innventorySuccess = (Integer) resultMap.get("innventorySuccess");
            Integer innventoryFailure = (Integer) resultMap.get("innventoryFailure");
            Integer updteSuccess = (Integer) resultMap.get("updteSuccess");
            Integer updteFailure = (Integer) resultMap.get("updteFailure");
            BInitInventorySaveResponseDTO responseDTO=new BInitInventorySaveResponseDTO();
            responseDTO.setInitSuccess(initSuccess);
            responseDTO.setInitFailure(initFailure);
            responseDTO.setInnventorySuccess(innventorySuccess);
            responseDTO.setInnventoryFailure(innventoryFailure);
            responseDTO.setUpdteSuccess(updteSuccess);
            responseDTO.setUpdteFailure(updteFailure);
  
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
            
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	
	
}
