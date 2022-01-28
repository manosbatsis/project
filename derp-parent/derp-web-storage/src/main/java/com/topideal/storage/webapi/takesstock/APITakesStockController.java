package com.topideal.storage.webapi.takesstock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.dto.TakesStockFrom;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.entity.vo.TakesStockModel;
import com.topideal.storage.service.takesstock.TakesStockItemService;
import com.topideal.storage.service.takesstock.TakesStockService;
import com.topideal.storage.shiro.ShiroUtils;
import com.topideal.storage.webapi.dto.ResultDTO;
import com.topideal.storage.webapi.dto.TakesStockResponseDTO;
import com.topideal.storage.webapi.form.TakesStockAddForm;
import com.topideal.storage.webapi.form.TakesStockForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi盘点指令
 * @date 2021-02-05
 */
@RestController
@RequestMapping("/webapi/storage/takesstock")
@Api(tags = "盘点指令")
public class APITakesStockController {
	
	@Autowired
	public TakesStockService takesStockService;
	
	@Autowired
	public TakesStockItemService takesStockItemService;

	/**
	 * 获取分页数据
	 * @param dto
	 * */
	@ApiOperation(value = "盘点指令列表查询")
	@PostMapping(value="/listTakesStock.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TakesStockDTO> listTakesStock(TakesStockForm form) {
		TakesStockDTO dto = new TakesStockDTO();
		try{
			User user = ShiroUtils.getUserByToken(form.getToken());			
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			dto.setMerchantName(form.getMerchantName());
			dto.setServerType(form.getServerType());
			dto.setStatus(form.getStatus());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = takesStockService.listTakesStockPage(dto);
			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 新增盘点指令
	 * */
	@ApiOperation(value = "新增盘点指令")
	@PostMapping(value="/saveTakesStock.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> saveTakesStock(TakesStockAddForm form) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, Object> retMap = new HashMap<String, Object>();
			TakesStockFrom model = new TakesStockFrom();
			model.setDepotId(form.getDepotId());
			model.setServerType(form.getServerType());
			model.setStatus(form.getStatus());
			model.setGoodsList((List<Map<String, Object>>)JSONArray.parseObject(form.getGoodsList(),List.class));
			model.setModel(form.getModel());
			model.setRemark(form.getRemark());
			User user = ShiroUtils.getUserByToken(form.getToken());
			retMap = takesStockService.saveTakesstock(model, user.getId(), user.getName(), user.getMerchantId(), user.getMerchantName());
			
			resultDTO.setCode((String)retMap.get("code"));
			resultDTO.setMessage((String)retMap.get("message"));
			
			if(retMap.get("code").equals("01")) {				
				return WebResponseFactory.responseBuild("99997",retMap.get("message").toString());//已知异常
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * */
	@ApiOperation(value = "获取编辑页面信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "盘点指令id", required = true)
	})
	@PostMapping(value="/toEdit.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<TakesStockResponseDTO> toEdit(String token,String id,Model model) throws Exception{
		TakesStockResponseDTO responseDTO = new TakesStockResponseDTO();
		try {
			TakesStockModel takesStock = takesStockService.queryById(id);
			
			//查询盘点申请详情
			TakesStockItemModel param = new TakesStockItemModel();
			param.setTakesStockId(takesStock.getId());
			List<TakesStockItemModel> itemList = takesStockItemService.list(param);
			
			responseDTO.setTakesStockModel(takesStock);
			responseDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);

	}
	/**
	 * 编辑盘点指令
	 * */
	@ApiOperation(value = "编辑盘点指令")
	@ApiImplicitParams({		
		@ApiImplicitParam(name = "id", value = "盘点指令id", required = true)
	})
	@PostMapping(value="/updateTakesStock.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> updateTakesStock(TakesStockForm form) {
		ResultDTO resultDTO = new ResultDTO();
		try {
			Map<String, Object> retMap = new HashMap<String, Object>();
			TakesStockFrom model = new TakesStockFrom();
			model.setId(form.getId());
			model.setDepotId(form.getDepotId());
			model.setServerType(form.getServerType());
			model.setStatus(form.getStatus());
			model.setGoodsList((List<Map<String, Object>>)JSONArray.parseObject(form.getGoodsList(),List.class));
			model.setModel(form.getModel());
			model.setRemark(form.getRemark());
			retMap = takesStockService.updateTakesstock(model);
			resultDTO.setCode((String)retMap.get("code"));
			resultDTO.setMessage((String)retMap.get("message"));
			
			if(retMap.get("code").equals("01")) {				
				return WebResponseFactory.responseBuild("99997",retMap.get("message").toString());//已知异常
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultDTO);
	}
	
	/**
	 * 访问详情页面
	 * @param model
	 * */
	@ApiOperation(value = "获取详情页面信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "盘点指令id", required = true)
	})
	@PostMapping(value="/toDetailPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<TakesStockResponseDTO> toDetailPage(String token,Long id,Model model) throws Exception{
		TakesStockResponseDTO responseDTO = new TakesStockResponseDTO();
		try {
			//查询盘点申请
			TakesStockDTO takesStock = takesStockService.queryDTOById(id);
			
			//查询盘点申请详情
			TakesStockItemModel param = new TakesStockItemModel();
			param.setTakesStockId(takesStock.getId());
			List<TakesStockItemModel> itemList = takesStockItemService.list(param);
			
			responseDTO.setTakesStockDTO(takesStock);
			responseDTO.setItemList(itemList);
						
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	
	/**
	 * 删除
	 * @param ids
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的盘点指令ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/delTakesStockBatch.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delTakesStockBatch(String token,String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//数据格式不对
            }
            if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
            List list = StrUtils.parseIds(ids);
            boolean b = takesStockService.delTakesStockBatch(list);
            if(!b){
            	return WebResponseFactory.responseBuild("99997","删除失败");//未知异常
            }
        }catch(Exception e){
        	e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	
	/**
     * 提交盘点申请-发送盘点指令
     */
	@ApiOperation(value = "提交盘点申请-发送盘点指令")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的盘点指令ids(多选用逗号隔开)", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=200,message="data => 指令提交失败单号")
	})
	@PostMapping(value="/sendtakesStock.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<String> sendtakesStock(String token,String ids){
		String failCode = null;
		try{
			//校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if(!isRight){
	            //输入信息不完整
	        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//数据格式不正确
	        }
	        if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
	        User user = ShiroUtils.getUserByToken(token);
			
			//发送调拨
			failCode = takesStockService.updateSendtakesStock(user.getId(), user.getName(), user.getTopidealCode(), ids);
			
			if(org.apache.commons.lang.StringUtils.isNotBlank(failCode)) {
				return WebResponseFactory.responseBuild("99997",failCode);//已知异常
			}
		 } catch (Exception e) {
			 e.printStackTrace();
			 return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		 }	
		 return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,failCode);	
    }

}
