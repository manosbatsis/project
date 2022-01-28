<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-edit-modal" class="modal fade bs-example-modal" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">编辑枚举类型</h5>
                </div>
                <div class="modal-body">
                	<form id='edit-form'>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-4 col-form-label"><div class="fr"><i class="red">*</i>枚举名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
	                                  <input type="text" class="form-control" name="enumName" id="enumName">
	                                  <input type="hidden" name="id" id="id">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">备注<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="remark" id="remark">
                                  </div>
                              </div>
                          </div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr"  onclick='$m6012.edit();'>保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                  	<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m6012.hide();'>关闭</button>
                                  </div>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var $m6012={
        init:function(id){
            this.show(id);
            //重置表单
            $($m6012.params.formId)[0].reset();
        },
        show:function(id){
        	$custom.request.ajax($m6012.params.detailsUrl,{"id":id},function(data){
        		$("#enumName").val(data.data.enumName);
        		$("#remark").val(data.data.remark);
        		$("#id").val(data.data.id);
        	});
            $($m6012.params.modalId).modal("show");
        },
        hide:function(){
            $($m6012.params.modalId).modal("hide");
        },
        edit:function(){
        	$custom.request.ajax($m6012.params.url,$($m6012.params.formId).serializeArray(),function(data){
            	if(data.state==200){
            		if(data.data == '编辑成功'){
	                	$custom.alert.success(data.data);
	                    //成功隐藏
	                    $m6012.hide();
	                    //重新加载table表格
	                    $mytable.fn.reload();
            		}else{
            			$custom.alert.error(data.data);
            		}
            	}else{
                    $custom.alert.error(data.message);
                }
            });
        },
        params:{
            url:'/enum/modifyEnum.asyn?r='+Math.random(),
            detailsUrl:'/enum/getEnumDetails.asyn?r='+Math.random(),
            formId:'#edit-form',
            modalId:'#signup-edit-modal'
        }
    }
</script>