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
	                      	  <label style="margin-left: 48px;">签收日期: </label>           
	                      	  <input class="form-control" id="id" name="id"  type="hidden" >
                              <input class="form-control" id="businessModel2" name="businessModel2"  type="hidden" >                     	
	                          <input style="width: 200px;"  class="input_msg form_datetime receive_date-input"  id="receiveDate" name="receiveDate"> <a style="color:red">(必填)</a>
	                    </div>
	                 	</div>
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;">PO单号： </label>                                	
	                          <input style="width: 200px;" class="input_msg" id="poNoReceive" name="poNoReceive"> <a style="color:red">(必填)</a>
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
		$derpTimer.lessThanNowDateTimer(".receive_date-input", "yyyy-MM-dd") ;
	});
	// po单号失焦时
	$("#poNoReceive").blur(function(){
		var id = $("#id").val();
		var businessModel2 = $("#businessModel2").val();
		var poNoReceive = $("#poNoReceive").val();
		if(businessModel2=="1" || businessModel2=="4"){
				if(poNoReceive==null || poNoReceive==""){
					$custom.alert.error("PO号不能为空");
				}else{
						var poResults	= poNoReceive.split("&");
						var result = poResults.sort();
						for(var i=0;i<result.length-1;i++){
							  if(result[i]==result[i+1]){	// PO号重复了
								  $custom.alert.error("PO号重复了");
								  return;
							  }								
						}	
					$custom.request.ajaxSync(serverAddr+"/sale/checkExistByPo.asyn", {"poNo":poNoReceive,"orderId":id}, function(data){
						 if(data.state==200){	
							 	if(data.data.length>0){
									$custom.alert.error("PO号:"+data.data+"已有存在销售订单信息");
							 	}
						} 
				 });
				}
		}
	});
	// 保存
	function receiveSave(){
		//必填项校验
		if($("#receiveDate").val()==""){
			$custom.alert.error("签收日期不能为空");
			return ;
		}	
		var poNoReceive = $("#poNoReceive").val();
		if(poNoReceive==""){
			$custom.alert.error("PO单号不能为空");
			return ;
		}	
		var id = $("#id").val();
		var businessModel2 = $("#businessModel2").val();
		var receiveDate = $("#receiveDate").val();
	
		var results	= poNoReceive.split("&");
		var result = results.sort();
		for(var i=0;i<result.length-1;i++){
			  if(result[i]==result[i+1]){	// PO号重复了
				  $custom.alert.error("签收失败，PO号重复了");
				  return;
			  }								
		}	
		// 1、购销-实销实结类型订单遵、购销-整批结算类型订单遵循校验”公司+PO“维度下弱校验仅做提示
		if(businessModel2=="1" || businessModel2=="4"){
		    if(poNoReceive){
                $custom.request.ajaxSync(serverAddr+"/sale/checkExistByPo.asyn", {"poNo":poNoReceive,"orderId":id}, function(data){
                    if(data.state==200){
                        if(data.data.length>0){
                            $custom.alert.error("PO号:"+data.data+"已有存在销售订单信息");
                        }
                    }
                });
            }
	    }
		$custom.request.ajaxSync(serverAddr+"/sale/receiveSaleOrder.asyn", {"id":id,"receiveDate":receiveDate,"manyPoNo":poNoReceive}, function(data){
			if(data.state == '200'){
				 $custom.alert.success("签收成功");
				 $("#id").val("");
				$("#receiveDate").val("");
				 $('#receive-modal').modal('hide');		 
			}else{
				$("#id").val("");
				$("#receiveDate").val("");
				$custom.alert.error(data.message);
				$('#receive-modal').modal('hide');						
			}
		});
		setTimeout("dh_list();" , 1000) ;
	};
    function dh_list(){
    	// 跳转到销售列表
        $module.load.pageOrder("act=4001");
    }
	
	function receiveCancel(){
		$('#receive-modal').modal('hide');
		$("#id").val("");
		$("#receiveDate").val("");	
	}

</script>