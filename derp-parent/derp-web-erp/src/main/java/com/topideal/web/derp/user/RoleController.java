package com.topideal.web.derp.user;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.service.user.RoleInfoService;
import com.topideal.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/5/2.
 */
@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private RoleInfoService service;

    /**
     * 跳转到用户列表页面
     * @return
     */
    @RequestMapping("/toPage.html")
    public String toPage(){
        return "/derp/user/role-list";
    }

    /**
     * 列表所需数据
     * @param model
     * @return
     */
    @RequestMapping("/list.asyn")
    @ResponseBody
    private ViewResponseBean listRole(RoleInfoDTO dto){
        try{
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //普通用户，取超管用户id
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                dto.setUserId(user.getId());
            }
            dto=service.listRoleInfo(dto);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success(dto);
    }

    /**
     * 保存用户数据
     * @param model
     * @return
     */
    @RequestMapping("/saveRole.asyn")
    @ResponseBody
    public ViewResponseBean saveRole(RoleInfoModel model){
        try{
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(model.getName()).
                    empty();
            if(isNull){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //绑定用户信息
            model.setUserId(user.getId());
            //存储用户信息
            /*if("3".equals(user.getUserType())){
                model.setUserId(user.getParentId());
            }*/
            //创建人
            model.setCreater(user.getId());
            Long id = service.saveRole(model);
            if(id==null){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
            return ResponseFactory.success(id);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(RuntimeException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_309,e);
        }
        catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        
    }

    /**
     *删除角色数据
     * @param id
     * @return
     */
    @RequestMapping("/del.asyn")
    @ResponseBody
    public ViewResponseBean delUser(Long id){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //查询该角色下是否已分配用户，若分配则不能删除
            if (service.isExistUsersByRoleId(id)) {
                return ResponseFactory.error(StateCodeEnum.MESSAGE_10009);
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delRole(ids);
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
     * 分配权限
     * @param ids
     * @param roleId
     * @return
     */
    @RequestMapping("/addPermission.asyn")
    @ResponseBody
    public ViewResponseBean addPermission(String ids, Long roleId){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids)&& StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //转成list
            List<Long> idList= StrUtils.parseIds(ids);
            //新增
            boolean b = service.addPermission(idList,roleId);
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
     * 绑定用户信息
     * @return
     */
    @RequestMapping("/searchUserInfo.asyn")
    @ResponseBody
    public ViewResponseBean searchUserInfo(Long roleId){
        Map map=null;
        try{
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //后台管理员取全部用户/普通用户取自己创建的用户
            Long userId=user.getId();
            //普通用户，取超管用户id
            /*if("3".equals(user.getUserType())){
                userId=user.getParentId();
            }*/
            map=service.seachUserInfoByRoleId(roleId,userId,user.getUserType());
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success(map);
    }
    /**
     * 获取启用用户
     * @return
     */
    @RequestMapping("/getUserInfo.asyn")
    @ResponseBody
    public ViewResponseBean getUserInfo(){
        Map map=null;
        try{
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //后台管理员取全部用户/普通用户取自己创建的用户
            Long userId=user.getId();
            map=service.getUserInfo(userId,user.getUserType(), false);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success(map);
    }
    /**
     * 分配权限
     * @param ids
     * @param roleId
     * @return
     */
    @RequestMapping("/bindUser.asyn")
    @ResponseBody
    public ViewResponseBean bindUser(String ids, Long roleId){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids)&& StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //转成list
            List<Long> idList= null;
            if(StringUtils.isNotBlank(ids)){
                idList=StrUtils.parseIds(ids);
            }

            if (roleId.equals(1001L)) {
                if (StringUtils.isBlank(ids) || idList == null) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_318);
                }
            }

            //新增
            boolean b = service.addBindUser(idList,roleId);
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
