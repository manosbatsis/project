package com.topideal.web.derp.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.service.main.GroupCommbarcodeService;


@Controller
@RequestMapping("groupCommbarcode")
public class GroupCommbarcodeController {
	
	@Autowired
	private GroupCommbarcodeService  groupCommbarcodeService;
	
	@RequestMapping("/toPage.html")
    public String toPage(Model model)throws SQLException{
        return "/derp/main/group-commbarcode-list";
    }
	
	/**
     * 列表所需数据
     * @param model
     * @return
     */
    @RequestMapping("/listGroupCommbarcodes.asyn")
    @ResponseBody
    private ViewResponseBean listGroupCommbarcodes(GroupCommbarcodeDTO model){
        try{
        	model = groupCommbarcodeService.listgroupCommbarcodesPage(model) ;
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);

        }

        return ResponseFactory.success(model);
    }
    
    /**
     * 获取标准条码
     * @param word
     * @return
     */
    @RequestMapping("/getCommbarcode.asyn")
    @ResponseBody
    public ViewResponseBean getCommbarcode(String commbarcode) {
    	
    	try {
			List<CommbarcodeModel> list = groupCommbarcodeService.getCommbarcode(commbarcode) ;
		
			Map<String , Object> map = new HashMap<String , Object>() ;
			map.put("list", list) ;
			
			return ResponseFactory.success(map) ;
    	} catch (Exception e) {
    		return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
    	
    	
    }
    
    /**
     * 保存、修改数据
     * @param model
     * @return
     */
    @RequestMapping("/saveGroupCommbarcodes.asyn")
    @ResponseBody
    private ViewResponseBean saveGroupCommbarcodes(GroupCommbarcodeDTO model){
        try{
        	
        	boolean flag = false ;
        	
        	if(model.getId() == null) {
        		flag = groupCommbarcodeService.save(model) ;
        	}else {
        		flag = groupCommbarcodeService.modify(model) ;
        	}
        	
        	if(!flag) {
        		return ResponseFactory.error(StateCodeEnum.ERROR_304) ;
        	}
        	
        	return ResponseFactory.success() ;
            
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

    }
    
    /**
     * 查询详情
     * @param model
     * @return
     */
    @RequestMapping("/detail.asyn")
    @ResponseBody
    private ViewResponseBean detail(GroupCommbarcodeDTO model){
        try{
        	
        	List<GroupCommbarcodeDTO> list = groupCommbarcodeService.getDetailList(model) ;
        	
        	Map<String , Object > map = new HashMap<String , Object>() ;
        	map.put("list", list) ;
        	
        	return ResponseFactory.success(map) ;
            
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

    }
    
    /**
     * 删除
     * @param model
     * @return
     */
    @RequestMapping("/delete.asyn")
    @ResponseBody
    private ViewResponseBean delete(String id){
        try{
        	
        	if(StringUtils.isNotBlank(id)) {
        		groupCommbarcodeService.delete(id) ;
        	}
            
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success() ;
    }
}
