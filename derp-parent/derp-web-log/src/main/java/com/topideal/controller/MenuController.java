package com.topideal.controller;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 菜单
 * @author chong
 *
 */
@RestController
@RequestMapping("/load/page.asyn")
public class MenuController {

    //api日志-启用
    @RequestMapping(params="act=301")
    public ModelAndView m301(String point, String keyword, String id, String selectScope){
    	if (point == null) {
			point = "1";
		} 
		if (keyword == null) {
			keyword = "1";
		} 
		if (id == null) {
			id = "1";
		}
		if(selectScope == null){
			selectScope = "1";
		}
        return new ModelAndView("forward:/apilog/toPage.html");
    }
    //爬虫日志-启用
    @RequestMapping(params="act=302")
    public ModelAndView m302(Model model){
         return new ModelAndView("forward:/crawlerlog/toPage.html");
    }
    //库存日志-启用
    @RequestMapping(params="act=303")
    public ModelAndView m303(String point, String keyword, String id, String selectScope){
    	String point_str = "";
		if (point == null) {
			point_str = "1";
		} else {
			point_str = point;
		}
		String keyword_str = "";
		if (keyword == null) {
			keyword_str = "1";
		} else {
			keyword_str = keyword;
		}
		String id_str = "";
		if (id == null) {
			id_str = "1";
		} else {
			id_str = id;
		}
		String selectScope_str = "";
		if(selectScope == null){
			selectScope_str = "1";
		}else{
			selectScope_str = selectScope;
		}
         return new ModelAndView("forward:/inventorylog/toPage.html");
    }
    //业务日志-启用
    @RequestMapping(params="act=304")
    public ModelAndView m304(String point, String keyword, String id, String selectScope){
    	String point_str = "";
		if (point == null) {
			point_str = "1";
		} else {
			point_str = point;
		}
		String keyword_str = "";
		if (keyword == null) {
			keyword_str = "1";
		} else {
			keyword_str = keyword;
		}
		String id_str = "";
		if (id == null) {
			id_str = "1";
		} else {
			id_str = id;
		}
		String selectScope_str = "";
		if(selectScope == null){
			selectScope_str = "1";
		}else{
			selectScope_str = selectScope;
		}
         return new ModelAndView("forward:/orderlog/toPage.html");
    }
    //仓储日志-启用
    @RequestMapping(params="act=305")
    public ModelAndView m305(String point, String keyword, String id, String selectScope){
    	String point_str = "";
		if (point == null) {
			point_str = "1";
		} else {
			point_str = point;
		}
		String keyword_str = "";
		if (keyword == null) {
			keyword_str = "1";
		} else {
			keyword_str = keyword;
		}
		String id_str = "";
		if (id == null) {
			id_str = "1";
		} else {
			id_str = id;
		}
		String selectScope_str = "";
		if(selectScope == null){
			selectScope_str = "1";
		}else{
			selectScope_str = selectScope;
		}
         return new ModelAndView("forward:/storagelog/toPage.html");
    }
    //推送外部api日志-启用
    @RequestMapping(params="act=306")
    public ModelAndView m306(String point, String keyword, String id, String selectScope){
    	String point_str = "";
		if (point == null) {
			point_str = "1";
		} else {
			point_str = point;
		}
		String keyword_str = "";
		if (keyword == null) {
			keyword_str = "1";
		} else {
			keyword_str = keyword;
		}
		String id_str = "";
		if (id == null) {
			id_str = "1";
		} else {
			id_str = id;
		}
		String selectScope_str = "";
		if(selectScope == null){
			selectScope_str = "1";
		}else{
			selectScope_str = selectScope;
		}
         return new ModelAndView("forward:/pushapilog/toPage.html");
    }
    //日志预警-启用
    @RequestMapping(params="act=307")
    public ModelAndView m307(Model model){
         return new ModelAndView("forward:/warninglog/toPage.html");
    }
    //日志监控-启用
    @RequestMapping(params="act=308")
    public ModelAndView m308(Model model){
         return new ModelAndView("forward:/consumeMonitor/toPage.html");
    }
    //mq日志流水-启用
    @RequestMapping(params="act=309")
    public ModelAndView m309(Model model){
         return new ModelAndView("forward:/logstream/toPage.html");
    }
    //进境异常监控-启用
    @RequestMapping(params="act=310")
    public ModelAndView m310(Model model){
         return new ModelAndView("forward:/errorMonitor/toPage.html");
    }
    
    // 报表日志-启用
 	@RequestMapping(params = "act=311")
 	public ModelAndView m311(HttpSession session) {
 		return new ModelAndView("forward:/reportlog/toPage.html");
 	}
 	
 	//api日志流水
    @RequestMapping(params="act=312")
    public ModelAndView m312(String point, String keyword, String id, String selectScope){
		if (point == null) {
			point = "1";
		} 
		if (keyword == null) {
			keyword = "1";
		} 
		if (id == null) {
			id = "1";
		}
		if(selectScope == null){
			selectScope = "1";
		}
        return new ModelAndView("forward:/smurfsapi/logmenu/toPage.html");
    }
    
    //api日志监控-启用
    @RequestMapping(params="act=313")
    public ModelAndView m313(String point, String keyword, String id, String selectScope){
		if (point == null) {
			point = "1";
		} 
		if (keyword == null) {
			keyword = "1";
		} 
		if (id == null) {
			id = "1";
		}
		if(selectScope == null){
			selectScope = "1";
		}
        return new ModelAndView("forward:/apimonitorlog/toPage.html");
    }
    
    //数据运维
    @RequestMapping(params="act=314")
    public ModelAndView m314(){		
        return new ModelAndView("forward:/dataoperations/toPage.html");
    }

    //数据运维
    @RequestMapping(params="act=315")
    public ModelAndView m315(){		
        return new ModelAndView("forward:/consumeMonitor/toImportPage.html");
    }
    
  //智能重推
    @RequestMapping(params="act=401")
    public ModelAndView m401(){		
        return new ModelAndView("forward:/autoLog/toPage.html");
    }
}
