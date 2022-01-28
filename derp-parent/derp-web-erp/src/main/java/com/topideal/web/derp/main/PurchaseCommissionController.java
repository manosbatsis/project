package com.topideal.web.derp.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.PurchaseCommissionService;
import com.topideal.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/purchaseCommission")
@Controller
public class PurchaseCommissionController {
	
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PurchaseCommissionService purchaseCommissionService ;

	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {

		User user = ShiroUtils.getUser();
		List<SelectBean> supplierBean = customerService.getSelectBeanBySupplier(user.getMerchantId());
		model.addAttribute("supplierBean", supplierBean);
		return "/derp/main/purchaseCommission-list";
	}
	
	@RequestMapping("/savePurchaseCommission.asyn")
	@ResponseBody
	public ViewResponseBean savePurchaseCommission(PurchaseCommissionModel model) {
		Map<String, Object> data = new HashMap<String, Object>() ;
		try {
			User user = ShiroUtils.getUser();
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getCustomerId())
					.addObject(model.getPurchaseCommission())
					.addObject(model.getSaleRebate())
					.addObject(model.getSupplierId())
					.addObject(model.getConfigType())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			if(model.getSaleRebate().doubleValue() < 0.0 ||
					model.getPurchaseCommission().doubleValue() < 0.0) {
				// 小数数值小于0 时参数错误
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			model.setMerchantId(user.getMerchantId());
			model.setCreater(user.getId());
		
			data = purchaseCommissionService.savePurchaseCommission(model);
			
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(data);
		
	}
	
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listPurchaseCommission.asyn")
	@ResponseBody
	private ViewResponseBean listPurchaseCommission(PurchaseCommissionDTO model) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			model = purchaseCommissionService.listPurchaseCommission(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/delPurchaseCommission.asyn")
	@ResponseBody
	public ViewResponseBean delPurchaseCommission(String id) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(id);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(id);
            boolean b = purchaseCommissionService.delPurchaseCommission(list);
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
	 * 获取单个对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getPurchaseCommission.asyn")
	@ResponseBody
	public ViewResponseBean getPurchaseCommission(String id) {
        
		PurchaseCommissionModel model = null ;
		
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(id);
            if(!isRight){
            	//输入信息不完整
            	return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            
            List list = StrUtils.parseIds(id);
            if(list.size() > 1) {
            	//只允许一个ID值
            	return ResponseFactory.error(StateCodeEnum.ERROR_307);
            }
            
            model = purchaseCommissionService.getPurchaseCommission(id);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(model);
        
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyPurchaseCommission.asyn")
	@ResponseBody
	public ViewResponseBean modifyPurchaseCommission(PurchaseCommissionModel model) {
		
		Map<String, Object> data = new HashMap<String, Object>() ;
		
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getCustomerId())
					.addObject(model.getPurchaseCommission())
					.addObject(model.getSaleRebate())
					.addObject(model.getSupplierId())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			if(model.getSaleRebate().doubleValue() < 0.0 ||
					model.getPurchaseCommission().doubleValue() < 0.0) {
				// 小数数值小于0 时参数错误
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
            User user= ShiroUtils.getUser();
            // 设置商家id
         	model.setMerchantId(user.getMerchantId());
         	model.setModifier(user.getId());
            data = purchaseCommissionService.modifyPurchaseCommission(model);
            
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(data);
	}
}
