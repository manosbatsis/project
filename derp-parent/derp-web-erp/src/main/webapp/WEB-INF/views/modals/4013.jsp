<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">查看商品报价</h5>
                </div>
                
                <div class="modal-body">
<!--                 <h4 class="header-title m-t-0">固定报价</h4> -->
                      <div class="form-row mt20" id = "mytable">
                   			<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
			                        <tr>
			                        	<td>剩余效期</td>
		                        		<td>供货价</td>
		                        	</tr>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m4013.hide();'>关闭</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m4013={
        init:function(id){
            this.show(id);
        },
        show:function(id){
        	$("#data-tbody").empty();
        	$custom.request.ajax($m4013.params.url, {'id':id}, function(result){
        		var tableStr = '<table id="datatable-buttons" class="table table-bordered" cellspacing="0" width="100%"> <tr><td>剩余效期</td>  <td>供货价</td> </tr>';
        		var list = result.data;
        		for(var index in list){  
        			var data = list[index];
        			tableStr +="<tr><td>"+data.residualMaturity+"</td><td>"+data.supplyMinPrice+"~"+data.supplyMaxPrice+"</td></tr>";
                } 
        		tableStr += "</table>";
        		$("#mytable").html(tableStr);
        	});
            $($m4013.params.modalId).modal("show");
        },
        hide:function(){
            $($m4013.params.modalId).modal("hide");
        },
        params:{
            url:'/supplierMerchandisePrice/getPrice.asyn?r='+Math.random(),
            modalId:'#signup-modal'
        }
    }
</script>