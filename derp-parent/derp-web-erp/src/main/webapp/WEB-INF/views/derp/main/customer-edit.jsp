<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <form id="edit-form" action="/customer/modifyCustomer.asyn">
             <div class="row">
              <div class="card-box margin table-responsive" style="overflow-x:hidden;">
                 <div class="row">
              <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">客户档案</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customer/toPage.html')">客户列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/customer/toEditPage.html')">客户编辑</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                  <div class="margin">
                      <div class="edit_box">
                      	  <div class="edit_item">
                              <input type="hidden" id="id" name="id" value="${detail.id }">
                              <input type="hidden" id ="deleteIds" name="deleteIds" />
                              <label class="edit_label"><i class="red">*</i>客户名称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="name" name="name" value="${detail.name }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">客户简称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="simpleName" name="simpleName" value="${detail.simpleName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>营业执照号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="creditCode" name="creditCode" value="${detail.creditCode }">
                          </div>
                      </div>
                      <div class="edit_box">                         
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>组织机构代码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input"  id="orgCode" name="orgCode" value="${detail.orgCode }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">企业性质：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input"  id="nature" name="nature" value="${detail.nature }">
                          </div>
                          <div class="edit_item">
                          	  <div class="edit_item">
		                            <label class="edit_label">销售币种：</label>
		                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change">
		                             		<option value="">请选择</option>
	                            	</select>
	                            	 <input id="currency_hidden" type="hidden" value="${detail.currency }">
	                          </div>
                          </div>
                           
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">中文名：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="chinaName" name="chinaName" value="${detail.chinaName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">英文简称：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="enSimpleName" name="enSimpleName" value="${detail.enSimpleName }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">英文名：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="enName" name="enName" value="${detail.enName }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>注册地：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="companyAddr"name="companyAddr" value="${detail.companyAddr }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">客户等级编码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="codeGrade"name="codeGrade" value="${detail.codeGrade }">
                          </div>
                          <div class="edit_item">
                              <label class="edit_label">客户授权码：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="authNo" name="authNo" value="${detail.authNo }">
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
                              <label class="edit_label">主数据ID：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="mainId"name="mainId" value="${detail.mainId }" <c:if test="${ detail.mainId != null}">readonly</c:if>>
                          </div>
                          <div class="edit_item">
                              <label class="edit_label"><i class="red">*</i>是否内部公司：</label>
                              <select class="edit_input" name="type" id="type">
                                  <option value="">请选择</option>
                                  <c:choose>
                                      <c:when test="${detail.type=='1' }">
                                          <option value="1" selected>是</option>
                                      </c:when>
                                      <c:otherwise>
                                          <option value="1">是</option>
                                      </c:otherwise>
                                  </c:choose>
                                  <c:choose>
                                      <c:when test="${detail.type=='2' }">
                                          <option value="2" selected>否</option>
                                      </c:when>
                                      <c:otherwise>
                                          <option value="2">否</option>
                                      </c:otherwise>
                                  </c:choose>
                              </select>
                          </div>
                          <div class="edit_item">
                          		<label class="edit_label innerMerchantDiv" style="display:none; "><i class="red">*</i>内部公司：</label>
	                              <select class="edit_input innerMerchantDiv" style="display:none; " name="innerMerchantId" id="innerMerchantId">
	                              	<option value="">请选择</option>
                                       <c:forEach items="${merchantList }" var="merchant">
                                           <option value="${merchant.value }">${merchant.label }</option>
                                       </c:forEach>
	                              </select>
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item" style="flex: 2">
                              <label class="edit_label" style="flex: 0.4">经营范围：</label>
                              <textarea class="edit_input" rows="5" id="operationScope"name="operationScope" >${detail.operationScope }</textarea>
                          </div>
                           <div class="edit_item">
                          	  <label class="edit_label">税号：</label>
                              <input type="text" required="" parsley-type="email" class="edit_input" id="taxNo" name="taxNo" value="${detail.taxNo }">                        
                          </div>
                      </div>
                      <div class="edit_box">
                          <div class="edit_item">
						     <label class="edit_label">渠道分类：</label>
						     <select class="edit_input" name="channelClassify" id="channelClassify">
						        <option value="">请选择</option>
						     </select >
						     <input id="channelClassify_hidden" type="hidden" value="${detail.channelClassify }">
						  </div>
                           <div class="edit_item">                         	                         
                          </div>
                          <div class="edit_item">                         	                         
                          </div>
                      </div>

					<ul class="nav nav-tabs">
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('3');" class="nav-link active" >银行信息</a>
						</li>
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('0');" class="nav-link" >联系方式</a>
						</li>	
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('1');" class="nav-link" >外部编码配置</a>
						</li>	
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('2');" class="nav-link" >合作公司</a>
						</li>
		           	</ul>

								<div class="tab-content">
                                    <div class="tab-pane fade show active" id="itemList">
                                    
                                    <div id="mytable3">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                             
						                      <!-- <div class="title_text">银行信息</div> -->
						                      <div class="edit_box mt20">
						                          <div class="edit_item">
						                              <label class="edit_label">开户银行：</label>
						                              <input type="text" required=""  class="edit_input" id="depositBank" name="depositBank" value="${detail.depositBank }">
						                          </div>
						                          <div class="edit_item">
						                              <label class="edit_label">银行账号：</label>
						                              <input type="text" required="" class="edit_input"
						                                     id="bankAccount" name="bankAccount" value="${detail.bankAccount }">
						                          </div>
						                          <div class="edit_item">
						                          </div>
						                      </div>
						                      <div class="edit_box">
						                          <div class="edit_item">
						                              <label class="edit_label">银行账户：</label>
						                              <input type="text" required="" parsley-type="email" class="edit_input"
						                                     id="beneficiaryName" name="beneficiaryName" value="${detail.beneficiaryName }">
						                          </div>
						                          <div class="edit_item">
						                              <label class="edit_label">开户行地址：</label>
						                              <input type="text" required="" parsley-type="email" class="edit_input"
						                                     id="bankAddress" name="bankAddress" value="${detail.bankAddress }">
						                          </div>
						                          <div class="edit_item"></div>
						                      </div>
						                      <div class="edit_box">
						                          <div class="edit_item">
						                              <label class="edit_label">Swift Code：</label>
						                              <input type="text" required="" parsley-type="email" class="edit_input"
						                                     id="swiftCode" name="swiftCode" value="${detail.swiftCode }">
						                          </div>
						                          <div class="edit_item">
						                          </div>
						                          <div class="edit_item"></div>
						                      </div>

                                            </table>
                                        </div>
                                    
                                        <div id="mytable" style="display: none;">
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                             <div class="edit_box mt20">
						                        <div class="edit_item">
						                            <label class="edit_label">法人代表：</label>
						                            <input type="text" required="" parsley-type="email" class="edit_input" id="legalPerson"name="legalPerson" value="${detail.legalPerson }">
						                        </div>
						                        <div class="edit_item">
						                            <label class="edit_label">法人国籍：</label>
						                            <input type="text" required="" parsley-type="email" class="edit_input" id="legalNationality"name="legalNationality" value="${detail.legalNationality }">
						                        </div>
						                        <div class="edit_item"></div>
						                    </div>
						                    <div class="edit_box">
						                        <div class="edit_item">
						                            <label class="edit_label" style="font-size: 13px">法人代表身份证：</label>
						                            <input type="text" required="" parsley-type="email" class="edit_input" id="legalCardNo"name="legalCardNo" value="${detail.legalCardNo }" maxlength="18" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="18">
						                        </div>
						                        <div class="edit_item">
						                            <label class="edit_label">法人电话：</label>
						                            <input type="text" required="" parsley-type="email" class="edit_input" id="legalTel" name="legalTel" value="${detail.legalTel }" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"maxlength="11">
						                        </div>
						                        <div class="edit_item"></div>
						                    </div>
						                    <div class="edit_box">
						                        <div class="edit_item">
						                            <label class="edit_label">公司电话：</label>
						                            <input type="text" required="" parsley-type="email" class="edit_input" id="oTelNo"name="oTelNo" value="${detail.oTelNo }" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"maxlength="11">
						                        </div>
						                        <div class="edit_item"></div>
						                        <div class="edit_item"></div>
						                    </div>

                                            </table>
                                        </div>
                                       <div id="mytable1" style="display: none;">
                                          <table class="table table-striped" cellspacing="0" width="100%">
                                              
                                               <div class="edit_box mt20">
						                          <div class="edit_item">
						                              <div class="edit_item">
						                                  <label class="edit_label">NC平台编码：</label>
						                                  <select class="edit_input" name="ncPlatformCode" id="ncPlatformCode" parsley-trigger="change">
						                                      <option value="">请选择</option>
						                                  </select>
						                              </div>
						                              <input id="ncPlatformCode_hidden" type="hidden" value="${detail.ncPlatformCode }">
						                          </div>
						                          <div class="edit_item">
						                              <div class="edit_item">
						                                  <label class="edit_label">NC渠道编码：</label>
						                                  <select class="edit_input" name="ncChannelCode" id="ncChannelCode" parsley-trigger="change">
						                                      <option value="">请选择</option>
						                                  </select>
						                              </div>
						                              <input id="ncChannelCode_hidden" type="hidden" value="${detail.ncChannelCode }">
						                          </div>
						                          <div class="edit_item">
						                          </div>
						                      </div>
        
                                            </table>  
                                        </div>

                                        <div id="mytable2" style="display: none;">
                                         <div class="col-12 ml10">
						                    <div class="row">
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="addMerchantRel();">新增</button>
						                        </div>
						                         <div class="col-1 mt10">
						                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="deleteMerchantRel">删 除</button>
						                        </div> 
						                    </div>
						                    </div> 
                                           
		                                   <div class="form-row mt20 ml15">
		                                    <table id="table-list-group" class="table table-striped" cellspacing="0"  width="100%">		                                        
					                      		<tbody>
					                            </tbody>
		                                    </table>
		                               	   </div>
                                            
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="batchDefault"></div>
                                </div>


                      <input type="hidden" id="relId2" value="${customerMerchantRelModel.id}">

                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              </div>
                  <!-- end row -->
          </form>
          <jsp:include page="/WEB-INF/views/modals/16001.jsp" />
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		
		$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
        $module.constant.getConstantListByNameURL.call($('select[name="ncPlatformCode"]')[0],"platform_codeList",null);
        $module.constant.getConstantListByNameURL.call($('select[name="ncChannelCode"]')[0],"channel_codeList",null);
        $module.constant.getConstantListByNameURL.call($('select[name="channelClassify"]')[0],"customerInfo_channelClassifyList",null);

        // 回显渠道分类
        var channelClassify = $("#channelClassify_hidden").val() ;
		if(channelClassify){
			$("#channelClassify").val(channelClassify) ;
		}
        
        /* if ($("#relId2").val()){
            $module.constant.getConstantListByNameURL.call($('select[name="accountPeriod"]')[0],"customerInfo_accountPeriodList",null);
            $module.constant.getConstantListByNameURL.call($('select[name="settlementType"]')[0],"settlement_typeList",null);
        } */
		//币种赋值
		var currency = $("#currency_hidden").val() ;
		if(currency){
			$("#currency").val(currency) ;
		}
		/* var accountPeriod = $("#accountPeriod_hidden").val() ;
		if(accountPeriod){
			$("#accountPeriod").val(accountPeriod) ;
		} */
		/* var settlementType = $("#settlementType_hidden").val() ;
		if(settlementType){
			$("#settlementType").val(settlementType) ;
		} */
		var ncChannelCode = $("#ncChannelCode_hidden").val() ;
		if(ncChannelCode){
			$("#ncChannelCode").val(ncChannelCode) ;
		}
		var ncPlatformCode = $("#ncPlatformCode_hidden").val() ;
		if(ncPlatformCode){
			$("#ncPlatformCode").val(ncPlatformCode) ;
		}

		
		if($("#type").val() == '1'){
			$(".innerMerchantDiv").show() ;
			
			var innerMerchantId = '${detail.innerMerchantId}' ;
			$("#innerMerchantId").val(innerMerchantId) ;
		}
		
		$("#type").change(function(){
    		var val = $("#type").val() ;
    		
    		if('1' == val){
    			$(".innerMerchantDiv").show() ;
    		}else{
    			$("#innerMerchantId").val('');
    			$(".innerMerchantDiv").hide() ;
    		}
    	}) ;

		//保存按钮
		$("#save-buttons").click(function(){
			debugger;
            $("#accountPeriod").attr("disabled",false);
			//var form = $("#edit-form").serializeArray();
			
			var obj = {};
 			var id = $("#id").val();
 			obj['id']=id;
 			var name = $("#name").val();
 			obj['name']=name;
 			var simpleName = $("#simpleName").val();
 			obj['simpleName']=simpleName;
 			var creditCode = $("#creditCode").val();
 			obj['creditCode']=creditCode;
 			var orgCode = $("#orgCode").val();
 			obj['orgCode']=orgCode;
 			var nature = $("#nature").val();
 			obj['nature']=nature;
 			var currency = $("#currency").val();
 			obj['currency']=currency;
 			var chinaName = $("#chinaName").val();
 			obj['chinaName']=chinaName;
 			var enSimpleName = $("#enSimpleName").val();
 			obj['enSimpleName']=enSimpleName;
 			var enName = $("#enName").val();
 			obj['enName']=enName;
 			var companyAddr = $("#companyAddr").val();
 			obj['companyAddr']=companyAddr;
 			var codeGrade = $("#codeGrade").val();
 			obj['codeGrade']=codeGrade;
 			var authNo = $("#authNo").val();
 			obj['authNo']=authNo;
 			var mainId = $("#mainId").val();
 			obj['mainId']=mainId;
 			var type = $("#type").val();
 			obj['type']=type;
 			var innerMerchantId = $("#innerMerchantId").val();
 			obj['innerMerchantId']=innerMerchantId;
 			var operationScope = $("#operationScope").val();
 			obj['operationScope']=operationScope;
 			var taxNo = $("#taxNo").val();
 			obj['taxNo']=taxNo;
 			var legalPerson = $("#legalPerson").val();
 			obj['legalPerson']=legalPerson;
 			var legalNationality = $("#legalNationality").val();
 			obj['legalNationality']=legalNationality;
 			var legalCardNo = $("#legalCardNo").val();
 			obj['legalCardNo']=legalCardNo;
 			var legalTel = $("#legalTel").val();
 			obj['legalTel']=legalTel;
 			var oTelNo = $("#oTelNo").val();
 			obj['oTelNo']=oTelNo;
 			var ncPlatformCode = $("#ncPlatformCode").val();
 			obj['ncPlatformCode']=ncPlatformCode;
 			var ncChannelCode = $("#ncChannelCode").val();
 			obj['ncChannelCode']=ncChannelCode;
 			var channelClassify = $("#channelClassify").val();
 			obj['channelClassify']=channelClassify;
 			
 			var depositBank = $("#depositBank").val();
 			obj['depositBank']=depositBank;
 			var bankAccount = $("#bankAccount").val();
 			obj['bankAccount']=bankAccount;
 			var beneficiaryName = $("#beneficiaryName").val();
 			obj['beneficiaryName']=beneficiaryName;
 			var bankAddress = $("#bankAddress").val();
 			obj['bankAddress']=bankAddress;
 			var swiftCode = $("#swiftCode").val();
 			obj['swiftCode']=swiftCode;
 			
			//必填项校验
			if($("#name").val()==""){
				$custom.alert.error("客户名称不能为空！");
				return ;
			}
			if($("#creditCode").val()==""){
				$custom.alert.error("营业执照号不能为空！");
				return ;
			}
			if($("#orgCode").val()==""){
				$custom.alert.error("组织机构代码不能为空！");
				return ;
			}
			
			if($("#type").val()==""){
				$custom.alert.error("是否内部公司不能为空！");
				return ;
			}
			
			if($("#type").val()=="1" && $("#innerMerchantId").val() == ""){
				$custom.alert.error("请选择内部公司！");
				return ;
			}
			if($("#companyAddr").val()==""){
				$custom.alert.error("注册地不能为空！");
				return ;
			}
			/* if($("#accountPeriod").val()==""){
				$custom.alert.error("账期不能为空！");
				return ;
			}
            if($("#settlementType").val()==""){
                $custom.alert.error("结算类型不能为空！");
                return ;
            } */
            var itemList = new Array();
            var falg="";
            $('#table-list-group tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};			
				var merchantId = $(this).find('input[name="merchantId"]').val();//商家id
				item['merchantId']=merchantId;
				var merchantName = $(this).find('input[name="merchantName"]').val();// 商家名称
				item['merchantName']=merchantName;
				var settlementType = $(this).find('select[name="settlementType"]').val();
				item['settlementType']=settlementType;
				var accountPeriod = $(this).find('select[name="accountPeriod"]').val();
				item['accountPeriod']=accountPeriod;
				//var salePriceManage = $(this).find('input[name="salePriceManage"]');
				//var salePriceManage = $(this).find('input[name="salePriceManage"]').is(':checked');
				//var salePriceManage = $(this).find('input[name="salePriceManage"]').attr("checked");
				var salePriceManage ="0";// 是否启用销售价格管理 1-启用 0-不启用
				if ($(this).find('input[name="salePriceManage"]').is(':checked')==true) { 
					salePriceManage='1';
				} 
				item['salePriceManage']=salePriceManage;
				item['salePriceManage']=salePriceManage;
				var businessModel = $(this).find('select[name="businessModel"]').val();
				item['businessModel']=businessModel;
				var tariffRateConfigId = $(this).find('select[name="tariffRateConfigId"]').val();
				if(!tariffRateConfigId){// 标识税率必填标识
					falg="1";
				}
				item['tariffRateConfigId']=tariffRateConfigId;
				itemList.push(item);
			});
            if("1"==falg){
				$custom.alert.error("请选择税率！");
				return ;
			}
            var json = {};
            var deleteIds = $("#deleteIds").val();
 			json['deleteIds']=deleteIds;
            json['merchantList']=itemList;
			var jsonObject= JSON.stringify(json); 
			obj['json']=jsonObject;
            
            
            var url = "/customer/modifyCustomer.asyn";
			$custom.request.ajax(url, obj, function(result){
				if(result.state == '200'){
					//1成功 0失败
					if (result.data.status=='1') {
						$custom.alert.success("编辑成功！");
						setTimeout(function () {
							$load.a("/customer/toPage.html");
						}, 1000);
					}else {

						$custom.alert.error(result.data.massage);
					}
				}else{
					$custom.alert.error(result.message);
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/customer/toPage.html");
		});
		
	});
    /* $("#settlementType").change(function () {
        var val = $("#settlementType").val() ;
        if("2" == val){
            $("#accountPeriod option[value='0']").prop("selected",true);
            $("#accountPeriod").attr("disabled",true);
        }else {
            $("#accountPeriod").attr("disabled",false);
        }
    }); */
   /*  if ($("#relId2").val()){
        $(function () {
            var val = $("#settlementType").val() ;
            if("2" == val){
                $("#accountPeriod option[value='0']").prop("selected",true);
                $("#accountPeriod").attr("disabled",true);
            }
        })
    } */
	function changeTable(type) {
        if (type == '3') {
        	$("#mytable3").show();
            $("#mytable").hide();
            $("#mytable1").hide();
            $("#mytable2").hide();
            
        }else if (type == '0') {
        	$("#mytable").show();
            $("#mytable1").hide();
            $("#mytable2").hide();
            $("#mytable3").hide();
		}else  if (type == '1') {
            $("#mytable1").show();
            $("#mytable").hide();
            $("#mytable2").hide();
            $("#mytable3").hide();
        }else  if (type == '2') {
        	
        	debugger;
        	var customerId = $("#id").val() ;
        	$("#table-list-group").empty();
        	$custom.request.ajax("/customer/getCustomerMerchantRelList.asyn", {"customerId": customerId}, function(res){
        		var list=res.data.relList;
        		var settlementTypeList=res.data.settlementTypeList;
        		var accountPeriodList=res.data.accountPeriodList;
        		var businessModelList=res.data.businessModelList;
        		var tariffRateConfigList=res.data.tariffRateConfigList;
        		if(res.state == 200){	
        			var html2 = '<thead><tr><th><input id="checkbox11" type="checkbox" name="all"></th><th>公司编码</th><th>公司名称</th><th>结算类型</th><th>账期</th><th>销售价管理</th><th><i class="red">*</i>税率</th><th>销售模式</th></tr></thead>';
        			html2=html2+'<tbody>';
        			
        			$(list).each(function(index,event){
                		debugger;
        				
        				// 结算类型
        				var settlementTypeHtml='<td><select name="settlementType" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(settlementTypeList).each(function(index,settlement){
        					var selected1="";
        	        		debugger;
        					if (event.settlement_type==settlement.key) {
        						selected1="selected";
    						}
        					settlementTypeHtml=settlementTypeHtml+
        						  '<option value="'+settlement.key+'"'+selected1+'>'+settlement.value+'</option>';
        				});
        				settlementTypeHtml=settlementTypeHtml+'</select></td>';
        				//账期

        				var accountPeriodHtml='<td><select name="accountPeriod" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(accountPeriodList).each(function(index,accountPeriod){
        					var selected2="";
        	        		debugger;
        					if (event.account_period==accountPeriod.key) {
        						selected2="selected";
    						}
        					accountPeriodHtml=accountPeriodHtml+
        						  '<option value="'+accountPeriod.key+'"'+selected2+'>'+accountPeriod.value+'</option>';
        				});
        				accountPeriodHtml=accountPeriodHtml+'</select></td>';
        				var checked3="";
        				if (event.sale_price_manage=='1') {
        					checked3="checked";
						}
        				
        				var businessModelHtml='<td><select name="businessModel" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(businessModelList).each(function(index,businessModel){
        					var selected4="";
        	        		debugger;
        					if (event.business_model==businessModel.key) {
        						selected4="selected";
    						}
        					businessModelHtml=businessModelHtml+
        						  '<option value="'+businessModel.key+'"'+selected4+'>'+businessModel.value+'</option>';
        				});
        				businessModelHtml=businessModelHtml+'</select></td>';
        				
        				
        				var tariffRateConfigHtml='<td><select name="tariffRateConfigId" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(tariffRateConfigList).each(function(index,tariffRateConfig){
        					var selected5="";
        	        		debugger;
        					if (event.rate_id==tariffRateConfig.id) {
        						selected5="selected";
    						}
        					tariffRateConfigHtml=tariffRateConfigHtml+
        						  '<option value="'+tariffRateConfig.id+'"'+selected5+'>'+tariffRateConfig.rate+'</option>';
        				});
        				tariffRateConfigHtml=tariffRateConfigHtml+'</select></td>';
        				
        				html2+="<tr>"+
    	            	"<td> <input type='hidden' name='customerMerchantRelId' value='"+event.id+"'/> <input type='checkbox'   class='iCheck' name='item-checkbox'><input type='hidden' name='merchantId' value='"+event.merchant_id+"'/></td>"+
                    	"<td>"+(event.code==null?'':event.code)+"<input type='hidden' name='merchantCode' value='"+(event.code==null?'':event.code)+"'/></td>"+
                        "<td>"+(event.merchant_name==null?'':event.merchant_name)+"<input type='hidden' name='merchantName' value='"+(event.name==null?'':event.merchant_name)+"'/></td>"+
                        settlementTypeHtml+
                        accountPeriodHtml+
                        '<td><input type="checkbox"  name="salePriceManage" '+checked3+'/>启用</td>'+ 
                        tariffRateConfigHtml+
                        businessModelHtml+
                        "</tr>";
					}) ;
        			html2=html2+'</tbody>';
        			$("#table-list-group").append(html2); 
        			 $("#mytable2").show();
                     $("#mytable").hide();
                     $("#mytable1").hide();
                     $("#mytable3").hide();
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        }
    }
	 //新增
	 function addMerchantRel(){
		 $m16001.init() ;
	 } 
	 
		//删除行
	 $("#deleteMerchantRel").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            var merchantId = $(this).parent().find("input[name='customerMerchantRelId']").val();
	            if(merchantId){
	            	merchantIds.push(merchantId);
	            }
	            $("#table-list-group").find("tr:eq("+n+")").remove();
	        });
	        $("#deleteIds").val(merchantIds);
	        $("input[name='all']").prop("checked",false);
	    });
	
	//全选
	$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});	
</script>
