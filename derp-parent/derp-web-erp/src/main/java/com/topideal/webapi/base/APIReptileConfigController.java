package com.topideal.webapi.base;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.service.base.ReptileConfigService;
import com.topideal.service.main.DepotService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.ReptileConfigForm;
import com.topideal.webapi.form.ReptileToAddResponseDTO;
import com.topideal.webapi.form.ReptileToEditResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 爬虫配置 控制层
 * @author lian_
 */
@RestController
@RequestMapping("/webapi/system/reptile")
@Api(tags = "爬虫配置列表")
public class APIReptileConfigController {

	@Autowired
	private ReptileConfigService reptileConfigService;
	// 仓库信息service
	@Autowired
	private DepotService depotService;
	
	/**
	 * 访问列表页面
	 * */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/reptile-list";
	}*/
	
	/**
	 * 访问详情页面
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ReptileConfigDTO> toDetailsPage(Long id) throws Exception {
		try {
			ReptileConfigDTO reptileConfigModel = reptileConfigService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,reptileConfigModel);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@ApiOperation(value = "访问新增页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "merchantId")
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ReptileToAddResponseDTO> toAddPage(Long merchantId) throws SQLException {
		try {
			ReptileToAddResponseDTO response=new ReptileToAddResponseDTO();
			List<MerchantInfoModel> merchantBean = reptileConfigService.getMerchant();
			response.setMerchantBean(merchantBean);
			List<SelectBean> customer = reptileConfigService.getSelectBean(merchantId);
			List<MerchantInfoModel> isPoxy = depotService.getSelectPoxy();
			response.setIsPoxy(isPoxy);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ReptileToEditResponseDTO> toEditPage(Long id) throws Exception {
		try {
			ReptileToEditResponseDTO response=new ReptileToEditResponseDTO();
			List<MerchantInfoModel> merchantBean = reptileConfigService.getMerchant();
			ReptileConfigDTO dtoModel = reptileConfigService.searchDetail(id);
			List<SelectBean> customerList = reptileConfigService.getSelectBean(dtoModel.getMerchantId());			
			response.setMerchantBean(merchantBean);
			response.setDetail(dtoModel);
			response.setCustomerList(customerList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "platformType", value = "使用平台"),
			@ApiImplicitParam(name = "loginName", value = "用户名"),
			@ApiImplicitParam(name = "shopId", value = "店铺id"),			
			@ApiImplicitParam(name = "createDate", value = "创建时间")
	})
	@PostMapping(value="/listReptile.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ReptileConfigDTO> listReptile(String token,Long shopId,String platformType,String loginName,
			Integer begin,Integer pageSize) {
		try{
			User user = ShiroUtils.getUserByToken(token);
			// 设置商家id
			ReptileConfigDTO dto=new ReptileConfigDTO();
			dto.setPlatformType(platformType);
			dto.setLoginName(loginName);
			//dto.setCreateDate(createDate);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto.setShopId(shopId);
			
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = reptileConfigService.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增")
	@PostMapping(value="/saveReptile.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveReptile(ReptileConfigForm form) {
		try{
			ReptileConfigModel model= new ReptileConfigModel();
			model.setPlatformType(form.getPlatformType());
			model.setLoginName(form.getLoginName());
			model.setTimePoint(form.getTimePoint());
			model.setMerchantId(form.getMerchantId());
			model.setCustomerId(form.getCustomerId());
			model.setOutDepotId(form.getOutDepotId());
			model.setInDepotId(form.getInDepotId());				
			model.setShopId(form.getShopId());
			Map<String, Object> retMap = reptileConfigService.saveReptile(model);
			String code = (String) retMap.get("code");
			String message = (String) retMap.get("message");
			if ("00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//未知异常
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改")
	@PostMapping(value="/modifyReptile.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseBean modifyReptile(ReptileConfigForm form) {		 
		try{
			Map<String, Object> retMap=new HashMap<>();
			//校验id是否正确
            boolean isRight = StrUtils.validateId(form.getId());
            if(!isRight){
    			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
            }
            ReptileConfigModel model= new ReptileConfigModel();
            model.setId(form.getId());
			model.setPlatformType(form.getPlatformType());
			model.setLoginName(form.getLoginName());
			model.setTimePoint(form.getTimePoint());
			model.setMerchantId(form.getMerchantId());
			model.setCustomerId(form.getCustomerId());
			model.setOutDepotId(form.getOutDepotId());
			model.setInDepotId(form.getInDepotId());
			model.setShopId(form.getShopId());
			retMap = reptileConfigService.modifyReptile(model);
			String code = (String) retMap.get("code");
			String message = (String) retMap.get("message");
			if ("00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//未知异常
			}
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常            
        }
	}
	
	/**
	 * 根据ID获取商品详情
	 */
	@ApiOperation(value = "根据ID获取商品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getReptileDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ReptileConfigDTO> getReptileDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
		}
		try {
			ReptileConfigDTO model = reptileConfigService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常    
		}
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delReptile.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delReptile(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
            }
            List list = StrUtils.parseIds(ids);
            boolean b = reptileConfigService.delReptile(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常    
        }
	}
	
	/**
	 * 商家客户关联查询
	 * @param model
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "商家客户关联查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id")
	})
	@PostMapping(value="/queryReptile.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> queryReptile(Long merchantId)throws Exception{
		try {
			List<SelectBean> customerList=null;
			if(merchantId != null){
				customerList = reptileConfigService.getSelectBean(merchantId);
				if (customerList==null||customerList.size()==0) {
					return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
				}	
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerList);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常    
		}

	}
	
	/**
	 * 查询店铺下拉列表
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getShopList.asyn")
	@ApiOperation(value = "查询店铺下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家ID", required = false),
	})
	public ResponseBean<List<SelectBean>> getShopList(@RequestParam(value="token", required=true)String token,
			Long merchantId) {
		
		try {
			List<MerchantShopRelModel> relList = reptileConfigService.getShopList(merchantId) ;
			List<SelectBean> selectBeanList = new ArrayList<SelectBean>() ;
			
			for (MerchantShopRelModel shop : relList) {
				
				SelectBean select = new SelectBean() ;
				select.setLabel(shop.getShopName());
				select.setValue(shop.getId().toString());
				
				selectBeanList.add(select) ;
				
			}
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, selectBeanList);
			
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	


}
