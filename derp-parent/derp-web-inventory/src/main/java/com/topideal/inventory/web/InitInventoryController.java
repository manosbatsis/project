package com.topideal.inventory.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.inventory.service.InitInventoryService;
import com.topideal.inventory.shiro.ShiroUtils;

/**
 * 库存-期初库存控制层
 */
@RequestMapping("/initInventory")
@Controller
public class InitInventoryController {

	// 库存信息service
	@Autowired
	private InitInventoryService initInventoryService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model)throws Exception  {
		
		return "/inventory/initInventory-list";
	}

	/**
	 * 访问新增页面
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage() {
		return "/inventory/initInventory-add";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInitInventory.asyn")
	@ResponseBody
	private ViewResponseBean listInitInventory(HttpSession session,InitInventoryDTO model) {
		InitInventoryDTO initInventoryDTO=null;
		try{
			//model.setPageSize(100);
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			initInventoryDTO = initInventoryService.listInitInventory(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(initInventoryDTO);
	}

	/**
	 * 删除
	 * */
	@RequestMapping("/delInitInventory.asyn")
	@ResponseBody
	public ViewResponseBean delInitInventory(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = initInventoryService.delInitInventory(list);
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
	 * 导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(HttpSession session,Model model){
		User user= ShiroUtils.getUser();
		model.addAttribute("merchantId", user.getMerchantId());
		return "/inventory/initInventory-import";
	}
	
	/**
	 * 导入库存期初（自己或代理商家下的商品）
	 * */
	@RequestMapping("/importInitInventory.asyn")
	@ResponseBody
	public ViewResponseBean importInitInventory(HttpSession session,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();//返回的结果集
		try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
			User user= ShiroUtils.getUser();
			resultMap = initInventoryService.importInitInventory(data,user.getId(),user.getMerchantId(),user.getMerchantName(),user.getTopidealCode());
        }catch(NullPointerException e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
	}
	

	/**
	 * 确认库存期初
	 * */
	@RequestMapping("/confirmInitInventory.asyn")
	@ResponseBody
	public ViewResponseBean confirminitInventory() {
		try {
			initInventoryService.confirmInitInventory();
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 确认期初建账
	 * */
	@RequestMapping("/toConfirmPage.asyn")
	@ResponseBody
	public ViewResponseBean toConfirmPage(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = initInventoryService.auditInitInventory(list);
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
	 * 确认和保存
	 * */
	@RequestMapping("/saveInitInventory.asyn")
	@ResponseBody
	public ViewResponseBean saveInitInventory(String jsonObj) {
		Map resultMap = new HashMap();//返回的结果集
		try{
            if(StringUtils.isBlank(jsonObj)){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            resultMap= initInventoryService.saveInitInventory(jsonObj);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
	}
	
	
	
}
