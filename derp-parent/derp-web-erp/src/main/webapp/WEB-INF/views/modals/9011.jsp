<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">是否批次效期强校验-变更记录</h5>
                </div>
                
                <div class="modal-body">
<!--                 <h4 class="header-title m-t-0">固定报价</h4> -->
                      <div class="form-row mt20" id = "mytable">
                   			<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
			                        <tr>
			                        	<td>仓库自编码</td>
			                            <td>仓库名称</td>
			                            <td>是否批次效期强校验</td>
			                            <td>创建/修改时间</td>
			                            <td>月结生效时间</td>
		                        	</tr>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9011.hide();'>关闭</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>



<script type="text/javascript">
var $m9011={
        init:function(id){
            this.show(id);
        },
        show:function(id){
        	$("#data-tbody").empty();
        	$custom.request.ajax($m9011.params.url, {'id':id}, function(result){
        		var tableStr = '<table id="datatable-buttons" class="table table-bordered" cellspacing="0" width="100%"> <tr><td>仓库自编码</td>  <td>仓库名称</td><td>是否批次效期强校验</td><td>创建/修改时间</td><td>月结生效时间</td> </tr>';
        		var list = result.data;
       			var data = list[index];
       			var list = result.data;
           		for(var index in list){
           			var data = list[index];		
//             			var date = new Date(data.createDate);
//                         var year = date.getFullYear();
//                         var month = date.getMonth() + 1;
//                         if(month < 10){
//                         	month = "0"+month;
//                         }
//                         var day = date.getDate();
//                         if(day < 10){
//                         	day = "0"+day;
//                         }
//                         var hours = date.getHours();
//                         if(hours < 10){
//                         	hours = "0"+hours;
//                         }
//                         var minutes = date.getMinutes();
//                         if(minutes < 10){
//                         	minutes = "0"+minutes;
//                         }
//                         var seconds = date.getSeconds();
//                         if(seconds< 10){
//                         	seconds = "0"+seconds;
//                         }
//                         var dateStr = year + '-' + month + '-' + day +' '+hours+':'+minutes+':'+seconds;
                        
                        
       			tableStr +="<tr><td>"+data.depotCode+"</td><td>"+data.depotName+"</td><td>"+data.batchValidation+"</td><td>"+data.createDate+"</td><td>"+data.effectiveTime+"</td></tr>";
            } 
       		tableStr += "</table>";
       		$("#mytable").html(tableStr);
        	});
            $($m9011.params.modalId).modal("show");
        },
        hide:function(){
            $($m9011.params.modalId).modal("hide");
        },
        params:{
            url:'/depot/getListById.asyn?r='+Math.random(),
            modalId:'#signup-modal'
        }
    }

</script>