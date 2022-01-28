<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg modal_w1000 " tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择公司</h5>
                </div>
                <div class="modal-body">
                	<div class="form-row">
                      </div>
                      <div class="form-row mt20">
                   			<table id="datatable-merchant" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                            </th>
			                            <th>公司编码</th>
			                            <th>公司名称</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        </tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m3010.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m3010.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var $m3010={
        init:function(id){
        	$m3010.params.customerId = id ;
            this.show(id);
        },
        show:function(id){
        	
        	var curentMerchantId = $("#curentMerchantId").val() ;
        	
        	$("#data-tbody").empty();
        	$custom.request.ajax($m3010.params.relUrl, {"id":id}, function(result){
        		if(result.state == 200){
        			var relList = result.data.relList ;
        			
        			
       				$custom.request.ajax($m3010.params.url, {"id":id}, function(res){
       					if(res.state == 200){
       						var list = res.data ;
       						
       						if(list){
       							
       							var html = "" ;
       							
       							$(list).each(function(i, m){
       								var name = m.name ;
       								var code = m.code ;
       								var id = m.id ;
       								
       								var checked = "" ;
       								var disabled = "" ;
       								
       								if(relList){
       									$(relList).each(function(j, rel){
       										merchantId = rel.merchantId ;
       										debugger;
       										if(id == merchantId){
       											checked = "checked" ;
       											return false ;
       										}
       										
       									}) ;
       								}
       								
       								/* if(id != curentMerchantId){
       									disabled = "disabled" ;  
       								} */
       								
       								html += '<tr><td class=" td-checkbox"><input type="checkbox" value="'+id+'" class="iCheck" '+ checked + ' ' + disabled + '></td>' +
       									'<td>'+code+'</td>' + '<td>'+name+'</td>' + '</tr>' ;
       							}) ;
       							
       							$("#datatable-merchant").append(html) ;
       						}
       					}
       				});
        			
        			
        		}
        	});
            $($m3010.params.modalId).modal("show");
        },
        hide:function(){
            $($m3010.params.modalId).modal("hide");
        },
        save:function(){
        	var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
        	
        	var rows = new Array ();
        	
        	$(checked).each(function(i,m){
        		var id = $(m).val() ;
        		
        		rows.push(id);
        	});
        	
        	rows = rows.join(",") ;
        	
        	$custom.request.ajax($m3010.params.saveRelUrl, {"merchantIds": rows, "customerId": $m3010.params.customerId}, function(res){
        		if(res.state == 200){
        			$custom.alert.success("操作成功");
        			
        			$m3010.hide() ;
        			
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        },
        params:{
            url:'/supplier/getMerchantInfo.asyn?r='+Math.random(),
            relUrl:'/supplier/getMerchantRel.asyn?r='+Math.random(),
            saveRelUrl:'/supplier/saveMerchantRel.asyn?r='+Math.random(),
           	customerId:"" ,
            modalId:'#signup-modal'
        }
    };
    
    $("input[name='keeperMerchantGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-merchant').prop("checked",$(this).prop("checked"));
            $('#datatable-merchant tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-merchant').prop("checked",false);
            $('#datatable-merchant tbody tr').removeClass('success');
        }
    })
    $('#datatable-merchant').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
    
</script>