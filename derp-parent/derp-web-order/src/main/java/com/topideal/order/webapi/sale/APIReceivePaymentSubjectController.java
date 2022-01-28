package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.ReceivePaymentSubjectForm;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * webapi NC收支费项
 * 
 */
@RequestMapping("/webapi/order/receivePaymentSubject")
@RestController
@Api(tags = "NC收支费项")
public class APIReceivePaymentSubjectController {

	@Autowired
	private ReceivePaymentSubjectService receivePaymentSubjectService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询NC收支费项列表信息")   	
   	@PostMapping(value="/listReceivePaymentSubject.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ReceivePaymentSubjectModel> listReceivePaymentSubject(ReceivePaymentSubjectForm form) {
		ReceivePaymentSubjectModel model = new ReceivePaymentSubjectModel();
		try{
			model.setName(form.getName());
			model.setSubCode(form.getSubCode());
			model.setSubName(form.getSubName());
			model.setPageSize(form.getPageSize());
			model.setBegin(form.getBegin());
			// 响应结果集
			model = receivePaymentSubjectService.listReceivePaymentSubjectByPage(model);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}

	/**
	 * 保存
	 * */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "name", value = "NC收支费项名称", required = true),
		@ApiImplicitParam(name = "code", value = "NC收支费项编码，0：停用，1：启用", required = true),
		@ApiImplicitParam(name = "subCode", value = "科目编码", required = true),
		@ApiImplicitParam(name = "subName", value = "科目名称", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="data = > 返回保存成功/失败的消息")
	})
	@PostMapping(value="/saveNcPay.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> saveNcPay(ReceivePaymentSubjectForm form) {
		String msg = null;
		try{
			if(StringUtils.isBlank(form.getName()) || StringUtils.isBlank(form.getCode()) || 
					StringUtils.isBlank(form.getSubCode()) || StringUtils.isBlank(form.getSubName())){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(form.getToken());
			msg = receivePaymentSubjectService.saveReceivePaymentSubject(form.getName(), form.getCode(), form.getSubCode(), form.getSubName(),user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 编辑修改
	 * */
	@ApiOperation(value = "编辑修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "NC收支费项id", required = true),
		@ApiImplicitParam(name = "name", value = "NC收支费项名称", required = true),
		@ApiImplicitParam(name = "code", value = "NC收支费项编码，0：停用，1：启用", required = true),
		@ApiImplicitParam(name = "subCode", value = "科目编码", required = true),
		@ApiImplicitParam(name = "subName", value = "科目名称", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="data = > 返回编辑修改成功/失败的消息")
	})
	@PostMapping(value="/modifyNcPay.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> modifyNcPay(ReceivePaymentSubjectForm form) {
		String msg = null;
		try{
			if(StringUtils.isBlank(form.getId()) || StringUtils.isBlank(form.getName()) || StringUtils.isBlank(form.getCode()) || 
					StringUtils.isBlank(form.getSubCode()) || StringUtils.isBlank(form.getSubName())){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			msg = receivePaymentSubjectService.modifyReceivePaymentSubject(form.getId(), form.getName(), form.getCode(), form.getSubCode(), form.getSubName());/*,user.getId(),user.getName()*/
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 启用/停用
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "启用/停用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
		@ApiImplicitParam(name = "type", value = "修改启用/停用状态，0：停用，1：启用", required = true)
	})
	@PostMapping(value="/enableNcPay.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean enableNcPay(String token,Long id, String type){
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			boolean b = receivePaymentSubjectService.enableNcPay(id, type);
			if(!b){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"修改状态失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 编辑回显
	 */
	@ApiOperation(value = "编辑回显")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ReceivePaymentSubjectModel> toEditPage(String token,Long id) throws Exception {
		ReceivePaymentSubjectModel model = new ReceivePaymentSubjectModel();
		try{
			model = receivePaymentSubjectService.getById(id);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}
}