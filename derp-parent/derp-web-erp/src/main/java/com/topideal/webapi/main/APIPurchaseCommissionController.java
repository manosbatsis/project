package com.topideal.webapi.main;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.PurchaseCommissionDTO;
import com.topideal.entity.vo.main.PurchaseCommissionModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.PurchaseCommissionService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/webapi/system/purchaseCommission")
@Api(tags = "加价比例配置")
public class APIPurchaseCommissionController {
	
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PurchaseCommissionService purchaseCommissionService ;

	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token) throws Exception {
		try {
			User user = ShiroUtils.getUserByToken(token);
			List<SelectBean> supplierBean = customerService.getSelectBeanBySupplier(user.getMerchantId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,supplierBean);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户ID", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商ID", required = true),
			@ApiImplicitParam(name = "saleRebate", value = "销售回扣", required = true),
			@ApiImplicitParam(name = "purchaseCommission", value = "采购价佣金", required = true),
			@ApiImplicitParam(name = "configType", value = "配置类型  1-采购执行比例 2-公司加价比例", required = true)
	})
	@PostMapping(value="/savePurchaseCommission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean savePurchaseCommission(String token,Long customerId,Long supplierId,
			BigDecimal saleRebate,BigDecimal purchaseCommission,String configType) {
		Map<String, Object> data = new HashMap<String, Object>() ;
		try {
			PurchaseCommissionModel model=new PurchaseCommissionModel();
			model.setCustomerId(customerId);
			model.setSupplierId(supplierId);
			model.setSaleRebate(saleRebate);
			model.setPurchaseCommission(purchaseCommission);
			model.setConfigType(configType);
			User user = ShiroUtils.getUserByToken(token);
			
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
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			if(model.getSaleRebate().doubleValue() < 0.0 ||
					model.getPurchaseCommission().doubleValue() < 0.0) {
				// 小数数值小于0 时参数错误
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
			}
			
			model.setMerchantId(user.getMerchantId());
			model.setCreater(user.getId());
		
			data = purchaseCommissionService.savePurchaseCommission(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户ID"),
			@ApiImplicitParam(name = "supplierId", value = "供应商ID", required = true),
			@ApiImplicitParam(name = "configType", value = "配置类型  1-采购执行比例 2-公司加价比例", required = true)
	})
	@PostMapping(value="/listPurchaseCommission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listPurchaseCommission(String token,Long customerId,
			Long supplierId,String configType,int begin,int pageSize) {
		try{
			PurchaseCommissionDTO model=new PurchaseCommissionDTO();
			model.setCustomerId(customerId);
			model.setSupplierId(supplierId);
			model.setConfigType(configType);
			model.setBegin(begin);
			model.setPageSize(pageSize);
			User user = ShiroUtils.getUserByToken(token);
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			model = purchaseCommissionService.listPurchaseCommission(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	

	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/delPurchaseCommission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delPurchaseCommission(String id) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(id);
            boolean b = purchaseCommissionService.delPurchaseCommission(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 获取单个对象
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取单个对象")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getPurchaseCommission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getPurchaseCommission(String id) {
        
		PurchaseCommissionModel model = null ;
		
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(id);
            if(!isRight){
            	//输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            
            List list = StrUtils.parseIds(id);
            if(list.size() > 1) {
            	//只允许一个ID值
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
            }
            
            model = purchaseCommissionService.getPurchaseCommission(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyPurchaseCommission.asyn")
	@ApiOperation(value = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户ID", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商ID", required = true),
			@ApiImplicitParam(name = "saleRebate", value = "销售回扣", required = true),
			@ApiImplicitParam(name = "purchaseCommission", value = "采购价佣金", required = true),
			@ApiImplicitParam(name = "configType", value = "配置类型  1-采购执行比例 2-公司加价比例", required = true)
	})
	@PostMapping(value="/modifyPurchaseCommission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyPurchaseCommission(String token,Long id,Long customerId,Long supplierId,
			BigDecimal saleRebate,BigDecimal purchaseCommission,String configType) {
		
		Map<String, Object> data = new HashMap<String, Object>() ;
		
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            PurchaseCommissionModel model=new PurchaseCommissionModel();
			model.setCustomerId(customerId);
			model.setSupplierId(supplierId);
			model.setSaleRebate(saleRebate);
			model.setPurchaseCommission(purchaseCommission);
			model.setConfigType(configType);
			model.setId(id);
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getCustomerId())
					.addObject(model.getPurchaseCommission())
					.addObject(model.getSaleRebate())
					.addObject(model.getSupplierId())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			if(model.getSaleRebate().doubleValue() < 0.0 ||
					model.getPurchaseCommission().doubleValue() < 0.0) {
				// 小数数值小于0 时参数错误
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
			}
			
			User user = ShiroUtils.getUserByToken(token);
            // 设置商家id
         	model.setMerchantId(user.getMerchantId());
         	model.setModifier(user.getId());
            data = purchaseCommissionService.modifyPurchaseCommission(model);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
}
