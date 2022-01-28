package com.topideal.web.derp;

import com.topideal.common.system.auth.User;
import com.topideal.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weixiaolei on 2018/4/19.
 */
@RequestMapping("/home")
@Controller
public class HomePageController {

    @RequestMapping("/toPage.html")
    public String toPage(Model model){
        User user = ShiroUtils.getUser();
        model.addAttribute("type", user.getUserType());
        return "/derp/home-page";
    }



}
