package com.topideal.order.web.sale;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.order.exception.NCException;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * NC收支费项 controller
 * 
 */
@RequestMapping("/receivePaymentSubject")
@Controller
public class ReceivePaymentSubjectController {

	@Autowired
	private ReceivePaymentSubjectService receivePaymentSubjectService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model, HttpSession session) throws Exception {
		return "/derp/sale/receive_payment_subject-list";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listReceivePaymentSubject.asyn")
	@ResponseBody
	private ViewResponseBean listReceivePaymentSubject(ReceivePaymentSubjectModel model, HttpSession session) {
		try{
			// 响应结果集
			model = receivePaymentSubjectService.listReceivePaymentSubjectByPage(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 保存
	 * */
	@RequestMapping("/saveNcPay.asyn")
	@ResponseBody
	public ViewResponseBean saveNcPay(String name,String code, String subCode, String subName,HttpSession session) {
		String msg = null;
		try{
			if(name == null || code == null || subCode == null || subName == null ||
					StringUtils.isBlank(name) || StringUtils.isBlank(code) || StringUtils.isBlank(subCode) || StringUtils.isBlank(subName)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user = ShiroUtils.getUser();
			msg = receivePaymentSubjectService.saveReceivePaymentSubject(name, code, subCode, subName,user);
		}catch(NCException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_320,e);
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
	 * 编辑修改
	 * */
	@RequestMapping("/modifyNcPay.asyn")
	@ResponseBody
	public ViewResponseBean modifyNcPay(String id, String name,String code, String subCode, String subName,HttpSession session) {
		String msg = null;
		try{
			if(name == null || code == null || subCode == null || subName == null ||
					StringUtils.isBlank(name) || StringUtils.isBlank(code) || StringUtils.isBlank(subCode) || StringUtils.isBlank(subName)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			msg = receivePaymentSubjectService.modifyReceivePaymentSubject(id, name, code, subCode, subName);/*,user.getId(),user.getName()*/
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
	 * 启用/停用
	 * @param id
	 * @return
	 */
	@RequestMapping("/enableNcPay.asyn")
	@ResponseBody
	public ViewResponseBean enableNcPay(Long id, String type){
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			boolean b = receivePaymentSubjectService.enableNcPay(id, type);
			if(!b){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 编辑回显
	 */
	@RequestMapping("/toEditPage.html")
	@ResponseBody
	public ViewResponseBean toEditPage(Long id) throws Exception {
		ReceivePaymentSubjectModel model = receivePaymentSubjectService.getById(id);
		return ResponseFactory.success(model);
	}
}