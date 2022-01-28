package com.topideal.web.derp.common;


import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ApolloUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * @author huangrenya
 * 系统公共配置
 **/
@RequestMapping("/commonConfig")
@Controller
public class CommonSystemConfigController {


    /**
     * 从配置获取系统名称
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping("/getSystemName.asyn")
    @ResponseBody
    public ViewResponseBean getSystemName() {
        String systemName = ApolloUtil.systemName;
        return ResponseFactory.success(systemName);
    }
}
