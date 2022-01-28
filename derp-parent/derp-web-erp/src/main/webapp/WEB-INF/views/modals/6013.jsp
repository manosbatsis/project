<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">新增枚举值</h5>
                </div>
                <div class="modal-body">
                	<form id="add-form">
                      <div class="form-row mt20" id = "mytable">
                      		<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>枚举实际值<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  		<input type="text" class="form-control" name="key">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i>枚举显示值<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  		<input type="text" class="form-control" name="value">
                                  </div>
                              </div>
                          </div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr"  onclick="$m6013.add();">保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                  	<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m6013.hide();'>关闭</button>
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

    var $m6013={
        init:function(){
            this.show();
            //重置表单
            $($m6013.params.formId)[0].reset();
        },
        show:function(){
            $($m6013.params.modalId).modal("show");
        },
        hide:function(){
            $($m6013.params.modalId).modal("hide");
        },
        add:function(){
        	var form = $($m6013.params.formId).serializeArray();
        	var id = $("#id").val();
        	var data_id = {name:"enumId",value:id};
        	form.push(data_id);
        	$custom.request.ajax($m6013.params.url,form,function(data){
            	if(data.state==200){
            		if(data.data == '新增成功'){
	                	$custom.alert.success(data.data);
	                    //成功隐藏
	                    $m6011.hide();
	                    setTimeout(function () {
	                    	$load.a("/enum/toDetailsPage.html?id="+id);
						}, 1000);
            		}else{
            			$custom.alert.error(data.data);
            		}
            	}else{
                    $custom.alert.error(data.message);
                }
            });
        },
        params:{
        	url:'/enum/saveEnumItem.asyn?r='+Math.random(),
            formId:'#add-form',
            modalId:'#signup-modal'
        }
    }
</script>