<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="invoice-modal" class="modal fade bs-example-modal-lg" >
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">操作日志</h5>
                </div>
                <div class="modal-body">
                      <div class="form-row mt20">
                   			<table id="datatable-buttons2" class="table table-bordered" cellspacing="0" width="70%">
			                        <thead>
			                      <tr>
			                            <th>操作人</th>
			                            <th>操作时间</th>
			                            <th>操作内容</th>
			                            <th>备注</th>
			                        </tr>
			                        </thead> 
			                        <thead id="logListDiv">
			                         
			                        </thead>
	
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" id="cancel-buttons" onclick="returnBtn()">关闭</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
		
		//日志详情
		function logDetails(relCode,module){	
		  var url = serverAddr+"/reportCommon/getOperateLogList.asyn" ;
		  $("#logListDiv").empty();
		  $("#invoice-modal").modal("show");
		  //获取日志数据
		  $custom.request.ajax(url, {"relCode" : relCode, "module":module}, function(result){
					if(result.state == '200'){

						var logList = result.data ;				
						var html = "" ;
						$(logList).each(function( index , log){
							html += '<tr>' + 
								'<td>'+log.operater+'</td>'+
								'<td>'+log.operateDate+'</td>' +
								'<td>'+log.operateAction+'</td>' + 
								'<td>'+log.operateRemark+'</td>' + 
								'</tr>' ;
								
						}) ;
						
						$("#logListDiv").append(html) ;
						
					}else{
						$custom.alert.error(result.message);
					}
				});
				  	
		}

		function returnBtn(){
				$("#invoice-modal").modal("hide");
		}
		  
</script>