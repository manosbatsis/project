<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="pay-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 528px;text-align: center;">
	                	<div class="header" >
			              <span class="header-title" >录入付款时间</span>
			          </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="pay-from">
                                <!-- 自定义参数  -->                              
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                        <label>付款日期:<a style="color:red">*</a></label>
                                        <input class="form-control" id="pay_id"   type="hidden" >
                                        <input class="form-control" id="code_pay"   type="hidden" >
                                        <input class="input_msg form_datetime pay_date-input" id="payDate" name="payDate" style="width: 250px;">
                                    </div>
                                </div>
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                         <label>支&nbsp;&nbsp; 付&nbsp;&nbsp; 人:</label>                                	
                                      	 <input class="input_msg" id="payName" name="payName" style="width: 250px;">
                                        
                                    </div>
                                </div>
                                </form>
                                <div class="form-group">
			                    	<div class="info_text">
			                           <label ><i class="red">*</i>&nbsp上传附件： </label>
			                           <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="btn-file-pay" >上传</button>
				                        <form enctype="multipart/form-data" id="form-upload-pay">
			                         		<input type="file" name="file" id="file-pay" class="btn-hidden file-import">
			                          	</form>
			                       </div>
			                        <div class="info_text">	
			                        	<a href="#" id="fileName-pay" onclick="void(0) ;"></a>
			                       </div>
			                    </div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='paySave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='payCancel()'>取消</button>
                          		</div>
                          	</div>
                      </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">

	$(document).ready(function() {
		$derpTimer.lessThanNowDateTimer(".pay_date-input", "yyyy-MM-dd") ;
	});

	// 保存
	function paySave(){
		var flag = 0;
		var id = $("#pay_id").val();
		var payDate = $("#payDate").val();
		var payName = $("#payName").val();
		var tag="2";// 1来源录入时间 2 来源支付时间
		
		//必填项校验
		if($("#payDate").val()==""){
			$custom.alert.error("付款日期不能为空");
			return ;
		}
		
		var fileVal = $("#file-pay").val() ; 
		if(!fileVal){
			$custom.alert.error("请上传附件");
			return ;
		}
		
		$custom.request.ajaxSync(serverAddr+"/purchase/updatePurchaseOrderInvoice.asyn", {"id":id,"payDate":payDate,"payName":payName,"tag":tag}, function(data){
			
			if(data.state == '200'){
				 $custom.alert.success("修改成功");
				 payCancel() ;	 
				 setTimeout("dh_list();" , 1000) ;
			}else{
				payCancel() ;	
				$custom.alert.error(data.message);
			}
		});
	};
	
	function payCancel(){
		$('#pay-modal').modal('hide');
		$("#pay_id").val("");
		$("#code_pay").val("");
		$("#payDate").val("");	
		$("#payName").val("");
	}
	
	//点击上传文件
	$("#btn-file-pay").click(function(){
		var input = '<input type="file" name="file" id="file-pay" class="btn-hidden file-import">';
		$("#form-upload-pay").empty();
		$("#form-upload-pay").append(input);
		$("#file-pay").click();
	});
	
	//上传文件到后端
	$("#form-upload-pay").on("change",'.file-import',function(){
		var code = $("#code_pay").val() ;
		
		var formData = new FormData($("#form-upload-pay")[0]); 
		$custom.request.ajaxUpload(serverAddr+"/attachment/uploadFiles.asyn?code=" + code, formData, function(result){
			if(result.state == 200 && result.data && result.data.code == 200 ){
				$("#fileName-pay").html(result.data.attachmentInfo.attachmentName) ;
				$("#fileName-pay").attr("href", result.data.attachmentInfo.attachmentUrl) ;
				
				$custom.alert.success("上传成功");
			}else{
				$custom.alert.error("上传失败");
			}
		});
	});

</script>