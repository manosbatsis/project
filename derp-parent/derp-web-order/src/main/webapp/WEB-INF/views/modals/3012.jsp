<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    .select2-container{border: 1px solid #dadada;}
</style>
<div>
    <!-- Signup modal content -->
    <div id="lbxno-signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
        <div class="modal-dialog  modal-lg" style="width: 420px;">
            <div class="modal-content">
            	 <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">编辑</h5>
                </div> 
                <div class="modal-body">
                	<div class="form-row">
                          <div class="col-6">
                               
                              <div class="row m0auto" style="margin-top:20px;margin-left:30px;width: 400px">
                                 
                                  <span style="font-size: 16px;">输入lbxNo:</span>
                                  <div class="col-7">
                                       <input type="text" class="input_msg" id="newLbxNo" style="width: 200px;">
                                       <input type="hidden" class="form-control" id="transOrderId">
                                  </div>
                              </div>
                          </div>
                      </div>
                 
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='updateLbxNo();'>保存</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='hideModel();'>取消</button>
                          		</div>
                          	</div>
                     </div>
                </div> 
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
function hideModel(){
	$("#lbxno-signup-modal").modal("hide");
}
function updateLbxNo(){
	var newLbxNo = $("#newLbxNo").val();
	var transOrderId = $("#transOrderId").val();
	hideModel();
  	 var url=serverAddr+"/transfer/updateLbxNo.asyn";
     $custom.request.ajax(url,{"transOrderId":transOrderId,"newLbxNo":newLbxNo},function(data){//start-0011
         if(data.state==200){
             if(data.data.code=='00'){
            	 $custom.alert.success('保存成功');
             }else{
            	 $custom.alert.error(data.data.message);
             }
             //保存成功，重新加载页面
             $mytable.fn.reload();
         }else{
             $custom.alert.error(data.message);
         }
     });//end-0011
}
</script>