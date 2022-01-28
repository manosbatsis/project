<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="pay-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:45%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="pay-from">
                                <!-- 自定义参数  -->                              
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                        <label>付款日期:<a style="color:red">*</a></label>
                                        <input class="form-control" id="id" name="id"  type="hidden" >
                                        <input class="input_msg form_datetime pay_date-input" id="payDate" name="payDate"  >
                                   <a style="color:red">(必填)</a>
                                    </div>
                                </div>
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                         <label>支&nbsp;&nbsp; 付&nbsp;&nbsp; 人:</label>                                	
                                      	 <input class="input_msg" id="payName" name="payName">
                                        
                                    </div>
                                </div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='paySave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='payCancel()'>取消</button>
                          		</div>
                          	</div>
                      </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">

	$(document).ready(function() {
		$(".pay_date-input").datetimepicker({
	        language:  'zh-CN',
	        format: "yyyy-mm-dd",
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2
	    });
	});

	// 保存
	function paySave(){
		//必填项校验
		if($("#payDate").val()==""){
			$custom.alert.error("付款日期不能为空");
			return ;
		}	
		var flag = 0;
		var id = $("#id").val();
		var payDate = $("#payDate").val();
		var payName = $("#payName").val();
		var tag="2";// 1来源录入时间 2 来源支付时间
		//校验合同号是否存在，存在给予提示
		$custom.request.ajaxSync($module.params.serverAddrByOrder+"/purchase/updatePurchaseOrderInvoice.asyn", {"id":id,"payDate":payDate,"payName":payName,"tag":tag}, function(data){
			
			if(data.state == '200'){
				 $custom.alert.success("修改成功");
				 $("#id").val("");
				$("#payDate").val("");
				$("#payName").val("");
				 $('#pay-modal').modal('hide');		 
				 setTimeout("getPendingRecordOrders();" , 1000) ;
			}else{
				$("#id").val("");
				$("#payDate").val("");
				$("#payName").val("");
				$custom.alert.error(data.message);
				$('#pay-modal').modal('hide');						
			}
		});
	};
	
	function payCancel(){
		$('#pay-modal').modal('hide');
		$("#id").val("");
		$("#payDate").val("");	
		$("#payName").val("");
	}

</script>