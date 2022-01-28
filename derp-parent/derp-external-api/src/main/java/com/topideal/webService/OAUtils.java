package com.topideal.webService;

import com.alibaba.fastjson.JSON;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.webService.oa.dto.WorkflowRequestIdResponse;
import com.topideal.webService.oa.dto.WorkflowRequestInfoWrap;
import com.topideal.webService.oa.o_01.CreatRequestIdRequest;
import com.topideal.webService.oa.o_01.CtreatRequestIdItemRequest;
import com.topideal.webService.oa.services.webservices.WorkflowService;
import com.topideal.webService.oa.services.webservices.WorkflowServiceLocator;
import com.topideal.webService.oa.services.webservices.WorkflowServicePortType;
import com.topideal.webService.oa.weaver.customer.workflow.webservices.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用OA webServices接口
 * @author 杨创
 *
 */
public class OAUtils {

	private static final Logger logger= LoggerFactory.getLogger(OAUtils.class);
	
	
	
	/**
	 * 获取员工信息接口
	 * 根据员工工号获取员工在OA中的信息
	 * @param userCode
	 * @return
	 */
	public static JSONArray getOARequestId(String userCode){
		//工号不能为空
		if (StringUtils.isEmpty(userCode)) {
			throw new RuntimeException("员工工号不能为空");
		}
		try {					
			List<String>userCodeList=new ArrayList<String>();
			userCodeList.add(userCode);
			JSONObject json=new JSONObject();
			json.put("data", userCodeList);
			//http://10.10.103.66:8000/QueryId
			String oaUrl1 = ApolloUtil.oaUrl1;// 获取员工信息接口地址
			logger.info("获取员工信息接口地址:"+oaUrl1);
			logger.info("获取员工信息接口报文:"+json.toString());
			String result = HttpClientUtil.doPost(oaUrl1, json.toString(), "utf-8");
			logger.info("获取员工信息接口响应报文:"+result);
			if (StringUtils.isEmpty(result)) throw new RuntimeException("获取OA员工信息失败");
			JSONArray resultArr = JSONArray.fromObject(result);
			if (resultArr==null||resultArr.size()==0) throw new RuntimeException("获取OA员工信息失败");
			return resultArr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取OA员工信息失败");
		}
		
	}
	
	/**
	 * userCode 工号
	 * 获取OA流程单号防方法
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	public static String getOARequestId(CreatRequestIdRequest model,String userCode) throws Exception{
		 /**
         * 添加规则
         * WorkflowMainTableInfo 主表对象
         * 主表只有一个
         *
         * WorkflowDetailTableInfo 明细表对象
         * 明细表可能有多个
         *
         * WorkflowRequestTableRecord 行对象
         * 每个表有多行数据
         *
         * WorkflowRequestTableField 列对象
         * 每行有多列
         *
         * 行里面存放列
         *
         *
         * 表里面存放行
         *
         * 现在业务逻辑
         * 一个主表、一个明细表，主表有1行9列，明细表有2行6列
         *
         * 首先创建一个9列的 WorkflowRequestTableField列对象
         * WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[9];
         * 每列赋值
         * 创建一个1行的 行对象
         * WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];
         *  wrtri[0] = new WorkflowRequestTableRecord();
         * 列保存到行
         * wrtri[0].setWorkflowRequestTableFields(wrti);
         *主表保存一行数据
         * wmi.setRequestRecords(wrtri);
         *
         * 明细表类似
         *
         */
		//根据员工工号获取员工在OA中的信息
		JSONArray oaUserJsonArr = getOARequestId(userCode);
		if (oaUserJsonArr==null||oaUserJsonArr.size()==0) {
			throw new RuntimeException("未获取OA员工信息");
		}
		JSONObject userJson = (JSONObject) oaUserJsonArr.get(0);
		if (userJson.get("resourceid")==null||StringUtils.isEmpty(userJson.getString("resourceid"))) {
			throw new RuntimeException("未获取到OA员工ID");
		}
		if (userJson.get("departmentid")==null||StringUtils.isEmpty(userJson.getString("departmentid"))) {
			throw new RuntimeException("未获取到OA员工所属部门ID");
		}
		String resourceid = userJson.getString("resourceid");//员工id
		String departmentid = userJson.getString("departmentid");//员工所属部门ID
		
		
		
