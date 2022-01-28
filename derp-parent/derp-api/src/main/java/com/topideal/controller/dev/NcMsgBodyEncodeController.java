package com.topideal.controller.dev;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.api.nc.NcClientUtils;

import net.sf.json.JSONObject;

/**
 * nc 报文加密 api
 * @author gy
 *
 */

@Controller
@RequestMapping("/derpapi/1006")
public class NcMsgBodyEncodeController {
	
	@RequestMapping(params="method=erp1006",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject getNcEncode(@RequestBody String json) {
		
		JSONObject jsonObj = JSONObject.fromObject(json);
		
		/**NC 加密*/
		String cripText = NcClientUtils.ncEncode(jsonObj.toString());
		
		jsonObj = new JSONObject() ;
		
		jsonObj.put("cripText", cripText) ;
		
		return jsonObj ;
		
	}

}
