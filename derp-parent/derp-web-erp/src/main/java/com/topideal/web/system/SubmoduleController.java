package com.topideal.web.system;

import com.topideal.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 子模块跳转工具类
 * Created by weixiaolei on 2018/6/15.
 */
@Controller
@RequestMapping(value="/module")
public class SubmoduleController {

	/**
     * 加载子模板页面
     * @param session
     * @return
     */
    @RequestMapping("/loadPage.asyn")
    public String m100(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
        String url=request.getQueryString();
        url=url.trim().substring(4);
        String token=session.getId();
        StringBuffer stringBuffer = new StringBuffer();
        return new StringBuffer().append("redirect:").append(url).append("&token=").append(token).toString();
    }


}