		// 流程创建接口 (获取流程单号)
		model.setSqbm(departmentid);
		model.setSqr(resourceid);
		model.setLyxt(ApolloUtil.oaLyxt);// 来源系统
		logger.info("推送OA实体数据:"+JSONObject.fromObject(model).toString());

		 //主表字段对象数组
        WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[21];
        //主表字段对象
        wrti[0] = new WorkflowRequestTableField();
        wrti[0].setFieldName("wbxtdjh");//外部系统单据号
        wrti[0].setFieldValue(model.getWbxtdjh());
        wrti[0].setView(true);
        wrti[0].setEdit(true);

        wrti[1] = new WorkflowRequestTableField();
        wrti[1].setFieldName("sqbm");//申请部门
        wrti[1].setFieldValue(departmentid);
        wrti[1].setView(true);
        wrti[1].setEdit(true);
        
        wrti[2] = new WorkflowRequestTableField();
        wrti[2].setFieldName("sqr");//申请人
        wrti[2].setFieldValue(resourceid);
        wrti[2].setView(true);
        wrti[2].setEdit(true);
        
        wrti[3] = new WorkflowRequestTableField();
        wrti[3].setFieldName("sqsj");//申请时间
        wrti[3].setFieldValue(model.getSqsj());
        wrti[3].setView(true);
        wrti[3].setEdit(true);
        
        wrti[4] = new WorkflowRequestTableField();
        wrti[4].setFieldName("cwzz");//财务组织
        wrti[4].setFieldValue(model.getCwzz());
        wrti[4].setView(true);
        wrti[4].setEdit(true);
        
        
        wrti[5] = new WorkflowRequestTableField();
        wrti[5].setFieldName("gysxz");//供应商性质 0：境内供应商 1：境外供应商
        wrti[5].setFieldValue(model.getGysxz());
        wrti[5].setView(true);
        wrti[5].setEdit(true);
        
        
        wrti[6] = new WorkflowRequestTableField();
        wrti[6].setFieldName("gys");//供应商 主数据客商编码
        wrti[6].setFieldValue(model.getGys());
        wrti[6].setView(true);
        wrti[6].setEdit(true);
        
        wrti[7] = new WorkflowRequestTableField();
        wrti[7].setFieldName("syb");//事业部编码
        wrti[7].setFieldValue(model.getSyb());
        wrti[7].setView(true);
        wrti[7].setEdit(true);
        
        wrti[8] = new WorkflowRequestTableField();
        wrti[8].setFieldName("skyhzh");//收款银行账户
        wrti[8].setFieldValue(model.getSkyhzh());
        wrti[8].setView(true);
        wrti[8].setEdit(true);
        
        wrti[9] = new WorkflowRequestTableField();
        wrti[9].setFieldName("skyhkhh");//收款银行开户行
        wrti[9].setFieldValue(model.getSkyhkhh());
        wrti[9].setView(true);
        wrti[9].setEdit(true);
        
        wrti[10] = new WorkflowRequestTableField();
        wrti[10].setFieldName("swiftcode");//Swift Code
        wrti[10].setFieldValue(model.getSwiftcode());
        wrti[10].setView(true);
        wrti[10].setEdit(true);
        
        wrti[11] = new WorkflowRequestTableField();
        wrti[11].setFieldName("hth");//合同号
        wrti[11].setFieldValue(model.getHth());
        wrti[11].setView(true);
        wrti[11].setEdit(true);

