<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->

<div>
    <!-- Signup modal content -->
    <div id="addMonthly-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              <span class="header-title" >新增月结账单</span>
          </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="settlement-from">                   
                    <div class="edit_box mt20">
                        <div class="edit_item">
                           <label >仓库 : </label>
	                       <select name="depotIdAdd" id="depotIdAdd" class="input_msg">
                            <option value="">请选择</option>
                           </select>
                        </div>
                        <div class="edit_item">
                           <label >结转月份 : </label>
	                      <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input" 
	                      name="settlementMonthAdd" id="settlementMonthAdd">	                                                
                        </div>
                        <div class="edit_item">
                            
                        </div>
                    </div>
                   </form>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="settlementSave();">保存</button>
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="settlementCancel();">取消</button>
                      </div>
                  </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">	
	$(document).ready(function() {
		$("#depotIdAdd").val('');
		$("#settlementMonthAdd").val('');
		//加载仓库下拉
		$module.depot.loadSelectData.call($("select[name='depotIdAdd']")[0],depotIdAdd);
	});
	
	// 保存
	function settlementSave(){
		var depotIdAdd = $("#depotIdAdd").val();	// 仓库
		 if(!depotIdAdd){
             $custom.alert.error("仓库不能为空");
             return;
         }
		 var settlementMonthAdd = $("#settlementMonthAdd").val();	// 仓库
		 if(!settlementMonthAdd){
             $custom.alert.error("结转月份不能为空");
             return;
         }
		var form = $("#settlement-from").serializeArray();
		$custom.request.ajax(serverAddr+"/monthlyAccount/saveMonthlyAccount.asyn", form , function(result){		
			if(result.state == '200'){
				if (result.data.code=='00') {
					$custom.alert.success("新增成功");
					$('.modal-backdrop').remove();
					$module.load.pageInventory("bls=6005");
					$('#addMonthly-modal').modal('hide');
				} else {
					$custom.alert.error(result.data.message);
				} 				 
				 setTimeout("dh_list();" , 100) ;
 
			}else{
				$custom.alert.error("新增失败");
				$('#addMonthly-modal').modal('hide');			
			}
		} ,null , function(){});
	};
	
	function settlementCancel(){
		$('#addMonthly-modal').modal('hide');
	}

	
</script>