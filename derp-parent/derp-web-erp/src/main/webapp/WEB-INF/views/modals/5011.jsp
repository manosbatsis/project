<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg modal_w1000 " tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择公司</h5>
                </div>
                <div class="modal-body">
                	<form id="addForm">
                	<div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">公司编码<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                          <div class="col-4">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">公司名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" >
                                  </div>
                              </div>
                          </div>
                          <div class="col-4">
                          	<div class="form-inline">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" >查询</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                          </div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span>全选</span>
			                                </label>
			                            </th>
			                            <th>公司编码</th>
			                            <th>公司名称</th>
			                            <th>卓志编码</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        	<tr>
			                        		<td></td>
			                        		<td></td>
			                        		<td></td>
			                        		<td></td>
			                        	</tr>
			                        </tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" >确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m5011.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var $m5011={
        init:function(){
            this.show();
            //重置表单
            $($m5011.params.formId)[0].reset();
        },
        show:function(){
        	$("#data-tbody").empty();
        	$custom.request.ajax($m5011.params.url, {}, function(result){
        		
        	});
            $($m5011.params.modalId).modal("show");
        },
        hide:function(){
            $($m5011.params.modalId).modal("hide");
        },
        params:{
            url:'/merchant/getSelectBean.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#signup-modal'
        }
    };

    //渲染 下拉框
    function selectLoad(data,id){
    	$("#"+id).empty();
        var ops="<option value=''>请选择</option>";
        $.each(data,function(index,event){
        	if(event.value!=null){
            	ops+="<option value='"+event.value+"'>"+event.label+"</option>";
            }
        });
        $("#"+id).append(ops);
    }
    
</script>