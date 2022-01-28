<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="settlement-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              		<span class="header-title" >新增项目ID</span>
         		</div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from01">
                        <!-- 自定义参数  -->  
                                                                   
	                  <div class="form-group">
	                      <div class="col-12">
		                      <label ><i class="red">*</i>所属层级 : </label>
		                       <select class="input_msg" name="levelAdd" id="levelAdd" onchange="levelAddChange(this.value)" style="width: 202px;">
                               </select>
		                       <label ><i class="red" id="redAdd">*</i>上级费项 : </label>
		                       
		                       <select class="input_msg" name="parentProjectNameAdd" id="parentProjectNameAdd" onchange="parentProjectNameChange(this.value)" style="width: 202px;">
		                       <option value="">请选择</option>
                               </select>
	                      </div>
	                      
	                  </div> 
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label ><i class="red">*</i>本级费项名称: </label>
		                      <input type="text" class="input_msg" id="projectNameAdd" name="projectNameAdd" style="width: 202px;">
		                      
	                      	  <label ><i class="red">*</i>NC收支费项: </label>
	                      	   <select class="input_msg" name="receivePaymentSubjectAdd" id="receivePaymentSubjectAdd"  style="width: 202px;">
		                       <option value="">请选择</option>
                               </select>

		                      
	                      </div>
	                  </div>   
	                   <div class="form-group" >
	                      <div class="col-12">
		                       <label >主数据编码: </label>
		                      <input type="text" class="input_msg" id="inExpCodeAdd" name="inExpCodeAdd" style="width: 202px;">
							  <div id="addModuleId"  style="display: inline-block">
								  <label> <i class="red">*</i>适用模块:</label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable" style="display: inline-block;"value="1" onclick="test('1')"/>
									  <span>应付</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable"  style="display: inline-block;" value="2" onclick="test('1')"/>
									  <span>应收</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable" style="display: inline-block;" value="3" onclick="test('1')"/>
									  <span>预付</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable"style="display: inline-block;" value="4" onclick="test('1')"/>
									  <span>预收</span>
								  </label>
							  </div>
		                      <%-- <select class="input_msg" name="typeAdd" id="typeAdd"  style="width: 202px;" >
                               </select>--%>
	                      </div>
	                  </div>
						<div class="form-group">
							<div class="col-12" id="addTypeId"style="display: inline-block">
								<label> <i class="red">*</i>适用类型:</label>
								<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									<input type="checkbox" name="type-checkable" style="display: inline-block;" value="1" />
									<span>To B</span>
								</label>
								<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									<input type="checkbox" name="type-checkable"style="display: inline-block;"  value="2"/>
									<span>To C</span>
								</label>
								<%--<select class="input_msg" name="moduleType" id="moduleType" >
								</select>--%>
							</div>
						</div>
						<div class="form-group" id="customerDiv">
	                      <div class="col-12">
		                      <label ><i class="red">*</i>适用客户 : </label>
		                      <input checked="true" type="radio"  name="suitableCustomerAdd" value="1" onclick="suitableCustomerChange(this.value)" style="margin-top: 17px;">通用
		                      <input type="radio"  name="suitableCustomerAdd" value="2" onclick="suitableCustomerChange(this.value)" style="margin-top: 17px;">指定客户
		                      <label> </label>
		                      <select class="input_msg" name="customerIdAdd" id="customerIdAdd" multiple="multiple"  >
                              </select>
	                      </div>
	                  </div> 
	                  <div class="form-group"> 
	                      <div class="col-12">
		                      <label ><i class="red">*</i>状态: </label>
		                       <input checked="true" type="radio"  name="statusAdd" value="1" >启用
		                      <input type="radio"  name="statusAdd" value="0">禁用
		                      <label> </label>		                      
	                      </div>
	                  </div>               	

                       </form>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="settlementSave();">保存</button>
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="settlementCancel();">取消</button>
                      </div>
                  </div>
                </div>
				<input type="hidden" id="typeStr">
				<input type="hidden" id="moduleStr">
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

	function test(type){
		debugger
		var moduleCheckArr =[];
		$('input[name="module-checkable"]:checked').each(function() {
			var check=$(this).val();
			moduleCheckArr.push(check);
		});
		var flagOne=false;
		var flagTwo=false;
		for(var i=0; i<moduleCheckArr.length; i++){
			if(moduleCheckArr[i] == "1"||moduleCheckArr[i] == "3"){
				flagOne=true;
			}
		}
		for(var i=0; i<moduleCheckArr.length; i++) {
			if (moduleCheckArr[i] == "2" || moduleCheckArr[i] == "4"){
				flagTwo = true;
			}
		}
debugger
		if(flagOne){
			if(type=="1"){
				$("#addTypeId").hide();
				$("#customerDiv").hide();
				$('#addTypeId input[name="type-checkable"]').prop("checked",false);
				$('#customerDiv input[name="suitableCustomerAdd"]').prop("checked",false);
			}else{
				$("#editTypeId").hide();
				$("#editCustomerDiv").hide();
				if(type=="2"){
					$('#editTypeId input[name="type-checkable"]').prop("checked",false);
					$('#editCustomerDiv input[name="suitableCustomerEdit"]').prop("checked",false);
				}
			}
		}
		if(flagTwo){
			if(type=="1"){
				$("#addTypeId").show();
				$("#customerDiv").show();
				$("#customerDiv input[name='suitableCustomerAdd'][value='1']").prop("checked", true);
			}else{
				$("#editTypeId").show();
				$("#editCustomerDiv").show();
				var val=$('#editCustomerDiv input:radio[name="suitableCustomerEdit"]:checked').val();
				if(val==null){
					$("#editCustomerDiv input[name='suitableCustomerEdit'][value='1']").prop("checked",true);
				}
			}
		}
		debugger
		if(!flagOne && !flagTwo){
			if(type=="1"){
				$("#addTypeId").show();
				$("#customerDiv").show();
				$("#customerDiv input[name='suitableCustomerAdd'][value='1']").prop("checked", true);
			}else{
				$("#editTypeId").show();
				$("#editCustomerDiv").show();
				$("#editCustomerDiv input[name='suitableCustomerEdit'][value='1']").prop("checked",true);
			}
		}
	}
	$(document).ready(function(){
	/*	$("input[name='module-checkable']").on("change", function(){
			$('input[name="module-checkable"]:checked').each(function() {
				var check=$(this).val();
				moduleCheckArr.push(check);


			});

		})*/
	});



