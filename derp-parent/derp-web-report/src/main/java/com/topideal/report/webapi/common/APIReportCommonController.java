package com.topideal.report.webapi.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.report.service.common.ReportCommonService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 财务进销存报表关账
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/webapi/report/reportCommon")
public class APIReportCommonController {
	
	@Autowired
	public ReportCommonService reportCommonService;

	

	
	/**
	 * 获取日志数据
	 * 类型为1 查询关账日志 relCode 为商家id+","+事业部id+","+"月份"拼接的数据
	 * */
	
	@RequestMapping("/getOperateLogList.asyn")
    @ResponseBody
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "relCode", value = "业务单号 1.关账单号 商家id,事业部id,月份 用逗号拼接", required = true),
            @ApiImplicitParam(name = "module", value = "操作日志 模块 1-关账 ", required = true)})
	private ResponseBean<OperateReportLogModel> getOperateLogList(String relCode,String module) {
		List<OperateReportLogModel> operateLogList=new ArrayList<OperateReportLogModel>();
		OperateReportLogModel model =new OperateReportLogModel();
		try{
			if (StringUtils.isBlank(relCode)||StringUtils.isBlank(module)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
			}
			// 响应结果集			
			model.setRelCode(relCode);
			model.setModule(module);
			operateLogList = reportCommonService.getOperateLogList(model);
		}catch(Exception e){
			 e.printStackTrace();
	         return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, operateLogList);//成功
	}
	

	

	
}
