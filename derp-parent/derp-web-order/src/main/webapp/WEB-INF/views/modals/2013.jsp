<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
	<div class="col-12">
		<div>
			<!-- Signup modal content -->
			<div id="signup-modal" class="modal fade" tabindex="-1" role="dialog"
				aria-labelledby="custom-width-modalLabel" aria-hidden="true"
				style="display: none; position: fixed; top: 130px; bottom: auto; height: 100%;">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h5 class="modal-title" id="myLargeModalLabel">审核确认</h5>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" id="receive-from">
								<!-- 自定义参数  -->
								<div class="form-group" style="text-align: center;color: red;">确认保存并审核该采购订单</div>
								<div class="form-group">
									<div class="col-12">
										<label style="margin-left: 48px;"><i class="red">*</i>归属时间:
										</label> <input type="text" style="width: 180px;" class="input_msg"
											name="attributionDate" id="attributionDate">
									</div>
								</div>
								<div class="form-group"></div>
								<div class="form-row mt20">
									<div class="form-inline m0auto">
										<div class=form-group>
											<button type="button"
												class="btn btn-info waves-effect waves-light fr delayedBtn"
												onclick='$m2013.save()'>确定</button>
											<button type="button"
												class="btn btn-light waves-effect waves-light ml15"
												onclick='$m2013.hide()'>取消</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
	</div>
	<!-- end col -->
</div>
<script type="text/javascript">
	$(document).ready(function() {
	});
	
	$derpTimer.lessThanNowDateTimer("#attributionDate", "yyyy-MM-dd") ;
	
   var $m2013={
	   init:function(val, callback){
		   $("#attributionDate").val(val) ;
		   
		   if(callback){
			   $m2013.params.callback = callback ;
		   }
		   
           this.show();
        },
        show:function(){
	        $($m2013.params.modalId).modal("show");
        },
        hide:function(){
        	$("#attributionDate").val('') ;
            $($m2013.params.modalId).modal("hide");
        },
        params:{
            formId:'#receive-from',
            modalId:'#signup-modal',
            callback : function(){}
        },
        save: function(){
        	if($m2013.params.callback){
        		$($m2013.params.modalId).modal("hide");
 			    $m2013.params.callback() ;
 		   }
        }
   }

</script>