<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
        <div>
            <!-- Signup modal content -->
            <div id="add-signup-modal" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:120px;">
                <div class="modal-dialog">
                    <div class="modal-content">
                    <div class="modal-header">
	                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	                     <h5 class="modal-title" id="myLargeModalLabel">修改品类</h5>
                    </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="addForm">
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label class="edit_label red">二级类目:</label>
                                        <select name="productTypeId" id = "productTypeId"class="edit_input ">
                                       		  <option value="">请选择</option>
		                                      <c:forEach items="${productTypeBean }" var="productType">
		                               	    		<option value="${productType.value }">${productType.label }</option>
		                               	    </c:forEach>
                                      </select>
                                    </div>
                                </div>
                                <div class="form-group account-btn text-center m-t-10">
                                    <div class="col-12">
                                        <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m8013.edit();">编辑</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m8013={
        init:function(){
        	 var ids=$mytable.fn.getCheckedRow();
             if(ids==null||ids==''||ids==undefined){
                 $custom.alert.warningText("至少选择一条记录！");
                 return;
             }
            this.show();
            //重置表单
            $($m8013.params.formId)[0].reset();
        },
        edit:function(){
        	//必填项校验
    		if($("#productTypeId").val()==""){
    			$custom.alert.error("二级类目不能为空");
    			return ;
    		}
        	var ids=$mytable.fn.getCheckedRow();
        	       	
        	 var productTypeId =$("#productTypeId").val();
            $custom.request.ajax(
                    $m8013.params.url,
                    {"ids":ids,"productTypeId":productTypeId},
                    function(data){
                        if(data.state==200){
                        	$custom.alert.success("修改成功");
                            //成功隐藏
                            $m8013.hide();
                            //重新加载table表格
                            $mytable.fn.reload();
                        }else{
                        	if(data.expMessage != null){
								$custom.alert.error(data.expMessage);
							}else{
								$custom.alert.error(data.message);
							}
                        }
            });
        },
        show:function(){
             $($m8013.params.modalId).modal("show");
        },
        hide:function(){
             $($m8013.params.modalId).modal("hide");
        },
        params:{
            url:'/product/modifyBatchProduct.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
        }
    }


</script>