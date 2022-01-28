<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="in-warehouse-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="receive-from">
                                <!-- 自定义参数  -->                              
                        <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;"><i class="red">*</i>入库时间: </label>           
	                      	  <input type="text" class="input_msg form_datetime date-input" name="inWarehouseDate" id="inWarehouseDate">
	                      	  <input type="hidden" id="inWarehouseIds"/>
	                    </div>
	                 	</div>
	                  <div class="form-group">
	                      <div class="col-12">
	                      	  <label style="margin-left: 48px;">对选中的单据进行中转仓入库确认！</label>                                	
	                      </div>
	                 	</div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='stockOutSave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='stockOutCancel()'>取消</button>
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
   
   $derpTimer.lessThanNowDateTimer("#inWarehouseDate", "yyyy-MM-dd HH:mm:ss") ;
   
	// 中转仓入库
	function stockOutSave(){
		//必填项校验
		var inWarehouseDate = $("#inWarehouseDate").val();	
		var ids = $("#inWarehouseIds").val();	
		if(inWarehouseDate==""){
			$custom.alert.error("请选择入库时间");
			return ;
		}	
		  $custom.request.ajax(serverAddr+"/purchase/inWarehouse.asyn",{"ids":ids,"inWarehouseDateStr":inWarehouseDate},function(data){
              if(data.state==200){
                  if(data.data=="成功"){
                      $custom.alert.success(data.data);
                      //成功，重新加载页面
              		setTimeout(function () {
              			$load.a(pageUrl+"3001");
              		}, 1000);
                  }else{
                      $custom.alert.error(data.data);
                  }
              }else{
                  $custom.alert.error(data.expMessage);
              }
          });
			$('#in-warehouse-modal').modal('hide');
			$("#id").val("");
			$("#inWarehouseDate").val("");	
	};
	function stockOutCancel(){
		$('#in-warehouse-modal').modal('hide');
		$("#id").val("");
		$("#inWarehouseDate").val("");	
	}

</script>