/* 	function suitableCustomerChange(suitableCustomer){
	debugger;
		if ('2'==suitableCustomer) {
			$("select[name='customerIdAdd']").attr("disabled","disabled");
		} else {
			$("select[name='customerIdAdd']").removeAttr("disabled");
		}
		
	} */
	

	// 下拉框改变
	function levelAddChange(level){			
		if ('2'==level) {
			$("select[name='parentProjectNameAdd']").removeAttr("disabled");
			$("#redAdd").show();
			$("#parentProjectNameAdd").removeAttr("disabled"); 
			$custom.request.ajax(serverAddr+"/settlementConfig/parentProjectNameList.asyn", {"level":level}, function(data){
				var jsonData=data.data;
				$("#parentProjectNameAdd").empty();
				var selectObj=$("#parentProjectNameAdd");
				selectObj.append("<option selected value=''>"+"请选择"+"</option>");
				jsonData.forEach(function(o,i){
	                selectObj.append("<option value='"+ o.id+"'>"+o.projectName+"</option>");
	            });								
			});
		}else{
			$("#parentProjectNameAdd").val('');
			$("select[name='parentProjectNameAdd']").attr("disabled","disabled");
			$("#redAdd").hide();
		}
			
	}
	function parentProjectNameChange(parentProjectId){	
		$custom.request.ajax(serverAddr+"/settlementConfig/toDetail.asyn", {"id" : parentProjectId }, function(data){
			if(data.state == '200'){
				var paymentSubjectId=data.data.paymentSubjectId;				
				$custom.request.ajax(serverAddr+"/settlementConfig/ncReceivePayment.asyn", {}, function(data){
					var jsonData=data.data;					
					$("#receivePaymentSubjectAdd").empty();
					var selectObj=$("#receivePaymentSubjectAdd");
					selectObj.append("<option selected value=''>"+"请选择"+"</option>");
					jsonData.forEach(function(o,i){		
				        if (paymentSubjectId== o.id) {
				        	selectObj.append("<option selected value='"+ o.id+"'>"+o.name+"</option>");
						}else {
							selectObj.append("<option value='"+ o.id+"'>"+o.name+"</option>");
						}
				    });								
				});

			}
		});
			
	}
	
	
	// 保存
	function settlementSave(){
		
		
		var inExpCode = $("#inExpCodeAdd").val();
		var level = $("#levelAdd").val();
		var parentProjectId = $("#parentProjectNameAdd").val();
		var parentProjectNameText=$("#parentProjectNameAdd").find("option:selected").text();
		var projectName = $("#projectNameAdd").val();
		var receivePaymentSubjectId = $("#receivePaymentSubjectAdd").val();
		var receivePaymentSubjectText=$("#receivePaymentSubjectAdd").find("option:selected").text();
		var suitableCustomer = $('input[name="suitableCustomerAdd"]:checked').val();
		var status = $('input[name="statusAdd"]:checked').val();
		var customerIdArr = $("#customerIdAdd").val();
		var customerIdStr=JSON.stringify(customerIdArr);

		//适应类型
		var isTypeCheck="";
		$('#addTypeId input[name="type-checkable"]:checked').each(function(){
			isTypeCheck += $(this).val() + ",";
		});
		//适应模块
		var isModuleCheck="";
		$('#addModuleId input[name="module-checkable"]:checked').each(function(){
			console.log($(this).val());
			isModuleCheck += $(this).val() + ",";
		});
		debugger

		if(isModuleCheck.indexOf("2") > -1||isModuleCheck.indexOf("4") > -1){
			if(isTypeCheck==null||isTypeCheck==""){
				$custom.alert.error("适用类型不能为空");
				return;
			}
			if(suitableCustomer==null || suitableCustomer=="" ||suitableCustomer==undefined){
				$custom.alert.error("适用客户不能为空");
				return;
			}
			if ('2'==suitableCustomer) {
				if (customerIdArr==null ||customerIdArr.length==0) {
					$custom.alert.error("请选择指定客户");
					return;
				}
			}
		}else{
			suitableCustomer="";
		}

		if(isModuleCheck==null || isModuleCheck==""){
			$custom.alert.error("适应模块不能为空");
			return;
		}

		if(!level){
			$custom.alert.error("所属层级不能为空");
			return;
		}
		if ('2'==level) {
			if(!parentProjectId){
				$custom.alert.error("上级费项不能为空");
				return;
			}
			if(!inExpCode){
				$custom.alert.error("主数据编码不能为空");
				return;
			}
		}
		if(!projectName){
			$custom.alert.error("本级费项名称不能为空");
			return;
		}
		if(!receivePaymentSubjectId){
			$custom.alert.error("NC收支费项不能为空");
			return;
		}
		if(!status){
			$custom.alert.error("状态不能为空");
			return;
		}
		var json=[];
		json.push({"inExpCode":inExpCode,"level":level,"parentProjectId":parentProjectId,
			 "parentProjectNameText":parentProjectNameText,"projectName":projectName,
			 "receivePaymentSubjectId":receivePaymentSubjectId,"receivePaymentSubjectText":receivePaymentSubjectText,
			 "suitableCustomer":suitableCustomer,"status":status,"customerIdStr":customerIdStr,"type":isTypeCheck,"module":isModuleCheck});
		var jsonStr=JSON.stringify(json);			
		$custom.request.ajax(serverAddr+"/settlementConfig/saveSettlementConfig.asyn",  {"json":jsonStr} , function(result){
			if(result.state == '200'){
				 if (result.data.code=='00') {
					 $custom.alert.success("新增成功");					
					 $('#settlement-modal').modal('hide');		
					 $module.load.pageOrder("act=14003");
					 
				}else {
					$custom.alert.error(result.data.message);	
				}
				 
 
			}else{
				$custom.alert.error(result.data.message);				
			}
		} ,null , function(){});
		$("#settlement-modal").bind('hide.bs.modal',function(){
            $(".modal-backdrop").remove();
        })
	};
	
	function settlementCancel(){
		$('#settlement-modal').modal('hide');
	}

	   function dh_list(){
		   $module.load.pageOrder("act=14003");
	    }
	
	
</script>