        wrti[12] = new WorkflowRequestTableField();
        wrti[12].setFieldName("jsbz");//结算币种
        wrti[12].setFieldValue(model.getJsbz());
        wrti[12].setView(true);
        wrti[12].setEdit(true);
        
        wrti[13] = new WorkflowRequestTableField();
        wrti[13].setFieldName("ybjehz");//原币金额汇总
        wrti[13].setFieldValue(model.getYbjehz());
        wrti[13].setView(true);
        wrti[13].setEdit(true);
        
        wrti[14] = new WorkflowRequestTableField();
        wrti[14].setFieldName("ybjedx");// 原币金额大写
        wrti[14].setFieldValue(model.getYbjedx());
        wrti[14].setView(true);
        wrti[14].setEdit(true);
        
        wrti[15] = new WorkflowRequestTableField();
        wrti[15].setFieldName("yjfksj");//预计付款时间
        wrti[15].setFieldValue(model.getYjfksj());
        wrti[15].setView(true);
        wrti[15].setEdit(true);
        
        wrti[16] = new WorkflowRequestTableField();
        wrti[16].setFieldName("qkyy");//请款原因
        wrti[16].setFieldValue(model.getQkyy());
        wrti[16].setView(true);
        wrti[16].setEdit(true);
        
        
        wrti[17] = new WorkflowRequestTableField();
        wrti[17].setFieldName("fj");//附件
        wrti[17].setFieldValue(model.getFj());
        wrti[17].setView(true);
        wrti[17].setEdit(true);
        
        wrti[18] = new WorkflowRequestTableField();
        wrti[18].setFieldName("skyhzh1");//收款银行账号
        wrti[18].setFieldValue(model.getSkyhzh1());
        wrti[18].setView(true);
        wrti[18].setEdit(true);
        
        wrti[19] = new WorkflowRequestTableField();
        wrti[19].setFieldName("sfdz");//是否垫资
        wrti[19].setFieldValue(model.getSfdz());
        wrti[19].setView(true);
        wrti[19].setEdit(true);
        
        wrti[20] = new WorkflowRequestTableField();
        wrti[20].setFieldName("lyxt");//来源系统
        wrti[20].setFieldValue(model.getLyxt());
        wrti[20].setView(true);
        wrti[20].setEdit(true);
        
