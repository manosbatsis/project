<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->

<div>
    <!-- Signup modal content -->
    <div id="platsave-settlement-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 400px;">
            	<div class="header" >
              		<span class="header-title" >新增费用项映射</span>
         		</div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from1">
                        <!-- 自定义参数  -->  
                                                                   
	                  <div class="form-group">
	                      <div class="col-12">
		                      <label ><i class="red">*</i>平台名称 : </label>
		                       <select class="input_msg" name="storePlatformCodeAdd" id="storePlatformCodeAdd"  style="width: 202px;">
                               </select>
	                      </div>
	                      
	                  </div> 
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label ><i class="red">*</i>平台费项名称: </label>
		                      <input type="text" class="input_msg" id="nameAdd" name="nameAdd"  style="width: 202px;">	                      
	                      </div>
	                  </div>   
	                   <div class="form-group"> 
	                      <div class="col-12">
		                       <label> <i class="red">*</i>本级费项名称:</label>
		                       <select class="input_msg" name="settlementConfigIdAdd" id="settlementConfigIdAdd" onchange="settlementConfigIdChangeAdd(this.value)" style="width: 202px;">
                               </select>
	                      </div>
	                  </div>  
	                  
	                  <div class="form-group">
	                      <div class="col-12">
		                      <label>NC收支费项: </label>
		                      <input type="text" id="ncPaymentNameAdd" ></span>
		                      <input type="hidden" name="ncPaymentIdAdd" id="ncPaymentIdAdd" >
		                      <input type="hidden" name="ncPaymentNameAddText" id="ncPaymentNameAddText" >
		                      <!-- <select class="input_msg" name="ncPaymentIdAdd" id="ncPaymentIdAdd"  style="width: 202px;">
                               </select> -->
	                      </div>
	                  </div> 
	                  <div class="form-group"> 
	                      <div class="col-12">
		                      <label ><i class="red">*</i>状态: </label>
		                       <input checked="true" type="radio"  name="statusAdd" value="1" >启用
		                      <input type="radio"  name="statusAdd" value="0">禁用	                      
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
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">	
	
function settlementConfigIdChangeAdd(id){	
	$custom.request.ajax(serverAddr+"/settlementConfig/toDetail.asyn", {"id":id}, function(data){
		var paymentSubjectName=data.data.paymentSubjectName;
		var paymentSubjectId=data.data.paymentSubjectId;
		$("#ncPaymentIdAdd").val(paymentSubjectId);
		$("#ncPaymentNameAdd").val(paymentSubjectName);
		$("#ncPaymentNameAddText").val(paymentSubjectName);
		$("#ncPaymentNameAdd").attr("readOnly",true); 
									
	});
		
}
	
	// 保存
	function settlementSave(){
		var storePlatformCode = $("#storePlatformCodeAdd").val();
		var storePlatformName=$("#storePlatformCodeAdd").find("option:selected").text();	
		var settlementConfigId = $("#settlementConfigIdAdd").val();
		var settlementConfigName=$("#settlementConfigIdAdd").find("option:selected").text();	
		var ncPaymentId = $("#ncPaymentIdAdd").val();
		var ncPaymentName = $("#ncPaymentNameAddText").val();
		//var ncPaymentName=$("#ncPaymentIdAdd").find("option:selected").text();	
		var name = $("#nameAdd").val();
		var status = $('input[name="statusAdd"]:checked').val();

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
			
		$custom.request.ajax(serverAddr+"/platformSettlementConfig/saveSettlementConfig.asyn",  {"storePlatformCode":storePlatformCode,
			"storePlatformName":storePlatformName,"settlementConfigId":settlementConfigId,
			"settlementConfigName":settlementConfigName,"ncPaymentId":ncPaymentId,
			"ncPaymentName":ncPaymentName,"name":name,
			"status":status} , function(result){
			if(result.state == '200'){
				 if (result.data.code=='00') {
					 $custom.alert.success("新增成功");			
					 $('.modal-backdrop').remove();
					 $('platsave-settlement-modal').modal('hide');	
					 $module.load.pageOrder("act=14017");
					 //$('.modal-backdrop').remove();
					 
				}else {
					$custom.alert.error(result.data.message);
				}
				 
 
			}else{
				$custom.alert.error(result.data.message);	
			}
		} ,null , function(){});
		/* $("#platsave-settlement-modal").bind('hide.bs.modal',function(){
            $(".modal-backdrop").remove();
        }) */
        /* $("#platsave-settlement-modal").bind('hide.bs.modal',function(){
            $(".modal-backdrop").remove();
        }) */
	};
	
	function settlementCancel(){
		$('#platsave-settlement-modal').modal('hide');
	}

	   function dh_list(){
		   $module.load.pageOrder("act=14017");
	    }
	
	
</script>