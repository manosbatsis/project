<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->

<div>
    <!-- Signup modal content -->
    <div id="verification-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              <span class="header-title" >回款备注</span>
          </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="invoice-from">
                        <!-- 自定义参数  -->  
                  <div class="form-group">
                  	<label >添加备注 : </label>
                      <div class="col-12">	                      
	                      <textarea rows="5"  cols="80" id="notes" name="notes" > </textarea>
	                      <input type="hidden" class="input_msg" id="receiveCode" name="receiveCode" >                      
                      </div>
                  </div>
                 	<div class="form-group">
                       	<div class="row col-12 table-responsive" id="itemTable" >
							<table class="table table-striped dataTable no-footer"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th>备注人</th>
										<th>备注时间</th>
										<th>备注信息</th>
									</tr>
								</thead>
								<tbody id="itemTbody">
								</tbody>
							</table>
						</div>
                       </div>
                       </form>

                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="itemSettlementSave();">保存</button>
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="itemSettlementCancel();">取消</button>
                      </div>
                  </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">	
	$(document).ready(function() {
	});
	
	// 保存
	function itemSettlementSave(){

		var receiveCode = $("#receiveCode").val();
		var notes = $("#notes").val();
		if(!notes){
        	$custom.alert.error("请输入备注");;
			return false;
        } 
		$custom.request.ajaxReqrCheck(serverAddr+"/receiveBillVerification/saveNotes.asyn", {"receiveCode":receiveCode,"notes":notes} , function(data){			
			if(data.state == '200'){
				 $custom.alert.success("修改成功");				 
				 $('#verification-modal').modal('hide');
				 
			}else{
				$('#verification-modal').modal('hide');	
			}
		} ,null , function(){});
	};
	/* 取消 */
	function itemSettlementCancel(){
		$('#verification-modal').modal('hide');
	};
	



	
</script>