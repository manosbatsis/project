package com.topideal.report.web.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.report.service.common.ReportCommonService;

/**
 * 财务进销存报表关账
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/reportCommon")
public class ReportCommonController {
	
	@Autowired
	public ReportCommonService reportCommonService;

	

	
	/**
	 * 获取日志数据
	 * 类型为1 查询关账日志 relCode 为商家id+","+事业部id+","+"月份"拼接的数据
	 * */
	@RequestMapping("/getOperateLogList.asyn")
	@ResponseBody
	private ViewResponseBean getOperateLogList(String relCode,String module) {
		List<OperateReportLogModel> operateLogList=new ArrayList<OperateReportLogModel>();
		OperateReportLogModel model =new OperateReportLogModel();
		try{
			if (StringUtils.isBlank(relCode)||StringUtils.isBlank(module)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			// 响应结果集			
			model.setRelCode(relCode);
			model.setModule(module);
			operateLogList = reportCommonService.getOperateLogList(model);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(operateLogList);
	}
	

	

	
}