        //主表对象数组 暂定一行
        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];
        //主表第一行对象
        wrtri[0] = new WorkflowRequestTableRecord();
        //存放主表第一行字段信息
        wrtri[0].setWorkflowRequestTableFields(wrti);

        //主表对象
        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
        //主表添加行
        wmi.setRequestRecords(wrtri);

        List<CtreatRequestIdItemRequest> itemList = model.getItemList();
        //==========================
        //明细表对象数组，
        WorkflowRequestTableRecord[] wdti = new WorkflowRequestTableRecord[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
        	CtreatRequestIdItemRequest itemRequest = itemList.get(i);
        	//第一行
            wdti[i] = new WorkflowRequestTableRecord();
            //第一行6个字段
            WorkflowRequestTableField[] wdrti = new WorkflowRequestTableField[6];
            //第一行第一个字段
            wdrti[0] = new WorkflowRequestTableField();
            wdrti[0].setFieldName("zy");//摘要
            wdrti[0].setFieldValue(itemRequest.getZy());
            wdrti[0].setView(true);
            wdrti[0].setEdit(true);
            //第一行第二个字段
            wdrti[1] = new WorkflowRequestTableField();
            wdrti[1].setFieldName("poh");//PO号
            wdrti[1].setFieldValue(itemRequest.getPoh());
            wdrti[1].setView(true);
            wdrti[1].setEdit(true);
            
            wdrti[2] = new WorkflowRequestTableField();
            wdrti[2].setFieldName("szfx");//收支费项
            wdrti[2].setFieldValue(itemRequest.getSzfx());
            wdrti[2].setView(true);
            wdrti[2].setEdit(true);
            
            wdrti[3] = new WorkflowRequestTableField();
            wdrti[3].setFieldName("jebhs");//浮点数-2位
            wdrti[3].setFieldValue(itemRequest.getJebhs());
            wdrti[3].setView(true);
            wdrti[3].setEdit(true);
            
            wdrti[4] = new WorkflowRequestTableField();
            wdrti[4].setFieldName("se");//税额
            wdrti[4].setFieldValue(itemRequest.getSe());
            wdrti[4].setView(true);
            wdrti[4].setEdit(true);
            
            wdrti[5] = new WorkflowRequestTableField();
            wdrti[5].setFieldName("jehs");//金额（含税）
            wdrti[5].setFieldValue(itemRequest.getJehs());
            wdrti[5].setView(true);
            wdrti[5].setEdit(true);

            

            //第一行存放列数组
            wdti[i].setWorkflowRequestTableFields(wdrti);
		}

    

        WorkflowDetailTableInfo wd[] = new WorkflowDetailTableInfo[1];//一个明细表
        wd[0] = new WorkflowDetailTableInfo();
        wd[0].setWorkflowRequestTableRecords(wdti);

        //流程
        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        workflowBaseInfo.setWorkflowId(ApolloUtil.oaWorkflowId);//流程ID（workflowId）
        workflowBaseInfo.setWorkflowName("经分销采购请款");//

  
        
        WorkflowRequestInfo requestInfo = new WorkflowRequestInfo();//流程基本信息
        requestInfo.setCanView(true);
        requestInfo.setCanEdit(true);
        requestInfo.setCreatorId(resourceid);//创建人id
        requestInfo.setRequestLevel("0");//0 正常，1重要，2紧急
        if (StringUtils.isEmpty(model.getTitle())) {
        	requestInfo.setRequestName("经分销推送OA流程创建接口");//流程标题
		}else {
			requestInfo.setRequestName(model.getTitle());//流程标题
		}
        
        requestInfo.setWorkflowBaseInfo(workflowBaseInfo);//添加流程
        requestInfo.setWorkflowMainTableInfo(wmi);//添加主表数据
        requestInfo.setWorkflowDetailTableInfos(wd);//添加明细主表数据
        


        try {
            //获取调用对象
            WorkflowService workflowService = new WorkflowServiceLocator();
            String oaUrl2 = ApolloUtil.oaUrl2;// 获取员工信息接口地址
            URL url = new URL(oaUrl2);
            WorkflowServicePortType workflowServiceProxy = workflowService.getWorkflowServiceHttpPort(url);
            String s = workflowServiceProxy.doCreateWorkflowRequest(requestInfo, Integer.valueOf(resourceid));
            logger.info("流程号："+s+" 推送OA实体数据:"+JSONObject.fromObject(model).toString());
            if (StringUtils.isEmpty(s)) {
            	throw new RuntimeException("未获取到流程单号:"+s);
			}
            if (s.contains("-")) {
            	throw new RuntimeException("获取到流程单号s:"+s+"不正确");
			}
            return s;	            
        } catch (Exception e) {       	
            e.printStackTrace();
			throw new RuntimeException("获取流程号失败:"+e.getMessage());
        }
	}

    /**
     * 从OA获取采购框架合同
     * @param formId 模板ID,由合同系统提供，通过单据类型获取数据
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public static JSONArray getPurchaseFrameContractFormOA(String formId, String beginDate, String endDate){
        //工号不能为空
        if (StringUtils.isBlank(formId)) {
            throw new RuntimeException("模板ID不能为空");
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            throw new RuntimeException("时间不能为空");
        }
        try {
            JSONObject json=new JSONObject();
            json.put("formId", formId);
            json.put("beginDate", beginDate);
            json.put("endDate", endDate);
            // 从OA获取采购框架合同接口地址
            String oaUrl1 = ApolloUtil.oaPurchaseFrameContractUrl;
            logger.info("从OA获取采购框架合同地址:"+oaUrl1);
            logger.info("从OA获取采购框架合同报文:"+json.toString());
            String respJson = HttpClientUtil.doPost(oaUrl1, json.toString(), "utf-8");
            logger.info("从OA获取采购框架合同响应报文:" + respJson);

            if (StringUtils.isBlank(respJson)){ throw new RuntimeException("从OA获取采购框架合同失败");}
//            respJson = respJson.substring(1, respJson.length() - 1);
//            respJson = StringEscapeUtils.unescapeJava(respJson);
            JSONObject jsonData = JSONObject.fromObject(respJson);
            String resultCode = jsonData.getString("resultCode");
            if(resultCode.equals("0")) {
                throw new RuntimeException("从OA获取采购框架合同失败, 响应报文:" + respJson);
            }
            JSONArray resultArr = jsonData.getJSONArray("body");
            return resultArr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从OA获取采购框架合同失败");
        }
    }

    /**
     * 从OA获取采购立项/试单数据
     * @param formId 模板ID,由合同系统提供，通过单据类型获取数据
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public static JSONArray getPurchaseTryApplyOrderFormOA(String formId, String beginDate, String endDate){
        //工号不能为空
        if (StringUtils.isBlank(formId)) {
            throw new RuntimeException("模板ID不能为空");
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            throw new RuntimeException("时间不能为空");
        }
        try {
            JSONObject json=new JSONObject();
            json.put("formId", formId);
            json.put("beginDate", beginDate);
            json.put("endDate", endDate);
            // 从OA获取采购立项/试单数据接口地址
            String oaUrl1 = ApolloUtil.oaPurchaseTryApplyContractUrl;
            logger.info("从OA获取采购立项/试单数据地址:"+oaUrl1);
            logger.info("从OA获取采购立项/试单数据报文:"+json.toString());
            String respJson = HttpClientUtil.doPost(oaUrl1, json.toString(), "utf-8");
            logger.info("从OA获取采购立项/试单数据合同响应报文:" + respJson);
            if (StringUtils.isBlank(respJson)){
                throw new RuntimeException("从OA获取采购立项/试单数据失败, 响应报文:" + respJson);
            }
            JSONObject jsonData = JSONObject.fromObject(respJson);
            String resultCode = jsonData.getString("resultCode");
            if(resultCode.equals("0")) {
                throw new RuntimeException("从OA获取采购立项/试单数据失败, 响应报文:" + respJson);
            }
            JSONArray resultArr = jsonData.getJSONArray("body");
            return resultArr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从OA获取采购立项/试单数据失败");
        }
    }

    /**
     * userCode 工号
     * 获取OA流程创建
     * @param model
     * @return
     * @throws Exception
     */
    public static String getOARequestId(WorkflowRequestInfoWrap model) throws Exception{

        try {
            //获取调用对象
            String oaUrl1 = ApolloUtil.oaPurchaseWorkflowIdUrl;// 获取员工信息接口地址
            logger.info("从OA获取采购流程ID地址:" + oaUrl1);
            logger.info("从OA获取采购流程ID报文:" + JSON.toJSONString(model));
            String respJson = HttpClientUtil.doPost(oaUrl1, JSON.toJSONString(model), "utf-8");
            logger.info("从OA获取采购流程ID响应报文:" + respJson);
            if (StringUtils.isBlank(respJson)){
                throw new RuntimeException("从OA获取采购流程ID失败");
            }

            JSONObject jsonObject = JSONObject.fromObject(respJson);
            WorkflowRequestIdResponse resultDTO = (WorkflowRequestIdResponse) JSONObject.toBean(jsonObject, WorkflowRequestIdResponse.class);

            if (resultDTO == null || !resultDTO.getResultCode().equals("1")
                || StringUtils.isBlank(resultDTO.getRequestId())){
                throw new RuntimeException("从OA获取采购流程ID失败");
            }

            return resultDTO.getRequestId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取流程号失败:"+e.getMessage());
        }
    }
}
