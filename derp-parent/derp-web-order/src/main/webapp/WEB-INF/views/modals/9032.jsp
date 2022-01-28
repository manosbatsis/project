<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="shelf-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="shelf-from">
                                <!-- 自定义参数  -->    
                                <input class="form-control" id="id" name="id"  type="hidden" >
                                <input class="form-control" id="differenceNum" name="differenceNum"  type="hidden" value="0">                          
                                <div class="form-group row m-b-20">                                                                                           
                                    <div class="col-12">
                                        <label>上架日期:<a style="color:red">*</a></label>
                                        <input class="input_msg form_datetime shelf_date-input" readonly="true"   title="请输入"  id="shelfDate" name="shelfDate"  >
                                        <a style="color:red">(必填)</a>
                                    </div>
                                </div>
                                <div class="form-group row m-b-20">                                                                                           
                                    <div class="col-12">
                                        <label>备注:</label>
                                        <textarea class="form-control" rows="5" cols="20" name="remark" id="remark"></textarea>
                                    	<a style="color:red">(如有差异（5个以上），请说明差异原因。)</a>
                                    </div>
                                </div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='shelfSave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='shelfCancel()'>取消</button>
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
		$(".shelf_date-input").datetimepicker({
	        language:  'zh-CN',
	        format: "yyyy-mm-dd",
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2
	    });
	});
	
	// 保存
	function shelfSave(){
		if($("#differenceNum").val() >= 5){
			if($("#remark").val()==""){
				$custom.alert.error("差异数大于等于5，备注内容必填");
				return ;
			}	
		}
		//必填项校验
		if($("#shelfDate").val()==""){
			$custom.alert.error("上架日期不能为空");
			return ;
		}	
		var id = $("#id").val();
		var shelfDate = $("#shelfDate").val();
		var remark = $("#remark").val();
		//校验合同号是否存在，存在给予提示
		$custom.request.ajaxSync(serverAddr+"/saleOut/shelfSaleOutDepot.asyn", {"id":id,"shelfDate":shelfDate,"remark":remark}, function(data){
			if(data.state == '200'){
				 $custom.alert.success("上架成功");
				 $("#id").val("");
				 $("#shelfDate").val("");
				 $("#remark").val("");
				 $('#shelf-modal').modal('hide');		 
			}else{
				$("#id").val("");
				$("#shelfDate").val("");
				$("#remark").val("");
				$custom.alert.error(data.message);
				$('#shelf-modal').modal('hide');						
			}
		});
		searchData();
	};
	function shelfCancel(){
		$('#shelf-modal').modal('hide');
		$("#id").val("");
		$("#shelfDate").val("");	
		$("#remark").val("");
	}

</script>