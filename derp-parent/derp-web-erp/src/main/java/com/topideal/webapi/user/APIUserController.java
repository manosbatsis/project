package com.topideal.webapi.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.UserInfoService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.UserInfoAddForm;
import com.topideal.webapi.form.UserToEditPageResponseDTO;
import com.topideal.webapi.form.UserToPageResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * Created by weixiaolei on 2018/4/13.  
 */
@RequestMapping("/webapi/system/user")
@RestController
@Api(tags = "用户")
public class APIUserController {

	@Autowired
    private UserInfoService service;

	@Autowired
    private MerchantInfoService merchantInfoService;
	
	/**
	 *用户授权角色 ( 确定)操作 
	 * */
    @ApiOperation(value = "用户授权角色 ( 确定)操作")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "id", value = "用户id", required = true),
   			@ApiImplicitParam(name = "roleId", value = "角色id", required = true),
   	})
   	@PostMapping(value="/saveUserBindRole.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveUserBindRole( Long id,Long roleId)throws Exception {
		try {
			if (id==null) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"用户id不能为空");//未知异常
			}
			/*
			 * if (roleId==null) { return
			 * WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"角色id不能为空"
			 * );//未知异常 }
			 */
			service.saveUserBindRole(id,roleId);
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}

	}
    /**
     * 跳转到用户列表页面
     * @return
     */
    @ApiOperation(value = "跳转到用户列表页面限")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true)
   	})
   	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<UserToPageResponseDTO> toPage(String token)throws SQLException{
    	try {
    		UserToPageResponseDTO response= new UserToPageResponseDTO();
            //session 获取用户信息
        	User user = ShiroUtils.getUserByToken(token);
            //当登录用户为后台管理员
            if(user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)){
            	response.setTag("0");
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
    }
	/**
	 * 访问编辑页面
	 * */
    @ApiOperation(value = "访问编辑页面")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "id", value = "id", required = true)
   	})
   	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<UserToEditPageResponseDTO> toEditPage( Long id)throws Exception {
		try {
			UserToEditPageResponseDTO response=new UserToEditPageResponseDTO();
	        UserInfoDTO userInfoModel = service.searchDetail(id);
	        response.setDetail(userInfoModel);
	        UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
	        userMerchantRelModel.setUserId(id);
	        List<MerchantInfoModel> merchantInfoModels = merchantInfoService.getUserMerchantList(userMerchantRelModel);
	        response.setMerchantInfoModels(merchantInfoModels);
	        
	        List<UserBuRelModel> buRelList = service.getBuRel(id) ;
	        response.setBuRelList(buRelList);
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

    /**
     * 用户列表
     * @param model
     * @return
     */
    @ApiOperation(value = "用户列表")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "员工名称"),
			@ApiImplicitParam(name = "username", value = "用户名 "),
			@ApiImplicitParam(name = "tel", value = "电话号码"),
			@ApiImplicitParam(name = "disable", value = "状态 ")
   	})
   	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<UserInfoDTO> listUser(String token,String name,String username,String tel,String disable,int begin,int pageSize){
        try{
        	UserInfoDTO dto=new UserInfoDTO();
        	dto.setName(name);
        	dto.setUsername(username);
        	dto.setTel(tel);
        	dto.setDisable(disable);
        	dto.setBegin(begin);
        	dto.setPageSize(pageSize);
            //session 获取用户信息
        	User user = ShiroUtils.getUserByToken(token);
            dto=service.listUserInfo(dto,user.getUserType(),user.getId(),user.getParentId());
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

    }


    /**
     * 保存用户数据
     * @param dto
     * @return
     */
	@ApiOperation(value = "新增用户数据")
	@PostMapping(value="/saveUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveUser(UserInfoAddForm form){
    	ViewResponseBean viewResponse = null;
    	try{
    		UserInfoDTO dto=new UserInfoDTO();
    		dto.setCode(form.getCode());
    		dto.setTel(form.getTel());
    		//dto.setId(form.getId());
    		dto.setEmail(form.getEmail());
    		dto.setSex(form.getSex());
    		dto.setName(form.getName());
    		dto.setUsername(form.getUsername());
    		dto.setAccountType(form.getAccountType());
    		dto.setMerchantIds(form.getMerchantIds());
    		dto.setBuIds(form.getBuIds());
    		dto.setIds(form.getIds());
    		
    		
    		
	        //空值校验
	        boolean isNull = new EmptyCheckUtils().
	                addObject(dto.getUsername()).
	                addObject(dto.getName()).
	                addObject(dto.getTel()).
                    addObject(dto.getEmail()).
                    addObject(dto.getAccountType()).
	                empty();
	        if(isNull){
	            //输入信息不完整
	        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
	            
	        } else {
	            if (dto.getAccountType().equals(DERP_SYS.USERINFO_ACCOUNTTYPE_1) &&
                        StringUtils.isEmpty(dto.getMerchantIds())) {
	            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
                }
            }
	        //session 获取用户信息
	        User user = ShiroUtils.getUserByToken(form.getToken());
	        //存储用户信息
	        viewResponse  = service.saveUser(dto,user.getUserType(),user.getId(),user.getParentId());
	        
	        if (200==viewResponse.getState()) {
            	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),viewResponse.getMessage());//未知异常
			}

        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    /**
     * 修改用户数据
     * @param dto
     * @return
     */
	@ApiOperation(value = "修改用户数据")
	@PostMapping(value="/editUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean editUser(UserInfoAddForm form){
        ViewResponseBean viewResponse = null;
        try{
        	UserInfoDTO dto=new UserInfoDTO();
    		dto.setCode(form.getCode());
    		dto.setTel(form.getTel());
    		dto.setId(form.getId());
    		dto.setEmail(form.getEmail());
    		dto.setSex(form.getSex());
    		dto.setName(form.getName());
    		dto.setUsername(form.getUsername());
    		dto.setAccountType(form.getAccountType());
    		dto.setMerchantIds(form.getMerchantIds());
    		dto.setBuIds(form.getBuIds());
    		dto.setIds(form.getIds());
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(dto.getName()).
                    addObject(dto.getTel()).
                    addObject(dto.getEmail()).
                    addObject(dto.getAccountType()).
                    empty();
            if(isNull){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            } else {
                if (dto.getAccountType().equals(DERP_SYS.USERINFO_ACCOUNTTYPE_1) &&
                        StringUtils.isEmpty(dto.getMerchantIds())) {
                	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
                }
            }
            viewResponse  = service.modify(dto);
            if (200==viewResponse.getState()) {
            	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else { 
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),viewResponse.getMessage());//未知异常
			}
            
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e.getMessage());//未知异常
        }
    }
    
    /**
     *删除用户数据
     * @param id
     * @return
     */

    @ApiOperation(value = "删除用户数据")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
   	})
   	@PostMapping(value="/delUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delUser(Long id){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delUser(ids);
            if(!b){
    			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常;
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 重置用户密码
      * @param id
     * @return
     */
    @ApiOperation(value = "重置用户密码")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
   	})
   	@PostMapping(value="/resetPwd.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean resetUserPwd(String token,Long id){
        try{
            //空值校验
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);

            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            //session 获取用户信息
            User user = ShiroUtils.getUserByToken(token);
            //重置密码
            boolean b = service.resetPwd(id,user.getId());
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
     * 修改密码
     * @return
     */
    @ApiOperation(value = "修改密码")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "oldPwd", value = "oldPwd", required = true),
			@ApiImplicitParam(name = "password", value = "password", required = true)

   	})
   	@PostMapping(value="/modifyPwd.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean modifyPwd(String token,String oldPwd, String password){
        Map<String,String> retMap = new HashMap<>();
        try{
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(oldPwd).
                    addObject(password).
                    empty();
            if(isNull){
                //输入信息不完整               
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            //获取用户登陆信息
            User userInfoModel = ShiroUtils.getUserByToken(token);
            //执行修改密码操作
            retMap = service.modifyPwd(userInfoModel.getId(),oldPwd,password);
            if ("0000".equals(retMap.get("code"))) {
            	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);//成功           	
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,retMap.get("message"));//未知异常
			}
        }catch(Exception e){            
            e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 启用/停用
     * @param id
     * @return
     */
    @ApiOperation(value = "启用/停用")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "type", value = "是否禁用  0 启用(默认)  1 禁用", required = true)
   	})
   	@PostMapping(value="/enableUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean enableUser(Long id, String type){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            boolean b = service.enableUser(id, type);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
 			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }




}
