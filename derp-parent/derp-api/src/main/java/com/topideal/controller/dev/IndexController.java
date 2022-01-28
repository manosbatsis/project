package com.topideal.controller.dev;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/derpapi")
public class IndexController {
 
	@RequestMapping("/index")
	public String toDetailsPage(Model model) throws Exception {
		return "index";
	}
}
