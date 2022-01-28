<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
	<div class="col-12">
		<div>
			<!-- Signup modal content -->
			<div id="signup-trade-modal" class="modal fade" tabindex="-1" role="dialog"
				aria-labelledby="custom-width-modalLabel" aria-hidden="true"
				style="display: none; position: fixed; top: 130px; bottom: auto; height: 100%;">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h5 class="modal-title" id="myTradeModalLabel">选择采购链路</h5>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" id="trade-from">
								<!-- 自定义参数  -->
								<div class="form-row mt20">
		                   			<table id="popup-datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
				                        <thead>
				                        <tr>
				                            <th>选择</th>
				                            <th>链路名称</th>
				                        </tr>
				                        </thead>
				                        <tbody id="tradeLinkItemBody">
				                        </tbody>
				                    </table>
		                      </div>
								<div class="form-group"></div>
								<div class="form-row mt20">
									<div class="form-inline m0auto">
										<div class=form-group>
											<button type="button"
												class="btn btn-info waves-effect waves-light fr delayedBtn"
												onclick='$m2019.save()'>确定</button>
											<button type="button"
												class="btn btn-light waves-effect waves-light ml15"
												onclick='$m2019.hide()'>取消</button>
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
	
   var $m2019={
	   init:function(list, callback){
		   
		   $m2019.params.callback = callback ;
		  
		   var html = "<tr><td><input type='radio' name='tradeLinkId' value='' checked/></td><td>不生成交易链路单据</td></tr>" ;
		   $(list).each(function(index, item){
			   html += "<tr><td><input type='radio' name='tradeLinkId' value='"+ item.id +"' /></td><td>"+ item.linkName +"</td></tr>" ;
		   }) ;
		   
		   $("#tradeLinkItemBody").html(html) ;
		   
           this.show();
        },
        show:function(){
	        $($m2019.params.modalId).modal("show");
        },
        hide:function(){
            $($m2019.params.modalId).modal("hide");
        },
        params:{
            formId:'#trade-from',
            modalId:'#signup-trade-modal',
            callback : null,
            tradeLinkId: ''
        },
        save: function(){
        	$m2019.params.tradeLinkId = $("input[name='tradeLinkId']:checked").val();
        	
        	if($m2019.params.callback){
        		$($m2019.params.modalId).modal("hide");
 			    $m2019.params.callback() ;
 		   }
        }
   }

</script>