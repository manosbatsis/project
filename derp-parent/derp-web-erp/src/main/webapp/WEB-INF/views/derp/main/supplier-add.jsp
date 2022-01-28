<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
        <form id="add-form" action="/supplier/saveSupplier.asyn">
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
                                            <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/supplier/toPage.html')">供应商列表</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/supplier/toAddPage.html')">供应商新增</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <div class="margin">
                                	<input type="hidden"  id="source" name="source" value="${detail.source }">
                                    <div class="edit_box">                                        
                                        <div class="edit_item">
				                              <label class="edit_label"><i class="red">*</i>供应商名称：</label>
				                              <input type="text" required="" parsley-type="email" class="edit_input" id="name" name="name" value="${detail.name }">
				                          </div>
                                          <div class="edit_item">
                                             <label class="edit_label">供应商简称：</label>
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
				                              <label class="edit_label">客户等级编码：</label>
				                              <input type="text" required="" parsley-type="email" class="edit_input" value="${detail.codeGrade }" id="codeGrade" name="codeGrade">
				                          </div>
				                          <div class="edit_item">
				                          		<label class="edit_label">企业性质：</label>
				                              <input type="text" required="" parsley-type="email" class="edit_input" id="nature" name="nature" value="${detail.nature }">
				                          </div>
                                    </div>
                                    <div class="edit_box">
                                        <div class="edit_item">
                                            <label class="edit_label">中文名：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="chinaName"name="chinaName" value="${detail.chinaName }">
                                        </div>
                                        <div class="edit_item">
			                               <label class="edit_label">英文简称：</label>
			                               <input type="text" required="" parsley-type="email" class="edit_input" id="enSimpleName"name="enSimpleName" value="${detail.enSimpleName }">
			                            </div>
			                            <div class="edit_item">
			                               <label class="edit_label">英文名：</label>
			                               <input type="text" required="" parsley-type="email" class="edit_input" id="enName"name="enName" value="${detail.enName }">
			                            </div>
                                    </div>
                                    <div class="edit_box">
                                        <div class="edit_item">
                                            <label class="edit_label">主数据ID：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="mainId"name="mainId" value="${detail.mainId }" <c:if test="${ detail.mainId != null}">readonly</c:if>>
                                        </div>
                                        <div class="edit_item">
                                            <label class="edit_label">业务员：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="salesman" name="salesman" value="${detail.salesman }">
                                        </div>
                                        <div class="edit_item">
                                            <label class="edit_label">省市区代码：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="cityCode" name="cityCode" value="${detail.cityCode }">
                                        </div>
                                    </div>
                                    <div class="edit_box">
                                        <div class="edit_item">
                                            <label class="edit_label">注册地：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="companyAddr"name="companyAddr" value="${detail.companyAddr }">
                                        </div>
			                            <div class="edit_item">
			                               <label class="edit_label">企业地址：</label>
			                               <input type="text" required="" parsley-type="email" class="edit_input" id="businessAddress" name="businessAddress" value="${detail.businessAddress }">
			                            </div>
			                            <div class="edit_item">
			                          	   <label class="edit_label">企业英文地址：</label>
			                               <input type="text" required="" parsley-type="email" class="edit_input" id="enBusinessAddress" name="enBusinessAddress" value="${detail.enBusinessAddress }">
                                        </div>
									</div>
									<div class="edit_box">
                                        <div class="edit_item">
				                            <label class="edit_label">采购币种：</label>
				                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change">
				                             		<option value="">请选择</option>
			                            	</select>
			                             </div>
                                        <div class="edit_item">
                                              <label class="edit_label"><i class="red">*</i>是否内部公司：</label>
                                              <select class="edit_input" name="type" id="type">
                                                  <option value="">请选择</option>
                                                  <option value="1">是</option>
                                                  <option value="2">否</option>
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
                                      <div class="edit_item">
                                          <label class="edit_label" >经营范围：</label>
                                          <textarea class="edit_input" id="operationScope"name="operationScope" >${detail.operationScope }</textarea>
                                      </div>
                                      <div class="edit_item">
                                          <label class="edit_label">传真：</label>
                                          <input type="text" required="" parsley-type="email" class="edit_input" id="fax"name="fax" value="${detail.fax }">
                                      </div>
                                      <div class="edit_item">
                                          <label class="edit_label">email：</label>
                                          <input type="text" required="" parsley-type="email" class="edit_input" id="email"name="email" value="${detail.email }">
                                      </div>
		                          </div>
                                  <div class="edit_box">
                                        <div class="edit_item">
                                            <label class="edit_label">备注：</label>
                                            <input type="text" required="" parsley-type="email" class="edit_input" id="remark"name="remark" value="${detail.remark }">
                                        </div>
                                        <div class="edit_item"></div>
                                        <div class="edit_item"></div>
                                  </div>
		                          <div class="edit_box">
                                       <div class="edit_item"></div>
                                       <div class="edit_item"></div>
		                               <div class="edit_item"></div>
                                  </div>
                                  
                                  
                   <ul class="nav nav-tabs">
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('0');" class="nav-link active" >银行信息</a>
						</li>	
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('1');" class="nav-link" >联系方式</a>
						</li>	
						<li class="nav-item">
							<a href="#" data-toggle="tab" onclick="changeTable('2');" class="nav-link" >合作公司</a>
						</li>
		           	</ul>
		           	
		           	
                   			<div class="tab-content">
                                    <div class="tab-pane fade show active" id="itemList">
                                        <div id="mytable">
                                        
	                                        <div class="col-12 ml10">
							                    <div class="row">
							                         <div class="col-1 mt10">
							                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="addCustomerBank();">新增</button>
							                        </div>
							                         <div class="col-1 mt10">
							                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="deleteCustomerBank">删 除</button>
							                        </div> 
							                    </div>
							                    </div> 
	                                           
			                                   <div class="form-row mt20 ml15">
			                                    <table id="table-list-customerbank" class="table table-striped" cellspacing="0"  width="100%">
			                                        <thead>
			                                        <tr>
			                                           <th><input id="checkbox11" type="checkbox" name="bankall"></th>
							                           <th>开户银行</th>
							                           <th>银行账号</th>
							                           <th>银行账户</th>
							                           <th>开户行地址</th>
							                           <th>Swift Code</th>
							                           <th>合作公司</th>
						                      		</tr>
						                      		</thead>
						                      		<tbody>
						                            </tbody>
			                                    </table>
			                               	   </div>
                                        

                                        </div>
                                       <div id="mytable1" style="display: none;">
                                          <table class="table table-striped" cellspacing="0" width="100%">
                                              <div class="title_text">联系方式</div>
		                                    <div class="edit_box mt20">
		                                        <div class="edit_item">
		                                            <label class="edit_label">法人代表：</label>
		                                            <input type="text" required="" parsley-type="email" class="edit_input"
		                                                   id="legalPerson" name="legalPerson">
		                                        </div>
		                                        <div class="edit_item">
		                                            <label class="edit_label">法人国籍：</label>
		                                            <input type="text" required="" parsley-type="email" class="edit_input"
		                                                   id="legalNationality" name="legalNationality">
		                                        </div>
		                                        <div class="edit_item">
		                                            <label class="edit_label" style="font-size: 13px">法人代表身份证：</label>
		                                            <input type="text" required="" parsley-type="email" class="edit_input"
		                                                   id="legalCardNo" name="legalCardNo">
		                                        </div>
		                                    </div>
		                                    <div class="edit_box">
		                                        <div class="edit_item">
		                                            <label class="edit_label">法人电话：</label>
		                                            <input type="text" required="" parsley-type="email" class="edit_input"
		                                                   id="legalTel" name="legalTel">
		                                        </div>
		                                        <div class="edit_item">
		                                            <label class="edit_label">公司电话：</label>
		                                            <input type="text" required="" parsley-type="email" class="edit_input" id="oTelNo" name="oTelNo">
		                                        </div>
		                                        <div class="edit_item"></div>
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
		                                        <thead>
		                                        <tr>
		                                           <th><input id="checkbox11" type="checkbox" name="all"></th>
						                           <th>公司编码</th>
						                           <th>公司名称</th>
						                           <th>采购价格管理</th>
						                           <th><i class="red">*</i>税率</th>
					                      		</tr>
					                      		</thead>
					                      		<tbody>
					                            </tbody>
		                                    </table>
		                               	   </div>
                                            
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="batchDefault"></div>
                                </div>

                                    <div class="form-row m-t-50">
                                        <div class="col-12">
                                            <div class="row">
                                                <div class="col-6">
                                                    <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" value="保 存"/>
                                                </div>
                                                <div class="col-6">
                                                    <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
        </form>
        <jsp:include page="/WEB-INF/views/modals/16002.jsp" />
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">

    $(function () {    	

    	//加载销售币种的下拉数据	
		$module.currency.loadSelectData.call($("select[name='currency']")[0],"${detail.currency}");
    	
    	$("#type").change(function(){
    		var val = $("#type").val() ;
    		
    		if('1' == val){
    			$(".innerMerchantDiv").show() ;
    		}else{
    			$("#innerMerchantId").val('');
    			$(".innerMerchantDiv").hide() ;
    		}
    	});
    	
        //保存按钮
        $("#save-buttons").click(function () {
        	//$("#save-buttons").attr("disabled", true);
            //var form = $("#add-form").serializeArray();
            var obj = {};
 			var source = $("#source").val();
 			obj['source']=source;
 			var name = $("#name").val();
 			obj['name']=name;
 			var simpleName = $("#simpleName").val();
 			obj['simpleName']=simpleName;
 			var creditCode = $("#creditCode").val();
 			obj['creditCode']=creditCode;
 			var orgCode = $("#orgCode").val();
 			obj['orgCode']=orgCode;
 			var codeGrade = $("#codeGrade").val();
 			obj['codeGrade']=codeGrade;
 			var nature = $("#nature").val();
 			obj['nature']=nature;
 			var chinaName = $("#chinaName").val();
 			obj['chinaName']=chinaName;
 			var enSimpleName = $("#enSimpleName").val();
 			obj['enSimpleName']=enSimpleName;
 			var enName = $("#enName").val();
 			obj['enName']=enName;
 			var mainId = $("#mainId").val();
 			obj['mainId']=mainId;
 			
 			
 			var salesman = $("#salesman").val();
 			obj['salesman']=salesman;
 			var cityCode = $("#cityCode").val();
 			obj['cityCode']=cityCode;
 			var companyAddr = $("#companyAddr").val();
 			obj['companyAddr']=companyAddr;
 			var businessAddress = $("#businessAddress").val();
 			obj['businessAddress']=businessAddress;
 			var enBusinessAddress = $("#enBusinessAddress").val();
 			obj['enBusinessAddress']=enBusinessAddress;
 			var currency = $("#currency").val();
 			obj['currency']=currency;
 			var type = $("#type").val();
 			obj['type']=type;
 			var innerMerchantId = $("#innerMerchantId").val();
 			obj['innerMerchantId']=innerMerchantId;
 			var operationScope = $("#operationScope").val();
 			obj['operationScope']=operationScope;
 			var fax = $("#fax").val();
 			obj['fax']=fax;
 			var email = $("#email").val();
 			obj['email']=email;
 			var remark = $("#remark").val();
 			obj['remark']=remark;
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

         	 //必填项校验
			if($("#name").val()==""){
				$custom.alert.error("供应商名称不能为空！");
				return ;
			}
			//必填项校验
			if($("#creditCode").val()==""){
				$custom.alert.error("营业执照号不能为空！");
				return ;
			}
			//必填项校验
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
			var itemBankList = new Array();
			$('#table-list-customerbank tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};
				var aa=i;
				
				
				var ss=$(this);
				//var ss1=$(this).find("#bankMerchantIds']").val();
				var bankMerchantIdsArr = $(this).find('select[id="bankMerchantIds"]').val();//商家id
		
		        var bankMerchantIdsStr='';// 多个商家用逗号隔开
		        if(bankMerchantIdsArr!='' && bankMerchantIdsArr!=null){
		        	bankMerchantIdsStr=bankMerchantIdsArr.toString();
		        }
		        item['bankMerchantIdsStr']=bankMerchantIdsStr;

				var depositBank = $(this).find('input[name="depositBank"]').val();//开户银行
				item['depositBank']=depositBank;
				var bankAccount = $(this).find('input[name="bankAccount"]').val();// 银行账号
				item['bankAccount']=bankAccount;
				var beneficiaryName = $(this).find('input[name="beneficiaryName"]').val();//银行账户
				item['beneficiaryName']=beneficiaryName;
				var bankAddress = $(this).find('input[name="bankAddress"]').val();// 开户行地址
				item['bankAddress']=bankAddress;
				var swiftCode = $(this).find('input[name="swiftCode"]').val();// Swift Code
				item['swiftCode']=swiftCode;
			
				itemBankList.push(item);
			});
			
			
			
			var itemList = new Array();            
            var falg="";
            $('#table-list-group tbody tr').each(function(i){// 遍历 tr
            	debugger;
				var item ={};			
				var merchantId = $(this).find('input[name="merchantId"]').val();//商家id
				item['merchantId']=merchantId;
				var merchantName = $(this).find('input[name="merchantName"]').val();// 商家名称
				item['merchantName']=merchantName;
				var purchasePriceManage ="0";// 是否启用销售价格管理 1-启用 0-不启用
				if ($(this).find('input[name="purchasePriceManage"]').is(':checked')==true) { 
					purchasePriceManage='1';
				} 
				item['purchasePriceManage']=purchasePriceManage;
				var tariffRateConfigId = $(this).find('select[name="tariffRateConfigId"]').val();
				item['tariffRateConfigId']=tariffRateConfigId;
				if(!tariffRateConfigId){// 标识税率必填标识
					falg="1";
				}
				itemList.push(item);
			});
            if("1"==falg){
				$custom.alert.error("请选择税率！");
				return ;
			}
            var json = {};
            json['itemBankList']=itemBankList;
            json['merchantList']=itemList;
			var jsonObject= JSON.stringify(json); 
			obj['json']=jsonObject;
			debugger;
			
			
			
            var url = "/supplier/saveSupplier.asyn";
            $custom.request.ajax(url, obj, function (result) {  
            	//1成功 0失败
                if (result.state == '200') {
                	if (result.data.status=='1') {
                		$custom.alert.success("新增成功！");
                        setTimeout(function () {
                            $load.a("/supplier/toPage.html");
                        }, 1000);
    				} else {
    					$("#save-buttons").attr("disabled", false);
    					$custom.alert.error	(result.data.massage);
    				}                                                          
                }else{
					$("#save-buttons").attr("disabled", false);
                    $custom.alert.error(result.message);
                }
            });
        });

       

    });
    //取消按钮
    $("#cancel-buttons").click(function () {
        $load.a("/supplier/toPage.html");
    });
    
	function changeTable(type) {
        if (type == '0') {
            $("#mytable").show();
            $("#mytable1").hide();
            $("#mytable2").hide();
        }else  if (type == '1') {
            $("#mytable1").show();
            $("#mytable").hide();
            $("#mytable2").hide();
        }else  if (type == '2') {
            $("#mytable2").show();
            $("#mytable").hide();
            $("#mytable1").hide();
        }
    }
    
	 //新增
	 function addMerchantRel(){
		 $m16002.init() ;
	 } 
	 
		//删除行
	 $("#deleteMerchantRel").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            var merchantId = $(this).parent().find("input[name='merchantId']").val();
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
    
	var addNum=0;
	//新增
	 function addCustomerBank(){
		addNum+=1;
		var bankMerchantIds="bankMerchantIds"+addNum.toString(); // 下拉框是根据name赋值 name要 
		var str="select[name='"+bankMerchantIds+"']";
		debugger;		
		var bankMerchantHtml='<td><select   multiple  name="'+bankMerchantIds+'"  id="bankMerchantIds" title="请选择"  ></td>';			
		var bankHtml="<tr>"+
     	"<td><input type='checkbox'   class='iCheck' name='item-bank-checkbox'></td>"+
     	"<td><input type='text' name='depositBank' /></td>"+
     	"<td><input type='text' name='bankAccount' /></td>"+
     	"<td><input type='text' name='beneficiaryName' /></td>"+
     	"<td><input type='text' name='bankAddress' /></td>"+
     	"<td><input type='text' name='swiftCode' /></td>"+
     	bankMerchantHtml+
         "</tr>";
         $("#table-list-customerbank").append(bankHtml); 
         $module.merchant.getUserMerchantSelectBeanURL.call($(str)[0],null);
	 } 

	
		//删除行
	 $("#deleteCustomerBank").click(function() {
		 debugger;
	 		var merchantIds = [];
	        $("input[name='item-bank-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序	           
	            $("#table-list-customerbank").find("tr:eq("+n+")").remove();
	        });
	        $("input[name='bankall']").prop("checked",false);
	    });
	
	//全选
	$("input[name='bankall']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-bank-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-bank-checkbox']").prop("checked",false);
			}
		});	 
    
</script>
