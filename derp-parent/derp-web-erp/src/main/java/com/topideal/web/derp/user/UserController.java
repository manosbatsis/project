package com.topideal.web.derp.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.entity.vo.user.UserMerchantRelModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.UserInfoService;
import com.topideal.shiro.ShiroUtils;


/**
 * Created by weixiaolei on 2018/4/13.
 */
@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
    private UserInfoService service;

	@Autowired
    private MerchantInfoService merchantInfoService;
	

    /**
     * 跳转到用户列表页面
     * @return
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model)throws SQLException{
        //session 获取用户信息
        User user= ShiroUtils.getUser();
        //当登录用户为后台管理员
        if(user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)){
            model.addAttribute("tag","0");
        }
        return "/derp/user/user-list";
    }
	/**
	 * 访问编辑页面
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception {
        UserInfoDTO userInfoModel = service.searchDetail(id);
        model.addAttribute("detail", userInfoModel);
        UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
        userMerchantRelModel.setUserId(id);
        List<MerchantInfoModel> merchantInfoModels = merchantInfoService.getUserMerchantList(userMerchantRelModel);
        model.addAttribute("merchantInfoModels", merchantInfoModels);
        
        List<UserBuRelModel> buRelList = service.getBuRel(id) ;
        model.addAttribute("buRelList", buRelList);
        
		return "/derp/user/user-edit";
	}

    /**
     * 列表所需数据
     * @param model
     * @return
     */
    @RequestMapping("/list.asyn")
    @ResponseBody
    private ViewResponseBean listUser(UserInfoDTO dto){
        try{
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            dto=service.listUserInfo(dto,user.getUserType(),user.getId(),user.getParentId());
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);

        }

        return ResponseFactory.success(dto);
    }


    /**
     * 保存用户数据
     * @param dto
     * @return
     */
    @RequestMapping("/saveUser.asyn")
    @ResponseBody
    public ViewResponseBean saveUser(UserInfoDTO dto){
    	ViewResponseBean viewResponse = null;
    	try{
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
	            return ResponseFactory.error(StateCodeEnum.ERROR_303);
	        } else {
	            if (dto.getAccountType().equals(DERP_SYS.USERINFO_ACCOUNTTYPE_1) &&
                        StringUtils.isEmpty(dto.getMerchantIds())) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_303);
                }
            }
	        //session 获取用户信息
	        User user= ShiroUtils.getUser();
	        //存储用户信息
	        viewResponse  = service.saveUser(dto,user.getUserType(),user.getId(),user.getParentId());
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return viewResponse;
    }
    /**
     * 修改用户数据
     * @param dto
     * @return
     */
    @RequestMapping("/editUser.asyn")
    @ResponseBody
    public ViewResponseBean editUser(UserInfoDTO dto){
        ViewResponseBean viewResponse = null;
        try{
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(dto.getName()).
                    addObject(dto.getTel()).
                    addObject(dto.getEmail()).
                    addObject(dto.getAccountType()).
                    empty();
            if(isNull){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            } else {
                if (dto.getAccountType().equals(DERP_SYS.USERINFO_ACCOUNTTYPE_1) &&
                        StringUtils.isEmpty(dto.getMerchantIds())) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_303);
                }
            }
            viewResponse  = service.modify(dto);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return viewResponse;
    }
    
    /**
     *删除用户数据
     * @param id
     * @return
     */
    @RequestMapping("/delUser.asyn")
    @ResponseBody
    public ViewResponseBean delUser(Long id){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delUser(ids);
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
     * 重置用户密码
      * @param id
     * @return
     */
    @RequestMapping("/resetPwd.asyn")
    @ResponseBody
    public ViewResponseBean resetUserPwd(Long id){
        try{
            //空值校验
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);

            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //重置密码
            boolean b = service.resetPwd(id,user.getId());
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
     * 修改密码
     * @return
     */
    @RequestMapping("/modifyPwd.asyn")
    @ResponseBody
    public ViewResponseBean modifyPwd(String oldPwd, String password){
        Map<String,String> retMap = new HashMap<>();
        try{
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(oldPwd).
                    addObject(password).
                    empty();
            if(isNull){
                //输入信息不完整
                retMap.put("code","9999");
                retMap.put("message","新旧密码不能为空");
                return ResponseFactory.success(retMap);
            }
            //获取用户登陆信息
            User userInfoModel= ShiroUtils.getUser();
            //执行修改密码操作
            retMap = service.modifyPwd(userInfoModel.getId(),oldPwd,password);
        }catch(Exception e){
            retMap.put("code","9999");
            retMap.put("message","未知异常"+e);
            e.printStackTrace();
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 启用/停用
     * @param id
     * @return
     */
    @RequestMapping("/enableUser.asyn")
    @ResponseBody
    public ViewResponseBean enableUser(Long id, String type){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            boolean b = service.enableUser(id, type);
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








}
