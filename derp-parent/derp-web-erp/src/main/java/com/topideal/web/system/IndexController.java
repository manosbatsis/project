package com.topideal.web.system;

import com.topideal.common.constant.*;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.PermissionService;
import com.topideal.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载买家中心、卖家中心
 * Created by acer on 2016/8/25.
 * @author chen 
 * @since 2017/06/28
 */
//@Controller
//@RequestMapping(value="/")
public class IndexController {

   // @Autowired
    public PermissionService permissionService;

    //@Autowired
    private MerchantInfoService merchantInfoService;
    
    /**
     * 加载主页
     * @return
     */
    //@RequestMapping({"/","/index.html"})
    public ModelAndView toIndexPage()throws Exception{
        ModelAndView modelAndView = new ModelAndView("index");
        //登陆用户信息
        User user= ShiroUtils.getUser();

        //获取菜单权限
        List<TreeBean> permissions=permissionService.treeMenuList(user.getId());
        //拼接子服务IP端口
        if(permissions!=null&&permissions.size()>0){
            for(TreeBean treeParent:permissions){
                List<TreeBean> childrenList = treeParent.getChildren();
                if(childrenList!=null&&childrenList.size()>0){
                    for(int i = 0;i< childrenList.size();i++){
                        if(childrenList.get(i).getId() == null){
                            childrenList.remove(childrenList.get(i));
                            continue;
                        }
                        if(childrenList.get(i).getServerAddr()!=null&&!childrenList.get(i).getServerAddr().equals("")&&childrenList.get(i).getModule()!=null){
                            /**模块 1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report*/
                            if(childrenList.get(i).getModule().equals(DERP_SYS.PERMISSIONINFO_MODULE_2)){
                                childrenList.get(i).setServerAddr(ApolloUtil.orderWebhost+childrenList.get(i).getServerAddr());
                            }else if(childrenList.get(i).getModule().equals(DERP_SYS.PERMISSIONINFO_MODULE_3)){
                                childrenList.get(i).setServerAddr(ApolloUtil.storageWebhost+childrenList.get(i).getServerAddr());
                            } else if(childrenList.get(i).getModule().equals(DERP_SYS.PERMISSIONINFO_MODULE_4)){
                                childrenList.get(i).setServerAddr(ApolloUtil.inventoryWebhost+childrenList.get(i).getServerAddr());
                            } else if(childrenList.get(i).getModule().equals(DERP_SYS.PERMISSIONINFO_MODULE_5)){
                                childrenList.get(i).setServerAddr(ApolloUtil.reportWebhost+childrenList.get(i).getServerAddr());
                            }
                        }
                    }
                }
            }
        }
        //增加对象
        modelAndView.addObject("permissions",permissions);
        UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
        userMerchantRelModel.setUserId(user.getId());
        List<MerchantInfoModel> merchantSelectBeans = merchantInfoService.getUserMerchantList(userMerchantRelModel);
        modelAndView.addObject("merchantSelectBeans", merchantSelectBeans);
        modelAndView.addObject("merchantName", user.getMerchantName());
        modelAndView.addObject("merchantId", user.getMerchantId());
        modelAndView.addObject("tag", user.getUserType());
        return modelAndView;
    }

    /**
     * 根据名称获取常量集合
     * */
    //@RequestMapping("/getConstantListByName.asyn")
	//@ResponseBody
	public ViewResponseBean getConstantListByName(String listName){
    	ArrayList<DerpBasic> list = DERP.getConstantListByName(listName);
    	if(list==null) list = DERP_SYS.getConstantListByName(listName);
    	if(list==null) list = DERP_ORDER.getConstantListByName(listName);
    	if(list==null) list = DERP_STORAGE.getConstantListByName(listName);
    	if(list==null) list = DERP_INVENTORY.getConstantListByName(listName);
    	if(list==null) list = DERP_REPORT.getConstantListByName(listName);
    	
    	return ResponseFactory.success(list);
    }

    /**
     * 退出
     * @return
     */
    //@RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
       //清空单点cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        request.getSession().invalidate();//清空session
        ShiroUtils.logout();//shiro
        String idmUrl = ApolloUtil.serverUrlPrefix+"/logout";
        return "redirect:"+idmUrl;
    }

}
