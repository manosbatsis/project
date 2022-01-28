package com.topideal.web.derp.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.service.base.ReptileConfigService;
import com.topideal.service.main.DepotService;
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


/**
 * 爬虫配置 控制层
 * @author lian_
 */
@RequestMapping("/reptile")
@Controller
public class ReptileConfigController {

	@Autowired
	private ReptileConfigService reptileConfigService;
	// 仓库信息service
	@Autowired
	private DepotService depotService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/reptile-list";
	}
	
	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		ReptileConfigDTO reptileConfigModel = reptileConfigService.searchDetail(id);
		model.addAttribute("detail", reptileConfigModel);
		return "/derp/base/reptile-details";
	}
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long merchantId) throws SQLException {
		List<MerchantInfoModel> merchantBean = reptileConfigService.getMerchant();
		model.addAttribute("merchantBean", merchantBean);
		List<SelectBean> customer = reptileConfigService.getSelectBean(merchantId);
		List<MerchantInfoModel> isPoxy = depotService.getSelectPoxy();
		model.addAttribute("isPoxy", isPoxy);
		model.addAttribute("customer", customer);
		return "/derp/base/reptile-add";
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id) throws Exception {
		List<MerchantInfoModel> merchantBean = reptileConfigService.getMerchant();
		ReptileConfigDTO dtoModel = reptileConfigService.searchDetail(id);
		List<SelectBean> customerList = reptileConfigService.getSelectBean(dtoModel.getMerchantId());
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("detail", dtoModel);
		model.addAttribute("merchantBean", merchantBean);
		return "/derp/base/reptile-edit";
	}
	
	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@RequestMapping("/listReptile.asyn")
	@ResponseBody
	private ViewResponseBean listReptile(ReptileConfigDTO dto) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = reptileConfigService.getListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 新增
	 * */
	@RequestMapping("/saveReptile.asyn")
	@ResponseBody
	public ViewResponseBean saveReptile(ReptileConfigModel model) {
		Map<String, Object> retMap = null;
		try{
			retMap = reptileConfigService.saveReptile(model);
		} catch (Exception e) {
			retMap.put("code", "99");
			retMap.put("message", "保存失败");
			e.printStackTrace();
		}
		return ResponseFactory.success(retMap);
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyReptile.asyn")
	@ResponseBody
	public ViewResponseBean modifyReptile(ReptileConfigModel model) {
		Map<String, Object> retMap = null;
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
            	retMap.put("code", "99");
    			retMap.put("message", "保存失败");
    			return ResponseFactory.success(retMap);
            }
            
            retMap = reptileConfigService.modifyReptile(model);

        }catch(Exception e){
        	retMap.put("code", "99");
			retMap.put("message", "保存失败");
			e.printStackTrace();
            
        }
        return ResponseFactory.success(retMap);
	}
	
	/**
	 * 根据ID获取商品详情
	 */
	@RequestMapping("/getReptileDetails.asyn")
	@ResponseBody
	public ViewResponseBean getReptileDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		ReptileConfigDTO model = new ReptileConfigDTO();
		try {
			model = reptileConfigService.searchDetail(id);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delReptile.asyn")
	@ResponseBody
	public ViewResponseBean delReptile(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = reptileConfigService.delReptile(list);
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
	 * 商家客户关联查询
	 * @param model
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryReptile.asyn")
	@ResponseBody
	public ViewResponseBean queryReptile(ReptileConfigModel model, Long merchantId)throws Exception{
		List<SelectBean> customerList=null;
		if(merchantId != null){
			customerList = reptileConfigService.getSelectBean(merchantId);
			if (customerList==null||customerList.size()==0) {
				return ResponseFactory.error(StateCodeEnum.ERROR_306);
			}	
		}
		Map<String, Object> map = new  HashMap<>();
		map.put("customerList", customerList);
		return ResponseFactory.success(map);
	}
	
	/**
	 * 查询店铺下拉列表
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/getShopList.asyn")
	@ResponseBody
	public ViewResponseBean getShopList(Long merchantId) {
		
		try {
			List<MerchantShopRelModel> relList = reptileConfigService.getShopList(merchantId) ;
			
			return ResponseFactory.success(relList);
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		}
	}
	


}
