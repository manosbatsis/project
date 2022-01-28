<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="receive-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="receive-from">
                                <!-- 自定义参数  -->                              
                        <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;"><i class="red">*</i>事业部: </label>           
	                      	  <input class="form-control" id="info2" name="info2" type="hidden" >
                              <select class="input_msg" name="buId2" id="buId2"></select>                  	
	                    </div>
	                 	</div>
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;">请确认是否对当前所选单据填入事业部信息！</label>                                	
	                      </div>
	                 	</div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='receiveSave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='receiveCancel()'>取消</button>
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
	});
	// 保存
	function receiveSave(){
		//必填项校验
		var buId2 = $("#buId2").val();	
		var buName2 =  $("#buId2").find("option:selected").text();
		if(buId2==""){
			$custom.alert.error("事业部不能为空");
			return ;
		}	
		var info2 = $("#info2").val();

		$custom.request.ajaxSync(serverAddr+"/order/updateBusinessUnitRecord.asyn", {"info":info2,"buId":buId2,"buName":buName2}, function(data){
			console.log(data);
			if(data.state == '200' && (data.data == null || data.data == "")){
				 $custom.alert.success(data.message);
				 $("#info2").val("");
				$("#buId2").val("");
				 $('#receive-modal').modal('hide');		 
			}else{
				$("#info2").val("");
				$("#buId2").val("");
				if(data.data != null || data.data != ""){
					$custom.alert.error(data.data);
				}else{
					$custom.alert.error(data.message);
				}
				$('#receive-modal').modal('hide');						
			}
		});
		//重新加载页面
		setTimeout(function () {
			$load.a(pageUrl+"40023");
		}, 1000);
	};
	function receiveCancel(){
		$('#receive-modal').modal('hide');
		$("#id").val("");
		$("#receiveDate").val("");	
	}

</script>