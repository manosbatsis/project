<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->

<div>
    <!-- Signup modal content -->
    <div id="platedit-settlement-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
        <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              		<span class="header-title" >编辑项目ID</span>
         		</div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from2">
                        <!-- 自定义参数  -->  
                                                                   
	                  <div class="form-group">
	                      <div class="col-12">		                      
		                      <input type="hidden" class="input_msg" id="idEdit" name="idEdit" style="width: 202px;">
		                      <input type="hidden" class="input_msg" id="oldNameEdit" name="oldNameEdit" style="width: 202px;">
		                      <label ><i class="red">*</i>平台名称 : </label>
		                       <select class="input_msg" name="storePlatformCodeEdit" id="storePlatformCodeEdit"  style="width: 202px;">
                               </select>
	                      </div>
	                  </div> 
	                  <div class="form-group"> 
	                      <div class="col-12">
	                      	   <label ><i class="red">*</i>平台费项名称: </label>
		                      <input type="text" class="input_msg" id="nameEdit" name="nameEdit"  style="width: 202px;">	                      		                      
	                      </div>
	                  </div>   
	                  <div class="form-group"> 
	                      <div class="col-12">
		                       <label> <i class="red">*</i>本级费项名称:</label>
		                       <select class="input_msg" name="settlementConfigIdEdit" id="settlementConfigIdEdit" onchange="settlementConfigIdChangeEdit(this.value)" style="width: 202px;">
                               </select>
	                      </div>
	                  </div>   
	                  
	                  <div class="form-group">
	                      <div class="col-12">
		                      <label>NC收支费项: </label>
		                      <input type="text"  id="ncPaymentNameEdit" readonly="readonly" >
		                      <input type="hidden" name="ncPaymentIdEdit" id="ncPaymentIdEdit" >
		                      <input type="hidden" name="ncPaymentIdEditText" id="ncPaymentIdEditText" >
		                      
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

function settlementConfigIdChangeEdit(id){	
	$custom.request.ajax(serverAddr+"/settlementConfig/toDetail.asyn", {"id":id}, function(data){
		var paymentSubjectName=data.data.paymentSubjectName;
		var paymentSubjectId=data.data.paymentSubjectId;
		$("#ncPaymentIdEdit").val(paymentSubjectId);
		$("#ncPaymentNameEdit").val(paymentSubjectName);
		$("#ncPaymentIdEditText").val(paymentSubjectName);
		//$("#ncPaymentNameEdit").attr("disabled",false);
		$("#ncPaymentNameEdit").attr("readOnly",true); 
		$("#ncPaymentNameAdd").attr("readOnly",true); 
		
									
	});
		
}
	// 保存
	function itemSettlementSave(){
		var storePlatformCode = $("#storePlatformCodeEdit").val();
		var storePlatformName=$("#storePlatformCodeEdit").find("option:selected").text();	
		var settlementConfigId = $("#settlementConfigIdEdit").val();
		var settlementConfigName=$("#settlementConfigIdEdit").find("option:selected").text();	
		var ncPaymentId = $("#ncPaymentIdEdit").val();
		var ncPaymentName = $("#ncPaymentIdEditText").val();
		//var ncPaymentName=$("#ncPaymentIdEdit").find("option:selected").text();	
		var name = $("#nameEdit").val();
		var status = $('input[name="statusEdit"]:checked').val();
		var id = $("#idEdit").val();
		var oldName = $("#oldNameEdit").val();
		
		if(!id){
			$custom.alert.error("id不能为空");
			return;
		}
		if(!storePlatformCode){
			$custom.alert.error("平台名称不能为空");
			return;
		}		
		if(!name){
			$custom.alert.error("平台费项名称不能为空");
			return;
		}
		if(!settlementConfigId){
			$custom.alert.error("本级费项名称不能为空");
			return;
		}
		if(!status){
			$custom.alert.error("状态不能为空");
			return;
		}
		
		
		
		
					
		$custom.request.ajax(serverAddr+"/platformSettlementConfig/modifySettlementConfig.asyn",  {"storePlatformCode":storePlatformCode,
			"storePlatformName":storePlatformName,"settlementConfigId":settlementConfigId,
			"settlementConfigName":settlementConfigName,"ncPaymentId":ncPaymentId,
			"ncPaymentName":ncPaymentName,"name":name,"status":status,"id":id,"oldName":oldName} , function(result){
			if(result.state == '200'){
				if(result.data.code == '00'){				
					 $custom.alert.success("修改成功");				 			
					 $('#platedit-settlement-modal').modal('hide');						
					 $module.load.pageOrder("act=14017");
	 
				}else{
					$custom.alert.error(result.data.message);				
				}
			}else {
				$custom.alert.error(result.data.message);	
			}
			
			
		} ,null , function(){});
		 $("#platedit-settlement-modal").bind('hide.bs.modal',function(){
	            $(".modal-backdrop").remove();
	        })
		
	};
	/* 取消 */
	function itemSettlementCancel(){
		$('#platedit-settlement-modal').modal('hide');
	};
	



	
</script>