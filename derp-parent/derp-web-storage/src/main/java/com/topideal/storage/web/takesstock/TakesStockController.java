package com.topideal.storage.web.takesstock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.TakesStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.TakesStockFrom;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.entity.vo.TakesStockModel;
import com.topideal.storage.service.takesstock.TakesStockItemService;
import com.topideal.storage.service.takesstock.TakesStockService;
import com.topideal.storage.shiro.ShiroUtils;

/**
 * 盘点指令
 * @author yucaifu
 */
@RequestMapping("/takesstock")
@Controller
public class TakesStockController {
	
	@Autowired
	public TakesStockService takesStockService;
	
	@Autowired
	public TakesStockItemService takesStockItemService;

	/**
	 * 访问列表页面
	 * @param model
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception{
		return "derp/takesstock/takesstock-list";
	}
	
	/**
	 * 获取分页数据
	 * @param dto
	 * */
	@RequestMapping("/listTakesStock.asyn")
	@ResponseBody
	private ViewResponseBean listTakesStock(TakesStockDTO dto) {
		try{
			User user = ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			
			// 响应结果集
			dto = takesStockService.listTakesStockPage(dto);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 访问新增页面
	 * @param model
	 * */
	@RequestMapping("/toAdd.html")
	public String toAdd(Model model) throws Exception{
		User user = ShiroUtils.getUser();
		model.addAttribute("user", user);
		return "/derp/takesstock/takesstock-add";
	}
	
	/**
	 * 新增盘点指令
	 * */
	@RequestMapping("/saveTakesStock.asyn")
	@ResponseBody
	public ViewResponseBean saveTakesStock(@RequestBody TakesStockFrom model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			User user = ShiroUtils.getUser();
			retMap = takesStockService.saveTakesstock(model, user.getId(), user.getName(), user.getMerchantId(), user.getMerchantName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * */
	@RequestMapping("/toEdit.html")
	public String toEdit(Model model,String id) throws Exception{
		User user = ShiroUtils.getUser();

		//查询盘点申请
		TakesStockModel takesStock = takesStockService.queryById(id);
		
		//查询盘点申请详情
		TakesStockItemModel param = new TakesStockItemModel();
		param.setTakesStockId(takesStock.getId());
		List<TakesStockItemModel> itemList = takesStockItemService.list(param);
		
		model.addAttribute("takesStock", takesStock);
		model.addAttribute("itemList", itemList);
		model.addAttribute("user", user);
		return "/derp/takesstock/takesstock-edit";
	}
	/**
	 * 编辑盘点指令
	 * */
	@RequestMapping("/updateTakesStock.asyn")
	@ResponseBody
	public ViewResponseBean updateTakesStock(@RequestBody TakesStockFrom model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			retMap = takesStockService.updateTakesstock(model);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}
	
	/**
	 * 访问详情页面
	 * @param model
	 * */
	@RequestMapping("/toDetailPage.html")
	public String toDetailPage(Model model,Long id) throws Exception{
		
		//查询盘点申请
		TakesStockDTO takesStock = takesStockService.queryDTOById(id);
		
		//查询盘点申请详情
		TakesStockItemModel param = new TakesStockItemModel();
		param.setTakesStockId(takesStock.getId());
		List<TakesStockItemModel> itemList = takesStockItemService.list(param);
		
		model.addAttribute("takesStock", takesStock);
		model.addAttribute("itemList", itemList);
		return "/derp/takesstock/takesstock-detail";
	}
	
	/**
	 * 删除
	 * @param ids
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/delTakesStockBatch.asyn")
	@ResponseBody
	public ViewResponseBean delTakesStockBatch(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = takesStockService.delTakesStockBatch(list);
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
     * 提交盘点申请-发送盘点指令
     */
	@RequestMapping("/sendtakesStock.asyn")
	@ResponseBody
    public ViewResponseBean sendtakesStock(String ids){
		Map<String, String> data = new HashMap<String, String>();
		try{
			//校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if(!isRight){
	            //输入信息不完整
	            return ResponseFactory.error(StateCodeEnum.ERROR_303);
	        }
	        User user = ShiroUtils.getUser();
			
			//发送调拨
			String failCode = takesStockService.updateSendtakesStock(user.getId(), user.getName(), user.getTopidealCode(), ids);
	    	data.put("failCode",failCode);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		 }	
		 return ResponseFactory.success(data);	
    }

}
