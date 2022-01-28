package com.topideal.webapi.main;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.service.main.GroupCommbarcodeService;
import com.topideal.webapi.form.GroupCommbarcodeResponseDTO;
import com.topideal.webapi.form.GroupGetCommbarcodeResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/webapi/system/groupCommbarcode")
@Api(tags = "组码管理 ")
public class APIGroupCommbarcodeController {
	
	@Autowired
	private GroupCommbarcodeService  groupCommbarcodeService;
	
	/*@RequestMapping("/toPage.html")
    public String toPage(Model model)throws SQLException{
        return "/derp/main/group-commbarcode-list";
    }*/
	
	/**
     * 列表所需数据
     * @param model
     * @return
     */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "commbarcode", value = "标准条码"),
			@ApiImplicitParam(name = "groupCommbarcode", value = "组码")
	})
	@PostMapping(value="/listGroupCommbarcodes.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<GroupCommbarcodeDTO> listGroupCommbarcodes(String commbarcode,String groupCommbarcode,Integer begin,Integer pageSize){
        try{
        	GroupCommbarcodeDTO model=new GroupCommbarcodeDTO();
        	model.setCommbarcode(commbarcode);
        	model.setGroupCommbarcode(groupCommbarcode);
        	model.setBegin(begin);
        	model.setPageSize(pageSize);
        	model = groupCommbarcodeService.listgroupCommbarcodesPage(model) ;
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    
    /**
     * 获取标准条码
     * @param word
     * @return
     */
	@ApiOperation(value = "获取标准条码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "commbarcode", value = "标准条码", required = true)
	})
	@PostMapping(value="/getCommbarcode.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<GroupGetCommbarcodeResponseDTO> getCommbarcode(String commbarcode) {    	
    	try {
			List<CommbarcodeModel> list = groupCommbarcodeService.getCommbarcode(commbarcode) ;		
				
			GroupGetCommbarcodeResponseDTO responseDTO=new GroupGetCommbarcodeResponseDTO();
        	responseDTO.setList(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功
    	} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
    	
    	
    }
    
    /**
     * 保存、修改数据
     * @param model
     * @return
     */
	@ApiOperation(value = "保存、修改数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id新增非必填,修改必填"),
			@ApiImplicitParam(name = "commbarcode", value = "标准条码", required = true),
			@ApiImplicitParam(name = "groupName", value = "组品名", required = true),
			@ApiImplicitParam(name = "groupCommbarcode", value = "标识组码")
			
	})
	@PostMapping(value="/saveGroupCommbarcodes.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean saveGroupCommbarcodes(Long id,String commbarcode,
    		String groupCommbarcode,String groupName){
        try{       	
        	boolean flag = false ;
        	GroupCommbarcodeDTO model=new GroupCommbarcodeDTO();
        	model.setId(id);
        	model.setCommbarcode(commbarcode);
        	model.setGroupCommbarcode(groupCommbarcode);
        	model.setGroupName(groupName);       	
        	if(model.getId() == null) {
        		flag = groupCommbarcodeService.save(model) ;
        	}else {
        		flag = groupCommbarcodeService.modify(model) ;
        	}        	
        	if(!flag) {
        		return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
        	}
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);//成功;   
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    
    /**
     * 查询详情
     * @param model
     * @return
     */
	@ApiOperation(value = "查询详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id"),
		@ApiImplicitParam(name = "commbarcode", value = "标准条码"),
		@ApiImplicitParam(name = "groupName", value = "组品名"),
		@ApiImplicitParam(name = "groupCommbarcode", value = "标识组码")		
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<GroupCommbarcodeResponseDTO> detail(Long id,String commbarcode,
    		String groupCommbarcode,String groupName){
        try{
        	GroupCommbarcodeDTO model=new GroupCommbarcodeDTO();
        	model.setId(id);
        	model.setCommbarcode(commbarcode);
        	model.setGroupCommbarcode(groupCommbarcode);
        	model.setGroupName(groupName); 
        	List<GroupCommbarcodeDTO> list = groupCommbarcodeService.getDetailList(model) ;
        	
        	GroupCommbarcodeResponseDTO responseDTO=new GroupCommbarcodeResponseDTO();
        	responseDTO.setList(list);
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);//成功;
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

    }
    
    /**
     * 删除
     * @param model
     * @return
     */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id",required = true)	
	})
	@PostMapping(value="/delete.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean delete(String id){
        try{       	
        	if(StringUtils.isNotBlank(id)) {
        		groupCommbarcodeService.delete(id) ;
        	}
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功;
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
}
