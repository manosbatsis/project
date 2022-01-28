<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->

<div>
    <!-- Signup modal content -->
    <div id="itemSettlement-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
        <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              		<span class="header-title" >编辑项目ID</span>
         		</div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from">
                        <!-- 自定义参数  -->  
                                                                   
	                  <div class="form-group">
	                      <div class="col-12">
		                      <label ><i class="red">*</i>所属层级 : </label>
		                      <input type="text" readonly="readonly" class="input_msg" id="levelLabelEdit" name="levelLabelEdit" style="width: 202px;">
		                      <input type="hidden" class="input_msg" id="idEdit" name="idEdit" style="width: 202px;">
		                      <input type="hidden" class="input_msg" id="levelEdit" name="levelEdit" style="width: 202px;">
		                      <input type="hidden" class="input_msg" id="oldProjectNameEdit" name="oldProjectNameEdit" style="width: 202px;">
		                      
		                                                   
		                       <label ><i class="red" id="redEdit">*</i>上级费项 : </label>
		                       
		                       <select class="input_msg" name="parentProjectNameEdit" id="parentProjectNameEdit"  style="width: 202px;">
		                       <option value="">请选择</option>
                               </select>
	                      </div>
	                  </div> 
	                  <div class="form-group"> 
	                      <div class="col-12">
	                      	  <label ><i class="red">*</i>本级费项名称: </label>
		                      <input type="text" class="input_msg" id="projectNameEdit" name="projectNameEdit" style="width: 202px;">
		                      
	                      	  <label ><i class="red">*</i>NC收支费项: </label>
	                      	   <select class="input_msg" name="receivePaymentSubjectEdit" id="receivePaymentSubjectEdit"  style="width: 202px;">
		                       <option value="">请选择</option>
                               </select>

		                      
	                      </div>
	                  </div>   
	                  <div class="form-group"> 
	                      <div class="col-12">
		                      <label >主数据编码: </label>
		                      <input type="text" class="input_msg" id="inExpCodeEdit" name="inExpCodeEdit" style="width: 202px;">
							  <div id="editModuleId" name="typeAddDiv" style="display: inline-block;">
								  <label> <i class="red">*</i>适用模块:</label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable" style="display: inline-block;"  value="1"  onclick="test('2')"/>
									  <span>应付</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable"  style="display: inline-block;"  value="2"  onclick="test('2')"/>
									  <span>应收</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable" style="display: inline-block;"  value="3"  onclick="test('2')"/>
									  <span>预付</span>
								  </label>
								  <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
									  <input type="checkbox" name="module-checkable"style="display: inline-block;"  value="4"  onclick="test('2')"/>
									  <span>预收</span>
								  </label>
							  </div>

		                       <%--<select class="input_msg" name="typeEdit" id="typeEdit"  style="width: 202px;">
                               </select>--%>
	                      </div>
	                  </div>
					<div class="form-group">
						<div class="col-12" id="editTypeId" style="display: inline-block">
							 <label> <i class="red">*</i>适用类型:</label>
							<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
								<input type="checkbox" name="type-checkable" style="display: inline-block;" value="1" />
								<span>To B</span>
							</label>
							<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
								<input type="checkbox" name="type-checkable" style="display: inline-block;"  value="2"/>
								<span>To C</span>
							</label>
							<%--<select class="input_msg" name="moduleType" id="moduleType" >
							</select>--%>
						</div>
					</div>
	                  <div class="form-group" id="editCustomerDiv">
	                      <div class="col-12">
		                      <label ><i class="red">*</i>适用客户 : </label>
		                      <input type="radio" id="suitableCustomerEdit1" name="suitableCustomerEdit" value="1" onclick="" >通用
		                      <input type="radio"   id="suitableCustomerEdit2" name="suitableCustomerEdit" value="2" onclick="">指定客户
		                      <select class="input_msg"  name="customerIdEdit" id="customerIdEdit" multiple="multiple" title="请选择" >
                              </select>
	                      </div>
	                   </div> 
	                  <div class="form-group"> 
	                      <div class="col-12">
		                      <label ><i class="red">*</i>状态: </label>
		                      <input type="radio" id="statusEdit1"  name="statusEdit" value="1" >启用
		                      <input type="radio" id="statusEdit0" name="statusEdit" value="0">禁用		                      
	                      </div>
	                  </div>               	

                       </form>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="itemSettlementSave();">保存</button>
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="itemSettlementCancel();">取消</button>
                      </div>
                  </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
	$(document).ready(function(){

	});

	function addRow(TabId){
		 var table = document.getElementById(TabId);
		   //在最后一行插入一行 
		  // var newRow = table.insertRow(table.rows.length);
		   var html = "<tr><td><input type='hidden'/> </td> <td><input type='text'/>  </td><td><input type='text' /> </td></tr>";
		   $("#itemTbody").append(html);
	};

	// 保存
	function itemSettlementSave(){
		var inExpCode = $("#inExpCodeEdit").val();
		var level = $("#levelEdit").val();
		var parentProjectId = $("#parentProjectNameEdit").val();
		var parentProjectNameText=$("#parentProjectNameEdit").find("option:selected").text();
		var projectName = $("#projectNameEdit").val();
		var receivePaymentSubjectId = $("#receivePaymentSubjectEdit").val();
		var receivePaymentSubjectText=$("#receivePaymentSubjectEdit").find("option:selected").text();
		var suitableCustomer = $('input[name="suitableCustomerEdit"]:checked').val();				
		var status = $('input[name="statusEdit"]:checked').val();
		var customerIdArr = $("#customerIdEdit").val();
		var customerIdStr=JSON.stringify(customerIdArr);
		var oldProjectName = $("#oldProjectNameEdit").val();
		//var type = $("#typeEdit").val();

		//适应类型
		var isTypeCheck="";
		$('#editTypeId input[name="type-checkable"]:checked').each(function(){
			isTypeCheck += $(this).val() + ",";
		});
		//适应模块
		var isModuleCheck="";
debugger
		$('#editModuleId input[name="module-checkable"]:checked').each(function(){
			console.log($(this).val());
			isModuleCheck += $(this).val() + ",";
		});
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
		
		var customerIdStr=JSON.stringify(customerIdArr);
		
		var id = $("#idEdit").val();		
		/* if(!level){
			$custom.alert.error("所属层级不能为空");
			return;
		} */
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
		json.push({"id":id,"inExpCode":inExpCode,"oldProjectName":oldProjectName,"level":level,"parentProjectId":parentProjectId,
			 "parentProjectNameText":parentProjectNameText,"projectName":projectName,
			 "receivePaymentSubjectId":receivePaymentSubjectId,"receivePaymentSubjectText":receivePaymentSubjectText,
			 "suitableCustomer":suitableCustomer,"status":status,"customerIdStr":customerIdStr,"type":isTypeCheck,"module":isModuleCheck});
		var jsonStr=JSON.stringify(json);			
		$custom.request.ajax(serverAddr+"/settlementConfig/modifySettlementConfig.asyn",  {"json":jsonStr} , function(result){
			if(result.state == '200'){
				if(result.data.code == '00'){				
					 $custom.alert.success("修改成功");				 			
					 $('#itemSettlement-modal').modal('hide');	
					 $('.modal-backdrop').remove();
					 $module.load.pageOrder("act=14003");
	 
				}else{
					$custom.alert.error(result.data.message);				
				}
			}else {
				$custom.alert.error(result.data.message);	
			}
			
			
		} ,null , function(){});
		/* $("itemSettlement-modal").bind('hide.bs.modal',function(){
            $(".modal-backdrop").remove();
        }) */
		 $module.load.pageOrder("act=14003");
		
	};
	/* 取消 */
	function itemSettlementCancel(){
		$('#itemSettlement-modal').modal('hide');
	};
	



	
</script>