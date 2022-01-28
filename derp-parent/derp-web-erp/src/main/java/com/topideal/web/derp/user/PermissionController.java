package com.topideal.web.derp.user;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.entity.vo.user.RolePermissionRelModel;
import com.topideal.service.user.PermissionService;
import com.topideal.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixiaolei on 2018/5/2.
 */
@RequestMapping("/permission")
@Controller
public class PermissionController {

    @Autowired
    private PermissionService service;

    /**
     * 跳转到用户列表页面
     * @return
     */
    @RequestMapping("/toPage.html")
    public String toPage(){
        return "/derp/user/permission-list";
    }

    /**
     * 列表所需数据
     * @param model
     * @return
     */
    @RequestMapping("/list.asyn")
    @ResponseBody
    private ViewResponseBean listPermission(PermissionInfoDTO dto){
        try{
            dto=service.listPermissionInfo(dto);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success(dto);
    }

    /**
     * 保存用户数据
     * @param model
     * @return
     */
    @RequestMapping("/save.asyn")
    @ResponseBody
    public ViewResponseBean savePermission(PermissionInfoModel model, HttpSession session){
        try{
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(model.getAuthorityName()).
                    empty();
            if(isNull){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            if (model.getType() != null && model.getType() == 2) {
                if (StringUtils.isBlank(model.getPermission())) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_303);
                }
            }
            //session 获取用户信息
            User user= ShiroUtils.getUser();
            //创建人
            model.setCreater(user.getId());
            boolean b = service.savePermission(model);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
    }

    /**
     * 列表所需数据
     * @return
     */
    @RequestMapping("/listTree.asyn")
    @ResponseBody
    private List<TreeBean> listTree()throws SQLException{
        List<TreeBean> list=service.listTree();
        return list;
    }

    /**
     * 列表所需数据
     * @return
     */
    @RequestMapping("/listTreeAll.asyn")
    @ResponseBody
    private List<TreeBean> listTreeAll(HttpSession session)throws SQLException{
        //session 获取用户信息
        User user= ShiroUtils.getUser();
        //假设平台
        Long userId=null;
        //普通用户，取超管用户id
        if("3".equals(user.getUserType())){
            userId=user.getParentId();
        }else if("2".equals(user.getUserType())){
            userId=user.getId();
        }
        List<TreeBean> list=service.listTreeAll(userId);
        //若子节点只有一个，判断是否为空，为空不加载子节点
        if(list != null && list.size() > 0){
            for(TreeBean tr: list){
                if(tr.getChildren() != null && tr.getChildren().size() == 1){
                    if(tr.getChildren().get(0).getId() == null){
                        tr.getChildren().remove(0);
                    }
                }
            }
        }
        return list;
    }

    /**
     *删除用户数据
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
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delPermission(ids);
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
     *通过角色ID查询用户信息
     * @param roleId
     * @return
     */
    @RequestMapping("/searchByRoleId.asyn")
    @ResponseBody
    public ViewResponseBean searchByRoleId(Long roleId){
        List<RolePermissionRelModel> list=null;
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
             list= service.searchRelByRoleId(roleId);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }

        return ResponseFactory.success(list);
    }






}
